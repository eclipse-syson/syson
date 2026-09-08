/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.diagram.services;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Resolve the endpoints of relationships rendered as General View edges.
 *
 * @author cbrun
 */
public class GeneralViewEdgeEndpointsSwitch extends SysmlSwitch<GeneralViewEdgeEndpointsSwitch.EdgeEndpoints> {

    private final MetamodelQueryElementService metamodelQueryElementService = new MetamodelQueryElementService();

    /**
     * Return the General View edge endpoints for the given AllocationUsage.
     *
     * @param allocationUsage
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseAllocationUsage(AllocationUsage allocationUsage) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(this.metamodelQueryElementService.getSourceAllocateEdge(allocationUsage));
        targets.add(this.metamodelQueryElementService.getTargetAllocateEdge(allocationUsage));
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given FlowUsage.
     *
     * @param flowUsage
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseFlowUsage(FlowUsage flowUsage) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(flowUsage.getSourceOutputFeature());
        targets.add(flowUsage.getTargetInputFeature());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given Connector.
     *
     * @param connector
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseConnector(Connector connector) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(this.metamodelQueryElementService.getConnectorSource(connector));
        targets.addAll(this.metamodelQueryElementService.getConnectorTarget(connector));
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given TransitionUsage.
     *
     * @param transitionUsage
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseTransitionUsage(TransitionUsage transitionUsage) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(transitionUsage.getSource());
        targets.add(transitionUsage.getTarget());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given SatisfyRequirementUsage.
     *
     * @param satisfy
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseSatisfyRequirementUsage(SatisfyRequirementUsage satisfy) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        if (satisfy.getSatisfyingFeature() != null) {
            sources.add(satisfy.getSatisfyingFeature());
        } else {
            sources.add(satisfy.getOwner());
        }
        targets.add(satisfy.getSatisfiedRequirement());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given IncludeUseCaseUsage.
     *
     * @param include
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseIncludeUseCaseUsage(IncludeUseCaseUsage include) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(include.getOwningUsage());
        targets.add(include.getUseCaseIncluded());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given FeatureValue.
     *
     * @param featureValue
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseFeatureValue(FeatureValue featureValue) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(featureValue.getFeatureWithValue());
        targets.add(this.metamodelQueryElementService.getFeatureValueTarget(featureValue));
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given FramedConcernMembership.
     *
     * @param framedConcern
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseFramedConcernMembership(FramedConcernMembership framedConcern) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(((Relationship) framedConcern).getOwningRelatedElement());
        targets.add(this.metamodelQueryElementService.getFramedConcernTarget(framedConcern));
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given RequirementConstraintMembership.
     *
     * @param requirementConstraint
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseRequirementConstraintMembership(RequirementConstraintMembership requirementConstraint) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(requirementConstraint.getOwningRelatedElement());
        targets.add(this.metamodelQueryElementService.getRequirementConstraintTarget(requirementConstraint));
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given Annotation.
     *
     * @param annotation
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseAnnotation(Annotation annotation) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(annotation.getAnnotatingElement());
        targets.add(annotation.getAnnotatedElement());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given Dependency.
     *
     * @param dependency
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseDependency(Dependency dependency) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.addAll(dependency.getClient());
        targets.addAll(dependency.getSupplier());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given FeatureTyping.
     *
     * @param featureTyping
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseFeatureTyping(FeatureTyping featureTyping) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(featureTyping.getTypedFeature());
        targets.add(featureTyping.getType());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given Redefinition.
     *
     * @param redefinition
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseRedefinition(Redefinition redefinition) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(redefinition.getRedefiningFeature());
        targets.add(redefinition.getRedefinedFeature());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given ReferenceSubsetting.
     *
     * @param referenceSubsetting
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseReferenceSubsetting(ReferenceSubsetting referenceSubsetting) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(referenceSubsetting.getReferencingFeature());
        targets.add(referenceSubsetting.getReferencedFeature());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given Subsetting.
     *
     * @param subsetting
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseSubsetting(Subsetting subsetting) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(subsetting.getSubsettingFeature());
        targets.add(subsetting.getSubsettedFeature());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return the General View edge endpoints for the given Subclassification.
     *
     * @param subclassification
     *            the relationship to inspect
     * @return its endpoints, including an empty set for missing endpoints
     */
    @Override
    public EdgeEndpoints caseSubclassification(Subclassification subclassification) {
        var sources = new LinkedHashSet<Element>();
        var targets = new LinkedHashSet<Element>();
        sources.add(subclassification.getSubclassifier());
        targets.add(subclassification.getSuperclassifier());
        return new EdgeEndpoints(sources, targets);
    }

    /**
     * Return no endpoints for elements that do not represent an edge.
     *
     * @param object
     *            the inspected object
     * @return empty endpoints
     */
    @Override
    public EdgeEndpoints defaultCase(EObject object) {
        return new EdgeEndpoints(new LinkedHashSet<>(), new LinkedHashSet<>());
    }

    /**
     * Endpoints of a relationship rendered as an edge.
     *
     * @param sources
     *            source elements
     * @param targets
     *            target elements
     */
    public record EdgeEndpoints(Set<Element> sources, Set<Element> targets) {
        /**
         * Remove missing endpoints from the supplied sets.
         */
        public EdgeEndpoints {
            sources.remove(null);
            targets.remove(null);
        }
    }
}
