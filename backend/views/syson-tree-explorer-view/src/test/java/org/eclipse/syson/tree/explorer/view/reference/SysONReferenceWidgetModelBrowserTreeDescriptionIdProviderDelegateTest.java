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

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.web.application.browser.DefaultModelBrowsersTreeDescriptionProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SysONReferenceWidgetModelBrowserTreeDescriptionIdProviderDelegate}.
 *
 * @author arichard
 */
public class SysONReferenceWidgetModelBrowserTreeDescriptionIdProviderDelegateTest {

    /**
     * Verifies that only Reference Widget model browsers are handled.
     */
    @Test
    @DisplayName("GIVEN a model browser identifier, WHEN it is a reference browser, THEN the custom description is selected")
    void handlesReferenceModelBrowsers() {
        var delegate = new SysONReferenceWidgetModelBrowserTreeDescriptionIdProviderDelegate();
        IEditingContext editingContext = null;

        assertThat(delegate.canHandle(editingContext, DefaultModelBrowsersTreeDescriptionProvider.MODEL_BROWSER_REFERENCE_PREFIX + ":identifier")).isTrue();
        assertThat(delegate.canHandle(editingContext, DefaultModelBrowsersTreeDescriptionProvider.MODEL_BROWSER_CONTAINER_PREFIX + ":identifier")).isFalse();
        assertThat(delegate.getModelBrowserTreeDescriptionId(editingContext, "modelBrowser://reference:any"))
                .isEqualTo(SysONReferenceWidgetModelBrowserTreeDescriptionProvider.DESCRIPTION_ID);
    }
}
