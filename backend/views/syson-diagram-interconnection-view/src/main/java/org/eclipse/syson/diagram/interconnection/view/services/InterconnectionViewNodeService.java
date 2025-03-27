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
package org.eclipse.syson.diagram.interconnection.view.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.syson.diagram.common.view.services.ViewNodeService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Node-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author gdaniel
 */
public class InterconnectionViewNodeService extends ViewNodeService {

    private final Logger logger = LoggerFactory.getLogger(InterconnectionViewNodeService.class);

    public InterconnectionViewNodeService(IObjectSearchService objectSearchService) {
        super(objectSearchService);
    }

    /**
     * Returns the {@link PartUsage} instances contained in the provided {@code element}.
     *
     * @param element
     *            the parent element
     * @return the {@link PartUsage} instances contained in the provided {@code element}
     */
    public List<PartUsage> getPartUsages(Element element) {
        List<PartUsage> partUsages = List.of();
        if (element instanceof Usage usage) {
            partUsages = usage.getNestedPart();
        } else if (element instanceof Definition definition) {
            partUsages = definition.getOwnedPart();
        } else {
            this.logger.warn("Cannot get {} from the provided element {}", PartUsage.class.getSimpleName(), element);
        }
        return partUsages;
    }

    /**
     * Returns the {@link PortUsage} instances contained in the provided {@code element}.
     *
     * @param element
     *            the parent element
     * @return the {@link PortUsage} instances contained in the provided {@code element}
     */
    public List<PortUsage> getPortUsages(Element element) {
        List<PortUsage> portUsages = List.of();
        if (element instanceof Usage usage) {
            portUsages = usage.getNestedPort();
        } else if (element instanceof Definition definition) {
            portUsages = definition.getOwnedPort();
        } else {
            this.logger.warn("Cannot get {} from the provided element {}", PortUsage.class.getSimpleName(), element);
        }
        return portUsages;
    }

    /**
     * Returns the {@link ActionUsage} instances contained in the provided {@code element}.
     *
     * @param element
     *            the parent element
     * @return the {@link ActionUsage} instances contained in the provided {@code element}
     */
    public List<ActionUsage> getActionUsages(Element element) {
        List<ActionUsage> actionUsages = List.of();
        if (element instanceof Usage usage) {
            actionUsages = usage.getNestedAction();
        } else if (element instanceof Definition definition) {
            actionUsages = definition.getOwnedAction();
        } else {
            this.logger.warn("Cannot get {} from the provided element {}", ActionUsage.class.getSimpleName(), element);
        }
        return actionUsages;
    }

    /**
     * Returns the items of the provided element. Only intended to be used by ItemUsageBorderNodeDescriptionProvider.
     *
     * @param element
     *            the element
     * @return the the items of the provided element.
     */
    public List<ItemUsage> getBorderNodesItems(Element element) {
        List<ItemUsage> items = new ArrayList<>();
        if (element instanceof ActionUsage actionUsage) {
            items = actionUsage.getNestedItem().stream().filter(ni -> SysmlPackage.eINSTANCE.getItemUsage().equals(ni.eClass())).toList();
        }
        return items;
    }

}
