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
package org.eclipse.syson.sysml.serializer.rendering.actions;

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.omg.sysml.lang.sysml.Element;

/**
 * Writes the {@code ActionBody} of an action node whose declaration is made of parameters, such as an accept
 * (payload and receiver), an assignment (target and value) or a terminate (occurrence). Those parameters are written
 * in the declaration, so the body holds only the members the user wrote between braces, such as
 * {@code { in x : T; }}.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class ParameterActionBodyWriter {

    private final DeclarationRenderer declarations;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    /**
     * Creates the writer.
     *
     * @param declarations
     *            writes bodies and statement ends
     * @param syntax
     *            tells which body members are explicit
     * @param context
     *            the serialization context
     */
    ParameterActionBodyWriter(final DeclarationRenderer declarations, final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.declarations = declarations;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
    }

    /**
     * Tells whether the action has explicit body members besides its declaration parameters.
     *
     * @param action
     *            the action node
     * @return {@code true} when a body block must be written
     */
    boolean hasBody(final Element action) {
        return this.syntax.hasExplicitActionBodyContent(action, this.navigation.parameterElements(action));
    }

    /**
     * Writes the body block without the declaration parameters, preceded by a space and without trailing newline.
     *
     * @param action
     *            the action node
     */
    void writeBody(final Element action) {
        this.declarations.serializeBodyExcludingImplicitFeatures(action, this.navigation.parameterElements(action));
    }

    /**
     * Ends an action node statement with its body block and a newline, or with the statement end when it has no
     * explicit body member.
     *
     * @param action
     *            the action node
     */
    void writeBodyOrStatementEnd(final Element action) {
        if (this.hasBody(action)) {
            this.writeBody(action);
            this.context.output().newline();
        } else {
            this.declarations.serializeStatementEnd(action);
        }
    }
}
