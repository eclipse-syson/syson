/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.textual.view.handlers;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventHandler;
import org.eclipse.sirius.components.collaborative.api.IRepresentationMetadataPersistenceService;
import org.eclipse.sirius.components.collaborative.dto.CreateRepresentationInput;
import org.eclipse.sirius.components.collaborative.dto.CreateRepresentationSuccessPayload;
import org.eclipse.sirius.components.core.RepresentationMetadata;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.representations.IRepresentation;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.syson.textual.view.description.TextualViewDescription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Handler used to create a new textual view.
 *
 * @author gcoutable
 */
@Service
public class CreateTextualViewEventHandler implements IEditingContextEventHandler {

    private final IObjectSearchService objectSearchService;

    private final IRepresentationMetadataPersistenceService metadataPersistenceService;

    private final boolean enabled;

    public CreateTextualViewEventHandler(IObjectSearchService objectSearchService, IRepresentationMetadataPersistenceService metadataPersistenceService,
            @Value("${org.eclipse.syson.experimental.textual.representation.enabled:false}") boolean enabled) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.metadataPersistenceService = Objects.requireNonNull(metadataPersistenceService);
        this.enabled = enabled;
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IInput input) {
        return input instanceof CreateRepresentationInput creation && TextualViewDescription.ID.equals(creation.representationDescriptionId()) && enabled;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeSink, IEditingContext editingContext, IInput input) {
        if (input instanceof CreateRepresentationInput createRepresentationInput) {
            var variables = new VariableManager();
            this.objectSearchService.getObject(editingContext, createRepresentationInput.objectId()).ifPresent(object -> variables.put(VariableManager.SELF, object));
            var description = new TextualViewDescription();
            if (!description.getCanCreatePredicate().test(variables)) {
                payloadSink.tryEmitValue(new ErrorPayload(input.id(), "Select a SysML v2 element to create a textual representation."));
                changeSink.tryEmitNext(new ChangeDescription(ChangeKind.NOTHING, editingContext.getId(), input));
                return;
            }
            var metadata = RepresentationMetadata.newRepresentationMetadata(UUID.randomUUID().toString())
                    .kind(IRepresentation.KIND_PREFIX + "?type=TextualView")
                    .label(createRepresentationInput.representationName())
                    .descriptionId(description.getId())
                    .iconURLs(List.of())
                    .build();
            this.metadataPersistenceService.save(input, editingContext, metadata, createRepresentationInput.objectId());
            payloadSink.tryEmitValue(new CreateRepresentationSuccessPayload(input.id(), metadata));
            changeSink.tryEmitNext(new ChangeDescription(ChangeKind.REPRESENTATION_CREATION, editingContext.getId(), input));
        }
    }
}
