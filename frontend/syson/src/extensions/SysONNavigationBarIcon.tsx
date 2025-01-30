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
import { NavigationBarIconProps } from '@eclipse-sirius/sirius-web-application';
import IconButton from '@mui/material/IconButton';
import Link from '@mui/material/Link';
import { emphasize, Theme } from '@mui/material/styles';
import Tooltip from '@mui/material/Tooltip';
import { Link as RouterLink } from 'react-router-dom';
import { makeStyles } from 'tss-react/mui';

import { SysONIcon } from './../core/SysONIcon';

export const useNavigationBarIconStyles = makeStyles()((theme: Theme) => ({
  link: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  onDarkBackground: {
    '&:hover': {
      backgroundColor: emphasize(theme.palette.secondary.main, 0.08),
    },
  },
}));

export const SysONNavigationBarIcon = ({}: NavigationBarIconProps) => {
  const { classes } = useNavigationBarIconStyles();
  return (
    <Tooltip title="Back to the homepage">
      <Link component={RouterLink} to="/" className={classes.link} color="inherit">
        <IconButton className={classes.onDarkBackground} color="inherit">
          <SysONIcon />
        </IconButton>
      </Link>
    </Tooltip>
  );
};
