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

import { getCSSColor } from '@eclipse-sirius/sirius-components-core';
import {
  getTextDecorationLineValue,
  GQLLabelWidget,
  LabelStyleProps,
  PropertySectionComponent,
  PropertySectionComponentProps,
  PropertySectionLabel,
} from '@eclipse-sirius/sirius-components-forms';
import AddIcon from '@mui/icons-material/Add';
import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import Typography from '@mui/material/Typography';
import { useState } from 'react';
import { makeStyles } from 'tss-react/mui';
import { EditSysMLExpressionModal } from './EditSysMLExpressionModal';

const useStyle = makeStyles<LabelStyleProps>()(
  (theme, { color, fontSize, italic, bold, underline, strikeThrough }) => ({
    style: {
      color: color ? getCSSColor(color, theme) : undefined,
      fontSize: fontSize ? fontSize : undefined,
      fontStyle: italic ? 'italic' : undefined,
      fontWeight: bold ? 'bold' : undefined,
      textDecorationLine: getTextDecorationLineValue(underline, strikeThrough),
      verticalAlign: 'baseline',
      alignItems: 'center',
      display: 'flex',
    },
    propertySection: {
      display: 'flex',
      flexDirection: 'row',
      gap: theme.spacing(2),
    },
  })
);

// Extracts the UUID from the string of the form "details://?objectIds=[c5f78f3a-8b39-4cb0-903a-cedd8e6e71f6]" if it contains a single UUID, otherwise returns null.
const extractObjectIdFromDetailsString = (detailsString: string): string | null => {
  const regex = /objectIds=\[([0-9a-fA-F-]{36})\]/;
  const match = detailsString.match(regex);
  return match && match[1] ? match[1] : null;
};

type ExpressionPropertySectionState = {
  state: 'idle' | 'modal';
};

export const ExpressionPropertySection: PropertySectionComponent<GQLLabelWidget> = ({
  editingContextId,
  formId,
  widget,
}: PropertySectionComponentProps<GQLLabelWidget>) => {
  const props: LabelStyleProps = {
    color: widget.style?.color ?? null,
    fontSize: widget.style?.fontSize ?? null,
    italic: widget.style?.italic ?? null,
    bold: widget.style?.bold ?? null,
    underline: widget.style?.underline ?? null,
    strikeThrough: widget.style?.strikeThrough ?? null,
  };
  const { classes } = useStyle(props);
  const [state, setState] = useState<ExpressionPropertySectionState>({
    state: 'idle',
  });
  const onCloseModal = () => {
    setState((prevState) => ({ ...prevState, state: 'idle' }));
  };
  const onEditExpression = () => {
    setState((prevState) => ({ ...prevState, state: 'modal' }));
  };

  const targetObjectId = extractObjectIdFromDetailsString(formId);
  const expressionPresent = widget.label === 'syson:expression-value-widget';

  let modalElement: JSX.Element | null = null;
  if (state.state === 'modal' && targetObjectId !== null) {
    modalElement = (
      <EditSysMLExpressionModal
        editingContextId={editingContextId}
        mode={expressionPresent ? 'edit' : 'create'}
        elementId={targetObjectId}
        onClose={onCloseModal}
      />
    );
  }

  const action = expressionPresent ? 'Edit' : 'Create';
  const actionIcon = expressionPresent ? <MoreHorizIcon /> : <AddIcon />;
  const labelOverride = 'Expression value';
  const widgetForLabel = { ...widget, label: labelOverride };
  return (
    <div>
      <div className={classes.propertySection}>
        <PropertySectionLabel editingContextId={editingContextId} formId={formId} widget={widgetForLabel} />
      </div>
      <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between' }}>
        {expressionPresent ? (
          <Typography data-testid={`details-expression-value`}>{widget.stringValue}</Typography>
        ) : (
          <Typography color={'textDisabled'} data-testid={`details-expression-value`}>
            {'<none>'}
          </Typography>
        )}
        <Tooltip title={action}>
          <IconButton size="small" onClick={onEditExpression} data-testid={`${action}-expression-button`}>
            {actionIcon}
          </IconButton>
        </Tooltip>
      </div>
      {modalElement}
    </div>
  );
};
