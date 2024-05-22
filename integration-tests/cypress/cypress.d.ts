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

import { CreateDocumentData } from './support/server/createDocumentCommand.types';
import { CreateProjectData } from './support/server/createProjectCommand.types';
import { CreateProjectFromTemplateData } from './support/server/createProjectFromTemplateCommand.types';
import { DeleteProjectData } from './support/server/deleteProjectCommand.types';
import { MutationResponse } from './support/server/graphql.types';

export {};

declare global {
  namespace Cypress {
    interface Chainable {
      getByTestId: (testId: string) => Chainable<JQuery<HTMLElement>>;
      findByTestId: (testId: string) => Chainable<JQuery<HTMLElement>>;

      createProject: (name: string) => Chainable<Response<MutationResponse<CreateProjectData>>>;
      createProjectFromTemplate: (name: string) => Chainable<Response<MutationResponse<CreateProjectFromTemplateData>>>;
      deleteProject: (projectId: string) => Chainable<Response<MutationResponse<DeleteProjectData>>>;

      createDocument: (
        editingContextId: string,
        stereotypeId: string,
        name: string
      ) => Chainable<Response<MutationResponse<CreateDocumentData>>>;
    }
  }
}
