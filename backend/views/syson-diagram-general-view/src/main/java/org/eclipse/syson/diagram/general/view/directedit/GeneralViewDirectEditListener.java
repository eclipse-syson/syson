/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view.directedit;

import java.util.Objects;
import java.util.Optional;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditBaseListener;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser.ExpressionContext;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser.FeatureExpressionsContext;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser.RedefinitionExpressionContext;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser.SubsettingExpressionContext;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser.TypingExpressionContext;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser.ValueExpressionContext;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * The ANTLR Listener for the direct edit grammar for General View.
 *
 * @author arichard
 */
public class GeneralViewDirectEditListener extends DirectEditBaseListener {

    private final Element element;

    private final UtilService utilService;

    public GeneralViewDirectEditListener(Element element) {
        this.element = Objects.requireNonNull(element);
        this.utilService = new UtilService();
    }

    @Override
    public void exitExpression(ExpressionContext ctx) {
        var identifier = ctx.Ident();
        this.element.setDeclaredName(identifier.getText());
        this.handleMissingSubsettingExpression(ctx);
        this.handleMissingRedefinitionExpression(ctx);
        this.handleMissingTypingExpression(ctx);
        this.handleMissingValueExpression(ctx);
    }

    @Override
    public void exitTypingExpression(TypingExpressionContext ctx) {
        if (this.element instanceof Usage) {
            var identifier = ctx.Ident();
            var typeAsString = identifier.getText();
            var definition = this.utilService.findDefinitionByName(this.element, typeAsString);
            if (definition == null) {
                var containerPackage = this.utilService.getContainerPackage(this.element);
                var newMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                containerPackage.getOwnedRelationship().add(newMembership);
                EClassifier eClassifier = SysmlPackage.eINSTANCE.getEClassifier(this.element.eClass().getName().replace("Usage", "Definition"));
                if (eClassifier instanceof EClass eClass) {
                    definition = (Definition) SysmlFactory.eINSTANCE.create(eClass);
                    definition.setDeclaredName(typeAsString);
                    newMembership.getOwnedRelatedElement().add(definition);
                }
            }
            if (definition != null) {
                var featureTyping = this.element.getOwnedRelationship().stream()
                        .filter(FeatureTyping.class::isInstance)
                        .map(FeatureTyping.class::cast)
                        .findFirst();
                if (featureTyping.isPresent()) {
                    featureTyping.get().setType(definition);
                } else {
                    var newFeatureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
                    this.element.getOwnedRelationship().add(newFeatureTyping);
                    newFeatureTyping.setType(definition);
                }
            }
        }
    }

    @Override
    public void exitSubsettingExpression(SubsettingExpressionContext ctx) {
        if (this.element instanceof Usage subsettingUsage) {
            var identifier = ctx.Ident();
            var usageAsString = identifier.getText();
            var usage = this.utilService.findUsageByName(subsettingUsage, usageAsString);
            if (usage == null) {
                var containerPackage = this.utilService.getContainerPackage(subsettingUsage);
                var newMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                containerPackage.getOwnedRelationship().add(newMembership);
                EClassifier eClassifier = SysmlPackage.eINSTANCE.getEClassifier(subsettingUsage.eClass().getName());
                if (eClassifier instanceof EClass eClass) {
                    usage = (Usage) SysmlFactory.eINSTANCE.create(eClass);
                    usage.setDeclaredName(usageAsString);
                    newMembership.getOwnedRelatedElement().add(usage);
                }
            }
            if (usage != null) {
                var optSubsetting = subsettingUsage.getOwnedRelationship().stream()
                        .filter(Subsetting.class::isInstance)
                        .map(Subsetting.class::cast)
                        .findFirst();
                if (optSubsetting.isPresent()) {
                    Subsetting subsetting = optSubsetting.get();
                    subsetting.setSubsettedFeature(usage);
                    subsetting.setGeneral(usage);
                    subsetting.setSubsettingFeature(subsettingUsage);
                    subsetting.setSpecific(subsettingUsage);
                } else {
                    var newSubsetting = SysmlFactory.eINSTANCE.createSubsetting();
                    subsettingUsage.getOwnedRelationship().add(newSubsetting);
                    newSubsetting.setSubsettedFeature(usage);
                    newSubsetting.setGeneral(usage);
                    newSubsetting.setSubsettingFeature(subsettingUsage);
                    newSubsetting.setSpecific(subsettingUsage);
                }
            }
        }
    }

