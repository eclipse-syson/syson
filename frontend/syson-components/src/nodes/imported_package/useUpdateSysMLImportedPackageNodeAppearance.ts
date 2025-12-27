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
  GQLEditSysMLImportedPackageNodeAppearanceData,
  GQLEditSysMLImportedPackageNodeAppearanceVariables,
  GQLSysMLImportedPackageNodeAppearanceInput,
  UseUpdateSysMLImportedPackageNodeAppearanceValue,
} from './useUpdateSysMLImportedPackageNodeAppearance.types';

export const editSysMLImportedPackageNodeAppearanceMutation = gql`
  mutation editSysMLImportedPackageNodeAppearance($input: EditSysMLImportedPackageNodeAppearanceInput!) {
    editSysMLImportedPackageNodeAppearance(input: $input) {
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

export const useUpdateSysMLImportedPackageNodeAppearance = (): UseUpdateSysMLImportedPackageNodeAppearanceValue => {
  const [editSysMLImportedPackageNodeAppearance, editSysMLImportedPackageNodeAppearanceResult] = useMutation<
    GQLEditSysMLImportedPackageNodeAppearanceData,
    GQLEditSysMLImportedPackageNodeAppearanceVariables
  >(editSysMLImportedPackageNodeAppearanceMutation);

  useReporting(
    editSysMLImportedPackageNodeAppearanceResult,
    (data: GQLEditSysMLImportedPackageNodeAppearanceData) => data.editSysMLImportedPackageNodeAppearance
  );

  const updateSysMLImportedPackageNodeAppearance = (
    editingContextId: string,
    representationId: string,
    nodeIds: string[],
    appearance: Partial<GQLSysMLImportedPackageNodeAppearanceInput>
  ) =>
    editSysMLImportedPackageNodeAppearance({
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
    updateSysMLImportedPackageNodeAppearance,
  };
};
