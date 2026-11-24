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
package org.eclipse.syson.sysml.serializer;

import java.util.List;

import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Relationship;

/**
 * Callback that lets domain renderers recurse into nested elements without depending on the rendering session.
 * <p>
 * Every nested visit must go back through the session so that name scopes (used for contextual reference names) and
 * HTML element traces stay balanced. The interface deliberately exposes traversal only, no rendering services.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public interface ElementDispatch {

    /**
     * Renders an element with the renderer the registry selects for it, inside its own name scope and output trace.
     * Implied relationships are skipped because they have no textual syntax.
     *
     * @param element
     *            the element to render
     */
    void serializeElement(Element element);

    /**
     * Renders a relationship as a body member when the caller does not already know its siblings; the containing
     * body's relationship list is recomputed so membership lookahead still sees the source order. Implied
     * relationships are skipped.
     *
     * @param relationship
     *            the relationship to render
     */
    void serializeRelationship(Relationship relationship);

    /**
     * Renders a relationship as a body member, using the caller's body order so renderers can look ahead or behind
     * (for instance to merge a membership with the succession that follows it). Implied relationships are skipped.
     *
     * @param relationship
     *            the relationship to render
     * @param siblings
     *            the owning body's relationships in output order
     * @param relationshipIndex
     *            the position of {@code relationship} in {@code siblings}
     */
    void serializeRelationship(Relationship relationship, List<Relationship> siblings, int relationshipIndex);

    /**
     * Renders the element with the generic definition/usage declaration grammar, bypassing the specialized renderers
     * the registry would otherwise select. Used where the surrounding syntax only admits a plain declaration (a named
     * loop body action, a normalized metadata reference field). Unlike {@link #serializeElement(Element)}, no name
     * scope or output trace is opened.
     *
     * @param element
     *            the element to declare
     */
    void serializeDeclaration(Element element);
}
