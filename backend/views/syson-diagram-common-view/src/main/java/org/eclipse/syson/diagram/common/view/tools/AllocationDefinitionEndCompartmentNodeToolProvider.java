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

/**
 * Node tool provider for creating an Part usage as an end of Allocation definition.
 *
 * @author Jerome Gout
 */
public class AllocationDefinitionEndCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    public AllocationDefinitionEndCompartmentNodeToolProvider() {
        super();
    }

    @Override
    protected String getServiceCallExpression() {
        return "aql:self.createPartUsageAsAllocationDefinitionEnd(self.eContainer().eContainer())";
    }

    @Override
    protected String getNodeToolName() {
        return "New Part as End";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/PartUsage.svg";
    }

    @Override
    protected boolean expandOnCreate() {
        return false;
    }
}