    @Override
    public void exitRedefinitionExpression(RedefinitionExpressionContext ctx) {
        if (this.element instanceof Usage redefining) {
            var identifier = ctx.Ident();
            var usageAsString = identifier.getText();
            var usage = this.utilService.findUsageByName(redefining, usageAsString);
            if (usage == null) {
                var containerPackage = this.utilService.getContainerPackage(redefining);
                var newMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                containerPackage.getOwnedRelationship().add(newMembership);
                EClassifier eClassifier = SysmlPackage.eINSTANCE.getEClassifier(redefining.eClass().getName());
                if (eClassifier instanceof EClass eClass) {
                    usage = (Usage) SysmlFactory.eINSTANCE.create(eClass);
                    usage.setDeclaredName(usageAsString);
                    newMembership.getOwnedRelatedElement().add(usage);
                }
            }
            if (usage != null) {
                var optRedefinition = redefining.getOwnedRelationship().stream()
                        .filter(Redefinition.class::isInstance)
                        .map(Redefinition.class::cast)
                        .findFirst();
                if (optRedefinition.isPresent()) {
                    Redefinition redefinition = optRedefinition.get();
                    redefinition.setRedefinedFeature(usage);
                    redefinition.setSubsettedFeature(usage);
                    redefinition.setGeneral(usage);
                    redefinition.setRedefiningFeature(redefining);
                    redefinition.setSubsettingFeature(redefining);
                    redefinition.setSpecific(redefining);
                } else {
                    var newRedefinition = SysmlFactory.eINSTANCE.createRedefinition();
                    redefining.getOwnedRelationship().add(newRedefinition);
                    newRedefinition.setRedefinedFeature(usage);
                    newRedefinition.setSubsettedFeature(usage);
                    newRedefinition.setGeneral(usage);
                    newRedefinition.setRedefiningFeature(redefining);
                    newRedefinition.setSubsettingFeature(redefining);
                    newRedefinition.setSpecific(redefining);
                }
            }
        }
    }

    @Override
    public void exitValueExpression(ValueExpressionContext ctx) {
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
                LiteralInteger literalInteger = this.getOrCreateLiteralInteger(featureValue);
                literalInteger.setValue(Integer.parseInt(integerTN.getText()));
            } else if (realTN != null) {
                LiteralRational literalRational = this.getOrCreateLiteralRational(featureValue);
                literalRational.setValue(Double.parseDouble(realTN.getText()));
            } else if (booleanTN != null) {
                LiteralBoolean literalBoolean = this.getOrCreateLiteralBoolean(featureValue);
                literalBoolean.setValue(Boolean.parseBoolean(booleanTN.getText()));
            } else if (doubleQuotedStringTN != null) {
                LiteralString literalString = this.getOrCreateLiteralString(featureValue);
                literalString.setValue(doubleQuotedStringTN.getText());
            }
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }

    private void handleMissingSubsettingExpression(ExpressionContext ctx) {
        FeatureExpressionsContext featureExpressions = ctx.featureExpressions();
        if (this.element instanceof Usage usage && (featureExpressions == null || featureExpressions.subsettingExpression() == null)) {
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
        FeatureExpressionsContext featureExpressions = ctx.featureExpressions();
        if (this.element instanceof Usage usage && (featureExpressions == null || featureExpressions.redefinitionExpression() == null)) {
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
        FeatureExpressionsContext featureExpressions = ctx.featureExpressions();
        if (this.element instanceof Usage usage && (featureExpressions == null || featureExpressions.typingExpression() == null)) {
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
        FeatureExpressionsContext featureExpressions = ctx.featureExpressions();
        if (this.element instanceof Usage usage && (featureExpressions == null || featureExpressions.valueExpression() == null)) {
            var featureValue = this.element.getOwnedRelationship().stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .findFirst();
            if (featureValue.isPresent()) {
                EcoreUtil.remove(featureValue.get());
            }
        }
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
}
