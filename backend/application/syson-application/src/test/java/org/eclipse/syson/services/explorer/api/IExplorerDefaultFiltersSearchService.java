/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.services.explorer.api;

import java.util.List;

/**
 * Finds the default filters of a given explorer.
 *
 * @author gdaniel
 */
public interface IExplorerDefaultFiltersSearchService {

    List<String> findTreeDefaultFilterIds(String editingContextId, String treeDescriptionId);
}
