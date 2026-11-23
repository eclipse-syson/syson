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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.parser.tests.delegate.NotSetSettingDelegateFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.delegate.invocation.OperationInvocationDelegateFactory;
import org.omg.sysml.lang.sysml.Annotation;
import org.omg.sysml.lang.sysml.ConjugatedPortDefinition;
import org.omg.sysml.lang.sysml.Conjugation;
import org.omg.sysml.lang.sysml.Dependency;
import org.omg.sysml.lang.sysml.Differencing;
import org.omg.sysml.lang.sysml.Disjoining;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.EndFeatureMembership;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureDirectionKind;
import org.omg.sysml.lang.sysml.FeatureInverting;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.InterfaceUsage;
import org.omg.sysml.lang.sysml.Intersecting;
import org.omg.sysml.lang.sysml.NamespaceImport;
import org.omg.sysml.lang.sysml.OccurrenceUsage;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.Package;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.PartDefinition;
import org.omg.sysml.lang.sysml.PartUsage;
import org.omg.sysml.lang.sysml.PortConjugation;
import org.omg.sysml.lang.sysml.PortDefinition;
import org.omg.sysml.lang.sysml.PortUsage;
import org.omg.sysml.lang.sysml.PortionKind;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.Subclassification;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TypeFeaturing;
import org.omg.sysml.lang.sysml.Unioning;
import org.omg.sysml.lang.sysml.VisibilityKind;

/**
 * Test class of {@link StructuralModelCompletionService}.
 *
 * @author Arthur Daussy
 */
public class StructuralModelCompletionServiceTest {

    private static final String SYSML_DELEGATE_URI = "http://www.omg.org/spec/SysML";

    /**
     * The {@link NotSetSettingDelegateFactory} used while testing the parser.
     */
    private static final NotSetSettingDelegateFactory SETTING_FACTORY = new NotSetSettingDelegateFactory();

    private static final SysMLFactory FACTORY = new SysMLParsingFactoryImpl();

    private final StructuralModelCompletionService service = new StructuralModelCompletionService(
            (object, reference) -> object.eGet(reference, false) != null);

    /**
     * Registers the model delegates before creating fixtures, including when this class runs on its own.
     */
    @BeforeClass
    public static void setUp() {
        EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE
                .putIfAbsent(SYSML_DELEGATE_URI, new OperationInvocationDelegateFactory());
        EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE
                .put(SYSML_DELEGATE_URI, SETTING_FACTORY);
        SysMLPackage.eINSTANCE.eClass();
    }

    /**
     * Checks target inference and preservation of containment for a detached reference subsetting.
     */
    @Test
    public void infersLastFeatureTargets() {
        Differencing differencing = FACTORY.createDifferencing();
        Feature differencingTarget = addFeatureTarget(differencing);
        this.service.complete(differencing);
        assertSame(differencingTarget, differencing.getDifferencingType());

        Unioning unioning = FACTORY.createUnioning();
        Feature unioningTarget = addFeatureTarget(unioning);
        this.service.complete(unioning);
        assertSame(unioningTarget, unioning.getUnioningType());

        Intersecting intersecting = FACTORY.createIntersecting();
        Feature intersectingTarget = addFeatureTarget(intersecting);
        this.service.complete(intersecting);
        assertSame(intersectingTarget, intersecting.getIntersectingType());

        ReferenceSubsetting referenceSubsetting = FACTORY.createReferenceSubsetting();
        Feature referencedFeature = addFeatureTarget(referenceSubsetting);

        this.service.complete(referenceSubsetting);

        assertSame(referencedFeature, referenceSubsetting.getReferencedFeature());
        assertNull(referenceSubsetting.eContainer());
        assertSame(referenceSubsetting, referencedFeature.eContainer());
    }

