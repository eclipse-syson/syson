/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Custom condition used to detect that a test does not need to have access to default libraries.
 *
 * @author arichard
 */
public class OnNoDefaultLibrariesTests extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var message = ConditionMessage.forCondition(this.getClass().getSimpleName()).notAvailable(SysONTestsProperties.SYSON_TEST_ENABLED);
        ConditionOutcome outcome = ConditionOutcome.noMatch(message);

        var sysonTestEnabled = context.getEnvironment().getProperty(SysONTestsProperties.SYSON_TEST_ENABLED, "");
        var sysonTestEnabledFeatures = Arrays.stream(sysonTestEnabled.split(",")).map(String::trim).toList();

        if (sysonTestEnabledFeatures.contains(SysONTestsProperties.NO_DEFAULT_LIBRARIES)) {
            message = ConditionMessage.forCondition(this.getClass().getSimpleName()).available(SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY);
            outcome = ConditionOutcome.match(message);
        }

        return outcome;
    }

}
