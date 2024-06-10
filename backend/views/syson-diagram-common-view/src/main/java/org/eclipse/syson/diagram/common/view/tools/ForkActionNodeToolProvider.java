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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to add the fork action inside diagrams.
 *
 * @author Jerome Gout
 */
public class ForkActionNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    private final EClass ownerEClass;

    public ForkActionNodeToolProvider(EClass ownerEClass, IDescriptionNameGenerator descriptionNameGenerator) {
        this.ownerEClass = Objects.requireNonNull(ownerEClass);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var params = List.of(
                AQLUtils.aqlString(this.descriptionNameGenerator.getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME)),
                AQLUtils.aqlString(this.descriptionNameGenerator.getNodeName(this.ownerEClass)),
                AQLUtils.aqlString(ActionFlowCompartmentNodeDescriptionProvider.COMPARTMENT_LABEL),
                "selectedNode",
                "diagramContext",
                "convertedNodes");
        var creationServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createForkAction"));

        var createViewOperation = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createViewInFreeFormCompartment", params))
                .build();

        var revealOperation = this.viewBuilderHelper.newChangeContext()
                .expression("aql:selectedNode.revealCompartment(self, diagramContext, editingContext, convertedNodes)")
                .build();

        creationServiceCall.children(createViewOperation, revealOperation);

        return builder.name("New Fork")
                .iconURLsExpression("/icons/full/obj16/ForkNode.svg")
                .body(creationServiceCall.build())
                .build();
    }
}
