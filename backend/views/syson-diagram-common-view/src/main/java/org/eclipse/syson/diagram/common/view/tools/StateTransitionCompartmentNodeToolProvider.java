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

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.util.AQLUtils;

/**
 * Node tool provider for creating nested/owned states in the "state transition" State compartment.
 *
 * @author adieumegard
 */
public class StateTransitionCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    private final boolean isParallel;

    private final boolean isExhibit;

    public StateTransitionCompartmentNodeToolProvider(boolean isParallel, boolean isExhibit) {
        super();
        this.isParallel = isParallel;
        this.isExhibit = isExhibit;
    }

    @Override
    protected String getServiceCallExpression() {
        return AQLUtils.getSelfServiceCallExpression("createChildState",
                List.of(IEditingContext.EDITING_CONTEXT, IDiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE, String.valueOf(this.isParallel),
                        String.valueOf(this.isExhibit)));
    }

    @Override
    protected String getNodeToolName() {
        StringBuilder builder = new StringBuilder("New ");
        if (this.isExhibit) {
            builder.append("Exhibit ");
        }
        if (this.isParallel) {
            builder.append("Parallel ");
        }
        builder.append("State");
        return builder.toString();
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        var iconPath = "";
        if (this.isExhibit) {
            iconPath = "/icons/full/obj16/ExhibitStateUsage.svg";
        } else {
            iconPath = "/icons/full/obj16/StateUsage.svg";
        }
        return iconPath;
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }
}
