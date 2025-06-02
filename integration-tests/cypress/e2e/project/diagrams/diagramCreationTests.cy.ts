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
import { Diagram } from '../../../workbench/Diagram';
import { Explorer } from '../../../workbench/Explorer';

describe('Diagram Creation Tests', () => {
  const sysmlv2 = new SysMLv2();

  context('Given an empty SysMLv2 project', () => {
    const explorer = new Explorer();
    const diagram = new Diagram();
    let projectId: string = '';
    beforeEach(() =>
      sysmlv2.createSysMLv2Project().then((createdProjectData) => {
        projectId = createdProjectData.projectId;
        new Project().visit(projectId);
        explorer.getExplorerView().contains(sysmlv2.getProjectLabel());
        explorer.expand(sysmlv2.getProjectLabel());
        explorer.getTreeItemByLabel(sysmlv2.getProjectLabel()).should('have.attr', 'data-expanded', 'true');
      })
    );

    afterEach(() => {
      cy.deleteProject(projectId);
    });

    context.skip('When we select a Package in the explorer', () => {
      it('Then we can create a General View diagram', { retries: 3 }, () => {
        explorer.select(sysmlv2.getRootElementLabel());
        explorer.createRepresentation(sysmlv2.getRootElementLabel(), 'General View', 'generalView');
        diagram.getDiagram('generalView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });

      it('Then we can create an Action Flow View diagram', { retries: 3 }, () => {
        explorer.select(sysmlv2.getRootElementLabel());
        explorer.createRepresentation(sysmlv2.getRootElementLabel(), 'Action Flow View', 'actionFlowView');
        diagram.getDiagram('actionFlowView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });

      it('Then we can create a State Transition View diagram', { retries: 3 }, () => {
        explorer.select(sysmlv2.getRootElementLabel());
        explorer.createRepresentation(sysmlv2.getRootElementLabel(), 'State Transition View', 'stateTransitionView');
        diagram.getDiagram('stateTransitionView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });
    });

    context.skip('When we select a PartUsage in the explorer', () => {
      beforeEach(() => {
        explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-PartUsage');
      });

      it('Then we can create a General View diagram', { retries: 3 }, () => {
        explorer.select('part1');
        explorer.createRepresentation('part1', 'General View', 'generalView');
        diagram.getDiagram('generalView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });

      it('Then we can create an Interconnection View diagram', { retries: 3 }, () => {
        explorer.select('part1');
        explorer.createRepresentation('part1', 'Interconnection View', 'interconnectionView');
        diagram.getDiagram('interconnectionView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });
    });

    context.skip('When we select an ActionUsage in the explorer', () => {
      beforeEach(() => {
        explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-ActionUsage');
      });

      it('Then we can create a General View diagram', { retries: 3 }, () => {
        explorer.select('action1');
        explorer.createRepresentation('action1', 'General View', 'generalView');
        diagram.getDiagram('generalView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });

      it('Then we can create an Interconnection View diagram', { retries: 3 }, () => {
        explorer.select('action1');
        explorer.createRepresentation('action1', 'Interconnection View', 'interconnectionView');
        diagram.getDiagram('interconnectionView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });

      it('Then we can create an Action Flow View diagram', { retries: 3 }, () => {
        explorer.select('action1');
        explorer.createRepresentation('action1', 'Action Flow View', 'actionFlowView');
        diagram.getDiagram('actionFlowView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });
    });

    context.skip('When we select an ActionDefinition in the explorer', () => {
      beforeEach(() => {
        explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-ActionDefinition');
      });

      it('Then we can create a General View diagram', { retries: 3 }, () => {
        explorer.select('ActionDefinition1');
        explorer.createRepresentation('ActionDefinition1', 'General View', 'generalView');
        diagram.getDiagram('generalView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });

      it('Then we can create an Interconnection View diagram', { retries: 3 }, () => {
        explorer.select('ActionDefinition1');
        explorer.createRepresentation('ActionDefinition1', 'Interconnection View', 'interconnectionView');
        diagram.getDiagram('interconnectionView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });

      it('Then we can create an Action Flow View diagram', { retries: 3 }, () => {
        explorer.select('ActionDefinition1');
        explorer.createRepresentation('ActionDefinition1', 'Action Flow View', 'actionFlowView');
        diagram.getDiagram('actionFlowView').should('exist');
        cy.get('@consoleDebug').should('be.calledWith', 'mutation layoutDiagram: response received');
      });
    });

    context('When we select the Libraries directory in the explorer', () => {
      it('Then we cannot create a new representation in it', { retries: 3 }, () => {
        const explorer = new Explorer();
        explorer.getTreeItemByLabel(sysmlv2.getLibrariesLabel()).first().find('button').click();
        cy.getByTestId('new-representation').should('not.exist');
      });
    });

    context('When we select the KerML directory in the explorer', () => {
      it('Then we cannot create a new representation in it', { retries: 3 }, () => {
        const explorer = new Explorer();
        explorer.expand(sysmlv2.getLibrariesLabel());
        explorer.getTreeItemByLabel(sysmlv2.getKerMLLabel()).first().find('button').click();
        cy.getByTestId('new-representation').should('not.exist');
      });
    });

    context('When we select the SysML directory in the explorer', () => {
      it('Then we cannot create a new representation in it', { retries: 3 }, () => {
        const explorer = new Explorer();
        explorer.expand(sysmlv2.getLibrariesLabel());
        explorer.getTreeItemByLabel(sysmlv2.getSysMLLabel()).first().find('button').click();
        cy.getByTestId('new-representation').should('not.exist');
      });
    });

    context('When we select the Base KerML model in the explorer', () => {
      it('Then we cannot create a new representation in it', { retries: 3 }, () => {
        const explorer = new Explorer();
        explorer.expand(sysmlv2.getLibrariesLabel());
        explorer.expand(sysmlv2.getKerMLLabel());
        explorer.getTreeItemByLabel('Base').first().find('button').click();
        cy.getByTestId('new-representation').should('not.exist');
      });
    });

    context('When we select the Base KerML model top-level element in the explorer', () => {
      it('Then we cannot create a new representation in it', { retries: 3 }, () => {
        const explorer = new Explorer();
        explorer.expand(sysmlv2.getLibrariesLabel());
        explorer.expand(sysmlv2.getKerMLLabel());
        explorer.expand('Base');
        explorer
          .getTreeItemByLabelAndKind('Base', 'siriusComponents://semantic?domain=sysml&entity=LibraryPackage')
          .first()
          .find('button')
          .click();
        cy.getByTestId('new-representation').should('not.exist');
      });
    });

    context('When we select a root namespace element', () => {
      it('Then we can create a new representation in it', { retries: 3 }, () => {
        const explorer = new Explorer();
        cy.getByTestId('tree-filter-menu-icon').should('exist').click();
        cy.getByTestId('tree-filter-menu-checkbox-Hide Root Namespaces').click();
        explorer.getTreeItemByLabel('Namespace').should('exist');
        explorer.createRepresentation('Namespace', 'General View', 'generalView');
        diagram.getDiagram('generalView').should('exist');
      });
    });
  });
});
