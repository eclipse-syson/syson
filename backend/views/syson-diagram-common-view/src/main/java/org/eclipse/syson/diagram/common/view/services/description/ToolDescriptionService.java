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
package org.eclipse.syson.diagram.common.view.services.description;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tools.ToolSection;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.DropTool;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
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

    protected static final String SERVICE_ELEMENT_INITIALIZER = "elementInitializer";

    protected static final String NEW_INSTANCE = "newInstance";

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final NodeToolSectionNameSwitch nodeToolSectionNameSwitch = new NodeToolSectionNameSwitch();

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    public ToolDescriptionService(IDescriptionNameGenerator descriptionNameGenerator) {
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    /**
     * Create default node tool sections.
     *
     * @return an ordered list of {@link NodeToolSection}.
     */
    public LinkedList<NodeToolSection> createDefaultNodeToolSections() {
        var allToolSections = new LinkedList<NodeToolSection>();
        allToolSections.add(this.buildRequirementsSection());
        allToolSections.add(this.buildStructureSection());
        allToolSections.add(this.buildBehaviorSection());
        allToolSections.add(this.buildAnalysisSection());
        allToolSections.add(this.buildExtensionSection());
        allToolSections.add(this.buildViewAsSection());
        return allToolSections;
    }

    /**
     * Remove all empty node tool sections for the given list of node tool sections.
     *
     * @param toolSections
     *            a list of {@link NodeToolSection}.
     */
    public void removeEmptyNodeToolSections(List<NodeToolSection> toolSections) {
        toolSections.removeIf(nts -> nts.getNodeTools().isEmpty());
    }

    /**
     * Get the node tool section with the given name among the given list of node tool sections.
     *
     * @param toolSections
     *            a list of {@link NodeToolSection}.
     * @param toolSectionName
     *            the name of the node tool section to find.
     * @return an optional {@link NodeToolSection}.
     */
    public Optional<NodeToolSection> getNodeToolSection(List<NodeToolSection> toolSections, String toolSectionName) {
        return toolSections.stream().filter(nts -> Objects.equals(nts.getName(), toolSectionName)).findFirst();
    }

    /**
     * Add the given node tool to the node tool section with the given name among the given list of node tool sections.
     *
     * @param toolSections
     *            a list of {@link NodeToolSection}.
     * @param toolSectionName
     *            the name of the node tool section to find.
     * @param nodeTool
     *            the {@link NodeTool} to add.
     */
    public void addNodeTool(List<NodeToolSection> toolSections, String toolSectionName, NodeTool nodeTool) {
        toolSections.stream().filter(nts -> nts.getName().equals(toolSectionName)).findFirst().ifPresent(nts -> nts.getNodeTools().add(nodeTool));
    }

    /**
     * Get the tool section name for the given {@link EClass}.
     *
     * @param eClass
     *            the given {@link EClass}.
     * @return the tool section name.
     */
    public String getToolSectionName(EClass eClass) {
        return this.nodeToolSectionNameSwitch.doSwitch(eClass);
    }

    /**
     * Get the tool section name for the given {@link EClass}.
     *
     * @param eClass
     *            the given {@link EClass}.
     * @return the tool section name.
     */
    public String getToolSectionName(EClassifier eClassifier) {
        if (eClassifier instanceof EClass eClass) {
            return this.nodeToolSectionNameSwitch.doSwitch(eClass);
        }
        return "";
    }

    /**
     * Create a {@link DiagramToolSection} containing the {@code Add Existing Elements} tools.
     *
     * @return The created {@link DiagramToolSection}
     */
    public DiagramToolSection relatedElementsDiagramToolSection() {
        return this.diagramBuilderHelper.newDiagramToolSection()
                .name("Related Elements")
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
    public NodeToolSection relatedElementsNodeToolSection(boolean nested) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Related Elements")
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

        var addToExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(
                        AQLUtils.getSelfServiceCallExpression("addToExposedElements", List.of("" + recursive, IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                                ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE)));

        var changeContextViewUsageOwner = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("getViewUsageOwner"))
                .children(addToExposedElements.build());

        String title = "Add existing elements";
        if (nested) {
            title = "Add existing nested elements";
        }

        String iconURL = "/icons/AddExistingElements.svg";
        if (recursive) {
            title += " (recursive)";
            iconURL = "/icons/AddExistingElementsRecursive.svg";
        }

        return builder
                .name(title)
                .iconURLsExpression(iconURL)
                .body(changeContextViewUsageOwner.build())
                .build();
    }

    /**
     * Return a tool section used to store Requirements related tools.
     *
     * @param nodeTools
     *            optional list of {@link NodeTool}
     *
     * @return the {@link ToolSection}.
     */
    public NodeToolSection buildRequirementsSection(NodeTool... nodeTools) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name(ToolConstants.REQUIREMENTS)
                .nodeTools(nodeTools)
                .build();
    }

    /**
     * Return a tool section used to store Structure related tools.
     *
     * @param nodeTools
     *            optional list of {@link NodeTool}
     *
     * @return the {@link ToolSection}.
     */
    public NodeToolSection buildStructureSection(NodeTool... nodeTools) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name(ToolConstants.STRUCTURE)
                .nodeTools(nodeTools)
                .build();
    }

    /**
     * Return a tool section used to store Behavior related tools.
     *
     * @param nodeTools
     *            optional list of {@link NodeTool}
     *
     * @return the {@link ToolSection}.
     */
    public NodeToolSection buildBehaviorSection(NodeTool... nodeTools) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name(ToolConstants.BEHAVIOR)
                .nodeTools(nodeTools)
                .build();
    }

    /**
     * Return a tool section used to store Analysis related tools.
     *
     * @param nodeTools
     *            optional list of {@link NodeTool}
     *
     * @return the {@link ToolSection}.
     */
    public NodeToolSection buildAnalysisSection(NodeTool... nodeTools) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name(ToolConstants.ANALYSIS)
                .nodeTools(nodeTools)
                .build();
    }

    /**
     * Return a tool section used to store Extension related tools.
     *
     * @param nodeTools
     *            optional list of {@link NodeTool}
     *
     * @return the {@link ToolSection}.
     */
    public NodeToolSection buildExtensionSection(NodeTool... nodeTools) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name(ToolConstants.EXTENSION)
                .nodeTools(nodeTools)
                .build();
    }

    /**
     * Return a tool section used to store Behavior related tools.
     *
     * @param nodeTools
     *            optional list of {@link NodeTool}
     *
     * @return the {@link ToolSection}.
     */
    public NodeToolSection buildViewAsSection(NodeTool... nodeTools) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name(ToolConstants.VIEW_AS)
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
                .expression(
                        AQLUtils.getSelfServiceCallExpression("dropElementFromExplorer",
                                List.of(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE)));

        return this.diagramBuilderHelper.newDropTool()
                .name("Drop from Explorer")
                .body(dropElementFromExplorer.build())
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
        return this.createNodeToolWithDirection(nodeDescription, eClass, null, null);
    }

    /**
     * Returns the creation node tool description for the given Node Description to build a new node for the given
     * EClass.
     * <p>
     * This method creates a {@link FeatureMembership}, use
     * {@link #createNodeTool(NodeDescription, EClass, EClass, NodeContainmentKind)} to specify the membership type to
     * create.
     * </p>
     *
     * @param nodeDescription
     *            the Node Description where the returned tool is added
     * @param eClass
     *            the EClass that the returned tool is in charge of
     * @param nodeKind
     *            the kind of the node associated to the EClass that is built by the returned tool
     * @return the created node tool
     */
    public NodeTool createNodeTool(NodeDescription nodeDescription, EClass eClass, NodeContainmentKind nodeKind) {
        return this.createNodeToolWithDirection(nodeDescription, eClass, nodeKind, null);
    }

    /**
     * Returns the creation node tool description for the given Node Description to build a new node for the given
     * EClass.
     *
     * @param nodeDescription
     *            the Node Description where the returned tool is added
     * @param eClass
     *            the EClass that the returned tool is in charge of
     * @param membershipEClass
     *            the EClass of the membership to create
     * @param nodeKind
     *            the kind of the node associated to the EClass that is built by the returned tool
     * @return the created node tool
     */
    public NodeTool createNodeTool(NodeDescription nodeDescription, EClass eClass, EClass membershipEClass, NodeContainmentKind nodeKind) {
        return this.createNodeToolWithDirection(nodeDescription, eClass, membershipEClass, nodeKind, null);
    }

    /**
     * Returns the creation node tool description for the given Node Description to build a new node for the given
     * EClass (which must be a {@link Feature}).
     * <p>
     * This method creates a {@link FeatureMembership}, use
     * {@link #createNodeToolWithDirection(NodeDescription, EClass, EClass, NodeContainmentKind, FeatureDirectionKind)}
     * to specify the membership type to create.
     * </p>
     *
     * @param nodeDescription
     *            the Node Description where the returned tool is added
     * @param eClass
     *            the EClass that the returned tool is in charge of
     * @param nodeKind
     *            the kind of the node associated to the EClass that is build by the returned tool
     * @param direction
     *            the feature direction
     * @return the created node tool
     */
    public NodeTool createNodeToolWithDirection(NodeDescription nodeDescription, EClass eClass, NodeContainmentKind nodeKind, FeatureDirectionKind direction) {
        return this.createNodeToolWithDirection(nodeDescription, eClass, SysmlPackage.eINSTANCE.getFeatureMembership(), nodeKind, direction);
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

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("expose",
                        List.of(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE)));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(NEW_INSTANCE, SERVICE_ELEMENT_INITIALIZER));

        if (direction != null) {
            changeContextNewInstance.children(setDirection.build(), updateExposedElements.build());
        } else {
            changeContextNewInstance.children(updateExposedElements.build());
        }

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createMembership"))
                .children(createEClassInstance.build());

        var changeContextViewUsageOwner = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("getViewUsageOwner"))
                .children(changeContextMembership.build());

        var changeContextRoot = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self")
                .children(changeContextViewUsageOwner.build());

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
                .body(changeContextRoot.build())
                .elementsToSelectExpression("aql:newInstance")
                .preconditionExpression(AQLUtils.getSelfServiceCallExpression("toolShouldBeAvailable",
                        List.of(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, SysMLMetamodelHelper.buildQualifiedName(eClass))))
                .build();
    }

    /**
     * Returns the creation node tool description for the given Node Description to build a new node for the given
     * EClass (which must be a {@link Feature}).
     *
     * @param nodeDescription
     *            the Node Description where the returned tool is added
     * @param eClass
     *            the EClass that the returned tool is in charge of
     * @param membershipEClass
     *            the EClass of the membership to create
     * @param nodeKind
     *            the kind of the node associated to the EClass that is built by the returned tool
     * @param direction
     *            the feature direction
     * @return the created node tool
     */
    public NodeTool createNodeToolWithDirection(NodeDescription nodeDescription, EClass eClass, EClass membershipEClass, NodeContainmentKind nodeKind, FeatureDirectionKind direction) {
        // make sure that the given element is a feature to avoid error at runtime.
        if (!SysmlPackage.eINSTANCE.getFeature().isSuperTypeOf(eClass) && direction != null) {
            return this.createNodeTool(nodeDescription, eClass, nodeKind);
        }

        var builder = this.diagramBuilderHelper.newNodeTool();

        var setDirection = this.viewBuilderHelper.newSetValue().featureName("direction");

        if (direction != null) {
            setDirection.valueExpression(direction.getLiteral());
        }

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("expose",
                        List.of(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE)));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(NEW_INSTANCE, SERVICE_ELEMENT_INITIALIZER));

        if (direction != null) {
            changeContextNewInstance.children(setDirection.build(), updateExposedElements.build());
        } else {
            changeContextNewInstance.children(updateExposedElements.build());
        }

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newFeatureMembership")
                .children(createEClassInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(membershipEClass))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newFeatureMembership")
                .children(changeContextMembership.build());

        var changeContextViewUsageOwner = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("getViewUsageOwner"))
                .children(createMembership.build());

        var changeContextRoot = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self")
                .children(changeContextViewUsageOwner.build());

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
                .body(changeContextRoot.build())
                .elementsToSelectExpression("aql:newInstance")
                .preconditionExpression(AQLUtils.getSelfServiceCallExpression("toolShouldBeAvailable",
                        List.of(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, SysMLMetamodelHelper.buildQualifiedName(eClass))))
                .build();
    }
}
