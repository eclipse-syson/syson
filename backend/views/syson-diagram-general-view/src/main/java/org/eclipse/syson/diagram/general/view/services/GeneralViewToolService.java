/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view.services;

import java.util.Map;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.services.ToolService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;

/**
 * Tool-related Java services used by the {@link GeneralViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class GeneralViewToolService extends ToolService {

    public GeneralViewToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService) {
        super(objectService, representationDescriptionSearchService);
    }

    /**
     * Called by "Add existing elements" tool from General View diagram or General View Package node. Add nodes that are
     * not present in the diagram or the selectedNode (i.e. a Package).
     *
     * @param pkg
     *            the {@link Package} corresponding to the target object of the Diagram or the {@link Node} Package on
     *            which the tool has been called.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the tool has been called (may be null if the tool has been called from the
     *            diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the input {@link Package}.
     */
    public Package addExistingElements(Package pkg, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var members = pkg.getOwnedMember();
        members.stream()
                .filter(member -> !this.isPresent(member, this.getChildNodes(diagramContext, selectedNode)))
                .forEach(member -> this.createView(member, editingContext, diagramContext, selectedNode, convertedNodes));
        return pkg;
    }

    public PartUsage becomeNestedPart(PartUsage partUsage, Element newContainer) {
        var eContainer = partUsage.eContainer();
        if (eContainer instanceof FeatureMembership featureMembership) {
            newContainer.getOwnedRelationship().add(featureMembership);
        } else if (eContainer instanceof OwningMembership owningMembership) {
            var newFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            newFeatureMembership.getOwnedRelatedElement().add(partUsage);
            newContainer.getOwnedRelationship().add(newFeatureMembership);
            EcoreUtil.delete(owningMembership);
        }
        return partUsage;
    }

    public PartUsage addAsNestedPart(PartDefinition partDefinition, PartUsage partUsage) {
        var eContainer = partUsage.eContainer();
        if (eContainer instanceof FeatureMembership featureMembership) {
            partDefinition.getOwnedRelationship().add(featureMembership);
        } else if (eContainer instanceof OwningMembership owningMembership) {
            var newFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            newFeatureMembership.getOwnedRelatedElement().add(partUsage);
            partDefinition.getOwnedRelationship().add(newFeatureMembership);
            EcoreUtil.delete(owningMembership);
        }
        return partUsage;
    }

    /**
     * Called by "Add existing nested PartUsage" tool from General View PartUsage node. Add nodes that are not present
     * in selectedNode (i.e. a PartUsage).
     *
     * @param partUsage
     *            the {@link PartUsage} corresponding to the target object of the Diagram or the {@link Node} PartUsage
     *            on which the tool has been called.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the tool has been called (may be null if the tool has been called from the
     *            diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the input {@link PartUsage}.
     */
    public PartUsage addExistingElements(PartUsage partUsage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var nestedParts = partUsage.getNestedPart();
        Object parentNode = this.getParentNode(partUsage, selectedNode, diagramContext);
        nestedParts.stream()
                .filter(member -> !this.isPresent(member, this.getChildNodes(diagramContext, parentNode)))
                .forEach(member -> this.createView(member, editingContext, diagramContext, parentNode, convertedNodes));
        return partUsage;
    }

    /**
     * Called by "Add existing nested elements" tool from General View PartDefinition node. Add nodes that are not
     * present in selectedNode (i.e. a PartUsage/ItemUsage).
     *
     * @param partDef
     *            the {@link PartDefinition} corresponding to the target object of the Diagram or the {@link Node}
     *            PartDefinition on which the tool has been called.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the tool has been called (may be null if the tool has been called from the
     *            diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the input {@link PartDefinition}.
     */
    public PartDefinition addExistingElements(PartDefinition partDef, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var nestedItems = partDef.getOwnedItem();
        Object parentNode = this.getParentNode(partDef, selectedNode, diagramContext);
        nestedItems.stream()
                .filter(member -> !this.isPresent(member, this.getChildNodes(diagramContext, parentNode)))
                .forEach(member -> this.createView(member, editingContext, diagramContext, parentNode, convertedNodes));
        return partDef;
    }
}
