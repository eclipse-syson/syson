/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

export interface UseDeleteExpressionValue {
  deleteExpression: (editingContextId: string, elementId: string) => void;
}

export interface GQLDeleteExpressionData {
  deleteExpression: GQLDeleteExpressionPayload;
}

export interface GQLDeleteExpressionPayload {
  __typename: string;
}

export interface GQLDeleteExpressionVariables {
  input: GQLDeleteExpressionInput;
}

export interface GQLDeleteExpressionInput {
  id: string;
  editingContextId: string;
  parentElementId: string;
}

export interface GQLSuccessPayload extends GQLDeleteExpressionPayload {
  id: string;
}

export interface GQLErrorPayload extends GQLDeleteExpressionPayload {
  message: string;
}
