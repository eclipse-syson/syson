/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.application.sysmlv2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.collaborative.api.IEditingContextActionProvider;
import org.eclipse.sirius.components.collaborative.dto.EditingContextAction;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.springframework.stereotype.Service;

/**
 * Provides the list of possible actions on the editingContext.
 *
 * @author arichard
 */
@Service
public class SysMLv2EditingContextActionProvider implements IEditingContextActionProvider {

    private static final EditingContextAction EMPTY_SYSML_EDITING_CONTEXT_ACTION = new EditingContextAction(SysMLv2StereotypeProvider.EMPTY_SYSML_ID, SysMLv2StereotypeProvider.EMPTY_SYSML_LABEL);

    private static final EditingContextAction EMPTY_SYSML_LIBRARY_EDITING_CONTEXT_ACTION = new EditingContextAction(SysMLv2StereotypeProvider.EMPTY_SYSML_LIBRARY_ID,
            SysMLv2StereotypeProvider.EMPTY_SYSML_LIBRARY_LABEL);

    @Override
    public List<EditingContextAction> getEditingContextAction(IEditingContext editingContext) {
        var actions = new ArrayList<EditingContextAction>();
        if (editingContext instanceof IEMFEditingContext) {
            actions.add(EMPTY_SYSML_EDITING_CONTEXT_ACTION);
            actions.add(EMPTY_SYSML_LIBRARY_EDITING_CONTEXT_ACTION);
        }
        return actions;
    }
}
