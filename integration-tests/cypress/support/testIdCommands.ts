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

/**
 * All specific TestId query helpers.
 */

/**
 * Same spirit like in standard cy.get https://docs.cypress.io/api/commands/get.html#Syntax but on a specific tag attribute named "data-test-id".
 * To use this command, you must specify a data-test-id attribute in your DOM.
 *
 * @param {string} testId the testId.
 */
Cypress.Commands.add('getByTestId', (testId) => {
  return cy.get(`[data-testid="${testId}"]`);
});

/**
 * Same spirit like in standard cy.find https://docs.cypress.io/api/commands/find.html#Syntax but on a specific tag attribute named "data-test-id".
 * To use this command, you must specify a data-test-id attribute in your DOM.
 *
 * @param {string} testId the testId.
 */
Cypress.Commands.add(
  'findByTestId',
  {
    prevSubject: true,
  },
  (subject, testId) => {
    return cy.wrap(subject).find(`[data-testid="${testId}"]`);
  }
);
