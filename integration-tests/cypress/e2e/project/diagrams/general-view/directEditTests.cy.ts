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
    const explorer = new Explorer();
    const diagram = new Diagram();
    let projectId: string = '';
    beforeEach(() =>
      sysmlv2.createSysMLv2Project().then((createdProjectData) => {
        projectId = createdProjectData.projectId;
        new Project().visit(projectId);
        explorer.getExplorerView().contains(sysmlv2.getProjectLabel());
        explorer.expand(sysmlv2.getProjectLabel());
        explorer.getExplorerView().contains(sysmlv2.getRootNamespaceLabel());
        explorer.expand(sysmlv2.getRootNamespaceLabel());
        explorer.getExplorerView().contains(sysmlv2.getRootElementLabel());
        explorer.expand(sysmlv2.getRootElementLabel());
        explorer.select(diagramLabel);
        diagram.getDiagram(diagramLabel).should('exist');
        // Wait for the arrange all action to complete
        cy.wait(400);
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('On a PartUsage', () => {
      beforeEach(() => {
        diagram.getDiagramElement(diagramLabel).click();
        diagram.getPalette().should('exist').findByTestId('Structure').findByTestId('expand').click();
        diagram.getPalette().should('exist').find('div[role=tooltip]').findByTestId('New Part - Tool').click();
      });

      it('The inherited members are visible in compartments', () => {
        diagram.getNodes(diagramLabel, 'part').type('p1 :> parts{enter}');
        diagram.getNodes(diagramLabel, 'p1 :> Parts::parts').click();
        diagram
          .getNodes(diagramLabel, 'p1 :> Parts::parts')
          .getByTestId('Palette')
          .should('exist')
          .findByTestId('Create')
          .findByTestId('expand')
          .click();
        diagram
          .getNodes(diagramLabel, 'p1 :> Parts::parts')
          .getByTestId('Palette')
          .should('exist')
          .find('div[role=tooltip]')
          .findByTestId('New Attribute - Tool')
          .click();
        diagram.getNodes(diagramLabel, 'attributes').should('exist');
        diagram.getNodes(diagramLabel, '^isSolid = null').should('exist');
        diagram.getNodes(diagramLabel, 'attribute').should('exist');

        // palette is also available on inherited members
        diagram.getNodes(diagramLabel, '^isSolid = null').click().getByTestId('Palette').should('exist');
      });

      it('We can add a subset to Parts::parts by direct editing the existing PartUsage', () => {
        diagram.getNodes(diagramLabel, 'part').type('p1 :> parts{enter}');
        // for standard libraries elements, the qualified name is displayed
        diagram.getNodes(diagramLabel, 'p1 :> Parts::parts').should('exist');
      });

      it('We can add a subset to a new PartUsage (that will be created) by direct editing the existing PartUsage', () => {
        diagram.getNodes(diagramLabel, 'part').type('p1 :> aNewPart{enter}');
        // for standard libraries elements, the qualified name is displayed
        diagram.getNodes(diagramLabel, 'p1 :> aNewPart').should('exist');
        explorer.getExplorerView().contains('aNewPart');
      });
    });
  });
});
