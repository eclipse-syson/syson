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

package org.eclipse.syson.application.controllers.diagrams.general.view.inheritance;

import java.util.Objects;

/**
 * Nested element test argument to test compartment items inheritance (see {@link GVCompartmentItemInheritanceTests}).
 * <p>The purpose of this record is to describe the nested element argument that is created inside the super-element during the test.</p>
 *
 * <p>The main parameters are:</p>
 * <ul>
 *     <li>
 *         the name of the tool used to create the nested element inside the super-element graphical node.
 *     </li>
 *     <li>
 *         the name of the compartment of the super-element graphical node where the nested element will be added.
 *     </li>
 *     <li>
 *         the expected name of the nested element
 *     </li>
 *     <li>
 *         the node id of the another node from the GV diagram to reference during the creation of the nested element.
 *         Some creation tools may reference another element with a selection dialog.
 *         An empty value {@code ""} could be used instead to create a simple element without any reference.
 *     </li>
 * </ul>
 * <p>Additional parameters configure the checks performed after the nested element is created:</p>
 * <ul>
 *     <li>
 *         the expected number of border nodes created with the nested element (usually 0)
 *     </li>
 *     <li>
 *         the expected number of nodes created, including the nested element itself (usually 1)
 *     </li>
 *     <li>
 *         the expected number of edges created with the nested element (usually 1)
 *     </li>
 * </ul>
 *
 * @param creationToolName
 *         the tool name used to create the nested element.
 * @param compartmentName
 *         the compartment where the nested element will be visible.
 * @param expectedName
 *         the expected name of the new nested element.
 * @param referencedNodeId
 *         the node id of another element that could be used to create the nested element (used selection dialog)
 * @param expectedBorderNodes
 *         the number of border nodes created after nested element creation.
 * @param expectedNodes
 *         the number of nodes created after nested element creation.
 * @param expectedEdges
 *         the number of edges created after nested element creation.
 */
public record NestedElementTestArgument(String creationToolName,
        String compartmentName,
        String expectedName,
        String referencedNodeId,
        int expectedBorderNodes,
        int expectedNodes,
        int expectedEdges) {

    public NestedElementTestArgument {
        Objects.requireNonNull(creationToolName);
        Objects.requireNonNull(expectedName);
        Objects.requireNonNull(referencedNodeId);
    }

    /**
     * Constructor to build a common nested element argument.
     * In most cases the nested element creation adds one node (the element itself) and one edge (composition edge to the owner node).
     *
     * @param creationToolName
     *         the tool name used to create the nested element.
     * @param compartmentName
     *         the compartment where the nested element will be visible.
     * @param expectedName
     *         the expected name of the new nested element.
     * @return the nested element argument with only one node and one edge expected.
     */
    public NestedElementTestArgument(String creationToolName, String compartmentName, String expectedName) {
        this(creationToolName, compartmentName, expectedName, "", 0, 1, 1);
    }

    @Override
    public String toString() {
        return '\'' + this.expectedName + "' in '" + this.compartmentName + '\'';
    }
}
