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
package org.eclipse.syson.diagram.common.view.services.description;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.sirius.components.diagrams.tools.ToolSection;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.DropTool;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Services class for building Tool Description.
 *
 * @author adieumegard
 */
public class ToolDescriptionService {

    private static final String SERVICE_ELEMENT_INITIALIZER = "elementInitializer";

    private static final String NEW_INSTANCE = "newInstance";

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public ToolDescriptionService(IDescriptionNameGenerator descriptionNameGenerator) {
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    /**
     * Create a {@link DiagramToolSection} containing the {@code Add Existing Elements} tools.
     *
     * @return The created {@link DiagramToolSection}
     */
    public DiagramToolSection addElementsDiagramToolSection() {
        return this.diagramBuilderHelper.newDiagramToolSection()
                .name("Add")
                .nodeTools(this.addExistingElementsTool(false, false), this.addExistingElementsTool(true, false))
                .build();
    }

    /**
     * Create a {@link NodeToolSection} containing the {@code Add Existing Elements} tools.
     *
     * @param nested
     *            Whether the created tools adds nested elements
     * @return The created {@link NodeToolSection}
     */
    public NodeToolSection addElementsNodeToolSection(boolean nested) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Add")
                .nodeTools(this.addExistingElementsTool(false, nested), this.addExistingElementsTool(true, nested))
                .build();
    }

    /**
     * Create a {@link NodeTool} adding/displaying existing elements from {@code self} on the context diagram or diagram
     * element.
     *
     * @param recursive
     *            Whether or not the created tool is recursive
     * @param nested
     *            Whether or not the creation tool is adding nested elements
     * @return The created {@link NodeTool}
     */
    public NodeTool addExistingElementsTool(boolean recursive, boolean nested) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        String serviceName = "addExistingElements";
        String title = "Add existing elements";
        if (nested) {
            serviceName = "addExistingElements";
            title = "Add existing nested elements";
        }

