/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the Annotation edge description.
 *
 * @author jmallet
 */
public class AnnotationEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public AnnotationEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(nameGenerator);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getAnnotation());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(false)
                .name(this.getName())
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(AQLConstants.AQL_SELF + ".annotatedElement")
                .centerLabelExpression("")
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.getName());
        EdgeDescription edgeDescription = optEdgeDescription.get();
        diagramDescription.getEdgeDescriptions().add(edgeDescription);
        String commentNodeName = this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getComment());
        String documentationNodeName = this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getDocumentation());
        String textualRepresentationNodeName = this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getTextualRepresentation());

        var sourceNodes = new ArrayList<NodeDescription>();
        cache.getNodeDescription(commentNodeName).ifPresent(sourceNodes::add);
        cache.getNodeDescription(documentationNodeName).ifPresent(sourceNodes::add);
        cache.getNodeDescription(textualRepresentationNodeName).ifPresent(sourceNodes::add);

        var targetNodes = new ArrayList<NodeDescription>();
        targetNodes.addAll(
                cache.getNodeDescriptions().stream().filter(nodeDescription -> !commentNodeName.equals(nodeDescription.getName()) && !documentationNodeName.equals(nodeDescription.getName()))
                        .filter(nodeDescription -> !nodeDescription.getName().contains("Compartment"))
                        .toList());

        edgeDescription.getSourceDescriptions().addAll(sourceNodes);
        edgeDescription.getTargetDescriptions().addAll(targetNodes);

        edgeDescription.setPalette(this.createEdgePalette());
    }

    @Override
    protected boolean isDeletable() {
        return false;
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression("");
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(AQLConstants.EDGE_SEMANTIC_ELEMENT, "reconnnectTargetAnnotatedEdge", AQLConstants.SEMANTIC_RECONNECTION_TARGET));
    }

    /**
     * Implementers should provide the actual name of this {@link EdgeDescription}.
     *
     * @return the name of the edge description
     */
    private String getName() {
        return this.descriptionNameGenerator.getEdgeName("Annotation");
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.DASH)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.NONE)
                .build();
    }
}
