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
package org.eclipse.syson.sysml.serializer.rendering;

import java.util.List;
import java.util.Set;

import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.expression.ExpressionRenderer;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.StringTextOutput;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.CommentRenderer;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Starts a rendering session and scopes recursive dispatch with reference-name contexts and element traces.
 * Domain renderers receive their concrete collaborators; only recursive traversal uses ElementDispatch.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SysMLTextRenderer implements ElementDispatch {

    /** Parser rules that build helper model elements without any syntax of their own. */
    private static final Set<String> EMPTY_SYNTAX_FRAGMENTS = Set.of(
            "parseConjugatedPortDefinition", "parseEmptyActionUsage",
            "parseEmptyFeature", "parseEmptyMultiplicity",
            "parseEmptySourceEnd", "parseEmptyTargetEnd", "parseEmptyUsage",
            "parseSelfReferenceMember", "parseTargetFeature");

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ReferenceResolver referenceResolver;

    private final CommentRenderer comments;

    private final DeclarationRenderer declarations;

    private final SysMLTypeRendererRegistry rendererRegistry;

    /**
     * Creates the renderer graph for one serialization run, sharing its output, name scopes and model queries.
     *
     * @param context
     *            the run's context, whose mutable collaborators must not be shared with another active rendering session
     */
    public SysMLTextRenderer(final SerializationContext context) {
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.referenceResolver = new ReferenceResolver(context);
        this.comments = new CommentRenderer(this.referenceResolver, context);
        final ExpressionRenderer expressions = new ExpressionRenderer(this.referenceResolver, this.comments, context, this::relationshipsText);
        final ExpressionAndReferenceWriter referenceWriter = new ExpressionAndReferenceWriter(expressions, this.referenceResolver, context);
        final ElementSyntaxQueries syntax = new ElementSyntaxQueries(context, this.referenceResolver, expressions);
        this.declarations = new DeclarationRenderer(context, this.referenceResolver, expressions, referenceWriter, syntax, this, this.comments);
        this.rendererRegistry = new SysMLTypeRendererRegistry(context, this, this.referenceResolver, referenceWriter, syntax, this.declarations, this.comments);
    }

    /** Renders a complete list of body relationships into the text of a nested expression. */
    public String relationshipsText(final List<Relationship> relationships) {
        final TextOutput output = new StringTextOutput(this.context.options().formatting());
        final SysMLTextRenderer renderer = new SysMLTextRenderer(this.context.withOutput(output));
        for (int index = 0; index < relationships.size(); index++) {
            renderer.serializeRelationship(relationships.get(index), relationships, index);
        }
        return output.content();
    }

    /**
     * Entry point of a rendering session. Writes nothing for a null element or for parser rules that only build helper
     * elements without syntax. The parser's unnamed root namespace contributes only its members and trailing comments,
     * not a declaration of its own.
     */
    public void serialize(final Element element) {
        if (element == null || EMPTY_SYNTAX_FRAGMENTS
                .contains(this.context.options().parserEntryPoint())) {
            return;
        }
        if (element instanceof final Namespace namespace
                && this.isSyntheticRoot(namespace)) {
            this.declarations.serializeNamespaceBody(namespace);
            this.comments.serializeTrailingComments(namespace);
            return;
        }
        this.serializeElement(element);
    }

    /**
     * Renders one element through the rule registry, inside a name scope and an output trace for that element, with its
     * leading comments first. Implied relationships are skipped because they have no source syntax. Scopes are closed
     * even when rendering fails.
     */
    @Override
    public void serializeElement(final Element element) {
        if (element instanceof final Relationship relationship
                && relationship.isImplied()) {
            return;
        }
        final TextOutput output = this.context.output();
        output.beginElement(element);
        this.context.nameRenderer().beginContext(element);
        try {
            this.comments.serializeLeadingComments(element);
            this.rendererRegistry.render(element);
        } finally {
            this.context.nameRenderer().endContext();
            output.endElement(element);
        }
    }

    /**
     * Renders a body relationship when the caller has no sibling list at hand; the siblings are recomputed from the
     * owner's body so membership rendering still sees neighbouring members.
     */
    @Override
    public void serializeRelationship(final Relationship relationship) {
        final Element container = this.navigation.containingElement(relationship);
        final List<Relationship> siblings = this.navigation
                .bodyRelationships(container);
        this.serializeRelationship(relationship, siblings,
                siblings.indexOf(relationship));
    }

    /** Renders a body relationship in its membership syntax; implied relationships are skipped. */
    @Override
    public void serializeRelationship(final Relationship relationship,
            final List<Relationship> siblings, final int relationshipIndex) {
        if (relationship.isImplied()) {
            return;
        }
        this.rendererRegistry.serializeRelationship(relationship, siblings,
                relationshipIndex);
    }

    /** Writes the generic declaration of {@code element}, bypassing the registry rules. */
    @Override
    public void serializeDeclaration(final Element element) {
        this.rendererRegistry.serializeDeclaration(element);
    }

    /**
     * Tells whether {@code namespace} is the unnamed top-level plain Namespace the parser creates around a file's
     * members.
     */
    private boolean isSyntheticRoot(final Namespace namespace) {
        return namespace.eContainer() == null
                && this.navigation.isMetaclass(namespace,
                        SysMLPackage.eINSTANCE.getNamespace())
                && this.referenceResolver.declaredName(namespace).isBlank();
    }
}
