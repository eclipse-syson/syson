/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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

test.describe('diagram - general view', () => {
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
    await playwrightExplorer.uploadDocument('SysMLv2WithGeneralView.sysml');
    await playwrightExplorer.expand('SysMLv2WithGeneralView.sysml');
    await playwrightExplorer.expand('Package1');
    await playwrightExplorer.createRepresentation('view1 [GeneralView]', 'General View', 'view1');

    // Make sure all the elements are visible and at well-defined locations
    await page.getByTestId('arrange-all-menu').click();
    await page.getByTestId('arrange-all-elk-layered').click();
    await page.getByTestId('zoom-out').click();
  });

  test.afterEach(async ({ request }) => {
    await new PlaywrightProject(request).deleteProject(projectId);
  });

  test('WHEN creating an attribute inside a part, THEN the new attribute is automatically selected and can be renamed immediately', async ({
    page,
  }) => {
    const partNode = new PlaywrightNode(page, 'part1', 'List');
    await expect(partNode.nodeLocator).toBeAttached();

    // Create a new attribute
    await partNode.click();
    await partNode.openPalette();
    await page.getByTestId('toolSection-Structure').click();
    await page.getByTestId('tool-New Attribute').click();

    // It should be selected
    const newNode = new PlaywrightNode(page, 'attribute1', 'IconLabel');
    await expect(newNode.nodeLocator).toBeAttached();
    await expect(newNode.nodeLocator).toContainClass('selected');

    // Start typing a new name immediately
    const newName = 'editedAttribute';
    await page.keyboard.type(newName);
    await page.keyboard.press('Enter');
    await expect(newNode.nodeLocator).not.toBeAttached();

    // The newly created node has changed its label and is still selected
    const editedNode = new PlaywrightNode(page, newName, 'IconLabel');
    await expect(editedNode.nodeLocator).toBeAttached();
    await expect(editedNode.nodeLocator).toContainClass('selected');

    // The selection can be seen from the rest of the workbench, e.g. in the Explorer
    const explorer = new PlaywrightExplorer(page);
    const treeItem = await explorer.getTreeItemLabel(newName);
    await expect(treeItem).not.toBeVisible();
    await explorer.revealGlobalSelection();
    await expect(treeItem).toBeVisible();
  });

  test('WHEN creating a port on a part, THEN the new port is automatically selected', async ({ page }) => {
    const partNode = new PlaywrightNode(page, 'part1', 'List');
    await expect(partNode.nodeLocator).toBeAttached();

    // Create a new port
    await partNode.click();
    await partNode.openPalette();
    await page.getByTestId('toolSection-Structure').click();
    await page.getByTestId('tool-New Port').click();

    // It should be selected
    const newNode = new PlaywrightNode(page, 'port1', 'FreeForm');
    await expect(newNode.nodeLocator).toBeAttached();
    await expect(newNode.nodeLocator).toContainClass('selected');

    // Start typing a new name immediately
    const newName = 'editedPort';
    await page.keyboard.type(newName);
    await page.keyboard.press('Enter');
    await expect(newNode.nodeLocator).not.toBeAttached();

    // The newly created node has changed its label and is still selected
    const editedNode = new PlaywrightNode(page, newName, 'FreeForm');
    await expect(editedNode.nodeLocator).toBeAttached();
    await expect(editedNode.nodeLocator).toContainClass('selected');

    // The selection can be seen from the rest of the workbench, e.g. in the Explorer
    const explorer = new PlaywrightExplorer(page);
    const treeItem = await explorer.getTreeItemLabel(newName);
    await expect(treeItem).not.toBeVisible();
    await explorer.revealGlobalSelection();
    await expect(treeItem).toBeVisible();
  });
});
