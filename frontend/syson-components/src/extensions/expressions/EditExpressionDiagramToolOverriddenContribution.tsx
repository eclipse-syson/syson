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
import {
  DiagramContext,
  DiagramContextValue,
  EdgeData,
  NodeData,
  useDiagramPalette,
} from '@eclipse-sirius/sirius-components-diagrams';
import { PaletteToolContributionComponentProps } from '@eclipse-sirius/sirius-components-palette';
import EditIcon from '@mui/icons-material/Edit';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import { Edge, InternalNode, Node, useStoreApi } from '@xyflow/react';
import { Fragment, useContext, useState } from 'react';
import { makeStyles } from 'tss-react/mui';
import { EditSysMLExpressionModal } from './EditSysMLExpressionModal';

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

export const EditExpressionDiagramToolOverriddenContribution = ({
  representationElementIds,
}: PaletteToolContributionComponentProps) => {
  const { classes } = useStyle();
  const { editingContextId, readOnly } = useContext<DiagramContextValue>(DiagramContext);
  const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
  const { hideDiagramPalette } = useDiagramPalette();
  const [modalVisible, setModalVisible] = useState<boolean>(false);

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

  const onClose = () => {
    setModalVisible(false);
    hideDiagramPalette();
  };

  let modalElement: JSX.Element | null = null;
  if (modalVisible) {
    modalElement = (
      <EditSysMLExpressionModal
        editingContextId={editingContextId}
        elementId={elementId}
        mode="edit"
        onClose={onClose}
      />
    );
  }

  return (
    <Fragment key="overridden_tool_edit_expression">
      <ListItemButton
        onClick={() => setModalVisible(true)}
        data-testid="overridden_tool_edit_expression"
        disabled={readOnly}
        className={classes.listItemButton}>
        <ListItemIcon className={classes.listItemIcon}>
          <EditIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Edit expression" className={classes.listItemText} />
      </ListItemButton>
      {modalElement}
    </Fragment>
  );
};
