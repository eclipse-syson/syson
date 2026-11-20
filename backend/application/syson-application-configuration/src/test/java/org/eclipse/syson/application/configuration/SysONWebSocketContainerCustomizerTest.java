/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.application.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import jakarta.websocket.server.ServerContainer;

/**
 * Tests the effective WebSocket container limit after Spring bean initialization.
 *
 * @author arichard
 */
public class SysONWebSocketContainerCustomizerTest {

    /**
     * Checks that initialization applies the customized text limit to the servlet container.
     */
    @Test
    @DisplayName("When the WebSocket container is initialized, the text limit is increased and the binary limit is preserved")
    public void configuresTextMessageLimit() {
        var serverContainer = mock(ServerContainer.class);
        var servletContext = new MockServletContext();
        servletContext.setAttribute(ServerContainer.class.getName(), serverContainer);
        var factory = new ServletServerContainerFactoryBean();
        factory.setServletContext(servletContext);
        factory.setMaxTextMessageBufferSize(100_000);
        factory.setMaxBinaryMessageBufferSize(100_000);

        assertThat(new SysONWebSocketContainerCustomizer().postProcessBeforeInitialization(factory, "createWebSocketContainer")).isSameAs(factory);
        factory.afterPropertiesSet();

        verify(serverContainer).setDefaultMaxTextMessageBufferSize(1024 * 1024);
        verify(serverContainer).setDefaultMaxBinaryMessageBufferSize(100_000);
    }

    /**
     * Checks that unrelated beans retain their identity.
     */
    @Test
    @DisplayName("When an unrelated bean is initialized, its instance is preserved")
    public void preservesOtherBeans() {
        var bean = new Object();
        assertThat(new SysONWebSocketContainerCustomizer().postProcessBeforeInitialization(bean, "other")).isSameAs(bean);
    }
}
