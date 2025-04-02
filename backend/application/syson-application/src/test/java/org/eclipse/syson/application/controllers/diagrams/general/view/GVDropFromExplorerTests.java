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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeOnDiagram;
import org.eclipse.syson.application.controllers.diagrams.testers.DropFromExplorerTester;
import org.eclipse.syson.application.data.GeneralViewAddExistingElementsIdentifiers;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.nodes.GeneralViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Tests the drop of elements from the explorer on the General View diagram.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVDropFromExplorerTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramReference givenDiagram;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private DropFromExplorerTester dropFromExplorerTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID,
                GeneralViewAddExistingElementsIdentifiers.Diagram.ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID,
                SysMLv2Identifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void dropFromExplorerOnEmptyDiagram() {
        AtomicReference<String> semanticElementId = new AtomicReference<>();
        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    String partUsageId = this.getSemanticElementWithTargetObjectLabel(editingContext, "part1");
                    semanticElementId.set(partUsageId);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        this.verifier.then(semanticChecker);

        this.verifier.then(() -> {
            assertThat(this.diagram.get().getNodes()).hasSize(1);
            Node emptyDiagramNode = this.diagram.get().getNodes().get(0);
            String emptyDiagramNodeDescriptionId = this.diagramDescriptionIdProvider.getNodeDescriptionId(GeneralViewEmptyDiagramNodeDescriptionProvider.NAME);
            // Ensure the existing node is the "empty diagram" one.
            assertThat(emptyDiagramNode).hasDescriptionId(emptyDiagramNodeDescriptionId);
            this.dropFromExplorerTester.dropFromExplorerOnDiagram(GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID,
                    this.diagram,
                    semanticElementId.get());
        });

        Consumer<DiagramRefreshedEventPayload> updatedDiagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    new CheckDiagramElementCount(this.diagramComparator)
                            .hasNewEdgeCount(0)
                            // 1 node for the PartUsage and 7 for its compartments
                            .hasNewNodeCount(8)
                            .check(this.diagram.get(), newDiagram);
                    new CheckNodeOnDiagram(this.diagramDescriptionIdProvider, this.diagramComparator)
                            .hasNodeDescriptionName(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()))
                            .hasTargetObjectLabel("part1")
                            .hasCompartmentCount(7)
                            .check(this.diagram.get(), newDiagram);
                }, () -> fail("Missing diagram"));

        this.verifier.consumeNextWith(updatedDiagramConsumer);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void dropFromExplorerOnEmptyDiagramNode() {
        AtomicReference<String> semanticElementId = new AtomicReference<>();
        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    String partUsageId = this.getSemanticElementWithTargetObjectLabel(editingContext, "part1");
                    semanticElementId.set(partUsageId);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        this.verifier.then(semanticChecker);

        this.verifier.then(() -> {
            assertThat(this.diagram.get().getNodes()).hasSize(1);
            Node emptyDiagramNode = this.diagram.get().getNodes().get(0);
            String emptyDiagramNodeDescriptionId = this.diagramDescriptionIdProvider.getNodeDescriptionId(GeneralViewEmptyDiagramNodeDescriptionProvider.NAME);
            // Ensure the existing node is the "empty diagram" one.
            assertThat(emptyDiagramNode).hasDescriptionId(emptyDiagramNodeDescriptionId);
            this.dropFromExplorerTester.dropFromExplorer(GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID,
                    this.diagram,
                    emptyDiagramNode.getId(),
                    semanticElementId.get());
        });

        Consumer<DiagramRefreshedEventPayload> updatedDiagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    new CheckDiagramElementCount(this.diagramComparator)
                            .hasNewEdgeCount(0)
                            // 1 node for the PartUsage and 7 for its compartments
                            .hasNewNodeCount(8)
                            .check(this.diagram.get(), newDiagram);
                    new CheckNodeOnDiagram(this.diagramDescriptionIdProvider, this.diagramComparator)
                            .hasNodeDescriptionName(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()))
                            .hasTargetObjectLabel("part1")
                            .hasCompartmentCount(7)
                            .check(this.diagram.get(), newDiagram);
                }, () -> fail("Missing diagram"));

        this.verifier.consumeNextWith(updatedDiagramConsumer);
    }

    private String getSemanticElementWithTargetObjectLabel(IEditingContext editingContext, String targetObjectLabel) {
        Object semanticRootObject = this.objectSearchService.getObject(editingContext,
                GeneralViewAddExistingElementsIdentifiers.Semantic.PACKAGE_1).orElse(null);
        assertThat(semanticRootObject).isInstanceOf(Package.class);
        Package rootPackage = (Package) semanticRootObject;
        Optional<Element> optPartUsage = rootPackage.getOwnedMember().stream().filter(m -> Objects.equals(m.getName(), targetObjectLabel)).findFirst();
        assertThat(optPartUsage).isPresent();
        String partUsageId = this.identityService.getId(optPartUsage.get());
        assertThat(partUsageId).isNotNull();
        return partUsageId;
    }
}
