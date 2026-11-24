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
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.InterfaceUsage;

/**
 * Writes interface usages in their connector form: {@code interface i : I connect a to b;}, {@code interface connect
 * (a, b, c) { ... }}. Port usages declared as ends stay inline; other members go to the body.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class InterfaceRenderer {

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    private final ConnectorEndRenderer endRenderer;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes user keywords, the usage declaration, the value part and the body
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param context
     *            the serialization context
     * @param endRenderer
     *            writes interface ends
     */
    public InterfaceRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final SerializationContext context,
            final ConnectorEndRenderer endRenderer) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
        this.endRenderer = endRenderer;
    }

    /**
     * Writes an interface usage (grammar {@code interfaceUsage}): {@code interface <short> name : T :> s [m] connect}
     * declaration, ends, then body or terminator. Callers must check
     * {@link #canSerializeInterfaceUsage(InterfaceUsage)} first.
     *
     * @param interfaceUsage
     *            the interface usage
     */
    public void serializeInterfaceUsage(final InterfaceUsage interfaceUsage) {
        this.declarations.serializeOccurrenceUsagePrefix(interfaceUsage);
        this.writeInterfaceDeclaration(interfaceUsage);
        this.writeInterfaceEnds(this.navigation.interfaceEnds(interfaceUsage));
        this.writeInterfaceBody(interfaceUsage);
    }

    /**
     * Writes {@code interface} with the {@code usageDeclaration} (identification and the shared specialization clauses,
     * multiplicity included) and the value part, then {@code connect} when a declaration or value was written (the
     * grammar then requires it) or the source wrote it.
     */
    private void writeInterfaceDeclaration(final InterfaceUsage interfaceUsage) {
        final TextOutput output = this.context.output();
        final boolean hasFeatureValue = !this.navigation.featureValues(interfaceUsage).isEmpty();
        final boolean declared = this.declarations.hasUsageDeclaration(interfaceUsage) || hasFeatureValue;
        this.declarations.serializeUserKeywords(interfaceUsage);
        output.token(SysMLTokens.INTERFACE_KEYWORD);
        this.declarations.renderOptionalIdentification(interfaceUsage,
                this.referenceResolver.declaredName(interfaceUsage));
        this.declarations.serializeSpecializations(interfaceUsage, true);
        this.declarations.serializeFeatureValues(interfaceUsage);
        output.space();
        if (declared || this.adapters.hasTextAdapter(interfaceUsage, ITextAdapter.EXPLICIT_INTERFACE_CONNECT_KEYWORD)) {
            output.token(SysMLTokens.CONNECT_KEYWORD).space();
        }
    }

    /** Writes {@code a to b} for two ends or {@code (a, b, c)} for more, keeping comments attached to each end. */
    private void writeInterfaceEnds(final List<Element> ends) {
        final TextOutput output = this.context.output();
        if (ends.size() > 2) {
            this.endRenderer.writeParenthesizedEnds(output, ends, this.endRenderer::writeCommentedInterfaceEnd);
        } else {
            this.endRenderer.writeBinaryEnds(output, ends, this.endRenderer::writeCommentedInterfaceEnd);
        }
    }

    /** Writes the body with the members that are not ends and the trailing comments, or {@code ;} when there are none. */
    private void writeInterfaceBody(final InterfaceUsage interfaceUsage) {
        this.declarations.serializeSelectedBodyOrStatementEnd(interfaceUsage,
                this.navigation.interfaceBodyRelationships(interfaceUsage));
    }

    /**
     * Tells whether the interface fits the connector form: at least two ends, none declared through a plain feature
     * membership, and no body block in the source unless the source also had a connector declaration.
     *
     * @param interfaceUsage
     *            the interface usage
     * @return {@code true} when {@link #serializeInterfaceUsage(InterfaceUsage)} applies
     */
    public boolean canSerializeInterfaceUsage(final InterfaceUsage interfaceUsage) {
        if (this.endRenderer.hasBodyEndsWithoutExplicitEndMembership(interfaceUsage)) {
            return false;
        }
        final boolean connectorFormInSource = !this.adapters.hasTextAdapter(interfaceUsage, ITextAdapter.BODY_BLOCK)
                || this.adapters.hasTextAdapter(interfaceUsage, ITextAdapter.INTERFACE_CONNECTOR_DECLARATION);
        return this.navigation.interfaceEnds(interfaceUsage).size() >= 2 && connectorFormInSource;
    }
}
