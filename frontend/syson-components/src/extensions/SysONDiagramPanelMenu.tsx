/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import { DiagramToolbarActionProps } from '@eclipse-sirius/sirius-components-diagrams';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import ClickAwayListener from '@mui/material/ClickAwayListener';
import Fade from '@mui/material/Fade';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormGroup from '@mui/material/FormGroup';
import IconButton from '@mui/material/IconButton';
import Paper from '@mui/material/Paper';
import Popper from '@mui/material/Popper';
import Tooltip from '@mui/material/Tooltip';
import { useEffect, useRef, useState } from 'react';
import { makeStyles } from 'tss-react/mui';

import { ShowHideDiagramsIcons } from './ShowHideDiagramsIcons';
import { ShowHideDiagramsInheritedMembers } from './ShowHideDiagramsInheritedMembers';
import { ShowHideDiagramsInheritedMembersFromStandardLibraries } from './ShowHideDiagramsInheritedMembersFromStandardLibraries';

const useMenuStyles = makeStyles()((_) => ({
  menuEntry: {
    marginLeft: '0px',
  },
}));

export const SysONDiagramPanelMenu = ({ editingContextId, diagramId }: DiagramToolbarActionProps) => {
  const { classes } = useMenuStyles();
  const [open, setOpen] = useState<boolean>(false);
  const anchorRef = useRef<HTMLButtonElement | null>(null);

  const handleToggle = () => {
    setOpen((prevOpen) => !prevOpen);
  };

  const handleClose = (event: MouseEvent | TouchEvent) => {
    if (anchorRef.current && anchorRef.current.contains(event.target as HTMLElement)) {
      return;
    }
    setOpen(false);
  };

  const prevOpen = useRef<boolean>(open);
  useEffect(() => {
    if (prevOpen.current === true && open === false && anchorRef.current) {
      anchorRef.current.focus();
    }
    prevOpen.current = open;
  }, [open]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      const urlParams = new URLSearchParams(window.location.search);
      if (urlParams.has('showIcons') && urlParams.get('showIcons') === 'false') {
        setOpen(true);
      }
    }, 400);

    return () => clearTimeout(timeout);
  }, []);

  return (
    <span>
      <Tooltip title="Diagrams Options" placement="right">
        <IconButton
          data-testid={'syson-diagram-panel-menu-icon'}
          color="inherit"
          size="small"
          ref={anchorRef}
          aria-haspopup="true"
          onClick={handleToggle}>
          <KeyboardArrowDownIcon color={open ? 'disabled' : 'action'} />
        </IconButton>
      </Tooltip>
      <Popper
        data-testid={'syson-diagram-panel-menu'}
        open={open}
        anchorEl={anchorRef.current}
        role={undefined}
        placement={'bottom-start'}
        transition>
        {({ TransitionProps }) => (
          <Fade {...TransitionProps} timeout={350}>
            <Paper>
              <ClickAwayListener onClickAway={handleClose}>
                <FormGroup>
                  <FormControlLabel
                    key={'Show Icons - Menu Entry'}
                    className={classes.menuEntry}
                    control={<ShowHideDiagramsIcons editingContextId={editingContextId} diagramId={diagramId} />}
                    label={'Show Icons'}
                  />
                  <FormControlLabel
                    key={'Show Inherited Members - Menu Entry'}
                    className={classes.menuEntry}
                    control={
                      <ShowHideDiagramsInheritedMembers editingContextId={editingContextId} diagramId={diagramId} />
                    }
                    label={'Show Inherited Members'}
                  />
                  <FormControlLabel
                    key={'Show Inherited Members from Standard Libraries - Menu Entry'}
                    className={classes.menuEntry}
                    control={
                      <ShowHideDiagramsInheritedMembersFromStandardLibraries
                        editingContextId={editingContextId}
                        diagramId={diagramId}
                      />
                    }
                    label={'Show Inherited Members from Standard Libraries'}
                  />
                </FormGroup>
              </ClickAwayListener>
            </Paper>
          </Fade>
        )}
      </Popper>
    </span>
  );
};
