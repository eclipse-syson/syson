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
import ImageIcon from '@mui/icons-material/Image';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import { useEffect, useState } from 'react';
import { makeStyles } from 'tss-react/mui';
import { useProtoSvgExport } from './useProtoSvgExport';

const useSvgExportStyles = makeStyles()((_) => ({
  content: {
    display: 'flex',
    justifyContent: 'space-between',
    flexDirection: 'column',
  },
}));

export const SvgExport = () => {
  const { classes } = useSvgExportStyles();
  const [exportSvgDialogOpen, setExportSvgDialogOpen] = useState<boolean>(false);
  const [expectedSvg, setExpectedSvg] = useState<string | null>(null);
  const [resultSvg, setResultSvg] = useState<string | null>(null);
  const { protoExportToSvg } = useProtoSvgExport();

  useEffect(() => {
    protoExportToSvg((result, expected) => {
      setResultSvg(result);
      setExpectedSvg(expected);
    });
  }, []);

  const onCloseExportSvgDialog = () => setExportSvgDialogOpen(false);

  return (
    <>
      <Tooltip title="Export SVG" placement="right">
        <IconButton color="inherit" size="small" aria-label="Export SVG" onClick={() => setExportSvgDialogOpen(true)}>
          <ImageIcon />
        </IconButton>
      </Tooltip>
      {exportSvgDialogOpen && expectedSvg && resultSvg ? (
        <Dialog
          open={exportSvgDialogOpen}
          onClose={onCloseExportSvgDialog}
          aria-labelledby="dialog-title"
          fullWidth
          maxWidth={'lg'}>
          <DialogTitle>Svg being exported</DialogTitle>
          <DialogContent className={classes.content}>
            <img alt="diagram as svg" src={resultSvg} />
            <div dangerouslySetInnerHTML={{ __html: expectedSvg }} />
          </DialogContent>
        </Dialog>
      ) : null}
    </>
  );
};
