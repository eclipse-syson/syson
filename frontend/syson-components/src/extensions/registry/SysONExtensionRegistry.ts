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
import { ExtensionRegistry } from '@eclipse-sirius/sirius-components-core';
import {
  diagramPanelActionExtensionPoint,
  EdgeAppearanceSection,
  EdgeData,
  ImageNodeAppearanceSection,
  NodeData,
  PaletteAppearanceSectionContributionProps,
  paletteAppearanceSectionExtensionPoint,
  RectangularNodeAppearanceSection,
} from '@eclipse-sirius/sirius-components-diagrams';
import {
  OmniboxCommand,
  OmniboxCommandOverrideContribution,
  omniboxCommandOverrideContributionExtensionPoint,
} from '@eclipse-sirius/sirius-components-omnibox';
import { toolsButtonMenuEntryExtensionPoint } from '@eclipse-sirius/sirius-components-tables';
import {
  GQLTreeItemContextMenuEntry,
  treeItemContextMenuEntryOverrideExtensionPoint,
  TreeItemContextMenuOverrideContribution,
} from '@eclipse-sirius/sirius-components-trees';
import {
  ApolloClientOptionsConfigurer,
  apolloClientOptionsConfigurersExtensionPoint,
  ImportLibraryCommand,
  navigationBarMenuIconExtensionPoint,
} from '@eclipse-sirius/sirius-web-application';

import { Edge, Node, useStoreApi } from '@xyflow/react';
import { SysMLImportedPackageNodePaletteAppearanceSection } from '../../nodes/imported_package/SysMLImportedPackageNodePaletteAppearanceSection';
import { SysMLNoteNodePaletteAppearanceSection } from '../../nodes/note/SysMLNoteNodePaletteAppearanceSection';
import { SysMLPackageNodePaletteAppearanceSection } from '../../nodes/package/SysMLPackageNodePaletteAppearanceSection';
import { sysMLNodesStyleDocumentTransform } from '../../nodes/SysMLNodesDocumentTransform';
import { SysMLViewFrameNodePaletteAppearanceSection } from '../../nodes/view_frame/SysMLViewFrameNodePaletteAppearanceSection';
import { InsertTextualSysMLMenuContribution } from '../InsertTextualSysMLv2MenuContribution';
import { SysONNavigationBarMenuIcon } from '../navigationBarMenu/SysONNavigationBarMenuIcon';
import { PublishProjectSysMLContentsAsLibraryCommand } from '../omnibox/PublishProjectSysMLContentsAsLibraryCommand';
import { CreateRequirementMenuEntry } from '../requirementsTable/CreateRequirementMenuEntry';
import { ExposeRequirementsMenuEntry } from '../requirementsTable/ExposeRequirementsMenuEntry';
import { SysONDiagramPanelMenu } from '../SysONDiagramPanelMenu';

const sysONExtensionRegistry: ExtensionRegistry = new ExtensionRegistry();

const omniboxCommandOverrides: OmniboxCommandOverrideContribution[] = [
  {
    canHandle: (action: OmniboxCommand) => {
      return action.id === 'publishProjectSysMLContentsAsLibrary';
    },
    component: PublishProjectSysMLContentsAsLibraryCommand,
  },
  {
    canHandle: (action: OmniboxCommand) => {
      return action.id === 'importPublishedLibrary';
    },
    component: ImportLibraryCommand,
  },
];

sysONExtensionRegistry.putData<OmniboxCommandOverrideContribution[]>(omniboxCommandOverrideContributionExtensionPoint, {
  identifier: `syson_${omniboxCommandOverrideContributionExtensionPoint.identifier}`,
  data: omniboxCommandOverrides,
});

