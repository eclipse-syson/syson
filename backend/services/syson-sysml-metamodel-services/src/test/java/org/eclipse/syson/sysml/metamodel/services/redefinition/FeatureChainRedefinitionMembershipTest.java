/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.sysml.metamodel.services.redefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.syson.sysml.metamodel.services.redefinition.FeatureChainRedefinitionAssertions.assertChainSuccess;
import static org.eclipse.syson.sysml.metamodel.services.redefinition.FeatureChainRedefinitionAssertions.assertLocalRedefinition;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AssertConstraintUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks specialized usage categories and their semantic membership roles.
 *
 * @author Arthur Daussy
 */
public class FeatureChainRedefinitionMembershipTest {

    private static final String ROOT_PREFIX = "Root::";

    private static final String MODEL = "UsageCategoryRedefinitions::";

    private static final String CONTEXT = MODEL + "context";

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    private final MetamodelQueryElementService queryService = new MetamodelQueryElementService();

    /**
     * Action nodes (SysML 2.1 Beta 2, 8.3.17) are ActionUsage specializations already accepted through inheritance.
     * Creating the selected node must retain that specialization without copying its parameters or behavior body.
     *
     * @param sourceName
     *            the source usage name relative to the fixture namespace
     * @param metaclass
     *            the exact SysML metaclass expected on the local redefinition
     */
    @ParameterizedTest
    @CsvSource({
        "sendUse, SendActionUsage", "acceptUse, AcceptActionUsage", "assignmentUse, AssignmentActionUsage",
        "ifUse, IfActionUsage", "whileUse, WhileLoopActionUsage", "forUse, ForLoopActionUsage",
        "terminateUse, TerminateActionUsage", "decisionUse, DecisionNode", "mergeUse, MergeNode",
        "forkUse, ForkNode", "joinUse, JoinNode"
    })
    @DisplayName("GIVEN a specialized action node, WHEN materializing controller.node, THEN its exact metaclass is retained without copying its parameters or body")
    public void verifyActionNode(String sourceName, String metaclass) throws IOException {
        // GIVEN The selected action node is declared inside an inherited controller action.
        SysMLTestModel model = SysMLTestModel.load("/feature-chains/action-node-redefinitions.xmi");
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        ActionUsage controller = model.get(ActionUsage.class, MODEL + "Root::controller");
        ActionUsage source = model.get(ActionUsage.class, MODEL + "Root::controller::" + sourceName);
        List<Relationship> sourceRelationships = List.copyOf(source.getOwnedRelationship());
        assertThat(source.eClass().getName()).isEqualTo(metaclass);

        // WHEN Materialize the controller and the selected node.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(controller, source));

        // THEN Preserve the action node's exact category without creating default payloads, branches or operands.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertLocalRedefinition(result.chain().getFirst(), context, controller);
        Feature localNode = result.chain().getLast();
        assertLocalRedefinition(localNode, result.chain().getFirst(), source);
        assertThat(localNode.getOwnedRelationship()).containsExactlyElementsOf(localNode.getOwnedRedefinition());
        assertThat(localNode.getOwnedFeature()).isEmpty();
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * Usage itself is concrete in SysML 2.1 Beta 2, 8.3.6.4, though textual usage declarations select a specialized metaclass.
     * API-created usages must follow the same exact-metaclass contract as textual specializations.
     */
    @Test
    @DisplayName("GIVEN an API-created generic Usage inherited by the context, WHEN materializing it, THEN the local feature remains a generic Usage")
    public void verifyGenericUsage() throws IOException {
        // GIVEN Attach a plain Usage to Root through a feature membership.
        SysMLTestModel model = this.loadModel();
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        Usage source = SysmlFactory.eINSTANCE.createUsage();
        source.setDeclaredName("genericUsage");
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        membership.getOwnedRelatedElement().add(source);
        model.get(Type.class, MODEL + "Root").getOwnedRelationship().add(membership);

        // WHEN Materialize this generic usage in the occurrence.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Retain the generic metaclass and the ordinary local ownership contract.
        assertChainSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> assertLocalRedefinition(local, context, source));
    }

