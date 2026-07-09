/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.model.services.aql;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.model.services.ModelQueryElementService;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;

/**
 * Entry point for all model-related services doing queries in models and called by AQL expressions in representation
 * descriptions.
 *
 * @author arichard
 */
public class ModelQueryAQLService {

    private final ModelQueryElementService modelQueryElementService;

    private final MetamodelQueryElementService metamodelQueryElementService;

    public ModelQueryAQLService() {
        this.modelQueryElementService = new ModelQueryElementService();
        this.metamodelQueryElementService = new MetamodelQueryElementService();
    }

    /**
     * {@link ModelQueryElementService#checkTransitionEdgeTarget(Element, Element)}.
     */
    public boolean checkTransitionEdgeTarget(Element source, Element target) {
        return this.modelQueryElementService.checkTransitionEdgeTarget(source, target);
    }

    /**
     * {@link ModelQueryElementService#getAllReachableAllocateEdges(EObject)}.
     */
    public List<AllocationUsage> getAllReachableAllocateEdges(EObject eObject) {
        return this.modelQueryElementService.getAllReachableAllocateEdges(eObject);
    }

    /**
     * {@link MetamodelQueryElementService#getConnectorSource(Connector)}.
     */
    public Feature getConnectorSource(Connector connector) {
        return this.metamodelQueryElementService.getConnectorSource(connector);
    }

    /**
     * {@link MetamodelQueryElementService#getConnectorTarget(Connector)}.
     */
    public List<Feature> getConnectorTarget(ConnectorAsUsage connector) {
        return this.metamodelQueryElementService.getConnectorTarget(connector);
    }

    /**
     * {@link MetamodelQueryElementService#getFeatureValueTarget(FeatureValue)}.
     */
    public Feature getFeatureValueTarget(FeatureValue featureValue) {
        return this.metamodelQueryElementService.getFeatureValueTarget(featureValue);
    }

    /**
     * {@link MetamodelQueryElementService#getFramedConcernTarget(FramedConcernMembership)}.
     */
    public ConcernUsage getFramedConcernTarget(FramedConcernMembership framedConcernMembership) {
        return this.metamodelQueryElementService.getFramedConcernTarget(framedConcernMembership);
    }

    /**
     * {@link MetamodelQueryElementService#getRequirementConstraintTarget(RequirementConstraintMembership)}.
     */
    public ConstraintUsage getRequirementConstraintTarget(RequirementConstraintMembership requirementConstraintMembership) {
        return this.metamodelQueryElementService.getRequirementConstraintTarget(requirementConstraintMembership);
    }

    /**
     * {@link MetamodelQueryElementService#getSourceAllocateEdge(AllocationUsage)}.
     */
    public Element getSourceAllocateEdge(AllocationUsage allocationUsage) {
        return this.metamodelQueryElementService.getSourceAllocateEdge(allocationUsage);
    }

    /**
     * {@link MetamodelQueryElementService#getTargetAllocateEdge(AllocationUsage)}.
     */
    public Element getTargetAllocateEdge(AllocationUsage allocationUsage) {
        return this.metamodelQueryElementService.getTargetAllocateEdge(allocationUsage);
    }

    /**
     * {@link MetamodelQueryElementService#isActor(Element)}.
     */
    public boolean isActor(Element element) {
        return this.metamodelQueryElementService.isActor(element);
    }

    /**
     * {@link MetamodelQueryElementService#isStakeholder(Element)}.
     */
    public boolean isStakeholder(Element element) {
        return this.metamodelQueryElementService.isStakeholder(element);
    }

    /**
     * {@link MetamodelQueryElementService#isSubject(Element)}.
     */
    public boolean isSubject(Element element) {
        return this.metamodelQueryElementService.isSubject(element);
    }

    /**
     * {@link MetamodelQueryElementService#isTransitionUsageForState(TransitionUsage)}.
     */
    public boolean isTransitionUsageForState(TransitionUsage transition) {
        return this.metamodelQueryElementService.isTransitionUsageForState(transition);
    }

    /**
     * {@link MetamodelQueryElementService#unwrapFeature(Feature)}.
     */
    public Feature unwrapFeature(Feature input) {
        return this.metamodelQueryElementService.unwrapFeature(input);
    }
}
