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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
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

    protected final IDescriptionNameGenerator nameGenerator;

    public AbstractCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.eClass = Objects.requireNonNull(eClass);
        this.eReference = Objects.requireNonNull(eReference);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
    }

    /**
     * Implementers should provide the list of {@link NodeDescription} that can be dropped inside this compartment {@link NodeDescription}.
     *
     * @param cache the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that can be dropped inside this compartment.
     */
    protected abstract List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache);

    @Override
    public NodeDescription create() {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().bottomGapExpression("10").build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .labelExpression(this.getCompartmentLabel())
                .name(this.nameGenerator.getCompartmentName(this.eClass, this.eReference))
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createCompartmentNodeStyle())
                .userResizable(false)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optCompartmentNodeDescription = cache.getNodeDescription(this.nameGenerator.getCompartmentName(this.eClass, this.eReference));
        NodeDescription nodeDescription = optCompartmentNodeDescription.get();

        cache.getNodeDescription(this.nameGenerator.getCompartmentItemName(this.eClass, this.eReference))
            .ifPresent(node -> nodeDescription.getChildrenDescriptions().add(node));

        nodeDescription.setPalette(this.createCompartmentPalette(cache));
    }

    /**
     * Implementers might override to specify a custom label for the compartment that is used instead of the default label.<br>
     * Default implementation returns {@code null}.
     * @return a custom label for the compartment
     */
    protected String getCustomCompartmentLabel() {
        // default implementation does nothing
        return null;
    }

    private String getCompartmentLabel() {
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
        } else {
            defaultName = eType.getName();
        }
        return defaultName;
    }

    private NodeStyleDescription createCompartmentNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_COMPARTMENT_BACKGROUND_COLOR))
                .displayHeaderSeparator(false)
                .fontSize(12)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(false)
                .withHeader(true)
                .build();
    }

    private NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        CompartmentNodeToolProvider compartmentNodeToolProvider = new CompartmentNodeToolProvider(this.eReference, this.nameGenerator);

        return this.diagramBuilderHelper.newNodePalette()
                .dropNodeTool(this.createCompartmentDropFromDiagramTool(cache))
                .nodeTools(compartmentNodeToolProvider.create(cache))
                .build();
    }

    private DropNodeTool createCompartmentDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(this.getDroppableNodes(cache).toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }
}
