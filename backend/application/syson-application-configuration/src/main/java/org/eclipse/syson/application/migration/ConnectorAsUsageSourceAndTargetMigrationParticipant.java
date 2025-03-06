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
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * Migration participant used to migrate SysON SysMLv2 ConnectorAsUsage elements previous to 2025.4.0.
 *
 * <p>
 * It is intended to overcome the previous implementation that serialized data in <i>source</i> and <i>target</i>
 * features of {@link ConnectorAsUsage} whereas now those features are derived and non editable. So we don't want to
 * deserialize them. See detail at https://github.com/eclipse-syson/syson/issues/1030
 * </p>
 *
 * @author Arthur Daussy
 */
@Service
public class ConnectorAsUsageSourceAndTargetMigrationParticipant implements IMigrationParticipant {

    private static final String PARTICIPANT_VERSION = "2025.4.0-202502270000";

    private final MigrationParticipantService migrationParticipantService = new MigrationParticipantService();

    @Override
    public String getVersion() {
        return PARTICIPANT_VERSION;
    }

    @Override
    public void preDeserialization(JsonResource resource, JsonObject jsonObject) {
        if (this.migrationParticipantService.isSysMLResource(jsonObject)) {
            this.migrationParticipantService.collectElementsOfType(jsonObject, SysmlPackage.eINSTANCE.getConnectorAsUsage()).forEach(successionNode -> {
                successionNode.remove("source");
                successionNode.remove("target");
            });
        }
    }
}
