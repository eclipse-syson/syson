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
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.collaborative.selection.dto.SelectionDialogTreeEventInput;
import org.eclipse.sirius.components.collaborative.trees.dto.TreeRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.sirius.web.tests.services.selection.SelectionDialogTreeEventSubscriptionRunner;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeOnDiagram;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.data.SysONRepresentationDescriptionIdentifiers;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import graphql.execution.DataFetcherResult;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Tests the creation of top nodes in the General View diagram.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVTopNodeCreationTests extends AbstractIntegrationTests {

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

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
    private NodeCreationTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private SelectionDialogTreeEventSubscriptionRunner selectionDialogTreeEventSubscriptionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private static Stream<Arguments> topNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getEnumerationDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition(), 2),
                // A package doesn't have a compartment: it is handled as a custom node
                Arguments.of(SysmlPackage.eINSTANCE.getPackage(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), 9),
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition(), 9),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationDefinition(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceDefinition(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPortDefinition(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getActionDefinition(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getAssignmentActionUsage(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernUsage(), 7),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernDefinition(), 8),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getConnectionDefinition(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), 7),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementDefinition(), 8),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseUsage(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseDefinition(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceDefinition(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getMetadataDefinition(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), 7),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getStateDefinition(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getExhibitStateUsage(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getViewUsage(), 0)
        ).map(TestNameGenerator::namedArguments);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("topNodeParameters")
    public void createTopNode(EClass eClass, int compartmentCount) {
        String creationToolId = this.diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(eClass));

        this.verifier.then(() -> this.nodeCreationTester.createNodeOnDiagram(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                this.diagram,
                creationToolId));

        Consumer<DiagramRefreshedEventPayload> updatedDiagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    int createdNodesExpectedCount = 1 + compartmentCount;
                    new CheckDiagramElementCount(this.diagramComparator)
                            .hasNewEdgeCount(0)
                            .hasNewNodeCount(createdNodesExpectedCount)
                            .check(this.diagram.get(), newDiagram);

                    String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(eClass);

                    new CheckNodeOnDiagram(this.diagramDescriptionIdProvider, this.diagramComparator)
                            .hasNodeDescriptionName(newNodeDescriptionName)
                            .hasCompartmentCount(compartmentCount)
                            .check(this.diagram.get(), newDiagram);
                }, () -> fail("Missing diagram"));

        this.verifier.consumeNextWith(updatedDiagramConsumer);

        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID).orElse(null);
                    assertThat(semanticRootObject).isInstanceOf(Element.class);
                    Element semanticRootElement = (Element) semanticRootObject;
                    assertThat(semanticRootElement.getOwnedElement()).anySatisfy(element -> {
                        assertThat(eClass.isInstance(element)).isTrue();
                    });
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        this.verifier.then(semanticChecker);
    }

    @DisplayName("Given an empty SysML Project, when New Namespace Import tool on diagram is requested, then a new NamespaceImport is created")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createTopNamespaceImportNode() {
        AtomicReference<Optional<String>> libId = new AtomicReference<>(Optional.empty());
        this.verifier.then(this.semanticRunnableFactory.createRunnable(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                (editingContext, executeEditingContextFunctionInput) -> {
                    libId.set(this.getISQAcousticsLibraryId(editingContext));
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                }
        )).thenCancel().verify();

        assertThat(libId.get()).isNotEmpty();

        String creationToolId = this.diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getNamespaceImport()));
        if (libId.get().isPresent()) {
            this.verifier.then(() -> {
                this.nodeCreationTester.createNode(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                        this.diagram,
                        null,
                        creationToolId,
                        List.of(new ToolVariable("selectedObject", libId.get().get(), ToolVariableType.OBJECT_ID)));
            });

            Consumer<DiagramRefreshedEventPayload> updatedDiagramConsumer = payload -> Optional.of(payload)
                    .map(DiagramRefreshedEventPayload::diagram)
                    .ifPresentOrElse(newDiagram -> {
                        new CheckDiagramElementCount(this.diagramComparator)
                                .hasNewEdgeCount(0)
                                .hasNewNodeCount(1)
                                .check(this.diagram.get(), newDiagram);

                        String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport());

                        new CheckNodeOnDiagram(this.diagramDescriptionIdProvider, this.diagramComparator)
                                .hasNodeDescriptionName(newNodeDescriptionName)
                                .hasCompartmentCount(0)
                                .check(this.diagram.get(), newDiagram);
                    }, () -> fail("Missing diagram"));

            this.verifier.consumeNextWith(updatedDiagramConsumer);
        }
    }

    @Test
    @DisplayName("Given an empty SysML Project, when we subscribe to the selection dialog tree of the NamespaceImport tool, then the tree is sent")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenAnEmptySysMLProjectWhenWeSubscribeToTheSelectionDialogTreeOfTheNamespaceImportToolThenTheTreeIsSent() {
        AtomicReference<Optional<String>> selectionDialogDescriptionId = new AtomicReference<>(Optional.empty());

        this.verifier.then(this.semanticRunnableFactory.createRunnable(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                (editingContext, executeEditingContextFunctionInput) -> {
                    selectionDialogDescriptionId.set(this.getSelectionDialogDescriptionId(editingContext));
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                })).thenCancel().verify();

        assertThat(selectionDialogDescriptionId.get()).isNotEmpty();

        if (selectionDialogDescriptionId.get().isPresent()) {
            var representationId = this.representationIdBuilder.buildSelectionRepresentationId(selectionDialogDescriptionId.get().get(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                    List.of());
            var input = new SelectionDialogTreeEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, representationId);
            var flux = this.selectionDialogTreeEventSubscriptionRunner.run(input);

            var hasResourceRootContent = this.getTreeSubscriptionConsumer(tree -> {
                // 95 is the number of standard libraries
                assertThat(tree.getChildren()).isNotEmpty().hasSize(95);
            });
            StepVerifier.create(flux)
                    .consumeNextWith(hasResourceRootContent)
                    .thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    private Optional<String> getISQAcousticsLibraryId(IEditingContext editingContext) {
        Optional<ResourceSet> optionalResourceSet = Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain)
                .map(EditingDomain::getResourceSet);
        if (optionalResourceSet.isPresent()) {
            var resourceSet = optionalResourceSet.get();
            var optionalPackage = resourceSet.getResources().stream()
                    .filter(resource -> resource.eAdapters().stream()
                            .filter(ResourceMetadataAdapter.class::isInstance)
                            .map(ResourceMetadataAdapter.class::cast)
                            .findFirst()
                            .map(ResourceMetadataAdapter::getName).stream()
                            .anyMatch("ISQAcoustics"::equals))
                    .findFirst()
                    .flatMap(this::getISQAcousticsLibraryPackageElement);
            if (optionalPackage.isPresent()) {
                return Optional.of(this.identityService.getId(optionalPackage.get()));
            }
        }
        return Optional.empty();
    }

    private Optional<LibraryPackage> getISQAcousticsLibraryPackageElement(Resource resource) {
        return resource.getContents().stream()
                .filter(Namespace.class::isInstance)
                .map(Namespace.class::cast)
                .flatMap(namespace -> namespace.getOwnedMembership().stream())
                .flatMap(membership -> membership.getRelatedElement().stream())
                .filter(LibraryPackage.class::isInstance)
                .filter(element -> "ISQAcoustics".equals(element.getDeclaredName()))
                .map(LibraryPackage.class::cast)
                .findFirst();
    }

    private Optional<String> getSelectionDialogDescriptionId(IEditingContext editingContext) {
        Optional<String> result = Optional.empty();
        var optionalDiagramViewDescription = this.viewDiagramDescriptionSearchService.findById(editingContext, this.diagram.get().getDescriptionId());
        if (optionalDiagramViewDescription.isPresent()) {
            var diagramViewDescription = optionalDiagramViewDescription.get();
            Optional<SelectionDialogDescription> selectionDialogDescription = diagramViewDescription.getPalette().getToolSections().stream()
                    .filter(section -> "Structure".equals(section.getName()))
                    .flatMap(section -> section.getNodeTools().stream())
                    .filter(nodeTool -> "/icons/full/obj16/NamespaceImport.svg".equals(nodeTool.getIconURLsExpression()))
                    .findFirst()
                    .map(NodeTool::getDialogDescription)
                    .filter(SelectionDialogDescription.class::isInstance)
                    .map(SelectionDialogDescription.class::cast);
            if (selectionDialogDescription.isPresent()) {
                result = Optional.of(this.diagramIdProvider.getId(selectionDialogDescription.get().getSelectionDialogTreeDescription()));
            }
        }
        return result;
    }

    private Consumer<Object> getTreeSubscriptionConsumer(Consumer<Tree> treeConsumer) {
        return object -> Optional.of(object)
                .filter(DataFetcherResult.class::isInstance)
                .map(DataFetcherResult.class::cast)
                .map(DataFetcherResult::getData)
                .filter(TreeRefreshedEventPayload.class::isInstance)
                .map(TreeRefreshedEventPayload.class::cast)
                .map(TreeRefreshedEventPayload::tree)
                .ifPresentOrElse(treeConsumer, () -> Assertions.fail("Missing tree"));
    }
}
