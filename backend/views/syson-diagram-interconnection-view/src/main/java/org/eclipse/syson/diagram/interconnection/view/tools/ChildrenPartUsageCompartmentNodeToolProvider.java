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
package org.eclipse.syson.diagram.interconnection.view.tools;

import org.eclipse.syson.diagram.common.view.tools.AbstractCompartmentNodeToolProvider;

/**
 * Node tool provider for Children Part compartment.
 *
 * @author arichard
 */
public class ChildrenPartUsageCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    public ChildrenPartUsageCompartmentNodeToolProvider() {
        super();
    }

    @Override
    protected String getServiceCallExpression() {
        return "aql:self.createChildPart(editingContext, diagramContext, selectedNode, convertedNodes)";
    }

    @Override
    protected String getNodeToolName() {
        return "New Part";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/PartUsage.svg";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }
}
