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

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.syson.tree.services.aql.TreeQueryAQLService;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node tool to redefine a nested feature of a Usage or Definition.
 *
 * @author Jerome Gout
 */
public class RedefineNestedFeatureToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newNodeTool()
                .name("Redefine a nested feature")
                .iconURLsExpression("/icons/full/obj16/Redefinition.svg")
                .dialogDescription(this.getSelectionDialogDescription())
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLConstants.AQL_SELF)
                        .build())
                .build();
    }

    private SelectionDialogDescription getSelectionDialogDescription() {
        var tree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of0(TreeQueryAQLService::getFeatureChainSelectionDialogElements).aqlSelf())
                .childrenExpression("aql:Sequence{}")
                .isSelectableExpression(AQLConstants.AQL_TRUE)
                .build();

        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(tree)
                .defaultTitleExpression("Redefine a nested feature")
                .descriptionExpression("Select a feature chain to redefine:")
                .multiple(false)
                .optional(false)
                .build();
    }
}
