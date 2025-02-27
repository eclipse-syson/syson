/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.grammars.DirectEditBaseListener;
import org.eclipse.syson.services.grammars.DirectEditParser.AbstractPrefixExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.ConstraintExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.DerivedPrefixExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.DirectionPrefixExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.EffectExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.EndPrefixExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.FeatureChainExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.FeatureExpressionsContext;
import org.eclipse.syson.services.grammars.DirectEditParser.FeatureValueExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.ListItemExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.LiteralExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.MeasurementExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.MultiplicityExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.MultiplicityExpressionMemberContext;
import org.eclipse.syson.services.grammars.DirectEditParser.NodeExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.NonuniqueMultiplicityExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.OperandContext;
import org.eclipse.syson.services.grammars.DirectEditParser.OrderedMultiplicityExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.ReadonlyPrefixExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.RedefinitionExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.ReferenceExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.ShortNameContext;
import org.eclipse.syson.services.grammars.DirectEditParser.SubsettingExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.TriggerExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.TriggerExpressionNameContext;
import org.eclipse.syson.services.grammars.DirectEditParser.TypingExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.ValueExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.VariationPrefixExpressionContext;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ANTLR Listener for the direct edit grammar for SysON diagrams.
 *
 * @author arichard
 */
public class DiagramDirectEditListener extends DirectEditBaseListener {

    private final Logger logger = LoggerFactory.getLogger(DiagramDirectEditListener.class);

    private final Element element;

    private final IFeedbackMessageService feedbackMessageService;

    private final UtilService utilService;

    private final ImportService importService;

    private final ElementInitializerSwitch elementInitializer;

    private final Map<TransitionFeatureKind, Boolean> visitedTransitionFeatures;

    private List<String> options;

    public DiagramDirectEditListener(Element element, IFeedbackMessageService feedbackMessageService, String... options) {
        this.element = Objects.requireNonNull(element);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.options = List.of();
        if (options != null) {
            this.options = Arrays.asList(options);
        }
        this.utilService = new UtilService();
        this.importService = new ImportService();
        this.elementInitializer = new ElementInitializerSwitch();
        this.visitedTransitionFeatures = new HashMap<>();
        this.getVisitedTransitionFeatures().put(TransitionFeatureKind.TRIGGER, false);
        this.getVisitedTransitionFeatures().put(TransitionFeatureKind.GUARD, false);
        this.getVisitedTransitionFeatures().put(TransitionFeatureKind.EFFECT, false);
    }

    public Map<TransitionFeatureKind, Boolean> getVisitedTransitionFeatures() {
        return this.visitedTransitionFeatures;
    }

    @Override
    public void exitNodeExpression(NodeExpressionContext ctx) {
        if (!this.options.contains(LabelService.NAME_OFF)) {
            var identifier = ctx.name();
            if (identifier != null) {
                String newValue = null;
                if (!identifier.getText().isBlank()) {
                    newValue = this.getFullText(identifier);
                }
                new AttributeToDirectEditSwitch(newValue).doSwitch(this.element);
            }
        }
        this.handleMissingAbstractPrefixExpression(ctx);
        this.handleMissingVariationPrefixExpression(ctx);
        this.handleMissingReferenceExpression(ctx);
        this.handleMissingMultiplicityExpression(ctx);
        this.handleMissingSubclassificationExpression(ctx);
        this.handleMissingSubsettingExpression(ctx);
        this.handleMissingRedefinitionExpression(ctx);
        this.handleMissingTypingExpression(ctx);
        this.handleMissingValueExpression(ctx);
    }

    @Override
    public void exitShortName(ShortNameContext ctx) {
        if (ctx != null) {
            var innerShortName = ctx.name();
            String newShortName = this.element.getShortName();
            if (innerShortName == null || innerShortName.getText().isBlank()) {
                newShortName = "";
            } else {
                newShortName = this.getFullText(innerShortName);
            }
            this.element.setDeclaredShortName(newShortName);
        }
    }

    @Override
    public void exitListItemExpression(ListItemExpressionContext ctx) {
        if (!this.options.contains(LabelService.NAME_OFF)) {
            var identifier = ctx.name();
            if (identifier != null) {
                String newValue = null;
                if (!identifier.getText().isBlank()) {
                    newValue = this.getFullText(identifier);
                }
                new AttributeToDirectEditSwitch(newValue).doSwitch(this.element);
            }
        }
        this.handleMissingDirectionPrefixExpression(ctx);
        this.handleMissingAbstractPrefixExpression(ctx);
        this.handleMissingDerivedPrefixExpression(ctx);
        this.handleMissingEndPrefixExpression(ctx);
        this.handleMissingVariationPrefixExpression(ctx);
        this.handleMissingReadonlyPrefixExpression(ctx);
        this.handleMissingReferenceExpression(ctx);
        this.handleMissingMultiplicityExpression(ctx);
        this.handleMissingOrderedMultiplicityExpression(ctx);
        this.handleMissingNonuniqueMultiplicityExpression(ctx);
        this.handleMissingSubclassificationExpression(ctx);
        this.handleMissingSubsettingExpression(ctx);
        this.handleMissingRedefinitionExpression(ctx);
        this.handleMissingTypingExpression(ctx);
        this.handleMissingValueExpression(ctx);
    }

    @Override
    public void exitDirectionPrefixExpression(DirectionPrefixExpressionContext ctx) {
        if (this.element instanceof Usage usage) {
            if (ctx != null) {
                if (ctx.getText().equals(LabelConstants.IN + LabelConstants.SPACE)) {
                    usage.setDirection(FeatureDirectionKind.IN);
                } else if (ctx.getText().equals(LabelConstants.OUT + LabelConstants.SPACE)) {
                    usage.setDirection(FeatureDirectionKind.OUT);
                } else if (ctx.getText().equals(LabelConstants.INOUT + LabelConstants.SPACE)) {
                    usage.setDirection(FeatureDirectionKind.INOUT);
                }
            } else {
                usage.setDirection(null);
            }
        }
        super.exitDirectionPrefixExpression(ctx);
    }

    @Override
    public void exitAbstractPrefixExpression(AbstractPrefixExpressionContext ctx) {
        if (this.element instanceof Usage usage) {
            if (ctx != null) {
                usage.setIsAbstract(true);
            } else {
                usage.setIsAbstract(false);
                // A variation is always abstract
                usage.setIsVariation(false);
            }
        }
        super.exitAbstractPrefixExpression(ctx);
    }

    @Override
    public void exitDerivedPrefixExpression(DerivedPrefixExpressionContext ctx) {
        if (this.element instanceof Usage usage) {
            if (ctx != null) {
                usage.setIsDerived(true);
            } else {
                usage.setIsDerived(false);
            }
        }
        super.exitDerivedPrefixExpression(ctx);
    }

    @Override
    public void exitEndPrefixExpression(EndPrefixExpressionContext ctx) {
        if (this.element instanceof Usage usage) {
            if (ctx != null) {
                usage.setIsEnd(true);
            } else {
                usage.setIsEnd(false);
            }
        }
        super.exitEndPrefixExpression(ctx);
    }

