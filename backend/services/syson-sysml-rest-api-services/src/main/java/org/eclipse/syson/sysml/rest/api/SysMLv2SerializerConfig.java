/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import org.eclipse.syson.sysml.Element;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import io.swagger.v3.oas.annotations.Hidden;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

/**
 * Custom JSON ResponseBodyAdvice for SysMLv2 Elements.
 *
 * @author arichard
 */
@Hidden
@ControllerAdvice("org.eclipse.sirius.web.application.object.controllers")
public class SysMLv2SerializerConfig implements ResponseBodyAdvice<Object> {

    private final ObjectMapper customObjectMapper;

    public SysMLv2SerializerConfig() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Element.class, new SysMLv2JsonSerializer());
        this.customObjectMapper = JsonMapper.builder()
                .addModule(module)
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body == null || !selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            return body;
        }
        return this.customObjectMapper.valueToTree(body);
    }

    public ObjectMapper getCustomObjectMapper() {
        return this.customObjectMapper;
    }
}
