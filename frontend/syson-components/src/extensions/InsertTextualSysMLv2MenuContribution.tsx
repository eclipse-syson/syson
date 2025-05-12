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

import { TreeItemContextMenuComponentProps } from '@eclipse-sirius/sirius-components-trees';
import AddIcon from '@mui/icons-material/Add';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MenuItem from '@mui/material/MenuItem';
import React, { forwardRef, Fragment, useState } from 'react';
import { InsertTextualSysMLv2Modal } from './InsertTextualSysMLv2Modal';

export const InsertTextualSysMLMenuContribution = forwardRef(
  (
    { editingContextId, treeId, item, readOnly, expandItem, onClose }: TreeItemContextMenuComponentProps,
    ref: React.ForwardedRef<HTMLLIElement>
  ) => {
    const [modal, setModal] = useState<boolean>(false);

    if (!treeId.startsWith('explorer://') || !item.kind.startsWith('siriusComponents://semantic') || !item.editable) {
      return null;
    }
    const onDialogClose = () => {
      onClose();
      expandItem();
    };

    let modalElement: JSX.Element | null = null;
    if (modal === true) {
      modalElement = (
        <InsertTextualSysMLv2Modal editingContextId={editingContextId} item={item} onClose={onDialogClose} />
      );
    }

    return (
      <Fragment key="insert-textual-sysmlv2-context-menu-contribution">
        <MenuItem
          key="insert-textual-sysmlv2"
          onClick={() => setModal(true)}
          data-testid="insert-textual-sysmlv2-menu"
          disabled={readOnly}
          ref={ref}
          aria-disabled>
          <ListItemIcon>
            <AddIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText primary="New objects from text" />
        </MenuItem>
        {modalElement}
      </Fragment>
    );
  }
);
