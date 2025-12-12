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

import { GetLibraryIdData, GetLibraryIdVariables } from './getLibraryId.types';
import { QueryResponse } from './graphql.types';

const url = Cypress.env('baseAPIUrl') + '/api/graphql';

Cypress.Commands.add('getLibraryId', (namespace: string, name: string, version: string) => {
  const query = `
    query getLibraryId($namespace: String!, $name: String!, $version: String!) {
      viewer {
        library(namespace: $namespace, name: $name, version: $version) {
          id
        }
      }
    }
  `;
  const variables: GetLibraryIdVariables = {
    namespace,
    name,
    version,
  };

  const body = {
    query,
    variables,
  };
  return cy.request<QueryResponse<GetLibraryIdData>>({
    method: 'POST',
    url,
    body,
  });
});
