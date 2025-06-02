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
import { Workbench } from '../../../../workbench/Workbench';

// D&D does not work anymore with Cypress APIs
describe('Drop From Explorer Tests', () => {
  const sysmlv2 = new SysMLv2();
  const diagramLabel = 'General View';

  context('Given a SysMLv2 project with a General View diagram', () => {
    const diagram = new Diagram();
    const explorer = new Explorer();
    const workbench = new Workbench();
    let projectId: string = '';
    beforeEach(() =>
      sysmlv2.createSysMLv2Project().then((createdProjectData) => {
        projectId = createdProjectData.projectId;
        new Project().visit(projectId);
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
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('When we create a PartUsage with an AttributeUsage in the root Package in the explorer', () => {
      beforeEach(() => {
        explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-PartUsage');
        explorer.createObject('part1', 'SysMLv2EditService-AttributeUsage');
        explorer.getTreeItemByLabel('part1').should('exist');
        explorer
          .getTreeItemByLabel('attribute1')
          .should('exist')
          .parents('ul')
          .first()
          .siblings()
          .contains('part1')
          .should('exist');
      });

      it('Then we can drop the Package on the diagram', { retries: 3 }, () => {
        const dataTransfer = new DataTransfer();
        explorer.getTreeItemByLabel(sysmlv2.getRootElementLabel()).should('exist');
        explorer.dragTreeItem(sysmlv2.getRootElementLabel(), dataTransfer);
        diagram.dropOnDiagram(diagramLabel, dataTransfer);
        explorer.getTreeItemByLabel(sysmlv2.getRootElementLabel()).trigger('dragend');
        diagram.getNodes(diagramLabel, sysmlv2.getRootElementLabel()).should('exist');
      });

      it('Then we can drop the PartUsage on the diagram, and its compartment are not visible', () => {
        const dataTransfer = new DataTransfer();
        explorer.dragTreeItem('part1', dataTransfer);
        diagram.dropOnDiagram(diagramLabel, dataTransfer);

        diagram.getNodes(diagramLabel, 'part1').should('exist');
        // Check that the compartments of the node aren't visible
        diagram.getNodes(diagramLabel, 'attributes1').should('not.exist');
      });

      it('Then when we drop the PartUsage on the diagram twice, it is only represented once', { retries: 3 }, () => {
        const dataTransfer = new DataTransfer();
        explorer.dragTreeItem('part1', dataTransfer);
        diagram.dropOnDiagram(diagramLabel, dataTransfer);
        diagram.dropOnDiagram(diagramLabel, dataTransfer);

        diagram.getNodes(diagramLabel, 'part1').should('exist').should('have.length', 1);
        diagram.getNodes(diagramLabel, 'attributes1').should('not.exist');
        workbench
          .getSnackbar()
          .should('exist')
          .contains('The element part1 is already visible in its parent General View');
      });

      it(
        'Then when we drop the PartUsage on the diagram and on itself, it is only represented once, and a warning message is displayed',
        { retries: 3 },
        () => {
          const dataTransfer = new DataTransfer();
          explorer.dragTreeItem('part1', dataTransfer);
          diagram.dropOnDiagram(diagramLabel, dataTransfer);
          diagram.getNodes(diagramLabel, 'part1').should('exist').trigger('drop', { dataTransfer });

          diagram.getNodes(diagramLabel, 'part1').should('exist').should('have.length', 1);
          workbench.getSnackbar().should('exist').contains('Cannot drop part1 on itself');
        }
      );
    });
    context('When we create a PartUsage and a PartDefinition in the root Package in the explorer', () => {
      beforeEach(() => {
        explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-PartUsage');
        explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-PartDefinition');
        explorer.getTreeItemByLabel('part1').should('exist');
        explorer.getTreeItemByLabel('PartDefinition1').should('exist');
      });

      it(
        'Then when we drop the PartUsage on the diagram, and drop the PartDefinition on the PartUsage, the PartUsage is typed with the PartDefinition',
        { retries: 3 },
        () => {
          const dataTransfer = new DataTransfer();
          explorer.dragTreeItem('part1', dataTransfer);
          diagram.dropOnDiagram(diagramLabel, dataTransfer);
          diagram.getNodes(diagramLabel, 'part1').should('exist').should('have.length', 1);
          explorer.dragTreeItem('PartDefinition1', dataTransfer);
          diagram.getNodes(diagramLabel, 'part1').should('exist').trigger('drop', { dataTransfer });
          diagram.getNodes(diagramLabel, 'part1 : PartDefinition1').should('exist').should('have.length', 1);
        }
      );

      it(
        'Then when we drop the PartUsage and PartDefinition on the diagram, and drop the PartDefinition on the PartUsage, the PartUsage is typed with the PartDefinition and an edge is visible between the PartUsage and PartDefinition',
        { retries: 3 },
        () => {
          const dataTransfer = new DataTransfer();
          explorer.dragTreeItem('part1', dataTransfer);
          diagram.dropOnDiagram(diagramLabel, dataTransfer);
          diagram.getNodes(diagramLabel, 'part1').should('exist').should('have.length', 1);
          explorer.dragTreeItem('PartDefinition1', dataTransfer);
          diagram.dropOnDiagram(diagramLabel, dataTransfer, 'center');
          diagram.getNodes(diagramLabel, 'PartDefinition1').should('exist').should('have.length', 1);
          explorer.dragTreeItem('PartDefinition1', dataTransfer);
          diagram.getNodes(diagramLabel, 'part1').should('exist').trigger('drop', { dataTransfer });
          diagram.getNodes(diagramLabel, 'part1 : PartDefinition1').should('exist').should('have.length', 1);
          // Check that an edge has been created and that its end is a closed arrow with dots (i.e. a feature typing).
          diagram
            .getEdgePaths(diagramLabel)
            .should('have.length', 1)
            .invoke('attr', 'marker-end')
            .should('contain', '#ClosedArrowWithDots');
        }
      );
    });

    context('When we create a PartUsage in the root Package in the explorer', () => {
      beforeEach(() => {
        explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-PartUsage');
        explorer.getTreeItemByLabel('part1').should('exist');
      });

      it(
        'Then when we create a Documentation inside the Part and we drop the Documentation on the diagram, the Documentation is visible on the diagram',
        { retries: 3 },
        () => {
          explorer.createObject('part1', 'SysMLv2EditService-Documentation');
          explorer
            .getTreeItemByLabel('Documentation')
            .should('exist')
            .parents('ul')
            .first()
            .siblings()
            .contains('part1')
            .should('exist');

          const dataTransfer = new DataTransfer();
          explorer.dragTreeItem('part1', dataTransfer);
          diagram.dropOnDiagram(diagramLabel, dataTransfer);
          diagram.getNodes(diagramLabel, 'part1').should('exist').should('have.length', 1);
          explorer.dragTreeItem('Documentation', dataTransfer);
          diagram.dropOnDiagram(diagramLabel, dataTransfer);
          diagram.getNodes(diagramLabel, 'add doc here').should('exist').should('have.length', 1);
        }
      );

      it(
        'Then when we create a Comment inside the Part and we drop the Comment on the diagram, the Comment is visible on the diagram',
        { retries: 3 },
        () => {
          explorer.createObject('part1', 'SysMLv2EditService-Comment');
          explorer
            .getTreeItemByLabel('Comment')
            .should('exist')
            .parents('ul')
            .first()
            .siblings()
            .contains('part1')
            .should('exist');

          const dataTransfer = new DataTransfer();
          explorer.dragTreeItem('part1', dataTransfer);
          diagram.dropOnDiagram(diagramLabel, dataTransfer);
          diagram.getNodes(diagramLabel, 'part1').should('exist').should('have.length', 1);
          explorer.dragTreeItem('Comment', dataTransfer);
          diagram.dropOnDiagram(diagramLabel, dataTransfer);
          diagram.getNodes(diagramLabel, 'add comment here').should('exist').should('have.length', 1);
        }
      );
    });
  });
});
