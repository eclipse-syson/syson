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

package org.eclipse.syson.application.sysmlv2.api;

import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Provides the content of a default SysMLv2 resource.
 *
 * @author gcoutable
 */
public interface IDefaultSysMLv2ResourceProvider {
    Resource getEmptyResource(UUID resourcePath, String name);

    Resource getDefaultSysMLv2Resource(UUID resourcePath, String name);

    void loadBatmobileResource(Resource resource);
}
