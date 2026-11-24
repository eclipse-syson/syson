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

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;

/**
 * Writes {@code exhibit} state usages, either as the shorthand {@code exhibit s;} that references an existing state or
 * as a full declaration {@code exhibit state s : S { ... }}.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ExhibitStateRenderer {

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final PerformedActionTargetResolver targetResolver;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes modifiers, specializations, values and bodies
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param referenceWriter
     *            writes reference text with traceability metadata
     * @param context
     *            the serialization context
     * @param targetResolver
     *            resolves the exhibited state of the shorthand form
     */
    public ExhibitStateRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context,
            final PerformedActionTargetResolver targetResolver) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.targetResolver = targetResolver;
    }

    /**
     * Writes an exhibit state usage (grammar rule {@code ExhibitStateUsage}). An unnamed usage with a target and no
     * explicit {@code state} keyword is written in the {@code OwnedReferenceSubsetting} form {@code exhibit target ...;};
     * any other usage is written {@code exhibit state name ...} with its body. When serializing a standalone
     * exhibit-state fragment, user-defined keywords precede {@code exhibit}, as the fragment grammar requires.
     *
     * @param exhibitStateUsage
     *            the exhibit state usage
     */
    public void serializeExhibitStateUsage(final Element exhibitStateUsage) {
        this.declarations.serializePrefixModifiers(exhibitStateUsage);
        final boolean exhibitStateFragment = SourceSyntax.isRootParserEntry(this.context.options(), exhibitStateUsage,
                ParserFragments.EXHIBIT_STATE_USAGE);
        if (exhibitStateFragment) {
            this.declarations.serializeUserKeywords(exhibitStateUsage);
        }
        this.context.output().token(SysMLTokens.EXHIBIT_KEYWORD);
        final String name = this.referenceResolver.declaredName(exhibitStateUsage);
        final String target = this.targetResolver.resolve(exhibitStateUsage);
        final boolean explicitStateKeyword = this.context.modelQueries().adapters().hasTextAdapter(exhibitStateUsage,
                ITextAdapter.EXPLICIT_STATE_USAGE_KEYWORD);
        if (!target.isBlank() && name.isBlank() && !explicitStateKeyword) {
            this.serializeExhibitedTarget(exhibitStateUsage, target);
        } else {
            this.serializeExhibitedStateDeclaration(exhibitStateUsage, name, !exhibitStateFragment);
        }
    }

    /**
     * Writes {@code target ...} after {@code exhibit}. The target is the usage's reference subsetting, so reference
     * clauses are not repeated. The {@code StateUsageBody} that follows is the same as for a declared state, so its
     * subactions, entry transitions and {@code parallel} keyword are written as a state body, or {@code ;} when it is
     * empty.
     */
    private void serializeExhibitedTarget(final Element exhibitStateUsage, final String target) {
        this.referenceWriter.writeReferenceText(this.context.output().space(), target, exhibitStateUsage);
        this.declarations.serializeSpecializations(exhibitStateUsage, true, false);
        this.declarations.serializeFeatureValues(exhibitStateUsage);
        this.declarations.renderBodyOrStatementEnd(exhibitStateUsage);
    }

    /** Writes {@code state [keywords] name ...} after {@code exhibit}, followed by the state body or {@code ;}. */
    private void serializeExhibitedStateDeclaration(final Element exhibitStateUsage, final String name,
            final boolean writeUserKeywords) {
        this.context.output().space().token(SysMLTokens.STATE_KEYWORD);
        if (writeUserKeywords) {
            this.declarations.serializeUserKeywords(exhibitStateUsage);
        }
        this.declarations.renderOptionalName(name);
        this.declarations.serializeSpecializations(exhibitStateUsage, true);
        this.declarations.serializeFeatureValues(exhibitStateUsage);
        this.declarations.renderBodyOrStatementEnd(exhibitStateUsage);
    }
}
