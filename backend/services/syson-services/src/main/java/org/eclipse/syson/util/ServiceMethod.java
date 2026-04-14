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
package org.eclipse.syson.util;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
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
 * Overloaded services: if several service methods share the same name, use the factory overloads that also take the
 * service class and Java parameter types, for example
 * {@code ServiceMethod.of1(EObjectServices.class, EObjectServices::eGet, EObject.class, EStructuralFeature.class)}.
 * <p>
 * Performance: this uses reflection once per reference at startup to read a method name. The cost is negligible
 * compared to normal init work.
 *
 * @author cbrun
 *
 */
public final class ServiceMethod {

    private final Method declaration;

    private final String name;

    private final int arity;

    private ServiceMethod(Method declaration, int arity) {
        this.declaration = declaration;
        this.name = declaration.getName();
        this.arity = arity;
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
     * the Java declaration that will be called from AQL.
     *
     * @return the declaration.
     */
    public Method declaration() {
        return this.declaration;
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
        this.checkArity(params);
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
            this.checkArity(params);
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
        return new ServiceMethod(method(ref), 0);
    }

    /**
     * Instance method with signature {@code R method(T self)}.
     * <p>
     * Use this overload when the referenced Java service is overloaded and you need to disambiguate on the
     * {@code self} type.
     */
    public static <S, T> ServiceMethod of0(Class<T> selfType, Inst0<S, T> ref) {
        return new ServiceMethod(method(ref, selfType), 0);
    }

    /**
     * Instance method with signature {@code R method(T self)}.
     * <p>
     * Use this overload when the referenced Java service is overloaded and you need to disambiguate on the declaring
     * service and {@code self} types.
     */
    public static <S, T> ServiceMethod of0(Class<S> serviceType, Inst0<S, T> ref, Class<T> selfType) {
        return new ServiceMethod(method(serviceType, ref, selfType), 0);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1)}.
     */
    public static <S, T, P1> ServiceMethod of1(Inst1<S, T, P1> ref) {
        return new ServiceMethod(method(ref), 1);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1)}.
     * <p>
     * Use this overload when the referenced Java service is overloaded and you need to disambiguate on parameter
     * types.
     */
    public static <S, T, P1> ServiceMethod of1(Class<T> selfType, Class<P1> p1Type, Inst1<S, T, P1> ref) {
        return new ServiceMethod(method(ref, selfType, p1Type), 1);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1)}.
     * <p>
     * Use this overload when the referenced Java service is overloaded and you need to disambiguate on the declaring
     * service and parameter types.
     */
    public static <S, T, P1> ServiceMethod of1(Class<S> serviceType, Inst1<S, T, P1> ref, Class<T> selfType, Class<P1> p1Type) {
        return new ServiceMethod(method(serviceType, ref, selfType, p1Type), 1);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2)}.
     */
    public static <S, T, P1, P2> ServiceMethod of2(Inst2<S, T, P1, P2> ref) {
        return new ServiceMethod(method(ref), 2);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2)}.
     */
    public static <S, T, P1, P2> ServiceMethod of2(Class<T> selfType, Class<P1> p1Type, Class<P2> p2Type, Inst2<S, T, P1, P2> ref) {
        return new ServiceMethod(method(ref, selfType, p1Type, p2Type), 2);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2)}.
     */
    public static <S, T, P1, P2> ServiceMethod of2(Class<S> serviceType, Inst2<S, T, P1, P2> ref, Class<T> selfType, Class<P1> p1Type, Class<P2> p2Type) {
        return new ServiceMethod(method(serviceType, ref, selfType, p1Type, p2Type), 2);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3)}.
     */
    public static <S, T, P1, P2, P3> ServiceMethod of3(Inst3<S, T, P1, P2, P3> ref) {
        return new ServiceMethod(method(ref), 3);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3)}.
     */
    public static <S, T, P1, P2, P3> ServiceMethod of3(Class<T> selfType, Class<P1> p1Type, Class<P2> p2Type, Class<P3> p3Type, Inst3<S, T, P1, P2, P3> ref) {
        return new ServiceMethod(method(ref, selfType, p1Type, p2Type, p3Type), 3);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3)}.
     */
    public static <S, T, P1, P2, P3> ServiceMethod of3(Class<S> serviceType, Inst3<S, T, P1, P2, P3> ref, Class<T> selfType, Class<P1> p1Type, Class<P2> p2Type,
            Class<P3> p3Type) {
        return new ServiceMethod(method(serviceType, ref, selfType, p1Type, p2Type, p3Type), 3);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4)}.
     */
    public static <S, T, P1, P2, P3, P4> ServiceMethod of4(Inst4<S, T, P1, P2, P3, P4> ref) {
        return new ServiceMethod(method(ref), 4);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4)}.
     */
    public static <S, T, P1, P2, P3, P4> ServiceMethod of4(Class<T> selfType, Class<P1> p1Type, Class<P2> p2Type, Class<P3> p3Type, Class<P4> p4Type,
            Inst4<S, T, P1, P2, P3, P4> ref) {
        return new ServiceMethod(method(ref, selfType, p1Type, p2Type, p3Type, p4Type), 4);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4)}.
     */
    public static <S, T, P1, P2, P3, P4> ServiceMethod of4(Class<S> serviceType, Inst4<S, T, P1, P2, P3, P4> ref, Class<T> selfType, Class<P1> p1Type,
            Class<P2> p2Type, Class<P3> p3Type, Class<P4> p4Type) {
        return new ServiceMethod(method(serviceType, ref, selfType, p1Type, p2Type, p3Type, p4Type), 4);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5)}.
     */
    public static <S, T, P1, P2, P3, P4, P5> ServiceMethod of5(Inst5<S, T, P1, P2, P3, P4, P5> ref) {
        return new ServiceMethod(method(ref), 5);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5)}.
     */
    public static <S, T, P1, P2, P3, P4, P5> ServiceMethod of5(Class<T> selfType, Class<P1> p1Type, Class<P2> p2Type, Class<P3> p3Type, Class<P4> p4Type,
            Class<P5> p5Type, Inst5<S, T, P1, P2, P3, P4, P5> ref) {
        return new ServiceMethod(method(ref, selfType, p1Type, p2Type, p3Type, p4Type, p5Type), 5);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5)}.
     */
    // CHECKSTYLE:OFF
    public static <S, T, P1, P2, P3, P4, P5> ServiceMethod of5(Class<S> serviceType, Inst5<S, T, P1, P2, P3, P4, P5> ref, Class<T> selfType, Class<P1> p1Type,
            Class<P2> p2Type, Class<P3> p3Type, Class<P4> p4Type, Class<P5> p5Type) {
        return new ServiceMethod(method(serviceType, ref, selfType, p1Type, p2Type, p3Type, p4Type, p5Type), 5);
    }
    // CHECKSTYLE:ON

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6)}.
     */
    public static <S, T, P1, P2, P3, P4, P5, P6> ServiceMethod of6(Inst6<S, T, P1, P2, P3, P4, P5, P6> ref) {
        return new ServiceMethod(method(ref), 6);
    }

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6)}.
     */
    // CHECKSTYLE:OFF
    public static <S, T, P1, P2, P3, P4, P5, P6> ServiceMethod of6(Class<T> selfType, Class<P1> p1Type, Class<P2> p2Type, Class<P3> p3Type, Class<P4> p4Type,
            Class<P5> p5Type, Class<P6> p6Type, Inst6<S, T, P1, P2, P3, P4, P5, P6> ref) {
        return new ServiceMethod(method(ref, selfType, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type), 6);
    }
    // CHECKSTYLE:ON

    /**
     * Instance method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3, P4 p4, P5 p5, P6 p6)}.
     */
    // CHECKSTYLE:OFF
    public static <S, T, P1, P2, P3, P4, P5, P6> ServiceMethod of6(Class<S> serviceType, Inst6<S, T, P1, P2, P3, P4, P5, P6> ref, Class<T> selfType,
            Class<P1> p1Type, Class<P2> p2Type, Class<P3> p3Type, Class<P4> p4Type, Class<P5> p5Type, Class<P6> p6Type) {
        return new ServiceMethod(method(serviceType, ref, selfType, p1Type, p2Type, p3Type, p4Type, p5Type, p6Type), 6);
    }
    // CHECKSTYLE:ON

    // ---------------------- Factories for static methods ----------------------

    /**
     * Static method with signature {@code R method(T self)}.
     */
    public static <T> ServiceMethod ofStatic0(IStat0<T> ref) {
        return new ServiceMethod(method(ref), 0);
    }

    /**
     * Static method with signature {@code R method(T self)}.
     */
    public static <T> ServiceMethod ofStatic0(Class<T> selfType, IStat0<T> ref) {
        return new ServiceMethod(method(ref, selfType), 0);
    }

    /**
     * Static method with signature {@code R method(T self, P1 p1)}.
     */
    public static <T, P1> ServiceMethod ofStatic1(IStat1<T, P1> ref) {
        return new ServiceMethod(method(ref), 1);
    }

    /**
     * Static method with signature {@code R method(T self, P1 p1)}.
     */
    public static <T, P1> ServiceMethod ofStatic1(Class<T> selfType, Class<P1> p1Type, IStat1<T, P1> ref) {
        return new ServiceMethod(method(ref, selfType, p1Type), 1);
    }

    /**
     * Static method with signature {@code R method(T self, P1 p1, P2 p2)}.
     */
    public static <T, P1, P2> ServiceMethod ofStatic2(IStat2<T, P1, P2> ref) {
        return new ServiceMethod(method(ref), 2);
    }

    /**
     * Static method with signature {@code R method(T self, P1 p1, P2 p2)}.
     */
    public static <T, P1, P2> ServiceMethod ofStatic2(Class<T> selfType, Class<P1> p1Type, Class<P2> p2Type, IStat2<T, P1, P2> ref) {
        return new ServiceMethod(method(ref, selfType, p1Type, p2Type), 2);
    }

    /**
     * Static method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3)}.
     */
    public static <T, P1, P2, P3> ServiceMethod ofStatic3(IStat3<T, P1, P2, P3> ref) {
        return new ServiceMethod(method(ref), 3);
    }

    /**
     * Static method with signature {@code R method(T self, P1 p1, P2 p2, P3 p3)}.
     */
    public static <T, P1, P2, P3> ServiceMethod ofStatic3(Class<T> selfType, Class<P1> p1Type, Class<P2> p2Type, Class<P3> p3Type, IStat3<T, P1, P2, P3> ref) {
        return new ServiceMethod(method(ref, selfType, p1Type, p2Type, p3Type), 3);
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

    private static Method method(Serializable lambdaRef, Class<?>... expectedParameterTypes) {
        try {
            SerializedLambda lambda = serializedLambda(lambdaRef);
            Class<?> implementationClass = Class.forName(lambda.getImplClass().replace('/', '.'), false, lambdaRef.getClass().getClassLoader());
            MethodType methodType = MethodType.fromMethodDescriptorString(lambda.getImplMethodSignature(), implementationClass.getClassLoader());
            Method method = thisClassMethod(implementationClass, lambda.getImplMethodName(), methodType.parameterArray());
            if (expectedParameterTypes.length > 0 && !Arrays.equals(method.getParameterTypes(), expectedParameterTypes)) {
                throw new IllegalArgumentException(
                        MessageFormat.format("Resolved method {0} has parameters {1} but expected {2}", method, Arrays.toString(method.getParameterTypes()), Arrays.toString(expectedParameterTypes)));
            }
            return method;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot resolve method declaration from lambda", e);
        }
    }

    private static Method method(Class<?> expectedServiceType, Serializable lambdaRef, Class<?>... expectedParameterTypes) {
        Method method = method(lambdaRef, expectedParameterTypes);
        if (!expectedServiceType.isAssignableFrom(method.getDeclaringClass())) {
            throw new IllegalArgumentException(MessageFormat.format("Resolved method {0} is declared on {1} but expected a service assignable to {2}", method,
                    method.getDeclaringClass().getName(), expectedServiceType.getName()));
        }
        return method;
    }

    private static SerializedLambda serializedLambda(Serializable lambdaRef) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method writeReplace = lambdaRef.getClass().getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(true);
        return (SerializedLambda) writeReplace.invoke(lambdaRef);
    }

    private static Method thisClassMethod(Class<?> implementationClass, String methodName, Class<?>[] parameterTypes) throws NoSuchMethodException {
        try {
            return implementationClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException exception) {
            return implementationClass.getMethod(methodName, parameterTypes);
        }
    }

    /**
     * Checks that the provided {@code params} array matches the arity of the service method.
     *
     * @param params the parameters passed to the service method (excluding self)
     *
     * @throws IllegalArgumentException if the provided {@code params} doesn't match the arity of the service method.
     */
    private void checkArity(String... params) {
        int paramLength;
        if (params == null) {
            paramLength = 0;
        } else {
            paramLength = params.length;
        }
        if (this.arity != paramLength) {
            throw new IllegalArgumentException(MessageFormat.format("Service {0} has an arity of {1} but {2} parameters were provided", this.name, this.arity, paramLength));
        }
    }
}
