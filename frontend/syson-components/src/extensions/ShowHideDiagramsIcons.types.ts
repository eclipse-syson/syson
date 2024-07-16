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

export interface ShowHideDiagramsIconsState {
  checked: boolean | null;
  tooltip: string;
  message: string | null;
}

export interface GQLShowDiagramsIconsMutationData {
  showDiagramsIcons: GQLSetShowDiagramsIconsPayload;
}

export interface GQLSetShowDiagramsIconsPayload {
  __typename: string;
  show: boolean;
}

export interface GQLErrorPayload extends GQLSetShowDiagramsIconsPayload {
  message: string;
}

export interface GQLShowDiagramsIconsMutationVariables {
  input: GQLShowDiagramsIconsMutationInput;
}

export interface GQLShowDiagramsIconsMutationInput {
  id: string;
  editingContextId: string;
  representationId: string;
  show: boolean;
}
