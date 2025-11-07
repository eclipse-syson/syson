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
import { getCSSColor } from '@eclipse-sirius/sirius-components-core';
import {
  ConnectionCreationHandles,
  ConnectionHandles,
  ConnectionTargetHandle,
  DiagramContext,
  DiagramContextValue,
  Label,
  useConnectionLineNodeStyle,
  useConnectorNodeStyle,
  useDrop,
  useDropNodeStyle,
  useRefreshConnectionHandles,
} from '@eclipse-sirius/sirius-components-diagrams';
import { Theme, useTheme } from '@mui/material/styles';
import { Node, NodeProps, NodeResizer } from '@xyflow/react';
import React, { memo, useContext } from 'react';

import { NodeComponentsMap, SysMLViewFrameNodeData } from './SysMLViewFrameNode.types';

const sysMLViewFrameNodeStyle = (
  theme: Theme,
  style: React.CSSProperties,
  selected: boolean,
  hovered: boolean,
  faded: boolean
): React.CSSProperties => {
  const packageContainerStyle: React.CSSProperties = {
    display: 'flex',
    padding: '0px',
    width: '100%',
    height: '100%',
    opacity: faded ? '0.4' : '',
    ...style,
    backgroundColor: 'transparent',
  };

  if (selected || hovered) {
    packageContainerStyle.outline = `${theme.palette.selected} solid 1px`;
  }

  return packageContainerStyle;
};

const viewFrameNameCompartmentStyle = (
  theme: Theme,
  style: React.CSSProperties,
  faded: boolean
): React.CSSProperties => {
  return {
    display: 'flex',
    position: 'absolute',
    top: 0,
    left: 0,
    width: '70%',
    padding: '4px 8px',
    opacity: faded ? '0.4' : '',
    ...style,
    background: getCSSColor(String(style.background), theme),
    overflowX: 'clip',
    borderBottomLeftRadius: '0px',
    borderTopRightRadius: '0px',
  };
};

const viewFrameContainerStyle = (theme: Theme, style: React.CSSProperties, faded: boolean): React.CSSProperties => {
  return {
    display: 'flex',
    padding: '8px',
    width: '100%',
    height: '100%',
    opacity: faded ? '0.4' : '',
    ...style,
    background: getCSSColor(String(style.background), theme),
    borderWidth: '0px',
  };
};

const resizeLineStyle = (theme: Theme): React.CSSProperties => {
  return { borderWidth: theme.spacing(0.15) };
};

const resizeHandleStyle = (theme: Theme): React.CSSProperties => {
  return {
    width: theme.spacing(1),
    height: theme.spacing(1),
    borderRadius: '100%',
  };
};

export const SysMLViewFrameNode: NodeComponentsMap['sysMLViewFrameNode'] = memo(
  ({ data, id, selected, dragging }: NodeProps<Node<SysMLViewFrameNodeData>>) => {
    const { readOnly } = useContext<DiagramContextValue>(DiagramContext);
    const theme: Theme = useTheme();
    const { onDrop, onDragOver } = useDrop();
    const { style: connectionFeedbackStyle } = useConnectorNodeStyle(id, data.nodeDescription.id);
    const { style: dropFeedbackStyle } = useDropNodeStyle(data.isDropNodeTarget, data.isDropNodeCandidate, dragging);
    const { style: connectionLineActiveNodeStyle } = useConnectionLineNodeStyle(data.connectionLinePositionOnNode);

    const handleOnDrop = (event: React.DragEvent) => {
      onDrop(event, id);
    };

    const label: any = {
      ...data.insideLabel,
      style: {
        ...data?.insideLabel?.style,
        whiteSpace: 'pre',
        overflow: 'hidden',
        paddingRight: '0',
        justifyContent: 'flex-start',
        textAlign: 'left',
      },
    };

    useRefreshConnectionHandles(id, data.connectionHandles);

    return (
      <>
        {data.nodeDescription?.userResizable && !readOnly ? (
          <NodeResizer
            handleStyle={{ ...resizeHandleStyle(theme) }}
            lineStyle={{ ...resizeLineStyle(theme) }}
            color={theme.palette.selected}
            isVisible={selected}
            shouldResize={() => !data.isBorderNode}
            keepAspectRatio={data.nodeDescription?.keepAspectRatio}
          />
        ) : null}
        <div
          style={{
            ...sysMLViewFrameNodeStyle(theme, data.style, selected, data.isHovered, data.faded),
          }}
          onDragOver={onDragOver}
          onDrop={handleOnDrop}
          data-testid={`SysMLViewFrame - ${data?.insideLabel?.text}`}
          data-svg="rect">
          {selected ? <ConnectionCreationHandles nodeId={id} /> : null}
          <ConnectionTargetHandle nodeId={id} nodeDescription={data.nodeDescription} isHovered={data.isHovered} />
          <ConnectionHandles connectionHandles={data.connectionHandles} />
          <div
            style={{
              ...viewFrameNameCompartmentStyle(theme, data.style, data.faded),
              ...connectionFeedbackStyle,
              ...dropFeedbackStyle,
              ...connectionLineActiveNodeStyle,
            }}
            data-svg="rect">
            {data.insideLabel ? <Label diagramElementId={id} label={label} faded={data.faded} /> : null}
          </div>
          <div
            style={{
              ...viewFrameContainerStyle(theme, data.style, data.faded),
              ...connectionFeedbackStyle,
              ...dropFeedbackStyle,
              ...connectionLineActiveNodeStyle,
            }}></div>
        </div>
      </>
    );
  }
);
