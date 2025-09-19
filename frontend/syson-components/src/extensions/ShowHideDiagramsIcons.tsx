/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import { DiagramPanelActionProps } from '@eclipse-sirius/sirius-components-diagrams';
import Checkbox from '@mui/material/Checkbox';
import Tooltip from '@mui/material/Tooltip';
import { useEffect, useState } from 'react';

import {
  GQLErrorPayload,
  GQLSetShowDiagramsIconsPayload,
  GQLShowDiagramsIconsMutationData,
  GQLShowDiagramsIconsMutationInput,
  GQLShowDiagramsIconsMutationVariables,
  ShowHideDiagramsIconsState,
} from './ShowHideDiagramsIcons.types';
import { useShowDiagramsIcons } from './useShowDiagramsIcons';

const setShowDiagramsIconsMutation = gql`
  mutation showDiagramsIcons($input: ShowDiagramsIconsInput!) {
    showDiagramsIcons(input: $input) {
      __typename
      ... on ShowDiagramsIconsSuccessPayload {
        show
      }
      ... on ErrorPayload {
        message
      }
    }
  }
`;

const isErrorPayload = (payload: GQLSetShowDiagramsIconsPayload): payload is GQLErrorPayload =>
  payload.__typename === 'ErrorPayload';

const showTooltip: string = 'Hide Icons in Diagrams';
const hideTooltip: string = 'Show Icons in Diagrams';

export const ShowHideDiagramsIcons = ({ editingContextId, diagramId }: DiagramPanelActionProps) => {
  const [state, setState] = useState<ShowHideDiagramsIconsState>({
    checked: null,
    tooltip: 'Show/Hide Icons in Diagrams',
    message: null,
  });

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    updateDiagramIconsVisibility(event.target.checked);
  };

  useEffect(() => {
    const timeout = setTimeout(() => {
      const urlParams = new URLSearchParams(window.location.search);
      if (urlParams.has('showIcons') && urlParams.get('showIcons') === 'false') {
        updateDiagramIconsVisibility(false);
      }
    }, 200);

    return () => clearTimeout(timeout);
  }, []);

  const updateDiagramIconsVisibility = (show: boolean) => {
    const input: GQLShowDiagramsIconsMutationInput = {
      id: crypto.randomUUID(),
      editingContextId,
      representationId: diagramId,
      show,
    };
    showDiagramsIcons({ variables: { input } });
    setState((prevState) => {
      const checked: boolean = show;
      return { ...prevState, checked, tooltip: checked ? showTooltip : hideTooltip };
    });
  };

  const { data: queryData, loading: queryLoading } = useShowDiagramsIcons();
  if (!queryLoading && queryData && state.checked === null) {
    const showDiagramsIcons: boolean = queryData?.viewer.showDiagramsIconsValue;
    if (showDiagramsIcons !== state.checked) {
      setState((prevState) => {
        return { ...prevState, checked: showDiagramsIcons, tooltip: showDiagramsIcons ? showTooltip : hideTooltip };
      });
    }
  }

  const [showDiagramsIcons, { loading, data, error }] = useMutation<
    GQLShowDiagramsIconsMutationData,
    GQLShowDiagramsIconsMutationVariables
  >(setShowDiagramsIconsMutation);

  useEffect(() => {
    if (!loading) {
      if (error) {
        setState((prevState) => {
          return { ...prevState, message: 'An unexpected error has occurred, please refresh the page' };
        });
      }
      if (data) {
        const { showDiagramsIcons } = data;
        if (showDiagramsIcons.show !== null && showDiagramsIcons.show !== undefined) {
          setState((prevState) => {
            return { ...prevState, checked: showDiagramsIcons.show };
          });
        }
        if (isErrorPayload(showDiagramsIcons)) {
          setState((prevState) => {
            return { ...prevState, message: showDiagramsIcons.message };
          });
        }
      }
    }
  }, [loading, error, data]);

  return (
    <Tooltip title={state.tooltip} placement="left">
      <Checkbox checked={state.checked !== null ? state.checked : true} onChange={handleChange} />
    </Tooltip>
  );
};
