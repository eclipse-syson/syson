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
        explorer.getExplorerView().contains(sysmlv2.getRootNamespaceLabel());
        explorer.expand(sysmlv2.getRootNamespaceLabel());
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('When we select a Package in the explorer', () => {
      it('Then we can create a General View diagram', () => {
        explorer.createRepresentation(sysmlv2.getRootElementLabel(), 'General View', 'generalView');
        diagram.getDiagram('generalView').should('exist');
      });

      it('Then we can create an Action Flow View diagram', () => {
        explorer.createRepresentation(sysmlv2.getRootElementLabel(), 'Action Flow View', 'actionFlowView');
        diagram.getDiagram('actionFlowView').should('exist');
      });

      it('Then we can create a State Transition View diagram', () => {
        explorer.createRepresentation(sysmlv2.getRootElementLabel(), 'State Transition View', 'stateTransitionView');
        diagram.getDiagram('stateTransitionView').should('exist');
      });
    });

    context('When we select a PartUsage in the explorer', () => {
      beforeEach(() => explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-PartUsage'));

      it('Then we can create a General View diagram', () => {
        explorer.createRepresentation('part', 'General View', 'generalView');
        diagram.getDiagram('generalView').should('exist');
      });

      it('Then we can create an Interconnection View diagram', () => {
        explorer.createRepresentation('part', 'Interconnection View', 'interconnectionView');
        diagram.getDiagram('interconnectionView').should('exist');
      });
    });
  });
});
