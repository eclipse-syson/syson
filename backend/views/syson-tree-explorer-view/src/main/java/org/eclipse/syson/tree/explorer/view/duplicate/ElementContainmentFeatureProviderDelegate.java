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
package org.eclipse.syson.tree.explorer.view.duplicate;

import java.util.List;

import org.eclipse.sirius.web.application.views.explorer.dto.ContainmentFeature;
import org.eclipse.sirius.web.application.views.explorer.services.api.IContainmentFeatureProviderDelegate;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.util.GetIntermediateContainerCreationSwitch;
import org.springframework.stereotype.Service;

/**
 * Provides the containment features to be used for SysML Elements.
 *
 * @author Arthur Daussy
 */
@Service
public class ElementContainmentFeatureProviderDelegate implements IContainmentFeatureProviderDelegate {

    private final SysMLContainmentMembershipLabelSwitch labelSwitch = new SysMLContainmentMembershipLabelSwitch();

    @Override
    public boolean canHandle(Object container, Object child) {
        return this.isNonMembershipElement(child) && container instanceof Element;
    }

    @Override
    public List<ContainmentFeature> getContainmentFeatures(Object container, Object child) {
        if (container instanceof Relationship) {
            // We want to forbid the duplication under a Relationship.
            return List.of();
        }

        return new GetIntermediateContainerCreationSwitch((Element) container).doSwitch((Element) child)
                .map(eClass -> new ContainmentFeature("membership:" + eClass.getName(), this.labelSwitch.doSwitch(eClass)))
                .map(List::of)
                .orElse(List.of());
    }

    private boolean isNonMembershipElement(Object target) {
        return !(target instanceof Membership) && target instanceof Element;
    }
}
