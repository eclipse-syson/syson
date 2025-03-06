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
 * Migration participant used to migrate SysON SysMLv2 Feature elements previous to Beta 2.4. Feature#valuation does not
 * exists anymore in Beta 2.4. EMF could probably handle this without migration participant, but this migration
 * participant allows to handle it explicitly.
 *
 * @author arichard
 */
@Service
public class FeatureValuationMigrationParticipant implements IMigrationParticipant {

    private static final String PARTICIPANT_VERSION = "2025.4.0-202503100000";

    private final MigrationParticipantService migrationParticipantService = new MigrationParticipantService();

    @Override
    public String getVersion() {
        return PARTICIPANT_VERSION;
    }

    @Override
    public void preDeserialization(JsonResource resource, JsonObject jsonObject) {
        if (this.migrationParticipantService.isSysMLResource(jsonObject)) {
            this.migrationParticipantService.collectElementsOfType(jsonObject, SysmlPackage.eINSTANCE.getFeature()).forEach(featureNode -> {
                featureNode.remove("valuation");
            });
        }
    }
}
