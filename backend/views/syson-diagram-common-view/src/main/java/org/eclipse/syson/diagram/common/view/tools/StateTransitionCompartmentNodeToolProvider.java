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
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node tool provider for creating nested/owned states in the "state transition" State compartment.
 *
 * @author adieumegard
 */
public class StateTransitionCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    private final boolean isParallel;

    public StateTransitionCompartmentNodeToolProvider(boolean isParallel) {
        this.isParallel = isParallel;
    }

    @Override
    protected String getServiceCallExpression() {
        return ServiceMethod.of6(DiagramMutationAQLService::createChildState).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE, String.valueOf(this.isParallel),
                String.valueOf(Boolean.FALSE));
    }

    @Override
    protected String getNodeToolName() {
        StringBuilder builder = new StringBuilder("New ");
        if (this.isParallel) {
            builder.append("Parallel ");
        }
        builder.append("State");
        return builder.toString();
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/StateUsage.svg";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }
}
