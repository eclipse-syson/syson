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

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContext;

/**
 * A fragment displayed in the SysON explorer.
 * <p>
 * A fragment is a <i>virtual</i> element that is not present as-is in a {@link Resource}s manipulated by the explorer.
 * </p>
 *
 * @author gdaniel
 */
public interface ISysONExplorerFragment {

    String getId();

    String getLabel();

    List<String> getIconURL();

    boolean hasChildren(IEditingContext editingContext, List<String> expandedIds, List<String> activeFilterIds);

    List<Object> getChildren(IEditingContext editingContext, List<String> expandedIds, List<String> activeFilterIds);

    boolean isEditable();

    boolean isDeletable();

    boolean isSelectable();

}
