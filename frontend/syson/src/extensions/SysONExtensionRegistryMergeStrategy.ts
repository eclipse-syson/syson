/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import { ComponentExtension, ExtensionRegistryMergeStrategy } from '@eclipse-sirius/sirius-components-core';
import { DefaultExtensionRegistryMergeStrategy } from '@eclipse-sirius/sirius-web-application';
import { treeItemContextMenuEntryExtensionPoint } from '@eclipse-sirius/sirius-components-trees';

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
    }
    return super.mergeComponentExtensions(_identifier, existingValues, newValues);
  }
}
