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
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.util.IDescriptionNameGenerator;

import reactor.test.StepVerifier.Step;

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
     * Creates a node using a creation tool.
     *
     * @param verifier
     *            the verifier to chain the node creation to
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent node
     * @param parentLabel
     *            the label of the parent node
     * @param childEClass
     *            the EClass of the child node to create
     * @deprecated use {@link #createNode(DiagramDescriptionIdProvider, AtomicReference, EClass, String, EClass)} instead.
     */
    @Deprecated
    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, EClass childEClass) {
        this.createNode(verifier, diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass, List.of());
    }

    /**
     * Creates a node using a creation tool with variables.
     *
     * @param verifier
     *            the verifier to chain the node creation to
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent node
     * @param parentLabel
     *            the label of the parent node
     * @param childEClass
     *            the EClass of the child node to create
     * @param variables
     *            the tool variables
     * @deprecated use {@link #createNode(DiagramDescriptionIdProvider, AtomicReference, EClass, String, EClass, List)} instead.
     */
    @Deprecated
    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, EClass childEClass, List<ToolVariable> variables) {
        this.createNode(verifier, diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, this.descriptionNameGenerator.getCreationToolName(childEClass), variables);
    }

    /**
     * Creates a node using a named tool.
     *
     * @param verifier
     *            the verifier to chain the node creation to
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent node
     * @param parentLabel
     *            the label of the parent node
     * @param toolName
     *            the name of the creation tool
     * @deprecated use {@link #createNode(DiagramDescriptionIdProvider, AtomicReference, EClass, String, String)} instead.
     */
    @Deprecated
    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName) {
        this.createNode(verifier, diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, toolName, List.of());
    }

    /**
     * Creates a node using a named tool with variables.
     *
     * @param verifier
     *            the verifier to chain the node creation to
     * @param diagramDescriptionIdProvider
     *            the diagram description ID provider
     * @param diagram
     *            the diagram reference
     * @param parentEClass
     *            the EClass of the parent node
     * @param parentLabel
     *            the label of the parent node
     * @param toolName
     *            the name of the creation tool
     * @param variables
     *            the tool variables
     * @deprecated use {@link #createNode(DiagramDescriptionIdProvider, AtomicReference, EClass, String, String, List)} instead.
     */
    @Deprecated
    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName, List<ToolVariable> variables) {
        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(parentEClass), toolName);
        verifier.then(() -> this.toolTester.invokeTool(this.editingContextId,
                diagram,
                parentLabel,
                creationToolId,
                variables));
    }

    /**
     * Creates a node on an edge using a named tool.
     *
     * @param verifier
     *            the verifier to chain the node creation to
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
     * @deprecated use {@link #createNodeOnEdge(DiagramDescriptionIdProvider, AtomicReference, EClass, String, String)} instead.
     */
    @Deprecated
    public void createNodeOnEdge(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName) {
        this.createNodeOnEdge(verifier, diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, toolName, List.of());
    }

    /**
     * Creates a node on an edge using a named tool with variables.
     *
     * @param verifier
     *            the verifier to chain the node creation to
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
     * @deprecated use {@link #createNodeOnEdge(DiagramDescriptionIdProvider, AtomicReference, EClass, String, String, List)} instead.
     */
    @Deprecated
    public void createNodeOnEdge(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName, List<ToolVariable> variables) {
        String creationToolId = diagramDescriptionIdProvider.getNodeCreationToolIdOnEdge(this.descriptionNameGenerator.getEdgeName(parentEClass), toolName);
        verifier.then(() -> this.toolTester.createNodeOnEdge(this.editingContextId,
                diagram,
                parentLabel,
                creationToolId,
                variables));
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
     * @param parentLabel
     *            the label of the parent node
     * @param childEClass
     *            the EClass of the child node to create
     * @return a runnable that performs the node creation
     */
    public Runnable createNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, EClass childEClass) {
        return this.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass, List.of());
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
     * @param parentLabel
     *            the label of the parent node
     * @param childEClass
     *            the EClass of the child node to create
     * @param variables
     *            the tool variables
     * @return a runnable that performs the node creation
     */
    public Runnable createNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, EClass childEClass, List<ToolVariable> variables) {
        return this.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, this.descriptionNameGenerator.getCreationToolName(childEClass), variables);
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
     * @param parentLabel
     *            the label of the parent node
     * @param toolName
     *            the name of the creation tool
     * @return a runnable that performs the node creation
     */
    public Runnable createNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName) {
        return this.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, toolName, List.of());
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
     * @param parentLabel
     *            the label of the parent node
     * @param toolName
     *            the name of the creation tool
     * @param variables
     *            the tool variables
     * @return a runnable that performs the node creation
     */
    public Runnable createNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName, List<ToolVariable> variables) {
        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(parentEClass), toolName);
        return () -> this.toolTester.invokeTool(this.editingContextId,
                diagram,
                parentLabel,
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
