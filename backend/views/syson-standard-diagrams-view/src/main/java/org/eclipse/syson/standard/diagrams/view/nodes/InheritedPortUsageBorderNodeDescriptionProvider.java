/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
import org.eclipse.sirius.components.view.diagram.OutsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelPosition;
import org.eclipse.syson.diagram.common.view.nodes.AbstractPortUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the inherited port usage border node description.
 *
 * @author frouene
 */
public class InheritedPortUsageBorderNodeDescriptionProvider extends AbstractPortUsageBorderNodeDescriptionProvider {

    public InheritedPortUsageBorderNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider, nameGenerator);
    }

    @Override
    protected String getSemanticCandidatesExpression() {
        return AQLUtils.getSelfServiceCallExpression("getInheritedCompartmentItems", "'" + SysmlPackage.eINSTANCE.getUsage_NestedPort().getName() + "'");
    }

    @Override
    protected OutsideLabelDescription createOutsideLabelDescription() {
        return this.diagramBuilderHelper.newOutsideLabelDescription()
                .labelExpression(AQLConstants.AQL + "'^' + self.getBorderNodeUsageLabel()")
                .position(OutsideLabelPosition.BOTTOM_CENTER)
                .style(this.createOutsideLabelStyle())
                .build();
    }

    @Override
    protected String getName() {
        return this.nameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage());
    }
}
