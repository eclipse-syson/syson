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

import {
  CreateProjectFromTemplateData,
  CreateProjectFromTemplatePayload,
  CreateProjectFromTemplateSuccessPayload,
  CreateProjectFromTemplateVariables,
} from './createProjectFromTemplateCommand.types';
import { MutationResponse } from './graphql.types';

const url = Cypress.env('baseAPIUrl') + '/api/graphql';

export const isCreateProjectFromTemplateSuccessPayload = (
  payload: CreateProjectFromTemplatePayload
): payload is CreateProjectFromTemplateSuccessPayload =>
  payload.__typename === 'CreateProjectFromTemplateSuccessPayload';

Cypress.Commands.add('createProjectFromTemplate', (name: string, templateId: string, natures: string[]) => {
  const query = `
  mutation createProjectFromTemplate($input: CreateProjectFromTemplateInput!) {
    createProjectFromTemplate(input: $input) {
      __typename
      ... on CreateProjectFromTemplateSuccessPayload {
        project {
          id
        }
        representationToOpen {
          id
        }
      }
      ... on ErrorPayload {
        message
      }
    }
  }
  `;
  const variables: CreateProjectFromTemplateVariables = {
    input: {
      id: crypto.randomUUID(),
      name,
      templateId,
      natures,
    },
  };

  const body = {
    query,
    variables,
  };
  return cy.request<MutationResponse<CreateProjectFromTemplateData>>({
    method: 'POST',
    url,
    body,
  });
});
