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
    _newlyAddedNode: Node<NodeData, DiagramNodeType> | undefined,
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

    const minNodeWith: number = node.data.defaultWidth ?? 0;
    const minNodeHeight: number = Math.max(labelHeight, node.data.defaultHeight ?? 0);

    const previousNode: Node<NodeData, string> | undefined = (previousDiagram?.nodes ?? []).find(
      (prevNode) => prevNode.id === node.id
    );
    const previousDimensions: Dimensions = computePreviousSize(previousNode, node);

    if (node.data.resizedByUser) {
      if (minNodeWith > previousDimensions.width) {
        node.width = minNodeWith;
      } else {
        node.width = previousDimensions.width;
      }
      if (minNodeHeight > previousDimensions.height) {
        node.height = minNodeHeight;
      } else {
        node.height = previousDimensions.height;
      }
    } else {
      node.width = minNodeWith;
      node.height = minNodeHeight;
    }
  }
}
