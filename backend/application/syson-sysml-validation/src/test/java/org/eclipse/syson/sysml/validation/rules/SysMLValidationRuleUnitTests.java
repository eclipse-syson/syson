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
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for the validation rules from the SysML specification. These are mostly sanity checks to help maintain the
 * rather large number of rules in {@link SysMLValidationRule}.
 *
 * @author flatombe
 */
public class SysMLValidationRuleUnitTests {

    private static List<SysMLValidationRule> allRules = Stream.of(SysMLValidationRule.values()).toList();

    private static final String HOMONYM = "x";

    /**
     * Owned/inherited name and short-name conflicts are validation errors in definitions and typed occurrences.
     * Adding and removing an explicit redefinition updates both validation and effective memberships.
     *
     * @param occurrence whether to validate a typed occurrence instead of a definition
     * @param inheritedShortName whether the inherited short name conflicts
     * @param ownedShortName whether the owned short name conflicts
     */
    @ParameterizedTest
    @CsvSource({"false, false, false", "false, false, true", "false, true, false", "false, true, true",
        "true, false, false", "true, false, true", "true, true, false", "true, true, true"})
    public void namespaceInheritedHomonyms(boolean occurrence, boolean inheritedShortName, boolean ownedShortName) {
        var base = SysmlFactory.eINSTANCE.createPartDefinition();
        var inherited = this.createAttribute(base, "inherited");
        final Type context;
        if (occurrence) {
            var usage = SysmlFactory.eINSTANCE.createPartUsage();
            var typing = SysmlFactory.eINSTANCE.createFeatureTyping();
            typing.setTypedFeature(usage);
            typing.setType(base);
            usage.getOwnedRelationship().add(typing);
            context = usage;
        } else {
            var derived = SysmlFactory.eINSTANCE.createPartDefinition();
            this.addSubclassification(derived, base);
            context = derived;
        }
        var owned = this.createAttribute(context, "owned");
        this.assertNamespaceDistinguishability(context, true);
        this.setConflictingName(inherited, inheritedShortName);
        this.setConflictingName(owned, ownedShortName);
        this.assertNamespaceDistinguishability(context, false);
        assertThat(context.getInheritedMembership()).containsExactly(inherited.getOwningMembership());
        assertThat(context.getFeatureMembership()).extracting(membership -> membership.getMemberElement()).containsExactlyInAnyOrder(owned, inherited);
        assertThat(context.getFeature()).containsExactlyInAnyOrder(owned, inherited);

        var redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefinedFeature(inherited);
        redefinition.setRedefiningFeature(owned);
        owned.getOwnedRelationship().add(redefinition);
        this.assertNamespaceDistinguishability(context, true);
        assertThat(context.getInheritedMembership()).isEmpty();
        assertThat(context.getFeature()).containsExactly(owned);

        owned.getOwnedRelationship().remove(redefinition);
        this.assertNamespaceDistinguishability(context, false);
        assertThat(context.getFeatureMembership()).extracting(membership -> membership.getMemberElement()).containsExactlyInAnyOrder(owned, inherited);
        assertThat(context.getFeature()).containsExactlyInAnyOrder(owned, inherited);
    }

    /**
     * Conflicting memberships from two bases are reported in either specialization order and remain accessible.
     *
     * @param reverse whether to reverse the specialization order
     * @param leftShortName whether the left inherited short name conflicts
     * @param rightShortName whether the right inherited short name conflicts
     */
    @ParameterizedTest
    @CsvSource({"false, false, false", "false, false, true", "false, true, false", "false, true, true",
        "true, false, false", "true, false, true", "true, true, false", "true, true, true"})
    public void namespaceConflictingInheritedMembers(boolean reverse, boolean leftShortName, boolean rightShortName) {
        var left = SysmlFactory.eINSTANCE.createPartDefinition();
        var right = SysmlFactory.eINSTANCE.createPartDefinition();
        var leftFeature = this.createAttribute(left, "left");
        var rightFeature = this.createAttribute(right, "right");
        var derived = SysmlFactory.eINSTANCE.createPartDefinition();
        if (reverse) {
            this.addSubclassification(derived, right);
            this.addSubclassification(derived, left);
        } else {
            this.addSubclassification(derived, left);
            this.addSubclassification(derived, right);
        }
        this.assertNamespaceDistinguishability(derived, true);
        this.setConflictingName(leftFeature, leftShortName);
        this.setConflictingName(rightFeature, rightShortName);
        this.assertNamespaceDistinguishability(derived, false);
        assertThat(derived.getInheritedMembership()).containsExactlyInAnyOrder(leftFeature.getOwningMembership(), rightFeature.getOwningMembership());
        assertThat(derived.getFeature()).containsExactlyInAnyOrder(leftFeature, rightFeature);
    }

    /**
     * The same membership inherited through both branches of a diamond does not conflict with itself.
     */
    @Test
    public void namespaceDiamondMembershipIsDistinguishable() {
        var base = SysmlFactory.eINSTANCE.createPartDefinition();
        var inherited = this.createAttribute(base, HOMONYM);
        var left = SysmlFactory.eINSTANCE.createPartDefinition();
        var right = SysmlFactory.eINSTANCE.createPartDefinition();
        this.addSubclassification(left, base);
        this.addSubclassification(right, base);
        var derived = SysmlFactory.eINSTANCE.createPartDefinition();
        this.addSubclassification(derived, left);
        this.addSubclassification(derived, right);

        this.assertNamespaceDistinguishability(derived, true);
        assertThat(derived.getInheritedMembership()).containsExactly(inherited.getOwningMembership());
        assertThat(derived.getFeature()).containsExactly(inherited);
    }

    /**
     * Evaluates the real namespace rule, checking that a false result is not caused by an AQL diagnostic.
     *
     * @param context the namespace to validate
     * @param expected whether its memberships are distinguishable
     */
    private void assertNamespaceDistinguishability(Type context, boolean expected) {
        var interpreter = new AQLInterpreter(List.of(SysMLValidationRuleQueryServices.class), List.of(), List.of(SysmlPackage.eINSTANCE));
        var result = interpreter.evaluateExpression(Map.of("self", context), SysMLValidationRule.validateNamespaceDistinguishibility.expression());
        assertThat(result.getStatus()).isEqualTo(Status.OK);
        assertThat(result.asBoolean()).contains(expected);
    }

    /**
     * Creates an owned attribute with its semantic feature membership.
     *
     * @param owner the owning type
     * @param name the initial distinct name
     * @return the owned attribute
     */
    private AttributeUsage createAttribute(Type owner, String name) {
        var attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        attribute.setDeclaredName(name);
        var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        membership.getOwnedRelatedElement().add(attribute);
        owner.getOwnedRelationship().add(membership);
        return attribute;
    }

    /**
     * Adds a real subclassification between the definitions being validated.
     *
     * @param specific the specializing definition
     * @param general the general definition
     */
    private void addSubclassification(PartDefinition specific, PartDefinition general) {
        var specialization = SysmlFactory.eINSTANCE.createSubclassification();
        specialization.setSubclassifier(specific);
        specialization.setSuperclassifier(general);
        specific.getOwnedRelationship().add(specialization);
    }

    /**
     * Sets the conflicting identifier while keeping the other identifier unchanged.
     *
     * @param attribute the attribute to rename
     * @param shortName whether to use its short name instead of its name
     */
    private void setConflictingName(AttributeUsage attribute, boolean shortName) {
        if (shortName) {
            attribute.setDeclaredShortName(HOMONYM);
        } else {
            attribute.setDeclaredName(HOMONYM);
        }
    }

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
