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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.Operation;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.util.AQLConstants;

/**
 * Node tool provider for elements inside compartments.
 *
 * @author gdaniel
 */
public abstract class AbstractCompartmentNodeToolProvider implements INodeToolProvider {

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    public AbstractCompartmentNodeToolProvider() {
    }

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
     * By default no precondition expression is provided. Implementers might override this method to explicitly provide
     * this precondition expression.
     *
     * @return the node tool precondition expression.
     */
    protected String getPreconditionExpression() {
        return null;
    }

    /**
     * Whether the tool will reveal its associated node compartment after its execution or not.
     *
     * @return if <code>true</code>, the tool will reveal its associated node node after the execution of the tool.
     */
    protected abstract boolean revealOnCreate();

    /**
     * Return the node tool icon URL expression to retrieve the icon of the tool visible in the compartment palette.
     *
     * @return
     */
    protected abstract String getNodeToolIconURLsExpression();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        List<Operation> allOperations = new ArrayList<>();

        var creationCompartmentItemServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(this.getServiceCallExpression())
                .build();
        allOperations.add(creationCompartmentItemServiceCall);

        if (this.revealOnCreate()) {
            var revealOperation = this.viewBuilderHelper.newChangeContext()
                    .expression("aql:selectedNode.revealCompartment(self, diagramContext, editingContext, convertedNodes)")
                    .build();
            allOperations.add(revealOperation);
        }

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(allOperations.toArray(Operation[]::new))
                .build();

        return builder.name(this.getNodeToolName())
                .iconURLsExpression(this.getNodeToolIconURLsExpression())
                .body(rootChangContext)
                .preconditionExpression(this.getPreconditionExpression())
                .build();
    }
}
