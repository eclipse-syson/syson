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

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ServiceMethod-related tests.
 *
 * @author gdaniel
 */
public class ServiceMethodTest {

    private static final String AQL_VALUE = "'value'";

    private static final String AQL_INT = "2";

    @Test
    @DisplayName("GIVEN a service method with arity 0, WHEN an AQL expression is constructed with 0 parameter, THEN the expression is returned")
    public void givenServiceMethodWithArity0WhenAQLExpressionIsConstructedWith0ParameterThenExpressionIsReturned() {
        String expression = ServiceMethod.of0(ArityFixture::instance0).aqlSelf();
        assertThat(expression).isEqualTo("aql:self.instance0()");
        expression = ServiceMethod.of0(ArityFixture::instance0).aql("var");
        assertThat(expression).isEqualTo("aql:var.instance0()");
    }

    @Test
    @DisplayName("GIVEN a service method with arity 0, WHEN an AQL expression is constructed with 1 parameter, THEN an exception is thrown")
    public void givenServiceMethodWithArity0WhenAQLExpressionIsConstructedWith1ParameterThenExceptionIsThrown() {
        assertThatThrownBy(() -> ServiceMethod.of0(ArityFixture::instance0).aqlSelf("param1")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ServiceMethod.of0(ArityFixture::instance0).aql("var", "param1")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("GIVEN instance service factories, WHEN each arity is used, THEN the expected expressions are returned")
    public void givenInstanceServiceFactoriesWhenEachArityIsUsedThenTheExpectedExpressionsAreReturned() throws NoSuchMethodException {
        assertThat(ServiceMethod.of0(ArityFixture::instance0).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance0", Object.class));
        assertThat(ServiceMethod.of1(ArityFixture::instance1).aql("ctx", AQL_VALUE)).isEqualTo("aql:ctx.instance1('value')");
        assertThat(ServiceMethod.of2(ArityFixture::instance2).aqlSelf(AQL_VALUE, AQL_INT)).isEqualTo("aql:self.instance2('value',2)");
        assertThat(ServiceMethod.of3(ArityFixture::instance3).aqlSelf(AQL_VALUE, AQL_INT, "true")).isEqualTo("aql:self.instance3('value',2,true)");
        assertThat(ServiceMethod.of4(ArityFixture::instance4).aqlSelf(AQL_VALUE, AQL_INT, "true", "4.0"))
                .isEqualTo("aql:self.instance4('value',2,true,4.0)");
        assertThat(ServiceMethod.of5(ArityFixture::instance5).aqlSelf(AQL_VALUE, AQL_INT, "true", "4.0", "5L"))
                .isEqualTo("aql:self.instance5('value',2,true,4.0,5L)");
        assertThat(ServiceMethod.of6(ArityFixture::instance6).aqlSelf(AQL_VALUE, AQL_INT, "true", "4.0", "5L", "6.0f"))
                .isEqualTo("aql:self.instance6('value',2,true,4.0,5L,6.0f)");
    }

    @Test
    @DisplayName("GIVEN typed instance service factories, WHEN each overload is used, THEN the expected declarations are resolved")
    public void givenTypedInstanceServiceFactoriesWhenEachOverloadIsUsedThenTheExpectedDeclarationsAreResolved() throws NoSuchMethodException {
        assertThat(ServiceMethod.of0(Object.class, ArityFixture::instance0).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance0", Object.class));
        assertThat(ServiceMethod.of0(ArityFixture.class, ArityFixture::instance0, Object.class).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance0", Object.class));
        assertThat(ServiceMethod.of1(Object.class, String.class, ArityFixture::instance1).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance1", Object.class, String.class));
        assertThat(ServiceMethod.of1(ArityFixture.class, ArityFixture::instance1, Object.class, String.class).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance1", Object.class, String.class));
        assertThat(ServiceMethod.of2(Object.class, String.class, Integer.class, ArityFixture::instance2).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance2", Object.class, String.class, Integer.class));
        assertThat(ServiceMethod.of2(ArityFixture.class, ArityFixture::instance2, Object.class, String.class, Integer.class).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance2", Object.class, String.class, Integer.class));
        assertThat(ServiceMethod.of3(Object.class, String.class, Integer.class, Boolean.class, ArityFixture::instance3).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance3", Object.class, String.class, Integer.class, Boolean.class));
        assertThat(ServiceMethod.of3(ArityFixture.class, ArityFixture::instance3, Object.class, String.class, Integer.class, Boolean.class).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance3", Object.class, String.class, Integer.class, Boolean.class));
        assertThat(ServiceMethod.of4(Object.class, String.class, Integer.class, Boolean.class, Double.class, ArityFixture::instance4).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance4", Object.class, String.class, Integer.class, Boolean.class, Double.class));
        assertThat(ServiceMethod.of4(ArityFixture.class, ArityFixture::instance4, Object.class, String.class, Integer.class, Boolean.class, Double.class).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance4", Object.class, String.class, Integer.class, Boolean.class, Double.class));
        assertThat(ServiceMethod.of5(Object.class, String.class, Integer.class, Boolean.class, Double.class, Long.class, ArityFixture::instance5).declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance5", Object.class, String.class, Integer.class, Boolean.class, Double.class, Long.class));
        assertThat(ServiceMethod.of5(ArityFixture.class, ArityFixture::instance5, Object.class, String.class, Integer.class, Boolean.class, Double.class, Long.class)
                .declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance5", Object.class, String.class, Integer.class, Boolean.class, Double.class, Long.class));
        assertThat(ServiceMethod.of6(Object.class, String.class, Integer.class, Boolean.class, Double.class, Long.class, Float.class, ArityFixture::instance6)
                .declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance6", Object.class, String.class, Integer.class, Boolean.class, Double.class, Long.class, Float.class));
        assertThat(ServiceMethod
                .of6(ArityFixture.class, ArityFixture::instance6, Object.class, String.class, Integer.class, Boolean.class, Double.class, Long.class, Float.class)
                .declaration())
                .isEqualTo(ArityFixture.class.getDeclaredMethod("instance6", Object.class, String.class, Integer.class, Boolean.class, Double.class, Long.class, Float.class));
    }

    @Test
    @DisplayName("GIVEN static service factories, WHEN each arity is used, THEN the expected expressions are returned")
    public void givenStaticServiceFactoriesWhenEachArityIsUsedThenTheExpectedExpressionsAreReturned() throws NoSuchMethodException {
        assertThat(ServiceMethod.ofStatic0(StaticFixture::static0).declaration())
                .isEqualTo(StaticFixture.class.getDeclaredMethod("static0", Object.class));
        assertThat(ServiceMethod.ofStatic0(Object.class, StaticFixture::static0).declaration())
                .isEqualTo(StaticFixture.class.getDeclaredMethod("static0", Object.class));
        assertThat(ServiceMethod.ofStatic1(StaticFixture::static1).aqlSelf(AQL_VALUE)).isEqualTo("aql:self.static1('value')");
        assertThat(ServiceMethod.ofStatic1(Object.class, String.class, StaticFixture::static1).declaration())
                .isEqualTo(StaticFixture.class.getDeclaredMethod("static1", Object.class, String.class));
        assertThat(ServiceMethod.ofStatic2(StaticFixture::static2).aql("ctx", AQL_VALUE, AQL_INT)).isEqualTo("aql:ctx.static2('value',2)");
        assertThat(ServiceMethod.ofStatic2(Object.class, String.class, Integer.class, StaticFixture::static2).declaration())
                .isEqualTo(StaticFixture.class.getDeclaredMethod("static2", Object.class, String.class, Integer.class));
        assertThat(ServiceMethod.ofStatic3(StaticFixture::static3).aqlSelf(AQL_VALUE, AQL_INT, "true")).isEqualTo("aql:self.static3('value',2,true)");
        assertThat(ServiceMethod.ofStatic3(Object.class, String.class, Integer.class, Boolean.class, StaticFixture::static3).declaration())
                .isEqualTo(StaticFixture.class.getDeclaredMethod("static3", Object.class, String.class, Integer.class, Boolean.class));
    }

    @Test
    @DisplayName("GIVEN a service method with another arity, WHEN the wrong number of parameters is provided, THEN an exception is thrown")
    public void givenServiceMethodWithAnotherArityWhenWrongNumberOfParametersIsProvidedThenExceptionIsThrown() {
        assertThatThrownBy(() -> ServiceMethod.of3(ArityFixture::instance3).aqlSelf(AQL_VALUE, AQL_INT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("instance3")
                .hasMessageContaining("arity of 3");
    }

    @Test
    @DisplayName("GIVEN an invalid AQL variable, WHEN aql is called, THEN an exception is thrown")
    public void givenInvalidAQLVariableWhenAqlIsCalledThenExceptionIsThrown() {
        assertThatThrownBy(() -> ServiceMethod.of0(ArityFixture::instance0).aql((String) null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ServiceMethod.of0(ArityFixture::instance0).aql("")).isInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    @DisplayName("GIVEN wrong expected parameter types, WHEN the declaration is resolved, THEN an exception is thrown")
    public void givenWrongExpectedParameterTypesWhenDeclarationIsResolvedThenAnExceptionIsThrown() {
        ServiceMethod.Inst1<ArityFixture, Object, String> ref = ArityFixture::instance1;

        assertThatThrownBy(() -> ServiceMethod.of1(Object.class, Integer.class, (ServiceMethod.Inst1) ref))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("expected");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    @DisplayName("GIVEN a wrong expected service type, WHEN the declaration is resolved, THEN an exception is thrown")
    public void givenWrongExpectedServiceTypeWhenDeclarationIsResolvedThenAnExceptionIsThrown() {
        ServiceMethod.Inst1<ArityFixture, Object, String> ref = ArityFixture::instance1;

        assertThatThrownBy(() -> ServiceMethod.of1((Class) WrongService.class, (ServiceMethod.Inst1) ref, Object.class, String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("expected a service assignable");
    }

    @Test
    @DisplayName("GIVEN overloaded services, WHEN explicit signature types are provided, THEN the expected declaration is resolved")
    public void givenOverloadedServicesWhenExplicitSignatureTypesAreProvidedThenTheExpectedDeclarationIsResolved() throws NoSuchMethodException {
        ServiceMethod serviceMethod = ServiceMethod.of1(OverloadedService.class, OverloadedService::overloaded, Object.class, Integer.class);

        Method expectedDeclaration = OverloadedService.class.getDeclaredMethod("overloaded", Object.class, Integer.class);
        assertThat(serviceMethod.name()).isEqualTo("overloaded");
        assertThat(serviceMethod.declaration()).isEqualTo(expectedDeclaration);
        assertThat(serviceMethod.declaration().getReturnType()).isEqualTo(Integer.class);
        assertThat(serviceMethod.aqlSelf("42")).isEqualTo("aql:self.overloaded(42)");
    }

    @Test
    @DisplayName("GIVEN overloaded services, WHEN explicit self type is provided, THEN arity-0 overloads can be selected")
    public void givenOverloadedServicesWhenExplicitSelfTypeIsProvidedThenArity0OverloadsCanBeSelected() throws NoSuchMethodException {
        ServiceMethod serviceMethod = ServiceMethod.of0(OverloadedService.class, OverloadedService::overloaded, String.class);

        Method expectedDeclaration = OverloadedService.class.getDeclaredMethod("overloaded", String.class);
        assertThat(serviceMethod.declaration()).isEqualTo(expectedDeclaration);
        assertThat(serviceMethod.declaration().getReturnType()).isEqualTo(String.class);
        assertThat(serviceMethod.aqlSelf()).isEqualTo("aql:self.overloaded()");
    }

    @Test
    @DisplayName("GIVEN overloaded services, WHEN another explicit self type is provided, THEN the matching arity-0 overload is selected")
    public void givenOverloadedServicesWhenAnotherExplicitSelfTypeIsProvidedThenTheMatchingArity0OverloadIsSelected() throws NoSuchMethodException {
        ServiceMethod serviceMethod = ServiceMethod.of0(OverloadedService.class, OverloadedService::overloaded, Integer.class);

        Method expectedDeclaration = OverloadedService.class.getDeclaredMethod("overloaded", Integer.class);
        assertThat(serviceMethod.declaration()).isEqualTo(expectedDeclaration);
        assertThat(serviceMethod.declaration().getReturnType()).isEqualTo(Integer.class);
        assertThat(serviceMethod.aqlSelf()).isEqualTo("aql:self.overloaded()");
    }

    @Test
    @DisplayName("GIVEN overloaded services, WHEN another explicit parameter type is provided, THEN the matching arity-1 overload is selected")
    public void givenOverloadedServicesWhenAnotherExplicitParameterTypeIsProvidedThenTheMatchingArity1OverloadIsSelected() throws NoSuchMethodException {
        ServiceMethod serviceMethod = ServiceMethod.of1(OverloadedService.class, OverloadedService::overloaded, Object.class, String.class);

        Method expectedDeclaration = OverloadedService.class.getDeclaredMethod("overloaded", Object.class, String.class);
        assertThat(serviceMethod.declaration()).isEqualTo(expectedDeclaration);
        assertThat(serviceMethod.declaration().getReturnType()).isEqualTo(String.class);
        assertThat(serviceMethod.aqlSelf(AQL_VALUE)).isEqualTo("aql:self.overloaded('value')");
    }

    /**
     * Fixture used to validate factory methods across supported instance arities.
     */
    private static final class ArityFixture {
        private Object instance0(Object self) {
            return self;
        }

        private Object instance1(Object self, String p1) {
            return p1;
        }

        private Object instance2(Object self, String p1, Integer p2) {
            return p2;
        }

        private Object instance3(Object self, String p1, Integer p2, Boolean p3) {
            return p3;
        }

        private Object instance4(Object self, String p1, Integer p2, Boolean p3, Double p4) {
            return p4;
        }

        private Object instance5(Object self, String p1, Integer p2, Boolean p3, Double p4, Long p5) {
            return p5;
        }

        private Object instance6(Object self, String p1, Integer p2, Boolean p3, Double p4, Long p5, Float p6) {
            return p6;
        }
    }

    /**
     * Fixture used to validate factory methods for static service references.
     */
    private static final class StaticFixture {
        private static Object static0(Object self) {
            return self;
        }

        private static Object static1(Object self, String p1) {
            return p1;
        }

        private static Object static2(Object self, String p1, Integer p2) {
            return p2;
        }

        private static Object static3(Object self, String p1, Integer p2, Boolean p3) {
            return p3;
        }
    }

    /**
     * Fixture used to validate service type mismatch checks.
     */
    private static final class WrongService {
        private WrongService() {
            // Utility fixture.
        }
    }

    /**
     * Fixture used to validate overloaded method resolution.
     */
    private static final class OverloadedService {
        private String overloaded(String self) {
            return self;
        }

        private Integer overloaded(Integer self) {
            return self;
        }

        private String overloaded(Object self, String value) {
            return value;
        }

        private Integer overloaded(Object self, Integer value) {
            return value;
        }
    }
}
