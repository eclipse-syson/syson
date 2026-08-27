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
package org.eclipse.syson.application.controllers.diagrams.standarddiagram.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.sirius.components.collaborative.dto.CreateRepresentationInput;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.graphql.PaletteQueryRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenCreatedDiagramSubscription;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Used to verify which tools are available on the different kinds of standard diagrams for a root {@link org.eclipse.syson.sysml.Namespace}.
 *
 * @author gcoutable
 */
@Transactional
@GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SDVRootNamespaceAvailableDiagramPaletteToolsTests extends AbstractIntegrationTests {

    private static final Map<String, Map<String, Boolean>> ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM = new HashMap<>();

    static {
        // Requirements
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Concern", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Concern Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Constraint", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Constraint Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Requirement", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Requirement Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Satisfy Requirement", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));

        // Structure
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Attribute", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, false, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Attribute Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Connection Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Enumeration Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Interface", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Interface Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Item", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Item Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Item In", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Item Inout", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Item Out", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Namespace Import", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, true));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Package", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Part", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Part Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Port", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, false, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Port Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Port In", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, false, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Port Inout", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, false, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Port Out", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, false, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Reference", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));

        // Behavior
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Accept Action", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Action", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Action Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Allocation", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Allocation Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Assignment Action", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Exhibit Parallel State", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, true));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Exhibit State", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, true));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Occurrence", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Occurrence Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New State", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, true));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New State Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, true));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New View", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));

        // Analysis
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Case", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Case Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Use Case", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, false));
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Use Case Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, true, StandardDiagramsConstants.STV, false));

        // Extension
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.put("New Metadata Definition", Map.of(StandardDiagramsConstants.GV, true, StandardDiagramsConstants.IV, true, StandardDiagramsConstants.AFV, false, StandardDiagramsConstants.STV, false));
    }

    /*
     * This map is used solely to improve test failure readability.
     * While the test requires the diagram description ID, logging the diagram kind name makes test failures significantly easier to diagnose.
     */
    private static final Map<String, String> DIAGRAM_KIND_TO_DIAGRAM_DESCRIPTION = Map.of(
            StandardDiagramsConstants.GV, SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID,
            StandardDiagramsConstants.IV, SysONRepresentationDescriptionIdentifiers.INTERCONNECTION_VIEW_DIAGRAM_DESCRIPTION_ID,
            StandardDiagramsConstants.AFV, SysONRepresentationDescriptionIdentifiers.ACTION_FLOW_VIEW_DIAGRAM_DESCRIPTION_ID,
            StandardDiagramsConstants.STV, SysONRepresentationDescriptionIdentifiers.STATE_TRANSITION_VIEW_DIAGRAM_DESCRIPTION_ID
    );

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenCreatedDiagramSubscription givenCreatedDiagramSubscription;

    @Autowired
    private PaletteQueryRunner paletteQueryRunner;

    private static Stream<Arguments> diagramPaletteToolAvailabilityForRootNamespaceParameters() {
        Map<String, Map<String, Boolean>> toolAvailablePerDiagram = new HashMap<>();
        ROOT_NAMESPACE_DIAGRAM_TOOLS_AVAILABILITY_IN_DIAGRAM.forEach((toolName, toolAvailabilityInDiagram) -> {
            toolAvailabilityInDiagram.forEach((viewDefinitionKind, toolIsPresent) -> {
                toolAvailablePerDiagram.computeIfAbsent(viewDefinitionKind, (key) -> new HashMap<>()).put(toolName, toolIsPresent);
            });
        });

        return toolAvailablePerDiagram.entrySet().stream().map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }

    private Flux<Object> givenDiagramSubscription(String diagramKind) {
        var input = new CreateRepresentationInput(
                UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                DIAGRAM_KIND_TO_DIAGRAM_DESCRIPTION.get(diagramKind),
                GeneralViewWithTopNodesTestProjectData.SemanticIds.ROOT_NAMESPACE_ID,
                "diagram"
        );
        return this.givenCreatedDiagramSubscription.createAndSubscribe(input).flux();
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a standard view kind, WHEN checking tool availability for the root Namespace THEN each view enforces its semantic context rules")
    @ParameterizedTest(name = "In a {0} diagram created on a root Namespace, check whether a tool should be present in the diagram palette")
    @MethodSource("diagramPaletteToolAvailabilityForRootNamespaceParameters")
    void testDiagramPaletteToolAvailabilityForRootNamespace(String diagramKind, Map<String, Boolean> toolNameToToolAvailability) {
        var flux = this.givenDiagramSubscription(diagramKind);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable getDiagramPalette = () -> {
            String diagramId = diagram.get().getId();
            Map<String, Object> variables = Map.of(
                    "editingContextId", GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", diagramId,
                    "diagramElementIds", List.of(diagramId));
            var result = this.paletteQueryRunner.run(variables);
            List<String> toolLabels = JsonPath.read(result.data(), "data.viewer.editingContext.representation.description.palette.paletteEntries[*].tools..label");
            toolNameToToolAvailability.forEach((toolName, expectedToolPresence) -> {
                var actualToolPresent = toolLabels.contains(toolName);
                assertThat(actualToolPresent).as("The tool '%s' presence is expected to be '%s', but was '%s'", toolName, expectedToolPresence, actualToolPresent).isEqualTo(expectedToolPresence);
            });
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramConsumer)
                .then(getDiagramPalette)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
