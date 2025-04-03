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
import { NavigationBarMenuIconProps } from '@eclipse-sirius/sirius-web-application';
import MenuIcon from '@mui/icons-material/Menu';
import IconButton from '@mui/material/IconButton';
import { emphasize } from '@mui/material/styles';
import { makeStyles } from 'tss-react/mui';

const useSysONNavigationBarMenuIcon = makeStyles()((theme) => ({
  navigationBarMenuIcon: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: theme.spacing(1),
  },
  iconButton: {
    '&:hover': {
      backgroundColor: emphasize(theme.palette.secondary.main, 0.08),
    },
  },
}));

export const SysONNavigationBarMenuIcon = ({ onClick }: NavigationBarMenuIconProps) => {
  const { classes } = useSysONNavigationBarMenuIcon();

  return (
    <div className={classes.navigationBarMenuIcon}>
      <IconButton className={classes.iconButton} color="inherit" onClick={onClick} data-testid="user-menu">
        <MenuIcon />
      </IconButton>
    </div>
  );
};
