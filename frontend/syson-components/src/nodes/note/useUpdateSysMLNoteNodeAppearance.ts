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
  GQLEditSysMLNoteNodeAppearanceData,
  GQLEditSysMLNoteNodeAppearanceVariables,
  GQLSysMLNoteNodeAppearanceInput,
  UseUpdateSysMLNoteNodeAppearanceValue,
} from './useUpdateSysMLNoteNodeAppearance.types';

export const editSysMLNoteNodeAppearanceMutation = gql`
  mutation editSysMLNoteNodeAppearance($input: EditSysMLNoteNodeAppearanceInput!) {
    editSysMLNoteNodeAppearance(input: $input) {
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

export const useUpdateSysMLNoteNodeAppearance = (): UseUpdateSysMLNoteNodeAppearanceValue => {
  const [editSysMLNoteNodeAppearance, editSysMLNoteNodeAppearanceResult] = useMutation<
    GQLEditSysMLNoteNodeAppearanceData,
    GQLEditSysMLNoteNodeAppearanceVariables
  >(editSysMLNoteNodeAppearanceMutation);

  useReporting(
    editSysMLNoteNodeAppearanceResult,
    (data: GQLEditSysMLNoteNodeAppearanceData) => data.editSysMLNoteNodeAppearance
  );

  const updateSysMLNoteNodeAppearance = (
    editingContextId: string,
    representationId: string,
    nodeIds: string[],
    appearance: Partial<GQLSysMLNoteNodeAppearanceInput>
  ) =>
    editSysMLNoteNodeAppearance({
      variables: {
        input: {
          id: crypto.randomUUID(),
          editingContextId,
          representationId,
          nodeIds,
          appearance,
        },
      },
    });

  return {
    updateSysMLNoteNodeAppearance,
  };
};
