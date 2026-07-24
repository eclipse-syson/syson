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
import { Theme } from '@mui/material/styles';
import { describe, expect, it } from 'vitest';

import { getSysMLNoteNodeContainerStyle, getSysMLNotePathProps } from './SysMLNoteNode';

describe('getSysMLNotePathProps', () => {
  it('keeps the border color when the background changes', () => {
    const theme = {} as Theme;
    const initialStyle = { background: '#ffffff', borderColor: '#000000', borderWidth: 1 };
    const updatedStyle = { ...initialStyle, background: '#ffff00' };

    const initialPathProps = getSysMLNotePathProps(theme, initialStyle, false);
    const updatedPathProps = getSysMLNotePathProps(theme, updatedStyle, false);

    expect(initialPathProps.fill).toBe('#ffffff');
    expect(updatedPathProps.fill).toBe('#ffff00');
    expect(updatedPathProps.stroke).toBe(initialPathProps.stroke);
    expect(updatedPathProps.stroke).toBe('#000000');
  });
});

describe('getSysMLNoteNodeContainerStyle', () => {
  it('does not apply the node appearance outside the SVG paths', () => {
    const containerStyle = getSysMLNoteNodeContainerStyle({} as Theme, false, false, false);

    expect(containerStyle.background).toBe('transparent');
    expect(containerStyle.border).toBe('none');
  });
});
