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
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
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
