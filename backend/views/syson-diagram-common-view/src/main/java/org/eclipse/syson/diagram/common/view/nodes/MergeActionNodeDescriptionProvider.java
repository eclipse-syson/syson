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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the Merge node description of an Action.
 *
 * @author Jerome Gout
 */
public class MergeActionNodeDescriptionProvider extends AbstractControlNodeActionNodeDescriptionProvider {

    public static final String MERGE_ACTION_NAME = "MergeAction";

    public MergeActionNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getNodeDescriptionName() {
        return MERGE_ACTION_NAME;
    }

    @Override
    protected EClass getDomainType() {
        return SysmlPackage.eINSTANCE.getMergeNode();
    }

    @Override
    protected String getImagePath() {
        return "images/merge_action.svg";
    }

    @Override
    protected String getRemoveToolLabel() {
        return "Remove Merge";
    }

    @Override
    protected String getDefaultWidthExpression() {
        return "36";
    }

    @Override
    protected String getDefaultHeightExpression() {
        return this.getDefaultWidthExpression();
    }

    @Override
    protected UserResizableDirection isNodeResizable() {
        return UserResizableDirection.NONE;
    }
}
