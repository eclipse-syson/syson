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
package org.eclipse.syson.services.diagrams;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Service class for CreationTests classes.
 *
 * @author arichard
 */
public class NodeCreationTestsService {

    private final ToolTester toolTester;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    private final String editingContextId;

    public NodeCreationTestsService(ToolTester nodeCreationTester, IDescriptionNameGenerator descriptionNameGenerator, String editingContextId) {
        this.editingContextId = Objects.requireNonNull(editingContextId);
        this.toolTester = Objects.requireNonNull(nodeCreationTester);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    /**
     * Creates a runnable that invokes a node creation tool.
     *
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent node
     * @param targetObjectId
     *            the ID of the element on which apply the tool
     * @param childEClass
     *            the EClass of the child node to create
     * @return a runnable that performs the node creation
     */
    public Runnable createNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String targetObjectId, EClass childEClass) {
        return this.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, childEClass, List.of());
    }

    /**
     * Creates a runnable that invokes a node creation tool with variables.
     *
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent node
     * @param targetObjectId
     *            the ID of the element on which apply the tool
     * @param childEClass
     *            the EClass of the child node to create
     * @param variables
     *            the tool variables
     * @return a runnable that performs the node creation
     */
    public Runnable createNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String targetObjectId, EClass childEClass, List<ToolVariable> variables) {
        return this.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, this.descriptionNameGenerator.getCreationToolName(childEClass), variables);
    }

    /**
     * Creates a runnable that invokes a named node creation tool.
     *
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent node
     * @param targetObjectId
     *            the ID of the element on which apply the tool
     * @param toolName
     *            the name of the creation tool
     * @return a runnable that performs the node creation
     */
    public Runnable createNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String targetObjectId, String toolName) {
        return this.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, toolName, List.of());
    }

    /**
     * Creates a runnable that invokes a named node creation tool.
     *
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent node
     * @param targetObjectId
     *            the ID of the element on which apply the tool
     * @param toolName
     *            the name of the creation tool
     * @param selectedObject
     *            the object ID selected in the selection dialog.
     *            Most of the time the ID is computed thanks to {@link org.eclipse.sirius.components.core.api.IIdentityService#getId(Object)}.
     *            Provide an empty string if the selection in the selection dialog is optional and you do not want to provide a selection.
     * @return a runnable that performs the node creation
     *
     * @see org.eclipse.sirius.components.view.emf.diagram.SelectionDialogDescriptionConverter
     */
    public Runnable createNodeWithSelectionDialog(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String targetObjectId, String toolName, String selectedObject) {
        return this.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, toolName, List.of(new ToolVariable("selectedObject", selectedObject, ToolVariableType.OBJECT_ID)));
    }

    /**
     * Creates a runnable that invokes a named node creation tool with variables.
     *
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent node
     * @param targetObjectId
     *            the ID of the element on which apply the tool
     * @param toolName
     *            the name of the creation tool
     * @param variables
     *            the tool variables
     * @return a runnable that performs the node creation
     */
    public Runnable createNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String targetObjectId, String toolName, List<ToolVariable> variables) {
        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(parentEClass), toolName);
        return () -> this.toolTester.invokeTool(this.editingContextId,
                diagram,
                targetObjectId,
                creationToolId,
                variables);
    }

    /**
     * Creates a runnable that invokes a node creation tool on an edge.
     *
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent edge
     * @param parentLabel
     *            the label of the parent edge
     * @param toolName
     *            the name of the creation tool
     * @return a runnable that performs the node creation on the edge
     */
    public Runnable createNodeOnEdge(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName) {
        return this.createNodeOnEdge(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, toolName, List.of());
    }

    /**
     * Creates a runnable that invokes a node creation tool on an edge with variables.
     *
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent edge
     * @param parentLabel
     *            the label of the parent edge
     * @param toolName
     *            the name of the creation tool
     * @param variables
     *            the tool variables
     * @return a runnable that performs the node creation on the edge
     */
    public Runnable createNodeOnEdge(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName, List<ToolVariable> variables) {
        String creationToolId = diagramDescriptionIdProvider.getNodeCreationToolIdOnEdge(this.descriptionNameGenerator.getEdgeName(parentEClass), toolName);
        return () -> this.toolTester.createNodeOnEdge(this.editingContextId,
                diagram,
                parentLabel,
                creationToolId,
                variables);
    }
}
