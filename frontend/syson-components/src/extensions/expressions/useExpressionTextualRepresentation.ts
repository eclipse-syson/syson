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

import { gql, useQuery } from '@apollo/client';
import { useMultiToast } from '@eclipse-sirius/sirius-components-core';
import { useEffect } from 'react';
import {
  GQLGetExpressionTextualRepresentationData,
  GQLGetExpressionTextualRepresentationVariables,
  UseExpressionTextualRepresentationValue,
} from './useExpressionTextualRepresentation.types';

const getExpressionTextualRepresentationQuery = gql`
  query getExpressionTextualRepresentation($editingContextId: ID!, $elementId: ID!) {
    viewer {
      editingContext(editingContextId: $editingContextId) {
        expressionTextualRepresentation(elementId: $elementId)
      }
    }
  }
`;

export const useExpressionTextualRepresentation = (
  editingContextId: string,
  elementId: string | null
): UseExpressionTextualRepresentationValue => {
  const { loading, data, error } = useQuery<
    GQLGetExpressionTextualRepresentationData,
    GQLGetExpressionTextualRepresentationVariables
  >(getExpressionTextualRepresentationQuery, {
    variables: { editingContextId, elementId: elementId || '' },
    skip: elementId === null,
  });

  const { addErrorMessage } = useMultiToast();

  useEffect(() => {
    if (error) {
      addErrorMessage(error.message);
    }
  }, [error]);

  const textualRepresentation: string | null = data?.viewer.editingContext.expressionTextualRepresentation ?? null;

  return { textualRepresentation, loading };
};
