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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.syson.form.services.api.IDetailsViewHelpTextProvider;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;

/**
 * Form-related element services doing queries.
 *
 * @author arichard
 */
public class FormQueryElementService {

    private final List<Descriptor> composedAdapterFactoryDescriptors;

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    private final List<IDetailsViewHelpTextProvider> detailsViewHelpTextProviders;

    private final EEnumLiteral unsetEnumLiteral;

    private final MetamodelQueryElementService metamodelQueryElementService;

    /**
     * Creates the form query element service.
     *
     * @param composedAdapterFactoryDescriptors
     *            the EMF item-provider descriptors
     * @param readOnlyObjectPredicate
     *            the read-only predicate
     * @param metamodelQueryElementService
     *            the metamodel query service
     * @param detailsViewHelpTextProviders
     *            the details-view help text providers
     */
    public FormQueryElementService(List<Descriptor> composedAdapterFactoryDescriptors, IReadOnlyObjectPredicate readOnlyObjectPredicate,
            MetamodelQueryElementService metamodelQueryElementService, List<IDetailsViewHelpTextProvider> detailsViewHelpTextProviders) {
        this.composedAdapterFactoryDescriptors = Objects.requireNonNull(composedAdapterFactoryDescriptors);
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
        this.detailsViewHelpTextProviders = Objects.requireNonNull(detailsViewHelpTextProviders);
        this.unsetEnumLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
        this.unsetEnumLiteral.setName("unset");
        this.unsetEnumLiteral.setLiteral("unset");
        this.metamodelQueryElementService = Objects.requireNonNull(metamodelQueryElementService);
    }

    /**
     * Get accept action usage for the form details view.
     * @param self
     *            the input value
     * @return the result of this operation
     */
    public AcceptActionUsage getAcceptActionUsage(Element self) {
        if (self instanceof AcceptActionUsage accept) {
            return accept;
        }
        return null;
    }

    /**
     * Get advanced features for the form details view.
     * @param element
     *            the input value
     * @return the result of this operation
     */
    public List<EStructuralFeature> getAdvancedFeatures(Element element) {
        List<EStructuralFeature> coreFeatures = new CoreFeaturesSwitch().doSwitch(element);
        return element.eClass().getEAllStructuralFeatures().stream().filter(feature -> !coreFeatures.contains(feature)).toList();
    }

    /**
     * Get core features for the form details view.
     * @param element
     *            the input value
     * @return the result of this operation
     */
    public List<EStructuralFeature> getCoreFeatures(Element element) {
        return new CoreFeaturesSwitch().doSwitch(element);
    }

    /**
     * Get details view help text for the form details view.
     * @param element
     *            the input value
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
    public String getDetailsViewHelpText(Element element, EStructuralFeature eStructuralFeature) {
        return this.detailsViewHelpTextProviders.stream()
                .filter(provider -> provider.canHandle(element, eStructuralFeature))
                .findFirst()
                .map(provider -> provider.getHelpText(element, eStructuralFeature))
                .orElse("");
    }

    /**
     * Get details view label for the form details view.
     * @param element
     *            the input value
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
    public String getDetailsViewLabel(Element element, EStructuralFeature eStructuralFeature) {
        return this.getLabelProvider().apply(element, eStructuralFeature);
    }

    /**
     * Get enum candidates for the form details view.
     * @param element
     *            the input value
     * @param eAttribute
     *            the input value
     * @return the result of this operation
     */
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

    /**
     * Get enum candidates for the form details view.
     * @param element
     *            the input value
     * @param eAttributeName
     *            the input value
     * @return the result of this operation
     */
    public List<EEnumLiteral> getEnumCandidates(Element element, String eAttributeName) {
        EStructuralFeature eStructuralFeature = element.eClass().getEStructuralFeature(eAttributeName);
        if (eStructuralFeature instanceof EAttribute eAttribute) {
            return this.getEnumCandidates(element, eAttribute);
        }
        return List.of();
    }

    /**
     * Get enum value for the form details view.
     * @param element
     *            the input value
     * @param eAttribute
     *            the input value
     * @return the result of this operation
     */
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

    /**
     * Get enum value for the form details view.
     * @param element
     *            the input value
     * @param eAttributeName
     *            the input value
     * @return the result of this operation
     */
    public EEnumLiteral getEnumValue(Element element, String eAttributeName) {
        EStructuralFeature eStructuralFeature = element.eClass().getEStructuralFeature(eAttributeName);
        if (eStructuralFeature instanceof EAttribute eAttribute) {
            return this.getEnumValue(element, eAttribute);
        }
        return null;
    }

