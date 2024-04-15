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
package org.eclipse.syson.diagram.general.view.nodes;

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the Allocation usage edge description in General View diagram.
 *
 * @author Jerome Gout
 */
public class AllocationUsageNodeDescriptionProvider extends UsageNodeDescriptionProvider {

    public AllocationUsageNodeDescriptionProvider(IColorProvider colorProvider) {
        super(SysmlPackage.eINSTANCE.getAllocationUsage(), colorProvider);
    }

    @Override
    protected String getSemanticCandidatesExpression(String domainType) {
        return "aql:self.getAllReachableAllocationUsages()";
    }
}
