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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create the ending node description of an Action.
 *
 * @author Jerome Gout
 */
public class DoneActionNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String DONE_ACTION_NAME = "DoneAction";

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public DoneActionNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getActionUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(false)
                .domainType(domainType)
                .defaultWidthExpression("36")
                .defaultHeightExpression("36")
                .name(this.descriptionNameGenerator.getNodeName(DONE_ACTION_NAME))
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("retrieveStandardDoneAction"))
                .style(this.createImageNodeStyleDescription("images/done_action.svg"))
                .userResizable(UserResizableDirection.NONE)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        // this nodeDescription has not been added to the diagramDescription children but to the fakeNodeDescription
        // children instead
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(DONE_ACTION_NAME)).ifPresent(nodeDescription -> {
            nodeDescription.setPalette(this.createNodePalette());
        });
    }

    private NodePalette createNodePalette() {
        return this.diagramBuilderHelper.newNodePalette()
                .quickAccessTools(this.getDeleteFromDiagramTool())
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .build();
    }
}
