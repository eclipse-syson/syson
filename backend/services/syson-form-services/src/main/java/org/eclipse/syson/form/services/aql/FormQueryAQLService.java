/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.form.services.aql;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.form.services.FormQueryElementService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;

/**
 * Entry point for all form-related services doing queries in forms and called by AQL expressions in form descriptions.
 *
 * @author arichard
 */
public class FormQueryAQLService {

    private final FormQueryElementService formQueryElementService;

    private final MetamodelQueryElementService metamodelQueryElementService;

    /**
     * Creates a query AQL service.
     *
     * @param formQueryElementService
     *            the shared form details element service
     */
    public FormQueryAQLService(FormQueryElementService formQueryElementService) {
        this.formQueryElementService = Objects.requireNonNull(formQueryElementService);
        this.metamodelQueryElementService = new MetamodelQueryElementService();
    }

    /**
     * {@link FormQueryElementService#getAcceptActionUsage(Element)}.
     */
    public AcceptActionUsage getAcceptActionUsage(Element element) {
        return this.formQueryElementService.getAcceptActionUsage(element);
    }

    /**
     * {@link FormQueryElementService#getAdvancedFeatures(Element)}.
     */
    public List<EStructuralFeature> getAdvancedFeatures(Element element) {
        return this.formQueryElementService.getAdvancedFeatures(element);
    }

    /**
     * {@link MetamodelQueryElementService#getCommentBody(Element)}.
     */
    public String getCommentBody(Element element) {
        return this.metamodelQueryElementService.getCommentBody(element);
    }

    /**
     * {@link FormQueryElementService#getCoreFeatures(Element)}.
     */
    public List<EStructuralFeature> getCoreFeatures(Element element) {
        return this.formQueryElementService.getCoreFeatures(element);
    }

    /**
     * {@link FormQueryElementService#getDetailsViewHelpText(Element, EStructuralFeature)}.
     */
    public String getDetailsViewHelpText(Element element, EStructuralFeature feature) {
        return this.formQueryElementService.getDetailsViewHelpText(element, feature);
    }

    /**
     * {@link FormQueryElementService#getDetailsViewLabel(Element, EStructuralFeature)}.
     */
    public String getDetailsViewLabel(Element element, EStructuralFeature feature) {
        return this.formQueryElementService.getDetailsViewLabel(element, feature);
    }

    /**
     * {@link MetamodelQueryElementService#getDocumentation(Element)}.
     */
    public String getDocumentation(Element element) {
        return this.metamodelQueryElementService.getDocumentation(element);
    }

    /**
     * {@link FormQueryElementService#getEnumCandidates(Element, EAttribute)}.
     */
    public List<EEnumLiteral> getEnumCandidates(Element element, EAttribute attribute) {
        return this.formQueryElementService.getEnumCandidates(element, attribute);
    }

    /**
     * {@link FormQueryElementService#getEnumCandidates(Element, String)}.
     */
    public List<EEnumLiteral> getEnumCandidates(Element element, String attributeName) {
        return this.formQueryElementService.getEnumCandidates(element, attributeName);
    }

    /**
     * {@link FormQueryElementService#getEnumValue(Element, EAttribute)}.
     */
    public EEnumLiteral getEnumValue(Element element, EAttribute attribute) {
        return this.formQueryElementService.getEnumValue(element, attribute);
    }

    /**
     * {@link FormQueryElementService#getEnumValue(Element, String)}.
     */
    public EEnumLiteral getEnumValue(Element element, String attributeName) {
        return this.formQueryElementService.getEnumValue(element, attributeName);
    }

    /**
     * {@link FormQueryElementService#getExpression(Element)}.
     */
    public Element getExpression(Element element) {
        return this.formQueryElementService.getExpression(element);
    }

    /**
     * {@link FormQueryElementService#getExpressionTextualRepresentation(Expression)}.
     */
    public String getExpressionTextualRepresentation(Expression expression) {
        return this.formQueryElementService.getExpressionTextualRepresentation(expression);
    }

    /**
     * {@link FormQueryElementService#getFeatureValue(Element)}.
     */
    public FeatureValue getFeatureValue(Element element) {
        return this.formQueryElementService.getFeatureValue(element);
    }

    /**
     * {@link FormQueryElementService#getGuardExpression(Element)}.
     */
    public Expression getGuardExpression(Element element) {
        return this.formQueryElementService.getGuardExpression(element);
    }

    /**
     * {@link FormQueryElementService#getPotentialExpressionOwner(Element)}.
     */
    public Element getPotentialExpressionOwner(Element element) {
        return this.formQueryElementService.getPotentialExpressionOwner(element);
    }

