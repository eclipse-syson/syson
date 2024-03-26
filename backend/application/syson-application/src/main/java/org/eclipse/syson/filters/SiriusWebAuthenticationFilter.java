/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.filters;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * Filter used to provide the authentication support.
 *
 * @author sbegaudeau
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SiriusWebAuthenticationFilter extends GenericFilterBean {

    private final String authorizationHeader;

    public SiriusWebAuthenticationFilter() {
        String rawCredentials = "system:012345678910";
        String credentials = Base64.getEncoder().encodeToString(rawCredentials.getBytes());
        this.authorizationHeader = "Basic " + credentials;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(httpServletRequest) {
                @Override
                public Enumeration<String> getHeaders(String name) {
                    if (name.equals(HttpHeaders.AUTHORIZATION)) {
                        return Collections.enumeration(List.of(SiriusWebAuthenticationFilter.this.authorizationHeader));
                    }
                    return super.getHeaders(name);
                }

                @Override
                public String getHeader(String name) {
                    if (name.equals(HttpHeaders.AUTHORIZATION)) {
                        return SiriusWebAuthenticationFilter.this.authorizationHeader;
                    }
                    return super.getHeader(name);
                }
            };
            chain.doFilter(wrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }

}
