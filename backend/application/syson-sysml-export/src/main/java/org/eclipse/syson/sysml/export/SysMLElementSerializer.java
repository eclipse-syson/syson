/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.AnalysisCaseUsage;
import org.eclipse.syson.sysml.AssertConstraintUsage;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.CalculationDefinition;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.CollectExpression;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
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
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.LibraryPackage;
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
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.NullExpression;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.PortionKind;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.SelectExpression;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.VerificationCaseUsage;
import org.eclipse.syson.sysml.ViewpointDefinition;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.export.utils.Appender;
import org.eclipse.syson.sysml.export.utils.NameDeresolver;
import org.eclipse.syson.sysml.export.utils.Status;
import org.eclipse.syson.sysml.export.utils.SysMLKeywordSwitch;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Convert a SysML {@link Element} to its textual representation.
 *
 * @author Arthur Daussy
 */
public class SysMLElementSerializer extends SysmlSwitch<String> {

    private final String lineSeparator;

    private final String indentation;

    private final NameDeresolver nameDeresolver;

    private final SysMLKeywordSwitch keywordProvider = new SysMLKeywordSwitch();

    private final Consumer<Status> reportConsumer;

    /**
     * Simple constructor.
     *
     * @param lineSeparator
     *            the string used to separate line
     * @param indentation
     *            the string used to indent the file
     */
    public SysMLElementSerializer(String lineSeparator, String indentation, NameDeresolver nameDeresolver, Consumer<Status> reportConsumer) {
        super();
        this.lineSeparator = lineSeparator;
        this.indentation = indentation;
        this.nameDeresolver = nameDeresolver;
        if (reportConsumer == null) {
            this.reportConsumer = r -> {
            };
        } else {
            this.reportConsumer = reportConsumer;
        }
    }

