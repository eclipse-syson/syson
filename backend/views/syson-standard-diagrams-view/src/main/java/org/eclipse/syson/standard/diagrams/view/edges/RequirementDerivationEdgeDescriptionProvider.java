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
import org.eclipse.syson.model.services.aql.ModelQueryAQLService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.metamodel.helper.LabelConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to describe a requirement derivation edge between two
 * {@link org.eclipse.syson.sysml.RequirementUsage}.
 * <p>
 * SysML v2 has no dedicated metaclass for requirement derivation: a derivation is a
 * {@link org.eclipse.syson.sysml.ConnectionUsage} annotated with the {@code #derivation} metadata or typed by the
 * {@code DerivationConnections::Derivation} connection definition. The edge goes from the derived requirement to the
 * original one, which is the direction the derivation is read in: "this requirement is derived from that one".
 * </p>
 *
 * @author kabayama
 */
public class RequirementDerivationEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    /**
     * The type used to name the {@link EdgeDescription}, it must not collide with the name of the generic
     * {@link ConnectionUsageEdgeDescriptionProvider} edge which uses the same domain type.
     */
    public static final String EDGE_TYPE = "RequirementDerivation";

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public RequirementDerivationEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getConnectionUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .centerLabelExpression(LabelConstants.OPEN_QUOTE + LabelConstants.DERIVE + LabelConstants.CLOSE_QUOTE)
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .name(this.getName())
                .preconditionExpression(ServiceMethod.of0(ModelQueryAQLService::isRequirementDerivation).aqlSelf()
                        // Needs this to avoid instantiation on inheriting concept
                        + " and self.oclIsTypeOf(" + domainType + ")")
                .semanticCandidatesExpression(ServiceMethod.of1(UtilService::getAllReachable).aqlSelf(domainType))
                .sourceExpression(ServiceMethod.of0(ModelQueryAQLService::getDerivationDerivedEnd).aqlSelf())
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(ServiceMethod.of0(ModelQueryAQLService::getDerivationOriginalEnd).aqlSelf())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.getName());
        if (optEdgeDescription.isPresent()) {
            var edgeDescription = optEdgeDescription.get();
            diagramDescription.getEdgeDescriptions().add(edgeDescription);

            var requirementNodes = this.getRequirementNodes(cache);
            edgeDescription.getSourceDescriptions().addAll(requirementNodes);
            edgeDescription.getTargetDescriptions().addAll(requirementNodes);

            edgeDescription.setPalette(this.createEdgePalette(cache));
        }
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        // Reconnecting a derivation end is not supported yet.
        return this.viewBuilderHelper.newChangeContext()
                .expression("");
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        // Reconnecting a derivation end is not supported yet.
        return this.viewBuilderHelper.newChangeContext()
                .expression("");
    }

    private String getName() {
        return this.descriptionNameGenerator.getEdgeName(EDGE_TYPE);
    }

    private EdgeStyle createEdgeStyle() {
        // Dashed line to distinguish a derivation from a satisfy edge, which uses the same arrow but a solid line.
        return this.diagramBuilderHelper.newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.DASH)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_ARROW)
                .build();
    }

    private List<NodeDescription> getRequirementNodes(IViewDiagramElementFinder cache) {
        return new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getRequirementUsage());
    }
}
