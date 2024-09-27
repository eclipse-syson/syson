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
        diagram.getDiagram(diagramLabel).should('exist');
        // Wait for the diagram to display
        cy.wait(400);
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    // D&D does not work anymore with Cypress APIs
    context.skip('On a diagram with the "item def Hero" node', () => {
      beforeEach(() => {
        diagram.getDiagramElement(diagramLabel).click();
        const dataTransferHero = new DataTransfer();
        explorer.dragTreeItem('Hero', dataTransferHero);
        diagram.dropOnDiagram(diagramLabel, dataTransferHero);
        const dataTransferBatman = new DataTransfer();
        explorer.dragTreeItem('Batman', dataTransferBatman);
        diagram.dropOnDiagram(diagramLabel, dataTransferBatman);
      });

      it('The inherited members are visible in compartments (only if the option "inherited members" is enabled)', () => {
        cy.getByTestId('syson-diagram-panel-menu-icon').click();
        // wait for the GraphQL query to retrieve the value of the checkboxes
        cy.wait(1000);
        // be sure to have the inherited members enable
        cy.getByTestId('ShowHideDiagramInheritedMembersCheckbox').then(($cb) => {
          const check = $cb.attr('aria-label');
          if (check === 'Show Inherited Members in Diagrams') {
            $cb.trigger('click');
          }
        });
        // be sure to have the inherited members from standard libraries disable
        cy.getByTestId('ShowHideDiagramInheritedMembersFromStandardLibrariesCheckbox').then(($cb) => {
          const check = $cb.attr('aria-label');
          if (check === 'Hide Inherited Members from Standard Libraries in Diagrams') {
            $cb.trigger('click');
          }
        });
        // wait for the GraphQL mutations to apply
        cy.wait(1000);
        diagram.getNodes(diagramLabel, '«item def» Hero').should('exist');
        diagram
          .getNodes(diagramLabel, '«item def» Hero')
          .click({ force: true })
          .getByTestId('Palette')
          .should('exist')
          .findByTestId('Create')
          .findByTestId('expand')
          .click();
        diagram
          .getNodes(diagramLabel, '«item def» Hero')
          .getByTestId('Palette')
          .should('exist')
          .find('div[role=tooltip]')
          .findByTestId('New Attribute - Tool')
          .click();
        // wait for the tool to apply
        cy.wait(400);
        diagram.getNodes(diagramLabel, 'attributes').should('exist');
        diagram.getNodes(diagramLabel, '^isSolid = null').should('not.exist');
        diagram.getNodes(diagramLabel, 'attribute').should('exist');
      });

      it('The inherited members from standard libraries are visible in compartments (only if the option "inherited members from standard libraries" is enabled)', () => {
        cy.getByTestId('syson-diagram-panel-menu-icon').click();
        // wait for the GraphQL query to retrieve the value of the checkbox
        cy.wait(1000);
        // be sure to have the inherited members from standard libraries enable
        cy.getByTestId('ShowHideDiagramInheritedMembersFromStandardLibrariesCheckbox').then(($cb) => {
          $cb.trigger('click');
          const check = $cb.attr('aria-label');
          if (check === 'Show Inherited Members from Standard Libraries in Diagrams') {
            $cb.trigger('click');
          }
        });
        // wait for the GraphQL mutation to apply
        cy.wait(1000);

        diagram.getNodes(diagramLabel, 'Batman :> Hero').should('exist');
        diagram
          .getNodes(diagramLabel, 'Batman :> Hero')
          .click({ force: true })
          .getByTestId('Palette')
          .should('exist')
          .findByTestId('Create')
          .findByTestId('expand')
          .click();
        diagram
          .getNodes(diagramLabel, 'Batman :> Hero')
          .getByTestId('Palette')
          .should('exist')
          .find('div[role=tooltip]')
          .findByTestId('New Attribute - Tool')
          .click();
        diagram.getNodes(diagramLabel, 'attributes').should('exist');
        diagram.getNodes(diagramLabel, '^isSolid = null').should('exist');
        diagram.getNodes(diagramLabel, 'realName').should('exist');
        diagram.getNodes(diagramLabel, 'attribute').should('exist');

        // palette is also available on inherited members
        diagram.getNodes(diagramLabel, '^isSolid = null').click().getByTestId('Palette').should('exist');
      });
    });
  });
});
