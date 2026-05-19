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
package org.eclipse.syson.application.controllers.expressions.graphql;

import java.util.Map;
import java.util.Objects;

import org.eclipse.sirius.components.graphql.tests.api.GraphQLResult;
import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.components.graphql.tests.api.IQueryRunner;
import org.springframework.stereotype.Service;

/**
 * User to fetch the textual representation of an expression.
 *
 * @author pcdavid
 */
@Service
public class ExpressionTextualRepresentationQueryRunner implements IQueryRunner {
    private static final String QUERY = """
            query getExpressionTextualRepresentation($editingContextId: ID!, $elementId: ID!) {
              viewer {
                editingContext(editingContextId: $editingContextId) {
                  expressionTextualRepresentation(elementId: $elementId)
                }
              }
            }
            """;

    private final IGraphQLRequestor graphQLRequestor;

    public ExpressionTextualRepresentationQueryRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public GraphQLResult run(Map<String, Object> variables) {
        return this.graphQLRequestor.execute(QUERY, variables);
    }
}
