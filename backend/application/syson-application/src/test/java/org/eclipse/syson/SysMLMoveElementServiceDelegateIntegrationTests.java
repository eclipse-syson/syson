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
package org.eclipse.syson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import com.jayway.jsonpath.JsonPath;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DropNodesInput;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.layoutdata.Position;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.application.controllers.diagrams.testers.DropNodesWithMessageMutationRunner;
import org.eclipse.syson.application.data.GeneralViewFlowUsageProjectData;
import org.eclipse.syson.services.api.IDefaultSysMLMoveElementService;
import org.eclipse.syson.services.api.ISysMLMoveElementServiceDelegate;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Integration tests for {@link ISysMLMoveElementServiceDelegate}.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SysMLMoveElementServiceDelegateIntegrationTests.SysMLMoveElementServiceDelegateTestConfiguration.class)
public class SysMLMoveElementServiceDelegateIntegrationTests extends AbstractIntegrationTests {

    private static final String DROP_NODES_TYPENAME = "$.data.dropNodes.__typename";

    private static final String DROP_NODES_MESSAGE_BODY = "$.data.dropNodes.messages[0].body";

    private static final String DROP_NODES_MESSAGE_LEVEL = "$.data.dropNodes.messages[0].level";

    private static final String EXPECTED_FEEDBACK_MESSAGE = "Moved screen to Package1";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private DropNodesWithMessageMutationRunner dropNodeRunner;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID,
                GeneralViewFlowUsageProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a move delegate, WHEN Screen is dropped into Package 1, THEN the delegate moves it and returns a feedback message")
    @GivenSysONServer({ GeneralViewFlowUsageProjectData.SCRIPT_PATH })
    @Test
    public void moveScreenPartWithDelegate() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable dropScreenPartFromDiagramToPackage = () -> {
            var input = new DropNodesInput(
                    UUID.randomUUID(),
                    GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID,
                    GeneralViewFlowUsageProjectData.GraphicalIds.DIAGRAM_ID,
                    List.of(GeneralViewFlowUsageProjectData.GraphicalIds.SCREEN_PART_ID),
                    null,
                    List.of(new Position(0, 0)));
            var result = this.dropNodeRunner.run(input);
            String typename = JsonPath.read(result.data(), DROP_NODES_TYPENAME);
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
            String messageBody = JsonPath.read(result.data(), DROP_NODES_MESSAGE_BODY);
            assertThat(messageBody).isEqualTo(EXPECTED_FEEDBACK_MESSAGE);
            String messageLevel = JsonPath.read(result.data(), DROP_NODES_MESSAGE_LEVEL);
            assertThat(messageLevel).isEqualTo(MessageLevel.INFO.toString());
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(newDiagram -> {
            assertThat(newDiagram.getNodes())
                    .extracting(org.eclipse.sirius.components.diagrams.Node::getTargetObjectId)
                    .contains(GeneralViewFlowUsageProjectData.SemanticIds.SCREEN_PART);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropScreenPartFromDiagramToPackage)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    /**
     * Test configuration used to activate the move delegate only for this integration test.
     */
    @TestConfiguration
    static class SysMLMoveElementServiceDelegateTestConfiguration {

        @Bean
        ISysMLMoveElementServiceDelegate feedbackMoveElementServiceDelegate(IDefaultSysMLMoveElementService defaultMoveElementService, IFeedbackMessageService feedbackMessageService) {
            return new FeedbackMoveElementServiceDelegate(defaultMoveElementService, feedbackMessageService);
        }
    }

    /**
     * Test move delegate which behaves like the default implementation and emits a feedback message.
     */
    private static final class FeedbackMoveElementServiceDelegate implements ISysMLMoveElementServiceDelegate {

        private final IDefaultSysMLMoveElementService defaultMoveElementService;

        private final IFeedbackMessageService feedbackMessageService;

        FeedbackMoveElementServiceDelegate(IDefaultSysMLMoveElementService defaultMoveElementService, IFeedbackMessageService feedbackMessageService) {
            this.defaultMoveElementService = Objects.requireNonNull(defaultMoveElementService);
            this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        }

        @Override
        public boolean canHandle(Element element, Element newParent) {
            return true;
        }

        @Override
        public MoveStatus moveSemanticElement(Element element, Element newParent) {
            MoveStatus moveStatus = this.defaultMoveElementService.moveSemanticElement(element, newParent);
            if (moveStatus.isSuccess() && element != newParent) {
                String elementLabel = element.getDeclaredName();
                String parentLabel = newParent.getDeclaredName();
                this.feedbackMessageService.addFeedbackMessage(new Message(MessageFormat.format("Moved {0} to {1}", elementLabel, parentLabel), MessageLevel.INFO));
            }
            return moveStatus;
        }
    }
}
