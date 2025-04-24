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
        explorer.expand(diagramLabel);
        explorer
          .getTreeItemByLabel(diagramLabel)
          .should('have.length', 2)
          .then(($elements) => {
            // $elements is a collection of all tree items with the label 'diagramLabel'
            // we want the second one, corresponding to the diagram
            const diag = $elements[1];
            diag?.click();
          });
        diagram.getDiagram(diagramLabel).should('exist').findByTestId('FreeForm - General View').should('exist');
        // Wait for the diagram to display
        cy.wait(400);
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('When we create an AttributeUsage using the diagram palette', () => {
      beforeEach(() => {
        diagram.getDiagramBackground(diagramLabel).rightclick().rightclick();
        diagram.getPalette().should('exist').findByTestId('toolSection-Structure').click();
        diagram.getPalette().should('exist').findByTestId('tool-New Attribute').click();
      });

      it(
        'Then the new AttributeUsage is visible on the diagram and its compartments are not visible',
        { retries: 3 },
        () => {
          diagram.getNodes(diagramLabel, 'attribute').should('exist');
          // Check that the compartments of the node aren't visible
          diagram.getNodes(diagramLabel, 'attributes').should('not.exist');
          diagram.getNodes(diagramLabel, 'references').should('not.exist');
          diagram.getNodes(diagramLabel, 'attribute').find('div').should('have.css', 'border-radius', '10px');
        }
      );

      //unstable test
      it.skip('Then we can create a new AttributeUsage inside it', () => {
        diagram.getNodes(diagramLabel, 'attribute').type('AttributeContainer{enter}', { waitForAnimations: true });
        // Make sure the edition is done and the new name is visible
        cy.getByTestId('List - «attribute»\nAttributeContainer').should('exist');

        diagram.getNodes(diagramLabel, 'AttributeContainer').click({ waitForAnimations: true });
        diagram.getPalette().should('exist').findByTestId('toolSection-Create').click();
        diagram.getPalette().should('exist').findByTestId('tool-New Attribute').click();
        diagram.getPalette().should('not.exist');
        diagram.getNodes(diagramLabel, 'attributes').should('exist');
        diagram.getNodes(diagramLabel, 'attribute').should('exist');
        diagram.isNodeInside('List - attributes', 'List - «attribute»\nAttributeContainer');
        diagram.isNodeInside('Label - attribute', 'List - attributes');
        diagram.getNodes(diagramLabel, 'references').should('not.exist');
      });
    });

    context('When we create an AttributeDefinition using the diagram palette', () => {
      beforeEach(() => {
        diagram.getDiagramBackground(diagramLabel).rightclick().rightclick();
        diagram.getPalette().should('exist').findByTestId('toolSection-Structure').click();
        diagram.getPalette().should('exist').findByTestId('tool-New Attribute Definition').click();
      });

      it(
        'Then the new AttributeDefinition is visible on the diagram and its compartments are not visible',
        { retries: 3 },
        () => {
          diagram.getNodes(diagramLabel, 'attribute').should('exist');
          diagram.getNodes(diagramLabel, 'attributes').should('not.exist');
          diagram.getNodes(diagramLabel, 'attribute').find('div').should('have.css', 'border-radius', '0px');
        }
      );

      //unstable test
      it.skip('Then we can create a new AttributeUsage inside it', () => {
        diagram
          .getNodes(diagramLabel, 'AttributeDefinition')
          .type('AttributeContainer{enter}', { waitForAnimations: true });
        // Make sure the edition is done and the new name is visible
        cy.getByTestId('List - «attribute def»\nAttributeContainer').should('exist');
        diagram.getNodes(diagramLabel, 'AttributeContainer').click();
        diagram.getPalette().should('exist').findByTestId('toolSection-Create').click();
        diagram.getPalette().should('exist').findByTestId('tool-New Attribute').click();
        diagram.getPalette().should('not.exist');
        diagram.getNodes(diagramLabel, 'attributes').should('exist');
        diagram.getNodes(diagramLabel, 'attribute').should('exist');
        diagram.isNodeInside('List - attributes', 'List - «attribute def»\nAttributeContainer');
        diagram.isNodeInside('IconLabel - attribute', 'List - attributes');
      });
    });
  });
});
