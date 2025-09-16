/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

export class Explorer {
  public getExplorerView(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.getByTestId('explorer://');
  }

  public getTreeItemByLabel(treeItemLabel: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getExplorerView().find(`[data-treeitemlabel="${treeItemLabel}"]`);
  }

  public getTreeItemByLabelAndKind(
    treeItemLabel: string,
    treeItemKind: string
  ): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getTreeItemByLabel(treeItemLabel).get(`[data-treeitemkind="${treeItemKind}"]`);
  }

  public getTreeItems(): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getExplorerView().find('[data-treeitemid]');
  }

  public getSelectedTreeItems(): Cypress.Chainable<JQuery<HTMLElement>> {
    return this.getExplorerView().find('[data-treeitemid][data-testid="selected"]');
  }

  public select(treeItemLabel: string, multiSelection: boolean = false): void {
    this.getTreeItemByLabel(treeItemLabel).should('exist');
    if (!multiSelection) {
      this.getTreeItemByLabel(treeItemLabel).first().click();
    } else {
      this.getTreeItemByLabel(treeItemLabel).click({ ctrlKey: true });
    }
    this.getTreeItemByLabel(treeItemLabel).should('have.attr', 'data-testid', 'selected');
  }

  public expand(treeItemLabel: string): void {
    this.getTreeItemByLabel(treeItemLabel).should('have.attr', 'data-expanded', 'false');
    this.getTreeItemByLabel(treeItemLabel).dblclick();
  }

  public collapse(treeItemLabel: string): void {
    this.getTreeItemByLabel(treeItemLabel).should('have.attr', 'data-expanded', 'true');
    this.getTreeItemByLabel(treeItemLabel).dblclick();
  }

  public rename(treeItemLabel: string, newName: string): void {
    this.getTreeItemByLabel(treeItemLabel).find('button').click();
    cy.getByTestId('treeitem-contextmenu').findByTestId('rename-tree-item').click();
    cy.getByTestId('name-edit');
    cy.getByTestId('name-edit').get('input').should('have.value', treeItemLabel);
    cy.getByTestId('name-edit').type(`${newName}{enter}`);
    cy.getByTestId('name-edit').should('not.exist');
    this.getTreeItemByLabel(newName).should('exist');
  }

  public delete(treeItemLabel: string): void {
    this.getTreeItemByLabel(treeItemLabel).find('button').click();
    cy.getByTestId('treeitem-contextmenu').findByTestId('delete').click();
  }

  public dragTreeItem(treeItemLabel: string, dataTransfer: DataTransfer): void {
    this.getTreeItemByLabel(treeItemLabel).trigger('dragstart', { dataTransfer });
  }

  public insertTextualSysMLv2(documentTreeItemLabel: string, textualContent: string) {
    this.getTreeItemByLabel(documentTreeItemLabel).should('exist').find('button').should('exist').click();
    cy.getByTestId('treeitem-contextmenu').should('exist');
    cy.get('@consoleDebug').should('be.calledWith', 'query getAllContextMenuEntries: response received');

    cy.getByTestId('insert-textual-sysmlv2-menu').should('exist').click();
    cy.getByTestId('insert-textual-sysmlv2-modal-textarea').should('exist');
    cy.getByTestId('insert-textual-sysmlv2-submit').should('exist').should('be.disabled');
    cy.getByTestId('insert-textual-sysmlv2-modal-textarea').type(textualContent);
    cy.getByTestId('insert-textual-sysmlv2-submit').should('not.be.disabled');
    cy.getByTestId('insert-textual-sysmlv2-submit').click();
    cy.getByTestId('insert-textual-sysmlv2-submit').should('be.disabled');
  }

  public getFilter(filterLabel: string): Cypress.Chainable<JQuery<HTMLElement>> {
    cy.getByTestId('tree-filter-menu-icon').click();
    cy.getByTestId('tree-filter-menu').should('be.visible');
    return cy.getByTestId(`tree-filter-menu-checkbox-${filterLabel}`);
  }

  public createRootObject(documentTreeItemLabel: string, childCreationDescriptionLabel: string) {
    this.getTreeItemByLabel(documentTreeItemLabel).find('button').click();
    cy.getByTestId('treeitem-contextmenu').should('exist');
    cy.get('@consoleDebug').should('be.calledWith', 'query getAllContextMenuEntries: response received');

    cy.getByTestId('new-object').click();
    cy.getByTestId('create-object').should('not.be.disabled');

    // Use get because Sirius Web doesn't provide a usable data-testid
    cy.get('input[name="suggested"]').should('exist').should('be.checked');
    cy.get('input[name="suggested"]').should('exist').click();
    cy.get('input[name="suggested"]').should('exist').should('not.be.checked');
    cy.getByTestId('type').should('not.be.disabled').contains('Accept Action');
    cy.getByTestId('type').should('not.be.disabled').click();
    cy.getByTestId('type').children('[role="combobox"]').should('have.attr', 'aria-expanded', 'true');
    // Use get because Sirius Web doesn't provide a usable data-testid
    cy.get(`li[data-value="${childCreationDescriptionLabel}"`).should('exist').click();
    cy.getByTestId('create-object').should('not.be.disabled').click();
    cy.getByTestId('create-object').should('not.exist');
  }

  public createObject(objectTreeItemLabel: string, childCreationDescriptionLabel: string) {
    this.getTreeItemByLabel(objectTreeItemLabel).first().find('button').should('exist').click();
    cy.getByTestId('treeitem-contextmenu').should('exist');
    cy.get('@consoleDebug').should('be.calledWith', 'query getAllContextMenuEntries: response received');

    cy.getByTestId('new-object').should('exist').click();
    cy.getByTestId('childCreationDescription')
      .should('exist')
      .children('[role="combobox"]')
      .invoke('text')
      .should('have.length.gt', 1);
    cy.getByTestId('childCreationDescription').should('exist').click();
    cy.getByTestId('childCreationDescription')
      .should('exist')
      .get(`[data-value="${childCreationDescriptionLabel}"]`)
      .should('exist')
      .click();
    cy.getByTestId('create-object').click();
    cy.getByTestId('new-object-modal').should('not.exist');
  }

  public createRepresentation(
    treeItemLabel: string,
    representationDescriptionName: string,
    representationLabel: string
  ): void {
    this.getTreeItemByLabel(treeItemLabel).should('exist').find('button').should('exist').click();
    cy.getByTestId('treeitem-contextmenu').should('exist');
    cy.get('@consoleDebug').should('be.calledWith', 'query getAllContextMenuEntries: response received');

    cy.getByTestId('new-representation').should('exist').click();
    cy.get('[aria-labelledby="dialog-title"]').then((modal) => {
      cy.wrap(modal).findByTestId('name').clear();
      cy.wrap(modal).findByTestId('name').type(representationLabel);
      cy.wrap(modal).findByTestId('representationDescription').click();
      // Make sure we search from inside the select's choices, which is not inside the modal
      cy.get('.MuiPopover-root').findByTestId(representationDescriptionName).click();
      cy.wrap(modal).findByTestId('create-representation').click();
    });

    // Wait for the modal to be closed and the representation actually opened
    cy.get('[aria-labelledby="dialog-title"]').should('not.exist');
    cy.getByTestId(`representation-tab-${representationLabel}`).should('be.visible');
  }

  public createNewModel(modelName: string, modelType: string): void {
    cy.getByTestId('new-model').should('exist');
    cy.getByTestId('tree-filter-menu-icon').should('exist'); // trick to avoid error if this menu is not render yet
    cy.getByTestId('new-model').click();
    cy.getByTestId('create-new-model').findByTestId('name-input').type(modelName);
    cy.getByTestId('create-new-model').findByTestId('stereotype').click();
    cy.get('li').filter(`:contains("${modelType}")`).click();
    cy.getByTestId('create-new-model').findByTestId('create-document').click();
  }
}
