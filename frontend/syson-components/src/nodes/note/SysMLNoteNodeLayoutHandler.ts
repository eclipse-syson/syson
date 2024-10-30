/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
 * This code has been fully inspired from NoteNodeLayoutHandler.ts in https://github.com/PapyrusSirius/papyrus-web.
 */
import {
  Diagram,
  DiagramNodeType,
  ForcedDimensions,
  ILayoutEngine,
  INodeLayoutHandler,
  NodeData,
  applyRatioOnNewNodeSizeValue,
  computePreviousSize,
  findNodeIndex,
  getDefaultOrMinHeight,
} from '@eclipse-sirius/sirius-components-diagrams';
import { Node } from '@xyflow/react';
import { SysMLNoteNodeData } from './SysMLNoteNode.types';

export class SysMLNoteNodeLayoutHandler implements INodeLayoutHandler<SysMLNoteNodeData> {
  public canHandle(node: Node<NodeData, DiagramNodeType>): boolean {
    return node.type === 'sysMLNoteNode';
  }

  public handle(
    _layoutEngine: ILayoutEngine,
    previousDiagram: Diagram | null,
    node: Node<SysMLNoteNodeData, 'sysMLNoteNode'>,
    visibleNodes: Node<NodeData, DiagramNodeType>[],
    _directChildren: Node<NodeData, DiagramNodeType>[],
    _newlyAddedNode: Node<NodeData, DiagramNodeType> | undefined,
    forceDimensions?: ForcedDimensions
  ) {
    const nodeIndex = findNodeIndex(visibleNodes, node.id);
    const nodeElement = document.getElementById(`${node.id}-sysMLNoteNode-${nodeIndex}`)?.children[0];
    const borderWidth = nodeElement ? parseFloat(window.getComputedStyle(nodeElement).borderWidth) : 1;

    const labelElement = document.getElementById(`${node.id}-label-${nodeIndex}`);

    const labelHeight = (labelElement?.getBoundingClientRect().height ?? 0) + borderWidth * 2;

    const nodeMinComputeHeight = labelHeight + borderWidth * 2;

    const nodeWith = forceDimensions?.width ?? node?.width ?? 200;
    const nodeHeight = forceDimensions?.height ?? getDefaultOrMinHeight(nodeMinComputeHeight, node);

    const previousNode = (previousDiagram?.nodes ?? []).find((previouseNode) => previouseNode.id === node.id);
    const previousDimensions = computePreviousSize(previousNode, node);
    if (node.data.resizedByUser) {
      if (nodeWith > previousDimensions.width) {
        node.width = nodeWith;
      } else {
        node.width = previousDimensions.width;
      }
      if (nodeMinComputeHeight > previousDimensions.height) {
        node.height = nodeMinComputeHeight;
      } else {
        node.height = previousDimensions.height;
      }
    } else {
      node.width = nodeWith;
      node.height = nodeHeight;
    }

    if (node.data.nodeDescription?.keepAspectRatio) {
      applyRatioOnNewNodeSizeValue(node);
    }
  }
}
