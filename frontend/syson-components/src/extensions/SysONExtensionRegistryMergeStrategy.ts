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

import { DataExtension, ExtensionRegistryMergeStrategy } from '@eclipse-sirius/sirius-components-core';
import { omniboxCommandOverrideContributionExtensionPoint } from '@eclipse-sirius/sirius-components-omnibox';
import { treeItemContextMenuEntryOverrideExtensionPoint } from '@eclipse-sirius/sirius-components-trees';
import {
  apolloClientOptionsConfigurersExtensionPoint,
  DefaultExtensionRegistryMergeStrategy,
} from '@eclipse-sirius/sirius-web-application';

export class SysONExtensionRegistryMergeStrategy
  extends DefaultExtensionRegistryMergeStrategy
  implements ExtensionRegistryMergeStrategy
{
  public override mergeDataExtensions(
    identifier: string,
    existingValues: DataExtension<any>,
    newValues: DataExtension<any>
  ): DataExtension<any> {
    if (identifier === apolloClientOptionsConfigurersExtensionPoint.identifier) {
      return this.mergeApolloClientContributions(existingValues, newValues);
    }
    if (identifier === omniboxCommandOverrideContributionExtensionPoint.identifier) {
      return this.mergeOmniboxCommandOverrideContributions(existingValues, newValues);
    }
    if (identifier === treeItemContextMenuEntryOverrideExtensionPoint.identifier) {
      return this.mergeTreeItemContributions(existingValues, newValues);
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

  private mergeOmniboxCommandOverrideContributions(
    existingOmniboxCommandOverrideContributions: DataExtension<any>,
    newOmniboxCommandOverrideContributions: DataExtension<any>
  ): DataExtension<any> {
    return {
      identifier: `syson_${omniboxCommandOverrideContributionExtensionPoint.identifier}_merged`,
      data: [...existingOmniboxCommandOverrideContributions.data, ...newOmniboxCommandOverrideContributions.data],
    };
  }

  private mergeTreeItemContributions(
    existingContributions: DataExtension<any>,
    newContributions: DataExtension<any>
  ): DataExtension<any> {
    return {
      identifier: `syson_${treeItemContextMenuEntryOverrideExtensionPoint.identifier}`,
      data: [...existingContributions.data, ...newContributions.data],
    };
  }
}
