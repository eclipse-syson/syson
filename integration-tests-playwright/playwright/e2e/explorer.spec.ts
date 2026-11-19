/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
import { PlaywrightProject } from '../helpers/PlaywrightProject';

test.describe('explorer', () => {
  let projectId;
  test.beforeEach(async ({ page, request }) => {
    await page.addInitScript(() => {
      // @ts-expect-error: we use a variable in the DOM to disable `fitView` functionality for Cypress tests.
      window.document.DEACTIVATE_FIT_VIEW_FOR_CYPRESS_TESTS = true;
    });
    const project = await new PlaywrightProject(request).createSysMLV2Project('general');
    projectId = project.projectId;
    await page.goto(`/projects/${projectId}/edit`);

    const playwrightExplorer = new PlaywrightExplorer(page);
    await playwrightExplorer.expand('SysMLv2.sysml');
    await playwrightExplorer.expand('Package1');
  });

  test.afterEach(async ({ request }) => {
    await new PlaywrightProject(request).deleteProject(projectId);
  });

  test("WHEN we select a Package in the explorer THEN we can perform the 'New objects from text' menu entry with a state definition (which starts with an 's')", async ({
    page,
  }) => {
    const playwrightExplorer = new PlaywrightExplorer(page);
    await playwrightExplorer.insertTextualSysMLv2('Package1', 'state def InitialState');

    await expect(await playwrightExplorer.explorerLocator.locator(`[data-treeitemlabel="InitialState"]`)).toBeVisible();
  });

  test('Collapse all resets descendants and preserves unrelated expanded branches', async ({ page }) => {
    const explorer = new PlaywrightExplorer(page);
    await explorer.insertTextualSysMLv2(
      'Package1',
      'package CollapseBranch { package NestedBranch { part CollapseLeaf; } } package OtherBranch { part OtherLeaf; }'
    );
    await explorer.expandAll('Package1');
    await expect(explorer.explorerLocator.getByTestId('CollapseLeaf')).toBeVisible();
    await expect(explorer.explorerLocator.getByTestId('OtherLeaf')).toBeVisible();

    await explorer.collapseAll('CollapseBranch');
    await expect(explorer.explorerLocator.getByTestId('NestedBranch')).toBeHidden();
    await expect(explorer.explorerLocator.getByTestId('OtherLeaf')).toBeVisible();

    await explorer.expand('CollapseBranch');
    await expect(explorer.explorerLocator.getByTestId('NestedBranch')).toBeVisible();
    await expect(explorer.explorerLocator.getByTestId('CollapseLeaf')).toBeHidden();

    await explorer.explorerLocator.getByTestId('OtherLeaf-more').click();
    await expect(page.getByTestId('Palette').getByTestId('collapse-all')).toHaveCount(0);
  });

  test('Collapse all is available on library directories without enabling Expand all', async ({ page }) => {
    const explorer = new PlaywrightExplorer(page);
    await explorer.expand('Libraries');
    const kerml = explorer.explorerLocator.locator('[data-treeitemkind="KerMLStandardLibraryDirectory"]');
    await expect(kerml).toBeVisible();
    await kerml.dblclick();
    await expect(kerml).toHaveAttribute('data-expanded', 'true');
    await explorer.explorerLocator.getByTestId('Libraries-more').click();
    await expect(page.getByTestId('Palette').getByTestId('expand-all')).toHaveCount(0);
    await page.getByTestId('Palette').getByTestId('collapse-all').click();
    await expect(kerml).toBeHidden();
    await expect(explorer.explorerLocator.getByTestId('Package1')).toBeVisible();
    await explorer.expand('Libraries');
    await expect(kerml).toBeVisible();
    await expect(kerml).toHaveAttribute('data-expanded', 'false');
  });
});
