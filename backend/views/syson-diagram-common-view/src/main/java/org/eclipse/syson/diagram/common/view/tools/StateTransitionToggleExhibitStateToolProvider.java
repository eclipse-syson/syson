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
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;

/**
 * Tool used to (un)set whether a StateUsage is exhibited or not.
 *
 * @author adieumegard
 */
public class StateTransitionToggleExhibitStateToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final boolean exhibit;

    public StateTransitionToggleExhibitStateToolProvider(boolean exhibit) {
        this.exhibit = exhibit;

    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var setUnsetAsExhibitedServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("setUnsetAsExhibit"))
                .build();

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(setUnsetAsExhibitedServiceCall)
                .build();

        String toolName = "Exhibit";
        String preconditionServiceName = "canBeExhibitedStateUsage";
        if (!this.exhibit) {
            toolName = "Set as not exhibited";
            preconditionServiceName = "isExhibitedStateUsage";
        }

        return builder.name(toolName)
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getExhibitStateUsage().getName() + ".svg")
                .body(rootChangContext)
                .preconditionExpression(AQLUtils.getSelfServiceCallExpression(preconditionServiceName))
                .build();
    }
}
