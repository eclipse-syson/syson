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
package org.eclipse.syson.diagram.common.view.edges;

import java.util.Objects;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link BindingConnectorAsUsage} edge description.
 *
 * @author adieumegard
 */
public abstract class AbstractTransitionEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public static final String NAME = "Edge TransitionUsage";

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public AbstractTransitionEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getTransitionUsage());
        return this.getDiagramBuilderHelper().newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression(AQLUtils.getSelfServiceCallExpression("getTransitionLabel"))
                .name(this.getEdgeDescriptionName())
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .sourceNodesExpression(AQLConstants.AQL_SELF + ".source")
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression(AQLConstants.AQL_SELF + ".target")
                .build();
    }

    protected String getEdgeDescriptionName() {
        return this.getDescriptionNameGenerator().getDiagramPrefix() + NAME;
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        return this.getViewBuilderHelper().newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(AQLConstants.EDGE_SEMANTIC_ELEMENT, "reconnectSourceTransitionEdge", AQLConstants.SEMANTIC_RECONNECTION_TARGET));
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        return this.getViewBuilderHelper().newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(AQLConstants.EDGE_SEMANTIC_ELEMENT, "reconnectTargetTransitionEdge", AQLConstants.SEMANTIC_RECONNECTION_TARGET));
    }

    @Override
    protected String getSourceReconnectToolPreconditionExpression() {
        return AQLUtils.getServiceCallExpression(AQLConstants.SEMANTIC_OTHER_END, "checkTransitionEdgeTarget", AQLConstants.SEMANTIC_RECONNECTION_TARGET);
    }

    @Override
    protected String getTargetReconnectToolPreconditionExpression() {
        return this.getSourceReconnectToolPreconditionExpression();
    }

    /**
     * Label edit tool setting the attributes of an {@linkplain EOperation} based on the provided input. Will allow to
     * set the {@linkplain EOperation} dataType, parameters, and cardinality values. <br/>
     * Relies on {@code org.eclipse.emf.ecoretools.design.service.DesignServices.performEdit(EAttribute, String)} or
     * {@code org.eclipse.emf.ecoretools.design.service.DesignServices.performEdit(EOperation, String)}.
     *
     * @return The LabelEditTool for the TransitionEdge
     */
    @Override
    protected LabelEditTool getEdgeEditTool() {
        var changeContext = this.getViewBuilderHelper().newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("directEditTransitionEdgeLabel", "newLabel"))
                .build();

        return this.getDiagramBuilderHelper().newLabelEditTool()
                .name("Edit TransitionUsage Label")
                .body(changeContext)
                .build();
    }

    private EdgeStyle createEdgeStyle() {
        return this.getDiagramBuilderHelper().newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_ARROW)
                .build();
    }

    protected ViewBuilders getViewBuilderHelper() {
        return this.viewBuilderHelper;
    }

    protected DiagramBuilders getDiagramBuilderHelper() {
        return this.diagramBuilderHelper;
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }
}
