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
  LabelAppearancePart,
  NodeData,
  PaletteAppearanceSectionContributionComponentProps,
} from '@eclipse-sirius/sirius-components-diagrams';
import { Node, useNodesData } from '@xyflow/react';

import { GQLSysMLImportedPackageNodeStyle } from './SysMLImportedPackageNode.types';
import { SysMLImportedPackageNodePart } from './SysMLImportedPackageNodePart';

export const SysMLImportedPackageNodePaletteAppearanceSection = ({
  diagramElementId,
}: PaletteAppearanceSectionContributionComponentProps) => {
  const nodeData = useNodesData<Node<NodeData>>(diagramElementId);

  if (!nodeData) {
    return null;
  }
  return (
    <>
      <SysMLImportedPackageNodePart
        nodeId={diagramElementId}
        style={nodeData.data.nodeAppearanceData.gqlStyle as GQLSysMLImportedPackageNodeStyle}
        customizedStyleProperties={nodeData.data.nodeAppearanceData.customizedStyleProperties}
      />
      s
      {nodeData.data.insideLabel ? (
        <LabelAppearancePart
          diagramElementId={diagramElementId}
          labelId={nodeData.data.insideLabel.id}
          position="Inside Label"
          style={nodeData.data.insideLabel.appearanceData.gqlStyle}
          customizedStyleProperties={nodeData.data.insideLabel.appearanceData.customizedStyleProperties}
        />
      ) : null}
    </>
  );
};
