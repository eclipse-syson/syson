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
  GQLEditSysMLPackageNodeAppearanceData,
  GQLEditSysMLPackageNodeAppearanceVariables,
  GQLSysMLPackageNodeAppearanceInput,
  UseUpdateSysMLPackageNodeAppearanceValue,
} from './useUpdateSysMLPackageNodeAppearance.types';

export const editSysMLPackageNodeAppearanceMutation = gql`
  mutation editSysMLPackageNodeAppearance($input: EditSysMLPackageNodeAppearanceInput!) {
    editSysMLPackageNodeAppearance(input: $input) {
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

export const useUpdateSysMLPackageNodeAppearance = (): UseUpdateSysMLPackageNodeAppearanceValue => {
  const [editSysMLPackageNodeAppearance, editSysMLPackageNodeAppearanceResult] = useMutation<
    GQLEditSysMLPackageNodeAppearanceData,
    GQLEditSysMLPackageNodeAppearanceVariables
  >(editSysMLPackageNodeAppearanceMutation);

  useReporting(
    editSysMLPackageNodeAppearanceResult,
    (data: GQLEditSysMLPackageNodeAppearanceData) => data.editSysMLPackageNodeAppearance
  );

  const updateSysMLPackageNodeAppearance = (
    editingContextId: string,
    representationId: string,
    nodeId: string,
    appearance: Partial<GQLSysMLPackageNodeAppearanceInput>
  ) =>
    editSysMLPackageNodeAppearance({
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
    updateSysMLPackageNodeAppearance,
  };
};
