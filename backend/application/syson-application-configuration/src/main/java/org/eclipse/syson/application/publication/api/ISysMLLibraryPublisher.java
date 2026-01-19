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
package org.eclipse.syson.application.publication.api;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.events.ICause;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;

/**
 * Publishes the proper SysML contents of an {@link IEditingContext} as a {@link Library}.
 *
 * @author flatombe
 */
public interface ISysMLLibraryPublisher {
    IPayload publish(ICause cause, IEditingContext libraryAuthoringEditingContext, String libraryNamespace, String libraryName, String libraryVersion, String libraryDescription);
}
