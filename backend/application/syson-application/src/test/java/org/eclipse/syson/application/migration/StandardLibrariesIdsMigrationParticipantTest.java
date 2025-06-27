/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.application.migration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.base.Objects;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.FeatureTyping;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Tests for all migration participant related to Standard Libraries elements prior to 2025.8.0.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StandardLibrariesIdsMigrationParticipantTest extends AbstractIntegrationTests {

    private static final String EDITING_CONTEXT_ID = "178c5f84-0a74-4674-a7ca-6f6891a0f400";

    private static final String DIAGRAM_ID = "d7ea5ff7-3da1-41ce-b082-b58e4786069b";

    private static final String ACTIONS_ACTION_START_ELEMENT_ID = "9a0d2905-0f9c-5bb4-af74-9780d6db1817";

    private static final String SCALAR_VALUES_STRING_ELEMENT_ID = "76028d3d-69a4-5e12-9002-ce403e0244bd";

    private static final String ATTRIBUTE_A1_FEATURE_TYPING_ELEMENT_ID = "b2850b04-3f1f-4799-8b44-f0706f22628b";

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramReference givenDiagram;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private IObjectSearchService objectSearchService;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), EDITING_CONTEXT_ID, DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Test
    @DisplayName("GIVEN a project with a diagram containg a reference to a standard library element, WHEN migrated, THEN the reference points to 'elementId' instead of 'id'")
    @Sql(scripts = { "/scripts/database-content/StandardLibrariesElementsIdsMigrationParticipant-Test.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void diagramMigrationParticpantTest() {
        this.givenCommittedTransaction.commit();
        var optionalEditingContext = this.editingContextSearchService.findById(EDITING_CONTEXT_ID.toString());
        assertThat(optionalEditingContext).isPresent();

        Runnable diagramChecker = () -> {
            var nodes = this.diagram.get().getNodes();
            assertTrue(nodes.size() == 1);
            var optActionFlowCompartment = nodes.get(0).getChildNodes().stream().filter(n -> Objects.equal("action flow", n.getInsideLabel().getText())).findFirst();
            assertTrue(optActionFlowCompartment.isPresent());

            List<Node> actionFlowChildNodes = optActionFlowCompartment.get().getChildNodes();
            assertTrue(actionFlowChildNodes.size() == 1);

            var startNodeTargetObjectId = actionFlowChildNodes.get(0).getTargetObjectId();
            assertEquals(ACTIONS_ACTION_START_ELEMENT_ID, startNodeTargetObjectId);

        };
        this.verifier.then(diagramChecker);
    }

    @Test
    @DisplayName("GIVEN a project with a model containg a reference to a standard library element, WHEN migrated, THEN the reference points to 'elementId' instead of 'id'")
    @Sql(scripts = { "/scripts/database-content/StandardLibrariesElementsIdsMigrationParticipant-Test.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void semanticDataMigrationParticpantTest() {
        this.givenCommittedTransaction.commit();
        var optionalEditingContext = this.editingContextSearchService.findById(EDITING_CONTEXT_ID.toString());
        assertThat(optionalEditingContext).isPresent();

        var optFeatureTyping = this.objectSearchService.getObject(optionalEditingContext.get(), ATTRIBUTE_A1_FEATURE_TYPING_ELEMENT_ID)
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast);
        assertTrue(optFeatureTyping.isPresent());
        var type = optFeatureTyping.get().getType();
        assertEquals(SCALAR_VALUES_STRING_ELEMENT_ID, type.getElementId());
    }
}
