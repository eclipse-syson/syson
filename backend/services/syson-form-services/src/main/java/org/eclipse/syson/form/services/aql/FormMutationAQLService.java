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

import java.util.Objects;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.form.services.FormMutationElementService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.TransitionUsage;
/**
 * Entry point for all form-related services doing mutations in forms and called by AQL expressions in form
 * descriptions.
 *
 * @author arichard
 */
public class FormMutationAQLService {

    private final FormMutationElementService formMutationElementService;

    /**
     * Creates a mutation AQL service.
     *
     * @param formMutationElementService
     *            the shared form details element service
     */
    public FormMutationAQLService(FormMutationElementService formMutationElementService) {
        this.formMutationElementService = Objects.requireNonNull(formMutationElementService);
    }

    /**
     * {@link FormMutationElementService#getOrCreateAcceptActionUsagePayloadFeatureTyping(AcceptActionUsage)}.
     */
    public Element getOrCreateAcceptActionUsagePayloadFeatureTyping(AcceptActionUsage usage) {
        return this.formMutationElementService.getOrCreateAcceptActionUsagePayloadFeatureTyping(usage);
    }

    /**
     * {@link FormMutationElementService#getOrCreateAcceptActionUsageReceiverMembership(AcceptActionUsage)}.
     */
    public Element getOrCreateAcceptActionUsageReceiverMembership(AcceptActionUsage usage) {
        return this.formMutationElementService.getOrCreateAcceptActionUsageReceiverMembership(usage);
    }

    /**
     * {@link FormMutationElementService#handleFeatureTypingNewValue(Feature, Object)}.
     */
    public Element handleFeatureTypingNewValue(Feature feature, Object value) {
        return this.formMutationElementService.handleFeatureTypingNewValue(feature, value);
    }

    /**
     * {@link FormMutationElementService#handleReferenceWidgetNewValue(Element, String, Object)}.
     */
    public Element handleReferenceWidgetNewValue(Element element, String featureName, Object value) {
        return this.formMutationElementService.handleReferenceWidgetNewValue(element, featureName, value);
    }

    /**
     * {@link FormMutationElementService#setAcceptActionUsagePayloadParameter(AcceptActionUsage, Element)}.
     */
    public boolean setAcceptActionUsagePayloadParameter(AcceptActionUsage usage, Element value) {
        return this.formMutationElementService.setAcceptActionUsagePayloadParameter(usage, value);
    }

    /**
     * {@link FormMutationElementService#setAcceptActionUsageReceiverArgument(AcceptActionUsage, Element)}.
     */
    public boolean setAcceptActionUsageReceiverArgument(AcceptActionUsage usage, Element value) {
        return this.formMutationElementService.setAcceptActionUsageReceiverArgument(usage, value);
    }

    /**
     * {@link FormMutationElementService#setNewCommentValue(Element, String)}.
     */
    public Element setNewCommentValue(Element element, String value) {
        return this.formMutationElementService.setNewCommentValue(element, value);
    }

    /**
     * {@link FormMutationElementService#setNewDocumentationValue(Element, String)}.
     */
    public Element setNewDocumentationValue(Element element, String value) {
        return this.formMutationElementService.setNewDocumentationValue(element, value);
    }

    /**
     * {@link FormMutationElementService#setNewValue(Element, EStructuralFeature, Object)}.
     */
    public boolean setNewValue(Element element, EStructuralFeature feature, Object value) {
        return this.formMutationElementService.setNewValue(element, feature, value);
    }

    /**
     * {@link FormMutationElementService#setNewValue(Element, String, Object)}.
     */
    public boolean setNewValue(Element element, String featureName, Object value) {
        return this.formMutationElementService.setNewValue(element, featureName, value);
    }

    /**
     * {@link FormMutationElementService#setTransitionSourceParameter(TransitionUsage, Element)}.
     */
    public boolean setTransitionSourceParameter(TransitionUsage usage, Element value) {
        return this.formMutationElementService.setTransitionSourceParameter(usage, value);
    }

    /**
     * {@link FormMutationElementService#setTransitionTargetParameter(TransitionUsage, Element)}.
     */
    public boolean setTransitionTargetParameter(TransitionUsage usage, Element value) {
        return this.formMutationElementService.setTransitionTargetParameter(usage, value);
    }

    /**
     * {@link FormMutationElementService#setVisibilityValue(Element, Object)}.
     */
    public boolean setVisibilityValue(Element element, Object value) {
        return this.formMutationElementService.setVisibilityValue(element, value);
    }
}
