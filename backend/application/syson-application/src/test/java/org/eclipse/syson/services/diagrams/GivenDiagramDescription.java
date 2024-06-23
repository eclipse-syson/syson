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
package org.eclipse.syson.services.diagrams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

/**
 * Retrieves the diagram description used by a test.
 *
 * @author gdaniel
 */
@Service
public class GivenDiagramDescription implements IGivenDiagramDescription {

    @Autowired
    private ExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    @Override
    public DiagramDescription getDiagramDescription(String editingContextId, String diagramDescriptionId) {
        var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), editingContextId, (editingContext, executeEditingContextFunctionInput) -> {
            var description = (DiagramDescription) this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramDescriptionId)
                    .orElse(null);
            return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), description);
        });

        Mono<IPayload> result = this.executeEditingContextFunctionRunner.execute(input);
        var payload = result.block();
        assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        ExecuteEditingContextFunctionSuccessPayload successPayload = (ExecuteEditingContextFunctionSuccessPayload) payload;
        assertThat(successPayload.result()).isInstanceOf(DiagramDescription.class);
        return (DiagramDescription) successPayload.result();
    }

}
