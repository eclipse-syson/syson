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
package org.eclipse.syson.standard.diagrams.view.nodes;

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to define an item on the items compartment for PortUsage getUsage_NestedItem.
 *
 * @author mcharfadi
 */
public class PortUsageNestedItemCompartmentItemNodeDescriptionProvider extends CompartmentItemNodeDescriptionProvider {

    public PortUsageNestedItemCompartmentItemNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), colorProvider, descriptionNameGenerator);
    }

    // We filter so elements that extends Item like Parts are not returned
    @Override
    protected String getSemanticCandidateExpression() {
        var itemUsage = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getItemUsage());
        return AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getUsage_NestedItem().getName() + "->select(e | e.oclIsTypeOf(" + itemUsage + "))";
    }
}
