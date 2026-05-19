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

import java.util.Objects;

import org.eclipse.sirius.components.graphql.tests.api.GraphQLResult;
import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.components.graphql.tests.api.IMutationRunner;
import org.eclipse.syson.sysml.dto.EditExpressionInput;
import org.springframework.stereotype.Service;

/**
 * Used to invoke the editExpression mutation via the GraphQL API.
 *
 * @author pcdavid
 */
@Service
public class EditExpressionMutationRunner implements IMutationRunner<EditExpressionInput> {

    private static final String EDIT_EXPRESSION_MUTATION = """
            mutation editExpression($input: EditExpressionInput!) {
              editExpression(input: $input) {
                __typename
                ... on ErrorPayload {
                  messages {
                    body
                    level
                  }
                }
                ... on EditExpressionSuccessPayload {
                  newExpressionId
                  messages {
                    body
                    level
                  }
                }
              }
            }
            """;

    private final IGraphQLRequestor graphQLRequestor;

    public EditExpressionMutationRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public GraphQLResult run(EditExpressionInput input) {
        return this.graphQLRequestor.execute(EDIT_EXPRESSION_MUTATION, input);
    }
}
