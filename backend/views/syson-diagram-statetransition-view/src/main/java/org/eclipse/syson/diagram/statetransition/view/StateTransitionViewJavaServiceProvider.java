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
package org.eclipse.syson.diagram.statetransition.view;

import java.util.List;

import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.emf.IJavaServiceProvider;
import org.eclipse.syson.diagram.common.view.services.ViewLabelService;
import org.eclipse.syson.diagram.statetransition.view.services.StateTransitionViewCreateService;
import org.eclipse.syson.diagram.statetransition.view.services.StateTransitionViewEdgeService;
import org.eclipse.syson.diagram.statetransition.view.services.StateTransitionViewToolService;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.UtilService;
import org.springframework.context.annotation.Configuration;

/**
 * List of all Java services classes used by the {@link StateTransitionViewDiagramDescriptionProvider}.
 *
 * @author adieumegard
 */
@Configuration
public class StateTransitionViewJavaServiceProvider implements IJavaServiceProvider {

    @Override
    public List<Class<?>> getServiceClasses(View view) {
        var descriptions = view.getDescriptions();
        var optDescription = descriptions.stream()
            .filter(desc -> StateTransitionViewDiagramDescriptionProvider.DESCRIPTION_NAME.equals(desc.getName()))
            .findFirst();
        if (optDescription.isPresent()) {
            return List.of(StateTransitionViewCreateService.class,
                    DeleteService.class,
                    StateTransitionViewEdgeService.class,
                    ViewLabelService.class,
                    UtilService.class,
                    StateTransitionViewToolService.class);
        }
        return List.of();
    }
}
