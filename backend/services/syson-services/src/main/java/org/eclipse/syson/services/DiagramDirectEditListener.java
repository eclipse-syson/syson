/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.grammars.DirectEditBaseListener;
import org.eclipse.syson.services.grammars.DirectEditParser.EffectExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.ExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.FeatureExpressionsContext;
import org.eclipse.syson.services.grammars.DirectEditParser.MultiplicityExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.MultiplicityExpressionMemberContext;
import org.eclipse.syson.services.grammars.DirectEditParser.RedefinitionExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.SubsettingExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.TriggerExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.TriggerExpressionNameContext;
import org.eclipse.syson.services.grammars.DirectEditParser.TypingExpressionContext;
import org.eclipse.syson.services.grammars.DirectEditParser.ValueExpressionContext;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Redefinition;
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

/**
 * The ANTLR Listener for the direct edit grammar for SysON diagrams.
 *
 * @author arichard
 */
public class DiagramDirectEditListener extends DirectEditBaseListener {

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

    @Override
    public void exitExpression(ExpressionContext ctx) {
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
        this.handleMissingMultiplicityExpression(ctx);
        this.handleMissingSubclassificationExpression(ctx);
        this.handleMissingSubsettingExpression(ctx);
        this.handleMissingRedefinitionExpression(ctx);
        this.handleMissingTypingExpression(ctx);
        this.handleMissingValueExpression(ctx);
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
                    this.handleFeatureTyping(usage, type);
                }
            }
        }
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

    private void handleFeatureTyping(Usage usage, Type type) {
        var optConjugatedPortTyping = this.element.getOwnedRelationship().stream()
                .filter(ConjugatedPortTyping.class::isInstance)
                .map(ConjugatedPortTyping.class::cast)
                .findFirst();
        if (optConjugatedPortTyping.isPresent()) {
            EcoreUtil.remove(optConjugatedPortTyping.get());
        }
        var optFeatureTyping = this.element.getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .findFirst();
        if (optFeatureTyping.isPresent()) {
            FeatureTyping featureTyping = optFeatureTyping.get();
            if (!type.equals(featureTyping.getType())) {
                featureTyping.setType(type);
                featureTyping.setIsImplied(false);
            }
            featureTyping.setTypedFeature(usage);
        } else {
            var newFeatureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
            this.element.getOwnedRelationship().add(newFeatureTyping);
            newFeatureTyping.setType(type);
            newFeatureTyping.setTypedFeature(usage);
            this.elementInitializer.doSwitch(newFeatureTyping);
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
                var optSubclassification = subclassificationDef.getOwnedRelationship().stream()
                        .filter(Subclassification.class::isInstance)
                        .map(Subclassification.class::cast)
                        .findFirst();
                if (optSubclassification.isPresent()) {
                    Subclassification subclassification = optSubclassification.get();
                    if (!definition.equals(subclassification.getSuperclassifier())) {
                        subclassification.setSuperclassifier(definition);
                        subclassification.setIsImplied(false);
                    }
                    subclassification.setSubclassifier(subclassificationDef);
                } else {
                    var newSubclassification = SysmlFactory.eINSTANCE.createSubclassification();
                    subclassificationDef.getOwnedRelationship().add(newSubclassification);
                    newSubclassification.setSuperclassifier(definition);
                    newSubclassification.setSubclassifier(subclassificationDef);
                    this.elementInitializer.caseSubclassification(newSubclassification);
                }
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
                var optSubsetting = subsettingUsage.getOwnedRelationship().stream()
                        .filter(elt -> elt instanceof Subsetting && !(elt instanceof Redefinition))
                        .map(Subsetting.class::cast)
                        .findFirst();
                if (optSubsetting.isPresent()) {
                    Subsetting subsetting = optSubsetting.get();
                    if (!usage.equals(subsetting.getSubsettedFeature())) {
                        subsetting.setSubsettedFeature(usage);
                        subsetting.setIsImplied(false);
                    }
                    subsetting.setSubsettingFeature(subsettingUsage);
                } else {
                    var newSubsetting = SysmlFactory.eINSTANCE.createSubsetting();
                    subsettingUsage.getOwnedRelationship().add(newSubsetting);
                    newSubsetting.setSubsettedFeature(usage);
                    newSubsetting.setSubsettingFeature(subsettingUsage);
                    this.elementInitializer.caseSubsetting(newSubsetting);
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
                var optRedefinition = redefining.getOwnedRelationship().stream()
                        .filter(Redefinition.class::isInstance)
                        .map(Redefinition.class::cast)
                        .findFirst();
                if (optRedefinition.isPresent()) {
                    Redefinition redefinition = optRedefinition.get();
                    if (!usage.equals(redefinition.getRedefinedFeature())) {
                        redefinition.setRedefinedFeature(usage);
                        redefinition.setIsImplied(false);
                    }
                    redefinition.setRedefiningFeature(redefining);
                } else {
                    var newRedefinition = SysmlFactory.eINSTANCE.createRedefinition();
                    redefining.getOwnedRelationship().add(newRedefinition);
                    newRedefinition.setRedefinedFeature(usage);
                    newRedefinition.setRedefiningFeature(redefining);
                    this.elementInitializer.caseRedefinition(newRedefinition);
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
            TerminalNode integerTN = ctx.Integer();
            TerminalNode realTN = ctx.Real();
            TerminalNode booleanTN = ctx.Boolean();
            TerminalNode doubleQuotedStringTN = ctx.DoubleQuotedString();
            FeatureValue featureValue = null;
            var optFeatureValue = this.element.getOwnedRelationship().stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .findFirst();
            if (optFeatureValue.isPresent()) {
                featureValue = optFeatureValue.get();
            } else {
                featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
                valueOwner.getOwnedRelationship().add(featureValue);
            }
            if (integerTN != null) {
                var literalInteger = this.getOrCreateLiteralInteger(featureValue);
                literalInteger.setValue(Integer.parseInt(integerTN.getText()));
            } else if (realTN != null) {
                var literalRational = this.getOrCreateLiteralRational(featureValue);
                literalRational.setValue(Double.parseDouble(realTN.getText()));
            } else if (booleanTN != null) {
                var literalBoolean = this.getOrCreateLiteralBoolean(featureValue);
                literalBoolean.setValue(Boolean.parseBoolean(booleanTN.getText()));
            } else if (doubleQuotedStringTN != null) {
                var literalString = this.getOrCreateLiteralString(featureValue);
                var doubleQuotedString = doubleQuotedStringTN.getText();
                var stringValue = doubleQuotedString.substring(1, doubleQuotedString.length() - 1);
                literalString.setValue(stringValue);
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
        if (this.element instanceof TransitionUsage transition) {
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

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }

    private void handleMissingMultiplicityExpression(ExpressionContext ctx) {
        if (this.options.contains(LabelService.MULTIPLICITY_OFF)) {
            return;
        }
        MultiplicityExpressionContext multiplicityExpression = ctx.multiplicityExpression();
        if (this.element instanceof Usage usage && multiplicityExpression != null && this.isDeleteMultiplicityExpression(multiplicityExpression)) {
            var optMultiplicityRange = this.element.getOwnedRelationship().stream()
                    .filter(OwningMembership.class::isInstance)
                    .map(OwningMembership.class::cast)
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

    private void handleMissingSubclassificationExpression(ExpressionContext ctx) {
        if (this.options.contains(LabelService.SUBSETTING_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = ctx.featureExpressions();
        if (this.element instanceof Definition definition && featureExpressions != null
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

    private void handleMissingSubsettingExpression(ExpressionContext ctx) {
        if (this.options.contains(LabelService.SUBSETTING_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = ctx.featureExpressions();
        if (this.element instanceof Usage usage && featureExpressions != null
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

    private void handleMissingRedefinitionExpression(ExpressionContext ctx) {
        if (this.options.contains(LabelService.REDEFINITION_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = ctx.featureExpressions();
        if (this.element instanceof Usage usage && featureExpressions != null
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

    private void handleMissingTypingExpression(ExpressionContext ctx) {
        if (this.options.contains(LabelService.TYPING_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = ctx.featureExpressions();
        if (this.element instanceof Usage usage && featureExpressions != null && this.isDeleteFeatureExpression(featureExpressions, featureExpressions.typingExpression(), LabelConstants.COLON)) {
            var featureTyping = this.element.getOwnedRelationship().stream()
                    .filter(FeatureTyping.class::isInstance)
                    .map(FeatureTyping.class::cast)
                    .findFirst();
            if (featureTyping.isPresent()) {
                EcoreUtil.remove(featureTyping.get());
            }
        }
    }

    private void handleMissingValueExpression(ExpressionContext ctx) {
        if (this.options.contains(LabelService.VALUE_OFF)) {
            return;
        }
        FeatureExpressionsContext featureExpressions = ctx.featureExpressions();
        if (this.element instanceof Usage usage && featureExpressions != null && this.isDeleteFeatureExpression(featureExpressions, featureExpressions.valueExpression(), LabelConstants.EQUAL)) {
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

    private LiteralBoolean getOrCreateLiteralBoolean(FeatureValue featureValue) {
        Optional<LiteralBoolean> optLiteral = featureValue.getOwnedRelatedElement().stream()
                .filter(LiteralBoolean.class::isInstance)
                .map(LiteralBoolean.class::cast)
                .findFirst();
        if (optLiteral.isPresent()) {
            return optLiteral.get();
        }
        LiteralBoolean literal = SysmlFactory.eINSTANCE.createLiteralBoolean();
        featureValue.getOwnedRelatedElement().clear();
        featureValue.getOwnedRelatedElement().add(literal);
        return literal;
    }

    private LiteralInteger getOrCreateLiteralInteger(FeatureValue featureValue) {
        Optional<LiteralInteger> optLiteral = featureValue.getOwnedRelatedElement().stream()
                .filter(LiteralInteger.class::isInstance)
                .map(LiteralInteger.class::cast)
                .findFirst();
        if (optLiteral.isPresent()) {
            return optLiteral.get();
        }
        LiteralInteger literal = SysmlFactory.eINSTANCE.createLiteralInteger();
        featureValue.getOwnedRelatedElement().clear();
        featureValue.getOwnedRelatedElement().add(literal);
        return literal;
    }

    private LiteralRational getOrCreateLiteralRational(FeatureValue featureValue) {
        Optional<LiteralRational> optLiteral = featureValue.getOwnedRelatedElement().stream()
                .filter(LiteralRational.class::isInstance)
                .map(LiteralRational.class::cast)
                .findFirst();
        if (optLiteral.isPresent()) {
            return optLiteral.get();
        }
        LiteralRational literal = SysmlFactory.eINSTANCE.createLiteralRational();
        featureValue.getOwnedRelatedElement().clear();
        featureValue.getOwnedRelatedElement().add(literal);
        return literal;
    }

    private LiteralString getOrCreateLiteralString(FeatureValue featureValue) {
        Optional<LiteralString> optLiteral = featureValue.getOwnedRelatedElement().stream()
                .filter(LiteralString.class::isInstance)
                .map(LiteralString.class::cast)
                .findFirst();
        if (optLiteral.isPresent()) {
            return optLiteral.get();
        }
        LiteralString literal = SysmlFactory.eINSTANCE.createLiteralString();
        featureValue.getOwnedRelatedElement().clear();
        featureValue.getOwnedRelatedElement().add(literal);
        return literal;
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

    public Map<TransitionFeatureKind, Boolean> getVisitedTransitionFeatures() {
        return this.visitedTransitionFeatures;
    }
}
