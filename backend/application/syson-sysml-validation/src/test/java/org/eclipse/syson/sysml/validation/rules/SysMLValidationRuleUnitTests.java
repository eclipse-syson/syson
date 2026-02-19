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
package org.eclipse.syson.sysml.validation.rules;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the validation rules from the SysML specification. These are mostly sanity checks to help maintain the
 * rather large number of rules in {@link SysMLValidationRule}.
 *
 * @author flatombe
 */
public class SysMLValidationRuleUnitTests {

    private static List<SysMLValidationRule> allRules = Stream.of(SysMLValidationRule.values()).toList();

    @Test
    @DisplayName("Check number of implemented rules")
    public void testNumberOfImplementedRules() {
        assertThat(allRules).hasSize(383);
    }

    @Test
    @DisplayName("For each rule, the Java literal is the rule name from the specification")
    public void testRuleLiteralIsSameAsRuleName() {
        assertThat(allRules).allMatch(rule -> {
            // The Java literal for the rule.
            final String ruleLiteral = rule.name();
            // The rule name, which should be from the SysML specification.
            final String ruleName = rule.ruleName();

            return ruleLiteral.equals(ruleName);
        });
    }

    @Test
    @DisplayName("For each rule, the expression starts with 'aql:'")
    public void testRuleExpressionStartsWithAql() {
        assertThat(allRules).allMatch(rule -> rule.expression().startsWith("aql:"));
    }

    @Test
    @DisplayName("For each rule, the EClass is from the SysML EPackage")
    public void testRuleEClassIsFromSysML() {
        assertThat(allRules).allMatch(rule -> rule.eClass().getEPackage() == SysmlPackage.eINSTANCE);
    }

    @Test
    @DisplayName("Check usual naming pattern of rules")
    public void testRuleNameMatchesExpectedPatterns() {
        final List<SysMLValidationRule> outliers = Stream.of(SysMLValidationRule.checkIncludeUseCaseSpecialization,
                SysMLValidationRule.checkVerificationCaseSpecialization,
                SysMLValidationRule.validateSendActionParameters,
                SysMLValidationRule.validateSpecificationSpecificNotConjugated).toList();

        assertThat(allRules)
                .filteredOn(Predicate.not(outliers::contains))
                .allMatch(rule -> rule.ruleName().startsWith("check" + rule.eClass().getName())
                        || rule.ruleName().startsWith("validate" + rule.eClass().getName()));
    }
}
