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
package org.eclipse.syson.diagram.common.view.services.handlers;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.IInputPreProcessor;
import org.eclipse.sirius.components.collaborative.api.IRepresentationEventProcessor;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.LayoutDiagramInput;
import org.eclipse.sirius.components.collaborative.editingcontext.EditingContextEventProcessorRegistry;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

/**
 * {@link IInputPreProcessor} that tries to create a Node for each ViewUsage#exposedElements that doesn't have a Node
 * yet. Only works on the diagram background for now. Works in combination with
 * {@link SynchronizedExposedElementsEventHandler}.
 *
 * @author arichard
 */
@Service
public class SynchronizeExposedElementsInputProcessor implements IInputPreProcessor {

    private final ApplicationContext applicationContext;

    public SynchronizeExposedElementsInputProcessor(ApplicationContext applicationContext) {
        this.applicationContext = Objects.requireNonNull(applicationContext);
    }

    @Override
    public IInput preProcess(IEditingContext editingContext, IInput input, Many<ChangeDescription> changeDescriptionSink) {
        if (editingContext instanceof EditingContext && input instanceof LayoutDiagramInput layoutDiagramInput && DiagramRefreshedEventPayload.CAUSE_REFRESH.equals(layoutDiagramInput.cause())) {
            EditingContextEventProcessorRegistry eventProcessorRegistry = this.applicationContext.getBean(EditingContextEventProcessorRegistry.class);
            Optional<IRepresentationEventProcessor> optionalRepresentationEventProcessor = eventProcessorRegistry.getEditingContextEventProcessors().stream()
                    .filter(p -> p.getEditingContextId().equals(editingContext.getId()))
                    .findFirst()
                    .flatMap(p -> p.getRepresentationEventProcessors().stream()
                            .filter(rep -> layoutDiagramInput.representationId().equals(rep.getRepresentation().getId()))
                            .findFirst());
            if (optionalRepresentationEventProcessor.isPresent()) {
                var diagramInput = new SynchronizedExposedElementsDiagramInput(input.id(), layoutDiagramInput.representationId());
                optionalRepresentationEventProcessor.get().handle(Sinks.one(), changeDescriptionSink, diagramInput);
            }
        }
        return input;
    }
}
