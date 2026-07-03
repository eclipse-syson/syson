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
import { GQLExpressionProperties } from './ExpressionProperties.types';

export interface UseEditExpressionValue {
  editExpression: (
    editingContextId: string,
    elementId: string,
    newValue: string,
    properties?: GQLExpressionProperties | null
  ) => void;
  loading: boolean;
  newExpressionId: string | null;
  messages: GQLMessage[] | null;
}

export interface GQLEditExpressionVariables {
  input: GQLEditExpressionInput;
}

export interface GQLEditExpressionInput {
  id: string;
  editingContextId: string;
  elementId: string;
  newExpressionText: string;
  properties?: GQLExpressionProperties | null;
}

export interface GQLEditExpressionData {
  editExpression: GQLEditExpressionPayload;
}

export interface GQLEditExpressionPayload {
  __typename: string;
  messages: GQLMessage[];
}

export interface GQLErrorPayload extends GQLEditExpressionPayload {}

export interface GQLEditExpressionSuccessPayload extends GQLEditExpressionPayload {
  newExpressionId: string;
}
