/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.application.controllers.utils;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Utility class that generates test names for arguments in parameterized tests.
 * <p>
 * Parameterized tests' arguments aren't supported by DisplayNameGenerator. It is possible to name arguments using
 * {@link Named#of(String, Object)}, but this makes the definition of test arguments cumbersome and hard to read. This
 * class is used to create such named arguments transparently. Note that the generated name for unsupported argument
 * types default to {@code toString()}.
 * </p>
 *
 * <pre>
 * {@code
 * public static Stream<Arguments> myArguments() {
 *     return Stream.of(
 *             Arguments.of(myObject1, myObject2)).map(TestNameGenerator::namedArguments);
 * }
 * }
 * </pre>
 *
 * @author gdaniel
 */
public class TestNameGenerator {

    public static Arguments namedArguments(Arguments arg) {
        return Arguments.of(Arrays.stream(arg.get()).map(TestNameGenerator::namedPayload).toArray());
    }

    public static <T> Named<T> namedPayload(T payload) {
        String name = Objects.toString(payload);
        if (payload instanceof EClassifier eClassifier) {
            name = eClassifier.getName();
        }
        if (payload instanceof EStructuralFeature eStructuralFeature) {
            name = eStructuralFeature.getName();
        }
        final String computedName = name;
        return new Named<>() {
            @Override
            public String getName() {
                return computedName;
            }

            @Override
            public T getPayload() {
                return payload;
            }
        };
    }

}
