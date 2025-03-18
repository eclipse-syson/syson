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
package org.eclipse.syson.diagram.common.view.edges;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.ConditionalEdgeStyle;
import org.eclipse.sirius.components.view.diagram.DeleteTool;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the edge description between usages and their nested usages.
 *
 * @author arichard
 */
public abstract class AbstractUsageNestedUsageEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final IDescriptionNameGenerator nameGenerator;

    private final EClass eClass;

    private final EReference eReference;

    private final String edgeName;

    public AbstractUsageNestedUsageEdgeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
        this.eClass = Objects.requireNonNull(eClass);
        this.eReference = Objects.requireNonNull(eReference);
        this.edgeName = this.nameGenerator.getEdgeName("Usage Nested " + this.eClass.getName());
    }

    /**
     * Implementers should provide the list of {@link EClass} that represent the set of possible source usages this
     * edge.
     *
     * @return the list of usage class
     */
    protected abstract List<EClass> getEdgeSources();

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(this.eClass);
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(false)
                .centerLabelExpression(AQLConstants.AQL + "''") // Empty aql expression is necessary to allow direct edit on edge
                .name(this.edgeName)
                .sourceExpression(AQLConstants.AQL_SELF)
                .style(this.createEdgeStyle())
                .conditionalStyles(this.createReferenceConditionalEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(AQLConstants.AQL_SELF + "." + this.eReference.getName())
                .preconditionExpression(AQLConstants.AQL + "not " + org.eclipse.sirius.components.diagrams.description.EdgeDescription.GRAPHICAL_EDGE_SOURCE + ".isAncestorOf("
                        + org.eclipse.sirius.components.diagrams.description.EdgeDescription.GRAPHICAL_EDGE_TARGET + "," + "cache"
                        + ")")
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.edgeName);
        var optUsageNodeDescription = cache.getNodeDescription(this.nameGenerator.getNodeName(this.eClass));
        var sourceNodes = new ArrayList<NodeDescription>();

        this.getEdgeSources().forEach(usage -> {
            cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(sourceNodes::add);
        });

        EdgeDescription edgeDescription = optEdgeDescription.get();
        diagramDescription.getEdgeDescriptions().add(edgeDescription);
        edgeDescription.getSourceDescriptions().addAll(sourceNodes);
        edgeDescription.getTargetDescriptions().add(optUsageNodeDescription.get());

        edgeDescription.setPalette(this.createEdgePalette());
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.FILL_DIAMOND)
                .targetArrowStyle(ArrowStyle.NONE)
                .build();
    }

    private ConditionalEdgeStyle createReferenceConditionalEdgeStyle() {
        return this.diagramBuilderHelper.newConditionalEdgeStyle()
                .condition(AQLConstants.AQL + org.eclipse.sirius.components.diagrams.description.EdgeDescription.SEMANTIC_EDGE_TARGET + ".isReference")
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.DIAMOND)
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
    protected LabelEditTool getEdgeEditTool() {
        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + org.eclipse.sirius.components.diagrams.description.EdgeDescription.SEMANTIC_EDGE_TARGET + ".editMultiplicityRangeCenterLabel(newLabel)");

        return this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(
                        AQLConstants.AQL + org.eclipse.sirius.components.diagrams.description.EdgeDescription.SEMANTIC_EDGE_TARGET + ".getMultiplicityRangeInitialDirectEditLabel()")
                .body(callEditService.build()).build();
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
