/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
  GQLEditExpressionData,
  GQLEditExpressionInput,
  GQLEditExpressionPayload,
  GQLEditExpressionSuccessPayload,
  GQLEditExpressionVariables,
  UseEditExpressionValue,
} from './useEditExpression.types';

const editExpressionMutation = gql`
  mutation editExpression($input: EditExpressionInput!) {
    editExpression(input: $input) {
      __typename
      ... on ErrorPayload {
        messages {
          body
          level
        }
      }
      ... on EditExpressionSuccessPayload {
        newExpressionId
        messages {
          body
          level
        }
      }
    }
  }
`;

const isSuccessPayload = (payload: GQLEditExpressionPayload): payload is GQLEditExpressionSuccessPayload =>
  payload.__typename === 'EditExpressionSuccessPayload';

export const useEditExpression = (): UseEditExpressionValue => {
  const [performEditExpression, { loading, data, error }] = useMutation<
    GQLEditExpressionData,
    GQLEditExpressionVariables
  >(editExpressionMutation);

  const editExpression = (editingContextId: string, elementId: string, newExpressionText: string) => {
    const input: GQLEditExpressionInput = {
      id: crypto.randomUUID(),
      editingContextId,
      elementId: elementId,
      newExpressionText,
    };

    performEditExpression({ variables: { input } });
  };

  let messages: GQLMessage[] | null = null;
  let newExpressionId: string | null = null;

  if (error) {
    messages = [{ body: 'An unexpected error has occurred, please refresh the page', level: 'ERROR' }];
  }
  if (data) {
    const { editExpression } = data;
    messages = editExpression.messages;
    if (isSuccessPayload(editExpression)) {
      newExpressionId = editExpression.newExpressionId;
    }
  }

  return {
    editExpression,
    loading,
    newExpressionId,
    messages,
  };
};
