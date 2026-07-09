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
import java.util.Objects;

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
     * Returns {@code true} if both elements are contained in the same graphical container.
     *
     * @param source
     *            the graphical source element
     * @param target
     *            the graphical target element
     * @param cache
     *            the rendering cache used in the current rendering process
     * @return {@code true} if both elements share the same graphical container
     */
    public boolean isInSameGraphicalContainer(org.eclipse.sirius.components.representations.Element source,
            org.eclipse.sirius.components.representations.Element target, DiagramRenderingCache cache) {
        if (source.getProps() instanceof NodeElementProps sourceNodeProps
                && target.getProps() instanceof NodeElementProps targetNodeProps) {
            String sourceParentId = this.getParentId(cache, sourceNodeProps);
            String targetParentId = this.getParentId(cache, targetNodeProps);
            return (sourceParentId == null && targetParentId == null)
                    || (sourceParentId != null && sourceParentId.equals(targetParentId));
        }
        return true;
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

    private String getParentId(DiagramRenderingCache cache, NodeElementProps nodeElementProps) {
        return cache.getParent(nodeElementProps.getId())
                .map(org.eclipse.sirius.components.representations.Element::getProps)
                .map(props -> {
                    if (props instanceof NodeElementProps parentNodeProps) {
                        return parentNodeProps.getId();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .orElse(null);
    }
}
