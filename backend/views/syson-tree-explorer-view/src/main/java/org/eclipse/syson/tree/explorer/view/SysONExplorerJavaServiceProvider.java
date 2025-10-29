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
package org.eclipse.syson.tree.explorer.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.emf.IJavaServiceProvider;
import org.eclipse.sirius.components.view.tree.TreeDescription;
import org.eclipse.syson.model.services.aql.ModelMutationAQLService;
import org.eclipse.syson.model.services.aql.ModelQueryAQLService;
import org.eclipse.syson.tree.explorer.view.services.ComposedSysONExplorerService;
import org.eclipse.syson.tree.services.aql.TreeMutationAQLService;
import org.eclipse.syson.tree.services.aql.TreeQueryAQLService;
import org.springframework.context.annotation.Configuration;

/**
 * List of all Java services classes used by the {@link SysONExplorerTreeDescriptionProvider}.
 *
 * @author gdaniel
 */
@Configuration
public class SysONExplorerJavaServiceProvider implements IJavaServiceProvider {

    @Override
    public List<Class<?>> getServiceClasses(View view) {
        List<Class<?>> serviceClasses = new ArrayList<>();
        if (view.getDescriptions().stream()
                .filter(TreeDescription.class::isInstance)
                .anyMatch(this::isSysONDefaultExplorerTreeDescription)) {
            serviceClasses.addAll(List.of(ComposedSysONExplorerService.class,
                    ModelMutationAQLService.class,
                    ModelQueryAQLService.class,
                    TreeMutationAQLService.class,
                    TreeQueryAQLService.class));
        }
        return serviceClasses;
    }

    private boolean isSysONDefaultExplorerTreeDescription(RepresentationDescription representationDescription) {
        return SysONExplorerTreeDescriptionProvider.SYSON_EXPLORER.equals(representationDescription.getName());

    }

}
