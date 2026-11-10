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
package org.eclipse.syson.standard.diagrams.view;

import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.components.representations.IRepresentationRenderVariableCustomizer;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.syson.diagram.services.DiagramQueryCache;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.stereotype.Service;

/**
 * Installs a fresh semantic query cache for each standard SysON diagram render.
 *
 * @author arichard
 */
@Service
public class SDVDiagramQueryCacheCustomizer implements IRepresentationRenderVariableCustomizer {

    /**
     * Adds the render-local cache without changing variables of other representations.
     *
     * @param representationDescription
     *            the representation being rendered
     * @param variableManager
     *            the variables shared by the render's expressions
     * @return the variable manager with a fresh cache for standard SysON diagrams
     */
    @Override
    public VariableManager customize(IRepresentationDescription representationDescription, VariableManager variableManager) {
        if (SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID.equals(representationDescription.getId())) {
            variableManager.put(DiagramQueryCache.VARIABLE_NAME, new DiagramQueryCache());
        }
        return variableManager;
    }
}
