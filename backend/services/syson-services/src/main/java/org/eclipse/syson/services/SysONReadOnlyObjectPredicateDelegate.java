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
package org.eclipse.syson.services;

import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.web.application.object.services.api.IReadOnlyObjectPredicateDelegate;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * {@link IReadOnlyObjectPredicateDelegate} implementation for SysON.
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
        return candidate instanceof Element;
    }

    @Override
    public boolean test(Object candidate) {
        if (candidate instanceof Element element) {
            final Resource resource = element.eResource();
            return ElementUtil.isStandardLibraryResource(resource)
                    || (resource != null && this.sysONResourceService.isImported(resource) && !new UtilService().getLibraries(resource, false).isEmpty());
        } else {
            throw new IllegalArgumentException("Unsupported type: '%s'. Expected: '%s'.".formatted(candidate.getClass().getCanonicalName(), Element.class.getCanonicalName()));
        }
    }

}
