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
package org.eclipse.syson.application.configuration;

import java.util.List;

import org.eclipse.sirius.web.services.api.projects.IProjectTemplateProvider;
import org.eclipse.sirius.web.services.api.projects.ProjectTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * Provides SysMLv2-specific project templates.
 *
 * @author arichard
 */
@Configuration
public class SysMLv2ProjectTemplatesProvider implements IProjectTemplateProvider {

    public static final String SYSMLV2_TEMPLATE_ID = "sysmlv2-template";

    public static final String BATMOBILE_TEMPLATE_ID = "batmobile-template";

    @Override
    public List<ProjectTemplate> getProjectTemplates() {
        var sysmlv2Template = ProjectTemplate.newProjectTemplate(SYSMLV2_TEMPLATE_ID)
                .label("SysMLv2")
                .imageURL("/images/sysmlv2-logo.png")
                .natures(List.of())
                .build();
        var batmobileTemplate = ProjectTemplate.newProjectTemplate(BATMOBILE_TEMPLATE_ID)
                .label("Batmobile")
                .imageURL("/images/sysmlv2-logo.png")
                .natures(List.of())
                .build();
        return List.of(sysmlv2Template, batmobileTemplate);
    }

}
