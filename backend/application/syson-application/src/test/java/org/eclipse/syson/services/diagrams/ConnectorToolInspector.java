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
package org.eclipse.syson.services.diagrams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.syson.sysml.metamodel.helper.EMFUtils;

/**
 * Inspects connector actions in the edge tools of a node description.
 *
 * @author mcharfadi
 */
public class ConnectorToolInspector {

    private final DiagramDescription diagramDescription;

    private final IDiagramIdProvider diagramIdProvider;

    private final AQLInterpreter interpreter;

    public ConnectorToolInspector(DiagramDescription diagramDescription, IDiagramIdProvider diagramIdProvider, AQLInterpreter interpreter) {
        this.diagramDescription = Objects.requireNonNull(diagramDescription);
        this.diagramIdProvider = Objects.requireNonNull(diagramIdProvider);
        this.interpreter = Objects.requireNonNull(interpreter);
    }

    /**
     * Resolves an action from the new connector palette and evaluates its target precondition against the diagram's
     * node descriptions. Each action inherits its parent's target candidates, so inspecting those candidates alone
     * would miss the action-specific restrictions.
     *
     * @param source
     *            the source node description
     * @param toolName
     *            the connector action's name
     * @param editingContext
     *            the editing context used by the action's precondition
     * @return the accepted node description names
     */
    public List<String> getTargetDescriptionNames(NodeDescription source, String toolName, IEditingContext editingContext) {
        var edgeTool = source.getEdgeTools().stream()
                .filter(tool -> tool.getPalette() != null)
                .filter(tool -> EMFUtils.allContainedObjectOfType(tool.getPalette(), NodeTool.class)
                        .anyMatch(action -> toolName.equals(action.getName())))
                .findFirst();
        assertThat(edgeTool).as("Connector tool %s on %s", toolName, source.getName()).isPresent();
        var action = EMFUtils.allContainedObjectOfType(edgeTool.orElseThrow().getPalette(), NodeTool.class)
                .filter(tool -> toolName.equals(tool.getName())).findFirst().orElseThrow();

        return EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class)
                .filter(edgeTool.orElseThrow().getTargetElementDescriptions()::contains)
                .filter(description -> {
                    var target = mock(Node.class);
                    when(target.getDescriptionId()).thenReturn(this.diagramIdProvider.getId(description));
                    var variables = Map.<String, Object>of("edgeTarget", target, "editingContext", editingContext);
                    var result = this.interpreter.evaluateExpression(variables, action.getPreconditionExpression());
                    assertThat(result.asBoolean()).as("Precondition of %s for %s", toolName, description.getName()).isPresent();
                    return result.asBoolean().orElseThrow();
                })
                .map(NodeDescription::getName)
                .toList();
    }
}
