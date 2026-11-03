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
package org.eclipse.syson.standard.diagrams.view;

import java.util.Objects;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.variables.CoreVariables;
import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.components.representations.IRepresentationRenderVariableCustomizer;
import org.eclipse.sirius.components.representations.Variable;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.web.application.project.services.api.IProjectEditingContextService;
import org.eclipse.sirius.web.projects.stylecustomizations.domain.services.api.IProjectStyleCustomizationSearchService;
import org.eclipse.syson.standard.diagrams.view.services.SDVStyleCustomizationDescriptionProvider;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

/**
 * The renderer variable customizer registering variables for SDV diagrams rendering.
 *
 * @author gcoutable
 */
@Service
public class SDVProjectStyleCustomizationRendererVariableCustomizer implements IRepresentationRenderVariableCustomizer {

    public static final Variable IS_SDV_PROJECT_STYLE_CUSTOMIZATION_ENABLED = new Variable("isSDVProjectStyleCustomizationEnabled", Boolean.class, false, "Whether the standard diagrams project style customization is enabled");

    private final IProjectEditingContextService projectEditingContextService;

    private final IProjectStyleCustomizationSearchService projectStyleCustomizationSearchService;

    public SDVProjectStyleCustomizationRendererVariableCustomizer(IProjectEditingContextService projectEditingContextService, IProjectStyleCustomizationSearchService projectStyleCustomizationSearchService) {
        this.projectEditingContextService = Objects.requireNonNull(projectEditingContextService);
        this.projectStyleCustomizationSearchService = Objects.requireNonNull(projectStyleCustomizationSearchService);
    }

    @Override
    public VariableManager customize(IRepresentationDescription representationDescription, VariableManager variableManager) {
        if (this.isSDVDiagram(representationDescription)) {
            var isSDVProjectStyleCustomizationEnabled = variableManager.get(CoreVariables.EDITING_CONTEXT.name(), IEditingContext.class)
                    .map(IEditingContext::getId)
                    .flatMap(this.projectEditingContextService::getProjectId)
                    .map(projectId -> this.projectStyleCustomizationSearchService.existsByProjectIdAndStyleCustomizationDescriptionId(AggregateReference.to(projectId), SDVStyleCustomizationDescriptionProvider.SYSON_STYLE_CUSTOMIZATION_DESCRIPTION_ID))
                    .orElse(Boolean.FALSE);

            variableManager.put(IS_SDV_PROJECT_STYLE_CUSTOMIZATION_ENABLED.name(), isSDVProjectStyleCustomizationEnabled);
        }

        return variableManager;
    }

    private boolean isSDVDiagram(IRepresentationDescription representationDescription) {
        return SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID.equals(representationDescription.getId());
    }
}
