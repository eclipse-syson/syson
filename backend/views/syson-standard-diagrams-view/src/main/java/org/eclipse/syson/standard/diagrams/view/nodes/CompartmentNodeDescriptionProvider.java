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
package org.eclipse.syson.standard.diagrams.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.SDVDiagramDescriptionProvider;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Used to create a Compartment node description inside the General View diagram.
 *
 * @author Jerome Gout
 */
public class CompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public CompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider) {
        super(eClass, eReference, colorProvider, new SDVDescriptionNameGenerator());
    }

    @Override
    protected String getCustomCompartmentLabel() {
        String customLabel = super.getCustomCompartmentLabel();
        if (this.eReference == SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint() ||
                this.eReference == SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint()) {
            customLabel = "assume constraints";
        } else if (this.eReference == SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint() ||
                this.eReference == SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint()) {
            customLabel = "require constraints";
        }
        return customLabel;
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        SDVDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(ref -> {
                if (this.eReference.getEType().equals(ref.getEType())) {
                    cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(type, ref))
                            .ifPresent(acceptedNodeTypes::add);
                }
            });
        });

        return acceptedNodeTypes;
    }

    @Override
    protected String getDropElementFromDiagramExpression() {
        String customExpression = super.getDropElementFromDiagramExpression();
        if (this.eReference == SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint()
                || this.eReference == SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint()) {
            customExpression = ServiceMethod.of6(DiagramMutationAQLService::dropElementFromDiagramInRequirementAssumeConstraintCompartment).aql("droppedElement", "droppedNode", "targetElement",
                    "targetNode", IEditingContext.EDITING_CONTEXT,
                    DiagramContext.DIAGRAM_CONTEXT, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE);
        } else if (this.eReference == SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint()
                || this.eReference == SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint()) {
            customExpression = ServiceMethod.of6(DiagramMutationAQLService::dropElementFromDiagramInRequirementRequireConstraintCompartment).aql("droppedElement", "droppedNode", "targetElement",
                    "targetNode", IEditingContext.EDITING_CONTEXT,
                    DiagramContext.DIAGRAM_CONTEXT, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE);
        } else if (this.eReference == SysmlPackage.eINSTANCE.getUsage_NestedConstraint() || this.eReference == SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint()) {
            customExpression = ServiceMethod.of6(DiagramMutationAQLService::dropElementFromDiagramInConstraintCompartment).aql("droppedElement", "droppedNode", "targetElement",
                    "targetNode", IEditingContext.EDITING_CONTEXT,
                    DiagramContext.DIAGRAM_CONTEXT, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE);
        }
        return customExpression;
    }

    @Override
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        List<INodeToolProvider> itemCreationToolProviders = super.getItemCreationToolProviders();
        if (SDVDiagramDescriptionProvider.DIRECTIONAL_ELEMENTS.contains(this.eReference.getEType())) {
            itemCreationToolProviders.add(new CompartmentNodeToolProvider(this.eReference, this.getDescriptionNameGenerator(), FeatureDirectionKind.IN));
            itemCreationToolProviders.add(new CompartmentNodeToolProvider(this.eReference, this.getDescriptionNameGenerator(), FeatureDirectionKind.INOUT));
            itemCreationToolProviders.add(new CompartmentNodeToolProvider(this.eReference, this.getDescriptionNameGenerator(), FeatureDirectionKind.OUT));
        }
        return itemCreationToolProviders;
    }
}
