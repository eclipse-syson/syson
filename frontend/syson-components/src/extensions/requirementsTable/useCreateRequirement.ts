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
  GQLCreateRequirementMutationData,
  GQLCreateRequirementPayload,
  GQLErrorPayload,
  UseCreateRequirementValue,
} from './useCreateRequirement.types';

const createRequirementsMutation = gql`
  mutation createRequirement($input: CreateRequirementInput!) {
    createRequirement(input: $input) {
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

const isErrorPayload = (payload: GQLCreateRequirementPayload): payload is GQLErrorPayload =>
  payload.__typename === 'ErrorPayload';

export const useCreateRequirement = (): UseCreateRequirementValue => {
  const { addErrorMessage, addMessages } = useMultiToast();

  const [mutationCreateRequirement, { loading, data, error }] =
    useMutation<GQLCreateRequirementMutationData>(createRequirementsMutation);

  useEffect(() => {
    if (error) {
      addErrorMessage('An unexpected error has occurred, please refresh the page');
    }
    if (data) {
      const { createRequirement } = data;
      if (isErrorPayload(createRequirement)) {
        const { messages } = createRequirement;
        addMessages(messages);
      }
    }
  }, [data, error]);

  const createRequirement = (editingContextId: string, tableId: string) => {
    const input = {
      id: crypto.randomUUID(),
      editingContextId,
      tableId,
    };
    mutationCreateRequirement({ variables: { input } });
  };

  const messages: GQLMessage[] = data?.createRequirement.messages || [];
  return {
    createRequirement,
    loading,
    messages,
  };
};
