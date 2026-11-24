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
package org.eclipse.syson.sysml.serializer.rendering.declarations;

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.LibraryPackage;
import org.omg.sysml.lang.sysml.Package;

/**
 * Renders package and library package declarations.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class PackageRenderer {

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    /**
     * Creates rendering for ordinary and library package declarations using the run's output and syntax decisions.
     *
     * @param declarations
     *            writes package modifiers, user keywords and namespace bodies
     * @param referenceResolver
     *            supplies the declared package name
     * @param syntax
     *            decides whether the package has a statement body
     * @param context
     *            supplies the run's output and parser-adapter queries
     */
    public PackageRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.syntax = syntax;
        this.context = context;
    }

    /**
     * Writes {@code standard library package}, {@code library package} or {@code package} with its short name and name, then a body or
     * {@code ;}. User keywords ({@code prefixMetadataMember}) come right before {@code package}.
     */
    public void serializePackage(final Package packageElement) {
        final TextOutput output = this.context.output();
        if (packageElement instanceof final LibraryPackage libraryPackage
                && libraryPackage.isStandard()) {
            output.token(SysMLTokens.STANDARD_KEYWORD).space();
        }
        if (packageElement instanceof LibraryPackage) {
            output.token(SysMLTokens.LIBRARY_KEYWORD).space();
        }
        this.declarations.serializeUserKeywords(packageElement);
        output.token(SysMLTokens.PACKAGE_KEYWORD);
        final String shortName = this.context.nameRenderer()
                .declaredShortName(packageElement);
        if (!shortName.isBlank()) {
            output.space().token(SysMLTokens.SHORT_NAME_START).token(shortName)
                    .token(SysMLTokens.SHORT_NAME_END);
        }
        this.declarations.renderOptionalName(
                this.referenceResolver.declaredName(packageElement));
        if (!this.syntax.hasSerializableBody(packageElement)) {
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
            return;
        }
        this.declarations.serializeBody(packageElement);
        output.newline();
    }
}
