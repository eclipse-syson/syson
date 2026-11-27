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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRule;

/**
 * Application validation rules introduced by SysON, separate from the constraints of the SysML specification.
 *
 * @author adaussy
 */
public enum SysONValidationRule implements IValidationRule {
    validateRedefinitionRequiredFeatures(
            new ValidationRule(SysmlPackage.eINSTANCE.getRedefinition(), "validateRedefinitionRequiredFeatures",
                    "aql:self.redefiningFeature <> null and self.redefinedFeature <> null",
                    "A redefinition requires both a redefining feature and a redefined feature. Select both features, or remove this incomplete relationship.",
                    Diagnostic.ERROR),
            List.of(SysMLValidationRule.validateRedefinitionDirectionConformance.ruleName(),
                    SysMLValidationRule.validateRedefinitionEndConformance.ruleName(),
                    SysMLValidationRule.validateRedefinitionFeaturingTypes.ruleName(),
                    SysMLValidationRule.validateSpecificationSpecificNotConjugated.ruleName(),
                    SysMLValidationRule.validateSubsettingConstantConformance.ruleName(),
                    SysMLValidationRule.validateSubsettingFeaturingTypes.ruleName(),
                    SysMLValidationRule.validateSubsettingUniquenessConformance.ruleName()));

    private final ValidationRule rule;

    private final List<String> rulesToIgnoreOnFailure;

    SysONValidationRule(ValidationRule rule, List<String> rulesToIgnoreOnFailure) {
        this.rule = Objects.requireNonNull(rule);
        this.rulesToIgnoreOnFailure = List.copyOf(rulesToIgnoreOnFailure);
    }

    @Override
    public EClass eClass() {
        return this.rule.eClass();
    }

    @Override
    public String expression() {
        return this.rule.expression();
    }

    @Override
    public List<String> getRulesToIgnoreOnFailure() {
        return this.rulesToIgnoreOnFailure;
    }

    @Override
    public String message() {
        return this.rule.message();
    }

    @Override
    public String ruleName() {
        return this.rule.ruleName();
    }

    @Override
    public int severity() {
        return this.rule.severity();
    }
}
