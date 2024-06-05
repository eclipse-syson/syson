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

/**
 * Checks an invariant on an element or a list of elements.
 *
 * @param <T>
 *            the type of the elements to check
 * @author gdaniel
 */
public interface Checker<T> {

    void check(T element);

    void checkAll(Collection<T> elements);

}
