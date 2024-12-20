/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.HeaderSeparatorDisplayMode;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the Compartment node description.
 *
 * @author arichard
 */
public abstract class AbstractCompartmentNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    protected final EClass eClass;

    protected final EReference eReference;

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    protected final ToolDescriptionService toolDescriptionService;

    public AbstractCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.eClass = Objects.requireNonNull(eClass);
        this.eReference = Objects.requireNonNull(eReference);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
        this.toolDescriptionService = new ToolDescriptionService(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().bottomGapExpression("10").build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .insideLabel(this.createInsideLabelDescription())
                .isHiddenByDefaultExpression(this.isHiddenByDefaultExpression())
                .name(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference))
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createCompartmentNodeStyle())
                .userResizable(UserResizableDirection.NONE)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference)).ifPresent(nodeDescription -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getInheritedCompartmentItemName(this.eClass, this.eReference))
                    .ifPresent(node -> nodeDescription.getChildrenDescriptions().add(node));
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference))
                    .ifPresent(node -> nodeDescription.getChildrenDescriptions().add(node));
            nodeDescription.setPalette(this.createCompartmentPalette(cache));
        });
    }

    /**
     * Implementers should provide the list of {@link NodeDescription} that can be dropped inside this compartment
     * {@link NodeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that can be dropped inside this compartment.
     */
    protected abstract List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache);

    /**
     * Returns the Compartment Node tool providers used to create a new item inside this compartment. In most cases,
     * this will return only one tool, but sometime can return several tools (e.g.: Ports with a tool for each
     * direction)
     *
     * @return the {@link INodeToolProviders} that handles the item creation inside this compartment.
     */
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        List<INodeToolProvider> creationToolProviders = new ArrayList<>();
        if (!Objects.equals(this.eReference, SysmlPackage.eINSTANCE.getElement_Documentation())) {
            creationToolProviders.add(new CompartmentNodeToolProvider(this.eReference, this.getDescriptionNameGenerator()));
        }
        return creationToolProviders;
    }

    /**
     * Provide the default state of the compartment (hidden/revealed).
     *
     * @return An AQL expression returning <code>true</code> if the compartment should be hidden by default,
     *         <code>false</code> otherwise.
     */
    protected String isHiddenByDefaultExpression() {
        return AQLUtils.getSelfServiceCallExpression("isHiddenByDefault", "'" + this.eReference.getName() + "'");
    }

    /**
     * Returns the AQL expression evaluated to drop an element from the diagram inside this compartment.
     *
     * @return
     */
    protected String getDropElementFromDiagramExpression() {
        return AQLUtils.getServiceCallExpression("droppedElement", "dropElementFromDiagram",
                List.of("droppedNode", "targetElement", "targetNode", IEditingContext.EDITING_CONTEXT, IDiagramContext.DIAGRAM_CONTEXT, "convertedNodes"));
    }

    /**
     * Implementers might override to specify a custom label for the compartment that is used instead of the default
     * label.<br>
     * Default implementation returns {@code null}.
     *
     * @return a custom label for the compartment
     */
    protected String getCustomCompartmentLabel() {
        // default implementation does nothing
        return null;
    }

    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(this.getCompartmentLabel())
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .build();
    }

    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .headerSeparatorDisplayMode(HeaderSeparatorDisplayMode.NEVER)
                .fontSize(12)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression("aql:false")
                .withHeader(true)
                .build();
    }

    protected NodeStyleDescription createCompartmentNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .background(this.colorProvider.getColor(ViewConstants.DEFAULT_COMPARTMENT_BACKGROUND_COLOR))
                .build();
    }

    protected NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        var palette = this.diagramBuilderHelper.newNodePalette()
                .dropNodeTool(this.createCompartmentDropFromDiagramTool(cache));

        var toolSections = this.toolDescriptionService.createDefaultNodeToolSections();

        var itemCreationToolProviders = this.getItemCreationToolProviders();
        for (var iNodeToolProvider : itemCreationToolProviders) {
            var nodeTool = iNodeToolProvider.create(cache);
            var eType = this.eReference.getEType();
            var toolSectionName = this.toolDescriptionService.getToolSectionName(eType);
            this.toolDescriptionService.addNodeTool(toolSections, toolSectionName, nodeTool);
        }

        toolSections.add(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection());
        this.toolDescriptionService.removeEmptyNodeToolSections(toolSections);

        var toolsWithoutSection = new ArrayList<NodeTool>();
        toolsWithoutSection.addAll(this.getToolsWithoutSection(cache));

        return palette.toolSections(toolSections.toArray(NodeToolSection[]::new))
                .nodeTools(toolsWithoutSection.toArray(NodeTool[]::new))
                .build();
    }

    protected List<NodeTool> getToolsWithoutSection(IViewDiagramElementFinder cache) {
        if (Objects.equals(this.eReference, SysmlPackage.eINSTANCE.getElement_Documentation())) {
            var documentationNodeTool = new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(cache);
            return List.of(documentationNodeTool);
        }
        return List.of();
    }

    protected DropNodeTool createCompartmentDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression(this.getDropElementFromDiagramExpression());

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(this.getDroppableNodes(cache).toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    protected String getCompartmentLabel() {
        String customLabel = this.getCustomCompartmentLabel();
        if (customLabel != null) {
            return customLabel;
        }
        String defaultName = "";
        EClassifier eType = this.eReference.getEType();
        if (eType instanceof EClass eTypeClass && SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eTypeClass)) {
            char[] charArray = eTypeClass.getName().toCharArray();
            charArray[0] = Character.toLowerCase(charArray[0]);
            defaultName = new String(charArray);
            if (defaultName.endsWith("Usage")) {
                defaultName = defaultName.substring(0, defaultName.length() - 5) + "s";
            }
        } else if (SysmlPackage.eINSTANCE.getDocumentation().equals(eType)) {
            defaultName = "doc";
        } else {
            defaultName = eType.getName();
        }
        return defaultName;
    }
}
