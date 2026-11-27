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
package org.eclipse.syson.sysml.serializer.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.expression.ExpressionRenderer;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.references.ReferenceNameMatcher;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes reference-aware text and supplies traceable model occurrences.
 * Reference collection stays lazy so the output implementation can decide whether tracing is needed.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ExpressionAndReferenceWriter {

    private static final String CONJUGATED_PORT_DEFINITION_REFERENCE = "conjugatedPortDefinition";

    private static final String PORT_DEFINITION_REFERENCE = "portDefinition";

    private static final String TYPE_REFERENCE = "type";

    private static final String GENERAL_REFERENCE = "general";

    private final ExpressionRenderer expressions;

    private final ReferenceResolver referenceResolver;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ReferenceTargets referenceTargets;

    private final NestedTargetSearch nestedTargets;

    /**
     * Creates the writer of one serialization run.
     *
     * @param expressions
     *            the run's expression renderer, which also collects reference occurrences
     * @param referenceResolver
     *            the run's reference spelling service
     * @param context
     *            the run's context
     */
    public ExpressionAndReferenceWriter(final ExpressionRenderer expressions, final ReferenceResolver referenceResolver,
            final SerializationContext context) {
        this.expressions = expressions;
        this.referenceResolver = referenceResolver;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.referenceTargets = context.modelQueries().referenceTargets();
        this.nestedTargets = context.modelQueries().nestedTargets();
    }

    /**
     * Renders an expression with its leading comments and writes it with its reference occurrences.
     *
     * @param output
     *            the output receiving the text
     * @param expression
     *            the expression to render, or {@code null} for empty text
     * @return {@code output}
     */
    public TextOutput writeExpression(final TextOutput output,
            final Expression expression) {
        final String text = this.expressions.expressionText(expression);
        return this.writeExpression(output, expression, text);
    }

    /**
     * Writes text that the caller already rendered (or adjusted) for {@code expression}; references are collected from
     * the expression subtree and matched against that text.
     *
     * @param output
     *            the output receiving the text
     * @param expression
     *            the expression whose model references appear in {@code text}
     * @param text
     *            the text to write
     * @return {@code output}
     */
    public TextOutput writeExpression(final TextOutput output,
            final Expression expression, final String text) {
        return output.referenceText(text,
                () -> this.expressions
                        .referenceOccurrences(expression));
    }

    /**
     * Writes text that mentions references owned somewhere below {@code contextElement}, such as a multiplicity, an
     * assignment target or a parser-captured path. Parser spellings made stale by references resolved after parsing are
     * replaced with the targets' current names before writing.
     *
     * @param output
     *            the output receiving the text
     * @param text
     *            the text to write
     * @param contextElement
     *            the element whose subtree owns the references
     * @return {@code output}
     */
    public TextOutput writeReferenceText(final TextOutput output, final String text,
            final Element contextElement) {
        final String updatedText = this.expressions
                .updatedReferenceText(contextElement, text);
        return output.referenceText(updatedText,
                () -> this.expressions
                        .referenceOccurrences(contextElement, updatedText));
    }

    /**
     * Writes a dotted feature chain (for example a metadata prefix) whose segments link to the chained features found
     * in {@code element}'s subtree. The text is written unchanged.
     *
     * @param output
     *            the output receiving the text
     * @param text
     *            the feature chain text
     * @param element
     *            the element owning the chaining features
     * @return {@code output}
     */
    public TextOutput writeChainedFeatureReferences(final TextOutput output,
            final String text, final Element element) {
        return output.referenceText(text, () -> this.chainedFeatureReferenceOccurrences(element, text));
    }

    private List<TextOutput.Reference> chainedFeatureReferenceOccurrences(final Element element, final String text) {
        return this.expressions.visibleReferenceOccurrences(text,
                this.distinctReferenceOccurrences(this.chainedFeatureReferences(element).toList()));
    }

    /** Returns one reference per segment of the feature chains owned in {@code element}'s subtree. */
    private Stream<TextOutput.Reference> chainedFeatureReferences(final Element element) {
        return this.nestedTargets
                .chainedFeatureReferencesInSubtree(element,
                        this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName)
                .stream()
                .flatMap(target -> this.expressions
                        .atomicReferenceOccurrences(
                                this.referenceResolver.referenceName(target.name()),
                                target.element())
                        .stream());
    }

    /**
     * Returns the reference occurrences of {@code element}'s subtree visible in {@code text}, for callers that combine
     * them with other references before writing.
     *
     * @param element
     *            the element whose subtree owns the references
     * @param text
     *            the rendered text
     * @return an immutable list in text order, possibly empty
     */
    public List<TextOutput.Reference> referenceOccurrences(final Element element,
            final String text) {
        return this.expressions
                .referenceOccurrences(element, text);
    }

    /**
     * Writes the target of one relationship, linking only the values of the named EMF references of that relationship.
     * Narrowing to named references avoids linking unrelated text that happens to spell another reference.
     *
     * @param output
     *            the output receiving the text
     * @param text
     *            the rendered target
     * @param relationship
     *            the relationship holding the references
     * @param referenceNames
     *            the names of the EMF references to link
     * @return {@code output}
     */
    public TextOutput writeRelationshipReference(final TextOutput output,
            final String text, final Element relationship,
            final String... referenceNames) {
        return output.referenceText(text, () -> {
            final List<TextOutput.Reference> references = this
                    .relationshipReferenceOccurrences(relationship,
                            referenceNames)
                    .toList();
            return this.distinctReferenceOccurrences(references);
        });
    }

    private Stream<TextOutput.Reference> relationshipReferenceOccurrences(
            final Element relationship, final String... referenceNames) {
        return Arrays.stream(referenceNames)
                .flatMap(referenceName -> this
                        .relationshipReferenceOccurrences(relationship,
                                referenceName));
    }

    private Stream<TextOutput.Reference> relationshipReferenceOccurrences(
            final Element relationship, final String referenceName) {
        final EStructuralFeature feature = relationship.eClass()
                .getEStructuralFeature(referenceName);
        if (!(feature instanceof final EReference reference)) {
            return Stream.empty();
        }
        return this.referenceTargets
                .referenceTargets(relationship, referenceName,
                        this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName)
                .stream()
                .flatMap(target -> this.expressions
                        .atomicReferenceOccurrences(
                                this.referenceResolver.referenceName(target.name()),
                                target.element(), relationship, reference)
                        .stream());
    }

    /**
     * Writes a typing clause target list, linking the types of {@code element}'s owned feature typings. Conjugated port
     * typings also expose their port definition and conjugated port definition, so either spelling links.
     *
     * @param output
     *            the output receiving the text
     * @param text
     *            the rendered type list
     * @param element
     *            the typed feature
     * @return {@code output}
     */
    public TextOutput writeTypingReferences(final TextOutput output,
            final String text, final Element element) {
        return output.referenceText(text, () -> this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getFeatureTyping())
                        || this.navigation.isMetaclass(relationship,
                                SysMLPackage.eINSTANCE
                                        .getConjugatedPortTyping()))
                .flatMap(this::typingReferenceOccurrences).toList());
    }

    /**
     * Writes the target list of one typing clause, linking only the types of the given typings, so that a feature
     * typed in several clauses ({@code : T :> a : U}) links each type once, in its own clause.
     *
     * @param output
     *            the output receiving the text
     * @param text
     *            the rendered type list of the clause
     * @param typings
     *            the feature typings listed in the clause
     * @return {@code output}
     */
    public TextOutput writeTypingReferences(final TextOutput output,
            final String text, final List<? extends Relationship> typings) {
        return output.referenceText(text, () -> typings.stream()
                .flatMap(this::typingReferenceOccurrences).toList());
    }

    /**
     * Returns the types a typing references, and the segments of the feature chain it owns ({@code : a.b}). The
     * references that expose the same target of this typing are merged, but a qualifier repeated by another typing of
     * the list ({@code : P::A, P::B}) is another occurrence to link.
     */
    private Stream<TextOutput.Reference> typingReferenceOccurrences(
            final Relationship relationship) {
        String[] referenceNames = { TYPE_REFERENCE };
        if (this.navigation.isMetaclass(relationship,
                SysMLPackage.eINSTANCE.getConjugatedPortTyping())) {
            referenceNames = new String[] {
                PORT_DEFINITION_REFERENCE,
                CONJUGATED_PORT_DEFINITION_REFERENCE,
                TYPE_REFERENCE, GENERAL_REFERENCE };
        }
        return this.distinctReferenceOccurrences(Stream.concat(this.relationshipReferenceOccurrences(relationship, referenceNames),
                this.navigation.ownedRelatedElements(relationship).stream().flatMap(this::chainedFeatureReferences)).toList()).stream();
    }

    /**
     * Writes a reference to a single known target. A feature-chain path ({@code a.b}) is linked segment by segment from
     * the target's subtree; any other spelling is linked as a whole. An anonymous target that reference-subsets another
     * element, such as a succession end, links to the subsetted element, which is the element the user named.
     *
     * @param output
     *            the output receiving the text
     * @param text
     *            the rendered reference
     * @param target
     *            the referenced object, a proxy, or {@code null} for a dangling reference
     * @return {@code output}
     */
    public TextOutput writeReferencePath(final TextOutput output, final String text,
            final EObject target) {
        if (!(target instanceof final Element element)
                || !text.contains(SysMLTokens.PATH_SEPARATOR)) {
            return output.referenceText(text,
                    () -> this.expressions
                            .atomicReferenceOccurrences(text,
                                    this.referencePathTarget(target)));
        }
        return output.referenceText(text, () -> {
            final EObject referenceTarget = this.referencePathTarget(element);
            return this.expressions.referenceOccurrences(
                    element, text, referenceTarget);
        });
    }

    /**
     * Returns the element reference-subsetted by an anonymous {@code target}, written as the path it references, or
     * {@code target} itself. A named or short-named target is the element the text names, such as the feature an alias
     * is for, even when it references another feature ({@code part p ::> q}).
     */
    private EObject referencePathTarget(final EObject target) {
        if (target instanceof final Element element && element.getDeclaredName() == null && element.getDeclaredShortName() == null) {
            final Element referencedTarget = this.referenceTargets.firstDirectRelationshipTargetElement(element,
                    SysMLPackage.eINSTANCE.getReferenceSubsetting());
            if (referencedTarget != null) {
                return referencedTarget;
            }
        }
        return target;
    }

    /**
     * Writes text naming the targets of {@code element}'s owned relationships of one metaclass (for example subsettings
     * of an event or payload), together with references from {@code element}'s subtree. Stale parser spellings are
     * replaced as in {@link #writeReferenceText(TextOutput, String, Element)}.
     *
     * @param output
     *            the output receiving the text
     * @param text
     *            the text to write
     * @param element
     *            the element owning the relationships
     * @param relationshipClass
     *            the metaclass of the relationships to link
     * @param targetReferenceNames
     *            the EMF references of those relationships that hold the targets
     * @return {@code output}
     */
    public TextOutput writeRelationshipReferences(final TextOutput output,
            final String text, final Element element,
            final EClass relationshipClass,
            final String... targetReferenceNames) {
        final String updatedText = this.expressions
                .updatedReferenceText(element, text);
        return output.referenceText(updatedText, () -> this.visibleRelationshipReferenceOccurrences(element, updatedText,
                relationshipClass, targetReferenceNames));
    }

    private List<TextOutput.Reference> visibleRelationshipReferenceOccurrences(final Element element, final String text,
            final EClass relationshipClass, final String... targetReferenceNames) {
        final List<Relationship> relationships = this.navigation.ownedRelationships(element);
        final List<TextOutput.Reference> directReferences = relationships.stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship, relationshipClass))
                .flatMap(relationship -> this.relationshipReferenceOccurrences(relationship, targetReferenceNames)
                        .distinct())
                .toList();
        final List<TextOutput.Reference> references = Stream
                .concat(directReferences.stream(), this.expressions.referenceOccurrences(element, text).stream())
                .toList();
        return this.expressions.visibleReferenceOccurrences(text, this.distinctReferenceOccurrences(references));
    }

    /** Drops repeated occurrences of the same target under an equivalent spelling, keeping first-seen order. */
    private List<TextOutput.Reference> distinctReferenceOccurrences(
            final List<TextOutput.Reference> references) {
        final List<TextOutput.Reference> distinctReferences = new ArrayList<>();
        for (final TextOutput.Reference reference : references) {
            // Matching spellings must still link to distinct model objects when their targets differ.
            final boolean alreadyPresent = distinctReferences.stream().anyMatch(
                    existing -> existing.target() == reference.target()
                            && ReferenceNameMatcher.equivalent(existing.name(),
                                    reference.name()));
            if (!alreadyPresent) {
                distinctReferences.add(reference);
            }
        }
        return List.copyOf(distinctReferences);
    }

    /**
     * Renders an expression with its leading comments without writing it, for callers that adjust the text first.
     *
     * @param expression
     *            the expression to render, or {@code null}
     * @return the rendered text, empty for {@code null}
     */
    public String expressionText(final Expression expression) {
        return this.expressions.expressionText(expression);
    }

    /**
     * Renders an expression written as an element without the comments that its dispatch already wrote before it.
     *
     * @param expression
     *            the expression to render, or {@code null}
     * @return the rendered text, empty for {@code null}
     * @see ExpressionRenderer#standaloneExpressionText(Expression)
     */
    public String standaloneExpressionText(final Expression expression) {
        return this.expressions.standaloneExpressionText(expression);
    }

    /**
     * Renders the expression bound as the source of an assignment target, grouped when the grammar requires it.
     *
     * @param source
     *            the bound expression, or {@code null}
     * @return the rendered text, empty for {@code null}
     * @see ExpressionRenderer#assignmentTargetSourceText(Expression)
     */
    public String assignmentTargetSourceText(final Expression source) {
        return this.expressions.assignmentTargetSourceText(source);
    }
}
