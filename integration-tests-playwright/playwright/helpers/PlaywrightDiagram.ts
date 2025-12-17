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
import { expect, type Page } from '@playwright/test';

export class PlaywrightDiagram {
  readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  async expectNumberOfTopNodes(expectedNumberOfTopNodes: number) {
    expect(await this.page.locator('.react-flow__nodes > div').count()).toBe(expectedNumberOfTopNodes);
  }

  async revealElement(name: string) {
    await this.page.getByTestId('filter-elements').click();
    const popup = this.page.getByTestId('group-Filter elements');
    await expect(popup).toBeAttached();
    await popup.locator(`[data-testid$="${name}"]`).click();
    //The drop down lack a proper data-testid so we use its sibling
    await expect(popup.locator(`[data-testid="Apply to 1 selected element:"]`)).toBeAttached();
    await popup
      .locator(`[data-testid="Apply to 1 selected element:"]`)
      .locator('..')
      .locator('> button')
      .last()
      .click();
    await this.page.getByAltText('Show').click();
    await popup.locator(`[data-testid="Apply to 1 selected element:"]`).click();
    await this.page.keyboard.press('Escape');
  }
}
