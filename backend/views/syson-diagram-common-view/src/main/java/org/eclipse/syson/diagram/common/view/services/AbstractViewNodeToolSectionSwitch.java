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
package org.eclipse.syson.diagram.common.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
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

    public AbstractViewNodeToolSectionSwitch(IDescriptionNameGenerator descriptionNameGenerator) {
        this.viewBuilderHelper = new ViewBuilders();
        this.diagramBuilderHelper = new DiagramBuilders();
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
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

    protected List<NodeTool> createToolsForCompartmentItems(Element object) {
        return this.getElementCompartmentReferences(object).stream().flatMap(ref -> this.createToolsForCompartmentItem(ref).stream()).toList();
    }

    protected List<NodeTool> createToolsForCompartmentItem(EReference eReference) {
        List<NodeTool> compartmentNodeTools = new ArrayList<>();
        CompartmentNodeToolProvider provider = new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator);
        compartmentNodeTools.add(provider.create(null));
        return compartmentNodeTools;
    }

    protected NodeTool createNestedUsageNodeTool(EClass eClass) {
        NodeDescription nodeDesc = this.getAllNodeDescriptions().stream().filter(nd -> this.descriptionNameGenerator.getNodeName(eClass).equals(nd.getName())).findFirst().get();
        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("newInstance", "elementInitializer"));

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDesc)
                .parentViewExpression(AQLUtils.getSelfServiceCallExpression("getParentNode", List.of("selectedNode", "diagramContext")))
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newFeatureMembership")
                .children(createEClassInstance.build(), createView.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFeatureMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newFeatureMembership")
                .children(changeContexMembership.build());

        return this.diagramBuilderHelper.newNodeTool()
                .name(this.descriptionNameGenerator.getCreationToolName("New ", eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }
}
