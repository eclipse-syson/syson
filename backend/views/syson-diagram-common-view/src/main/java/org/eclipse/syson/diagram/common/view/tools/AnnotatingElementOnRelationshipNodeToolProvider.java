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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node tool provider for creating an {@link AnnotatingElement} on a {@link Relationship}.
 *
 * @author arichard
 */
public class AnnotatingElementOnRelationshipNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    // The kind of AnnotatingElement to instantiate.
    private final EClass annotatingElement;

    private final NodeDescription nodeDescription;

    public AnnotatingElementOnRelationshipNodeToolProvider(NodeDescription nodeDescription, EClass annotatingElement, IDescriptionNameGenerator descriptionNameGenerator) {
        this.nodeDescription = Objects.requireNonNull(nodeDescription);
        this.annotatingElement = Objects.requireNonNull(annotatingElement);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("newInstance", "elementInitializer"));

        var parentViewExpression = AQLUtils.getSelfServiceCallExpression("getParentNode", List.of("selectedNode", "selectedEdge", IDiagramContext.DIAGRAM_CONTEXT));

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(this.nodeDescription)
                .parentViewExpression(parentViewExpression)
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var setAnnotatedElement = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getAnnotatingElement_AnnotatedElement().getName())
                .valueExpression("aql:relationshipElt");

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(this.annotatingElement))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(createView.build(), changeContextNewInstance.build());

        var changeContextAnnotation = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newAnnotation")
                .children(setAnnotatedElement.build(), createEClassInstance.build());

        var createAnnotation = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getAnnotation()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newAnnotation")
                .children(changeContextAnnotation.build());

        var storeRoot = this.viewBuilderHelper.newLet()
                .variableName("relationshipElt")
                .valueExpression("aql:self")
                .children(createAnnotation.build());

        var changeContextRoot = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self")
                .children(storeRoot.build());

        String toolLabel = this.descriptionNameGenerator.getCreationToolName(this.annotatingElement);

        StringBuilder iconPath = new StringBuilder();
        iconPath.append("/icons/full/obj16/");
        iconPath.append(this.annotatingElement.getName());
        iconPath.append(".svg");

        return builder
                .name(toolLabel)
                .iconURLsExpression(iconPath.toString())
                .body(changeContextRoot.build())
                .elementsToSelectExpression("aql:newInstance")
                .build();
    }
}
