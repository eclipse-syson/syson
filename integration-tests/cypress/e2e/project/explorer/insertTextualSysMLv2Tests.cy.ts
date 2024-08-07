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
import { Details } from '../../../workbench/Details';
import { Explorer } from '../../../workbench/Explorer';

describe('Insert Textual SysMLv2 Menu Tests', () => {
  const sysmlv2 = new SysMLv2();

  context('Given an empty SysMLv2 project', () => {
    const explorer = new Explorer();
    const details = new Details();
    let projectId: string = '';
    beforeEach(() =>
      sysmlv2.createSysMLv2Project().then((createdProjectData) => {
        projectId = createdProjectData.projectId;
        new Project().visit(projectId);
        explorer.getExplorerView().contains(sysmlv2.getProjectLabel());
        explorer.expand(sysmlv2.getProjectLabel());
        explorer.getExplorerView().contains(sysmlv2.getRootNamespaceLabel());
        explorer.expand(sysmlv2.getRootNamespaceLabel());
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('When we selected a Package in the explorer', () => {
      it("Then we can perform the 'New objects from text' menu entry", () => {
        explorer.insertTextualSysMLv2(sysmlv2.getRootElementLabel(), 'attribute myAttribute');
        cy.getByTestId('insert-textual-sysmlv2-modal').should('not.exist');
        explorer.getExplorerView().contains('myAttribute');
      });

      it("Then we can perform the 'New objects from text' menu entry and it will references elements from standard libraries (with appropriate imports)", () => {
        explorer.insertTextualSysMLv2(
          sysmlv2.getRootElementLabel(),
          'import ScalarValues::*; attribute myAttribute : String;'
        );
        cy.getByTestId('insert-textual-sysmlv2-modal').should('not.exist');
        explorer.getExplorerView().contains('myAttribute');
        explorer.expand('myAttribute');
        explorer.getExplorerView().contains('FeatureTyping');
        explorer.select('myAttribute');
        details.getGroup('Attribute Properties').should('be.visible');
        details.getReferenceWidget('Typed by').should('exist');
        details.getReferenceWidgetSelectedValue('Typed by', 'String').should('exist');
      });

      it("Then we can perform the 'New objects from text' menu entry with not valid textual sysml", () => {
        explorer.insertTextualSysMLv2(sysmlv2.getRootElementLabel(), '£$$`:=;');
        cy.getByTestId('insert-textual-sysmlv2-modal').should('exist');
      });
    });

    context('When we selected a Document in the explorer', () => {
      it("Then we can't perform the 'New objects from text' menu entry", () => {
        explorer.getTreeItemByLabel(sysmlv2.getProjectLabel()).find('button').click();
        cy.getByTestId('insert-textual-sysmlv2-menu').should('not.exist');
      });
    });
  });
});
