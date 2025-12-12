/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
import { expect, test } from '@playwright/test';
import { PlaywrightExplorer } from '../helpers/PlaywrightExplorer';
import { PlaywrightNode } from '../helpers/PlaywrightNode';
import { PlaywrightProject } from '../helpers/PlaywrightProject';

test.describe('diagram - interconnection view', () => {
  let projectId;
  test.beforeEach(async ({ page, request }) => {
    await page.addInitScript(() => {
      // @ts-expect-error: we use a variable in the DOM to disable `fitView` functionality for Cypress tests.
      window.document.DEACTIVATE_FIT_VIEW_FOR_CYPRESS_TESTS = true;
    });
    const project = await new PlaywrightProject(request).createSysMLV2Project('interconnection');
    projectId = project.projectId;
    await page.goto(`/projects/${projectId}/edit`);

    const playwrightExplorer = new PlaywrightExplorer(page);
    await playwrightExplorer.uploadDocument('SysMLv2WithInterconnectionView.sysml');
    await playwrightExplorer.expand('SysMLv2WithInterconnectionView.sysml');
    await playwrightExplorer.expand('Package1');
    await playwrightExplorer.createRepresentation('view1 [InterconnectionView]', 'General View', 'view1');
  });

  test.afterEach(async ({ request }) => {
    await new PlaywrightProject(request).deleteProject(projectId);
  });

  test('when resizing a part, then interconnection compartment is resized', async ({ page }) => {
    const partNode = new PlaywrightNode(page, 'part1', 'List');
    const interconnectionCompartment = new PlaywrightNode(page, 'part1', 'FreeForm');
    await expect(partNode.nodeLocator).toBeAttached();
    await expect(interconnectionCompartment.nodeLocator).toBeAttached();

    await page.getByTestId('zoom-out').click();

    const compartmentSizeBefore = await interconnectionCompartment.getDOMBoundingBox();

    await partNode.click();
    await partNode.resize({ height: 20, width: 20 });

    const compartmentSizeAfter = await interconnectionCompartment.getDOMBoundingBox();
    expect(compartmentSizeAfter.height).toBeLessThan(compartmentSizeBefore.height);
  });
});
