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

export class Project {
  public visit(projectId: string): Cypress.Chainable<Cypress.AUTWindow> {
    return cy.visit(`/projects/${projectId}/edit`);
  }
  public disableDeletionConfirmationDialog(): void {
    cy.window().then((win) => {
      win.localStorage.setItem('sirius-confirmation-dialog-disabled', JSON.stringify(true));
    });
  }
  public enableDeletionConfirmationDialog(): void {
    cy.window().then((win) => {
      win.localStorage.setItem('sirius-confirmation-dialog-disabled', JSON.stringify(false));
    });
  }
}
