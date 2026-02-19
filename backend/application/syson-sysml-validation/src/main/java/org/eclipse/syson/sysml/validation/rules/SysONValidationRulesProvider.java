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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRule;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRulesProvider;
import org.springframework.stereotype.Service;

/**
 * Default {@link IValidationRulesProvider} implementation that always provides all the {@link SysMLValidationRule
 * SysML validation rules}.
 *
 * Consumers may override this implementation to add and/or remove rules.
 *
 * @author flatombe
 */
@Service
public class SysONValidationRulesProvider implements IValidationRulesProvider {

    protected static final List<? extends IValidationRule> ALL_SYSML_VALIDATION_RULES = Stream.of(SysMLValidationRule.values()).toList();

    @Override
    public List<? extends IValidationRule> getValidationRules(final EClass eClass) {
        return ALL_SYSML_VALIDATION_RULES.stream()
                .filter(rule -> rule.eClass().equals(eClass) || rule.eClass().isSuperTypeOf(eClass))
                .toList();
    }

    @Override
    public List<Class<?>> getAQLServiceClasses() {
        return List.of(SysMLValidationRuleQueryServices.class);
    }

    @Override
    public List<Object> getAQLServiceInstances() {
        return Collections.emptyList();
    }

    @Override
    public List<EPackage> getEPackages() {
        return List.of(SysmlPackage.eINSTANCE);
    }
}
