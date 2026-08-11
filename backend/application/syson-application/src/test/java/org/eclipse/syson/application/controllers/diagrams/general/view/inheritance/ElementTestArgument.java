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

import org.eclipse.emf.ecore.EClass;

/**
 * Arguments used to test compartment item inheritance
 * (see {@link GVCompartmentItemInheritanceTests}).
 *
 * <p>This record describes the arguments for a super- or sub-element when testing the inheritance of nested elements.</p>
 *
 * <p>The main parameters are:</p>
 * <ul>
 *     <li>
 *         the metamodel class of the element (e.g. {@code SysmlPackage.eINSTANCE.getActionDefinition()})
 *     </li>
 *     <li>
 *         the name of the tool in the GV diagram palette used to create the element (e.g. {@code "New Action Definition"})
 *     </li>
 * </ul>
 *
 * <p>Additional parameters configure the checks performed after the element is created:</p>
 * <ul>
 *     <li>
 *         the expected number of border nodes created with the element (usually 0)
 *     </li>
 *     <li>
 *         the expected number of nodes created, including the element node itself (usually 1)
 *     </li>
 *     <li>
 *         the expected number of edges created with the element (usually 0)
 *     </li>
 * </ul>
 *
 *
 * @param eClass
 *         the EClass of the element
 * @param creationToolName
 *         the name of the tool which is used to create this element on the GV diagram.
 * @param expectedBorderNodes
 *         the number of border nodes created after the element creation.
 * @param expectedNodes
 *         the number of nodes created after the element creation.
 * @param expectedEdges
 *         the number of edges created after the element creation.
 */
public record ElementTestArgument(EClass eClass, String creationToolName, int expectedBorderNodes, int expectedNodes, int expectedEdges) {

    public ElementTestArgument {
        Objects.requireNonNull(eClass);
        Objects.requireNonNull(creationToolName);
    }

    ElementTestArgument(EClass eClass, String creationToolName) {
        // by default creating an element on the diagram only creates 1 new node (the element graphical node itself).
        this(eClass, creationToolName, 0, 1, 0);
    }

    @Override
    public String toString() {
        return this.eClass.getName();
    }
}
