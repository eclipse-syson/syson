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
package org.eclipse.syson.diagram.common.view.edges;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DeleteTool;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the edge description between definitions and their owned usages.
 *
 * @author arichard
 */
public abstract class AbstractDefinitionOwnedUsageEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    private final EClass eClass;

    private final EReference eReference;

    public AbstractDefinitionOwnedUsageEdgeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(nameGenerator);
        this.eClass = Objects.requireNonNull(eClass);
        this.eReference = Objects.requireNonNull(eReference);
    }

    /**
     * Implementers should provide the list of {@link EClass} that represent the set of possible source definitions of
     * this edge.
     *
     * @return the list of definition class
     */
    protected abstract List<EClass> getEdgeSources();

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(this.eClass);
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(false)
                .centerLabelExpression(AQLConstants.AQL + org.eclipse.sirius.components.diagrams.description.EdgeDescription.SEMANTIC_EDGE_TARGET + ".getMultiplicityLabel()")
                .name(this.descriptionNameGenerator.getEdgeName("Definition Owned " + this.eClass.getName()))
                .sourceNodesExpression(AQLConstants.AQL_SELF)
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression(AQLConstants.AQL_SELF + "." + this.eReference.getName())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.descriptionNameGenerator.getEdgeName("Definition Owned " + this.eClass.getName()));
        var optUsageNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(this.eClass));
        var sourceNodes = new ArrayList<NodeDescription>();

        this.getEdgeSources().forEach(definition -> {
            cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition)).ifPresent(sourceNodes::add);
        });

        EdgeDescription edgeDescription = optEdgeDescription.get();
        diagramDescription.getEdgeDescriptions().add(edgeDescription);
        edgeDescription.getSourceNodeDescriptions().addAll(sourceNodes);
        edgeDescription.getTargetNodeDescriptions().add(optUsageNodeDescription.get());

        edgeDescription.setPalette(this.createEdgePalette());
    }

    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression("aql:self.getContainerLabel()")
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .build();
    }

    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .displayHeaderSeparator(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .withHeader(true)
                .build();
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.FILL_DIAMOND)
                .targetArrowStyle(ArrowStyle.NONE)
                .build();
    }

    @Override
    protected boolean isDeletable() {
        return true;
    }

    @Override
    protected DeleteTool getEdgeDeleteTool() {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression("aql:semanticEdgeTarget.moveToClosestContainingPackage()");

        return this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build())
                .build();
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        var params = List.of(AQLConstants.SEMANTIC_RECONNECTION_TARGET, AQLConstants.SEMANTIC_OTHER_END);
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT + ".reconnnectSourceCompositionEdge(" + String.join(",", params) + ")");
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        var params = List.of(AQLConstants.SEMANTIC_RECONNECTION_SOURCE, AQLConstants.SEMANTIC_RECONNECTION_TARGET, AQLConstants.SEMANTIC_OTHER_END);
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT + ".reconnnectTargetCompositionEdge(" + String.join(",", params) + ")");
    }
}
