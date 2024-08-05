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

import { GQLMessage } from '@eclipse-sirius/sirius-components-core';

export interface UseInsertTextualSysMLv2Value {
  insertTextualSysMLv2: (editingContextId: string, objectId: string, textualContent: string) => void;
  loading: boolean;
  textualSysMLv2Inserted: boolean;
}

export interface GQLInsertTextualSysMLv2MutationData {
  insertTextualSysMLv2: GQLInsertTextualSysMLv2Payload;
}

export interface GQLInsertTextualSysMLv2Payload {
  __typename: string;
}

export interface GQLErrorPayload extends GQLInsertTextualSysMLv2Payload {
  messages: GQLMessage[];
}
