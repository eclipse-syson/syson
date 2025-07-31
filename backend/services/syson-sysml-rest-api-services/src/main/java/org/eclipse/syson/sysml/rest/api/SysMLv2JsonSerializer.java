/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.sysml.rest.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.emf.services.EObjectIDManager;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.sysml.util.VirtualLinkAdapter;

/**
 * Custom JSON Serializer for SysMLv2 Elements.
 *
 * @author arichard
 */
public class SysMLv2JsonSerializer extends JsonSerializer<EObject> {

    private static final String ID = "@id";

    private final EObjectIDManager eObjectIDManager = new EObjectIDManager();

    @Override
    public void serialize(EObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        Optional<String> id = Optional.empty();
        if (value instanceof Element elt) {
            id = Optional.of(elt.getElementId());
        } else {
            id = this.eObjectIDManager.findId(value);
        }
        if (id.isPresent()) {
            gen.writeStringField(ID, id.get());
        }
        gen.writeStringField("@type", value.eClass().getName());
        EList<EAttribute> eAllAttributes = value.eClass().getEAllAttributes();
        for (EAttribute eAttribute : eAllAttributes) {
            Object objectValue = value.eGet(eAttribute);
            if (objectValue != null) {
                gen.writeStringField(eAttribute.getName(), objectValue.toString());
            } else {
                gen.writeStringField(eAttribute.getName(), null);
            }
        }
        EList<EReference> eAllReferences = value.eClass().getEAllReferences();
        for (EReference eReference : eAllReferences) {
            Object objectValue = value.eGet(eReference);
            if (objectValue != null) {
                if (eReference.isMany()) {
                    this.writeArray(gen, eReference.getName(), objectValue);
                } else if (objectValue instanceof Element elt && !ElementUtil.isFromStandardLibrary(elt) && !VirtualLinkAdapter.hasVirtualLink(elt)) {
                    var refElementId = elt.getElementId();
                    if (refElementId != null) {
                        gen.writeObjectFieldStart(eReference.getName());
                        gen.writeStringField(ID, refElementId);
                        gen.writeEndObject();
                    }
                }
            } else {
                gen.writeStringField(eReference.getName(), null);
            }
        }
        gen.writeEndObject();
    }

    private void writeArray(JsonGenerator gen, String arrayName, Object objectValue) throws IOException {
        gen.writeArrayFieldStart(arrayName);
        if (objectValue instanceof List<?> listValue && !listValue.isEmpty()) {
            for (Object listElementValue : listValue) {
                if (listElementValue instanceof Element elt && !ElementUtil.isFromStandardLibrary(elt) && !VirtualLinkAdapter.hasVirtualLink(elt)) {
                    var listElementId = elt.getElementId();
                    if (listElementId != null) {
                        gen.writeStartObject();
                        gen.writeStringField(ID, listElementId);
                        gen.writeEndObject();
                    }
                }
            }
        }
        gen.writeEndArray();
    }
}