/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view;

import java.util.List;

import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.emf.IJavaServiceProvider;
import org.eclipse.syson.diagram.general.view.services.GeneralViewCreateService;
import org.eclipse.syson.diagram.general.view.services.GeneralViewDeleteService;
import org.eclipse.syson.diagram.general.view.services.GeneralViewEditService;
import org.eclipse.syson.diagram.general.view.services.GeneralViewLabelService;
import org.eclipse.syson.diagram.general.view.services.GeneralViewToolService;
import org.eclipse.syson.services.UtilService;
import org.springframework.context.annotation.Configuration;

/**
 * List of all Java services classes used by the {@link GeneralViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
@Configuration
public class GeneralViewJavaServiceProvider implements IJavaServiceProvider {

    @Override
    public List<Class<?>> getServiceClasses(View view) {
        var descriptions = view.getDescriptions();
        var optGVDescription = descriptions.stream()
            .filter(desc -> GeneralViewDiagramDescriptionProvider.DESCRIPTION_NAME.equals(desc.getName()))
            .findFirst();
        if (optGVDescription.isPresent()) {
            return List.of(GeneralViewCreateService.class,
                    GeneralViewDeleteService.class,
                    GeneralViewEditService.class,
                    GeneralViewLabelService.class,
                    GeneralViewToolService.class,
                    UtilService.class);
        }
        return List.of();
    }
}
