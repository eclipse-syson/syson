/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node tool provider for creating an port end on an Interface definition.
 *
 * @author pcdavid
 */
public class InterfaceDefinitionEndCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    @Override
    protected String getServiceCallExpression() {
        return ServiceMethod.of0(ViewCreateService::createInterfaceDefinitionEnd).aqlSelf();
    }

    @Override
    protected String getNodeToolName() {
        return "New Port as end";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/PortUsage.svg";
    }

    @Override
    protected boolean revealOnCreate() {
        return false;
    }
}
