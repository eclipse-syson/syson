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

import java.util.List;

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.SuccessionAsUsage;

/**
 * Writes the endpoints of successions and transitions ({@code first [1] a then b}), with the multiplicity that may sit
 * on the end feature or on a nested feature. A named end is written {@code name ::> path}.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SuccessionEndpointRenderer {

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final MultiplicityText multiplicities;

    /**
     * Creates the renderer.
     *
     * @param referenceResolver
     *            resolves endpoint names and paths
     * @param referenceWriter
     *            writes reference text with traceability metadata
     * @param syntax
     *            syntax queries detecting explicit feature bindings
     * @param context
     *            the serialization context
     */
    public SuccessionEndpointRenderer(final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.multiplicities = context.modelQueries().multiplicities();
    }

    /**
     * Returns the endpoint text without writing it: the path, the declared name, or {@code name ::> path} when both
     * exist.
     *
     * @param endpoint
     *            the end feature
     * @return the endpoint text, empty when the end has neither name nor path
     */
    public String path(final Element endpoint) {
        final String path = this.referenceResolver.endpointPath(endpoint);
        final String name = this.referenceResolver.declaredName(endpoint);
        return this.qualifiedPath(name, path);
    }

    /**
     * Writes the endpoint as {@link #path(Element)} would spell it, with the path traced to the endpoint in HTML
     * output. Writes nothing when the end has neither name nor path.
     *
     * @param endpoint
     *            the end feature
     */
    public void write(final Element endpoint) {
        final TextOutput output = this.context.output();
        final String path = this.referenceResolver.endpointPath(endpoint);
        final String name = this.referenceResolver.declaredName(endpoint);
        if (!name.isBlank()) {
            output.token(name);
        }
        if (!path.isBlank()) {
            if (!name.isBlank()) {
                output.space().token(SysMLTokens.REFERENCE_SUBSETTING_OPERATOR)
                        .space();
            }
            this.referenceWriter.writeReferenceText(output, path,
                    endpoint);
        }
    }

    /**
     * Writes the endpoint multiplicity between spaces, so that it stays apart from the endpoint that follows
     * ({@code then [2] b}). The implicit {@code [1]} is omitted unless the caller asks for it, since end features default
     * to it.
     *
     * @param endpoint
     *            the end feature
     * @param renderUnitMultiplicity
     *            whether {@code [1]} must be written
     */
    public void writeMultiplicity(final Element endpoint,
            final boolean renderUnitMultiplicity) {
        final String multiplicity = this.multiplicity(endpoint);
        if (!multiplicity.isBlank() && (renderUnitMultiplicity
                || !SysMLTokens.UNIT_MULTIPLICITY.equals(multiplicity))) {
            this.referenceWriter.writeReferenceText(
                    this.context.output().space(), multiplicity, endpoint);
            this.context.output().space();
        }
    }

    /**
     * Tells whether a source {@code [1]} must be written explicitly because the target has a different multiplicity, as
     * in {@code first [1] a then [*] b}; otherwise the default {@code [1]} is left implicit.
     *
     * @param ends
     *            the succession ends, source first
     * @return {@code true} when the source {@code [1]} must be written
     */
    public boolean shouldRenderUnitSourceMultiplicity(
            final List<Element> ends) {
        boolean renderUnitMultiplicity = false;
        if (ends.size() > 1) {
            final String sourceMultiplicity = this.multiplicity(ends.get(0));
            final String targetMultiplicity = this.multiplicity(ends.get(1));
            renderUnitMultiplicity = SysMLTokens.UNIT_MULTIPLICITY
                    .equals(sourceMultiplicity) && !targetMultiplicity.isBlank()
                    && !SysMLTokens.UNIT_MULTIPLICITY
                            .equals(targetMultiplicity);
        }
        return renderUnitMultiplicity;
    }

    /**
     * Tells whether the succession has user-written feature bindings besides its ends, which must be kept in a body.
     *
     * @param successionAsUsage
     *            the succession
     * @param ends
     *            its end features
     * @return {@code true} when a body with feature bindings must be written
     */
    public boolean hasNonEndpointFeatureBindings(
            final SuccessionAsUsage successionAsUsage,
            final List<Element> ends) {
        return this.navigation.bodyRelationships(successionAsUsage).stream()
                .flatMap(relationship -> this.navigation
                        .ownedRelatedElements(relationship).stream())
                .anyMatch(related -> !ends.contains(related)
                        && this.syntax.isExplicitFeatureBinding(related));
    }

    /** Combines name and path as {@code name ::> path}, or returns whichever is present. */
    private String qualifiedPath(final String name, final String path) {
        String qualifiedPath;
        if (name.isBlank()) {
            qualifiedPath = path;
        } else if (path.isBlank()) {
            qualifiedPath = name;
        } else {
            qualifiedPath = name + SysMLTokens.REFERENCE_SUBSETTING_INFIX
                    + path;
        }
        return qualifiedPath;
    }

    /** Returns the multiplicity of the end feature, or of the first nested feature that has one. */
    private String multiplicity(final Element endpoint) {
        String multiplicity = this.multiplicities.multiplicityText(endpoint,
                this.referenceWriter::expressionText);
        if (multiplicity.isBlank()) {
            multiplicity = this.multiplicities.crossFeatureMultiplicityText(endpoint,
                    this.referenceWriter::expressionText);
        }
        return multiplicity;
    }
}
