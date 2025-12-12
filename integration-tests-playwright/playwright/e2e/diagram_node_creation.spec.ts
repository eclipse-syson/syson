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
import { PlaywrightDiagram } from '../helpers/PlaywrightDiagram';
import { PlaywrightNode } from '../helpers/PlaywrightNode';
import { PlaywrightProject } from '../helpers/PlaywrightProject';

test.describe('diagram - general view', () => {
  let projectId;
  test.beforeEach(async ({ page, request }) => {
    await page.addInitScript(() => {
      // @ts-expect-error: we use a variable in the DOM to disable `fitView` functionality for Cypress tests.
      window.document.DEACTIVATE_FIT_VIEW_FOR_CYPRESS_TESTS = true;
    });
    const project = await new PlaywrightProject(request).createSysMLV2Project('general view');
    projectId = project.projectId;
    await page.goto(`/projects/${projectId}/edit`);
    await page.locator(`[data-testid="onboard-open-view1"]`).first().click();
  });

  test.afterEach(async ({ request }) => {
    await new PlaywrightProject(request).deleteProject(projectId);
  });

  test('when creating a port on a part definition, then the port top node is hidden', async ({ page }) => {
    const diagram = new PlaywrightDiagram(page);
    // Create PartDef
    await page.getByTestId('rf__wrapper').click({ button: 'right' });
    await expect(page.getByTestId('Palette')).toBeAttached();
    await page.getByTestId('toolSection-Structure').click();
    await page.getByTestId('tool-New Part Definition').click();
    diagram.expectNumberOfTopNodes(1);
    // Create Port
    const partDefinitionNode = new PlaywrightNode(page, 'PartDefinition1', 'List');
    await expect(partDefinitionNode.nodeLocator).toBeAttached();
    await partDefinitionNode.openPalette();
    await page.getByTestId('toolSection-Structure').click();
    await page.getByTestId('tool-New Port Inout').click();
    // The port is only displayed as a border node of the parent
    const portBorderNode = new PlaywrightNode(page, 'port1Inout');
    await expect(portBorderNode.nodeLocator).toBeAttached();
    diagram.expectNumberOfTopNodes(2);
    // The port can be displayed as a top node
    await diagram.revealElement('port1Inout');
    const portNode = new PlaywrightNode(page, 'port1Inout', 'List');
    await expect(portNode.nodeLocator).toBeAttached();
    diagram.expectNumberOfTopNodes(3);
    // The port can be displayed as a list item
    await partDefinitionNode.click();
    await partDefinitionNode.revealElement('ports');
    const portsListNode = new PlaywrightNode(page, 'ports', 'List');
    await expect(portsListNode.nodeLocator).toBeAttached();
    const portListItemNode = new PlaywrightNode(page, 'inout port1Inout', 'IconLabel');
    await expect(portListItemNode.nodeLocator).toBeAttached();
    await diagram.expectNumberOfTopNodes(4);
  });

  test('when creating a port on a port compartment of a part definition, then the port top node is hidden', async ({
    page,
  }) => {
    const diagram = new PlaywrightDiagram(page);
    // Create PartDef
    await page.getByTestId('rf__wrapper').click({ button: 'right' });
    await expect(page.getByTestId('Palette')).toBeAttached();
    await page.getByTestId('toolSection-Structure').click();
    await page.getByTestId('tool-New Part Definition').click();
    diagram.expectNumberOfTopNodes(1);
    const partDefinitionNode = new PlaywrightNode(page, 'PartDefinition1', 'List');
    await expect(partDefinitionNode.nodeLocator).toBeAttached();
    // Show the port compartment
    await partDefinitionNode.revealElement('ports');
    const portsListNode = new PlaywrightNode(page, 'ports', 'List');
    await expect(portsListNode.nodeLocator).toBeAttached();
    await portsListNode.openPalette();
    // Create Port
    await diagram.expectNumberOfTopNodes(2);
    await page.getByTestId('toolSection-Structure').click();
    await page.getByTestId('tool-New Port Inout').click();
    // The port is  displayed as an item node
    const portListItemNode = new PlaywrightNode(page, 'inout port1Inout', 'IconLabel');
    await expect(portListItemNode.nodeLocator).toBeAttached();
    // The port is  displayed as a border node of the parent
    const portBorderNode = new PlaywrightNode(page, 'port1Inout');
    await expect(portBorderNode.nodeLocator).toBeAttached();
    diagram.expectNumberOfTopNodes(4);
  });
});
