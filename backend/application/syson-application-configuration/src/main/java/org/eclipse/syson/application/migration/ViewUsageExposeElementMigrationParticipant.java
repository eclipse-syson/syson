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

import com.google.gson.JsonObject;

import org.eclipse.sirius.components.emf.migration.api.IMigrationParticipant;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * Migration participant used to migrate SysON SysMLv2 ViewUsage#exposedElement feature previous to 2025.4.8.
 *
 * <p>
 * We first decided to move the ViewUsage#exposedElement feature from derived to non-derived. Now, with a better
 * understanding of the specification, it appears that this feature should be derived. All existing serialized models
 * that have ViewUsage#exposeElement should be processed to avoid errors while deserializing. Indeed we don't want to
 * deserialize this feature anymore. See detail at https://github.com/eclipse-syson/syson/issues/1310
 * </p>
 *
 * @author Arthur Daussy
 */
@Service
public class ViewUsageExposeElementMigrationParticipant implements IMigrationParticipant {

    private static final String PARTICIPANT_VERSION = "2025.6.0-202506110000";

    private final MigrationParticipantService migrationParticipantService = new MigrationParticipantService();

    @Override
    public String getVersion() {
        return PARTICIPANT_VERSION;
    }

    @Override
    public void preDeserialization(JsonResource resource, JsonObject jsonObject) {
        if (this.migrationParticipantService.isSysMLResource(jsonObject)) {
            this.migrationParticipantService.collectElementsOfType(jsonObject, SysmlPackage.eINSTANCE.getViewUsage()).forEach(viewUsage -> {
                viewUsage.remove("exposedElement");
            });
        }
    }
}
