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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.CollectExpression;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataAccessExpression;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.NullExpression;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SelectExpression;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.export.utils.Appender;
import org.eclipse.syson.sysml.export.utils.NameDeresolver;
import org.eclipse.syson.sysml.export.utils.SysMLKeywordSwitch;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.util.SysmlSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.util.function.Tuples;

/**
 * Convert a SysML {@link Element} to its textual representation.
 *
 * @author Arthur Daussy
 */
public class SysMLElementSerializer extends SysmlSwitch<String> {

    private static final Predicate<Object> NOT_NULL = s -> s != null;

    private static final Logger LOGGER = LoggerFactory.getLogger(SysMLElementSerializer.class);

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
        } else if (namespace.eClass() == SysmlPackage.eINSTANCE.getNamespace()) {
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

    @Override
    public String caseItemDefinition(ItemDefinition itemDef) {
        return this.appendDefaultDefinition(this.newAppender(), itemDef).toString();
    }

    @Override
    public String caseEnumerationDefinition(EnumerationDefinition enumDef) {
        return this.appendDefaultDefinition(this.newAppender(), enumDef).toString();
    }

    @Override
    public String caseAttributeDefinition(AttributeDefinition attrDef) {
        return this.appendDefaultDefinition(this.newAppender(), attrDef).toString();
    }

    @Override
    public String casePortDefinition(PortDefinition portDef) {
        return this.appendDefaultDefinition(this.newAppender(), portDef).toString();
    }

    @Override
    public String casePartDefinition(PartDefinition partDef) {
        return this.appendDefaultDefinition(this.newAppender(), partDef).toString();
    }

    @Override
    public String casePartUsage(PartUsage partUsage) {
        return this.appendDefaultUsage(newAppender(), partUsage).toString();
    }

    private Appender appendDefaultDefinition(Appender builder, Definition def) {

        if (def instanceof OccurrenceDefinition occDef) {
            this.appendDefinitionPrefix(builder, occDef);
        }

        builder.appendSpaceIfNeeded().append(this.getDefinitionKeyword(def));

        this.appendDefinition(builder, def);

        return builder;

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

    private String appendDefaultUsage(Appender builder, Usage usage) {

        this.appendUsagePrefix(builder, usage);

        builder.appendSpaceIfNeeded().append(this.getUsageKeyword(usage));

        this.appendUsageDeclaration(builder, usage);

        this.appendUsageCompletion(builder, usage);

        List<Relationship> children = usage.getOwnedRelationship().stream().filter(IS_DEFINITION_BODY_ITEM_MEMBER).toList();

        this.appendChildrenContent(builder, usage, children);

        return builder.toString();
    }

    @Override
    public String caseAttributeUsage(AttributeUsage attrUsage) {
        Appender builder = this.newAppender();

        this.appendDefaultUsage(builder, attrUsage);

        return builder.toString();
    }

    @Override
    public String caseEnumerationUsage(EnumerationUsage enumUsage) {
        Appender builder = this.newAppender();

        this.appendDefaultUsage(builder, enumUsage);

        return builder.toString();
    }

    @Override
    public String caseFeatureReferenceExpression(FeatureReferenceExpression expression) {
        Appender builder = this.newAppender();

        Membership membership = expression.getOwnedMembership().stream()
                .findFirst()
                .orElse(null);

        if (membership instanceof FeatureMembership feature) {
            LOGGER.warn("BodyExpression are not handled yet");
        } else {
            this.appendFeatureReferenceMember(builder, membership);
        }

        return builder.toString();
    }

    @Override
    public String caseOperatorExpression(OperatorExpression op) {
        Appender builder = this.newAppender();
        switch (op.getOperator()) {
            case "if":
                LOGGER.warn("ConditionalExpression are not handled yet");
                break;
            case "|":
            case "&":
            case "xor":
            case "..":
            case "==":
            case "!=":
            case "===":
            case "!==":
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
            case "^":
            case "**":
            case LabelConstants.CONJUGATED:
            case "not":
                this.appendBinaryOrUnaryOperatorExpression(builder, op);
                break;
            case "istype":
            case "hastype":
            case "@":
            case "as":
                LOGGER.warn("ClassificationExpression are not handled yet");
                break;
            case "all":
                LOGGER.warn("ExtentExpression are not handled yet");
                break;
            case LabelConstants.OPEN_BRACKET:
                this.appendBracketExpression(builder, op);
                break;
            case "#":
                LOGGER.warn("IndexExpression are not handled yet");
                break;
            case ",":
                this.appendSequenceExpression(builder, op);
                break;
            default:
                break;
        }
        return builder.toString();
    }

    @Override
    public String caseInvocationExpression(InvocationExpression expression) {
        Appender builder = this.newAppender();
        List<Relationship> relationships = expression.getOwnedRelationship();
        if (!relationships.isEmpty() && (relationships.get(0) instanceof FeatureTyping || relationships.get(0) instanceof Subsetting)) {
            relationships.stream()
                    .filter(Specialization.class::isInstance)
                    .map(Specialization.class::cast)
                    .findFirst()
                    .ifPresent(specialization -> builder.appendSpaceIfNeeded().append(this.getDeresolvableName(specialization.getGeneral(), specialization)));
            this.appendArgumentList(builder, expression);
        } else {
            LOGGER.warn("FunctionOperationExpression are not handled yet");
        }
        return builder.toString();
    }

    @Override
    public String caseNullExpression(NullExpression expression) {
        LOGGER.warn("NullExpression are not handled yet");
        return "";
    }

    @Override
    public String caseCollectExpression(CollectExpression expression) {
        LOGGER.warn("CollectExpression are not handled yet");
        return "";
    }

    @Override
    public String caseSelectExpression(SelectExpression expression) {
        LOGGER.warn("SelectExpression are not handled yet");
        return "";
    }

    @Override
    public String caseMetadataAccessExpression(MetadataAccessExpression expression) {
        LOGGER.warn("MetadataAccessExpression are not handled yet");
        return "";
    }

    @Override
    public String caseFeatureChainExpression(FeatureChainExpression feature) {
        Appender builder = this.newAppender();

        feature.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .findFirst()
                .map(ParameterMembership::getOwnedMemberParameter)
                .filter(NOT_NULL)
                .map(Feature::getOwnedRelationship)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst()
                .ifPresent(ft -> this.appendNonFeatureChainExpression(builder, ft.getValue()));

        builder.append(".");

        feature.getOwnedRelationship().stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .skip(1)
                .findFirst()
                .ifPresent(exp -> this.appendFeatureChainMember(builder, exp));

        return builder.toString();
    }

    private String getUsageKeyword(Usage usage) {
        return this.keywordProvider.doSwitch(usage);
    }

    private void appendFeatureSpecilizationPart(Appender builder, Feature feature, boolean includeImplied) {
        List<Redefinition> ownedRedefinition = feature.getOwnedRedefinition();
        this.appendRedefinition(builder, ownedRedefinition, feature, includeImplied);

        this.appendReferenceSubsetting(builder, feature.getOwnedReferenceSubsetting(), feature, includeImplied);

        List<Subsetting> ownedSubsetting = new ArrayList<>(feature.getOwnedSubsetting());
        ownedSubsetting.removeAll(ownedRedefinition);
        ownedSubsetting.remove(feature.getOwnedReferenceSubsetting());

        this.appendSubsettings(builder, ownedSubsetting.stream().filter(s -> includeImplied || !s.isIsImplied()).toList(), feature);

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

    private void appendRedefinition(Appender builder, List<Redefinition> redefinitions, Element element, boolean includeImplied) {
        if (!redefinitions.isEmpty()) {
            builder.appendSpaceIfNeeded().append(LabelConstants.REDEFINITION);
            builder.appendSpaceIfNeeded().append(redefinitions.stream()
                    .filter(redef -> includeImplied || !redef.isIsImplied())
                    .map(Redefinition::getRedefinedFeature)
                    .filter(Objects::nonNull)
                    .map(superFeature -> this.getDeresolvableName(superFeature, element))
                    .collect(Collectors.joining(", ")));
        }
    }

    private void appendReferenceSubsetting(Appender builder, ReferenceSubsetting ownedReferenceSubsetting, Element element, boolean includeImplied) {
        if (ownedReferenceSubsetting != null && (includeImplied || !ownedReferenceSubsetting.isIsImplied())) {
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

    private void appendUsageCompletion(Appender builder, Usage usage) {
        List<FeatureValue> ownedRelationship = usage.getOwnedRelationship().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .collect(Collectors.toList());

        for (FeatureValue feature : ownedRelationship) {
            builder.appendSpaceIfNeeded();
            if (feature.isIsInitial()) {
                builder.append(":=");
            } else if (feature.isIsDefault()) {
                builder.append("default");
                if (feature.isIsInitial()) {
                    builder.append(":");
                }
                builder.append("=");
            } else {
                builder.append("=");
            }
            this.appendOwnedExpression(builder, feature.getValue());
        }

    }

    private void appendOwnedExpression(Appender builder, Expression expression) {
        if (expression instanceof OperatorExpression op && op.getOperator() != null) {
            builder.appendSpaceIfNeeded().append(this.caseOperatorExpression(op));
        } else {
            this.appendPrimaryExpression(builder, expression);
        }
    }

    private void appendSequenceExpression(Appender builder, Expression expression) {
        builder.appendSpaceIfNeeded().append(LabelConstants.OPEN_PARENTHESIS);
        this.appendSequenceExpressionList(builder, expression);
        builder.append(LabelConstants.CLOSE_PARENTHESIS);
    }

    private void appendSequenceOperatorExpression(Appender builder, OperatorExpression expression) {
        List<ParameterMembership> features = expression.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .toList();

        if (!features.isEmpty()) {
            this.appendArgumentMember(builder, features.get(0));

            builder.append(LabelConstants.COMMA);

            this.appendSequenceExpressionListMember(builder, features.subList(1, features.size()));
        }
    }

    private void appendPrimaryExpression(Appender builder, Expression expression) {
        if (expression instanceof FeatureChainExpression feature) {
            builder.appendSpaceIfNeeded().append(this.caseFeatureChainExpression(feature));
        } else {
            this.appendNonFeatureChainExpression(builder, expression);
        }
    }

    private void appendFeatureChainMember(Appender builder, Membership membership) {
        if (membership instanceof OwningMembership owningMembership) {
            if (owningMembership.getOwnedMemberElement() instanceof Feature feature) {
                this.appendFeatureChain(builder, feature);
            }
        } else {
            if (membership.getMemberElement() instanceof Feature feature) {
                builder.append(this.getDeresolvableName(feature, membership.getMemberElement()));
            }
        }
    }

    private void appendFeatureChain(Appender builder, Feature feature) {
        String chainings = feature.getOwnedRelationship().stream()
                .filter(FeatureChaining.class::isInstance)
                .map(FeatureChaining.class::cast)
                .map(featureChaining -> this.getDeresolvableName(featureChaining.getChainingFeature(), featureChaining))
                .collect(Collectors.joining("."));

        if (!chainings.isEmpty()) {
            builder.appendSpaceIfNeeded().append(chainings);
        }
    }

    private void appendNonFeatureChainExpression(Appender builder, Expression expression) {
        if (expression instanceof SelectExpression exp) {
            builder.appendSpaceIfNeeded().append(this.caseSelectExpression(exp));
        } else if (expression instanceof CollectExpression exp) {
            builder.appendSpaceIfNeeded().append(this.caseCollectExpression(exp));
        } else if (expression instanceof NullExpression exp) {
            builder.appendSpaceIfNeeded().append(this.caseNullExpression(exp));
        } else if (expression instanceof LiteralExpression exp) {
            builder.appendSpaceIfNeeded().append(this.caseLiteralExpression(exp));
        } else if (expression instanceof FeatureReferenceExpression exp) {
            builder.appendSpaceIfNeeded().append(this.caseFeatureReferenceExpression(exp));
        } else if (expression instanceof MetadataAccessExpression exp) {
            builder.appendSpaceIfNeeded().append(this.caseMetadataAccessExpression(exp));
        } else if (expression instanceof InvocationExpression exp) {
            builder.appendSpaceIfNeeded().append(this.caseInvocationExpression(exp));
        } else {
            this.appendSequenceExpression(builder, expression);
        }
    }

    private void appendSequenceExpressionList(Appender builder, Expression expression) {
        if (expression instanceof OperatorExpression op && LabelConstants.COMMA.equals(op.getOperator())) {
            this.appendSequenceOperatorExpression(builder, op);
        } else if (expression != null) {
            this.appendOwnedExpression(builder, expression);
        }
    }

    private void appendFeatureReferenceMember(Appender builder, Membership membership) {
        if (membership.getMemberElement() instanceof Feature feature) {
            builder.appendSpaceIfNeeded().append(this.getDeresolvableName(feature, membership));
        }
    }

    private void appendBracketExpression(Appender builder, OperatorExpression expression) {
        List<ParameterMembership> features = expression.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .toList();

        if (!features.isEmpty()) {
            this.appendPrimaryExpressionMember(builder, features.get(0));

            builder.appendSpaceIfNeeded().append(expression.getOperator());

            this.appendSequenceExpressionListMember(builder, features.subList(1, features.size()));

            builder.append(LabelConstants.CLOSE_BRACKET);
        }
    }

    private void appendSequenceExpressionListMember(Appender builder, List<ParameterMembership> parameters) {
        List<FeatureValue> featureValueList = parameters.stream()
                .map(ParameterMembership::getOwnedMemberParameter)
                .filter(Objects::nonNull)
                .flatMap(parameter -> parameter.getOwnedRelationship().stream())
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .toList();

        for (int i = 0; i < featureValueList.size(); i++) {
            Expression expression = featureValueList.get(i).getValue();
            this.appendSequenceExpressionList(builder, expression);
        }
    }

    private void appendPrimaryExpressionMember(Appender builder, ParameterMembership parameterMembership) {
        Feature ownedMemberParameter = parameterMembership.getOwnedMemberParameter();
        if (ownedMemberParameter != null) {
            ownedMemberParameter.getOwnedRelationship()
                    .stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .forEach(val -> this.appendPrimaryExpression(builder, val.getValue()));
        }
    }

    private void appendBinaryOperatorExpression(Appender builder, OperatorExpression expression) {
        Iterator<ParameterMembership> iterator = expression.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast).toList().iterator();

        if (iterator.hasNext()) {
            this.appendArgumentMember(builder, iterator.next());
        }
        builder.appendSpaceIfNeeded().append(expression.getOperator());
        while (iterator.hasNext()) {
            this.appendArgumentMember(builder, iterator.next());
        }
    }

    private void appendBinaryOrUnaryOperatorExpression(Appender builder, OperatorExpression expression) {
        List<ParameterMembership> ownedRelationships = expression.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .toList();

        String operator = expression.getOperator();
        List<String> unaryOperators = List.of("+", "-", LabelConstants.CONJUGATED, "not");

        if (ownedRelationships.size() < 2 && (unaryOperators.contains(operator))) {
            LOGGER.warn("UnaryOperatorExpression are not handled yet");
        } else {
            this.appendBinaryOperatorExpression(builder, expression);
        }
    }

    private void appendArgumentMember(Appender builder, ParameterMembership parameterMembership) {
        Feature ownedMemberParameter = parameterMembership.getOwnedMemberParameter();
        if (ownedMemberParameter != null) {
            ownedMemberParameter.getOwnedRelationship()
                    .stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .forEach(val -> this.appendOwnedExpression(builder, val.getValue()));
        }
    }

    private void appendArgumentList(Appender builder, InvocationExpression expression) {
        builder.append(LabelConstants.OPEN_PARENTHESIS);
        Relationship relationship = expression.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .findFirst()
                .orElse(null);
        if (relationship instanceof ParameterMembership) {
            this.appendPositionalArgumentList(builder, expression);
        } else if (relationship instanceof FeatureMembership) {
            LOGGER.warn("NamedArgumentList are not handled yet");
        }
        builder.append(LabelConstants.CLOSE_PARENTHESIS);
    }

    private void appendPositionalArgumentList(Appender builder, InvocationExpression expression) {
        List<ParameterMembership> parameterMemberships = expression.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .filter(param -> {
                    if (param.getOwnedMemberParameter() != null) {
                        return param.getOwnedMemberParameter().getOwnedRelationship().stream()
                                .filter(FeatureValue.class::isInstance)
                                .map(FeatureValue.class::cast)
                                .findAny()
                                .isPresent();
                    } else {
                        return false;
                    }
                })
                .toList();

        for (int i = 0; i < parameterMemberships.size(); i++) {
            this.appendArgumentMember(builder, parameterMemberships.get(i));
            if (i < parameterMemberships.size() - 1) {
                builder.append(",");
            }
        }
    }

    @Override
    public String caseConjugatedPortDefinition(ConjugatedPortDefinition object) {
        // Conjugated port definition are implicit
        return "";
    }

    private String getDefinitionKeyword(Definition def) {
        return this.keywordProvider.doSwitch(def) + " def";
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

        this.appendFeatureSpecilizationPart(builder, usage, false);
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

        this.appendBasicUsagePrefix(builder, usage);

        final String isRef;
        if (usage.isIsReference() && !this.isImplicitlyReferencial(usage)) {
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

    private void appendBasicUsagePrefix(Appender builder, Usage usage) {

        if (usage.getDirection() != FeatureDirectionKind.IN) {
            builder.appendWithSpaceIfNeeded(usage.getDirection().toString());
        }

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

        this.appendMembershipPrefix(owningMembership, builder);

        String content = owningMembership.getOwnedRelatedElement().stream().map(rel -> this.doSwitch(rel)).filter(NOT_NULL).collect(joining(builder.getNewLine()));
        builder.appendSpaceIfNeeded().append(content);

        return builder.toString();
    }

    private void appendMembershipPrefix(Membership membership, Appender builder) {
        VisibilityKind visibility = membership.getVisibility();
        if (visibility != VisibilityKind.PUBLIC) {
            builder.append(this.getVisivilityIndicator(visibility));
        }
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