    @Test
    public void infersRelationshipEndsFromOwnerAndFeatureChain() {
        Feature subsettingOwner = FACTORY.createFeature();
        Subsetting subsetting = ownRelationship(subsettingOwner, FACTORY.createSubsetting());
        Feature subsettedFeature = addFeatureTarget(subsetting);
        this.service.complete(subsetting);
        assertSame(subsettingOwner, subsetting.getSubsettingFeature());
        assertSame(subsettedFeature, subsetting.getSubsettedFeature());

        Feature redefiningFeature = FACTORY.createFeature();
        Redefinition redefinition = ownRelationship(redefiningFeature, FACTORY.createRedefinition());
        Feature redefinedFeature = addFeatureTarget(redefinition);
        this.service.complete(redefinition);
        assertSame(redefiningFeature, redefinition.getRedefiningFeature());
        assertSame(redefinedFeature, redefinition.getRedefinedFeature());

        Feature invertedFeature = FACTORY.createFeature();
        FeatureInverting inverting = ownRelationship(invertedFeature, FACTORY.createFeatureInverting());
        Feature invertingFeature = addFeatureTarget(inverting);
        this.service.complete(inverting);
        assertSame(invertedFeature, inverting.getFeatureInverted());
        assertSame(invertingFeature, inverting.getInvertingFeature());

        PartDefinition disjoinedType = FACTORY.createPartDefinition();
        Disjoining disjoining = ownRelationship(disjoinedType, FACTORY.createDisjoining());
        Feature disjoiningType = addFeatureTarget(disjoining);
        this.service.complete(disjoining);
        assertSame(disjoinedType, disjoining.getTypeDisjoined());
        assertSame(disjoiningType, disjoining.getDisjoiningType());

        PartDefinition specific = FACTORY.createPartDefinition();
        Specialization specialization = ownRelationship(specific, FACTORY.createSpecialization());
        Feature general = addFeatureTarget(specialization);
        this.service.complete(specialization);
        assertSame(specific, specialization.getSpecific());
        assertSame(general, specialization.getGeneral());

        PartDefinition conjugated = FACTORY.createPartDefinition();
        Conjugation conjugation = ownRelationship(conjugated, FACTORY.createConjugation());
        PartDefinition original = FACTORY.createPartDefinition();
        conjugation.getOwnedRelatedElement().add(original);
        this.service.complete(conjugation);
        assertSame(conjugated, conjugation.getConjugatedType());
        assertSame(original, conjugation.getOriginalType());
    }

    @Test
    public void infersOwnerAndFirstElementReferences() {
        Feature typedFeature = FACTORY.createFeature();
        FeatureTyping featureTyping = ownRelationship(typedFeature, FACTORY.createFeatureTyping());
        Feature type = addFeatureTarget(featureTyping);
        this.service.complete(featureTyping);
        assertSame(typedFeature, featureTyping.getTypedFeature());
        assertSame(type, featureTyping.getType());

        Feature featured = FACTORY.createFeature();
        TypeFeaturing typeFeaturing = ownRelationship(featured, FACTORY.createTypeFeaturing());
        this.service.complete(typeFeaturing);
        assertSame(featured, typeFeaturing.getFeatureOfType());

        PartDefinition annotated = FACTORY.createPartDefinition();
        Annotation annotation = ownRelationship(annotated, FACTORY.createAnnotation());
        this.service.complete(annotation);
        assertSame(annotated, annotation.getAnnotatedElement());

        Package importingNamespace = FACTORY.createPackage();
        NamespaceImport namespaceImport = ownRelationship(importingNamespace, FACTORY.createNamespaceImport());
        this.service.complete(namespaceImport);
        assertSame(importingNamespace, namespaceImport.getImportedNamespace());

        PartDefinition subclassifier = FACTORY.createPartDefinition();
        Subclassification subclassification = ownRelationship(subclassifier, FACTORY.createSubclassification());
        this.service.complete(subclassification);
        assertSame(subclassifier, subclassification.getSubclassifier());
    }

