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

export class Diagram {
  public getDiagram(diagramLabel: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(`[data-representation-kind="diagram"][data-representation-label="${diagramLabel}"]`);
  }

  public fitToScreen() {
    cy.getByTestId('fit-to-screen').click();

    /* eslint-disable-next-line cypress/no-unnecessary-waiting */
    cy.wait(4000);
  }

  public arrangeAll() {
    cy.getByTestId('arrange-all').click();
  }

  public getDiagramBackground(diagramLabel: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getDiagram(diagramLabel).findByTestId('rf__wrapper');
  }

  public getNodes(diagramLabel: string, nodeLabel: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getDiagram(diagramLabel).contains('.react-flow__node', nodeLabel);
  }

  public getEdgePaths(diagramLabel: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getDiagram(diagramLabel).find('.react-flow__edge-path');
  }

  public getSelectedNodes(diagramLabel: string, nodeLabel: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getDiagram(diagramLabel).get('div.react-flow__node.selected').contains('.react-flow__node', nodeLabel);
  }

  public getPalette(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.getByTestId('Palette');
  }

  public getPaletteToolSection(index: number): Cypress.Chainable<JQuery<HTMLDivElement>> {
    return this.getPalette().should('exist').children('div').eq(index).children('div');
  }

  public getGroupPalette(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.getByTestId('GroupPalette');
  }

  public getDiagramScale(diagramLabel: string): Cypress.Chainable<number> {
    return this.getDiagram(diagramLabel)
      .find('.react-flow__viewport')
      .invoke('attr', 'style')
      .then((styleValue) => {
        let scale = 1;
        if (styleValue) {
          const match = /scale\(([^)]+)\)/.exec(styleValue);
          if (match && match[1]) {
            scale = parseFloat(match[1]);
          }
        }
        return scale;
      });
  }

  public getNodeCssValue(diagramLabel: string, nodeLabel: string, cssValue: string): Cypress.Chainable<number> {
    return this.getNodes(diagramLabel, nodeLabel)
      .invoke('css', cssValue)
      .then((widthValue) => {
        return parseInt(String(widthValue));
      });
  }

  public dragAndDropNode(sourceNodeTestId: string, targetNodeTestId: string): void {
    cy.window().then((win) => {
      cy.getByTestId('rf__wrapper')
        .findByTestId(targetNodeTestId)
        .then((node) => {
          const target = node[0];
          if (target) {
            const { x, y } = target.getBoundingClientRect();
            cy.getByTestId('rf__wrapper').findByTestId(sourceNodeTestId).trigger('mousedown', {
              button: 0,
              force: true,
              view: win,
            });
            // Move a first time to trigger nodeDrag (needed since nodeDragThreshold={1})
            cy.getByTestId('rf__wrapper').findByTestId(sourceNodeTestId).trigger('mousemove', 500, 500, {
              force: true,
              view: win,
            });
            // eslint-disable-next-line cypress/no-unnecessary-waiting
            cy.wait(400); // the time needed to calculate compatible nodes
            //Warning : THERE IS AN ISSUE WITH THE COORDINATES HERE, the code is not doing what is meant to do ...
            cy.getByTestId('rf__wrapper').findByTestId(sourceNodeTestId).trigger('mousemove', x, y, {
              force: true,
              view: win,
            });
            cy.getByTestId('rf__wrapper').findByTestId(sourceNodeTestId).trigger('mouseup', {
              view: win,
            });
            // eslint-disable-next-line cypress/no-unnecessary-waiting
            cy.wait(500); // the time needed to process the drop action
          }
        });
    });
  }

  public isNodeInside(childNodeTestId: string, parentNodeTestId: string): void {
    cy.window().then(() => {
      cy.getByTestId(parentNodeTestId).then(($parentNode) => {
        cy.getByTestId(childNodeTestId).then(($childNode) => {
          const parentPosition = $parentNode[0]?.getBoundingClientRect();
          const childPosition = $childNode[0]?.getBoundingClientRect();
          if (parentPosition && childPosition) {
            expect(childPosition.top >= parentPosition.top).to.be.true;
            expect(childPosition.bottom <= parentPosition.bottom).to.be.true;
            expect(childPosition.left >= parentPosition.left).to.be.true;
            expect(childPosition.right <= parentPosition.right).to.be.true;
          } else {
            expect(false, 'Nodes to be tested do not exist');
          }
        });
      });
    });
  }

  public shareDiagram(): void {
    cy.getByTestId('share').click();
  }

  public dropOnDiagram(diagramLabel: string, dataTransfer: DataTransfer, position?: Cypress.PositionType): void {
    const dropPosition = position ? position : 'bottomRight';
    this.getDiagram(diagramLabel).getByTestId('rf__wrapper').trigger('drop', dropPosition, { dataTransfer });
  }

  public roundSvgPathData(pathData: string): string {
    const pathValues = pathData.split(/([a-zA-Z])/);
    const roundedPathValues: string[] = [];

    for (let i = 0; i < pathValues.length; i++) {
      const pathValue = pathValues[i];
      if (pathValue) {
        if (pathValue.match(/[-+]?\d*\.?\d+(?:[eE][-+]?\d+)?/g)) {
          roundedPathValues.push(parseFloat(pathValue).toFixed(2));
        } else {
          roundedPathValues.push(pathValue);
        }
      }
    }

    let roundedPathData = '';
    for (let i = 0; i < roundedPathValues.length; i++) {
      roundedPathData += roundedPathValues[i];
    }
    return roundedPathData;
  }

  public getLabel(labelId: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.getByTestId(`Label - ${labelId}`);
  }
}
