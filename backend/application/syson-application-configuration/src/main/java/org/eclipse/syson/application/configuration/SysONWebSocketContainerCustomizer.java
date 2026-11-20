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

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * Allows large Explorer expansion subscriptions while keeping incoming text messages bounded to one MiB. When
 *
 * @technical-debt https://github.com/eclipse-sirius/sirius-web/issues/7188 will be fixed, this class should probably be
 *                 removed.
 *
 * @author arichard
 */
@Component
public class SysONWebSocketContainerCustomizer implements BeanPostProcessor {

    /**
     * Raises Sirius Web's text-message limit before the servlet WebSocket container is initialized.
     *
     * @param bean
     *            the bean being initialized
     * @param beanName
     *            the bean's name
     * @return the unchanged bean instance
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof ServletServerContainerFactoryBean factory) {
            factory.setMaxTextMessageBufferSize(1024 * 1024);
        }
        return bean;
    }
}
