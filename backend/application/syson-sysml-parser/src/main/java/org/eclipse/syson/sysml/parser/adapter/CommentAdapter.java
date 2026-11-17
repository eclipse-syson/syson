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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

/**
 * Mutable parser-side implementation of {@link ICommentAdapter}.
 *
 * @author ylussaud
 */
public class CommentAdapter implements ICommentAdapter {

    private final List<ParsedComment> leadingComments = new ArrayList<>();

    private final List<ParsedComment> trailingComments = new ArrayList<>();

    /**
     * Adds a leading comment.
     *
     * @param comment
     *            the comment to add
     */
    public void addLeadingComment(ParsedComment comment) {
        this.leadingComments.add(comment);
    }

    /**
     * Adds a trailing comment.
     *
     * @param comment
     *            the comment to add
     */
    public void addTrailingComment(ParsedComment comment) {
        this.trailingComments.add(comment);
    }

    @Override
    public List<ParsedComment> getLeadingComments() {
        return List.copyOf(this.leadingComments);
    }

    @Override
    public List<ParsedComment> getTrailingComments() {
        return List.copyOf(this.trailingComments);
    }

    @Override
    public void notifyChanged(Notification notification) {
        // Nothing to do.
    }

    @Override
    public Notifier getTarget() {
        return null;
    }

    @Override
    public void setTarget(Notifier newTarget) {
        // Nothing to do.
    }

    @Override
    public boolean isAdapterForType(Object type) {
        return type == ICommentAdapter.class;
    }

}
