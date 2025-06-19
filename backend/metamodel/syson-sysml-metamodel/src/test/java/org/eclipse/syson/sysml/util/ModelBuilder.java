/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.sysml.util;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.VariantMembership;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Helper used to build SysML model.
 *
 * @author Arthur Daussy
 */
public class ModelBuilder {

    private static final Map<Class<?>, EClass> CLASS_TO_ECLASS;

    private final SysmlFactory fact = SysmlFactory.eINSTANCE;

    static {
        CLASS_TO_ECLASS = EMFUtils.eAllContentSteamWithSelf(SysmlPackage.eINSTANCE).filter(e -> e instanceof EClass eClass && !eClass.isAbstract() && !eClass.isInterface()).map(EClass.class::cast)
                .collect(toMap(EClass::getInstanceClass, e -> e));
    }

    private final ECrossReferenceAdapter crossReferencerAdapter = new ECrossReferenceAdapter();

    private final ResourceSet resourceSet;

    public ModelBuilder() {
        this.resourceSet = new ResourceSetImpl();
    }

    public void addSubclassification(Classifier child, Classifier parent) {
        Subclassification subClassification = this.create(Subclassification.class);
        subClassification.setSuperclassifier(parent);
        subClassification.setSubclassifier(child);

        child.getOwnedRelationship().add(subClassification);
    }

    public void addSubsetting(Feature child, Feature parent) {
        Subsetting subsetting = this.create(Subsetting.class);
        subsetting.setSubsettingFeature(child);
        subsetting.setSubsettedFeature(parent);

        child.getOwnedRelationship().add(subsetting);
    }

    public ReferenceSubsetting addReferenceSubsetting(Feature child, Feature subsettedFeature) {
        ReferenceSubsetting referenceSubsetting = this.create(ReferenceSubsetting.class);
        referenceSubsetting.setSubsettingFeature(child);
        referenceSubsetting.setSubsettedFeature(subsettedFeature);
        referenceSubsetting.setReferencedFeature(subsettedFeature);

        child.getOwnedRelationship().add(referenceSubsetting);

        if (!subsettedFeature.getOwnedFeatureChaining().isEmpty()) {
            referenceSubsetting.getOwnedRelatedElement().add(subsettedFeature);
        }

        return referenceSubsetting;
    }

    public MultiplicityRange addMultiplicityRange(Element owner, Expression expression) {
        MultiplicityRange multiplicity = this.createIn(MultiplicityRange.class, owner);

        OwningMembership owningMembership = this.createIn(OwningMembership.class, multiplicity);

        owningMembership.getOwnedRelatedElement().add(expression);

        return multiplicity;
    }

    public MultiplicityRange addIntMutiplicityRange(Element owner, int value) {
        LiteralInteger literalInteger = this.create(LiteralInteger.class);
        literalInteger.setValue(value);
        return this.addMultiplicityRange(owner, literalInteger);
    }

    public MultiplicityRange addInfinityMutiplicityRange(Element owner) {
        return this.addMultiplicityRange(owner, this.create(LiteralInfinity.class));
    }

    public void addRedefinition(Feature redefiningFeature, Feature redefinedFeature) {
        Redefinition redefinition = this.create(Redefinition.class);
        redefinition.setRedefinedFeature(redefinedFeature);
        redefinition.setRedefiningFeature(redefiningFeature);

        redefiningFeature.getOwnedRelationship().add(redefinition);
    }

    public <T extends Element> T create(Class<T> type) {
        return this.createIn(type, null);
    }

    public Namespace createRootNamespace() {
        var resource = new ResourceImpl(URI.createURI(UUID.randomUUID().toString()));
        var namespace = SysmlFactory.eINSTANCE.createNamespace();
        resource.getContents().add(namespace);
        this.resourceSet.getResources().add(resource);
        return namespace;
    }

    public <T extends Element> T createWithName(Class<T> type, String name) {
        return this.createInWithFullName(type, null, name, null);
    }

    public <T extends Element> T createIn(Class<T> type, Element parent) {
        return this.createInWithName(type, parent, null);
    }

    public <T extends Element> T createInWithName(Class<T> type, Element parent, String name) {
        return this.createInWithFullName(type, parent, name, null);
    }