    /**
     * Gets the {@link ResultExpressionMembership} from a {@link Namespace} or a {@link ResultExpressionMembership}.
     *
     * @param self
     *            a {@link Namespace} or a {@link ResultExpressionMembership}.
     * @return a {@link ResultExpressionMembership} or <code>null</code>
     */
    public Element getExpression(Element self) {
        if (self instanceof Expression && !(self instanceof Usage) && !(self instanceof Definition)) {
            return self;
        } else {
            return null;
        }
    }

    /**
     * Gets the textual representation of the value of an actual {@link Expression}.
     *
     * @param expression
     *            an {@link Expression}
     * @return a textual representation of the expression (or empty string if none)
     */
    public String getExpressionTextualRepresentation(Expression expression) {
        return this.getExpressionAsText(expression);
    }

    /**
     * Gets the {@link FeatureValue} from a {@link Feature} or a {@link FeatureValue}.
     *
     * @param self
     *            a {@link FeatureValue} or {@link Feature}
     * @return a {@link FeatureValue} or <code>null</code>
     */
    public FeatureValue getFeatureValue(Element self) {
        FeatureValue result = null;
        if (self instanceof FeatureValue featureValue && featureValue.getValue() != null) {
            result = featureValue;
        } else if (self instanceof Feature feature) {
            result = feature.getOwnedRelationship().stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .findFirst()
                    .orElse(null);
        }
        return result;
    }

    /**
     * Gets the guard expression from a {@link TransitionUsage} or {@link TransitionFeatureMembership}.
     *
     * @param self
     *            {@link TransitionUsage} or {@link TransitionFeatureMembership}.
     * @return the corresponding guard {@link Expression} or <code>null</code>
     */
    public Expression getGuardExpression(Element self) {
        Expression result = null;
        if (self instanceof TransitionFeatureMembership expressionMembership && expressionMembership.getOwnedMemberElement() instanceof Expression expression) {
            result = expression;
        } else if (self instanceof TransitionUsage transitionUsage && !transitionUsage.getGuardExpression().isEmpty()) {
            result = transitionUsage.getGuardExpression().get(0);
        }
        return result;
    }

    /**
     * Checks if an element could contain an expression but does not.
     *
     * @param self
     *            an element.
     * @return the element if it could contain an expression but does not, <code>null</code> otherwise.
     */
    public Element getPotentialExpressionOwner(Element self) {
        if (!this.readOnlyObjectPredicate.test(self) && this.metamodelQueryElementService.canContainExpressionDefinition(self)
                && !this.metamodelQueryElementService.hasSingleExpressionDefinition(self)) {
            return self;
        } else {
            return null;
        }
    }

    /**
     * Gets the {@link ResultExpressionMembership} from a {@link Namespace} or a {@link ResultExpressionMembership}.
     *
     * @param self
     *            a {@link Namespace} or a {@link ResultExpressionMembership}.
     * @return a {@link ResultExpressionMembership} or <code>null</code>
     */
    public Element getResultExpression(Element self) {
        Element result = null;
        if (self instanceof ResultExpressionMembership expressionMembership && expressionMembership.getOwnedResultExpression() != null) {
            result = expressionMembership;
        } else if (self instanceof Namespace namespace && this.metamodelQueryElementService.getResultExpressionMembership(namespace) != null
                && this.metamodelQueryElementService.getResultExpressionMembership(namespace).getOwnedResultExpression() != null) {
            result = this.metamodelQueryElementService.getResultExpressionMembership(namespace);
        }
        return result;
    }

    /**
     * Gets the textual representation of the value of a {@link ResultExpressionMembership}.
     *
     * @param resultExpression
     *            a {@link ResultExpressionMembership}
     * @return a textual representation of the value (or empty string if none)
     */
    public String getResultExpressionTextualRepresentation(ResultExpressionMembership resultExpression) {
        Expression value = resultExpression.getOwnedResultExpression();
        return this.getExpressionAsText(value);
    }

    /**
     * Get state usage for the form details view.
     * @param self
     *            the input value
     * @return the result of this operation
     */
    public StateUsage getStateUsage(Element self) {
        if (self instanceof StateUsage su) {
            return su;
        }
        return null;
    }

