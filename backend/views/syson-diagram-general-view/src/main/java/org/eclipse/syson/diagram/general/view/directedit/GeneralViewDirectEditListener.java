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

import org.antlr.v4.runtime.tree.ErrorNode;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditBaseListener;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser.ExpressionContext;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser.TypingExpressionContext;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser.ValueExpressionContext;
import org.eclipse.syson.diagram.general.view.services.GeneralViewUtilService;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
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

    private final GeneralViewUtilService generalViewUtilService;

    public GeneralViewDirectEditListener(Element element) {
        this.element = Objects.requireNonNull(element);
        this.generalViewUtilService = new GeneralViewUtilService();
    }

    @Override
    public void exitExpression(ExpressionContext ctx) {
        var identifier = ctx.Ident();
        this.element.setDeclaredName(identifier.getText());
    }

    @Override
    public void exitTypingExpression(TypingExpressionContext ctx) {
        if (this.element instanceof Usage) {
            var identifier = ctx.Ident();
            var typeAsString = identifier.getText();
            var definition = this.generalViewUtilService.findDefinitionByName(this.element, typeAsString);
            if (definition == null) {
                var containerPackage = this.generalViewUtilService.getContainerPackage(this.element);
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
                    featureTyping.get().setType(definition);
                }
            }
        }
    }

    @Override
    public void exitValueExpression(ValueExpressionContext ctx) {
        super.exitValueExpression(ctx);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}
