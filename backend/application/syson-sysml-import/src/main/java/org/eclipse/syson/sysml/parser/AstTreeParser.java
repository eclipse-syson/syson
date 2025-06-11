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
import com.google.common.collect.Streams;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.Element;
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
                    values = Streams.stream(valueNode.elements()).toList();
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

    public void resolveAllReference(List<ProxiedReference> unresolvedReferences0) {
        // Sorts proxies to resolve by breadth-first search strategy. This way we resolve first the import
        // generally located at top level part of the model. Those proxies are often used by lowest part of the
        // model to resolve their link
        List<ProxiedReference> unresolvedReferencesTmp = unresolvedReferences0.stream()
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
            LOGGER.info(MessageFormat.format("{0} remaining proxy to resolve after {1} try", Integer.toString(proxiedReferences.size()), Integer.toString(tryNb)));
            if (proxiedReferences.equals(unresolvedReferencesTmp)) {
                this.printResolutionError(proxiedReferences);
                break;
            } else {
                unresolvedReferencesTmp = proxiedReferences;
            }
        }
    }

    private void printResolutionError(List<ProxiedReference> proxiedReferences) {

        for (ProxiedReference pr : proxiedReferences) {
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
}
