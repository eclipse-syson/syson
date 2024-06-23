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
  ApolloClientOptionsConfigurer,
  DiagramRepresentationConfiguration,
  NavigationBarIconProps,
  NavigationBarMenuProps,
  NodeTypeRegistry,
  SiriusWebApplication,
  apolloClientOptionsConfigurersExtensionPoint,
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
  sysMLPackageNodeStyleDocumentTransform,
} from '@eclipse-syson/syson-components';
import './fonts.css';
import './reset.css';
import './variables.css';

if (process.env.NODE_ENV !== 'production') {
  loadDevMessages();
  loadErrorMessages();
}

const SysONNavigationBarIcon = ({}: NavigationBarIconProps) => {
  return <SysONIcon />;
};
const SysONNavigationBarMenu = ({}: NavigationBarMenuProps) => {
  return <Help />;
};

const extensionRegistry: ExtensionRegistry = new ExtensionRegistry();
extensionRegistry.addComponent(navigationBarIconExtensionPoint, {
  identifier: 'syson_navigationbar#icon',
  Component: SysONNavigationBarIcon,
});
extensionRegistry.addComponent(navigationBarMenuExtensionPoint, {
  identifier: 'syson_navigationbar#menu',
  Component: SysONNavigationBarMenu,
});
const apolloClientOptionsConfigurer: ApolloClientOptionsConfigurer = (currentOptions) => {
  const { documentTransform } = currentOptions;

  const newDocumentTransform = documentTransform
    ? documentTransform.concat(sysMLPackageNodeStyleDocumentTransform)
    : sysMLPackageNodeStyleDocumentTransform;
  return {
    ...currentOptions,
    documentTransform: newDocumentTransform,
  };
};

extensionRegistry.putData(apolloClientOptionsConfigurersExtensionPoint, {
  identifier: `siriusWeb_${apolloClientOptionsConfigurersExtensionPoint.identifier}`,
  data: [apolloClientOptionsConfigurer],
});

const nodeTypeRegistry: NodeTypeRegistry = {
  nodeLayoutHandlers: [new SysMLPackageNodeLayoutHandler()],
  nodeConverters: [new SysMLPackageNodeConverter()],
  nodeTypeContributions: [<NodeTypeContribution component={SysMLPackageNode} type={'sysMLPackageNode'} />],
};

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
