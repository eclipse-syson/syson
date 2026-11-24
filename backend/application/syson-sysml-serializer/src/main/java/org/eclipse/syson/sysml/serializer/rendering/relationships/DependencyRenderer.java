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

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelReference;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Dependency;

/**
 * Writes {@code dependency} relationships between clients and suppliers.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class DependencyRenderer {

    private static final String CLIENT_REFERENCE = "client";

    private static final String SUPPLIER_REFERENCE = "supplier";

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ParserAdapterQueries adapters;

    private final ReferenceTargets referenceTargets;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes comments, user keywords and the statement end
     * @param referenceResolver
     *            resolves the declared name and reference spellings
     * @param referenceWriter
     *            writes client and supplier paths with traceability metadata
     * @param context
     *            the serialization context
     */
    public DependencyRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.adapters = context.modelQueries().adapters();
        this.referenceTargets = context.modelQueries().referenceTargets();
    }

    /**
     * Writes a dependency (grammar {@code dependency}) as {@code dependency [name] from clients to suppliers;}. The
     * {@code from} keyword is dropped when the source omitted it (allowed for anonymous dependencies). Client and
     * supplier names keep their parser spellings so unresolved targets survive.
     *
     * @param dependency
     *            the dependency
     */
    public void serializeDependency(final Dependency dependency) {
        this.declarations.serializeLeadingComments(dependency);
        this.declarations.serializeUserKeywords(dependency);
        final TextOutput output = this.context.output().token(SysMLTokens.DEPENDENCY_KEYWORD);
        this.declarations.renderOptionalName(this.referenceResolver.declaredName(dependency));
        output.space();
        if (!this.adapters.hasTextAdapter(dependency, ITextAdapter.DEPENDENCY_FROM_OMITTED)) {
            output.token(SysMLTokens.FROM_KEYWORD).space();
        }
        this.writeTargets(output, this.endpointTargets(dependency, CLIENT_REFERENCE));
        output.space().token(SysMLTokens.TO_KEYWORD).space();
        this.writeTargets(output, this.endpointTargets(dependency, SUPPLIER_REFERENCE));
        this.declarations.serializeStatementEnd(dependency);
    }

    /**
     * Returns the targets of the {@code client} or {@code supplier} reference, preferring parser spellings over model
     * names.
     */
    private List<ModelReference> endpointTargets(final Dependency dependency, final String referenceName) {
        return this.referenceTargets.referenceTargets(dependency, referenceName,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName);
    }

    /** Writes a comma-separated list of qualified names, each traced to its target. */
    private void writeTargets(final TextOutput output, final List<ModelReference> targets) {
        for (int index = 0; index < targets.size(); index++) {
            if (index > 0) {
                output.token(SysMLTokens.COMMA).space();
            }
            final ModelReference target = targets.get(index);
            this.referenceWriter.writeReferencePath(output,
                    this.referenceResolver.referenceName(target.name()),
                    target.element());
        }
    }

}
