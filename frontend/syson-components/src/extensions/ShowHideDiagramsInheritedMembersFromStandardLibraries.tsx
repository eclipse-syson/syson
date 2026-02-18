/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import { DiagramToolbarActionProps } from '@eclipse-sirius/sirius-components-diagrams';
import Checkbox from '@mui/material/Checkbox';
import Tooltip from '@mui/material/Tooltip';
import { useEffect, useState } from 'react';

import {
  GQLErrorPayload,
  GQLSetShowDiagramsInheritedMembersFromStandardLibrariesPayload,
  GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationData,
  GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationInput,
  GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationVariables,
  ShowHideDiagramsInheritedMembersFromStandardLibrariesState,
} from './ShowHideDiagramsInheritedMembersFromStandardLibraries.types';
import { useShowDiagramsInheritedMembersFromStandardLibraries } from './useShowDiagramsInheritedMembersFromStandardLibraries';

const setShowDiagramsInheritedMembersFromStandardLibrariesMutation = gql`
  mutation showDiagramsInheritedMembersFromStandardLibraries(
    $input: ShowDiagramsInheritedMembersFromStandardLibrariesInput!
  ) {
    showDiagramsInheritedMembersFromStandardLibraries(input: $input) {
      __typename
      ... on ShowDiagramsInheritedMembersFromStandardLibrariesSuccessPayload {
        show
      }
      ... on ErrorPayload {
        message
      }
    }
  }
`;

const isErrorPayload = (
  payload: GQLSetShowDiagramsInheritedMembersFromStandardLibrariesPayload
): payload is GQLErrorPayload => payload.__typename === 'ErrorPayload';

const showTooltip: string = 'Hide Inherited Members from Standard Libraries in Diagrams';
const hideTooltip: string = 'Show Inherited Members from Standard Libraries in Diagrams';

export const ShowHideDiagramsInheritedMembersFromStandardLibraries = ({
  editingContextId,
  diagramId,
}: DiagramToolbarActionProps) => {
  const [state, setState] = useState<ShowHideDiagramsInheritedMembersFromStandardLibrariesState>({
    checked: null,
    tooltip: 'Show/Hide Inherited Members from Standard Libraries in Diagrams',
    message: null,
  });

  const handleChange = (event) => {
    const input: GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationInput = {
      id: crypto.randomUUID(),
      editingContextId,
      representationId: diagramId,
      show: event.target.checked,
    };
    showDiagramsInheritedMembersFromStandardLibraries({ variables: { input } });
    setState((prevState) => {
      const checked: boolean = event.target.checked;
      return { ...prevState, checked, tooltip: checked ? showTooltip : hideTooltip };
    });
  };

  const { data: queryData, loading: queryLoading } = useShowDiagramsInheritedMembersFromStandardLibraries();
  if (!queryLoading && queryData && state.checked === null) {
    const showDiagramsInheritedMembersFromStandardLibraries: boolean =
      queryData?.viewer.showDiagramsInheritedMembersFromStandardLibrariesValue;
    if (showDiagramsInheritedMembersFromStandardLibraries !== state.checked) {
      setState((prevState) => {
        return {
          ...prevState,
          checked: showDiagramsInheritedMembersFromStandardLibraries,
          tooltip: showDiagramsInheritedMembersFromStandardLibraries ? showTooltip : hideTooltip,
        };
      });
    }
  }

  const [showDiagramsInheritedMembersFromStandardLibraries, { loading, data, error }] = useMutation<
    GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationData,
    GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationVariables
  >(setShowDiagramsInheritedMembersFromStandardLibrariesMutation);

  useEffect(() => {
    if (!loading) {
      if (error) {
        setState((prevState) => {
          return { ...prevState, message: 'An unexpected error has occurred, please refresh the page' };
        });
      }
      if (data) {
        const { showDiagramsInheritedMembersFromStandardLibraries } = data;
        if (
          showDiagramsInheritedMembersFromStandardLibraries.show !== null &&
          showDiagramsInheritedMembersFromStandardLibraries.show !== undefined
        ) {
          setState((prevState) => {
            return { ...prevState, checked: showDiagramsInheritedMembersFromStandardLibraries.show };
          });
        }
        if (isErrorPayload(showDiagramsInheritedMembersFromStandardLibraries)) {
          setState((prevState) => {
            return { ...prevState, message: showDiagramsInheritedMembersFromStandardLibraries.message };
          });
        }
      }
    }
  }, [loading, error, data]);

  return (
    <Tooltip title={state.tooltip} placement="left">
      <Checkbox
        checked={state.checked !== null ? state.checked : true}
        onChange={handleChange}
        data-testid={'ShowHideDiagramInheritedMembersFromStandardLibrariesCheckbox'}
      />
    </Tooltip>
  );
};
