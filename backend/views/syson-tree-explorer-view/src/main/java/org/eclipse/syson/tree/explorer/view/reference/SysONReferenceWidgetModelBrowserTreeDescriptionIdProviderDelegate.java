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
package org.eclipse.syson.tree.explorer.view.reference;

import org.eclipse.sirius.components.collaborative.browser.api.IModelBrowserTreeDescriptionIdProviderDelegate;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.web.application.browser.DefaultModelBrowsersTreeDescriptionProvider;
import org.springframework.stereotype.Service;

/**
 * Routes every Reference Widget model browser to SysON's filtered tree description.
 *
 * @author arichard
 */
@Service
public class SysONReferenceWidgetModelBrowserTreeDescriptionIdProviderDelegate implements IModelBrowserTreeDescriptionIdProviderDelegate {

    @Override
    public boolean canHandle(IEditingContext editingContext, String modelBrowserId) {
        return modelBrowserId.startsWith(DefaultModelBrowsersTreeDescriptionProvider.MODEL_BROWSER_REFERENCE_PREFIX);
    }

    @Override
    public String getModelBrowserTreeDescriptionId(IEditingContext editingContext, String modelBrowserId) {
        return SysONReferenceWidgetModelBrowserTreeDescriptionProvider.DESCRIPTION_ID;
    }
}
