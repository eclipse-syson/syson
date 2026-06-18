/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import { EdgeData, NodeData, useLayout, useSynchronizeLayoutData } from '@eclipse-sirius/sirius-components-diagrams';
import { Edge, Node, useReactFlow } from '@xyflow/react';
import { UseRotateNodeValue } from './useRotateNode.types';

export const useRotateNode = (): UseRotateNodeValue => {
  const { getNodes, getEdges, setNodes, setEdges } = useReactFlow<Node<NodeData>, Edge<EdgeData>>();
  const { synchronizeLayoutData } = useSynchronizeLayoutData();
  const { layout } = useLayout();

  const rotate = (representationElementIds: string[]) => {
    const updatedNodes: Node<NodeData>[] = getNodes().map((node) => {
      if (representationElementIds.length > 0 && node.id === representationElementIds[0]) {
        const cx = node.position.x + (node.width ?? 0) / 2;
        const cy = node.position.y + (node.height ?? 0) / 2;

        const newWidth = node.height ?? 0;
        const newHeight = node.width ?? 0;

        const newX = cx - newWidth / 2;
        const newY = cy - newHeight / 2;
        return {
          ...node,
          position: { x: newX, y: newY },
          width: newWidth,
          height: newHeight,
          data: { ...node.data, resizedByUser: true, connectionHandles: [] },
        };
      }
      return node;
    });

    const updatedEdges: Edge<EdgeData>[] = getEdges().map((edge) => {
      if (
        ((representationElementIds.length > 0 && edge.source === representationElementIds[0]) ||
          edge.target === representationElementIds[0]) &&
        edge.data
      ) {
        return {
          ...edge,
          data: {
            ...edge.data,
            bendingPoints: null,
          },
        };
      }
      return edge;
    });

    const diagramToLayout = {
      nodes: updatedNodes,
      edges: updatedEdges,
    };

    layout(diagramToLayout, diagramToLayout, null, 'UNDEFINED', (laidOutDiagram) => {
      const updatedNodesAfterLayout = updatedNodes.map((node) => {
        if (representationElementIds.find((nodeId) => nodeId === node.id)) {
          return laidOutDiagram.nodes.find((laidOutNode) => laidOutNode.id === node.id) ?? node;
        }
        return node;
      });

      const updatedEdgesAfterLayout = updatedEdges.map((edge) => {
        if (representationElementIds.find((nodeId) => nodeId === edge.source || nodeId === edge.target)) {
          return laidOutDiagram.edges.find((laidOutEdge) => laidOutEdge.id === edge.id) ?? edge;
        }
        return edge;
      });

      setNodes(updatedNodesAfterLayout);
      setEdges(updatedEdgesAfterLayout);
      const finalDiagram = {
        nodes: updatedNodesAfterLayout,
        edges: updatedEdgesAfterLayout,
      };
      synchronizeLayoutData(crypto.randomUUID(), 'layout', finalDiagram, 'UNCHANGED');
    });
  };

  return { rotate };
};
