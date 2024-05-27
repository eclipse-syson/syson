/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml.export;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;
import static java.util.stream.Collectors.joining;
import static org.eclipse.syson.sysml.export.SysMLRelationPredicates.IS_DEFINITION_BODY_ITEM_MEMBER;
import static org.eclipse.syson.sysml.export.SysMLRelationPredicates.IS_IMPORT;
import static org.eclipse.syson.sysml.export.SysMLRelationPredicates.IS_MEMBERSHIP;
import static org.eclipse.syson.sysml.export.SysMLRelationPredicates.IS_METADATA_USAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.export.utils.Appender;
import org.eclipse.syson.sysml.export.utils.NameDeresolver;
import org.eclipse.syson.sysml.export.utils.SysMLKeywordSwitch;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.util.SysmlSwitch;

import reactor.util.function.Tuples;

/**
 * Convert a SysML {@link Element} to its textual representation.
 *
 * @author Arthur Daussy
 */
public class SysMLElementSerializer extends SysmlSwitch<String> {

    private static final Predicate<Object> NOT_NULL = s -> s != null;

    private String lineSeparator;

    private String indentation;

    private NameDeresolver nameDeresolver;

    private final SysMLKeywordSwitch keywordProvider = new SysMLKeywordSwitch();

    /**
     * Simple constructor.
     *
     * @param newLine
     *            the string used to separate line
     * @param indentation
     *            the string used to indent the file
     */
    public SysMLElementSerializer(String lineSeparator, String indentation, NameDeresolver nameDeresolver) {
        super();
        this.lineSeparator = lineSeparator;
        this.indentation = indentation;
        this.nameDeresolver = nameDeresolver;
    }

    public SysMLElementSerializer() {
        this(System.lineSeparator(), "\t", new NameDeresolver());
    }

    @Override
    public String doSwitch(EObject eObject) {
        String value = super.doSwitch(eObject);
        if (value != null && value.trim().isBlank()) {
            return null;
        } else {
            return value;
        }
    }

    @Override
    public String caseNamespace(Namespace namespace) {
        Appender builder = this.newAppender();

        if (namespace.eContainer() == null && namespace.getName() == null) {
            // Root namespace are not serialized
            String content = this.getContent(namespace.getOwnedMembership());
            if (content != null && !content.isBlank()) {
                builder.appendIndentedContent(content);
            }
        } 
        else {
            builder.append("namespace ");
            this.appendChildrenContent(builder, namespace, namespace.getOwnedMembership());
        }
        return builder.toString();
    }

    @Override
    public String casePackage(Package pack) {

        Appender builder = this.newAppender();

        builder.append("package ");
        this.appendNameWithShortName(builder, pack);

        List<Relationship> children = pack.getOwnedRelationship().stream().filter(IS_MEMBERSHIP.and(IS_METADATA_USAGE.negate()).or(IS_IMPORT)).toList();
        this.appendChildrenContent(builder, pack, children);

        return builder.toString();
    }

    /**
     * This method handle most of the {@link Definition} elements. Some elements may required some custom handling. In
     * this case look for a more specific method.
     */
    @Override
    public String caseDefinition(Definition def) {
        Appender builder = this.newAppender();

        if (def instanceof OccurrenceDefinition occDef) {
            this.appendDefinitionPrefix(builder, occDef);
        }

        builder.appendSpaceIfNeeded().append(this.getDefinitionKeyword(def));

        this.appendDefinition(builder, def);

        return builder.toString();
    }

    @Override
    public String caseLiteralExpression(LiteralExpression expression) {
        Appender builder = this.newAppender();

        if (expression instanceof LiteralInteger lit) {
            builder.append(this.caseLiteralInteger(lit));
        } else if (expression instanceof LiteralString lit) {
            builder.append(this.caseLiteralString(lit));
        } else if (expression instanceof LiteralRational lit) {
            builder.append(this.caseLiteralRational(lit));
        } else if (expression instanceof LiteralBoolean lit) {
            builder.append(this.caseLiteralBoolean(lit));
        } else if (expression instanceof LiteralInfinity lit) {
            builder.append(this.caseLiteralInfinity(lit));
        }

        return builder.toString();
    }

    @Override
    public String caseLiteralString(LiteralString literal) {
        Appender builder = this.newAppender();

        builder.append(literal.getValue());

        return builder.toString();
    }

    @Override
    public String caseLiteralRational(LiteralRational literal) {
        Appender builder = this.newAppender();

        builder.append(String.valueOf(literal.getValue()));

        return builder.toString();
    }

