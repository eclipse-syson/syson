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
 * The specialization test argument used to test compartment items inheritance (see {@link GVCompartmentItemInheritanceTests}).
 *
 * <p>The purpose of the class is to describe the specialization used between super and sub elements in the test.</p>
 *
 * <p>The main parameter is:</p>
 *     <ul>
 *         <li>
 *             the name of the edge tool used to create the specialization.
 *         </li>
 *     </ul>
 * <p>Additional parameters configure the checks performed after the specialization is created:</p>
 * <ul>
 *     <li>
 *         the expected number of border nodes created with the specialization (usually 0)
 *     </li>
 *     <li>
 *         the expected number of nodes created, including the specialization itself (usually 1 = inherited element)
 *     </li>
 *     <li>
 *         the expected number of edges created with the specialization (usually 1)
 *     </li>
 * </ul>
 *
 * @author Jerome Gout
 */
public class SpecializationTestArgument {

    private final String creationToolName;

    private final int expectedBorderNodes;

    private final int expectedNodes;

    private final int expectedEdges;

    /**
     * Builds a specialization argument for inheritance tests, with full control over the checks performed.
     *
     * @param creationToolName
     *         the tool name used to create the specialization edge
     * @param expectedBorderNodes
     *         the number of border nodes created after the creation of the specialization edge.
     * @param expectedNodes
     *         the number of nodes created after the creation of the specialization edge.
     * @param expectedEdges
     *         the number of edges created after the creation of the specialization edge.
     */
    public SpecializationTestArgument(String creationToolName, int expectedBorderNodes, int expectedNodes, int expectedEdges) {
        this.creationToolName = Objects.requireNonNull(creationToolName);
        this.expectedBorderNodes = expectedBorderNodes;
        this.expectedNodes = expectedNodes;
        this.expectedEdges = expectedEdges;
    }

    /**
     *  Builds a common specialization argument used in inheritance tests.
     *  <p>
     *      In most cases, applying a specialization adds a node (the inherited item) and an edge (representing the specialization itself).
     *  </p>
     *
     * @param creationToolName the tool name used to create the specialization edge
     */
    public SpecializationTestArgument(String creationToolName) {
        this(creationToolName, 0, 1, 1);
    }

    public String creationToolName() {
        return this.creationToolName;
    }

    public int expectedBorderNodes() {
        return this.expectedBorderNodes;
    }

    public int expectedNodes() {
        return this.expectedNodes;
    }

    public int expectedEdges() {
        return this.expectedEdges;
    }

    /**
     *  Returns a new specialization based on this specialization with extra border nodes.
     *
     * @param extraBorderNodes
     *         the number of extra border nodes.
     * @return a new specialization test argument with extra border nodes.
     */
    public SpecializationTestArgument withExtraBorderNodes(int extraBorderNodes) {
        // border nodes are counted as nodes as well, so we need to add extra border nodes count to nodes count.
        return new SpecializationTestArgument(this.creationToolName, this.expectedBorderNodes + extraBorderNodes, this.expectedNodes + extraBorderNodes, this.expectedEdges);
    }

    /**
     *  Returns a new specialization based on this specialization with extra nodes.
     *
     * @param extraNodes
     *         the number of extra nodes.
     * @return a new specialization test argument with extra nodes.
     */
    public SpecializationTestArgument withExtraNodes(int extraNodes) {
        return new SpecializationTestArgument(this.creationToolName, this.expectedBorderNodes, this.expectedNodes + extraNodes, this.expectedEdges);
    }

    /**
     * Returns a new specialization based on this specialization with extra edges.
     *
     * @param extraEdges
     *         the number of extra edges.
     * @return a new specialization test argument with extra edges.
     */
    public SpecializationTestArgument withExtraEdges(int extraEdges) {
        return new SpecializationTestArgument(this.creationToolName, this.expectedBorderNodes, this.expectedNodes, this.expectedEdges + extraEdges);
    }

    @Override
    public String toString() {
        if (this.creationToolName.startsWith("New ")) {
            return this.creationToolName.substring(4);
        }
        return this.creationToolName;
    }
}

