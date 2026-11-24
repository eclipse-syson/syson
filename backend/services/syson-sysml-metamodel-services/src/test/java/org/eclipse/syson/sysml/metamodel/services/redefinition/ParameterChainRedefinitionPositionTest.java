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

import java.io.IOException;
import java.util.List;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Checks the identity and order of positional parameter redefinition targets.
 *
 * @author Arthur Daussy
 */
public class ParameterChainRedefinitionPositionTest {

    private static final String BASE_STATE = "Base::currentState";

    private static final String MODEL = "ComplexParameterRedefinitions::";

    /**
     * KerML 1.1 Beta 2, 7.4.7.2-3: a parent may replace its first parameter and inherit the remaining suffix.
     * The service also identifies local parameters to reuse without changing their relationships.
     *
     * @param contextName
     *            the action usage whose direct parent may replace or rename inherited parameters
     * @param firstName
     *            the qualified effective first-position source expected as the direct redefinition target
     */
    @ParameterizedTest
    @CsvSource({"linear, Base::input",
                "renamed, Renamed::renamed",
                "partial, Base::input"})
    @DisplayName("GIVEN a linear parameter hierarchy, WHEN computing inherited positions, THEN the local parameters redefine the effective prefix and inherited suffix")
    public void verifyInheritedPrefixAndSuffix(String contextName, String firstName) throws IOException {
        // GIVEN The direct parent may rename or replace a position while retaining its inherited suffix.
        SysMLTestModel model = this.loadModel();
        Type context = model.get(Type.class, MODEL + contextName);

        // WHEN Select Base::currentState at the third position.
        FeatureChainRedefinitionResult result = new MetamodelMutationElementService().redefineFeatureChain(context, List.of(model.get(Feature.class, MODEL + BASE_STATE)));

        // THEN Each new local directly targets the effective source for its position, including a renamed replacement.
        assertThat(result.messages()).isEmpty();
        assertThat(context.getOwnedFeature().stream()
                .map(local -> local.getOwnedRedefinition().stream()
                        .map(Redefinition::getRedefinedFeature)
                        .toList()))
                .containsExactly(
                List.of(model.get(Feature.class, MODEL + firstName)),
                List.of(model.get(Feature.class, MODEL + "Base::output")),
                List.of(model.get(Feature.class, MODEL + BASE_STATE)));
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.4: corresponding ordinary parameters and results have separate redefinition obligations.
     * Compute contributes two ordinary positions, OtherCompute only one, and each contributes a return parameter.
     */
    @Test
    @DisplayName("GIVEN two calculation parents of different lengths, WHEN computing inherited positions, THEN sources are grouped by position and results are collected separately")
    public void verifyMergedPositionsAndResults() throws IOException {
        // GIVEN Compute has two ordinary positions; OtherCompute has one; each declares a result.
        SysMLTestModel model = SysMLTestModel.load("/feature-chains/calculation-parameter-redefinitions.xmi");
        Type context = model.get(Type.class, MODEL + "CombinedCompute");

        // WHEN Select the first ordinary parameter in their combined definition.
        FeatureChainRedefinitionResult result = new MetamodelMutationElementService().redefineFeatureChain(context, List.of(model.get(Feature.class, MODEL + "Compute::operand")));

        // THEN Merge the first ordinary position, retain Compute's second position, and merge results in a separate slot.
        assertThat(result.messages()).isEmpty();
        assertThat(context.getOwnedFeature().stream()
                .map(local -> local.getOwnedRedefinition().stream()
                        .map(Redefinition::getRedefinedFeature)
                        .toList()))
                .containsExactly(
                List.of(model.get(Feature.class, MODEL + "Compute::operand"), model.get(Feature.class, MODEL + "OtherCompute::otherOperand")),
                List.of(model.get(Feature.class, MODEL + "Compute::trace")),
                List.of(model.get(Feature.class, MODEL + "Compute::result"), model.get(Feature.class, MODEL + "OtherCompute::otherResult")));
    }

    private SysMLTestModel loadModel() throws IOException {
        return SysMLTestModel.load("/feature-chains/complex-parameter-redefinitions.xmi");
    }
}
