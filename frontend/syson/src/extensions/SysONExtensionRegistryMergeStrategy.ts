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

import {
  ComponentExtension,
  DataExtension,
  ExtensionRegistryMergeStrategy,
} from '@eclipse-sirius/sirius-components-core';
import { treeItemContextMenuEntryExtensionPoint } from '@eclipse-sirius/sirius-components-trees';
import {
  apolloClientOptionsConfigurersExtensionPoint,
  DefaultExtensionRegistryMergeStrategy,
  navigationBarMenuEntryExtensionPoint,
} from '@eclipse-sirius/sirius-web-application';

export class SysONExtensionRegistryMergeStrategy
  extends DefaultExtensionRegistryMergeStrategy
  implements ExtensionRegistryMergeStrategy
{
  public override mergeComponentExtensions(
    _identifier: string,
    existingValues: ComponentExtension<any>[],
    newValues: ComponentExtension<any>[]
  ): ComponentExtension<any>[] {
    if (_identifier === treeItemContextMenuEntryExtensionPoint.identifier) {
      return super.mergeComponentExtensions(
        _identifier,
        existingValues.filter((contribution) => {
          // Discard default versions of these extensions, we want to replace them with our own.
          return (
            contribution.identifier !== `siriusweb_${treeItemContextMenuEntryExtensionPoint.identifier}_object` &&
            contribution.identifier !== `siriusweb_${treeItemContextMenuEntryExtensionPoint.identifier}_document`
          );
        }),
        newValues
      );
    } else if (_identifier === navigationBarMenuEntryExtensionPoint.identifier) {
      // Temporary hack to remove the Libraries menu
      // To remove when the libraries mechanism will be fully implemented in Sirius Web
      return super.mergeComponentExtensions(
        _identifier,
        existingValues.filter((contribution) => {
          return contribution.identifier !== `siriusweb_${navigationBarMenuEntryExtensionPoint.identifier}_libraries`;
        }),
        newValues
      );
    }
    return super.mergeComponentExtensions(_identifier, existingValues, newValues);
  }

  public override mergeDataExtensions(
    identifier: string,
    existingValues: DataExtension<any>,
    newValues: DataExtension<any>
  ): DataExtension<any> {
    if (identifier === apolloClientOptionsConfigurersExtensionPoint.identifier) {
      return this.mergeApolloClientContributions(existingValues, newValues);
    }
    return newValues;
  }

  private mergeApolloClientContributions(
    existingApolloClientContributions: DataExtension<any>,
    newApolloClientContributions: DataExtension<any>
  ): DataExtension<any> {
    return {
      identifier: 'syson_apolloClient#apolloClientOptionsConfigurers',
      data: [...existingApolloClientContributions.data, ...newApolloClientContributions.data],
    };
  }
}
