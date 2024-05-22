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
    const explorer = new Explorer();
    let projectId: string = '';
    beforeEach(() =>
      sysmlv2.createSysMLv2Project().then((createdProjectData) => {
        projectId = createdProjectData.projectId;
        new Project().visit(projectId);
        explorer.getExplorerView().contains(sysmlv2.getProjectLabel());
        explorer.expand(sysmlv2.getProjectLabel());
      })
    );

    afterEach(() => cy.deleteProject(projectId));

    context('When we selected a Package in the explorer', () => {
      it('Then we can create a PartUsage in it', () => {
        explorer.createObject(sysmlv2.getRootElementLabel(), 'SysMLv2EditService-PartUsage');
      });
    });
  });
});
