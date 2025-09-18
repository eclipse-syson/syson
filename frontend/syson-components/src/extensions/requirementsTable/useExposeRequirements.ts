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

import { gql, useMutation } from '@apollo/client';
import { GQLMessage, useMultiToast } from '@eclipse-sirius/sirius-components-core';
import { useEffect } from 'react';
import {
  GQLErrorPayload,
  GQLExposeRequirementsMutationData,
  GQLExposeRequirementsPayload,
  UseExposeRequirementsValue,
} from './useExposeRequirements.types';

const exposeRequirementsMutation = gql`
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
`;

const isErrorPayload = (payload: GQLExposeRequirementsPayload): payload is GQLErrorPayload =>
  payload.__typename === 'ErrorPayload';

export const useExposeRequirements = (): UseExposeRequirementsValue => {
  const { addErrorMessage, addMessages } = useMultiToast();

  const [mutationExposeRequirements, { loading, data, error }] =
    useMutation<GQLExposeRequirementsMutationData>(exposeRequirementsMutation);

  useEffect(() => {
    if (error) {
      addErrorMessage('An unexpected error has occurred, please refresh the page');
    }
    if (data) {
      const { exposeRequirements } = data;
      if (isErrorPayload(exposeRequirements)) {
        const { messages } = exposeRequirements;
        addMessages(messages);
      }
    }
  }, [data, error]);

  const exposeRequirements = (editingContextId: string, tableId: string) => {
    const input = {
      id: crypto.randomUUID(),
      editingContextId,
      tableId,
    };
    mutationExposeRequirements({ variables: { input } });
  };

  const messages: GQLMessage[] = data?.exposeRequirements.messages || [];
  return {
    exposeRequirements,
    loading,
    messages,
  };
};
