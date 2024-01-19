/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
 * This code has been fully inspired from PackageNodeLayoutHandler.ts in https://github.com/PapyrusSirius/papyrus-web
 */
import {
  Diagram,
  DiagramNodeType,
  ILayoutEngine,
  INodeLayoutHandler,
  NodeData,
  computeNodesBox,
  computePreviousPosition,
  computePreviousSize,
  findNodeIndex,
  getBorderNodeExtent,
  getChildNodePosition,
  getEastBorderNodeFootprintHeight,
  getHeaderFootprint,
  getNorthBorderNodeFootprintWidth,
  getSouthBorderNodeFootprintWidth,
  getWestBorderNodeFootprintHeight,
  setBorderNodesPosition,
} from '@eclipse-sirius/sirius-components-diagrams-reactflow';
import { Dimensions, Node, Rect } from 'reactflow';
import { SysMLPackageNodeData } from './SysMLPackageNode.types';

const rectangularNodePadding: number = 8;

export class SysMLPackageNodeLayoutHandler implements INodeLayoutHandler<SysMLPackageNodeData> {
  public canHandle(node: Node<NodeData, DiagramNodeType>): boolean {
    return node.type === 'sysMLPackageNode';
  }

  public handle(
    layoutEngine: ILayoutEngine,
    previousDiagram: Diagram | null,
    node: Node<SysMLPackageNodeData, 'sysMLPackageNode'>,
    visibleNodes: Node<NodeData, DiagramNodeType>[],
    directChildren: Node<NodeData, DiagramNodeType>[],
    newlyAddedNode: Node<NodeData, DiagramNodeType> | undefined,
    forceWidth?: number
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
        forceWidth
      );
    } else {
      this.handleLeafNode(previousDiagram, node, visibleNodes, borderWidth, forceWidth);
    }
  }

  private handleParentNode(
    layoutEngine: ILayoutEngine,
    previousDiagram: Diagram | null,
    node: Node<SysMLPackageNodeData, 'sysMLPackageNode'>,
    visibleNodes: Node<NodeData, DiagramNodeType>[],
    directChildren: Node<NodeData, DiagramNodeType>[],
    newlyAddedNode: Node<NodeData, DiagramNodeType> | undefined,
    borderWidth: number,
    _forceWidth?: number
  ) {
    layoutEngine.layoutNodes(previousDiagram, visibleNodes, directChildren, newlyAddedNode);

    const nodeIndex: number = findNodeIndex(visibleNodes, node.id);
    const labelElement: HTMLElement | null = document.getElementById(`${node.id}-label-${nodeIndex}`);
    const headerHeightFootprint: number = labelElement ? getHeaderFootprint(labelElement, true, false) : 33;

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
      } else if (previousPosition) {
        child.position = previousPosition;
        if (previousNode && previousNode.position.y < headerHeightFootprint + rectangularNodePadding) {
          child.position = { ...child.position, y: headerHeightFootprint + rectangularNodePadding };
        } else {
          child.position = child.position;
        }
      } else {
        child.position = getChildNodePosition(visibleNodes, child, labelElement, false, false, borderWidth);
        if (child.position.y < headerHeightFootprint + rectangularNodePadding) {
          child.position = { ...child.position, y: child.position.y + headerHeightFootprint };
        }
        const previousSibling = directNodesChildren[index - 1];
        if (previousSibling) {
          child.position = getChildNodePosition(visibleNodes, child, labelElement, false, false, borderWidth);
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

    const nodeWidth: number =
      Math.max(
        directChildrenAwareNodeWidth,
        northBorderNodeFootprintWidth,
        southBorderNodeFootprintWidth,
        node.data.defaultWidth ? node.data.defaultWidth : 0
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

    const nodeHeight: number =
      Math.max(
        directChildrenAwareNodeHeight,
        eastBorderNodeFootprintHeight,
        westBorderNodeFootprintHeight,
        node.data.defaultHeight ? node.data.defaultHeight : 0
      ) +
      borderWidth * 2;

    const minNodeWith: number = nodeWidth;
    const minNodeHeight: number = nodeHeight;

    const previousNode: Node<NodeData, string> | undefined = (previousDiagram?.nodes ?? []).find(
      (prevNode) => prevNode.id === node.id
    );
    const previousDimensions: Dimensions = computePreviousSize(previousNode, node);
    if (node.data.nodeDescription?.userResizable) {
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

    // Update border nodes positions
    borderNodes.forEach((borderNode) => {
      borderNode.extent = getBorderNodeExtent(node, borderNode);
    });
    setBorderNodesPosition(borderNodes, node, previousDiagram);
  }

  private handleLeafNode(
    previousDiagram: Diagram | null,
    node: Node<SysMLPackageNodeData, 'sysMLPackageNode'>,
    visibleNodes: Node<NodeData, DiagramNodeType>[],
    _borderWidth: number,
    _forceWidth?: number
  ) {
    const nodeIndex: number = findNodeIndex(visibleNodes, node.id);
    const labelElement: HTMLElement | null = document.getElementById(`${node.id}-label-${nodeIndex}`);

    const labelHeight: number =
      rectangularNodePadding + (labelElement?.getBoundingClientRect().height ?? 0) + rectangularNodePadding;

    const minNodeWith: number = Math.max(node.data.defaultWidth ? node.data.defaultWidth : 0);
    const minNodeHeight: number = Math.max(labelHeight, node.data.defaultHeight ? node.data.defaultHeight : 0);

    const previousNode: Node<NodeData, string> | undefined = (previousDiagram?.nodes ?? []).find(
      (prevNode) => prevNode.id === node.id
    );
    const previousDimensions: Dimensions = computePreviousSize(previousNode, node);

    if (node.data.nodeDescription?.userResizable) {
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