    @Override
    public void exitReadonlyPrefixExpression(ReadonlyPrefixExpressionContext ctx) {
        if (this.element instanceof Usage usage) {
            if (ctx != null) {
                usage.setIsReadOnly(true);
            } else {
                usage.setIsReadOnly(false);
            }
        }
        super.exitReadonlyPrefixExpression(ctx);
    }

    @Override
    public void exitVariationPrefixExpression(VariationPrefixExpressionContext ctx) {
        if (this.element instanceof Usage usage) {
            if (ctx != null) {
                usage.setIsVariation(true);
                // A variation is always abstract
                usage.setIsAbstract(true);
            } else {
                usage.setIsVariation(false);
            }
        }
        super.exitVariationPrefixExpression(ctx);
    }

    @Override
    public void exitReferenceExpression(ReferenceExpressionContext ctx) {
        if (this.element instanceof Usage usage) {
            // isComposite is defined at the Feature level, but the derived isReference is only defined at the Usage level.
            usage.setIsComposite(false);
        }
        super.exitReferenceExpression(ctx);
    }

    @Override
    public void exitMultiplicityExpression(MultiplicityExpressionContext ctx) {
        if (this.options.contains(LabelService.MULTIPLICITY_OFF)) {
            return;
        }
        MultiplicityRange multiplicityRange = this.getOrCreateMultiplicityRange(this.element);
        List<MultiplicityExpressionMemberContext> bounds = ctx.multiplicityExpressionMember();
        for (MultiplicityExpressionMemberContext bound : bounds) {
            int boundIndex = bounds.indexOf(bound);
            OwningMembership membership = this.getOrCreateOwningMembership(multiplicityRange, boundIndex);
            if (membership != null) {
                MultiplicityExpressionMemberContext boundExpression = bounds.get(boundIndex);
                TerminalNode boundExpressionValue = boundExpression.Integer();
                if (boundExpressionValue == null) {
                    this.getOrCreateLiteralInfinity(membership);
                } else {
                    LiteralInteger literalInteger = this.getOrCreateLiteralInteger(membership);
                    literalInteger.setValue(Integer.parseInt(boundExpressionValue.getText()));
                }
            }
        }
        if (bounds.size() == 1) {
            var memberships = multiplicityRange.getOwnedRelationship().stream()
                    .filter(OwningMembership.class::isInstance)
                    .map(OwningMembership.class::cast)
                    .toList();
            if (memberships.size() == 2) {
                multiplicityRange.getOwnedRelationship().remove(1);
            }
        }
    }

    @Override
    public void exitOrderedMultiplicityExpression(OrderedMultiplicityExpressionContext ctx) {
        if (this.element instanceof Usage usage) {
            if (ctx != null) {
                usage.setIsOrdered(true);
            } else {
                usage.setIsOrdered(false);
            }
        }
        super.exitOrderedMultiplicityExpression(ctx);
    }

    @Override
    public void exitNonuniqueMultiplicityExpression(NonuniqueMultiplicityExpressionContext ctx) {
        if (this.element instanceof Usage usage) {
            if (ctx != null) {
                usage.setIsUnique(false);
            } else {
                usage.setIsUnique(true);
            }
        }
        super.exitNonuniqueMultiplicityExpression(ctx);
    }

    @Override
    public void exitTypingExpression(TypingExpressionContext ctx) {
        if (!this.options.contains(LabelService.TYPING_OFF) && this.element instanceof Usage usage) {
            var identifier = ctx.qualifiedName();
            if (identifier != null) {
                var typeAsString = this.getFullText(identifier);
                var type = this.utilService.findByNameAndType(this.element, typeAsString, Type.class);
                if (type == null && this.utilService.isQualifiedName(typeAsString)) {
                    this.feedbackMessageService.addFeedbackMessage(new Message("The qualified name used for the typing does not exist", MessageLevel.ERROR));
                    return;
                } else if (type == null && typeAsString.startsWith(LabelConstants.CONJUGATED)) {
                    this.feedbackMessageService.addFeedbackMessage(new Message("The conjugated port def used for the typing does not exist", MessageLevel.ERROR));
                    return;
                } else if (type == null) {
                    var containerPackage = this.utilService.getContainerPackage(this.element);
                    var newMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                    containerPackage.getOwnedRelationship().add(newMembership);
                    EClassifier eClassifier = SysmlPackage.eINSTANCE.getEClassifier(this.element.eClass().getName().replace("Usage", "Definition"));
                    if (eClassifier instanceof EClass eClass) {
                        type = (Definition) SysmlFactory.eINSTANCE.create(eClass);
                        this.elementInitializer.doSwitch(type);
                        type.setDeclaredName(typeAsString);
                        newMembership.getOwnedRelatedElement().add(type);
                    }
                } else {
                    this.importService.handleImport(this.element, type);
                }
                if (type instanceof ConjugatedPortDefinition conjugatedPortDef) {
                    this.handleConjugatedPortTyping(usage, conjugatedPortDef);
                } else if (type != null) {
                    this.utilService.setFeatureTyping(usage, type);
                }
            }
        }
    }

    @Override
    public void exitConstraintExpression(ConstraintExpressionContext ctx) {
        if (this.element instanceof ConstraintUsage constraintUsage) {
            constraintUsage.getOwnedRelationship().clear();
            if (ctx.operand(0) != null && !ctx.operand(0).getText().isBlank()) {
                ResultExpressionMembership resultExpressionMembership = SysmlFactory.eINSTANCE.createResultExpressionMembership();
                OperatorExpression operatorExpression = SysmlFactory.eINSTANCE.createOperatorExpression();
                resultExpressionMembership.getOwnedRelatedElement().add(operatorExpression);
                operatorExpression.setOperator(ctx.operatorExpression().getText());

                Element leftOperandElement = this.handleOperandExpression(ctx.operand(0), constraintUsage);
                if (leftOperandElement != null) {
                    ParameterMembership p1 = SysmlFactory.eINSTANCE.createParameterMembership();
                    operatorExpression.getOwnedRelationship().add(p1);
                    Feature x = SysmlFactory.eINSTANCE.createFeature();
                    p1.getOwnedRelatedElement().add(x);
                    x.setDeclaredName("x");
                    x.setDirection(FeatureDirectionKind.IN);
                    FeatureValue xValue = SysmlFactory.eINSTANCE.createFeatureValue();
                    x.getOwnedRelationship().add(xValue);
                    xValue.getOwnedRelatedElement().add(leftOperandElement);
                    // Add the expression in the constraint only if at least one operand has been correctly parsed.
                    constraintUsage.getOwnedRelationship().add(resultExpressionMembership);
                }

                if (ctx.operand().get(1) != null && !ctx.operand(1).getText().isBlank()) {
                    Element rightOperandElement = this.handleOperandExpression(ctx.operand(1), constraintUsage);
                    ParameterMembership p2 = SysmlFactory.eINSTANCE.createParameterMembership();
                    operatorExpression.getOwnedRelationship().add(p2);
                    Feature y = SysmlFactory.eINSTANCE.createFeature();
                    p2.getOwnedRelatedElement().add(y);
                    y.setDeclaredName("y");
                    y.setDirection(FeatureDirectionKind.IN);
                    FeatureValue yValue = SysmlFactory.eINSTANCE.createFeatureValue();
                    y.getOwnedRelationship().add(yValue);
                    yValue.getOwnedRelatedElement().add(rightOperandElement);
                }
            }
        }
    }

