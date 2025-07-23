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
import {
  Diagram,
  DiagramNodeType,
  ForcedDimensions,
  ILayoutEngine,
  INodeLayoutHandler,
  NodeData,
  computeNodesBox,
  computePreviousPosition,
  computePreviousSize,
  findNodeIndex,
  getBorderNodeExtent,
  getChildNodePosition,
  getDefaultOrMinHeight,
  getDefaultOrMinWidth,
  getEastBorderNodeFootprintHeight,
  getHeaderHeightFootprint,
  getNorthBorderNodeFootprintWidth,
  getSouthBorderNodeFootprintWidth,
  getWestBorderNodeFootprintHeight,
  setBorderNodesPosition,
} from '@eclipse-sirius/sirius-components-diagrams';
import { Dimensions, Node, Rect } from '@xyflow/react';
import { SysMLViewFrameNodeData } from './SysMLViewFrameNode.types';

const rectangularNodePadding: number = 8;

export class SysMLViewFrameNodeLayoutHandler implements INodeLayoutHandler<SysMLViewFrameNodeData> {
  public canHandle(node: Node<NodeData, DiagramNodeType>): boolean {
    return node.type === 'sysMLViewFrameNode';
  }

  public handle(
    layoutEngine: ILayoutEngine,
    previousDiagram: Diagram | null,
    node: Node<SysMLViewFrameNodeData, 'sysMLViewFrameNode'>,
    visibleNodes: Node<NodeData, DiagramNodeType>[],
    directChildren: Node<NodeData, DiagramNodeType>[],
    newlyAddedNode: Node<NodeData, DiagramNodeType> | undefined,
    forceDimensions?: ForcedDimensions
  ) {
    const nodeIndex = findNodeIndex(visibleNodes, node.id);
    const nodeElement = document.getElementById(`${node.id}-rectangularNode-${nodeIndex}`)?.children[0];
    const borderWidth = nodeElement ? parseFloat(window.getComputedStyle(nodeElement).borderWidth) : 0;
    if (directChildren.length > 0) {
      this.handleParentNode(
        layoutEngine,
        previousDiagram,
        node,
        visibleNodes,
        directChildren,
        newlyAddedNode,
        borderWidth,
        forceDimensions
      );
    } else {
      this.handleLeafNode(previousDiagram, node, visibleNodes, borderWidth, forceDimensions);
    }
  }

