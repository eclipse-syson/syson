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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE_STYLE;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.LineStyle;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.DropFromExplorerTester;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.imports.MutationInsertTextualSysMLv2DataRunner;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.edges.RequirementDerivationEdgeDescriptionProvider;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.dto.InsertTextualSysMLv2Input;
import org.eclipse.syson.sysml.metamodel.helper.LabelConstants;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the display of requirement derivations as edges in the General View.
 * <p>
 * A requirement derivation has no dedicated metaclass in SysML v2: it is a
 * {@link org.eclipse.syson.sysml.ConnectionUsage} annotated with the {@code #derivation} metadata.
 * </p>
 *
 * @author kabayama
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVRequirementDerivationEdgeTests extends AbstractIntegrationTests {

    private static final String ORIGINAL_REQUIREMENT_NAME = "article5Compliance";

    private static final String DERIVED_REQUIREMENT_NAME = "paymentDelayPrevention";

    private static final String DERIVATION_MODEL = """
            private import RequirementDerivation::*;
            requirement %s;
            requirement %s;
            #derivation connection {
                end #original ::> %s;
                end #derive ::> %s;
            }
            """.formatted(ORIGINAL_REQUIREMENT_NAME, DERIVED_REQUIREMENT_NAME, ORIGINAL_REQUIREMENT_NAME, DERIVED_REQUIREMENT_NAME);

    /**
     * The same derivation, with its ends declared in the reverse order. The direction of the edge is given by the
     * {@code #original} and {@code #derive} metadata, so it must not change.
     */
    private static final String REVERSED_DERIVATION_MODEL = """
            private import RequirementDerivation::*;
            requirement %s;
            requirement %s;
            #derivation connection {
                end #derive ::> %s;
                end #original ::> %s;
            }
            """.formatted(ORIGINAL_REQUIREMENT_NAME, DERIVED_REQUIREMENT_NAME, DERIVED_REQUIREMENT_NAME, ORIGINAL_REQUIREMENT_NAME);

    private static final String DERIVE_LABEL = LabelConstants.OPEN_QUOTE + LabelConstants.DERIVE + LabelConstants.CLOSE_QUOTE;

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private DropFromExplorerTester dropFromExplorerTester;

    @Autowired
    private MutationInsertTextualSysMLv2DataRunner insertTextRunner;

    @Autowired
    private ExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IIdentityService identityService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @DisplayName("GIVEN a requirement derivation between two requirements, WHEN both requirements are displayed on the General View, THEN a derive edge is displayed from the derived requirement to the original one")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @Test
    public void derivationBetweenDisplayedRequirementsIsRenderedAsAnEdge() {
        this.checkDerivationEdge(DERIVATION_MODEL);
    }

    @DisplayName("GIVEN a requirement derivation whose ends are declared in the reverse order, WHEN both requirements are displayed on the General View, THEN the derive edge still goes from the derived requirement to the original one")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @Test
    public void derivationWithReversedEndDeclarationOrderKeepsItsDirection() {
        this.checkDerivationEdge(REVERSED_DERIVATION_MODEL);
    }

    /**
     * Displays the two requirements of the given derivation model on the General View and checks the resulting edge.
     *
     * @param derivationModel
     *            the SysML v2 text declaring the two requirements and the derivation between them
     */
    private void checkDerivationEdge(String derivationModel) {
        // The semantic model is created before subscribing to the diagram, so that the only diagram refreshes the
        // StepVerifier has to handle are the ones triggered by the drops.
        this.insertText(GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID, derivationModel);
        var originalRequirementId = this.getRequirementId(ORIGINAL_REQUIREMENT_NAME);
        var derivedRequirementId = this.getRequirementId(DERIVED_REQUIREMENT_NAME);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var derivationEdgeDescriptionId = diagramDescriptionIdProvider
                .getEdgeDescriptionId(this.descriptionNameGenerator.getEdgeName(RequirementDerivationEdgeDescriptionProvider.EDGE_TYPE));

        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        AtomicReference<String> originalNodeId = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(newDiagram -> {
            assertThat(newDiagram.getNodes()).isEmpty();
            diagram.set(newDiagram);
        });

        Runnable dropOriginalRequirement = () -> this.dropFromExplorerTester.dropFromExplorerOnDiagram(
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                diagram,
                originalRequirementId);

        Consumer<Object> afterFirstDropConsumer = assertRefreshedDiagramThat(newDiagram -> {
            assertThat(newDiagram.getNodes()).hasSize(1);
            originalNodeId.set(newDiagram.getNodes().get(0).getId());
            // No edge can be displayed yet, the other end of the derivation is not on the diagram.
            assertThat(this.visibleEdges(newDiagram)).isEmpty();
            diagram.set(newDiagram);
        });

        Runnable dropDerivedRequirement = () -> this.dropFromExplorerTester.dropFromExplorerOnDiagram(
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                diagram,
                derivedRequirementId);

        Consumer<Object> afterSecondDropConsumer = assertRefreshedDiagramThat(newDiagram -> {
            assertThat(newDiagram.getNodes()).hasSize(2);
            var derivedNodeId = newDiagram.getNodes().stream()
                    .map(node -> node.getId())
                    .filter(nodeId -> !nodeId.equals(originalNodeId.get()))
                    .findFirst()
                    .orElseThrow();

            var newVisibleEdges = this.visibleEdges(newDiagram);

            // Exactly one edge: the generic ConnectionUsage edge must not render the derivation a second time.
            assertThat(newVisibleEdges).hasSize(1);
            assertThat(newVisibleEdges.get(0).getDescriptionId()).isEqualTo(derivationEdgeDescriptionId);

            assertThat(newVisibleEdges).first(EDGE)
                    .hasSourceId(derivedNodeId)
                    .hasTargetId(originalNodeId.get())
                    .extracting(Edge::getCenterLabel)
                    .extracting(Label::text)
                    .hasToString(DERIVE_LABEL);

            assertThat(newVisibleEdges).first(EDGE)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.InputArrow);

            // A dashed line distinguishes a derivation from the solid satisfy edge.
            assertThat(newVisibleEdges.get(0).getStyle().getLineStyle()).isEqualTo(LineStyle.Dash);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropOriginalRequirement)
                .consumeNextWith(afterFirstDropConsumer)
                .then(dropDerivedRequirement)
                .consumeNextWith(afterSecondDropConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(20));
    }

    private List<Edge> visibleEdges(Diagram diagram) {
        return diagram.getEdges().stream()
                .filter(edge -> edge.getState() != ViewModifier.Hidden)
                .toList();
    }

    private void insertText(String parentElementId, String content) {
        var input = new InsertTextualSysMLv2Input(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, parentElementId, content);
        var result = this.insertTextRunner.run(input);

        Map<String, Object> parsed = JsonPath.read(result.data(), "$.data.insertTextualSysMLv2");
        assertThat(parsed.get("__typename")).isEqualTo(SuccessPayload.class.getSimpleName());
        assertThat((List<?>) parsed.get("messages")).isEmpty();
    }

    /**
     * Get the semantic identifier of the {@link RequirementUsage} owned by the root package with the given name.
     *
     * @param requirementName
     *            the name of the searched requirement
     * @return the semantic identifier of the requirement
     */
    private String getRequirementId(String requirementName) {
        BiFunction<IEditingContext, IInput, IPayload> function = (editingContext, input) -> {
            var requirementId = this.objectSearchService.getObject(editingContext, GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID)
                    .filter(Package.class::isInstance)
                    .map(Package.class::cast)
                    .stream()
                    .flatMap(rootPackage -> rootPackage.getOwnedElement().stream())
                    .filter(RequirementUsage.class::isInstance)
                    .filter(requirement -> requirementName.equals(requirement.getName()))
                    .map(this.identityService::getId)
                    .findFirst()
                    .orElse(null);
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), requirementId);
        };

        var mono = this.executeEditingContextFunctionRunner
                .execute(new ExecuteEditingContextFunctionInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, function));

        var identifier = Optional.ofNullable(mono.block(Duration.ofSeconds(10)))
                .filter(ExecuteEditingContextFunctionSuccessPayload.class::isInstance)
                .map(ExecuteEditingContextFunctionSuccessPayload.class::cast)
                .map(ExecuteEditingContextFunctionSuccessPayload::result)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElse(null);

        assertThat(identifier).as("The requirement %s should have been created", requirementName).isNotNull();
        return identifier;
    }
}
