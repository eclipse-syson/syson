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
import { loadDevMessages, loadErrorMessages } from '@apollo/client/dev';
import {
  DiagramRepresentationConfiguration,
  NavigationBarIconProps,
  NavigationBarMenuProps,
  NodeTypeRegistry,
  SiriusWebApplication,
  navigationBarIconExtensionPoint,
  navigationBarMenuExtensionPoint,
} from '@eclipse-sirius/sirius-web-application';
import ReactDOM from 'react-dom';
import { Help } from './core/Help';
import { SysONIcon } from './core/SysONIcon';
import { httpOrigin, wsOrigin } from './core/URL';
import { sysonTheme } from './theme/sysonTheme';

import { ExtensionRegistry } from '@eclipse-sirius/sirius-components-core';
import { NodeTypeContribution } from '@eclipse-sirius/sirius-components-diagrams';
import {
  SysMLPackageNode,
  SysMLPackageNodeConverter,
  SysMLPackageNodeLayoutHandler,
} from '@eclipse-syson/syson-components';
import './fonts.css';
import './reset.css';
import './variables.css';

if (process.env.NODE_ENV !== 'production') {
  loadDevMessages();
  loadErrorMessages();
}

const nodeTypeRegistry: NodeTypeRegistry = {
  graphQLNodeStyleFragments: [
    {
      type: 'SysMLPackageNodeStyle',
      fields: `borderColor borderSize borderStyle color`,
    },
  ],
  nodeLayoutHandlers: [new SysMLPackageNodeLayoutHandler()],
  nodeConverters: [new SysMLPackageNodeConverter()],
  nodeTypeContributions: [<NodeTypeContribution component={SysMLPackageNode} type={'sysMLPackageNode'} />],
};

const SysONNavigationBarIcon = ({}: NavigationBarIconProps) => {
  return <SysONIcon />;
};
const SysONNavigationBarMenu = ({}: NavigationBarMenuProps) => {
  return <Help />;
};

const extensionRegistry: ExtensionRegistry = new ExtensionRegistry();
extensionRegistry.addComponent(navigationBarIconExtensionPoint, {
  Component: SysONNavigationBarIcon,
});
extensionRegistry.addComponent(navigationBarMenuExtensionPoint, {
  Component: SysONNavigationBarMenu,
});
ReactDOM.render(
  <SiriusWebApplication
    httpOrigin={httpOrigin}
    wsOrigin={wsOrigin}
    theme={sysonTheme}
    extensionRegistry={extensionRegistry}>
    <DiagramRepresentationConfiguration nodeTypeRegistry={nodeTypeRegistry} />
  </SiriusWebApplication>,
  document.getElementById('root')
);
