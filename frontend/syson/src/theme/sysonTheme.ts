/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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

import { theme } from '@eclipse-sirius/sirius-components-core';
import { Theme, createTheme } from '@material-ui/core/styles';

export const baseTheme: Theme = createTheme({
  ...theme,
  palette: {
    type: 'light',
    primary: {
      main: '#292253',
      dark: '#64669B',
      light: '#534E75',
    },
    secondary: {
      main: '#292253',
      dark: '#64669B',
      light: '#D2D3D9',
    },
    text: {
      primary: '#292253',
      disabled: '#29225354',
      hint: '#29225312',
    },
    error: {
      main: '#DE1000',
      dark: '#9B0B00',
      light: '#E43F33',
    },
    success: {
      main: '#43A047',
      dark: '#327836',
      light: '#4EBA54',
    },
    warning: {
      main: '#FF9800',
      dark: '#D98200',
      light: '#FFB800',
    },
    info: {
      main: '#2196F3',
      dark: '#1D7DCC',
      light: '#24A7FF',
    },
    divider: '#E0E0E0',
    navigation: {
      leftBackground: '#E8E9F0',
      rightBackground: '#E8E9F0',
    },
    navigationBar: {
      border: '#FBBF52',
      background: '#292253',
    },
    selected: '#8321FF',
    action: {
      hover: '#A1A4C436',
      selected: '#A1A4C460',
    },
  },
  props: {
    MuiAppBar: {
      color: 'secondary',
    },
  },
  overrides: {
    MuiSnackbarContent: {
      root: {
        backgroundColor: '#64669B',
      },
    },
  },
});

export const sysonTheme = createTheme(
  {
    overrides: {
      MuiAvatar: {
        colorDefault: {
          backgroundColor: baseTheme.palette.primary.main,
        },
      },
    },
  },
  baseTheme
);
