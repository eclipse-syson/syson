/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import { useDeletionConfirmationDialog } from '@eclipse-sirius/sirius-components-core';
import { TreeItemContextMenuComponentProps } from '@eclipse-sirius/sirius-components-trees';
import DeleteIcon from '@mui/icons-material/Delete';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MenuItem from '@mui/material/MenuItem';
import React, { forwardRef, Fragment } from 'react';
import { useDeleteExpression } from './useDeleteExpression';

export const DeleteSysMLExpressionMenuContribution = forwardRef(
  (
    { editingContextId, treeId, item, readOnly, onClose }: TreeItemContextMenuComponentProps,
    ref: React.ForwardedRef<HTMLLIElement>
  ) => {
    const { deleteExpression } = useDeleteExpression();
    const { showDeletionConfirmation } = useDeletionConfirmationDialog();

    if (!treeId.startsWith('explorer://') || readOnly) {
      return null;
    }

    const handleDeleteExpression = () => {
      showDeletionConfirmation(() => {
        deleteExpression(editingContextId, item.id);
        onClose();
      });
    };

    return (
      <Fragment key="delete-sysml-expression-context-menu-contribution">
        <MenuItem
          key="delete-sysml-expression-menu"
          onClick={handleDeleteExpression}
          data-testid="delete-sysml-expression-menu"
          disabled={readOnly}
          ref={ref}
          aria-disabled>
          <ListItemIcon>
            <DeleteIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText primary="Delete expression" />
        </MenuItem>
      </Fragment>
    );
  }
);
