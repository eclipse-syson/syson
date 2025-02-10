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
import {
  GetCurrentEditingContextIdtData,
  GetCurrentEditingContextIdVariables,
} from './getCurrentEditingContextId.types';
import { QueryResponse } from './graphql.types';
const url = Cypress.env('baseAPIUrl') + '/api/graphql';
Cypress.Commands.add('getCurrentEditingContextId', (projectId: string) => {
  const query = `
    query getCurrentEditingContextId($projectId: ID!) {
      viewer {
        project(projectId: $projectId) {
          currentEditingContext {
            id
          }
        }
      }
    }
  `;
  const variables: GetCurrentEditingContextIdVariables = {
    projectId,
  };
  const body = {
    query,
    variables,
  };
  return cy.request<QueryResponse<GetCurrentEditingContextIdtData>>({
    method: 'POST',
    url,
    body,
  });
});
