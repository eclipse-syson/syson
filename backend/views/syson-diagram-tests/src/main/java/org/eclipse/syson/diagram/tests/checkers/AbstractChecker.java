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
package org.eclipse.syson.diagram.tests.checkers;

import java.util.Collection;
import java.util.Objects;

import org.assertj.core.api.SoftAssertions;

/**
 * Checks an invariant on an element or list of elements.
 *
 * @param <T>
 *            the type of the element to check
 * @author gdaniel
 */
public abstract class AbstractChecker<T> implements Checker<T> {

    @Override
    public abstract void check(T element);

    /**
     * Checks the invariant on the provided {@code elements}.
     * <p>
     * This method checks all the elements and collects the errors. This ensures that when the check fails it returns
     * all the invalid elements instead of the first one.
     * </p>
     */
    @Override
    public void checkAll(Collection<T> elements) {
        Objects.requireNonNull(elements);
        SoftAssertions softly = new SoftAssertions();
        elements.forEach(element -> softly.check(() -> this.check(element)));
        softly.assertAll();
    }

}
