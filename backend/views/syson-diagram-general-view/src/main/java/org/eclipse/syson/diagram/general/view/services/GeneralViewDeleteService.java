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
package org.eclipse.syson.diagram.general.view.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.emf.services.EditingContextCrossReferenceAdapter;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Relationship;

/**
 * Deletion-related Java services used by the {@link GeneralViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class GeneralViewDeleteService {

    /**
     * Delete the given {@link Element} and its container if it's a {@link Membership}. Also delete related
     * {@link Relationship} (e.g. delete {@link Dependency} related to the given {@link Element}.
     *
     * @param element
     *            the {@link Element} to delete.
     * @return the deleted element.
     */
    public EObject deleteFromModel(Element element) {
        Set<EObject> elementsToDelete = new HashSet<>();
        elementsToDelete.add(element);
        if (element.eContainer() instanceof Membership membership) {
            elementsToDelete.add(membership);
        }
        elementsToDelete.addAll(this.collectRelatedElements(element));
        EcoreUtil.removeAll(elementsToDelete);
        return element;
    }

    private Set<EObject> collectRelatedElements(Element element) {
        Set<EObject> relatedElements = new HashSet<>();
        var optAdapter = element.eAdapters().stream().filter(EditingContextCrossReferenceAdapter.class::isInstance).map(EditingContextCrossReferenceAdapter.class::cast).findFirst();
        if (optAdapter.isPresent()) {
            EditingContextCrossReferenceAdapter referenceAdapter = optAdapter.get();
            Collection<Setting> inverseReferences = referenceAdapter.getInverseReferences(element);
            for (Setting setting : inverseReferences) {
                EObject relatedElement = setting.getEObject();
                Set<EObject> collectedElements = new RelatedElementsSwitch(setting.getEStructuralFeature()).doSwitch(relatedElement);
                relatedElements.addAll(collectedElements);
            }
        }
        return relatedElements;
    }
}
