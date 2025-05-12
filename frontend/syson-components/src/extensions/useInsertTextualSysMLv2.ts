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
  GQLInsertTextualSysMLv2MutationData,
  GQLInsertTextualSysMLv2Payload,
  UseInsertTextualSysMLv2Value,
} from './useInsertTextualSysML.v2types';

const insertTextualSysMLv2Mutation = gql`
  mutation insertTextualSysMLv2($input: InsertTextualSysMLv2Input!) {
    insertTextualSysMLv2(input: $input) {
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

const isErrorPayload = (payload: GQLInsertTextualSysMLv2Payload): payload is GQLErrorPayload =>
  payload.__typename === 'ErrorPayload';

export const useInsertTextualSysMLv2 = (): UseInsertTextualSysMLv2Value => {
  const { addErrorMessage, addMessages } = useMultiToast();

  const [performInsertTextualSysMLv2, { loading, data, error }] =
    useMutation<GQLInsertTextualSysMLv2MutationData>(insertTextualSysMLv2Mutation);

  useEffect(() => {
    if (error) {
      addErrorMessage('An unexpected error has occurred, please refresh the page');
    }
    if (data) {
      const { insertTextualSysMLv2 } = data;
      if (isErrorPayload(insertTextualSysMLv2)) {
        const { messages } = insertTextualSysMLv2;
        addMessages(messages);
      }
    }
  }, [data, error]);

  const insertTextualSysMLv2 = (editingContextId: string, objectId: string, textualContent: string) => {
    const input = {
      id: crypto.randomUUID(),
      editingContextId,
      objectId,
      textualContent,
    };

    performInsertTextualSysMLv2({ variables: { input } });
  };

  const textualSysMLv2Inserted: boolean = data?.insertTextualSysMLv2.__typename === 'SuccessPayload';
  const messages: GQLMessage[] = data?.insertTextualSysMLv2.messages || [];
  return {
    insertTextualSysMLv2,
    loading,
    textualSysMLv2Inserted,
    messages,
  };
};
