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
package org.eclipse.syson.application.sysmlv2;

import java.util.List;

import org.eclipse.sirius.web.application.project.services.api.IProjectTemplateProvider;
import org.eclipse.sirius.web.application.project.services.api.ProjectTemplate;
import org.springframework.stereotype.Service;

/**
 * Provides SysMLv2-specific project templates.
 *
 * @author arichard
 */
@Service
public class SysMLv2ProjectTemplatesProvider implements IProjectTemplateProvider {


    public static final String SYSMLV2_LIBRARY_TEMPLATE_ID = "sysmlv2-library-template";

    public static final String SYSMLV2_TEMPLATE_ID = "sysmlv2-template";

    public static final String BATMOBILE_TEMPLATE_ID = "batmobile-template";

    public static final String SYSMLV2_LIBRARY_TEMPLATE_NAME = "SysMLv2-Library";

    public static final String SYSMLV2_TEMPLATE_NAME = "SysMLv2";

    public static final String BATMOBILE_TEMPLATE_NAME = "Batmobile";

    @Override
    public List<ProjectTemplate> getProjectTemplates() {
        var sysmlv2LibraryTemplate = new ProjectTemplate(SYSMLV2_LIBRARY_TEMPLATE_ID, SYSMLV2_LIBRARY_TEMPLATE_NAME, "/images/sysmlv2-logo.png", List.of());
        var sysmlv2Template = new ProjectTemplate(SYSMLV2_TEMPLATE_ID, SYSMLV2_TEMPLATE_NAME, "/images/sysmlv2-logo.png", List.of());
        var batmobileTemplate = new ProjectTemplate(BATMOBILE_TEMPLATE_ID, BATMOBILE_TEMPLATE_NAME, "/images/sysmlv2-logo.png", List.of());
        return List.of(sysmlv2LibraryTemplate, sysmlv2Template, batmobileTemplate);
    }

}
