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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.core.api.ILabelServiceDelegate;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFragment;
import org.springframework.stereotype.Service;

/**
 * Label service for SelectionDialog, to handle ISysONExplorerFragment elements.
 *
 * @author arichard
 */
@Service
public class SysONSelectionDialogFragmentLabelService implements ILabelServiceDelegate {

    @Override
    public boolean canHandle(Object object) {
        return object instanceof ISysONExplorerFragment;
    }

    @Override
    public StyledString getStyledLabel(Object object) {
        String label = "";
        if (object instanceof ISysONExplorerFragment fragment) {
            label = fragment.getLabel();
        }
        return StyledString.of(label);
    }

    @Override
    public List<String> getImagePaths(Object object) {
        List<String> imagePaths = new ArrayList<>();
        if (object instanceof ISysONExplorerFragment fragment) {
            imagePaths = fragment.getIconURL();
        }
        return imagePaths;
    }

}
