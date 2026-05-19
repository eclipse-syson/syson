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
import { useMultiToast } from '@eclipse-sirius/sirius-components-core';
import { useEffect } from 'react';
import {
  GQLDeleteExpressionData,
  GQLDeleteExpressionInput,
  GQLDeleteExpressionPayload,
  GQLDeleteExpressionVariables,
  GQLErrorPayload,
  UseDeleteExpressionValue,
} from './useDeleteExpression.types';

const deleteExpressionMutation = gql`
  mutation deleteExpression($input: DeleteExpressionInput!) {
    deleteExpression(input: $input) {
      __typename
      ... on ErrorPayload {
        message
      }
    }
  }
`;

const isErrorPayload = (payload: GQLDeleteExpressionPayload): payload is GQLErrorPayload =>
  payload.__typename === 'ErrorPayload';

export const useDeleteExpression = (): UseDeleteExpressionValue => {
  const [performDeleteExpression, { data, error }] = useMutation<GQLDeleteExpressionData, GQLDeleteExpressionVariables>(
    deleteExpressionMutation
  );

  const { addErrorMessage } = useMultiToast();
  useEffect(() => {
    if (error) {
      addErrorMessage('An error has occurred while executing this action, please contact the server administrator');
    }
    if (data) {
      const { deleteExpression } = data;
      if (isErrorPayload(deleteExpression)) {
        addErrorMessage(deleteExpression.message);
      }
    }
  }, [error, data]);

  const deleteExpression = (editingContextId: string, parentElementId: string) => {
    const input: GQLDeleteExpressionInput = {
      id: crypto.randomUUID(),
      editingContextId,
      parentElementId,
    };

    performDeleteExpression({ variables: { input } });
  };

  return {
    deleteExpression,
  };
};
