/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.DeleteService;
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

    private final MessageReporter messageReporter;

    private final AstTreeParser astTreeParser;

    private final NonContainmentReferenceHandler nonContainmentReferenceHandler;

    public ASTTransformer() {
        this.messageReporter = new MessageReporter();
        this.nonContainmentReferenceHandler = new NonContainmentReferenceHandler(this.messageReporter);
        var astContainmentReferenceParser = new ContainmentReferenceHandler(this.messageReporter);
        var proxyResolver = new ProxyResolver(this.messageReporter);
        var astObjectParser = new EAttributeHandler(this.messageReporter);
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
                this.logger.info("File Parsed");
                resourceSet.getResources().add(result);
                result.getContents().addAll(rootSysmlObjects);

                this.fixAndResolve(rootSysmlObjects);
            }
        }
        return result;
    }

    /**
     * Convert the given SysML text into Elements and add them into the given parent.
     *
     * @param input
     *            the textual representation
     * @param resourceSet
     *            the current {@link ResourceSet}
     * @param parentElement
     *            the parent element in which created element will be added.
     * @return the list of the created elements
     */
    public List<Element> convertToElements(final InputStream input, final ResourceSet resourceSet, Element parentElement) {
        List<Element> result = List.of();
        if (input != null) {
            final JsonNode astJson = this.readAst(input);
            if (astJson != null) {
                this.logger.info("Create the Root eObject containment structure");
                result = this.extractContent(this.astTreeParser.parseAst(astJson));
                this.logger.info("File Parsed");
                for (Element root : result) {
                    this.addInParent(parentElement, root);
                }
                this.logger.info("Elements added in parent");

                this.fixAndResolve(result);
            }
        }
        return result;
    }

    private void fixAndResolve(List<? extends EObject> result) {
        this.preResolvingFixingPhase(result);

        List<ProxiedReference> proxiedReferences = this.nonContainmentReferenceHandler.getProxiesToResolve();
        this.logger.info("{} references to resolve.", proxiedReferences.size());
        this.astTreeParser.resolveAllReference(proxiedReferences);
        this.logger.info("End of references resolving");

        this.postResolvingFixingPhase(result);
    }

    private List<Element> extractContent(List<EObject> roots) {
        return roots.stream().filter(Namespace.class::isInstance)
                .map(Namespace.class::cast)
                .flatMap(ns -> ns.getOwnedRelationship().stream())
                .flatMap(r -> this.getChildren(r).stream())
                .toList();
    }

    private List<Element> getChildren(Relationship relationship) {
        List<Element> children = new ArrayList<>();
        if (relationship instanceof OwningMembership) {
            children.addAll(relationship.getOwnedRelatedElement());
        } else {
            children.add(relationship);
        }
        return children;
    }

    private void addInParent(Element parent, Element child) {
        if (child instanceof Import imp) {
            parent.getOwnedRelationship().add(imp);
        } else if (child instanceof Feature && parent instanceof Type) {
            Membership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
            parent.getOwnedRelationship().add(membership);
            membership.getOwnedRelatedElement().add(child);
        } else if (parent instanceof Package || SysmlPackage.eINSTANCE.getNamespace().equals(parent.eClass())) {
            Membership membership = SysmlFactory.eINSTANCE.createOwningMembership();
            membership.getOwnedRelatedElement().add(child);
            parent.getOwnedRelationship().add(membership);
        } else if (child instanceof Relationship rel) {
            parent.getOwnedRelationship().add(rel);
        }
    }

    private void postResolvingFixingPhase(List<? extends EObject> rootSysmlObjects) {
        for (EObject root : rootSysmlObjects) {
            this.fixTransitionUsageImplicitSource(root);
            this.fixOperatorExpressionUsedAsRanges(root);
            this.fixReferenceSubsettingWithNestedFeature(root);
            this.fixEndFeatureMembership(root);
        }
    }

    /**
     * The current implementation of the parser does not force the memberFeature of EndFeatureMembership to have "isEnd = true" like stated in the SysML specification see "8.3.3.3.3
     * EndFeatureMembership".
     *
     * @param root
     *         the root of the imported object
     */
    private void fixEndFeatureMembership(EObject root) {
        EMFUtils.allContainedObjectOfType(root, EndFeatureMembership.class)
                .filter(endFeatureMembership -> endFeatureMembership.getOwnedMemberFeature() != null)
                .forEach(endFeatureMembership -> endFeatureMembership.getOwnedMemberFeature().setIsEnd(true));
    }

    /**
     * SysIDE AST does not provide a special feature to set the referenced feature of the Feature holding the feature
     * chaining element. This code sets this reference.
     *
     * @param root
     *            the root of the imported object
     */
    private void fixReferenceSubsettingWithNestedFeature(EObject root) {
        EMFUtils.allContainedObjectOfType(root, ReferenceSubsetting.class)
                .filter(ref -> ref.getReferencedFeature() == null
                        && ref.getOwnedRelatedElement().stream().anyMatch(e -> e.eClass() == SysmlPackage.eINSTANCE.getFeature() && !((Feature) e).getChainingFeature().isEmpty()))
                .forEach(this::fixFeatureChainReferenceUsage);
    }

    private void fixOperatorExpressionUsedAsRanges(EObject root) {
        // Only get the OperatorExpressions used in MultiplicityRange. Based on KerML 8.2.5.8.1 OperatorExpression
        // referring to the ".." function can exist, but the specification does not allow them in MultiplicityRange (see
        // SysML 8.2.2.6.6 and KerML 8.2.5.11).
        List<OperatorExpression> operatorExpressions = EMFUtils.allContainedObjectOfType(root, MultiplicityRange.class)
                .flatMap(multiplicityRange -> multiplicityRange.getOwnedMember().stream())
                .filter(OperatorExpression.class::isInstance)
                .map(OperatorExpression.class::cast)
                .filter(operatorExpression -> Objects.equals(operatorExpression.getOperator(), ".."))
                .toList();
        for (OperatorExpression operatorExpression : operatorExpressions) {
            Element owner = operatorExpression.getOwner();
            for (Feature parameter : operatorExpression.getParameter()) {
                Expression parameterValue = this.getValuation(parameter).getValue();
                // Only LiteralExpressions and FeatureReferenceExpressions can be used in a MultiplicityRange
                if (parameterValue instanceof LiteralExpression || parameterValue instanceof FeatureReferenceExpression) {
                    OwningMembership newOwningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                    owner.getOwnedRelationship().add(newOwningMembership);
                    newOwningMembership.getOwnedRelatedElement().add(parameterValue);
                }
            }
            if (operatorExpression.getOwningMembership() != null) {
                new DeleteService().deleteFromModel(operatorExpression.getOwningMembership());
            } else {
                new DeleteService().deleteFromModel(operatorExpression);
            }
        }
        this.logger.info("Post resolving fixing phase done");
    }

    private FeatureValue getValuation(Feature feature) {
        return feature.getOwnedMembership().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst()
                .orElse(null);
    }

    private void fixTransitionUsageImplicitSource(EObject root) {
        List<TransitionUsage> transitionUsages = EMFUtils.allContainedObjectOfType(root, TransitionUsage.class).toList();
        this.fixImplicitTransitionSourceFeature(transitionUsages);
    }

    private void preResolvingFixingPhase(List<? extends EObject> rootSysmlObjects) {
        for (EObject root : rootSysmlObjects) {
            this.fixSuccessionUsageImplicitSource(root);
            this.fixConjugatedPorts(root);
            this.fixParameterFeatureDirection(root);
            this.fixReturnParameterFeatureDirection(root);
            this.fixReferenceSubsettingWithNestedFeature(root);
        }
        this.logger.info("Pre resolving fixing phase done");
    }

    private void fixReturnParameterFeatureDirection(EObject root) {
        // The default direction of feature owned by a ReturnParameterMembership is out
        // The AST from SysIDE does not give that "default" direction
        List<ReturnParameterMembership> parameterMembership = EMFUtils.allContainedObjectOfType(root, ReturnParameterMembership.class)
                .toList();
        this.fixDefaultDirection(parameterMembership, FeatureDirectionKind.OUT);
    }

    private void fixParameterFeatureDirection(EObject root) {
        // The default direction of feature owned by a ParameterMembership is in (if no a ReturnParameterMembership)
        // The AST from SysIDE does not give that "default" direction
        List<ParameterMembership> parameterMembership = EMFUtils.allContainedObjectOfType(root, ParameterMembership.class)
                .filter(m -> !(m instanceof ReturnParameterMembership))
                .toList();
        this.fixDefaultDirection(parameterMembership, FeatureDirectionKind.IN);
    }

    private void fixDefaultDirection(List<? extends ParameterMembership> parameterMemberships, FeatureDirectionKind direction) {
        // The default value of the parameter feature is in
        for (var paramMembership : parameterMemberships) {
            Feature feature = paramMembership.getOwnedMemberFeature();
            if (feature != null && feature.getDirection() == null) {
                feature.setDirection(direction);
            }
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
        List<SuccessionAsUsage> successionAsUsage = EMFUtils.allContainedObjectOfType(root, SuccessionAsUsage.class).toList();
        this.fixImplicitSourceFeature(successionAsUsage);
        this.fixImplicitTarget(successionAsUsage);
    }

    /**
     * Add all missing conjugate ports on PortDefinitions.
     *
     * @param root
     *            a root element
     */
    private void fixConjugatedPorts(EObject root) {
        List<PortDefinition> portDefinitions = EMFUtils.allContainedObjectOfType(root, PortDefinition.class).toList();

        for (PortDefinition portDef : portDefinitions) {
            if (portDef.getConjugatedPortDefinition() == null) {
                OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                portDef.getOwnedRelationship().add(owningMembership);
                // No need to set the declaredName for the ConjugatedPortDefinition here, it is always the same than its
                // originalPortDefinition and computed elsewhere
                ConjugatedPortDefinition conjugatedPortDefinition = SysmlFactory.eINSTANCE.createConjugatedPortDefinition();
                owningMembership.getOwnedRelatedElement().add(conjugatedPortDefinition);
                PortConjugation portConjugation = SysmlFactory.eINSTANCE.createPortConjugation();
                conjugatedPortDefinition.getOwnedRelationship().add(portConjugation);
                portConjugation.setConjugatedType(conjugatedPortDefinition);
                portConjugation.setOriginalPortDefinition(portDef);
            }
        }
    }

    /**
     * Try to fix all {@link SuccessionAsUsage} elements contained in the given root to workaround
     * https://github.com/eclipse-syson/syson/issues/1042 and https://github.com/sensmetry/sysml-2ls/issues/13.
     *
     * @param root
     *            a root element.
     */
    private void fixImplicitTarget(List<SuccessionAsUsage> successionAsUsage) {
        successionAsUsage.stream().filter(this::hasImplicitTargetFeature)
                .forEach(suc -> {
                    Feature invalidFeature = suc.getConnectorEnd().get(1);
                    FeatureMembership owningFeatureMembership = invalidFeature.getOwningFeatureMembership();
                    ReferenceUsage refUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
                    EList<Element> ownedRelatedElements = owningFeatureMembership.getOwnedRelatedElement();

                    int index = ownedRelatedElements.indexOf(invalidFeature);
                    ownedRelatedElements.remove(index);
                    ownedRelatedElements.add(index, refUsage);
                });
    }

    private void fixImplicitSourceFeature(List<SuccessionAsUsage> successionAsUsage) {
        successionAsUsage.stream().filter(this::hasImplicitSourceFeature)
                .forEach(suc -> {
                    Feature invalidFeature = suc.getConnectorEnd().get(0);
                    FeatureMembership owningFeatureMembership = invalidFeature.getOwningFeatureMembership();
                    ReferenceUsage refUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
                    EList<Element> ownedRelatedElements = owningFeatureMembership.getOwnedRelatedElement();

                    int index = ownedRelatedElements.indexOf(invalidFeature);
                    ownedRelatedElements.remove(index);
                    ownedRelatedElements.add(index, refUsage);
                });
    }

    private void fixImplicitTransitionSourceFeature(List<TransitionUsage> transitionUsages) {
        transitionUsages.stream().filter(this::hasImplicitSourceFeature)
                .forEach(transition -> {
                    // The specification define the computation of the sourceFeature of TransitionUsage by:
                    // Return the Feature to be used as the source of the succession of this TransitionUsage, which is
                    // the first ownedMember of the TransitionUsage that is a Feature not owned via a FeatureMembership
                    // whose featureTarget is an ActionUsage.
                    // The current implementation of SysIDE providing the AST does create such element. This fix aims to
                    // provide a workaround.
                    Membership previousFeature = this.computePreviousFeatureMembership(transition, m -> this.isValidPreviousFeature(m));
                    if (previousFeature != null && previousFeature.getMemberElement() instanceof ActionUsage actionUsage) {
                        Membership membership = SysmlFactory.eINSTANCE.createMembership();
                        membership.setMemberElement(actionUsage);
                        transition.getOwnedRelationship().add(0, membership);
                    }
                });
    }

    private boolean isValidPreviousFeature(Membership m) {
        Element memberElement = m.getMemberElement();
        return memberElement instanceof ActionUsage && !(memberElement instanceof TransitionUsage);
    }

    private Membership computePreviousFeatureMembership(Feature testedFeature, java.util.function.Predicate<Membership> filter) {
        Type owningType = testedFeature.getOwningType();
        if (owningType != null) {
            EList<Membership> ownedMemberships = owningType.getOwnedMembership();
            int index = ownedMemberships.indexOf(testedFeature.getOwningMembership());
            if (index > 0) {
                ListIterator<Membership> iterator = ownedMemberships.subList(0, index).listIterator(index);
                while (iterator.hasPrevious()) {
                    Membership previous = iterator.previous();
                    if (previous.getMemberElement() instanceof Feature && (filter == null || filter.test(previous))) {
                        return previous;
                    }
                }
            }
        }
        return null;
    }

    private boolean hasImplicitSourceFeature(SuccessionAsUsage successionAsUsage) {
        EList<Feature> ends = successionAsUsage.getConnectorEnd();
        if (!ends.isEmpty()) {
            Feature sourceEnd = ends.get(0);
            return sourceEnd.eClass() == SysmlPackage.eINSTANCE.getFeature() && sourceEnd.getOwnedRelationship().isEmpty();
        }
        return false;
    }

    private boolean hasImplicitSourceFeature(TransitionUsage transitionUsage) {
        ActionUsage source = transitionUsage.getSource();
        return source == null;
    }

    private boolean hasImplicitTargetFeature(SuccessionAsUsage successionAsUsage) {
        EList<Feature> ends = successionAsUsage.getConnectorEnd();
        if (ends.size() > 1) {
            Feature sourceEnd = ends.get(1);
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
            String logMessage = messages.stream().map(this::toLogMessage).collect(joining("\n", "Following messages have been reported during conversion: \n", ""));
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

    private void fixFeatureChainReferenceUsage(ReferenceSubsetting referenceSubsetting) {
        referenceSubsetting.getOwnedRelatedElement().stream()
                .filter(e -> e.eClass() == SysmlPackage.eINSTANCE.getFeature() && !((Feature) e).getChainingFeature().isEmpty())
                .map(Feature.class::cast)
                .filter(f -> !f.eIsProxy())
                .findFirst()
                .ifPresent(referenceSubsetting::setReferencedFeature);
    }
}