    @Override
    public void exitRedefinitionExpression(RedefinitionExpressionContext ctx) {
        if (this.options.contains(LabelService.REDEFINITION_OFF)) {
            return;
        }
        if (this.element instanceof Usage redefining) {
            this.handleRedefintion(ctx, redefining);
        }
    }

    @Override
    public void exitSubsettingExpression(SubsettingExpressionContext ctx) {
        if (this.options.contains(LabelService.SUBSETTING_OFF)) {
            return;
        }
        if (this.element instanceof Usage subsettingUsage) {
            this.handleSubsetting(ctx, subsettingUsage);
        } else if (this.element instanceof Definition subclassificationDef) {
            this.handleSubclassification(ctx, subclassificationDef);
        }
    }

    @Override
    public void exitFeatureValueExpression(FeatureValueExpressionContext ctx) {
        if (this.options.contains(LabelService.VALUE_OFF)) {
            return;
        }
        if (this.element instanceof Feature valueOwner) {
            FeatureValue featureValue = null;
            featureValue = this.element.getOwnedRelationship().stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .findFirst()
                    .orElseGet(() -> {
                        var result = SysmlFactory.eINSTANCE.createFeatureValue();
                        valueOwner.getOwnedRelationship().add(result);
                        return result;
                    });

            LiteralExpression literalExpression = this.createLiteralExpression(ctx.literalExpression());
            Optional<OperatorExpression> optMeasurementOperator = this.handleMeasurementExpression(ctx.measurementExpression(), valueOwner, literalExpression);
            if (optMeasurementOperator.isPresent()) {
                featureValue.getOwnedRelatedElement().clear();
                featureValue.getOwnedRelatedElement().add(optMeasurementOperator.get());
            } else {
                // Add the literal expression as a fallback if there is no measurement or if the measurement reference
                // cannot be found.
                featureValue.getOwnedRelatedElement().clear();
                featureValue.getOwnedRelatedElement().add(literalExpression);
            }
            // Check for isDefault token;
            int childCount = ctx.getChildCount();
            if (childCount > 0) {
                String token = ctx.getChild(0).getText();
                if (token != null && LabelConstants.DEFAULT.equals(token.trim())) {
                    featureValue.setIsDefault(true);
                    if (childCount > 1) {
                        token = ctx.getChild(1).getText();
                    }
                } else {
                    featureValue.setIsDefault(false);
                }

                if (LabelConstants.COLON_EQUAL.equals(token)) {
                    featureValue.setIsInitial(true);
                } else if (LabelConstants.EQUAL.equals(token)) {
                    featureValue.setIsInitial(false);
                }
            }
        }
    }

    @Override
    public void exitValueExpression(ValueExpressionContext ctx) {
        if (this.options.contains(LabelService.VALUE_OFF)) {
            return;
        }
        if (this.element instanceof Usage valueOwner) {
            FeatureValue featureValue = null;
            featureValue = this.element.getOwnedRelationship().stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .findFirst()
                    .orElseGet(() -> {
                        var result = SysmlFactory.eINSTANCE.createFeatureValue();
                        valueOwner.getOwnedRelationship().add(result);
                        return result;
                    });

            LiteralExpression literalExpression = this.createLiteralExpression(ctx.literalExpression());
            Optional<OperatorExpression> optMeasurementOperator = this.handleMeasurementExpression(ctx.measurementExpression(), valueOwner, literalExpression);
            if (optMeasurementOperator.isPresent()) {
                featureValue.getOwnedRelatedElement().clear();
                featureValue.getOwnedRelatedElement().add(optMeasurementOperator.get());
            } else {
                // Add the literal expression as a fallback if there is no measurement or if the measurement reference
                // cannot be found.
                featureValue.getOwnedRelatedElement().clear();
                featureValue.getOwnedRelatedElement().add(literalExpression);
            }
        }
    }

    @Override
    public void enterTriggerExpression(TriggerExpressionContext ctx) {
        if (this.options.contains(LabelService.TRANSITION_EXPRESSION_OFF)) {
            return;
        }
        if (this.element instanceof TransitionUsage transition) {
            this.utilService.removeTransitionFeaturesOfSpecificKind(transition, TransitionFeatureKind.TRIGGER);
            this.getVisitedTransitionFeatures().put(TransitionFeatureKind.TRIGGER, true);
        }
        super.enterTriggerExpression(ctx);
    }

    @Override
    public void exitTriggerExpression(TriggerExpressionContext ctx) {
        if (this.options.contains(LabelService.TRANSITION_EXPRESSION_OFF)) {
            return;
        }
        if (this.element instanceof TransitionUsage) {
            this.getVisitedTransitionFeatures().put(TransitionFeatureKind.TRIGGER, true);
        }
        super.exitTriggerExpression(ctx);
    }

    @Override
    public void exitTriggerExpressionName(TriggerExpressionNameContext ctx) {
        if (this.options.contains(LabelService.TRANSITION_EXPRESSION_OFF)) {
            return;
        }
        if (this.element instanceof TransitionUsage transition) {
            this.handleTriggerExpressionName(transition, ctx);
        }
        super.exitTriggerExpressionName(ctx);
    }

    @Override
    public void exitEffectExpression(EffectExpressionContext ctx) {
        if (this.options.contains(LabelService.TRANSITION_EXPRESSION_OFF)) {
            return;
        }
        if (this.element instanceof TransitionUsage transition) {
            this.utilService.removeTransitionFeaturesOfSpecificKind(transition, TransitionFeatureKind.EFFECT);
            this.getVisitedTransitionFeatures().put(TransitionFeatureKind.EFFECT, true);
            this.handleEffectExpression(transition, ctx);
        }
        super.exitEffectExpression(ctx);
    }

    /**
     * Returns the full text contained in the provided {@code ctx}.
     * <p>
     * The full text includes whitespaces that are skipped by the lexer. It is used to handle KerML unrestricted names
     * without quotes (e.g. "A Part" as the name of a Part Definition).
     * </p>
     *
     * @param ctx
     *            the parser rule's context
     * @return the full text contained in the provided {@code ctx}
     */
    private String getFullText(ParserRuleContext ctx) {
        if (ctx.start == null || ctx.stop == null || ctx.start.getStartIndex() < 0 || ctx.stop.getStopIndex() < 0) {
            // Fallback
            return ctx.getText();
        } else {
            return ctx.start.getInputStream().getText(Interval.of(ctx.start.getStartIndex(), ctx.stop.getStopIndex()))
                    .strip();
        }
    }

