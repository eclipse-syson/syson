/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.NodeToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.HeaderSeparatorDisplayMode;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * This is the welcome node description that is presented if and only if the diagram is empty.
 *
 * @author Jerome Gout
 */
public abstract class AbstractEmptyDiagramNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    /**
     * The suffix used to identify empty diagram nodes.
     * <p>
     * This suffix has to be used to define the name of the {@link NodeDescription} created by subclasses. It is used to
     * differentiate empty diagram nodes from other nodes, e.g. when dropping element on them.
     * </p>
     */
    public static final String EMPTY_DIAGRAM_NAME_SUFFIX = "EmptyDiagram";

    private final ToolDescriptionService toolDescriptionService;

    private final IDescriptionNameGenerator nameGenerator;

    public AbstractEmptyDiagramNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
        this.toolDescriptionService = new ToolDescriptionService(nameGenerator);
    }

    /**
     * Implementers should provide the actual name of this {@link NodeDescription}.
     *
     * @return the name of the node.
     */
    protected abstract String getName();

    /**
     * Implementers should provide the list of tool section descriptions used inside this {@link NodeDescription}.
     *
     * @return the list of tool section descriptions
     */
    protected abstract List<ToolSectionDescription> getToolSections();

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement());
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression("476")
                .defaultWidthExpression("1061")
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .name(this.getName())
                .semanticCandidatesExpression("aql:if(editingContext <> null) then self.getDiagramEmptyCandidate(editingContext, diagramContext, previousDiagram) else Sequence{} endif")
                .style(this.createEmptyDiagramNodeStyle())
                .userResizable(UserResizableDirection.BOTH)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getName()).ifPresent(nodeDescription -> {
            diagramDescription.getNodeDescriptions().add(nodeDescription);
            nodeDescription.setPalette(this.createNodePalette(cache));
        });
    }

    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression("")
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .build();
    }

    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .headerSeparatorDisplayMode(HeaderSeparatorDisplayMode.NEVER)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression("aql:false")
                .withHeader(false)
                .build();
    }

    protected NodeStyleDescription createEmptyDiagramNodeStyle() {
        return this.diagramBuilderHelper.newImageNodeStyleDescription()
                .borderColor(this.colorProvider.getColor("transparent"))
                .borderRadius(0)
                .shape("images/add_your_first_element.svg")
                .build();
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newNodePalette()
                .toolSections(this.createToolSections(cache))
                .build();
    }

    private NodeTool createNodeToolFromPackage(NodeDescription nodeDescription, EClass eClass) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance.elementInitializer()");

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression("")
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(createView.build(), changeContextNewInstance.build());

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newOwningMembership")
                .children(createEClassInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getOwningMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newOwningMembership")
                .children(changeContexMembership.build());

        return builder
                .name(this.nameGenerator.getCreationToolName(eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }

    private NodeToolSection[] createToolSections(IViewDiagramElementFinder cache) {
        var sections = new ArrayList<NodeToolSection>();

        this.getToolSections().forEach(sectionTool -> {
            NodeToolSectionBuilder sectionBuilder = this.diagramBuilderHelper.newNodeToolSection()
                    .name(sectionTool.name())
                    .nodeTools(this.createElementsOfToolSection(cache, sectionTool.elements()));
            sections.add(sectionBuilder.build());
        });

        sections.add(this.toolDescriptionService.addElementsNodeToolSection(true));

        return sections.toArray(NodeToolSection[]::new);
    }

    private NodeTool[] createElementsOfToolSection(IViewDiagramElementFinder cache, List<EClass> elements) {
        var nodeTools = new ArrayList<NodeTool>();

        elements.forEach(element -> {
            cache.getNodeDescription(this.nameGenerator.getNodeName(element)).ifPresent(nodeDescription -> {
                nodeTools.add(this.createNodeToolFromPackage(nodeDescription, element));
            });
        });

        nodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));

        return nodeTools.toArray(NodeTool[]::new);
    }
}
