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
import { GQLNodeStyle } from '@eclipse-sirius/sirius-components-diagrams';

export interface GQLSysMLImportedPackageNodeStyle extends GQLNodeStyle {
  background: string;
  borderColor: string;
  borderSize: number;
  borderStyle: string;
}

export interface SysMLImportedPackageNodePartProps {
  nodeId: string;
  style: GQLSysMLImportedPackageNodeStyle;
  customizedStyleProperties: string[];
}
