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
import { type Locator, type Page, expect } from '@playwright/test';

export class PlaywrightEdge {
  readonly page: Page;
  readonly edgeLocator: Locator;

  constructor(page: Page, index = 0) {
    this.page = page;
    this.edgeLocator = page.locator('[data-testid^="rf__edge-"]').nth(index);
  }

  async click() {
    await this.edgeLocator.click();
  }

  async controlClick() {
    await this.edgeLocator.click({ modifiers: ['ControlOrMeta'] });
  }

  async isSelected() {
    await expect(this.edgeLocator).toHaveClass(/selected/);
  }

  async getEdgePath() {
    return await this.edgeLocator.locator('path').first().getAttribute('d');
  }

  async getEdgeStyle() {
    return await this.edgeLocator.locator('.react-flow__edge-path').first();
  }

  async openPalette() {
    await this.edgeLocator.click({ button: 'right' });
  }

  async closePalette() {
    await this.page.getByTestId('Close-palette').click();
  }

  async getEdgeColor() {
    const path = this.edgeLocator.locator('path').first();
    return await path.evaluate((el) => {
      return window.getComputedStyle(el).getPropertyValue('stroke');
    });
  }
}