    @Test
    public void portConjugationContinuesWithConjugationInference() {
        PortDefinition originalPort = FACTORY.createPortDefinition();
        ConjugatedPortDefinition conjugatedPort = FACTORY.createConjugatedPortDefinition();
        OwningMembership membership = ownRelationship(originalPort, FACTORY.createOwningMembership());
        membership.getOwnedRelatedElement().add(conjugatedPort);
        PortConjugation portConjugation = ownRelationship(conjugatedPort, FACTORY.createPortConjugation());

        this.service.complete(portConjugation);

        assertSame(originalPort, portConjugation.getOriginalPortDefinition());
        assertSame(conjugatedPort, portConjugation.getConjugatedType());
    }

    @Test
    public void explicitReferencePreventsInference() {
        ReferenceSubsetting referenceSubsetting = FACTORY.createReferenceSubsetting();
        addFeatureTarget(referenceSubsetting);
        StructuralModelCompletionService explicitService = new StructuralModelCompletionService(
                (object, reference) -> reference == SysMLPackage.Literals.REFERENCE_SUBSETTING__REFERENCED_FEATURE);

        explicitService.complete(referenceSubsetting);

        assertNull(referenceSubsetting.getReferencedFeature());
    }

    @Test
    public void completesFeatureAndUsagePropertiesWithoutApplyingFeatureRuleToUsages() {
        Feature feature = FACTORY.createFeature();
        feature.setIsConstant(true);
        this.service.complete(feature);
        assertTrue(feature.isVariable());

        PartUsage usage = FACTORY.createPartUsage();
        usage.setIsConstant(true);
        usage.setIsVariation(true);
        usage.setDirection(FeatureDirectionKind.IN);
        usage.setIsComposite(true);
        this.service.complete(usage);
        assertFalse(usage.isVariable());
        assertTrue(usage.isAbstract());
        assertFalse(usage.isComposite());

        // InterfaceUsage visits Feature before Usage in the generated switch. This
        // regression proves that the Feature guard preserves Usage semantics.
        InterfaceUsage interfaceUsage = FACTORY.createInterfaceUsage();
        interfaceUsage.setIsConstant(true);
        this.service.complete(interfaceUsage);
        assertFalse(interfaceUsage.isVariable());
    }

    @Test
    public void completesOccurrenceAndDefinitionProperties() {
        OccurrenceUsage occurrenceUsage = FACTORY.createOccurrenceUsage();
        occurrenceUsage.setPortionKind(PortionKind.SNAPSHOT);
        this.service.complete(occurrenceUsage);
        assertTrue(occurrenceUsage.isPortion());

        PartDefinition definition = FACTORY.createPartDefinition();
        definition.setIsVariation(true);
        this.service.complete(definition);
        assertTrue(definition.isAbstract());
    }

    @Test
    public void propagatesMembershipPropertiesFromOwnedElementsWithoutDerivedGetters() {
        ParameterMembership parameterMembership = FACTORY.createParameterMembership();
        Feature parameter = FACTORY.createFeature();
        parameterMembership.getOwnedRelatedElement().add(parameter);
        this.service.complete(parameterMembership);
        assertEquals(FeatureDirectionKind.IN, parameter.getDirection());

        EndFeatureMembership endMembership = FACTORY.createEndFeatureMembership();
        Feature endFeature = FACTORY.createFeature();
        endMembership.getOwnedRelatedElement().add(endFeature);
        this.service.complete(endMembership);
        assertTrue(endFeature.isEnd());
    }

    @Test
    public void correctsPortUsageStructureAndPropagatesOccurrenceProperties() {
        PartDefinition owner = FACTORY.createPartDefinition();
        FeatureMembership membership = ownRelationship(owner, FACTORY.createFeatureMembership());
        PortUsage portUsage = FACTORY.createPortUsage();
        portUsage.setIsComposite(true);
        portUsage.setPortionKind(PortionKind.SNAPSHOT);
        membership.getOwnedRelatedElement().add(portUsage);

        this.service.complete(portUsage);

        assertFalse(portUsage.isComposite());
        assertTrue(portUsage.isPortion());
    }