    /**
     * Get transition usage for the form details view.
     * @param self
     *            the input value
     * @return the result of this operation
     */
    public TransitionUsage getTransitionUsage(Element self) {
        if (self instanceof TransitionUsage transition) {
            return transition;
        }
        return null;
    }

    /**
     * Gets the reference that defines the allowed values of the {@code Typed by} widget.
     *
     * @param feature
     *         the feature displayed in the details view
     * @return the definition reference for a usage, or the generic {@code Feature.type} reference otherwise
     */
    public EReference getTypedByReference(Feature feature) {
        if (feature instanceof Usage) {
            return Stream.concat(Stream.of(feature.eClass()), feature.eClass().getEAllSuperTypes().stream())
                    .map(this::getDefinitionReference)
                    .flatMap(Optional::stream)
                    .findFirst()
                    .orElse(SysmlPackage.eINSTANCE.getFeature_Type());
        }
        return SysmlPackage.eINSTANCE.getFeature_Type();
    }

    /**
     * Gets the reference name used to display the {@code Typed by} widget for a feature.
     *
     * <p>
     * A usage is displayed through its closest {@code *Definition} derived reference so that the reference widget
     * exposes the definition type allowed by the SysML metamodel. Other features keep the generic {@code type}
     * reference.
     * </p>
     *
     * @param feature
     *         the feature displayed in the details view
     * @return the name of the reference displayed by the widget
     */
    public String getTypedByReferenceName(Feature feature) {
        return this.getTypedByReference(feature).getName();
    }

    /**
     * Gets the textual representation of the value of a {@link FeatureValue}.
     *
     * @param featureValue
     *            a {@link FeatureValue}
     * @return a textual representation of the value (or empty string if none)
     */
    public String getValueExpressionTextualRepresentation(FeatureValue featureValue) {
        Expression value = featureValue.getValue();
        return this.getExpressionAsText(value);
    }

    /**
     * Returns the enumeration literals for the visibility feature of the given element.
     *
     * @param self
     *            An element for which the list of visibility literals are being searched.
     * @return the enumeration literals for the visibility feature of the given element.
     */
    public List<EEnumLiteral> getVisibilityEnumLiterals(Element self) {
        List<EEnumLiteral> result = List.of();
        if (self instanceof Membership membership) {
            result = this.getEnumCandidates(membership, SysmlPackage.eINSTANCE.getMembership_Visibility().getName());
        }
        return result;
    }

    /**
     * Returns the element that owns the visibility feature of the given element.
     *
     * @param self
     *            An element for which the visibility owner is being searched.
     * @return the element that owns the visibility feature of the given element
     */
    public Element getVisibilityPropertyOwner(Element self) {
        if (!(self instanceof Import) && (self.eContainer() instanceof Membership membership)) {
            return membership;
        }
        return null;
    }

    /**
     * Returns the visibility value of the given element.
     *
     * @param self
     *            An element for which the list of visibility literals are being searched.
     * @return the current value of the visibility feature of the given element.
     */
    public EEnumLiteral getVisibilityValue(Element self) {
        EEnumLiteral result = null;
        if (self instanceof Membership membership) {
            result = this.getEnumValue(membership, SysmlPackage.eINSTANCE.getMembership_Visibility().getName());
        }
        return result;
    }

    /**
     * Is boolean attribute for the form details view.
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
    public boolean isBooleanAttribute(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature instanceof EAttribute eAttribute && EcorePackage.Literals.EBOOLEAN.equals(eAttribute.getEType());
    }

    /**
     * Is enum attribute for the form details view.
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
    public boolean isEnumAttribute(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature instanceof EAttribute eAttribute && eAttribute.getEType() instanceof EEnum;
    }

    /**
     * Is multiline string attribute for the form details view.
     * @param element
     *            the input value
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
    public boolean isMultilineStringAttribute(Element element, EStructuralFeature eStructuralFeature) {
        boolean isMultiline = false;
        isMultiline = this.isBodyField(eStructuralFeature);
        return isMultiline;
    }

    /**
     * Is number attribute for the form details view.
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
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

    /**
     * Is read only for the form details view.
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
    public boolean isReadOnly(EStructuralFeature eStructuralFeature) {
        boolean isReadOnly = false;
        if (SysmlPackage.eINSTANCE.getConjugation_ConjugatedType().equals(eStructuralFeature)) {
            isReadOnly = true;
        } else if (SysmlPackage.eINSTANCE.getPortConjugation_OriginalPortDefinition().equals(eStructuralFeature)) {
            isReadOnly = true;
        } else if (SysmlPackage.eINSTANCE.getLibraryPackage_IsStandard().equals(eStructuralFeature)) {
            // Based on KerML 8.3.4.13.3, this feature should be set for LibraryPackages in the standard Kernel Model
            // Libraries or normative model libraries for a language built on KerML.
            // SysON only allows to work with SysML at the moment, and does not support the definition of other
            // normative model libraries.
            isReadOnly = true;
        } else {
            isReadOnly = eStructuralFeature.isDerived() || !eStructuralFeature.isChangeable();
        }
        return isReadOnly;
    }

    /**
     * Is read only for the form details view.
     * @param element
     *            the input value
     * @return the result of this operation
     */
    public boolean isReadOnly(Element element) {
        return this.readOnlyObjectPredicate.test(element);
    }

