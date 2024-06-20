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
package org.eclipse.syson.application.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.application.configuration.SysMLStandardLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysMLv2PropertiesConfigurer;
import org.eclipse.syson.services.ImportService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;

/**
 * Java services needed to execute the AQL expressions used in the {@link SysMLv2PropertiesConfigurer}.
 *
 * @author arichard
 */
public class DetailsViewService {

    private final ComposedAdapterFactory composedAdapterFactory;

    private final IFeedbackMessageService feedbackMessageService;

    private final ImportService importService;

    private final EEnumLiteral unsetEnumLiteral;

    public DetailsViewService(ComposedAdapterFactory composedAdapterFactory, IFeedbackMessageService feedbackMessageService) {
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.importService = new ImportService();
        this.unsetEnumLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
        this.unsetEnumLiteral.setName("unset");
        this.unsetEnumLiteral.setLiteral("unset");
    }

    public String getDetailsViewLabel(Element element, EStructuralFeature eStructuralFeature) {
        return this.getLabelProvider().apply(element, eStructuralFeature);
    }

    private BiFunction<Element, EStructuralFeature, String> getLabelProvider() {
        return new EStructuralFeatureLabelProvider(this.composedAdapterFactory);
    }

    public List<EStructuralFeature> getAdvancedFeatures(Element element) {
        List<EStructuralFeature> coreFeatures = new CoreFeaturesSwitch().doSwitch(element);
        return element.eClass().getEAllStructuralFeatures().stream().filter(feature -> !coreFeatures.contains(feature)).toList();
    }

    public List<EStructuralFeature> getCoreFeatures(Element element) {
        return new CoreFeaturesSwitch().doSwitch(element);
    }

