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

export interface UseUpdateSysMLNoteNodeAppearanceValue {
  updateSysMLNoteNodeAppearance: (
    editingContextId: string,
    representationId: string,
    nodeId: string,
    appearance: Partial<GQLSysMLNoteNodeAppearanceInput>
  ) => void;
}

export interface GQLEditSysMLNoteNodeAppearanceData {
  editSysMLNoteNodeAppearance: GQLEditSysMLNoteNodeAppearancePayload;
}

export type GQLEditSysMLNoteNodeAppearancePayload = GQLErrorPayload | GQLSuccessPayload;

export interface GQLEditSysMLNoteNodeAppearanceVariables {
  input: GQLEditSysMLNoteNodeAppearanceInput;
}

export interface GQLEditSysMLNoteNodeAppearanceInput {
  id: string;
  editingContextId: string;
  representationId: string;
  nodeId: string;
  appearance: Partial<GQLSysMLNoteNodeAppearanceInput>;
}

export interface GQLSysMLNoteNodeAppearanceInput {
  background: string;
  borderColor: string;
  borderSize: number;
  borderStyle: string;
}
