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
package org.eclipse.syson.application.controllers.diagrams.graphql;

import java.util.Objects;

import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.components.graphql.tests.api.IMutationRunner;
import org.eclipse.syson.table.requirements.view.dto.CreateRequirementInput;
import org.springframework.stereotype.Service;

/**
 * Used to invoke a create requirement table action with the GraphQL API.
 *
 * @author arichard
 */
@Service
public class CreateRequirementMutationRunner implements IMutationRunner<CreateRequirementInput> {

    private static final String CREATE_REQUIREMENT_MUTATION = """
            mutation createRequirement($input: CreateRequirementInput!) {
                createRequirement(input: $input) {
                  __typename
                  ... on ErrorPayload {
                    messages {
                      body
                      level
                    }
                  }
                  ... on SuccessPayload {
                    messages {
                      body
                      level
                    }
                  }
                }
            }
            """;

    private final IGraphQLRequestor graphQLRequestor;

    public CreateRequirementMutationRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public String run(CreateRequirementInput input) {
        return this.graphQLRequestor.execute(CREATE_REQUIREMENT_MUTATION, input);
    }
}