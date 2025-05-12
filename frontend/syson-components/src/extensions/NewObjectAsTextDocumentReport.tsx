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

import GetAppIcon from '@mui/icons-material/GetApp';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { Theme } from '@mui/material/styles';
import { useState } from 'react';
import { makeStyles } from 'tss-react/mui';
import { NewObjectAsTextReportProps, NewObjectAsTextReportState } from './NewObjectAsTextDocumentReport.types';

const useNewObjectAsTextReportStyles = makeStyles()((theme: Theme) => ({
  report: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    gap: theme.spacing(2),
  },
  message: {
    color: theme.palette.text.secondary,
  },
}));

export const NewObjectAsTextReport = ({ messages, success }: NewObjectAsTextReportProps) => {
  const [state, setState] = useState<NewObjectAsTextReportState>({
    downloaded: false,
  });

  const onDownloadReport = () => {
    if (success) {
      const report = messages.map((m) => m.body).join('\n');

      const fileName: string = 'upload-document-report.txt';
      const blob: Blob = new Blob([report], { type: 'text/plain' });
      const hyperlink: HTMLAnchorElement = document.createElement('a');
      hyperlink.setAttribute('download', fileName);
      hyperlink.setAttribute('href', window.URL.createObjectURL(blob));
      hyperlink.click();

      setState((prevState) => ({ ...prevState, downloaded: true }));
    }
  };

  const { classes } = useNewObjectAsTextReportStyles();

  if (!success) {
    return null;
  }

  return (
    <div className={classes.report}>
      {messages.length == 0 ? (
        <Typography variant="body1">The new object has been successfully created</Typography>
      ) : null}

      {messages.length > 0 ? (
        <>
          <Typography variant="body1">
            The new object has been partially created ({messages.length} limitations){' '}
          </Typography>
          <Button
            variant="outlined"
            size="small"
            disabled={state.downloaded}
            color="primary"
            type="button"
            form="upload-form-id"
            startIcon={<GetAppIcon />}
            data-testid="new-object-as-text-download-report"
            onClick={() => onDownloadReport()}>
            Download report
          </Button>
        </>
      ) : null}
    </div>
  );
};
