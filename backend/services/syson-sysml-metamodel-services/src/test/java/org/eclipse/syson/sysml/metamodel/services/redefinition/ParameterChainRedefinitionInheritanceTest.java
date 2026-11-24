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
package org.eclipse.syson.sysml.metamodel.services.redefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.syson.sysml.metamodel.services.redefinition.FeatureChainRedefinitionAssertions.assertChainSuccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks materialization of parameters across inherited signatures.
 *
 * @author Arthur Daussy
 */
public class ParameterChainRedefinitionInheritanceTest {

    private static final String BASE_STATE = "Base::currentState";

    private static final String MODEL = "ComplexParameterRedefinitions::";

    private static final String BASE_INPUT = "Base::input";

    private static final String OTHER_REQUEST = "Other::request";

    private static final List<String> BASE_PARAMETERS = List.of(BASE_INPUT, "Base::output", BASE_STATE);

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    /**
     * An inherited position counts protected parameters and excludes private parameters. The selected local
     * parameter must actually redefine the source chosen by the caller.
     *
     * @param visibility
     *            the visibility of the input preceding the public selection
     * @param throughIntermediate
     *            whether the selection inherits the signature through an empty intermediate action
     */
    @ParameterizedTest
    @CsvSource({"PRIVATE, false", "PRIVATE, true", "PROTECTED, false", "PROTECTED, true"})
    @DisplayName("GIVEN an input preceded by a private or protected input, WHEN redefining the public input, THEN only its inheritable prefix is created and the returned feature redefines it")
    public void verifyInheritedPositionRespectsVisibility(VisibilityKind visibility, boolean throughIntermediate) {
        // GIVEN action def VisibilityBase { private in ref hidden; in ref visible; } and action def VisibilityChild :> VisibilityBase.
        // The protected invocation makes the first input protected, so it also belongs to Child's signature.
        ModelBuilder builder = new ModelBuilder();
        ActionDefinition base = builder.createWithName(ActionDefinition.class, "VisibilityBase");
        Feature hidden = builder.createInWithName(ReferenceUsage.class, base, "hidden");
        hidden.setDirection(FeatureDirectionKind.IN);
        hidden.getOwningFeatureMembership().setVisibility(visibility);
        Feature visible = builder.createInWithName(ReferenceUsage.class, base, "visible");
        visible.setDirection(FeatureDirectionKind.IN);
        ActionDefinition child = builder.createWithName(ActionDefinition.class, "VisibilityChild");
        ActionDefinition parent = base;
        if (throughIntermediate) {
            // Insert an empty action between the declaration and the destination, retaining the same inherited signature.
            parent = builder.createWithName(ActionDefinition.class, "VisibilityIntermediate");
            builder.addSubclassification(parent, base);
        }
        builder.addSubclassification(child, parent);
        List<Relationship> sourceRelationships = List.copyOf(base.getOwnedRelationship());

        // WHEN selecting visible in Child through the public chain service.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(child, List.of(visible));

        // THEN the returned object is the local redefinition of visible, at its inherited position.
        assertChainSuccess(result);
        Feature selected = result.chain().getFirst();
        assertThat(result.chain()).hasSize(1);
        assertThat(selected.getOwnedRedefinition())
                .extracting(Redefinition::getRedefinedFeature)
                .containsExactly(visible);
        if (visibility == VisibilityKind.PRIVATE) {
            assertThat(child.getOwnedFeature()).containsExactly(selected);
        } else {
            assertThat(child.getOwnedFeature()).hasSize(2);
            assertThat(child.getOwnedFeature().getFirst().getOwnedRedefinition())
                    .extracting(Redefinition::getRedefinedFeature)
                    .containsExactly(hidden);
            assertThat(child.getOwnedFeature().getLast()).isSameAs(selected);
        }
        assertThat(base.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * An intermediate parameter explicitly mapped to a later ancestor position must never redirect a selection
     * of the earlier parameter. An unresolved position produces a structured failure and rolls back the prefix.
     *
     * @param throughPrefix
     *            whether a newly materialized action precedes the selected parameter in the chain
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a parent whose first declaration redefines the second ancestor input, WHEN selecting the first ancestor input, THEN an unresolved-position failure preserves the model")
    public void verifyUnresolvedPositionInPartialParent(boolean throughPrefix) {
        // GIVEN action def PartialBase { in ref p1; in ref p2; }
        // action def PartialParent :> PartialBase { in ref localP2 :>> PartialBase::p2; }
        // action def PartialChild :> PartialParent;
        ModelBuilder builder = new ModelBuilder();
        ActionDefinition base = builder.createWithName(ActionDefinition.class, "PartialBase");
        ReferenceUsage first = builder.createInWithName(ReferenceUsage.class, base, "p1");
        first.setDirection(FeatureDirectionKind.IN);
        ReferenceUsage second = builder.createInWithName(ReferenceUsage.class, base, "p2");
        second.setDirection(FeatureDirectionKind.IN);
        ActionDefinition parent = builder.createWithName(ActionDefinition.class, "PartialParent");
        builder.addSubclassification(parent, base);
        ReferenceUsage replacement = builder.createInWithName(ReferenceUsage.class, parent, "localP2");
        replacement.setDirection(FeatureDirectionKind.IN);
        builder.addRedefinition(replacement, second);
        ActionDefinition child = builder.createWithName(ActionDefinition.class, "PartialChild");
        builder.addSubclassification(child, parent);
        assertThat(child.getFeature()).contains(first);
        Type context = child;
        List<Feature> chain = List.of(first);
        String parentLabel = "'PartialParent'";
        String contextLabel = "PartialChild";
        if (throughPrefix) {
            // Select work.p1 through part partialSite : PartialWrapper, where work : PartialChild is inherited.
            PartDefinition wrapper = builder.createWithName(PartDefinition.class, "PartialWrapper");
            ActionUsage work = builder.createInWithName(ActionUsage.class, wrapper, "work");
            builder.setType(work, child);
            PartUsage site = builder.createWithName(PartUsage.class, "partialSite");
            builder.setType(site, wrapper);
            context = site;
            chain = List.of(work, first);
            parentLabel = "'PartialWrapper::work'";
            contextLabel = "partialSite";
        }
        List<Relationship> contextRelationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> parentRelationships = List.copyOf(parent.getOwnedRelationship());
        List<Relationship> replacementRelationships = List.copyOf(replacement.getOwnedRelationship());

        // WHEN selecting p1, whose inherited position cannot be materialized by the positional metamodel lookup.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, chain);

        // THEN report the original selection and its parent hierarchy instead of returning a replacement of p2.
        int failedIndex = chain.size() - 1;
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.failureContext()).isSameAs(context);
        assertThat(result.failedSegmentIndex()).isEqualTo(failedIndex);
        assertThat(result.failedFeature()).isSameAs(first);
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment %s 'PartialBase::p1' in context '%s': the selected parameter has no matching inherited position in the parent behaviors [%s]. Check their parameter order and explicit redefinitions before trying again."
                        .formatted(failedIndex + 1, contextLabel, parentLabel));
        // Any action prefix created by the operation has been removed, and the explicit parent mapping is preserved.
        assertThat(context.getOwnedFeature()).isEmpty();
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(parent.getOwnedRelationship()).containsExactlyElementsOf(parentRelationships);
        assertThat(replacement.getOwnedRelationship()).containsExactlyElementsOf(replacementRelationships);
        assertThat(replacement.getOwnedRedefinition())
                .extracting(Redefinition::getRedefinedFeature)
                .containsExactly(second);
    }

