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

import java.util.Collection;
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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.application.configuration.SysMLStandardLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysMLv2PropertiesConfigurer;
import org.eclipse.syson.services.ImportService;
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

    private final ImportService importService;

    public DetailsViewService(ComposedAdapterFactory composedAdapterFactory, IFeedbackMessageService feedbackMessageService) {
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.importService = new ImportService();
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

    public boolean setNewValue(Element element, String eStructuralFeatureName, Object newValue) {
        try {
            element.eSet(element.eClass().getEStructuralFeature(eStructuralFeatureName), newValue);
        } catch (IllegalArgumentException | ClassCastException | ArrayStoreException e) {
            this.feedbackMessageService.addFeedbackMessage(new Message("Unable to update the value of the " + eStructuralFeatureName + " feature", MessageLevel.ERROR));
            return false;
        }
        return true;
    }

    public boolean isReadOnly(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature.isDerived() || !eStructuralFeature.isChangeable();
    }

    public boolean isReadOnly(Element element) {
        Resource resource = element.eResource();
        if (resource != null) {
            String uri = resource.getURI().toString();
            return uri.startsWith(SysMLStandardLibrariesConfiguration.SYSML_LIBRARY_SCHEME) || uri.startsWith(SysMLStandardLibrariesConfiguration.KERML_LIBRARY_SCHEME);
        }
        return false;
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

    public List<EEnumLiteral> getEnumCandidates(Element element, String eAttributeName) {
        EStructuralFeature eStructuralFeature = element.eClass().getEStructuralFeature(eAttributeName);
        if (eStructuralFeature instanceof EAttribute eAttribute) {
            return this.getEnumCandidates(element, eAttribute);
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
}