    public boolean setNewValue(Element element, EStructuralFeature eStructuralFeature, Object newValue) {
        try {
            Object valueToSet = newValue;
            if (eStructuralFeature.getEType() instanceof EEnum eDataType && eStructuralFeature.isUnsettable() && !(valueToSet instanceof Enumerator)) {
                element.eUnset(eStructuralFeature);
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
        return true;
    }

    public boolean setNewValue(Element element, String eStructuralFeatureName, Object newValue) {
        EStructuralFeature eStructuralFeature = element.eClass().getEStructuralFeature(eStructuralFeatureName);
        if (eStructuralFeature != null) {
            return this.setNewValue(element, eStructuralFeature, newValue);
        } else {
            this.feedbackMessageService.addFeedbackMessage(new Message("Unable to update the value of the " + eStructuralFeatureName + " feature", MessageLevel.ERROR));
            return false;
        }
    }

    public boolean isReadOnly(EStructuralFeature eStructuralFeature) {
        boolean isReadOnly = false;
        if (SysmlPackage.eINSTANCE.getConjugation_ConjugatedType().equals(eStructuralFeature)) {
            isReadOnly = true;
        } else if (SysmlPackage.eINSTANCE.getPortConjugation_OriginalPortDefinition().equals(eStructuralFeature)) {
            isReadOnly = true;
        } else {
            isReadOnly = eStructuralFeature.isDerived() || !eStructuralFeature.isChangeable();
        }
        return isReadOnly;
    }

    public boolean isReadOnly(Element element) {
        boolean isReadOnly = false;
        Resource resource = element.eResource();
        if (resource != null) {
            String uri = resource.getURI().toString();
            isReadOnly = uri.startsWith(SysMLStandardLibrariesConfiguration.SYSML_LIBRARY_SCHEME) || uri.startsWith(SysMLStandardLibrariesConfiguration.KERML_LIBRARY_SCHEME);
        }
        return isReadOnly;
    }

    /**
     * Checks that {@code element} OR {@code eStructuralFeature} are readOnly respectively based on isReadOnly(Element)
     * and isReadOnly(EStructuralFeature).
     *
     * @param element
     *            The {@link Element} to check
     * @param eStructuralFeature
     *            The {@link EStructuralFeature} to check
     * @return
     */
    public boolean isReadOnly(Element element, EStructuralFeature eStructuralFeature) {
        boolean isReadOnly = false;
        if (eStructuralFeature != null) {
            isReadOnly = isReadOnly || this.isReadOnly(eStructuralFeature);
        }
        if (element != null) {
            isReadOnly = isReadOnly || this.isReadOnly(element);
            if ((element instanceof StateUsage && SysmlPackage.eINSTANCE.getStateUsage_IsParallel().equals(eStructuralFeature))
                    || (element instanceof StateDefinition && SysmlPackage.eINSTANCE.getStateDefinition_IsParallel().equals(eStructuralFeature))) {
                isReadOnly = isReadOnly || ((Type) element).getOwnedFeature().stream().filter(TransitionUsage.class::isInstance).findAny().isPresent();
            } else if (element instanceof FeatureMembership && SysmlPackage.eINSTANCE.getFeaturing_Feature().equals(eStructuralFeature)) {
                isReadOnly = true;
            }
        }
        return isReadOnly;
    }

    public boolean isReadOnlyStringAttribute(Element element, EStructuralFeature eStructuralFeature) {
        if (eStructuralFeature instanceof EAttribute) {
            EClassifier eType = eStructuralFeature.getEType();
            boolean readOnlyProperty = false;
            if (SysmlPackage.eINSTANCE.getElement_ElementId().equals(eStructuralFeature)) {
                readOnlyProperty = true;
            } else if (eStructuralFeature.isDerived() || !eStructuralFeature.isChangeable()) {
                readOnlyProperty = true;
            } else if (element instanceof ConjugatedPortDefinition) {
                readOnlyProperty = true;
            }
            return readOnlyProperty && (!eStructuralFeature.isMany() && (eType.equals(EcorePackage.Literals.ESTRING) || Objects.equals(eType.getInstanceClassName(), String.class.getName())));
        }
        return false;
    }

    public boolean isStringAttribute(Element element, EStructuralFeature eStructuralFeature) {
        if (eStructuralFeature instanceof EAttribute) {
            EClassifier eType = eStructuralFeature.getEType();
            boolean readOnlyProperty = false;
            if (SysmlPackage.eINSTANCE.getElement_ElementId().equals(eStructuralFeature)) {
                readOnlyProperty = true;
            } else if (eStructuralFeature.isDerived() || !eStructuralFeature.isChangeable()) {
                readOnlyProperty = true;
            } else if (element instanceof ConjugatedPortDefinition) {
                readOnlyProperty = true;
            }
            return !readOnlyProperty && (!eStructuralFeature.isMany() && (eType.equals(EcorePackage.Literals.ESTRING) || Objects.equals(eType.getInstanceClassName(), String.class.getName())));
        }
        return false;
    }

    public boolean isNumberAttribute(EStructuralFeature eStructuralFeature) {
        var numericDataTypes = List.of(
                EcorePackage.Literals.EINT,
                EcorePackage.Literals.EINTEGER_OBJECT,
                EcorePackage.Literals.EDOUBLE,
                EcorePackage.Literals.EDOUBLE_OBJECT,
                EcorePackage.Literals.EFLOAT,
                EcorePackage.Literals.EFLOAT_OBJECT,
                EcorePackage.Literals.ELONG,
                EcorePackage.Literals.ELONG_OBJECT,
                EcorePackage.Literals.ESHORT,
                EcorePackage.Literals.ESHORT_OBJECT);
        return eStructuralFeature instanceof EAttribute eAttribute && numericDataTypes.contains(eAttribute.getEType());
    }

    public boolean isBooleanAttribute(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature instanceof EAttribute eAttribute && EcorePackage.Literals.EBOOLEAN.equals(eAttribute.getEType());
    }

    public boolean isEnumAttribute(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature instanceof EAttribute eAttribute && eAttribute.getEType() instanceof EEnum;
    }

    public List<EEnumLiteral> getEnumCandidates(Element element, EAttribute eAttribute) {
        List<EEnumLiteral> candidates = new ArrayList<>();
        if (eAttribute.getEAttributeType() instanceof EEnum eEnum) {
            EList<EEnumLiteral> eLiterals = eEnum.getELiterals();
            candidates.addAll(eLiterals);
            if (eAttribute.isUnsettable()) {
                candidates.add(this.unsetEnumLiteral);
            }
        }
        return candidates;
    }

    public List<EEnumLiteral> getEnumCandidates(Element element, String eAttributeName) {
        EStructuralFeature eStructuralFeature = element.eClass().getEStructuralFeature(eAttributeName);
        if (eStructuralFeature instanceof EAttribute eAttribute) {
            return this.getEnumCandidates(element, eAttribute);
        }
        return List.of();
    }

    public EEnumLiteral getEnumValue(Element element, EAttribute eAttribute) {
        EEnumLiteral enumValue = null;
        if (eAttribute.getEAttributeType() instanceof EEnum eEnum) {
            Object eLiteralValue = element.eGet(eAttribute);
            if (eLiteralValue != null) {
                enumValue = eEnum.getEEnumLiteralByLiteral(eLiteralValue.toString());
            } else if (eAttribute.isUnsettable()) {
                enumValue = this.unsetEnumLiteral;
            }
        }
        return enumValue;
    }

    public EEnumLiteral getEnumValue(Element element, String eAttributeName) {
        EStructuralFeature eStructuralFeature = element.eClass().getEStructuralFeature(eAttributeName);
        if (eStructuralFeature instanceof EAttribute eAttribute) {
            return this.getEnumValue(element, eAttribute);
        }
        return null;
    }

    public boolean isReference(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature instanceof EReference eReference && !eReference.isContainment() && !eReference.isContainer() && eReference.isChangeable();
    }

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

    public AcceptActionUsage getAcceptActionUsage(Element self) {
        if (self instanceof AcceptActionUsage accept) {
            return accept;
        }
        return null;
    }

    public StateUsage getStateUsage(Element self) {
        if (self instanceof StateUsage su) {
            return su;
        }
        return null;
    }

    public TransitionUsage getTransitionUsage(Element self) {
        if (self instanceof TransitionUsage transition) {
            return transition;
        }
        return null;
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

    public Element getAcceptActionUsagePayloadFeatureTyping(AcceptActionUsage acceptActionUsage) {
        this.checkAndRepairAcceptActionUsageStructure(acceptActionUsage);
        return acceptActionUsage.getPayloadParameter().getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .findFirst()
                .orElse(null);
    }

    public boolean setAcceptActionUsagePayloadParameter(AcceptActionUsage acceptActionUsage, Element newPayloadParameter) {
        if (newPayloadParameter instanceof Type newType) {
            var payloadParam = acceptActionUsage.getPayloadParameter();
            if (payloadParam != null) {
                var ft = payloadParam.getOwnedRelationship().stream()
                        .filter(FeatureTyping.class::isInstance)
                        .map(FeatureTyping.class::cast)
                        .findFirst()
                        .orElse(null);
                ft.setType(newType);
                return true;
            }
        }
        return false;
    }

    public boolean setTransitionSourceParameter(TransitionUsage transitionUsage, Element newSource) {
        if (newSource instanceof ActionUsage au) {
            // Update transition source
            transitionUsage.getOwnedMembership().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
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

    public Element getAcceptActionUsageReceiverMembership(AcceptActionUsage acceptActionUsage) {
        this.checkAndRepairAcceptActionUsageStructure(acceptActionUsage);
        return acceptActionUsage.getReceiverArgument().getOwnedRelationship().stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .findFirst()
                .orElse(null);
    }

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
}
