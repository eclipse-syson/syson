/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.diagram.services.api;

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Usage;

/**
 * Interface providing diagram label-related services.
 *
 * @author arichard
 */
public interface IDiagramLabelService {

    /**
     * Returns the identification of the provided {@code element}.
     * <p>
     * The identification of an element is the concatenation of its <i>short name</i> and its <i>declared name</i>.
     * </p>
     *
     * @param element
     *            the element to get the identification from
     * @return the identification
     */
    String getIdentificationLabel(Element element);

    /**
     * Return the label of the redefinition of the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @return the label of the redefinition of the given {@link Element} if there is one, an empty string otherwise.
     */
    String getRedefinitionLabel(Element element);

    /**
     * Return the label of the subclassification of the given {@link Element}.
     *
     * @param usage
     *            the given {@link Element}.
     * @return the label of the subclassification of the given {@link Element} if there is one, an empty string
     *         otherwise.
     */
    String getSubclassificationLabel(Element element);

    /**
     * Return the label of the subsetting of the given {@link Element}.
     *
     * @param usage
     *            the given {@link Element}.
     * @return the label of the subsetting of the given {@link Element} if there is one, an empty string otherwise.
     */
    String getSubsettingLabel(Element element);

    /**
     * Get the SysML textual representation of the given element.
     *
     * @param element
     *            the element to convert to textual format
     * @param resolvableName
     *            holds <code>true</code> to compute resolvable names for references, otherwise simple name are used to
     *            reference an element.
     * @return a textual representation or <code>null</code> if none
     */
    String getSysmlTextualRepresentation(Element element, boolean resolvableName);

    /**
     * Return the label of the typing part of the given {@link Element}.
     *
     * @param usage
     *            the given {@link Element}.
     * @return the label of the typing part of the given {@link Element} if there is one, an empty string otherwise.
     */
    String getTypingLabel(Element element);

    /**
     * Return the label of the value part of the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label of the value part of the given {@link Usage} if there is one, an empty string otherwise.
     */
    String getValueLabel(Usage usage);
}
