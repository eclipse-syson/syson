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
import { ToolsButtonMenuEntryProps } from '@eclipse-sirius/sirius-components-tables';
import AddIcon from '@mui/icons-material/Add';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MenuItem from '@mui/material/MenuItem';

import { useCreateRequirement } from './useCreateRequirement';

export const CreateRequirementMenuEntry = ({ editingContextId, tableId }: ToolsButtonMenuEntryProps) => {
  const { createRequirement } = useCreateRequirement();

  return (
    <MenuItem onClick={() => createRequirement(editingContextId, tableId)}>
      <ListItemIcon>
        <AddIcon fontSize="small" />
      </ListItemIcon>
      <ListItemText>Create requirement</ListItemText>
    </MenuItem>
  );
};
