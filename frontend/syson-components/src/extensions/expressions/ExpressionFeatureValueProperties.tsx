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
import Checkbox from '@mui/material/Checkbox';
import FormLabel from '@mui/material/FormLabel';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormGroup from '@mui/material/FormGroup';
import { Theme } from '@mui/material/styles';
import { FeatureValueExpressionProperties } from './ExpressionProperties.types';
import { makeStyles } from 'tss-react/mui';

const useStyles = makeStyles()((theme: Theme) => ({
  label: {
    fontFamily: theme.typography.body2.fontFamily,
    fontSize: theme.typography.body2.fontSize,
    fontWeight: theme.typography.body2.fontWeight,
    lineHeight: theme.typography.body2.lineHeight,
    letterSpacing: theme.typography.body2.letterSpacing,
    color: theme.palette.text.secondary,
  },
  checkboxLabel: {
    '& .MuiFormControlLabel-label': {
      fontFamily: theme.typography.body2.fontFamily,
      fontSize: theme.typography.body2.fontSize,
      fontWeight: theme.typography.body2.fontWeight,
      lineHeight: theme.typography.body2.lineHeight,
      letterSpacing: theme.typography.body2.letterSpacing,
      color: theme.palette.text.secondary,
    },
  },
}));

export interface ExpressionFeatureValuePropertiesProps {
  disabled: boolean;
  properties: FeatureValueExpressionProperties;
  onChange: (properties: FeatureValueExpressionProperties) => void;
}

export const ExpressionFeatureValueProperties = ({
  disabled,
  properties,
  onChange,
}: ExpressionFeatureValuePropertiesProps) => {
  const { classes } = useStyles();

  return (
    <div data-testid="expression-feature-value-properties">
      <FormLabel className={classes.label}>Properties</FormLabel>
      <FormGroup row>
        <FormControlLabel
          className={classes.checkboxLabel}
          control={
            <Checkbox
              checked={properties.isDefault}
              disabled={disabled}
              onChange={(_, checked) => onChange({ ...properties, isDefault: checked })}
            />
          }
          label="Default value"
        />
        <FormControlLabel
          className={classes.checkboxLabel}
          control={
            <Checkbox
              checked={properties.isInitial}
              disabled={disabled}
              onChange={(_, checked) => onChange({ ...properties, isInitial: checked })}
            />
          }
          label="Initial value"
        />
      </FormGroup>
    </div>
  );
};
