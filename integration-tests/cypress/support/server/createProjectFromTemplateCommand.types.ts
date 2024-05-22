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

export interface CreateProjectFromTemplateVariables {}

export interface CreateProjectFromTemplateInput {
  id: string;
  templateId: string;
}

export interface CreateProjectFromTemplateData {
  createProjectFromTemplate: CreateProjectFromTemplatePayload;
}

export interface CreateProjectFromTemplatePayload {
  __typename: string;
}

export interface CreateProjectFromTemplateSuccessPayload extends CreateProjectFromTemplatePayload {
  project: Project;
  representationToOpen: Representation;
}

export interface Project {
  id: string;
}

export interface Representation {
  id: string;
}

export interface ErrorPayload extends CreateProjectFromTemplatePayload {
  message: string;
}
