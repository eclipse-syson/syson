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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.sirius.components.emf.migration.api.IMigrationParticipant;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * Migration participant used to migrate SysON SysMLv2 models previous to 2025.4.0.
 *
 * <p>
 * It is intended to overcome the previous implementation that serialized data in <i>source</i> and <i>target</i>
 * features of {@link SuccessionAsUsage} whereas now those features are derived and non editable. See detail at
 * https://github.com/eclipse-syson/syson/issues/1030
 * </p>
 *
 * @author Arthur Daussy
 */
@Service
public class SuccessionAsUsageMigrationParticipant implements IMigrationParticipant {

    private static final String PARTICIPANT_VERSION = "2025.4.0-202502270000";

    @Override
    public String getVersion() {
        return PARTICIPANT_VERSION;
    }

    @Override
    public void preDeserialization(JsonResource resource, JsonObject jsonObject) {
        if (this.isSysMLResource(jsonObject)) {
            this.collectElementsOfType(jsonObject, SysmlPackage.eINSTANCE.getConnectorAsUsage()).forEach(successionNode -> {
                successionNode.remove("source");
                successionNode.remove("target");
            });
        }
    }

    private boolean isSysMLResource(JsonObject jsonObject) {
        JsonElement nsElements = jsonObject.get("ns");
        if (nsElements instanceof JsonObject nsElementObjects) {
            for (String currentKey : nsElementObjects.keySet()) {
                JsonPrimitive nsURI = nsElementObjects.getAsJsonPrimitive(currentKey);
                return SysmlPackage.eNS_URI.equals(nsURI.getAsString());
            }
        }
        return false;
    }

    private List<JsonObject> collectElementsOfType(JsonObject jsonObject, EClass eClass) {
        List<JsonObject> successionAsUsages = new ArrayList<>();
        this.collectElementsOfType(jsonObject, eClass, successionAsUsages);
        return successionAsUsages;
    }

    private void collectElementsOfType(JsonObject jsonObject, EClass eClass, List<JsonObject> accumulator) {
        for (String currentKey : jsonObject.keySet()) {
            Object value = jsonObject.get(currentKey);

            if (value instanceof JsonObject child) {
                if (this.isInstanceOf(jsonObject, eClass)) {
                    accumulator.add(child);
                }
                this.collectElementsOfType(child, eClass, accumulator);
            } else if (value instanceof JsonArray jsonArray) {
                this.collectElementsOfTypeInArray(jsonArray, eClass, accumulator);
            }
        }
    }

    private boolean isInstanceOf(JsonObject child, EClass expectedEClass) {
        JsonElement eClassQn = child.get("eClass");
        if (eClassQn instanceof JsonPrimitive jsonPrimitive) {
            String eClassQualifedName = jsonPrimitive.getAsString();
            if (eClassQualifedName != null && eClassQualifedName.startsWith("sysml:")) {
                EClassifier objectEClassier = SysmlPackage.eINSTANCE.getEClassifier(eClassQualifedName.replaceFirst("sysml:", ""));
                if (objectEClassier instanceof EClass objectEClass && expectedEClass.isSuperTypeOf(objectEClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void collectElementsOfTypeInArray(JsonArray jsonArray, EClass eClass, List<JsonObject> accumulator) {
        for (Object obj : jsonArray) {
            if (obj instanceof JsonArray) {
                this.collectElementsOfTypeInArray((JsonArray) obj, eClass, accumulator);
            } else if (obj instanceof JsonObject) {
                this.collectElementsOfType((JsonObject) obj, eClass, accumulator);
            }
        }
    }
}
