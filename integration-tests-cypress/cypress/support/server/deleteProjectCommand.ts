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

import { DeleteProjectData, DeleteProjectVariables } from './deleteProjectCommand.types';
import { MutationResponse } from './graphql.types';

const url = Cypress.env('baseAPIUrl') + '/api/graphql';

Cypress.Commands.add('deleteProject', (projectId) => {
  const query = `
    mutation deleteProject($input: DeleteProjectInput!) {
      deleteProject(input: $input) {
        __typename
      }
    }
    `;

  const variables: DeleteProjectVariables = {
    input: {
      id: crypto.randomUUID(),
      projectId,
    },
  };
  return cy
    .request<MutationResponse<DeleteProjectData>>({
      method: 'POST',
      url,
      body: { query, variables },
    })
    .then((resp) => {
      expect(resp.status).to.eq(200);
    });
});
