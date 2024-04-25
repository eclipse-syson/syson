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
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;

/**
 * Used to create a {@link PortUsage} as the receiver of an accept action usage.
 *
 * @author Jerome Gout
 */
public class AcceptActionPortUsageReceiverToolNodeProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var creationPayloadServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".createAcceptActionReceiver()")
                .build();

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(creationPayloadServiceCall)
                .build();

        return builder.name("New Port as Receiver")
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getPortUsage().getName() + ".svg")
                .body(rootChangContext)
                .preconditionExpression(AQLConstants.AQL_SELF + ".isEmptyAcceptActionUsageReceiver()")
                .build();
    }
}
