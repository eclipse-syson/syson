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

export interface ShowHideDiagramsInheritedMembersState {
  checked: boolean | null;
  tooltip: string;
  message: string | null;
}

export interface GQLShowDiagramsInheritedMembersMutationData {
  showDiagramsInheritedMembers: GQLSetShowDiagramsInheritedMembersPayload;
}

export interface GQLSetShowDiagramsInheritedMembersPayload {
  __typename: string;
  show: boolean;
}

export interface GQLErrorPayload extends GQLSetShowDiagramsInheritedMembersPayload {
  message: string;
}

export interface GQLShowDiagramsInheritedMembersMutationVariables {
  input: GQLShowDiagramsInheritedMembersMutationInput;
}

export interface GQLShowDiagramsInheritedMembersMutationInput {
  id: string;
  editingContextId: string;
  representationId: string;
  show: boolean;
}
