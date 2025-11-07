/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

export interface CreateProjectVariables {
  input: CreateProjectInput;
}

export interface CreateProjectInput {
  id: string;
  name: string;
  natures: string[];
  libraryIds: string[];
}

export interface CreateProjectData {
  createProject: CreateProjectPayload;
}

export interface CreateProjectPayload {
  __typename: string;
}

export interface CreateProjectSuccessPayload extends CreateProjectPayload {
  project: Project;
}

export interface Project {
  id: string;
}
