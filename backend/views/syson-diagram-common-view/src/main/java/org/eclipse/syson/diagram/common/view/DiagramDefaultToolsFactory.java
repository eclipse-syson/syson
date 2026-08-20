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

package org.eclipse.syson.diagram.common.view;

import org.eclipse.sirius.components.view.ChangeContext;
import org.eclipse.sirius.components.view.ViewFactory;
import org.eclipse.sirius.components.view.diagram.DiagramFactory;
import org.eclipse.sirius.components.view.diagram.EdgeToolSection;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;

/**
 * Factory to create generic tool descriptions which invoke the default/canonical behaviors.
 *
 * @author tgiraudet
 */
public class DiagramDefaultToolsFactory {

    private static final String HAS_CHILDREN_EXPRESSION = "aql:selectedNode.getChildNodes()->notEmpty() or selectedNode.getBorderNodes()->notEmpty()";

    private static final String HAS_HIDDEN_CHILDREN_EXPRESSION = "aql:selectedNode.getChildNodes()->union(selectedNode.getBorderNodes())->select(n | n.isHidden())->notEmpty()";

    public NodeToolSection createDefaultHideRevealNodeToolSection() {
        NodeToolSection nodeToolSection = DiagramFactory.eINSTANCE.createNodeToolSection();
        nodeToolSection.setName("Show/Hide");
        nodeToolSection.getNodeTools().add(this.createDefaultHideNodeTool());
        nodeToolSection.getNodeTools().add(this.createDefaultHideAllChildrenNodeTool());
        nodeToolSection.getNodeTools().add(this.createDefaultRevealAllChildrenNodeTool());
        nodeToolSection.getNodeTools().add(this.createDefaultResetAllChildrenVisibilityModifiersNodeTool());
        nodeToolSection.getNodeTools().add(this.createDefaultRevealChildrenWithValueNodeTool());
        return nodeToolSection;
    }

    public NodeTool createDefaultHideNodeTool() {
        NodeTool newNodeTool = DiagramFactory.eINSTANCE.createNodeTool();
        newNodeTool.setName("Hide");
        ChangeContext body = ViewFactory.eINSTANCE.createChangeContext();
        body.setExpression("aql:diagramServices.hide(Sequence{selectedNode})");
        newNodeTool.getBody().add(body);
        newNodeTool.setIconURLsExpression("aql:'/icons/full/obj16/HideTool.svg'");
        return newNodeTool;
    }

    public NodeTool createDefaultHideAllChildrenNodeTool() {
        NodeTool newNodeTool = DiagramFactory.eINSTANCE.createNodeTool();
        newNodeTool.setName("Hide all content");
        ChangeContext body = ViewFactory.eINSTANCE.createChangeContext();
        body.setExpression("aql:diagramServices.hide(selectedNode.getChildNodes()->union(selectedNode.getBorderNodes()))");
        newNodeTool.getBody().add(body);
        newNodeTool.setIconURLsExpression("aql:'/icons/full/obj16/HideTool.svg'");
        newNodeTool.setPreconditionExpression(HAS_CHILDREN_EXPRESSION);
        return newNodeTool;
    }

    public NodeTool createDefaultRevealAllChildrenNodeTool() {
        NodeTool newNodeTool = DiagramFactory.eINSTANCE.createNodeTool();
        newNodeTool.setName("Show all content");
        ChangeContext body = ViewFactory.eINSTANCE.createChangeContext();
        body.setExpression("aql:diagramServices.reveal(selectedNode.getChildNodes()->union(selectedNode.getBorderNodes()))");
        newNodeTool.getBody().add(body);
        newNodeTool.setIconURLsExpression("aql:'/icons/full/obj16/ShowTool.svg'");
        newNodeTool.setPreconditionExpression(HAS_HIDDEN_CHILDREN_EXPRESSION);
        return newNodeTool;
    }

    public NodeTool createDefaultResetAllChildrenVisibilityModifiersNodeTool() {
        NodeTool newNodeTool = DiagramFactory.eINSTANCE.createNodeTool();
        newNodeTool.setName("Reset content");
        ChangeContext body = ViewFactory.eINSTANCE.createChangeContext();
        body.setExpression("aql:diagramServices.resetViewModifiers(selectedNode.getChildNodes()->union(selectedNode.getBorderNodes()))");
        newNodeTool.getBody().add(body);
        newNodeTool.setIconURLsExpression("aql:'/icons/full/obj16/ShowTool.svg'");
        newNodeTool.setPreconditionExpression(HAS_CHILDREN_EXPRESSION);
        return newNodeTool;
    }

    public NodeTool createDefaultRevealChildrenWithValueNodeTool() {
        NodeTool newNodeTool = DiagramFactory.eINSTANCE.createNodeTool();
        newNodeTool.setName("Show valued content");
        ChangeContext body = ViewFactory.eINSTANCE.createChangeContext();
        body.setExpression("aql:diagramServices.reveal(selectedNode.getChildNodes()->union(selectedNode.getBorderNodes())->select(n | n.getChildNodes()->notEmpty() or n.getBorderNodes()->notEmpty()))");
        newNodeTool.getBody().add(body);
        newNodeTool.setIconURLsExpression("aql:'/icons/full/obj16/ShowTool.svg'");
        newNodeTool.setPreconditionExpression(HAS_HIDDEN_CHILDREN_EXPRESSION);
        return newNodeTool;
    }

    public EdgeToolSection createDefaultHideRevealEdgeToolSection() {
        EdgeToolSection edgeToolSection = DiagramFactory.eINSTANCE.createEdgeToolSection();
        edgeToolSection.setName("Show/Hide");
        edgeToolSection.getNodeTools().add(this.createDefaultHideEdgeTool());
        return edgeToolSection;
    }

    public NodeTool createDefaultHideEdgeTool() {
        NodeTool newNodeTool = DiagramFactory.eINSTANCE.createNodeTool();
        newNodeTool.setName("Hide");
        ChangeContext body = ViewFactory.eINSTANCE.createChangeContext();
        body.setExpression("aql:diagramServices.hide(Sequence{selectedEdge})");
        newNodeTool.getBody().add(body);
        newNodeTool.setIconURLsExpression("aql:'/icons/full/obj16/HideTool.svg'");
        return newNodeTool;
    }

}
