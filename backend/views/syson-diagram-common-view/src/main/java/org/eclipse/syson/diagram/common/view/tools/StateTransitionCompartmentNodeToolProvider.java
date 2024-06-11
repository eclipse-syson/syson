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

import java.util.List;

import org.eclipse.syson.util.AQLUtils;

/**
 * Node tool provider for creating nested/owned states in the "state transition" State compartment.
 *
 * @author adieumegard
 */
public class StateTransitionCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    private final boolean isParallel;

    public StateTransitionCompartmentNodeToolProvider(boolean isParallel) {
        super();
        this.isParallel = isParallel;
    }

    @Override
    protected String getServiceCallExpression() {
        if (this.isParallel) {
            return AQLUtils.getSelfServiceCallExpression("createChildState", List.of("editingContext", "diagramContext", "selectedNode", "convertedNodes", "true"));
        } else {
            return AQLUtils.getSelfServiceCallExpression("createChildState", List.of("editingContext", "diagramContext", "selectedNode", "convertedNodes", "false"));
        }
    }

    @Override
    protected String getNodeToolName() {
        if (this.isParallel) {
            return "New Parallel State";
        } else {
            return "New State";
        }
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
