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
package org.eclipse.syson.application.controllers.diagrams.checkers;

import static org.assertj.core.api.Assertions.fail;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
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
