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
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolSuccessPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.collaborative.selection.dto.SelectionDialogTreeEventInput;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnDiagramElementToolMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.graphql.tests.api.GraphQLResult;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.sirius.web.tests.services.selection.SelectionDialogTreeEventSubscriptionRunner;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.helper.EMFUtils;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Exercises feature chain selection and redefinition through the diagram tool and its AQL operations.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class GVRedefineNestedFeatureTests extends AbstractIntegrationTests {

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
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private SelectionDialogTreeEventSubscriptionRunner selectionDialogTreeEventSubscriptionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private InvokeSingleClickOnDiagramElementToolMutationRunner invokeToolRunner;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("GIVEN a part with an inherited nested feature chain, WHEN the diagram tool redefines the chain selected in its dialog, THEN the chain is created and its last feature is selected")
    public void redefinesTheSelectedChainAndReturnsItsLastFeatureAsTheNewSelection() {
        // GIVEN a diagram part typed by Vehicle and the inherited wheel.diameter chain available in the tool dialog
        String editingContextId = GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID;
        String contextId = GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID;
        String diagramId = GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID;
        DiagramDescription description = this.givenDiagramDescription.getDiagramDescription(editingContextId,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        String nodeDescriptionName = new SDVDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartUsage());
        String toolId = new DiagramDescriptionIdProvider(description, this.diagramIdProvider).getNodeToolId(nodeDescriptionName, "Redefine a nested feature");
        NodeTool tool = EMFUtils.allContainedObjectOfType(description, NodeDescription.class)
                .filter(node -> nodeDescriptionName.equals(node.getName()))
                .flatMap(node -> EMFUtils.allContainedObjectOfType(node.getPalette(), NodeTool.class))
                .filter(nodeTool -> "Redefine a nested feature".equals(nodeTool.getName()))
                .findFirst().orElseThrow();
        SelectionDialogDescription dialog = (SelectionDialogDescription) tool.getDialogDescription();
        String treeDescriptionId = this.diagramIdProvider.getId(dialog.getSelectionDialogTreeDescription());

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        AtomicReference<String> selectedChainId = new AtomicReference<>();
        AtomicReference<List<String>> newSelection = new AtomicReference<>();
        AtomicReference<PartUsage> sourcePart = new AtomicReference<>();
        AtomicReference<AttributeUsage> sourceAttribute = new AtomicReference<>();

        Runnable initializeModel = this.createModelInitializationRunnable(editingContextId, contextId, sourcePart, sourceAttribute);

        Runnable selectChainInDialog = () -> {
            String treeId = this.representationIdBuilder.buildSelectionRepresentationId(treeDescriptionId, contextId, List.of());
            var treeInput = new SelectionDialogTreeEventInput(UUID.randomUUID(), editingContextId, treeId);
            StepVerifier.create(this.selectionDialogTreeEventSubscriptionRunner.run(treeInput).flux())
                    .consumeNextWith(assertRefreshedTreeThat(tree -> {
                        TreeItem chain = tree.getChildren().stream()
                                .filter(item -> "wheel.diameter".equals(item.getLabel().toString()))
                                .findFirst().orElseThrow();
                        assertThat(chain.isSelectable()).isTrue();
                        selectedChainId.set(chain.getId());
                    }))
                    .thenCancel()
                    .verify(Duration.ofSeconds(10));
        };

        // WHEN the diagram tool is invoked with the feature chain selected in the dialog
        Runnable invokeTool = () -> {
            String nodeId = new DiagramNavigator(diagram.get()).nodeWithTargetObjectId(contextId).getNode().getId();
            var input = new InvokeSingleClickOnDiagramElementToolInput(UUID.randomUUID(), editingContextId, diagramId, List.of(nodeId), toolId, 0, 0,
                    List.of(new ToolVariable("selectedObject", selectedChainId.get(), ToolVariableType.OBJECT_ID)));
            GraphQLResult result = this.invokeToolRunner.run(input);

            // THEN the tool succeeds without errors or feedback messages and returns a single selection entry
            assertThat(result.errors()).isEmpty();
            String typename = JsonPath.read(result.data(), "$.data.invokeSingleClickOnDiagramElementTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnDiagramElementToolSuccessPayload.class.getSimpleName());
            List<Object> messages = JsonPath.read(result.data(), "$.data.invokeSingleClickOnDiagramElementTool.messages");
            assertThat(messages).isEmpty();
            List<String> selectionIds = JsonPath.read(result.data(), "$.data.invokeSingleClickOnDiagramElementTool.newSelection.entries[*].id");
            assertThat(selectionIds).hasSize(1);
            newSelection.set(selectionIds);
        };

        // THEN the local chain redefines wheel.diameter, its last feature is selected and the source chain is unchanged
        Runnable checkChainAndSelection = this.semanticRunnableFactory.createQueryRunnable(editingContextId, (editingContext, input) -> {
            PartUsage context = (PartUsage) this.objectSearchService.getObject(editingContext, contextId).orElseThrow();
            assertThat(context.getOwnedFeature()).hasSize(1);
            Feature localPart = context.getOwnedFeature().getFirst();
            assertThat(localPart.getOwnedFeature()).hasSize(1);
            Feature localAttribute = localPart.getOwnedFeature().getFirst();
            assertThat(localPart.getOwnedRedefinition()).singleElement()
                    .satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(sourcePart.get()));
            assertThat(localAttribute.getOwnedRedefinition()).singleElement()
                    .satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(sourceAttribute.get()));
            assertThat(newSelection.get()).containsExactly(this.identityService.getId(localAttribute));
            assertThat(sourcePart.get().getOwnedFeature()).containsExactly(sourceAttribute.get());
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), true);
        });

        var diagramInput = new DiagramEventInput(UUID.randomUUID(), editingContextId, diagramId);
        StepVerifier.create(this.givenDiagramSubscription.subscribe(diagramInput))
                // GIVEN the initialized model, refreshed diagram and wheel.diameter selected in the dialog
                .consumeNextWith(assertRefreshedDiagramThat(diagram::set))
                .then(initializeModel)
                .consumeNextWith(assertRefreshedDiagramThat(diagram::set))
                .then(selectChainInDialog)
                // WHEN the tool redefines the selected chain
                .then(invokeTool)
                // THEN the diagram refreshes and the local chain and final selection satisfy the assertions
                .consumeNextWith(assertRefreshedDiagramThat(diagram::set))
                .then(checkChainAndSelection)
                .thenCancel()
                .verify(Duration.ofSeconds(20));
    }

    private void addFeature(Type owner, Feature feature) {
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        owner.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(feature);
    }

    private Runnable createModelInitializationRunnable(String editingContextId, String contextId, AtomicReference<PartUsage> sourcePart, AtomicReference<AttributeUsage> sourceAttribute) {
        return this.semanticRunnableFactory.createMutationRunnable(editingContextId, (editingContext, input) -> {
            /*
             * Fixture structure (context denotes the existing part used by the tool):
             * part def Vehicle {
             *     part wheel {
             *         attribute diameter;
             *     }
             * }
             * part context : Vehicle;
             */
            PartUsage context = (PartUsage) this.objectSearchService.getObject(editingContext, contextId).orElseThrow();
            PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
            definition.setDeclaredName("Vehicle");
            context.eResource().getContents().add(definition);
            PartUsage wheel = SysmlFactory.eINSTANCE.createPartUsage();
            wheel.setDeclaredName("wheel");
            AttributeUsage diameter = SysmlFactory.eINSTANCE.createAttributeUsage();
            diameter.setDeclaredName("diameter");
            this.addFeature(definition, wheel);
            this.addFeature(wheel, diameter);
            FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
            context.getOwnedRelationship().add(typing);
            typing.setType(definition);
            typing.setTypedFeature(context);
            sourcePart.set(wheel);
            sourceAttribute.set(diameter);
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), true);
        });
    }
}
