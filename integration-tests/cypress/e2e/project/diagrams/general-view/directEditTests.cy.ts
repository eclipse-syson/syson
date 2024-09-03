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
import { Details } from '../../../../workbench/Details';
import { Diagram } from '../../../../workbench/Diagram';
import { Explorer } from '../../../../workbench/Explorer';

describe('Node Creation Tests', () => {
  const sysmlv2 = new SysMLv2();
  const diagramLabel = 'General View';
  const details = new Details();

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

      it('We can add a subset to Parts::parts by direct editing the existing PartUsage', () => {
        diagram.getNodes(diagramLabel, 'part').type('p1 :> parts{enter}');
        // for standard libraries elements, the qualified name is displayed
        diagram.getNodes(diagramLabel, 'p1 :> Parts::parts').should('exist');
      });

      it('We can add a value to 10 [kg] by direct editing the existing PartUsage', () => {
        diagram.getNodes(diagramLabel, 'part').type('p1 = 10 [kg]{enter}');
        diagram.getNodes(diagramLabel, 'p1 = 10 [kg]').should('exist');
        // Check that the library containing kg has been imported
        explorer.getExplorerView().contains('import SI::*');
      });

      it('We can add a subset to a new PartUsage (that will be created) by direct editing the existing PartUsage', () => {
        diagram.getNodes(diagramLabel, 'part').type('p1 :> aNewPart{enter}');
        // for standard libraries elements, the qualified name is displayed
        diagram.getNodes(diagramLabel, 'p1 :> aNewPart').should('exist');
        explorer.getExplorerView().contains('aNewPart');
      });

      it('We can rename a part with a name containing a properties keyword', () => {
        diagram.getNodes(diagramLabel, 'part').type('abstractPart{enter}');
        diagram.getNodes(diagramLabel, 'abstractPart').should('exist');
        explorer.getExplorerView().contains('abstractPart');
      });

      it('We can type the PartUsage with an element from a standard library', () => {
        diagram.getNodes(diagramLabel, 'part').type('part : ISQBase::MassValue{enter}');
        diagram.getNodes(diagramLabel, 'part : ISQBase::MassValue').should('exist');
        // The import is added in the explorer
        explorer.getTreeItemByLabel('import ISQBase::*').should('exist');
        // There are only 2 elements next to the part in the explorer (the import and the diagram).
        // No PartDefinition has been created.
        explorer.getTreeItemByLabel('part').parents('li').first().siblings().should('have.length', 2);
      });

      it('We can type the PartUsage with an element from an aliased standard library', () => {
        diagram.getNodes(diagramLabel, 'part').type('part : ISQ::MassValue{enter}');
        diagram.getNodes(diagramLabel, 'part : ISQBase::MassValue').should('exist');
        // The import is added in the explorer
        explorer.getTreeItemByLabel('import ISQBase::*').should('exist');
        // There are only 2 elements next to the part in the explorer (the import and the diagram).
        // No PartDefinition has been created.
        explorer.getTreeItemByLabel('part').parents('li').first().siblings().should('have.length', 2);
      });

      it('We can type the PartUsage with an element from a standard library by using its name with no qualifier', () => {
        diagram.getNodes(diagramLabel, 'part').type('part : MassValue{enter}');
        diagram.getNodes(diagramLabel, 'part : ISQBase::MassValue').should('exist');
        // The import is added in the explorer
        explorer.getTreeItemByLabel('import ISQBase::*').should('exist');
        // There are only 2 elements next to the part in the explorer (the import and the diagram).
        // No PartDefinition has been created.
        explorer.getTreeItemByLabel('part').parents('li').first().siblings().should('have.length', 2);
      });

      it('We can add properties to a new compartment item by direct editing the existing compartment item', () => {
        diagram.getNodes(diagramLabel, 'part').click();
        diagram
          .getNodes(diagramLabel, 'part')
          .getByTestId('Palette')
          .should('exist')
          .findByTestId('Create')
          .findByTestId('expand')
          .click();
        diagram
          .getNodes(diagramLabel, 'part')
          .getByTestId('Palette')
          .should('exist')
          .find('div[role=tooltip]')
          .findByTestId('New Attribute - Tool')
          .click();

        diagram.getNodes(diagramLabel, 'attribute').should('exist');

        // direct edit attribute
        cy.getByTestId('IconLabel - attribute').type('variation end myAttribute ordered nonunique{enter}');
        diagram.getNodes(diagramLabel, 'myAttribute').should('exist');

        // check attribute properties
        details.getPage('Advanced').click();
        details.getGroup('Attribute Properties').should('be.visible');
        details.getDetailsView().find(`[data-testid="Is Abstract"]`).should('have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is Variation"]`).should('have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is End"]`).should('have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is Ordered"]`).should('have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is Unique"]`).should('not.have.class', 'Mui-checked');

        // reset attribute to default properties
        diagram.getNodes(diagramLabel, 'myAttribute').should('exist');
        cy.getByTestId('IconLabel - variation end myAttribute ordered nonunique').type('myAttribute{enter}');

        // check attribute properties
        details.getPage('Advanced').click();
        details.getGroup('Attribute Properties').should('be.visible');
        details.getDetailsView().find(`[data-testid="Is Abstract"]`).should('have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is Variation"]`).should('not.have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is End"]`).should('not.have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is Ordered"]`).should('not.have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is Unique"]`).should('have.class', 'Mui-checked');
      });

      it('We can add direction to a new compartment item by direct editing the existing compartment item', () => {
        diagram.getNodes(diagramLabel, 'part').click();
        diagram
          .getNodes(diagramLabel, 'part')
          .getByTestId('Palette')
          .should('exist')
          .findByTestId('Create')
          .findByTestId('expand')
          .click();
        diagram
          .getNodes(diagramLabel, 'part')
          .getByTestId('Palette')
          .should('exist')
          .find('div[role=tooltip]')
          .findByTestId('New Attribute - Tool')
          .click();

        diagram.getNodes(diagramLabel, 'attribute').should('exist');

        // direct edit attribute
        cy.getByTestId('IconLabel - attribute').type('inout myAttribute{enter}');
        diagram.getNodes(diagramLabel, 'myAttribute').should('exist');

        // check direction attribute
        details.getRadioOption('Direction', 'inout').should('be.checked');

        // reset attribute to default properties
        diagram.getNodes(diagramLabel, 'myAttribute').should('exist');
        cy.getByTestId('IconLabel - inout myAttribute').type('myAttribute{enter}');

        // check direction attribute
        details.getRadioOption('Direction', 'inout').should('not.be.checked');
      });

      it('We can add properties to a new compartment item by direct editing the existing compartment item without taking into account keywords order', () => {
        diagram.getNodes(diagramLabel, 'part').click();
        diagram
          .getNodes(diagramLabel, 'part')
          .getByTestId('Palette')
          .should('exist')
          .findByTestId('Create')
          .findByTestId('expand')
          .click();
        diagram
          .getNodes(diagramLabel, 'part')
          .getByTestId('Palette')
          .should('exist')
          .find('div[role=tooltip]')
          .findByTestId('New Attribute - Tool')
          .click();

        diagram.getNodes(diagramLabel, 'attribute').should('exist');

        // direct edit attribute
        cy.getByTestId('IconLabel - attribute').type('end variation derived myAttribute{enter}');
        diagram.getNodes(diagramLabel, 'myAttribute').should('exist');

        // check attribute properties
        details.getPage('Advanced').click();
        details.getGroup('Attribute Properties').should('be.visible');
        details.getDetailsView().find(`[data-testid="Is Abstract"]`).should('have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is Variation"]`).should('have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is End"]`).should('have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is Derived"]`).should('have.class', 'Mui-checked');
      });

      it('Direct edit a usage graphical node', () => {
        diagram.getNodes(diagramLabel, 'part').click();

        diagram.getNodes(diagramLabel, 'part').should('exist');

        // direct edit attribute
        cy.getByTestId('Label - «part»\\a part').type('abstract myPart{enter}');
        diagram.getNodes(diagramLabel, 'myPart').should('exist');

        // check attribute properties
        details.getPage('Advanced').click();
        details.getGroup('Part Properties').should('be.visible');
        details.getDetailsView().find(`[data-testid="Is Abstract"]`).should('have.class', 'Mui-checked');
        details.getDetailsView().find(`[data-testid="Is Derived"]`).click();

        cy.getByTestId('Label - «abstract»\\a «part»\\a myPart').type('myPart{enter}');
        diagram.getNodes(diagramLabel, 'myPart').should('exist');
        details.getDetailsView().find(`[data-testid="Is Abstract"]`).should('not.have.class', 'Mui-checked');
        // even if derived keyword has not been typed, it is still derived because DirectEdit on graphical node does not take into account his keyword
        details.getDetailsView().find(`[data-testid="Is Derived"]`).click();
      });

      it('we can modify body of Documenation with direct edit on Documentation graphical node', () => {
        explorer.createObject('part', 'SysMLv2EditService-Documentation');
        explorer
          .getTreeItemByLabel('Documentation')
          .should('exist')
          .parents('ul')
          .first()
          .siblings()
          .contains('part')
          .should('exist');

        const dataTransfer = new DataTransfer();
        explorer.dragTreeItem('part', dataTransfer);
        diagram.dropOnDiagram(diagramLabel, dataTransfer);
        diagram.getNodes(diagramLabel, 'part').should('exist').should('have.length', 1);
        explorer.dragTreeItem('Documentation', dataTransfer);
        diagram.dropOnDiagram(diagramLabel, dataTransfer);
        diagram.getNodes(diagramLabel, 'add doc here').should('exist').should('have.length', 1);
        cy.getByTestId('Label\\ -\\ «doc»\\a \\a add\\ doc\\ here').type('body of my doc{enter}');
        diagram.getNodes(diagramLabel, 'body of my doc').should('exist');
        details.getTextField('Body').should('have.value', 'body of my doc');
      });
    });
  });
});
