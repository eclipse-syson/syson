/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.model.services;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewUsage;
import org.springframework.stereotype.Service;

/**
 * Element-related services doing queries in models.
 *
 * @author arichard
 */
@Service
public class ModelQueryElementService {

    private final UtilService utilService;

    public ModelQueryElementService() {
        this.utilService = new UtilService();
    }

    /**
     * Check is the given element can be added to the exposed elements of a ViewUsage.
     *
     * @param element
     *            the given {@link Element}.
     * @return <code>true</code> if the given element can be added to the exposed elements of a ViewUsage,
     *         <code>false</code> otherwise.
     */
    public boolean isExposable(Element element) {
        boolean isExposable = false;
        if (this.utilService.isStandardDoneAction(element) || this.utilService.isStandardStartAction(element)) {
            isExposable = false;
        } else {
            isExposable = this.hasName(element);
        }
        return isExposable;
    }

    /**
     * Check is the given Element is exposed by the given ViewUsage.
     *
     * @param element
     *            the given {@link Element}.
     * @param viewUsage
     *            the given {@link ViewUsage}.
     * @return <code>true</code> if the given element is exposed by the given ViewUsage, <code>false</code> otherwise.
     */
    public boolean isExposed(Element element, ViewUsage viewUsage) {
        return viewUsage != null && viewUsage.getExposedElement().contains(element);
    }

    /**
     * Get all reachable allocate edges from the resource set containing the given object.
     *
     * @param eObject
     *            an object stored in a resource set
     * @return the reachable allocate edges
     */
    public List<AllocationUsage> getAllReachableAllocateEdges(EObject eObject) {
        return this.utilService.getAllReachableType(eObject, SysmlPackage.eINSTANCE.getAllocationUsage()).stream()
                .filter(AllocationUsage.class::isInstance)
                .map(AllocationUsage.class::cast)
                .filter(this::isAllocateEdge)
                .toList();
    }

    /**
     * Checks whether the target of a transition reconnection is valid.
     *
     * @param source
     *            the source element
     * @param target
     *            the target element
     * @return {@code true} if the target is valid
     */
    public boolean checkTransitionEdgeTarget(Element source, Element target) {
        boolean sameParent = false;
        if (source instanceof ActionUsage sourceAction && target instanceof ActionUsage targetAction) {
            sameParent = sourceAction.getOwner() == targetAction.getOwner();
        }
        boolean parentIsParallel = this.utilService.isParallelState(source.getOwner());
        return sameParent && !parentIsParallel;
    }

    private boolean hasName(Element element) {
        String dName = element.getName();
        if (dName != null && !dName.isBlank()) {
            return true;
        }
        String sName = element.getShortName();
        return sName != null && !sName.isBlank();
    }

    private boolean isAllocateEdge(AllocationUsage allocationUsage) {
        return allocationUsage.getOwnedFeatureMembership().stream()
                .filter(EndFeatureMembership.class::isInstance)
                .limit(3)
                .count() == 2;
    }
}
