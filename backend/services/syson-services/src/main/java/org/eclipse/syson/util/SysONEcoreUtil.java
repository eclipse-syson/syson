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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;

/**
 * SysON specialization of {@link EcoreUtil} class coming from org.eclipse.emf.ecore. The specialization allows to have
 * a specific version of deleteAll/removeAll that do not browse all derived references. In SysON, browsing all derived
 * references of all objects in the model takes a very long time and is useless. Indeed, most of metamodel references
 * are derived and their computation is complex. We don't know why derived references are browsed in the original
 * implementation.
 *
 * @author arichard
 */
public class SysONEcoreUtil extends EcoreUtil {

    /**
     * Deletes each object from its {@link EObject#eResource containing} resource and/or its {@link EObject#eContainer
     * containing} object as well as from any other feature that references it within the enclosing resource set,
     * resource, or root object of any of the objects. If recursive true, contained children of the object that are in
     * the same resource are similarly removed from any features that reference them.
     *
     * @param eObjects
     *            the objects to delete.
     * @see #delete(EObject, boolean)
     * @param recursive
     *            whether references to contained children should also be removed.
     */
    public static void deleteAll(Collection<? extends EObject> eObjects, boolean recursive) {
        // Get objects to remove and all their descendants.
        Set<Notifier> roots = new HashSet<>();
        Set<EObject> eAllObjects = new HashSet<>(eObjects);
        Set<EObject> crossResourceEObjects = new HashSet<>();
        for (EObject eObject : eObjects) {
            roots.add(getRoot(eObject, true));

            if (recursive) {
                TreeIterator<EObject> eAllContents = eObject.eAllContents();
                for (TreeIterator<EObject> j = eAllContents; j.hasNext(); ) {
                    InternalEObject childEObject = (InternalEObject) j.next();
                    if (childEObject.eDirectResource() != null) {
                        crossResourceEObjects.add(childEObject);
                        j.prune();
                    } else {
                        eAllObjects.add(childEObject);
                    }
                }
            }
        }

        // Find usages.
        Map<EObject, Collection<EStructuralFeature.Setting>> usages = NoDerivedCrossReferencer.findAll(eAllObjects, roots);

        // Remove all usages.
        for (Map.Entry<EObject, Collection<EStructuralFeature.Setting>> entry : usages.entrySet()) {
            EObject deletedEObject = entry.getKey();
            Collection<EStructuralFeature.Setting> settings = entry.getValue();
            for (EStructuralFeature.Setting setting : settings) {
                if (!eAllObjects.contains(setting.getEObject()) && setting.getEStructuralFeature().isChangeable()) {
                    EcoreUtil.remove(setting, deletedEObject);
                }
            }
        }

        // Remove all objects.
        removeAll(eObjects);

        // Disconnect all cross resource objects.
        for (EObject crossResourceEObject : crossResourceEObjects) {
            EcoreUtil.remove(crossResourceEObject.eContainer(), crossResourceEObject.eContainmentFeature(), crossResourceEObject);
        }
    }

    /**
     * Removes the values from the setting.
     *
     * @param setting
     *            the setting holding the value.
     * @param values
     *            the values to remove.
     */
    public static void removeAll(EStructuralFeature.Setting setting, Collection<?> values) {
        EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
        if (eStructuralFeature != null && !eStructuralFeature.isDerived()) {
            if (FeatureMapUtil.isMany(setting.getEObject(), eStructuralFeature)) {
                ((List<?>) setting.get(false)).removeAll(values);
            } else {
                // The feature is assumed to hold the value.
                setting.unset();
            }
        }
    }
}
