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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.adapter.ParsedComment;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ModelReference;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.output.FormattingOptions;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Comment;
import org.omg.sysml.lang.sysml.Documentation;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TextualRepresentation;

/**
 * Renders the {@code comment}, {@code documentation} and {@code textualRepresentation} grammar rules, and replays
 * source notes.
 * <p>
 * Two kinds of comments are handled. Model {@link Comment} elements ({@code comment}, {@code doc}, bare
 * {@code /* ... *}{@code /}) carry their prefix in the model; their body is written from the parser's raw spelling
 * when available, so the original layout survives, and otherwise wrapped from the model body. Source notes captured
 * by the parser ({@code //} line notes and the like, which have no model element) are replayed verbatim before or
 * after the object they were attached to.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class CommentRenderer {

    private static final String ANNOTATED_ELEMENT_REFERENCE = "annotatedElement";

    /** The block comment terminator as exported inside a body, so that it no longer closes the comment. */
    private static final String ESCAPED_BLOCK_COMMENT_END = "* /";

    private final ReferenceResolver referenceResolver;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    private final ReferenceTargets referenceTargets;

    /**
     * Creates the comment renderer of one serialization run.
     *
     * @param referenceResolver
     *            the run's name and reference spelling service
     * @param context
     *            the run's context
     */
    public CommentRenderer(final ReferenceResolver referenceResolver, final SerializationContext context) {
        this.referenceResolver = referenceResolver;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
        this.referenceTargets = context.modelQueries().referenceTargets();
    }

    /**
     * Writes a comment or documentation element with the shortest prefix that keeps its meaning: a bare body when it
     * has no name, annotated elements, locale or recorded {@code comment} keyword; {@code locale "x" /* ... *}{@code /}
     * when only a locale is set; otherwise the full {@code comment}/{@code doc} prefix. Ends with a line break.
     *
     * @param comment
     *            the comment or documentation to write
     */
    public void serializeComment(final Comment comment) {
        final TextOutput output = this.context.output();
        final CommentRenderingPlan plan = this.planCommentRendering(comment);
        if (plan.isBareBody()) {
            output.raw(plan.body()).newline();
        } else if (plan.isLocaleShorthand()) {
            output.token(SysMLTokens.LOCALE_KEYWORD).space().token(plan.locale()).space().raw(plan.body()).newline();
        } else {
            this.serializeCommentPrefix(plan);
            output.space().raw(plan.body()).newline();
        }
    }

    /**
     * Returns the same syntax as {@link #serializeComment(Comment)} as a single string without the final line break,
     * for constructs that are assembled as text (compact action bodies, inline invocation bodies). Annotated element
     * references are not traced in HTML output.
     *
     * @param comment
     *            the comment or documentation to render
     * @return the comment syntax
     */
    public String commentText(final Comment comment) {
        final CommentRenderingPlan plan = this.planCommentRendering(comment);
        final String prefix;
        if (plan.isBareBody()) {
            prefix = "";
        } else if (plan.isLocaleShorthand()) {
            prefix = SysMLTokens.LOCALE_PREFIX + plan.locale() + SysMLTokens.SPACE;
        } else {
            prefix = this.commentPrefixText(plan) + SysMLTokens.SPACE;
        }
        return prefix + plan.body();
    }

    /** Writes {@code comment|doc identification? (about refs)? (locale "x")?}, tracing each annotated element. */
    private void serializeCommentPrefix(final CommentRenderingPlan plan) {
        final TextOutput output = this.context.output();
        output.token(plan.keyword());
        if (!plan.shortName().isBlank()) {
            output.space().token(SysMLTokens.SHORT_NAME_START).token(plan.shortName())
                    .token(SysMLTokens.SHORT_NAME_END);
        }
        if (!plan.name().isBlank()) {
            output.space().token(plan.name());
        }
        final List<ModelReference> annotatedElements = plan.annotatedElements();
        if (!annotatedElements.isEmpty()) {
            output.space().token(SysMLTokens.ABOUT_KEYWORD).space();
            for (int index = 0; index < annotatedElements.size(); index++) {
                if (index > 0) {
                    output.token(SysMLTokens.COMMA).space();
                }
                final ModelReference target = annotatedElements.get(index);
                output.referenceName(this.referenceResolver.referenceName(target.name()), target.element());
            }
        }
        if (!plan.locale().isBlank()) {
            output.space().token(SysMLTokens.LOCALE_KEYWORD).space().token(plan.locale());
        }
    }

    /** String form of {@link #serializeCommentPrefix(CommentRenderingPlan)}. */
    private String commentPrefixText(final CommentRenderingPlan plan) {
        final StringBuilder prefix = new StringBuilder(plan.keyword());
        if (!plan.shortName().isBlank()) {
            prefix.append(SysMLTokens.SHORT_NAME_START_SUFFIX).append(plan.shortName())
                    .append(SysMLTokens.SHORT_NAME_END);
        }
        if (!plan.name().isBlank()) {
            prefix.append(SysMLTokens.SPACE).append(plan.name());
        }
        if (!plan.annotatedElements().isEmpty()) {
            prefix.append(SysMLTokens.ABOUT_INFIX).append(plan.annotatedElements().stream()
                    .map(target -> this.referenceResolver.referenceName(target.name()))
                    .collect(Collectors.joining(SysMLTokens.ARGUMENT_SEPARATOR)));
        }
        if (!plan.locale().isBlank()) {
            prefix.append(SysMLTokens.LOCALE_INFIX).append(plan.locale());
        }
        return prefix.toString();
    }

    /**
     * Gathers what both the streaming and the string renderings need, so they choose the same prefix form. The locale
     * is pre-quoted.
     */
    private CommentRenderingPlan planCommentRendering(final Comment comment) {
        String locale = "";
        if (comment.getLocale() != null) {
            locale = SysMLTextSyntax.quote(comment.getLocale());
        }
        return new CommentRenderingPlan(comment instanceof Documentation,
                this.adapters.hasTextAdapter(comment, ITextAdapter.COMMENT_KEYWORD),
                this.referenceResolver.declaredName(comment),
                this.context.nameRenderer().declaredShortName(comment),
                this.commentAnnotatedElements(comment),
                locale,
                this.commentBody(comment));
    }

    /**
     * Returns the explicit {@code about} targets, including unresolved parser spellings. Documentation always
     * annotates its owner, which the grammar leaves implicit, so it has none.
     */
    private List<ModelReference> commentAnnotatedElements(final Comment comment) {
        if (comment instanceof Documentation) {
            return List.of();
        }
        return this.navigation.ownedRelationships(comment).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getAnnotation()))
                .flatMap(annotation -> this.referenceTargets.referenceTargets(annotation, ANNOTATED_ELEMENT_REFERENCE,
                        this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName).stream())
                .toList();
    }

    /**
     * Writes a {@code rep <shortName> name language "..."} block ({@code textualRepresentation: ('rep' identification?)?
     * 'language' ...}). The {@code rep} keyword is kept when the element has a short name or a name, or the source spelled
     * it. The raw body is replayed as-is in round-trip mode; normalized output re-indents it, and a
     * body without source spelling is wrapped as a block comment first.
     *
     * @param textualRepresentation
     *            the representation to write
     */
    public void serializeTextualRepresentation(final TextualRepresentation textualRepresentation) {
        String body = this.adapters.rawCommentBody(textualRepresentation);
        if (body.isBlank()) {
            body = this.indentedTextualRepresentationBody(this.wrapBlockCommentBody(textualRepresentation.getBody()));
        } else if (this.context.options().mode() != SerializationMode.ROUND_TRIP) {
            body = this.indentedTextualRepresentationBody(body);
        }
        final TextOutput output = this.context.output();
        final String shortName = this.context.nameRenderer().declaredShortName(textualRepresentation);
        final String name = this.referenceResolver.declaredName(textualRepresentation);
        if (!shortName.isBlank() || !name.isBlank()
                || this.adapters.hasTextAdapter(textualRepresentation, ITextAdapter.EXPLICIT_REP_KEYWORD)) {
            output.token(SysMLTokens.REP_KEYWORD).space();
            if (!shortName.isBlank()) {
                output.token(SysMLTokens.SHORT_NAME_START).token(this.referenceResolver.renderedShortName(shortName))
                        .token(SysMLTokens.SHORT_NAME_END).space();
            }
            output.token(name).space();
        }
        output.token(SysMLTokens.LANGUAGE_KEYWORD).space()
                .token(SysMLTextSyntax.quote(textualRepresentation.getLanguage())).newline()
                .raw(body).newline();
    }

    private String indentedTextualRepresentationBody(final String body) {
        final FormattingOptions formatting = this.context.options().formatting();
        return formatting.indentationUnit()
                + body.replace(formatting.lineSeparator(), formatting.lineSeparator() + formatting.indentationUnit());
    }

    /** Prefers the source spelling of the body, which keeps its original line layout, over the model body. */
    private String commentBody(final Comment comment) {
        final String sourceBody = this.adapters.rawCommentBody(comment);
        if (!sourceBody.isBlank()) {
            return sourceBody;
        }
        return this.wrapBlockCommentBody(comment.getBody());
    }

    /**
     * Turns a model body, which may have been typed freely in SysON, into a {@code REGULAR_COMMENT} that the parser
     * reads back as the same body: continuation lines get the {@code *} margin the parser strips. A missing body is
     * an empty comment, and the block terminator inside the body is broken up because KerML offers no way to escape
     * it.
     */
    private String wrapBlockCommentBody(final String modelBody) {
        final String body = Objects.requireNonNullElse(modelBody, "")
                .replace(SysMLTokens.BLOCK_COMMENT_END, ESCAPED_BLOCK_COMMENT_END);
        if (!body.contains(SysMLTokens.LINE_FEED)) {
            return SysMLTokens.BLOCK_COMMENT_START + body + SysMLTokens.BLOCK_COMMENT_END;
        }
        final StringBuilder comment = new StringBuilder(SysMLTokens.BLOCK_COMMENT_START);
        final String[] lines = body.split(SysMLTokens.LINE_FEED, -1);
        for (int index = 0; index < lines.length; index++) {
            if (index > 0) {
                comment.append(SysMLTokens.LINE_FEED);
            }
            comment.append(SysMLTokens.BLOCK_COMMENT_ASTERISK);
            if (!lines[index].isEmpty()) {
                comment.append(SysMLTokens.SPACE).append(lines[index]);
            }
        }
        return comment.append(SysMLTokens.BLOCK_COMMENT_END).toString();
    }

    /**
     * Replays the source notes the parser attached before {@code object}; does nothing for models without them.
     *
     * @param object
     *            the object about to be written
     */
    public void serializeLeadingComments(final EObject object) {
        this.serializeNotes(this.adapters.leadingComments(object));
    }

    /**
     * Replays the source notes the parser attached after {@code object}, at the end of its body.
     *
     * @param object
     *            the object whose body is being closed
     */
    public void serializeTrailingComments(final EObject object) {
        this.serializeNotes(this.adapters.trailingComments(object));
    }

    /** Writes each note verbatim and guarantees it ends a line, since a {@code //} note would swallow what follows. */
    private void serializeNotes(final List<ParsedComment> notes) {
        final TextOutput output = this.context.output();
        for (final ParsedComment note : notes) {
            output.raw(note.text());
            if (!note.text().endsWith(SysMLTokens.LINE_FEED) && !note.text().endsWith(SysMLTokens.CARRIAGE_RETURN)) {
                output.newline();
            }
        }
    }

    /**
     * Returns the directly owned comment elements as an inline {@code { ... }} body, for compact declarations whose
     * body may only hold comments; empty text when there are none, so the caller can end with {@code ;} instead.
     *
     * @param element
     *            the element owning the comments
     * @return the inline body, or an empty string
     */
    public String inlineBodyComments(final Element element) {
        final String comments = this.navigation.ownedRelationships(element).stream()
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(Comment.class::isInstance)
                .map(Comment.class::cast)
                .map(this::commentText)
                .collect(Collectors.joining());
        if (comments.isBlank()) {
            return "";
        }
        return SysMLTokens.OPEN_BRACE + comments + SysMLTokens.CLOSE_BRACE;
    }

    /**
     * Comment content and the explicit prefixes retained by the parser. {@code about} targets are never present for
     * documentation, whose annotated element is implicit.
     */
    private record CommentRenderingPlan(boolean documentation, boolean explicitComment, String name, String shortName,
            List<ModelReference> annotatedElements, String locale, String body) {

        private String keyword() {
            if (this.documentation) {
                return SysMLTokens.DOC_KEYWORD;
            }
            return SysMLTokens.COMMENT_KEYWORD;
        }

        /** {@code /* ... *}{@code /} alone: a comment whose prefix carries nothing. */
        private boolean isBareBody() {
            return !this.documentation && this.hasNoExplicitPrefix() && this.locale.isBlank();
        }

        /** {@code locale "x" /* ... *}{@code /}: the {@code comment} keyword may be omitted before a locale. */
        private boolean isLocaleShorthand() {
            return !this.documentation && this.hasNoExplicitPrefix() && !this.locale.isBlank();
        }

        private boolean hasNoExplicitPrefix() {
            return !this.explicitComment && this.shortName.isBlank() && this.name.isBlank()
                    && this.annotatedElements.isEmpty();
        }
    }
}
