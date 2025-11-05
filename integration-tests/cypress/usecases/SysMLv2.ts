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
import { CreatedProjectData } from './SysMLv2.types';

export class SysMLv2 {
  public createSysMLv2Project(): Cypress.Chainable<CreatedProjectData> {
    return cy.createProjectFromTemplate('sysmlv2-template').then((res) => {
      const payload = res.body.data.createProjectFromTemplate;
      if (isCreateProjectFromTemplateSuccessPayload(payload)) {
        const projectId = payload.project.id;
        const data: CreatedProjectData = { projectId };
        return cy.wrap(data);
      } else {
        throw new Error(`The project SysMLv2 has not been created`);
      }
    });
  }

  public getProjectLabel(): string {
    return 'SysMLv2.sysml';
  }

  public getRootNamespaceLabel(): string {
    return 'Namespace';
  }

  public getRootElementLabel(): string {
    return 'Package1';
  }

  public getLibrariesLabel(): string {
    return 'Libraries';
  }

  public getKerMLLabel(): string {
    return 'KerML [read-only]';
  }

  public getSysMLLabel(): string {
    return 'SysML [read-only]';
  }
}
