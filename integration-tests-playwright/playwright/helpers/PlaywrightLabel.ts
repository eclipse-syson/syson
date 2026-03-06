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
import { type Locator, type Page } from '@playwright/test';

export class PlaywrightLabel {
  readonly page: Page;
  readonly labelLocator: Locator;
  readonly labelContentLocator: Locator;

  constructor(page: Page, label: string) {
    this.page = page;
    this.labelLocator = page.locator(`[data-testid="Label - ${label}"]:not(#hidden-node-container *)`).first();
    this.labelContentLocator = page
      .locator(`[data-testid="Label content - ${label}"]:not(#hidden-node-container *)`)
      .first();
  }

  async getFontSize() {
    const elementHandle = await this.labelLocator.elementHandle();
    await this.page.waitForFunction(
      (element) => {
        const computedStyle = window.getComputedStyle(element);
        return computedStyle && computedStyle.fontSize !== undefined;
      },
      elementHandle,
      { timeout: 2000 }
    );
    const style = await this.labelLocator.evaluate((element) => {
      const computedStyle = window.getComputedStyle(element);
      return {
        fontSize: computedStyle.fontSize,
      };
    });
    return style.fontSize;
  }
}
