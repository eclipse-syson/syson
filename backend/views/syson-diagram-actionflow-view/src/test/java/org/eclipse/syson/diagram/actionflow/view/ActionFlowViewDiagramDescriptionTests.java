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
package org.eclipse.syson.diagram.actionflow.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.emf.IJavaServiceProvider;
import org.eclipse.syson.diagram.tests.checkers.AQLExpressionCallsExistingServicesChecker;
import org.eclipse.syson.diagram.tests.checkers.DiagramDescriptionHasDropToolChecker;
import org.eclipse.syson.diagram.tests.checkers.EdgeDescriptionHasDirectEditToolChecker;
import org.eclipse.syson.diagram.tests.checkers.EdgeDescriptionHasReconnectToolChecker;
import org.eclipse.syson.diagram.tests.checkers.NodeDescriptionHasChildrenChecker;
import org.eclipse.syson.diagram.tests.checkers.NodeDescriptionHasDeleteToolChecker;
import org.eclipse.syson.diagram.tests.checkers.NodeDescriptionHasDirectEditToolChecker;
import org.eclipse.syson.diagram.tests.checkers.NodeDescriptionHasDropNodeToolChecker;
import org.eclipse.syson.diagram.tests.checkers.NodeDescriptionHasNameChecker;
import org.eclipse.syson.diagram.tests.checkers.NodeDescriptionIsReusedByChecker;
import org.eclipse.syson.diagram.tests.checkers.NodeDescriptionReusesChecker;
import org.eclipse.syson.diagram.tests.predicates.DiagramPredicates;
import org.eclipse.syson.services.ColorProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Checks the structure of the Action Flow View diagram.
 *
 * @author gdaniel
 */
public class ActionFlowViewDiagramDescriptionTests {

    private List<Class<?>> diagramServices;

    private DiagramDescription diagramDescription;

    private final DiagramPredicates diagramPredicates = new DiagramPredicates("AFV", new AFVDescriptionNameGenerator());

    @BeforeEach
    public void setUp() {
        ViewBuilder viewBuilder = new ViewBuilder();
        View view = viewBuilder.build();
        IColorProvider colorProvider = new ColorProvider(view);
        IRepresentationDescriptionProvider representationDescriptionProvider = new ActionFlowViewDiagramDescriptionProvider();
        this.diagramDescription = (DiagramDescription) representationDescriptionProvider.create(colorProvider);
        view.getDescriptions().add(this.diagramDescription);
        IJavaServiceProvider serviceProvider = new ActionFlowViewJavaServiceProvider();
        this.diagramServices = serviceProvider.getServiceClasses(view);
    }

    @Test
    @DisplayName("Each EdgeDescription has reconnect tools")
    public void eachEdgeHasReconnectTools() {
        List<EdgeDescription> edgeDescriptionCandidates = this.diagramDescription.getEdgeDescriptions();
        new EdgeDescriptionHasReconnectToolChecker().withExpectedReconnectToolCount(2).checkAll(edgeDescriptionCandidates);
    }

    @Test
    @DisplayName("Each EdgeDescription with a center label expression has a direct edit tool")
    public void eachEdgeWithCenterLabelHasDirectEditTool() {
        List<EdgeDescription> edgeDescriptionCandidates = this.diagramDescription.getEdgeDescriptions().stream()
                .filter(edgeDescription -> edgeDescription.getCenterLabelExpression() != null && !edgeDescription.getCenterLabelExpression().isBlank())
                // SuccessionAsUsage has a label but the grammar does not support the direct edit tool yet
                .filter(this.diagramPredicates.hasDomainType(SysmlPackage.eINSTANCE.getSuccessionAsUsage()).negate())
                .toList();
        new EdgeDescriptionHasDirectEditToolChecker().checkAll(edgeDescriptionCandidates);
    }

