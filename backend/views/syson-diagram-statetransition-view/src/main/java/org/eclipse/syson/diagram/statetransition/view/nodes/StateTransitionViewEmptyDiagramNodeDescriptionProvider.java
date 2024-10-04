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
package org.eclipse.syson.diagram.statetransition.view.nodes;

import java.util.List;

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.nodes.AbstractEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * This is the welcome node description that is presented if and only if the State Transitino View diagram is empty.
 *
 * @author adieumegard
 */
public class StateTransitionViewEmptyDiagramNodeDescriptionProvider extends AbstractEmptyDiagramNodeDescriptionProvider {

    public static final String NAME = "STV Node " + EMPTY_DIAGRAM_NAME_SUFFIX;

    public StateTransitionViewEmptyDiagramNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return NAME;
    }

    @Override
    protected List<ToolSectionDescription> getToolSections() {
        return StateTransitionViewDiagramDescriptionProvider.TOOL_SECTIONS;
    }

}
