/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
/*
 * This code has been fully inspired from NoteNode.tsx in https://github.com/PapyrusSirius/papyrus-web
 */
import { getCSSColor } from '@eclipse-sirius/sirius-components-core';
import {
  ConnectionCreationHandles,
  ConnectionHandles,
  ConnectionTargetHandle,
  EdgeData,
  Label,
  NodeData,
  Resizer,
  useConnectionLineNodeStyle,
  useConnectorNodeStyle,
  useDrop,
  useDropNodeStyle,
  useRefreshConnectionHandles,
} from '@eclipse-sirius/sirius-components-diagrams';
import { Theme, useTheme } from '@mui/material/styles';
import { Edge, Node, NodeProps, useReactFlow } from '@xyflow/react';
import React, { memo } from 'react';

import { NodeComponentsMap, SysMLNoteNodeData } from './SysMLNoteNode.types';

const sysMLNoteNodeStyle = (
  theme: Theme,
  style: React.CSSProperties,
  selected: boolean,
  hovered: boolean,
  faded: boolean
): React.CSSProperties => {
  const sysMLNoteNodeStyle: React.CSSProperties = {
    display: 'flex',
    padding: '0px',
    width: '100%',
    height: '100%',
    opacity: faded ? '0.4' : '',
    ...style,
    // No border nor background color: this is handled by the SVG image
    border: 'none',
    backgroundColor: 'transparent',
  };

  if (selected || hovered) {
    sysMLNoteNodeStyle.outline = `${theme.palette.selected} solid 1px`;
  }

  return sysMLNoteNodeStyle;
};

const svgPathStyle = (theme: Theme, style: React.CSSProperties, faded: boolean): React.CSSProperties => {
  const svgPathStyle: React.CSSProperties = {
    stroke: getCSSColor(String(style.borderColor), theme),
    fill: getCSSColor(String(style.background), theme),
    fillOpacity: faded ? '0.4' : '1',
    strokeOpacity: faded ? '0.4' : '1',
    strokeWidth: style.borderWidth,
    vectorEffect: 'non-scaling-stroke',
  };
  return svgPathStyle;
};

export const SysMLNoteNode: NodeComponentsMap['sysMLNoteNode'] = memo(
  ({ data, id, selected, dragging }: NodeProps<Node<SysMLNoteNodeData>>) => {
    const theme = useTheme();
    const { onDrop, onDragOver } = useDrop();
    const { style: connectionFeedbackStyle } = useConnectorNodeStyle(id, data.nodeDescription.id);
    const { style: dropFeedbackStyle } = useDropNodeStyle(data.isDropNodeTarget, data.isDropNodeCandidate, dragging);
    const { style: connectionLineActiveNodeStyle } = useConnectionLineNodeStyle(data.connectionLinePositionOnNode);

    const reactFlowInstance = useReactFlow<Node<NodeData>, Edge<EdgeData>>();
    const node = reactFlowInstance.getNodes().find((node) => node.id === id);
    const nodeHeight = node?.height ?? 70;
    const nodeWidth = node?.width ?? 200;

    const updatedLabel: any = {
      ...data.insideLabel,
      style: {
        ...data?.insideLabel?.style,
        paddingLeft: parseInt(data.style.borderWidth?.toString() ?? '0') + 8 + 'px',
        paddingTop: parseInt(data.style.borderWidth?.toString() ?? '0') + 8 + 'px',
        paddingRight: parseInt(data.style.borderWidth?.toString() ?? '1') / 2 + 20 + 'px',
        paddingBottom: parseInt(data.style.borderWidth?.toString() ?? '0') + 8 + 'px',
        //justifyContent: 'left',
      },
    };

    const handleOnDrop = (event: React.DragEvent) => {
      onDrop(event, id);
    };

    useRefreshConnectionHandles(id, data.connectionHandles);

    const borderOffset = data.style.borderWidth ? parseInt(data.style.borderWidth.toString()) / 2 : 0;

    return (
      <>
        <Resizer data={data} selected={!!selected} />
        <div
          style={{
            ...sysMLNoteNodeStyle(theme, data.style, !!selected, data.isHovered, data.faded),
            ...connectionFeedbackStyle,
            ...dropFeedbackStyle,
            ...connectionLineActiveNodeStyle,
          }}
          onDragOver={onDragOver}
          onDrop={handleOnDrop}
          data-testid={`SysMLNote - ${data?.insideLabel?.text}`}>
          <div
            style={{
              width: '100%',
              height: '100%',
              position: 'absolute',
              top: '0px',
              left: '0px',
              zIndex: '-1',
            }}>
            <svg viewBox={`0 0 ${nodeWidth} ${nodeHeight}`} data-svg="svg">
              <path
                style={svgPathStyle(theme, data.style, data.faded)}
                d={`M ${borderOffset},${borderOffset} H ${nodeWidth - 15} L ${nodeWidth - borderOffset} 15 V ${
                  nodeHeight - borderOffset
                } H ${borderOffset} Z`}
              />
              <path
                style={{
                  ...svgPathStyle(theme, data.style, data.faded),
                  fillOpacity: 0,
                }}
                d={`M ${nodeWidth - 15},${borderOffset} V 15 H ${nodeWidth - borderOffset}`}
              />
            </svg>
          </div>
          {data.insideLabel ? <Label diagramElementId={id} label={updatedLabel} faded={data.faded} /> : null}
          {!!selected ? <ConnectionCreationHandles nodeId={id} /> : null}
          <ConnectionTargetHandle nodeId={id} nodeDescription={data.nodeDescription} isHovered={data.isHovered} />
          <ConnectionHandles connectionHandles={data.connectionHandles} />
        </div>
      </>
    );
  }
);
