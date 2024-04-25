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

import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create element as a payload of an accept action usage.
 *
 * @author Jerome Gout
 */
public class AcceptActionPayloadNodeToolProvider implements INodeToolProvider {

    private final EClass payloadEClass;

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator nameGenerator;

    public AcceptActionPayloadNodeToolProvider(EClass payloadEClass, IDescriptionNameGenerator nameGenerator) {
        this.payloadEClass = Objects.requireNonNull(payloadEClass);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var creationPayloadServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".createAcceptActionPayload('" + this.payloadEClass.getName() + "')")
                .build();

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(creationPayloadServiceCall)
                .build();

        return builder.name(this.nameGenerator.getCreationToolName("New {0} as Payload", this.payloadEClass))
                .iconURLsExpression("/icons/full/obj16/" + this.payloadEClass.getName() + ".svg")
                .body(rootChangContext)
                .preconditionExpression(AQLConstants.AQL_SELF + ".isEmptyAcceptActionUsagePayload()")
                .build();
    }
}
