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

export interface CreateDocumentVariables {
  input: CreateDocumentInput;
}

export interface CreateDocumentInput {
  id: string;
  editingContextId: string;
  stereotypeId: string;
  name: string;
}

export interface CreateDocumentData {
  createDocument: CreateDocumentPayload;
}

export interface CreateDocumentPayload {
  __typename: string;
}

export interface CreateDocumentSuccessPayload extends CreateDocumentPayload {
  document: Document;
}

export interface Document {
  id: string;
}
