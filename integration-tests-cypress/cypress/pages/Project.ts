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

export class Project {
  public visit(projectId: string): Cypress.Chainable<Cypress.AUTWindow> {
    return cy.visit(`/projects/${projectId}/edit`, {
      onBeforeLoad(win) {
        cy.spy(win.console, 'debug').as('consoleDebug');
      },
    });
  }

  public disableDeletionConfirmationDialog(projectId: string): void {
    cy.window().then((win) => {
      const disabledIds = this.getDisabledProjectIds(win);
      if(!disabledIds.includes(projectId)) {
        win.localStorage.setItem('sirius-confirmation-dialog-disabled', JSON.stringify([projectId, ...disabledIds]));
      }
    });
  }

  public enableDeletionConfirmationDialog(projectId: string): void {
    cy.window().then((win) => {
      const disabledIds = this.getDisabledProjectIds(win);
      if(disabledIds.includes(projectId)) {
        win.localStorage.setItem('sirius-confirmation-dialog-disabled', JSON.stringify(disabledIds.filter((id: string) => id !== projectId)));
      }
    });
  }

  public isDeletionConfirmationDialogDisabled(projectId: string): Cypress.Chainable<boolean> {
    return cy.window().then((win) => {
     return this.getDisabledProjectIds(win).includes(projectId);
    });
  }
  
  private getDisabledProjectIds(win: Cypress.AUTWindow): string[] {
    const value = win.localStorage.getItem('sirius-confirmation-dialog-disabled');
    if (value) {
      return JSON.parse(value);
    }
    return [];
  }
}
