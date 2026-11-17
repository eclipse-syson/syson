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
package org.eclipse.syson.sysml.parser.adapter;

import org.eclipse.emf.common.notify.Adapter;

/**
 * Preserves the lexical token used for a parsed string literal.
 *
 * @author ylussaud
 */
public interface IStringLiteralFormatAdapter extends Adapter {

    /**
     * Gets the string literal exactly as it appeared in the source, including delimiters and escape sequences.
     *
     * @return the source literal
     */
    String getLiteralText();
}
