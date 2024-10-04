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
package org.eclipse.syson.diagram.tests.checkers;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Checks that the provided {@link DiagramDescription} does not have an empty diagram node.
 * <p>
 * Empty diagram nodes are information boxes displayed on empty diagrams to indicate the end user how to get started
 * with the diagram. A diagram may not need such node if it is never empty.
 * </p>
 *
 * @author gdaniel
 */
public class DiagramDescriptionHasNoEmptyDiagramNodeChecker extends AbstractChecker<DiagramDescription> {

    @Override
    public void check(DiagramDescription diagramDescription) {
        List<NodeDescription> emptyDiagramNodeDescriptions = EMFUtils.allContainedObjectOfType(diagramDescription, NodeDescription.class)
                .filter(nodeDescription -> nodeDescription.getName() != null && nodeDescription.getName().contains(AbstractEmptyDiagramNodeDescriptionProvider.EMPTY_DIAGRAM_NAME_SUFFIX))
                .toList();
        assertThat(emptyDiagramNodeDescriptions).as("DiagramDescription should not have an empty diagram node").isEmpty();
    }
}
