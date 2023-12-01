/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.application.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.collaborative.api.IEditingContextActionProvider;
import org.eclipse.sirius.components.collaborative.dto.EditingContextAction;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.services.EditingContext;
import org.springframework.stereotype.Service;

/**
 * Provides the list of possible actions on the editingContext.
 *
 * @author frouene
 */
@Service
public class EditingContextActionProvider implements IEditingContextActionProvider {

    public static final String EMPTY_ACTION_ID = "empty";

    public static final String EMPTY_SYSML_ID = "empty_sysml";

    private static final EditingContextAction EMPTY_EDITING_CONTEXT_ACTION = new EditingContextAction(EMPTY_ACTION_ID, "Others...");

    private static final EditingContextAction EMPTY_FLOW_EDITING_CONTEXT_ACTION = new EditingContextAction(EMPTY_SYSML_ID, "SysMLv2");

    @Override
    public List<EditingContextAction> getEditingContextAction(IEditingContext editingContext) {
        var actions = new ArrayList<EditingContextAction>();
        if (editingContext instanceof EditingContext emfEditingContext) {
            actions.add(EMPTY_FLOW_EDITING_CONTEXT_ACTION);
            actions.add(EMPTY_EDITING_CONTEXT_ACTION);
        }
        return actions;
    }
}
