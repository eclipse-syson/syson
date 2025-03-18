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
  const diagramLabel = 'General View';

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
        explorer.expand(batmobile.getRootElementLabel());
        explorer.select(diagramLabel);
        diagram.getDiagram(diagramLabel).should('exist').findByTestId('FreeForm - Batmobile').should('exist');
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('The add existing elements (recursive) tool have been applied, following by an arrange all', () => {
      beforeEach(() => {
        diagram
          .getDiagram(diagramLabel)
          .should('exist')
          .findByTestId('FreeForm - Batmobile')
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
          .getDiagram(diagramLabel)
          .should('exist')
          .findByTestId('FreeForm - Batmobile')
          .should('not.exist', { timeout: 10000 });
        diagram.arrangeAll();
        cy.getByTestId('arrange-all-circular-loading')
          .should('exist')
          .then(() => {
            cy.getByTestId('arrange-all-circular-loading').should('not.exist');
          });
      });

      it('The add existing elements (recursive) tool add elements recursively', () => {
        diagram.getNodes(diagramLabel, '«item def» Hero').should('exist');
        diagram.getNodes(diagramLabel, '«item» power : Power').should('exist');
      });
    });
  });
});
