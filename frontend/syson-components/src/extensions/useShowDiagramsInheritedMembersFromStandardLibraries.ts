/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
import { GQLShowDiagramsInheritedMembersFromStandardLibrariesQueryData } from './useShowDiagramsInheritedMembersFromStandardLibraries.types';

export const showDiagramsInheritedMembersFromStandardLibrariesQuery = gql`
  query getShowDiagramsInheritedMembersFromStandardLibraries {
    viewer {
      showDiagramsInheritedMembersFromStandardLibrariesValue
    }
  }
`;

export const useShowDiagramsInheritedMembersFromStandardLibraries = () => {
  const { data, loading, error, refetch } = useQuery<GQLShowDiagramsInheritedMembersFromStandardLibrariesQueryData>(
    showDiagramsInheritedMembersFromStandardLibrariesQuery
  );

  const { addErrorMessage } = useMultiToast();
  useEffect(() => {
    if (error) {
      addErrorMessage(error.message);
    }
  }, [error]);

  const refreshShowDiagramsInheritedMembersFromStandardLibraries = () => refetch();

  return { data: data ?? null, loading, refreshShowDiagramsInheritedMembersFromStandardLibraries };
};
