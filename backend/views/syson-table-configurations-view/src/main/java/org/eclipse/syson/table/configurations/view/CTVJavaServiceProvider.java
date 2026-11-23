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
package org.eclipse.syson.table.configurations.view;

import java.util.List;

import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.emf.IJavaServiceProvider;
import org.eclipse.syson.services.ContextualFeatureChainObjectService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.table.configurations.view.services.CTVQueryServices;
import org.springframework.context.annotation.Configuration;

/**
 * List of Java service classes used by the {@link CTVTableDescriptionProvider}.
 *
 * @author Jerome Gout
 */
@Configuration
public class CTVJavaServiceProvider implements IJavaServiceProvider {

    /**
     * Provides the Java services used by configurations tables.
     *
     * @param view
     *            the view whose representation descriptions are inspected
     * @return the configurations table services, or an empty list for other views
     */
    @Override
    public List<Class<?>> getServiceClasses(View view) {
        if (view.getDescriptions().stream().anyMatch(desc -> CTVTableDescriptionProvider.DESCRIPTION_NAME.equals(desc.getName()))) {
            return List.of(CTVQueryServices.class, UtilService.class, ContextualFeatureChainObjectService.class);
        }
        return List.of();
    }
}
