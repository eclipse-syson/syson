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

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.ReferenceUsage;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes the anonymous reference usage that types a flow or action payload as {@code ref :>> payload : T;}. The generic
 * declaration path would write it without the redefined {@code payload} feature it depends on.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class PayloadReferenceRenderer {

    private static final String PAYLOAD_FEATURE_NAME = "payload";

    private static final String REDEFINED_FEATURE_REFERENCE = "redefinedFeature";

    private static final String TYPE_REFERENCE = "type";

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final SpecializationTargetNames specializationTargets;

    /**
     * Creates the renderer.
     *
     * @param referenceResolver
     *            resolves reference names
     * @param referenceWriter
     *            writes references with traceability metadata
     * @param context
     *            the serialization context
     */
    public PayloadReferenceRenderer(final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context) {
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.specializationTargets = context.modelQueries().specializationTargets();
    }

    /**
     * Tells whether the element is an unnamed, typed reference usage that redefines {@code payload}.
     *
     * @param element
     *            the candidate element
     * @return {@code true} when {@link #serialize(Element)} applies
     */
    public boolean canSerialize(final Element element) {
        return element instanceof ReferenceUsage
                && this.referenceResolver.declaredName(element).isBlank()
                && this.redefinedFeatureNames(element).stream().anyMatch(PAYLOAD_FEATURE_NAME::equals)
                && !this.featureTypeNames(element).isEmpty();
    }

    /**
     * Writes {@code ref :>> payload : T;} followed by a newline.
     *
     * @param payloadReference
     *            a reference usage accepted by {@link #canSerialize(Element)}
     */
    public void serialize(final Element payloadReference) {
        final TextOutput output = this.context.output()
                .token(SysMLTokens.REF_KEYWORD).space()
                .token(SysMLTokens.REDEFINITION_OPERATOR).space();
        this.referenceWriter.writeRelationshipReferences(output,
                this.referenceResolver.referenceNames(this.redefinedFeatureNames(payloadReference)),
                payloadReference, SysMLPackage.eINSTANCE.getRedefinition(), REDEFINED_FEATURE_REFERENCE)
                .space().token(SysMLTokens.TYPE_SEPARATOR).space();
        this.referenceWriter.writeRelationshipReferences(output,
                this.referenceResolver.referenceNames(this.featureTypeNames(payloadReference)),
                payloadReference, SysMLPackage.eINSTANCE.getFeatureTyping(), TYPE_REFERENCE)
                .token(SysMLTokens.STATEMENT_TERMINATOR).newline();
    }

    private List<String> redefinedFeatureNames(final Element element) {
        return this.specializationTargets.redefinedFeatureNames(element, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
    }

    private List<String> featureTypeNames(final Element element) {
        return this.specializationTargets.featureTypeNames(element, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
    }
}
