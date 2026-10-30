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

import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.variables.CoreVariables;
import org.eclipse.sirius.components.diagrams.INodeStyle;
import org.eclipse.sirius.components.diagrams.IconLabelNodeStyle;
import org.eclipse.sirius.components.diagrams.ImageNodeStyle;
import org.eclipse.sirius.components.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.components.diagrams.components.NodeComponent;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.renderer.api.INodeStyleCustomizer;
import org.eclipse.sirius.components.representations.Variable;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.sirius.web.application.project.services.api.IProjectEditingContextService;
import org.eclipse.sirius.web.projects.stylecustomizations.domain.services.api.IProjectStyleCustomizationSearchService;
import org.eclipse.syson.application.nodes.SysMLImportedPackageNodeStyle;
import org.eclipse.syson.application.nodes.SysMLNoteNodeStyle;
import org.eclipse.syson.application.nodes.SysMLPackageNodeStyle;
import org.eclipse.syson.application.nodes.SysMLViewFrameNodeStyle;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.services.api.ISDVThemeProvider;
import org.eclipse.syson.standard.diagrams.view.services.api.SDVTheme;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The SysON node style customizer.
 *
 * @author gcoutable
 */
@Service
public class SysONNodeStyleCustomizer implements INodeStyleCustomizer {

    private static final Variable OWNER_DESCRIPTION_NAME_VARIABLE = new Variable("ownerDescriptionName", String.class, false, "Put the description name of a node in the variable manager to let children compartments know in which family they are");

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private final IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    private final ISDVThemeProvider themeProvider;

    private final IProjectEditingContextService projectEditingContextService;

    private final IProjectStyleCustomizationSearchService projectStyleCustomizationSearchService;

    public SysONNodeStyleCustomizer(IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, ISDVThemeProvider themeProvider,
            IProjectEditingContextService projectEditingContextService,
            IProjectStyleCustomizationSearchService projectStyleCustomizationSearchService) {
        this.viewDiagramDescriptionSearchService = Objects.requireNonNull(viewDiagramDescriptionSearchService);
        this.themeProvider = Objects.requireNonNull(themeProvider);
        this.projectEditingContextService = Objects.requireNonNull(projectEditingContextService);
        this.projectStyleCustomizationSearchService = Objects.requireNonNull(projectStyleCustomizationSearchService);
    }

    /**
     * Customizes the node background color, the compartment background color, and the node border color with the theme applying to its family (Structure, Behavior, ...).
     * It does not apply to border nodes, image nodes, or icon label nodes.
     *
     * @param variableManager
     *              The variableManager holding the whole rendering context
     * @param diagramDescription
     *              The diagram description - not used in this customization
     * @param nodeDescription
     *              The description of the node being customized
     * @param nodeStyle
     *              The node style to customize
     * @return The customized node style
     */
    @Override
    @Transactional(readOnly = true)
    public INodeStyle customize(VariableManager variableManager, DiagramDescription diagramDescription, NodeDescription nodeDescription, INodeStyle nodeStyle) {
        if (!this.canCustomize(variableManager, nodeStyle)) {
            return nodeStyle;
        }
        return variableManager.get(CoreVariables.EDITING_CONTEXT.name(), IEditingContext.class)
                .flatMap(editingContext -> this.viewDiagramDescriptionSearchService.findViewNodeDescriptionById(editingContext, nodeDescription.getId()))
                .map(DiagramElementDescription::getName)
                .flatMap(descriptionName -> this.findTheme(variableManager, descriptionName)
                        .map(theme -> this.applyTheme(descriptionName, theme, nodeStyle)))
                .orElse(nodeStyle);
    }

