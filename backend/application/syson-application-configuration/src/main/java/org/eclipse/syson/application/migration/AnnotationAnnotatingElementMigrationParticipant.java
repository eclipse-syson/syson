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
 * Migration participant used to migrate SysON SysMLv2 Annotation elements previous to 2025.4.0.
 *
 * Annotation#annotatingElement was not derived prior to 2025.4.0. Also apply to the opposite relation
 * AnnotatingElement#annotation that was not derived either prior to 2025.4.0.
 *
 * @author arichard
 */
@Service
public class AnnotationAnnotatingElementMigrationParticipant implements IMigrationParticipant {

    private static final String PARTICIPANT_VERSION = "2025.4.0-202503140000";

    private final MigrationParticipantService migrationParticipantService = new MigrationParticipantService();

    @Override
    public String getVersion() {
        return PARTICIPANT_VERSION;
    }

    @Override
    public void preDeserialization(JsonResource resource, JsonObject jsonObject) {
        if (this.migrationParticipantService.isSysMLResource(jsonObject)) {
            this.migrationParticipantService.collectElementsOfType(jsonObject, SysmlPackage.eINSTANCE.getAnnotation()).forEach(annotation -> {
                annotation.remove("annotatingElement");
            });
            this.migrationParticipantService.collectElementsOfType(jsonObject, SysmlPackage.eINSTANCE.getAnnotatingElement()).forEach(annotatingElement -> {
                annotatingElement.remove("annotation");
            });
        }
    }
}
