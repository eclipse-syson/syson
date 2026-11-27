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
package org.eclipse.syson.sysml.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.rules.ValidationRule;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRule;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRulesProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks generic rule exclusions independently of SysML relationship types.
 *
 * @author adaussy
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class ValidationRuleExclusionTests {

    @Test
    @DisplayName("GIVEN a violated prerequisite, WHEN validating an attribute, THEN only named dependent rules are skipped")
    public void validateFailedPrerequisite() {
        // GIVEN attribute speed, with a failed prerequisite, a dependent check and an independent violation.
        AttributeUsage feature = SysmlFactory.eINSTANCE.createAttributeUsage();
        IValidationRule prerequisite = this.createPrerequisite("aql:false");
        SysONSysMLValidator validator = this.createValidator(prerequisite);
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN the prerequisite is successfully evaluated as false.
        boolean valid = validator.validate(feature, diagnostics, Map.of());

        // THEN the dependent violation is absent while the independent check still runs.
        assertThat(valid)
                .isFalse();
        assertThat(diagnostics.getChildren())
                .extracting(Diagnostic::getSource)
                .containsExactly("prerequisite", "independent");
    }

    @ParameterizedTest
    @ValueSource(strings = {"aql:true", "aql:null", "aql:42", "aql:self.missingOperation()", "aql:Sequence{self.missingOperation(), false}->last()"})
    @DisplayName("GIVEN a passing or unsuccessful prerequisite evaluation, WHEN validating, THEN dependent rules are still evaluated")
    public void validatePrerequisiteWithoutViolation(String expression) {
        // GIVEN attribute speed and a prerequisite that does not produce a reliable false result.
        AttributeUsage feature = SysmlFactory.eINSTANCE.createAttributeUsage();
        SysONSysMLValidator validator = this.createValidator(this.createPrerequisite(expression));
        BasicDiagnostic diagnostics = new BasicDiagnostic();

        // WHEN validating with the default rule evaluation contract.
        validator.validate(feature, diagnostics, Map.of());

        // THEN a technical failure must not silently suppress a dependent check.
        assertThat(diagnostics.getChildren())
                .extracting(Diagnostic::getSource)
                .containsExactly("dependent", "independent");
    }

    @Test
    @DisplayName("GIVEN two attributes, WHEN validating with the same validator, THEN exclusions do not leak between elements or calls")
    public void validateExclusionsAreLocal() {
        // GIVEN attribute incomplete and attribute complete, with a prerequisite based on the declared name.
        AttributeUsage incomplete = SysmlFactory.eINSTANCE.createAttributeUsage();
        incomplete.setDeclaredName("incomplete");
        AttributeUsage complete = SysmlFactory.eINSTANCE.createAttributeUsage();
        complete.setDeclaredName("complete");
        SysONSysMLValidator validator = this.createValidator(this.createPrerequisite("aql:self.declaredName = 'complete'"));
        BasicDiagnostic first = new BasicDiagnostic();
        validator.validate(incomplete, first, Map.of());

        // WHEN validating a second element and then the repaired first element with the same validator.
        BasicDiagnostic second = new BasicDiagnostic();
        validator.validate(complete, second, Map.of());
        incomplete.setDeclaredName("complete");
        BasicDiagnostic repaired = new BasicDiagnostic();
        validator.validate(incomplete, repaired, Map.of());

        // THEN the dependent rule resumes for both the other element and the repaired original.
        assertThat(first.getChildren())
                .extracting(Diagnostic::getSource)
                .containsExactly("prerequisite", "independent");
        assertThat(second.getChildren())
                .extracting(Diagnostic::getSource)
                .containsExactly("dependent", "independent");
        assertThat(repaired.getChildren())
                .extracting(Diagnostic::getSource)
                .containsExactly("dependent", "independent");
    }

    @Test
    @DisplayName("GIVEN an existing ValidationRule implementation, WHEN querying exclusions, THEN no rule is excluded by default")
    public void getDefaultExclusions() {
        // GIVEN a rule using the existing record constructor without any new implementation.
        IValidationRule rule = new ValidationRule(SysmlPackage.eINSTANCE.getAttributeUsage(), "existing", "aql:false", "Violation.", Diagnostic.WARNING);

        // WHEN querying the newly introduced optional contract.
        List<String> excludedRules = rule.getRulesToIgnoreOnFailure();

        // THEN existing implementations preserve their independent evaluation behavior.
        assertThat(excludedRules)
                .isEmpty();
    }

    private IValidationRule createPrerequisite(String expression) {
        IValidationRule prerequisite = mock(IValidationRule.class);
        when(prerequisite.eClass()).thenReturn(SysmlPackage.eINSTANCE.getAttributeUsage());
        when(prerequisite.ruleName()).thenReturn("prerequisite");
        when(prerequisite.expression()).thenReturn(expression);
        when(prerequisite.message()).thenReturn("Prerequisite violation.");
        when(prerequisite.severity()).thenReturn(Diagnostic.ERROR);
        when(prerequisite.getRulesToIgnoreOnFailure()).thenReturn(List.of("dependent"));
        return prerequisite;
    }

    private SysONSysMLValidator createValidator(IValidationRule prerequisite) {
        IValidationRulesProvider provider = mock(IValidationRulesProvider.class);
        when(provider.getAQLServiceClasses()).thenReturn(List.of());
        when(provider.getAQLServiceInstances()).thenReturn(List.of());
        when(provider.getEPackages()).thenReturn(List.of());
        IValidationRule dependent = new ValidationRule(SysmlPackage.eINSTANCE.getAttributeUsage(), "dependent", "aql:false", "Dependent violation.", Diagnostic.WARNING);
        IValidationRule independent = new ValidationRule(SysmlPackage.eINSTANCE.getAttributeUsage(), "independent", "aql:false", "Independent violation.", Diagnostic.WARNING);
        doReturn(List.of(prerequisite, dependent, independent)).when(provider).getValidationRules(SysmlPackage.eINSTANCE.getAttributeUsage());
        return new SysONSysMLValidator(provider);
    }
}
