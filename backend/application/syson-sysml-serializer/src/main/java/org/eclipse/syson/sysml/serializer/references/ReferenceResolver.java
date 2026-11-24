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
package org.eclipse.syson.sysml.serializer.references;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.tokens.ReferenceNameSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.FlowEnd;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Entry point for writing references: chooses the target spelling of a reference (model target, or parser spelling
 * for unresolved and round-tripped references), builds endpoint paths ({@code a.b.c}) and formats names for their
 * text position. Shared by expression and declaration rendering.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ReferenceResolver {

    private static final String UNNAMED_QUALIFIED_NAME_SUFFIX = "::''";

    private final SysMLNameFormatter nameFormatter;

    private final SerializationContext context;

    private final ParserAdapterQueries adapters;

    private final ReferenceTargets referenceTargets;

    private final SpecializationTargetNames specializationTargets;

    private final ModelNavigation navigation;

    private final NestedTargetSearch nestedTargets;

    /**
     * Creates a resolver bound to the run's name renderer, dangling references and options.
     *
     * @param context
     *            the serialization run context
     */
    public ReferenceResolver(final SerializationContext context) {
        this.context = context;
        this.adapters = context.modelQueries().adapters();
        this.referenceTargets = context.modelQueries().referenceTargets();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.nestedTargets = context.modelQueries().nestedTargets();
        this.navigation = context.modelQueries().navigation();
        this.nameFormatter = new SysMLNameFormatter(context.nameRenderer(), this.adapters);
    }

    /**
     * Returns the text of the element referenced by {@code element}'s own reference subsetting, as written after a
     * bare reference usage, a framed concern or a subject. When the subsetted target is a feature chain held by an
     * anonymous chaining feature, the dotted chain {@code a.b.c} is written instead of the chain's opaque name;
     * a source name written quoted stays quoted, without quoting its qualification ({@code P::'c'}).
     *
     * @param element
     *            the referencing usage
     * @return the reference text, or an empty string when the element has no such target
     */
    public String directReferencedElementTarget(final Element element) {
        final String target = this.firstDirectRelationshipTarget(element,
                SysMLPackage.eINSTANCE.getReferenceSubsetting());
        final String featureChain = this.nestedTargets.firstChainedFeatureName(element, ReferenceSubsetting.class,
                this.context.danglingReferenceIndex());
        String renderedTarget = target;
        if (!featureChain.isBlank() && (target.isBlank() || this.context.options().implicitReferenceKeywordElision())) {
            renderedTarget = featureChain;
        } else if (this.adapters.hasTextAdapter(element, ITextAdapter.QUOTED_NAME)) {
            renderedTarget = this.nameFormatter.quoteFinalQualifiedNameSegment(target);
        }
        return renderedTarget;
    }

    /**
     * Returns the model target of {@code element}'s own reference subsetting, the element named by
     * {@link #directReferencedElementTarget(Element)}.
     *
     * @param element
     *            the referencing usage
     * @return the target, or {@code null} when absent or unresolved
     */
    public Element directReferencedElementTargetElement(final Element element) {
        return this.referenceTargets.firstDirectRelationshipTargetElement(element,
                SysMLPackage.eINSTANCE.getReferenceSubsetting());
    }

    /**
     * Returns the path naming an endpoint, such as a connector or succession end or a feature reference expression.
     * Sources are tried in order: the end's direct reference subsetting, its owned chain, the expression's referent
     * or feature value, then a dangling parser target. A flow feature supplies a separate final segment. A single source name that
     * contains dots is quoted, because it denotes one identifier rather than feature navigation.
     *
     * @param endpoint
     *            the end feature or expression
     * @return the rendered path, or an empty string when no target can be named
     */
    public String endpointPath(final Element endpoint) {
        final List<String> pathParts = this.endpointPathParts(endpoint);
        final String path;
        if (pathParts.size() != 1) {
            path = this.nameFormatter.referenceName(pathParts.stream()
                    .filter(part -> !part.isBlank())
                    .collect(Collectors.joining(SysMLTokens.PATH_SEPARATOR)));
        } else if (isDottedSimpleName(pathParts.get(0))) {
            path = this.nameFormatter.quotedName(pathParts.get(0));
        } else if (pathParts.get(0).contains(SysMLTokens.NAMESPACE_SEPARATOR)) {
            path = this.nameFormatter.quoteQualifiedNameSegments(pathParts.get(0));
        } else {
            path = this.nameFormatter.referenceName(pathParts.get(0));
        }
        return path;
    }

    /** Tells whether an unquoted, unqualified name contains a dot, which then belongs to the name itself. */
    private static boolean isDottedSimpleName(final String name) {
        return name.contains(SysMLTokens.PATH_SEPARATOR)
                && !name.contains(SysMLTokens.NAMESPACE_SEPARATOR)
                && !name.startsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER);
    }

    /** Collects the raw path segments of an endpoint; see {@link #endpointPath(Element)} for the source order. */
    private List<String> endpointPathParts(final Element endpoint) {
        final List<String> pathParts = new ArrayList<>();
        this.specializationTargets.directReferenceSubsettingTargetNames(endpoint, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName).stream().findFirst()
                .ifPresent(target -> pathParts.addAll(ReferenceNameSyntax.splitOutsideQuotes(target, SysMLTokens.PATH_SEPARATOR)));
        if (pathParts.isEmpty()) {
            pathParts.addAll(this.nestedTargets.chainedFeatureNames(endpoint, this.context.danglingReferenceIndex()));
        }
        this.addFeatureReferenceIfEmpty(pathParts, endpoint);
        if (pathParts.isEmpty()) {
            this.navigation.featureValues(endpoint).stream().map(this.navigation::featureValueExpression)
                    .filter(FeatureReferenceExpression.class::isInstance).findFirst()
                    .ifPresent(expression -> pathParts.addAll(this.endpointPathParts(expression)));
        }
        // Only flowEnd owns a separate flowFeature after its prefix. Equal names still denote two path positions.
        if (endpoint instanceof FlowEnd) {
            final String suffix = this.nestedTargets.nestedReferenceTarget(endpoint, this.context.danglingReferenceIndex());
            if (!suffix.isBlank()) {
                pathParts.add(suffix);
            }
        }
        this.addUnresolvedTargetIfEmpty(pathParts, endpoint);
        return pathParts;
    }

    /** Falls back to the referent of a feature reference expression, unless it is an unnamed feature. */
    private void addFeatureReferenceIfEmpty(final List<String> pathParts, final Element endpoint) {
        if (pathParts.isEmpty()
                && endpoint instanceof final FeatureReferenceExpression expression) {
            final String name = this.nestedTargets.featureReferenceName(expression,
                    this.context.danglingReferenceIndex(),
                    this.context.nameRenderer()::referenceName);
            if (!name.isBlank()
                    && !name.contains(UNNAMED_QUALIFIED_NAME_SUFFIX)) {
                pathParts.add(name);
            }
        }
    }

    /** Falls back to the first parser spelling of an unresolved reference in the endpoint's subtree. */
    private void addUnresolvedTargetIfEmpty(final List<String> pathParts, final Element endpoint) {
        if (pathParts.isEmpty()) {
            final String unresolvedTarget = this.nestedTargets.firstDanglingTarget(
                    endpoint, this.context.danglingReferenceIndex());
            if (!unresolvedTarget.isBlank()) {
                pathParts.add(unresolvedTarget);
            }
        }
    }

    /**
     * Returns the target name of the first owned relationship of the given metaclass. Round-trip mode keeps the
     * spelling captured by the parser; normalized mode writes the model target's name and uses the parsed spelling
     * only when the target is unresolved.
     *
     * @param element
     *            the relationship owner
     * @param relationshipClass
     *            the metaclass of the relationship to look for
     * @return the rendered name, or an empty string when there is none
     */
    public String firstDirectRelationshipTarget(final Element element,
            final EClass relationshipClass) {
        final String parsedTargetName = this.referenceTargets.firstDirectRelationshipTarget(element,
                relationshipClass, this.context.danglingReferenceIndex());
        final Element target = this.firstDirectRelationshipTargetElement(
                element, relationshipClass);
        if (target != null && (parsedTargetName.isBlank()
                || this.context.options().mode() != SerializationMode.ROUND_TRIP)) {
            return this.context.nameRenderer().referenceName(target);
        }
        return this.nameFormatter.referenceName(parsedTargetName);
    }

    /**
     * Returns the model target of the first owned relationship of the given metaclass.
     *
     * @param element
     *            the relationship owner
     * @param relationshipClass
     *            the metaclass of the relationship to look for
     * @return the target, or {@code null} when absent or unresolved
     */
    public Element firstDirectRelationshipTargetElement(final Element element,
            final EClass relationshipClass) {
        return this.referenceTargets.firstDirectRelationshipTargetElement(element,
                relationshipClass);
    }

    /** Tells whether the current declaration has a name or short name that a shorthand would discard. */
    public boolean hasIdentification(final Element element) {
        return !this.declaredName(element).isBlank() || !this.context.nameRenderer().declaredShortName(element).isBlank();
    }

    /**
     * Returns the declared name as written in a declaration, quoted when the source quoted it.
     *
     * @param element
     *            the declared element, or {@code null}
     * @return the rendered name, or an empty string when unnamed
     */
    public String declaredName(final Element element) {
        return this.nameFormatter.declaredName(element);
    }

    /**
     * Renders a raw reference spelling for a reference position: segments that are not names are quoted, a quoted
     * segment repeated across a {@code .} is collapsed, and quoted index-expression segments are unquoted.
     *
     * @param name
     *            the raw reference spelling
     * @return the rendered reference
     */
    public String referenceName(final String name) {
        return this.nameFormatter.referenceName(name);
    }

    /**
     * Renders a comma-separated reference list, as used after {@code :} or {@code :>}.
     *
     * @param names
     *            the raw reference spellings
     * @return the rendered list, empty for an empty list
     */
    public String referenceNames(final List<String> names) {
        return this.nameFormatter.referenceNames(names);
    }

    /**
     * Writes a raw model name, such as an alias name, quoted when the source quoted it or when it is not an identifier;
     * apostrophes around it belong to the name.
     *
     * @param rawName
     *            the decoded model name, or {@code null}
     * @param quoted
     *            whether the source quoted the name
     * @return the escaped name, or an empty string when there is no name
     */
    public String escapedName(final String rawName, final boolean quoted) {
        return this.nameFormatter.escapedName(rawName, quoted);
    }

    /**
     * Writes a name as an unrestricted name, escaping backslashes and quotes; an already quoted name is unchanged.
     *
     * @param name
     *            the raw name
     * @return the quoted name
     */
    public String quotedName(final String name) {
        return this.nameFormatter.quotedName(name);
    }

    /**
     * Renders each {@code ::} segment of a qualified name on its own, quoting the segments that are not identifiers
     * (including dotted ones), so that dots inside a segment are not read as feature navigation.
     *
     * @param name
     *            the qualified name
     * @return the rendered qualified name
     */
    public String quoteQualifiedNameSegments(final String name) {
        return this.nameFormatter.quoteQualifiedNameSegments(name);
    }

    /**
     * Forces quotes on the last {@code ::} segment only, keeping a member name the source wrote quoted.
     *
     * @param name
     *            the qualified name
     * @return the name with a quoted final segment
     */
    public String quoteFinalQualifiedNameSegment(final String name) {
        return this.nameFormatter.quoteFinalQualifiedNameSegment(name);
    }

    /**
     * Renders a short name for its position between {@code <} and {@code >}, quoting it unless it is an identifier.
     *
     * @param shortName
     *            the short name
     * @return the rendered short name
     */
    public String renderedShortName(final String shortName) {
        return this.nameFormatter.renderedShortName(shortName);
    }
}
