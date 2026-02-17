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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
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
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.sirius.web.tests.services.selection.SelectionDialogTreeEventSubscriptionRunner;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeOnDiagram;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.eclipse.syson.util.ViewConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the creation of top nodes in the General View diagram.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVTopNodeCreationTests extends AbstractIntegrationTests {

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

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
    private IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    @Autowired
    private ToolTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private SelectionDialogTreeEventSubscriptionRunner selectionDialogTreeEventSubscriptionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private ExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    private static Stream<Arguments> topNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeDefinition(), 2, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), 3, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), 3, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationDefinition(), 4, 1, 2, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), 2, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getActionDefinition(), 6, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), 7, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getAssignmentActionUsage(), 1, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getComment(), 0, 0, 0, 0, ViewConstants.DEFAULT_NOTE_HEIGHT, ViewConstants.DEFAULT_NOTE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernDefinition(), 8, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernUsage(), 8, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintDefinition(), 2, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), 4, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getConnectionDefinition(), 5, 1, 2, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getEnumerationDefinition(), 2, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getExhibitStateUsage(), 6, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceDefinition(), 7, 0, 6, 2, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), 4, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition(), 2, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), 4, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getMetadataDefinition(), 3, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceDefinition(), 3, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), 2, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                // A package doesn't have a compartment: it is handled as a custom node
                Arguments.of(SysmlPackage.eINSTANCE.getPackage(), 0, 0, 0, 0, ViewConstants.DEFAULT_PACKAGE_NODE_HEIGHT, ViewConstants.DEFAULT_PACKAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition(), 11, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), 11, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getPortDefinition(), 5, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), 5, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementDefinition(), 8, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), 8, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseDefinition(), 5, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseUsage(), 7, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), 8, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getStateDefinition(), 6, 0, 0, 0, ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), 6, 0, 0, 0, ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH),
                Arguments.of(SysmlPackage.eINSTANCE.getViewUsage(), 0, 0, 0, 0, ViewConstants.DEFAULT_VIEW_USAGE_NODE_HEIGHT,
                        ViewConstants.DEFAULT_VIEW_USAGE_NODE_WIDTH)
        ).map(TestNameGenerator::namedArguments);
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("topNodeParameters")
    public void createTopNode(EClass eClass, int totalCompartmentCount, int visibleCompartmentCount, int itemsCount, int newBorderNodesCount, int expectedDefaultHeight, int expectedDefaultWidth) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(eClass));

        Runnable createNodeRunnable = () -> this.nodeCreationTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagram, creationToolId);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 1 + totalCompartmentCount + itemsCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(0)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewBorderNodeCount(newBorderNodesCount)
                    .check(initialDiagram, newDiagram);

            String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(eClass);

            new CheckNodeOnDiagram(diagramDescriptionIdProvider, this.diagramComparator)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasTotalCompartmentCount(totalCompartmentCount)
                    .hasVisibleCompartmentCount(visibleCompartmentCount)
                    .check(initialDiagram, newDiagram);

            var parentNode = newDiagram.getNodes().get(0);
            assertThat(parentNode.getDefaultHeight()).isEqualTo(expectedDefaultHeight);
            assertThat(parentNode.getDefaultWidth()).isEqualTo(expectedDefaultWidth);
        });

        Runnable semanticCheck = this.semanticRunnableFactory.createRunnable(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID).orElse(null);
                    assertThat(semanticRootObject).isInstanceOf(Element.class);
                    Element semanticRootElement = (Element) semanticRootObject;
                    assertThat(semanticRootElement.getOwnedElement()).anySatisfy(element -> {
                        assertThat(eClass.isInstance(element)).isTrue();
                    });
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an empty SysML Project, WHEN New Namespace Import tool on diagram is requested, THEN a new NamespaceImport is created")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @Test
    public void createTopNamespaceImportNode() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<String> libId = new AtomicReference<>();

        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getNamespaceImport()));

        BiFunction<IEditingContext, IInput, IPayload> initiateContextFunction = (editingContext, executeEditingContextFunctionInput) -> {
            libId.set(this.getISQAcousticsLibraryId(editingContext));
            return new SuccessPayload(executeEditingContextFunctionInput.id());
        };

        Runnable initiate = () -> {
            var checkEditingContextInput = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, initiateContextFunction);
            var payload = this.executeEditingContextFunctionRunner.execute(checkEditingContextInput).block();
            assertThat(payload).isInstanceOf(SuccessPayload.class);
        };

        Runnable invokeTool = () -> this.nodeCreationTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                diagram,
                null,
                creationToolId,
                List.of(new ToolVariable("selectedObject", libId.get(), ToolVariableType.OBJECT_ID)));

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            assertThat(libId.get()).isNotEmpty();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(0)
                    .hasNewNodeCount(1)
                    .check(initialDiagram, newDiagram);
            // the "Add your first element" empty diagram image node is not present anymore
            assertThat(newDiagram.getNodes()).hasSize(1);

            String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport());

            new CheckNodeOnDiagram(diagramDescriptionIdProvider, this.diagramComparator)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasTotalCompartmentCount(0)
                    .check(initialDiagram, newDiagram);

            var parentNode = newDiagram.getNodes().get(0);
            assertThat(parentNode.getDefaultHeight()).isEqualTo(Integer.parseInt(ViewConstants.DEFAULT_PACKAGE_NODE_HEIGHT));
            assertThat(parentNode.getDefaultWidth()).isEqualTo(Integer.parseInt(ViewConstants.DEFAULT_PACKAGE_NODE_WIDTH));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(initiate)
                .then(invokeTool)
                .consumeNextWith(diagramCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("GIVEN an empty SysML Project, WHEN we subscribe to the selection dialog tree of the NamespaceImport tool, THEN the tree is sent")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    public void givenAnEmptySysMLProjectWhenWeSubscribeToTheSelectionDialogTreeOfTheNamespaceImportToolThenTheTreeIsSent() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        AtomicReference<Optional<String>> selectionDialogDescriptionId = new AtomicReference<>(Optional.empty());

        Runnable getSelectionDialogId = this.semanticRunnableFactory.createRunnable(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                (editingContext, executeEditingContextFunctionInput) -> {
                    selectionDialogDescriptionId.set(this.getSelectionDialogDescriptionId(editingContext, diagram.get()));
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(getSelectionDialogId)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

        assertThat(selectionDialogDescriptionId.get()).isNotEmpty();

        if (selectionDialogDescriptionId.get().isPresent()) {
            var representationId = this.representationIdBuilder.buildSelectionRepresentationId(selectionDialogDescriptionId.get().get(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                    List.of());
            var input = new SelectionDialogTreeEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, representationId);
            var treeFlux = this.selectionDialogTreeEventSubscriptionRunner.run(input).flux();

            var hasResourceRootContent = this.getTreeSubscriptionConsumer(tree -> {
                // 95 is the number of standard libraries
                assertThat(tree.getChildren()).isNotEmpty().hasSize(95);
            });
            StepVerifier.create(treeFlux)
                    .consumeNextWith(hasResourceRootContent)
                    .thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    private String getISQAcousticsLibraryId(IEditingContext editingContext) {
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
                return this.identityService.getId(optionalPackage.get());
            }
        }
        return null;
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

    private Optional<String> getSelectionDialogDescriptionId(IEditingContext editingContext, Diagram diagram) {
        Optional<String> result = Optional.empty();
        var optionalDiagramViewDescription = this.viewDiagramDescriptionSearchService.findById(editingContext, diagram.getDescriptionId());
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
                .filter(TreeRefreshedEventPayload.class::isInstance)
                .map(TreeRefreshedEventPayload.class::cast)
                .map(TreeRefreshedEventPayload::tree)
                .ifPresentOrElse(treeConsumer, () -> Assertions.fail("Missing tree"));
    }
}
