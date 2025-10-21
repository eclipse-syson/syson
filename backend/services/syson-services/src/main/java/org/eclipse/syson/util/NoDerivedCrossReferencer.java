/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.eclipse.emf.ecore.util.EContentsEList.FeatureIterator;
import org.eclipse.emf.ecore.util.ECrossReferenceEList;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * Specialized {@link UsageCrossReferencer} that do not browse derived references. In the original implementation in
 * EMF, an eIsSet is done on all references while browsing them. eIsSet does a getXXX, and in case of derived
 * references, it comes down to compute them.
 *
 * @author arichard
 */
public class NoDerivedCrossReferencer extends UsageCrossReferencer {

    private static final long serialVersionUID = 1L;

    protected NoDerivedCrossReferencer(EObject eObject) {
        super(eObject);
    }

    protected NoDerivedCrossReferencer(Collection<?> emfObjects) {
        super(emfObjects);
    }

    public static Map<EObject, Collection<EStructuralFeature.Setting>> findAll(Collection<?> eObjectsOfInterest, Collection<?> emfObjectsToSearch) {
        return new NoDerivedCrossReferencer(emfObjectsToSearch).findAllUsage(eObjectsOfInterest);
    }

    @Override
    protected boolean crossReference(EObject eObject, EReference eReference, EObject crossReferencedEObject) {
        // Skip derived references to avoid unnecessary computation
        if (!eReference.isDerived()) {
            return super.crossReference(eObject, eReference, crossReferencedEObject);
        }
        return false;
    }

    @Override
    protected FeatureIterator<EObject> getCrossReferences(EObject eObject) {
        if (this.resolve()) {
            EList<EObject> eCrossReferences = this.createECrossReferenceEList(eObject);
            return (EContentsEList.FeatureIterator<EObject>) eCrossReferences.iterator();
        } else {
            InternalEList<EObject> eCrossReferences = this.createECrossReferenceEList(eObject);
            return (EContentsEList.FeatureIterator<EObject>) eCrossReferences.basicIterator();
        }
    }

    public <T> ECrossReferenceEList<T> createECrossReferenceEList(EObject eObject) {
        EList<EStructuralFeature> eAllStructuralFeatures = eObject.eClass().getEAllStructuralFeatures();
        EStructuralFeature[] eStructuralFeatures = ((EClassImpl.FeatureSubsetSupplier) eAllStructuralFeatures).crossReferences();
        var nonDerivedStructuralFeatures = Arrays.stream(eStructuralFeatures).filter(eSF -> !eSF.isDerived()).toArray(EStructuralFeature[]::new);
        if (nonDerivedStructuralFeatures == null) {
            return ECrossReferenceEList.<T> emptyCrossReferenceEList();
        } else {
            return new ECrossReferenceEList<>(eObject, nonDerivedStructuralFeatures) {
            };
        }
    }
}
