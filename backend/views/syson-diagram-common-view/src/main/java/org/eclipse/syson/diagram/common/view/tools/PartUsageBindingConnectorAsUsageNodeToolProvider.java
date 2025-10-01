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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Node tool provider to create a bind connection edge from a Part usage port to a new Part usage port.
 *
 * @author Jerome Gout
 */
public class PartUsageBindingConnectorAsUsageNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public PartUsageBindingConnectorAsUsageNodeToolProvider(IDescriptionNameGenerator descriptionNameGenerator) {
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("expose",
                        List.of(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE)));

        var creationBindingConnectorAsUsageServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createPartUsageAndBindingConnectorAsUsage"))
                .children(updateExposedElements.build())
                .build();

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(creationBindingConnectorAsUsageServiceCall)
                .build();

        return builder.name(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage()))
                .iconURLsExpression("/icons/full/obj16/BindingConnectorAsUsage.svg")
                .body(rootChangContext)
                .build();
    }
}

