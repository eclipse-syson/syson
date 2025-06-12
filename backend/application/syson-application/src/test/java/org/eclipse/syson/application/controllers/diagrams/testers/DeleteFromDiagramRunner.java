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
package org.eclipse.syson.application.controllers.diagrams.testers;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DeleteFromDiagramInput;
import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.components.graphql.tests.api.IMutationRunner;
import org.springframework.stereotype.Service;

/**
 * Used to invoke a remove from diagram tool with the GraphQL API.
 *
 * <p>
 * This class should be provided by Sirius Web, remove this once
 * https://github.com/eclipse-sirius/sirius-web/issues/5041 is closed
 * </p>
 *
 * @author Arthur Daussy
 */
@Service
public class DeleteFromDiagramRunner implements IMutationRunner<DeleteFromDiagramInput> {

    private static final String REMOVE_FROM_DIAGRAM_MUTATION = """
            mutation deleteFromDiagram($input: DeleteFromDiagramInput!) {
              deleteFromDiagram(input: $input) {
                __typename
                ... on ErrorPayload {
                  messages {
                    body
                    level
                    __typename
                  }
                  __typename
                }
                ... on DeleteFromDiagramSuccessPayload {
                  messages {
                    body
                    level
                    __typename
                  }
                  __typename
                }
              }
            }
            """;

    private final IGraphQLRequestor graphQLRequestor;

    public DeleteFromDiagramRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public String run(DeleteFromDiagramInput input) {
        return this.graphQLRequestor.execute(REMOVE_FROM_DIAGRAM_MUTATION, input);
    }

}