    @Test
    @DisplayName("Each NodeDescription has a name that starts with the diagram prefix")
    public void eachNodeHasDiagramPrefix() {
        List<NodeDescription> nodeDescriptionCandidates = EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class).toList();
        new NodeDescriptionHasNameChecker().withExpectedPrefix("AFV").checkAll(nodeDescriptionCandidates);
    }

    @Test
    @DisplayName("Each NodeDescription with an inside label has a direct edit tool")
    public void eachNodeHasDirectEditTool() {
        Collection<NodeDescription> nodeDescriptionCandidates = EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class)
                .filter(this.diagramPredicates.isFakeNode().negate()
                        .and(this.diagramPredicates.isEmptyDiagramNode().negate())
                        .and(this.diagramPredicates.isCompartmentNode().negate())
                        .and(this.diagramPredicates.isInheritedCompartmentItemNode().negate()))
                .filter(nodeDescription -> nodeDescription.getInsideLabel() != null && nodeDescription.getInsideLabel().getLabelExpression() != null
                        && !nodeDescription.getInsideLabel().getLabelExpression().isBlank())
                .toList();
        new NodeDescriptionHasDirectEditToolChecker().checkAll(nodeDescriptionCandidates);
    }

    @Test
    @DisplayName("Each NodeDescription has a delete tool")
    public void eachNodeHasDeleteTool() {
        Collection<NodeDescription> nodeDescriptionCandidates = EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class)
                .filter(this.diagramPredicates.isFakeNode().negate()
                        .and(this.diagramPredicates.isEmptyDiagramNode().negate())
                        .and(this.diagramPredicates.isCompartmentNode().negate())
                        .and(this.diagramPredicates.isInheritedCompartmentItemNode().negate()))
                .toList();
        new NodeDescriptionHasDeleteToolChecker().checkAll(nodeDescriptionCandidates);
    }

    @Test
    @DisplayName("Each NodeDescription has a graphical drag & drop tool")
    public void eachNodeHasGraphicalDragAndDropTool() {
        Collection<NodeDescription> nodeDescriptionCandidates = EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class)
                    .filter(this.diagramPredicates.isCompartmentNode())
                    .toList();
        new NodeDescriptionHasDropNodeToolChecker().checkAll(nodeDescriptionCandidates);
    }

    @Test
    @DisplayName("Each NodeDescription has child node descriptions or reused node descriptions")
    public void eachNodeHasChildNodeDescriptionsOrReusedNodeDescriptions() {
        Collection<NodeDescription> nodeDescriptionCandidates = EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class)
                .filter(this.diagramPredicates.isCompartmentNode())
                .toList();
        new NodeDescriptionHasChildrenChecker().withExpectedChildCountLowerBound(1).checkAll(nodeDescriptionCandidates);
    }

    @Test
    @DisplayName("Each NodeDescription in a Fake Node is reused by at least 1 node description")
    public void eachNodeInFakeNodeIsReused() {
        Optional<NodeDescription> optFakeNode = this.diagramDescription.getNodeDescriptions().stream()
                .filter(this.diagramPredicates.isFakeNode())
                .findFirst();
        assertThat(optFakeNode).as("A diagram should have a fake node").isPresent();
        new NodeDescriptionIsReusedByChecker(this.diagramDescription).withExpectedReuserCountLowerBound(1).checkAll(optFakeNode.get().getChildrenDescriptions());
    }

    @Test
    @DisplayName("A Fake Node doesn't reuse any node description")
    public void fakeNodeDoesNotReuse() {
        Optional<NodeDescription> optFakeNode = this.diagramDescription.getNodeDescriptions().stream()
                .filter(this.diagramPredicates.isFakeNode())
                .findFirst();
        assertThat(optFakeNode).as("A diagram should have a fake node").isPresent();
        new NodeDescriptionReusesChecker().withExpectedReuseCount(0).check(optFakeNode.get());

    }

    @Test
    @DisplayName("Diagram has a semantic drag & drop tool")
    public void diagramHasSemanticDragAndDropTool() {
        new DiagramDescriptionHasDropToolChecker().check(this.diagramDescription);
    }

    @Test
    @DisplayName("Each service called in AQL is a public method from a service classe")
    public void eachAQLServicesIsPublicServiceMethod() {
        Set<String> aqlExpressions = EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class)
                .map(NodeDescription::getSemanticCandidatesExpression)
                .filter(expression -> expression != null && !expression.isBlank())
                .collect(Collectors.toSet());
        new AQLExpressionCallsExistingServicesChecker(this.diagramServices).checkAll(aqlExpressions);
    }
}
