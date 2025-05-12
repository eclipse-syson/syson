/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.application.imports;

import java.util.Objects;

import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.components.graphql.tests.api.IMutationRunner;
import org.eclipse.syson.sysml.dto.InsertTextualSysMLv2Input;
import org.springframework.stereotype.Service;

/**
 * Runner for the 'insertTextualSysMLv2' query.
 *
 * @author Arthur Daussy
 */
@Service
public class MutationInsertTextualSysMLv2DataRunner implements IMutationRunner<InsertTextualSysMLv2Input> {

    private static final String QUERY = """
            mutation insertTextualSysMLv2($input: InsertTextualSysMLv2Input!) {
                    insertTextualSysMLv2(input: $input) {
                      __typename
                          ... on SuccessPayload {
                              messages {
                                level
                                body
                              }
                            }
                        ... on ErrorPayload {
                          messages {
                            level
                            body
                          }
                        }
                    }
                }
            """;

    private final IGraphQLRequestor graphQLRequestor;

    public MutationInsertTextualSysMLv2DataRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public String run(InsertTextualSysMLv2Input input) {
        return this.graphQLRequestor.execute(QUERY, input);
    }
}
