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
package org.eclipse.syson.services.diagrams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.syson.application.controller.editingContext.checkers.CheckElementInParent;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckChildNode;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeOnDiagram;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.services.SemanticCheckerFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

import reactor.test.StepVerifier.Step;

/**
 * Service class for CreationTests classes.
 *
 * @author arichard
 */
public class NodeCreationTestsService {

    private final DiagramComparator diagramComparator;

    private final SemanticCheckerFactory semanticCheckerFactory;

    private final NodeCreationTester nodeCreationTester;

    private final IObjectService objectService;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public NodeCreationTestsService(DiagramComparator diagramComparator, SemanticCheckerFactory semanticCheckerFactory, NodeCreationTester nodeCreationTester, IObjectService objectService,
            IDescriptionNameGenerator descriptionNameGenerator) {
        this.diagramComparator = Objects.requireNonNull(diagramComparator);
        this.semanticCheckerFactory = Objects.requireNonNull(semanticCheckerFactory);
        this.nodeCreationTester = Objects.requireNonNull(nodeCreationTester);
        this.objectService = Objects.requireNonNull(objectService);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, EClass childEClass) {
        this.createNode(verifier, diagramDescriptionIdProvider, diagram, parentEClass, parentLabel,
                this.descriptionNameGenerator.getCreationToolName(childEClass));
    }

    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName) {
        String creationToolId = diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(parentEClass), toolName);
        verifier.then(() -> this.nodeCreationTester.createNode(SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_PROJECT,
                diagram,
                parentLabel,
                creationToolId));
    }

    public IDiagramChecker getChildNodeGraphicalChecker(AtomicReference<Diagram> previousDiagram, DiagramDescriptionIdProvider diagramDescriptionIdProvider, String parentLabel, EClass childEClass,
            int compartmentCount) {
        return (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = 1 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(0)
                    .check(previousDiagram.get(), newDiagram);

            String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(childEClass);
            new CheckChildNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(compartmentCount)
                    .check(previousDiagram.get(), newDiagram);
        };
    }

    public IDiagramChecker getCompartmentNodeGraphicalChecker(AtomicReference<Diagram> previousDiagram, DiagramDescriptionIdProvider diagramDescriptionIdProvider, String parentLabel,
            EClass parentEClass, EReference containmentReference, String compartmentName) {
        return (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(previousDiagram.get(), newDiagram);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(previousDiagram.get(), newDiagram);
        };
    }

    public IDiagramChecker getSiblingNodeGraphicalChecker(AtomicReference<Diagram> previousDiagram, DiagramDescriptionIdProvider diagramDescriptionIdProvider, EClass childEClass,
            int compartmentCount) {
        return this.getSiblingNodeGraphicalChecker(previousDiagram, diagramDescriptionIdProvider, childEClass, compartmentCount, 1);
    }

    public IDiagramChecker getSiblingNodeGraphicalChecker(AtomicReference<Diagram> previousDiagram, DiagramDescriptionIdProvider diagramDescriptionIdProvider, EClass childEClass,
            int compartmentCount, int newNodesCount) {
        return (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = newNodesCount + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(previousDiagram.get(), newDiagram);

            String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(childEClass);
            new CheckNodeOnDiagram(diagramDescriptionIdProvider, this.diagramComparator)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(compartmentCount)
                    .check(previousDiagram.get(), newDiagram);
        };
    }

    public void checkDiagram(IDiagramChecker diagramChecker, AtomicReference<Diagram> previousDiagram, Step<DiagramRefreshedEventPayload> verifier) {
        Consumer<DiagramRefreshedEventPayload> diagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    diagramChecker.check(previousDiagram.get(), newDiagram);
                }, () -> fail("Missing diagram"));
        verifier.consumeNextWith(diagramConsumer);
    }

    public void checkEditingContext(ISemanticChecker semanticChecker, Step<DiagramRefreshedEventPayload> verifier) {
        Runnable runnableChecker = this.semanticCheckerFactory.createRunnableChecker(SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_PROJECT,
                (editingContext, executeEditingContextFunctionInput) -> {
                    semanticChecker.check(editingContext);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        verifier.then(runnableChecker);
    }

    public ISemanticChecker getElementInParentSemanticChecker(String parentLabel, EReference containmentReference, EClass childEClass) {
        return new CheckElementInParent(this.objectService, SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM_OBJECT)
                .withParentLabel(parentLabel)
                .withContainmentReference(containmentReference)
                .hasType(childEClass);
    }

    public String getActionFlowNodeName(EClass eClass) {
        assertThat(eClass).isNotNull();
        String result = eClass.getName();
        if (SysmlPackage.eINSTANCE.getDecisionNode().equals(eClass)) {
            result = DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME;
        } else if (SysmlPackage.eINSTANCE.getForkNode().equals(eClass)) {
            result = ForkActionNodeDescriptionProvider.FORK_ACTION_NAME;
        } else if (SysmlPackage.eINSTANCE.getJoinNode().equals(eClass)) {
            result = JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME;
        } else if (SysmlPackage.eINSTANCE.getMergeNode().equals(eClass)) {
            result = MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME;
        }
        return result;
    }
}
