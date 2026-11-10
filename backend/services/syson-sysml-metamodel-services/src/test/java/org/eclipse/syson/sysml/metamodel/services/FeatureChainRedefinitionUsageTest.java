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
package org.eclipse.syson.sysml.metamodel.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Exercises usage families and their semantic membership roles with independent textual examples.
 * Exact metaclass preservation is a service policy, not a requirement to clone source contents.
 *
 * @author Arthur Daussy
 */
public class FeatureChainRedefinitionUsageTest {

    private static final String MODEL = "UsageCategoryRedefinitions::";

    private static final String CONTEXT = MODEL + "context";

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    private final MetamodelQueryElementService queryService = new MetamodelQueryElementService();

    /**
     * SysML 2.1 Beta 2, 8.3.6.4, makes Usage the common base of the specialized usage categories.
     * The service must preserve each selected category, reusing inherited contents rather than copying them.
     */
    @ParameterizedTest
    @CsvSource({
        "happening, OccurrenceUsage", "eventUse, EventOccurrenceUsage", "payload, ItemUsage",
        "link, ConnectionUsage", "contract, InterfaceUsage", "allocationUse, AllocationUsage",
        "flowUse, FlowUsage", "successionFlowUse, SuccessionFlowUsage", "bindingUse, BindingConnectorAsUsage",
        "successionUse, SuccessionAsUsage", "enumerationUse, EnumerationUsage", "calculationUse, CalculationUsage",
        "constraintUse, ConstraintUsage", "assertionUse, AssertConstraintUsage", "requirementUse, RequirementUsage",
        "satisfactionUse, SatisfyRequirementUsage",
        "concernUse, ConcernUsage", "viewpointUse, ViewpointUsage", "stateUse, StateUsage",
        "performedUse, PerformActionUsage", "exhibitedUse, ExhibitStateUsage", "caseUse, CaseUsage",
        "analysisUse, AnalysisCaseUsage", "verificationUse, VerificationCaseUsage", "useCaseUse, UseCaseUsage",
        "includedUse, IncludeUseCaseUsage", "viewUse, ViewUsage", "renderingUse, RenderingUsage"
    })
    @DisplayName("GIVEN an inherited usage of a specialized category, WHEN materializing it twice, THEN its exact metaclass is retained without copying children or duplicating it")
    public void usageCategory(String sourceName, String metaclass) throws IOException {
        var model = this.loadModel();
        var context = model.get(PartUsage.class, CONTEXT);
        var source = model.get(Usage.class, MODEL + "Root::" + sourceName);
        var sourceRelationships = List.copyOf(source.getOwnedRelationship());
        assertThat(source.eClass().getName()).isEqualTo(metaclass);
        var selections = this.queryService.streamFeatureChains(context, null, (owner, chain) -> chain.size() == 1 && chain.getFirst() == source).toList();
        assertThat(selections).containsExactly(List.of(source));

        var result = this.service.redefineFeatureChain(context, selections.getFirst());
        var repeated = this.service.redefineFeatureChain(context, selections.getFirst());

        this.assertSuccess(result);
        this.assertSuccess(repeated);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            this.assertLocal(local, context, source);
            assertThat(local.getOwnedRelationship()).containsExactlyElementsOf(local.getOwnedRedefinition());
            assertThat(local.getOwnedTyping()).isEmpty();
            assertThat(local.getOwnedFeature()).isEmpty();
            assertThat(local.isIsComposite()).isEqualTo(source.isIsComposite());
        });
        assertThat(repeated.chain()).containsExactlyElementsOf(result.chain());
        assertThat(context.getOwnedFeature()).containsExactlyElementsOf(result.chain());
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * Action nodes (SysML 2.1 Beta 2, 8.3.17) are ActionUsage specializations already accepted through inheritance.
     * Creating the selected node must retain that specialization without copying its parameters or behavior body.
     */
    @ParameterizedTest
    @CsvSource({
        "sendUse, SendActionUsage", "acceptUse, AcceptActionUsage", "assignmentUse, AssignmentActionUsage",
        "ifUse, IfActionUsage", "whileUse, WhileLoopActionUsage", "forUse, ForLoopActionUsage",
        "terminateUse, TerminateActionUsage", "decisionUse, DecisionNode", "mergeUse, MergeNode",
        "forkUse, ForkNode", "joinUse, JoinNode"
    })
    @DisplayName("GIVEN a specialized action node, WHEN materializing controller.node, THEN its exact metaclass is retained without copying its parameters or body")
    public void actionNode(String sourceName, String metaclass) throws IOException {
        var model = this.loadModel();
        var context = model.get(PartUsage.class, CONTEXT);
        var controller = model.get(ActionUsage.class, MODEL + "Root::controller");
        var source = model.get(ActionUsage.class, MODEL + "Root::controller::" + sourceName);
        var sourceRelationships = List.copyOf(source.getOwnedRelationship());
        assertThat(source.eClass().getName()).isEqualTo(metaclass);

        var result = this.service.redefineFeatureChain(context, List.of(controller, source));

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(2);
        this.assertLocal(result.chain().getFirst(), context, controller);
        var localNode = result.chain().getLast();
        this.assertLocal(localNode, result.chain().getFirst(), source);
        assertThat(localNode.getOwnedRelationship()).containsExactlyElementsOf(localNode.getOwnedRedefinition());
        assertThat(localNode.getOwnedFeature()).isEmpty();
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * Non-directed items, occurrences, connectors and constraints can expose inherited features just as parts do (service contract).
     * Connector ends remain inherited until selected themselves; they must not be copied as default children.
     */
    @ParameterizedTest
    @CsvSource({"happening, Root::happening::value", "payload, Payload::value", "link, Link::value",
        "contract, Contract::value", "calculationUse, Root::calculationUse::value", "constraintUse, Root::constraintUse::value"})
    @DisplayName("GIVEN a specialized usage exposing an attribute, WHEN materializing usage.value, THEN the exact prefix category owns only the selected local attribute")
    public void intermediateUsage(String prefixName, String sourceName) throws IOException {
        var model = this.loadModel();
        var context = model.get(PartUsage.class, CONTEXT);
        var prefix = model.get(Usage.class, MODEL + "Root::" + prefixName);
        var source = model.get(Feature.class, MODEL + sourceName);

        var result = this.service.redefineFeatureChain(context, List.of(prefix, source));

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(2);
        this.assertLocal(result.chain().getFirst(), context, prefix);
        this.assertLocal(result.chain().getLast(), result.chain().getFirst(), source);
        assertThat(result.chain().getFirst().getOwnedFeature()).containsExactly(result.chain().getLast());
    }

    /**
     * SysML 2.1 Beta 2, 8.3.18.4 and 8.3.21.7, define membership kinds for state subactions and requirement constraints.
     * 8.3.22.4 defines ObjectiveMembership. Preserve these roles when materializing the selected feature (service policy).
     */
    @ParameterizedTest
    @CsvSource({"stateUse, Lifecycle::enter, entryAction", "stateUse, Lifecycle::run, doAction", "stateUse, Lifecycle::leave, exitAction",
        "requirementUse, Specification::assumption, assumedConstraint", "requirementUse, Specification::obligation, requiredConstraint",
        "requirementUse, Specification::contextConcern, framedConcern", "caseUse, Study::goal, objectiveRequirement"})
    @DisplayName("GIVEN a feature with a specialized semantic membership, WHEN materializing its chain, THEN the membership metaclass and role are retained")
    public void membershipRole(String prefixName, String sourceName, String roleName) throws IOException {
        var model = this.loadModel();
        var context = model.get(PartUsage.class, CONTEXT);
        var prefix = model.get(Usage.class, MODEL + "Root::" + prefixName);
        var source = model.get(Usage.class, MODEL + sourceName);
        var sourceMembership = source.getOwningFeatureMembership();

        var result = this.service.redefineFeatureChain(context, List.of(prefix, source));

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(2);
        var local = result.chain().getLast();
        this.assertLocal(local, result.chain().getFirst(), source);
        this.assertMembershipRole(local.getOwningFeatureMembership(), sourceMembership);
        assertThat(local.isIsComposite()).isEqualTo(source.isIsComposite());
        var localOwner = result.chain().getFirst();
        var role = localOwner.eGet(localOwner.eClass().getEStructuralFeature(roleName));
        if (role instanceof List<?> features) {
            assertThat(features.contains(local)).as("The local feature is exposed through %s", roleName).isTrue();
        } else {
            assertThat(role).as("The local feature is exposed through %s", roleName).isSameAs(local);
        }
    }

    /**
     * SysML 2.1 Beta 2, 8.3.18.8, distinguishes transition triggers and effects by TransitionFeatureMembership.kind.
     * Their internal parameters remain inherited and are not materialized by selecting the action itself.
     */
    @ParameterizedTest
    @CsvSource({"trigger", "effect"})
    @DisplayName("GIVEN an inherited transition trigger or effect, WHEN materializing that action, THEN its transition membership kind is retained")
    public void transitionRole(String kind) throws IOException {
        var model = this.loadModel();
        var context = model.get(PartUsage.class, CONTEXT);
        var state = model.get(Usage.class, MODEL + "Root::stateUse");
        var transition = model.get(ActionUsage.class, MODEL + "Lifecycle::change");
        var source = transition.getOwnedFeature().stream()
                .filter(feature -> feature.getOwningFeatureMembership() instanceof TransitionFeatureMembership membership && membership.getKind().getLiteral().equals(kind))
                .findFirst().orElseThrow();

        var result = this.service.redefineFeatureChain(context, List.of(state, transition, source));

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(3);
        var local = result.chain().getLast();
        this.assertLocal(local, result.chain().get(1), source);
        this.assertMembershipRole(local.getOwningFeatureMembership(), source.getOwningFeatureMembership());
        assertThat(local.getOwnedFeature()).isEmpty();
        var localTransition = result.chain().get(1);
        var role = localTransition.eClass().getEStructuralFeature(kind + "Action");
        assertThat(((List<?>) localTransition.eGet(role)).contains(local)).as("The local action is exposed through %s", role.getName()).isTrue();
    }

    /**
     * SysML 2.1 Beta 2, 8.3.27, represents metadata annotations as owned members rather than accessible feature memberships.
     * Supporting Usage categories must not make annotations appear as selectable configuration features.
     */
    @Test
    @DisplayName("GIVEN a metadata annotation owned outside feature memberships, WHEN trying to materialize it, THEN it is reported as inaccessible without changing the context")
    public void metadataAnnotation() throws IOException {
        var model = this.loadModel();
        var context = model.get(PartUsage.class, CONTEXT);
        var source = model.get(MetadataUsage.class, MODEL + "Root::tagUse");
        var relationships = List.copyOf(context.getOwnedRelationship());
        assertThat(this.queryService.streamFeatureChains(context, null, (owner, chain) -> chain.getLast() == source)).isEmpty();

        var result = this.service.redefineFeatureChain(context, List.of(source));

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 1 'UsageCategoryRedefinitions::Root::tagUse' in context 'UsageCategoryRedefinitions::context': the feature is not accessible from this context. Select an accessible feature chain again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * shortcut: RequirementVerificationMembership.ownedRequirement and ViewRenderingMembership.ownedRendering return null in SysON.
     * Once those metamodel getters are implemented, these selections should be accessible and materialization should succeed,
     * preserving the RequirementVerificationMembership or ViewRenderingMembership (SysML 2.1 Beta 2, 8.3.24.2 and 8.3.26.10).
     * Until then, the query excludes these features and the mutation must leave no partial prefix.
     */
    @ParameterizedTest
    @CsvSource({"requirementUse, Specification::verifiedRequirement", "viewUse, Visual::presentation"})
    @DisplayName("GIVEN a specialized membership whose feature getter is not implemented, WHEN materializing its child, THEN segment 2 is reported as inaccessible without creating a prefix")
    public void unimplementedMembershipAccessors(String prefixName, String sourceName) throws IOException {
        var model = this.loadModel();
        var context = model.get(PartUsage.class, CONTEXT);
        var prefix = model.get(Usage.class, MODEL + "Root::" + prefixName);
        var source = model.get(Usage.class, MODEL + sourceName);
        var relationships = List.copyOf(context.getOwnedRelationship());
        var sourceRelationships = List.copyOf(source.getOwnedRelationship());
        assertThat(source.getOwningFeatureMembership().getOwnedMemberFeature()).isNull();
        assertThat(this.queryService.streamFeatureChains(context, null, (owner, chain) -> chain.getLast() == source)).isEmpty();

        var result = this.service.redefineFeatureChain(context, List.of(prefix, source));

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 2 '%s' in context 'UsageCategoryRedefinitions::context': the feature is not accessible from this context. Select an accessible feature chain again."
                        .formatted(source.getQualifiedName()));
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * Usage itself is concrete in SysML 2.1 Beta 2, 8.3.6.4, though textual usage declarations select a specialized metaclass.
     * API-created usages must follow the same exact-metaclass contract as textual specializations.
     */
    @Test
    @DisplayName("GIVEN an API-created generic Usage inherited by the context, WHEN materializing it, THEN the local feature remains a generic Usage")
    public void genericUsage() throws IOException {
        var model = this.loadModel();
        var context = model.get(PartUsage.class, CONTEXT);
        var source = SysmlFactory.eINSTANCE.createUsage();
        source.setDeclaredName("genericUsage");
        var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        membership.getOwnedRelatedElement().add(source);
        model.get(Type.class, MODEL + "Root").getOwnedRelationship().add(membership);

        var result = this.service.redefineFeatureChain(context, List.of(source));

        this.assertSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> this.assertLocal(local, context, source));
    }

    /**
     * A ParameterMembership still identifies a parameter when its explicit direction is absent.
     * Until ordered redefinition is implemented, reject it before attaching the newly created prefix.
     */
    @Test
    @DisplayName("GIVEN a parameter membership without an explicit direction, WHEN selecting that parameter through a new prefix, THEN the operation fails without creating the prefix")
    public void parameterMembershipWithoutDirection() throws IOException {
        var model = this.loadModel();
        var context = model.get(PartUsage.class, CONTEXT);
        var controller = model.get(ActionUsage.class, MODEL + "Root::controller");
        var accept = model.get(ActionUsage.class, MODEL + "Root::controller::acceptUse");
        var parameter = model.get(Usage.class, MODEL + "Root::controller::acceptUse::payload");
        parameter.setDirection(null);
        assertThat(parameter.getDirection()).isNull();
        var relationships = List.copyOf(context.getOwnedRelationship());

        var result = this.service.redefineFeatureChain(context, List.of(controller, accept, parameter));

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 3 'UsageCategoryRedefinitions::Root::controller::acceptUse::payload' in context 'UsageCategoryRedefinitions::context': redefining a parameter is not supported yet because the required parameter order must be preserved.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    private void assertMembershipRole(FeatureMembership local, FeatureMembership source) {
        assertThat(local.eClass()).isSameAs(source.eClass());
        if (source instanceof StateSubactionMembership subaction) {
            assertThat(((StateSubactionMembership) local).getKind()).isSameAs(subaction.getKind());
        } else if (source instanceof RequirementConstraintMembership constraint) {
            assertThat(((RequirementConstraintMembership) local).getKind()).isSameAs(constraint.getKind());
        } else if (source instanceof TransitionFeatureMembership transition) {
            assertThat(((TransitionFeatureMembership) local).getKind()).isSameAs(transition.getKind());
        }
    }

    private void assertLocal(Feature local, Type owner, Feature source) {
        assertThat(local).isNotSameAs(source);
        assertThat(local.eClass()).isSameAs(source.eClass());
        assertThat(local.getOwningType()).isSameAs(owner);
        assertThat(local.getDeclaredName()).isNull();
        assertThat(local.getDeclaredShortName()).isNull();
        assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> {
            assertThat(redefinition.getRedefiningFeature()).isSameAs(local);
            assertThat(redefinition.getRedefinedFeature()).isSameAs(source);
        });
    }

    private void assertSuccess(FeatureChainRedefinitionResult result) {
        assertThat(result.isSuccess()).as("Unexpected failure: %s", result.messages()).isTrue();
        assertThat(result.messages()).isEmpty();
    }

    private SysMLTestModel loadModel() throws IOException {
        return SysMLTestModel.load("/feature-chains/usage-category-redefinitions.xmi");
    }
}
