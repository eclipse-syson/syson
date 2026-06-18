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
import { DiagramContext, DiagramContextValue } from '@eclipse-sirius/sirius-components-diagrams';
import { PaletteToolContributionComponentProps } from '@eclipse-sirius/sirius-components-palette';
import Rotate90DegreesCwIcon from '@mui/icons-material/Rotate90DegreesCw';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import { Fragment, useContext } from 'react';
import { useRotateNode } from './useRotateNode';

export const RotateNodeToolOverriddenContribution = ({
  representationElementIds,
}: PaletteToolContributionComponentProps) => {
  const { readOnly } = useContext<DiagramContextValue>(DiagramContext);
  const { rotate } = useRotateNode();

  return (
    <Fragment key="overridden_tool_node-rotate-contribution">
      <ListItemButton
        onClick={() => rotate(representationElementIds)}
        data-testid="overridden_tool_node-rotate"
        disabled={readOnly}
        sx={{ paddingTop: 0, paddingBottom: 0 }}>
        <ListItemIcon
          sx={{ minWidth: 0, marginRight: (theme) => theme.spacing(2), color: (theme) => theme.palette.text.primary }}>
          <Rotate90DegreesCwIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText
          primary={'Rotate'}
          sx={{ '& .MuiListItemText-primary': { whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' } }}
        />
      </ListItemButton>
    </Fragment>
  );
};
