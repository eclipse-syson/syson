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

export interface QueryResponse<T> {
  data: QueryData<T>;
}

export interface QueryData<T> {
  viewer: Viewer<T>;
}

export type Viewer<T> = {
  [Key in keyof T]: T[Key];
};

export interface MutationResponse<T> {
  data: MutationData<T>;
}

export type MutationData<T> = {
  [Key in keyof T]: T[Key];
};
