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

import { isCreateProjectFromTemplateSuccessPayload } from '../support/server/createProjectFromTemplateCommand';
import { CreatedProjectData } from './Batmobile.types';

export class Batmobile {
  public createBatmobileProject(): Cypress.Chainable<CreatedProjectData> {
    return cy.createProjectFromTemplate('Batmobile', 'batmobile-template', []).then((res) => {
      const payload = res.body.data.createProjectFromTemplate;
      if (isCreateProjectFromTemplateSuccessPayload(payload)) {
        const projectId = payload.project.id;
        const data: CreatedProjectData = { projectId };
        return cy.wrap(data);
      } else {
        throw new Error(`The project Batmobile has not been created`);
      }
    });
  }

  public getProjectLabel(): string {
    return 'Batmobile.sysml';
  }

  public getRootNamespaceLabel(): string {
    return 'Namespace';
  }

  public getRootElementLabel(): string {
    return 'Batmobile';
  }
}
