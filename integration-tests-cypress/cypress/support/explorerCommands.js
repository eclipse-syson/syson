/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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

Cypress.Commands.add('createChildObject', (parent, objectType) => {
  cy.getByTestId(`${parent}-more`).click();
  cy.getByTestId('new-object').click();
  cy.getByTestId('new-object-modal').should('exist');
  cy.getByTestId('childCreationDescription').children('[role="combobox"]').invoke('text').should('have.length.gt', 1);
  cy.getByTestId('childCreationDescription').click();
  cy.getByTestId('childCreationDescription').get(`[data-value="${objectType}"]`).should('exist').click();
  cy.getByTestId('create-object').click();
  cy.getByTestId('new-object-modal').should('not.exist');
});

Cypress.Commands.add('createRepresentationFromExplorer', (parent, representationType) => {
  cy.getByTestId(`${parent}-more`).click();
  cy.getByTestId('treeitem-contextmenu').findByTestId('new-representation').click();
  cy.getByTestId('representationDescription').children('[role="combobox"]').invoke('text').should('have.length.gt', 1);
  cy.getByTestId('representationDescription').click();
  cy.getByTestId(representationType).should('exist').click();
  cy.getByTestId('create-representation').click();
});
