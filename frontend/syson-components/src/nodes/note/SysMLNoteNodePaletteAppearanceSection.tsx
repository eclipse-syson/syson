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

import { GQLSysMLNoteNodeStyle } from './SysMLNoteNode.types';
import { SysMLNoteNodePart } from './SysMLNoteNodePart';

export const SysMLNoteNodePaletteAppearanceSection = ({
  diagramElementIds,
}: PaletteAppearanceSectionContributionComponentProps) => {
  const nodeDatas = useNodesData<Node<NodeData>>(diagramElementIds).map((nodeData) => nodeData.data);
  const data = nodeDatas.at(nodeDatas.length - 1);
  if (!data) {
    return null;
  }
  const insideLabelsIds = nodeDatas.flatMap((nodeData) => nodeData.insideLabel?.id || []);
  return (
    <>
      <SysMLNoteNodePart
        nodeIds={diagramElementIds}
        style={data.nodeAppearanceData.gqlStyle as GQLSysMLNoteNodeStyle}
        customizedStyleProperties={data.nodeAppearanceData.customizedStyleProperties}
      />
      {data.insideLabel ? (
        <LabelAppearancePart
          diagramElementIds={diagramElementIds}
          labelIds={insideLabelsIds}
          position="Inside Label"
          style={data.insideLabel.appearanceData.gqlStyle}
          customizedStyleProperties={data.insideLabel.appearanceData.customizedStyleProperties}
        />
      ) : null}
    </>
  );
};
