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
package org.eclipse.syson.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * A tiny helper to build AQL service call expressions from type-safe Java method references instead of hardcoded
 * strings.
 * <p>
 * Why use it:
 * <ul>
 * <li>Refactoring friendly: IDE rename of the Java method updates call sites.</li>
 * <li>Navigation: find usages works, you can jump to the service implementation.</li>
 * <li>Fewer string literals, fewer typos.</li>
 * <li>Produces the exact same AQL strings as {@code AQLUtils} helpers.</li>
 * </ul>
 * <p>
 * How it works:
 * <ol>
 * <li>You pass a <b>serializable</b> method reference to one of the factory methods {@code of0}, {@code of1},
 * {@code of2}, {@code of3} for instance methods, or {@code ofStatic0..3} for static methods. The arity suffix equals
 * the number of AQL parameters after {@code self}.</li>
 * <li>The helper extracts the Java method name through the standard lambda serialization hook (reading a
 * {@link SerializedLambda}). No service is invoked.</li>
 * <li>You then call {@link #aqlSelf(String...)} or {@link #aql(String, String...)} to build the final AQL string,
 * delegating to {@code AQLUtils}.</li>
 * </ol>
 * <p>
 * Important: the arguments passed to {@code aqlSelf(...)} and {@code aql(var, ...)} are AQL snippets, not Java values.
 * For example use {@code "'declaredName'"} for a string literal and {@code ViewFormDescriptionConverter.NEW_VALUE} if
 * it already returns an AQL snippet.
 * <p>
 * When to use which call:
 * <ul>
 * <li>{@link #aqlSelf(String...)} when the receiver is {@code self}, for expressions like
 * {@code aql:self.myService(...)}.</li>
 * <li>{@link #aql(String, String...)} when you target another variable in the AQL context, for example
 * {@code aql:elt.myService(...)}.</li>
 * </ul>
 * <p>
 * Type inference: if the compiler says something like <i>The type X does not define methodName(Object, Object,
 * ...)</i>, add a type witness to the factory so it can match the real signature. Examples:
 *
 * <pre>{@code
 * // setNewValue(Element, String, Object)
 * ServiceMethod.<DetailsViewService, Element, String, Object> of2(DetailsViewService::setNewValue)
 *         .aqlSelf("'declaredName'", ViewFormDescriptionConverter.NEW_VALUE);
 *
 * // setElementDescription(Usage, String)
 * ServiceMethod.<LAMutationService, Usage, String> of1(LAMutationService::setElementDescription)
 *         .aqlSelf(ViewFormDescriptionConverter.NEW_VALUE);
 *
 * // predicate isComponent(Element)
 * ServiceMethod.<LAQueryService, Element> of0(LAQueryService::isComponent)
 *         .aqlSelf();
 * }
 * </pre>
 * <p>
 * Performance: this uses reflection once per reference at startup to read a method name. The cost is negligible
 * compared to normal init work.
 *
 * @author cbrun
 *
 */
public final class ServiceMethod {

    private final String name;

    private ServiceMethod(String name) {
        this.name = name;
    }

    /**
     * the Java method name that will be called from AQL. Useful if you need the raw name.
     *
     * @return the name.
     */
    public String name() {
        return this.name;
    }

    /**
     * Build {@code aql:self.method(...)} for the captured service name.
     * <p>
     * Use when the receiver is {@code self}. Parameters are concatenated as-is. Pass zero parameters for
     * {@code myService()}.
     *
     * @param params
     *            AQL parameter snippets, for example {@code "'declaredName'"} or {@code someVar}
     * @return A full AQL expression string
     */
    public String aqlSelf(String... params) {
        if (params == null || params.length == 0) {
            return AQLUtils.getSelfServiceCallExpression(this.name);
        }
        return AQLUtils.getSelfServiceCallExpression(this.name, Arrays.asList(params));
    }

    /**
     * Build {@code aql:var.method(...)} for the captured service name.
     * <p>
     * Use when you need to target a specific variable in the AQL scope, for example {@code elt} or {@code container}.
     * Pass zero parameters for {@code myService()}.
     *
     * @param var
     *            the AQL variable name to call the service on, for example {@code "elt"}
     * @param params
     *            AQL parameter snippets
     * @return A full AQL expression string
     */
    public String aql(String var, String... params) {
        String aqlString = null;
        if (var == null || var.isEmpty()) {
            throw new IllegalArgumentException("var must be a non empty AQL variable name");
        } else {
            if (params == null || params.length == 0) {
                aqlString = AQLUtils.getServiceCallExpression(var, this.name);
            }
            aqlString = AQLUtils.getServiceCallExpression(var, this.name, Arrays.asList(params));
        }
        return aqlString;
    }

    // ---------------------- Factories for unbound instance methods ----------------------
    // The arity N is the number of parameters after self in the Java signature.

    /**
     * Instance method with signature {@code R method(T self)}.
     */
    public static <S, T> ServiceMethod of0(Inst0<S, T> ref) {
        return new ServiceMethod(methodName(ref));
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1)}.
     */
    public static <S, T, P1> ServiceMethod of1(Inst1<S, T, P1> ref) {
        return new ServiceMethod(methodName(ref));
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2)}.
     */
    public static <S, T, P1, P2> ServiceMethod of2(Inst2<S, T, P1, P2> ref) {
        return new ServiceMethod(methodName(ref));
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3)}.
     */
    public static <S, T, P1, P2, P3> ServiceMethod of3(Inst3<S, T, P1, P2, P3> ref) {
        return new ServiceMethod(methodName(ref));
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4)}.
     */
    public static <S, T, P1, P2, P3, P4> ServiceMethod of4(Inst4<S, T, P1, P2, P3, P4> ref) {
        return new ServiceMethod(methodName(ref));
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5)}.
     */
    public static <S, T, P1, P2, P3, P4, P5> ServiceMethod of5(Inst5<S, T, P1, P2, P3, P4, P5> ref) {
        return new ServiceMethod(methodName(ref));
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6)}.
     */
    public static <S, T, P1, P2, P3, P4, P5, P6> ServiceMethod of6(Inst6<S, T, P1, P2, P3, P4, P5, P6> ref) {
        return new ServiceMethod(methodName(ref));
    }

    // ---------------------- Factories for static methods ----------------------

    /**
     * Static method with signature {@code R method(T self)}.
     */
    public static <T> ServiceMethod ofStatic0(IStat0<T> ref) {
        return new ServiceMethod(methodName(ref));
    }

    /**
     * Static method with signature {@code R method(T self, P1 p1)}.
     */
    public static <T, P1> ServiceMethod ofStatic1(IStat1<T, P1> ref) {
        return new ServiceMethod(methodName(ref));
    }

    /**
     * Static method with signature {@code R method(T self, P1 p1, P2 p2)}.
     */
    public static <T, P1, P2> ServiceMethod ofStatic2(IStat2<T, P1, P2> ref) {
        return new ServiceMethod(methodName(ref));
    }

    /**
     * Static method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3)}.
     */
    public static <T, P1, P2, P3> ServiceMethod ofStatic3(IStat3<T, P1, P2, P3> ref) {
        return new ServiceMethod(methodName(ref));
    }

    // ---------------------- SAMs for method references ----------------------

    /**
     * Receiver S, then self T. No extra parameters.
     *
     * @param <S>
     *            : the Service Class type.
     * @param <T>
     *            : the self type.
     *
     */
    @FunctionalInterface
    public interface Inst0<S, T> extends Serializable {
        Object apply(S svc, T self);
    }

    /**
     * Receiver S, then self T, then one parameter.
     *
     * @param <S>
     *            : the Service Class type.
     * @param <T>
     *            : the self type.
     * @param <P1>
     *            : the first parameter type.
     *
     */
    @FunctionalInterface
    public interface Inst1<S, T, P1> extends Serializable {
        Object apply(S svc, T self, P1 p1);
    }

    /**
     * Receiver S, then self T, then two parameter.
     *
     * @param <S>
     *            : the Service Class type.
     * @param <T>
     *            : the self type.
     * @param <P1>
     *            : the first parameter type.
     * @param <P2>
     *            : the second parameter type.
     *
     */
    @FunctionalInterface
    public interface Inst2<S, T, P1, P2> extends Serializable {
        Object apply(S svc, T self, P1 p1, P2 p2);
    }

    /**
     * Receiver S, then self T, then three parameter.
     *
     * @param <S>
     *            : the Service Class type.
     * @param <T>
     *            : the self type.
     * @param <P1>
     *            : the first parameter type.
     * @param <P2>
     *            : the second parameter type.
     * @param <P3>
     *            : the third parameter type.
     *
     */
    @FunctionalInterface
    public interface Inst3<S, T, P1, P2, P3> extends Serializable {
        Object apply(S svc, T self, P1 p1, P2 p2, P3 p3);
    }

    /**
     * Receiver S, then self T, then four parameter.
     *
     * @param <S>
     *            : the Service Class type.
     * @param <T>
     *            : the self type.
     * @param <P1>
     *            : the first parameter type.
     * @param <P2>
     *            : the second parameter type.
     * @param <P3>
     *            : the third parameter type.
     * @param <P4>
     *            : the fourth parameter type.
     *
     */
    @FunctionalInterface
    public interface Inst4<S, T, P1, P2, P3, P4> extends Serializable {
        Object apply(S svc, T self, P1 p1, P2 p2, P3 p3, P4 p4);
    }

    /**
     * Receiver S, then self T, then five parameter.
     *
     * @param <S>
     *            : the Service Class type.
     * @param <T>
     *            : the self type.
     * @param <P1>
     *            : the first parameter type.
     * @param <P2>
     *            : the second parameter type.
     * @param <P3>
     *            : the third parameter type.
     * @param <P4>
     *            : the fourth parameter type.
     * @param <P5>
     *            : the fifth parameter type.
     *
     */
    public interface Inst5<S, T, P1, P2, P3, P4, P5> extends Serializable {
        Object apply(S svc, T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);
    }

    /**
     * Receiver S, then self T, then six parameter.
     *
     * @param <S>
     *            : the Service Class type.
     * @param <T>
     *            : the self type.
     * @param <P1>
     *            : the first parameter type.
     * @param <P2>
     *            : the second parameter type.
     * @param <P3>
     *            : the third parameter type.
     * @param <P4>
     *            : the fourth parameter type.
     * @param <P5>
     *            : the fifth parameter type.
     * @param <P6>
     *            : the sixth parameter type.
     *
     */
    // CHECKSTYLE:OFF
    public interface Inst6<S, T, P1, P2, P3, P4, P5, P6> extends Serializable {
        Object apply(S svc, T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6);
    }
    // CHECKSTYLE:ON

    /**
     * Self T only, for static methods.
     *
     * @param <T>
     *            : the self type.
     */
    @FunctionalInterface
    public interface IStat0<T> extends Serializable {
        Object apply(T self);
    }

    /**
     * Self T and one parameter, for static methods.
     *
     * @param <T>
     *            : the self type.
     *
     * @param <P1>
     *            : the first parameter type.
     */
    @FunctionalInterface
    public interface IStat1<T, P1> extends Serializable {
        Object apply(T self, P1 p1);
    }

    /**
     * Self T and two parameters, for static methods.
     *
     * @param <T>
     *            : the self type.
     *
     * @param <P1>
     *            : the first parameter type.
     * @param <P2>
     *            : the second parameter type.
     */
    @FunctionalInterface
    public interface IStat2<T, P1, P2> extends Serializable {
        Object apply(T self, P1 p1, P2 p2);
    }

    /**
     * Self T and three parameters, for static methods.
     *
     * @param <T>
     *            : the self type.
     *
     * @param <P1>
     *            : the first parameter type.
     * @param <P2>
     *            : the second parameter type.
     * @param <P3>
     *            : the third parameter type.
     */
    @FunctionalInterface
    public interface IStat3<T, P1, P2, P3> extends Serializable {
        Object apply(T self, P1 p1, P2 p2, P3 p3);
    }

    // ---------------------- Lambda -> method name ----------------------

    private static String methodName(Serializable lambdaRef) {
        try {
            Method m = lambdaRef.getClass().getDeclaredMethod("writeReplace");
            m.setAccessible(true);
            SerializedLambda sl = (SerializedLambda) m.invoke(lambdaRef);
            return sl.getImplMethodName();
        } catch (InvocationTargetException | NoSuchMethodException | SecurityException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot resolve method name from lambda", e);
        }
    }
}

