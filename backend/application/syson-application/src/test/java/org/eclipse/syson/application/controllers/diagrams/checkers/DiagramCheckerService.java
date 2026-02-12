/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.application.controllers.diagrams.checkers;

import static org.assertj.core.api.Assertions.fail;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

import reactor.test.StepVerifier.Step;

/**
 * Helper to create preset {@link IDiagramChecker} and run semantic checks on a diagram.
 *
 * @author gdaniel
 */
public class DiagramCheckerService {

    private final DiagramComparator diagramComparator;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public DiagramCheckerService(DiagramComparator diagramComparator, IDescriptionNameGenerator descriptionNameGenerator) {
        this.diagramComparator = diagramComparator;
        this.descriptionNameGenerator = descriptionNameGenerator;
    }

    /**
     * Returns a consumer that checks a child node was created on the diagram.
     *
     * @param previousDiagram
     *            the reference to the previous diagram state
     * @param diagramDescriptionIdProvider
     *            the provider for diagram description IDs
     * @param parentLabel
     *            the label of the parent node
     * @param childEClass
     *            the EClass of the expected child node
     * @param compartmentCount
     *            the expected number of compartments
     * @param newNodesCount
     *            the expected total number of new nodes
     * @param newBorderNodesCount
     *            the expected total number of new border nodes
     * @return a consumer that performs the graphical check
     */
    public Consumer<Object> childNodeGraphicalChecker(AtomicReference<Diagram> previousDiagram, DiagramDescriptionIdProvider diagramDescriptionIdProvider, String parentLabel, EClass childEClass,
            int compartmentCount, int newNodesCount, int newBorderNodesCount) {
        return assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(newNodesCount)
                    .hasNewEdgeCount(0)
                    .hasNewBorderNodeCount(newBorderNodesCount)
                    .check(previousDiagram.get(), newDiagram);

            String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(childEClass);
            new CheckChildNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(compartmentCount)
                    .check(previousDiagram.get(), newDiagram);
        });
    }

    /**
     * Returns a consumer that checks a node was created in a compartment.
     *
     * @param previousDiagram
     *            the reference to the previous diagram state
     * @param diagramDescriptionIdProvider
     *            the provider for diagram description IDs
     * @param parentLabel
     *            the label of the parent node
     * @param parentEClass
     *            the EClass of the parent node
     * @param containmentReference
     *            the containment reference for the compartment item
     * @param compartmentName
     *            the name of the compartment
     * @param onlyNewVisibleNodesAndEdges
     *            check only new nodes and edges that are visible on the diagram
     * @return a consumer that performs the graphical check
     */
    public Consumer<Object> compartmentNodeGraphicalChecker(AtomicReference<Diagram> previousDiagram, DiagramDescriptionIdProvider diagramDescriptionIdProvider, String parentLabel,
            EClass parentEClass, EReference containmentReference, String compartmentName, boolean onlyNewVisibleNodesAndEdges) {
        return assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(previousDiagram.get(), newDiagram, onlyNewVisibleNodesAndEdges);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(previousDiagram.get(), newDiagram);
        });
    }

    /**
     * Returns a consumer that checks a sibling node was created on the diagram.
     *
     * @param previousDiagram
     *            the reference to the previous diagram state
     * @param diagramDescriptionIdProvider
     *            the provider for diagram description IDs
     * @param childEClass
     *            the EClass of the expected child node
     * @param compartmentCount
     *            the expected number of compartments
     * @return a consumer that performs the graphical check
     */
    public Consumer<Object> siblingNodeGraphicalChecker(AtomicReference<Diagram> previousDiagram, DiagramDescriptionIdProvider diagramDescriptionIdProvider, EClass childEClass,
            int compartmentCount) {
        return this.siblingNodeGraphicalChecker(previousDiagram, diagramDescriptionIdProvider, childEClass, compartmentCount, 1);
    }

    /**
     * Returns a consumer that checks a sibling node was created on the diagram.
     *
     * @param previousDiagram
     *            the reference to the previous diagram state
     * @param diagramDescriptionIdProvider
     *            the provider for diagram description IDs
     * @param childEClass
     *            the EClass of the expected child node
     * @param compartmentCount
     *            the expected number of compartments
     * @param newNodesCount
     *            the expected number of new nodes
     * @return a consumer that performs the graphical check
     */
    public Consumer<Object> siblingNodeGraphicalChecker(AtomicReference<Diagram> previousDiagram, DiagramDescriptionIdProvider diagramDescriptionIdProvider, EClass childEClass,
            int compartmentCount, int newNodesCount) {
        return assertRefreshedDiagramThat(newDiagram -> {
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
        });
    }

    /**
     * Runs the given diagram checker on the refreshed diagram using a StepVerifier.
     *
     * @param diagramChecker
     *            the checker to run on the diagram
     * @param previousDiagram
     *            the reference to the previous diagram state
     * @param verifier
     *            the StepVerifier step to consume the result
     * @deprecated this function will be removed when all the tests will be migrated to follow the same format as Sirius Web.
     * Please, directly use {@link DiagramEventPayloadConsumer#assertRefreshedDiagramThat}.
     */
    @Deprecated
    public void checkDiagram(IDiagramChecker diagramChecker, AtomicReference<Diagram> previousDiagram, Step<DiagramRefreshedEventPayload> verifier) {
        Consumer<DiagramRefreshedEventPayload> diagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    diagramChecker.check(previousDiagram.get(), newDiagram);
                }, () -> fail("Missing diagram"));
        verifier.consumeNextWith(diagramConsumer);
    }

    /**
     * Returns a consumer that runs the given diagram checker on the refreshed diagram.
     *
     * @param diagramChecker
     *            the checker to run on the diagram
     * @param previousDiagram
     *            the reference to the previous diagram state
     * @return a consumer that performs the diagram check
     * @deprecated this function will be removed when all the tests will be migrated to follow the same format as Sirius Web.
     * Please, directly use the consumer returned by the other functions of this class into your {@link reactor.test.StepVerifier} instead.
     */
    @Deprecated
    public Consumer<Object> checkDiagram(IDiagramChecker diagramChecker, AtomicReference<Diagram> previousDiagram) {
        return object -> Optional.of(object)
                .filter(DiagramRefreshedEventPayload.class::isInstance)
                .map(DiagramRefreshedEventPayload.class::cast)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    diagramChecker.check(previousDiagram.get(), newDiagram);
                }, () -> fail("Missing diagram"));
    }
}