    /**
     * Non-directed items, occurrences, connectors and constraints can expose inherited features just as parts do (service contract).
     * Connector ends remain inherited until selected themselves; they must not be copied as default children.
     *
     * @param prefixName
     *            the intermediate usage name declared in Root
     * @param sourceName
     *            the source usage name relative to the fixture namespace
     */
    @ParameterizedTest
    @CsvSource({"happening, Root::happening::value", "payload, Payload::value", "link, Link::value",
        "contract, Contract::value", "calculationUse, Root::calculationUse::value", "constraintUse, Root::constraintUse::value"})
    @DisplayName("GIVEN a specialized usage exposing an attribute, WHEN materializing usage.value, THEN the exact prefix category owns only the selected local attribute")
    public void verifyIntermediateUsage(String prefixName, String sourceName) throws IOException {
        // GIVEN The selected usage category serves as an intermediate feature exposing another source.
        SysMLTestModel model = this.loadModel();
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        Usage prefix = model.get(Usage.class, MODEL + ROOT_PREFIX + prefixName);
        Feature source = model.get(Feature.class, MODEL + sourceName);

        // WHEN Materialize both segments.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(prefix, source));

        // THEN Create the local terminal inside its local intermediate owner.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertLocalRedefinition(result.chain().getFirst(), context, prefix);
        assertLocalRedefinition(result.chain().getLast(), result.chain().getFirst(), source);
        assertThat(result.chain().getFirst().getOwnedFeature()).containsExactly(result.chain().getLast());
    }

    /**
     * SysML 2.1 Beta 2, 8.3.18.4 and 8.3.21.7, define membership kinds for state subactions and requirement constraints.
     * 8.3.22.4 defines ObjectiveMembership. Preserve these roles when materializing the selected feature (service policy).
     *
     * @param prefixName
     *            the intermediate usage name declared in Root
     * @param sourceName
     *            the source usage name relative to the fixture namespace
     * @param roleName
     *            the derived property that must expose the local role feature
     */
    @ParameterizedTest
    @CsvSource({"stateUse, Lifecycle::enter, entryAction", "stateUse, Lifecycle::run, doAction", "stateUse, Lifecycle::leave, exitAction",
        "requirementUse, Specification::assumption, assumedConstraint", "requirementUse, Specification::obligation, requiredConstraint",
        "requirementUse, Specification::contextConcern, framedConcern", "caseUse, Study::goal, objectiveRequirement"})
    @DisplayName("GIVEN a feature with a specialized semantic membership, WHEN materializing its chain, THEN the membership metaclass and role are retained")
    public void verifyMembershipRole(String prefixName, String sourceName, String roleName) throws IOException {
        // GIVEN The source feature is exposed through a role-specific membership.
        SysMLTestModel model = this.loadModel();
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        Usage prefix = model.get(Usage.class, MODEL + ROOT_PREFIX + prefixName);
        Usage source = model.get(Usage.class, MODEL + sourceName);
        FeatureMembership sourceMembership = source.getOwningFeatureMembership();

        // WHEN Materialize the path to that role feature.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(prefix, source));

        // THEN Keep the membership role so the destination's derived role accessor returns the new local feature.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        Feature local = result.chain().getLast();
        assertLocalRedefinition(local, result.chain().getFirst(), source);
        this.assertMembershipRole(local.getOwningFeatureMembership(), sourceMembership);
        assertThat(local.isIsComposite()).isEqualTo(source.isIsComposite());
        Feature localOwner = result.chain().getFirst();
        Object role = localOwner.eGet(localOwner.eClass().getEStructuralFeature(roleName));
        if (role instanceof List<?> features) {
            assertThat(features.contains(local)).as("The local feature is exposed through %s", roleName).isTrue();
        } else {
            assertThat(role).as("The local feature is exposed through %s", roleName).isSameAs(local);
        }
    }

