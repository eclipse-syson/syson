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
package org.eclipse.syson.standard.diagrams.view.services;

import java.util.List;

import org.eclipse.sirius.web.projects.stylecustomizations.application.services.StyleCustomizationDescription;
import org.eclipse.sirius.web.projects.stylecustomizations.application.services.api.IStyleCustomizationDescriptionProvider;
import org.springframework.stereotype.Service;

/**
 * Provides style customization descriptions for SysON.
 *
 * @author gcoutable
 */
@Service
public class SysONStyleCustomizationDescriptionProvider implements IStyleCustomizationDescriptionProvider {

    public static final String SYSON_STYLE_CUSTOMIZATION_DESCRIPTION_ID = "syson-standard-diagrams-theme-style-customization";

    @Override
    public List<StyleCustomizationDescription> getStyleCustomizationDescriptions(String projectId) {
        return List.of(new StyleCustomizationDescription(SYSON_STYLE_CUSTOMIZATION_DESCRIPTION_ID, "SysON standard diagrams theme", "Colors standard diagram elements according to their SysML element family (Structure, Behavior, ...)"));
    }
}
