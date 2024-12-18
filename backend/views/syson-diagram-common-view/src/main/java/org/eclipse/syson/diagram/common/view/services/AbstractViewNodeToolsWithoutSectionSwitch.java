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
package org.eclipse.syson.diagram.common.view.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Base class for Switch retrieving the list of NodeTools (without ToolSection) for each SysMLv2 concept represented in
 * diagrams.
 *
 * @author arichard
 */
public abstract class AbstractViewNodeToolsWithoutSectionSwitch extends SysmlEClassSwitch<List<NodeTool>> {

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    protected final ViewBuilders viewBuilderHelper;

    protected final DiagramBuilders diagramBuilderHelper;

    public AbstractViewNodeToolsWithoutSectionSwitch(IDescriptionNameGenerator descriptionNameGenerator) {
        this.viewBuilderHelper = new ViewBuilders();
        this.diagramBuilderHelper = new DiagramBuilders();
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    /**
     * Implementers should provide the list of all node descriptions defined for its diagram.
     *
     * @return a list of {@link NodeDescription}
     */
    protected abstract List<NodeDescription> getAllNodeDescriptions();

    protected NodeDescription getNodeDescription(EClass eClass) {
        return this.getAllNodeDescriptions().stream()
                .filter(nd -> this.descriptionNameGenerator.getNodeName(eClass).equals(nd.getName()))
                .findFirst()
                .get();
    }
}
