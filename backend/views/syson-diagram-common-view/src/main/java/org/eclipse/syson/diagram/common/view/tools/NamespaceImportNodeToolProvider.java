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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create a {@link NamespaceImport}.
 *
 * @author Jerome Gout
 */
public class NamespaceImportNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    private final NodeDescription nodeDescription;

    public NamespaceImportNodeToolProvider(NodeDescription nodeDescription, IDescriptionNameGenerator descriptionNameGenerator) {
        this.nodeDescription = Objects.requireNonNull(nodeDescription);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        EClass eClass = SysmlPackage.eINSTANCE.getNamespaceImport();

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(this.nodeDescription)
                .parentViewExpression("aql:selectedNode")
                .semanticElementExpression(AQLConstants.AQL_SELF);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(ViewCreateService::createNamespaceImport).aqlSelf("selectedObject"))
                .children(createView.build());

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
        var domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPackage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of0(ViewToolService::getNamespaceImportSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of0(ViewToolService::getNamespaceImportSelectionDialogChildren).aqlSelf())
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + domainType + ")")
                .build();
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select a Package to import:")
                .build();
    }
}
