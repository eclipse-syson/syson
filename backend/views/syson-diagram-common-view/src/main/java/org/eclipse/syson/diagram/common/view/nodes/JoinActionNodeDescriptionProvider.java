/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the Join node description of an Action.
 *
 * @author Jerome Gout
 */
public class JoinActionNodeDescriptionProvider extends AbstractControlNodeActionNodeDescriptionProvider {

    public static final String JOIN_ACTION_NAME = "JoinAction";

    public JoinActionNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getNodeDescriptionName() {
        return JOIN_ACTION_NAME;
    }

    @Override
    protected EClass getDomainType() {
        return SysmlPackage.eINSTANCE.getJoinNode();
    }

    @Override
    protected String getRemoveToolLabel() {
        return "Remove Join";
    }

    @Override
    protected UserResizableDirection isNodeResizable() {
        return UserResizableDirection.HORIZONTAL;
    }

    @Override
    protected NodeStyleDescription createNodeStyleDescription() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor("transparent"))
                .borderRadius(0)
                .borderSize(0)
                .background(this.colorProvider.getColor("black"))
                .build();
    }
}
