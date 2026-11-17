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
package org.eclipse.syson.sysml.parser.internal;

import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.omg.sysml.lang.sysml.AnnotatingElement;
import org.omg.sysml.lang.sysml.Annotation;
import org.omg.sysml.lang.sysml.AttributeDefinition;
import org.omg.sysml.lang.sysml.AttributeUsage;
import org.omg.sysml.lang.sysml.Classifier;
import org.omg.sysml.lang.sysml.ConjugatedPortDefinition;
import org.omg.sysml.lang.sysml.Conjugation;
import org.omg.sysml.lang.sysml.Definition;
import org.omg.sysml.lang.sysml.Dependency;
import org.omg.sysml.lang.sysml.Differencing;
import org.omg.sysml.lang.sysml.Disjoining;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.EndFeatureMembership;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureInverting;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.Intersecting;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.NamespaceImport;
import org.omg.sysml.lang.sysml.OccurrenceUsage;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.PortConjugation;
import org.omg.sysml.lang.sysml.PortDefinition;
import org.omg.sysml.lang.sysml.PortUsage;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.Subclassification;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.lang.sysml.TypeFeaturing;
import org.omg.sysml.lang.sysml.Unioning;
import org.omg.sysml.lang.sysml.Usage;
import org.omg.sysml.lang.sysml.VisibilityKind;
import org.omg.sysml.lang.sysml.util.SysMLSwitch;

/**
 * Completes structural model features that were omitted from the concrete syntax or that can be propagated from the
 * containment structure.
 * <p>
 * This service processes one element at a time and deliberately does not own a traversal strategy. It only reads
 * unresolved references and containment links, so callers may use it as soon as the parsed containment graph is
 * complete, before link resolution. The Xtext pipeline may also invoke the same cases later through its historical
 * adapter post-processing phase.
 * </p>
 * NOTE: Maybe not the final implementation. Current discussion on this topic in
 * https://openmbee.atlassian.net/browse/ST6RI-953
 *
 * @author Arthur Daussy
 */
public class StructuralModelCompletionService extends SysMLSwitch<Boolean> {

    private static final Boolean STOP_SUPERTYPE_PROCESSING = Boolean.TRUE;

    private final IReferenceSetChecker referenceSetChecker;

    /**
     * Creates a service that checks the current, unresolved EMF value of each reference. This preserves the historical
     * adapter post-processing behavior.
     */
    public StructuralModelCompletionService() {
        this((object, reference) -> object.eGet(reference, false) != null);
    }

    public StructuralModelCompletionService(IReferenceSetChecker referenceSetChecker) {
        this.referenceSetChecker = Objects.requireNonNull(referenceSetChecker);
    }

    /**
     * Completes the structural features of one element.
     *
     * @param element
     *            the element to process
     */
    public void complete(Element element) {
        if (element != null) {
            this.doSwitch(element);
        }
    }

    @Override
    public Boolean caseFeatureChainExpression(FeatureChainExpression object) {
        // ExpressionParserPostProcessor prepares this before semantic consultation.
        FeatureChainSourceTargetCompleter.completeSourceTarget(object);
        // Continue to Feature so its independent structural flags are completed too.
        return null;
    }

    @Override
    public Boolean caseFeature(Feature object) {
        // Source: org.omg.sysml.adapter.FeatureAdapter#postProcess().
        // A constant Feature is variable, except for Usage, whose adapter overrides
        // setIsVariableIfConstant() with a deliberate no-op.
        if (object instanceof Usage) {
            // Some multiple-inheritance switch paths (notably InterfaceUsage) visit
            // Feature before Usage. Continue so caseUsage can apply the right rules.
            return null;
        }
        if (object.isConstant()) {
            object.setIsVariable(true);
        }
        // FeatureAdapter calls super.postProcess().
        return null;
    }

    @Override
    public Boolean caseUsage(Usage object) {
        // Source: org.omg.sysml.adapter.UsageAdapter#postProcess().
        // Propagate syntax flags that constrain abstraction and composition.
        if (object.isVariation()) {
            object.setIsAbstract(true);
        }
        Type featuringType = this.getExpectedFeaturingTypeOf(object);
        boolean isNotCompositeFeatureType = featuringType == null ||
                featuringType instanceof AttributeDefinition || featuringType instanceof AttributeUsage;
        if (object.getDirection() != null || object.isEnd() || isNotCompositeFeatureType) {
            object.setIsComposite(false);
        }
        // UsageAdapter calls super.postProcess(). caseFeature will therefore be
        // reached on ordinary Usage switch paths, but its Usage guard reproduces the
        // adapter's no-op setIsVariableIfConstant() override.
        return null;
    }

