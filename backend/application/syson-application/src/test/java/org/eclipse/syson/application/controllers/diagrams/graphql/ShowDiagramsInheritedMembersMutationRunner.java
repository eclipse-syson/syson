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

import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.components.graphql.tests.api.IMutationRunner;
import org.springframework.stereotype.Service;

/**
 * Used to show diagram inherited members with the GraphQL API.
 *
 * @author frouene
 */
@Service
public class ShowDiagramsInheritedMembersMutationRunner implements IMutationRunner<IInput> {

    private static final String SHOW_DIAGRAM_INHERITED_MEMBERS_MUTATION = """
             mutation showDiagramsInheritedMembers($input: ShowDiagramsInheritedMembersInput!) {
                showDiagramsInheritedMembers(input: $input) {
                  __typename
                  ... on ShowDiagramsInheritedMembersSuccessPayload {
                    show
                  }
                  ... on ErrorPayload {
                    message
                  }
                }
              }
            """;

    private final IGraphQLRequestor graphQLRequestor;

    public ShowDiagramsInheritedMembersMutationRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public String run(IInput input) {
        return this.graphQLRequestor.execute(SHOW_DIAGRAM_INHERITED_MEMBERS_MUTATION, input);
    }
}
