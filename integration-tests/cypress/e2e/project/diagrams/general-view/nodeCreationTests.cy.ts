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
import { SysMLv2 } from '../../../../usecases/SysMLv2';
import { Diagram } from '../../../../workbench/Diagram';
import { Explorer } from '../../../../workbench/Explorer';

describe('Node Creation Tests', () => {
  const sysmlv2 = new SysMLv2();
  const diagramLabel = 'General View';

  context('Given a SysMLv2 project with a General View diagram', () => {
    const diagram = new Diagram();
    let projectId: string = '';
    beforeEach(() =>
      sysmlv2.createSysMLv2Project().then((createdProjectData) => {
        projectId = createdProjectData.projectId;
        new Project().visit(projectId);
        const explorer = new Explorer();
        explorer.getExplorerView().contains(sysmlv2.getProjectLabel());
        explorer.expand(sysmlv2.getProjectLabel());
        explorer.getExplorerView().contains(sysmlv2.getRootElementLabel());
        explorer.expand(sysmlv2.getRootElementLabel());
        explorer.select(diagramLabel);
        diagram.getDiagram(diagramLabel).should('exist');
        // Wait for the arrange all action to complete
        cy.wait(400);
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('When we create an AttributeUsage using the diagram palette', () => {
      beforeEach(() => {
        diagram.getDiagramElement(diagramLabel).click();
        diagram.getPaletteToolSection(0).click();
      });

      it('Then the new AttributeUsage is visible on the diagram and its compartments are not visible', () => {
        diagram.getNodes(diagramLabel, 'attribute').should('exist');
        // Check that the compartments of the node aren't visible
        diagram.getNodes(diagramLabel, 'attributes').should('not.exist');
        diagram.getNodes(diagramLabel, 'references').should('not.exist');
      });

      it('Then the new AttributeUsage has rounded borders', () => {
        diagram.getNodes(diagramLabel, 'attribute').find('div').should('have.css', 'border-radius', '10px');
      });

      it('Then we can create a new AttributeUsage inside it', () => {
        diagram.getNodes(diagramLabel, 'attribute').type('AttributeContainer{enter}');
        // Make sure the edition is done and the new name is visible
        cy.getByTestId('List - «attribute»\nAttributeContainer').should('exist');
        diagram.getNodes(diagramLabel, 'AttributeContainer').click();
        diagram.getPaletteToolSection(0).click();
        diagram.getNodes(diagramLabel, 'attribute').should('exist');
        diagram.getNodes(diagramLabel, 'attributes').should('exist');
        diagram.isNodeInside('List - attributes', 'List - «attribute»\nAttributeContainer');
        diagram.isNodeInside('IconLabel - attribute', 'List - attributes');
        diagram.getNodes(diagramLabel, 'references').should('not.exist');
      });
    });

    context('When we create an AttributeDefinition using the diagram palette', () => {
      beforeEach(() => {
        diagram.getDiagramElement(diagramLabel).click();
        diagram.getPalette().should('exist').findByTestId('Structure').findByTestId('expand').click();
        diagram
          .getPalette()
          .should('exist')
          .find('div[role=tooltip]')
          .findByTestId('New Attribute Definition - Tool')
          .click();
      });

      it('Then the new AttributeDefinition is visible on the diagram and its compartments are not visible', () => {
        diagram.getNodes(diagramLabel, 'attribute').should('exist');
        diagram.getNodes(diagramLabel, 'attributes').should('not.exist');
      });

      it('Then the new AttributeDefinition has squared borders', () => {
        diagram.getNodes(diagramLabel, 'attribute').find('div').should('have.css', 'border-radius', '0px');
      });

      it('Then we can create a new AttributeUsage inside it', () => {
        diagram.getNodes(diagramLabel, 'attribute').type('AttributeContainer{enter}');
        // Make sure the edition is done and the new name is visible
        cy.getByTestId('List - «attribute def»\nAttributeContainer').should('exist');
        diagram.getNodes(diagramLabel, 'AttributeContainer').click();
        diagram.getPaletteToolSection(0).click();
        diagram.getNodes(diagramLabel, 'attribute').should('exist');
        diagram.getNodes(diagramLabel, 'attributes').should('exist');
        diagram.isNodeInside('List - attributes', 'List - «attribute def»\nAttributeContainer');
        diagram.isNodeInside('IconLabel - attribute', 'List - attributes');
      });
    });
  });
});