    /**
     * Materializing a protected inherited attribute must preserve the source membership's visibility.
     * Repeating the selection must reuse that same local attribute and membership.
     *
     * @param visibility
     *            the inheritable visibility to preserve on the local membership
     */
    @ParameterizedTest
    @EnumSource(value = VisibilityKind.class, names = {"PUBLIC", "PROTECTED"})
    @DisplayName("GIVEN a public or protected inherited attribute, WHEN materializing its chain twice, THEN the local membership retains its visibility")
    public void verifyMembershipVisibility(VisibilityKind visibility) throws IOException {
        // GIVEN Root::payload : Payload and Payload::value, made protected for the corresponding invocation.
        SysMLTestModel model = this.loadModel();
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        Usage payload = model.get(Usage.class, MODEL + ROOT_PREFIX + "payload");
        AttributeUsage source = model.get(AttributeUsage.class, MODEL + "Payload::value");
        source.getOwningFeatureMembership().setVisibility(visibility);

        // WHEN creating the local payload.value path and then selecting the original path again.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(payload, source));
        FeatureChainRedefinitionResult repeated = this.service.redefineFeatureChain(context, List.of(payload, source));

        // THEN the local attribute keeps its source's visibility and is reused on the second call.
        assertChainSuccess(result);
        assertChainSuccess(repeated);
        Feature local = result.chain().getLast();
        assertLocalRedefinition(local, result.chain().getFirst(), source);
        assertThat(local.getOwningFeatureMembership().getVisibility()).isEqualTo(visibility);
        assertThat(repeated.chain()).containsExactlyElementsOf(result.chain());
        assertThat(source.getOwningFeatureMembership().getVisibility()).isEqualTo(visibility);
    }

    /**
     * SysML 2.1 Beta 2, 8.3.27, represents metadata annotations as owned members rather than accessible feature memberships.
     * Supporting Usage categories must not make annotations appear as selectable configuration features.
     */
    @Test
    @DisplayName("GIVEN a metadata annotation owned outside feature memberships, WHEN trying to materialize it, THEN it is reported as inaccessible without changing the context")
    public void verifyMetadataAnnotation() throws IOException {
        // GIVEN The metadata usage is an annotation and is absent from selectable feature chains.
        SysMLTestModel model = this.loadModel();
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        MetadataUsage source = model.get(MetadataUsage.class, MODEL + "Root::tagUse");
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());
        assertThat(this.queryService.streamFeatureChains(context, null, (owner, chain) -> chain.getLast() == source)).isEmpty();