    private Element handleOperandExpression(OperandContext operandContext, Namespace context) {
        Element result = null;
        if (operandContext.featureChainExpression() != null && !operandContext.featureChainExpression().getText().isBlank()) {
            result = this.handleFeatureChainExpression(operandContext.featureChainExpression(), context);
        } else if (operandContext.literalExpression() != null && !operandContext.literalExpression().getText().isBlank()) {
            LiteralExpression literalExpression = this.createLiteralExpression(operandContext.literalExpression());
            Optional<OperatorExpression> optMeasurementOperator = this.handleMeasurementExpression(operandContext.measurementExpression(), context, literalExpression);
            if (optMeasurementOperator.isPresent()) {
                result = optMeasurementOperator.get();
            } else {
                result = literalExpression;
            }
        }

        return result;

    }

    private Element handleFeatureChainExpression(FeatureChainExpressionContext featureChainExpressionContext, Namespace context) {
        Element result = null;
        Element baseElement = Optional.ofNullable(context.resolve(featureChainExpressionContext.featureReference().getText()))
                .map(Membership::getMemberElement)
                .orElse(null);
        if (baseElement == null) {
            this.logMissingFeatureReference(this.getFullText(featureChainExpressionContext.featureReference()), context.getOwningNamespace());
        } else {
            FeatureReferenceExpression xFeatureReference = SysmlFactory.eINSTANCE.createFeatureReferenceExpression();
            Membership xFeatureReferenceMembership = SysmlFactory.eINSTANCE.createMembership();
            xFeatureReference.getOwnedRelationship().add(xFeatureReferenceMembership);
            xFeatureReferenceMembership.setMemberElement(baseElement);
            if (featureChainExpressionContext.featureChainExpression() == null) {
                // This is a simple feature access, there is no chained expressions.
                result = xFeatureReference;
            } else {
                // This is a chained feature access, we need to create the corresponding FeatureChainExpression.
                FeatureChainExpression featureChainExpression = SysmlFactory.eINSTANCE.createFeatureChainExpression();
                result = featureChainExpression;

                ParameterMembership parameterMembership = SysmlFactory.eINSTANCE.createParameterMembership();
                featureChainExpression.getOwnedRelationship().add(parameterMembership);
                Feature source = SysmlFactory.eINSTANCE.createFeature();
                parameterMembership.getOwnedRelatedElement().add(source);
                source.setDeclaredName("source");
                source.setDirection(FeatureDirectionKind.IN);
                FeatureValue sourceValue = SysmlFactory.eINSTANCE.createFeatureValue();
                source.getOwnedRelationship().add(sourceValue);
                sourceValue.getOwnedRelatedElement().add(xFeatureReference);

                if (featureChainExpressionContext.featureChainExpression().featureChainExpression() == null) {
                    // The chained feature access contains a single element, this is a special case that is handled with
                    // a Membership added in the FeatureChainExpression.
                    if (baseElement instanceof Namespace namespace) {
                        this.getNamespaceMember(namespace, featureChainExpressionContext.featureChainExpression().featureReference().getText())
                                .ifPresentOrElse(member -> {
                                    Membership featureChainMembership = SysmlFactory.eINSTANCE.createMembership();
                                    featureChainExpression.getOwnedRelationship().add(featureChainMembership);
                                    featureChainMembership.setMemberElement(member);
                                }, () -> {
                                    this.logMissingFeatureReference(this.getFullText(featureChainExpressionContext.featureChainExpression().featureReference()), namespace);
                                });
                    }
                } else {
                    // The chained feature access contains multiple elements, we create FeatureChainings to represent
                    // them.
                    List<FeatureChaining> featureChainings = this.getFeatureChainings(featureChainExpressionContext.featureChainExpression(), baseElement);
                    OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                    featureChainExpression.getOwnedRelationship().add(owningMembership);
                    Feature feature = SysmlFactory.eINSTANCE.createFeature();
                    owningMembership.getOwnedRelatedElement().add(feature);
                    feature.getOwnedRelationship().addAll(featureChainings);
                }
            }
        }
        return result;
    }

    private Optional<Element> getNamespaceMember(Namespace namespace, String memberName) {
        return namespace.getMember().stream()
                .filter(e -> Objects.equals(e.getName(), memberName))
                .findFirst();
    }

    private void logMissingFeatureReference(String referenceName, Namespace namespace) {
        String errorMessage = MessageFormat.format("Cannot find the element matching expression {0} in Namespace {1}",
                referenceName,
                namespace.getName());
        this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
        this.logger.warn(errorMessage);
    }

    private List<FeatureChaining> getFeatureChainings(FeatureChainExpressionContext featureChainExpressionContext, Element context) {
        return this.getFeatureChainings(featureChainExpressionContext, context, new ArrayList<>());
    }

    private List<FeatureChaining> getFeatureChainings(FeatureChainExpressionContext featureChainExpressionContext, Element context, List<FeatureChaining> accumulator) {
        if (context instanceof Namespace namespace) {
            Optional<Feature> chainedFeature = namespace.getMember().stream()
                    .filter(Feature.class::isInstance)
                    .map(Feature.class::cast)
                    .filter(e -> Objects.equals(e.getName(), featureChainExpressionContext.featureReference().getText()))
                    .findFirst();
            if (chainedFeature.isPresent()) {
                FeatureChaining featureChaining = SysmlFactory.eINSTANCE.createFeatureChaining();
                featureChaining.setChainingFeature(chainedFeature.get());
                accumulator.add(featureChaining);
                if (featureChainExpressionContext.featureChainExpression() != null) {
                    this.getFeatureChainings(featureChainExpressionContext.featureChainExpression(), chainedFeature.get(), accumulator);
                }
            } else {
                this.logMissingFeatureReference(this.getFullText(featureChainExpressionContext.featureReference()), namespace);
            }
        }
        return accumulator;
    }

    private Optional<OperatorExpression> handleMeasurementExpression(MeasurementExpressionContext measurementExpressionContext, Element context, Expression valueExpression) {
        Optional<OperatorExpression> result = Optional.empty();
        if (measurementExpressionContext != null && !measurementExpressionContext.getText().isBlank()) {
            String measurementText = this.getFullText(measurementExpressionContext);
            Optional<AttributeUsage> optMeasurementAttribute = this.getMeasurementAttribute(context, measurementText);
            if (optMeasurementAttribute.isPresent()) {
                this.importService.handleImport(this.element, optMeasurementAttribute.get());
                OperatorExpression operatorExpression = this.createOperatorExpression(LabelConstants.OPEN_BRACKET);
                result = Optional.of(operatorExpression);

                ParameterMembership p1 = SysmlFactory.eINSTANCE.createParameterMembership();
                operatorExpression.getOwnedRelationship().add(p1);
                Feature x = SysmlFactory.eINSTANCE.createFeature();
                p1.getOwnedRelatedElement().add(x);
                x.setDeclaredName("x");
                x.setDirection(FeatureDirectionKind.IN);
                FeatureValue xValue = SysmlFactory.eINSTANCE.createFeatureValue();
                x.getOwnedRelationship().add(xValue);
                xValue.getOwnedRelatedElement().add(valueExpression);

                ParameterMembership p2 = SysmlFactory.eINSTANCE.createParameterMembership();
                operatorExpression.getOwnedRelationship().add(p2);
                Feature y = SysmlFactory.eINSTANCE.createFeature();
                p2.getOwnedRelatedElement().add(y);
                y.setDeclaredName("y");
                y.setDirection(FeatureDirectionKind.IN);
                FeatureValue yValue = SysmlFactory.eINSTANCE.createFeatureValue();
                y.getOwnedRelationship().add(yValue);
                FeatureReferenceExpression yFeatureReference = SysmlFactory.eINSTANCE.createFeatureReferenceExpression();
                yValue.getOwnedRelatedElement().add(yFeatureReference);
                Membership yFeatureReferenceMembership = SysmlFactory.eINSTANCE.createMembership();
                yFeatureReference.getOwnedRelationship().add(yFeatureReferenceMembership);
                yFeatureReferenceMembership.setMemberElement(optMeasurementAttribute.get());
            } else {
                String errorMessage = "Cannot find measurement reference " + measurementText;
                this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
                this.logger.warn(errorMessage);
            }
        }
        return result;
    }