  private handleParentNode(
    layoutEngine: ILayoutEngine,
    previousDiagram: Diagram | null,
    node: Node<SysMLViewFrameNodeData, 'sysMLViewFrameNode'>,
    visibleNodes: Node<NodeData, DiagramNodeType>[],
    directChildren: Node<NodeData, DiagramNodeType>[],
    newlyAddedNode: Node<NodeData, DiagramNodeType> | undefined,
    borderWidth: number,
    forceDimensions?: ForcedDimensions
  ) {
    layoutEngine.layoutNodes(previousDiagram, visibleNodes, directChildren, newlyAddedNode);

    const nodeIndex: number = findNodeIndex(visibleNodes, node.id);
    const labelElement: HTMLElement | null = document.getElementById(`${node.id}-label-${nodeIndex}`);
    const headerHeightFootprint: number = labelElement
      ? getHeaderHeightFootprint(labelElement, node.data.insideLabel, 'TOP')
      : 33;

    const borderNodes: Node<NodeData, string>[] = directChildren.filter((node) => node.data.isBorderNode);
    const directNodesChildren: Node<NodeData, string>[] = directChildren.filter((child) => !child.data.isBorderNode);

    // Update children position to be under the label and at the right padding.
    directNodesChildren.forEach((child, index) => {
      const previousNode = (previousDiagram?.nodes ?? []).find((prevNode) => prevNode.id === child.id);
      const previousPosition = computePreviousPosition(previousNode, child);
      const createdNode = newlyAddedNode?.id === child.id ? newlyAddedNode : undefined;

      if (!!createdNode) {
        child.position = createdNode.position;
        if (child.position.y < borderWidth + headerHeightFootprint + rectangularNodePadding) {
          child.position = { ...child.position, y: borderWidth + headerHeightFootprint + rectangularNodePadding };
        }
        if (child.position.x < 0) {
          child.position = { ...child.position, x: rectangularNodePadding };
        }
      } else if (previousPosition) {
        child.position = previousPosition;
        if (previousNode && previousNode.position.y < headerHeightFootprint + rectangularNodePadding) {
          child.position = { ...child.position, y: headerHeightFootprint + rectangularNodePadding };
        }
        if (child.position.x < 0) {
          child.position = { ...child.position, x: rectangularNodePadding };
        }
      } else {
        child.position = getChildNodePosition(visibleNodes, child, headerHeightFootprint, borderWidth);
        if (child.position.y < headerHeightFootprint + rectangularNodePadding) {
          child.position = { ...child.position, y: child.position.y + headerHeightFootprint };
        }
        const previousSibling = directNodesChildren[index - 1];
        if (previousSibling) {
          child.position = getChildNodePosition(
            visibleNodes,
            child,
            headerHeightFootprint,
            borderWidth,
            previousSibling
          );
        }
        if (child.position.x < 0) {
          child.position = { ...child.position, x: rectangularNodePadding };
        }
      }
    });

    // Update node to layout size
    // WARN: We suppose label are always on top of children (that wrong)
    const childrenContentBox: Rect = computeNodesBox(visibleNodes, directNodesChildren); // WARN: The current content box algorithm does not take the margin of direct children (it should)
    const directChildrenAwareNodeWidth: number =
      childrenContentBox.x + childrenContentBox.width + rectangularNodePadding;
    const northBorderNodeFootprintWidth: number = getNorthBorderNodeFootprintWidth(
      visibleNodes,
      borderNodes,
      previousDiagram
    );
    const southBorderNodeFootprintWidth: number = getSouthBorderNodeFootprintWidth(
      visibleNodes,
      borderNodes,
      previousDiagram
    );

    const nodeMinComputeWidth: number =
      Math.max(
        directChildrenAwareNodeWidth,
        northBorderNodeFootprintWidth,
        southBorderNodeFootprintWidth,
        node.data.defaultWidth && !node.data.resizedByUser ? node.data.defaultWidth : 0
      ) +
      borderWidth * 2;

    // WARN: the label is not used for the height because children are already position under the label
    const directChildrenAwareNodeHeight: number =
      childrenContentBox.y + childrenContentBox.height + rectangularNodePadding;
    const eastBorderNodeFootprintHeight: number = getEastBorderNodeFootprintHeight(
      visibleNodes,
      borderNodes,
      previousDiagram
    );
    const westBorderNodeFootprintHeight: number = getWestBorderNodeFootprintHeight(
      visibleNodes,
      borderNodes,
      previousDiagram
    );

    const nodeMinComputeHeight: number =
      Math.max(
        directChildrenAwareNodeHeight,
        eastBorderNodeFootprintHeight,
        westBorderNodeFootprintHeight,
        node.data.defaultHeight && !node.data.resizedByUser ? node.data.defaultHeight : 0
      ) +
      borderWidth * 2;

    const nodeWidth: number = forceDimensions?.width ?? getDefaultOrMinWidth(nodeMinComputeWidth, node);
    const nodeHeight: number = getDefaultOrMinHeight(nodeMinComputeHeight, node);

    const previousNode: Node<NodeData, string> | undefined = (previousDiagram?.nodes ?? []).find(
      (prevNode) => prevNode.id === node.id
    );
    const previousDimensions: Dimensions = computePreviousSize(previousNode, node);
    if (node.data.resizedByUser && !forceDimensions?.width) {
      if (nodeMinComputeWidth > previousDimensions.width) {
        node.width = nodeMinComputeWidth;
      } else {
        node.width = previousDimensions.width;
      }
    } else {
      node.width = nodeWidth;
    }

    if (node.data.resizedByUser && !forceDimensions?.height) {
      if (nodeMinComputeHeight > previousDimensions.height) {
        node.height = nodeMinComputeHeight;
      } else {
        node.height = previousDimensions.height;
      }
    } else {
      node.height = nodeHeight;
    }

    // Update border nodes positions
    borderNodes.forEach((borderNode) => {
      borderNode.extent = getBorderNodeExtent(node, borderNode);
    });
    setBorderNodesPosition(borderNodes, node, previousDiagram);
  }

  private handleLeafNode(
    previousDiagram: Diagram | null,
    node: Node<SysMLViewFrameNodeData, 'sysMLViewFrameNode'>,
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
