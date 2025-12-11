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
import { CreateDocumentData, CreateDocumentVariables } from './createDocumentCommand.types';
import { MutationResponse } from './graphql.types';

const url = Cypress.env('baseAPIUrl') + '/api/graphql';

Cypress.Commands.add('createDocument', (editingContextId: string, stereotypeId: string, name: string) => {
  const query = `
  mutation createDocument($input: CreateDocumentInput!) {
    createDocument(input: $input) {
      __typename
      ... on CreateDocumentSuccessPayload {
        id
      }
    }
  }
  `;
  const variables: CreateDocumentVariables = {
    input: {
      id: crypto.randomUUID(),
      editingContextId,
      stereotypeId,
      name,
    },
  };

  const body = {
    query,
    variables,
  };
  return cy.request<MutationResponse<CreateDocumentData>>({
    method: 'POST',
    url,
    body,
  });
});
