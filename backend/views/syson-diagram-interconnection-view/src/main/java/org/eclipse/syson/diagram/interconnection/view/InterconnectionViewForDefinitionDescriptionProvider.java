/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.diagram.interconnection.view;

import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.syson.diagram.common.view.IViewDescriptionProvider;
import org.springframework.stereotype.Service;

/**
 * Allows to register the Interconnection View for Definition diagram in the application.
 *
 * @author arichard
 */
@Service
public class InterconnectionViewForDefinitionDescriptionProvider implements IViewDescriptionProvider {

    @Override
    public String getViewDiagramId() {
        return "InterconnectionViewForDefinitionDiagram";
    }

    @Override
    public IRepresentationDescriptionProvider getRepresentationDescriptionProvider() {
        return new InterconnectionViewForDefinitionDiagramDescriptionProvider();
    }
}
