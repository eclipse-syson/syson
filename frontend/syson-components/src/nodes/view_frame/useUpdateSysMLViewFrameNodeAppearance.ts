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
import { useReporting } from '@eclipse-sirius/sirius-components-core';
import {
  GQLEditSysMLViewFrameNodeAppearanceData,
  GQLEditSysMLViewFrameNodeAppearanceVariables,
  GQLSysMLViewFrameNodeAppearanceInput,
  UseUpdateSysMLViewFrameNodeAppearanceValue,
} from './useUpdateSysMLViewFrameNodeAppearance.types';

export const editSysMLViewFrameNodeAppearanceMutation = gql`
  mutation editSysMLViewFrameNodeAppearance($input: EditSysMLViewFrameNodeAppearanceInput!) {
    editSysMLViewFrameNodeAppearance(input: $input) {
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

export const useUpdateSysMLViewFrameNodeAppearance = (): UseUpdateSysMLViewFrameNodeAppearanceValue => {
  const [editSysMLViewFrameNodeAppearance, editSysMLViewFrameNodeAppearanceResult] = useMutation<
    GQLEditSysMLViewFrameNodeAppearanceData,
    GQLEditSysMLViewFrameNodeAppearanceVariables
  >(editSysMLViewFrameNodeAppearanceMutation);

  useReporting(
    editSysMLViewFrameNodeAppearanceResult,
    (data: GQLEditSysMLViewFrameNodeAppearanceData) => data.editSysMLViewFrameNodeAppearance
  );

  const updateSysMLViewFrameNodeAppearance = (
    editingContextId: string,
    representationId: string,
    nodeId: string,
    appearance: Partial<GQLSysMLViewFrameNodeAppearanceInput>
  ) =>
    editSysMLViewFrameNodeAppearance({
      variables: {
        input: {
          id: crypto.randomUUID(),
          editingContextId,
          representationId,
          nodeId,
          appearance,
        },
      },
    });

  return {
    updateSysMLViewFrameNodeAppearance,
  };
};