        var addExistingelements = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression(serviceName, List.of("editingContext", "diagramContext", "selectedNode", "convertedNodes", "" + recursive)));

        String iconURL = "/icons/AddExistingElements.svg";
        if (recursive) {
            title += " (recursive)";
            iconURL = "/icons/AddExistingElementsRecursive.svg";
        }

        return builder
                .name(title)
                .iconURLsExpression(iconURL)
                .body(addExistingelements.build())
                .build();
    }

    /**
     * Return a tool section used to new children creation.
     *
     * @param nodeTools
     *            optional list of {@link NodeTool}
     *
     * @return the {@link ToolSection} grouping all creations of children.
     */
    public NodeToolSection buildCreateSection(NodeTool... nodeTools) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Create")
                .nodeTools(nodeTools)
                .build();
    }

    /**
     * Return a tool section used to edit elements.
     *
     * @param nodeTools
     *            optional list of {@link NodeTool}
     *
     * @return the {@link ToolSection} grouping all editions of elements.
     */
    public NodeToolSection buildEditSection(NodeTool... nodeTools) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Edit")
                .nodeTools(nodeTools)
                .build();
    }

    /**
     * Create a generic {@link DropTool} to handle drop from Explorer View.
     *
     * @return the created {@link DropTool}
     */
    public DropTool createDropFromExplorerTool() {
        var dropElementFromExplorer = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("dropElementFromExplorer", List.of("editingContext", "diagramContext", "selectedNode", "convertedNodes")));

        return this.diagramBuilderHelper.newDropTool()
                .name("Drop from Explorer")
                .body(dropElementFromExplorer.build())
                .build();
    }

    /**
     * Create a {@link NodeTool} to create a new instance of type {@code eClass} represented using the provided
     * {@code nodeDescription}.
     *
     * @param nodeDescription
     *            THe {@link NodeDescription} used to represent the element created using the built {@link NodeTool}
     * @param eClass
     *            The {@link EClassifier} of the created semantic element
     * @return The created {@link NodeTool}
     */
    public NodeTool createNodeToolFromDiagramBackground(NodeDescription nodeDescription, EClass eClass) {
        return this.createNodeToolFromDiagramWithDirection(nodeDescription, eClass, null);
    }

    public NodeTool createNodeToolFromDiagramWithDirection(NodeDescription nodeDescription, EClass eClass, FeatureDirectionKind direction) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        // make sure that the given element is a feature to avoid error at runtime.
        if (!SysmlPackage.eINSTANCE.getFeature().isSuperTypeOf(eClass) && direction != null) {
            return this.createNodeToolFromDiagramBackground(nodeDescription, eClass);
        }

        var setDirection = this.viewBuilderHelper.newSetValue()
                .featureName("direction");

        if (direction != null) {
            setDirection.valueExpression(direction.getLiteral());
        }

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(NEW_INSTANCE, SERVICE_ELEMENT_INITIALIZER));

        if (direction != null) {
            changeContextNewInstance.children(setDirection.build());
        }

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression("aql:selectedNode")
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createMembership"))
                .children(createEClassInstance.build(), createView.build());

        String toolLabel = this.descriptionNameGenerator.getCreationToolName(eClass);

        if (direction != null) {
            toolLabel += " " + StringUtils.capitalize(direction.getLiteral());
        }

        return builder
                .name(toolLabel)
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(changeContexMembership.build())
                .build();
    }

    /**
     * Returns the creation node tool description for the given Node Description to build a new child node for the given
     * EClass.
     *
     * @param nodeDescription
     *            the Node Description where the returned tool is added
     * @param eClass
     *            the EClass that the returned tool is in charge of
     */
    public NodeTool createNodeTool(NodeDescription nodeDescription, EClass eClass) {
        return this.createNodeToolWithDirection(nodeDescription, eClass, NodeContainmentKind.CHILD_NODE, null);
    }

    /**
     * Returns the creation node tool description for the given Node Description to build a new node for the given
     * EClass.
     *
     * @param nodeDescription
     *            the Node Description where the returned tool is added
     * @param eClass
     *            the EClass that the returned tool is in charge of
     * @param nodeKind
     *            the kind of the node associated to the EClass that is built by the returned tool
     */
    public NodeTool createNodeTool(NodeDescription nodeDescription, EClass eClass, NodeContainmentKind nodeKind) {
        return this.createNodeToolWithDirection(nodeDescription, eClass, nodeKind, null);
    }

    /**
     * Returns the creation node tool description for the given Node Description to build a new node for the given
     * EClass (which must be a {@link Feature}.
     *
     * @param nodeDescription
     *            the Node Description where the returned tool is added
     * @param eClass
     *            the EClass that the returned tool is in charge of
     * @param nodeKind
     *            the kind of the node associated to the EClass that is built by the returned tool
     * @param direction
     *            the feature direction
     */
    public NodeTool createNodeToolWithDirection(NodeDescription nodeDescription, EClass eClass, NodeContainmentKind nodeKind, FeatureDirectionKind direction) {
        // make sure that the given element is a feature to avoid error at runtime.
        if (!SysmlPackage.eINSTANCE.getFeature().isSuperTypeOf(eClass) && direction != null) {
            return this.createNodeTool(nodeDescription, eClass, nodeKind);
        }

        var builder = this.diagramBuilderHelper.newNodeTool();

        var setDirection = this.viewBuilderHelper.newSetValue()
                .featureName("direction");

        if (direction != null) {
            setDirection.valueExpression(direction.getLiteral());
        }

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(NEW_INSTANCE, SERVICE_ELEMENT_INITIALIZER));

        if (direction != null) {
            changeContextNewInstance.children(setDirection.build());
        }

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        var parentViewExpression = "aql:selectedNode";
        if (NodeContainmentKind.CHILD_NODE.equals(nodeKind)) {
            parentViewExpression = AQLUtils.getSelfServiceCallExpression("getParentNode", List.of("selectedNode", "diagramContext"));
        }

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(nodeKind)
                .elementDescription(nodeDescription)
                .parentViewExpression(parentViewExpression)
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

        String toolLabel = this.descriptionNameGenerator.getCreationToolName(eClass);

        if (direction != null) {
            toolLabel += " " + StringUtils.capitalize(direction.getLiteral());
        }

        StringBuilder iconPath = new StringBuilder();
        iconPath.append("/icons/full/obj16/");
        iconPath.append(eClass.getName());
        if (direction != null) {
            iconPath.append(StringUtils.capitalize(direction.getLiteral()));
        }
        iconPath.append(".svg");

        return builder
                .name(toolLabel)
                .iconURLsExpression(iconPath.toString())
                .body(createMembership.build())
                .build();
    }
}
