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
package org.eclipse.syson.tree.selectiondialog.services;

import org.eclipse.sirius.components.core.api.IIdentityServiceDelegate;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFragment;
import org.springframework.stereotype.Service;

/**
 * Identity service for SelectionDialog, to handle ISysONExplorerFragment elements.
 *
 * @author arichard
 */
@Service
public class SysONSelectionDialogFragmentIdentityService implements IIdentityServiceDelegate {

    @Override
    public boolean canHandle(Object object) {
        return object instanceof ISysONExplorerFragment;
    }

    @Override
    public String getId(Object object) {
        String id = null;
        if (object instanceof ISysONExplorerFragment fragment) {
            id = fragment.getId();
        }
        return id;
    }

    @Override
    public String getKind(Object object) {
        String kind = null;
        if (object instanceof ISysONExplorerFragment fragment) {
            kind = fragment.getKind();
        }
        return kind;
    }

}
