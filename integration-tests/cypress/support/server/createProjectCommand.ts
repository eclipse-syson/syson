/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import {
  CreateProjectData,
  CreateProjectPayload,
  CreateProjectSuccessPayload,
  CreateProjectVariables,
} from './createProjectCommand.types';
import { MutationResponse } from './graphql.types';

const url = Cypress.env('baseAPIUrl') + '/api/graphql';

export const isCreateProjectSuccessPayload = (payload: CreateProjectPayload): payload is CreateProjectSuccessPayload =>
  payload.__typename === 'CreateProjectSuccessPayload';

Cypress.Commands.add('createProject', (name) => {
  const query = `
   mutation createProject($input: CreateProjectInput!) {
     createProject(input: $input) {
       __typename
       ... on CreateProjectSuccessPayload {
         project {
           id
         }
       }
     }
   }
   `;
  const variables: CreateProjectVariables = {
    input: {
      id: crypto.randomUUID(),
      name,
      natures: [],
    },
  };

  const body = {
    query,
    variables,
  };
  return cy.request<MutationResponse<CreateProjectData>>({
    method: 'POST',
    url,
    body,
  });
});
