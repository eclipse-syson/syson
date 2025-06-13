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

import { Project } from '../../../../pages/Project';
import { Batmobile } from '../../../../usecases/Batmobile';
import { Diagram } from '../../../../workbench/Diagram';
import { Explorer } from '../../../../workbench/Explorer';

describe('Diagram Panel in General View Tests', () => {
  const batmobile = new Batmobile();
  const viewUsage = 'view3';

  context('Given a Batmobile Project with a General View diagram', () => {
    const explorer = new Explorer();
    const diagram = new Diagram();
    let projectId: string = '';

    beforeEach(() =>
      batmobile.createBatmobileProject().then((createdProjectData) => {
        projectId = createdProjectData.projectId;
        new Project().visit(projectId);
        explorer.getExplorerView().contains(batmobile.getProjectLabel());
        explorer.expand(batmobile.getProjectLabel());
        explorer.getExplorerView().contains(batmobile.getRootElementLabel());
        explorer.select(batmobile.getRootElementLabel());
        explorer.createObject(batmobile.getRootElementLabel(), 'SysMLv2EditService-ViewUsage');

        explorer.select(viewUsage);
        explorer.expand(viewUsage);
        explorer
          .getTreeItemByLabel(viewUsage)
          .should('have.length', 2)
          .then(($elements) => {
            // $elements is a collection of all tree items with the label 'diagramLabel'
            // we want the second one, corresponding to the diagram
            const diag = $elements[1];
            diag?.click();
          });
        diagram.getDiagram(viewUsage).should('exist').findByTestId(`FreeForm - ${viewUsage}`).should('exist');
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context(
      'The add existing elements (recursive) tool have been applied, following by an arrange all',
      { retries: 3 },
      () => {
        beforeEach(() => {
          diagram
            .getDiagram(viewUsage)
            .should('exist')
            .findByTestId(`FreeForm - ${viewUsage}`)
            .should('exist')
            .rightclick()
            .rightclick();
          diagram.getPalette().should('exist').findByTestId('toolSection-Related Elements').should('exist').click();
          diagram
            .getPalette()
            .should('exist')
            .findByTestId('tool-Add existing nested elements (recursive)')
            .should('exist')
            .click();
          diagram.getPalette().should('not.exist', { timeout: 10000 });
          diagram
            .getDiagram(viewUsage)
            .should('exist')
            .findByTestId(`FreeForm - ${viewUsage}`)
            .should('not.exist', { timeout: 10000 });
          diagram.arrangeAll();
          cy.getByTestId('arrange-all-circular-loading')
            .should('exist')
            .then(() => {
              cy.getByTestId('arrange-all-circular-loading').should('not.exist');
            });
        });

        it('The add existing elements (recursive) tool add elements recursively', () => {
          diagram.getNodes(viewUsage, '«item def» Hero').should('exist');
          diagram.getNodes(viewUsage, '«item» power : Power').should('exist');
        });
      }
    );
  });
});
