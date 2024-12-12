/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
import { Selection, useSelection } from '@eclipse-sirius/sirius-components-core';
import { TreeItemContextMenuComponentProps } from '@eclipse-sirius/sirius-components-trees';
import AddIcon from '@mui/icons-material/Add';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MenuItem from '@mui/material/MenuItem';
import { Fragment, forwardRef, useState } from 'react';
import { NewObjectModal } from '@eclipse-sirius/sirius-web-application';
import { NewRepresentationModal } from '@eclipse-sirius/sirius-web-application';

type Modal = 'CreateNewObject' | 'CreateNewRepresentation';

export const SysONObjectTreeItemContextMenuContribution = forwardRef(
  (
    { editingContextId, treeId, item, readOnly, expandItem, onClose }: TreeItemContextMenuComponentProps,
    ref: React.ForwardedRef<HTMLLIElement>
  ) => {
    const [modal, setModal] = useState<Modal>(null);
    const { setSelection } = useSelection();

    if (!treeId.startsWith('explorer://') || !item.kind.startsWith('siriusComponents://semantic')) {
      return null;
    }

    const onObjectCreated = (selection: Selection) => {
      setSelection(selection);
      expandItem();
      onClose();
    };

    let modalElement = null;
    if (modal === 'CreateNewObject') {
      modalElement = (
        <NewObjectModal
          editingContextId={editingContextId}
          item={item}
          onObjectCreated={onObjectCreated}
          onClose={onClose}
        />
      );
    } else if (modal === 'CreateNewRepresentation') {
      modalElement = (
        <NewRepresentationModal
          editingContextId={editingContextId}
          item={item}
          onRepresentationCreated={onObjectCreated}
          onClose={onClose}
        />
      );
    }

    let menuItems = [];
    if (item.editable) {
      menuItems.push(
        <MenuItem
          key="new-object"
          onClick={() => setModal('CreateNewObject')}
          data-testid="new-object"
          disabled={readOnly}
          ref={ref}
          aria-disabled>
          <ListItemIcon>
            <AddIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText primary="New object" />
        </MenuItem>
      );
      menuItems.push(
        <MenuItem
          key="new-representation"
          onClick={() => setModal('CreateNewRepresentation')}
          data-testid="new-representation"
          disabled={readOnly}
          aria-disabled>
          <ListItemIcon>
            <AddIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText primary="New representation" />
        </MenuItem>
      );
    }

    return (
      <Fragment key="object-tree-item-context-menu-contribution">
        {menuItems}
        {modalElement}
      </Fragment>
    );
  }
);
