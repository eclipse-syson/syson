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

import { IconOverlay } from '@eclipse-sirius/sirius-components-core';
import { OmniboxCommandComponentProps } from '@eclipse-sirius/sirius-components-omnibox';
import { PublishLibraryDialog } from '@eclipse-sirius/sirius-web-application';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import { useState } from 'react';
import { PublishProjectSysMLContentsAsLibraryCommandState } from './PublishProjectSysMLContentsAsLibraryCommand.types';

export const PublishProjectSysMLContentsAsLibraryCommand = ({
  command,
  onKeyDown,
  onClose,
}: OmniboxCommandComponentProps) => {
  const [state, setState] = useState<PublishProjectSysMLContentsAsLibraryCommandState>({
    open: false,
  });

  const handleClick = () => setState((prevState) => ({ ...prevState, open: true }));

  return (
    <>
      <ListItemButton key={command.id} data-testid={command.label} onClick={handleClick} onKeyDown={onKeyDown}>
        <ListItemIcon>
          <IconOverlay iconURLs={command.iconURLs} alt={command.label} />
        </ListItemIcon>
        <ListItemText sx={{ whiteSpace: 'nowrap', textOverflow: 'ellipsis' }}>{command.label}</ListItemText>
      </ListItemButton>
      {state.open ? (
        <PublishLibraryDialog
          open={state.open}
          title={'Publish SysML project contents as library'}
          message={
            'Extracts the SysML contents of the project to publish them as a library that can be referenced by other projects.'
          }
          publicationKind={'Project_SysML_AllProperContents'}
          onClose={onClose}
        />
      ) : null}
    </>
  );
};
