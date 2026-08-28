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
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE;

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
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingcontext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.testers.DropFromExplorerTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.imports.MutationInsertTextualSysMLv2DataRunner;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.dto.InsertTextualSysMLv2Input;
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
 * Tests the creation of a requirement derivation from the General View.
 *
 * @author kabayama
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVRequirementDerivationCreationTests extends AbstractIntegrationTests {

    private static final String ORIGINAL_REQUIREMENT_NAME = "article5Compliance";

    private static final String DERIVED_REQUIREMENT_NAME = "paymentDelayPrevention";

    private static final String CREATION_TOOL_NAME = "New Derived Requirement";

    private static final String DERIVATION_METADATA = "RequirementDerivation::DerivationMetadata";

    private static final String ORIGINAL_END_METADATA = "RequirementDerivation::OriginalRequirementMetadata";

    private static final String DERIVED_END_METADATA = "RequirementDerivation::DerivedRequirementMetadata";

    /**
     * Two requirements without any derivation between them, and without importing the RequirementDerivation library,
     * so that the tool has to add that import itself.
     */
    private static final String TWO_REQUIREMENTS = """
            requirement %s;
            requirement %s;
            """.formatted(ORIGINAL_REQUIREMENT_NAME, DERIVED_REQUIREMENT_NAME);

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
    private EdgeCreationTester edgeCreationTester;

    @Autowired
    private MutationInsertTextualSysMLv2DataRunner insertTextRunner;

    @Autowired
    private ExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private SemanticCheckerService semanticCheckerService;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewEmptyTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @DisplayName("GIVEN two requirements displayed in a General View, WHEN using the requirement derivation edge tool between them, THEN a derive edge is created from the derived requirement to the original one")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @Test
    public void createRequirementDerivationFromTheDiagram() {
        this.insertText(GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID, TWO_REQUIREMENTS);
        var originalRequirementId = this.getRequirementId(ORIGINAL_REQUIREMENT_NAME);
        var derivedRequirementId = this.getRequirementId(DERIVED_REQUIREMENT_NAME);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getRequirementUsage()), CREATION_TOOL_NAME);

        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        AtomicReference<String> originalNodeId = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable dropOriginalRequirement = () -> this.dropFromExplorerTester.dropFromExplorerOnDiagram(
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT_ID, diagram, originalRequirementId);

        Consumer<Object> afterFirstDropConsumer = assertRefreshedDiagramThat(newDiagram -> {
            originalNodeId.set(newDiagram.getNodes().get(0).getId());
            diagram.set(newDiagram);
        });

        Runnable dropDerivedRequirement = () -> this.dropFromExplorerTester.dropFromExplorerOnDiagram(
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT_ID, diagram, derivedRequirementId);

        AtomicReference<String> derivedNodeId = new AtomicReference<>();
        Consumer<Object> afterSecondDropConsumer = assertRefreshedDiagramThat(newDiagram -> {
            assertThat(newDiagram.getNodes()).hasSize(2);
            derivedNodeId.set(newDiagram.getNodes().stream()
                    .map(node -> node.getId())
                    .filter(nodeId -> !nodeId.equals(originalNodeId.get()))
                    .findFirst()
                    .orElseThrow());
            // No derivation exists yet, the tool has not been used.
            assertThat(this.visibleEdges(newDiagram)).isEmpty();
            diagram.set(newDiagram);
        });

        // The edge is drawn from the derived requirement to the original one, the direction it is displayed in.
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT_ID, diagram, derivedNodeId.get(), originalNodeId.get(), creationToolId);

        AtomicReference<String> edgeSemanticId = new AtomicReference<>();

        Consumer<Object> afterCreationConsumer = assertRefreshedDiagramThat(newDiagram -> {
            var newVisibleEdges = this.visibleEdges(newDiagram);
            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .hasSourceId(derivedNodeId.get())
                    .hasTargetId(originalNodeId.get());
            edgeSemanticId.set(newVisibleEdges.getFirst().getTargetObjectId());
        });

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.checkCreatedDerivation(edgeSemanticId));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropOriginalRequirement)
                .consumeNextWith(afterFirstDropConsumer)
                .then(dropDerivedRequirement)
                .consumeNextWith(afterSecondDropConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(afterCreationConsumer)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private ISemanticChecker checkCreatedDerivation(AtomicReference<String> edgeSemanticId) {
        return editingContext -> {
            var edgeSemanticObject = this.objectSearchService.getObject(editingContext, edgeSemanticId.get());
            assertThat(edgeSemanticObject).isPresent()
                    .get()
                    .isInstanceOf(ConnectionUsage.class)
                    .asInstanceOf(type(ConnectionUsage.class))
                    .satisfies(connectionUsage -> {
                        // There should be three owned relationships, one OwningMembership and two FeatureMembership.
                        assertThat(connectionUsage.getOwnedRelationship()).size().isEqualTo(3);

                        // The owning membership targets the MetadataUsage
                        assertThat(connectionUsage.getOwnedRelationship())
                                .filteredOn(relationship -> relationship.eClass().equals(SysmlPackage.eINSTANCE.getOwningMembership()))
                                .flatMap(Relationship::getOwnedRelatedElement)
                                .allSatisfy(element -> {
                                    assertThat(element)
                                            .isInstanceOf(MetadataUsage.class)
                                            .asInstanceOf(type(MetadataUsage.class))
                                            .satisfies(metadataUsage -> {
                                                assertThat(metadataUsage.getMetadataDefinition().getQualifiedName()).isEqualTo(DERIVATION_METADATA);
                                            });
                                });

                        assertThat(connectionUsage.getOwnedFeatureMembership()).size().isEqualTo(2);
                        assertThat(connectionUsage.getOwnedFeatureMembership())
                                .allMatch(featureMembership -> featureMembership.eClass().equals(SysmlPackage.eINSTANCE.getFeatureMembership()))
                                .map(FeatureMembership::getOwnedMemberFeature)
                                .allSatisfy(feature -> {
                                    // Both feature membership related element are straight Usage
                                    assertThat(feature.eClass()).isEqualTo(SysmlPackage.eINSTANCE.getUsage());
                                    assertThat(feature.isIsEnd()).isTrue();
                                })
                                .anySatisfy(feature -> {
                                    // One of the two feature membership related element targets the original requirement
                                    assertThat(feature.getOwnedReferenceSubsetting().getReferencedFeature().getName()).isEqualTo(ORIGINAL_REQUIREMENT_NAME);
                                    assertThat(feature.getOwnedMembership()).size().isEqualTo(1);
                                    assertThat(feature.getOwnedMembership().getFirst().getMemberElement())
                                            .isInstanceOf(MetadataUsage.class)
                                            .asInstanceOf(type(MetadataUsage.class))
                                            .satisfies(metadataUsage -> {
                                                assertThat(metadataUsage.getMetadataDefinition().getQualifiedName()).isEqualTo(ORIGINAL_END_METADATA);
                                            });
                                })
                                .anySatisfy(feature -> {
                                    // One of the two feature membership related element targets the derived requirement
                                    assertThat(feature.getOwnedReferenceSubsetting().getReferencedFeature().getName()).isEqualTo(DERIVED_REQUIREMENT_NAME);
                                    assertThat(feature.getOwnedMembership().getFirst().getMemberElement())
                                            .isInstanceOf(MetadataUsage.class)
                                            .asInstanceOf(type(MetadataUsage.class))
                                            .satisfies(metadataUsage -> {
                                                assertThat(metadataUsage.getMetadataDefinition().getQualifiedName()).isEqualTo(DERIVED_END_METADATA);
                                            });
                                });
                    });
        };
    }

    private List<org.eclipse.sirius.components.diagrams.Edge> visibleEdges(Diagram diagram) {
        return diagram.getEdges().stream()
                .filter(edge -> edge.getState() != ViewModifier.Hidden)
                .toList();
    }

    private void insertText(String parentElementId, String content) {
        var input = new InsertTextualSysMLv2Input(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT_ID, parentElementId, content);
        var result = this.insertTextRunner.run(input);

        Map<String, Object> parsed = JsonPath.read(result.data(), "$.data.insertTextualSysMLv2");
        assertThat(parsed.get("__typename")).isEqualTo(SuccessPayload.class.getSimpleName());
        assertThat((List<?>) parsed.get("messages")).isEmpty();
    }

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
                .execute(new ExecuteEditingContextFunctionInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT_ID, function));

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
