/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.application.configuration;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.web.application.project.services.api.IRewriteProxiesResourceFilter;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * IRewriteProxiesResourceFilter to filter SysMLv2 standard libraries.
 *
 * @author arichard
 */
@Service
public class SysMLv2RewriteProxiesResourcesFilter implements IRewriteProxiesResourceFilter {

    @Override
    public boolean shouldRewriteProxies(Resource resource) {
        boolean filter = true;
        if (resource == null) {
            filter = false;
        } else {
            filter = !ElementUtil.isStandardLibraryResource(resource);
        }
        return filter;
    }
}
