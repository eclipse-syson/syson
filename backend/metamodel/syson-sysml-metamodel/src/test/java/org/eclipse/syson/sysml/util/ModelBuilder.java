/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
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

    public void addSuperType(Classifier child, Classifier parent) {
        Subclassification subClassification = fact.createSubclassification();
        // Workaround waiting for the subset/refine implementation
        subClassification.setGeneral(parent);
        subClassification.setSpecific(child);
        subClassification.setSuperclassifier(parent);
        subClassification.setSubclassifier(child);

        child.getOwnedRelationship().add(subClassification);

    }

    public void addRedefinition(Feature child, Feature parent) {
        Redefinition redefinition = fact.createRedefinition();
        redefinition.setGeneral(parent);
        redefinition.setSpecific(child);
        // Workaround waiting for the subset/refine implementation
        redefinition.setRedefinedFeature(child);
        redefinition.setRedefiningFeature(parent);
        redefinition.setSubsettedFeature(child);
        redefinition.setSubsettingFeature(parent);

        child.getOwnedRelationship().add(redefinition);

    }

    public <T extends Element> T create(Class<T> type) {
        return createIn(type, null);
    }

    public <T extends Element> T createWithName(Class<T> type, String name) {
        return createInWithFullName(type, null, name, null);
    }

    public <T extends Element> T createIn(Class<T> type, Element parent) {
        return createInWithName(type, parent, null);
    }

    public <T extends Element> T createInWithName(Class<T> type, Element parent, String name) {
        return createInWithFullName(type, parent, name, null);
    }

    public void setType(Feature feature, Type type) {
        FeatureTyping featureTyping = fact.createFeatureTyping();
        featureTyping.setSpecific(feature);
        featureTyping.setTypedFeature(feature);
        // Waiting for subset/refine implementation
        featureTyping.setType(type);
        featureTyping.setGeneral(type);
        feature.getOwnedRelationship().add(featureTyping);
    }

    public <T extends Element> T createInWithFullName(Class<T> type, Element parent, String name, String shortName) {
        T newInstance = (T) fact.create(CLASS_TO_ECLASS.get(type));
        if (name != null) {
            newInstance.setDeclaredName(name);
        }

        if (shortName != null) {
            newInstance.setDeclaredShortName(shortName);
        }
        if (parent != null) {
            if (newInstance instanceof Relationship newInstanceRelationship && !(newInstance instanceof Definition)) {
                parent.getOwnedRelationship().add(newInstanceRelationship);
            } else if (newInstance instanceof Feature feature) {
                addFeatureMembership(parent, feature);
            } else {
                addOwnedMembership(parent, newInstance);
            }
        }
        return newInstance;
    }

    private void addOwnedMembership(Element parent, Element ownedElement) {
        addOwnedMembership(parent, ownedElement, OwningMembership.class, null);
    }

    private <T extends OwningMembership> T addOwnedMembership(Element parent, Element ownedElement, Class<T> relationshipType, VisibilityKind visibility) {
        T ownedRelation = (T) fact.create(CLASS_TO_ECLASS.get(relationshipType));
        parent.getOwnedRelationship().add(ownedRelation);
        ownedRelation.getOwnedRelatedElement().add(ownedElement);
        // I guess this should be done automatically when adding to ownedRelatedElement but waiting for subset and super
        // set to be implemented
        ownedRelation.getTarget().add(ownedElement);
        ownedRelation.setMemberElement(ownedElement);

        if (visibility != null) {

            ownedRelation.setVisibility(visibility);
        }

        return ownedRelation;

    }

    private void addFeatureMembership(Element parent, Feature feature) {
        FeatureMembership relationship = addOwnedMembership(parent, feature, FeatureMembership.class, null);
        // Workaround waiting for subset/refine implementation
        relationship.setFeature(feature);
    }

}
