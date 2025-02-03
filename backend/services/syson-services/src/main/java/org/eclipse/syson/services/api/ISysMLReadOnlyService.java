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
package org.eclipse.syson.services.api;

import org.eclipse.syson.sysml.Element;

/**
 * Check if an element is read only.
 *
 * @author Arthur Daussy
 */
public interface ISysMLReadOnlyService {

    /**
     * Check if an element is read only.
     *
     * @param element
     *            an element to test
     * @return <code>true</code> if the element is read only, <code>false</code> otherwise
     */
    boolean isReadOnly(Element element);

}
