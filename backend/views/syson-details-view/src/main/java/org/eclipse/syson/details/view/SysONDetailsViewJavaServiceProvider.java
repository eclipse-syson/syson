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
package org.eclipse.syson.details.view;

import java.util.List;

import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.emf.IJavaServiceProvider;
import org.eclipse.syson.form.services.aql.FormMutationAQLService;
import org.eclipse.syson.form.services.aql.FormQueryAQLService;
import org.eclipse.syson.model.services.aql.ModelMutationAQLService;
import org.eclipse.syson.model.services.aql.ModelQueryAQLService;
import org.springframework.context.annotation.Configuration;

/**
 * List of all Java services classes used by the {@link SysONDetailsViewDescriptionProvider}.
 *
 * @author arichard
 */
@Configuration
public class SysONDetailsViewJavaServiceProvider implements IJavaServiceProvider {

    @Override
    public List<Class<?>> getServiceClasses(View view) {
        var descriptions = view.getDescriptions();
        var optSysONDetailsViewDescription = descriptions.stream()
                .filter(desc -> SysONDetailsViewDescriptionProvider.SYSON_DETAILS_VIEW_NAME.equals(desc.getName()))
                .findFirst();
        if (optSysONDetailsViewDescription.isPresent()) {
            return List.of(
                    FormMutationAQLService.class,
                    FormQueryAQLService.class,
                    ModelMutationAQLService.class,
                    ModelQueryAQLService.class);
        }
        return List.of();
    }

}
