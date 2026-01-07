/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelSuccessPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.GeneralViewDirectEditTestProjectData;
import org.eclipse.syson.application.data.GeneralViewItemAndAttributeProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Test the direct edit on elements inside the General View diagram.
 *
 * @author frouene
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVDirectEditTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private EditLabelMutationRunner editLabelMutationRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewDirectEditTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with a part and a part definition, WHEN the part is typed with direct edit, THEN the feature typing is created")
    @Sql(scripts = { GeneralViewDirectEditTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void directEditUsingName() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();
        var partNodeLabelId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart1").getNode();
            partNodeId.set(partNode.getId());
            partNodeLabelId.set(partNode.getInsideLabel().getId());
        });

        Runnable editLabel = () -> {
            var input = new EditLabelInput(UUID.randomUUID(), GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), partNodeLabelId.get(), ": PartDefinition1");
            var result = this.editLabelMutationRunner.run(input);

            String typename = JsonPath.read(result, "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentMatcher = assertRefreshedDiagramThat(diagram -> {
            var node = new DiagramNavigator(diagram).nodeWithId(partNodeId.get()).getNode();
            DiagramAssertions.assertThat(node.getInsideLabel()).hasText(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart1 : PartDefinition1");
        });

        Runnable exposedElementsChecker = this.semanticRunnableFactory.createRunnable(GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    PartUsage part = this.objectSearchService.getObject(editingContext, GeneralViewDirectEditTestProjectData.SemanticIds.PART_USAGE_ID)
                            .filter(PartUsage.class::isInstance)
                            .map(PartUsage.class::cast)
                            .orElse(null);
                    assertThat(part).isNotNull();
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getName()).isEqualTo("PartDefinition1"));
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getElementId()).isEqualTo(GeneralViewDirectEditTestProjectData.SemanticIds.PART_DEF_1_USAGE_ELEMENT_ID));
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(editLabel)
                .consumeNextWith(updatedDiagramContentMatcher)
                .then(exposedElementsChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with a part and a part definition, WHEN the part is typed with direct edit using the qualified name, THEN the feature typing is created")
    @Sql(scripts = { GeneralViewDirectEditTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void directEditUsingWithQualifiedFullName() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();
        var partNodeLabelId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart1").getNode();
            partNodeId.set(partNode.getId());
            partNodeLabelId.set(partNode.getInsideLabel().getId());
        });

        Runnable editLabel = () -> {
            var input = new EditLabelInput(UUID.randomUUID(), GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), partNodeLabelId.get(), ": PackageRoot::Package2::PartDefinition2");
            var result = this.editLabelMutationRunner.run(input);

            String typename = JsonPath.read(result, "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentMatcher = assertRefreshedDiagramThat(diagram -> {
            var node = new DiagramNavigator(diagram).nodeWithId(partNodeId.get()).getNode();
            DiagramAssertions.assertThat(node.getInsideLabel()).hasText(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart1 : PartDefinition2");
        });

        Runnable exposedElementsChecker = this.semanticRunnableFactory.createRunnable(GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    PartUsage part = this.objectSearchService.getObject(editingContext, GeneralViewDirectEditTestProjectData.SemanticIds.PART_USAGE_ID)
                            .filter(PartUsage.class::isInstance)
                            .map(PartUsage.class::cast)
                            .orElse(null);
                    assertThat(part).isNotNull();
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getName()).isEqualTo("PartDefinition2"));
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getElementId()).isEqualTo(GeneralViewDirectEditTestProjectData.SemanticIds.PART_DEF_2_USAGE_ELEMENT_ID));
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(editLabel)
                .consumeNextWith(updatedDiagramContentMatcher)
                .then(exposedElementsChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with a part and a part definition, WHEN the part is typed with direct edit using short name, THEN the feature typing is created")
    @Sql(scripts = { GeneralViewDirectEditTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void directEditUsingShortName() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();
        var partNodeLabelId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart1").getNode();
            partNodeId.set(partNode.getId());
            partNodeLabelId.set(partNode.getInsideLabel().getId());
        });

        Runnable editLabel = () -> {
            var input = new EditLabelInput(UUID.randomUUID(), GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), partNodeLabelId.get(), ": PDef1");
            var result = this.editLabelMutationRunner.run(input);

            String typename = JsonPath.read(result, "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentMatcher = assertRefreshedDiagramThat(diagram -> {
            var node = new DiagramNavigator(diagram).nodeWithId(partNodeId.get()).getNode();
            DiagramAssertions.assertThat(node.getInsideLabel()).hasText(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart1 : PartDefinition1");
        });

        Runnable exposedElementsChecker = this.semanticRunnableFactory.createRunnable(GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    PartUsage part = this.objectSearchService.getObject(editingContext, GeneralViewDirectEditTestProjectData.SemanticIds.PART_USAGE_ID)
                            .filter(PartUsage.class::isInstance)
                            .map(PartUsage.class::cast)
                            .orElse(null);
                    assertThat(part).isNotNull();
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getName()).isEqualTo("PartDefinition1"));
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getElementId()).isEqualTo(GeneralViewDirectEditTestProjectData.SemanticIds.PART_DEF_1_USAGE_ELEMENT_ID));
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(editLabel)
                .consumeNextWith(updatedDiagramContentMatcher)
                .then(exposedElementsChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with a part and a part definition, WHEN the part is typed with direct edit using the qualified short name, THEN the feature typing is created")
    @Sql(scripts = { GeneralViewDirectEditTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void directEditUsingQualifiedShortName() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();
        var partNodeLabelId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart1").getNode();
            partNodeId.set(partNode.getId());
            partNodeLabelId.set(partNode.getInsideLabel().getId());
        });

        Runnable editLabel = () -> {
            var input = new EditLabelInput(UUID.randomUUID(), GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), partNodeLabelId.get(), ": PRoot::P2::PDef2");
            var result = this.editLabelMutationRunner.run(input);

            String typename = JsonPath.read(result, "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentMatcher = assertRefreshedDiagramThat(diagram -> {
            var node = new DiagramNavigator(diagram).nodeWithId(partNodeId.get()).getNode();
            DiagramAssertions.assertThat(node.getInsideLabel()).hasText(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart1 : PartDefinition2");
        });

        Runnable exposedElementsChecker = this.semanticRunnableFactory.createRunnable(GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    PartUsage part = this.objectSearchService.getObject(editingContext, GeneralViewDirectEditTestProjectData.SemanticIds.PART_USAGE_ID)
                            .filter(PartUsage.class::isInstance)
                            .map(PartUsage.class::cast)
                            .orElse(null);
                    assertThat(part).isNotNull();
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getName()).isEqualTo("PartDefinition2"));
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getElementId()).isEqualTo(GeneralViewDirectEditTestProjectData.SemanticIds.PART_DEF_2_USAGE_ELEMENT_ID));
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(editLabel)
                .consumeNextWith(updatedDiagramContentMatcher)
                .then(exposedElementsChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with a part and a part definition in the same scope, WHEN the part is typed with direct edit using short name, THEN the feature typing is created with the best candidat")
    @Sql(scripts = { GeneralViewDirectEditTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void directEditUsingShortNameAndScope() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();
        var partNodeLabelId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2").getNode();
            partNodeId.set(partNode.getId());
            partNodeLabelId.set(partNode.getInsideLabel().getId());
        });

        Runnable editLabel = () -> {
            var input = new EditLabelInput(UUID.randomUUID(), GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), partNodeLabelId.get(), ": PDef2");
            var result = this.editLabelMutationRunner.run(input);

            String typename = JsonPath.read(result, "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentMatcher = assertRefreshedDiagramThat(diagram -> {
            var node = new DiagramNavigator(diagram).nodeWithId(partNodeId.get()).getNode();
            DiagramAssertions.assertThat(node.getInsideLabel()).hasText(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2 : PartDefinition2");
        });

        Runnable exposedElementsChecker = this.semanticRunnableFactory.createRunnable(GeneralViewDirectEditTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    PartUsage part = this.objectSearchService.getObject(editingContext, GeneralViewDirectEditTestProjectData.SemanticIds.PART_USAGE_ID)
                            .filter(PartUsage.class::isInstance)
                            .map(PartUsage.class::cast)
                            .orElse(null);
                    assertThat(part).isNotNull();
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getName()).isEqualTo("PartDefinition2"));
                    assertThat(part.getPartDefinition()).allSatisfy(partDefinition -> assertThat(partDefinition.getElementId()).isEqualTo(GeneralViewDirectEditTestProjectData.SemanticIds.PART_DEF_2_USAGE_ELEMENT_ID));
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(editLabel)
                .consumeNextWith(updatedDiagramContentMatcher)
                .then(exposedElementsChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with an attribute, WHEN we direct edit with an operation using an unimported namespace, THEN the attribute is correctly set")
    @Sql(scripts = { GeneralViewItemAndAttributeProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void directEditOperationUsingUnImportedNameSpaceName() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.DIAGRAM_ID);

        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);

        var diagramId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();
        var partNodeLabelId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var partNode = new DiagramNavigator(diagram).nodeWithLabel("x1").getNode();
            partNodeId.set(partNode.getId());
            partNodeLabelId.set(partNode.getInsideLabel().getId());
        });

        Runnable editLabel = () -> {
            var input = new EditLabelInput(UUID.randomUUID(), GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID, diagramId.get(), partNodeLabelId.get(), "t1 = 1[g]");
            var result = this.editLabelMutationRunner.run(input);

            String typename = JsonPath.read(result, "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentMatcher = assertRefreshedDiagramThat(diagram -> {
            var node = new DiagramNavigator(diagram).nodeWithId(partNodeId.get()).getNode();
            DiagramAssertions.assertThat(node.getInsideLabel()).hasText("t1 = 1 [g]");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(editLabel)
                .consumeNextWith(updatedDiagramContentMatcher)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
