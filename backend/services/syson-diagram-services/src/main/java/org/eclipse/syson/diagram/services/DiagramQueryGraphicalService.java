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
package org.eclipse.syson.diagram.services;

import java.util.List;

import org.eclipse.sirius.components.diagrams.elements.NodeElementProps;
import org.eclipse.sirius.components.diagrams.renderer.DiagramRenderingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Query services related to graphical containment in rendered diagrams.
 *
 * @author arichard
 */
@Service
public class DiagramQueryGraphicalService {

    private final Logger logger = LoggerFactory.getLogger(DiagramQueryGraphicalService.class);

    /**
     * Returns {@code true} if {@code parentNodeElement} is not an ancestor of {@code childNodeElement}.
     *
     * @param parentNodeElement
     *            the element representing the parent node
     * @param childNodeElement
     *            the element representing the child node
     * @param cache
     *            the rendering cache used in the current rendering process
     * @return {@code true} if {@code parentNodeElement} is not an ancestor of {@code childNodeElement}
     */
    public boolean isNotAncestorOf(org.eclipse.sirius.components.representations.Element parentNodeElement,
            org.eclipse.sirius.components.representations.Element childNodeElement, DiagramRenderingCache cache) {
        return !this.isAncestorOf(parentNodeElement, childNodeElement, cache);
    }

    /**
     * Returns {@code true} if {@code parentNodeElement} is an ancestor of {@code childNodeElement}.
     *
     * @param parentNodeElement
     *            the element representing the parent node
     * @param childNodeElement
     *            the element representing the child node
     * @param cache
     *            the rendering cache used in the current rendering process
     * @return {@code true} if {@code parentNodeElement} is an ancestor of {@code childNodeElement}
     */
    private boolean isAncestorOf(org.eclipse.sirius.components.representations.Element parentNodeElement,
            org.eclipse.sirius.components.representations.Element childNodeElement, DiagramRenderingCache cache) {
        boolean result = false;
        if (parentNodeElement.getProps() instanceof NodeElementProps parentNodeProps
                && childNodeElement.getProps() instanceof NodeElementProps childNodeProps) {
            List<String> ancestorIds = cache.getAncestors(childNodeProps.getId()).stream()
                    .map(org.eclipse.sirius.components.representations.Element::getProps)
                    .filter(NodeElementProps.class::isInstance)
                    .map(NodeElementProps.class::cast)
                    .map(NodeElementProps::getId)
                    .toList();
            result = ancestorIds.contains(parentNodeProps.getId());
        } else {
            this.logger.warn("Cannot check graphical containment between {} and {}", parentNodeElement, childNodeElement);
        }
        return result;
    }
}
