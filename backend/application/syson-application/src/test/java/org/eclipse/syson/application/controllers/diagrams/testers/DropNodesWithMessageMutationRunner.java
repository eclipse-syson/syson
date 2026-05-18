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
package org.eclipse.syson.application.controllers.diagrams.testers;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DropNodesInput;
import org.eclipse.sirius.components.graphql.tests.api.GraphQLResult;
import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.components.graphql.tests.api.IMutationRunner;
import org.springframework.stereotype.Service;
/**
 * Same as {@link org.eclipse.sirius.components.diagrams.tests.graphql.DropNodesMutationRunner} but with feedback messages.
 *
 * @author Arthur Daussy
 */
@Service
public class DropNodesWithMessageMutationRunner implements IMutationRunner<DropNodesInput> {
    private static final String DROP_NODE_MUTATION = """
            mutation dropNodes($input: DropNodesInput!) {
              dropNodes(input: $input) {
                __typename
                ... on SuccessPayload {
                  messages {
                    body
                    level
                  }
                }
                ... on ErrorPayload {
                  message
                  messages {
                    body
                    level
                  }
                }
              }
            }
            """;

    private final IGraphQLRequestor graphQLRequestor;

    public DropNodesWithMessageMutationRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public GraphQLResult run(DropNodesInput input) {
        return this.graphQLRequestor.execute(DROP_NODE_MUTATION, input);
    }
}
