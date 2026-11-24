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
package org.eclipse.syson.sysml.serializer.rendering.connections;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;

/**
 * Writes event occurrence usages in the two forms of their grammar: the shorthand that references an existing
 * occurrence ({@code event a.b;}, {@code event e = v;}) and the full declaration
 * ({@code event occurrence e : E :>> x;}).
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class EventOccurrenceRenderer {

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes modifiers, specializations and statement ends
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param referenceWriter
     *            writes references with traceability metadata
     * @param context
     *            the serialization context
     */
    public EventOccurrenceRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
    }

    /**
     * Writes an event occurrence usage (grammar {@code eventOccurrenceUsage}) in one of its two forms, followed by
     * its value and its body or {@code ;}. The shorthand {@code event target} names the referenced occurrence in
     * place of a declaration; it applies to an unnamed event unless the source spelled the reference as a clause.
     * Any other event is declared with {@code event occurrence}, as any occurrence usage.
     *
     * @param eventOccurrenceUsage
     *            the event occurrence usage
     */
    public void serializeEventOccurrenceUsage(final Element eventOccurrenceUsage) {
        this.writeEventPrefix(eventOccurrenceUsage);
        final TextOutput output = this.context.output().token(SysMLTokens.EVENT_KEYWORD);
        final String name = this.referenceResolver.declaredName(eventOccurrenceUsage);
        final String target = this.referenceResolver.directReferencedElementTarget(eventOccurrenceUsage);
        if (!this.referenceResolver.hasIdentification(eventOccurrenceUsage) && !target.isBlank() && !this.hasReferenceClause(eventOccurrenceUsage)) {
            this.referenceWriter.writeReferenceText(output.space(), target, eventOccurrenceUsage);
            this.declarations.serializeSpecializations(eventOccurrenceUsage, true, false);
        } else {
            output.space().token(SysMLTokens.OCCURRENCE_KEYWORD);
            this.declarations.renderOptionalIdentification(eventOccurrenceUsage, name);
            this.declarations.serializeSpecializations(eventOccurrenceUsage, true);
        }
        this.declarations.serializeFeatureValues(eventOccurrenceUsage);
        this.declarations.renderBodyOrStatementEnd(eventOccurrenceUsage);
    }

    /** Tells whether the source wrote the reference as a {@code ::>} or {@code references} clause. */
    private boolean hasReferenceClause(final Element eventOccurrenceUsage) {
        return this.adapters.hasTextAdapter(eventOccurrenceUsage, ITextAdapter.REFERENCES_SYMBOL)
                || this.adapters.hasTextAdapter(eventOccurrenceUsage, ITextAdapter.REFERENCES);
    }

    /**
     * Writes the direction and prefix modifiers; user keywords too in a standalone event-occurrence fragment, whose
     * grammar puts them before {@code event}.
     */
    private void writeEventPrefix(final Element eventOccurrenceUsage) {
        final TextOutput output = this.context.output();
        final String direction = this.flags.direction(eventOccurrenceUsage);
        if (!direction.isBlank()) {
            output.token(direction).space();
        }
        this.declarations.serializePrefixModifiers(eventOccurrenceUsage);
        if (SourceSyntax.isRootParserEntry(this.context.options(), eventOccurrenceUsage,
                ParserFragments.EVENT_OCCURRENCE_USAGE)) {
            this.declarations.serializeUserKeywords(eventOccurrenceUsage);
        }
    }
}
