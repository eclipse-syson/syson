/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml.export;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingContextPersistenceFilter;
import org.springframework.stereotype.Service;

/**
 * Used to ensure that the standard libraries are never exported in project downloads.
 *
 * @author arichard
 */
@Service
public class SysMLv2EditingContextPersistenceFilter implements IEditingContextPersistenceFilter {

    @Override
    public boolean shouldPersist(Resource resource) {
        boolean shouldPersist = true;
        String resourceURI = resource.getURI().toString();
        if (resourceURI.startsWith("kermllibrary") || resourceURI.startsWith("sysmllibrary")) {
            shouldPersist = false;
        }
        return shouldPersist;
    }

}
