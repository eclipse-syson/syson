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
package org.eclipse.syson.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ServiceMethod-related tests.
 *
 * @author gdaniel
 */
public class ServiceMethodTest {

    @Test
    @DisplayName("GIVEN a service method with arity 0, WHEN an AQL expression is constructed with 0 parameter, THEN the expression is returned")
    public void givenServiceMethodWithArity0WhenAQLExpressionIsConstructedWith0ParameterThenExpressionIsReturned() {
        String expression = ServiceMethod.of0(ServiceMethodTest::serviceWithArity0).aqlSelf();
        assertThat(expression).isEqualTo("aql:self.serviceWithArity0()");
        expression = ServiceMethod.of0(ServiceMethodTest::serviceWithArity0).aql("var");
        assertThat(expression).isEqualTo("aql:var.serviceWithArity0()");
    }

    @Test
    @DisplayName("GIVEN a service method with arity 0, WHEN an AQL expression is constructed with 1 parameter, THEN an exception is thrown")
    public void givenServiceMethodWithArity0WhenAQLExpressionIsConstructedWith1ParameterThenExceptionIsThrown() {
        assertThatThrownBy(() -> ServiceMethod.of0(ServiceMethodTest::serviceWithArity0).aqlSelf("param1")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ServiceMethod.of0(ServiceMethodTest::serviceWithArity0).aql("var", "param1")).isInstanceOf(IllegalArgumentException.class);
    }

    private Object serviceWithArity0(Object self) {
        return null;
    }
}
