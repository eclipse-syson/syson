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
import { expect, type Locator, type Page } from '@playwright/test';

export class PlaywrightExplorer {
  readonly page: Page;
  readonly explorerLocator: Locator;

  constructor(page: Page) {
    this.page = page;
    this.explorerLocator = page.locator('[data-testid^="explorer://"]').first();
  }

  async createRootObject(treeItemLabel: string, domain: string, entity: string) {
    await this.explorerLocator.getByTestId(`${treeItemLabel}-more`).click();
    await this.page.getByTestId('new-object').click({ force: true });
    await this.page.getByTestId('domain').click();
    await this.page.locator(`[data-value="domain://${domain}"]`).click();
    await this.page.getByTestId('type').click();
    await this.page.locator(`[data-value="${entity}"]`).click();
    await this.page.getByTestId('create-object').click();
  }

  async createNewObject(treeItemLabel: string, childCreationDescriptionLabel: string) {
    await this.explorerLocator.getByTestId(`${treeItemLabel}-more`).click();
    await this.page.getByTestId('new-object').click({ force: true });
    await this.page.getByTestId('childCreationDescription').click();
    await this.page.locator(`[data-value="${childCreationDescriptionLabel}"]`).click();
    await this.page.getByTestId('create-object').click();
  }

  async createRepresentation(
    treeItemLabel: string,
    representationDescriptionName: string,
    representationLabel: string
  ) {
    await this.explorerLocator.getByTestId(`${treeItemLabel}-more`).click();
    await this.page.getByTestId('new-representation').click({ force: true });
    await this.page.getByTestId('name').clear();
    await this.page.getByTestId('name').fill(representationLabel);
    await this.page.getByTestId('representationDescription').click();
    await this.page.getByTestId(representationDescriptionName).click();
    await this.page.getByTestId('create-representation').click();
  }

  async select(treeItemLabel: string) {
    await this.explorerLocator.locator(`[data-treeitemlabel="${treeItemLabel}"]`).click();
  }

  async getTreeItemLabel(treeItemLabel: string) {
    return await this.explorerLocator.locator(`[data-treeitemlabel="${treeItemLabel}"]`);
  }

  async expand(treeItemLabel: string) {
    await this.explorerLocator.locator(`[data-treeitemlabel="${treeItemLabel}"]`).dblclick();
  }

  async uploadDocument(fileName: string) {
    await this.page.getByTestId('upload-document-icon').click();
    await this.page.locator('input[name="file"]').setInputFiles(`./playwright/resources/${fileName}`);
    await this.page.getByTestId('upload-document-split-button').click();
    await this.page.getByTestId('upload-document-close').click();
  }

  async dragTo(treeItemLabel: string, target: Locator) {
    await this.explorerLocator.locator(`[data-treeitemlabel="${treeItemLabel}"]`).dragTo(target);
  }

  async revealGlobalSelection() {
    await this.page.getByTestId('explorer-reveal-selection-button').click();
  }

  async showIn(treeItemLabel: string, selectionTargetLabel: string) {
    await this.explorerLocator.locator(`[data-treeitemlabel="${treeItemLabel}"]`).click();
    await this.explorerLocator.getByTestId(`${treeItemLabel}-more`).click();
    await this.page.getByTestId(`push-selection-to-${selectionTargetLabel}`).click();
  }

  async insertTextualSysMLv2(treeItemLabel: string, textualContent: string) {
    await this.explorerLocator.locator(`[data-treeitemlabel="${treeItemLabel}"]`).click();
    await this.explorerLocator.getByTestId(`${treeItemLabel}-more`).click();

    const menu = 'insert-textual-sysmlv2-menu';
    const textarea = 'insert-textual-sysmlv2-modal-textarea';
    const submitButton = 'insert-textual-sysmlv2-submit';
    const closeButton = 'new-object-from-text-close';

    await this.page.getByTestId(menu).click({ force: true });
    await expect(this.page.getByTestId(textarea)).toBeAttached();
    await expect(this.page.getByTestId(submitButton)).toBeAttached();
    await expect(this.page.getByTestId(submitButton)).toBeDisabled();

    await this.page.getByTestId(textarea).click({ force: true });
    await this.page.getByTestId(textarea).type(textualContent);
    await expect(this.page.getByTestId(submitButton)).toBeEnabled();
    await this.page.getByTestId(submitButton).click();

    await expect(this.page.getByTestId(submitButton)).toBeDisabled();

    await this.page.getByTestId(closeButton).click();
  }
}
