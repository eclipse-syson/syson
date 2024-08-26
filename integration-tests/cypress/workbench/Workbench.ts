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

export class Workbench {
  public openRepresentation(representationLabel: string): void {
    cy.getByTestId(`onboard-open-${representationLabel}`).click();
  }

  public closeRepresentation(representationLabel: string): void {
    cy.getByTestId(`close-representation-tab-${representationLabel}`).click();
  }

  public performAction(actionLabel: string): void {
    cy.get('[data-testid="onboard-area"]')
      .find('[data-testid="actions"]')
      .contains(new RegExp('^' + actionLabel + '$', 'g'))
      .click();
  }

  public showTab(label: string): void {
    cy.getByTestId(`representation-tab-${label}`).click();
  }

  public closeTab(label: string): void {
    cy.getByTestId(`close-representation-tab-${label}`).click();
  }

  public getSnackbar(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get('#notistack-snackbar');
  }
}
