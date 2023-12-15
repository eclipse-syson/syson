/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
  DiagramElementPalette,
  Label,
  NodeContext,
  NodeContextValue,
  useConnector,
  useDrop,
  useDropNodeStyle,
  useRefreshConnectionHandles,
} from '@eclipse-sirius/sirius-components-diagrams-reactflow';
import { Theme, useTheme } from '@material-ui/core/styles';
import React, { memo, useContext } from 'react';
import { NodeProps, NodeResizer } from 'reactflow';
import { SysMLPackageNodeData } from './SysMLPackageNode.types';

const sysMLPackageNodeStyle = (
  theme: Theme,
  style: React.CSSProperties,
  selected: boolean,
  hovered: boolean,
  faded: boolean
): React.CSSProperties => {
  const packageContainerStyle: React.CSSProperties = {
    display: 'flex',
    overflow: 'hidden',
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
  faded: boolean,
  labelWidth: number
): React.CSSProperties => {
  const packageHeaderStyle: React.CSSProperties = {
    display: 'inline-flex',
    flexDirection: 'row',
    flexWrap: 'nowrap',
    padding: '0px',
    width: 'calc(' + labelWidth + 'ch + 56px)',
    maxWidth: '45%',
    opacity: faded ? '0.4' : '',
    ...style,
    backgroundColor: getCSSColor(String(style.backgroundColor), theme),
    borderBottomStyle: 'none',
    borderRightColor: getCSSColor(String(style.borderColor), theme),
    borderLeftColor: getCSSColor(String(style.borderColor), theme),
    borderTopColor: getCSSColor(String(style.borderColor), theme),
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
    backgroundColor: getCSSColor(String(style.backgroundColor), theme),
    borderColor: getCSSColor(String(style.borderColor), theme),
  };

  if (selected || hovered) {
    packageNodeStyle.outline = `${theme.palette.selected} solid 1px`;
  }

  return packageNodeStyle;
};

export const SysMLPackageNode = memo(({ data, id, selected }: NodeProps<SysMLPackageNodeData>) => {
  const theme: Theme = useTheme();
  const { onDrop, onDragOver } = useDrop();
  const { newConnectionStyleProvider } = useConnector();
  const { style: dropFeedbackStyle } = useDropNodeStyle(id);
  const { hoveredNode } = useContext<NodeContextValue>(NodeContext);

  const handleOnDrop = (event: React.DragEvent) => {
    onDrop(event, id);
  };

  const label: any = {
    ...data.label,
    style: {
      ...data?.label?.style,
      whiteSpace: 'pre',
      overflow: 'hidden',
      justifyContent: 'flex-start',
      textAlign: 'left',
      columnGap: '16px',
      textOverflow: 'ellipsis',
      '& div': {},
    },
  };

  useRefreshConnectionHandles(id, data.connectionHandles);

  return (
    <>
      <NodeResizer color={theme.palette.selected} isVisible={selected} />
      <div
        style={{
          ...sysMLPackageNodeStyle(theme, data.style, selected, hoveredNode?.id === id, data.faded),
        }}
        onDragOver={onDragOver}
        onDrop={handleOnDrop}
        data-testid={`SysMLPackage - ${data?.label?.text}`}>
        {selected ? <DiagramElementPalette diagramElementId={id} labelId={data.label ? data.label.id : null} /> : null}
        {selected ? <ConnectionCreationHandles nodeId={id} /> : null}
        <ConnectionTargetHandle nodeId={id} />
        <ConnectionHandles connectionHandles={data.connectionHandles} />
        <div
          style={{
            ...packageHeaderStyle(
              theme,
              data.style,
              selected,
              hoveredNode?.id === id,
              data.faded,
              data.label ? data.label.text.length : 0
            ),
            ...newConnectionStyleProvider.getNodeStyle(id, data.descriptionId),
            ...dropFeedbackStyle,
          }}>
          {data.label ? <Label diagramElementId={id} label={label} faded={data.faded} transform="" /> : null}
        </div>
        <div
          style={{
            ...packageContainerStyle(theme, data.style, selected, hoveredNode?.id === id, data.faded),
            ...newConnectionStyleProvider.getNodeStyle(id, data.descriptionId),
            ...dropFeedbackStyle,
          }}
        />
      </div>
    </>
  );
});
