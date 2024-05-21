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
package org.eclipse.syson.diagram.actionflow.view.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.actionflow.view.AFVDescriptionNameGenerator;
import org.eclipse.syson.diagram.actionflow.view.ActionFlowViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionPayloadNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionPortUsageReceiverToolNodeProvider;
import org.eclipse.syson.diagram.common.view.tools.ActionFlowCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in the Action Flow View diagram.
 *
 * @author Jerome Gout
 */
public class ActionFlowViewNodeToolSectionSwitch extends AbstractViewNodeToolSectionSwitch {

    private final List<NodeDescription> allNodeDescriptions;

    public ActionFlowViewNodeToolSectionSwitch(List<NodeDescription> allNodeDescriptions) {
        super(new AFVDescriptionNameGenerator());
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
    }

    @Override
    protected List<EReference> getElementCompartmentReferences(Element element) {
        List<EReference> refs = ActionFlowViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.get(element.eClass());
        if (refs != null) {
            return refs;
        } else {
            return List.of();
        }
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions() {
        return this.allNodeDescriptions;
    }

    @Override
    public List<NodeToolSection> caseAcceptActionUsage(AcceptActionUsage object) {
        var createSection = this.buildCreateSection(
                this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getItemDefinition()),
                this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getPartDefinition()),
                this.createPortUsageAsReceiverNodeTool());
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseActionUsage(ActionUsage object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().add(new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getUsage_NestedItem(), this.descriptionNameGenerator).create(null));
        createSection.getNodeTools().add(new ActionFlowCompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(null));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseActionDefinition(ActionDefinition object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().add(new ActionFlowCompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(null));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseDefinition(Definition object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
    }

    @Override
    public List<NodeToolSection> caseUsage(Usage object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    private NodeTool createPayloadNodeTool(EClass payloadType) {
        var payloadNodeToolProvider = new AcceptActionPayloadNodeToolProvider(payloadType, new AFVDescriptionNameGenerator());
        return payloadNodeToolProvider.create(null);
    }

    private NodeTool createPortUsageAsReceiverNodeTool() {
        var newPortAsReceiverToolProvider = new AcceptActionPortUsageReceiverToolNodeProvider();
        return newPortAsReceiverToolProvider.create(null);
    }
}
