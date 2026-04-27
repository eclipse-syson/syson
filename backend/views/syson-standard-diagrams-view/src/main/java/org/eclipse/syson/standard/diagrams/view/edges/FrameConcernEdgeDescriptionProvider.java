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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DeleteTool;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.DescriptionFinder;
import org.eclipse.syson.diagram.common.view.edges.AbstractEdgeDescriptionProvider;
import org.eclipse.syson.model.services.aql.ModelQueryAQLService;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the edge description representing the `frame` relationship between a {@link org.eclipse.syson.sysml.RequirementUsage} and a {@link org.eclipse.syson.sysml.ConcernUsage}.
 *
 * @author gcoutable
 */
public class FrameConcernEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public FrameConcernEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression("");
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression("");
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFramedConcernMembership());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression(LabelConstants.OPEN_QUOTE + LabelConstants.FRAME + LabelConstants.CLOSE_QUOTE)
                .name(this.getName())
                .semanticCandidatesExpression(ServiceMethod.of1(UtilService::getAllReachable).aqlSelf(domainType))
                .sourceExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getRelationship_OwningRelatedElement().getName())
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(ServiceMethod.of0(ModelQueryAQLService::getFramedConcernTarget).aqlSelf())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.getName());
        optEdgeDescription.ifPresent(edgeDescription -> {
            diagramDescription.getEdgeDescriptions().add(edgeDescription);

            var sourceNodes = this.getSourceNodes(cache);
            var targetNodes = this.getTargetNodes(cache);
            edgeDescription.getSourceDescriptions().addAll(sourceNodes);
            edgeDescription.getTargetDescriptions().addAll(targetNodes);

            edgeDescription.setPalette(this.createEdgePalette(cache));
        });

    }

    @Override
    protected DeleteTool getEdgeDeleteTool() {
        // Delete the referenced subsetting only.
        var referenceSubsetting = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".ownedConcern.ownedRelationship->select(rel | rel.oclIsKindOf(sysml::ReferenceSubsetting))->first()")
                .children(this.viewBuilderHelper.newChangeContext()
                        .expression(ServiceMethod.of0(DeleteService::deleteFromModel).aqlSelf())
                        .build()
                );

        return this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(referenceSubsetting.build())
                .build();
    }

    private String getName() {
        return this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getFramedConcernMembership());
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
        List<NodeDescription> sources = new ArrayList<>();
        var descriptionFinder = new DescriptionFinder(this.descriptionNameGenerator);
        sources.addAll(descriptionFinder.getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getRequirementUsage()));
        sources.addAll(descriptionFinder.getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getRequirementDefinition()));
        return sources;
    }

    private List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getConcernUsage());
    }
}
