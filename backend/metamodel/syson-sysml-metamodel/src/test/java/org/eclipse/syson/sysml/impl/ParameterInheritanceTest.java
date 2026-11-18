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
package org.eclipse.syson.sysml.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.helper.ImplicitParameterRedefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks effective parameter signatures and positional redefinitions through the public metamodel queries.
 *
 * @author Arthur Daussy
 */
public class ParameterInheritanceTest {

    /**
     * An empty intermediate type can hide the return path between the owners of two matching declarations.
     *
     * @param indirect
     *            whether the return path passes through an intermediate type
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN two anonymous inputs and results in a specialization cycle, WHEN resolving their names, THEN no circular targets are inferred")
    public void avoidMutualRedefinitionsInSpecializationCycle(boolean indirect) {
        // GIVEN both declarations match by position, even when one side inherits through an empty type.
        Behavior first = SysmlFactory.eINSTANCE.createFunction();
        Behavior second = SysmlFactory.eINSTANCE.createFunction();
        this.specialize(first, second);
        if (indirect) {
            Behavior intermediate = SysmlFactory.eINSTANCE.createFunction();
            this.specialize(second, intermediate);
            this.specialize(intermediate, first);
        } else {
            this.specialize(second, first);
        }
        Feature firstInput = this.addParameter(first, null, FeatureDirectionKind.IN);
        Feature secondInput = this.addParameter(second, null, FeatureDirectionKind.IN);
        Feature firstResult = this.addResult(first, null);
        Feature secondResult = this.addResult(second, null);

        for (Feature parameter : List.of(firstInput, secondInput, firstResult, secondResult)) {
            // WHEN deriving the relationships used by effective name lookup.
            List<Redefinition> relationships = parameter.getOwnedRedefinition();

            // THEN neither end can supply the other's name.
            assertThat(relationships).isEmpty();
            assertThat(parameter.getName()).isNull();
        }
    }

    @Test
    @DisplayName("GIVEN a specialization cycle with one declared input, WHEN reading effective features, THEN lookup terminates without a self-redefinition")
    public void avoidSelfRedefinitionInSpecializationCycle() {
        ActionDefinition first = SysmlFactory.eINSTANCE.createActionDefinition();
        ActionDefinition second = SysmlFactory.eINSTANCE.createActionDefinition();
        this.specialize(first, second);
        this.specialize(second, first);
        Feature input = this.addParameter(first, "input", FeatureDirectionKind.IN);

        assertThat(first.getParameter()).containsExactly(input);
        assertThat(second.getFeature()).containsExactly(input);
        assertThat(input.getOwnedRedefinition()).isEmpty();
    }

    /**
     * KerML 7.4.7.2-3: implicit positional redefinitions must be visible to redefinition and naming queries.
     * In multiple inheritance, an explicit relationship to one parent does not suppress the missing branch.
     */
    @ParameterizedTest
    @ValueSource(strings = {"Behavior", "Step", "ActionDefinition", "ActionUsage"})
    @DisplayName("GIVEN partially explicit parameter redefinitions, WHEN querying the local parameter, THEN the missing parent is inferred without duplicating or persisting relationships")
    public void completeMissingParentRedefinitions(String metaclass) {
        Type first = this.createType(metaclass);
        Feature input = this.addParameter(first, "input", FeatureDirectionKind.IN);
        Feature output = this.addParameter(first, "output", FeatureDirectionKind.OUT);
        Type second = this.createType(metaclass);
        Feature request = this.addParameter(second, "request", FeatureDirectionKind.IN);
        Feature response = this.addParameter(second, "response", FeatureDirectionKind.OUT);
        Type child = this.createType(metaclass);
        this.specialize(child, first);
        this.specialize(child, second);
        Feature local = this.addParameter(child, "local", FeatureDirectionKind.IN);
        Redefinition explicit = this.addRedefinition(local, input);
        Feature localOutput = this.addParameter(child, "localOutput", FeatureDirectionKind.OUT);

        List<Redefinition> redefinitions = local.getOwnedRedefinition();

        assertThat(redefinitions).extracting(Redefinition::getRedefinedFeature).containsExactly(input, request);
        assertThat(redefinitions.getFirst()).isSameAs(explicit);
        assertThat(redefinitions.getLast().isIsImplied()).isTrue();
        assertThat(local.redefines(request)).isTrue();
        assertThat(local.allRedefinedFeatures()).contains(local, input, request);
        assertThat(local.getOwnedRelationship()).containsExactly(explicit);
        assertThat(localOutput.getOwnedRedefinition()).extracting(Redefinition::getRedefinedFeature).containsExactly(output, response);
        assertThat(localOutput.getOwnedRelationship()).isEmpty();
        assertThat(this.getEffectiveParameters(child)).containsExactly(local, localOutput);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ActionUsage", "FlowUsage"})
    @DisplayName("GIVEN an inherited input with a value, WHEN querying the child, THEN its signature includes the input and its local argument is absent")
    public void excludeInheritedInputsFromArguments(String metaclass) {
        Type parent = this.createType(metaclass);
        ActionUsage child = (ActionUsage) this.createType(metaclass);
        this.specialize(child, parent);
        Feature input = this.addParameter(parent, "input", FeatureDirectionKind.IN);
        FeatureValue binding = SysmlFactory.eINSTANCE.createFeatureValue();
        LiteralInteger value = SysmlFactory.eINSTANCE.createLiteralInteger();
        binding.getOwnedRelatedElement().add(value);
        input.getOwnedRelationship().add(binding);

        assertThat(this.getEffectiveParameters(child)).containsExactly(input);
        assertThat(child.argument(1)).isNull();
    }

    /**
     * A private input and result belong to the declaring function's signature. An empty intermediate
     * function inherits neither, so its child cannot take a name from them.
     *
     * @param throughIntermediate
     *            whether an empty function separates the child from the private declarations
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN private parameters in a direct or indirect parent, WHEN resolving anonymous parameters, THEN neither inherits a private name")
    public void excludePrivateParametersFromInheritedSignature(boolean throughIntermediate) {
        // GIVEN the intermediate function inherits an empty signature.
        Behavior base = SysmlFactory.eINSTANCE.createFunction();
        Feature hidden = this.addParameter(base, "hidden", FeatureDirectionKind.IN);
        hidden.getOwningFeatureMembership().setVisibility(VisibilityKind.PRIVATE);
        Feature hiddenResult = this.addResult(base, "hiddenResult");
        hiddenResult.getOwningFeatureMembership().setVisibility(VisibilityKind.PRIVATE);
        Behavior intermediate = SysmlFactory.eINSTANCE.createFunction();
        this.specialize(intermediate, base);
        assertThat(intermediate.getParameter()).isEmpty();

        // WHEN the child supplies an anonymous input and result.
        Behavior child = SysmlFactory.eINSTANCE.createFunction();
        this.specialize(child, throughIntermediate ? intermediate : base);
        Feature input = this.addParameter(child, null, FeatureDirectionKind.IN);
        Feature result = this.addResult(child, null);

        // THEN the empty parent signature supplies neither a target nor a name.
        for (Feature parameter : List.of(input, result)) {
            assertThat(parameter.getOwnedRedefinition()).isEmpty();
            assertThat(parameter.getName()).isNull();
        }

        // WHEN a protected input is appended after the hidden input, it becomes the first inherited input.
        Feature visible = this.addParameter(base, "visible", FeatureDirectionKind.IN);
        visible.getOwningFeatureMembership().setVisibility(VisibilityKind.PROTECTED);

        // THEN the child's first input now matches that same first position in the parent.
        assertThat(intermediate.getParameter()).containsExactly(visible);
        assertThat(input.getOwnedRedefinition())
                .extracting(Redefinition::getRedefinedFeature)
                .containsExactly(visible);
        assertThat(input.getName()).isEqualTo("visible");
    }

    /**
     * A private prefix replaces public inputs without exposing those replacements to descendants.
     * Both the direct child and a child behind an empty intermediate action must select p3.
     *
     * @param throughIntermediate
     *            whether an empty action separates the selected input from the private prefix
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN two private input replacements, WHEN matching a descendant input, THEN it redefines the remaining public input p3")
    public void excludePrivateRedefinitionsFromDescendantTargets(boolean throughIntermediate) {
        // GIVEN the equivalent SysML model:
        // action def A { in p1; in p2; in p3; }
        // action def B :> A { private in redefines p1; private in redefines p2; }
        // action def C :> B;
        // The selected anonymous input is declared in C, or in D :> C when C stays empty.
        ActionDefinition base = SysmlFactory.eINSTANCE.createActionDefinition();
        Feature first = this.addParameter(base, "p1", FeatureDirectionKind.IN);
        Feature second = this.addParameter(base, "p2", FeatureDirectionKind.IN);
        Feature third = this.addParameter(base, "p3", FeatureDirectionKind.IN);
        ActionDefinition specialized = SysmlFactory.eINSTANCE.createActionDefinition();
        this.specialize(specialized, base);
        Feature privateFirst = this.addParameter(specialized, null, FeatureDirectionKind.IN);
        privateFirst.getOwningFeatureMembership().setVisibility(VisibilityKind.PRIVATE);
        Feature privateSecond = this.addParameter(specialized, null, FeatureDirectionKind.IN);
        privateSecond.getOwningFeatureMembership().setVisibility(VisibilityKind.PRIVATE);
        assertThat(privateFirst.getOwnedRedefinition())
                .extracting(Redefinition::getRedefinedFeature)
                .containsExactly(first);
        assertThat(privateSecond.getOwnedRedefinition())
                .extracting(Redefinition::getRedefinedFeature)
                .containsExactly(second);
        ActionDefinition child = SysmlFactory.eINSTANCE.createActionDefinition();
        this.specialize(child, specialized);
        assertThat(child.getParameter()).containsExactly(third);
        if (throughIntermediate) {
            ActionDefinition descendant = SysmlFactory.eINSTANCE.createActionDefinition();
            this.specialize(descendant, child);
            child = descendant;
        }

        // WHEN resolving the first locally declared input of the selected descendant.
        Feature selected = this.addParameter(child, null, FeatureDirectionKind.IN);
        List<Redefinition> relationships = selected.getOwnedRedefinition();

        // THEN p3 supplies the name and target, and neither private replacement is exposed.
        assertThat(relationships)
                .extracting(Redefinition::getRedefinedFeature)
                .containsExactly(third);
        assertThat(selected.getName()).isEqualTo("p3");
        assertThat(child.getParameter()).containsExactly(selected);
    }

    /**
     * Private declarations occupy positions in their owner's signature. Once inherited, only the visible
     * suffix participates in the next generation's positional matching.
     */
    @Test
    @DisplayName("GIVEN a private replacement followed by an inherited protected input, WHEN matching through an empty parent, THEN the protected input occupies the first position")
    public void findFirstVisibleInheritedParameter() {
        // GIVEN the private local matches the base's first declaration, leaving a protected suffix.
        Behavior base = SysmlFactory.eINSTANCE.createBehavior();
        Feature hidden = this.addParameter(base, "hidden", FeatureDirectionKind.IN);
        Feature visible = this.addParameter(base, "visible", FeatureDirectionKind.IN);
        visible.getOwningFeatureMembership().setVisibility(VisibilityKind.PROTECTED);
        Behavior specialized = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(specialized, base);
        Feature replacement = this.addParameter(specialized, null, FeatureDirectionKind.IN);
        replacement.getOwningFeatureMembership().setVisibility(VisibilityKind.PRIVATE);
        assertThat(replacement.getName()).isEqualTo("hidden");
        Behavior intermediate = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(intermediate, specialized);
        assertThat(intermediate.getParameter()).containsExactly(visible);

        // WHEN a child matches its first input against the intermediate signature.
        Behavior child = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(child, intermediate);
        Feature local = this.addParameter(child, null, FeatureDirectionKind.IN);

        // THEN the protected input supplies both the positional target and the inherited name.
        assertThat(local.getOwnedRedefinition())
                .extracting(Redefinition::getRedefinedFeature)
                .containsExactly(visible);
        assertThat(local.getName()).isEqualTo("visible");
    }

    @Test
    @DisplayName("GIVEN a growing parameter prefix through fourteen generations, WHEN querying the leaf, THEN missing positions do not query ancestor signatures")
    public void findInheritedPositionsWithoutRecomputingSignatures() {
        AtomicInteger ancestorQueries = new AtomicInteger();
        ActionDefinition parent = null;
        for (int depth = 1; depth < 14; depth++) {
            ActionDefinition ancestor = new ActionDefinitionImpl() {
                @Override
                public EList<Feature> getParameter() {
                    ancestorQueries.incrementAndGet();
                    return super.getParameter();
                }
            };
            if (parent != null) {
                this.specialize(ancestor, parent);
            }
            for (int position = 0; position < depth; position++) {
                this.addParameter(ancestor, "p" + position, FeatureDirectionKind.IN);
            }
            parent = ancestor;
        }
        ActionDefinition leaf = SysmlFactory.eINSTANCE.createActionDefinition();
        this.specialize(leaf, parent);
        for (int position = 0; position < 14; position++) {
            this.addParameter(leaf, "p" + position, FeatureDirectionKind.IN);
        }

        assertThat(leaf.getParameter()).containsExactlyElementsOf(leaf.getOwnedFeature());
        assertThat(ancestorQueries.get()).isZero();
    }

    /**
     * KerML direction conformance allows a local input to narrow an inout parameter. It cannot redefine an output.
     */
    @Test
    @DisplayName("GIVEN parents with inout and out parameters, WHEN a child declares an input, THEN only the inout parameter is redefined")
    public void inferOnlyDirectionCompatibleTargets() {
        Behavior bidirectionalParent = SysmlFactory.eINSTANCE.createBehavior();
        Feature inout = this.addParameter(bidirectionalParent, "both", FeatureDirectionKind.INOUT);
        Behavior outputParent = SysmlFactory.eINSTANCE.createBehavior();
        Feature output = this.addParameter(outputParent, "output", FeatureDirectionKind.OUT);
        Behavior child = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(child, bidirectionalParent);
        this.specialize(child, outputParent);
        Feature input = this.addParameter(child, "input", FeatureDirectionKind.IN);

        assertThat(input.getOwnedRedefinition()).extracting(Redefinition::getRedefinedFeature).containsExactly(inout);
        assertThat(child.getParameter()).containsExactly(input, output);
    }

    /**
     * Each direct parent supplies its own effective signature when several branches are specialized.
     */
    @Test
    @DisplayName("GIVEN an empty inherited signature and a public input on another branch, WHEN matching a child input, THEN only the public input supplies a target")
    public void inferPublicTargetBesidePrivateAncestor() {
        // GIVEN one parent inherits no input, while the other declares a public input at position zero.
        Behavior base = SysmlFactory.eINSTANCE.createBehavior();
        Feature hidden = this.addParameter(base, "hidden", FeatureDirectionKind.IN);
        hidden.getOwningFeatureMembership().setVisibility(VisibilityKind.PRIVATE);
        Behavior empty = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(empty, base);
        Behavior other = SysmlFactory.eINSTANCE.createBehavior();
        Feature visible = this.addParameter(other, "visible", FeatureDirectionKind.IN);
        Behavior child = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(child, empty);
        this.specialize(child, other);

        // WHEN deriving the first input's targets across both branches.
        Feature input = this.addParameter(child, null, FeatureDirectionKind.IN);
        List<Redefinition> relationships = input.getOwnedRedefinition();

        // THEN the public input is the sole source of the inherited name.
        assertThat(relationships)
                .extracting(Redefinition::getRedefinedFeature)
                .containsExactly(visible);
        assertThat(input.getName()).isEqualTo("visible");
    }

    /**
     * Shared ancestor targets must be inferred once; inferred typing must be visible without persisting the relationship.
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a typed parameter inherited through a diamond with or without a direct specialization, WHEN querying an anonymous replacement, THEN one virtual target supplies the inherited name and type")
    public void inferSharedParameterTargetOnce(boolean directlySpecializesBase) {
        Behavior base = SysmlFactory.eINSTANCE.createBehavior();
        Feature source = this.addParameter(base, "source", FeatureDirectionKind.IN);
        Feature output = this.addParameter(base, "output", FeatureDirectionKind.OUT);
        Type valueType = SysmlFactory.eINSTANCE.createDataType();
        FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        typing.setTypedFeature(source);
        typing.setType(valueType);
        source.getOwnedRelationship().add(typing);
        Behavior left = SysmlFactory.eINSTANCE.createBehavior();
        Behavior right = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(left, base);
        this.specialize(right, base);
        Behavior child = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(child, left);
        this.specialize(child, right);
        if (directlySpecializesBase) {
            this.specialize(child, base);
        }
        Feature local = this.addParameter(child, null, FeatureDirectionKind.IN);

        assertThat(local.getOwnedRedefinition()).extracting(Redefinition::getRedefinedFeature).containsExactly(source);
        assertThat(local.getType()).containsExactly(valueType);
        assertThat(local.getName()).isEqualTo("source");
        assertThat(child.getParameter()).containsExactly(local, output);
        assertThat(local.getOwnedRelationship()).isEmpty();
    }

    /**
     * KerML 7.4.7.3 and Feature.namingFeature() let a step's unnamed parameter take its name from its virtual target.
     */
    @Test
    @DisplayName("GIVEN a typed action with one anonymous input, WHEN querying its signature, THEN the input inherits its name and the output remains inherited")
    public void inheritParameterNameInTypedAction() {
        ActionDefinition definition = SysmlFactory.eINSTANCE.createActionDefinition();
        Feature input = this.addParameter(definition, "p1", FeatureDirectionKind.IN);
        Feature output = this.addParameter(definition, "p2", FeatureDirectionKind.OUT);
        ActionUsage action = SysmlFactory.eINSTANCE.createActionUsage();
        FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        typing.setTypedFeature(action);
        typing.setType(definition);
        action.getOwnedRelationship().add(typing);
        Feature local = this.addParameter(action, null, FeatureDirectionKind.IN);

        assertThat(local.getOwnedRedefinition()).extracting(Redefinition::getRedefinedFeature).containsExactly(input);
        assertThat(local.getOwnedSpecialization()).filteredOn(Redefinition.class::isInstance).extracting(Specialization::getGeneral).containsExactly(input);
        assertThat(local.redefines(input)).isTrue();
        assertThat(local.getName()).isEqualTo("p1");
        assertThat(action.getParameter()).containsExactly(local, output);
        assertThat(local.getOwnedRelationship()).isEmpty();
    }

    /**
     * KerML checkFeatureResultRedefinition is separate from ordinary positional correspondence.
     * A return declared before an input must not shift that input's index in its parent's signature.
     */
    @Test
    @DisplayName("GIVEN a result declared before an input, WHEN deriving implicit redefinitions, THEN the result and ordinary input match independently")
    public void matchResultsIndependentlyOfInputPosition() {
        Type parent = SysmlFactory.eINSTANCE.createFunction();
        Feature input = this.addParameter(parent, "input", FeatureDirectionKind.IN);
        Feature result = this.addResult(parent, "result");
        Type child = SysmlFactory.eINSTANCE.createFunction();
        this.specialize(child, parent);
        Feature localResult = this.addResult(child, "localResult");
        Feature localInput = this.addParameter(child, "localInput", FeatureDirectionKind.IN);

        assertThat(localInput.getOwnedRedefinition()).extracting(Redefinition::getRedefinedFeature).containsExactly(input);
        assertThat(localResult.getOwnedRedefinition()).extracting(Redefinition::getRedefinedFeature).containsExactly(result);
        assertThat(this.getEffectiveParameters(child)).containsExactly(localResult, localInput);
    }

    /**
     * KerML checkFeatureParameterRedefinition excludes invocation arguments with an explicit mapping, even when another parent has a parameter at the same position.
     */
    @Test
    @DisplayName("GIVEN an explicitly mapped invocation argument, WHEN querying redefinitions, THEN no additional positional target is inferred")
    public void preserveExplicitInvocationArgument() {
        Behavior parent = SysmlFactory.eINSTANCE.createBehavior();
        Feature first = this.addParameter(parent, "first", FeatureDirectionKind.IN);
        Behavior other = SysmlFactory.eINSTANCE.createBehavior();
        this.addParameter(other, "otherInput", FeatureDirectionKind.IN);
        InvocationExpression invocation = SysmlFactory.eINSTANCE.createInvocationExpression();
        this.specialize(invocation, parent);
        this.specialize(invocation, other);
        Feature argument = this.addParameter(invocation, "argument", FeatureDirectionKind.IN);
        Redefinition explicit = this.addRedefinition(argument, first);

        assertThat(argument.getOwnedRedefinition()).containsExactly(explicit);
    }

    /**
     * A specialization cycle on one branch must not hide a valid positional target on another.
     */
    @Test
    @DisplayName("GIVEN a cyclic parent and an independent parent, WHEN resolving a local input, THEN only the independent parent supplies its name")
    public void preserveIndependentTargetBesideSpecializationCycle() {
        // GIVEN the cyclic parent declares the same position as an independent named input.
        Behavior child = SysmlFactory.eINSTANCE.createBehavior();
        Behavior cyclic = SysmlFactory.eINSTANCE.createBehavior();
        Behavior independent = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(child, cyclic);
        this.specialize(cyclic, child);
        this.specialize(child, independent);
        Feature local = this.addParameter(child, null, FeatureDirectionKind.IN);
        this.addParameter(cyclic, null, FeatureDirectionKind.IN);
        Feature source = this.addParameter(independent, "request", FeatureDirectionKind.IN);

        // WHEN resolving the anonymous input across both branches.
        List<Redefinition> relationships = local.getOwnedRedefinition();

        // THEN only the independent input supplies a target and an effective name.
        assertThat(relationships)
                .extracting(Redefinition::getRedefinedFeature)
                .containsExactly(source);
        assertThat(local.getName()).isEqualTo("request");
    }

    /**
     * A local parameter can explicitly select a later position. Preserve the selected target when inferring relationships.
     */
    @Test
    @DisplayName("GIVEN a first parameter explicitly mapped to the second inherited input, WHEN querying redefinitions, THEN its mapping is preserved")
    public void preserveReorderedExplicitParameter() {
        Behavior parent = SysmlFactory.eINSTANCE.createBehavior();
        this.addParameter(parent, "first", FeatureDirectionKind.IN);
        Feature second = this.addParameter(parent, "second", FeatureDirectionKind.IN);
        Behavior child = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(child, parent);
        Feature local = this.addParameter(child, "local", FeatureDirectionKind.IN);
        Redefinition explicit = this.addRedefinition(local, second);

        assertThat(local.getOwnedRedefinition()).containsExactly(explicit);
    }

    /**
     * Branches can reach the same type and visible position after skipping different prefixes.
     * The lookup cache must retain that distinction.
     */
    @Test
    @DisplayName("GIVEN a diamond with a private replacement on one branch, WHEN matching an input, THEN each branch retains its own positional target")
    public void retainDistinctSkippedPrefixesInSharedAncestor() {
        // GIVEN A has p1 and p2, Left privately replaces p1, and Right inherits both unchanged.
        Behavior base = SysmlFactory.eINSTANCE.createBehavior();
        Feature first = this.addParameter(base, "p1", FeatureDirectionKind.IN);
        Feature second = this.addParameter(base, "p2", FeatureDirectionKind.IN);
        Behavior left = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(left, base);
        Feature replacement = this.addParameter(left, null, FeatureDirectionKind.IN);
        replacement.getOwningFeatureMembership().setVisibility(VisibilityKind.PRIVATE);
        Behavior right = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(right, base);
        Behavior child = SysmlFactory.eINSTANCE.createBehavior();
        this.specialize(child, left);
        this.specialize(child, right);
        Feature selected = this.addParameter(child, null, FeatureDirectionKind.IN);

        // WHEN matching the first input through Left then Right.
        ImplicitParameterRedefinition computation = new ImplicitParameterRedefinition(selected);
        List<Feature> targets = computation.findMissingRedefinitionTargets(List.of());

        // THEN Left contributes p2, while Right contributes p1.
        assertThat(targets).containsExactly(second, first);

        // WHEN Right subsequently stops specializing A, the same helper must observe the edited model.
        right.getOwnedRelationship().clear();

        // THEN no target from the previous invocation is retained.
        assertThat(computation.findMissingRedefinitionTargets(List.of())).containsExactly(second);
    }

    /**
     * Six stacked diamonds provide 64 paths to the same base. Positional lookup must reuse the
     * base lookup, including an empty result, within one computation.
     *
     * @param hasSource
     *            whether the common base declares an input at the requested position
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN stacked inheritance diamonds, WHEN looking up one input, THEN the shared base is searched once even when the position is absent")
    public void reuseSharedAncestorParameterLookup(boolean hasSource) {
        // GIVEN each level introduces Left :> Tip, Right :> Tip and Merge :> Left, Right.
        AtomicInteger baseVisits = new AtomicInteger();
        Behavior base = new BehaviorImpl() {
            @Override
            public EList<Feature> getOwnedFeature() {
                baseVisits.incrementAndGet();
                return super.getOwnedFeature();
            }
        };
        Feature source = hasSource ? this.addParameter(base, "source", FeatureDirectionKind.IN) : null;
        Behavior tip = base;
        for (int depth = 0; depth < 6; depth++) {
            Behavior left = SysmlFactory.eINSTANCE.createBehavior();
            Behavior right = SysmlFactory.eINSTANCE.createBehavior();
            Behavior merged = SysmlFactory.eINSTANCE.createBehavior();
            this.specialize(left, tip);
            this.specialize(right, tip);
            this.specialize(merged, left);
            this.specialize(merged, right);
            tip = merged;
        }
        Feature selected = this.addParameter(tip, null, FeatureDirectionKind.IN);
        baseVisits.set(0);

        // WHEN deriving targets without unrelated effective-signature and naming queries.
        List<Feature> targets = new ImplicitParameterRedefinition(selected).findMissingRedefinitionTargets(List.of());

        // THEN both successful and unsuccessful lookups visit the common base once.
        assertThat(targets).containsExactlyElementsOf(hasSource ? List.of(source) : List.of());
        assertThat(baseVisits.get()).isEqualTo(1);
    }

    /**
     * Virtual relationships are recomputed after serialization; querying them must not add containment to the model.
     */
    @Test
    @DisplayName("GIVEN an implicitly redefining prefix, WHEN saving and reloading XMI, THEN the same semantic relationships are inferred without persisted redirections")
    public void recomputeImpliedRedefinitionsAfterReload() throws IOException {
        Behavior parent = SysmlFactory.eINSTANCE.createBehavior();
        parent.setDeclaredName("Parent");
        Feature source = this.addParameter(parent, "source", FeatureDirectionKind.IN);
        Behavior child = SysmlFactory.eINSTANCE.createBehavior();
        child.setDeclaredName("Child");
        this.specialize(child, parent);
        Feature local = this.addParameter(child, null, FeatureDirectionKind.IN);
        assertThat(local.redefines(source)).isTrue();
        XMIResourceImpl resource = new XMIResourceImpl();
        resource.getContents().addAll(List.of(parent, child));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        resource.save(output, Map.of());
        XMIResourceImpl loaded = new XMIResourceImpl();
        loaded.load(new ByteArrayInputStream(output.toByteArray()), Map.of());

        Type loadedParent = (Type) loaded.getContents().getFirst();
        Type loadedChild = (Type) loaded.getContents().getLast();
        Feature loadedLocal = loadedChild.getOwnedFeature().getFirst();
        assertThat(loadedLocal.redefines(loadedParent.getOwnedFeature().getFirst())).isTrue();
        assertThat(loadedLocal.getName()).isEqualTo("source");
        assertThat(loadedLocal.getOwnedRelationship()).isEmpty();
    }

    /**
     * KerML 7.4.7.2 allows an intermediate behavior to inherit the whole signature without declaring parameters.
     */
    @Test
    @DisplayName("GIVEN C specializes B and B inherits both parameters from A, WHEN C declares both positions, THEN each local replaces the corresponding parameter of A")
    public void redefineParametersThroughIntermediateAction() {
        ActionDefinition base = SysmlFactory.eINSTANCE.createActionDefinition();
        Feature input = this.addParameter(base, "p1", FeatureDirectionKind.IN);
        Feature output = this.addParameter(base, "p2", FeatureDirectionKind.OUT);
        ActionDefinition intermediate = SysmlFactory.eINSTANCE.createActionDefinition();
        this.specialize(intermediate, base);
        ActionDefinition child = SysmlFactory.eINSTANCE.createActionDefinition();
        this.specialize(child, intermediate);
        Feature localInput = this.addParameter(child, "local1", FeatureDirectionKind.IN);
        Feature localOutput = this.addParameter(child, "local2", FeatureDirectionKind.OUT);

        assertThat(intermediate.getParameter()).containsExactly(input, output);
        assertThat(intermediate.getOwnedFeature()).isEmpty();
        assertThat(localInput.getOwnedRedefinition()).extracting(Redefinition::getRedefinedFeature).containsExactly(input);
        assertThat(localOutput.getOwnedRedefinition()).extracting(Redefinition::getRedefinedFeature).containsExactly(output);
        assertThat(child.getFeature()).containsExactly(localInput, localOutput);
        assertThat(child.getParameter()).containsExactly(localInput, localOutput);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.4.6.2-3: parameters are directed features, including inherited features.
     * A local replacement precedes the inherited suffix (7.4.7.2); the replaced source must disappear.
     */
    @ParameterizedTest
    @ValueSource(strings = {"Behavior", "Step", "ActionDefinition", "ActionUsage", "Interaction", "Flow", "FlowUsage", "ConstraintDefinition", "ConstraintUsage"})
    @DisplayName("GIVEN inherited parameters and a local prefix, WHEN querying parameters, THEN inherited identities are retained and the replaced source is hidden")
    public void replaceInheritedParameterPrefix(String metaclass) {
        Type parent = this.createType(metaclass);
        Feature first = this.addParameter(parent, "first", FeatureDirectionKind.IN);
        Feature second = this.addParameter(parent, "second", FeatureDirectionKind.OUT);
        Type child = this.createType(metaclass);
        this.specialize(child, parent);

        List<Feature> inherited = this.getEffectiveParameters(child);

        assertThat(inherited).containsExactly(first, second);
        assertThat(child.getOwnedFeature()).isEmpty();

        Feature local = this.addParameter(child, "local", FeatureDirectionKind.IN);
        Redefinition redefinition = this.addRedefinition(local, first);

        assertThat(local.getOwnedRedefinition()).containsExactly(redefinition);
        assertThat(this.getEffectiveParameters(child)).containsExactly(local, second);
        assertThat(this.getEffectiveParameters(parent)).containsExactly(first, second);
    }

    /**
     * KerML 1.1 Beta 2, 7.3.2.4: conjugation reverses directions relative to the owner without copying parameters.
     */
    @ParameterizedTest
    @ValueSource(strings = {"Behavior", "Step", "ActionDefinition", "ActionUsage"})
    @DisplayName("GIVEN a conjugated behavior or step, WHEN querying parameters, THEN original parameter identities are returned with contextual directions")
    public void reverseInheritedParameterDirection(String metaclass) {
        Type original = this.createType(metaclass);
        Feature parameter = this.addParameter(original, "request", FeatureDirectionKind.IN);
        Type conjugated = this.createType(metaclass);
        Conjugation conjugation = SysmlFactory.eINSTANCE.createConjugation();
        conjugation.setConjugatedType(conjugated);
        conjugation.setOriginalType(original);
        conjugated.getOwnedRelationship().add(conjugation);

        List<Feature> parameters = this.getEffectiveParameters(conjugated);

        assertThat(parameters).containsExactly(parameter);
        assertThat(conjugated.directionOf(parameter)).isEqualTo(FeatureDirectionKind.OUT);
        assertThat(parameter.getDirection()).isEqualTo(FeatureDirectionKind.IN);

        Type child = this.createType(metaclass);
        this.specialize(child, conjugated);
        Feature output = this.addParameter(child, "reply", FeatureDirectionKind.OUT);

        assertThat(output.getOwnedRedefinition()).extracting(Redefinition::getRedefinedFeature).containsExactly(parameter);
    }

    private Feature addParameter(Type owner, String name, FeatureDirectionKind direction) {
        Feature feature = owner instanceof ActionDefinition || owner instanceof ActionUsage
                ? SysmlFactory.eINSTANCE.createReferenceUsage() : SysmlFactory.eINSTANCE.createFeature();
        feature.setDeclaredName(name);
        feature.setDirection(direction);
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        membership.getOwnedRelatedElement().add(feature);
        owner.getOwnedRelationship().add(membership);
        return feature;
    }

    private Redefinition addRedefinition(Feature local, Feature source) {
        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefiningFeature(local);
        redefinition.setRedefinedFeature(source);
        local.getOwnedRelationship().add(redefinition);
        return redefinition;
    }

    private Feature addResult(Type owner, String name) {
        Feature result = SysmlFactory.eINSTANCE.createFeature();
        result.setDeclaredName(name);
        result.setDirection(FeatureDirectionKind.OUT);
        ReturnParameterMembership membership = SysmlFactory.eINSTANCE.createReturnParameterMembership();
        membership.getOwnedRelatedElement().add(result);
        owner.getOwnedRelationship().add(membership);
        return result;
    }

    private Type createType(String metaclass) {
        return (Type) SysmlFactory.eINSTANCE.create((EClass) SysmlPackage.eINSTANCE.getEClassifier(metaclass));
    }

    private List<Feature> getEffectiveParameters(Type type) {
        if (type instanceof Behavior behavior) {
            return behavior.getParameter();
        }
        return ((Step) type).getParameter();
    }

    private void specialize(Type child, Type parent) {
        Specialization specialization = child instanceof Classifier && parent instanceof Classifier
                ? SysmlFactory.eINSTANCE.createSubclassification() : SysmlFactory.eINSTANCE.createSpecialization();
        specialization.setSpecific(child);
        specialization.setGeneral(parent);
        child.getOwnedRelationship().add(specialization);
    }
}
