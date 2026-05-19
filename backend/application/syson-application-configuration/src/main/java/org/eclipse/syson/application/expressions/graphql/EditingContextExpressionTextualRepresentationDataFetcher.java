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
package org.eclipse.syson.application.expressions.graphql;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.sirius.components.annotations.spring.graphql.QueryDataFetcher;
import org.eclipse.sirius.components.graphql.api.IDataFetcherWithFieldCoordinates;
import org.eclipse.sirius.components.graphql.api.IEditingContextDispatcher;
import org.eclipse.syson.application.expressions.dto.ExpressionTextualRepresentationInput;
import org.eclipse.syson.application.expressions.dto.ExpressionTextualRepresentationPayload;

import graphql.schema.DataFetchingEnvironment;

/**
 * Data fetcher for the field {@code EditingContext#expressionTextualRepresentation} to fetch the textual representation
 * of a SysMLv2 expression.
 *
 * @author pcdavid
 */
@QueryDataFetcher(type = "EditingContext", field = "expressionTextualRepresentation")
public class EditingContextExpressionTextualRepresentationDataFetcher implements IDataFetcherWithFieldCoordinates<CompletableFuture<String>> {

    private static final String ELEMENT_ID_ARGUMENT = "elementId";

    private final IEditingContextDispatcher editingContextDispatcher;

    public EditingContextExpressionTextualRepresentationDataFetcher(IEditingContextDispatcher editingContextDispatcher) {
        this.editingContextDispatcher = Objects.requireNonNull(editingContextDispatcher);
    }

    @Override
    public CompletableFuture<String> get(DataFetchingEnvironment environment) throws Exception {
        String editingContextId = environment.getSource();
        String elementId = environment.getArgument(ELEMENT_ID_ARGUMENT);

        ExpressionTextualRepresentationInput input = new ExpressionTextualRepresentationInput(UUID.randomUUID(), editingContextId, elementId);
        return this.editingContextDispatcher.dispatchQuery(input.editingContextId(), input)
                .filter(ExpressionTextualRepresentationPayload.class::isInstance)
                .map(ExpressionTextualRepresentationPayload.class::cast)
                .map(ExpressionTextualRepresentationPayload::textualRepresentation)
                .toFuture();
    }
}
