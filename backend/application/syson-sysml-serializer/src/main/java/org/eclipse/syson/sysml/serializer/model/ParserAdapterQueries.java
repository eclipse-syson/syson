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
package org.eclipse.syson.sysml.serializer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.adapter.ICommentAdapter;
import org.eclipse.syson.sysml.parser.adapter.ICommentBodyAdapter;
import org.eclipse.syson.sysml.parser.adapter.INumericLiteralFormatAdapter;
import org.eclipse.syson.sysml.parser.adapter.IStringLiteralFormatAdapter;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.adapter.ParsedComment;

/**
 * Reads the syntax choices that the parser attached to model objects as EMF adapters: text markers, source comments,
 * raw comment bodies and literal spellings.
 * <p>
 * Look here when a source spelling (an optional keyword, a quoted name, a {@code //} comment, a numeric or string
 * literal format) is lost or wrongly restored. These adapters carry information the model does not; the renderers
 * decide how each mode uses them.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ParserAdapterQueries {

    /**
     * Tells whether the parser marked an object with a given text adapter, i.e. whether the source used the syntax
     * alternative that the marker records. Markers are singletons, so identity is compared.
     */
    public boolean hasTextAdapter(final EObject object, final ITextAdapter adapter) {
        if (object != null) {
            for (final Adapter candidate : object.eAdapters()) {
                if (candidate == adapter) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns the source comments that preceded the object, so that they are written back before its syntax. */
    public List<ParsedComment> leadingComments(final EObject object) {
        return this.comments(object, ICommentAdapter::getLeadingComments);
    }

    /** Returns the source comments that followed the object on its line, so that they are written back after it. */
    public List<ParsedComment> trailingComments(final EObject object) {
        return this.comments(object, ICommentAdapter::getTrailingComments);
    }

    /**
     * Returns the comment body exactly as written in the source, including delimiters and layout, or an empty string
     * when the comment was not parsed or its body adapter is absent.
     */
    public String rawCommentBody(final EObject object) {
        return this.adapters(object, ICommentBodyAdapter.class)
                .map(ICommentBodyAdapter::getRawBody)
                .findFirst()
                .orElse("");
    }

    /**
     * Returns the source spelling of a numeric literal (e.g. {@code 1.0e3} rather than the model value), or an empty
     * string when none was recorded.
     */
    public String numericLiteralFormat(final EObject object) {
        return this.adapters(object, INumericLiteralFormatAdapter.class)
                .map(INumericLiteralFormatAdapter::formattedLiteral)
                .findFirst()
                .orElse("");
    }

    /**
     * Returns the source spelling of a string literal, including its escapes, or an empty string when none was
     * recorded.
     */
    public String stringLiteralFormat(final EObject object) {
        return this.adapters(object, IStringLiteralFormatAdapter.class)
                .map(IStringLiteralFormatAdapter::getLiteralText)
                .findFirst()
                .orElse("");
    }

    private List<ParsedComment> comments(final EObject object, final Function<ICommentAdapter, List<ParsedComment>> commentSelector) {
        List<ParsedComment> comments = List.of();
        if (object != null) {
            comments = this.collectComments(object, commentSelector);
        }
        return comments;
    }

    private List<ParsedComment> collectComments(final EObject object, final Function<ICommentAdapter, List<ParsedComment>> commentSelector) {
        List<ParsedComment> comments = null;
        for (final Adapter adapter : object.eAdapters()) {
            if (!adapter.isAdapterForType(ICommentAdapter.class) || !(adapter instanceof final ICommentAdapter commentAdapter)) {
                continue;
            }
            final List<ParsedComment> selected = commentSelector.apply(commentAdapter);
            if (!selected.isEmpty()) {
                if (comments == null) {
                    comments = new ArrayList<>();
                }
                comments.addAll(selected);
            }
        }
        if (comments == null) {
            return List.of();
        }
        return Collections.unmodifiableList(comments);
    }

    /** Returns the parser adapters of the given type attached to an object, in attachment order. */
    private <T> Stream<T> adapters(final EObject object, final Class<T> adapterType) {
        if (object == null) {
            return Stream.empty();
        }
        return object.eAdapters().stream()
                .filter(adapter -> adapter.isAdapterForType(adapterType))
                .filter(adapterType::isInstance)
                .map(adapterType::cast);
    }
}
