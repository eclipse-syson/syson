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
import { PlaywrightDetails } from '../helpers/PlaywrightDetails';
import { PlaywrightExplorer } from '../helpers/PlaywrightExplorer';
import { PlaywrightNode } from '../helpers/PlaywrightNode';
import { PlaywrightProject } from '../helpers/PlaywrightProject';

test.describe('Redefine a nested feature', () => {
  let projectId: string;

  test.beforeEach(async ({ page, request }) => {
    const project = await new PlaywrightProject(request).createSysMLV2Project('redefine nested feature');
    projectId = project.projectId;
    await page.goto(`/projects/${projectId}/edit`);
    const explorer = new PlaywrightExplorer(page);
    await explorer.uploadDocument('RedefineNestedFeature.sysml');
    await explorer.expand('RedefineNestedFeature.sysml');
    await explorer.expand('Redefinition');
    await explorer.createRepresentation('view1 [GeneralView]', 'General View', 'view1');
    await page.getByTestId('arrange-all-main-button').click();
  });

  test.afterEach(async ({ request }) => {
    await new PlaywrightProject(request).deleteProject(projectId);
  });

  for (const { chain, activation } of [
    { chain: 'wheel', activation: 'pointer' },
    { chain: 'wheel.diameter', activation: 'pointer' },
    { chain: 'wheel.diameter', activation: 'Enter' },
  ]) {
    test(`reveals and selects ${chain} in Explorer and Details after ${activation} activation and confirmation`, async ({
      page,
    }) => {
      const vehicle = new PlaywrightNode(page, 'vehicle : Vehicle', 'List');
      await vehicle.click();
      await vehicle.openPalette();
      if (activation === 'Enter') {
        const search = page.getByTestId('Palette').getByRole('textbox');
        await search.fill('Redefine a nested feature');
        await expect(page.getByTestId('tool-Redefine a nested feature')).toBeVisible();
        await search.press('Enter');
      } else {
        await page.getByTestId('toolSection-Structure').click();
        await page.getByTestId('tool-Redefine a nested feature').click();
      }

      const dialog = page.getByRole('dialog');
      await expect(dialog.getByTestId('confirm-action')).toBeDisabled();
      await dialog.locator(`[data-treeitemlabel="${chain}"]`).click();
      const invocation = page.waitForResponse((response) => {
        return (
          response.url().endsWith('/api/graphql') &&
          response.request().postDataJSON()?.operationName === 'invokeSingleClickOnDiagramElementTool'
        );
      });
      await dialog.getByTestId('confirm-action').click();
      await expect(dialog).toBeHidden();
      const result = await (await invocation).json();
      expect(result.errors).toBeUndefined();
      const payload = result.data.invokeSingleClickOnDiagramElementTool;
      expect(payload.__typename).toBe('InvokeSingleClickOnDiagramElementToolSuccessPayload');
      expect(payload.messages).toEqual([]);
      expect(payload.newSelection.entries).toHaveLength(1);
      const selectedId = payload.newSelection.entries[0].id;

      // Do not select or reveal anything manually: the tool must update both views itself.
      const explorer = new PlaywrightExplorer(page);
      const selected = explorer.explorerLocator.locator(`[data-treeitemid="${selectedId}"]`);
      await expect(selected).toBeVisible();
      await expect(selected).toHaveAttribute('data-testid', 'selected');
      const details = new PlaywrightDetails(page);
      await expect(details.detailsLocator).toContainText(`Redefinition::vehicle::${chain.replace('.', '::')}`);

      if (chain === 'wheel.diameter') {
        await expect(page.locator('.react-flow__node [data-testid*="diameter"]')).toHaveCount(0);
      }
    });
  }
});
