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

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * The default SysON implementation for {@link ISysONResourceService}.
 *
 * @author flatombe
 */
@Service
public class SysONResourceService implements ISysONResourceService {

    @Override
    public boolean isImported(final Resource resource) {
        Objects.requireNonNull(resource);

        return ElementUtil.isImported(resource) ||
                this.isFromReferencedLibrary(resource);
    }

    @Override
    public boolean isFromReferencedLibrary(final Resource resource) {
        Objects.requireNonNull(resource);

        return resource.eAdapters().stream().anyMatch(LibraryMetadataAdapter.class::isInstance);
    }

}
