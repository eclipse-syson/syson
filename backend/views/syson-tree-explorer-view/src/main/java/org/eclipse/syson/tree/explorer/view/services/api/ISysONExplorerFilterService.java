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
package org.eclipse.syson.tree.explorer.view.services.api;

import java.util.List;

/**
 * Services to apply filters on SysON explorer.
 * 
 * @author gdaniel
 */
public interface ISysONExplorerFilterService {

    boolean isKerMLStandardLibrary(Object object);

    boolean isSysMLStandardLibrary(Object object);

    List<Object> hideKerMLStandardLibraries(List<Object> elements);

    List<Object> hideSysMLStandardLibraries(List<Object> elements);

    List<Object> hideMemberships(List<Object> elements);

    List<Object> hideRootNamespace(List<Object> elements);

    List<Object> applyFilters(List<?> elements, List<String> activeFilterIds);
}
