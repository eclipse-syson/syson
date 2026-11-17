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
 * Preserves the raw block text of a SysML model comment or documentation body.
 *
 * <p>
 * Unlike {@link ICommentAdapter}, this adapter is attached to model {@code comment} and {@code doc} elements. It stores
 * the exact source block, including delimiters and decorative leading stars, so serialization can preserve forms such
 * as:
 * </p>
 *
 * <pre>
 * doc /*
 *      * Text continued.
 *     *&#47;
 * </pre>
 *
 * @author ylussaud
 */
public interface ICommentBodyAdapter extends Adapter {

    /**
     * Gets the exact source block comment, including {@code /*} and {@code *&#47;}.
     *
     * @return the raw block text
     */
    String getRawBody();

}
