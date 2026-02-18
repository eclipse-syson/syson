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
import Checkbox from '@mui/material/Checkbox';
import Tooltip from '@mui/material/Tooltip';
import { useEffect, useState } from 'react';

import { DiagramToolbarActionProps } from '@eclipse-sirius/sirius-components-diagrams';
import {
  GQLErrorPayload,
  GQLSetShowDiagramsInheritedMembersPayload,
  GQLShowDiagramsInheritedMembersMutationData,
  GQLShowDiagramsInheritedMembersMutationInput,
  GQLShowDiagramsInheritedMembersMutationVariables,
  ShowHideDiagramsInheritedMembersState,
} from './ShowHideDiagramsInheritedMembers.types';
import { useShowDiagramsInheritedMembers } from './useShowDiagramsInheritedMembers';

const setShowDiagramsInheritedMembersMutation = gql`
  mutation showDiagramsInheritedMembers($input: ShowDiagramsInheritedMembersInput!) {
    showDiagramsInheritedMembers(input: $input) {
      __typename
      ... on ShowDiagramsInheritedMembersSuccessPayload {
        show
      }
      ... on ErrorPayload {
        message
      }
    }
  }
`;

const isErrorPayload = (payload: GQLSetShowDiagramsInheritedMembersPayload): payload is GQLErrorPayload =>
  payload.__typename === 'ErrorPayload';

const showTooltip: string = 'Hide Inherited Members in Diagrams';
const hideTooltip: string = 'Show Inherited Members in Diagrams';

export const ShowHideDiagramsInheritedMembers = ({ editingContextId, diagramId }: DiagramToolbarActionProps) => {
  const [state, setState] = useState<ShowHideDiagramsInheritedMembersState>({
    checked: null,
    tooltip: 'Show/Hide Inherited Members in Diagrams',
    message: null,
  });

  const handleChange = (event) => {
    const input: GQLShowDiagramsInheritedMembersMutationInput = {
      id: crypto.randomUUID(),
      editingContextId,
      representationId: diagramId,
      show: event.target.checked,
    };
    showDiagramsInheritedMembers({ variables: { input } });
    setState((prevState) => {
      const checked: boolean = event.target.checked;
      return { ...prevState, checked, tooltip: checked ? showTooltip : hideTooltip };
    });
  };

  const { data: queryData, loading: queryLoading } = useShowDiagramsInheritedMembers();
  if (!queryLoading && queryData && state.checked === null) {
    const showDiagramsInheritedMembers: boolean = queryData?.viewer.showDiagramsInheritedMembersValue;
    if (showDiagramsInheritedMembers !== state.checked) {
      setState((prevState) => {
        return {
          ...prevState,
          checked: showDiagramsInheritedMembers,
          tooltip: showDiagramsInheritedMembers ? showTooltip : hideTooltip,
        };
      });
    }
  }

  const [showDiagramsInheritedMembers, { loading, data, error }] = useMutation<
    GQLShowDiagramsInheritedMembersMutationData,
    GQLShowDiagramsInheritedMembersMutationVariables
  >(setShowDiagramsInheritedMembersMutation);

  useEffect(() => {
    if (!loading) {
      if (error) {
        setState((prevState) => {
          return { ...prevState, message: 'An unexpected error has occurred, please refresh the page' };
        });
      }
      if (data) {
        const { showDiagramsInheritedMembers } = data;
        if (showDiagramsInheritedMembers.show !== null && showDiagramsInheritedMembers.show !== undefined) {
          setState((prevState) => {
            return { ...prevState, checked: showDiagramsInheritedMembers.show };
          });
        }
        if (isErrorPayload(showDiagramsInheritedMembers)) {
          setState((prevState) => {
            return { ...prevState, message: showDiagramsInheritedMembers.message };
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
        data-testid={'ShowHideDiagramInheritedMembersCheckbox'}
      />
    </Tooltip>
  );
};
