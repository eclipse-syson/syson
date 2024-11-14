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
      it('Then the details view contains the extra property "Typed by" even if the PartUsage has no type yet.', () => {
        explorer.createObject(batmobile.getRootElementLabel(), 'SysMLv2EditService-PartUsage');
        explorer.select('part');
        details.getGroup('Part Properties').should('be.visible');
        details.getReferenceWidget('Typed by').should('exist');
        details.getReferenceWidgetSelectedValue('Typed by', '').should('not.exist');
      });
    });

    context('When we select an Element', () => {
      beforeEach(() => explorer.select('Vehicle'));

      it('Then the details view contains the extra property "Documentation" even if the Element has no documentation yet.', () => {
        details.getGroup('Part Definition Properties').should('be.visible');
        details.getTextField('Declared Name').should('have.value', 'Vehicle');
        details.getTextField('Documentation').should('exist');
        details.getTextField('Documentation').should('have.value', '');
      });
    });
  });
});