    /**
     * Determine the featuringType a Usage is expected to have after transformation, without actually computing the full
     * derivation for featuringType. Assumes that a Usage in SysML can only get a featuringType in one of two ways: 1.
     * If it is an ownedFeature. 2. If it is a variant of a variation Usage that has a featuringType. Note that the
     * special featuringTypes of variable features are ignored.
     */
    // copied from logic UsageUtil
    private Type getExpectedFeaturingTypeOf(Usage usage) {
        Type res;

        EObject container = usage.eContainer();
        if (container instanceof FeatureMembership featureMembership) {
            res = featureMembership.getOwningType();
        } else if (container != null &&
                container.eContainer() instanceof Usage containingUsage &&
                containingUsage.isVariation()) {
            res = this.getExpectedFeaturingTypeOf(containingUsage);
        } else {
            res = null;
        }

        return res;
    }

    @Override
    public Boolean caseOccurrenceUsage(OccurrenceUsage object) {
        // Source: org.omg.sysml.xtext.postprocessing.OccurrenceUsageParserPostProcessor#postProcess().
        if (object.getPortionKind() != null) {
            object.setIsPortion(true);
        }
        // Continue to Usage, matching OccurrenceUsageParserPostProcessor's super call. The two
        // propagations write independent flags, so the switch order is immaterial.
        return null;
    }

    @Override
    public Boolean caseDefinition(Definition object) {
        // Source: org.omg.sysml.adapter.DefinitionAdapter#postProcess().
        // Variations cannot be concrete definitions.
        if (object.isVariation()) {
            object.setIsAbstract(true);
        }
        // DefinitionAdapter calls super.postProcess().
        return null;
    }

    @Override
    public Boolean caseParameterMembership(ParameterMembership object) {
        // Source: org.omg.sysml.adapter.ParameterMembershipAdapter#postProcess().
        // Read the owned Feature from containment instead of the resolving derived
        // getOwnedMemberParameter() reference.
        Feature parameter = this.firstOwnedRelatedElement(object, Feature.class);
        if (parameter != null) {
            if (object instanceof ReturnParameterMembership && parameter.getDirection() == null) {
                parameter.eAdapters().add(ITextAdapter.RETURN_DIRECTION_OMITTED);
            }
            parameter.setDirection(object.parameterDirection());
        }
        // ParameterMembershipAdapter calls super.postProcess().
        return null;
    }

    @Override
    public Boolean caseEndFeatureMembership(EndFeatureMembership object) {
        // Source: org.omg.sysml.adapter.EndFeatureMembershipAdapter#postProcess().
        // Read the owned Feature from containment instead of the resolving derived
        // getOwnedMemberFeature() reference.
        Feature endFeature = this.firstOwnedRelatedElement(object, Feature.class);
        if (endFeature != null) {
            endFeature.setIsEnd(true);
        }
        // EndFeatureMembershipAdapter calls super.postProcess().
        return null;
    }

    @Override
    public Boolean casePortUsage(PortUsage object) {
        // Source: org.omg.sysml.adapter.PortUsageAdapter#postProcess().
        // Follow containment rather than FeatureMembership#getOwningType(), whose
        // generated implementation may resolve a proxy.
        if (object.eContainer() instanceof FeatureMembership membership) {
            Element owner = membership.getOwningRelatedElement();
            if (!(owner instanceof PortDefinition || owner instanceof PortUsage)) {
                object.setIsComposite(false);
            }
        }
        // Continue through OccurrenceUsage and then Usage, reproducing the current
        // PortUsageParserPostProcessor inheritance chain.
        return null;
    }

    @Override
    public Boolean caseImport(Import object) {
        // Source: org.omg.sysml.adapter.ImportAdapter#postProcess().
        // An import of a namespace owned through a NamespaceImport is a filtered
        // package import and must be public. Both links below are containments.
        if (object.getOwningRelatedElement() instanceof Namespace owningNamespace
                && owningNamespace.getOwningRelationship() instanceof NamespaceImport) {
            object.setVisibility(VisibilityKind.PUBLIC);
        }
        // ImportAdapter calls super.postProcess().
        return null;
    }

