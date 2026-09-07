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

import {
  fuzzyMatch,
  PaletteToolOverriddenContributionComponentProps,
  ToolListItemText,
} from '@eclipse-sirius/sirius-components-palette';
import { TreePaletteContext, TreePaletteContextValue } from '@eclipse-sirius/sirius-components-trees';
import EditIcon from '@mui/icons-material/Edit';
import ListItemIcon from '@mui/material/ListItemIcon';
import MenuItem from '@mui/material/MenuItem';
import React, { forwardRef, Fragment, useContext, useState } from 'react';
import { EditSysMLExpressionModal } from './EditSysMLExpressionModal';

export const EditExpressionExplorerToolOverriddenContribution = forwardRef(
  (
    { onInvoked, searchedValue, tool }: PaletteToolOverriddenContributionComponentProps,
    ref: React.ForwardedRef<HTMLLIElement>
  ) => {
    const { editingContextId, item, treeId, readOnly, onClose } =
      useContext<TreePaletteContextValue>(TreePaletteContext);
    const [modalOpened, setModalOpened] = useState<boolean>(false);

    if (!treeId.startsWith('explorer://') || item === null || readOnly) {
      return null;
    }

    const onCloseModal = () => {
      setModalOpened(false);
      onClose();
    };

    const matchResult = searchedValue ? fuzzyMatch(tool.label, searchedValue) : null;
    if (!!searchedValue && !matchResult?.matches) {
      return null;
    }

    let modalElement: JSX.Element | null = null;
    if (modalOpened === true) {
      modalElement = (
        <EditSysMLExpressionModal
          editingContextId={editingContextId}
          elementId={item.id}
          mode="edit"
          onClose={onCloseModal}
        />
      );
    }

    return (
      <Fragment key="edit-sysml-expression-context-menu-contribution">
        <MenuItem
          key="edit-sysml-expression-menu"
          onClick={() => {
            onInvoked();
            setModalOpened(true);
          }}
          data-testid="edit-sysml-expression-menu"
          disabled={readOnly}
          ref={ref}>
          <ListItemIcon>
            <EditIcon fontSize="small" />
          </ListItemIcon>
          <ToolListItemText label={tool.label} searchedValue={searchedValue} />
        </MenuItem>
        {modalElement}
      </Fragment>
    );
  }
);
