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
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.acceleo.query.runtime.Query;
import org.eclipse.acceleo.query.runtime.impl.QueryBuilderEngine;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.interpreter.Status;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    @DisplayName("For each rule, the AQL expression can be parsed")
    public void testRuleExpressionsCanBeParsed() {
        final var environment = Query.newEnvironmentWithDefaultServices(null);
        final var builder = new QueryBuilderEngine(environment);

        allRules.forEach(rule -> assertThatCode(() -> builder.build(rule.expression()
                .substring("aql:".length())))
                        .as(rule.ruleName())
                        .doesNotThrowAnyException());
    }

    /**
     * Checks class specialization semantics and ensures evaluation produces no AQL diagnostic.
     *
     * @param classType
     *            the type of class being validated
     * @param generalType
     *            the type of superclass, or {@code null} for a class without a superclass
     * @param expected
     *            whether the specialization is valid
     */
    @ParameterizedTest
    @MethodSource("classSpecializationCases")
    @DisplayName("Classes cannot specialize DataTypes and only Associations can specialize Associations")
    public void testClassSpecialization(EClass classType, EClass generalType, boolean expected) {
        var self = (Type) SysmlFactory.eINSTANCE.create(classType);
        if (generalType != null) {
            var specialization = SysmlFactory.eINSTANCE.createSpecialization();
            specialization.setSpecific(self);
            specialization.setGeneral((Type) SysmlFactory.eINSTANCE.create(generalType));
            self.getOwnedRelationship().add(specialization);
        }

        var interpreter = new AQLInterpreter(List.of(SysMLValidationRuleQueryServices.class), List.of(), List.of(SysmlPackage.eINSTANCE));
        var result = interpreter.evaluateExpression(Map.of("self", self), SysMLValidationRule.validateClassSpecialization.expression());

        assertThat(result.getStatus()).isEqualTo(Status.OK);
        assertThat(result.asBoolean()).contains(expected);
    }

    /**
     * Provides class specialization cases, including an empty superclass collection that exposes AQL scope errors.
     *
     * @return the class type, superclass type, and expected validity for each case
     */
    private static Stream<Arguments> classSpecializationCases() {
        var sysmlPackage = SysmlPackage.eINSTANCE;
        return Stream.of(
                Arguments.of(sysmlPackage.getClass_(), null, true),
                Arguments.of(sysmlPackage.getClass_(), sysmlPackage.getClass_(), true),
                Arguments.of(sysmlPackage.getClass_(), sysmlPackage.getDataType(), false),
                Arguments.of(sysmlPackage.getClass_(), sysmlPackage.getAssociation(), false),
                Arguments.of(sysmlPackage.getAssociationStructure(), sysmlPackage.getAssociation(), true),
                Arguments.of(sysmlPackage.getAssociationStructure(), sysmlPackage.getDataType(), false));
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
