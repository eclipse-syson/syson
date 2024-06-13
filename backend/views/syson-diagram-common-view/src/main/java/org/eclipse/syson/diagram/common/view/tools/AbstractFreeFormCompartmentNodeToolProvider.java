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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to define a creation tool for a free form compartment of an element inside diagrams.
 *
 * @author Jerome Gout
 */
public abstract class AbstractFreeFormCompartmentNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    private final EClass ownerEClass;

    private final String compartmentName;

    public AbstractFreeFormCompartmentNodeToolProvider(EClass ownerEClass, String compartmentName, IDescriptionNameGenerator descriptionNameGenerator) {
        this.ownerEClass = Objects.requireNonNull(ownerEClass);
        this.compartmentName = Objects.requireNonNull(compartmentName);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    /**
     * Implementers should provide the name of the node description associated to the view that is created in the
     * compartment.
     *
     * @return the name of the node description
     */
    protected abstract String getNodeDescriptionName();

    /**
     * Implementers should provide the name of the AQL service call expression that creates the semantic element when
     * the tool is triggered.<br>
     * This service should return the newly created semantic element.
     *
     * @return the creation service call expression as a String
     */
    protected abstract String getCreationServiceCallExpression();

    /**
     * Implementers should provide the name of the label of the tool.
     *
     * @return the user visible label of the tool.
     */
    protected abstract String getLabel();

    /**
     * Implementers should provide the name of the path of the icon associated to the tool.
     *
     * @return the icon path
     */
    protected abstract String getIconPath();

    /**
     * Implementers may provide the tool precondition service call expression used to show or hide the tool.<br>
     * This service should return <code>true</code> if the tool is available and <code>false</code> if it is not.<br>
     * By default no precondition is provided.
     *
     * @return the tool precondition service call expression.
     */
    protected String getPreconditionServiceCallExpression() {
        /// no precondition by default
        return null;
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    /**
     * Tooling method that can be used to retrieve the path of the icon to use for the given {@link EClass} element.
     *
     * @param eClass
     *            the type of the semantic element the icon path is searched for
     * @return the path of the icon of a given element type
     */
    protected String getIconPathFromType(EClass eClass) {
        return "/icons/full/obj16/" + eClass.getName() + ".svg";
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var params = List.of(
                AQLUtils.aqlString(this.getNodeDescriptionName()),
                AQLUtils.aqlString(this.descriptionNameGenerator.getNodeName(this.ownerEClass)),
                AQLUtils.aqlString(this.compartmentName),
                "selectedNode",
                "diagramContext",
                "convertedNodes");
        var creationServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(this.getCreationServiceCallExpression());

        var createViewOperation = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createViewInFreeFormCompartment", params))
                .build();

        var revealOperation = this.viewBuilderHelper.newChangeContext()
                .expression("aql:selectedNode.revealCompartment(self, diagramContext, editingContext, convertedNodes)")
                .build();

        creationServiceCall.children(createViewOperation, revealOperation);

        String preconditionExpression = this.getPreconditionServiceCallExpression();

        return builder.name(this.getLabel())
                .iconURLsExpression(this.getIconPath())
                .body(creationServiceCall.build())
                .preconditionExpression(preconditionExpression)
                .build();
    }
}
