/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
  GQLWidget,
  PropertySectionComponent,
  PropertySectionComponentRegistry,
  PropertySectionContext,
  PropertySectionContextValue,
  WidgetContribution,
} from '@eclipse-sirius/sirius-components-forms';
import {
  GQLReferenceWidget,
  ReferenceIcon,
  ReferencePreview,
  ReferencePropertySection,
} from '@eclipse-sirius/sirius-components-widget-reference';
import { SiriusWebApplication, Views } from '@eclipse-sirius/sirius-web-application';
import ReactDOM from 'react-dom';
import { Help } from './core/Help';
import { SysONIcon } from './core/SysONIcon';
import { httpOrigin, wsOrigin } from './core/URL';
import { sysonTheme } from './theme/sysonTheme';

import './Sprotty.css';
import './fonts.css';
import './reset.css';
import './variables.css';

if (process.env.NODE_ENV !== 'production') {
  loadDevMessages();
  loadErrorMessages();
}

const isReferenceWidget = (widget: GQLWidget): widget is GQLReferenceWidget => widget.__typename === 'ReferenceWidget';

const propertySectionsRegistry: PropertySectionComponentRegistry = {
  getComponent: (widget: GQLWidget): PropertySectionComponent<GQLWidget> | null => {
    if (isReferenceWidget(widget)) {
      return ReferencePropertySection;
    }
    return null;
  },
  getPreviewComponent: (widget: GQLWidget) => {
    if (widget.__typename === 'ReferenceWidget') {
      return ReferencePreview;
    }
    return null;
  },
  getWidgetContributions: () => {
    const referenceWidget: WidgetContribution = {
      name: 'ReferenceWidget',
      fields: `label
               iconURL
               ownerId
               descriptionId
               reference {
                 ownerKind
                 referenceKind
                 containment
                 manyValued
               }
               referenceValues {
                 id
                 label
                 kind
                 iconURL
                 hasClickAction
               }
               style {
                 color
                 fontSize
                 italic
                 bold
                 underline
                 strikeThrough
               }`,
      icon: <ReferenceIcon />,
    };
    return [referenceWidget];
  },
};

const propertySectionRegistryValue: PropertySectionContextValue = {
  propertySectionsRegistry,
};

ReactDOM.render(
  <PropertySectionContext.Provider value={propertySectionRegistryValue}>
    <SiriusWebApplication httpOrigin={httpOrigin} wsOrigin={wsOrigin} theme={sysonTheme}>
      <Views applicationIcon={<SysONIcon />} applicationBarMenu={<Help />} />
    </SiriusWebApplication>
  </PropertySectionContext.Provider>,
  document.getElementById('root')
);
