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
import org.eclipse.syson.table.requirements.view.dto.ExposeRequirementsInput;
import org.springframework.stereotype.Service;

/**
 * Used to invoke a expose requirements table action with the GraphQL API.
 *
 * @author arichard
 */
@Service
public class ExposeRequirementsMutationRunner implements IMutationRunner<ExposeRequirementsInput> {

    private static final String EXPOSE_REQUIREMENTS_MUTATION = """
            mutation exposeRequirements($input: ExposeRequirementsInput!) {
                exposeRequirements(input: $input) {
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

    public ExposeRequirementsMutationRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public String run(ExposeRequirementsInput input) {
        return this.graphQLRequestor.execute(EXPOSE_REQUIREMENTS_MUTATION, input);
    }
}