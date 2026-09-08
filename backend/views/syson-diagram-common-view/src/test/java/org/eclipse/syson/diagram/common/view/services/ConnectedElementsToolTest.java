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
package org.eclipse.syson.diagram.common.view.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.sirius.components.view.ChangeContext;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.util.DescriptionNameGenerator;
import org.junit.jupiter.api.Test;

/**
 * Tests the availability of connected element tools.
 *
 * @author cbrun
 */
public class ConnectedElementsToolTest {

    /**
     * Verify the tool is present in every node context and the group precondition checks each selected node.
     */
    @Test
    void connectedElementsAreAvailableForNodesCompartmentsAndGroups() {
        var service = new ToolDescriptionService(new DescriptionNameGenerator("test"));
        for (var section : java.util.List.of(service.relatedElementsNodeToolSection(true),
                service.relatedElementsNodeToolSection(false), service.relatedElementsGroupToolSection())) {
            assertThat(section.getNodeTools()).filteredOn(tool -> "Add existing connected elements".equals(tool.getName()))
                    .singleElement().satisfies(tool -> {
                        assertThat(tool.getPreconditionExpression()).contains("isView(");
                        assertThat(tool.getBody()).singleElement().isInstanceOfSatisfying(ChangeContext.class,
                                context -> assertThat(context.getExpression()).contains("->addExistingConnectedElements("));
                    });
        }
        NodeTool groupTool = service.relatedElementsGroupToolSection().getNodeTools().getLast();
        assertThat(groupTool.getPreconditionExpression()).contains("selectedNodes->notEmpty()", "selectedEdges->isEmpty()",
                "selectedNodes->forAll(n | self->first().isView(");
    }
}
