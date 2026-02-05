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
package org.eclipse.syson.standard.diagrams.view.edges;

import java.util.List;
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
import org.eclipse.syson.diagram.common.view.DescriptionFinder;
import org.eclipse.syson.diagram.common.view.edges.AbstractEdgeDescriptionProvider;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Describe an {@link org.eclipse.syson.sysml.SatisfyRequirementUsage} edge between a {@link Feature} and a
 * {@link RequirementUsage}.
 *
 * @author arichard
 */
public class SatisfyRequirementEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public SatisfyRequirementEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression(ServiceMethod.of0(DiagramQueryAQLService::getSatisfyLabel).aqlSelf())
                .name(this.getName())
                .semanticCandidatesExpression(ServiceMethod.of1(UtilService::getAllReachable).aqlSelf(domainType))
                .sourceExpression("aql:if self.satisfyingFeature <> null then self.satisfyingFeature else self.owner endif")
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getSatisfyRequirementUsage_SatisfiedRequirement().getName())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.getName());
        if (optEdgeDescription.isPresent()) {
            var edgeDescription = optEdgeDescription.get();
            diagramDescription.getEdgeDescriptions().add(edgeDescription);

            var sourceNodes = this.getSourceNodes(cache);
            var targetNodes = this.getTargetNodes(cache);
            edgeDescription.getSourceDescriptions().addAll(sourceNodes);
            edgeDescription.getTargetDescriptions().addAll(targetNodes);

            edgeDescription.setPalette(this.createEdgePalette(cache));
        }
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramMutationAQLService::reconnectSatisfyRequirementSource).aql(
                        AQLConstants.EDGE_SEMANTIC_ELEMENT,
                        AQLConstants.SEMANTIC_RECONNECTION_TARGET));
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramMutationAQLService::reconnectSatisfyRequirementTarget).aql(
                        AQLConstants.EDGE_SEMANTIC_ELEMENT,
                        AQLConstants.SEMANTIC_RECONNECTION_TARGET));
    }

    private String getName() {
        return this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage());
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_ARROW)
                .build();
    }

    private List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getFeature());
    }

    private List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getRequirementUsage());
    }
}
