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
import { PaletteToolOverriddenContributionComponentProps } from '@eclipse-sirius/sirius-components-palette';
import { TreePaletteContext, TreePaletteContextValue } from '@eclipse-sirius/sirius-components-trees';
import AddIcon from '@mui/icons-material/Add';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MenuItem from '@mui/material/MenuItem';
import { forwardRef, Fragment, useContext, useState } from 'react';
import { EditSysMLExpressionModal } from './EditSysMLExpressionModal';

export const NewExpressionExplorerToolOverriddenContribution = forwardRef(
  ({}: PaletteToolOverriddenContributionComponentProps, ref: React.ForwardedRef<HTMLLIElement>) => {
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

    let modalElement: JSX.Element | null = null;
    if (modalOpened === true) {
      modalElement = (
        <EditSysMLExpressionModal
          editingContextId={editingContextId}
          elementId={item.id}
          mode="create"
          onClose={onCloseModal}
        />
      );
    }

    return (
      <Fragment key="new-sysml-expression-context-menu-contribution">
        <MenuItem
          key="new-sysml-expression-menu"
          onClick={() => setModalOpened(true)}
          data-testid="new-sysml-expression-menu"
          disabled={readOnly}
          ref={ref}
          aria-disabled>
          <ListItemIcon>
            <AddIcon fontSize="small" />
          </ListItemIcon>
          <ListItemText primary="New expression" />
        </MenuItem>
        {modalElement}
      </Fragment>
    );
  }
);