    private void handleConjugatedPortTyping(Usage usage, ConjugatedPortDefinition type) {
        var optConjugatedPortTyping = this.element.getOwnedRelationship().stream()
                .filter(ConjugatedPortTyping.class::isInstance)
                .map(ConjugatedPortTyping.class::cast)
                .findFirst();
        if (optConjugatedPortTyping.isPresent()) {
            ConjugatedPortTyping conjugatedPortTyping = optConjugatedPortTyping.get();
            conjugatedPortTyping.setType(type);
            conjugatedPortTyping.setTypedFeature(usage);
            conjugatedPortTyping.setConjugatedPortDefinition(type);
        } else {
            var optFeatureTyping = this.element.getOwnedRelationship().stream()
                    .filter(FeatureTyping.class::isInstance)
                    .map(FeatureTyping.class::cast)
                    .findFirst();
            if (optFeatureTyping.isPresent()) {
                EcoreUtil.remove(optFeatureTyping.get());
            }
            var newConjugatedPortTyping = SysmlFactory.eINSTANCE.createConjugatedPortTyping();
            this.element.getOwnedRelationship().add(newConjugatedPortTyping);
            newConjugatedPortTyping.setType(type);
            newConjugatedPortTyping.setTypedFeature(usage);
            newConjugatedPortTyping.setConjugatedPortDefinition(type);
            this.elementInitializer.doSwitch(newConjugatedPortTyping);
        }
    }

    private void handleSubclassification(SubsettingExpressionContext ctx, Definition subclassificationDef) {
        var identifier = ctx.qualifiedName();
        if (identifier != null) {
            var definitionAsString = this.getFullText(identifier);
            var definition = this.utilService.findByNameAndType(subclassificationDef, definitionAsString, Classifier.class);
            if (definition == null && this.utilService.isQualifiedName(definitionAsString)) {
                this.feedbackMessageService.addFeedbackMessage(new Message("The qualified name used for the subclassification does not exist", MessageLevel.ERROR));
                return;
            } else if (definition == null) {
                var containerPackage = this.utilService.getContainerPackage(subclassificationDef);
                var newMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                containerPackage.getOwnedRelationship().add(newMembership);
                EClassifier eClassifier = SysmlPackage.eINSTANCE.getEClassifier(subclassificationDef.eClass().getName());
                if (eClassifier instanceof EClass eClass) {
                    definition = (Definition) SysmlFactory.eINSTANCE.create(eClass);
                    definition.setDeclaredName(definitionAsString);
                    newMembership.getOwnedRelatedElement().add(definition);
                }
            } else {
                this.importService.handleImport(this.element, definition);
            }

            if (definition != null) {
                this.utilService.setSubclassification(subclassificationDef, definition);
            }
        }
    }

    private void handleSubsetting(SubsettingExpressionContext ctx, Usage subsettingUsage) {
        var identifier = ctx.qualifiedName();
        if (identifier != null) {
            var usageAsString = this.getFullText(identifier);
            var usage = this.utilService.findByNameAndType(subsettingUsage, usageAsString, Feature.class);
            if (usage == null && this.utilService.isQualifiedName(usageAsString)) {
                this.feedbackMessageService.addFeedbackMessage(new Message("The qualified name used for the subsetting does not exist", MessageLevel.ERROR));
                return;
            } else if (usage == null) {
                var containerPackage = this.utilService.getContainerPackage(subsettingUsage);
                var newMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                containerPackage.getOwnedRelationship().add(newMembership);
                EClassifier eClassifier = SysmlPackage.eINSTANCE.getEClassifier(subsettingUsage.eClass().getName());
                if (eClassifier instanceof EClass eClass) {
                    usage = (Usage) SysmlFactory.eINSTANCE.create(eClass);
                    usage.setDeclaredName(usageAsString);
                    newMembership.getOwnedRelatedElement().add(usage);
                }
            } else {
                this.importService.handleImport(this.element, usage);
            }

            if (usage != null) {
                this.utilService.setSubsetting(subsettingUsage, usage);
            }
        }
    }

    private void handleRedefintion(RedefinitionExpressionContext ctx, Usage redefining) {
        var identifier = ctx.qualifiedName();
        if (identifier != null) {
            var usageAsString = this.getFullText(identifier);
            var usage = this.utilService.findByNameAndType(redefining, usageAsString, Feature.class);
            if (usage == null && this.utilService.isQualifiedName(usageAsString)) {
                this.feedbackMessageService.addFeedbackMessage(new Message("The qualified name used for the redefinition does not exist", MessageLevel.ERROR));
                return;
            } else if (usage == null) {
                var containerPackage = this.utilService.getContainerPackage(redefining);
                var newMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                containerPackage.getOwnedRelationship().add(newMembership);
                EClassifier eClassifier = SysmlPackage.eINSTANCE.getEClassifier(redefining.eClass().getName());
                if (eClassifier instanceof EClass eClass) {
                    usage = (Usage) SysmlFactory.eINSTANCE.create(eClass);
                    usage.setDeclaredName(usageAsString);
                    newMembership.getOwnedRelatedElement().add(usage);
                }
            } else {
                this.importService.handleImport(this.element, usage);
            }

            if (usage != null) {
                this.utilService.setRedefinition(redefining, usage);
            }
        }
    }

    private void handleTriggerExpressionName(TransitionUsage transition, TriggerExpressionNameContext triggerExpressionName) {
        String name = triggerExpressionName.name().getText();
        TypingExpressionContext typingExpression = triggerExpressionName.typingExpression();
        String type;
        if (typingExpression != null) {
            // Here the user have provided both a name and a type
            type = typingExpression.qualifiedName().getText();
        } else {
            // Here only a type is provided, name is considered null
            type = name;
            name = null;
        }
        var typeValue = this.utilService.findByNameAndType(this.element, type, Type.class);
        if (typeValue != null) {
            this.addTransitionFeature(transition, TransitionFeatureKind.TRIGGER, name, typeValue);
        }
    }

