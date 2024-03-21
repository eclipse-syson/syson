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

import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Node tool provider for elements inside compartments.
 *
 * @author gdaniel
 */
public class CompartmentNodeToolProvider implements INodeToolProvider {

    private DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final EReference eReference;

    private final IDescriptionNameGenerator nameGenerator;

    public CompartmentNodeToolProvider(EReference eReference, IDescriptionNameGenerator nameGenerator) {
        this.eReference = Objects.requireNonNull(eReference);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var creationCompartmentItemServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.createCompartmentItem('" + this.eReference.getName() + "')");

        return builder.name(this.nameGenerator.getCreationToolName(this.eReference))
                .iconURLsExpression("/icons/full/obj16/" + this.eReference.getEType().getName() + ".svg")
                .body(creationCompartmentItemServiceCall.build())
                .build();
    }
}
