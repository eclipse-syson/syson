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

import {
  RepresentationComponentProps,
  RepresentationLoadingIndicator,
  WorkbenchMainRepresentationHandle,
} from '@eclipse-sirius/sirius-components-core';
import Box from '@mui/material/Box';
import { ForwardedRef, forwardRef, JSX, lazy, Suspense, useImperativeHandle } from 'react';

const TextualViewEditor = lazy(() =>
  import('./TextualViewEditor').then(({ TextualViewEditor }) => ({ default: TextualViewEditor }))
);

export const TEXTUAL_VIEW_REPRESENTATION_KIND = 'siriusComponents://representation?type=TextualView';

const mockedSysMLDocument = `package SimpleModel {
  part def Vehicle;
  part vehicle : Vehicle;
}`;

export const TextualViewRepresentation = forwardRef<WorkbenchMainRepresentationHandle, RepresentationComponentProps>(
  (
    { representationId }: RepresentationComponentProps,
    ref: ForwardedRef<WorkbenchMainRepresentationHandle>
  ): JSX.Element => {
    useImperativeHandle(
      ref,
      () => {
        return {
          id: representationId,
          applySelection: null,
        };
      },
      [representationId]
    );

    return (
      <Box sx={{ height: '100%', minHeight: 0 }}>
        <Suspense fallback={<RepresentationLoadingIndicator />}>
          <TextualViewEditor text={mockedSysMLDocument} />
        </Suspense>
      </Box>
    );
  }
);
