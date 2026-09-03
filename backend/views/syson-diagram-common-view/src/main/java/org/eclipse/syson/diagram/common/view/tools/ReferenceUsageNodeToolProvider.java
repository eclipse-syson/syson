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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.trees.renderer.TreeRenderer;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.tree.services.aql.TreeQueryAQLService;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create a {@link ReferenceUsage} on diagram background.
 *
 * @author Jerome Gout
 */
public class ReferenceUsageNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    private final NodeDescription nodeDescription;

    public ReferenceUsageNodeToolProvider(NodeDescription nodeDescription, IDescriptionNameGenerator descriptionNameGenerator) {
        this.nodeDescription = Objects.requireNonNull(nodeDescription);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        EClass eClass = SysmlPackage.eINSTANCE.getReferenceUsage();

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(this.nodeDescription)
                .parentViewExpression("aql:selectedNode")
                .semanticElementExpression(AQLConstants.AQL_SELF);

        var addToExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramMutationAQLService::createReferenceUsage).aqlSelf("selectedObject"))
                .children(createView.build(), addToExposedElements.build());

        var changeContextViewUsageOwner = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(UtilService::getViewUsageOwner).aqlSelf())
                .children(changeContextNewInstance.build());

        return this.diagramBuilderHelper.newNodeTool()
                .name(this.descriptionNameGenerator.getCreationToolName(eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .dialogDescription(this.getSelectionDialogDescription())
                .body(changeContextViewUsageOwner.build())
                .build();
    }

    private SelectionDialogDescription getSelectionDialogDescription() {
        var domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of1(TreeQueryAQLService::getSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT, AQLUtils.aqlSequence(List.of(domainType))))
                .childrenExpression(
                        ServiceMethod.of3(TreeQueryAQLService::getSelectionDialogChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, TreeRenderer.EXPANDED, AQLUtils.aqlSequence(List.of(domainType))))
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + domainType + ")")
                .build();

        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression("New Reference")
                .noSelectionTitleExpression("New Reference")
                .withSelectionTitleExpression("New Reference")
                .noSelectionActionLabelExpression("Create a new reference")
                .noSelectionActionDescriptionExpression("Create a new empty reference")
                .withSelectionActionLabelExpression("Select an existing element")
                .withSelectionActionDescriptionExpression("Create a new reference of selected element")
                .noSelectionActionStatusMessageExpression("It will create en empty reference")
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one usage to reference")
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will reference ' + selectedObjects->first().name")
                .optional(true)
                .build();
    }
}
