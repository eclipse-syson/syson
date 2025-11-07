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

import { NodeTypeContribution } from '@eclipse-sirius/sirius-components-diagrams';
import { NodeTypeRegistry } from '@eclipse-sirius/sirius-web-application';

import { NodeProps } from '@xyflow/react';
import { SysMLImportedPackageNode } from '../../nodes/imported_package/SysMLImportedPackageNode';
import { SysMLImportedPackageNodeConverter } from '../../nodes/imported_package/SysMLImportedPackageNodeConverter';
import { SysMLImportedPackageNodeLayoutHandler } from '../../nodes/imported_package/SysMLImportedPackageNodeLayoutHandler';
import { SysMLNoteNode } from '../../nodes/note/SysMLNoteNode';
import { SysMLNoteNodeConverter } from '../../nodes/note/SysMLNoteNodeConverter';
import { SysMLNoteNodeLayoutHandler } from '../../nodes/note/SysMLNoteNodeLayoutHandler';
import { SysMLPackageNode } from '../../nodes/package/SysMLPackageNode';
import { SysMLPackageNodeConverter } from '../../nodes/package/SysMLPackageNodeConverter';
import { SysMLPackageNodeLayoutHandler } from '../../nodes/package/SysMLPackageNodeLayoutHandler';
import { SysMLViewFrameNode } from '../../nodes/view_frame/SysMLViewFrameNode';
import { SysMLViewFrameNodeConverter } from '../../nodes/view_frame/SysMLViewFrameNodeConverter';
import { SysMLViewFrameNodeLayoutHandler } from '../../nodes/view_frame/SysMLViewFrameNodeLayoutHandler';

/*******************************************************************************
 *
 * Custom nodes contributions
 *
 *******************************************************************************/
const sysONNodeTypeRegistry: NodeTypeRegistry = {
  nodeLayoutHandlers: [
    new SysMLPackageNodeLayoutHandler(),
    new SysMLNoteNodeLayoutHandler(),
    new SysMLImportedPackageNodeLayoutHandler(),
    new SysMLViewFrameNodeLayoutHandler(),
  ],
  nodeConverters: [
    new SysMLPackageNodeConverter(),
    new SysMLNoteNodeConverter(),
    new SysMLImportedPackageNodeConverter(),
    new SysMLViewFrameNodeConverter(),
  ],
  nodeTypeContributions: [
    <NodeTypeContribution component={SysMLPackageNode as unknown as React.FC<NodeProps>} type={'sysMLPackageNode'} />,
    <NodeTypeContribution component={SysMLNoteNode as unknown as React.FC<NodeProps>} type={'sysMLNoteNode'} />,
    <NodeTypeContribution
      component={SysMLImportedPackageNode as unknown as React.FC<NodeProps>}
      type={'sysMLImportedPackageNode'}
    />,
    <NodeTypeContribution
      component={SysMLViewFrameNode as unknown as React.FC<NodeProps>}
      type={'sysMLViewFrameNode'}
    />,
  ],
};

export { sysONNodeTypeRegistry };
