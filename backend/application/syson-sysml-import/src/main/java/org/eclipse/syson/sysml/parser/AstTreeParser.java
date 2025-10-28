/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.sysml.parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.parser.translation.EClassifierTranslator;
import org.eclipse.syson.sysml.utils.LogNameProvider;
import org.eclipse.syson.sysml.utils.MessageReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AstTreeParser.
 *
 * @author gescande.
 */
public class AstTreeParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstTreeParser.class);

    private final ContainmentReferenceHandler containmentReferenceHandler;

    private final NonContainmentReferenceHandler nonContainmentReferenceHandler;

    private final EClassifierTranslator typeBuilder = new EClassifierTranslator();

    private final ProxyResolver proxyResolver;

    private final EAttributeHandler attributeHandler;

    private final LogNameProvider logNameProvider = new LogNameProvider();

    private final MessageReporter messageReporter;

    public AstTreeParser(final ContainmentReferenceHandler astContainmentReferenceParser, final NonContainmentReferenceHandler astWeakReferenceParser, final ProxyResolver proxyResolver,
            final EAttributeHandler astObjectParser, MessageReporter messageReporter) {
        this.containmentReferenceHandler = astContainmentReferenceParser;
        this.nonContainmentReferenceHandler = astWeakReferenceParser;
        this.proxyResolver = proxyResolver;
        this.attributeHandler = astObjectParser;
        this.messageReporter = messageReporter;
    }

    public List<EObject> parseAst(final JsonNode astJson) {
        if (!astJson.isEmpty()) {
            return this.parseJsonNode(astJson);
        }
        return List.of();
    }

    private List<EObject> parseJsonNode(final JsonNode astJson) {
        List<EObject> result;
        if (astJson == null) {
            result = List.of();
        } else if (astJson.isArray()) {
            result = this.parseJsonArray(astJson);
        } else {
            result = this.parseJsonObject(astJson);
        }
        return result;
    }

    private List<EObject> parseJsonArray(final JsonNode astJson) {
        final List<EObject> result = new ArrayList<>();
        astJson.forEach(jsonNode -> {
            result.addAll(this.parseJsonNode(jsonNode));
        });
        return result;
    }

    private List<EObject> parseJsonObject(final JsonNode astJson) {
        final List<EObject> result = new ArrayList<>();
        final EObject eObject = this.typeBuilder.createObject(astJson);
        if (eObject != null) {
            this.fillElementWith(astJson, eObject);
            result.add(eObject);
        } else {
            this.messageReporter.error("Error building the object " + astJson);
        }
        return result;
    }

    /**
     * Parse the given node and fill information into the given {@link EObject}.
     *
     * @param astJson
     *            the current node
     * @param eObject
     *            the {@link EObject} to fill
     */
    private void fillElementWith(final JsonNode astJson, final EObject eObject) {
        Iterator<Entry<String, JsonNode>> fiedIterator = astJson.fields();
        while (fiedIterator.hasNext()) {
            Entry<String, JsonNode> node = fiedIterator.next();
            String key = node.getKey();
            // Ignore meta information
            if (!key.startsWith("$")) {

                JsonNode valueNode = node.getValue();
                final List<JsonNode> values;
                if (valueNode.isArray()) {
                    Iterable<JsonNode> iterable = () -> valueNode.elements();
                    values = StreamSupport.stream(iterable.spliterator(), false).toList();
                } else {
                    values = List.of(valueNode);
                }
                for (JsonNode value : values) {
                    this.handleNodeValue(eObject, key, value);
                }

            } else {
                LOGGER.trace("Ignoring field " + key);
            }
        }
    }

    private void handleNodeValue(final EObject eObject, String key, JsonNode value) {
        if (this.attributeHandler.isAttribute(eObject, key, value)) {
            this.attributeHandler.setAttribute(eObject, key, value);
        } else {
            if (this.nonContainmentReferenceHandler.isNonContainmentReference((Element) eObject, key, value)) {
                this.nonContainmentReferenceHandler.createProxy((Element) eObject, key, value);
            } else if (this.containmentReferenceHandler.isContainmentReference(eObject, key, value)) {
                EObject child = this.containmentReferenceHandler.handleContainmentReference(eObject, key, value);
                if (child != null) {
                    this.fillElementWith(value, child);
                } else {
                    this.messageReporter.error(MessageFormat.format("The following element has not be handled in the ast key {0} value {1}", key, value));
                }
            } else {
                this.messageReporter.error(MessageFormat.format("The transformer does not know how to handle key {0} with value {1} in {2}", key, value, this.logNameProvider.getName(eObject)));
            }
        }
    }

    public void resolveAllReference(List<ProxiedReference> unresolvedReferences) {
        /*
         * We need to order some of the proxies to resolve. For example, redefinition resolution should be handled once
         * all specializations have been handled. Indeed, the name resolution of a redefined feature on Redefinition can
         * depend on either the Subclassification or Subsetting of its owning Namespace (that might be implied through
         * the use of semantic MetadataUsage) or the type of the owning namespace</li> </ul></p>
         */
        Map<ReferenceType, List<ProxiedReference>> partitionedUnresolvedReferences = unresolvedReferences.stream().collect(Collectors.groupingBy(this::getProxyClassification));

        // First resolve imports
        List<ProxiedReference> unresolvedProxiesAfterImport = this.doResolveAllReference(partitionedUnresolvedReferences, ReferenceType.IMPORT,
                "Import resolution phase", List.of());

        // Then resolve unclassified proxies and the previous unresolved proxies
        List<ProxiedReference> unresolvedProxiesAfterUnqualified = this.doResolveAllReference(partitionedUnresolvedReferences, ReferenceType.UNCLASSIFIED,
                "Non qualified proxy resolution phase", unresolvedProxiesAfterImport);

        // Then resolve specializations that are not redefinitions since redefinition needs to access inherited memberships
        List<ProxiedReference> unresolvedAfterSpecialization = this.doResolveAllReference(partitionedUnresolvedReferences, ReferenceType.OTHER_SPECIALIZATION,
                "Non redefinition specialization proxy resolution phase",
                unresolvedProxiesAfterUnqualified);

        // Then resolve base_type redefinition proxies stored MetadataDefinition since they may impact implicit inherited members
        List<ProxiedReference> unresolvedAfterBaseTypeRedefinition = this.doResolveAllReference(partitionedUnresolvedReferences, ReferenceType.BASE_TYPE_METADATA_DEFINITION_REDEFINITION,
                "Semantic MetadataData base_type redefinition proxy resolution phase", unresolvedAfterSpecialization);

        // At the end resolve redefinition
        List<ProxiedReference> unresolvedAfterRedefinition = this.doResolveAllReference(partitionedUnresolvedReferences, ReferenceType.OTHER_REDEFINITION, "Other redefinition proxy resolution phase",
                unresolvedAfterBaseTypeRedefinition);

        if (!unresolvedAfterRedefinition.isEmpty()) {
            this.handleUnresolvableProxies(unresolvedAfterRedefinition);
        }
    }

    private ReferenceType getProxyClassification(ProxiedReference proxyReference) {
        EObject owner = proxyReference.owner();
        InternalEObject targetProxy = proxyReference.targetProxy();
        final ReferenceType result;
        if (owner instanceof Redefinition && targetProxy.toString().contains("baseType")) {
            result = ReferenceType.BASE_TYPE_METADATA_DEFINITION_REDEFINITION;
        } else if (owner instanceof Import) {
            result = ReferenceType.IMPORT;
        } else if (owner instanceof Redefinition) {
            result = ReferenceType.OTHER_REDEFINITION;
        } else if (owner instanceof Specialization) {
            result = ReferenceType.OTHER_SPECIALIZATION;
        } else {
            result = ReferenceType.UNCLASSIFIED;
        }
        return result;
    }

    /**
     * Runs a resolution phase of a given class of proxy
     *
     * @param classifiedProxies
     *         all the proxy classified
     * @param proxyClass
     *         the type of proxy to resolve
     * @param resolutionPhaseName
     *         a name of resolution phase
     * @param yetNotResolved
     *         A list of unresolved proxy from the previous phases
     * @return all proxies that have not been resolved
     */
    private List<ProxiedReference> doResolveAllReference(Map<ReferenceType, List<ProxiedReference>> classifiedProxies, ReferenceType proxyClass, String resolutionPhaseName,
            List<ProxiedReference> yetNotResolved) {
        List<ProxiedReference> toResolve = classifiedProxies.get(proxyClass);
        if (toResolve != null && !toResolve.isEmpty()) {
            List<ProxiedReference> tmpToResolve = new ArrayList<>(toResolve);
            tmpToResolve.addAll(yetNotResolved);
            return this.doResolveAllReference(tmpToResolve, resolutionPhaseName);
        }
        return yetNotResolved;
    }

    private List<ProxiedReference> doResolveAllReference(List<ProxiedReference> unresolvedReferences, String resolutionPhase) {
        // Sorts proxies to resolve by breadth-first search strategy. This way we resolve first the import
        // generally located at top level part of the model. Those proxies are often used by lowest part of the
        // model to resolve their link
        List<ProxiedReference> unresolvedReferencesTmp = unresolvedReferences.stream()
                .sorted(new PoxiedReferenceComparator())
                .toList();

        // There can be at first unsolvable references or aliases because they depend on another yet unresolved element.
        // Iterating over every unresolved reference until every single one is solved or no new resolution is possible.
        int tryNb = 0;
        while (!unresolvedReferencesTmp.isEmpty()) {
            tryNb++;
            List<ProxiedReference> proxiedReferences = unresolvedReferencesTmp.stream()
                    .filter(proxiedReference -> !this.proxyResolver.resolveProxy(proxiedReference))
                    .toList();
            LOGGER.info(MessageFormat.format("{2} : {0} remaining proxies to resolve after {1} try", Integer.toString(proxiedReferences.size()), Integer.toString(tryNb), resolutionPhase));
            if (proxiedReferences.equals(unresolvedReferencesTmp)) {
                break;
            } else {
                unresolvedReferencesTmp = proxiedReferences;
            }
        }

        return unresolvedReferencesTmp;
    }

    private void handleUnresolvableProxies(List<ProxiedReference> unresolvableProxies) {
        for (ProxiedReference pr : unresolvableProxies) {
            String msg = MessageFormat.format("Unable to resolve name ''{1}'' for reference ''{2}'' on element ''{0}''", this.logNameProvider.getName(pr.owner()),
                    pr.targetProxy().eProxyURI().fragment(),
                    pr.reference().getName());
            this.messageReporter.warning(msg);
            // Unset this proxy
            EReference ref = pr.reference();
            if (ref.isMany()) {
                ((List<Object>) pr.owner().eGet(ref)).remove(pr.targetProxy());
            } else {
                pr.owner().eSet(ref, null);
            }
        }
    }

    /**
     * Comparator that sorts the {@link ProxiedReference} by their {@link ProxiedReference#owner()} using a
     * breadth-first search strategy.
     *
     * @author Arthur Daussy
     */
    private static final class PoxiedReferenceComparator implements Comparator<ProxiedReference> {

        @Override
        public int compare(ProxiedReference o1, ProxiedReference o2) {
            return this.compare(o1.owner(), o2.owner());
        }

        public int compare(EObject owner1, EObject owner2) {
            int o1Ancestors = EMFUtils.getAncestors(Element.class, owner1, t -> true).size();
            int o2Ancestors = EMFUtils.getAncestors(Element.class, owner2, t -> true).size();
            int signum = o1Ancestors - o2Ancestors;
            // If same level check if same on container
            if (signum == 0) {
                EObject o1Container = owner1.eContainer();
                EObject o2Container = owner2.eContainer();
                if (o1Container != null && o1Container == o2Container) {
                    signum = o1Container.eContents().indexOf(owner1) - o2Container.eContents().indexOf(owner2);
                } else if (o1Container != null && o2Container != null) {
                    signum = this.compare(o1Container, o2Container);
                } else if (o1Container == null && o2Container != null) {
                    signum = -1;
                } else if (o1Container != null && o2Container == null) {
                    signum = 1;
                }
            }

            return signum;
        }
    }

    /**
     * Classification of proxy to resolve.
     */
    private enum ReferenceType {
        /**
         * A proxy related to imports.
         */
        IMPORT,
        /**
         * All proxies that do not belong to any other categories.
         */
        UNCLASSIFIED,
        /**
         * Proxy of the "base_type" redefinition feature of the "SemanticMetadata" used in MetadataDefinition.
         */
        BASE_TYPE_METADATA_DEFINITION_REDEFINITION,
        /**
         * All other redefinitions except the one stored in BASE_TYPE_METADATA_DEFINITION_REDEFINITION.
         */
        OTHER_REDEFINITION,
        /**
         * All specializations except the Redefinition.
         */
        OTHER_SPECIALIZATION
    }
}