const apolloClientOptionsConfigurer: ApolloClientOptionsConfigurer = (currentOptions) => {
  const { documentTransform } = currentOptions;

  const newDocumentTransform = documentTransform
    ? documentTransform.concat(sysMLNodesStyleDocumentTransform)
    : sysMLNodesStyleDocumentTransform;
  return {
    ...currentOptions,
    documentTransform: newDocumentTransform,
  };
};
sysONExtensionRegistry.putData(apolloClientOptionsConfigurersExtensionPoint, {
  identifier: `syson_${apolloClientOptionsConfigurersExtensionPoint.identifier}`,
  data: [apolloClientOptionsConfigurer],
});

sysONExtensionRegistry.addComponent(diagramPanelActionExtensionPoint, {
  identifier: `syson_${diagramPanelActionExtensionPoint.identifier}_CustomPanelEntriesMenu`,
  Component: SysONDiagramPanelMenu,
});

sysONExtensionRegistry.addComponent(navigationBarMenuIconExtensionPoint, {
  identifier: `syson_${navigationBarMenuIconExtensionPoint.identifier}`,
  Component: SysONNavigationBarMenuIcon,
});

sysONExtensionRegistry.addComponent(toolsButtonMenuEntryExtensionPoint, {
  identifier: `syson_${toolsButtonMenuEntryExtensionPoint.identifier}`,
  Component: CreateRequirementMenuEntry,
});

sysONExtensionRegistry.addComponent(toolsButtonMenuEntryExtensionPoint, {
  identifier: `syson_${toolsButtonMenuEntryExtensionPoint.identifier}`,
  Component: ExposeRequirementsMenuEntry,
});

const treeItemContextMenuOverrideContributions: TreeItemContextMenuOverrideContribution[] = [
  {
    canHandle: (entry: GQLTreeItemContextMenuEntry) => {
      return entry.id === 'newObjectsFromText';
    },
    component: InsertTextualSysMLMenuContribution,
  },
];

sysONExtensionRegistry.putData<TreeItemContextMenuOverrideContribution[]>(
  treeItemContextMenuEntryOverrideExtensionPoint,
  {
    identifier: `syson_${treeItemContextMenuEntryOverrideExtensionPoint.identifier}`,
    data: treeItemContextMenuOverrideContributions,
  }
);

/*******************************************************************************
 *
 * Custom nodes appearance contributions
 *
 *******************************************************************************/
const customNodePaletteAppearanceSectionContribution: PaletteAppearanceSectionContributionProps[] = [
  // standard nodes and edges from Sirius Web
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      const canHandle = diagramElementIds.every(
        (elementId) =>
          store.getState().nodeLookup.get(elementId)?.data.nodeAppearanceData?.gqlStyle.__typename ===
          'RectangularNodeStyle'
      );

      return canHandle;
    },
    component: RectangularNodeAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) =>
          store.getState().nodeLookup.get(elementId)?.data.nodeAppearanceData?.gqlStyle.__typename === 'ImageNodeStyle'
      );
    },
    component: ImageNodeAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every((elementId) => !!store.getState().edgeLookup.get(elementId));
    },
    component: EdgeAppearanceSection,
  },
  // custom nodes from SysON
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) => store.getState().nodeLookup.get(elementId)?.type === 'sysMLPackageNode'
      );
    },
    component: SysMLPackageNodePaletteAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) => store.getState().nodeLookup.get(elementId)?.type === 'sysMLImportedPackageNode'
      );
    },
    component: SysMLImportedPackageNodePaletteAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) => store.getState().nodeLookup.get(elementId)?.type === 'sysMLNoteNode'
      );
    },
    component: SysMLNoteNodePaletteAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) => store.getState().nodeLookup.get(elementId)?.type === 'sysMLNoteNode'
      );
    },
    component: SysMLViewFrameNodePaletteAppearanceSection,
  },
];

sysONExtensionRegistry.putData<PaletteAppearanceSectionContributionProps[]>(paletteAppearanceSectionExtensionPoint, {
  identifier: `syson_${paletteAppearanceSectionExtensionPoint.identifier}`,
  data: customNodePaletteAppearanceSectionContribution,
});

export { sysONExtensionRegistry };
