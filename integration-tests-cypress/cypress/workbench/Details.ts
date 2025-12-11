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

export class Details {
  public getDetailsView(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.getByTestId('view-Details');
  }

  public getPage(label: string): Cypress.Chainable<JQuery<HTMLInputElement>> {
    return cy.get(`[data-testid="page-tab-${label}"] div > div`);
  }

  public getGroup(label: string): Cypress.Chainable<JQuery<HTMLInputElement>> {
    return this.getDetailsView().find(`[data-testid="group-${label}"]`);
  }

  public getTextField(label: string): Cypress.Chainable<JQuery<HTMLInputElement | HTMLTextAreaElement>> {
    return this.getDetailsView().find(`[data-testid="input-${label}"]`);
  }

  public getCheckBoxInput(label: string): Cypress.Chainable<JQuery<HTMLInputElement>> {
    return this.getDetailsView().find(`[data-testid="${label}"]`).get('input[type="checkbox"]');
  }

  public getRadioOption(label: string, option: string): Cypress.Chainable<JQuery<HTMLInputElement>> {
    return this.getDetailsView().contains('div', label).siblings().find(`[data-testid="${option}"]`).find('input');
  }

  public getReferenceWidget(label: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getDetailsView().findByTestId(label);
  }

  public getReferenceWidgetSelectedValue(
    referenceWidget: string,
    value: string
  ): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getReferenceWidget(referenceWidget).findByTestId(`reference-value-${value}`);
  }

  public openReferenceWidgetOptions(label: string): void {
    this.getReferenceWidget(label).find('button[title="Open"]').click();
  }

  public deleteReferenceWidgetOption(referenceWidgetLabel: string, optionLabel: string): void {
    this.getReferenceWidget(referenceWidgetLabel).findByTestId(`reference-value-${optionLabel}`).find('svg').click();
  }

  public selectReferenceWidgetOption(option: string): void {
    cy.getByTestId(`option-${option}`).should('exist').click();
  }

  public selectValue(selectWidgetLabel: string, valueLabel: string): void {
    this.getDetailsView().findByTestId(selectWidgetLabel).click();
    cy.get('ul[role="listbox"]').contains(valueLabel).click();
  }
}
