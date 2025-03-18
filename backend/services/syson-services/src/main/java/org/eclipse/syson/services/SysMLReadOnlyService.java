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

import org.eclipse.syson.services.api.ISysMLReadOnlyService;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ISysMLReadOnlyService}.
 *
 * @author Arthur Daussy
 */
@Service
public class SysMLReadOnlyService implements ISysMLReadOnlyService {

    private final ISysONResourceService sysONResourceService;

    public SysMLReadOnlyService(final ISysONResourceService sysONResourceService) {
        this.sysONResourceService = Objects.requireNonNull(sysONResourceService);
    }

    @Override
    public boolean isReadOnly(Element element) {
        return this.isStandardLibrary(element) || this.isReadOnlyLibraryElement(element);
    }

    private boolean isStandardLibrary(Element element) {
        if (element != null) {
            return ElementUtil.isStandardLibraryResource(element.eResource());
        }
        return false;
    }

    private boolean isReadOnlyLibraryElement(Element element) {
        return EMFUtils.getFirstAncestor(LibraryPackage.class, element, null)
                .map(lib -> lib.isIsStandard() || this.sysONResourceService.isImported(lib.eResource()))
                .orElse(false);
    }
}
