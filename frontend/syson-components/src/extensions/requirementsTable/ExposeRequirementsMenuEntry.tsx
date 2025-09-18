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
import { SettingsButtonMenuEntryProps } from '@eclipse-sirius/sirius-components-tables';
import FormatListBulletedAddIcon from '@mui/icons-material/FormatListBulletedAdd';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MenuItem from '@mui/material/MenuItem';

import { useExposeRequirements } from './useExposeRequirements';

export const ExposeRequirementsMenuEntry = ({ editingContextId, tableId }: SettingsButtonMenuEntryProps) => {
  const { exposeRequirements } = useExposeRequirements();

  return (
    <MenuItem onClick={() => exposeRequirements(editingContextId, tableId)}>
      <ListItemIcon>
        <FormatListBulletedAddIcon fontSize="small" />
      </ListItemIcon>
      <ListItemText>Import all existing requirements</ListItemText>
    </MenuItem>
  );
};
