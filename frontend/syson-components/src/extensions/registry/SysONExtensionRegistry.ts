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
  ImageNodeAppearanceSection,
  PaletteAppearanceSectionContributionProps,
  paletteAppearanceSectionExtensionPoint,
  RectangularNodeAppearanceSection,
} from '@eclipse-sirius/sirius-components-diagrams';
import {
  GQLOmniboxCommand,
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
    canHandle: (action: GQLOmniboxCommand) => {
      return action.id === 'publishProjectSysMLContentsAsLibrary';
    },
    component: PublishProjectSysMLContentsAsLibraryCommand,
  },
  {
    canHandle: (action: GQLOmniboxCommand) => {
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
    canHandle: (node, _edge) => {
      return node?.data.nodeAppearanceData?.gqlStyle.__typename === 'RectangularNodeStyle';
    },
    component: RectangularNodeAppearanceSection,
  },
  {
    canHandle: (node, _edge) => {
      return node?.data.nodeAppearanceData?.gqlStyle.__typename === 'ImageNodeStyle';
    },
    component: ImageNodeAppearanceSection,
  },
  {
    canHandle: (_node, edge) => {
      return !!edge;
    },
    component: EdgeAppearanceSection,
  },
  // custom nodes from SysON
  {
    canHandle: (node, _edge) => {
      return node?.type === 'sysMLPackageNode';
    },
    component: SysMLPackageNodePaletteAppearanceSection,
  },
  {
    canHandle: (node, _edge) => {
      return node?.type === 'sysMLImportedPackageNode';
    },
    component: SysMLImportedPackageNodePaletteAppearanceSection,
  },
  {
    canHandle: (node, _edge) => {
      return node?.type === 'sysMLNoteNode';
    },
    component: SysMLNoteNodePaletteAppearanceSection,
  },
  {
    canHandle: (node, _edge) => {
      return node?.type === 'sysMLViewFrameNode';
    },
    component: SysMLViewFrameNodePaletteAppearanceSection,
  },
];

sysONExtensionRegistry.putData<PaletteAppearanceSectionContributionProps[]>(paletteAppearanceSectionExtensionPoint, {
  identifier: `syson_${paletteAppearanceSectionExtensionPoint.identifier}`,
  data: customNodePaletteAppearanceSectionContribution,
});

export { sysONExtensionRegistry };