    private void handleEffectExpression(TransitionUsage transition, EffectExpressionContext effectExpression) {
        effectExpression.qualifiedName().stream().forEach(identifier -> {
            var name = identifier.getText();
            var actionUsage = this.utilService.findByNameAndType(this.element, name, ActionUsage.class);
            if (actionUsage != null) {
                this.addTransitionFeature(transition, TransitionFeatureKind.EFFECT, null, actionUsage);
            }
        });
    }

    /**
     * Returns the {@link AttributeUsage} representing the provided {@code measurementName} measurement.
     * <p>
     * A measurement attribute should by typed by the {@code MeasurementReferences::TensorMeasurementReference}
     * definition (directly or indirectly).
     * </p>
     * <p>
     * The provided {@code context} could be any element of a SysMLv2 model. It is used as an entry point to navigate
     * the entire resource set to search for the attribute.
     * </p>
     *
     * @param context
     *            the element used to search the attribute
     * @param measurementName
     *            the name of the measurement to find
     * @return the measurement attribute if it exists, {@link Optional#empty()} otherwise
     */
    private Optional<AttributeUsage> getMeasurementAttribute(Element context, String measurementName) {
        AttributeDefinition tensorMeasurementReference = this.utilService.findByNameAndType(context, "MeasurementReferences::TensorMeasurementReference", AttributeDefinition.class);
        List<EObject> attributes = this.utilService.getAllReachable(context, SysmlPackage.eINSTANCE.getAttributeUsage());
        Optional<AttributeUsage> optUnitAttribute = attributes.stream()
                .map(AttributeUsage.class::cast)
                // The short name or the regular name can be use to set units
                .filter(attribute -> Objects.equals(attribute.getShortName(), measurementName) || Objects.equals(attribute.getName(), measurementName))
                // The attribute needs to be a measurement (i.e. inherit from TensorMeasurementReference)
                .filter(attribute -> attribute.allSupertypes().contains(tensorMeasurementReference))
                .findFirst();
        return optUnitAttribute;
    }

    /**
     * Creates an {@link OperatorExpression} with the provided {@code operator}.
     *
     * @param operator
     *            the operator of the expression
     * @return the created {@link OperatorExpression}
     */
    private OperatorExpression createOperatorExpression(String operator) {
        OperatorExpression operatorExpression = SysmlFactory.eINSTANCE.createOperatorExpression();
        operatorExpression.setOperator(operator);
        return operatorExpression;
    }

    /**
     * Create and set a Transition Feature to be assigned on the {@link TransitionUsage}.
     *
     * @param transition
     *            The transition onto which a Transition Feature is created.
     * @param kind
     *            The {@link TransitionFeatureKind} of the Transition Feature to create.
     * @param name
     *            The name of the payload. Should not be null if {@code kind} is TRIGGER.
     * @param typeValue
     *            The parameter value to assign to the Transition Feature.
     */
    private void addTransitionFeature(TransitionUsage transition, TransitionFeatureKind kind, String name, Type typeValue) {
        TransitionFeatureMembership tfMembership = SysmlFactory.eINSTANCE.createTransitionFeatureMembership();
        tfMembership.setKind(kind);
        if (kind.equals(TransitionFeatureKind.TRIGGER)) {
            // Add the root membership
            transition.getOwnedRelationship().add(tfMembership);

            AcceptActionUsage acceptActionUsage = SysmlFactory.eINSTANCE.createAcceptActionUsage();
            tfMembership.getOwnedRelatedElement().add(acceptActionUsage);
            tfMembership.setFeature(acceptActionUsage);

            // Set AcceptActionUsage payload as first Parameter. See paragraph 7.16.8
            var payloadParam = SysmlFactory.eINSTANCE.createParameterMembership();
            acceptActionUsage.getOwnedRelationship().add(payloadParam);

            // create the reference usage to be contained in the parameter membership
            var payloadRef = SysmlFactory.eINSTANCE.createReferenceUsage();
            payloadRef.setDirection(FeatureDirectionKind.INOUT);
            payloadParam.getOwnedRelatedElement().add(payloadRef);
            if (name != null) {
                payloadRef.setDeclaredName(name);
            } else {
                payloadRef.setDeclaredName("payload");
            }

            var ft = SysmlFactory.eINSTANCE.createFeatureTyping();
            payloadRef.getOwnedRelationship().add(ft);
            ft.setType(typeValue);

            // Set AcceptActionUsage receiver as second Parameter
            var receiverParam = SysmlFactory.eINSTANCE.createParameterMembership();
            acceptActionUsage.getOwnedRelationship().add(receiverParam);

            // create the reference usage to be contained in the parameter membership
            var receiverRef = SysmlFactory.eINSTANCE.createReferenceUsage();
            receiverRef.setDirection(FeatureDirectionKind.IN);
            receiverParam.getOwnedRelatedElement().add(receiverRef);

            // create the feature value relationship to be contained inside the reference usage
            var receiverFeatureVal = SysmlFactory.eINSTANCE.createFeatureValue();
            receiverRef.getOwnedRelationship().add(receiverFeatureVal);

            // create the feature reference expression to be contained inside the feature value relationship
            var receiverFeatureRefExpr = SysmlFactory.eINSTANCE.createFeatureReferenceExpression();
            receiverFeatureVal.getOwnedRelatedElement().add(receiverFeatureRefExpr);

            // find or create the membership relationship contained inside the feature reference expression
            var receiverMembership = SysmlFactory.eINSTANCE.createMembership();
            receiverFeatureRefExpr.getOwnedRelationship().add(receiverMembership);
            Type containerPart = this.utilService.getReceiverContainerDefinitionOrUsage(acceptActionUsage);
            receiverMembership.setMemberElement(containerPart);

            // find or create the return parameter membership relationship contained inside the feature reference
            // expression
            var receiverReturn = SysmlFactory.eINSTANCE.createReturnParameterMembership();
            receiverFeatureRefExpr.getOwnedRelationship().add(receiverReturn);

            // find or create the feature contained inside the parameter membership relationship
            var receiverFeature = SysmlFactory.eINSTANCE.createFeature();
            receiverFeature.setDirection(FeatureDirectionKind.OUT);
            receiverReturn.getOwnedRelatedElement().add(receiverFeature);
        } else {
            if (typeValue instanceof ActionUsage au) {
                tfMembership.setFeature(au);
                transition.getOwnedRelationship().add(tfMembership);
            }
        }
        this.importService.handleImport(this.element, typeValue);
    }

    private void handleMissingDirectionPrefixExpression(ListItemExpressionContext ctx) {
        DirectionPrefixExpressionContext directionPrefixExpression = ctx.prefixListItemExpression().directionPrefixExpression(0);
        if (this.element instanceof Usage usage && directionPrefixExpression == null) {
            usage.setDirection(null);
        }
    }

