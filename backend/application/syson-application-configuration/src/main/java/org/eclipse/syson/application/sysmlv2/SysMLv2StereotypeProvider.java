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

package org.eclipse.syson.application.sysmlv2;

import java.util.List;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.web.application.document.dto.Stereotype;
import org.eclipse.sirius.web.application.document.services.api.IStereotypeProvider;
import org.springframework.stereotype.Service;

/**
 * Returns the list of stereotype to create SysMLv2 documents.
 *
 * @author gcoutable
 */
@Service
public class SysMLv2StereotypeProvider implements IStereotypeProvider {

    public static final String EMPTY_ID = "empty";

    public static final String EMPTY_LABEL = "Others...";

    public static final String EMPTY_SYSML_ID = "empty_sysmlv2";

    public static final String EMPTY_SYSML_LABEL = "SysMLv2";

    public static final String EMPTY_SYSML_LIBRARY_ID = "empty_sysmlv2_library";

    public static final String EMPTY_SYSML_LIBRARY_LABEL = "SysMLv2-Library";

    @Override
    public List<Stereotype> getStereotypes(IEditingContext editingContext) {
        return List.of(
                new Stereotype(EMPTY_SYSML_ID, EMPTY_SYSML_LABEL),
                new Stereotype(EMPTY_SYSML_LIBRARY_ID, EMPTY_SYSML_LIBRARY_LABEL),
                new Stereotype(EMPTY_ID, EMPTY_LABEL)
        );
    }
}
