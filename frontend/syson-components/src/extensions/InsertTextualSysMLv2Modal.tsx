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

import PublishIcon from '@mui/icons-material/Publish';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { Theme } from '@mui/material/styles';
import TextField from '@mui/material/TextField';
import { useEffect, useState } from 'react';
import { makeStyles } from 'tss-react/mui';

import { InsertTextualSysMLv2ModalProps, InsertTextualSysMLv2ModalState } from './InsertTextualSysMLv2Modal.types';
import { useInsertTextualSysMLv2 } from './useInsertTextualSysMLv2';
import { NewObjectAsTextReport } from './NewObjectAsTextDocumentReport';

const useInsertTextualSysMLv2ModalStyles = makeStyles()((theme: Theme) => ({
  form: {
    display: 'flex',
    flexDirection: 'row',
    '& > *': {
      marginBottom: theme.spacing(1),
    },
  },
  textarea: {
    flexGrow: 1,
  },
  actions: {
    marginRight: theme.spacing(2),
    marginBottom: theme.spacing(1),
  },
}));

export const InsertTextualSysMLv2Modal = ({ editingContextId, item, onClose }: InsertTextualSysMLv2ModalProps) => {
  const { classes } = useInsertTextualSysMLv2ModalStyles();

  const [state, setState] = useState<InsertTextualSysMLv2ModalState>({
    insertInProgress: false,
    textualContent: '',
  });

  const { insertTextualSysMLv2, loading, textualSysMLv2Inserted, messages } = useInsertTextualSysMLv2();

  const onInsertTextualSysMLv2 = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    event.preventDefault();
    setState((prevState) => ({ ...prevState, insertInProgress: true }));
    insertTextualSysMLv2(editingContextId, item.id, state.textualContent);
  };

  useEffect(() => {
    setState((prevState) => ({
      ...prevState,
      insertInProgress: false,
      messages: messages,
      success: textualSysMLv2Inserted,
    }));
  }, [textualSysMLv2Inserted]);

  return (
    <>
      <Dialog
        open={true}
        onClose={onClose}
        aria-labelledby="dialog-title"
        maxWidth="md"
        fullWidth
        data-testid={'insert-textual-sysmlv2-modal'}>
        <DialogTitle id="dialog-title">Enter or paste SysMLv2 text to create new objects in the model</DialogTitle>
        <DialogContent>
          <div className={classes.form}>
            <TextField
              id="insert-textual-sysmlv2-modal-textarea"
              data-testid="insert-textual-sysmlv2-modal-textarea"
              disabled={loading || textualSysMLv2Inserted}
              className={classes.textarea}
              autoFocus
              multiline
              minRows={35}
              maxRows={35}
              variant="outlined"
              onChange={(event) => setState((prevState) => ({ ...prevState, textualContent: event.target.value }))}
            />
          </div>
          <NewObjectAsTextReport messages={messages} success={textualSysMLv2Inserted} />
        </DialogContent>
        <DialogActions className={classes.actions}>
          <Button
            variant="contained"
            disabled={loading || state.textualContent.length === 0 || !!textualSysMLv2Inserted}
            data-testid="insert-textual-sysmlv2-submit"
            color="primary"
            onClick={(event) => onInsertTextualSysMLv2(event)}
            startIcon={
              state.insertInProgress ? (
                <CircularProgress size="20px" data-testid="arrange-all-circular-loading" />
              ) : (
                <PublishIcon />
              )
            }>
            Create Objects
          </Button>
          <Button
            variant={'contained'}
            color="primary"
            type="button"
            form="upload-form-id"
            data-testid="new-object-from-text-close"
            onClick={() => onClose()}>
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};
