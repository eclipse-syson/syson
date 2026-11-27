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
package org.eclipse.syson.sysml.validation.rules;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.SysONSysMLValidator;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Checks the SysON registry and its integration with the default rules provider.
 *
 * @author adaussy
 */
public class SysONValidationRuleTests {

    @Test
    @DisplayName("GIVEN the default provider, WHEN requesting redefinition rules, THEN the SysON prerequisite precedes all its inherited dependents")
    public void getRedefinitionRules() {
        // GIVEN the unmodified application provider and its separate SysON rule registry.
        SysONValidationRulesProvider provider = new SysONValidationRulesProvider();
        IValidationRule prerequisite = SysONValidationRule.validateRedefinitionRequiredFeatures;

        // WHEN requesting every applicable rule, including rules inherited from Specialization and Subsetting.
        List<? extends IValidationRule> rules = provider.getValidationRules(SysmlPackage.eINSTANCE.getRedefinition());

        // THEN the prerequisite is first, every named exclusion exists, and the independent Element check is retained.
        assertThat(rules.getFirst())
                .isSameAs(prerequisite);
        assertThat(prerequisite.getRulesToIgnoreOnFailure())
                .hasSize(7);
        assertThat(rules.subList(1, rules.size()))
                .extracting(IValidationRule::ruleName)
                .containsAll(prerequisite.getRulesToIgnoreOnFailure())
                .contains(SysMLValidationRule.validateElementIsImpliedIncluded.ruleName());
        assertThat(provider.getValidationRules(SysmlPackage.eINSTANCE.getAttributeUsage()))
                .extracting(IValidationRule::ruleName)
                .doesNotContain(prerequisite.ruleName());
    }

    @ParameterizedTest
    @CsvSource({"true, false", "false, true", "false, false"})
    @DisplayName("GIVEN missing redefinition endpoints, WHEN using the full provider, THEN only one structural error is reported")
    public void validateIncompleteRedefinition(boolean hasLocal, boolean hasSource) {
        // GIVEN an incomplete stored relationship: one or both endpoint selections have not been made.
        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        if (hasLocal) {
            redefinition.setRedefiningFeature(SysmlFactory.eINSTANCE.createAttributeUsage());
        }
        if (hasSource) {
            redefinition.setRedefinedFeature(SysmlFactory.eINSTANCE.createAttributeUsage());
        }
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN validating through the actual application provider rather than an isolated rule fixture.
        boolean valid = new SysONSysMLValidator(new SysONValidationRulesProvider()).validate(redefinition, diagnostics, Map.of());

        // THEN inherited endpoint-dependent constraints do not create secondary business diagnostics.
        assertThat(valid)
                .isFalse();
        assertThat(diagnostics.getChildren())
                .singleElement()
                .satisfies((Diagnostic diagnostic) -> {
                    assertThat(diagnostic.getSource())
                            .isEqualTo(SysONValidationRule.validateRedefinitionRequiredFeatures.ruleName());
                    assertThat(diagnostic.getSeverity())
                            .isEqualTo(Diagnostic.ERROR);
                    assertThat(diagnostic.getData())
                            .isEqualTo(List.of(redefinition));
                });
    }

    @Test
    @DisplayName("GIVEN an incomplete redefinition with an independent violation, WHEN validating, THEN that violation is still reported")
    public void validateIndependentConstraint() {
        // GIVEN an incomplete relationship owning an implied relationship without isImpliedIncluded.
        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        Redefinition implied = SysmlFactory.eINSTANCE.createRedefinition();
        implied.setIsImplied(true);
        redefinition.getOwnedRelationship().add(implied);
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN the structural prerequisite excludes only rules navigating the missing endpoints.
        new SysONSysMLValidator(new SysONValidationRulesProvider()).validate(redefinition, diagnostics, Map.of());

        // THEN the independent Element constraint is evaluated alongside the structural error.
        assertThat(diagnostics.getChildren())
                .extracting(Diagnostic::getSource)
                .containsExactly(SysONValidationRule.validateRedefinitionRequiredFeatures.ruleName(),
                        SysMLValidationRule.validateElementIsImpliedIncluded.ruleName());
    }

    @Test
    @DisplayName("GIVEN a provider omitting the structural rule, WHEN validating an incomplete relationship, THEN the validator does not impose it")
    public void validateWithCustomizedProvider() {
        // GIVEN a consumer deliberately replacing the application rules with an empty selection.
        SysONValidationRulesProvider provider = new SysONValidationRulesProvider() {
            @Override
            public List<? extends IValidationRule> getValidationRules(EClass eClass) {
                return List.of();
            }
        };
        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN validating a relationship whose two endpoints are still absent.
        boolean valid = new SysONSysMLValidator(provider).validate(redefinition, diagnostics, Map.of());

        // THEN the provider remains the sole authority for which model constraints are applied.
        assertThat(valid)
                .isTrue();
        assertThat(diagnostics.getChildren())
                .isEmpty();
    }
}
