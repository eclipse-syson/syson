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
  GQLTextarea,
  GQLTextfield,
  PropertySectionComponent,
  PropertySectionComponentProps,
  PropertySectionLabel,
  TextfieldStyleProps,
} from '@eclipse-sirius/sirius-components-forms';
import Typography from '@mui/material/Typography';

import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import { useState } from 'react';
import { makeStyles } from 'tss-react/mui';
import { EditSysMLExpressionModal } from './EditSysMLExpressionModal';

const useStyle = makeStyles<TextfieldStyleProps>()(
  (theme, { backgroundColor, foregroundColor, fontSize, italic, bold, gridLayout }) => {
    const {
      gridTemplateColumns,
      gridTemplateRows,
      labelGridColumn,
      labelGridRow,
      widgetGridColumn,
      widgetGridRow,
      gap,
    } = {
      ...gridLayout,
    };
    return {
      style: {
        backgroundColor: backgroundColor ? getCSSColor(backgroundColor, theme) : undefined,
        color: foregroundColor ? getCSSColor(foregroundColor, theme) : undefined,
        fontSize: fontSize ? fontSize : undefined,
        fontStyle: italic ? 'italic' : undefined,
        fontWeight: bold ? 'bold' : undefined,
      },
      input: {
        paddingTop: theme.spacing(0.5),
        paddingBottom: theme.spacing(0.5),
      },
      textfield: {
        marginTop: theme.spacing(0.5),
        marginBottom: theme.spacing(0.5),
      },
      formControl: {},
      propertySection: {
        display: 'grid',
        gridTemplateColumns,
        gridTemplateRows,
        alignItems: 'center',
        gap: gap ?? '',
      },
      propertySectionLabel: {
        gridColumn: labelGridColumn,
        gridRow: labelGridRow,
        display: 'flex',
        flexDirection: 'row',
        gap: theme.spacing(2),
        alignItems: 'center',
      },
      propertySectionWidget: {
        gridColumn: widgetGridColumn,
        gridRow: widgetGridRow,
      },
    };
  }
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

export const ExpressionPropertySection: PropertySectionComponent<GQLTextfield | GQLTextarea> = ({
  editingContextId,
  formId,
  widget,
}: PropertySectionComponentProps<GQLTextfield | GQLTextarea>) => {
  const props: TextfieldStyleProps = {
    backgroundColor: widget.style?.backgroundColor ?? null,
    foregroundColor: widget.style?.foregroundColor ?? null,
    fontSize: widget.style?.fontSize ?? null,
    italic: widget.style?.italic ?? null,
    bold: widget.style?.bold ?? null,
    underline: widget.style?.underline ?? null,
    strikeThrough: widget.style?.strikeThrough ?? null,
    gridLayout: widget.style?.widgetGridLayout ?? null,
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

  let modalElement: JSX.Element | null = null;
  if (state.state === 'modal' && targetObjectId !== null) {
    modalElement = (
      <EditSysMLExpressionModal
        editingContextId={editingContextId}
        mode="edit"
        elementId={targetObjectId}
        onClose={onCloseModal}
      />
    );
  }

  const labelOverride = 'Expression value';
  const widgetForLabel = { ...widget, label: labelOverride };
  return (
    <div>
      <div className={classes.propertySectionLabel}>
        <PropertySectionLabel editingContextId={editingContextId} formId={formId} widget={widgetForLabel} />
      </div>
      <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between' }}>
        <Typography data-testid={`details-expression-value`}>{widget.stringValue}</Typography>
        <Tooltip title={'Edit'}>
          <IconButton size="small" onClick={onEditExpression}>
            <MoreHorizIcon />
          </IconButton>
        </Tooltip>
      </div>
      {modalElement}
    </div>
  );
};
