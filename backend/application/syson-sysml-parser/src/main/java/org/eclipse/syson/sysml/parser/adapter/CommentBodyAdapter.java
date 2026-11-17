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

/**
 * Immutable parser-side implementation of {@link ICommentBodyAdapter}.
 *
 * @author ylussaud
 */
public class CommentBodyAdapter extends AbstractTextAdapter implements ICommentBodyAdapter {

    private final String rawBody;

    /**
     * Creates the adapter.
     *
     * @param rawBody
     *            the exact source block comment
     */
    public CommentBodyAdapter(String rawBody) {
        this.rawBody = rawBody;
    }

    @Override
    public String getRawBody() {
        return this.rawBody;
    }

    @Override
    public boolean isAdapterForType(Object type) {
        return type == ICommentBodyAdapter.class || super.isAdapterForType(type);
    }

}
