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
package org.eclipse.syson.sysml;

import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.parser.AstTreeParser;
import org.eclipse.syson.sysml.parser.ContainmentReferenceHandler;
import org.eclipse.syson.sysml.parser.EAttributeHandler;
import org.eclipse.syson.sysml.parser.NonContainmentReferenceHandler;
import org.eclipse.syson.sysml.parser.ProxiedReference;
import org.eclipse.syson.sysml.parser.ProxyResolver;
import org.eclipse.syson.sysml.utils.MessageReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms AST data using defined mappings and updates resources accordingly.
 *
 * @author gescande.
 */
public class ASTTransformer {

    private final Logger logger = LoggerFactory.getLogger(ASTTransformer.class);

    private final AstTreeParser astTreeParser;

    private final NonContainmentReferenceHandler nonContainmentReferenceHandler;

    private final MessageReporter messageReporter = new MessageReporter();

    public ASTTransformer() {
        var proxyResolver = new ProxyResolver(this.messageReporter);
        var astObjectParser = new EAttributeHandler(this.messageReporter);
        var astContainmentReferenceParser = new ContainmentReferenceHandler(this.messageReporter);
        this.nonContainmentReferenceHandler = new NonContainmentReferenceHandler(this.messageReporter);
        this.astTreeParser = new AstTreeParser(astContainmentReferenceParser, this.nonContainmentReferenceHandler, proxyResolver, astObjectParser, this.messageReporter);
    }

    public Resource convertResource(final InputStream input, final ResourceSet resourceSet) {
        Resource result = null;
        if (input != null) {
            final JsonNode astJson = this.readAst(input);
            if (astJson != null) {
                this.logger.info("Create the Root eObject containment structure");
                final List<EObject> rootSysmlObjects = this.astTreeParser.parseAst(astJson);
                result = new JSONResourceFactory().createResourceFromPath(null);
                resourceSet.getResources().add(result);
                result.getContents().addAll(rootSysmlObjects);

                this.preResolvingFixingPhase(rootSysmlObjects);

                this.logger.info("File Parsed");
                List<ProxiedReference> proxiedReferences = this.nonContainmentReferenceHandler.getProxiesToResolve();
                this.logger.info("{} references to resolve.", proxiedReferences.size());
                this.astTreeParser.resolveAllReference(proxiedReferences);
                this.logger.info("End of references resolving");

            }
        }
        return result;
    }

    private void preResolvingFixingPhase(List<EObject> rootSysmlObjects) {
        for (EObject root : rootSysmlObjects) {
            this.fixSuccessionUsageImplicitSource(root);
        }
    }

    /**
     * Try to fix all {@link SuccessionAsUsage} element contained in the given root to workaround
     * https://github.com/sensmetry/sysml-2ls/issues/13.
     *
     * @param root
     *            a root element.
     */
    private void fixSuccessionUsageImplicitSource(EObject root) {
        EMFUtils.allContainedObjectOfType(root, SuccessionAsUsage.class)
                .filter(this::hasImplicitSourceFeature)
                .forEach(suc -> {
                    Feature invalidFeature = suc.getConnectorEnd().get(0);
                    FeatureMembership owningFeatureMembershit = invalidFeature.getOwningFeatureMembership();
                    ReferenceUsage refUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
                    EList<Element> ownedRelatedElements = owningFeatureMembershit.getOwnedRelatedElement();

                    Membership previousMembershipFeature = this.computePreviousFeatureMembership(suc);
                    if (previousMembershipFeature != null) {
                        // For implicit source that targets an element of the standard library, we need to keep a
                        // "virtual link" to the previous feature membership to identify the source of SuccessionAsUsage
                        // see implementation :
                        // org.eclipse.syson.diagram.common.view.services.ViewCreateService.createEndFeatureMembershipFor(Element)
                        refUsage.getAliasIds().add(previousMembershipFeature.getElementId());
                    }

                    int index = ownedRelatedElements.indexOf(invalidFeature);
                    ownedRelatedElements.remove(index);
                    ownedRelatedElements.add(index, refUsage);
                });
    }

    private Membership computePreviousFeatureMembership(SuccessionAsUsage successionAsUsage) {
        Type owningType = successionAsUsage.getOwningType();
        if (owningType != null) {
            EList<Membership> ownedMemberships = owningType.getOwnedMembership();
            int index = ownedMemberships.indexOf(successionAsUsage.getOwningMembership());
            if (index > 0) {
                ListIterator<Membership> iterator = ownedMemberships.subList(0, index).listIterator(index);
                while (iterator.hasPrevious()) {
                    Membership previous = iterator.previous();
                    if (previous.getMemberElement() instanceof Feature) {
                        return previous;
                    }
                }
            }
        }
        return null;
    }

    private boolean hasImplicitSourceFeature(SuccessionAsUsage successionUsage) {
        EList<Feature> ends = successionUsage.getConnectorEnd();
        if (!ends.isEmpty()) {
            Feature sourceEnd = ends.get(0);
            return sourceEnd.eClass() == SysmlPackage.eINSTANCE.getFeature() && sourceEnd.getOwnedRelationship().isEmpty();
        }
        return false;
    }

    public List<Message> getTransformationMessages() {
        return this.messageReporter.getReportedMessages();
    }

    private JsonNode readAst(final InputStream input) {
        var objectMapper = new ObjectMapper();
        try {
            // Read JSON file and map to JSON Object
            return objectMapper.readTree(input);
        } catch (final IOException e) {
            this.logger.error(e.getMessage());
            this.messageReporter.error(e.getMessage());
            return null;
        }
    }

    // Check after resolution of https://github.com/eclipse-syson/syson/issues/860 and
    // https://github.com/eclipse-sirius/sirius-web/issues/4163 if this method is still required
    public void logTransformationMessages() {
        List<Message> messages = this.getTransformationMessages();
        if (!messages.isEmpty()) {
            String logMessage = messages.stream().map(this::toLogMessage).collect(joining("\n", "Folling messages have been reported during conversion : \n", ""));
            boolean isError = messages.stream().anyMatch(m -> m.level() == MessageLevel.WARNING || m.level() == MessageLevel.ERROR);
            if (isError) {
                this.logger.error(logMessage);
            } else {
                this.logger.info(logMessage);
            }
        }
    }

    private String toLogMessage(Message msg) {
        String prefix = switch (msg.level()) {
            case INFO -> "[Info] ";
            case WARNING -> "[Warning] ";
            case ERROR -> "[Error] ";
            case SUCCESS -> "[Success] ";
            default -> "[??] ";
        };
        return prefix + msg.body();
    }
}
