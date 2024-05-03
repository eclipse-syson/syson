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
package org.eclipse.syson.diagram.statetransition.view.tools;

import org.eclipse.syson.diagram.common.view.tools.AbstractCompartmentNodeToolProvider;

/**
 * Node tool provider for Children State compartment.
 *
 * @author adieumegard
 */
public class StateUsageCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    private boolean isParallel;

    public StateUsageCompartmentNodeToolProvider(boolean isParallel) {
        super();
        this.isParallel = isParallel;
    }

    @Override
    protected String getServiceCallExpression() {
        if (this.isParallel) {
            return "aql:self.createChildState(editingContext, diagramContext, selectedNode, convertedNodes, true)";
        } else {
            return "aql:self.createChildState(editingContext, diagramContext, selectedNode, convertedNodes, false)";
        }
    }

    @Override
    protected String getNodeToolName() {
        if (this.isParallel) {
            return "Parallel";
        } else {
            return "State";
        }
    }

    @Override
    protected String getPreconditionExpression() {
        if (this.isParallel) {
            return "aql:self.canCreateChildState(true)";
        } else {
            return "aql:self.canCreateChildState(false)";
        }
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/StateUsage.svg";
    }

    @Override
    protected boolean revealOnCreate() {
        return false;
    }
}