    @Override
    public Boolean caseDependency(Dependency object) {
        // Source: org.omg.sysml.adapter.DependencyAdapter#postProcess().
        // Materialize the suppliers represented as owned related elements. Use the
        // non-resolving InternalEList API: EObjectResolvingEList#contains may resolve
        // proxies, while addUnique alone does not check uniqueness.
        @SuppressWarnings({ "rawtypes", "unchecked" })
        InternalEList<Element> suppliers = (InternalEList<Element>) object.getSupplier();
        for (Element supplier : object.getOwnedRelatedElement()) {
            if (!suppliers.basicContains(supplier)) {
                suppliers.addUnique(supplier);
            }
        }
        // DependencyAdapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseTypeFeaturing(TypeFeaturing object) {
        // Source: org.omg.sysml.adapter.TypeFeaturingAdapter#postProcess().
        // Infer featureOfType from the owning related Feature when it was omitted.
        if (this.isNotSet(object, SysMLPackage.Literals.TYPE_FEATURING__FEATURE_OF_TYPE)
                && object.getOwningRelatedElement() instanceof Feature feature) {
            object.setFeatureOfType(feature);
        }
        // The source adapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseDifferencing(Differencing object) {
        // Source: org.omg.sysml.adapter.DifferencingAdapter#postProcess().
        // Infer a feature-chain target from the last owned related element.
        if (this.isNotSet(object, SysMLPackage.Literals.DIFFERENCING__DIFFERENCING_TYPE)) {
            Feature target = this.lastOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setDifferencingType(target);
            }
        }
        // The source adapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean casePortConjugation(PortConjugation object) {
        // Source: org.omg.sysml.adapter.PortConjugationAdapter#postProcess().
        // Infer the original port from the owner of the conjugated port definition.
        if (this.isNotSet(object, SysMLPackage.Literals.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION)) {
            ConjugatedPortDefinition conjugatedPortDefinition = this.unresolvedReference(object,
                    SysMLPackage.Literals.PORT_CONJUGATION__CONJUGATED_PORT_DEFINITION,
                    ConjugatedPortDefinition.class);
            Element owner = this.owningRelatedElementOf(conjugatedPortDefinition);
            if (owner instanceof PortDefinition original) {
                object.setOriginalPortDefinition(original);
            }
        }
        // Returning null reproduces the source adapter's call to ConjugationAdapter.
        return null;
    }

    @Override
    public Boolean caseNamespaceImport(NamespaceImport object) {
        // Source: org.omg.sysml.adapter.NamespaceImportAdapter#postProcess().
        // Infer an implicit filter-package import, or fall back to the owning namespace.
        if (this.isNotSet(object, SysMLPackage.Literals.NAMESPACE_IMPORT__IMPORTED_NAMESPACE)) {
            EList<Element> ownedRelatedElements = object.getOwnedRelatedElement();
            if (!ownedRelatedElements.isEmpty() && ownedRelatedElements.get(0) instanceof Namespace namespace) {
                object.setImportedNamespace(namespace);
            } else if (object.getOwningRelatedElement() instanceof Namespace owningNamespace) {
                // getImportOwningNamespace() is derived through this containment but its
                // generated getter requests proxy resolution.
                object.setImportedNamespace(owningNamespace);
            }
        }
        // Returning null reproduces the source adapter's call to ImportAdapter.
        return null;
    }

    @Override
    public Boolean caseSubsetting(Subsetting object) {
        // Source: org.omg.sysml.adapter.SubsettingAdapter#postProcess().
        // Infer the target from the end of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.SUBSETTING__SUBSETTED_FEATURE)) {
            Feature target = this.lastOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setSubsettedFeature(target);
            }
        }
        // Infer the source from the owner, or from the start of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.SUBSETTING__SUBSETTING_FEATURE)) {
            Element owner = object.getOwningRelatedElement();
            final Feature source;
            if (owner instanceof Feature feature) {
                source = feature;
            } else {
                source = this.firstOwnedRelatedElement(object, Feature.class);
            }
            if (source != null) {
                object.setSubsettingFeature(source);
            }
        }
        // Continue to Specialization, matching SubsettingParserPostProcessor#postProcess().
        return null;
    }

    @Override
    public Boolean caseAnnotation(Annotation object) {
        // Source: org.omg.sysml.adapter.AnnotationAdapter#postProcess().
        // Infer the annotated element from the relationship owner when applicable.
        if (this.isNotSet(object, SysMLPackage.Literals.ANNOTATION__ANNOTATED_ELEMENT)) {
            Element owner = object.getOwningRelatedElement();
            if (!(owner instanceof AnnotatingElement)) {
                object.setAnnotatedElement(owner);
            }
        }
        // The source adapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseRedefinition(Redefinition object) {
        // Source: org.omg.sysml.adapter.RedefinitionAdapter#postProcess().
        // Infer the redefining feature from the owner or the start of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.REDEFINITION__REDEFINING_FEATURE)) {
            Element owner = object.getOwningRelatedElement();
            final Feature source;
            if (owner instanceof Feature feature) {
                source = feature;
            } else {
                source = this.firstOwnedRelatedElement(object, Feature.class);
            }
            if (source != null) {
                object.setRedefiningFeature(source);
            }
        }
        // Infer the redefined feature from the end of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.REDEFINITION__REDEFINED_FEATURE)) {
            Feature target = this.lastOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setRedefinedFeature(target);
            }
        }
        // Continue to Subsetting and Specialization, matching RedefinitionParserPostProcessor#postProcess().
        return null;
    }

    @Override
    public Boolean caseUnioning(Unioning object) {
        // Source: org.omg.sysml.adapter.UnioningAdapter#postProcess().
        // Infer a feature-chain target from the last owned related element.
        if (this.isNotSet(object, SysMLPackage.Literals.UNIONING__UNIONING_TYPE)) {
            Feature target = this.lastOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setUnioningType(target);
            }
        }
        // The source adapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseFeatureInverting(FeatureInverting object) {
        // Source: org.omg.sysml.adapter.FeatureInvertingAdapter#postProcess().
        // Infer the inverted feature from the owner or the start of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.FEATURE_INVERTING__FEATURE_INVERTED)) {
            Element owner = object.getOwningRelatedElement();
            final Feature source;

            if (owner instanceof Feature feature) {
                source = feature;
            } else {
                source = this.firstOwnedRelatedElement(object, Feature.class);
            }
            if (source != null) {
                object.setFeatureInverted(source);
            }
        }
        // Infer the inverting feature from the end of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.FEATURE_INVERTING__INVERTING_FEATURE)) {
            Feature target = this.lastOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setInvertingFeature(target);
            }
        }
        // The source adapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseReferenceSubsetting(ReferenceSubsetting object) {
        // Source: org.omg.sysml.adapter.ReferenceSubsettingAdapter#postProcess().
        // Infer the referenced feature from the end of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.REFERENCE_SUBSETTING__REFERENCED_FEATURE)) {
            Feature target = this.lastOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setReferencedFeature(target);
            }
        }
        // Continue to Subsetting and Specialization, matching ReferenceSubsettingParserPostProcessor#postProcess().
        return null;
    }

    @Override
    public Boolean caseFeatureTyping(FeatureTyping object) {
        // Source: org.omg.sysml.adapter.FeatureTypingAdapter#postProcess().
        // Infer the type from the start of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.FEATURE_TYPING__TYPE)) {
            Feature target = this.firstOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setType(target);
            }
        }
        // Infer the typed feature from the relationship owner.
        if (this.isNotSet(object, SysMLPackage.Literals.FEATURE_TYPING__TYPED_FEATURE)
                && object.getOwningRelatedElement() instanceof Feature feature) {
            object.setTypedFeature(feature);
        }
        // Continue to Specialization, matching FeatureTypingParserPostProcessor#postProcess().
        return null;
    }

    @Override
    public Boolean caseConjugation(Conjugation object) {
        // Source: org.omg.sysml.adapter.ConjugationAdapter#postProcess().
        // Infer the conjugated type from the owner or the start of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.CONJUGATION__CONJUGATED_TYPE)) {
            Element owner = object.getOwningRelatedElement();
            Type source;
            if (owner instanceof Type type) {
                source = type;
            } else {
                source = this.firstOwnedRelatedElement(object, Type.class);
            }
            if (source != null) {
                object.setConjugatedType(source);
            }
        }
        // Infer the original type from the end of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.CONJUGATION__ORIGINAL_TYPE)) {
            Type target = this.lastOwnedRelatedElement(object, Type.class);
            if (target != null) {
                object.setOriginalType(target);
            }
        }
        // The source adapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseDisjoining(Disjoining object) {
        // Source: org.omg.sysml.adapter.DisjoiningAdapter#postProcess().
        // Infer the disjoining type from the end of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.DISJOINING__DISJOINING_TYPE)) {
            Feature target = this.lastOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setDisjoiningType(target);
            }
        }
        // Infer the disjoined type from the owner or the start of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.DISJOINING__TYPE_DISJOINED)) {
            Element owner = object.getOwningRelatedElement();
            final Type source;
            if (owner instanceof Type type) {
                source = type;
            } else {
                source = this.firstOwnedRelatedElement(object, Feature.class);
            }
            if (source != null) {
                object.setTypeDisjoined(source);
            }
        }
        // The source adapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseIntersecting(Intersecting object) {
        // Source: org.omg.sysml.adapter.IntersectingAdapter#postProcess().
        // Infer a feature-chain target from the last owned related element.
        if (this.isNotSet(object, SysMLPackage.Literals.INTERSECTING__INTERSECTING_TYPE)) {
            Feature target = this.lastOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setIntersectingType(target);
            }
        }
        // The source adapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    @Override
    public Boolean caseSubclassification(Subclassification object) {
        // Source: org.omg.sysml.adapter.SubclassificationAdapter#postProcess().
        // Infer the subclassifier from the owning related Classifier.
        if (this.isNotSet(object, SysMLPackage.Literals.SUBCLASSIFICATION__SUBCLASSIFIER)
                && object.getOwningRelatedElement() instanceof Classifier classifier) {
            object.setSubclassifier(classifier);
        }
        // Continue to Specialization, matching SubclassificationParserPostProcessor#postProcess().
        return null;
    }

    @Override
    public Boolean caseSpecialization(Specialization object) {
        // Source: org.omg.sysml.adapter.SpecializationAdapter#postProcess().
        // Infer the general type from the end of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.SPECIALIZATION__GENERAL)) {
            Feature target = this.lastOwnedRelatedElement(object, Feature.class);
            if (target != null) {
                object.setGeneral(target);
            }
        }
        // Infer the specific type from the owner or the start of a feature chain.
        if (this.isNotSet(object, SysMLPackage.Literals.SPECIALIZATION__SPECIFIC)) {
            Element owner = object.getOwningRelatedElement();
            final Type source;

            if (owner instanceof Type type) {
                source = type;
            } else {
                source = this.firstOwnedRelatedElement(object, Feature.class);
            }

            if (source != null) {
                object.setSpecific(source);
            }
        }
        // The source adapter does not call super.postProcess().
        return STOP_SUPERTYPE_PROCESSING;
    }

    private boolean isNotSet(Element object, EReference reference) {
        return !this.referenceSetChecker.isSet(object, reference);
    }

    private <T extends Element> T unresolvedReference(Element object, EReference reference, Class<T> type) {
        Object value = object.eGet(reference, false);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    private Element owningRelatedElementOf(Element element) {
        if (element != null && element.getOwningRelationship() != null) {
            return element.getOwningRelationship().getOwningRelatedElement();
        }
        return null;
    }

    private <T extends Element> T firstOwnedRelatedElement(Relationship relationship, Class<T> type) {
        EList<Element> elements = relationship.getOwnedRelatedElement();
        if (elements.isEmpty() || !type.isInstance(elements.get(0))) {
            return null;
        }
        return type.cast(elements.get(0));
    }

    private <T extends Element> T lastOwnedRelatedElement(Relationship relationship, Class<T> type) {
        EList<Element> elements = relationship.getOwnedRelatedElement();
        final Element last;
        if (elements.isEmpty()) {
            last = null;
        } else {
            last = elements.get(elements.size() - 1);
        }

        if (type.isInstance(last)) {
            return type.cast(last);
        } else {
            return null;
        }
    }
}
