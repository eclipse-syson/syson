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
package org.eclipse.syson.services;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch returning the related elements of an element to delete in the Explorer View, General View or any other
 * representations. These related elements must also be deleted.
 *
 * @author arichard
 */
public class RelatedElementsSwitch extends SysmlSwitch<Set<EObject>> {

    private final EStructuralFeature eStructuralFeature;

    public RelatedElementsSwitch(EStructuralFeature eStructuralFeature) {
        this.eStructuralFeature = Objects.requireNonNull(eStructuralFeature);
    }

    @Override
    public Set<EObject> caseElement(Element object) {
        return Set.of();
    }

    @Override
    public Set<EObject> caseDependency(Dependency object) {
        Set<EObject> relatedElements = new HashSet<>();
        if (this.eStructuralFeature.equals(SysmlPackage.eINSTANCE.getDependency_Client())) {
            relatedElements.add(object);
            if (object.eContainer() instanceof Membership membership) {
                relatedElements.add(membership);
            }
        } else if (this.eStructuralFeature.equals(SysmlPackage.eINSTANCE.getDependency_Supplier())) {
            relatedElements.add(object);
            if (object.eContainer() instanceof Membership membership) {
                relatedElements.add(membership);
            }
        }
        return relatedElements;
    }

    @Override
    public Set<EObject> caseFeatureTyping(FeatureTyping object) {
        Set<EObject> relatedElements = new HashSet<>();
        if (this.eStructuralFeature.equals(SysmlPackage.eINSTANCE.getFeatureTyping_Type())) {
            relatedElements.add(object);
        }
        return relatedElements;
    }

    @Override
    public Set<EObject> caseRedefinition(Redefinition object) {
        Set<EObject> relatedElements = new HashSet<>();
        if (this.eStructuralFeature.equals(SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature())) {
            relatedElements.add(object);
        }
        return relatedElements;
    }

    @Override
    public Set<EObject> caseReferenceSubsetting(ReferenceSubsetting object) {
        Set<EObject> relatedElements = new HashSet<>();
        if (object.getReferencedFeature() instanceof ActionUsage && object.eContainer() instanceof Feature feat) {
            if (feat.eContainer() instanceof EndFeatureMembership efm && efm.eContainer() instanceof Succession succ) {
                if (succ.eContainer() instanceof FeatureMembership fm && fm.eContainer() instanceof TransitionUsage tu) {
                    relatedElements.add(tu);
                }
            }
        }
        return relatedElements;
    }

    @Override
    public Set<EObject> caseSubclassification(Subclassification object) {
        Set<EObject> relatedElements = new HashSet<>();
        if (this.eStructuralFeature.equals(SysmlPackage.eINSTANCE.getSubclassification_Superclassifier())) {
            relatedElements.add(object);
        }
        return relatedElements;
    }

    @Override
    public Set<EObject> caseSuccession(Succession object) {
        Set<EObject> relatedElements = new HashSet<>();
        if (this.eStructuralFeature.equals(SysmlPackage.eINSTANCE.getRelationship_Target())
                || this.eStructuralFeature.equals(SysmlPackage.eINSTANCE.getRelationship_Source())) {
            // Handle the case where the succession source or target has already been deleted
            if (this.hasBeenAlreadyDeleted(object.getSource()) || this.hasBeenAlreadyDeleted(object.getTarget())) {
                return relatedElements;
            }
            relatedElements.add(object);
            if (object.eContainer() instanceof Membership membership) {
                relatedElements.add(membership);
                if (membership.eContainer() instanceof TransitionUsage transitionUsage) {
                    relatedElements.add(transitionUsage);
                    if (transitionUsage.eContainer() instanceof Membership transitionUsageContainer) {
                        relatedElements.add(transitionUsageContainer);
                    }
                }
            }
        }
        return relatedElements;
    }

    private boolean hasBeenAlreadyDeleted(List<Element> ends) {
        return !ends.isEmpty() && !(ends.get(0) instanceof Membership) && ends.get(0).getOwner() == null;
    }
}
