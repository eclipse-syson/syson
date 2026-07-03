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
import { ExpressionProperties, ExpressionPropertiesContext } from './expressionProperties.types';

export type Mode = 'create' | 'edit';

export interface EditSysMLExpressionModalProps {
  editingContextId: string;
  elementId: string;
  mode: Mode;
  onClose: () => void;
  expressionPropertiesContext?: ExpressionPropertiesContext;
}

export type Operation = 'loading' | 'creating' | 'editing';

export interface EditSysMLExpressionModalState {
  operationInProgress: Operation | null;
  textualContent: string | null;
  validationResult: GQLMessage[] | null;
  properties: ExpressionProperties;
}
