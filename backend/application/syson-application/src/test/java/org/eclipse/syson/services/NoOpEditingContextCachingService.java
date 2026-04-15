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
package org.eclipse.syson.services;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Service;

/**
 * Default {@link IEditingContextCachingService} that does nothing.
 * <p>
 * This service is used when the property {@code org.eclipse.syson.test.cacheStandardLibraries} is not set or set to {@code false}. It allows to keep the code as-is, but doesn't perform actual
 * caching.
 * </p>
 *
 * @author gdaniel
 */
@Service
@ConditionalOnBooleanProperty(value = "org.eclipse.syson.test.cacheStandardLibraries", havingValue = false, matchIfMissing = true)
public class NoOpEditingContextCachingService implements IEditingContextCachingService {

    @Override
    public void cache() {
    }

    @Override
    public void invalidate() {
    }
}
