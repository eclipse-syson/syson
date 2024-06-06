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
 * This code has been fully inspired from PackageNodeConverterHandler.ts in https://github.com/PapyrusSirius/papyrus-web
 */
import {
  BorderNodePosition,
  ConnectionHandle,
  GQLDiagram,
  GQLDiagramDescription,
  GQLEdge,
  GQLNode,
  GQLNodeDescription,
  GQLNodeLayoutData,
  GQLNodeStyle,
  GQLViewModifier,
  IConvertEngine,
  INodeConverter,
  convertHandles,
  convertInsideLabel,
  convertLineStyle,
  convertOutsideLabels,
  isListLayoutStrategy,
} from '@eclipse-sirius/sirius-components-diagrams';
import { Node, XYPosition } from 'reactflow';
import { GQLSysMLPackageNodeStyle, SysMLPackageNodeData } from './SysMLPackageNode.types';

const defaultPosition: XYPosition = { x: 0, y: 0 };

const toPackageNode = (
  gqlDiagram: GQLDiagram,
  gqlNode: GQLNode<GQLSysMLPackageNodeStyle>,
  gqlParentNode: GQLNode<GQLNodeStyle> | null,
  nodeDescription: GQLNodeDescription,
  isBorderNode: boolean,
  gqlEdges: GQLEdge[]
): Node<SysMLPackageNodeData> => {
  const {
    targetObjectId,
    targetObjectLabel,
    targetObjectKind,
    descriptionId,
    id,
    insideLabel,
    outsideLabels,
    state,
    pinned,
    style,
    labelEditable,
  } = gqlNode;

  const connectionHandles: ConnectionHandle[] = convertHandles(gqlNode, gqlEdges);
  const gqlNodeLayoutData: GQLNodeLayoutData | undefined = gqlDiagram.layoutData.nodeLayoutData.find(
    (nodeLayoutData) => nodeLayoutData.id === id
  );
  const isNew = gqlDiagram.layoutData.nodeLayoutData.find((nodeLayoutData) => nodeLayoutData.id === id) === undefined;
  const resizedByUser = gqlNodeLayoutData?.resizedByUser ?? false;

  const data: SysMLPackageNodeData = {
    targetObjectId,
    targetObjectLabel,
    targetObjectKind,
    descriptionId,
    style: {
      display: 'flex',
      background: style.background,
      borderColor: style.borderColor,
      borderWidth: style.borderSize,
      borderStyle: convertLineStyle(style.borderStyle),
    },
    insideLabel: null,
    outsideLabels: convertOutsideLabels(outsideLabels),
    faded: state === GQLViewModifier.Faded,
    pinned,
    isBorderNode: isBorderNode,
    nodeDescription,
    defaultWidth: gqlNode.defaultWidth,
    defaultHeight: gqlNode.defaultHeight,
    borderNodePosition: isBorderNode ? BorderNodePosition.EAST : null,
    connectionHandles,
    labelEditable,
    isNew,
    resizedByUser,
    isListChild: isListLayoutStrategy(gqlParentNode?.childrenLayoutStrategy),
    isDropNodeTarget: false,
    isDropNodeCandidate: false,
    isHovered: false,
  };

  data.insideLabel = convertInsideLabel(
    insideLabel,
    data,
    `${style.borderSize}px ${style.borderStyle} ${style.borderColor}`
  );

  if (data.insideLabel) {
    data.insideLabel.isHeader = true;
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

export class SysMLPackageNodeConverter implements INodeConverter {
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
    diagramDescription: GQLDiagramDescription,
    nodeDescriptions: GQLNodeDescription[]
  ) {
    const nodeDescription: GQLNodeDescription | undefined = nodeDescriptions.find(
      (description) => description.id === gqlNode.descriptionId
    );
    if (nodeDescription) {
      nodes.push(toPackageNode(gqlDiagram, gqlNode, parentNode, nodeDescription, isBorderNode, gqlEdges));
    }
    const borderNodeDescriptions: GQLNodeDescription[] = (nodeDescription?.borderNodeDescriptionIds ?? []).flatMap(
      (nodeDescriptionId) =>
        diagramDescription.nodeDescriptions.filter((nodeDescription) => nodeDescription.id === nodeDescriptionId)
    );
    const childNodeDescriptions: GQLNodeDescription[] = (nodeDescription?.childNodeDescriptionIds ?? []).flatMap(
      (nodeDescriptionId) =>
        diagramDescription.nodeDescriptions.filter((nodeDescription) => nodeDescription.id === nodeDescriptionId)
    );
    convertEngine.convertNodes(
      gqlDiagram,
      gqlNode.borderNodes ?? [],
      gqlNode,
      nodes,
      diagramDescription,
      borderNodeDescriptions
    );
    convertEngine.convertNodes(
      gqlDiagram,
      gqlNode.childNodes ?? [],
      gqlNode,
      nodes,
      diagramDescription,
      childNodeDescriptions
    );
  }
}
