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
package org.eclipse.syson.sysml.serializer.rendering.fragments;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ModelReference;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.model.StoredModel;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.ConjugatedPortTyping;
import org.omg.sysml.lang.sysml.CrossSubsetting;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Renders compact parser-entry fragments such as references, multiplicities, imports, and feature chains.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class CompactParserFragmentRenderer {

    private static final String CONJUGATED_PORT_DEFINITION_REFERENCE = "conjugatedPortDefinition";

    private static final String CROSSED_FEATURE_REFERENCE = "crossedFeature";

    private static final String MEMBER_ELEMENT_REFERENCE = "memberElement";

    private static final String PORT_DEFINITION_REFERENCE = "portDefinition";

    private static final String REDEFINED_FEATURE_REFERENCE = "redefinedFeature";

    private static final String REFERENCED_FEATURE_REFERENCE = "referencedFeature";

    private static final String TYPE_REFERENCE = "type";

    private static final String GENERAL_REFERENCE = "general";

    private static final String SUBSETTED_FEATURE_REFERENCE = "subsettedFeature";


    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final MultiplicityText multiplicities;

    private final SpecializationTargetNames specializationTargets;

    private final NestedTargetSearch nestedTargets;

    private final ReferenceTargets referenceTargets;

    /** Text of each single-line parser rule, keyed by entry point; see {@link #simpleFragmentTexts()}. */
    private final Map<String, SimpleFragmentText> simpleFragmentTexts;

    /**
     * Creates rendering for compact parser-rule fragments, retaining the syntax and spellings of each entry point.
     *
     * @param declarations
     *            writes shared declaration clauses used by fragments
     * @param referenceResolver
     *            supplies fragment names and retained reference spellings
     * @param referenceWriter
     *            emits fragment text with its reference targets
     * @param context
     *            supplies the parser entry point, output and model queries for this run
     */
    public CompactParserFragmentRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.multiplicities = context.modelQueries().multiplicities();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.nestedTargets = context.modelQueries().nestedTargets();
        this.referenceTargets = context.modelQueries().referenceTargets();
        this.simpleFragmentTexts = this.simpleFragmentTexts();
    }

    /**
     * Matches the root of a parse made with one of the single-line parser rules of {@link #simpleFragmentTexts()}
     * (arguments, type references, multiplicities, filters, guards, ...).
     */
    public boolean canSerializeSimpleFragment(final Element element) {
        final String entryPoint = this.context.options().parserEntryPoint();
        return this.simpleFragmentTexts.containsKey(entryPoint)
                && SourceSyntax.isRootParserEntry(this.context.options(), element, entryPoint);
    }

    /**
     * Writes the one-line text of a simple fragment, unwrapping the membership of {@code ...Member} rules. Most of these
     * rules parse without name resolution, so the text comes from parser spellings rather than resolved targets. Always
     * ends the line, even when the text is empty.
     */
    public void serializeSimpleFragment(final Element element) {
        final String entryPoint = this.context.options().parserEntryPoint();
        Element related = element;
        if (element instanceof final Relationship relationship) {
            related = this.navigation.ownedRelatedElements(relationship).stream()
                    .findFirst().orElse(null);
        }
        final String text = this.simpleFragmentTexts.get(entryPoint).text(element, related);
        final TextOutput output = this.context.output();
        Element referenceContext = related;
        if (referenceContext == null) {
            referenceContext = element;
        }
        this.referenceWriter.writeReferenceText(output, text, referenceContext)
                .newline();
    }

    /**
     * Builds the text function of every single-line parser rule. {@code root} is the parse root, {@code related} the
     * element its membership owns for {@code ...Member} rules (the root itself otherwise, possibly null). Most of these
     * rules parse without name resolution, so the texts come from parser spellings rather than resolved targets.
     */
    private Map<String, SimpleFragmentText> simpleFragmentTexts() {
        final SimpleFragmentText featureValue = (root, related) -> this.parserFeatureValueText(related);
        final SimpleFragmentText featureType = (root, related) -> this.parserFeatureTypeText(related);
        final SimpleFragmentText flowFeature = (root, related) -> this.parserRelationshipTargetText(related,
                SysMLPackage.eINSTANCE.getRedefinition());
        final SimpleFragmentText flowEnd = (root, related) -> this.parserFlowEndText(related);
        final SimpleFragmentText danglingTarget = (root, related) -> this.referenceResolver.referenceName(this.currentParsedTarget(root));
        final SimpleFragmentText multiplicity = (root, related) -> this.parserMultiplicityText(related);
        final SimpleFragmentText filterImport = (root, related) -> this.parserFilterImportText(root);
        final SimpleFragmentText namedArgument = (root, related) -> this.parserNamedArgumentText(related);
        return Map.ofEntries(
                Map.entry(ParserFragments.ARGUMENT, featureValue),
                Map.entry(ParserFragments.ARGUMENT_EXPRESSION, featureValue),
                Map.entry(ParserFragments.ARGUMENT_MEMBER, featureValue),
                Map.entry(ParserFragments.ARGUMENT_EXPRESSION_MEMBER, featureValue),
                Map.entry(ParserFragments.SATISFACTION_PARAMETER, featureValue),
                Map.entry(ParserFragments.SATISFACTION_SUBJECT_MEMBER, featureValue),
                Map.entry(ParserFragments.NODE_PARAMETER, featureValue),
                Map.entry(ParserFragments.NODE_PARAMETER_MEMBER, featureValue),
                Map.entry(ParserFragments.TYPE_REFERENCE, featureType),
                Map.entry(ParserFragments.TYPE_REFERENCE_MEMBER, featureType),
                Map.entry(ParserFragments.TYPE_RESULT_MEMBER, featureType),
                Map.entry(ParserFragments.PREFIX_METADATA_USAGE, featureType),
                Map.entry(ParserFragments.PREFIX_METADATA_MEMBER,
                        (root, related) -> this.prefixMetadataMemberText(related)),
                Map.entry(ParserFragments.FLOW_FEATURE, flowFeature),
                Map.entry(ParserFragments.FLOW_FEATURE_MEMBER, flowFeature),
                Map.entry(ParserFragments.FLOW_END, flowEnd),
                Map.entry(ParserFragments.FLOW_END_MEMBER, flowEnd),
                Map.entry(ParserFragments.FLOW_END_SUBSETTING,
                        (root, related) -> this.pathPrefix(this.parserChainedFeatureText(related))),
                Map.entry(ParserFragments.FEATURE_CHAIN_PREFIX,
                        (root, related) -> this.pathPrefix(this.parserChainedFeatureText(root))),
                Map.entry(ParserFragments.OWNED_FEATURE_CHAINING, danglingTarget),
                Map.entry(ParserFragments.OWNED_SUBCLASSIFICATION, danglingTarget),
                Map.entry(ParserFragments.OWNED_MULTIPLICITY, multiplicity),
                Map.entry(ParserFragments.MULTIPLICITY_RANGE, multiplicity),
                Map.entry(ParserFragments.MULTIPLICITY_SOURCE_END_MEMBER, multiplicity),
                Map.entry(ParserFragments.BODY_PARAMETER_MEMBER, (root, related) -> this.bodyParameterText(related)),
                Map.entry(ParserFragments.FILTER_PACKAGE_IMPORT, filterImport),
                Map.entry(ParserFragments.FILTER_PACKAGE_MEMBERSHIP_IMPORT, filterImport),
                Map.entry(ParserFragments.FILTER_PACKAGE_NAMESPACE_IMPORT, filterImport),
                Map.entry(ParserFragments.FILTER_PACKAGE_MEMBER, (root, related) -> this.filterConditionText(root)),
                Map.entry(ParserFragments.FILTER_PACKAGE, (root, related) -> this.parserFilterPackageText(root)),
                Map.entry(ParserFragments.NAMED_ARGUMENT, namedArgument),
                Map.entry(ParserFragments.NAMED_ARGUMENT_MEMBER, namedArgument),
                Map.entry(ParserFragments.EQUALITY_EXPRESSION_REFERENCE,
                        (root, related) -> this.parserExpressionFragmentText(root)),
                Map.entry(ParserFragments.GUARD_EXPRESSION_MEMBER, (root, related) -> this.guardText(related)));
    }

    /** Returns {@code in name} for a body parameter member. */
    private String bodyParameterText(final Element related) {
        String text = "";
        if (related != null) {
            text = SysMLTokens.IN_PREFIX
                    + this.referenceResolver.declaredName(related);
        }
        return text;
    }

    /** Returns {@code #Type} for a prefix metadata member, or an empty string when its type is unknown. */
    private String prefixMetadataMemberText(final Element related) {
        final String target = this.parserFeatureTypeText(related);
        String text = "";
        if (!target.isBlank()) {
            text = SysMLTokens.METADATA_KEYWORD_PREFIX + target;
        }
        return text;
    }

    /** Returns the {@code [condition]} of an element filter membership. */
    private String filterConditionText(final Element filterMembership) {
        final String expression = this.navigation
                .ownedRelatedElements((Relationship) filterMembership).stream()
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast)
                .map(this.referenceWriter::expressionText).findFirst().orElse("");
        return SysMLTokens.OPEN_BRACKET + expression + SysMLTokens.CLOSE_BRACKET;
    }

    /** Returns {@code if condition} for a guard member, or an empty string without condition. */
    private String guardText(final Element related) {
        final String expression = this.parserExpressionFragmentText(related);
        if (expression.isBlank()) {
            return "";
        }
        return SysMLTokens.IF_PREFIX + expression;
    }

    /** Returns {@code chain.} for the rules that parse a chain prefix, or an empty string for an empty chain. */
    private String pathPrefix(final String chain) {
        String prefix = "";
        if (!chain.isBlank()) {
            prefix = chain + SysMLTokens.PATH_SEPARATOR;
        }
        return prefix;
    }

    /** Returns the text of the first non-blank value expression of {@code element}, or an empty string. */
    private String parserFeatureValueText(final Element element) {
        if (element == null) {
            return "";
        }
        return this.navigation.featureValues(element).stream()
                .map(this.navigation::featureValueExpression)
                .map(this.referenceWriter::expressionText)
                .filter(text -> !text.isBlank()).findFirst().orElse("");
    }

    /**
     * Returns {@code name = value}, taking the name from the redefined parameter (or the declared name), or the value alone.
     */
    private String parserNamedArgumentText(final Element element) {
        String text = "";
        if (element != null) {
            final String name = this.specializationTargets
                    .redefinedFeatureNames(element,
                            this.context.danglingReferenceIndex(),
                            this.context.nameRenderer()::referenceName)
                    .stream().findFirst()
                    .orElse(this.referenceResolver.declaredName(element));
            final String value = this.parserFeatureValueText(element);
            text = value;
            if (!name.isBlank()) {
                text = name + SysMLTokens.EQUALS_INFIX + value;
            }
        }
        return text;
    }

    /** Returns the complete expression owned by the fragment rule; non-expressions have no expression syntax. */
    private String parserExpressionFragmentText(final Element element) {
        if (element instanceof final Expression expression) {
            return this.referenceWriter.expressionText(expression);
        }
        return "";
    }

    /** Returns the dot-separated feature chain found in the element's subtree, preferring parser spellings. */
    private String parserChainedFeatureText(final Element element) {
        if (element == null) {
            return "";
        }
        final List<String> names = this.nestedTargets.directFeatureChainNames(
                element, this.context.danglingReferenceIndex());
        return names.stream().map(this.referenceResolver::referenceName)
                .collect(Collectors
                        .joining(SysMLTokens.PATH_SEPARATOR));
    }

    /** Returns the parser spelling of the element's first typing target, or an empty string. */
    private String parserFeatureTypeText(final Element element) {
        String text = "";
        if (element != null) {
            final String target = this.navigation.ownedRelationships(element).stream()
                    .filter(relationship -> this.navigation.isMetaclass(relationship,
                            SysMLPackage.eINSTANCE.getFeatureTyping()))
                    .flatMap(relationship -> this.context
                            .danglingReferenceIndex().targets(relationship)
                            .stream())
                    .findFirst().orElse("");
            if (!target.isBlank()) {
                text = this.referenceResolver.referenceName(target);
            }
        }
        return text;
    }

    /**
     * Returns the parser spelling of the target of the first owned relationship of the given metaclass, or an empty
     * string.
     */
    private String parserRelationshipTargetText(final Element element,
            final EClass relationshipClass) {
        if (element == null) {
            return "";
        }
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        relationshipClass))
                .flatMap(relationship -> this.context.danglingReferenceIndex()
                        .targets(relationship).stream())
                .findFirst().map(this.referenceResolver::referenceName).orElse("");
    }

    /**
     * Returns a flow end as {@code path.feature}: the end's endpoint path, extended with the redefined flow feature when the
     * path does not already end with it.
     */
    private String parserFlowEndText(final Element element) {
        String text = "";
        if (element != null) {
            final String path = this.referenceResolver.endpointPath(element);
            final String redefined = this.navigation.ownedRelationships(element).stream()
                    .flatMap(relationship -> this.navigation
                            .ownedRelatedElements(relationship).stream())
                    .flatMap(related -> this.specializationTargets
                            .redefinedFeatureNames(related,
                                    this.context.danglingReferenceIndex(),
                                    this.context.nameRenderer()::referenceName)
                            .stream())
                    .findFirst().orElse("");
            text = path;
            if (!redefined.isBlank() && !path.isBlank()
                    && !path.endsWith(SysMLTokens.PATH_SEPARATOR + redefined)) {
                text = path + SysMLTokens.PATH_SEPARATOR
                        + this.referenceResolver.referenceName(redefined);
            }
        }
        return text;
    }

    /** Returns the element's own multiplicity, or the nested one when it has none. */
    private String parserMultiplicityText(final Element element) {
        String text = "";
        if (element != null) {
            text = this.multiplicities.multiplicityText(element,
                    this.referenceWriter::expressionText);
            if (text.isBlank()) {
                text = this.multiplicities.crossFeatureMultiplicityText(element,
                        this.referenceWriter::expressionText);
            }
        }
        return text;
    }

    /** Returns a filter package as {@code target[condition]} from its import and filter membership. */
    private String parserFilterPackageText(final Element element) {
        final Relationship importRelationship = this.navigation.ownedRelationships(element)
                .stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getMembershipImport())
                        || this.navigation.isMetaclass(relationship,
                                SysMLPackage.eINSTANCE.getNamespaceImport()))
                .findFirst().orElse(null);
        String target = "";
        if (importRelationship != null) {
            target = this.parserFilterImportText(importRelationship);
        }
        final String filter = this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getElementFilterMembership()))
                .flatMap(relationship -> this.navigation
                        .ownedRelatedElements(relationship).stream())
                .filter(candidate -> candidate instanceof Expression)
                .map(candidate -> this.referenceWriter.expressionText((Expression) candidate))
                .findFirst().orElse("");
        return target + SysMLTokens.OPEN_BRACKET + filter
                + SysMLTokens.CLOSE_BRACKET;
    }

    /** Returns an import target with its {@code ::*}, {@code ::**} or {@code ::*::**} suffix, using the parser spelling. */
    private String parserFilterImportText(final Element element) {
        String parsedTarget;
        if (element instanceof final Import importElement) {
            parsedTarget = this.referenceTargets.importTargetName(importElement, this.context.danglingReferenceIndex(),
                    this.context.nameRenderer()::referenceName);
        } else {
            parsedTarget = this.currentParsedTarget(element);
        }
        final String target = this.referenceResolver.referenceName(parsedTarget);
        String text = target;
        if (element instanceof final Import importElement
                && !target.isBlank()) {
            final boolean namespaceImport = this.navigation.isMetaclass(element,
                    SysMLPackage.eINSTANCE.getNamespaceImport());
            if (importElement.isRecursive()) {
                text = target + this.recursiveImportSuffix(namespaceImport);
            } else if (namespaceImport) {
                text = target + SysMLTokens.MEMBER_IMPORT_SUFFIX;
            }
        }
        return text;
    }

    private String recursiveImportSuffix(final boolean namespaceImport) {
        String suffix = SysMLTokens.DESCENDANT_IMPORT_SUFFIX;
        if (namespaceImport) {
            suffix = SysMLTokens.RECURSIVE_MEMBER_IMPORT_SUFFIX;
        }
        return suffix;
    }

    /** Matches the root of a {@code TargetParameter} parse or the root membership of an {@code AssignmentTargetMember} parse. */
    public boolean canSerializeTargetParameter(final Element element) {
        final boolean targetParameter = SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.TARGET_PARAMETER)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getReferenceUsage());
        final boolean assignmentTarget = SourceSyntax.isRootParserEntry(this.context.options(), element,
                        ParserFragments.ASSIGNMENT_TARGET_MEMBER)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getParameterMembership());
        return targetParameter || assignmentTarget;
    }

    /**
     * Writes the assignment target binding followed by {@code .}, as the target parameter rule spells it before the assigned
     * feature. Writes an empty line when the target has no value.
     */
    public void serializeTargetParameter(final Element element) {
        Element targetParameter = element;
        if (this.navigation.isMetaclass(element,
                SysMLPackage.eINSTANCE.getParameterMembership())) {
            targetParameter = this.navigation.ownedRelatedElements((Relationship) element)
                    .stream().findFirst().orElse(null);
        }
        Expression valueExpression = null;
        if (targetParameter != null) {
            valueExpression = this.navigation.featureValues(targetParameter).stream()
                    .map(this.navigation::featureValueExpression).findFirst()
                    .orElse(null);
        }
        String expressionText = "";
        if (valueExpression != null) {
            expressionText = this.referenceWriter.expressionText(valueExpression);
        }
        if (expressionText.isBlank()) {
            this.context.output().newline();
        } else {
            this.referenceWriter.writeExpression(this.context.output(), valueExpression)
                    .token(SysMLTokens.PATH_SEPARATOR).newline();
        }
    }

    /** Matches the root feature membership of a {@code ForVariableDeclarationMember} parse. */
    public boolean canSerializeForVariableDeclaration(final Element element) {
        return SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.FOR_VARIABLE_DECLARATION_MEMBER)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getFeatureMembership());
    }

    /** Writes the loop variable declaration without a keyword, after its non-default visibility. */
    public void serializeForVariableDeclaration(final Element element) {
        final TextOutput output = this.context.output();
        if (element instanceof final Membership membership
                && (this.flags.isPrivate(membership)
                        || this.flags.isProtected(membership))) {
            output.token(membership.getVisibility().getName()).space();
        }
        this.navigation.ownedRelatedElements((Relationship) element).stream()
                .findFirst().ifPresent(related -> this.declarations.serializeMembershipFeature("", related));
    }

    /**
     * Matches a reference usage parsed as {@code VariantReference}, or as {@code VariantUsageElement} without {@code ref} when it
     * references a feature chain; both are written as the referenced feature rather than as a declaration.
     */
    public boolean canSerializeVariantReference(final Element element) {
        final boolean variantUsageElement = SourceSyntax.isRootParserEntry(this.context.options(),
                element, ParserFragments.VARIANT_USAGE_ELEMENT)
                && !this.flags.hasExplicitRefPrefix(element)
                && !this.nestedTargets.firstChainedFeatureName(element,
                        ReferenceSubsetting.class,
                        this.context.danglingReferenceIndex()).isBlank();
        return (SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.VARIANT_REFERENCE) || variantUsageElement)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getReferenceUsage());
    }

    /**
     * Writes a variant reference: the base reference, then its other specializations, multiplicity, values, and body or
     * {@code ;}.
     */
    public void serializeVariantReference(final Element element) {
        final TextOutput output = this.context.output();
        this.declarations.serializePrefixModifiers(element);
        final String baseReference = this.parserVariantBaseReference(element);
        this.referenceWriter.writeReferenceText(output,
                this.referenceResolver.referenceName(baseReference), element);
        boolean baseReferenceSkipped = false;
        for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
            if (relationship instanceof ReferenceSubsetting
                    && !baseReferenceSkipped) {
                // The first reference subsetting supplied the base reference already emitted above.
                baseReferenceSkipped = true;
                continue;
            }
            this.writeParserVariantSpecialization(output, relationship);
        }
        final String multiplicity = this.multiplicities.multiplicityText(element,
                this.referenceWriter::expressionText);
        if (!multiplicity.isBlank()) {
            this.referenceWriter.writeReferenceText(output, multiplicity, element);
        }
        this.declarations.serializeFeatureValues(element);
        this.declarations.renderBodyOrStatementEnd(element);
    }

    /**
     * Returns the referenced feature that serves as the variant's written name, or the feature chain of its subtree.
     */
    private String parserVariantBaseReference(final Element element) {
        final String referencedFeature = this.specializationTargets
                .referencedFeatureNames(element,
                        this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName)
                .stream().findFirst().orElse("");
        if (!referencedFeature.isBlank()) {
            return referencedFeature;
        }
        return this.nestedTargets.firstChainedFeatureName(element, ReferenceSubsetting.class, this.context.danglingReferenceIndex());
    }

    /**
     * Writes a {@code ::>}, {@code :} or crossing clause for one owned relationship; other relationships and unknown targets
     * produce nothing.
     */
    private void writeParserVariantSpecialization(final TextOutput output,
            final Relationship relationship) {
        final String target = this.parserRelationshipTarget(relationship);
        if (target.isBlank()) {
            return;
        }
        if (relationship instanceof CrossSubsetting) {
            output.space().token(SysMLTokens.CROSSING_OPERATOR).space();
        } else if (relationship instanceof ReferenceSubsetting) {
            output.space().token(SysMLTokens.REFERENCE_SUBSETTING_OPERATOR)
                    .space();
        } else if (this.navigation.isMetaclass(relationship,
                SysMLPackage.eINSTANCE.getFeatureTyping())) {
            output.space().token(SysMLTokens.TYPE_SEPARATOR).space();
        } else {
            return;
        }
        this.referenceWriter.writeReferenceText(output, target, relationship);
    }

    /** Returns the parser spelling of a relationship target, or the feature chain of its owned element. */
    private String parserRelationshipTarget(final Relationship relationship) {
        final String target = this.context.danglingReferenceIndex()
                .targets(relationship).stream().findFirst().orElse("");
        if (!target.isBlank()) {
            return this.referenceResolver.referenceName(target);
        }
        return this.navigation.ownedRelatedElements(relationship).stream()
                .map(related -> String.join(SysMLTokens.PATH_SEPARATOR,
                        this.nestedTargets.directFeatureChainNames(related,
                                this.context.danglingReferenceIndex())))
                .filter(Predicate.not(String::isBlank)).findFirst()
                .map(this.referenceResolver::referenceName).orElse("");
    }

    /**
     * Matches a detached, unnamed relationship (typing, subsetting, membership, ...) parsed on its own, whose written
     * form is just its target. Initial node and alias members are excluded because they have their own syntax.
     */
    public boolean canSerializeRootRelationshipTarget(
            final Relationship relationship) {
        final boolean namedMembership = relationship instanceof final Membership membership
                && (!this.flags.memberName(membership).isBlank()
                        || !this.flags.memberShortName(membership).isBlank());
        boolean serializable = relationship.eContainer() == null
                && !namedMembership;
        serializable = serializable
                && !SourceSyntax.isRootParserEntry(this.context.options(), relationship,
                        ParserFragments.INITIAL_NODE_MEMBER);
        serializable = serializable && !SourceSyntax.isRootParserEntry(this.context.options(), relationship, ParserFragments.ALIAS_MEMBER);
        return serializable
                && !this.rootRelationshipTargetText(relationship).isBlank();
    }

    /**
     * Returns the written target of a root relationship: the parser spelling of its target reference, else the owned
     * feature chain, else the resolved target name. Conjugated port typings get the <code>&#126;</code> prefix.
     */
    private String rootRelationshipTargetText(final Relationship relationship) {
        final String[] referenceNames = this
                .rootRelationshipReferenceNames(relationship);
        if (referenceNames.length == 0) {
            return "";
        }

        String target = Arrays.stream(referenceNames)
                .map(referenceName -> this.currentParsedTarget(relationship, referenceName))
                .filter(name -> !name.isBlank()).findFirst().orElse("");
        final List<Element> ownedRelatedElements = this.navigation
                .ownedRelatedElements(relationship);
        if (target.isBlank()) {
            target = ownedRelatedElements.stream()
                    .map(related -> String.join(SysMLTokens.PATH_SEPARATOR,
                            this.nestedTargets.directFeatureChainNames(related,
                                    this.context.danglingReferenceIndex())))
                    .filter(name -> !name.isBlank()).findFirst().orElse("");
        }
        if (target.isBlank() && ownedRelatedElements.isEmpty()) {
            target = StoredModel.targets(relationship).stream().findFirst()
                    .map(this.context.nameRenderer()::referenceName).orElse("");
        }
        if (relationship instanceof ConjugatedPortTyping && !target.isBlank()
                && !target.startsWith(SysMLTokens.CONJUGATION_OPERATOR)) {
            target = SysMLTokens.CONJUGATION_OPERATOR + target;
        }
        return this.referenceResolver.referenceName(target);
    }

    /** Returns the current text of the first reference of {@code element} that has a parser spelling; see below. */
    private String currentParsedTarget(final Element element) {
        return this.context.danglingReferenceIndex().referenceNames(element).stream()
                .map(referenceName -> this.currentParsedTarget(element, referenceName))
                .filter(name -> !name.isBlank())
                .findFirst()
                .orElse("");
    }

    /**
     * Returns the text of a reference that has a parser spelling: the spelling while nothing is installed or it still
     * designates the installed target, else the name of the target installed since parsing, so that an edited target
     * wins over the stale spelling. Empty when the reference has no parser spelling.
     */
    private String currentParsedTarget(final Element element, final String referenceName) {
        final List<String> parsedNames = this.context.danglingReferenceIndex().targets(element, referenceName);
        if (parsedNames.isEmpty()) {
            return "";
        }
        return this.referenceTargets.referenceTargets(element, referenceName, this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName).stream()
                .map(ModelReference::name)
                .filter(name -> !name.isBlank())
                .findFirst()
                .orElse(parsedNames.get(0));
    }

    /**
     * Returns the EMF reference names that may hold the target of {@code relationship}, most specific first, or an
     * empty array for relationship kinds without a target form.
     */
    private String[] rootRelationshipReferenceNames(
            final Relationship relationship) {
        String[] references = new String[0];
        if (relationship instanceof ConjugatedPortTyping) {
            references = new String[] { PORT_DEFINITION_REFERENCE,
                CONJUGATED_PORT_DEFINITION_REFERENCE, TYPE_REFERENCE,
                GENERAL_REFERENCE };
        } else if (relationship instanceof FeatureTyping) {
            references = new String[] { TYPE_REFERENCE, GENERAL_REFERENCE };
        } else if (relationship instanceof Redefinition) {
            references = new String[] { REDEFINED_FEATURE_REFERENCE,
                SUBSETTED_FEATURE_REFERENCE, GENERAL_REFERENCE };
        } else if (relationship instanceof ReferenceSubsetting) {
            references = new String[] { REFERENCED_FEATURE_REFERENCE,
                SUBSETTED_FEATURE_REFERENCE, GENERAL_REFERENCE };
        } else if (relationship instanceof CrossSubsetting) {
            references = new String[] { CROSSED_FEATURE_REFERENCE,
                SUBSETTED_FEATURE_REFERENCE, GENERAL_REFERENCE };
        } else if (relationship instanceof Subsetting) {
            references = new String[] { SUBSETTED_FEATURE_REFERENCE, GENERAL_REFERENCE };
        } else if (this.navigation.isMetaclass(relationship,
                SysMLPackage.eINSTANCE.getMembership())
                || this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getOwningMembership())) {
            references = new String[] { MEMBER_ELEMENT_REFERENCE };
        }
        return references;
    }

    /**
     * Writes the target of a root relationship on its own line, traced as a feature chain, an owned element, or a
     * relationship reference depending on how the target is held.
     */
    public void serializeRootRelationshipTarget(final Relationship relationship) {
        this.declarations.serializeUserKeywords(relationship);
        final String text = this.rootRelationshipTargetText(relationship);
        final Element related = this.navigation.ownedRelatedElements(relationship)
                .stream().findFirst().orElse(null);
        final boolean chainedTarget = related != null
                && !this.nestedTargets
                        .directFeatureChainNames(related,
                                this.context.danglingReferenceIndex())
                        .isEmpty();
        if (chainedTarget) {
            this.referenceWriter.writeChainedFeatureReferences(this.context.output(),
                    text, related).newline();
        } else if (related != null && this.navigation.isMetaclass(relationship,
                SysMLPackage.eINSTANCE.getOwningMembership())) {
            this.referenceWriter.writeReferenceText(this.context.output(), text, related)
                    .newline();
        } else {
            this.referenceWriter.writeRelationshipReference(this.context.output(), text,
                            relationship,
                            this.rootRelationshipReferenceNames(relationship))
                    .newline();
        }
    }

    /**
     * Matches a detached, unnamed feature that is only a feature chain: no specializations, values or multiplicity.
     */
    public boolean canSerializeRootFeatureChain(final Feature feature) {
        boolean serializable = feature.eContainer() == null
                && this.referenceResolver.declaredName(feature).isBlank();
        serializable = serializable
                && !this.nestedTargets
                        .directFeatureChainNames(feature,
                                this.context.danglingReferenceIndex())
                        .isEmpty();
        serializable = serializable && !this.hasFeatureSpecializations(feature);
        serializable = serializable
                && this.navigation.featureValues(feature).isEmpty();
        return serializable && this.multiplicities
                .multiplicityText(feature, this.referenceWriter::expressionText)
                .isBlank();
    }

    /** Tells whether the feature redefines, subsets, references or crosses anything. */
    private boolean hasFeatureSpecializations(final Feature feature) {
        boolean hasSpecializations = !this.specializationTargets.redefinedFeatureNames(feature,
                this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName).isEmpty();
        hasSpecializations = hasSpecializations
                || !this.specializationTargets
                        .subsettedFeatureNames(feature,
                                this.context.danglingReferenceIndex(),
                                this.context.nameRenderer()::referenceName)
                        .isEmpty();
        hasSpecializations = hasSpecializations
                || !this.specializationTargets
                        .referencedFeatureNames(feature,
                                this.context.danglingReferenceIndex(),
                                this.context.nameRenderer()::referenceName)
                        .isEmpty();
        return hasSpecializations
                || !this.specializationTargets
                        .crossedFeatureNames(feature,
                                this.context.danglingReferenceIndex(),
                                this.context.nameRenderer()::referenceName)
                        .isEmpty();
    }

    /** Writes the feature chain of a detached feature on its own line. */
    public void serializeRootFeatureChain(final Feature feature) {
        this.referenceWriter.writeChainedFeatureReferences(this.context.output(),
                String.join(SysMLTokens.PATH_SEPARATOR,
                        this.nestedTargets.directFeatureChainNames(feature,
                                this.context.danglingReferenceIndex())),
                feature).newline();
    }

    /** Text of a single-line parser rule, from the parse root and the element its root membership owns. */
    @FunctionalInterface
    private interface SimpleFragmentText {

        /**
         * Renders a fragment from its parse root and the member unwrapped by the caller.
         *
         * @param root
         *            the model element returned by the selected parser entry point
         * @param related
         *            the element owned by a root membership, or the root itself for other rules; may be {@code null}
         *            when the membership has no owned element
         * @return the fragment text to write before the caller's terminating line break
         */
        String text(Element root, Element related);
    }
}