    /**
     * An undirected explicit replacement must not be skipped when inspecting the required parameter prefix.
     * The failure preserves existing relationships, and assigning the missing direction makes the request succeed.
     *
     * @param replacementPosition
     *            the one-based inherited position replaced by an undirected local feature
     * @param selectedPosition
     *            the one-based position selected, either the replacement itself or a later required parameter
     */
    @ParameterizedTest
    @CsvSource({"1, 1", "2, 2", "2, 3"})
    @DisplayName("GIVEN an undirected local parameter replacement, WHEN materializing its position or a later one, THEN fail without duplicates and allow retry after correcting its direction")
    public void verifyUndirectedReplacementPreservesModel(int replacementPosition, int selectedPosition) {
        // GIVEN action def UndirectedBase { in ref p1; ... } and action def UndirectedChild :> UndirectedBase.
        // Child owns ref localReplacement :>> UndirectedBase::pN, deliberately without a direction.
        ModelBuilder builder = new ModelBuilder();
        ActionDefinition base = builder.createWithName(ActionDefinition.class, "UndirectedBase");
        List<Feature> parameters = new ArrayList<>();
        for (int index = 1; index <= selectedPosition; index++) {
            ReferenceUsage parameter = builder.createInWithName(ReferenceUsage.class, base, "p" + index);
            parameter.setDirection(FeatureDirectionKind.IN);
            parameters.add(parameter);
        }
        ActionDefinition child = builder.createWithName(ActionDefinition.class, "UndirectedChild");
        builder.addSubclassification(child, base);
        ReferenceUsage replacement = builder.createInWithName(ReferenceUsage.class, child, "localReplacement");
        Feature replaced = parameters.get(replacementPosition - 1);
        Feature selected = parameters.get(selectedPosition - 1);
        builder.addRedefinition(replacement, replaced);
        List<Relationship> childRelationships = List.copyOf(child.getOwnedRelationship());
        List<Relationship> replacementRelationships = List.copyOf(replacement.getOwnedRelationship());
        List<Relationship> baseRelationships = List.copyOf(base.getOwnedRelationship());

        // WHEN selecting the replaced parameter, or a later parameter requiring completion of its prefix.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(child, List.of(selected));

        // THEN identify the existing replacement and its missing direction before adding any parameters.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.failureContext()).isSameAs(child);
        assertThat(result.failedSegmentIndex()).isZero();
        assertThat(result.failedFeature()).isSameAs(selected);
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 1 'UndirectedBase::p%s' in context 'UndirectedChild': local feature 'UndirectedChild::localReplacement' redefines inherited parameters ['UndirectedBase::p%s'] but has no direction. Set a direction compatible with those parameters before trying again."
                        .formatted(selectedPosition, replacementPosition));
        assertThat(child.getOwnedFeature()).containsExactly(replacement);
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(childRelationships);
        assertThat(replacement.getOwnedRelationship()).containsExactlyElementsOf(replacementRelationships);
        assertThat(replacement.getDirection()).isNull();
        assertThat(base.getOwnedRelationship()).containsExactlyElementsOf(baseRelationships);

        // WHEN the user follows the diagnostic and supplies the inherited input direction, retry the same selection.
        replacement.setDirection(FeatureDirectionKind.IN);
        FeatureChainRedefinitionResult retry = this.service.redefineFeatureChain(child, List.of(selected));

