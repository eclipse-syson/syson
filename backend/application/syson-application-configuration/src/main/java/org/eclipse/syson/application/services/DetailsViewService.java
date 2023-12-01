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
package org.eclipse.syson.application.services;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.application.configuration.SysMLv2PropertiesConfigurer;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Java services needed to execute the AQL expressions used in the {@link SysMLv2PropertiesConfigurer}.
 *
 * @author arichard
 */
public class DetailsViewService {

    private final ComposedAdapterFactory composedAdapterFactory;

    private final IFeedbackMessageService feedbackMessageService;

    public DetailsViewService(ComposedAdapterFactory composedAdapterFactory, IFeedbackMessageService feedbackMessageService) {
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
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
            element.eSet(eStructuralFeature, newValue);
        } catch (IllegalArgumentException | ClassCastException | ArrayStoreException e) {
            this.feedbackMessageService.addFeedbackMessage(new Message("Unable to update the value of the " + eStructuralFeature.getName() + " feature", MessageLevel.ERROR));
            return false;
        }
        return true;
    }

    public boolean isReadOnly(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature.isDerived() || !eStructuralFeature.isChangeable();
    }

    public boolean isReadOnlyStringAttribute(EStructuralFeature eStructuralFeature) {
        if (eStructuralFeature instanceof EAttribute) {
            EClassifier eType = eStructuralFeature.getEType();
            boolean readOnlyProperty = false;
            if (SysmlPackage.eINSTANCE.getElement_ElementId().equals(eStructuralFeature)) {
                readOnlyProperty = true;
            } else if (eStructuralFeature.isDerived() || !eStructuralFeature.isChangeable()) {
                readOnlyProperty = true;
            }
            return readOnlyProperty && (!eStructuralFeature.isMany() && (eType.equals(EcorePackage.Literals.ESTRING) || Objects.equals(eType.getInstanceClassName(), String.class.getName())));
        }
        return false;
    }

    public boolean isStringAttribute(EStructuralFeature eStructuralFeature) {
        if (eStructuralFeature instanceof EAttribute) {
            EClassifier eType = eStructuralFeature.getEType();
            boolean readOnlyProperty = false;
            if (SysmlPackage.eINSTANCE.getElement_ElementId().equals(eStructuralFeature)) {
                readOnlyProperty = true;
            } else if (eStructuralFeature.isDerived() || !eStructuralFeature.isChangeable()) {
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
                EcorePackage.Literals.ESHORT_OBJECT
                );
        return eStructuralFeature instanceof EAttribute eAttribute && numericDataTypes.contains(eAttribute.getEType());
    }

    public boolean isBooleanAttribute(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature instanceof EAttribute eAttribute && EcorePackage.Literals.EBOOLEAN.equals(eAttribute.getEType());
    }

    public boolean isEnumAttribute(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature instanceof EAttribute eAttribute && eAttribute.getEType() instanceof EEnum;
    }

    public List<EEnumLiteral> getEnumCandidates(Element element, EAttribute eAttribute) {
        if (eAttribute.getEAttributeType() instanceof EEnum eEnum) {
            return eEnum.getELiterals();
        }
        return List.of();
    }

    public EEnumLiteral getEnumValue(Element element, EAttribute eAttribute) {
        if (eAttribute.getEAttributeType() instanceof EEnum eEnum) {
            Object eLiteralValue = element.eGet(eAttribute);
            return eEnum.getEEnumLiteralByLiteral(eLiteralValue.toString());
        }
        return null;
    }

    public boolean isReference(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature instanceof EReference eReference && !eReference.isContainment() && !eReference.isContainer() && eReference.isChangeable();
    }
}