    /**
     * {@link FormQueryElementService#getResultExpression(Element)}.
     */
    public Element getResultExpression(Element element) {
        return this.formQueryElementService.getResultExpression(element);
    }

    /**
     * {@link FormQueryElementService#getResultExpressionTextualRepresentation(ResultExpressionMembership)}.
     */
    public String getResultExpressionTextualRepresentation(ResultExpressionMembership expression) {
        return this.formQueryElementService.getResultExpressionTextualRepresentation(expression);
    }

    /**
     * {@link FormQueryElementService#getStateUsage(Element)}.
     */
    public StateUsage getStateUsage(Element element) {
        return this.formQueryElementService.getStateUsage(element);
    }

    /**
     * {@link FormQueryElementService#getTransitionUsage(Element)}.
     */
    public TransitionUsage getTransitionUsage(Element element) {
        return this.formQueryElementService.getTransitionUsage(element);
    }

    /**
     * {@link FormQueryElementService#getTypedByReferenceName(Feature)}.
     */
    public String getTypedByReferenceName(Feature feature) {
        return this.formQueryElementService.getTypedByReferenceName(feature);
    }

    /**
     * {@link FormQueryElementService#getValueExpressionTextualRepresentation(FeatureValue)}.
     */
    public String getValueExpressionTextualRepresentation(FeatureValue value) {
        return this.formQueryElementService.getValueExpressionTextualRepresentation(value);
    }

    /**
     * {@link FormQueryElementService#getVisibilityEnumLiterals(Element)}.
     */
    public List<EEnumLiteral> getVisibilityEnumLiterals(Element element) {
        return this.formQueryElementService.getVisibilityEnumLiterals(element);
    }

    /**
     * {@link FormQueryElementService#getVisibilityPropertyOwner(Element)}.
     */
    public Element getVisibilityPropertyOwner(Element element) {
        return this.formQueryElementService.getVisibilityPropertyOwner(element);
    }

    /**
     * {@link FormQueryElementService#getVisibilityValue(Element)}.
     */
    public EEnumLiteral getVisibilityValue(Element element) {
        return this.formQueryElementService.getVisibilityValue(element);
    }

    /**
     * {@link FormQueryElementService#isBooleanAttribute(EStructuralFeature)}.
     */
    public boolean isBooleanAttribute(EStructuralFeature feature) {
        return this.formQueryElementService.isBooleanAttribute(feature);
    }

    /**
     * {@link FormQueryElementService#isEnumAttribute(EStructuralFeature)}.
     */
    public boolean isEnumAttribute(EStructuralFeature feature) {
        return this.formQueryElementService.isEnumAttribute(feature);
    }

    /**
     * {@link FormQueryElementService#isMultilineStringAttribute(Element, EStructuralFeature)}.
     */
    public boolean isMultilineStringAttribute(Element element, EStructuralFeature feature) {
        return this.formQueryElementService.isMultilineStringAttribute(element, feature);
    }

    /**
     * {@link FormQueryElementService#isNumberAttribute(EStructuralFeature)}.
     */
    public boolean isNumberAttribute(EStructuralFeature feature) {
        return this.formQueryElementService.isNumberAttribute(feature);
    }

    /**
     * {@link FormQueryElementService#isReadOnly(EStructuralFeature)}.
     */
    public boolean isReadOnly(EStructuralFeature feature) {
        return this.formQueryElementService.isReadOnly(feature);
    }

    /**
     * {@link FormQueryElementService#isReadOnly(Element)}.
     */
    public boolean isReadOnly(Element element) {
        return this.formQueryElementService.isReadOnly(element);
    }

    /**
     * {@link FormQueryElementService#isReadOnly(Element, EStructuralFeature)}.
     */
    public boolean isReadOnly(Element element, EStructuralFeature feature) {
        return this.formQueryElementService.isReadOnly(element, feature);
    }

    /**
     * {@link FormQueryElementService#isReadOnlyStringAttribute(Element, EStructuralFeature)}.
     */
    public boolean isReadOnlyStringAttribute(Element element, EStructuralFeature feature) {
        return this.formQueryElementService.isReadOnlyStringAttribute(element, feature);
    }

    /**
     * {@link FormQueryElementService#isReference(EStructuralFeature)}.
     */
    public boolean isReference(EStructuralFeature feature) {
        return this.formQueryElementService.isReference(feature);
    }

    /**
     * {@link FormQueryElementService#isStringAttribute(Element, EStructuralFeature)}.
     */
    public boolean isStringAttribute(Element element, EStructuralFeature feature) {
        return this.formQueryElementService.isStringAttribute(element, feature);
    }
}
