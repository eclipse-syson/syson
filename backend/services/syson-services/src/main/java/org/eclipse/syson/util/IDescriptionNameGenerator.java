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
package org.eclipse.syson.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;

/**
 * Interface of name generator used by all SysON description providers.
 *
 * @author Jerome Gout
 */
public interface IDescriptionNameGenerator {

    /**
     * Returns the name of a creation tool using the given {@link EClassifier} and a prefix.
     *
     * @param prefix
     *            the string that should be prepended to the name of the given {@link EClassifier}
     * @param eClassifier
     *            the {@link EClassifier} the creation tool is in charge of.
     * @return a string used to name a creation {@link NodeTool}.
     */
    String getCreationToolName(String prefix, EClassifier eClassifier);

    /**
     * Returns the name of a creation tool using the given {@link EClassifier}.
     *
     * @param eClassifier
     *            the {@link EClassifier} the creation tool is in charge of.
     * @return a string used to name a creation {@link NodeTool}.
     */
    String getCreationToolName(EClassifier eClassifier);

    /**
     * Returns the name of a creation tool using the given {@link EReference}.<br>
     * The default behavior is to return the creation tool name of the reference's type.
     *
     * @param eReference
     *            the {@link EReference} that the compartment is containing.
     * @return a string used to name a creation {@link NodeTool}.
     */
    default String getCreationToolName(EReference eReference) {
        return this.getCreationToolName(eReference.getEType());
    }

    /**
     * Returns the name of a {@link NodeDescription} using the given string.
     *
     * @param type
     *            a string to form the name of the node description.
     * @return a string used to name a {@link NodeDescription}.
     */
    String getNodeName(String type);

    /**
     * Returns the name of a {@link NodeDescription} using the given {@link EClass}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @return a string used to name a {@link NodeDescription}.
     */
    String getNodeName(EClass eClass);

    /**
     * Returns the name of a border {@link NodeDescription} using the given string.
     *
     * @param type
     *            a string to form the name of the border node description.
     * @return a string used to name a border {@link NodeDescription}.
     */
    String getBorderNodeName(String type);

    /**
     * Returns the name of a border {@link NodeDescription} using the given {@link EClass}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the border {@link NodeDescription}.
     * @return a string used to name a border {@link NodeDescription}.
     */
    String getBorderNodeName(EClass eClass);

    /**
     * Returns the name of a compartment {@link NodeDescription} using the given {@link EClass} and {@link EReference}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @param eReference
     *            the {@link EReference} that the compartment is containing.
     * @return a string used to name a compartment {@link NodeDescription}.
     */
    String getCompartmentName(EClass eClass, EReference eReference);

    /**
     * Returns the name of a free form compartment {@link NodeDescription} using the given {@link EClass} and
     * {@link EReference}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @param eReference
     *            the {@link EReference} that the compartment is containing.
     *
     * @return a string used to name a compartment {@link NodeDescription}.
     */
    String getFreeFormCompartmentName(EClass eClass, EReference eReference);

    /**
     * Returns the name of a free form compartment {@link NodeDescription} based on the given name.
     *
     * @param name
     *            the name used to compute the name of the {@link NodeDescription}.
     *
     * @return a string used to name a compartment {@link NodeDescription}.
     */
    String getFreeFormCompartmentName(String name);

    /**
     * Returns the name of a compartment item {@link NodeDescription} using the given {@link EClass} and
     * {@link EReference}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @param eReference
     *            the {@link EReference} that the compartment is containing.
     * @return a string used to name a compartment item {@link NodeDescription}.
     */
    String getCompartmentItemName(EClass eClass, EReference eReference);

    /**
     * Returns the name of an inherited compartment item {@link NodeDescription} using the given {@link EClass} and
     * {@link EReference}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @param eReference
     *            the {@link EReference} that the compartment is containing.
     * @return a string used to name an inherited compartment item {@link NodeDescription}.
     */
    String getInheritedCompartmentItemName(EClass eClass, EReference eReference);

    /**
     * Returns the name of a domain based {@link EdgeDescription} using given the given {@link EClass}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of a domain based {@link EdgeDescription}.
     * @return a string used to name a domain based {@link EdgeDescription}.
     */
    String getEdgeName(EClass eClass);

    /**
     * Returns the name of a {@link EdgeDescription} using given edge type.
     *
     * @param type
     *            a string representing the name of the edge.
     * @return a string used to name an {@link EdgeDescription}.
     */
    String getEdgeName(String type);

    /**
     * Returns the prefix to be used during the computation of the {@link NodeDescription} and {@link EdgeDescription}.
     *
     * @return a string used to name a {@link NodeDescription} or a {@link EdgeDescription}.
     */
    String getDiagramPrefix();
}
