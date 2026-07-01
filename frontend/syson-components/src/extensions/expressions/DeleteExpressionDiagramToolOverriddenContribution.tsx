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
import { IconOverlay, useDeletionConfirmationDialog } from '@eclipse-sirius/sirius-components-core';
import { DiagramContext, DiagramContextValue, EdgeData, NodeData } from '@eclipse-sirius/sirius-components-diagrams';
import { PaletteToolOverriddenContributionComponentProps, usePalette } from '@eclipse-sirius/sirius-components-palette';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import { Edge, InternalNode, Node, useStoreApi } from '@xyflow/react';
import { useContext } from 'react';
import { makeStyles } from 'tss-react/mui';
import { useDeleteExpression } from './useDeleteExpression';

const useStyle = makeStyles()((theme) => ({
  listItemText: {
    '& .MuiListItemText-primary': {
      whiteSpace: 'nowrap',
      overflow: 'hidden',
      textOverflow: 'ellipsis',
    },
  },
  listItemButton: {
    paddingTop: 0,
    paddingBottom: 0,
  },
  listItemIcon: {
    minWidth: 0,
    marginRight: theme.spacing(2),
  },
}));

export const DeleteExpressionDiagramToolOverriddenContribution = ({
  representationElementIds,
  onInvoked,
}: PaletteToolOverriddenContributionComponentProps) => {
  const { classes } = useStyle();
  const { editingContextId, readOnly } = useContext<DiagramContextValue>(DiagramContext);
  const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
  const { deleteExpression } = useDeleteExpression();
  const { showDeletionConfirmation } = useDeletionConfirmationDialog();
  const { hidePalette } = usePalette();

  let elementId = '';
  const targetedNodes: InternalNode<Node<NodeData>>[] = representationElementIds
    .map((elementId) => store.getState().nodeLookup.get(elementId))
    .filter((element): element is InternalNode<Node<NodeData>> => !!element);

  if (targetedNodes.length === 1 && targetedNodes[0] !== null) {
    elementId = targetedNodes[0]?.data.targetObjectId || '';
  } else {
    const targetedEdges: Edge<EdgeData>[] = representationElementIds
      .map((elementId) => store.getState().edgeLookup.get(elementId))
      .filter((element): element is Edge<EdgeData> => !!element);
    const targetedEdge = targetedEdges[0];
    if (targetedEdges.length === 1 && targetedEdge) {
      elementId = targetedEdge.data?.targetObjectId || '';
    } else {
      return null;
    }
  }

  const handleDeleteExpression = () => {
    showDeletionConfirmation(() => {
      deleteExpression(editingContextId, elementId);
      hidePalette();
    });
    onInvoked();
  };

  const toolLabel = 'Delete Expression';
  const toolIconURL = '/api/images/diagram-images/semanticDelete.svg';

  return (
    <ListItemButton
      key="overridden_tool_delete_expression"
      data-testid="overridden_tool_delete_expression"
      onClick={handleDeleteExpression}
      disabled={readOnly}
      className={classes.listItemButton}>
      <ListItemIcon className={classes.listItemIcon}>
        <IconOverlay iconURLs={[toolIconURL]} alt={toolLabel} customIconHeight={16} customIconWidth={16} />
      </ListItemIcon>
      <ListItemText primary={toolLabel} className={classes.listItemText} />
    </ListItemButton>
  );
};
