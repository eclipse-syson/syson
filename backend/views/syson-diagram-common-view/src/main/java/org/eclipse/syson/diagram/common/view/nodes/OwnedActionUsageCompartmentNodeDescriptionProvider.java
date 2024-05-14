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
package org.eclipse.syson.diagram.common.view.nodes;

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.OwnedActionUsageCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the compartment owning ActionUsage inside an ActionDefinition.
 *
 * @author Jerome Gout
 */
public class OwnedActionUsageCompartmentNodeDescriptionProvider extends ActionUsageCompartmentNodeDescriptionProvider {

    public OwnedActionUsageCompartmentNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), colorProvider, nameGenerator);
    }

    @Override
    protected INodeToolProvider getItemCreationToolProvider() {
        return new OwnedActionUsageCompartmentNodeToolProvider(this.nameGenerator);
    }
}
