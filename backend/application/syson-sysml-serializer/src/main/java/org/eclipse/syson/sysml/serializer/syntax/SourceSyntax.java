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
package org.eclipse.syson.sysml.serializer.syntax;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationOptions;
import org.omg.sysml.lang.sysml.Element;

/**
 * Interprets parser entry points and captured syntax choices without rendering model elements.
 * Entry-point predicates use containment and the configured entry name; they do not require parser adapters.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SourceSyntax {

    private SourceSyntax() {
    }

    /**
     * Tells whether the object was produced by the textual parser, which attaches text adapters to what it builds.
     * Renderers use a negative answer to pick a canonical form for models created elsewhere (e.g. diagrams), where no
     * source choice exists to preserve.
     */
    public static boolean hasTextAdapter(final EObject object) {
        for (final var adapter : object.eAdapters()) {
            if (adapter instanceof ITextAdapter) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells whether {@code element} is the root of a standalone fragment parsed with {@code parserEntryPoint}. Such
     * roots have no container, so grammar context that the enclosing rule would normally supply must be inferred from
     * the entry point.
     */
    public static boolean isRootParserEntry(final SerializationOptions options, final Element element,
            final String parserEntryPoint) {
        return element != null && element.eContainer() == null
                && parserEntryPoint
                        .equals(options.parserEntryPoint());
    }

    /**
     * Tells whether {@code element} is directly owned by the root relationship of a fragment parsed with
     * {@code parserEntryPoint}, as happens for {@code ...Member} entry rules whose root is the membership.
     */
    public static boolean isParserEntryChild(final SerializationOptions options, final Element element,
            final String parserEntryPoint) {
        return element != null && element.eContainer() != null
                && element.eContainer().eContainer() == null && parserEntryPoint
                        .equals(options.parserEntryPoint());
    }
}
