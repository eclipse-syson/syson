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
package org.eclipse.syson.tree.explorer.view.services;

import java.util.Objects;

import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerLabelServiceDelegate;
import org.eclipse.syson.sysml.Element;
import org.springframework.stereotype.Service;

/**
 * Used to provide the behavior of the SysON Explorer view for {@link Element}.
 *
 * @author arichard
 */
@Service
public class SysONExplorerLabelService implements IExplorerLabelServiceDelegate {

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    public SysONExplorerLabelService(IReadOnlyObjectPredicate readOnlyObjectPredicate) {
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof Element;
    }

    @Override
    public boolean isEditable(Object self) {
        boolean editable = false;
        if (self instanceof Element && !this.readOnlyObjectPredicate.test(self)) {
            editable = true;
        }
        return editable;
    }

    @Override
    public void editLabel(Object self, String newValue) {
        if (self instanceof Element element && !this.readOnlyObjectPredicate.test(self)) {
            element.setDeclaredName(newValue);
        }
    }
}