    private void handleMissingAbstractPrefixExpression(ParserRuleContext ctx) {
        AbstractPrefixExpressionContext abstractPrefixExpression = null;
        if (ctx instanceof ListItemExpressionContext listCtx) {
            abstractPrefixExpression = listCtx.prefixListItemExpression().abstractPrefixExpression(0);
        } else if (ctx instanceof NodeExpressionContext nodeCtx) {
            abstractPrefixExpression = nodeCtx.prefixNodeExpression().abstractPrefixExpression(0);
        }
        if (this.element instanceof Usage usage && abstractPrefixExpression == null && !usage.isIsVariation()) {
            // A variation is always abstract
            usage.setIsAbstract(false);
        }
    }

    private void handleMissingDerivedPrefixExpression(ListItemExpressionContext ctx) {
        DerivedPrefixExpressionContext derivedPrefixExpression = ctx.prefixListItemExpression().derivedPrefixExpression(0);
        if (this.element instanceof Usage usage && derivedPrefixExpression == null) {
            usage.setIsDerived(false);
        }
    }

    private void handleMissingEndPrefixExpression(ListItemExpressionContext ctx) {
        EndPrefixExpressionContext endPrefixExpression = ctx.prefixListItemExpression().endPrefixExpression(0);
        if (this.element instanceof Usage usage && endPrefixExpression == null) {
            usage.setIsEnd(false);
        }
    }

    private void handleMissingReadonlyPrefixExpression(ListItemExpressionContext ctx) {
        ReadonlyPrefixExpressionContext readonlyPrefixExpression = ctx.prefixListItemExpression().readonlyPrefixExpression(0);
        if (this.element instanceof Usage usage && readonlyPrefixExpression == null) {
            usage.setIsReadOnly(false);
        }
    }

    private void handleMissingVariationPrefixExpression(ParserRuleContext ctx) {
        VariationPrefixExpressionContext variationPrefixExpression = null;
        if (ctx instanceof ListItemExpressionContext listCtx) {
            variationPrefixExpression = listCtx.prefixListItemExpression().variationPrefixExpression(0);
        } else if (ctx instanceof NodeExpressionContext nodeCtx) {
            variationPrefixExpression = nodeCtx.prefixNodeExpression().variationPrefixExpression(0);
        }
        if (this.element instanceof Usage usage && variationPrefixExpression == null) {
            usage.setIsVariation(false);
        }
    }

    private void handleMissingReferenceExpression(ParserRuleContext ctx) {
        ReferenceExpressionContext referenceExpression = null;
        if (ctx instanceof ListItemExpressionContext listCtx) {
            referenceExpression = listCtx.referenceExpression();
        } else if (ctx instanceof NodeExpressionContext nodeCtx) {
            referenceExpression = nodeCtx.referenceExpression();
        }
        if (this.element instanceof Usage usage && referenceExpression == null) {
            usage.setIsComposite(true);
        }
    }

    private void handleMissingMultiplicityExpression(ParserRuleContext ctx) {
        if (this.options.contains(LabelService.MULTIPLICITY_OFF)) {
            return;
        }
        MultiplicityExpressionContext multiplicityExpression = null;
        if (ctx instanceof ListItemExpressionContext listCtx) {
            multiplicityExpression = listCtx.multiplicityExpression();
        } else if (ctx instanceof NodeExpressionContext nodeCtx) {
            multiplicityExpression = nodeCtx.multiplicityExpression();
        }
        if (this.element instanceof Usage && multiplicityExpression != null && this.isDeleteMultiplicityExpression(multiplicityExpression)) {
            var optMultiplicityRange = this.element.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .flatMap(m -> m.getOwnedRelatedElement().stream())
                    .filter(MultiplicityRange.class::isInstance)
                    .map(MultiplicityRange.class::cast)
                    .findFirst();
            if (optMultiplicityRange.isPresent()) {
                MultiplicityRange multiplicityRange = optMultiplicityRange.get();
                EcoreUtil.remove(multiplicityRange.eContainer());
            }
        }
    }

    private boolean isDeleteMultiplicityExpression(MultiplicityExpressionContext multiplicityExpression) {
        return multiplicityExpression.exception instanceof InputMismatchException
                && multiplicityExpression.getChildCount() == 2
                && LabelConstants.OPEN_BRACKET.equals(multiplicityExpression.getChild(0).getText())
                && LabelConstants.CLOSE_BRACKET.equals(multiplicityExpression.getChild(1).getText());
    }

    private void handleMissingOrderedMultiplicityExpression(ListItemExpressionContext ctx) {
        OrderedMultiplicityExpressionContext orderedMultiplicityExpression = ctx.multiplicityPropExpression().orderedMultiplicityExpression();
        if (this.element instanceof Usage usage && orderedMultiplicityExpression == null) {
            usage.setIsOrdered(false);
        }
    }

    private void handleMissingNonuniqueMultiplicityExpression(ListItemExpressionContext ctx) {
        NonuniqueMultiplicityExpressionContext nonuniqueMultiplicityExpression = ctx.multiplicityPropExpression().nonuniqueMultiplicityExpression();
        if (this.element instanceof Usage usage && nonuniqueMultiplicityExpression == null) {
            usage.setIsUnique(true);
        }
    }

