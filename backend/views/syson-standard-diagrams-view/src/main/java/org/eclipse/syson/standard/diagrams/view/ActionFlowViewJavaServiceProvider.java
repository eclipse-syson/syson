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
package org.eclipse.syson.standard.diagrams.view;

import java.util.List;

import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.emf.IJavaServiceProvider;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.standard.diagrams.view.services.ActionFlowViewCreateService;
import org.springframework.context.annotation.Configuration;

/**
 * List of all Java services classes used by the {@link ActionFlowViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
@Configuration
public class ActionFlowViewJavaServiceProvider implements IJavaServiceProvider {

    @Override
    public List<Class<?>> getServiceClasses(View view) {
        var descriptions = view.getDescriptions();
        var optGVDescription = descriptions.stream()
                .filter(desc -> ActionFlowViewDiagramDescriptionProvider.DESCRIPTION_NAME.equals(desc.getName()))
                .findFirst();
        if (optGVDescription.isPresent()) {
            return List.of(ActionFlowViewCreateService.class, UtilService.class);
        }
        return List.of();
    }
}
