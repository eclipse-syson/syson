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
package org.eclipse.syson.sysml.textual.utils;

import org.eclipse.syson.sysml.Element;

public interface INameDeresolver {

    /**
     * Retrieve the name of the given element from a given context.
     *
     * @param element
     *            the element from which a name should be computed
     * @param context
     *            the context element from which the deresolution occurs
     * @return a name or <code>null</code>
     */
    public String getDeresolvedName(Element element, Element context);

}