        // THEN reuse the existing replacement, completing only the required prefix without duplicating its position.
        assertChainSuccess(retry);
        assertThat(child.getOwnedFeature()).hasSize(selectedPosition);
        assertThat(child.getOwnedFeature().get(replacementPosition - 1)).isSameAs(replacement);
        assertThat(retry.chain().getFirst().allRedefinedFeatures()).contains(selected);
        assertThat(replacement.getOwnedRelationship()).containsExactlyElementsOf(replacementRelationships);
    }

    /**
     * A parameter already owned by a context without inherited parameters needs no new feature or relationship (service contract).
     *
     * @param fixture
     *            the fixture filename without its extension
     * @param contextName
     *            the declaring type name relative to the fixture package
     * @param selectedName
     *            the qualified regular or result parameter name relative to the fixture package
     */
    @ParameterizedTest
    @CsvSource({"complex-parameter-redefinitions, Base, Base::input", "calculation-parameter-redefinitions, Compute, Compute::result"})
    @DisplayName("GIVEN an already owned regular or result parameter without inherited positions, WHEN selecting it, THEN it is returned without changing the model")
    public void verifyAlreadyOwnedParameter(String fixture, String contextName, String selectedName) throws IOException {
        // GIVEN The selected regular or result parameter already belongs to its declaring type.
        SysMLTestModel model = SysMLTestModel.load("/feature-chains/" + fixture + ".xmi");
        Type context = model.get(Type.class, MODEL + contextName);
        Feature selected = model.get(Feature.class, MODEL + selectedName);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> featureRelationships = List.copyOf(selected.getOwnedRelationship());

        // WHEN Request that same parameter in its own type.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Return its identity and preserve both the owner's and parameter's stored relationships.
        assertChainSuccess(result);
        assertThat(result.chain()).containsExactly(selected);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(selected.getOwnedRelationship()).containsExactlyElementsOf(featureRelationships);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.8, permits an inout source to be redefined with a non-null narrower direction.
     * The parameter must satisfy the input direction required by the other branch, regardless of which source was selected.
     *
     * @param selectedName
     *            the qualified source name selecting either branch at the shared first position
     */
    @ParameterizedTest
    @ValueSource(strings = {BASE_INPUT, OTHER_REQUEST})
    @DisplayName("GIVEN corresponding in and inout sources, WHEN selecting either source, THEN their common local redefinition uses the required input direction")
    public void verifyCompatibleDirections(String selectedName) throws IOException {
        // GIVEN At the first position Base requires in; Other is changed from in to inout.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "multiple");
        model.get(Feature.class, MODEL + OTHER_REQUEST).setDirection(FeatureDirectionKind.INOUT);
        Feature selected = model.get(Feature.class, MODEL + selectedName);

        // WHEN Select either branch's source at that shared position.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Use in, which satisfies both requirements, and keep both positional targets.
        assertChainSuccess(result);
        Feature local = result.chain().getFirst();
        assertThat(local.getDirection()).isEqualTo(FeatureDirectionKind.IN);
        this.assertTargets(local, List.of(model.get(Feature.class, MODEL + BASE_INPUT), model.get(Feature.class, MODEL + OTHER_REQUEST)));
        assertThat(context.getOwnedFeature()).hasSize(4);
    }

    /**
     * KerML 1.1 Beta 2, 7.4.7.2: an existing local parameter must redefine all corresponding behavior branches.
     * The explicit mapping is preserved and the metamodel supplies the other branch implicitly.
     */
    @Test
    @DisplayName("GIVEN a local input explicitly redefining one branch, WHEN selecting it, THEN the second target is inferred and the remaining parameters are created")
    public void verifyCompleteExistingBranchRelationships() throws IOException {
        // GIVEN localInput explicitly redefines Base::input; the second parent contributes Other::request.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "mergedPartial");
        Feature selected = model.get(Feature.class, MODEL + "mergedPartial::localInput");
        Redefinition existingRelation = selected.getOwnedRedefinition().getFirst();

        // WHEN Select the existing local input in the multiply inherited signature.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Keep its explicit relationship and let the metamodel infer the other target while completing the suffix.
        assertChainSuccess(result);
        assertThat(result.chain()).containsExactly(selected);
        // We have 4 feature here because of the multiple inheritances. If more than one inheritance redefine all parameters
        assertThat(context.getOwnedFeature()).hasSize(4);
        this.assertTargets(selected, List.of(model.get(Feature.class, MODEL + BASE_INPUT), model.get(Feature.class, MODEL + OTHER_REQUEST)));
        assertThat(selected.getOwnedRelationship()).containsExactly(existingRelation);
    }

    /**
     * KerML 1.1 Beta 2, 7.4.7.2-3: linear inheritance permits a local prefix with an inherited suffix.
     * Reuse the existing first parameter when extending that prefix, including renamed effective sources.
     *
     * @param contextName
     *            the action usage name relative to the fixture package
     * @param firstName
     *            the qualified effective source name of the first inherited parameter
     */
    @ParameterizedTest
    @CsvSource({"simple, Base::input", "linear, Base::input", "renamed, Renamed::renamed"})
    @DisplayName("GIVEN a first parameter already materialized through linear inheritance, WHEN selecting the second parameter, THEN the prefix is completed without duplicating its first member")
    public void verifyCompleteLinearPrefix(String contextName, String firstName) throws IOException {
        // GIVEN Create the first local parameter before selecting Base::output at position two.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + contextName);
        Feature first = model.get(Feature.class, MODEL + firstName);
        Feature selected = model.get(Feature.class, MODEL + "Base::output");
        // Establish a pre-existing local input whose identity must survive the second call.
        FeatureChainRedefinitionResult initial = this.service.redefineFeatureChain(context, List.of(first));
        assertChainSuccess(initial);

        // WHEN Extend the prefix by selecting the second parameter.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Reuse the first parameter and leave currentState inherited at position three.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(2);
        assertThat(context.getOwnedFeature().getFirst()).isSameAs(initial.chain().getFirst());
        this.assertTargets(context.getOwnedFeature().getFirst(), List.of(first));
        this.assertTargets(context.getOwnedFeature().getLast(), List.of(selected));
        assertThat(result.chain()).containsExactly(context.getOwnedFeature().getLast());
        assertThat(context.getInheritedFeature()).contains(model.get(Feature.class, MODEL + BASE_STATE));
    }

    /**
     * KerML 7.4.7.2: a parameter in the child redefines the effective parameter of its direct parent.
     * Once Multiple owns its merged parameters, Base and Other are reached transitively.
     */
    @Test
    @DisplayName("GIVEN a parent with a complete multiple-inheritance signature, WHEN configuring its child, THEN each local parameter redefines only its immediate source")
    public void verifyCompleteParentSignature() throws IOException {
        // GIVEN First materialize Multiple's four positions so the child inherits effective local replacements.
        SysMLTestModel model = this.loadModel();
        Type parent = model.get(Type.class, MODEL + "Multiple");
        Type child = model.get(Type.class, MODEL + "multiple");
        Feature input = model.get(Feature.class, MODEL + BASE_INPUT);
        Feature request = model.get(Feature.class, MODEL + OTHER_REQUEST);
        // After this call Multiple owns effective replacements; the child must target these intermediate features.
        FeatureChainRedefinitionResult parentResult = this.service.redefineFeatureChain(parent, List.of(input));
        assertChainSuccess(parentResult);
        Feature selected = parentResult.chain().getFirst();

        // WHEN Select the parent's newly materialized first parameter from its child.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(child, List.of(selected));

        // THEN Each child parameter directly targets its parent's replacement; Base and Other are reached transitively.
        assertChainSuccess(result);
        assertThat(child.getOwnedFeature()).hasSize(4);
        for (int index = 0; index < 4; index++) {
            Feature local = child.getOwnedFeature().get(index);
            this.assertTargets(local, List.of(parent.getOwnedFeature().get(index)));
            assertThat(local.getOwnedRelationship())
                    .noneMatch(Redefinition.class::isInstance);
        }
        assertThat(result.chain()).containsExactly(child.getOwnedFeature().getFirst());
        assertThat(result.chain().getFirst().allRedefinedFeatures()).contains(selected, input, request);
    }

    /**
     * KerML 1.1 Beta 2, 7.4.7.2, requires positional order; conflicting local replacements must not be selected arbitrarily.
     *
     * @param contextName
     *            the action usage containing reversed or duplicate positional mappings
     * @param reason
     *            the complete expected diagnostic cause for the conflicting local mappings
     */
    @ParameterizedTest
    @CsvSource({"reordered, local parameter 'ComplexParameterRedefinitions::reordered::localInput' is out of order: inherited position 1 follows position 2. Reorder the local parameters before trying again.",
        "ambiguous, multiple local parameters redefine position 1: 'ComplexParameterRedefinitions::ambiguous::firstInput' and 'ComplexParameterRedefinitions::ambiguous::secondInput'. Resolve the conflicting redefinitions before trying again."})
    @DisplayName("GIVEN reversed or ambiguous existing parameter redefinitions, WHEN selecting an inherited parameter, THEN the local conflict is reported and existing relationships are preserved")
    public void verifyConflictingLocalParameters(String contextName, String reason) throws IOException {
        // GIVEN Existing locals either reverse inherited positions or both redefine position one.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + contextName);
        Feature source = model.get(Feature.class, MODEL + BASE_STATE);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // WHEN Select currentState, which would normally require completing the prefix.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Report the existing mapping conflict and preserve all original relationships.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 1 'ComplexParameterRedefinitions::Base::currentState' in context '%s': %s".formatted(context.getQualifiedName(), reason));
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.8, requires direction relative to the destination, including conjugation.
     * This API-created conjugation retains the independent textual example's parameter declarations.
     */
    @Test
    @DisplayName("GIVEN a conjugated behavior context, WHEN selecting its second inherited parameter, THEN the created prefix has reversed input and output directions")
    public void verifyConjugatedDirections() throws IOException {
        // GIVEN Conjugate Base so input becomes out and output becomes in in the destination.
        SysMLTestModel model = this.loadModel();
        ActionUsage context = SysmlFactory.eINSTANCE.createActionUsage();
        context.setDeclaredName("conjugated");
        Conjugation conjugation = SysmlFactory.eINSTANCE.createConjugation();
        conjugation.setConjugatedType(context);
        conjugation.setOriginalType(model.get(Type.class, MODEL + "Base"));
        context.getOwnedRelationship().add(conjugation);
        Feature selected = model.get(Feature.class, MODEL + "Base::output");

        // WHEN Select Base::output, the second inherited position.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Create the two-position prefix using destination directions, not the source's stored directions.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature())
                .extracting(Feature::getDirection)
                .containsExactly(FeatureDirectionKind.OUT, FeatureDirectionKind.IN);
        this.assertTargets(context.getOwnedFeature().getFirst(), List.of(model.get(Feature.class, MODEL + BASE_INPUT)));
        this.assertTargets(context.getOwnedFeature().getLast(), List.of(selected));
        assertThat(result.chain()).containsExactly(context.getOwnedFeature().getLast());
    }

    /**
     * A cyclic behavior hierarchy cannot define a finite sequence of inherited parameter positions.
     *
     * @param redundantParent
     *            whether to add a direct Base typing alongside the cyclic ancestry
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a cyclic behavior hierarchy, WHEN materializing a parameter, THEN the cycle is identified without attaching any local feature")
    public void verifyCyclicBehaviorHierarchy(boolean redundantParent) throws IOException {
        // GIVEN Add Base -> Linear to the existing Linear -> Base hierarchy; optionally add another direct Base typing.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "linear");
        Type base = model.get(Type.class, MODEL + "Base");
        Subclassification cycle = SysmlFactory.eINSTANCE.createSubclassification();
        cycle.setSubclassifier((Classifier) base);
        cycle.setSuperclassifier(model.get(Classifier.class, MODEL + "Linear"));
        base.getOwnedRelationship().add(cycle);
        // Exercise cycle detection both with and without a direct shortcut from the occurrence to Base.
        if (redundantParent) {
            FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
            typing.setTypedFeature((Feature) context);
            typing.setType(base);
            context.getOwnedRelationship().add(typing);
        }
        List<Relationship> baseRelationships = List.copyOf(base.getOwnedRelationship());
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // WHEN Request Base::input through the resulting cycle.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(model.get(Feature.class, MODEL + BASE_INPUT)));

        // THEN Detect the cycle before querying an unbounded signature and preserve the context and Base.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 1 'ComplexParameterRedefinitions::Base::input' in context 'ComplexParameterRedefinitions::linear': the parameter inheritance contains a cycle through 'ComplexParameterRedefinitions::Linear'. Resolve the cyclic specialization before trying again.");
        assertThat(base.getOwnedRelationship()).containsExactlyElementsOf(baseRelationships);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * A diamond is complex under the service policy, but identical source objects must not produce duplicate redefinitions.
     */
    @Test
    @DisplayName("GIVEN a diamond sharing the same source parameters, WHEN selecting its first parameter, THEN all positions are materialized with one relationship per distinct source")
    public void verifyDiamond() throws IOException {
        // GIVEN Left and Right both inherit the same three Base parameter objects.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "diamond");
        Feature selected = model.get(Feature.class, MODEL + BASE_INPUT);

        // WHEN Select the first parameter through the diamond.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Complete all three positions under the branching policy, with one target per distinct source.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(3);
        for (int index = 0; index < 3; index++) {
            this.assertTargets(context.getOwnedFeature().get(index), List.of(model.get(Feature.class, MODEL + BASE_PARAMETERS.get(index))));
        }
    }

    /**
     * A later failure must preserve the original explicit mapping and remove any newly created sibling parameters.
     */
    @Test
    @DisplayName("GIVEN a local input explicitly mapped to one branch, WHEN a later segment is inaccessible, THEN its existing relationships and context remain unchanged")
    public void verifyFailedChainLeavesExistingRelationshipsUnchanged() throws IOException {
        // GIVEN Keep localInput's explicit Base mapping and snapshot the existing context relationships.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "mergedPartial");
        Feature selected = model.get(Feature.class, MODEL + "mergedPartial::localInput");
        Feature inaccessible = model.get(Feature.class, MODEL + "Container::work");
        List<Relationship> contextRelationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> featureRelationships = List.copyOf(selected.getOwnedRelationship());

        // WHEN Request localInput followed by Container::work, which is inaccessible below that input.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected, inaccessible));

        // THEN Discard induced sibling parameters while preserving the original local input and its explicit relationship.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 2 'ComplexParameterRedefinitions::Container::work' in context 'ComplexParameterRedefinitions::mergedPartial': the feature is not accessible from this context. Select an accessible feature chain again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(selected.getOwnedRelationship()).containsExactlyElementsOf(featureRelationships);
    }

    /**
     * Under the complex-inheritance policy a parameter after the selection is still mandatory.
     * The model adds Base::extra as an out parameter opposite Other::extra (in) at position four.
     * No local direction can satisfy both sources (KerML 8.3.3.3.8, validateRedefinitionDirectionConformance).
     */
    @Test
    @DisplayName("GIVEN a mandatory parameter after the selection that cannot be initialized, WHEN materializing a complex selection, THEN the later parameter is identified and all creations are discarded")
    public void verifyFailingLaterParameter() throws IOException {
        // GIVEN The fourth positions in Base and Other will require opposite directions.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "multiple");
        Feature source = model.get(Feature.class, MODEL + BASE_INPUT);
        // Add out extra to Base so its fourth position conflicts with Other's existing in extra.
        Feature extra = SysmlFactory.eINSTANCE.createAttributeUsage();
        extra.setDeclaredName("extra");
        extra.setDirection(FeatureDirectionKind.OUT);
        FeatureMembership extraMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        extraMembership.getOwnedRelatedElement().add(extra);
        model.get(Type.class, MODEL + "Base").getOwnedRelationship().add(extraMembership);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // Observe attachments so an empty final context cannot hide an operation that failed before creating anything.
        List<FeatureMembership> additions = new ArrayList<>();
        context.eAdapters().add(new AdapterImpl() {
            /**
             * Records memberships attached during the call so rollback can be checked after the failure.
             *
             * @param notification
             *            the EMF change event emitted by the parameter owner
             */
            @Override
            public void notifyChanged(Notification notification) {
                if (notification.getEventType() == Notification.ADD && notification.getNewValue() instanceof FeatureMembership membership) {
                    additions.add(membership);
                }
            }
        });

        // WHEN Select the first input; branching inheritance also requires processing the fourth position.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Report both fourth-position sources and remove every membership created before the conflict.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 1 'ComplexParameterRedefinitions::Base::input' in context 'ComplexParameterRedefinitions::multiple': inherited parameters ['ComplexParameterRedefinitions::Base::extra', 'ComplexParameterRedefinitions::Other::extra'] require incompatible or missing directions [out, in]. Resolve the parameter directions before trying again.");
        // The first three positions were attached before initialization failed at the fourth.
        assertThat(additions).hasSize(3).allSatisfy(membership -> assertThat(membership.eContainer()).isNull());
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * KerML 1.1 Beta 2, 7.4.7.2 and 8.3.3.3.8: corresponding parameters must have conforming directions.
     * An impossible merge rolls back parameters created at earlier positions.
     */
    @Test
    @DisplayName("GIVEN corresponding input and output parameters in different branches, WHEN selecting that position, THEN the incompatible sources are reported without modifying the model")
    public void verifyIncompatibleDirections() throws IOException {
        // GIVEN Base requires in and Incompatible requires out at the same first position.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "conflict");
        Feature source = model.get(Feature.class, MODEL + BASE_INPUT);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // WHEN Select that conflicting position.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Report both source directions and leave the context exactly as it was.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 1 'ComplexParameterRedefinitions::Base::input' in context 'ComplexParameterRedefinitions::conflict': inherited parameters ['ComplexParameterRedefinitions::Base::input', 'ComplexParameterRedefinitions::Incompatible::request'] require incompatible or missing directions [in, out]. Resolve the parameter directions before trying again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * Implied relationships still contribute independent branches and positional obligations.
     * KerML 1.1 Beta 2, 8.3.3.3.4, checkFeatureParameterRedefinition, does not exclude implied generals.
     *
     * @param implied
     *            whether to mark the added independent parent typing as implied
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN an independent explicit or implied parent, WHEN selecting the first parameter, THEN all positions and both branch targets are materialized")
    public void verifyIndependentDirectParent(boolean implied) throws IOException {
        // GIVEN Add Other as an independent parent of an action previously typed only by Base.
        SysMLTestModel model = this.loadModel();
        Feature context = model.get(Feature.class, MODEL + "simple");
        FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        typing.setTypedFeature(context);
        typing.setType(model.get(Type.class, MODEL + "Other"));
        typing.setIsImplied(implied);
        context.getOwnedRelationship().add(typing);
        Feature selected = model.get(Feature.class, MODEL + BASE_INPUT);
        Feature other = model.get(Feature.class, MODEL + OTHER_REQUEST);

        // WHEN Select Base::input with either an explicit or implied additional typing.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Complete four positions and merge both first-position targets in either case.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(4);
        this.assertTargets(result.chain().getFirst(), List.of(selected, other));
    }

    /**
     * An existing redefinition at position four needs a complete prefix even when the user selects position two.
     */
    @Test
    @DisplayName("GIVEN a local fourth parameter, WHEN selecting the second inherited parameter, THEN the third is created and the fourth is reused")
    public void verifyInheritedGapAfterSelection() throws IOException {
        // GIVEN The sole local parameter redefines Other's fourth position, although it is physically first for now.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "partialOther");
        Feature selected = model.get(Feature.class, MODEL + "Other::response");
        Feature fourth = context.getOwnedFeature().getFirst();

        // WHEN Select Other::response at position two.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Insert positions one through three before the existing fourth parameter, retaining its identity.
        assertChainSuccess(result);
        List<Feature> locals = context.getOwnedFeature();
        assertThat(locals).hasSize(4);
        this.assertTargets(locals.get(0), List.of(model.get(Feature.class, MODEL + OTHER_REQUEST)));
        this.assertTargets(locals.get(1), List.of(selected));
        this.assertTargets(locals.get(2), List.of(model.get(Feature.class, MODEL + "Other::status")));
        assertThat(locals.get(3)).isSameAs(fourth);
        assertThat(result.chain()).containsExactly(locals.get(1));
    }

    /**
     * KerML 1.1 Beta 2, 7.4.7.2: missing earlier positions must precede an existing later local redefinition.
     * Keep the existing feature and its relationships rather than replacing or renaming it.
     */
    @Test
    @DisplayName("GIVEN an existing local output with a missing input, WHEN selecting the final parameter, THEN the missing input is inserted before the reused output")
    public void verifyInsertBeforeExistingParameter() throws IOException {
        // GIVEN localOutput already maps to Base's second position; its name and relationships must survive insertion.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "partial");
        Feature existing = model.get(Feature.class, MODEL + "partial::localOutput");
        List<Relationship> relationships = List.copyOf(existing.getOwnedRelationship());

        // WHEN Select currentState at position three.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(model.get(Feature.class, MODEL + BASE_STATE)));

        // THEN Insert the missing input before localOutput and append currentState after it.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(3);
        assertThat(context.getOwnedFeature().get(1)).isSameAs(existing);
        assertThat(existing.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(existing.getDeclaredName()).isEqualTo("localOutput");
    }

    /**
     * KerML 1.1 Beta 2, 7.4.7.2, merges parameters of multiple behaviors by position, not by name.
     * Service policy materializes all parameters for a branching ancestry, even behind one immediate parent.
     *
     * @param contextName
     *            the action usage with direct or indirectly inherited independent branches
     */
    @ParameterizedTest
    @ValueSource(strings = {"multiple", "wrapped"})
    @DisplayName("GIVEN multiple behavior branches with differently named parameters, WHEN selecting the first parameter, THEN every position is materialized and redefines the corresponding parameters of both branches")
    public void verifyMultipleBranches(String contextName) throws IOException {
        // GIVEN Base and Other use different names at corresponding positions; wrapped reaches this branching indirectly.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + contextName);
        Feature selected = model.get(Feature.class, MODEL + BASE_INPUT);
        Feature other = model.get(Feature.class, MODEL + OTHER_REQUEST);

        // WHEN Select Base's input, then Other's corresponding request.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));
        FeatureChainRedefinitionResult secondSelection = this.service.redefineFeatureChain(context, List.of(other));

        // THEN Both selections resolve to one local first parameter; all four positions are materialized.
        assertChainSuccess(result);
        assertChainSuccess(secondSelection);
        List<Feature> locals = context.getOwnedFeature();
        assertThat(locals).hasSize(4);
        // Positional redefinitions are computed by the metamodel; no explicit relationships need to be stored.
        assertThat(locals).allSatisfy(local -> assertThat(local.getOwnedRelationship())
                .noneMatch(Redefinition.class::isInstance));
        for (int index = 0; index < 3; index++) {
            Feature base = model.get(Feature.class, MODEL + BASE_PARAMETERS.get(index));
            Feature additional = model.get(Feature.class, MODEL + List.of("Other::request", "Other::response", "Other::status").get(index));
            this.assertTargets(locals.get(index), List.of(base, additional));
            assertThat(locals.get(index).getDirection()).isEqualTo(base.getDirection());
        }
        this.assertTargets(locals.getLast(), List.of(model.get(Feature.class, MODEL + "Other::extra")));
        assertThat(result.chain()).containsExactly(locals.getFirst());
        assertThat(secondSelection.chain()).containsExactly(locals.getFirst());
    }

    /**
     * The parameter rule applies at the intermediate action context, independently of the selected chain's length.
     * The service returns the local path, excluding additional sibling parameters created for positional conformance.
     */
    @Test
    @DisplayName("GIVEN a nested action with multiple behavior branches, WHEN selecting work.input.value, THEN all sibling parameters are created and the terminal belongs to the selected input")
    public void verifyNestedSelection() throws IOException {
        // GIVEN The selected path crosses an inherited action, its first input and Payload::value.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "root");
        ActionUsage work = model.get(ActionUsage.class, MODEL + "Container::work");
        Feature input = model.get(Feature.class, MODEL + BASE_INPUT);
        Feature value = model.get(Feature.class, MODEL + "Payload::value");

        // WHEN Materialize work.input.value from the outer context.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(work, input, value));

        // THEN Return three selected segments while also creating the action's three unselected sibling parameters.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(3);
        Feature localWork = result.chain().getFirst();
        Feature localInput = result.chain().get(1);
        assertThat(localWork.getOwnedFeature()).hasSize(4);
        assertThat(localWork.getOwnedFeature().getFirst()).isSameAs(localInput);
        assertThat(result.chain().getLast().getOwningType()).isSameAs(localInput);
        this.assertTargets(localInput, List.of(input, model.get(Feature.class, MODEL + OTHER_REQUEST)));
        assertThat(localInput.getOwnedRelationship())
                .noneMatch(Redefinition.class::isInstance);
        this.assertTargets(result.chain().getLast(), List.of(value));
    }

    /**
     * Service policy classifies independent inheritance branches, regardless of isImplied or relationship order.
     * Starting from the textual Linear/Renamed/Wrapped examples, add a direct typing to their existing Base ancestor.
     * KerML 1.1 Beta 2, 7.4.7.2-3, permits a prefix for linear inheritance; a redundant edge must not force a suffix.
     * Wrapped still contains the independent Base and Other branches and must retain full materialization.
     *
     * @param contextName
     *            the action usage receiving a redundant direct Base typing
     * @param selectedName
     *            the qualified effective source parameter name relative to the fixture package
     * @param implied
     *            whether to mark the redundant typing as implied
     * @param first
     *            whether to insert the redundant typing before the existing relationships
     * @param count
     *            the expected number of local parameters after classifying independent branches
     */
    @ParameterizedTest
    @CsvSource({"linear, Base::input, false, false, 1",
                "linear, Base::input, true, false, 1",
                "linear, Base::input, false, true, 1",
                "linear, Base::input, true, true, 1",
                "renamed, Renamed::renamed, false, false, 1",
                "renamed, Renamed::renamed, true, true, 1",
                "wrapped, Base::input, false, false, 4",
                "wrapped, Base::input, true, true, 4"})
    @DisplayName("GIVEN a redundant direct ancestor, WHEN selecting the first parameter, THEN only independent branches determine whether the suffix is materialized")
    public void verifyRedundantDirectParent(String contextName, String selectedName, boolean implied, boolean first, int count) throws IOException {
        // GIVEN Add a direct Base typing alongside a parent that already reaches Base; vary relationship order and isImplied.
        SysMLTestModel model = this.loadModel();
        Feature context = model.get(Feature.class, MODEL + contextName);
        FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        typing.setTypedFeature(context);
        typing.setType(model.get(Type.class, MODEL + "Base"));
        typing.setIsImplied(implied);
        int insertionIndex = context.getOwnedRelationship().size();
        if (first) {
            insertionIndex = 0;
        }
        context.getOwnedRelationship().add(insertionIndex, typing);
        Feature selected = model.get(Feature.class, MODEL + selectedName);

        // WHEN Select the effective first parameter.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Redundant links keep linear cases short, while wrapped still requires all four positions.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(count);
        assertThat(result.chain()).containsExactly(context.getOwnedFeature().getFirst());
        assertThat(result.chain().getFirst().allRedefinedFeatures()).contains(selected, model.get(Feature.class, MODEL + BASE_INPUT));
        assertThat(context.getOwnedRelationship()).contains(typing);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.8: every source must satisfy direction conformance.
     * Discounting a redundant edge for classification must not discard its positional constraints.
     */
    @Test
    @DisplayName("GIVEN conflicting parameters on a parent and its redundant ancestor, WHEN selecting the first parameter, THEN both constraints are checked without changing the context")
    public void verifyRedundantParentConstraints() throws IOException {
        // GIVEN Change Renamed's first parameter to out and add a direct typing to Base, whose first parameter is in.
        SysMLTestModel model = this.loadModel();
        Feature context = model.get(Feature.class, MODEL + "renamed");
        Feature selected = model.get(Feature.class, MODEL + "Renamed::renamed");
        selected.setDirection(FeatureDirectionKind.OUT);
        FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        typing.setTypedFeature(context);
        typing.setType(model.get(Type.class, MODEL + "Base"));
        context.getOwnedRelationship().add(typing);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // WHEN Select the renamed parameter through the two paths.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Still check Base's direction even though its direct edge is redundant for branching classification.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 1 'ComplexParameterRedefinitions::Renamed::renamed' in context 'ComplexParameterRedefinitions::renamed': inherited parameters ['ComplexParameterRedefinitions::Renamed::renamed', 'ComplexParameterRedefinitions::Base::input'] require incompatible or missing directions [out, in]. Resolve the parameter directions before trying again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.2, treats results separately from positional parameters.
     * In complex inheritance all regular parameters and the common result are materialized (service policy).
     * shortcut: CalculationUsage.result is not implemented in SysON; inspect ReturnParameterMembership until that derived getter is implemented.
     *
     * @param contextName
     *            the simple or combined calculation usage name
     * @param sourceName
     *            the qualified ordinary or result parameter name relative to the fixture package
     * @param count
     *            the expected number of locally owned parameters, including the result
     */
    @ParameterizedTest
    @CsvSource({"calculation, Compute::result, 1",
                "combinedCalculation, Compute::operand, 3",
                "combinedCalculation, Compute::result, 3"})
    @DisplayName("GIVEN a calculation result parameter, WHEN materializing a simple result or a complex selection, THEN results are merged separately from the ordered regular parameters")
    public void verifyResultParameter(String contextName, String sourceName, int count) throws IOException {
        // GIVEN Compute and OtherCompute declare ordinary parameters and separate return memberships.
        SysMLTestModel model = SysMLTestModel.load("/feature-chains/calculation-parameter-redefinitions.xmi");
        Type context = model.get(Type.class, MODEL + contextName);
        Feature selected = model.get(Feature.class, MODEL + sourceName);

        // WHEN Select an ordinary parameter or result in the simple or combined calculation.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Keep one return slot distinct from the ordinary positions and merge its result targets when required.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(count);
        // ReturnParameterMembership determines the result role independently of ordinary parameter indices.
        List<Feature> returns = context.getOwnedFeature().stream()
                .filter(feature -> feature.getOwningFeatureMembership() instanceof ReturnParameterMembership)
                .toList();
        assertThat(returns).hasSize(1);
        assertThat(returns.getFirst().getDirection()).isEqualTo(FeatureDirectionKind.OUT);
        assertThat(((ReturnParameterMembership) returns.getFirst().getOwningFeatureMembership()).getOwnedMemberParameter()).isSameAs(returns.getFirst());
        if (count == 1) {
            this.assertTargets(returns.getFirst(), List.of(selected));
        } else {
            this.assertTargets(returns.getFirst(), List.of(model.get(Feature.class, MODEL + "Compute::result"), model.get(Feature.class, MODEL + "OtherCompute::otherResult")));
        }
        assertThat(result.chain()).singleElement().satisfies(local -> assertThat(local.redefines(selected)).isTrue());
    }

    /**
     * KerML 7.4.7.2: an owned prefix can redefine inherited parameters implicitly by position.
     * The metamodel supplies that relationship; the service must reuse the prefix without storing a duplicate.
     */
    @Test
    @DisplayName("GIVEN an implicitly redefining local prefix, WHEN selecting the next inherited parameter, THEN the prefix is reused and its relationships remain virtual")
    public void verifyReuseImplicitPrefix() throws IOException {
        // GIVEN: a local first parameter with no stored Redefinition, followed by the inherited suffix.
        SysMLTestModel model = this.loadModel();
        ActionUsage context = model.get(ActionUsage.class, MODEL + "linear");
        Feature first = model.get(Feature.class, MODEL + BASE_INPUT);
        Feature selected = model.get(Feature.class, MODEL + "Base::output");
        Feature local = (Feature) SysmlFactory.eINSTANCE.create(first.eClass());
        local.setDirection(first.getDirection());
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        membership.getOwnedRelatedElement().add(local);
        context.getOwnedRelationship().add(membership);

        // WHEN: extending that implicit prefix to the second inherited parameter.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN: the existing identity and virtual relationship are retained, and the remaining suffix stays inherited.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).containsExactly(local, result.chain().getFirst());
        assertThat(context.getParameter()).containsExactly(local, result.chain().getFirst(), model.get(Feature.class, MODEL + BASE_STATE));
        this.assertTargets(local, List.of(first));
        assertThat(local.getOwnedRelationship()).isEmpty();
        assertThat(local.getDeclaredName()).isNull();
        assertThat(local.getName()).isEqualTo("input");
    }

    /**
     * A reused action must keep its original relationships when a later segment fails after parameter insertion.
     */
    @Test
    @DisplayName("GIVEN an existing local action, WHEN a chain fails below a new parameter, THEN all inserted siblings are removed and the action is preserved")
    public void verifyRollbackUnderExistingAction() throws IOException {
        // GIVEN Materialize work first so it must survive any subsequent failed operation.
        SysMLTestModel model = this.loadModel();
        Type root = model.get(Type.class, MODEL + "root");
        Feature work = model.get(Feature.class, MODEL + "Container::work");
        // Commit the action prefix in an earlier successful call; the failing call must not remove it.
        FeatureChainRedefinitionResult prefix = this.service.redefineFeatureChain(root, List.of(work));
        assertChainSuccess(prefix);
        Feature localWork = prefix.chain().getFirst();
        List<Relationship> original = List.copyOf(localWork.getOwnedRelationship());
        Feature input = model.get(Feature.class, MODEL + BASE_INPUT);

        // WHEN Extend work through input, then request work again where it is inaccessible.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(root, List.of(work, input, work));

        // THEN Remove the new parameter siblings but retain the original work object and its relationships.
        assertThat(result.isSuccess()).isFalse();
        assertThat(root.getOwnedFeature()).contains(localWork);
        assertThat(localWork.getOwnedRelationship()).containsExactlyElementsOf(original);
    }

    /**
     * Exact-metaclass preservation is a service policy for newly created features, independently of the other positional sources.
     * An item parameter and a reference parameter can expose the same Payload type without having the same metaclass.
     *
     * @param selectedName
     *            the qualified source name selecting the item or reference usage at the shared first position
     */
    @ParameterizedTest
    @ValueSource(strings = {BASE_INPUT, OTHER_REQUEST})
    @DisplayName("GIVEN corresponding item and reference parameters, WHEN selecting either source before materialization, THEN its exact metaclass is used for the shared local replacement")
    public void verifySelectedMetaclassInMergedPosition(String selectedName) throws IOException {
        // GIVEN One first-position source is an item usage and the other is a reference usage.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "multiple");
        Feature selected = model.get(Feature.class, MODEL + selectedName);

        // WHEN Select either of these positionally corresponding sources.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Use the selected source's metaclass and retain both semantic targets on the replacement.
        assertChainSuccess(result);
        assertThat(result.chain().getFirst().eClass()).isSameAs(selected.eClass());
        assertThat(result.chain().getFirst().getDeclaredName()).isNull();
        this.assertTargets(result.chain().getFirst(), List.of(model.get(Feature.class, MODEL + BASE_INPUT), model.get(Feature.class, MODEL + OTHER_REQUEST)));
    }

    private void assertTargets(Feature local, List<Feature> sources) {
        assertThat(local.getOwnedRedefinition()).extracting(redefinition -> redefinition.getRedefinedFeature()).containsExactlyElementsOf(sources);
    }

    private SysMLTestModel loadModel() throws IOException {
        return SysMLTestModel.load("/feature-chains/complex-parameter-redefinitions.xmi");
    }
}
