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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.ArrayList;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.HeaderSeparatorDisplayMode;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelOverflowStrategy;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesFactory;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the imported package node description in all diagrams. This node description is UNSYNCHRONIZED as it
 * can't be referenced by its name and so it can't be exposed.
 *
 * @author Jerome Gout
 */
public class ImportedPackageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    private final ToolDescriptionService toolDescriptionService;

    public ImportedPackageNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
        this.toolDescriptionService = new ToolDescriptionService(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getNamespaceImport());
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(true)
                .defaultHeightExpression("100")
                .defaultWidthExpression("300")
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .name(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport()))
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .style(this.createImportedPackageNodeStyle())
                .userResizable(UserResizableDirection.BOTH)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optImportedPackageNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport()));
        if (optImportedPackageNodeDescription.isPresent()) {
            NodeDescription importedPackageNodeDescription = optImportedPackageNodeDescription.get();

            diagramDescription.getNodeDescriptions().add(importedPackageNodeDescription);

            importedPackageNodeDescription.setPalette(this.createNodePalette(importedPackageNodeDescription, cache));
        }
    }

    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(AQLUtils.getSelfServiceCallExpression("getContainerLabel"))
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .overflowStrategy(LabelOverflowStrategy.WRAP)
                .build();
    }

    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .headerSeparatorDisplayMode(HeaderSeparatorDisplayMode.NEVER)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression(AQLUtils.getSelfServiceCallExpression("showIcon"))
                .withHeader(false)
                .build();
    }

    protected NodeStyleDescription createImportedPackageNodeStyle() {
        var nodeStyleDescription = SysMLCustomnodesFactory.eINSTANCE.createSysMLImportedPackageNodeStyleDescription();
        nodeStyleDescription.setBorderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR));
        nodeStyleDescription.setBorderRadius(0);
        nodeStyleDescription.setBackground(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR));
        return nodeStyleDescription;
    }

    private NodePalette createNodePalette(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("deleteFromModel"));

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .quickAccessTools(this.getDeleteFromDiagramTool())
                .toolSections(this.createToolSections(cache))
                .build();
    }

    private NodeToolSection[] createToolSections(IViewDiagramElementFinder cache) {
        var sections = new ArrayList<NodeToolSection>();

        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        sections.add(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection());

        return sections.toArray(NodeToolSection[]::new);
    }
}
