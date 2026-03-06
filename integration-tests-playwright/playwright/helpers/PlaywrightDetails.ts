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
import type { Locator, Page } from '@playwright/test';

export class PlaywrightDetails {
  readonly page: Page;
  readonly detailsLocator: Locator;

  constructor(page: Page) {
    this.page = page;
    this.detailsLocator = page.getByTestId('view-Details').first();
  }

  async addReference(refWidget: string, option: string) {
    await this.detailsLocator.getByTestId(refWidget).locator('button[title="Open"]').click();
    await this.page.getByTestId(`option-${option}`).click();
  }

  async setText(widget: string, text: string) {
    const inputField = this.detailsLocator.getByTestId(`input-${widget}`);
    await inputField.fill(text);
    await inputField.press('Enter');
  }

  async selectTab(label: string) {
    await this.page.getByTestId(`page-tab-${label}`).click();
  }

  async isTabSelected(label: string): Promise<boolean> {
    return (await this.page.getByTestId(`page-tab-${label}`).getAttribute('aria-selected')) === 'true';
  }

  async isReferenceValueSet(refWidget: string, refValue: string): Promise<boolean> {
    await this.page.waitForFunction(
      ({ refWidget }) => {
        return !!document.querySelector(`[data-testid="${refWidget}"]`);
      },
      { refWidget },
      { timeout: 2000 }
    );
    const element = this.detailsLocator.getByTestId(refWidget).getByTestId(`reference-value-${refValue}`);
    return (await element.count()) > 0 && (await element.isVisible());
  }
}
