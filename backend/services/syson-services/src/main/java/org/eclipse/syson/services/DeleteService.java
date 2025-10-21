/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.util.SysONEcoreUtil;

/**
 * Deletion-related Java services used by SysON representations.
 *
 * @author arichard
 */
public class DeleteService {

    /**
     * Delete the given {@link Element} and its container if it's a {@link Membership}. Also delete related
     * {@link Relationship} (e.g. delete {@link Dependency} related to the given {@link Element}.
     *
     * @param element
     *            the {@link Element} to delete.
     * @return the deleted element.
     */
    public EObject deleteFromModel(Element element) {
        Set<EObject> elementsToDelete = new LinkedHashSet<>();
        Set<EObject> relatedElements = new HashSet<>();
        if (element.eContainer() instanceof Membership membership) {
            elementsToDelete.add(membership);
            this.collectRelatedElements(membership, relatedElements);
        } else {
            elementsToDelete.add(element);
        }
        this.collectRelatedElements(element, relatedElements);
        element.eAllContents().forEachRemaining(eObject -> this.collectRelatedElements(eObject, relatedElements));
        elementsToDelete.addAll(relatedElements);
        SysONEcoreUtil.deleteAll(elementsToDelete, true);
        return element;
    }

    private void collectRelatedElements(EObject eObject, Set<EObject> relatedElements) {
        var referenceAdapter = ECrossReferenceAdapter.getCrossReferenceAdapter(eObject);
        if (referenceAdapter != null) {
            Collection<Setting> inverseReferences = referenceAdapter.getInverseReferences(eObject);
            for (Setting setting : inverseReferences) {
                EObject relatedElement = setting.getEObject();
                Set<EObject> collectedElements = new RelatedElementsSwitch(setting.getEStructuralFeature()).doSwitch(relatedElement);
                for (EObject collectedElement : collectedElements) {
                    boolean notAlreadyContained = relatedElements.add(collectedElement);
                    if (notAlreadyContained) {
                        this.collectRelatedElements(collectedElement, relatedElements);
                    }
                }
            }
        }
    }
}
