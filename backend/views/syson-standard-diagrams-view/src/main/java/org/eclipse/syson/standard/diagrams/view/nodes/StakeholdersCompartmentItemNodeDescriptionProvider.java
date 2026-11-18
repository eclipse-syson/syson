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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Lists the stakeholders owned by the compartment's requirement. Inherited stakeholders have a separate
 * node description that supplies the inheritance marker and the restricted palette.
 *
 * @author Arthur Daussy
 */
public class StakeholdersCompartmentItemNodeDescriptionProvider extends CompartmentItemNodeDescriptionProvider {

    public StakeholdersCompartmentItemNodeDescriptionProvider(EClass eClass, EReference eReference,
            IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getSemanticCandidateExpression() {
        return "aql:self.ownedMembership->filter(sysml::StakeholderMembership).ownedStakeholderParameter";
    }
}
