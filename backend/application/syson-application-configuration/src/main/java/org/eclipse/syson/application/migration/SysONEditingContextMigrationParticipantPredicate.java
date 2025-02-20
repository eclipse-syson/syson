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
package org.eclipse.syson.application.migration;

import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingContextMigrationParticipantPredicate;
import org.springframework.stereotype.Service;

/**
 * {@link IEditingContextMigrationParticipantPredicate} used to plug the migration mechanism on SysON SysMLv2 projects.
 *
 * @author Arthur Daussy
 */
@Service
public class SysONEditingContextMigrationParticipantPredicate implements IEditingContextMigrationParticipantPredicate {

    @Override
    public boolean test(String editingContextId) {
        // No Sysml/Syson project nature at the time of writing to restrict the number of impacted projects.
        return true;
    }
}
