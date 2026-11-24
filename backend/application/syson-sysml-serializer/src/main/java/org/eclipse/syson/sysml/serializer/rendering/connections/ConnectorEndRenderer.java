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

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.adapter.ParsedComment;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.EndFeatureMembership;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes connector ends for connections, allocations, bindings and interfaces. Each end is a feature that
 * reference-subsets the connected feature; the renderer writes it as {@code [mult] path}, or with a name as
 * {@code name references path} / {@code name ::> path} depending on the construct and the source spelling.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ConnectorEndRenderer {

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    /**
     * Creates the renderer.
     *
     * @param referenceResolver
     *            resolves end names and paths
     * @param referenceWriter
     *            writes reference text with traceability metadata
     * @param context
     *            the serialization context
     */
    public ConnectorEndRenderer(final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context) {
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
        this.multiplicities = context.modelQueries().multiplicities();
    }

    /**
     * Tells whether the connector declares its ends in its body ({@code end a ::> x;}) through plain feature
     * memberships rather than end feature memberships; such a connector cannot be written in the {@code connect a to b}
     * shorthand.
     *
     * @param connector
     *            the connector usage
     * @return {@code true} when the shorthand must not be used
     */
    boolean hasBodyEndsWithoutExplicitEndMembership(final Element connector) {
        final boolean hasExplicitEndMembership = this.navigation.ownedRelationships(connector).stream()
                .anyMatch(EndFeatureMembership.class::isInstance);
        return !hasExplicitEndMembership && this.navigation.bodyRelationships(connector).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getFeatureMembership()))
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .anyMatch(this.flags::isEnd);
    }

    /**
     * Writes an end of the binary {@code connect a to b} form (grammar {@code connectorEnd}) as {@code [mult] path} or
     * {@code [mult] name references path}, keeping {@code ::>} when the source wrote it.
     *
     * @param output
     *            the output to write to
     * @param end
     *            the end feature
     */
    void writeBinaryConnectorEnd(final TextOutput output, final Element end) {
        this.writeConnectorEndMultiplicity(output, end);
        this.writeEndNameAndTarget(output, end, this.referenceKeyword(end, SysMLTokens.REFERENCES_KEYWORD),
                this.connectorEndpointPath(end));
    }

    /**
     * Writes an end of the n-ary {@code connect (a, n ::> b, ...)} form (grammar {@code connectorEnd}) as
     * {@code [mult] path} or {@code [mult] name ::> path}, keeping {@code references} when the source wrote it.
     *
     * @param output
     *            the output to write to
     * @param end
     *            the end feature
     */
    void writeNaryConnectorEnd(final TextOutput output, final Element end) {
        this.writeConnectorEndMultiplicity(output, end);
        this.writeEndNameAndTarget(output, end, this.referenceKeyword(end, SysMLTokens.REFERENCE_SUBSETTING_OPERATOR),
                this.connectorEndpointPath(end));
    }

    /**
     * Writes the {@code ownedCrossMultiplicityMember} of a connector end, followed by a space when present.
     */
    private void writeConnectorEndMultiplicity(final TextOutput output, final Element end) {
        final String multiplicity = this.endMultiplicity(end);
        if (!multiplicity.isBlank()) {
            this.referenceWriter.writeReferenceText(output, multiplicity, end).space();
        }
    }

    /**
     * Writes the two ends of a binary connector part as {@code a to b}.
     *
     * @param output
     *            the output to write to
     * @param ends
     *            the two end features
     * @param endWriter
     *            writes one end in the syntax of the enclosing construct
     */
    void writeBinaryEnds(final TextOutput output, final List<Element> ends,
            final BiConsumer<TextOutput, Element> endWriter) {
        endWriter.accept(output, ends.get(0));
        output.space().token(SysMLTokens.TO_KEYWORD).space();
        endWriter.accept(output, ends.get(1));
    }

    /**
     * Writes the ends of an n-ary connector part as {@code (a, b, c)}.
     *
     * @param output
     *            the output to write to
     * @param ends
     *            the end features
     * @param endWriter
     *            writes one end in the syntax of the enclosing construct
     */
    void writeParenthesizedEnds(final TextOutput output, final List<Element> ends,
            final BiConsumer<TextOutput, Element> endWriter) {
        output.token(SysMLTokens.OPEN_PARENTHESIS);
        for (int index = 0; index < ends.size(); index++) {
            if (index > 0) {
                output.token(SysMLTokens.COMMA).space();
            }
            endWriter.accept(output, ends.get(index));
        }
        output.token(SysMLTokens.CLOSE_PARENTHESIS);
    }

    /**
     * Writes an interface end preceded by the comments the parser attached to it, so comments inside an end list
     * survive.
     *
     * @param output
     *            the output to write to
     * @param end
     *            the end feature
     */
    void writeCommentedInterfaceEnd(final TextOutput output, final Element end) {
        output.raw(this.leadingCommentText(end));
        this.writeInterfaceEnd(output, end);
    }

    /** Returns the parser comments attached before an end; a line comment is terminated so it does not swallow the end. */
    private String leadingCommentText(final Element end) {
        final String comments = this.adapters.leadingComments(end).stream()
                .map(ParsedComment::text)
                .collect(Collectors.joining());
        String text = comments;
        if (comments.isBlank()) {
            text = "";
        } else if (comments.trim().startsWith(SysMLTokens.LINE_COMMENT_PREFIX)
                && !comments.endsWith(SysMLTokens.LINE_FEED) && !comments.endsWith(SysMLTokens.CARRIAGE_RETURN)) {
            text = comments + SysMLTokens.LINE_FEED;
        }
        return text;
    }

    /**
     * Writes an interface or allocation end (grammar {@code interfaceEnd}/{@code connectorEnd}) as {@code [mult]path} or
     * {@code [mult]name ::> path}, using {@code references} when the source wrote the keyword.
     *
     * @param output
     *            the output to write to
     * @param end
     *            the end feature
     */
    public void writeInterfaceEnd(final TextOutput output, final Element end) {
        final String multiplicity = this.endMultiplicity(end);
        if (!multiplicity.isBlank()) {
            this.referenceWriter.writeReferenceText(output, multiplicity, end);
        }
        this.writeEndNameAndTarget(output, end, this.referenceKeyword(end, SysMLTokens.REFERENCE_SUBSETTING_OPERATOR),
                this.referenceResolver.endpointPath(end));
    }

    /**
     * Returns the grammar {@code referencesKeyword} spelling of a named end: the one the source used ({@code ::>} or
     * {@code references}), otherwise the construct's default.
     *
     * @param end
     *            the end feature
     * @param defaultKeyword
     *            the spelling used when the source recorded none
     * @return {@code ::>} or {@code references}
     */
    String referenceKeyword(final Element end, final String defaultKeyword) {
        String keyword = defaultKeyword;
        if (this.adapters.hasTextAdapter(end, ITextAdapter.REFERENCES)) {
            keyword = SysMLTokens.REFERENCES_KEYWORD;
        } else if (this.adapters.hasTextAdapter(end, ITextAdapter.REFERENCES_SYMBOL)) {
            keyword = SysMLTokens.REFERENCE_SUBSETTING_OPERATOR;
        }
        return keyword;
    }

    /**
     * Writes the part of an end after its multiplicity: {@code name referenceKeyword path} for a named end, otherwise the
     * bare path. Either part may be absent.
     */
    private void writeEndNameAndTarget(final TextOutput output, final Element end, final String referenceKeyword,
            final String path) {
        final String declaredName = this.referenceResolver.declaredName(end);
        if (!declaredName.isBlank()) {
            output.token(declaredName);
            if (!path.isBlank()) {
                output.space().token(referenceKeyword).space();
            }
        }
        this.writeEndpointReference(output, this.referenceResolver.referenceName(path), end);
    }

    /**
     * Writes the endpoint reference traced to {@code contextElement}; writes nothing for a blank reference.
     *
     * @param output
     *            the output to write to
     * @param reference
     *            the reference text
     * @param contextElement
     *            the element the reference is traced to
     */
    void writeEndpointReference(final TextOutput output, final String reference, final Element contextElement) {
        if (!reference.isBlank()) {
            this.referenceWriter.writeReferenceText(output, reference, contextElement);
        }
    }

    /** Returns the target of the end's own reference subsetting, excluding cross-multiplicity expressions. */
    private String connectorEndpointPath(final Element end) {
        return this.referenceResolver.endpointPath(end);
    }

    /**
     * Returns the multiplicity of the end feature, or of the first nested feature that has one, as the parser attaches
     * a {@code connectorEnd} multiplicity to a nested cross-multiplicity feature.
     *
     * @param end
     *            the end feature
     * @return the multiplicity text, or an empty string
     */
    String endMultiplicity(final Element end) {
        final String multiplicity = this.multiplicities.multiplicityText(end, this.referenceWriter::expressionText);
        if (!multiplicity.isBlank()) {
            return multiplicity;
        }
        return this.multiplicities.crossFeatureMultiplicityText(end, this.referenceWriter::expressionText);
    }
}
