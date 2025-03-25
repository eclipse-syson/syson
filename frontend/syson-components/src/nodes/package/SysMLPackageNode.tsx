/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
 * This code has been fully inspired from PackageNode.tsx in https://github.com/PapyrusSirius/papyrus-web
 */
import { getCSSColor } from '@eclipse-sirius/sirius-components-core';
import {
  ConnectionCreationHandles,
  ConnectionHandles,
  ConnectionTargetHandle,
  DiagramContext,
  DiagramContextValue,
  DiagramElementPalette,
  Label,
  useConnectorNodeStyle,
  useDrop,
  useDropNodeStyle,
  useRefreshConnectionHandles,
} from '@eclipse-sirius/sirius-components-diagrams';
import { Theme, useTheme } from '@mui/material/styles';
import { Node, NodeProps, NodeResizer } from '@xyflow/react';
import React, { memo, useContext } from 'react';

import { NodeComponentsMap, SysMLPackageNodeData } from './SysMLPackageNode.types';

const sysMLPackageNodeStyle = (
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
    border: 'none',
    backgroundColor: 'transparent',
  };

  if (selected || hovered) {
    packageContainerStyle.outline = `${theme.palette.selected} solid 1px`;
  }

  return packageContainerStyle;
};

const packageHeaderStyle = (
  theme: Theme,
  style: React.CSSProperties,
  selected: boolean,
  hovered: boolean,
  faded: boolean
): React.CSSProperties => {
  const packageHeaderStyle: React.CSSProperties = {
    display: 'inline-flex',
    flexDirection: 'row',
    flexWrap: 'nowrap',
    padding: '0 16px 0 0',
    width: 'fit-content',
    maxWidth: '70%',
    opacity: faded ? '0.4' : '',
    ...style,
    background: getCSSColor(String(style.background), theme),
    borderBottomStyle: 'none',
    borderRightColor: getCSSColor(String(style.borderColor), theme),
    borderLeftColor: getCSSColor(String(style.borderColor), theme),
    borderTopColor: getCSSColor(String(style.borderColor), theme),
    overflowX: 'clip',
  };

  if (selected || hovered) {
    packageHeaderStyle.outline = `${theme.palette.selected} solid 1px`;
  }

  return packageHeaderStyle;
};

const packageContainerStyle = (
  theme: Theme,
  style: React.CSSProperties,
  selected: boolean,
  hovered: boolean,
  faded: boolean
): React.CSSProperties => {
  const packageNodeStyle: React.CSSProperties = {
    display: 'flex',
    padding: '8px',
    width: '100%',
    height: '100%',
    opacity: faded ? '0.4' : '',
    ...style,
    background: getCSSColor(String(style.background), theme),
    borderColor: getCSSColor(String(style.borderColor), theme),
  };

  if (selected || hovered) {
    packageNodeStyle.outline = `${theme.palette.selected} solid 1px`;
  }

  return packageNodeStyle;
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

export const SysMLPackageNode: NodeComponentsMap['sysMLPackageNode'] = memo(
  ({ data, id, selected, dragging }: NodeProps<Node<SysMLPackageNodeData>>) => {
    const { readOnly } = useContext<DiagramContextValue>(DiagramContext);
    const theme: Theme = useTheme();
    const { onDrop, onDragOver } = useDrop();
    const { style: connectionFeedbackStyle } = useConnectorNodeStyle(id, data.nodeDescription.id);
    const { style: dropFeedbackStyle } = useDropNodeStyle(data.isDropNodeTarget, data.isDropNodeCandidate, dragging);

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
            isVisible={!!selected}
            shouldResize={() => !data.isBorderNode}
            keepAspectRatio={data.nodeDescription?.keepAspectRatio}
          />
        ) : null}
        <div
          style={{
            ...sysMLPackageNodeStyle(theme, data.style, !!selected, data.isHovered, data.faded),
          }}
          onDragOver={onDragOver}
          onDrop={handleOnDrop}
          data-testid={`SysMLPackage - ${data?.insideLabel?.text}`}>
          {!!selected ? (
            <DiagramElementPalette
              diagramElementId={id}
              targetObjectId={data.targetObjectId}
              labelId={data.insideLabel ? data.insideLabel.id : null}
            />
          ) : null}
          {!!selected ? <ConnectionCreationHandles nodeId={id} /> : null}
          <ConnectionTargetHandle nodeId={id} nodeDescription={data.nodeDescription} isHovered={data.isHovered} />
          <ConnectionHandles connectionHandles={data.connectionHandles} />
          <div
            style={{
              ...packageHeaderStyle(theme, data.style, !!selected, data.isHovered, data.faded),
              ...connectionFeedbackStyle,
              ...dropFeedbackStyle,
            }}
            data-svg="rect">
            {data.insideLabel ? <Label diagramElementId={id} label={label} faded={data.faded} /> : null}
          </div>
          <div
            style={{
              ...packageContainerStyle(theme, data.style, !!selected, data.isHovered, data.faded),
              ...connectionFeedbackStyle,
              ...dropFeedbackStyle,
            }}
            data-svg="rect"
          />
        </div>
      </>
    );
  }
);