    @Override
    public String caseLiteralBoolean(LiteralBoolean literal) {
        Appender builder = this.newAppender();

        builder.append(String.valueOf(literal.isValue()));

        return builder.toString();
    }

    @Override
    public String caseLiteralInfinity(LiteralInfinity literal) {
        Appender builder = this.newAppender();

        builder.append("*");

        return builder.toString();
    }

    @Override
    public String caseLiteralInteger(LiteralInteger literal) {
        Appender builder = this.newAppender();

        builder.append(String.valueOf(literal.getValue()));

        return builder.toString();
    }

    @Override
    public String caseUsage(Usage usage) {

        Appender builder = this.newAppender();

        this.appendUsagePrefix(builder, usage);

        builder.appendSpaceIfNeeded().append(getUsageKeyword(usage));

        this.appendUsageDeclaration(builder, usage);

        List<Relationship> children = usage.getOwnedRelationship().stream().filter(IS_DEFINITION_BODY_ITEM_MEMBER).toList();

        this.appendChildrenContent(builder, usage, children);

        return builder.toString();
    }

    private String getUsageKeyword(Usage usage) {
        return keywordProvider.doSwitch(usage);
    }

    private void appendFeatureSpecilizationPart(Appender builder, Feature feature) {
        List<Redefinition> ownedRedefinition = feature.getOwnedRedefinition();
        this.appendRedefinition(builder, ownedRedefinition, feature);

        this.appendReferenceSubsetting(builder, feature.getOwnedReferenceSubsetting(), feature);

        List<Subsetting> ownedSubsetting = new ArrayList<>(feature.getOwnedSubsetting());
        ownedSubsetting.removeAll(ownedRedefinition);
        ownedSubsetting.remove(feature.getOwnedReferenceSubsetting());

        this.appendSubsettings(builder, ownedSubsetting, feature);

        this.appendFeatureTyping(builder, feature.getOwnedTyping(), feature);
        this.appendMultiplicityPart(builder, feature);
    }

    private void appendSubsettings(Appender builder, List<Subsetting> subSettings, Element element) {
        if (!subSettings.isEmpty()) {
            builder.appendSpaceIfNeeded().append(LabelConstants.SUBSETTING);
            builder.appendSpaceIfNeeded().append(subSettings.stream().map(this.getSubsettedFeature())
                    .filter(Objects::nonNull)
                    .map(superFeature -> this.getDeresolvableName(superFeature, element))
                    .collect(Collectors.joining(", ")));
        }
    }

    /**
     * When the function Subsetting::getSubsettedFeature() is correctly implemented then remove this function and use
     * only Subsetting::getSubsettedFeature() in appendSubsettings();
     */
    private Function<? super Subsetting, ? extends Feature> getSubsettedFeature() {
        return subsetting -> {
            Feature subsettedFeature = subsetting.getSubsettedFeature();
            if (subsettedFeature != null) {
                return subsettedFeature;
            } else {
                return subsettedFeature;
            }
        };
    }

    private void appendRedefinition(Appender builder, List<Redefinition> redefinitions, Element element) {
        if (!redefinitions.isEmpty()) {
            builder.appendSpaceIfNeeded().append(LabelConstants.REDEFINITION);
            builder.appendSpaceIfNeeded().append(redefinitions.stream().map(Redefinition::getRedefinedFeature)
                    .filter(Objects::nonNull)
                    .map(superFeature -> this.getDeresolvableName(superFeature, element))
                    .collect(Collectors.joining(", ")));
        }
    }

    private void appendReferenceSubsetting(Appender builder, ReferenceSubsetting ownedReferenceSubsetting, Element element) {
        if (ownedReferenceSubsetting != null) {
            builder.appendSpaceIfNeeded().append(LabelConstants.REFERENCES);
            if (ownedReferenceSubsetting.getReferencedFeature() != null) {
                builder.appendSpaceIfNeeded().append(this.getDeresolvableName(ownedReferenceSubsetting.getReferencedFeature(), element));
            }
        }
    }

    private void appendFeatureTyping(Appender builder, EList<FeatureTyping> ownedTyping, Element element) {
        if (!ownedTyping.isEmpty()) {
            builder.appendSpaceIfNeeded().append(LabelConstants.COLON);
            builder.appendSpaceIfNeeded().append(ownedTyping.stream()
                    .filter(ft -> ft.getType() != null)
                    .map(ft -> Tuples.of(ft, ft.getType()))
                    .filter(t -> t.getT2() != null)
                    .map(t -> {
                        String name = this.getDeresolvableName(t.getT2(), element);

                        if (t.getT1() instanceof ConjugatedPortTyping) {
                            name = LabelConstants.CONJUGATED + name;
                        }
                        return name;
                    }

                    )
                    .collect(Collectors.joining(", ")));
        }
    }

