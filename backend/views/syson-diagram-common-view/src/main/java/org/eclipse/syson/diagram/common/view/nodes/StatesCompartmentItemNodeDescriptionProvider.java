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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionToggleExhibitStateToolProvider;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * {@link ExhibitStateUsage} compartment item Node description.
 *
 * @author adieumegard
 */
public class StatesCompartmentItemNodeDescriptionProvider extends CompartmentItemNodeDescriptionProvider {

    private final boolean showsExhibitOnly;

    private final ToolDescriptionService toolDescriptionService;

    public StatesCompartmentItemNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator, boolean showsExhibitOnly) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
        this.toolDescriptionService = new ToolDescriptionService(descriptionNameGenerator);
        this.showsExhibitOnly = showsExhibitOnly;
    }

    @Override
    public NodeDescription create() {
        NodeDescription nd = super.create();
        if (this.showsExhibitOnly) {
            nd.setName(this.getDescriptionNameGenerator().getCompartmentItemName(this.getEClass(), this.getEReference()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME);
        } else {
            nd.setName(this.getDescriptionNameGenerator().getCompartmentItemName(this.getEClass(), this.getEReference()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME);
        }
        var editSection = this.toolDescriptionService.buildEditSection(
                new StateTransitionToggleExhibitStateToolProvider(true).create(null),
                new StateTransitionToggleExhibitStateToolProvider(false).create(null));
        nd.getPalette().getToolSections().add(editSection);
        return nd;
    }

    @Override
    protected String getSemanticCandidateExpression() {
        if (this.showsExhibitOnly) {
            return AQLUtils.getSelfServiceCallExpression("getAllExhibitedStates");
        } else {
            return AQLUtils.getSelfServiceCallExpression("getAllNonExhibitStates");
        }
    }

}
