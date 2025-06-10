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
package org.eclipse.syson.application.services;

import java.util.Objects;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.web.application.object.services.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.web.application.object.services.api.IReadOnlyObjectPredicateDelegate;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * {@link IReadOnlyObjectPredicateDelegate} implementation for SysON.
 * <p>
 * It ensures that a {@link Resource}, an {@link EAnnotation} or a {@link Element SysML element} is considered read-only
 * by the {@link IReadOnlyObjectPredicate} implementation (used for the <i>Explorer</i> and <i>Details</i> views) when
 * it is (or belongs to) a {@link Resource} matching at least one of the following criterias:
 * <ul>
 * <li>From the SysML and KerML standard libraries</li>
 * <li>From an {@link org.eclipse.syson.services.api.ISysONResourceService#isImported(Resource) imported resource}
 * containing {@link org.eclipse.syson.sysml.LibraryPackage libraries}.</li>
 * </ul>
 *
 * @author flatombe
 */
@Service
public class SysONReadOnlyObjectPredicateDelegate implements IReadOnlyObjectPredicateDelegate {

    private final ISysONResourceService sysONResourceService;

    public SysONReadOnlyObjectPredicateDelegate(final ISysONResourceService sysONResourceService) {
        this.sysONResourceService = Objects.requireNonNull(sysONResourceService);
    }

    @Override
    public boolean canHandle(Object candidate) {
        return candidate instanceof Element || candidate instanceof Resource || candidate instanceof EAnnotation;
    }

    @Override
    public boolean test(Object candidate) {
        // In general, elements are not read-only.
        boolean isReadOnly = false;

        if (candidate instanceof Resource resource) {
            isReadOnly = ElementUtil.isStandardLibraryResource(resource)
                    || this.sysONResourceService.isImported(resource) && !new UtilService().getLibraries(resource, false).isEmpty();
        } else if (candidate instanceof Element || candidate instanceof EAnnotation) {
            // Derive editability from the editability of the containing resource.
            final EObject eObject = (EObject) candidate;
            final Resource resource = eObject.eResource();
            isReadOnly = this.test(resource);
        }

        return isReadOnly;
    }

}
