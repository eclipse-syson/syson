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

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.BindingConnectorAsUsage;
import org.omg.sysml.lang.sysml.ConnectionUsage;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.EndFeatureMembership;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes connection, allocation and binding usages in their connector shorthand:
 * {@code connection c : C connect a to b;}, {@code connect (a, b, c);}, {@code allocation x allocate a to b;} and
 * {@code binding b bind x = y;}. The declaration part is written only when the source had one or when it carries
 * information the shorthand would lose.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ConnectorRenderer {

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    private final ConnectorEndRenderer endRenderer;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes keywords, values, bodies and statement ends
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param referenceWriter
     *            writes references with traceability metadata
     * @param syntax
     *            syntax queries detecting explicit body content
     * @param context
     *            the serialization context
     * @param endRenderer
     *            writes connector ends
     */
    public ConnectorRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context,
            final ConnectorEndRenderer endRenderer) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
        this.endRenderer = endRenderer;
    }

    /**
     * Writes a connection usage (grammar {@code connectionUsage}): declaration part, feature value, {@code connect}
     * ends, then body or terminator. Callers check {@link #canSerializeConnectionUsage(ConnectionUsage)} first.
     *
     * @param connectionUsage
     *            the connection usage
     */
    public void serializeConnectionUsage(final ConnectionUsage connectionUsage) {
        final List<Element> ends = this.connectionReferenceEnds(connectionUsage);
        this.declarations.serializeOccurrenceUsagePrefix(connectionUsage);
        final boolean hasFeatureValue = !this.navigation.featureValues(connectionUsage).isEmpty();
        final boolean declared = this.writeUsageDeclarationPart(connectionUsage, SysMLTokens.CONNECTION_KEYWORD,
                hasFeatureValue || this.adapters.hasTextAdapter(connectionUsage, ITextAdapter.CONNECTION_KEYWORD));
        if (hasFeatureValue) {
            this.declarations.serializeFeatureValues(connectionUsage);
            this.context.output().space();
        }
        if (!declared) {
            this.declarations.serializeUserKeywords(connectionUsage);
        }
        this.writeConnectorPart(connectionUsage, ends);
        this.writeBodyOrStatementEnd(connectionUsage, ends,
                this.syntax.hasExplicitActionBodyContent(connectionUsage, ends));
    }

    /**
     * Writes the {@code keyword <short> name specializations} declaration part shared by connection, allocation and
     * binding usages (grammar {@code usageDeclaration} after the usage keyword), preceded by the user keywords. The
     * shorthand alternatives of these rules have no declaration, so the part is written whenever the usage has an
     * identification, a multiplicity or an explicit specialization, or when {@code keywordRequired} is set.
     *
     * @return whether the declaration part was written; when it was not, the caller still owes the user keywords
     */
    private boolean writeUsageDeclarationPart(final Element usage, final String keyword,
            final boolean keywordRequired) {
        if (!keywordRequired && !this.declarations.hasUsageDeclaration(usage)) {
            return false;
        }
        this.declarations.serializeUserKeywords(usage);
        this.context.output().token(keyword);
        this.declarations.renderOptionalIdentification(usage, this.referenceResolver.declaredName(usage));
        this.declarations.serializeSpecializations(usage, true);
        this.context.output().space();
        return true;
    }

    /** Writes {@code connect a to b} for two ends and {@code connect (a, b, c)} for more or when the source did. */
    private void writeConnectorPart(final Element connector, final List<Element> ends) {
        final TextOutput output = this.context.output();
        output.token(SysMLTokens.CONNECT_KEYWORD).space();
        if (this.usesNaryConnectorPart(connector, ends)) {
            this.endRenderer.writeParenthesizedEnds(output, ends, this.endRenderer::writeNaryConnectorEnd);
        } else {
            this.endRenderer.writeBinaryEnds(output, ends, this.endRenderer::writeBinaryConnectorEnd);
        }
    }

    /**
     * Tells whether the ends take the parenthesized {@code naryConnectorPart} form: required beyond two ends, and kept for
     * two ends when the parser recorded that the source used it, since both forms build the same model.
     */
    private boolean usesNaryConnectorPart(final Element connector, final List<Element> ends) {
        return ends.size() > 2 || this.adapters.hasTextAdapter(connector, ITextAdapter.NARY_CONNECTOR_PART);
    }

    /**
     * Tells whether the connection fits the {@code connect} shorthand: at least two ends, none declared through a plain
     * feature membership. A body is written after the ends whatever their number.
     *
     * @param connectionUsage
     *            the connection usage
     * @return {@code true} when {@link #serializeConnectionUsage(ConnectionUsage)} applies
     */
    public boolean canSerializeConnectionUsage(final ConnectionUsage connectionUsage) {
        return !this.endRenderer.hasBodyEndsWithoutExplicitEndMembership(connectionUsage)
                && this.connectionReferenceEnds(connectionUsage).size() >= 2;
    }

    /**
     * Returns the ends owned through end feature memberships when there are at least two, otherwise the reference ends
     * found by navigation.
     */
    private List<Element> connectionReferenceEnds(final ConnectionUsage connectionUsage) {
        final List<Element> explicitEnds = this.navigation.ownedRelationships(connectionUsage).stream()
                .filter(EndFeatureMembership.class::isInstance)
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .toList();
        if (explicitEnds.size() >= 2) {
            return explicitEnds;
        }
        return this.navigation.referenceEnds(connectionUsage);
    }

    /**
     * Writes an allocation usage (grammar {@code allocationUsage}): optional {@code allocation name [m] : T}
     * declaration, then {@code allocate (a, b, c)} for more than two ends or when the source used that form,
     * {@code allocate a to b} otherwise, then body or terminator. Callers check
     * {@link #canSerializeAllocationUsage(ConnectionUsage)} first.
     *
     * @param allocationUsage
     *            the allocation usage
     */
    public void serializeAllocationUsage(final ConnectionUsage allocationUsage) {
        final List<Element> ends = this.navigation.referenceEnds(allocationUsage);
        this.declarations.serializeOccurrenceUsagePrefix(allocationUsage);
        this.writeAllocationDeclaration(allocationUsage);
        final boolean hasBody = this.syntax.hasExplicitActionBodyContent(allocationUsage, ends);
        final TextOutput output = this.context.output().token(SysMLTokens.ALLOCATE_KEYWORD);
        if (this.usesNaryConnectorPart(allocationUsage, ends)) {
            this.endRenderer.writeParenthesizedEnds(output.space(), ends, this.endRenderer::writeInterfaceEnd);
        } else {
            output.space();
            this.endRenderer.writeBinaryEnds(output, ends, this.endRenderer::writeInterfaceEnd);
        }
        this.writeBodyOrStatementEnd(allocationUsage, ends, hasBody);
    }

    /**
     * Writes the {@code allocation <s> name specializations} declaration part when the allocation has one or the source
     * wrote the keyword; otherwise only the user keywords, which then precede {@code allocate}.
     */
    private void writeAllocationDeclaration(final ConnectionUsage allocationUsage) {
        final boolean declared = this.writeUsageDeclarationPart(allocationUsage, SysMLTokens.ALLOCATION_KEYWORD,
                this.adapters.hasTextAdapter(allocationUsage, ITextAdapter.EXPLICIT_DECLARATION_KEYWORD));
        if (!declared) {
            this.declarations.serializeUserKeywords(allocationUsage);
        }
    }

    /**
     * Tells whether the allocation has the two or more ends that {@code allocate a to b} or {@code allocate (a, b, c)}
     * requires.
     *
     * @param allocationUsage
     *            the allocation usage
     * @return {@code true} when {@link #serializeAllocationUsage(ConnectionUsage)} applies
     */
    public boolean canSerializeAllocationUsage(final ConnectionUsage allocationUsage) {
        return this.navigation.referenceEnds(allocationUsage).size() >= 2;
    }

    /** Writes the body without the end features (already written inline), or the statement end. */
    private void writeBodyOrStatementEnd(final Element connector, final List<Element> ends,
            final boolean hasBody) {
        if (hasBody) {
            this.declarations.serializeBodyExcludingImplicitFeatures(connector, ends);
            this.context.output().newline();
        } else {
            this.declarations.serializeStatementEnd(connector);
        }
    }

    /**
     * Writes a binding connector (grammar {@code bindingConnectorAsUsage}) as
     * {@code [binding <s> name specializations] bind [m] x = [m] y} followed by its {@code definitionBody}. The binding
     * must have two reference ends.
     *
     * @param bindingConnector
     *            the binding connector
     */
    public void serializeBindingConnector(final BindingConnectorAsUsage bindingConnector) {
        final List<Element> ends = this.navigation.referenceEnds(bindingConnector);
        final TextOutput output = this.context.output();
        this.declarations.serializeOccurrenceUsagePrefix(bindingConnector);
        final boolean declared = this.writeUsageDeclarationPart(bindingConnector, SysMLTokens.BINDING_KEYWORD,
                this.adapters.hasTextAdapter(bindingConnector, ITextAdapter.EXPLICIT_DECLARATION_KEYWORD));
        if (!declared) {
            this.declarations.serializeUserKeywords(bindingConnector);
        }
        output.token(SysMLTokens.BIND_KEYWORD);
        this.writeBindingEnd(output, ends.get(0));
        output.space().token(SysMLTokens.EQUALS_OPERATOR);
        this.writeBindingEnd(output, ends.get(1));
        this.writeBodyOrStatementEnd(bindingConnector, ends,
                this.syntax.hasExplicitActionBodyContent(bindingConnector, ends));
    }

    /** Writes one side of {@code bind}: the optional end multiplicity, then the end reference. */
    private void writeBindingEnd(final TextOutput output, final Element end) {
        output.space();
        final String multiplicity = this.endRenderer.endMultiplicity(end);
        if (!multiplicity.isBlank()) {
            this.referenceWriter.writeReferenceText(output, multiplicity, end).space();
        }
        this.writeBindingEndReference(output, end);
    }

    /**
     * Writes a binding end as {@code name ::> target} (or {@code name references target} as in the source) when it is
     * named, otherwise as its endpoint path.
     */
    private void writeBindingEndReference(final TextOutput output, final Element end) {
        final String name = this.referenceResolver.declaredName(end);
        final String target = this.referenceResolver.firstDirectRelationshipTarget(end,
                SysMLPackage.eINSTANCE.getReferenceSubsetting());
        if (!name.isBlank() && !target.isBlank()) {
            output.token(name).space()
                    .token(this.endRenderer.referenceKeyword(end, SysMLTokens.REFERENCE_SUBSETTING_OPERATOR)).space();
            this.referenceWriter.writeReferenceText(output, target, end);
            return;
        }
        this.endRenderer.writeEndpointReference(output, this.referenceResolver.endpointPath(end), end);
    }

    /**
     * Tells whether the connector has exactly two reference ends, the precondition of the binary shorthands.
     *
     * @param connector
     *            the connector usage
     * @return {@code true} for exactly two ends
     */
    public boolean hasExactlyTwoReferenceEnds(final Element connector) {
        return this.navigation.referenceEnds(connector).size() == 2;
    }
}
