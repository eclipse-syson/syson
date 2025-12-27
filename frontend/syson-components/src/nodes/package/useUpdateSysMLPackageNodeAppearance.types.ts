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
import { GQLErrorPayload, GQLSuccessPayload } from '@eclipse-sirius/sirius-components-core';

export interface UseUpdateSysMLPackageNodeAppearanceValue {
  updateSysMLPackageNodeAppearance: (
    editingContextId: string,
    representationId: string,
    nodeIds: string[],
    appearance: Partial<GQLSysMLPackageNodeAppearanceInput>
  ) => void;
}

export interface GQLEditSysMLPackageNodeAppearanceData {
  editSysMLPackageNodeAppearance: GQLEditSysMLPackageNodeAppearancePayload;
}

export type GQLEditSysMLPackageNodeAppearancePayload = GQLErrorPayload | GQLSuccessPayload;

export interface GQLEditSysMLPackageNodeAppearanceVariables {
  input: GQLEditSysMLPackageNodeAppearanceInput;
}

export interface GQLEditSysMLPackageNodeAppearanceInput {
  id: string;
  editingContextId: string;
  representationId: string;
  nodeIds: string[];
  appearance: Partial<GQLSysMLPackageNodeAppearanceInput>;
}

export interface GQLSysMLPackageNodeAppearanceInput {
  background: string;
  borderColor: string;
  borderRadius: number;
  borderSize: number;
  borderStyle: string;
}
