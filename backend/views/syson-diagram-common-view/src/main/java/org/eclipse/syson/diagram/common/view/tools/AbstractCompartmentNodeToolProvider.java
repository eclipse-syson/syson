/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;

/**
 * Node tool provider for elements inside compartments.
 *
 * @author gdaniel
 */
public abstract class AbstractCompartmentNodeToolProvider implements INodeToolProvider {

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

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
     * Whether the tool will reveal its associated node compartment after its execution or not.
     *
     * @return if <code>true</code>, the tool will reveal its associated node node after the execution of the tool.
     */
    protected abstract boolean revealOnCreate();

    /**
     * Return the node tool icon URL expression to retrieve the icon of the tool visible in the compartment palette.
     *
     * @return the node tool icon URL expression
     */
    protected abstract String getNodeToolIconURLsExpression();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();
        builder.dialogDescription(this.getSelectionDialogDescription());

        var revealOperation = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(Node.SELECTED_NODE, "revealCompartment",
                        List.of("self", DiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE)));

        var addToExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("expose",
                        List.of(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE)));

        var creationCompartmentItemServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(this.getServiceCallExpression())
                .children(addToExposedElements.build(), revealOperation.build());

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(creationCompartmentItemServiceCall.build())
                .build();

        return builder.name(this.getNodeToolName())
                .iconURLsExpression(this.getNodeToolIconURLsExpression())
                .body(rootChangContext)
                .preconditionExpression(this.getPreconditionExpression())
                .build();
    }

    /**
     * Returns the selection dialog to display as part of the tool's execution.
     * <p>
     * No selection dialog will be displayed if this method returns {@code null}.
     * </p>
     *
     * @return the selection dialog to display as part of the tool's execution
     */
    protected SelectionDialogDescription getSelectionDialogDescription() {
        // No selection dialog by default.
        return null;
    }

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
}
