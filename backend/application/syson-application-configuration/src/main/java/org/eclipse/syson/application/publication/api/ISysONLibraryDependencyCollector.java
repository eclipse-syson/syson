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
package org.eclipse.syson.application.publication.api;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.web.application.studio.services.library.api.DependencyGraph;

/**
 * Collects the dependencies between SysON {@link Resource}.
 *
 * @author gdaniel
 */
public interface ISysONLibraryDependencyCollector {

    DependencyGraph<Resource> collectDependencies(ResourceSet resourceSet);

}
