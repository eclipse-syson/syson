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
import { IconOverlay } from '@eclipse-sirius/sirius-components-core';
import { DiagramContext, DiagramContextValue, EdgeData, NodeData } from '@eclipse-sirius/sirius-components-diagrams';
import { PaletteToolOverriddenContributionComponentProps, usePalette } from '@eclipse-sirius/sirius-components-palette';
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

export const NewExpressionDiagramToolOverriddenContribution = ({
  representationElementIds,
  onInvoked,
}: PaletteToolOverriddenContributionComponentProps) => {
  const { classes } = useStyle();
  const { editingContextId, readOnly } = useContext<DiagramContextValue>(DiagramContext);
  const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
  const { hidePalette } = usePalette();
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
    hidePalette();
  };

  let modalElement: JSX.Element | null = null;
  if (modalVisible) {
    modalElement = (
      <EditSysMLExpressionModal
        editingContextId={editingContextId}
        elementId={elementId}
        mode="create"
        onClose={onClose}
      />
    );
  }

  const toolLabel = 'New Expression';
  const toolIconURL = '/api/images/palette/create.svg';

  return (
    <Fragment key="new-overridden_tool_new_expression-modal-contribution">
      <ListItemButton
        onClick={() => {
          setModalVisible(true);
          onInvoked();
        }}
        data-testid="overridden_tool_new_expression"
        disabled={readOnly}
        className={classes.listItemButton}>
        <ListItemIcon className={classes.listItemIcon}>
          <IconOverlay iconURLs={[toolIconURL]} alt={toolLabel} customIconHeight={16} customIconWidth={16} />
        </ListItemIcon>
        <ListItemText primary={toolLabel} className={classes.listItemText} />
      </ListItemButton>
      {modalElement}
    </Fragment>
  );
};
