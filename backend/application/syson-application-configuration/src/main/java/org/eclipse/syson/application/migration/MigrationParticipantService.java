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
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Migration participant-related services.
 *
 * @author arichard
 */
public class MigrationParticipantService {

    public boolean isSysMLResource(JsonObject jsonObject) {
        JsonElement nsElements = jsonObject.get("ns");
        if (nsElements instanceof JsonObject nsElementObjects) {
            for (String currentKey : nsElementObjects.keySet()) {
                JsonPrimitive nsURI = nsElementObjects.getAsJsonPrimitive(currentKey);
                return SysmlPackage.eNS_URI.equals(nsURI.getAsString());
            }
        }
        return false;
    }

    public List<JsonObject> collectElementsOfType(JsonObject jsonObject, EClass eClass) {
        List<JsonObject> elementsOfType = new ArrayList<>();
        this.collectElementsOfType(jsonObject, eClass, elementsOfType);
        return elementsOfType;
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
