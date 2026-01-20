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

test.describe('explorer - Insert Textual SysMLv2 Menu', () => {
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
});