    public SysMLElementSerializer(Consumer<Status> reportConsumer) {
        this(System.lineSeparator(), "\t", new NameDeresolver(), reportConsumer);
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
            String content = this.getContent(namespace.getOwnedMembership(), "");
            if (content != null && !content.isBlank()) {
                builder.append(content);
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
    public String caseLibraryPackage(LibraryPackage libraryPackage) {
        Appender builder = this.newAppender();
        if (libraryPackage.isIsStandard()) {
            builder.append("standard ");
        }
        builder.append("library package ");
        this.appendNameWithShortName(builder, libraryPackage);
        List<Relationship> children = libraryPackage.getOwnedRelationship().stream().filter(IS_MEMBERSHIP.and(IS_METADATA_USAGE.negate()).or(IS_IMPORT)).toList();
        this.appendChildrenContent(builder, libraryPackage, children);
        return builder.toString();
    }

    @Override
    public String caseItemDefinition(ItemDefinition itemDef) {
        return this.appendDefaultDefinition(this.newAppender(), itemDef).toString();
    }

    @Override
    public String caseItemUsage(ItemUsage itemUsage) {
        Appender builder = this.newAppender();
        this.appendOccurrenceUsagePrefix(builder, itemUsage);
        builder.appendWithSpaceIfNeeded("item");
        this.appendUsage(builder, itemUsage);
        return builder.toString();
    }

    @Override
    public String caseActionDefinition(ActionDefinition actionDef) {
        return this.appendDefaultDefinition(this.newAppender(), actionDef).toString();
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
    public String caseOccurrenceDefinition(OccurrenceDefinition occDef) {
        return this.appendDefaultDefinition(this.newAppender(), occDef).toString();
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
        return this.appendDefaultUsage(this.newAppender(), partUsage).toString();
    }

    @Override
    public String caseOccurrenceUsage(OccurrenceUsage occurrenceUsage) {
        Appender builder = new Appender(this.lineSeparator, this.indentation);
        this.appendUsagePrefix(builder, occurrenceUsage);
        this.appendOccurrenceUsagePrefix(builder, occurrenceUsage);
        if (PortionKind.SNAPSHOT.equals(occurrenceUsage.getPortionKind())) {
            builder.appendWithSpaceIfNeeded("snapshot");
        } else if (PortionKind.TIMESLICE.equals(occurrenceUsage.getPortionKind())) {
            builder.appendWithSpaceIfNeeded("timeslice");
        } else {
            builder.appendWithSpaceIfNeeded("occurrence");
        }
        this.appendOccurrenceUsageDeclaration(builder, occurrenceUsage);
        this.appendChildrenContent(builder, occurrenceUsage, occurrenceUsage.getOwnedMembership());
        return builder.toString();
    }

    private void appendOccurrenceUsageDeclaration(Appender builder, OccurrenceUsage occurrenceUsage) {
        this.appendUsageDeclaration(builder, occurrenceUsage);
        this.appendValuePart(builder, occurrenceUsage);
    }

    @Override
    public String caseReferenceUsage(ReferenceUsage reference) {
        Appender builder = new Appender(this.lineSeparator, this.indentation);
        if (!this.isImplicit(reference)) {
            this.appendBasicUsagePrefix(builder, reference);
            this.appendUsageDeclaration(builder, reference);
            this.appendUsageCompletion(builder, reference);
        }
        return builder.toString();
    }

    @Override
    public String caseActionUsage(ActionUsage actionUsage) {
        Appender builder = new Appender(this.lineSeparator, this.indentation);
        this.appendOccurrenceUsagePrefix(builder, actionUsage);
        builder.appendWithSpaceIfNeeded("action");
        this.appendActionUsageDeclaration(builder, actionUsage);
        this.appendChildrenContent(builder, actionUsage, actionUsage.getOwnedMembership());
        return builder.toString();
    }

    private void appendActionUsageDeclaration(Appender builder, ActionUsage actionUsage) {
        this.appendUsageDeclaration(builder, actionUsage);
        this.appendValuePart(builder, actionUsage);
    }

    @Override
    public String casePerformActionUsage(PerformActionUsage perfomActionUsage) {

        Appender builder = new Appender(this.lineSeparator, this.indentation);

        this.appendOccurrenceUsagePrefix(builder, perfomActionUsage);

        builder.appendWithSpaceIfNeeded("perform");

        Appender nameAppender = new Appender(this.lineSeparator, this.indentation);
        this.appendNameWithShortName(nameAppender, perfomActionUsage);

        if (nameAppender.isEmpty() && perfomActionUsage.getOwnedReferenceSubsetting() != null) {
            // Use simple form : perfom <nameOfReferenceSubSetting>
            this.appendOwnedReferenceSubsetting(builder, perfomActionUsage.getOwnedReferenceSubsetting());
        } else {
            // Use complete form
            builder.appendWithSpaceIfNeeded("action");
            this.appendUsageDeclaration(builder, perfomActionUsage);
        }

        this.appendValuePart(builder, perfomActionUsage);

        this.appendChildrenContent(builder, perfomActionUsage, perfomActionUsage.getOwnedMembership());

        return builder.toString();
    }

    @Override
    public String caseSuccessionAsUsage(SuccessionAsUsage successionAsUsage) {
        Appender builder = new Appender(this.lineSeparator, this.indentation);

        this.appendBasicUsagePrefix(builder, successionAsUsage);

        Appender declarationBuilder = new Appender(this.lineSeparator, this.indentation);
        this.appendUsageDeclaration(declarationBuilder, successionAsUsage);

        if (!declarationBuilder.isEmpty()) {
            builder.appendWithSpaceIfNeeded("succession ").append(declarationBuilder.toString());
        }

        List<EndFeatureMembership> endFeatureMemberships = successionAsUsage.getFeatureMembership().stream()
                .filter(EndFeatureMembership.class::isInstance)
                .map(EndFeatureMembership.class::cast)
                .toList();

        Set<Element> childrenToExclude = new HashSet<>();

        if (endFeatureMemberships.size() == 2) {

            EndFeatureMembership first = endFeatureMemberships.get(0);
            if (!this.isSuccessionUsageImplicitSource(first) || !this.isSourceFeaturePreviousDefinedFeature(successionAsUsage)) {
                builder.appendWithSpaceIfNeeded("first");
                this.appendConnectorEndMember(builder, first);
            }
            childrenToExclude.add(first);

            builder.appendWithSpaceIfNeeded("then");
            EndFeatureMembership second = endFeatureMemberships.get(1);
            childrenToExclude.add(second);
            this.appendConnectorEndMember(builder, second);

        } else {
            this.reportConsumer.accept(Status.warning("Unable to export a SuccessionAsUsage ({0}) invalid number of ends", successionAsUsage.getElementId()));
        }

        List<Relationship> children = successionAsUsage.getOwnedRelationship().stream()
                .filter(IS_DEFINITION_BODY_ITEM_MEMBER)
                .filter(e -> !childrenToExclude.contains(e))
                .toList();

        this.appendChildrenContent(builder, successionAsUsage, children);

        return builder.toString();
    }

    private boolean isSourceFeaturePreviousDefinedFeature(SuccessionAsUsage successionAsUsage) {
        Feature sourceFeature = successionAsUsage.getSourceFeature();

        Namespace namespace = successionAsUsage.getOwningNamespace();
        if (namespace != null) {

            EList<Membership> memberships = namespace.getMembership();
            int index = memberships.indexOf(successionAsUsage.getOwningFeatureMembership());
            if (index > 0) {
                Membership previousMembership = memberships.get(index - 1);
                return previousMembership instanceof FeatureMembership featureMembership && featureMembership.getFeature() == sourceFeature;
            }

        }
        return false;
    }

    /**
     * Checks if the source feature define force the given {@link EndFeatureMembership} is implicit or not
     *
     * @param endFeatureMembership
     *            the element to test
     * @return <code>true</code> if the given EndFeatureMembership represent an implicit feature
     */
    private boolean isSuccessionUsageImplicitSource(EndFeatureMembership endFeatureMembership) {
        EList<Element> relatedElements = endFeatureMembership.getOwnedRelatedElement();
        if (relatedElements.size() == 1) {
            Element relatedElement = relatedElements.get(0);
            if (relatedElement instanceof ReferenceUsage refUsage) {
                return refUsage.getOwnedSpecialization().stream().allMatch(s -> s.isIsImplied());
            }
        }
        return false;
    }

    private void appendConnectorEndMember(Appender builder, EndFeatureMembership endFeatureMembership) {
        endFeatureMembership.getOwnedRelatedElement().stream()
                .filter(ReferenceUsage.class::isInstance)
                .map(ReferenceUsage.class::cast)
                .findFirst()
                .ifPresent(ref -> this.appendConnectorEnd(builder, ref));
    }

    private void appendConnectorEnd(Appender builder, ReferenceUsage referenceUsage) {
        String declaredName = referenceUsage.getDeclaredName();

        if (declaredName != null && !declaredName.isBlank()) {
            builder.appendWithSpaceIfNeeded(declaredName).append(" ").append(LabelConstants.REFERENCES);
        }

        ReferenceSubsetting refSubsetting = referenceUsage.getOwnedReferenceSubsetting();

        if (refSubsetting != null) {
            this.appendOwnedReferenceSubsetting(builder, refSubsetting);
        }
        // We still need to implement this part here ( ownedRelationship += OwnedMultiplicity )?
    }

    private void appendOwnedReferenceSubsetting(Appender builder, ReferenceSubsetting refSubsetting) {
        Feature referencedFeature = refSubsetting.getReferencedFeature();

        if (referencedFeature != null) {

            if (!referencedFeature.getOwnedFeatureChaining().isEmpty()) {
                this.appendFeatureChain(builder, referencedFeature);
            } else {
                String deresolvedName = this.nameDeresolver.getDeresolvedName(referencedFeature, refSubsetting);
                if (deresolvedName == null || deresolvedName.isBlank()) {
                    this.reportConsumer.accept(Status.error("Unable to compute a valid identifier for ReferenceSubSetting {0}", refSubsetting.getElementId()));
                }
                builder.appendWithSpaceIfNeeded(deresolvedName);
            }
        }
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
        return this.serializeDeclarationWithModifiers(builder, usage, this.getUsageKeyword(usage));
    }

    private String serializeDeclarationWithModifiers(Appender builder, Usage usage, String keyword) {
        this.appendUsagePrefix(builder, usage);
        builder.appendSpaceIfNeeded().append(keyword);
        this.appendUsageDeclaration(builder, usage);
        this.appendUsageCompletion(builder, usage);
        return builder.toString();
    }

    private void appendDefinitionBody(Appender builder, Usage usage) {
        List<Relationship> children = usage.getOwnedRelationship().stream().filter(IS_DEFINITION_BODY_ITEM_MEMBER).toList();
        this.appendChildrenContent(builder, usage, children);
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

        if (membership instanceof FeatureMembership feature && feature.getOwnedMemberFeature() instanceof Expression exp) {
            this.reportConsumer.accept(Status.warning("BodyExpression are not handled yet ({0})", exp.getElementId()));
        } else {
            this.appendFeatureReferenceMember(builder, membership);
        }
        return builder.toString();
    }

    @Override
    public String caseOperatorExpression(OperatorExpression op) {
        Appender builder = this.newAppender();
        String operator = op.getOperator();
        if (operator == null) {
            return builder.toString();
        }
        switch (operator) {
            case "if":
                this.reportConsumer.accept(Status.warning("ConditionalExpression are not handled yet ({0})", op.getElementId()));
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
                this.appendClassificationExpression(builder, op);
                break;
            case "all":
                this.reportConsumer.accept(Status.warning("ExtentExpression are not handled yet ({0})", op.getElementId()));
                break;
            case LabelConstants.OPEN_BRACKET:
                this.appendBracketExpression(builder, op);
                break;
            case "#":
                this.reportConsumer.accept(Status.warning("IndexExpression are not handled yet ({0})", op.getElementId()));
                break;
            case ",":
                this.appendSequenceExpression(builder, op);
                break;
            case "??":
            case "and":
            case "or":
            case "implies":
                this.appendConditionalBinaryOperatorExpression(builder, op);
                break;
            default:
                break;
        }
        return builder.toString();
    }

    private void appendConditionalBinaryOperatorExpression(Appender builder, OperatorExpression op) {
        op.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .findFirst()
                .ifPresent(param -> this.appendArgumentMember(builder, param));

        builder.appendSpaceIfNeeded().append(op.getOperator());

        op.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .skip(1)
                .findFirst()
                .ifPresent(param -> this.appendArgumentExpressionMember(builder, param));
    }

    private void appendArgumentExpressionMember(Appender builder, ParameterMembership param) {
        Feature ownedMemberParameter = param.getOwnedMemberParameter();
        if (ownedMemberParameter != null) {
            ownedMemberParameter.getOwnedRelationship()
                    .stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .forEach(val -> this.appendOwnedExpressionReference(builder, val.getValue()));
        }
    }

    private void appendOwnedExpressionReference(Appender builder, Expression value) {
        if (value instanceof FeatureReferenceExpression featureReference) {
            featureReference.getOwnedRelationship().stream()
                    .filter(FeatureMembership.class::isInstance)
                    .map(FeatureMembership.class::cast)
                    .filter(feature -> feature.getOwnedMemberFeature() instanceof Expression)
                    .findFirst()
                    .ifPresent(feature -> this.appendOwnedExpression(builder, (Expression) feature.getOwnedMemberFeature()));
        }
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
            this.reportConsumer.accept(Status.warning("FunctionOperationExpression are not handled yet ({0})", expression.getElementId()));
        }
        return builder.toString();
    }

    @Override
    public String caseNullExpression(NullExpression expression) {
        return "null";
    }

    @Override
    public String caseCollectExpression(CollectExpression expression) {
        this.reportConsumer.accept(Status.warning("CollectExpression are not handled yet({0})", expression.getElementId()));
        return "";
    }

    @Override
    public String caseSelectExpression(SelectExpression expression) {
        this.reportConsumer.accept(Status.warning("SelectExpression are not handled yet({0})", expression.getElementId()));
        return "";
    }

    @Override
    public String caseMetadataAccessExpression(MetadataAccessExpression expression) {
        this.reportConsumer.accept(Status.warning("MetadataAccessExpression are not handled yet({0})", expression.getElementId()));
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
                .map(Feature::getOwnedRelationship)
                .stream()
                .flatMap(Collection::stream)
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

    @Override
    public String caseUseCaseDefinition(UseCaseDefinition useCase) {
        Appender builder = this.newAppender();

        this.appendDefinitionPrefix(builder, useCase);

        builder.appendSpaceIfNeeded().append("use");
        builder.appendSpaceIfNeeded().append("case");
        builder.appendSpaceIfNeeded().append("def");

        this.appendDefinitionDeclaration(builder, useCase);

        this.appendChildrenContent(builder, useCase, useCase.getOwnedMembership());

        return builder.toString();
    }

    @Override
    public String caseUseCaseUsage(UseCaseUsage useCaseUsage) {

        Appender builder = this.newAppender();

        this.appendOccurrenceUsagePrefix(builder, useCaseUsage);

        builder.appendWithSpaceIfNeeded("use case");

        this.appendUsageDeclaration(builder, useCaseUsage);

        this.appendValuePart(builder, useCaseUsage);

        this.appendChildrenContent(builder, useCaseUsage, useCaseUsage.getOwnedMembership());

        return builder.toString();
    }

    @Override
    public String caseIncludeUseCaseUsage(IncludeUseCaseUsage includeUseCaseUsage) {
        Appender builder = this.newAppender();

        this.appendOccurrenceUsagePrefix(builder, includeUseCaseUsage);

        builder.appendWithSpaceIfNeeded("include");

        if (includeUseCaseUsage.getOwnedReferenceSubsetting() != null) {
            builder.appendWithSpaceIfNeeded(this.getDeresolvableName(includeUseCaseUsage.getOwnedReferenceSubsetting().getReferencedFeature(), includeUseCaseUsage));
        } else {

            builder.appendWithSpaceIfNeeded("use case");
            this.appendUsageDeclaration(builder, includeUseCaseUsage);
        }

        this.appendValuePart(builder, includeUseCaseUsage);

        this.appendChildrenContent(builder, includeUseCaseUsage, includeUseCaseUsage.getOwnedMembership());

        return builder.toString();
    }

    @Override
    public String caseStakeholderMembership(StakeholderMembership stakeholderMembership) {
        Appender builder = this.newAppender();

        this.appendMembershipPrefix(stakeholderMembership, builder);

        this.appendStakeholderUsage(builder, stakeholderMembership);

        return builder.toString();
    }

    private void appendStakeholderUsage(Appender builder, StakeholderMembership stakeholderMembership) {

        for (Element relatedElement : stakeholderMembership.getOwnedRelatedElement()) {
            if (relatedElement instanceof Usage usage) {
                builder.appendWithSpaceIfNeeded("stakeholder ");
                this.serializeDeclarationWithModifiers(builder, usage, "");
            }
        }

    }

    @Override
    public String caseActorMembership(ActorMembership actor) {
        Appender builder = this.newAppender();

        PartUsage ownedActorParameter = actor.getOwnedActorParameter();

        if (ownedActorParameter != null) {
            VisibilityKind visibility = actor.getVisibility();
            if (visibility != VisibilityKind.PUBLIC) {
                builder.append(this.getVisibilityIndicator(visibility));
            }

            this.appendDefaultUsage(builder, ownedActorParameter);
        }

        return builder.toString();
    }

    @Override
    public String caseSubjectMembership(SubjectMembership subject) {
        Appender builder = this.newAppender();
        Usage ownedSubjectParameter = subject.getOwnedSubjectParameter();

        if (ownedSubjectParameter != null && !this.isImplicit(ownedSubjectParameter)) {
            VisibilityKind visibility = subject.getVisibility();
            if (visibility != VisibilityKind.PUBLIC) {
                builder.append(this.getVisibilityIndicator(visibility));
            }
            builder.appendWithSpaceIfNeeded("subject");

            this.appendUsage(builder, ownedSubjectParameter);
        }

        return builder.toString();
    }

    @Override
    public String caseObjectiveMembership(ObjectiveMembership objective) {
        Appender builder = this.newAppender();
        RequirementUsage ownedObjectiveRequirement = objective.getOwnedObjectiveRequirement();

        if (ownedObjectiveRequirement != null && !this.isImplicit(ownedObjectiveRequirement)) {
            VisibilityKind visibility = objective.getVisibility();
            if (visibility != VisibilityKind.PUBLIC) {
                builder.append(this.getVisibilityIndicator(visibility));
            }

            builder.append("objective");
            this.appendObjectiveRequirementUsage(builder, ownedObjectiveRequirement);
        }

        return builder.toString();
    }

    @Override
    public String caseConstraintUsage(ConstraintUsage usage) {
        Appender builder = this.newAppender();

        this.appendOccurrenceUsagePrefix(builder, usage);

        builder.appendSpaceIfNeeded().append(this.getUsageKeyword(usage));

        this.appendCalculationUsageDeclaration(builder, usage);

        this.appendChildrenContent(builder, usage, usage.getOwnedMembership());

        return builder.toString();
    }

    @Override
    public String caseRequirementUsage(RequirementUsage usage) {
        Appender builder = this.newAppender();

        this.appendOccurrenceUsagePrefix(builder, usage);

        builder.appendSpaceIfNeeded().append(this.getUsageKeyword(usage));

        this.appendCalculationUsageDeclaration(builder, usage);

        this.appendChildrenContent(builder, usage, usage.getOwnedMembership());

        return builder.toString();
    }

    @Override
    public String caseAssertConstraintUsage(AssertConstraintUsage usage) {
        Appender builder = this.newAppender();

        this.appendOccurrenceUsagePrefix(builder, usage);

        builder.appendSpaceIfNeeded().append(this.getUsageKeyword(usage));

        ReferenceSubsetting ownedReferenceSubsetting = usage.getOwnedReferenceSubsetting();

        if (ownedReferenceSubsetting != null) {
            // We still need to implement this part
            // ownedRelationship += OwnedReferenceSubsetting
            // FeatureSpecializationPart?
        } else {
            builder.appendSpaceIfNeeded().append("constraint");
            this.appendUsageDeclaration(builder, usage);
        }

        this.appendChildrenContent(builder, usage, usage.getOwnedMembership());

        return builder.toString();
    }

    @Override
    public String caseReturnParameterMembership(ReturnParameterMembership parameter) {
        // We still need to implement this part
        // ReturnFeatureMember : ReturnParameterMembership =
        // MemberPrefix 'return'
        // ownedRelatedElement += FeatureElement

        Appender builder = this.newAppender();
        Feature ownedMemberParameter = parameter.getOwnedMemberParameter();

        if (ownedMemberParameter != null && !this.isImplicit(ownedMemberParameter) && ownedMemberParameter instanceof Usage usage) {
            VisibilityKind visibility = parameter.getVisibility();
            if (visibility != VisibilityKind.PUBLIC) {
                builder.append(this.getVisibilityIndicator(visibility));
            }

            builder.append("return");
            this.appendDefaultUsage(builder, usage);
        }
        return builder.toString();
    }

    @Override
    public String caseRequirementDefinition(RequirementDefinition requirement) {
        Appender builder = this.newAppender();

        this.appendDefinitionPrefix(builder, requirement);

        builder.appendSpaceIfNeeded().append(this.getDefinitionKeyword(requirement));

        this.appendDefinitionDeclaration(builder, requirement);

        this.appendChildrenContent(builder, requirement, requirement.getOwnedMembership());

        return builder.toString();
    }

    @Override
    public String caseViewpointDefinition(ViewpointDefinition vp) {
        Appender builder = this.newAppender();

        this.appendDefinitionPrefix(builder, vp);

        builder.appendSpaceIfNeeded().append("viewpoint def");

        this.appendDefinitionDeclaration(builder, vp);

        this.appendChildrenContent(builder, vp, vp.getOwnedMembership());

        return builder.toString();
    }

    @Override
    public String caseMetadataDefinition(MetadataDefinition metadata) {
        Appender builder = this.newAppender();

        if (metadata.isIsAbstract()) {
            builder.append("abstract");
        }

        builder.appendSpaceIfNeeded().append("metadata def");

        this.appendDefinitionDeclaration(builder, metadata);

        this.appendChildrenContent(builder, metadata, metadata.getOwnedMembership());

        return builder.toString();
    }

    private boolean isImplicit(Element element) {
        boolean result = false;

        if (element != null) {
            if (element instanceof Relationship relationship && relationship.isIsImplied()) {
                result = true;
            } else if (element.isIsImpliedIncluded() && element.getOwnedRelationship().stream().allMatch(this::isImplicit)) {
                result = true;
            } else if (element instanceof Membership membership && this.isImplicit(membership.getMemberElement())) {
                result = true;
            }
        }

        return result;
    }

    private void appendObjectiveRequirementUsage(Appender builder, RequirementUsage usage) {
        this.appendCalculationUsageDeclaration(builder, usage);
        this.appendChildrenContent(builder, usage, usage.getOwnedMembership());
    }

    private void appendCalculationUsageDeclaration(Appender builder, Usage usage) {
        this.appendUsageDeclaration(builder, usage);
        this.appendValuePart(builder, usage);
    }

    private String getUsageKeyword(Usage usage) {
        return this.keywordProvider.doSwitch(usage);
    }

    private void appendFeatureSpecilizationPart(Appender builder, Feature feature, boolean includeImplied) {
        List<Redefinition> ownedRedefinition = feature.getOwnedRedefinition();
        this.appendRedefinition(builder, ownedRedefinition, includeImplied);

        this.appendReferenceSubsetting(builder, feature.getOwnedReferenceSubsetting(), includeImplied);

        List<Subsetting> ownedSubsetting = new ArrayList<>(feature.getOwnedSubsetting());
        ownedSubsetting.removeAll(ownedRedefinition);
        ownedSubsetting.remove(feature.getOwnedReferenceSubsetting());

        this.appendSubsettings(builder, ownedSubsetting, includeImplied);

        this.appendFeatureTyping(builder, feature.getOwnedTyping());
        this.appendMultiplicityPart(builder, feature);
    }

    private void appendSubsettings(Appender builder, List<Subsetting> subSettings, boolean includeImplied) {
        List<String> subSettedDifference = subSettings.stream()
                .filter(f -> includeImplied || !f.isIsImplied())
                .filter(sub -> this.isNotNullAndNotAProxy(sub.getSubsettedFeature()))
                .map(sub -> this.getDeresolvableName(sub.getSubsettedFeature(), sub))
                .filter(this::nameNotNullAndNotBlank)
                .toList();
        if (!subSettedDifference.isEmpty()) {
            String subSettingPart = String.join(", ", subSettedDifference);
            if (!subSettingPart.isBlank()) {
                builder.appendSpaceIfNeeded().append(LabelConstants.SUBSETTING);
                builder.appendSpaceIfNeeded().append(subSettingPart);
            }
        }
    }

    private void appendRedefinition(Appender builder, List<Redefinition> redefinitions, boolean includeImplied) {
        List<String> redefinedFeatures = redefinitions.stream()
                .filter(redef -> includeImplied || !redef.isIsImplied())
                .filter(red -> this.isNotNullAndNotAProxy(red.getRedefinedFeature()))
                .map(red -> this.getDeresolvableName(red.getRedefinedFeature(), red))
                .filter(this::nameNotNullAndNotBlank)
                .toList();
        if (!redefinedFeatures.isEmpty()) {
            String redefinitionPart = String.join(", ", redefinedFeatures);
            if (!redefinitionPart.isBlank()) {
                builder.appendSpaceIfNeeded().append(LabelConstants.REDEFINITION);
                builder.appendSpaceIfNeeded().append(redefinitionPart);
            }
        }
    }

    private void appendReferenceSubsetting(Appender builder, ReferenceSubsetting ownedReferenceSubsetting, boolean includeImplied) {
        if (ownedReferenceSubsetting != null && (includeImplied || !ownedReferenceSubsetting.isIsImplied())) {
            if (this.isNotNullAndNotAProxy(ownedReferenceSubsetting.getReferencedFeature())) {
                Appender localBuilder = this.newAppender();
                this.appendOwnedReferenceSubsetting(localBuilder, ownedReferenceSubsetting);

                if (!localBuilder.isEmpty()) {
                    builder.appendSpaceIfNeeded().append(LabelConstants.REFERENCES);
                    builder.appendWithSpaceIfNeeded(localBuilder.toString());
                }
            }
        }
    }

    private boolean isNotNullAndNotAProxy(EObject e) {
        final boolean result;
        if (e != null) {
            if (e.eIsProxy()) {
                this.reportConsumer.accept(Status.warning("Found one proxy {0}", ((InternalEObject) e).eProxyURI()));
                result = false;
            } else {
                result = true;
            }
        } else {
            result = false;
        }
        return result;
    }

    private boolean nameNotNullAndNotBlank(String name) {
        return name != null && !name.isBlank();
    }

    private void appendFeatureTyping(Appender builder, EList<FeatureTyping> ownedTyping) {
        List<String> types = ownedTyping.stream()
                .filter(ft -> this.isNotNullAndNotAProxy(ft.getType()))
                .map(this::getFeatureTypingTypeName)
                .filter(this::nameNotNullAndNotBlank)
                .toList();
        if (!types.isEmpty()) {
            String featureTypePart = String.join(", ", types);
            if (!featureTypePart.isBlank()) {
                builder.appendSpaceIfNeeded().append(LabelConstants.COLON);
                builder.appendSpaceIfNeeded().append(featureTypePart);
            }
        }
    }

    private String getFeatureTypingTypeName(FeatureTyping fTyping) {
        Type type = fTyping.getType();
        String qName = null;
        if (type != null) {
            if (fTyping instanceof ConjugatedPortTyping cTyping) {
                ConjugatedPortDefinition conjugatedPortDefinition = cTyping.getConjugatedPortDefinition();
                if (conjugatedPortDefinition != null) {
                    PortDefinition originalPort = conjugatedPortDefinition.getOriginalPortDefinition();
                    qName = LabelConstants.CONJUGATED + this.getDeresolvableName(originalPort, fTyping);
                }
            } else {
                qName = this.getDeresolvableName(type, fTyping);
            }
        }

        if (qName == null) {
            qName = "";
        }

        return qName;

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
        this.appendValuePart(builder, usage);
        this.appendDefinitionBody(builder, usage);
    }

    private void appendValuePart(Appender builder, Usage usage) {
        List<FeatureValue> ownedRelationship = usage.getOwnedRelationship().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .toList();

        for (FeatureValue feature : ownedRelationship) {
            builder.appendSpaceIfNeeded();
            if (feature.isIsDefault()) {
                builder.appendWithSpaceIfNeeded(LabelConstants.DEFAULT);
            }
            if (feature.isIsInitial()) {
                builder.appendWithSpaceIfNeeded(LabelConstants.COLON_EQUAL);
            } else {
                builder.appendWithSpaceIfNeeded(LabelConstants.EQUAL);
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
        } else if (expression instanceof OperatorExpression exp) {
            builder.appendSpaceIfNeeded().append(this.caseOperatorExpression(exp));
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

        for (FeatureValue featureValue : featureValueList) {
            Expression expression = featureValue.getValue();
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
            this.reportConsumer.accept(Status.warning("UnaryOperatorExpression are not handled yet {0}", expression.getElementId()));
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
        FeatureMembership featureMembership = expression.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .findFirst()
                .orElse(null);
        if (featureMembership instanceof ParameterMembership) {
            this.appendPositionalArgumentList(builder, expression);
        } else if (featureMembership != null) {
            this.reportConsumer.accept(Status.warning("NamedArgumentList are not handled yet {0}", expression.getElementId()));
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

    /**
     * The EBNF stipulates a ResultParameterMembership but this type does not exists so we assumed it was referring to
     * ReturnParameterMembership
     */
    private void appendClassificationExpression(Appender builder, OperatorExpression expression) {
        expression.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .filter(param -> param.getOwnedMemberParameter() != null)
                .findFirst()
                .ifPresent(membership -> this.appendArgumentMember(builder, membership));

        String operator = expression.getOperator();
        builder.appendSpaceIfNeeded().append(operator);

        if (operator.equals("as")) {
            expression.getOwnedRelationship().stream()
                    .filter(ReturnParameterMembership.class::isInstance)
                    .map(ReturnParameterMembership.class::cast)
                    .filter(feature -> feature.getOwnedMemberElement() instanceof Feature)
                    .findFirst()
                    .ifPresent(membership -> this.appendTypeMember(builder, membership));
        } else {
            expression.getOwnedRelationship().stream()
                    .filter(FeatureMembership.class::isInstance)
                    .map(FeatureMembership.class::cast)
                    .filter(Predicate.not(ParameterMembership.class::isInstance))
                    .filter(feature -> feature.getOwnedMemberElement() instanceof Feature)
                    .findFirst()
                    .ifPresent(membership -> this.appendTypeMember(builder, membership));
        }
    }

    private void appendTypeMember(Appender builder, FeatureMembership membership) {
        Feature ownedMemberFeature = membership.getOwnedMemberFeature();
        if (ownedMemberFeature != null) {
            ownedMemberFeature.getOwnedRelationship().stream()
                    .filter(FeatureTyping.class::isInstance)
                    .map(FeatureTyping.class::cast)
                    .forEach(feature -> builder.appendSpaceIfNeeded().append(this.getDeresolvableName(feature.getType(), feature)));
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

        List<Subclassification> subClassification = definition.getOwnedSubclassification().stream()
                .filter(s -> !s.isIsImplied())
                .toList();

        List<Classifier> subClassificationClassifier = subClassification.stream()
                .map(Subclassification::getSuperclassifier)
                .filter(this::isNotNullAndNotAProxy)
                .toList();
        if (!subClassificationClassifier.isEmpty()) {

            String superClasses = subClassificationClassifier.stream()
                    .map(sup -> this.getDeresolvableName(sup, definition))
                    .filter(this::nameNotNullAndNotBlank)
                    .collect(joining(", "));

            if (!superClasses.isBlank()) {
                builder.appendSpaceIfNeeded().append(":> ");
                builder.append(superClasses);
            }

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
        String deresolvedName = this.nameDeresolver.getDeresolvedName(toDeresolve, context);

        if (deresolvedName == null || deresolvedName.isBlank()) {
            this.reportConsumer.accept(Status.warning("Empty deresolved name for an {0} with id {1}", toDeresolve.eClass(), toDeresolve.getElementId()));
        }

        return deresolvedName;
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
        if (usage.isIsReference() && !this.isImplicitlyReferential(usage)) {
            isRef = "ref";
        } else {
            isRef = "";
        }

        builder.appendSpaceIfNeeded().append(isRef);

        this.appendExtensionKeyword(builder, usage);
    }

    private void appendOccurrenceUsagePrefix(Appender builder, OccurrenceUsage occUsage) {
        this.appendBasicUsagePrefix(builder, occUsage);

        if (occUsage.isIsIndividual()) {
            builder.appendSpaceIfNeeded().append("individual");
        }

        if (occUsage.isIsPortion() && occUsage.getPortionKind() != null) {
            builder.appendSpaceIfNeeded().append(occUsage.getPortionKind().toString());
        }

        this.appendExtensionKeyword(builder, occUsage);
    }

    private boolean isImplicitlyReferential(Usage usage) {
        return usage.getOwningMembership() instanceof ActorMembership
                || usage instanceof AttributeUsage
                || usage instanceof ReferenceUsage;
    }

    private void appendExtensionKeyword(Appender builder, Type type) {
        for (var rel : type.getOwnedRelationship()) {
            if (rel instanceof OwningMembership owningMember) {
                owningMember.getOwnedRelatedElement().stream()
                        .filter(MetadataUsage.class::isInstance)
                        .map(MetadataUsage.class::cast)
                        .map(MetadataUsage::getMetadataDefinition)
                        .filter(Objects::nonNull)
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
        FeatureDirectionKind direction = usage.getDirection();
        if (direction != null) {
            builder.appendWithSpaceIfNeeded(direction.toString());
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
        String content = this.getContent(childrenRelationships, this.lineSeparator);
        if (content != null && !content.isBlank()) {
            builder.append(" {");
            builder.appendIndentedContent(content);
            builder.newLine().append("}");
        } else {
            builder.append(";");
        }
    }

    private String getContent(List<? extends Relationship> children, String prefix) {
        return children.stream().map(this::doSwitch).filter(Objects::nonNull).collect(joining(this.lineSeparator, prefix, ""));
    }

    @Override
    public String caseImport(Import aImport) {
        Appender builder = this.newAppender();

        VisibilityKind visibility = aImport.getVisibility();
        if (visibility != VisibilityKind.PUBLIC) {
            builder.append(this.getVisibilityIndicator(visibility));
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
        if (isNullOrEmpty(comment.getLocale()) && selfNamespaceDescribingComment && comment.getDeclaredName() == null) {
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

    @Override
    public String caseDocumentation(Documentation doc) {
        Appender builder = this.newAppender();

        builder.appendSpaceIfNeeded().append("doc");
        this.appendNameWithShortName(builder, doc);
        boolean selfNamespaceDescribingComment = this.isSelfNamespaceDescribingComment(doc);
        if (isNullOrEmpty(doc.getLocale()) && selfNamespaceDescribingComment) {
            builder.appendWithSpaceIfNeeded(this.getCommentBody(doc.getBody()));
        }
        return builder.toString();
    }

    @Override
    public String caseTransitionUsage(TransitionUsage transitionUsage) {
        this.reportUnhandledType(transitionUsage);
        return "";
    }

    @Override
    public String caseStateUsage(StateUsage stateUsage) {
        this.reportUnhandledType(stateUsage);
        return "";
    }

    @Override
    public String caseSatisfyRequirementUsage(SatisfyRequirementUsage satisfyRequirementUsage) {
        this.reportUnhandledType(satisfyRequirementUsage);
        return "";
    }

    @Override
    public String caseCalculationDefinition(CalculationDefinition calculationDefinition) {
        this.reportUnhandledType(calculationDefinition);
        return "";
    }

    @Override
    public String caseAnalysisCaseUsage(AnalysisCaseUsage analysisCaseUsage) {
        this.reportUnhandledType(analysisCaseUsage);
        return "";
    }

    @Override
    public String caseVerificationCaseUsage(VerificationCaseUsage verificationCaseUsage) {
        this.reportUnhandledType(verificationCaseUsage);
        return "";
    }

    private void reportUnhandledType(Element e) {
        this.reportConsumer.accept(Status.warning("{0} are not yet handled : {1}", e.eClass().getName(), e.getElementId()));
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

        String content = owningMembership.getOwnedRelatedElement().stream().map(this::doSwitch).filter(Objects::nonNull).collect(joining(builder.getNewLine()));
        builder.appendSpaceIfNeeded().append(content);

        return builder.toString();
    }

    @Override
    public String caseInterfaceDefinition(InterfaceDefinition interfaceDef) {
        return this.appendDefaultDefinition(this.newAppender(), interfaceDef).toString();
    }

    @Override
    public String casePortUsage(PortUsage portUsage) {
        Appender builder = this.newAppender();
        // PortUsage inside a InterfaceDefintion which are InterfaceEnd have a special rule for serialization
        Element owner = portUsage.getOwner();
        if (owner instanceof InterfaceDefinition def && def.getInterfaceEnd().contains(portUsage)) {
            this.appendInterfaceEndPortUsage(builder, portUsage);
        } else {
            this.appendOccurrenceUsagePrefix(builder, portUsage);

            builder.appendWithSpaceIfNeeded("port");

            this.appendUsage(builder, portUsage);
        }
        return builder.toString();
    }

    private void appendInterfaceEndPortUsage(Appender builder, PortUsage portUsage) {
        FeatureDirectionKind direction = portUsage.getDirection();
        if (direction != null) {
            builder.appendWithSpaceIfNeeded(direction.toString());
        }

        if (portUsage.isIsAbstract()) {
            builder.appendWithSpaceIfNeeded("abstract");
        } else if (portUsage.isIsVariation()) {
            builder.appendWithSpaceIfNeeded("variation");
        }

        if (portUsage.isIsEnd()) {
            builder.appendWithSpaceIfNeeded("end");
        }
        this.appendUsage(builder, portUsage);
    }

    private void appendUsage(Appender builder, Usage portUsage) {
        this.appendUsageDeclaration(builder, portUsage);
        this.appendUsageCompletion(builder, portUsage);
    }

    private void appendMembershipPrefix(Membership membership, Appender builder) {
        VisibilityKind visibility = membership.getVisibility();
        if (visibility != VisibilityKind.PUBLIC) {
            builder.append(this.getVisibilityIndicator(visibility));
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
            String qnName = Stream.concat(Stream.ofNullable(importedMembership.getMemberElement()), importedMembership.getOwnedRelatedElement().stream()).filter(Objects::nonNull).findFirst()
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
                return qualifiedName.substring(prefix.length());
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

    public String getVisibilityIndicator(VisibilityKind visibility) {
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
