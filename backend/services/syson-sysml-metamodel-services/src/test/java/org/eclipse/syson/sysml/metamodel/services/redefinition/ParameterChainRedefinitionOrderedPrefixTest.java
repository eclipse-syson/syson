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
import java.util.List;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks local parameter prefixes in linear inheritance.
 *
 * @author Arthur Daussy
 */
public class ParameterChainRedefinitionOrderedPrefixTest {

    private static final String MODEL = "SimpleParameterRedefinitions::";

    private static final String OCCURRENCE = MODEL + "processUse";

    private static final List<String> PARAMETERS = List.of("input", "output", "currentState", "extra");

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    /**
     * KerML 1.1 Beta 2, 7.4.7.2: extend the owned prefix while preserving existing local parameter identities.
     */
    @Test
    @DisplayName("GIVEN an already materialized first parameter, WHEN requesting a later parameter, THEN the prefix is completed while reusing the existing first parameter")
    public void verifyCompletePartialPrefix() throws IOException {
        // GIVEN The first input is already local; currentState is the third inherited position.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, OCCURRENCE);
        Feature first = model.get(Feature.class, MODEL + "Process::input");
        Feature selected = model.get(Feature.class, MODEL + "Process::currentState");
        // Establish a pre-existing local input whose identity must survive the second call.
        FeatureChainRedefinitionResult initial = this.service.redefineFeatureChain(context, List.of(first));
        assertChainSuccess(initial);

        // WHEN Extend the local prefix through currentState.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Keep the first local identity and create only the two missing positions.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(3);
        assertThat(context.getOwnedFeature().getFirst()).isSameAs(initial.chain().getFirst());
        assertThat(result.chain()).containsExactly(context.getOwnedFeature().getLast());
    }

    /**
     * KerML 1.1 Beta 2, 7.4.7.2-3 and 8.3.3.3.2, require positional parameter redefinitions for behaviors and steps.
     * With one super-behavior, a prefix may be owned while the remaining parameters stay inherited.
     * Non-directed members between parameters do not occupy parameter positions; only the selected feature is returned.
     *
     * @param contextName
     *            the occurrence or specialization name relative to the fixture package
     * @param position
     *            the one-based inherited position to select, also the expected local prefix length
     */
    @ParameterizedTest
    @CsvSource({"processUse, 1", "processUse, 2", "processUse, 3", "processUse, 4",
        "Specialized, 1", "Specialized, 2", "Specialized, 3", "Specialized, 4"})
    @DisplayName("GIVEN inherited ordered parameters, WHEN selecting one parameter, THEN only the necessary prefix is created in order and the returned chain contains the selection")
    public void verifyParameterPrefix(String contextName, int position) throws IOException {
        // GIVEN Process has four directed parameters, with unrelated undirected members between them.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + contextName);
        List<Feature> sources = PARAMETERS.stream()
                .map(name -> model.get(Feature.class, MODEL + "Process::" + name))
                .toList();
        Feature selected = sources.get(position - 1);
        List<List<Relationship>> sourceRelationships = sources.stream()
                .map(source -> List.copyOf(source.getOwnedRelationship()))
                .toList();

        // WHEN Select a position, then repeat the same request to check reuse.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));
        FeatureChainRedefinitionResult repeated = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN Create exactly the required prefix; every suffix position remains inherited.
        assertChainSuccess(result);
        assertChainSuccess(repeated);
        List<Feature> locals = context.getOwnedFeature();
        assertThat(locals).hasSize(position);
        for (int index = 0; index < position; index++) {
            Feature source = sources.get(index);
            Feature local = locals.get(index);
            assertThat(local.eClass()).isSameAs(source.eClass());
            assertThat(local.getOwningType()).isSameAs(context);
            assertThat(local.getDirection()).isEqualTo(source.getDirection());
            assertThat(local.getOwningFeatureMembership().eClass()).isSameAs(source.getOwningFeatureMembership().eClass());
            assertThat(local.getDeclaredName()).isNull();
            assertThat(local.getDeclaredShortName()).isNull();
            assertThat(local.isIsComposite()).isFalse();
            assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(source));
            assertThat(local.getOwnedRelationship()).isEmpty();
        }
        assertThat(result.chain()).containsExactly(locals.getLast());
        assertThat(repeated.chain()).containsExactly(locals.getLast());
        // Only positions after the selected one remain inherited; the local prefix replaces its source objects.
        List<Feature> inherited = context.getFeatureMembership().stream()
                .map(membership -> membership.getOwnedMemberFeature())
                .toList();
        assertThat(inherited).containsAll(sources.subList(position, sources.size()));
        // The same declaration may configure other actions, so its original relationships must be preserved.
        for (int index = 0; index < sources.size(); index++) {
            assertThat(sources.get(index).getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships.get(index));
        }
    }

    /**
     * KerML 1.1 Beta 2, 7.4.7.2, also permits a prefix consisting of a single parameter.
     */
    @Test
    @DisplayName("GIVEN one inherited parameter, WHEN materializing it, THEN one anonymous local parameter is created")
    public void verifySingleParameter() throws IOException {
        // GIVEN OneParameter contributes a single input and the action has no local parameters.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "single");
        Feature source = model.get(Feature.class, MODEL + "OneParameter::value");

        // WHEN Materialize that input.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN The single local parameter occupies the source position and inherits its direction.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).containsExactlyElementsOf(result.chain());
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertThat(local.getDirection()).isEqualTo(source.getDirection());
            assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(source));
        });
    }

    /**
     * KerML 1.1 Beta 2, 7.4.3 and 7.3.4.5, require a redefinition of a variable source to be variable.
     * Creating an ordered prefix must propagate this requirement to both preceding and selected parameters.
     *
     * @param variableParameter
     *            the source parameter to make variable, either earlier or selected
     */
    @ParameterizedTest
    @ValueSource(strings = {"earlier", "selected"})
    @DisplayName("GIVEN a variable inherited parameter, WHEN materializing a selection requiring it, THEN the complete local prefix preserves its variability")
    public void verifyVariableParameterPrefix(String variableParameter) throws IOException {
        // GIVEN UnsupportedPrefix(firstParameter, earlier, selected), with one source made variable.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + "rejected");
        Type sourceOwner = model.get(Type.class, MODEL + "UnsupportedPrefix");
        Feature selected = model.get(Feature.class, MODEL + "UnsupportedPrefix::selected");
        Feature variableSource = model.get(Feature.class, MODEL + "UnsupportedPrefix::" + variableParameter);
        variableSource.setIsVariable(true);

        // WHEN selecting the third parameter, all three local positions must be created.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(selected));

        // THEN only the position whose source is variable becomes variable.
        assertChainSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(3);
        assertThat(result.chain()).containsExactly(context.getOwnedFeature().get(2));
        for (int index = 0; index < 3; index++) {
            Feature local = context.getOwnedFeature().get(index);
            Feature source = sourceOwner.getOwnedFeature().get(index);
            assertThat(local.allRedefinedFeatures()).contains(source);
            assertThat(local.isIsVariable()).isEqualTo(source == variableSource);
        }
    }

    private SysMLTestModel loadModel() throws IOException {
        return SysMLTestModel.load("/feature-chains/simple-parameter-redefinitions.xmi");
    }
}