    public Feature createFeatureChaining(Feature... featuresToChain) {
        Feature feature = this.create(Feature.class);
        for (Feature f : featuresToChain) {
            FeatureChaining chain = this.create(FeatureChaining.class);
            chain.setChainingFeature(f);
            feature.getOwnedRelationship().add(chain);
        }
        return feature;
    }

    public <T extends Succession> T createSuccessionAsUsage(Class<T> type, Element parent, Feature source, Feature target) {
        T succession = this.create(type);

        // Set source
        ReferenceUsage sourceRefUsage = this.create(ReferenceUsage.class);
        sourceRefUsage.setIsEnd(true);
        if (source != null) {
            ReferenceSubsetting sourceReferenceSubsetting = this.addReferenceSubsetting(sourceRefUsage, source);
            // FeatureChains are directly contained by the Subsetting relationship
            if (!source.getOwnedFeatureChaining().isEmpty()) {
                sourceReferenceSubsetting.getOwnedRelatedElement().add(source);
            }
        }

        EndFeatureMembership sourceEndFeatureMembership = this.create(EndFeatureMembership.class);
        succession.getOwnedRelationship().add(sourceEndFeatureMembership);
        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceRefUsage);

        // Set target
        ReferenceUsage targetTefUsage = this.create(ReferenceUsage.class);
        targetTefUsage.setIsEnd(true);

        if (target != null) {
            ReferenceSubsetting targetReferenceSubsetting = this.addReferenceSubsetting(targetTefUsage, target);

            // FeatureChains are directly contained by the Subsetting relationship
            if (!target.getOwnedFeatureChaining().isEmpty()) {
                targetReferenceSubsetting.getOwnedRelatedElement().add(target);
            }
        }

        EndFeatureMembership targetEndFeatureMembership = this.create(EndFeatureMembership.class);
        succession.getOwnedRelationship().add(targetEndFeatureMembership);
        targetEndFeatureMembership.getOwnedRelatedElement().add(targetTefUsage);

        this.addFeatureMembership(parent, succession);

        return succession;
    }

    public void setType(Feature feature, Type type) {
        FeatureTyping featureTyping = this.fact.createFeatureTyping();
        featureTyping.setTypedFeature(feature);
        featureTyping.setType(type);
        feature.getOwnedRelationship().add(featureTyping);
    }

    public <T extends Element> T createInWithFullName(Class<T> type, Element parent, String name, String shortName) {
        T newInstance = (T) this.fact.create(CLASS_TO_ECLASS.get(type));
        newInstance.eAdapters().add(this.crossReferencerAdapter);
        if (name != null) {
            newInstance.setDeclaredName(name);
        }
        if (shortName != null) {
            newInstance.setDeclaredShortName(shortName);
        }
        if (parent != null) {
            if (newInstance instanceof Relationship newInstanceRelationship && !(newInstance instanceof Definition)) {
                parent.getOwnedRelationship().add(newInstanceRelationship);
            } else if (newInstance instanceof EnumerationUsage feature && parent instanceof EnumerationDefinition) {
                this.addVariantMembership(parent, feature);
            } else if (newInstance instanceof Feature feature && !(parent instanceof Package)) {
                this.addFeatureMembership(parent, feature);
            } else {
                this.addOwnedMembership(parent, newInstance);
            }
        }
        return newInstance;
    }

    private void addVariantMembership(Element parent, EnumerationUsage feature) {
        this.addOwnedMembership(parent, feature, VariantMembership.class, null);
    }

    private void addOwnedMembership(Element parent, Element ownedElement) {
        this.addOwnedMembership(parent, ownedElement, OwningMembership.class, null);
    }

    private void addFeatureMembership(Element parent, Feature feature) {
        this.addOwnedMembership(parent, feature, FeatureMembership.class, null);
    }

    private <T extends OwningMembership> T addOwnedMembership(Element parent, Element ownedElement, Class<T> relationshipType, VisibilityKind visibility) {
        T ownedRelation = (T) this.fact.create(CLASS_TO_ECLASS.get(relationshipType));
        parent.getOwnedRelationship().add(ownedRelation);
        ownedRelation.getOwnedRelatedElement().add(ownedElement);
        // I guess this should be done automatically when adding to ownedRelatedElement but waiting for subset and super
        // set to be implemented
        ownedRelation.setMemberElement(ownedElement);
        if (visibility != null) {
            ownedRelation.setVisibility(visibility);
        }
        return ownedRelation;
    }
}
