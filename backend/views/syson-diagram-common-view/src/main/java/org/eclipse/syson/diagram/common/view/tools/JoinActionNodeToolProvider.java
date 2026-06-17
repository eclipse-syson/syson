/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Used to add the join action in actions body for all diagrams.
 *
 * @author Jerome Gout
 */
public class JoinActionNodeToolProvider extends AbstractFreeFormCompartmentNodeToolProvider {

    public JoinActionNodeToolProvider(EClass ownerEClass, IDescriptionNameGenerator descriptionNameGenerator) {
        super(ownerEClass, ActionFlowCompartmentNodeDescriptionProvider.COMPARTMENT_LABEL, descriptionNameGenerator);
    }

    @Override
    protected String getNodeDescriptionName() {
        return this.getDescriptionNameGenerator().getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME);
    }

    @Override
    protected String getCreationServiceCallExpression() {
        return ServiceMethod.of0(ViewCreateService::createJoinAction).aqlSelf();
    }

    @Override
    protected String getLabel() {
        return "New Join";
    }

    @Override
    protected String getIconPath() {
        return "/icons/full/obj16/JoinNode.svg";
    }

    @Override
    protected String getPreconditionServiceCallExpression() {
        if (this.ownerEClass == null) {
            // this tool will be invoked on the diagram background
            return ServiceMethod.of2(ViewToolService::isControlNodeActionCreationToolInsideActionOnAFV).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT);
        } else {
            // this tool will be invoked from a selected node
            return ServiceMethod.of1(ViewToolService::isControlNodeActionCreationToolInAction).aql(IEditingContext.EDITING_CONTEXT, Node.SELECTED_NODE);
        }
    }
}
