/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import { Project } from '../../../pages/Project';
import { Batmobile } from '../../../usecases/Batmobile';
import { Details } from '../../../workbench/Details';
import { Explorer } from '../../../workbench/Explorer';

describe('Details View Tests', () => {
  const batmobile = new Batmobile();
  context('Given a Batmobile Project', () => {
    const explorer = new Explorer();
    const details = new Details();
    let projectId: string = '';

    beforeEach(() =>
      batmobile.createBatmobileProject().then((createdProjectData) => {
        projectId = createdProjectData.projectId;
        new Project().visit(projectId);
        explorer.getExplorerView().contains(batmobile.getProjectLabel());
        explorer.expand(batmobile.getProjectLabel());
        explorer.getExplorerView().contains(batmobile.getRootElementLabel());
        explorer.expand(batmobile.getRootElementLabel());
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('When we select the Vehicle PartDefinition', () => {
      beforeEach(() => explorer.select('Vehicle'));

      it('Then the details view contains the properties of the Vehicle', () => {
        details.getGroup('Part Definition Properties').should('be.visible');
        details.getTextField('Declared Name').should('have.value', 'Vehicle');
      });
    });

    context('When we select a PartUsage without type', () => {
      it(
        'Then the details view contains the extra property "Typed by" even if the PartUsage has no type yet.',
        { retries: 3 },
        () => {
          explorer.select(batmobile.getRootElementLabel());
          explorer.createObject(batmobile.getRootElementLabel(), 'SysMLv2EditService-PartUsage');
          explorer.select('part4');
          details.getGroup('Part Properties').should('be.visible');
          details.getReferenceWidget('Typed by').should('exist');
          details.getReferenceWidgetSelectedValue('Typed by', '').should('not.exist');
        }
      );
    });

    context('When we select an Element', () => {
      beforeEach(() => explorer.select('Vehicle'));

      it(
        'Then the details view contains the extra property "Documentation" even if the Element has no documentation yet.',
        { retries: 3 },
        () => {
          details.getGroup('Part Definition Properties').should('be.visible');
          details.getTextField('Declared Name').should('have.value', 'Vehicle');
          details.getTextField('Documentation').should('exist');
          details.getTextField('Documentation').should('have.value', '');
        }
      );
    });
    context('When we open the references of a widget reference', () => {
      beforeEach(() => explorer.select('Batman'));

      it('Then all references are accessible.', { retries: 3 }, () => {
        details.getGroup('Item Definition Properties').should('be.visible');
        details.getReferenceWidget('Specializes').should('exist');
        details.openReferenceWidgetOptions('Specializes');
        details.selectReferenceWidgetOption('Power');
        details.getReferenceWidgetSelectedValue('Specializes', 'Power').should('exist');
      });
    });
    context('When we select an Expression', () => {
      beforeEach(() => {
        explorer.select('VehicleMaxSpeed');
        explorer.expandAll('VehicleMaxSpeed');
        explorer.select('vehicle.actualSpeed <= maxSpeed');
      });
      it("Then the Details view shows the expression's textual value", () => {
        cy.getByTestId('details-expression-value').should('exist');
        cy.getByTestId('details-expression-value').should('have.text', 'vehicle.actualSpeed <= maxSpeed');
        cy.getByTestId('Edit-expression-button').should('exist').should('be.enabled');
      });
    });

    context('When we select a ConstraintUsage which contains an Expression', () => {
      beforeEach(() => {
        explorer.select('Wheel');
        explorer.expand('Wheel');
        explorer.select('boundingBox');
        explorer.expand('boundingBox');
        explorer.select('length');
      });
      it("Then the Details view shows the child expression's textual value", () => {
        cy.getByTestId('details-expression-value').should('exist');
        cy.getByTestId('details-expression-value').should('have.text', '80 [SI::centimetre]');
        cy.getByTestId('Edit-expression-button').should('exist').should('be.enabled');
      });
    });

    context('When we select a TransitionUsage with a guard Expression', () => {
      beforeEach(() => {
        explorer.select('Drive Batmobile');
        explorer.expand('Drive Batmobile');
        // The TransitionUsage is anonymous so select the closest named item and move down using the keyboard
        explorer.select('scanEnvironment');
        cy.getByTestId('scanEnvironment').type('{downArrow}{downArrow}{downArrow}');
      });
      it("Then the Details view shows the child expression's textual value", () => {
        cy.getByTestId('details-expression-value').should('exist');
        cy.getByTestId('details-expression-value').should('have.text', 'scanEnvironment.status == StatusKind::safe');
        cy.getByTestId('Edit-expression-button').should('exist').should('be.enabled');
      });
    });

    context('When we select an attribute with no Expression', () => {
      beforeEach(() => {
        explorer.select('Vehicle');
        explorer.expand('Vehicle');
        explorer.select('actualSpeed');
      });
      it('Then the Details view shows the expression widget but indicate the element has none', () => {
        cy.getByTestId('details-expression-value').should('exist');
        cy.getByTestId('details-expression-value').should('have.text', '<none>');
        cy.getByTestId('Create-expression-button').should('exist').should('be.enabled');
      });
    });

    context('When we select a succession with no guard Expression', () => {
      beforeEach(() => {
        explorer.select('Drive Batmobile');
        explorer.expand('Drive Batmobile');
        // Select the first child, a TransitionUsage without a name/label
        cy.getByTestId('Drive Batmobile').type('{downArrow}');
      });
      it('Then the Details view shows the expression widget but indicate the element has none', () => {
        cy.getByTestId('details-expression-value').should('exist');
        cy.getByTestId('details-expression-value').should('have.text', '<none>');
        cy.getByTestId('Create-expression-button').should('exist').should('be.enabled');
      });
    });
  });
});
