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
package org.eclipse.syson.standard.diagrams.view.nodes;

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to define an item on the items compartment for PortDefinition getDefinition_OwnedItem.
 *
 * @author mcharfadi
 */
public class PortDefinitionOwnedItemCompartmentItemNodeDescriptionProvider extends CompartmentItemNodeDescriptionProvider {

    public PortDefinitionOwnedItemCompartmentItemNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(SysmlPackage.eINSTANCE.getPortDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), colorProvider, descriptionNameGenerator);
    }

    // We filter so elements that extends Item like Parts are not returned
    @Override
    protected String getSemanticCandidateExpression() {
        return "aql:self.ownedItem->select(e | e.oclIsTypeOf(sysml::ItemUsage))";
    }

}
