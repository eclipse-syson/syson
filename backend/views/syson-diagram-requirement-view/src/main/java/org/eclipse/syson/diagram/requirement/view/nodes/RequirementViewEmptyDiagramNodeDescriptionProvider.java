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
package org.eclipse.syson.diagram.requirement.view.nodes;

import java.util.List;

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.nodes.AbstractEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.requirement.view.RVDescriptionNameGenerator;
import org.eclipse.syson.diagram.requirement.view.RequirementViewDiagramDescriptionProvider;

/**
 * This is the welcome node description that is presented if and only if the Action Flow View diagram is empty.
 *
 * @author Jerome Gout
 */
public class RequirementViewEmptyDiagramNodeDescriptionProvider extends AbstractEmptyDiagramNodeDescriptionProvider {

    public static final String NAME = "RV Node EmptyDiagram";

    public RequirementViewEmptyDiagramNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider, new RVDescriptionNameGenerator());
    }

    @Override
    protected String getName() {
        return NAME;
    }

    @Override
    protected List<ToolSectionDescription> getToolSections() {
        return RequirementViewDiagramDescriptionProvider.TOOL_SECTIONS;
    }

}
