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
import { FeatureValueExpressionProperties } from './expressionProperties.types';

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
  return (
    <div data-testid="expression-feature-value-properties">
      <FormLabel>Properties</FormLabel>
      <FormGroup row>
        <FormControlLabel
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
