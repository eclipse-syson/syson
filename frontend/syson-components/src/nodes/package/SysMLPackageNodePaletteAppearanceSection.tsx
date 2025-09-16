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

import {
  LabelAppearancePart,
  PaletteAppearanceSectionContributionComponentProps,
} from '@eclipse-sirius/sirius-components-diagrams';

import { SysMLPackageNodePart } from './SysMLPackageNodePart';

export const SysMLPackageNodePaletteAppearanceSection = ({
  element,
  elementId,
}: PaletteAppearanceSectionContributionComponentProps) => {
  return (
    <>
      <SysMLPackageNodePart element={element} />
      {element.data.insideLabel ? (
        <LabelAppearancePart
          diagramElementId={elementId}
          labelId={element.data.insideLabel.id}
          position="Inside Label"
          style={element.data.insideLabel.appearanceData.gqlStyle}
          customizedStyleProperties={element.data.insideLabel.appearanceData.customizedStyleProperties}
        />
      ) : null}
    </>
  );
};
