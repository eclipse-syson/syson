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
import type { Page, Locator } from '@playwright/test';

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
}
