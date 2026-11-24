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
package org.eclipse.syson.sysml.serializer.rendering.relationships;

import java.util.List;
import java.util.Set;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expose;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.NamespaceImport;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes {@code import} and {@code expose} statements, including filtered imports whose conditions the parser wraps in
 * a filter package.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ImportRenderer {

    /** Parser entry points whose rule stops before the {@code relationshipBody} terminator. */
    private static final Set<String> TERMINATOR_FREE_IMPORT_ENTRY_POINTS = Set.of(
            "parseMembershipImport", "parseNamespaceImport",
            "parseMembershipExpose", "parseNamespaceExpose");

    private final DeclarationRenderer declarations;

    private final ElementDispatch dispatch;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    private final ReferenceTargets referenceTargets;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes comments and statement ends
     * @param dispatch
     *            writes the metadata of an expose body
     * @param referenceResolver
     *            renders reference names
     * @param referenceWriter
     *            writes import targets and filter expressions with traceability metadata
     * @param context
     *            the serialization context
     */
    public ImportRenderer(final DeclarationRenderer declarations,
            final ElementDispatch dispatch,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context) {
        this.declarations = declarations;
        this.dispatch = dispatch;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
        this.referenceTargets = context.modelQueries().referenceTargets();
    }

    /**
     * Writes an {@code import} (grammar {@code import_}) or {@code expose} statement: visibility, {@code all}, the
     * target with its {@code ::*} / {@code ::**} / {@code ::*::**} suffix, {@code [filter]} conditions, then the
     * relationship body.
     *
     * @param importRelationship
     *            the import or expose
     */
    void serializeImport(final Import importRelationship) {
        this.declarations.serializeLeadingComments(importRelationship);
        final TextOutput output = this.context.output();
        this.writePrefix(importRelationship, output);
        final Import effectiveImport = this.navigation.nestedImport(importRelationship);
        this.referenceWriter.writeReferencePath(output, this.importTargetName(effectiveImport),
                this.referenceTargets.importTargetElement(effectiveImport))
                .token(importSuffix(effectiveImport));
        this.writeFilterConditions(importRelationship, output);
        this.writeRelationshipBody(importRelationship, output);
    }

    /**
     * Writes visibility, keyword and {@code all}. Expose statements take neither visibility nor {@code all}, which their
     * grammar does not allow.
     */
    private void writePrefix(final Import importRelationship, final TextOutput output) {
        if (importRelationship instanceof Expose) {
            output.token(SysMLTokens.EXPOSE_KEYWORD).space();
            return;
        }
        output.token(importRelationship.getVisibility().getName()).space()
                .token(SysMLTokens.IMPORT_KEYWORD).space();
        if (importRelationship.isImportAll()) {
            output.token(SysMLTokens.ALL_KEYWORD).space();
        }
    }

    /** Writes each filter condition of a filtered import as a {@code [expression]} suffix, skipping empty ones. */
    private void writeFilterConditions(final Import importRelationship, final TextOutput output) {
        for (final Expression filterExpression : this.navigation.importFilterExpressions(importRelationship)) {
            final String text = this.referenceWriter.expressionText(filterExpression);
            if (!text.isBlank()) {
                output.token(SysMLTokens.OPEN_BRACKET);
                this.referenceWriter.writeExpression(output, filterExpression, text);
                output.token(SysMLTokens.CLOSE_BRACKET);
            }
        }
    }

    /**
     * Ends the statement: an expose with metadata gets a metadata body, an import parser fragment (whose rule has no
     * terminator) only a line break, anything else the shared statement end.
     */
    private void writeRelationshipBody(final Import importRelationship, final TextOutput output) {
        final List<Element> exposeMetadata = this.exposeMetadataUsages(importRelationship);
        final String entryPoint = this.context.options().parserEntryPoint();
        if (!exposeMetadata.isEmpty()) {
            output.space().beginBlock();
            exposeMetadata.forEach(this.dispatch::serializeElement);
            output.endBlock().newline();
        } else if (TERMINATOR_FREE_IMPORT_ENTRY_POINTS.contains(entryPoint)
                && SourceSyntax.isRootParserEntry(this.context.options(), importRelationship, entryPoint)) {
            output.newline();
        } else {
            this.declarations.serializeStatementEnd(importRelationship);
        }
    }

    /** Returns the metadata usages in the body of an expose, or an empty list for an import. */
    private List<Element> exposeMetadataUsages(final Import importRelationship) {
        if (!(importRelationship instanceof Expose)) {
            return List.of();
        }
        return this.navigation.bodyRelationships(importRelationship).stream()
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(element -> this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getMetadataUsage()))
                .toList();
    }

    /** Returns the written target of the effective import, keeping the source quoting of its last name segment. */
    private String importTargetName(final Import effectiveImport) {
        final String targetName = this.referenceResolver.referenceName(this.referenceTargets.importTargetName(
                effectiveImport, this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName));
        if (this.adapters.hasTextAdapter(effectiveImport, ITextAdapter.QUOTED_NAME)) {
            return this.referenceResolver.quoteFinalQualifiedNameSegment(targetName);
        }
        return targetName;
    }

    /**
     * Returns the target suffix of grammar rules {@code importedMembership} ({@code ::**} when recursive) and
     * {@code importedNamespace} ({@code ::*}, or {@code ::*::**} when recursive).
     */
    private static String importSuffix(final Import effectiveImport) {
        final boolean namespaceImport = effectiveImport instanceof NamespaceImport;
        String suffix = "";
        if (effectiveImport.isRecursive() && namespaceImport) {
            suffix = SysMLTokens.RECURSIVE_MEMBER_IMPORT_SUFFIX;
        } else if (effectiveImport.isRecursive()) {
            suffix = SysMLTokens.DESCENDANT_IMPORT_SUFFIX;
        } else if (namespaceImport) {
            suffix = SysMLTokens.MEMBER_IMPORT_SUFFIX;
        }
        return suffix;
    }
}
