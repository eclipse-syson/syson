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

export interface UseUpdateSysMLViewFrameNodeAppearanceValue {
  updateSysMLViewFrameNodeAppearance: (
    editingContextId: string,
    representationId: string,
    nodeIds: string[],
    appearance: Partial<GQLSysMLViewFrameNodeAppearanceInput>
  ) => void;
}

export interface GQLEditSysMLViewFrameNodeAppearanceData {
  editSysMLViewFrameNodeAppearance: GQLEditSysMLViewFrameNodeAppearancePayload;
}

export type GQLEditSysMLViewFrameNodeAppearancePayload = GQLErrorPayload | GQLSuccessPayload;

export interface GQLEditSysMLViewFrameNodeAppearanceVariables {
  input: GQLEditSysMLViewFrameNodeAppearanceInput;
}

export interface GQLEditSysMLViewFrameNodeAppearanceInput {
  id: string;
  editingContextId: string;
  representationId: string;
  nodeIds: string[];
  appearance: Partial<GQLSysMLViewFrameNodeAppearanceInput>;
}

export interface GQLSysMLViewFrameNodeAppearanceInput {
  background: string;
  borderColor: string;
  borderRadius: number;
  borderSize: number;
  borderStyle: string;
}
