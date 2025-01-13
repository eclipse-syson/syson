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

import { Project } from '../../../pages/Project';
import { SysMLv2 } from '../../../usecases/SysMLv2';
import { Explorer } from '../../../workbench/Explorer';

describe('Semantic Element Creation Tests', () => {
  const sysmlv2 = new SysMLv2();

  context('Given an empty SysMLv2 project', () => {
    let projectId: string = '';
    beforeEach(() =>
      sysmlv2.createSysMLv2Project().then((createdProjectData) => {
        projectId = createdProjectData.projectId;
        new Project().visit(projectId);
        const explorer = new Explorer();
        explorer.getExplorerView().contains(sysmlv2.getProjectLabel());
        explorer.expand(sysmlv2.getProjectLabel());
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('When we select a Package in the explorer', () => {
      it('Then we can create a PartUsage in it', () => {
        const explorer = new Explorer();
        explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-PartUsage');
      });
    });

    context('When we select the root document in the explorer', () => {
      it('Then we can create a PartUsage in it and it will be set as a child of the root document', () => {
        const explorer = new Explorer();
        explorer.createRootObject(sysmlv2.getProjectLabel(), 'SysMLv2EditService-PartUsage');
        // Check that the created part is a child of the root document
        explorer
          .getTreeItemByLabel('part1')
          .parents('ul')
          .first()
          .siblings()
          .contains(sysmlv2.getProjectLabel())
          .should('exist');
        // Check that the created part is a sibling of Package 1
        explorer.getTreeItemByLabel('part1').parents('li').first().siblings().contains('Package 1').should('exist');
      });

      it('Then we can create a PartUsage in it and it will be set as a child of the root namespace (when root namespaces are visible)', () => {
        const explorer = new Explorer();
        explorer.getFilter('Hide Root Namespaces').should('exist').click();
        explorer.createRootObject(sysmlv2.getProjectLabel(), 'SysMLv2EditService-PartUsage');
        // Check that the created part is a child of the root Namespace
        explorer
          .getTreeItemByLabel('part1')
          .parents('ul')
          .first()
          .siblings()
          .contains(sysmlv2.getRootNamespaceLabel())
          .should('exist');
        // Check that the created part is a sibling of Package 1
        explorer.getTreeItemByLabel('part1').parents('li').first().siblings().contains('Package 1').should('exist');
      });
    });

    context('When we select the Libraries directory in the explorer', () => {
      it('Then we cannot create a new object in it', () => {
        const explorer = new Explorer();
        explorer.getTreeItemByLabel(sysmlv2.getLibrariesLabel()).first().find('button').click();
        cy.getByTestId('new-object').should('not.exist');
      });
    });

    context('When we select the KerML directory in the explorer', () => {
      it('Then we cannot create a new object in it', () => {
        const explorer = new Explorer();
        explorer.expand(sysmlv2.getLibrariesLabel());
        explorer.getTreeItemByLabel(sysmlv2.getKerMLLabel()).first().find('button').click();
        cy.getByTestId('new-object').should('not.exist');
      });
    });

    context('When we select the SysML directory in the explorer', () => {
      it('Then we cannot create a new object in it', () => {
        const explorer = new Explorer();
        explorer.expand(sysmlv2.getLibrariesLabel());
        explorer.getTreeItemByLabel(sysmlv2.getSysMLLabel()).first().find('button').click();
        cy.getByTestId('new-object').should('not.exist');
      });
    });

    context('When we select the Base KerML model in the explorer', () => {
      it('Then we cannot create a new object in it', () => {
        const explorer = new Explorer();
        explorer.expand(sysmlv2.getLibrariesLabel());
        explorer.expand(sysmlv2.getKerMLLabel());
        explorer.getTreeItemByLabel('Base').first().find('button').click();
        cy.getByTestId('new-object').should('not.exist');
      });
    });

    context('When we select the Base KerML model top-level element in the explorer', () => {
      it('Then we cannot create a new object in it', () => {
        const explorer = new Explorer();
        explorer.expand(sysmlv2.getLibrariesLabel());
        explorer.expand(sysmlv2.getKerMLLabel());
        explorer.expand('Base');
        explorer
          .getTreeItemByLabelAndKind('Base', 'siriusComponents://semantic?domain=sysml&entity=LibraryPackage')
          .first()
          .find('button')
          .click();
        cy.getByTestId('new-object').should('not.exist');
      });
    });
  });
});
