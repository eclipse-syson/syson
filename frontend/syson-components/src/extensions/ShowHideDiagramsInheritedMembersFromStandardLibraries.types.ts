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

export interface ShowHideDiagramsInheritedMembersFromStandardLibrariesState {
  checked: boolean | null;
  tooltip: string;
  message: string | null;
}

export interface GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationData {
  showDiagramsInheritedMembersFromStandardLibraries: GQLSetShowDiagramsInheritedMembersFromStandardLibrariesPayload;
}

export interface GQLSetShowDiagramsInheritedMembersFromStandardLibrariesPayload {
  __typename: string;
  show: boolean;
}

export interface GQLErrorPayload extends GQLSetShowDiagramsInheritedMembersFromStandardLibrariesPayload {
  message: string;
}

export interface GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationVariables {
  input: GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationInput;
}

export interface GQLShowDiagramsInheritedMembersFromStandardLibrariesMutationInput {
  id: string;
  editingContextId: string;
  representationId: string;
  show: boolean;
}