        // WHEN Try to materialize it explicitly despite its absence from query results.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Reject the inaccessible annotation without adding a membership.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 1 'UsageCategoryRedefinitions::Root::tagUse' in context 'UsageCategoryRedefinitions::context': the feature is not accessible from this context. Select an accessible feature chain again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * A ParameterMembership still identifies a parameter when its explicit direction is absent.
     * Report the missing direction and remove the new prefix.
     */
    @Test
    @DisplayName("GIVEN a parameter membership without an explicit direction, WHEN selecting that parameter through a new prefix, THEN the operation fails and the new prefix is rolled back")
    public void verifyParameterMembershipWithoutDirection() throws IOException {
        // GIVEN Clear the payload direction while retaining its ParameterMembership.
        SysMLTestModel model = SysMLTestModel.load("/feature-chains/action-node-redefinitions.xmi");
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        ActionUsage controller = model.get(ActionUsage.class, MODEL + "Root::controller");
        ActionUsage accept = model.get(ActionUsage.class, MODEL + "Root::controller::acceptUse");
        Usage parameter = model.get(Usage.class, MODEL + "Root::controller::acceptUse::payload");
        parameter.setDirection(null);
        assertThat(parameter.getDirection()).isNull();
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // WHEN Materialize the controller, accept action and malformed payload path.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(controller, accept, parameter));

        // THEN Identify the directionless parameter and roll back the controller and accept-action prefixes.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 3 'UsageCategoryRedefinitions::Root::controller::acceptUse::payload' in context 'UsageCategoryRedefinitions::context': the parameter has no direction. Specify its direction before redefining it.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * SysML 2.0, 8.3.18.6, StateUsage::isSubstateUsage determines a substate's classification from its owner's
     * isParallel flag. The service preserves that classification when materializing a nested state chain.
     *
     * @param parallel
     *            whether the source operating state has parallel substates
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN parallel or exclusive substates, WHEN materializing operating.monitor, THEN the local child retains its substate classification")
    public void verifyStateParallelism(boolean parallel) {
        // GIVEN part def Device { state operating parallel { state monitor; } } and part device : Device.
        // The exclusive invocation omits 'parallel' on operating.
        ModelBuilder builder = new ModelBuilder();
        PartDefinition deviceType = builder.createWithName(PartDefinition.class, "Device");
        StateUsage operating = builder.createInWithName(StateUsage.class, deviceType, "operating");
        operating.setIsComposite(true);
        operating.setIsParallel(parallel);
        StateUsage monitor = builder.createInWithName(StateUsage.class, operating, "monitor");
        monitor.setIsComposite(true);
        PartUsage context = builder.createWithName(PartUsage.class, "device");
        builder.setType(context, deviceType);
        List<Relationship> sourceRelationships = List.copyOf(operating.getOwnedRelationship());
        assertThat(monitor.isSubstateUsage(parallel)).isTrue();
        assertThat(monitor.isSubstateUsage(!parallel)).isFalse();

        // WHEN materializing both states in the occurrence through the chain service.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(operating, monitor));

        // THEN each state is local, and monitor keeps the source's parallel or exclusive substate classification.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        StateUsage localOperating = (StateUsage) result.chain().getFirst();
        StateUsage localMonitor = (StateUsage) result.chain().getLast();
        assertLocalRedefinition(localOperating, context, operating);
        assertLocalRedefinition(localMonitor, localOperating, monitor);
        assertThat(localOperating.isIsParallel()).isEqualTo(parallel);
        assertThat(localMonitor.isSubstateUsage(parallel)).isTrue();
        assertThat(localMonitor.isSubstateUsage(!parallel)).isFalse();
        assertThat(operating.isIsParallel()).isEqualTo(parallel);
        assertThat(monitor.isSubstateUsage(parallel)).isTrue();
        assertThat(operating.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * SysML 2.1 Beta 2, 8.3.18.8, distinguishes transition triggers and effects by TransitionFeatureMembership.kind.
     * Their internal parameters remain inherited and are not materialized by selecting the action itself.
     *
     * @param kind
     *            the transition role to preserve, such as trigger, guard or effect
     */
    @ParameterizedTest
    @CsvSource({"trigger", "effect"})
    @DisplayName("GIVEN an inherited transition trigger or effect, WHEN materializing that action, THEN its transition membership kind is retained")
    public void verifyTransitionRole(String kind) throws IOException {
        // GIVEN A transition owns an effect, guard or trigger through a specialized membership.
        SysMLTestModel model = this.loadModel();
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        Usage state = model.get(Usage.class, MODEL + "Root::stateUse");
        ActionUsage transition = model.get(ActionUsage.class, MODEL + "Lifecycle::change");
        Feature source = transition.getOwnedFeature().stream()
                .filter(feature -> feature.getOwningFeatureMembership() instanceof TransitionFeatureMembership membership && membership.getKind().getLiteral().equals(kind))
                .findFirst().orElseThrow();

        // WHEN Materialize the state, transition and selected role feature.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(state, transition, source));

        // THEN Retain the role membership and expose the local feature through the transition's matching accessor.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(3);
        Feature local = result.chain().getLast();
        assertLocalRedefinition(local, result.chain().get(1), source);
        this.assertMembershipRole(local.getOwningFeatureMembership(), source.getOwningFeatureMembership());
        assertThat(local.getOwnedFeature()).isEmpty();
        Feature localTransition = result.chain().get(1);
        EStructuralFeature role = localTransition.eClass().getEStructuralFeature(kind + "Action");
        assertThat(((List<?>) localTransition.eGet(role)).contains(local)).as("The local action is exposed through %s", role.getName()).isTrue();
    }

    /**
     * shortcut: RequirementVerificationMembership.ownedRequirement and ViewRenderingMembership.ownedRendering return null in SysON.
     * Once those metamodel getters are implemented, these selections should be accessible and materialization should succeed,
     * preserving the RequirementVerificationMembership or ViewRenderingMembership (SysML 2.1 Beta 2, 8.3.24.2 and 8.3.26.10).
     * Until then, the query excludes these features and the mutation must leave no partial prefix.
     *
     * @param prefixName
     *            the intermediate usage name declared in Root
     * @param sourceName
     *            the source usage name relative to the fixture namespace
     */
    @ParameterizedTest
    @CsvSource({"requirementUse, Specification::verifiedRequirement", "viewUse, Visual::presentation"})
    @DisplayName("GIVEN a specialized membership whose feature getter is not implemented, WHEN materializing its child, THEN segment 2 is reported as inaccessible without creating a prefix")
    public void verifyUnimplementedMembershipAccessors(String prefixName, String sourceName) throws IOException {
        // GIVEN The membership's owned-feature accessor is unimplemented and the query cannot expose its source.
        SysMLTestModel model = this.loadModel();
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        Usage prefix = model.get(Usage.class, MODEL + ROOT_PREFIX + prefixName);
        Usage source = model.get(Usage.class, MODEL + sourceName);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> sourceRelationships = List.copyOf(source.getOwnedRelationship());
        assertThat(source.getOwningFeatureMembership().getOwnedMemberFeature()).isNull();
        assertThat(this.queryService.streamFeatureChains(context, null, (owner, chain) -> chain.getLast() == source)).isEmpty();

        // WHEN Explicitly request the path containing that source.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(prefix, source));

        // THEN Reject the inaccessible segment and preserve the context rather than leaving a partial prefix.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 2 '%s' in context 'UsageCategoryRedefinitions::context': the feature is not accessible from this context. Select an accessible feature chain again."
                        .formatted(source.getQualifiedName()));
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * SysML 2.1 Beta 2, 8.3.6.4, makes Usage the common base of the specialized usage categories.
     * The service must preserve each selected category, reusing inherited contents rather than copying them.
     * The negated assertion in the fixture also retains its truth requirement when created and reused.
     *
     * @param sourceName
     *            the selected usage name inside Root
     * @param metaclass
     *            the exact SysML metaclass expected on the selected usage and its local redefinition
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
    public void verifyUsageCategory(String sourceName, String metaclass) throws IOException {
        // GIVEN The query selects the requested usage category, including assert not constraint assertionUse, from the inherited model.
        SysMLTestModel model = this.loadModel();
        PartUsage context = model.get(PartUsage.class, CONTEXT);
        Usage source = model.get(Usage.class, MODEL + ROOT_PREFIX + sourceName);
        List<Relationship> sourceRelationships = List.copyOf(source.getOwnedRelationship());
        assertThat(source.eClass().getName()).isEqualTo(metaclass);
        List<List<Feature>> selections = this.queryService.streamFeatureChains(context, null, (owner, chain) -> chain.size() == 1 && chain.getFirst() == source).toList();
        assertThat(selections).containsExactly(List.of(source));

        // WHEN Materialize the selection twice.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, selections.getFirst());
        FeatureChainRedefinitionResult repeated = this.service.redefineFeatureChain(context, selections.getFirst());

        // THEN Reuse the same local object with its exact metaclass and no default children or copied typing.
        assertChainSuccess(result);
        assertChainSuccess(repeated);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertLocalRedefinition(local, context, source);
            assertThat(local.getOwnedRelationship()).containsExactlyElementsOf(local.getOwnedRedefinition());
            assertThat(local.getOwnedTyping()).isEmpty();
            assertThat(local.getOwnedFeature()).isEmpty();
            assertThat(local.isIsComposite()).isEqualTo(source.isIsComposite());
            if (source instanceof AssertConstraintUsage sourceAssertion) {
                // Preserve the asserted truth value, including the fixture's negated assertion.
                assertThat(local).isInstanceOf(AssertConstraintUsage.class);
                assertThat(((AssertConstraintUsage) local).isIsNegated()).isEqualTo(sourceAssertion.isIsNegated());
            }
        });
        assertThat(repeated.chain()).containsExactlyElementsOf(result.chain());
        assertThat(context.getOwnedFeature()).containsExactlyElementsOf(result.chain());
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
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

    private SysMLTestModel loadModel() throws IOException {
        return SysMLTestModel.load("/feature-chains/usage-category-redefinitions.xmi");
    }
}
