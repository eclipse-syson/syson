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

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.services.ViewNodeService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node tool provider for elements inside compartments.
 *
 * @author gdaniel
 */
public abstract class AbstractCompartmentNodeToolProvider implements INodeToolProvider {

    private static final String NEW_INSTANCE = "newInstance";

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

        ChangeContextBuilder revealOperation;
        if (this.revealOnCreate()) {
            revealOperation = this.viewBuilderHelper.newChangeContext()
                    .expression(ServiceMethod.of4(ViewNodeService::revealCompartment).aql(Node.SELECTED_NODE, AQLConstants.SELF, DiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT,
                            ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));
        } else {
            revealOperation = this.viewBuilderHelper.newChangeContext().expression(AQLConstants.AQL_SELF);
        }

        var addToExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var exposeAndRevealNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + NEW_INSTANCE)
                .children(addToExposedElements.build(), revealOperation.build());

        var letNewInstance = this.viewBuilderHelper.newLet()
                .variableName(NEW_INSTANCE)
                .valueExpression(this.getServiceCallExpression())
                .children(exposeAndRevealNewInstance.build());

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(letNewInstance.build())
                .build();

        return builder.name(this.getNodeToolName())
                .iconURLsExpression(this.getNodeToolIconURLsExpression())
                .body(rootChangContext)
                .preconditionExpression(this.getPreconditionExpression())
                .elementsToSelectExpression(AQLConstants.AQL + NEW_INSTANCE)
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
