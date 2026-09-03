/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.diagram.common.view.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Base class for Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in diagrams.
 *
 * @author Jerome Gout
 */
public abstract class AbstractViewNodeToolSectionSwitch extends SysmlEClassSwitch<List<NodeToolSection>> {

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    protected final ViewBuilders viewBuilderHelper;

    protected final DiagramBuilders diagramBuilderHelper;

    protected final ToolDescriptionService toolDescriptionService;

    public AbstractViewNodeToolSectionSwitch(IDescriptionNameGenerator descriptionNameGenerator) {
        this.viewBuilderHelper = new ViewBuilders();
        this.diagramBuilderHelper = new DiagramBuilders();
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
        this.toolDescriptionService = new ToolDescriptionService(descriptionNameGenerator);
    }

    /**
     * Implementers should provide the list of references used for compartments of the given element.
     *
     * @param element
     *            an element
     * @return the compartment reference list of the given element.
     */
    protected abstract List<EReference> getElementCompartmentReferences(Element element);

    /**
     * Implementers should provide the list of all node descriptions defined for its diagram.
     *
     * @return a list of {@link NodeDescription}
     */
    protected abstract List<NodeDescription> getAllNodeDescriptions();

    protected void createToolsForCompartmentItems(Element element, List<NodeToolSection> sections, IViewDiagramElementFinder cache) {
        this.createToolsForCompartmentItemsWithExclusions(element, List.of(), sections, cache);
    }

    /**
     * Creates all compartment item creation tools for all references retrieved by {@link AbstractViewNodeToolSectionSwitch::getElementCompartmentReferences}
     * that are not part of the given exclusion list.
     * This is useful when some compartment element creation tools are custom.
     *
     * @param element
     *         the element in which the compartment belongs
     * @param exclusions
     *         the list of references that should not create default tool for them.
     * @param sections
     *         the palette sections
     * @param cache
     *         the node descriptions cache
     */
    protected void createToolsForCompartmentItemsWithExclusions(Element element, List<EReference> exclusions, List<NodeToolSection> sections, IViewDiagramElementFinder cache) {
        var elementCompartmentReferences = this.getElementCompartmentReferences(element);
        for (EReference eReference : elementCompartmentReferences) {
            if (!exclusions.contains(eReference)) {
                this.createToolsForCompartmentItem(eReference, sections, cache);
            }
        }
    }

    protected void createToolsForCompartmentItem(EReference eReference, List<NodeToolSection> sections, IViewDiagramElementFinder cache) {
        var eType = eReference.getEType();
        var toolSectionName = this.toolDescriptionService.getToolSectionName(eType);
        var provider = new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator);
        this.toolDescriptionService.addNodeTool(sections, toolSectionName, provider.create(cache));
    }

    protected NodeDescription getNodeDescription(EClass eClass) {
        return this.getAllNodeDescriptions().stream()
                .filter(nd -> this.descriptionNameGenerator.getNodeName(eClass).equals(nd.getName()))
                .findFirst()
                .get();
    }
}
