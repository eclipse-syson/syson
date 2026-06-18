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

    await page.getByTestId('arrange-all-main-button').click();

    const partNode = new PlaywrightNode(page, 'part1', 'List');
    await expect(partNode.nodeLocator).toBeAttached();

    // Keep the diagram content visible without depending on Sirius Web's arrange-all toolbar controls.
    await page.getByTestId('zoom-out').click();
  });

  test.afterEach(async ({ request }) => {
    await new PlaywrightProject(request).deleteProject(projectId);
  });

  test('WHEN applying the rotate tool on a fork node, THEN the node is rotated', async ({ page, browserName }) => {
    if (browserName === 'firefox') {
      test.skip(); //The test fails inexplicably in Firefox.
    }

    // Create a new Action node
    await page.getByTestId('rf__wrapper').click({ button: 'right', position: { x: 250, y: 250 } });
    await expect(page.getByTestId('Palette')).toBeAttached();
    await page.getByTestId('toolSection-Behavior').click();
    await page.getByTestId('tool-New Action').click();
    const playwrightActionNode = new PlaywrightNode(page, 'action1', 'List');
    await expect(playwrightActionNode.nodeLocator).toBeAttached();

    // Create a new Fork node in the Action node
    await playwrightActionNode.openPalette();
    await page.getByTestId('toolSection-Behavior').click();
    await page.getByTestId('tool-New Fork').click();
    const playwrightForkNode = new PlaywrightNode(page, 'forkNode1');
    await expect(playwrightForkNode.nodeLocator).toBeAttached();
    const {width, height} = await playwrightForkNode.getReactFlowSize();
    const expectedRotatedWidth = height;
    const expectedRotatedHeight = width;

    // Apply the rotate tool on the Fork node
    await playwrightForkNode.openPalette();
    await page.getByTestId('toolSection-Edit').click();
    await page.getByTestId('overridden_tool_node-rotate').click();
    await playwrightForkNode.closePalette();
    const rotatedSize = await playwrightForkNode.getReactFlowSize();

    expect(rotatedSize.width).toBeCloseTo(expectedRotatedWidth, 0);
    expect(rotatedSize.height).toBeCloseTo(expectedRotatedHeight, 0);
  });

});