    @Test
    public void correctsFilteredImportVisibilityFromContainment() {
        Package outerPackage = FACTORY.createPackage();
        NamespaceImport owningImport = ownRelationship(outerPackage, FACTORY.createNamespaceImport());
        Package filteredPackage = FACTORY.createPackage();
        owningImport.getOwnedRelatedElement().add(filteredPackage);
        NamespaceImport nestedImport = ownRelationship(filteredPackage, FACTORY.createNamespaceImport());
        nestedImport.setVisibility(VisibilityKind.PRIVATE);

        this.service.complete(nestedImport);

        assertEquals(VisibilityKind.PUBLIC, nestedImport.getVisibility());
    }

    @Test
    public void derivesDependencySuppliersIdempotently() {
        Dependency dependency = FACTORY.createDependency();
        PartDefinition explicitSupplierProxy = FACTORY.createPartDefinition();
        ((InternalEObject) explicitSupplierProxy).eSetProxyURI(URI.createURI("unresolved:/supplier"));
        PartDefinition structuralSupplier = FACTORY.createPartDefinition();
        dependency.getSupplier().add(explicitSupplierProxy);
        dependency.getOwnedRelatedElement().add(structuralSupplier);

        this.service.complete(dependency);
        this.service.complete(dependency);

        assertEquals(2, dependency.getSupplier().size());
        @SuppressWarnings({ "rawtypes", "unchecked" })
        InternalEList<Element> suppliers = (InternalEList<Element>) dependency.getSupplier();
        assertSame(explicitSupplierProxy, suppliers.basicGet(0));
        assertTrue(suppliers.basicGet(0).eIsProxy());
        assertSame(structuralSupplier, suppliers.basicGet(1));
    }

    @Test
    public void doesNotResolveExplicitProxyReferences() {
        ReferenceSubsetting referenceSubsetting = FACTORY.createReferenceSubsetting();
        Feature proxy = FACTORY.createFeature();
        ((InternalEObject) proxy).eSetProxyURI(URI.createURI("unresolved:/feature"));
        referenceSubsetting.setReferencedFeature(proxy);

        new StructuralModelCompletionService().complete(referenceSubsetting);

        Feature unresolved = (Feature) referenceSubsetting.eGet(
                SysMLPackage.Literals.REFERENCE_SUBSETTING__REFERENCED_FEATURE, false);
        assertSame(proxy, unresolved);
        assertTrue(unresolved.eIsProxy());
    }

    /**
     * Checks that specialized relationships run the completion cases required for their references.
     */
    @Test
    public void specializedRelationshipsProcessSupertypeCases() {
        Set<EStructuralFeature> checkedReferences = new HashSet<>();
        StructuralModelCompletionService trackingService = new StructuralModelCompletionService((object, reference) -> {
            checkedReferences.add(reference);
            return true;
        });

        trackingService.complete(FACTORY.createSubsetting());
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SPECIALIZATION__GENERAL));
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SPECIALIZATION__SPECIFIC));

        checkedReferences.clear();
        trackingService.complete(FACTORY.createRedefinition());
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SUBSETTING__SUBSETTED_FEATURE));
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SUBSETTING__SUBSETTING_FEATURE));
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SPECIALIZATION__GENERAL));
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SPECIALIZATION__SPECIFIC));

        checkedReferences.clear();
        trackingService.complete(FACTORY.createReferenceSubsetting());
        assertEquals(Set.of(SysMLPackage.Literals.REFERENCE_SUBSETTING__REFERENCED_FEATURE), checkedReferences);

        checkedReferences.clear();
        trackingService.complete(FACTORY.createFeatureTyping());
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SPECIALIZATION__GENERAL));
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SPECIALIZATION__SPECIFIC));

        checkedReferences.clear();
        trackingService.complete(FACTORY.createSubclassification());
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SPECIALIZATION__GENERAL));
        assertTrue(checkedReferences.contains(SysMLPackage.Literals.SPECIALIZATION__SPECIFIC));
    }

    private static Feature addFeatureTarget(Relationship relationship) {
        Feature target = FACTORY.createFeature();
        relationship.getOwnedRelatedElement().add(target);
        return target;
    }

    private static <T extends Relationship> T ownRelationship(Element owner, T relationship) {
        owner.getOwnedRelationship().add(relationship);
        return relationship;
    }
}