    private void handleMissingSubclassificationExpression(ParserRuleContext ctx) {
        if (this.options.contains(LabelService.SUBSETTING_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = null;
        if (ctx instanceof ListItemExpressionContext listCtx) {
            featureExpressions = listCtx.featureExpressions();
        } else if (ctx instanceof NodeExpressionContext nodeCtx) {
            featureExpressions = nodeCtx.featureExpressions();
        }
        if (this.element instanceof Definition && featureExpressions != null
                && this.isDeleteFeatureExpression(featureExpressions, featureExpressions.subsettingExpression(), LabelConstants.SUBCLASSIFICATION)) {
            var subclassification = this.element.getOwnedRelationship().stream()
                    .filter(Subclassification.class::isInstance)
                    .map(Subclassification.class::cast)
                    .findFirst();
            if (subclassification.isPresent()) {
                EcoreUtil.remove(subclassification.get());
            }
        }
    }

    private void handleMissingSubsettingExpression(ParserRuleContext ctx) {
        if (this.options.contains(LabelService.SUBSETTING_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = null;
        if (ctx instanceof ListItemExpressionContext listCtx) {
            featureExpressions = listCtx.featureExpressions();
        } else if (ctx instanceof NodeExpressionContext nodeCtx) {
            featureExpressions = nodeCtx.featureExpressions();
        }
        if (this.element instanceof Usage && featureExpressions != null
                && this.isDeleteFeatureExpression(featureExpressions, featureExpressions.subsettingExpression(), LabelConstants.SUBSETTING)) {
            var subsetting = this.element.getOwnedRelationship().stream()
                    .filter(elt -> elt instanceof Subsetting && !(elt instanceof Redefinition))
                    .map(Subsetting.class::cast)
                    .findFirst();
            if (subsetting.isPresent()) {
                EcoreUtil.remove(subsetting.get());
            }
        }
    }

    private void handleMissingRedefinitionExpression(ParserRuleContext ctx) {
        if (this.options.contains(LabelService.REDEFINITION_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = null;
        if (ctx instanceof ListItemExpressionContext listCtx) {
            featureExpressions = listCtx.featureExpressions();
        } else if (ctx instanceof NodeExpressionContext nodeCtx) {
            featureExpressions = nodeCtx.featureExpressions();
        }
        if (this.element instanceof Usage && featureExpressions != null
                && this.isDeleteFeatureExpression(featureExpressions, featureExpressions.redefinitionExpression(), LabelConstants.REDEFINITION)) {
            var redefinition = this.element.getOwnedRelationship().stream()
                    .filter(Redefinition.class::isInstance)
                    .map(Redefinition.class::cast)
                    .findFirst();
            if (redefinition.isPresent()) {
                EcoreUtil.remove(redefinition.get());
            }
        }
    }

    private void handleMissingTypingExpression(ParserRuleContext ctx) {
        if (this.options.contains(LabelService.TYPING_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = null;
        if (ctx instanceof ListItemExpressionContext listCtx) {
            featureExpressions = listCtx.featureExpressions();
        } else if (ctx instanceof NodeExpressionContext nodeCtx) {
            featureExpressions = nodeCtx.featureExpressions();
        }
        if (this.element instanceof Usage && featureExpressions != null && this.isDeleteFeatureExpression(featureExpressions, featureExpressions.typingExpression(), LabelConstants.COLON)) {
            var featureTyping = this.element.getOwnedRelationship().stream()
                    .filter(FeatureTyping.class::isInstance)
                    .map(FeatureTyping.class::cast)
                    .findFirst();
            if (featureTyping.isPresent()) {
                EcoreUtil.remove(featureTyping.get());
            }
        }
    }

    private void handleMissingValueExpression(ParserRuleContext ctx) {
        if (this.options.contains(LabelService.VALUE_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = null;
        if (ctx instanceof ListItemExpressionContext listCtx) {
            featureExpressions = listCtx.featureExpressions();
        } else if (ctx instanceof NodeExpressionContext nodeCtx) {
            featureExpressions = nodeCtx.featureExpressions();
        }
        if (this.element instanceof Usage && featureExpressions != null && (this.isDeleteFeatureExpression(featureExpressions, featureExpressions.featureValueExpression(), LabelConstants.EQUAL)
                || this.isDeleteFeatureExpression(featureExpressions, featureExpressions.featureValueExpression(), LabelConstants.COLON_EQUAL))) {
            var featureValue = this.element.getOwnedRelationship().stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .findFirst();
            if (featureValue.isPresent()) {
                EcoreUtil.remove(featureValue.get());
            }
        }
    }

    private boolean isDeleteFeatureExpression(FeatureExpressionsContext featureExpressions, ParserRuleContext expression, String symbol) {
        return expression == null && featureExpressions.exception instanceof NoViableAltException && symbol.equals(featureExpressions.getChild(0).getText());
    }

    private LiteralExpression createLiteralExpression(LiteralExpressionContext literalExpressionContext) {
        LiteralExpression result = null;
        if (literalExpressionContext != null) {
            TerminalNode integerTN = literalExpressionContext.Integer();
            TerminalNode realTN = literalExpressionContext.Real();
            TerminalNode booleanTN = literalExpressionContext.Boolean();
            TerminalNode doubleQuotedStringTN = literalExpressionContext.DoubleQuotedString();
            if (integerTN != null) {
                var literalInteger = SysmlFactory.eINSTANCE.createLiteralInteger();
                literalInteger.setValue(Integer.parseInt(integerTN.getText()));
                result = literalInteger;
            } else if (realTN != null) {
                var literalRational = SysmlFactory.eINSTANCE.createLiteralRational();
                literalRational.setValue(Double.parseDouble(realTN.getText()));
                result = literalRational;
            } else if (booleanTN != null) {
                var literalBoolean = SysmlFactory.eINSTANCE.createLiteralBoolean();
                literalBoolean.setValue(Boolean.parseBoolean(booleanTN.getText()));
                result = literalBoolean;
            } else if (doubleQuotedStringTN != null) {
                var literalString = SysmlFactory.eINSTANCE.createLiteralString();
                var doubleQuotedString = doubleQuotedStringTN.getText();
                var stringValue = doubleQuotedString.substring(1, doubleQuotedString.length() - 1);
                literalString.setValue(stringValue);
                result = literalString;
            }
        }
        return result;
    }

    private MultiplicityRange getOrCreateMultiplicityRange(Element elt) {
        MultiplicityRange multiplicityRange = null;
        var optMultiplicityRange = elt.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .flatMap(m -> m.getOwnedRelatedElement().stream())
                .filter(MultiplicityRange.class::isInstance)
                .map(MultiplicityRange.class::cast)
                .findFirst();
        if (optMultiplicityRange.isEmpty()) {
            var membership = SysmlFactory.eINSTANCE.createOwningMembership();
            elt.getOwnedRelationship().add(membership);
            multiplicityRange = SysmlFactory.eINSTANCE.createMultiplicityRange();
            membership.getOwnedRelatedElement().add(multiplicityRange);
        } else {
            multiplicityRange = optMultiplicityRange.get();
        }
        return multiplicityRange;
    }

    private OwningMembership getOrCreateOwningMembership(MultiplicityRange multiplicityRange, int index) {
        OwningMembership owningMembership = null;
        var memberships = multiplicityRange.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .toList();
        if (memberships.size() == 0) {
            owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            multiplicityRange.getOwnedRelationship().add(owningMembership);
        } else if (memberships.size() >= 1 && index == 0) {
            owningMembership = memberships.get(0);
        } else if (memberships.size() == 1 && index == 1) {
            owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            multiplicityRange.getOwnedRelationship().add(owningMembership);
        } else if (memberships.size() == 2 && index == 1) {
            owningMembership = memberships.get(1);
        }
        return owningMembership;
    }

    private LiteralInteger getOrCreateLiteralInteger(OwningMembership membership) {
        LiteralInteger literalInteger = null;
        var optLiteralInteger = membership.getOwnedRelatedElement().stream()
                .filter(LiteralInteger.class::isInstance)
                .map(LiteralInteger.class::cast)
                .findFirst();
        if (optLiteralInteger.isPresent()) {
            literalInteger = optLiteralInteger.get();
        } else {
            membership.getOwnedRelatedElement().clear();
            literalInteger = SysmlFactory.eINSTANCE.createLiteralInteger();
            membership.getOwnedRelatedElement().add(literalInteger);
        }
        return literalInteger;
    }

    private LiteralInfinity getOrCreateLiteralInfinity(OwningMembership membership) {
        LiteralInfinity literalInfinity = null;
        var optLiteralInfinity = membership.getOwnedRelatedElement().stream()
                .filter(LiteralInfinity.class::isInstance)
                .map(LiteralInfinity.class::cast)
                .findFirst();
        if (optLiteralInfinity.isPresent()) {
            literalInfinity = optLiteralInfinity.get();
        } else {
            membership.getOwnedRelatedElement().clear();
            literalInfinity = SysmlFactory.eINSTANCE.createLiteralInfinity();
            membership.getOwnedRelatedElement().add(literalInfinity);
        }
        return literalInfinity;
    }
}
