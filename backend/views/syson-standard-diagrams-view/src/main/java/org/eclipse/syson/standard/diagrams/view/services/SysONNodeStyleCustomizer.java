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
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.application.nodes.SysMLImportedPackageNodeStyle;
import org.eclipse.syson.application.nodes.SysMLNoteNodeStyle;
import org.eclipse.syson.application.nodes.SysMLPackageNodeStyle;
import org.eclipse.syson.application.nodes.SysMLViewFrameNodeStyle;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.services.api.ISDVSemanticPaletteProvider;
import org.eclipse.syson.standard.diagrams.view.services.api.SDVSemanticPalette;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The SysON node style customizer.
 *
 * @author gcoutable
 */
@Service
public class SysONNodeStyleCustomizer implements INodeStyleCustomizer {

    private static final String OWNER_DESCRIPTION_NAME = "syson.palette.ownerDescriptionName";

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private final boolean nodeCustomizationEnabled;

    private final IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    private final ISDVSemanticPaletteProvider semanticPaletteProvider;

    public SysONNodeStyleCustomizer(@Value("${sirius.web.style.customization.enabled:false}") boolean nodeCustomizationEnabled,
            IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, ISDVSemanticPaletteProvider semanticPaletteProvider) {
        this.nodeCustomizationEnabled = nodeCustomizationEnabled;
        this.viewDiagramDescriptionSearchService = Objects.requireNonNull(viewDiagramDescriptionSearchService);
        this.semanticPaletteProvider = Objects.requireNonNull(semanticPaletteProvider);
    }

    @Override
    public INodeStyle customize(VariableManager variableManager, DiagramDescription diagramDescription, NodeDescription nodeDescription, INodeStyle nodeStyle) {
        if (!this.nodeCustomizationEnabled || variableManager.get(NodeComponent.IS_BORDER_NODE, Boolean.class).orElse(false) || nodeStyle instanceof ImageNodeStyle || nodeStyle instanceof IconLabelNodeStyle) {
            return nodeStyle;
        }
        return variableManager.get(CoreVariables.EDITING_CONTEXT.name(), IEditingContext.class)
                .flatMap(editingContext -> this.viewDiagramDescriptionSearchService.findViewNodeDescriptionById(editingContext, nodeDescription.getId()))
                .map(DiagramElementDescription::getName)
                .flatMap(descriptionName -> this.findPalette(variableManager, descriptionName)
                        .map(palette -> this.applyPalette(descriptionName, palette, nodeStyle)))
                .orElse(nodeStyle);
    }

    private Optional<SDVSemanticPalette> findPalette(VariableManager variableManager, String descriptionName) {
        if (this.descriptionNameGenerator.isCompartmentNodeDescriptionName(descriptionName)) {
            // Reused compartment descriptions must follow their actual graphical owner.
            return this.semanticPaletteProvider.findPalette(descriptionName)
                    .flatMap(palette -> this.semanticPaletteProvider.findPalette(variableManager.get(OWNER_DESCRIPTION_NAME, String.class).orElse(descriptionName)));
        }
        if (!this.descriptionNameGenerator.isCompartmentItemNodeDescriptionName(descriptionName)) {
            variableManager.put(OWNER_DESCRIPTION_NAME, descriptionName);
        }
        return this.semanticPaletteProvider.findPalette(descriptionName);
    }

    private INodeStyle applyPalette(String descriptionName, SDVSemanticPalette palette, INodeStyle nodeStyle) {
        String background = palette.headerBackground();
        if (this.descriptionNameGenerator.isCompartmentNodeDescriptionName(descriptionName)) {
            background = palette.compartmentBackground();
        }
        return this.copyWithColors(nodeStyle, background, palette.borderColor());
    }

    private INodeStyle copyWithColors(INodeStyle nodeStyle, String background, String borderColor) {
        return switch (nodeStyle) {
            case RectangularNodeStyle style -> RectangularNodeStyle.newRectangularNodeStyle(style)
                    .background(background).borderColor(borderColor).build();
            case SysMLPackageNodeStyle style -> SysMLPackageNodeStyle.newSysMLPackageNodeStyle(style)
                    .background(background).borderColor(borderColor).build();
            case SysMLImportedPackageNodeStyle style -> SysMLImportedPackageNodeStyle.newSysMLImportedPackageNodeStyle(style)
                    .background(background).borderColor(borderColor).build();
            case SysMLNoteNodeStyle style -> SysMLNoteNodeStyle.newSysMLNoteNodeStyle(style)
                    .background(background).borderColor(borderColor).build();
            case SysMLViewFrameNodeStyle style -> SysMLViewFrameNodeStyle.newSysMLViewFrameNodeStyle(style)
                    .background(background).borderColor(borderColor).build();
            default -> nodeStyle;
        };
    }
}
