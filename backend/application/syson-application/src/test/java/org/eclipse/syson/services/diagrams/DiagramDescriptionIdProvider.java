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
package org.eclipse.syson.services.diagrams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Utility methods used to retrieve identifiers of elements inside a {@link DiagramDescription}.
 *
 * @author gdaniel
 */
public class DiagramDescriptionIdProvider {

    private final DiagramDescription diagramDescription;

    private final IDiagramIdProvider diagramIdProvider;

    public DiagramDescriptionIdProvider(DiagramDescription diagramDescription, IDiagramIdProvider diagramIdProvider) {
        this.diagramDescription = Objects.requireNonNull(diagramDescription);
        this.diagramIdProvider = Objects.requireNonNull(diagramIdProvider);
    }

    public String getRepresentationDescriptionId() {
        return this.diagramIdProvider.getId(this.diagramDescription);
    }

    public String getDiagramCreationToolId(String toolName) {
        Optional<String> creationToolId = EMFUtils.allContainedObjectOfType(this.diagramDescription.getPalette(), NodeTool.class)
                .filter(nodeTool -> nodeTool.getName().equals(toolName))
                .map(nodeTool -> UUID.nameUUIDFromBytes(EcoreUtil.getURI(nodeTool).toString().getBytes()).toString())
                .findFirst();
        assertThat(creationToolId).as("Diagram tool " + toolName + " should exist").isPresent();
        return creationToolId.get();
    }

    public String getNodeCreationToolId(String nodeDescriptionName, String toolName) {
        Optional<String> creationToolId =  EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class)
                .filter(nodeDescription -> nodeDescription.getName().equals(nodeDescriptionName))
                .findFirst()
                .map(NodeDescription::getPalette)
                .flatMap(palette -> EMFUtils.allContainedObjectOfType(palette, NodeTool.class)
                        .filter(nodeTool -> nodeTool.getName().equals(toolName))
                        .map(nodeTool -> UUID.nameUUIDFromBytes(EcoreUtil.getURI(nodeTool).toString().getBytes()).toString())
                        .findFirst());
        assertThat(creationToolId).as(nodeDescriptionName + " tool " + toolName + " should exist").isPresent();
        return creationToolId.get();
    }

    public String getNodeDescriptionId(String name) {
        Optional<String> nodeDescriptionId = EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class)
                .filter(nodeDescription -> nodeDescription.getName().equals(name))
                .map(this.diagramIdProvider::getId)
                .findFirst();
        assertThat(nodeDescriptionId).as("Node " + name + " should exist").isPresent();
        return nodeDescriptionId.get();
    }

}
