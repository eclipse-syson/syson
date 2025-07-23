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
/*
 * This code has been fully inspired from SysMLPackageNode.tsx
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
  useConnectionLineNodeStyle,
  useConnectorNodeStyle,
  useDrop,
  useDropNodeStyle,
  useRefreshConnectionHandles,
} from '@eclipse-sirius/sirius-components-diagrams';
import { Theme, useTheme } from '@mui/material/styles';
import Typography from '@mui/material/Typography';
import { Node, NodeProps, NodeResizer } from '@xyflow/react';
import React, { memo, useContext } from 'react';

import { NodeComponentsMap, SysMLImportedPackageNodeData } from './SysMLImportedPackageNode.types';

const sysMLImportedPackageNodeStyle = (
  theme: Theme,
  style: React.CSSProperties,
  selected: boolean,
  hovered: boolean,
  faded: boolean
): React.CSSProperties => {
  const importedPackageContainerStyle: React.CSSProperties = {
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
    importedPackageContainerStyle.outline = `${theme.palette.selected} solid 1px`;
  }

  return importedPackageContainerStyle;
};

const importedPackageHeaderStyle = (
  theme: Theme,
  style: React.CSSProperties,
  selected: boolean,
  hovered: boolean,
  faded: boolean
): React.CSSProperties => {
  const importedPackageHeaderStyle: React.CSSProperties = {
    display: 'inline-flex',
    flexDirection: 'row',
    flexWrap: 'nowrap',
    padding: '0 16px 0 0',
    width: 'fit-content',
    maxWidth: '70%',
    opacity: faded ? '0.4' : '',
    ...style,
    background: getCSSColor(String(style.background), theme),
    borderStyle: 'dashed dashed none',
    borderRightColor: getCSSColor(String(style.borderColor), theme),
    borderLeftColor: getCSSColor(String(style.borderColor), theme),
    borderTopColor: getCSSColor(String(style.borderColor), theme),
    overflowX: 'clip',
  };

  if (selected || hovered) {
    importedPackageHeaderStyle.outline = `${theme.palette.selected} solid 1px`;
  }

  return importedPackageHeaderStyle;
};

const importedPackageContainerStyle = (
  theme: Theme,
  style: React.CSSProperties,
  selected: boolean,
  hovered: boolean,
  faded: boolean
): React.CSSProperties => {
  const importedPackageNodeStyle: React.CSSProperties = {
    display: 'flex',
    padding: '2px',
    width: '100%',
    height: '100%',
    opacity: faded ? '0.4' : '',
    ...style,
    background: getCSSColor(String(style.background), theme),
    borderColor: getCSSColor(String(style.borderColor), theme),
    borderStyle: 'dashed',
    alignItems: 'center',
    justifyContent: 'center',
  };

  if (selected || hovered) {
    importedPackageNodeStyle.outline = `${theme.palette.selected} solid 1px`;
  }

  return importedPackageNodeStyle;
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

export const SysMLImportedPackageNode: NodeComponentsMap['sysMLImportedPackageNode'] = memo(
  ({ data, id, selected, dragging }: NodeProps<Node<SysMLImportedPackageNodeData>>) => {
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
        overflow: 'hidden',
        paddingRight: '0',
        justifyContent: 'flex-start',
        textAlign: 'left',
      },
    };

    useRefreshConnectionHandles(id, data.connectionHandles);

    const visibilityPart = () => {
      if (data.insideLabel != null) {
        const endingQuoteIndex = data.insideLabel.text.indexOf('\u00BB');
        if (endingQuoteIndex > -1) {
          return data.insideLabel.text.substring(0, endingQuoteIndex + 1);
        }
      }
      return '';
    };

    const labelTextWithoutVisibility = () => {
      if (data.insideLabel != null) {
        const endingQuoteIndex = data.insideLabel.text.indexOf('\u00BB');
        if (endingQuoteIndex > -1) {
          return data.insideLabel.text.substring(endingQuoteIndex + 2);
        }
        return data.insideLabel.text;
      }
      return '';
    };

    const splitLabel = {
      ...label,
      text: labelTextWithoutVisibility(),
      iconURL: [],
      style: { ...label.style, paddingLeft: '0' },
    };

    const labelElement = data.insideLabel && (
      <>
        <Typography color={'black'} data-svg="text">
          {visibilityPart()}
        </Typography>
        <Label diagramElementId={id} label={splitLabel} faded={data.faded} />
      </>
    );

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
            ...sysMLImportedPackageNodeStyle(theme, data.style, !!selected, data.isHovered, data.faded),
          }}
          onDragOver={onDragOver}
          onDrop={handleOnDrop}
          data-testid={`SysMLImportedPackage - ${data?.insideLabel?.text}`}>
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
              ...importedPackageHeaderStyle(theme, data.style, !!selected, data.isHovered, data.faded),
              ...connectionFeedbackStyle,
              ...dropFeedbackStyle,
              ...connectionLineActiveNodeStyle,
            }}
            data-svg="rect">
            {data.insideLabel ? (
              <Label diagramElementId={id} label={{ ...label, text: '  ' }} faded={data.faded} />
            ) : null}
          </div>
          <div
            style={{
              ...importedPackageContainerStyle(theme, data.style, !!selected, data.isHovered, data.faded),
              ...connectionFeedbackStyle,
              ...dropFeedbackStyle,
              ...connectionLineActiveNodeStyle,
            }}
            data-svg="rect">
            {labelElement}
          </div>
        </div>
      </>
    );
  }
);
