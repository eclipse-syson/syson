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
 *
 * Used to edit a SysMLImportedPackage node's appearance.
 *
 * @author arichard
 */
@Service
public class EditSysMLImportedPackageNodeAppearanceMutationRunner implements IMutationRunner<IInput> {

    private static final String EDIT_SYSML_IMPORTED_PACKAGE_NODE_APPEARANCE_MUTATION = """
            mutation editSysMLImportedPackageNodeAppearance($input: EditSysMLImportedPackageNodeAppearanceInput!) {
                editSysMLImportedPackageNodeAppearance(input: $input) {
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

    public EditSysMLImportedPackageNodeAppearanceMutationRunner(IGraphQLRequestor graphQLRequestor) {
        this.graphQLRequestor = Objects.requireNonNull(graphQLRequestor);
    }

    @Override
    public String run(IInput input) {
        return this.graphQLRequestor.execute(EDIT_SYSML_IMPORTED_PACKAGE_NODE_APPEARANCE_MUTATION, input);
    }
}