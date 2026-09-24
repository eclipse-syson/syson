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
package org.eclipse.syson.form.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.ImportService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.ElementInitializerSwitch;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;

/**
 * Form-related element services doing mutations.
 *
 * @author arichard
 */
public class FormMutationElementService {

    private final IFeedbackMessageService feedbackMessageService;

    private final ImportService importService;

    private final ElementInitializerSwitch elementInitializerSwitch;

    private final MetamodelQueryElementService metamodelQueryElementService;

    private final FormQueryElementService formQueryElementService;

    /**
     * Creates the form mutation element service.
     *
     * @param feedbackMessageService
     *            the feedback message service
     * @param metamodelQueryElementService
     *            the metamodel query service
     * @param formQueryElementService
     *            the form query element service
     */
    public FormMutationElementService(IFeedbackMessageService feedbackMessageService, MetamodelQueryElementService metamodelQueryElementService,
            FormQueryElementService formQueryElementService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.importService = new ImportService();
        this.elementInitializerSwitch = new ElementInitializerSwitch();
        this.metamodelQueryElementService = Objects.requireNonNull(metamodelQueryElementService);
        this.formQueryElementService = Objects.requireNonNull(formQueryElementService);
    }

    /**
     * Gets the payload feature typing of an accept action usage, creating the required payload and receiver structure
     * when it is absent.
     *
     * @param acceptActionUsage
     *            the accept action usage to repair and query
     * @return the payload feature typing, or {@code null} when none can be obtained
     */
    public Element getOrCreateAcceptActionUsagePayloadFeatureTyping(AcceptActionUsage acceptActionUsage) {
        this.checkAndRepairAcceptActionUsageStructure(acceptActionUsage);
        ReferenceUsage payloadParameter = acceptActionUsage.getPayloadParameter();
        if (payloadParameter == null) {
            return null;
        }
        return payloadParameter.getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the receiver membership of an accept action usage, creating the required payload and receiver structure
     * when it is absent.
     *
     * @param acceptActionUsage
     *            the accept action usage to repair and query
     * @return the receiver membership, or {@code null} when none can be obtained
     */
    public Element getOrCreateAcceptActionUsageReceiverMembership(AcceptActionUsage acceptActionUsage) {
        this.checkAndRepairAcceptActionUsageStructure(acceptActionUsage);
        Expression receiverArgument = acceptActionUsage.getReceiverArgument();
        if (receiverArgument == null) {
            return null;
        }
        return receiverArgument.getOwnedRelationship().stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * Handle the new value (i.e. set operation) of the reference widget for the extra property "Typed by". If the real
     * element that holds the property to set does not exist, this method should create it and attach it to the current
     * feature.
     *
     * @param feature
     *         the current {@link Feature}.
     * @param newValue
     *         the newValue to set.
     * @return the real element (i.e. a FeatureTyping) that holds the property to set.
     */
    public Element handleFeatureTypingNewValue(Feature feature, Object newValue) {
        if (!this.isValidTypedByValue(feature, newValue)) {
            this.feedbackMessageService.addFeedbackMessage(new Message("Unable to update the value of the " + this.formQueryElementService.getTypedByReference(feature).getName() + " feature", MessageLevel.ERROR));
            return feature;
        }
        EList<Relationship> ownedRelationship = feature.getOwnedRelationship();
        FeatureTyping featureTyping = ownedRelationship.stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .findFirst()
                .orElseGet(() -> {
                    FeatureTyping newFeatureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
                    ownedRelationship.add(newFeatureTyping);
                    newFeatureTyping.setTypedFeature(feature);
                    this.elementInitializerSwitch.doSwitch(newFeatureTyping);
                    return newFeatureTyping;
                });
        this.handleReferenceWidgetNewValue(featureTyping, SysmlPackage.eINSTANCE.getFeatureTyping_Type().getName(), newValue);
        return featureTyping;
    }

    /**
     * Handle reference widget new value for the form details view.
     * @param element
     *            the input value
     * @param eStructuralFeature
     *            the input value
     * @param newValue
     *            the input value
     * @return the result of this operation
     */
    public Element handleReferenceWidgetNewValue(Element element, String eStructuralFeature, Object newValue) {
        this.setNewValue(element, element.eClass().getEStructuralFeature(eStructuralFeature), newValue);
        if (element.eContainer() instanceof Element parent) {
            if (newValue instanceof Element elementToImport) {
                this.importService.handleImport(parent, elementToImport);
            } else if (newValue instanceof Collection<?> newValues) {
                newValues.stream()
                        .filter(Element.class::isInstance)
                        .map(Element.class::cast)
                        .forEach(elementToImport -> {
                            this.importService.handleImport(parent, elementToImport);
                        });
            }
        }
        return element;
    }

    /**
     * Set accept action usage payload parameter for the form details view.
     * @param acceptActionUsage
     *            the input value
     * @param newPayloadParameter
     *            the input value
     * @return the result of this operation
     */
    public boolean setAcceptActionUsagePayloadParameter(AcceptActionUsage acceptActionUsage, Element newPayloadParameter) {
        if (newPayloadParameter instanceof Type newType) {
            var payloadParam = acceptActionUsage.getPayloadParameter();
            if (payloadParam != null) {
                payloadParam.getOwnedRelationship().stream()
                        .filter(FeatureTyping.class::isInstance)
                        .map(FeatureTyping.class::cast)
                        .findFirst()
                        .ifPresent(ft -> ft.setType(newType));
                return true;
            }
        }
        return false;
    }

    /**
     * Set accept action usage receiver argument for the form details view.
     * @param acceptActionUsage
     *            the input value
     * @param newReceiverArgument
     *            the input value
     * @return the result of this operation
     */
    public boolean setAcceptActionUsageReceiverArgument(AcceptActionUsage acceptActionUsage, Element newReceiverArgument) {
        var receiverArgument = acceptActionUsage.getReceiverArgument();
        if (receiverArgument != null) {
            var m = receiverArgument.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .findFirst()
                    .orElse(null);
            if (m != null) {
                m.setMemberElement(newReceiverArgument);
                return true;
            }
        }
        return false;
    }

    /**
     * Set new comment value for the form details view.
     * @param self
     *            the input value
     * @param newValue
     *            the input value
     * @return the result of this operation
     */
    public Element setNewCommentValue(Element self, String newValue) {
        Comment comment = this.metamodelQueryElementService.getComment(self);
        if (comment == null) {
            var newComment = SysmlFactory.eINSTANCE.createComment();
            newComment.setBody(newValue);
            var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            self.getOwnedRelationship().add(owningMembership);
            owningMembership.getOwnedRelatedElement().add(newComment);
        } else {
            comment.setBody(newValue);
        }
        return self;
    }

    /**
     * Set new documentation value for the form details view.
     * @param self
     *            the input value
     * @param newValue
     *            the input value
     * @return the result of this operation
     */
    public Element setNewDocumentationValue(Element self, String newValue) {
        var documentations = self.getDocumentation();
        if (documentations.isEmpty()) {
            var documentation = SysmlFactory.eINSTANCE.createDocumentation();
            documentation.setBody(newValue);
            var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            self.getOwnedRelationship().add(owningMembership);
            owningMembership.getOwnedRelatedElement().add(documentation);
        } else {
            documentations.get(0).setBody(newValue);
        }
        return self;
    }

    /**
     * Set new value for the form details view.
     * @param element
     *            the input value
     * @param eStructuralFeature
     *            the input value
     * @param newValue
     *            the input value
     * @return the result of this operation
     */
    public boolean setNewValue(Element element, EStructuralFeature eStructuralFeature, Object newValue) {
        try {
            Object valueToSet = newValue;
            if (!eStructuralFeature.isMany()) {
                if (newValue instanceof List<?> newListValue) {
                    valueToSet = newListValue.get(0);
                }
            }
            if (eStructuralFeature.getEType() instanceof EEnum && eStructuralFeature.isUnsettable() && !(valueToSet instanceof Enumerator)) {
                element.eUnset(eStructuralFeature);
            } else if (eStructuralFeature.isMany() && newValue instanceof List<?> newListValue) {
                ((List<Object>) element.eGet(eStructuralFeature)).addAll(newListValue);
            } else {
                if (eStructuralFeature.getEType() instanceof EDataType eDataType && newValue instanceof String stringValue) {
                    valueToSet = EcoreUtil.createFromString(eDataType, stringValue);
                }
                element.eSet(eStructuralFeature, valueToSet);
            }
        } catch (IllegalArgumentException | ClassCastException | ArrayStoreException e) {
            this.feedbackMessageService.addFeedbackMessage(new Message("Unable to update the value of the " + eStructuralFeature.getName() + " feature", MessageLevel.ERROR));
            return false;
        }
        this.handleImplied(element, eStructuralFeature);
        return true;
    }

    /**
     * Set new value for the form details view.
     * @param element
     *            the input value
     * @param eStructuralFeatureName
     *            the input value
     * @param newValue
     *            the input value
     * @return the result of this operation
     */
    public boolean setNewValue(Element element, String eStructuralFeatureName, Object newValue) {
        EStructuralFeature eStructuralFeature = element.eClass().getEStructuralFeature(eStructuralFeatureName);
        if (eStructuralFeature != null) {
            return this.setNewValue(element, eStructuralFeature, newValue);
        } else {
            this.feedbackMessageService.addFeedbackMessage(new Message("Unable to update the value of the " + eStructuralFeatureName + " feature", MessageLevel.ERROR));
            return false;
        }
    }

    /**
     * Set transition source parameter for the form details view.
     * @param transitionUsage
     *            the input value
     * @param newSource
     *            the input value
     * @return the result of this operation
     */
    public boolean setTransitionSourceParameter(TransitionUsage transitionUsage, Element newSource) {
        if (newSource instanceof ActionUsage au) {
            // Update transition source
            transitionUsage.getOwnedMembership().stream()
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresent(mem -> mem.setMemberElement(au));
            // Update succession source
            Succession succession = transitionUsage.getSuccession();
            succession.getFeatureMembership().stream()
                    .filter(EndFeatureMembership.class::isInstance)
                    .map(EndFeatureMembership.class::cast)
                    .findFirst()
                    .ifPresent(endFeat -> {
                        endFeat.getOwnedRelatedElement().stream()
                                .findFirst()
                                .ifPresent(feat -> feat.getOwnedRelationship().stream()
                                        .filter(ReferenceSubsetting.class::isInstance)
                                        .map(ReferenceSubsetting.class::cast)
                                        .findFirst()
                                        .ifPresent(refSub -> refSub.setReferencedFeature(au)));
                    });
            return true;
        }
        return false;
    }

    /**
     * Set transition target parameter for the form details view.
     * @param transitionUsage
     *            the input value
     * @param newTarget
     *            the input value
     * @return the result of this operation
     */
    public boolean setTransitionTargetParameter(TransitionUsage transitionUsage, Element newTarget) {
        if (newTarget instanceof ActionUsage au) {
            // Update succession target
            Succession succession = transitionUsage.getSuccession();
            List<EndFeatureMembership> succFeatMemberships = succession.getFeatureMembership().stream()
                    .filter(EndFeatureMembership.class::isInstance)
                    .map(EndFeatureMembership.class::cast)
                    .toList();
            if (succFeatMemberships.size() > 1) {
                succFeatMemberships.get(1).getOwnedRelatedElement().stream()
                        .findFirst()
                        .ifPresent(feat -> feat.getOwnedRelationship().stream()
                                .filter(ReferenceSubsetting.class::isInstance)
                                .map(ReferenceSubsetting.class::cast)
                                .findFirst()
                                .ifPresent(refSub -> refSub.setReferencedFeature(au)));
            }
            return true;
        }
        return false;
    }

    /**
     * Sets the visibility value of the given element.
     *
     * @param self
     *            An element for which the list of visibility literals are being searched.
     * @param newValue
     *            the value to set.
     * @return <code>true</code> if the visibility feature of the given element has been properly set and
     *         <code>false</code> otherwise.
     */
    public boolean setVisibilityValue(Element self, Object newValue) {
        boolean result = false;
        if (self instanceof Membership membership) {
            result = this.setNewValue(membership, SysmlPackage.eINSTANCE.getMembership_Visibility().getName(), newValue);
        }
        return result;
    }

    private void checkAndRepairAcceptActionUsagePayload(AcceptActionUsage aau) {
        var payloadParam = aau.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .findFirst()
                .orElse(null);
        if (payloadParam == null) {
            payloadParam = SysmlFactory.eINSTANCE.createParameterMembership();
            aau.getOwnedRelationship().add(payloadParam);
        }
        var payloadRef = payloadParam.getOwnedRelatedElement().stream()
                .filter(ReferenceUsage.class::isInstance)
                .map(ReferenceUsage.class::cast)
                .findFirst()
                .orElse(null);
        if (payloadRef == null) {
            payloadRef = SysmlFactory.eINSTANCE.createReferenceUsage();
            payloadRef.setDirection(FeatureDirectionKind.INOUT);
            payloadParam.getOwnedRelatedElement().add(payloadRef);
        }
        var ft = payloadRef.getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .findFirst()
                .orElse(null);
        if (ft == null) {
            ft = SysmlFactory.eINSTANCE.createFeatureTyping();
            payloadRef.getOwnedRelationship().add(ft);
        }
    }

    private void checkAndRepairAcceptActionUsageReceiver(AcceptActionUsage aau) {
        // find or create the second parameter membership
        var paramList = aau.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .toList();
        final ParameterMembership receiverParam;
        if (paramList.size() < 2) {
            receiverParam = SysmlFactory.eINSTANCE.createParameterMembership();
            aau.getOwnedRelationship().add(receiverParam);
        } else {
            receiverParam = paramList.get(1);
        }
        // find or create the reference usage contained in the parameter membership
        var receiverRef = receiverParam.getOwnedRelatedElement().stream()
                .filter(ReferenceUsage.class::isInstance)
                .map(ReferenceUsage.class::cast)
                .findFirst()
                .orElse(null);
        if (receiverRef == null) {
            receiverRef = SysmlFactory.eINSTANCE.createReferenceUsage();
            receiverRef.setDirection(FeatureDirectionKind.IN);
            receiverParam.getOwnedRelatedElement().add(receiverRef);
        }
        // find or create the feature value relationship contained inside the reference usage
        var receiverFeatureVal = receiverRef.getOwnedRelationship().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst()
                .orElse(null);
        if (receiverFeatureVal == null) {
            receiverFeatureVal = SysmlFactory.eINSTANCE.createFeatureValue();
            receiverRef.getOwnedRelationship().add(receiverFeatureVal);
        }
        // find or create the feature reference expression contained inside the feature value relationship
        var receiverFeatureRefExpr = receiverFeatureVal.getOwnedRelatedElement().stream()
                .filter(FeatureReferenceExpression.class::isInstance)
                .map(FeatureReferenceExpression.class::cast)
                .findFirst()
                .orElse(null);
        if (receiverFeatureRefExpr == null) {
            receiverFeatureRefExpr = SysmlFactory.eINSTANCE.createFeatureReferenceExpression();
            receiverFeatureVal.getOwnedRelatedElement().add(receiverFeatureRefExpr);
        }
        // find or create the membership relationship contained inside the feature reference expression
        var receiverMembership = receiverFeatureRefExpr.getOwnedRelationship().stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .findFirst()
                .orElse(null);
        if (receiverMembership == null) {
            receiverMembership = SysmlFactory.eINSTANCE.createMembership();
            receiverFeatureRefExpr.getOwnedRelationship().add(receiverMembership);
        }
        // find or create the return parameter membership relationship contained inside the feature reference expression
        var receiverReturn = receiverFeatureRefExpr.getOwnedRelationship().stream()
                .filter(ReturnParameterMembership.class::isInstance)
                .map(ReturnParameterMembership.class::cast)
                .findFirst()
                .orElse(null);
        if (receiverReturn == null) {
            receiverReturn = SysmlFactory.eINSTANCE.createReturnParameterMembership();
            receiverFeatureRefExpr.getOwnedRelationship().add(receiverReturn);
        }
        // find or create the feature contained inside the parameter membership relationship
        var receiverFeature = receiverReturn.getOwnedRelatedElement().stream()
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .findFirst()
                .orElse(null);
        if (receiverFeature == null) {
            receiverFeature = SysmlFactory.eINSTANCE.createFeature();
            receiverFeature.setDirection(FeatureDirectionKind.OUT);
            receiverReturn.getOwnedRelatedElement().add(receiverFeature);
        }
    }

    /**
     * Verify that the given accept action usage contains the correct underneath structure of elements.<br>
     * An @link {@link AcceptActionUsage} should have two @link {@link ParameterMembership} relationships with a
     * {@link ReferenceUsage} in each one. The first is the payload parameter (and may hold the payload argument as
     * well), the second one holds the receiver argument.<br>
     * In case of the structure of the given accept action usage is not correct, this method creates missing part to
     * guarantee that it is well formed after its call.
     *
     * @param aau
     *            an {@link AcceptActionUsage}
     */
    private void checkAndRepairAcceptActionUsageStructure(AcceptActionUsage aau) {
        this.checkAndRepairAcceptActionUsagePayload(aau);
        this.checkAndRepairAcceptActionUsageReceiver(aau);
    }

    private void handleImplied(Element element, EStructuralFeature eStructuralFeature) {
        if (element instanceof Relationship relationship) {
            if (SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature().equals(eStructuralFeature)) {
                relationship.setIsImplied(false);
            } else if (SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature().equals(eStructuralFeature)) {
                relationship.setIsImplied(false);
            } else if (SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().equals(eStructuralFeature)) {
                relationship.setIsImplied(false);
            } else if (SysmlPackage.eINSTANCE.getSubclassification_Superclassifier().equals(eStructuralFeature)) {
                relationship.setIsImplied(false);
            } else if (SysmlPackage.eINSTANCE.getFeatureTyping_Type().equals(eStructuralFeature)) {
                relationship.setIsImplied(false);
            }
        }
    }

    /**
     * Checks whether all values submitted by the widget conform to its displayed reference type.
     *
     * @param feature
     *         the feature being typed
     * @param newValue
     *         the value submitted by the reference widget
     * @return {@code true} when every submitted value has the allowed EClass
     */
    private boolean isValidTypedByValue(Feature feature, Object newValue) {
        EClass allowedType = this.formQueryElementService.getTypedByReference(feature).getEReferenceType();
        if (newValue instanceof Collection<?> values) {
            return !values.isEmpty() && values.stream().allMatch(allowedType::isInstance);
        }
        return allowedType.isInstance(newValue);
    }
}
