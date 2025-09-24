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
 * This code has been fully inspired from PackageNode.types.ts in https://github.com/PapyrusSirius/papyrus-web
 */
import { GQLNodeStyle, NodeData } from '@eclipse-sirius/sirius-components-diagrams';
import { Node, NodeProps } from '@xyflow/react';
import { FC } from 'react';

export interface SysMLImportedPackageNodeData extends NodeData {}

export interface GQLSysMLImportedPackageNodeStyle extends GQLNodeStyle {
  background: string;
  borderColor: string;
  borderStyle: string;
  borderSize: number;
}

export interface NodeDataMap {
  sysMLImportedPackageNode: SysMLImportedPackageNodeData;
}
export type NodeComponentsMap = {
  [K in keyof NodeDataMap]: FC<NodeProps<Node<NodeDataMap[K], K>>>;
};