    private void appendMultiplicityPart(Appender builder, Feature feature) {
        MultiplicityRange multiplicity = feature.getOwnedElement().stream()
                .filter(MultiplicityRange.class::isInstance)
                .map(MultiplicityRange.class::cast)
                .findFirst()
                .orElse(null);

        if (multiplicity != null) {
            String expression = multiplicity.getBound().stream()
                    .filter(element -> element instanceof LiteralExpression || element instanceof FeatureReferenceExpression)
                    .map(element -> this.mapToExpression(element, feature))
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(".."));

            if (!expression.isEmpty()) {
                builder.appendSpaceIfNeeded().append(LabelConstants.OPEN_BRACKET)
                        .append(expression)
                        .append(LabelConstants.CLOSE_BRACKET);
            }

            boolean isOrdered = multiplicity.isIsOrdered();
            boolean isUnique = multiplicity.isIsUnique();

            if (isOrdered) {
                builder.appendSpaceIfNeeded().append("ordered");
            }
            if (!isUnique) {
                builder.appendSpaceIfNeeded().append("nonunique");
            }
        }
    }

    private String mapToExpression(Element element, Feature feature) {
        String exp = null;
        if (element instanceof LiteralExpression) {
            exp = this.caseLiteralExpression((LiteralExpression) element);
        } else if (element instanceof FeatureReferenceExpression) {
            exp = this.getDeresolvableName(element, feature);
        }
        return exp;
    }

    @Override
    public String caseConjugatedPortDefinition(ConjugatedPortDefinition object) {
        // Conjugated port definition are implicit
        return "";
    }

    private String getDefinitionKeyword(Definition def) {
        return keywordProvider.doSwitch(def) + " def";
    }

    private void appendDefinition(Appender builder, Definition definition) {

        this.appendDefinitionDeclaration(builder, definition);

        List<Relationship> children = definition.getOwnedRelationship().stream().filter(IS_DEFINITION_BODY_ITEM_MEMBER).toList();
        this.appendChildrenContent(builder, definition, children);
    }

    private void appendDefinitionDeclaration(Appender builder, Definition definition) {
        this.appendNameWithShortName(builder, definition);

        EList<Subclassification> subClassification = definition.getOwnedSubclassification();
        if (!subClassification.isEmpty()) {
            builder.appendSpaceIfNeeded().append(":> ");

            String superClasses = subClassification.stream()
                    .map(sub -> sub.getSuperclassifier())
                    .filter(NOT_NULL)
                    .map(sup -> this.getDeresolvableName(sup, definition))
                    .collect(joining(", "));

            builder.append(superClasses);
        }
    }

    private void appendUsageDeclaration(Appender builder, Usage usage) {
        this.appendNameWithShortName(builder, usage);

        this.appendFeatureSpecilizationPart(builder, usage);
    }

    /**
     * Get a deresolvable name for a given element in a given context
     *
     * @param toDeresolve
     *            the object to deresolve
     * @param context
     *            a context
     * @return a name
     */
    private String getDeresolvableName(Element toDeresolve, Element context) {
        return this.nameDeresolver.getDeresolvedName(toDeresolve, context);
    }

    private void appendDefinitionPrefix(Appender builder, Definition def) {

        builder.appendSpaceIfNeeded().append(this.getBasicDefinitionPrefix(def));

        if (def instanceof OccurrenceDefinition occDef) {

            final String isIndividual;
            if (occDef.isIsIndividual()) {
                isIndividual = "individual";
            } else {
                isIndividual = "";
            }
            builder.appendSpaceIfNeeded().append(isIndividual);
        }

        this.appendExtensionKeyword(builder, def);
    }

    private void appendUsagePrefix(Appender builder, Usage usage) {

        this.getBasicUsagePrefix(builder, usage);

        final String isRef;
        if (usage.isIsReference() && !isImplicitlyReferencial(usage)) {
            isRef = "ref";
        } else {
            isRef = "";
        }

        builder.appendSpaceIfNeeded().append(isRef);

        this.appendExtensionKeyword(builder, usage);
    }

    private boolean isImplicitlyReferencial(Usage usage) {
        return usage instanceof AttributeUsage;
    }

    private void appendExtensionKeyword(Appender builder, Type type) {
        for (var rel : type.getOwnedRelationship()) {
            if (rel instanceof OwningMembership owningMember) {
                owningMember.getOwnedRelatedElement().stream()
                        .filter(MetadataUsage.class::isInstance)
                        .map(MetadataUsage.class::cast)
                        .map(MetadataUsage::getMetadataDefinition)
                        .filter(NOT_NULL)
                        .forEach(mDef -> this.appendPrefixMetadataMember(builder, mDef));
            }
        }
    }

    private void appendPrefixMetadataMember(Appender builder, Metaclass def) {
        builder.appendSpaceIfNeeded().append("#");
        this.appendSimpleName(builder, def);
    }

    private void appendSimpleName(Appender appender, Element e) {
        String shortName = e.getName();
        String declaredName = e.getDeclaredName();
        if (shortName != null && !shortName.isBlank()) {
            appender.appendPrintableName(shortName);
        } else if (declaredName != null && !declaredName.isBlank()) {
            appender.appendPrintableName(declaredName);

        } else {
            appender.appendPrintableName(e.effectiveName());
        }
    }

    private String getBasicDefinitionPrefix(Definition def) {
        StringBuilder builder = new StringBuilder();
        if (def.isIsAbstract()) {
            builder.append("abstract");
        }
        if (def.isIsVariation()) {
            if (!builder.isEmpty()) {
                builder.append(" ");
            }
            builder.append("variation");
        }
        return builder.toString();
    }

    private void getBasicUsagePrefix(Appender builder, Usage usage) {
        if (usage.isIsAbstract()) {
            builder.appendSpaceIfNeeded();
            builder.append("abstract");
        }
        if (usage.isIsVariation()) {
            builder.appendSpaceIfNeeded();
            builder.append("variation");
        }
        if (usage.isIsReadOnly()) {
            builder.appendSpaceIfNeeded();
            builder.append("readonly");
        }
        if (usage.isIsDerived()) {
            builder.appendSpaceIfNeeded();
            builder.append("derived");
        }
        if (usage.isIsEnd()) {
            builder.appendSpaceIfNeeded();
            builder.append("end");
        }
    }

    private Appender newAppender() {
        return new Appender(this.lineSeparator, this.indentation);
    }

    private void appendChildrenContent(Appender builder, Element element, List<? extends Relationship> childrenRelationships) {

        String content = this.getContent(childrenRelationships);
        if (content != null && !content.isBlank()) {
            builder.append(" {");
            builder.appendIndentedContent(content);
            builder.newLine().append("}");
        } else {
            builder.append(";");
        }
    }

    private String getContent(List<? extends Relationship> children) {
        return children.stream().map(rel -> this.doSwitch(rel)).filter(NOT_NULL).collect(joining(this.lineSeparator, this.lineSeparator, ""));
    }

    @Override
    public String caseImport(Import aImport) {

        Appender builder = this.newAppender();

        VisibilityKind visibility = aImport.getVisibility();
        if (visibility != VisibilityKind.PUBLIC) {
            builder.append(this.getVisivilityIndicator(visibility));
        }
        builder.appendSpaceIfNeeded().append("import ");

        if (aImport.isIsImportAll()) {
            builder.appendSpaceIfNeeded().append("all");
        }

        if (aImport instanceof NamespaceImport namespaceImport) {
            this.appendNamespaceImport(builder, namespaceImport);
        } else if (aImport instanceof MembershipImport membershipImport) {
            this.appendMembershipImport(builder, membershipImport);
        }

        if (aImport.isIsRecursive()) {
            builder.append("::**");
        }

        this.appendChildrenContent(builder, aImport, aImport.getOwnedRelationship());

        return builder.toString();
    }

    @Override
    public String caseComment(Comment comment) {

        Appender builder = this.newAppender();
        EList<Element> annotatedElements = comment.getAnnotatedElement();
        boolean selfNamespaceDescribingComment = this.isSelfNamespaceDescribingComment(comment);
        if (isNullOrEmpty(comment.getLocale()) && selfNamespaceDescribingComment) {
            builder.append(this.getCommentBody(comment.getBody()));
        } else {
            builder.append("comment");

            this.appendNameWithShortName(builder, comment);

            if (!selfNamespaceDescribingComment) {
                this.appendAnnotatedElements(builder, comment, annotatedElements);
            }

            this.appendLocale(builder, comment.getLocale());

            builder.newLine().indent();
            builder.appendIndentedContent(this.getCommentBody(comment.getBody()));
        }

        return builder.toString();
    }

    /**
     * Returns true if the comment describes its <b>direct</b> owning namespace
     *
     * @param comment
     *            a comment
     * @return true if described is direct owning namespace
     */
    private boolean isSelfNamespaceDescribingComment(Comment comment) {
        EList<Element> annotatedElements = comment.getAnnotatedElement();
        if (!annotatedElements.isEmpty()) {
            Element annotatedElement = annotatedElements.get(0);
            if (annotatedElement instanceof Namespace owningNamespace && (owningNamespace == this.getDirectContainer(comment, Namespace.class))) {
                return true;
            }
        }
        return false;
    }

    private void appendAnnotatedElements(Appender builder, Comment comment, EList<Element> annotatedElements) {
        if (!annotatedElements.isEmpty()) {
            builder.appendSpaceIfNeeded().append("about ");
            builder.append(annotatedElements.stream().map(e -> this.getDeresolvableName(e, comment)).collect(joining(",")));
        }
    }

    /**
     * Returns the direct container of the element with the expected type. A direct container is either the
     * {@link EObject#eContainer()} or the container of the {@link OwningMembership}
     *
     * @param element
     *            an element
     * @param expected
     *            the expected type
     * @return the expected type or <code>null</code> if no direct container of the expected type
     */
    private <T> T getDirectContainer(EObject element, Class<T> expected) {
        EObject eContainer = element.eContainer();
        T result = null;
        if (expected.isInstance(eContainer)) {
            result = (T) eContainer;
        } else if (eContainer instanceof OwningMembership owning && expected.isInstance(owning.eContainer())) {
            result = (T) owning.eContainer();
        }
        return result;
    }

    private String getCommentBody(String body) {
        Appender subBuilder = this.newAppender();
        subBuilder.append("/* ").append(body).append(" */");
        return subBuilder.toString();
    }

    private void appendLocale(Appender builder, String local) {
        if (!isNullOrEmpty(local)) {
            builder.appendSpaceIfNeeded().append("locale").append(" \"").append(local).append("\"");
        }
    }

    @Override
    public String caseOwningMembership(OwningMembership owningMembership) {
        Appender builder = this.newAppender();

        VisibilityKind visibility = owningMembership.getVisibility();
        if (visibility != VisibilityKind.PUBLIC) {
            builder.append(this.getVisivilityIndicator(visibility));
        }

        String content = owningMembership.getOwnedRelatedElement().stream().map(rel -> this.doSwitch(rel)).filter(NOT_NULL).collect(joining(builder.getNewLine()));
        builder.appendSpaceIfNeeded().append(content);

        return builder.toString();
    }

    private void appendNamespaceImport(Appender builder, NamespaceImport namespaceImport) {
        Namespace importedNamespace = namespaceImport.getImportedNamespace();
        if (importedNamespace != null) {
            builder.appendSpaceIfNeeded().append(this.buildImportContextRelativeQualifiedName(importedNamespace, namespaceImport)).append("::");
        }
        builder.append("*");
    }

    private void appendMembershipImport(Appender builder, MembershipImport membershipImport) {

        Membership importedMembership = membershipImport.getImportedMembership();
        if (importedMembership != null) {
            String qnName = Stream.concat(Stream.ofNullable(importedMembership.getMemberElement()), importedMembership.getOwnedRelatedElement().stream()).filter(e -> e != null).findFirst()
                    .map(e -> this.buildImportContextRelativeQualifiedName(e, membershipImport)).orElse("");

            builder.appendSpaceIfNeeded().append(qnName);
        }
    }

    private String buildImportContextRelativeQualifiedName(Element element, Element from) {
        String qualifiedName = nullToEmpty(element.getQualifiedName());
        Element commonAncestor = EMFUtils.getLeastCommonContainer(Element.class, element, from);
        if (commonAncestor != null) {
            String prefix = commonAncestor.getQualifiedName() + "::";
            if (qualifiedName.startsWith(prefix)) {
                return qualifiedName.substring(prefix.length(), qualifiedName.length());
            }
        }
        return qualifiedName;
    }

    private String appendNameWithShortName(Appender builder, Element element) {
        String shortName = element.getShortName();
        if (!isNullOrEmpty(shortName)) {
            builder.appendSpaceIfNeeded().append("<").appendPrintableName(shortName).append(">");
        }
        String name = element.getDeclaredName();
        if (!isNullOrEmpty(name)) {
            builder.appendSpaceIfNeeded().appendPrintableName(name);
        }
        return builder.toString();
    }

    public String getVisivilityIndicator(VisibilityKind visibility) {
        if (visibility == null) {
            return "";
        }
        return switch (visibility) {
            case PRIVATE -> "private";
            case PROTECTED -> "protected";
            case PUBLIC -> "public";
            default -> "";
        };
    }
}
