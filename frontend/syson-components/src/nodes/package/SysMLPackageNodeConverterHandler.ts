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
 * This code has been fully inspired from PackageNodeConverterHandler.ts in https://github.com/PapyrusSirius/papyrus-web
 */
import {
  BorderNodePositon,
  ConnectionHandle,
  GQLDiagram,
  GQLEdge,
  GQLNode,
  GQLNodeDescription,
  GQLNodeStyle,
  GQLViewModifier,
  IConvertEngine,
  INodeConverterHandler,
  convertHandles,
  convertLabelStyle,
  convertLineStyle,
} from '@eclipse-sirius/sirius-components-diagrams-reactflow';
import { Node, XYPosition } from 'reactflow';
import { GQLSysMLPackageNodeStyle, SysMLPackageNodeData } from './SysMLPackageNode.types';

const defaultPosition: XYPosition = { x: 0, y: 0 };

const toPackageNode = (
  gqlDiagram: GQLDiagram,
  gqlNode: GQLNode<GQLSysMLPackageNodeStyle>,
  gqlParentNode: GQLNode<GQLNodeStyle> | null,
  nodeDescription: GQLNodeDescription | undefined,
  isBorderNode: boolean,
  gqlEdge: GQLEdge[]
): Node<SysMLPackageNodeData> => {
  const {
    targetObjectId,
    targetObjectLabel,
    targetObjectKind,
    descriptionId,
    id,
    insideLabel,
    state,
    style,
    labelEditable,
  } = gqlNode;

  const connectionHandles: ConnectionHandle[] = convertHandles(gqlNode, gqlEdge);
  const isNew = gqlDiagram.layoutData.nodeLayoutData.find((nodeLayoutData) => nodeLayoutData.id === id) === undefined;

  const data: SysMLPackageNodeData = {
    targetObjectId,
    targetObjectLabel,
    targetObjectKind,
    descriptionId,
    style: {
      display: 'flex',
      backgroundColor: style.color,
      borderColor: style.borderColor,
      borderWidth: style.borderSize,
      borderStyle: convertLineStyle(style.borderStyle),
    },
    label: undefined,
    faded: state === GQLViewModifier.Faded,
    isBorderNode: isBorderNode,
    nodeDescription,
    defaultWidth: gqlNode.defaultWidth,
    defaultHeight: gqlNode.defaultHeight,
    borderNodePosition: isBorderNode ? BorderNodePositon.EAST : null,
    connectionHandles,
    labelEditable,
    isNew,
  };

  if (insideLabel) {
    const labelStyle = insideLabel.style;
    data.label = {
      id: insideLabel.id,
      text: insideLabel.text,
      isHeader: insideLabel.isHeader,
      style: {
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '8px 16px',
        textAlign: 'center',
        ...convertLabelStyle(labelStyle),
      },
      iconURL: labelStyle.iconURL,
    };

    data.style = {
      ...data.style,
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'flex-start',
    };
  }

  const node: Node<SysMLPackageNodeData> = {
    id,
    type: 'sysMLPackageNode',
    data,
    position: defaultPosition,
    hidden: gqlNode.state === GQLViewModifier.Hidden,
  };

  if (gqlParentNode) {
    node.parentNode = gqlParentNode.id;
  }

  const nodeLayoutData = gqlDiagram.layoutData.nodeLayoutData.filter((data) => data.id === id)[0];
  if (nodeLayoutData) {
    const {
      position,
      size: { height, width },
    } = nodeLayoutData;
    node.position = position;
    node.height = height;
    node.width = width;
    node.style = {
      ...node.style,
      width: `${node.width}px`,
      height: `${node.height}px`,
    };
  }

  return node;
};

export class SysMLPackageNodeConverterHandler implements INodeConverterHandler {
  canHandle(gqlNode: GQLNode<GQLNodeStyle>) {
    return gqlNode.style.__typename === 'SysMLPackageNodeStyle';
  }

  handle(
    convertEngine: IConvertEngine,
    gqlDiagram: GQLDiagram,
    gqlNode: GQLNode<GQLSysMLPackageNodeStyle>,
    gqlEdges: GQLEdge[],
    parentNode: GQLNode<GQLNodeStyle> | null,
    isBorderNode: boolean,
    nodes: Node[],
    nodeDescriptions: GQLNodeDescription[]
  ) {
    const nodeDescription = this.findNodeDescription(nodeDescriptions, gqlNode.descriptionId);
    nodes.push(toPackageNode(gqlDiagram, gqlNode, parentNode, nodeDescription ?? undefined, isBorderNode, gqlEdges));
    convertEngine.convertNodes(
      gqlDiagram,
      gqlNode.borderNodes ?? [],
      gqlNode,
      nodes,
      nodeDescription?.borderNodeDescriptions ?? []
    );
    convertEngine.convertNodes(
      gqlDiagram,
      gqlNode.childNodes ?? [],
      gqlNode,
      nodes,
      nodeDescription?.childNodeDescriptions ?? []
    );
  }

  findNodeDescription(nodeDescriptions: GQLNodeDescription[], id: string): GQLNodeDescription | null {
    for (let nodeDescription of nodeDescriptions) {
      if (nodeDescription.id === id) {
        return nodeDescription;
      }
      if (nodeDescription.childNodeDescriptions) {
        let subNodeDescription = this.findNodeDescription(nodeDescription.childNodeDescriptions, id);
        if (subNodeDescription !== null) {
          return subNodeDescription;
        }
      }
    }
    return null;
  }
}
