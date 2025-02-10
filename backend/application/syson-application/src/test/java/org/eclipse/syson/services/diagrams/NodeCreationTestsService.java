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
package org.eclipse.syson.services.diagrams;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.util.IDescriptionNameGenerator;

import reactor.test.StepVerifier.Step;

/**
 * Service class for CreationTests classes.
 *
 * @author arichard
 */
public class NodeCreationTestsService {

    private final NodeCreationTester nodeCreationTester;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public NodeCreationTestsService(NodeCreationTester nodeCreationTester, IDescriptionNameGenerator descriptionNameGenerator) {
        this.nodeCreationTester = Objects.requireNonNull(nodeCreationTester);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, EClass childEClass) {
        this.createNode(verifier, diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass, List.of());
    }

    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, EClass childEClass, List<ToolVariable> variables) {
        this.createNode(verifier, diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, this.descriptionNameGenerator.getCreationToolName(childEClass), variables);
    }

    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName) {
        this.createNode(verifier, diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, toolName, List.of());

    }

    public void createNode(Step<DiagramRefreshedEventPayload> verifier, DiagramDescriptionIdProvider diagramDescriptionIdProvider,
            AtomicReference<Diagram> diagram, EClass parentEClass, String parentLabel, String toolName, List<ToolVariable> variables) {
        String creationToolId = diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(parentEClass), toolName);
        verifier.then(() -> this.nodeCreationTester.createNode(SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_EDITING_CONTEXT_ID,
                diagram,
                parentLabel,
                creationToolId,
                variables));
    }
}
