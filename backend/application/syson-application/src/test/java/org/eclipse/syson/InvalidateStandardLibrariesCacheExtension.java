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
package org.eclipse.syson;

import org.eclipse.syson.services.IEditingContextCachingService;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The extension for the {@link InvalidateStandardLibrariesCache} annotation.
 *
 * @see InvalidateStandardLibrariesCache for more information on the annotation
 *
 * @author gdaniel
 */
public class InvalidateStandardLibrariesCacheExtension implements Extension, BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // Invalidate the standard libraries cache before the execution of any annotated test to ensure their editing context are loaded from scratch.
        IEditingContextCachingService editingContextCachingService = SpringExtension.getApplicationContext(context).getBean(IEditingContextCachingService.class);
        editingContextCachingService.invalidate();
    }
}
