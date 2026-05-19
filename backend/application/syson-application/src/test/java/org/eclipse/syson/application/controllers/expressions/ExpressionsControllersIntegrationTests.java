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
package org.eclipse.syson.application.controllers.expressions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.web.application.views.explorer.ExplorerEventInput;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.explorer.ExplorerEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.expressions.graphql.CreateExpressionMutationRunner;
import org.eclipse.syson.application.controllers.expressions.graphql.DeleteExpressionMutationRunner;
import org.eclipse.syson.application.controllers.expressions.graphql.EditExpressionMutationRunner;
import org.eclipse.syson.application.controllers.expressions.graphql.ExpressionTextualRepresentationQueryRunner;
import org.eclipse.syson.application.data.ExpressionSamplesProjectData;
import org.eclipse.syson.application.expressions.dto.DeleteExpressionInput;
import org.eclipse.syson.services.explorer.api.IExplorerDefaultFiltersSearchService;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.dto.CreateExpressionInput;
import org.eclipse.syson.sysml.dto.CreateExpressionSuccessPayload;
import org.eclipse.syson.sysml.dto.EditExpressionInput;
import org.eclipse.syson.sysml.dto.EditExpressionSuccessPayload;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests for GraphQL Queries and Mutations related to SysML Expressions.
 *
 * @author pcdavid
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExpressionsControllersIntegrationTests extends AbstractIntegrationTests {
    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private ExplorerEventSubscriptionRunner explorerEventSubscriptionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private SysONTreeViewDescriptionProvider sysonTreeViewDescriptionProvider;

    @Autowired
    private IExplorerDefaultFiltersSearchService explorerDefaultFiltersSearchService;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private ExpressionTextualRepresentationQueryRunner expressionTextualRepresentationQueryRunner;

    @Autowired
    private CreateExpressionMutationRunner createExpressionMutationRunner;

    @Autowired
    private EditExpressionMutationRunner editExpressionMutationRunner;

    @Autowired
    private DeleteExpressionMutationRunner deleteExpressionMutationRunner;

    private String sysONExplorerTreeDescriptionId;

    private MetamodelQueryElementService metamodelQueryElementService;

    @BeforeEach
    public void beforeEach() {
        this.sysONExplorerTreeDescriptionId = this.sysonTreeViewDescriptionProvider.getDescriptionId();
        this.givenInitialServerState.initialize();
        this.metamodelQueryElementService = new MetamodelQueryElementService();
    }

    @DisplayName("GIVEN a SysML attribute which does not have an initial or default value, WHEN creating a new expression on it THEN the new expression is created with proper name resolution")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void canCreateExpressionInEmptyAttribute() {
        String editingContextId = ExpressionSamplesProjectData.EDITING_CONTEXT_ID;

        List<String> defaultFilters = this.explorerDefaultFiltersSearchService.findTreeDefaultFilterIds(editingContextId, this.sysONExplorerTreeDescriptionId);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, List.of(), defaultFilters);
        var explorerInput = new ExplorerEventInput(UUID.randomUUID(), editingContextId, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(explorerInput).flux();

        var treeId = new AtomicReference<String>();
        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
        });

        Runnable checkPressureAttributeHasNoValueExpression = this.checkElementHasNoExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.TANK_PRESSURE_ATTRIBUTE_ID,
                AttributeUsage.class);

        Runnable createExpressionOnPressureAttribute = this.createExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.TANK_PRESSURE_ATTRIBUTE_ID, "maxPressure / 2");

        Consumer<Object> treeRefreshed = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getId()).isEqualTo(treeId.get());
        });

        Runnable checkPressureAttributeHasExpectedValueExpression = this.checkElementHasExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.TANK_PRESSURE_ATTRIBUTE_ID,
                AttributeUsage.class,
                new AtomicReference<>(), "maxPressure / 2");

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(checkPressureAttributeHasNoValueExpression)
                .then(createExpressionOnPressureAttribute)
                .consumeNextWith(treeRefreshed)
                .then(checkPressureAttributeHasExpectedValueExpression)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML attribute which does have an existing expression, WHEN trying to create a new expression on it THEN the new expression is created with proper name resolution")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void canNotCreateExpressionInNonEmptyAttribute() {
        String editingContextId = ExpressionSamplesProjectData.EDITING_CONTEXT_ID;

        List<String> defaultFilters = this.explorerDefaultFiltersSearchService.findTreeDefaultFilterIds(editingContextId, this.sysONExplorerTreeDescriptionId);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, List.of(), defaultFilters);
        var explorerInput = new ExplorerEventInput(UUID.randomUUID(), editingContextId, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(explorerInput).flux();

        var treeId = new AtomicReference<String>();
        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
        });

        Runnable checkMaxVolumeAttributeHasExistingValueExpression = this.checkElementHasExpression(editingContextId,
                ExpressionSamplesProjectData.SemanticIds.TANK_MAX_VOLUME_ATTRIBUTE_ID, AttributeUsage.class, new AtomicReference<>(),
                "100.0 * minVolume");

        Runnable tryCreateExpressionOnMaxVolumnAttribute = () -> {
            var input = new CreateExpressionInput(UUID.randomUUID(), editingContextId, ExpressionSamplesProjectData.SemanticIds.TANK_MAX_VOLUME_ATTRIBUTE_ID, "42");
            var result = this.createExpressionMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.createExpression.__typename");
            assertThat(typename).isEqualTo(ErrorPayload.class.getSimpleName());
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(checkMaxVolumeAttributeHasExistingValueExpression)
                .then(tryCreateExpressionOnMaxVolumnAttribute)
                .then(checkMaxVolumeAttributeHasExistingValueExpression)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML attribute that has an existing expression, WHEN editing the expression to a new one THEN the attribute's expression is replaced")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void textEditAttributeExpression() {
        String editingContextId = ExpressionSamplesProjectData.EDITING_CONTEXT_ID;

        List<String> defaultFilters = this.explorerDefaultFiltersSearchService.findTreeDefaultFilterIds(editingContextId, this.sysONExplorerTreeDescriptionId);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, List.of(), defaultFilters);
        var explorerInput = new ExplorerEventInput(UUID.randomUUID(), editingContextId, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(explorerInput).flux();

        var treeId = new AtomicReference<String>();
        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
        });

        var existingExpressionId = new AtomicReference<String>();

        Runnable checkInitialExpression = this.checkElementHasExpression(editingContextId,
                ExpressionSamplesProjectData.SemanticIds.TANK_MAX_VOLUME_ATTRIBUTE_ID, AttributeUsage.class, existingExpressionId,
                "100.0 * minVolume");

        Runnable editExpressionOnMaxVolumeAttribute = this.editExpression(editingContextId, existingExpressionId::get, "50 * minVolume");

        Consumer<Object> treeRefreshed = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getId()).isEqualTo(treeId.get());
        });

        Runnable checkExpressionUpdated = this.checkElementHasExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.TANK_MAX_VOLUME_ATTRIBUTE_ID,
                AttributeUsage.class,
                existingExpressionId,
                "50 * minVolume");

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(checkInitialExpression)
                .then(editExpressionOnMaxVolumeAttribute)
                .consumeNextWith(treeRefreshed)
                .then(checkExpressionUpdated)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML constraint which does not have a predicate expression, WHEN creating a new expression on it THEN the new expression is created with proper name resolution")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void canCreateExpressionInEmptyConstraint() {
        String editingContextId = ExpressionSamplesProjectData.EDITING_CONTEXT_ID;

        List<String> defaultFilters = this.explorerDefaultFiltersSearchService.findTreeDefaultFilterIds(editingContextId, this.sysONExplorerTreeDescriptionId);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, List.of(), defaultFilters);
        var explorerInput = new ExplorerEventInput(UUID.randomUUID(), editingContextId, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(explorerInput).flux();

        var treeId = new AtomicReference<String>();
        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
        });

        Runnable checkNoInitialExpression = this.checkElementHasNoExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.PERFORMANCE_CONCERN_ASSUME_ID,
                ConstraintUsage.class);

        Runnable createExpression = this.createExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.PERFORMANCE_CONCERN_ASSUME_ID, "s.enabled == true");

        Consumer<Object> treeRefreshed = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getId()).isEqualTo(treeId.get());
        });

        Runnable checkCreatedExpression = this.checkElementHasExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.PERFORMANCE_CONCERN_ASSUME_ID, ConstraintUsage.class,
                new AtomicReference<>(), "s.enabled == true");

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(checkNoInitialExpression)
                .then(createExpression)
                .consumeNextWith(treeRefreshed)
                .then(checkCreatedExpression)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML transition which does not have a guard expression, WHEN creating a new expression on it THEN the new expression is created with proper name resolution")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void canCreateGuardExpressionInEmptyTransition() {
        String editingContextId = ExpressionSamplesProjectData.EDITING_CONTEXT_ID;

        List<String> defaultFilters = this.explorerDefaultFiltersSearchService.findTreeDefaultFilterIds(editingContextId, this.sysONExplorerTreeDescriptionId);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, List.of(), defaultFilters);
        var explorerInput = new ExplorerEventInput(UUID.randomUUID(), editingContextId, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(explorerInput).flux();

        var treeId = new AtomicReference<String>();
        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
        });

        Runnable checkNoInitialExpression = this.checkElementHasNoExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.THERMAL_CONTROL_TO_HEATING_TRANSITION_ID,
                TransitionUsage.class);

        Runnable createExpression = this.createExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.THERMAL_CONTROL_TO_HEATING_TRANSITION_ID, "currentTemp < targetTemp - tolerance");

        Consumer<Object> treeRefreshed = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getId()).isEqualTo(treeId.get());
        });

        Runnable checkCreatedExpression = this.checkElementHasExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.THERMAL_CONTROL_TO_HEATING_TRANSITION_ID, TransitionUsage.class,
                new AtomicReference<>(), "currentTemp < targetTemp - tolerance");

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(checkNoInitialExpression)
                .then(createExpression)
                .consumeNextWith(treeRefreshed)
                .then(checkCreatedExpression)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML transition with a guard expression, WHEN editing the expression on it THEN the new expression is modified with proper name resolution")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void canEditGuardExpressionOnTransition() {
        String editingContextId = ExpressionSamplesProjectData.EDITING_CONTEXT_ID;

        List<String> defaultFilters = this.explorerDefaultFiltersSearchService.findTreeDefaultFilterIds(editingContextId, this.sysONExplorerTreeDescriptionId);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, List.of(), defaultFilters);
        var explorerInput = new ExplorerEventInput(UUID.randomUUID(), editingContextId, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(explorerInput).flux();

        var treeId = new AtomicReference<String>();
        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
        });

        var expressionId = new AtomicReference<String>();

        Runnable checkNoInitialExpression = this.checkElementHasExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.THERMAL_CONTROL_TO_COOLING_TRANSITION_ID,
                TransitionUsage.class, expressionId, "currentTemp > targetTemp + tolerance");

        Runnable createExpression = this.editExpression(editingContextId, expressionId::get, "currentTemp >= targetTemp + (tolerance * 0.9)");

        Consumer<Object> treeRefreshed = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getId()).isEqualTo(treeId.get());
        });

        Runnable checkCreatedExpression = this.checkElementHasExpression(editingContextId, ExpressionSamplesProjectData.SemanticIds.THERMAL_CONTROL_TO_COOLING_TRANSITION_ID, TransitionUsage.class,
                new AtomicReference<>(), "currentTemp >= targetTemp + tolerance * 0.9"); // Note that the "()" from the
                                                                                         // source are lost in the
                                                                                         // serialization

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(checkNoInitialExpression)
                .then(createExpression)
                .consumeNextWith(treeRefreshed)
                .then(checkCreatedExpression)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML attribute that has an existing expression, WHEN editing the expression to an invalid new value THEN the attribute's expression is not modified")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void editAttributeExpressionWithInvalidNewValue() {
        String editingContextId = ExpressionSamplesProjectData.EDITING_CONTEXT_ID;

        List<String> defaultFilters = this.explorerDefaultFiltersSearchService.findTreeDefaultFilterIds(editingContextId, this.sysONExplorerTreeDescriptionId);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, List.of(), defaultFilters);
        var explorerInput = new ExplorerEventInput(UUID.randomUUID(), editingContextId, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(explorerInput).flux();

        var treeId = new AtomicReference<String>();
        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
        });

        var existingExpressionId = new AtomicReference<String>();

        Runnable checkMaxVolumeAttributeHasExistingValueExpression = this.semanticCheck(editingContextId, (editingContext, input) -> {
            var optionalMaxVolumeAttribute = this.objectSearchService.getObject(editingContext, ExpressionSamplesProjectData.SemanticIds.TANK_MAX_VOLUME_ATTRIBUTE_ID);
            assertThat(optionalMaxVolumeAttribute).containsInstanceOf(AttributeUsage.class);
            var maxVolumeAttribute = (AttributeUsage) optionalMaxVolumeAttribute.get();
            Optional<Expression> valueExpression = this.metamodelQueryElementService.getValueExpression(maxVolumeAttribute);
            assertThat(valueExpression).isNotEmpty();
            assertThat(this.metamodelQueryElementService.getExpressionTextualRepresentation(valueExpression.get())).isEqualTo("100.0 * minVolume");
            existingExpressionId.set(this.identityService.getId(valueExpression.get()));
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), optionalMaxVolumeAttribute.get());
        });

        Runnable editExpressionOnMaxVolumeAttributeWithInvalidValue = () -> {
            var input = new EditExpressionInput(UUID.randomUUID(), editingContextId, existingExpressionId.get(), "50 * minVolumeTypo");
            var result = this.editExpressionMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.editExpression.__typename");
            assertThat(typename).isEqualTo(ErrorPayload.class.getSimpleName());
            String messageLevel = JsonPath.read(result.data(), "$.data.editExpression.messages[0].level");
            assertThat(messageLevel).isEqualTo("WARNING");
            String messageBody = JsonPath.read(result.data(), "$.data.editExpression.messages[0].body");
            assertThat(messageBody).contains("Unable to resolve name 'minVolumeTypo' for reference 'memberElement' on element '[Membership] Expressions::Tank::maxVolume");
        };

        Runnable checkMaxVolumeAttributeHasInitialValueExpression = this.semanticCheck(editingContextId, (editingContext, input) -> {
            var optionalMaxVolumeAttribute = this.objectSearchService.getObject(editingContext, ExpressionSamplesProjectData.SemanticIds.TANK_MAX_VOLUME_ATTRIBUTE_ID);
            assertThat(optionalMaxVolumeAttribute).containsInstanceOf(AttributeUsage.class);
            var maxVolumeAttribute = (AttributeUsage) optionalMaxVolumeAttribute.get();
            Optional<Expression> valueExpression = this.metamodelQueryElementService.getValueExpression(maxVolumeAttribute);
            assertThat(valueExpression).isNotEmpty();
            assertThat(this.metamodelQueryElementService.getExpressionTextualRepresentation(valueExpression.get())).isEqualTo("100.0 * minVolume");
            // Check this is indeed the exact same element as before
            String newValueExpressionId = this.identityService.getId(valueExpression.get());
            assertThat(newValueExpressionId).isEqualTo(existingExpressionId.get());

            // Make sure we did not create an additional invalid FeatureValue and left it around.
            assertThat(maxVolumeAttribute.getOwnedRelationship().stream().filter(FeatureValue.class::isInstance)).hasSize(1);

            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), optionalMaxVolumeAttribute.get());
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(checkMaxVolumeAttributeHasExistingValueExpression)
                .then(editExpressionOnMaxVolumeAttributeWithInvalidValue)
                .then(checkMaxVolumeAttributeHasInitialValueExpression)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an attribute with an expression, WHEN invoking delete expression on it THEN the expression and its owning relationship are removed from the model")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void deleteExpressionFromParentAttribute() {
        this.deleteExistingExpressionFromParentElement(ExpressionSamplesProjectData.SemanticIds.TANK_MAX_VOLUME_ATTRIBUTE_ID);
    }

    @DisplayName("GIVEN an assume constraint in a concern with an expression, WHEN invoking delete expression on it THEN the expression and its owning relationship are removed from the model")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void deleteExpressionFromParentConstraint() {
        this.deleteExistingExpressionFromParentElement(ExpressionSamplesProjectData.SemanticIds.PERFORMANCE_CONCERN_REQUIRE_ID);
    }

    @DisplayName("GIVEN a transition with a guard expression, WHEN invoking delete expression on it THEN the expression and its owning relationship are removed from the model")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void deleteExpressionFromParentTransition() {
        this.deleteExistingExpressionFromParentElement(ExpressionSamplesProjectData.SemanticIds.THERMAL_CONTROL_TO_COOLING_TRANSITION_ID);
    }

    /**
     * Invoke the {@code deleteExpression()} mutation on a parent element which contains a top-level expression
     * definition, and checks that both the expression and its owning relationship are correctly removed from the model.
     *
     * @param parentElementId
     *            the id of the parent element of the expression to remove.
     */
    public void deleteExistingExpressionFromParentElement(String parentElementId) {
        String editingContextId = ExpressionSamplesProjectData.EDITING_CONTEXT_ID;

        List<String> defaultFilters = this.explorerDefaultFiltersSearchService.findTreeDefaultFilterIds(editingContextId, this.sysONExplorerTreeDescriptionId);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, List.of(), defaultFilters);
        var explorerInput = new ExplorerEventInput(UUID.randomUUID(), editingContextId, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(explorerInput).flux();

        var treeId = new AtomicReference<String>();
        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
        });

        var initialExpressionId = new AtomicReference<String>();
        var initialExpressionOwningRelationshipId = new AtomicReference<String>();

        Runnable checkParentElementExistsAndHasExpression = this.semanticCheck(editingContextId, (editingContext, input) -> {
            var optionalParentElement = this.objectSearchService.getObject(editingContext, parentElementId);
            assertThat(optionalParentElement).containsInstanceOf(Element.class);
            var parentElement = (Element) optionalParentElement.get();
            Optional<Expression> optionalExpression = this.metamodelQueryElementService.findSingleExpressionDefinition(parentElement);
            assertThat(optionalExpression).containsInstanceOf(Expression.class);
            initialExpressionId.set(this.identityService.getId(optionalExpression.get()));
            initialExpressionOwningRelationshipId.set(this.identityService.getId(optionalExpression.get().getOwningRelationship()));
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), optionalParentElement.get());
        });

        Runnable deleteExpressionFromParentElement = () -> {
            var input = new DeleteExpressionInput(UUID.randomUUID(), editingContextId, parentElementId);
            var result = this.deleteExpressionMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.deleteExpression.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> treeRefreshed = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getId()).isEqualTo(treeId.get());
        });

        Runnable checkParentElementHasNoExpression = this.semanticCheck(editingContextId, (editingContext, input) -> {
            var optionalParentElement = this.objectSearchService.getObject(editingContext, parentElementId);
            assertThat(optionalParentElement).containsInstanceOf(Element.class);
            var parentElement = (Element) optionalParentElement.get();
            Optional<Expression> optionalExpression = this.metamodelQueryElementService.findSingleExpressionDefinition(parentElement);
            assertThat(optionalExpression).isEmpty();

            // The original Expression element and its Relationship should no longer be part of the model
            assertThat(this.objectSearchService.getObject(editingContext, initialExpressionId.get())).isEmpty();
            assertThat(this.objectSearchService.getObject(editingContext, initialExpressionOwningRelationshipId.get())).isEmpty();

            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), parentElement);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(checkParentElementExistsAndHasExpression)
                .then(deleteExpressionFromParentElement)
                .consumeNextWith(treeRefreshed)
                .then(checkParentElementHasNoExpression)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML model with expressions, WHEN requesting the textual representation of an actual top-level Expression element, THEN its textual representation is returned")
    @GivenSysONServer({ ExpressionSamplesProjectData.SCRIPT_PATH })
    @Test
    public void topLevelExpressionTextualRepresentation() {
        String editingContextId = ExpressionSamplesProjectData.EDITING_CONTEXT_ID;

        List<String> defaultFilters = this.explorerDefaultFiltersSearchService.findTreeDefaultFilterIds(editingContextId, this.sysONExplorerTreeDescriptionId);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, List.of(), defaultFilters);
        var input = new ExplorerEventInput(UUID.randomUUID(), editingContextId, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(input).flux();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        var treeId = new AtomicReference<String>();
        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
        });

        // The Tank part and its attribute are not themselves expressions => ""
        var checkTank = this.checkExpressiontTextualRepresentation(editingContextId, ExpressionSamplesProjectData.SemanticIds.TANK_ID, "");
        var checkTankAttribute = this.checkExpressiontTextualRepresentation(editingContextId, ExpressionSamplesProjectData.SemanticIds.TANK_MAX_VOLUME_ATTRIBUTE_ID, "");
        // The actual attribute default value expression however should be correctly represented
        var checkTankAttributeValueExpression = this.checkExpressiontTextualRepresentation(editingContextId, ExpressionSamplesProjectData.SemanticIds.TANK_MAX_VOLUME_ATTRIBUTE_VALUE_ID,
                "100.0 * minVolume");

        var checkPerformanceConcern = this.checkExpressiontTextualRepresentation(editingContextId, ExpressionSamplesProjectData.SemanticIds.PERFORMANCE_CONCERN_ID, "");
        // A ConstaintUsage *is* an Expression from the point of view of SysML's type hierarchy, but not a top-level
        // Expression, so we expect "" here.
        var checkPerformanceConcernAssumeConstraint = this.checkExpressiontTextualRepresentation(editingContextId, ExpressionSamplesProjectData.SemanticIds.PERFORMANCE_CONCERN_ASSUME_ID, "");
        var checkPerformanceConcernRequireConstraint = this.checkExpressiontTextualRepresentation(editingContextId, ExpressionSamplesProjectData.SemanticIds.PERFORMANCE_CONCERN_REQUIRE_ID, "");
        // require s.samplingRate >= 50.0 & s.currentValue != 0.0 | s.errorCount == 0
        var checkPerformanceConcernRequireConstraintExpression = this.checkExpressiontTextualRepresentation(editingContextId,
                ExpressionSamplesProjectData.SemanticIds.PERFORMANCE_CONCERN_REQUIRE_EXPRESSION_ID, "s.samplingRate >= 50.0 & s.currentValue != 0.0 | s.errorCount == 0");

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(checkTank)
                .then(checkTankAttribute)
                .then(checkTankAttributeValueExpression)
                .then(checkPerformanceConcern)
                .then(checkPerformanceConcernAssumeConstraint)
                .then(checkPerformanceConcernRequireConstraint)
                .then(checkPerformanceConcernRequireConstraintExpression)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }


    /**
     * Executes a function in the editing context with the specified id (which is assumed to be loaded). The function
     * can perform JUnit assertions to verify the state of the semantic model in the editing context.
     */
    private Runnable semanticCheck(String editingContextId, BiFunction<IEditingContext, IInput, IPayload> checker) {
        return () -> {
            var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), editingContextId, checker);
            var payload = this.executeEditingContextFunctionRunner.execute(input).block();
            assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        };
    }

    private Runnable createExpression(String editingContextId, String parentElementId, String expressionContent) {
        return () -> {
            var input = new CreateExpressionInput(UUID.randomUUID(), editingContextId, parentElementId, expressionContent);
            var result = this.createExpressionMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.createExpression.__typename");
            assertThat(typename).isEqualTo(CreateExpressionSuccessPayload.class.getSimpleName());
        };
    }

    private Runnable editExpression(String editingContextId, Supplier<String> elementId, String expressionContent) {
        return () -> {
            var input = new EditExpressionInput(UUID.randomUUID(), editingContextId, elementId.get(), expressionContent);
            var result = this.editExpressionMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.editExpression.__typename");
            assertThat(typename).isEqualTo(EditExpressionSuccessPayload.class.getSimpleName());
        };
    }

    private Runnable checkExpressiontTextualRepresentation(String editingContextId, String elementId, String expectedTextualRepresentation) {
        return () -> {
            Map<String, Object> variables = Map.of("editingContextId", editingContextId, "elementId", elementId);
            var result = this.expressionTextualRepresentationQueryRunner.run(variables);
            String textualRepresentation = JsonPath.read(result.data(), "$.data.viewer.editingContext.expressionTextualRepresentation");
            assertThat(textualRepresentation).as("elementId: {}", elementId).isEqualTo(expectedTextualRepresentation);
        };
    }

    private <T extends Element> Runnable checkElementHasNoExpression(String editingContextId, String elementId, Class<T> expectedElementType) {
        return this.semanticCheck(editingContextId, (editingContext, input) -> {
            var optionalElement = this.objectSearchService.getObject(editingContext, elementId);
            assertThat(optionalElement).containsInstanceOf(expectedElementType);
            T element = expectedElementType.cast(optionalElement.get());
            Optional<Expression> valueExpression = this.metamodelQueryElementService.findSingleExpressionDefinition(element);
            assertThat(valueExpression).isEmpty();
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), optionalElement.get());
        });
    }

    private <T extends Element> Runnable checkElementHasExpression(String editingContextId, String elementId, Class<T> expectedElementType, AtomicReference<String> expressionId,
            String expectedExpressionTextualRepresentation) {
        return this.semanticCheck(editingContextId, (editingContext, input) -> {
            var optionalElement = this.objectSearchService.getObject(editingContext, elementId);
            assertThat(optionalElement).containsInstanceOf(expectedElementType);
            T element = expectedElementType.cast(optionalElement.get());
            Optional<Expression> valueExpression = this.metamodelQueryElementService.findSingleExpressionDefinition(element);
            assertThat(valueExpression).isNotEmpty();
            expressionId.set(this.identityService.getId(valueExpression.get()));
            assertThat(this.metamodelQueryElementService.getExpressionTextualRepresentation(valueExpression.get())).isEqualTo(expectedExpressionTextualRepresentation);
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), optionalElement.get());
        });
    }
}