    /**
     * Checks whether the customization applies to the node.
     * It does not apply to border nodes, image nodes, or icon label nodes.
     * Checks the database to determine whether style customization is enabled for the project associated with the editing context.
     */
    private boolean canCustomize(VariableManager variableManager, INodeStyle nodeStyle) {
        var canCustomize = !(variableManager.get(NodeComponent.IS_BORDER_NODE, Boolean.class).orElse(false) || nodeStyle instanceof ImageNodeStyle || nodeStyle instanceof IconLabelNodeStyle);
        if (canCustomize) {
            canCustomize = variableManager.get(CoreVariables.EDITING_CONTEXT.name(), IEditingContext.class)
                    .map(IEditingContext::getId)
                    .flatMap(this.projectEditingContextService::getProjectId)
                    .map(projectId -> this.projectStyleCustomizationSearchService.existsByProjectIdAndStyleCustomizationDescriptionId(AggregateReference.to(projectId), SysONStyleCustomizationDescriptionProvider.SYSON_STYLE_CUSTOMIZATION_DESCRIPTION_ID))
                    .orElse(Boolean.FALSE);
        }

        return canCustomize;
    }

    /**
     * Find the theme applying to the description name.
     * <p>
     *     For a simple node, stores its description name in the variable manager so child compartments can determine their family.
     *     A compartment's background color depends on the family of its containing node.
     *     For instance, the _attributes_ compartment has a different background color depending on whether it is displayed in a {@link org.eclipse.syson.sysml.PartUsage} or in a {@link org.eclipse.syson.sysml.ConstraintUsage}.
     * </p>
     * <p>
     *     If the node is a compartment, returns the theme applying to its graphical ancestor.
     * </p>
     *
     * @param variableManager
     *              The variable manager used to store or retrieve the description name of the graphical ancestor
     * @param descriptionName
     *              The description name used to find the theme
     * @return The theme applying to the description name
     */
    private Optional<SDVTheme> findTheme(VariableManager variableManager, String descriptionName) {
        if (this.descriptionNameGenerator.isCompartmentNodeDescriptionName(descriptionName)) {
            return this.themeProvider.findTheme(descriptionName)
                    .flatMap(theme -> this.themeProvider.findTheme(variableManager.get(OWNER_DESCRIPTION_NAME_VARIABLE.name(), String.class).orElse(descriptionName)));
        }
        if (!this.descriptionNameGenerator.isCompartmentItemNodeDescriptionName(descriptionName)) {
            variableManager.put(OWNER_DESCRIPTION_NAME_VARIABLE.name(), descriptionName);
        }
        return this.themeProvider.findTheme(descriptionName);
    }

    /**
     * Apply the theme to the node style.
     *
     * @param descriptionName
     *              The description name used to know if the node being customized is a compartment or not
     * @param theme
     *              The theme to apply
     * @param nodeStyle
     *              The node style to customize
     * @return The customized node style
     */
    private INodeStyle applyTheme(String descriptionName, SDVTheme theme, INodeStyle nodeStyle) {
        String backgroundColor = theme.nodeBackgroundColor();
        if (this.descriptionNameGenerator.isCompartmentNodeDescriptionName(descriptionName)) {
            backgroundColor = theme.compartmentBackgroundColor();
        }
        return this.copyWithColors(nodeStyle, backgroundColor, theme.borderColor());
    }

    /**
     * Apply the background color and the border color from the theme to the node style.
     *
     * @param nodeStyle
     *              The node style to customize
     * @param backgroundColor
     *              The background color to apply
     * @param borderColor
     *              The border color to apply
     * @return The customized node style
     */
    private INodeStyle copyWithColors(INodeStyle nodeStyle, String backgroundColor, String borderColor) {
        return switch (nodeStyle) {
            case RectangularNodeStyle style -> RectangularNodeStyle.newRectangularNodeStyle(style)
                    .background(backgroundColor).borderColor(borderColor).build();
            case SysMLPackageNodeStyle style -> SysMLPackageNodeStyle.newSysMLPackageNodeStyle(style)
                    .background(backgroundColor).borderColor(borderColor).build();
            case SysMLImportedPackageNodeStyle style -> SysMLImportedPackageNodeStyle.newSysMLImportedPackageNodeStyle(style)
                    .background(backgroundColor).borderColor(borderColor).build();
            case SysMLNoteNodeStyle style -> SysMLNoteNodeStyle.newSysMLNoteNodeStyle(style)
                    .background(backgroundColor).borderColor(borderColor).build();
            case SysMLViewFrameNodeStyle style -> SysMLViewFrameNodeStyle.newSysMLViewFrameNodeStyle(style)
                    .background(backgroundColor).borderColor(borderColor).build();
            default -> nodeStyle;
        };
    }
}
