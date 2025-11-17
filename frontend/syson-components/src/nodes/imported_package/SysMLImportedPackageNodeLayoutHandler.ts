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
 * This code has been fully inspired from SysMLPackageNodeLayoutHandler
 */
import {
  Diagram,
  DiagramNodeType,
  ForcedDimensions,
  ILayoutEngine,
  INodeLayoutHandler,
  NodeData,
  computePreviousSize,
  findNodeIndex,
} from '@eclipse-sirius/sirius-components-diagrams';
import { Dimensions, Node } from '@xyflow/react';

import { SysMLImportedPackageNodeData } from './SysMLImportedPackageNode.types';

const rectangularNodePadding: number = 8;

export class SysMLImportedPackageNodeLayoutHandler implements INodeLayoutHandler<SysMLImportedPackageNodeData> {
  public canHandle(node: Node<NodeData, DiagramNodeType>): boolean {
    return node.type === 'sysMLImportedPackageNode';
  }

  public handle(
    _layoutEngine: ILayoutEngine,
    previousDiagram: Diagram | null,
    node: Node<SysMLImportedPackageNodeData, 'sysMLImportedPackageNode'>,
    visibleNodes: Node<NodeData, DiagramNodeType>[],
    _directChildren: Node<NodeData, DiagramNodeType>[],
    _newlyAddedNodes: Node<NodeData, DiagramNodeType>[],
    forceDimensions?: ForcedDimensions
  ) {
    const nodeIndex = findNodeIndex(visibleNodes, node.id);
    const nodeElement = document.getElementById(`${node.id}-rectangularNode-${nodeIndex}`)?.children[0];
    const borderWidth = nodeElement ? parseFloat(window.getComputedStyle(nodeElement).borderWidth) : 0;

    this.handleLeafNode(previousDiagram, node, visibleNodes, borderWidth, forceDimensions);
  }

  private handleLeafNode(
    previousDiagram: Diagram | null,
    node: Node<SysMLImportedPackageNodeData, 'sysMLImportedPackageNode'>,
    visibleNodes: Node<NodeData, DiagramNodeType>[],
    _borderWidth: number,
    _forceDimensions?: ForcedDimensions
  ) {
    const nodeIndex: number = findNodeIndex(visibleNodes, node.id);
    const labelElement: HTMLElement | null = document.getElementById(`${node.id}-label-${nodeIndex}`);

    const labelHeight: number =
      rectangularNodePadding + (labelElement?.getBoundingClientRect().height ?? 0) + rectangularNodePadding;

    const minNodeWidth: number = node.data.resizedByUser ? 75 : node.data.defaultWidth ?? 75;
    const minNodeHeight: number = Math.max(labelHeight, node.data.resizedByUser ? 50 : node.data.defaultHeight ?? 50);

    const previousNode: Node<NodeData, string> | undefined = (previousDiagram?.nodes ?? []).find(
      (prevNode) => prevNode.id === node.id
    );
    const previousDimensions: Dimensions = computePreviousSize(previousNode, node);

    if (node.data.resizedByUser && !_forceDimensions?.width) {
      if (minNodeWidth > previousDimensions.width) {
        node.width = minNodeWidth;
      } else {
        node.width = previousDimensions.width;
      }
    } else {
      node.width = minNodeWidth;
    }

    if (node.data.resizedByUser && !_forceDimensions?.height) {
      if (minNodeHeight > previousDimensions.height) {
        node.height = minNodeHeight;
      } else {
        node.height = previousDimensions.height;
      }
    } else {
      node.height = minNodeHeight;
    }
  }
}
