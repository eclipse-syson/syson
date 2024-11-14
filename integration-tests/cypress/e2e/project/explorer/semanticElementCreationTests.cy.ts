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
          .getTreeItemByLabel('part')
          .parents('ul')
          .first()
          .siblings()
          .contains(sysmlv2.getProjectLabel())
          .should('exist');
        // Check that the created part is a sibling of Package 1
        explorer.getTreeItemByLabel('part').parents('li').first().siblings().contains('Package 1').should('exist');
      });

      it('Then we can create a PartUsage in it and it will be set as a child of the root namespace (when root namespaces are visible)', () => {
        const explorer = new Explorer();
        explorer.getFilter('Hide Root Namespaces').should('exist').click();
        explorer.createRootObject(sysmlv2.getProjectLabel(), 'SysMLv2EditService-PartUsage');
        // Check that the created part is a child of the root Namespace
        explorer
          .getTreeItemByLabel('part')
          .parents('ul')
          .first()
          .siblings()
          .contains(sysmlv2.getRootNamespaceLabel())
          .should('exist');
        // Check that the created part is a sibling of Package 1
        explorer.getTreeItemByLabel('part').parents('li').first().siblings().contains('Package 1').should('exist');
      });
    });
  });
});
