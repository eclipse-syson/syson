/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.diagram.requirement.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.RVDescriptionNameGenerator;
import org.eclipse.syson.diagram.requirement.view.RequirementViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the Compartment node description inside the Requirement View diagram.
 *
 * @author Jerome Gout
 */
public class CompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public CompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider) {
        super(eClass, eReference, colorProvider, new RVDescriptionNameGenerator());
    }

    @Override
    protected String getCustomCompartmentLabel() {
        String customLabel = super.getCustomCompartmentLabel();
        if (this.eReference == SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint()) {
            customLabel = "assume constraints";
        } else if (this.eReference == SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint()) {
            customLabel = "require constraints";
        }
        return customLabel;
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        RequirementViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(ref -> {
                if (this.eReference.getEType().equals(ref.getEType())) {
                    var optCompartmentItemNodeDescription = cache.getNodeDescription(this.nameGenerator.getCompartmentItemName(type, ref));
                    acceptedNodeTypes.add(optCompartmentItemNodeDescription.get());
                }
            });
        });

        return acceptedNodeTypes;
    }

    @Override
    protected String getDropElementFromDiagramExpression() {
        String customExpression = super.getDropElementFromDiagramExpression();
        if (this.eReference == SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint()) {
            customExpression = "aql:droppedElement.dropElementFromDiagramInRequirementAssumeConstraintCompartment(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)";
        } else if (this.eReference == SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint()) {
            customExpression = "aql:droppedElement.dropElementFromDiagramInRequirementRequireConstraintCompartment(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)";
        } else if (this.eReference == SysmlPackage.eINSTANCE.getUsage_NestedConstraint() || this.eReference == SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint()) {
            customExpression = "aql:droppedElement.dropElementFromDiagramInConstraintCompartment(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)";
        }
        return customExpression;
    }
}
