/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
import { GQLMessage } from '@eclipse-sirius/sirius-components-core';
import {
  GQLCreateExpressionData,
  GQLCreateExpressionInput,
  GQLCreateExpressionPayload,
  GQLCreateExpressionSuccessPayload,
  GQLCreateExpressionVariables,
  UseCreateExpressionValue,
} from './useCreateExpression.types';

const createExpressionMutation = gql`
  mutation createExpression($input: CreateExpressionInput!) {
    createExpression(input: $input) {
      __typename
      ... on ErrorPayload {
        messages {
          body
          level
        }
      }
      ... on CreateExpressionSuccessPayload {
        newExpressionId
        messages {
          body
          level
        }
      }
    }
  }
`;

const isSuccessPayload = (payload: GQLCreateExpressionPayload): payload is GQLCreateExpressionSuccessPayload =>
  payload.__typename === 'CreateExpressionSuccessPayload';

export const useCreateExpression = (): UseCreateExpressionValue => {
  const [performCreateExpression, { loading, data, error }] = useMutation<
    GQLCreateExpressionData,
    GQLCreateExpressionVariables
  >(createExpressionMutation);

  const createExpression = (editingContextId: string, parentElementId: string, expressionText: string) => {
    const input: GQLCreateExpressionInput = {
      id: crypto.randomUUID(),
      editingContextId,
      parentElementId,
      expressionText,
    };
    performCreateExpression({ variables: { input } });
  };

  let messages: GQLMessage[] | null = null;
  let newExpressionId: string | null = null;

  if (error) {
    messages = [{ body: 'An unexpected error has occurred, please refresh the page', level: 'ERROR' }];
  }
  if (data) {
    const { createExpression } = data;
    messages = createExpression.messages;
    if (isSuccessPayload(createExpression)) {
      newExpressionId = createExpression.newExpressionId;
    }
  }

  return {
    createExpression,
    loading,
    newExpressionId,
    messages,
  };
};
