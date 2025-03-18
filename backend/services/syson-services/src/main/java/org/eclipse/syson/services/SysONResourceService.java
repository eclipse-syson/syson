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
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.syson.sysml.util.ElementUtil;

/**
 * Services related to SysON and its {@link Resource}.
 *
 * @author flatombe
 */
public class SysONResourceService {

    public static boolean isImported(final Resource resource) {
        Objects.requireNonNull(resource);

        return ElementUtil.isImported(resource) ||
                isFromReferencedLibrary(resource);
    }

    public static boolean isFromReferencedLibrary(final Resource resource) {
        Objects.requireNonNull(resource);

        return resource.eAdapters().stream().anyMatch(LibraryMetadataAdapter.class::isInstance);
    }

}
