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
package org.eclipse.syson.diagram.common.view.tools;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;

/**
 * Node Tool to set the "isComposite" attribute of a Feature to false.
 *
 * @author arichard
 */
public class SetAsRefToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var setIsComposite = this.viewBuilderHelper.newSetValue()
                .featureName("isComposite")
                .valueExpression("aql:false");

        var toolLabel = "Set as ref";

        return builder
                .name(toolLabel)
                .iconURLsExpression("/icons/full/obj16/Membership.svg")
                .body(setIsComposite.build())
                .preconditionExpression("aql:self.oclIsKindOf(sysml::Feature) and self.isComposite == true")
                .build();
    }

}
