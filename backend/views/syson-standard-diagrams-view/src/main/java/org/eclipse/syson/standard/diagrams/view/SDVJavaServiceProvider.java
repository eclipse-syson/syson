/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeService;
import org.eclipse.syson.diagram.common.view.services.ViewLabelService;
import org.eclipse.syson.diagram.common.view.services.ViewNodeService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.services.DiagramMutationAQLService;
import org.eclipse.syson.diagram.services.DiagramQueryAQLService;
import org.eclipse.syson.model.services.ModelMutationAQLService;
import org.eclipse.syson.model.services.ModelQueryAQLService;
import org.eclipse.syson.representation.services.RepresentationMutationAQLService;
import org.eclipse.syson.representation.services.RepresentationQueryAQLService;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.UtilService;
import org.springframework.context.annotation.Configuration;

/**
 * List of all Java services classes used by the {@link SDVDiagramDescriptionProvider}.
 *
 * @author arichard
 */
@Configuration
public class SDVJavaServiceProvider implements IJavaServiceProvider {

    @Override
    public List<Class<?>> getServiceClasses(View view) {
        var descriptions = view.getDescriptions();
        var optGVDescription = descriptions.stream()
                .filter(desc -> SDVDiagramDescriptionProvider.DESCRIPTION_NAME.equals(desc.getName()))
                .findFirst();
        if (optGVDescription.isPresent()) {
            return List.of(DeleteService.class,
                    ViewCreateService.class,
                    ViewEdgeService.class,
                    ViewLabelService.class,
                    ViewToolService.class,
                    ViewNodeService.class,
                    UtilService.class,
                    DiagramMutationAQLService.class,
                    DiagramQueryAQLService.class,
                    ModelMutationAQLService.class,
                    ModelQueryAQLService.class,
                    RepresentationMutationAQLService.class,
                    RepresentationQueryAQLService.class);
        }
        return List.of();
    }
}
