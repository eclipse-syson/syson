/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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

// It should be the first import
// Otherwise the following error will be thrown: styled_default is not a function
// https://github.com/vitejs/vite/issues/12423#issuecomment-2080351394
import '@mui/material/styles/styled';

import { loadDevMessages, loadErrorMessages } from '@apollo/client/dev';
import { ExtensionRegistry } from '@eclipse-sirius/sirius-components-core';
import { diagramPanelActionExtensionPoint, NodeTypeContribution } from '@eclipse-sirius/sirius-components-diagrams';
import {
  GQLOmniboxCommand,
  OmniboxCommandOverrideContribution,
  omniboxCommandOverrideContributionExtensionPoint,
} from '@eclipse-sirius/sirius-components-omnibox';
import { treeItemContextMenuEntryExtensionPoint } from '@eclipse-sirius/sirius-components-trees';
import {
  ApolloClientOptionsConfigurer,
  apolloClientOptionsConfigurersExtensionPoint,
  DiagramRepresentationConfiguration,
  footerExtensionPoint,
  ImportLibraryCommand,
  navigationBarIconExtensionPoint,
  navigationBarMenuHelpURLExtensionPoint,
  NodeTypeRegistry,
  SiriusWebApplication,
} from '@eclipse-sirius/sirius-web-application';
import {
  InsertTextualSysMLMenuContribution,
  PublishProjectSysMLContentsAsLibraryCommand,
  SysMLImportedPackageNode,
  SysMLImportedPackageNodeConverter,
  SysMLImportedPackageNodeLayoutHandler,
  sysMLNodesStyleDocumentTransform,
  SysMLNoteNode,
  SysMLNoteNodeConverter,
  SysMLNoteNodeLayoutHandler,
  SysMLPackageNode,
  SysMLPackageNodeConverter,
  SysMLPackageNodeLayoutHandler,
  SysONDiagramPanelMenu,
} from '@eclipse-syson/syson-components';
import { createRoot } from 'react-dom/client';

import { httpOrigin, wsOrigin } from './core/URL';
import { SysONDocumentTreeItemContextMenuContribution } from './extensions/SysONDocumentTreeItemContextMenuContribution';
import { SysONExtensionRegistryMergeStrategy } from './extensions/SysONExtensionRegistryMergeStrategy';
import { SysONFooter } from './extensions/SysONFooter';
import { SysONNavigationBarIcon } from './extensions/SysONNavigationBarIcon';
import { SysONObjectTreeItemContextMenuContribution } from './extensions/SysONObjectTreeItemContextMenuContribution';
import './fonts.css';
import './reset.css';
import { sysonTheme } from './theme/sysonTheme';
import './variables.css';

if (process.env.NODE_ENV !== 'production') {
  loadDevMessages();
  loadErrorMessages();
}

const extensionRegistry: ExtensionRegistry = new ExtensionRegistry();

extensionRegistry.addComponent(navigationBarIconExtensionPoint, {
  identifier: `syson_${navigationBarIconExtensionPoint.identifier}`,
  Component: SysONNavigationBarIcon,
});
extensionRegistry.putData(navigationBarMenuHelpURLExtensionPoint, {
  identifier: `syson_${navigationBarMenuHelpURLExtensionPoint.identifier}`,
  data: 'https://doc.mbse-syson.org',
});

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

extensionRegistry.putData<OmniboxCommandOverrideContribution[]>(omniboxCommandOverrideContributionExtensionPoint, {
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
extensionRegistry.putData(apolloClientOptionsConfigurersExtensionPoint, {
  identifier: `syson_${apolloClientOptionsConfigurersExtensionPoint.identifier}`,
  data: [apolloClientOptionsConfigurer],
});

extensionRegistry.addComponent(diagramPanelActionExtensionPoint, {
  identifier: `syson_${diagramPanelActionExtensionPoint.identifier}_CustomPanelEntriesMenu`,
  Component: SysONDiagramPanelMenu,
});

extensionRegistry.addComponent(treeItemContextMenuEntryExtensionPoint, {
  identifier: `siriusweb_${treeItemContextMenuEntryExtensionPoint.identifier}_object`,
  Component: SysONObjectTreeItemContextMenuContribution,
});

extensionRegistry.addComponent(treeItemContextMenuEntryExtensionPoint, {
  identifier: `siriusweb_${treeItemContextMenuEntryExtensionPoint.identifier}_document`,
  Component: SysONDocumentTreeItemContextMenuContribution,
});

extensionRegistry.addComponent(treeItemContextMenuEntryExtensionPoint, {
  identifier: `syson${treeItemContextMenuEntryExtensionPoint.identifier}_insertTextualSysML`,
  Component: InsertTextualSysMLMenuContribution,
});

extensionRegistry.addComponent(footerExtensionPoint, {
  identifier: `syson_${footerExtensionPoint.identifier}`,
  Component: SysONFooter,
});

/*
 * Custom node contribution
 */
const nodeTypeRegistry: NodeTypeRegistry = {
  nodeLayoutHandlers: [
    new SysMLPackageNodeLayoutHandler(),
    new SysMLNoteNodeLayoutHandler(),
    new SysMLImportedPackageNodeLayoutHandler(),
  ],
  nodeConverters: [
    new SysMLPackageNodeConverter(),
    new SysMLNoteNodeConverter(),
    new SysMLImportedPackageNodeConverter(),
  ],
  nodeTypeContributions: [
    <NodeTypeContribution component={SysMLPackageNode} type={'sysMLPackageNode'} />,
    <NodeTypeContribution component={SysMLNoteNode} type={'sysMLNoteNode'} />,
    <NodeTypeContribution component={SysMLImportedPackageNode} type={'sysMLImportedPackageNode'} />,
  ],
};

const container = document.getElementById('root');
const root = createRoot(container!);
root.render(
  <SiriusWebApplication
    httpOrigin={httpOrigin}
    wsOrigin={wsOrigin}
    theme={sysonTheme}
    extensionRegistryMergeStrategy={new SysONExtensionRegistryMergeStrategy()}
    extensionRegistry={extensionRegistry}>
    <DiagramRepresentationConfiguration nodeTypeRegistry={nodeTypeRegistry} />
  </SiriusWebApplication>
);
