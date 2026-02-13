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

import org.eclipse.sirius.components.collaborative.diagrams.dto.DropOnDiagramInput;
import org.eclipse.sirius.components.graphql.tests.api.GraphQLResult;
import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.components.graphql.tests.api.IMutationRunner;
import org.springframework.stereotype.Service;

/**
 * Used to drop elements on a diagram. Based on
 * {@link org.eclipse.sirius.components.diagrams.tests.graphql.DropOnDiagramMutationRunner} but also fetches any
 * feedback message returned by the operation, and returns the full {@link GraphQLResult} to the caller for further
 * inspection if needed.
 *
 * @author pcdavid
 */
@Service
public class DropOnDiagramWithMessagesMutationRunner implements IMutationRunner<DropOnDiagramInput> {

    private static final String DROP_ON_DIAGRAM_MUTATION = """
            mutation dropOnDiagram($input: DropOnDiagramInput!) {
              dropOnDiagram(input: $input) {
                __typename
                ... on DropOnDiagramSuccessPayload {
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

    public DropOnDiagramWithMessagesMutationRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public GraphQLResult run(DropOnDiagramInput input) {
        return this.graphQLRequestor.execute(DROP_ON_DIAGRAM_MUTATION, input);
    }
}