    /**
     * Checks that {@code element} OR {@code eStructuralFeature} are readOnly respectively based on isReadOnly(Element)
     * and isReadOnly(EStructuralFeature).
     *
     * @param element
     *         The {@link Element} to check
     * @param eStructuralFeature
     *         The {@link EStructuralFeature} to check
     * @return the result of this operation
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
                isReadOnly = isReadOnly || ((Type) element).getOwnedFeature().stream().anyMatch(TransitionUsage.class::isInstance);
            } else if (element instanceof ViewUsage && SysmlPackage.eINSTANCE.getViewUsage_ExposedElement().equals(eStructuralFeature)) {
                isReadOnly = true;
            }
        }
        return isReadOnly;
    }

    /**
     * Is read only string attribute for the form details view.
     * @param element
     *            the input value
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
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

    /**
     * Is reference for the form details view.
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
    public boolean isReference(EStructuralFeature eStructuralFeature) {
        return eStructuralFeature instanceof EReference eReference && !eReference.isContainment() && !eReference.isContainer() && eReference.isChangeable();
    }

    /**
     * Is string attribute for the form details view.
     * @param element
     *            the input value
     * @param eStructuralFeature
     *            the input value
     * @return the result of this operation
     */
    public boolean isStringAttribute(Element element, EStructuralFeature eStructuralFeature) {
        if (eStructuralFeature instanceof EAttribute && !eStructuralFeature.isMany() && !this.isMultilineStringAttribute(element, eStructuralFeature)) {
            EClassifier eType = eStructuralFeature.getEType();
            boolean readOnlyProperty = false;
            if (SysmlPackage.eINSTANCE.getElement_ElementId().equals(eStructuralFeature)) {
                readOnlyProperty = true;
            } else if (eStructuralFeature.isDerived() || !eStructuralFeature.isChangeable()) {
                readOnlyProperty = true;
            } else if (element instanceof ConjugatedPortDefinition) {
                readOnlyProperty = true;
            }
            return !readOnlyProperty
                    && (eType.equals(EcorePackage.Literals.ESTRING) || Objects.equals(eType.getInstanceClassName(), String.class.getName()));
        }
        return false;
    }

    /**
     * Gets the {@code *Definition} reference associated with a usage EClass.
     *
     * @param usageEClass
     *         a usage EClass
     * @return its corresponding definition reference, if any
     */
    private Optional<EReference> getDefinitionReference(EClass usageEClass) {
        String usageName = usageEClass.getName();
        if (!usageName.endsWith("Usage") || usageName.length() == "Usage".length()) {
            return Optional.empty();
        }
        String definitionReferenceName = Character.toLowerCase(usageName.charAt(0)) + usageName.substring(1, usageName.length() - "Usage".length()) + "Definition";
        return Optional.ofNullable(usageEClass.getEStructuralFeature(definitionReferenceName))
                .filter(EReference.class::isInstance)
                .map(EReference.class::cast);
    }

    /**
     * Returns the serialized representation of an expression as plain text.
     *
     * @param expression
     *            the Expression
     * @return the plain text representation of the expression.
     */
    private String getExpressionAsText(Expression expression) {
        return this.metamodelQueryElementService.getExpressionTextualRepresentation(expression);
    }

    private BiFunction<Element, EStructuralFeature, String> getLabelProvider() {
        return new EStructuralFeatureLabelProvider(this.composedAdapterFactoryDescriptors);
    }

    private boolean isBodyField(EStructuralFeature eStructuralFeature) {
        return SysmlPackage.eINSTANCE.getTextualRepresentation_Body().equals(eStructuralFeature) || SysmlPackage.eINSTANCE.getComment_Body().equals(eStructuralFeature);
    }
}
