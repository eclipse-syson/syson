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
import '@testing-library/user-event';

Cypress.Commands.add('dragAndDropNode', (sourceNodeTestId, targetNodeTestId) => {
  cy.window().then((win) => {
    const sourceNode = cy.getByTestId('rf__wrapper').findByTestId(sourceNodeTestId);
    const targetNode = cy.getByTestId('rf__wrapper').findByTestId(targetNodeTestId);

    targetNode.then((node) => {
      const target = node[0];
      const { x, y } = target.getBoundingClientRect();
      sourceNode
        .trigger('mousedown', {
          button: 0,
          force: true,
          view: win,
        })
        .wait(400)
        .trigger('mousemove', {
          clientX: x,
          clientY: y,
          force: true,
          view: win,
        })
        .trigger('mouseup', { view: win })
        .wait(400);
    });
  });
});

Cypress.Commands.add('isNodeInside', (childNodeTestId, parentNodeTestId) => {
  cy.window().then(() => {
    cy.getByTestId(parentNodeTestId).then(($parentNode) => {
      cy.getByTestId(childNodeTestId).then(($childNode) => {
        const parentPosition = $parentNode[0].getBoundingClientRect();
        const childPosition = $childNode[0].getBoundingClientRect();

        expect(childPosition.top >= parentPosition.top).to.be.true;
        expect(childPosition.bottom <= parentPosition.bottom).to.be.true;
        expect(childPosition.left >= parentPosition.left).to.be.true;
        expect(childPosition.right <= parentPosition.right).to.be.true;
      });
    });
  });
});
