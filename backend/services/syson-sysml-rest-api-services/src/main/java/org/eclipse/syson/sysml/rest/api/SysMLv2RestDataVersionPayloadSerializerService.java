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
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import org.eclipse.sirius.web.application.project.data.versioning.services.api.IRestDataVersionPayloadSerializerService;
import org.eclipse.syson.sysml.Element;
import org.springframework.stereotype.Service;

/**
 * Custom {@link IRestDataVersionPayloadSerializerService} for SysMLv2 Elements.
 *
 * @author arichard
 */
@Service
public class SysMLv2RestDataVersionPayloadSerializerService implements IRestDataVersionPayloadSerializerService {

    private final SysMLv2JsonSerializer sysMLv2JsonSerializer;

    public SysMLv2RestDataVersionPayloadSerializerService() {
        this.sysMLv2JsonSerializer = new SysMLv2JsonSerializer();
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof Element;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof Element element) {
            this.sysMLv2JsonSerializer.serialize(element, gen, serializers);
        } else {
            gen.writeNull();
        }
    }
}
