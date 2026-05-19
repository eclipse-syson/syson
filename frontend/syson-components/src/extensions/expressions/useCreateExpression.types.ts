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
import { GQLMessage } from '@eclipse-sirius/sirius-components-core';

export interface UseCreateExpressionValue {
  createExpression: (editingContextId: string, parentElementId: string, expressionText: string) => void;
  messages: GQLMessage[] | null;
  newExpressionId: string | null;
  loading: boolean;
}

export interface GQLCreateExpressionVariables {
  input: GQLCreateExpressionInput;
}

export interface GQLCreateExpressionInput {
  id: string;
  editingContextId: string;
  parentElementId: string;
  expressionText: string;
}

export interface GQLCreateExpressionData {
  createExpression: GQLCreateExpressionPayload;
}

export interface GQLCreateExpressionPayload {
  __typename: string;
  messages: GQLMessage[];
}

export interface GQLErrorPayload extends GQLCreateExpressionPayload {}

export interface GQLCreateExpressionSuccessPayload extends GQLCreateExpressionPayload {
  newExpressionId: string;
}
