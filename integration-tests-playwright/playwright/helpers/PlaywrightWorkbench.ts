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
import { type Page } from '@playwright/test';

export class PlaywrightWorkbench {
  readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  async performAction(actionLabel: string) {
    await this.page
      .locator('[data-testid="onboard-area"]')
      .locator('[data-testid="actions"]')
      .getByText(new RegExp('^' + actionLabel + '$', 'i'))
      .click();
  }
}
