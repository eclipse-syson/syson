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
 * Node tool provider for elements inside compartments.
 *
 * @author gdaniel
 */
public abstract class AbstractCompartmentNodeToolProvider implements INodeToolProvider {

    private DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private ViewBuilders viewBuilderHelper = new ViewBuilders();

    public AbstractCompartmentNodeToolProvider() { }

    /**
     * Return the AQL service call expression that is interpreted to perform the node tool.
     *
     * @return the AQL expression associated to this node tool.
     */
    protected abstract String getServiceCallExpression();

    /**
     * Return the node tool label visible in the compartment palette.
     *
     * @return the name of the node tool.
     */
    protected abstract String getNodeToolName();

    /**
     * Return the node tool precondition expression to control whether the tool is added to the palette or not.<br>
     * By default no precondition expression is provided. 
     * Implementers might override this method to explicitly provide this precondition expression.
     * @return the node tool precondition expression.
     */
    protected String getPreconditionExpression() {
        return null;
    }

    /**
     * Return the node tool icon URL expression to retrieve the icon of the tool visible in the compartment palette.
     *
     * @return
     */
    protected abstract String getNodeToolIconURLsExpression();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var creationCompartmentItemServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(this.getServiceCallExpression());

        return builder.name(this.getNodeToolName())
                .iconURLsExpression(this.getNodeToolIconURLsExpression())
                .body(creationCompartmentItemServiceCall.build())
                .preconditionExpression(this.getPreconditionExpression())
                .build();
    }
}
