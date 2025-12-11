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
import { type Locator, type Page } from '@playwright/test';

export class PlaywrightNodeLabel {
  readonly page: Page;
  readonly labelLocator: Locator;

  constructor(page: Page, name: string) {
    this.page = page;
    this.labelLocator = page.locator(`[data-testid="Label - ${name}"]`).first();
  }

  async click() {
    await this.labelLocator.click({ position: { x: 10, y: 10 } });
  }

  async move(offset: { x: number; y: number }, position: { x: number; y: number }) {
    await this.labelLocator.hover({ position });
    await this.page.mouse.down();
    await this.page.mouse.move(offset.x, +offset.y, { steps: 2 });
    await this.page.mouse.up();
  }
}
