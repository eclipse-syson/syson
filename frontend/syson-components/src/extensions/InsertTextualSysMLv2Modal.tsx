/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { makeStyles, Theme } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import PublishIcon from '@material-ui/icons/Publish';
import { useEffect, useState } from 'react';

import CircularProgress from '@material-ui/core/CircularProgress';
import { InsertTextualSysMLv2ModalProps, InsertTextualSysMLv2ModalState } from './InsertTextualSysMLv2Modal.types';
import { useInsertTextualSysMLv2 } from './useInsertTextualSysMLv2';

const useInsertTextualSysMLv2ModalStyles = makeStyles((theme: Theme) => ({
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
}));

export const InsertTextualSysMLv2Modal = ({
  editingContextId,
  item,
  onTextualSysMLv2Inserted,
  onClose,
}: InsertTextualSysMLv2ModalProps) => {
  const classes = useInsertTextualSysMLv2ModalStyles();

  const [state, setState] = useState<InsertTextualSysMLv2ModalState>({
    insertInProgress: false,
    textualContent: '',
  });

  const { insertTextualSysMLv2, loading, textualSysMLv2Inserted } = useInsertTextualSysMLv2();

  const onInsertTextualSysMLv2 = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    event.preventDefault();
    setState((prevState) => ({ ...prevState, insertInProgress: true }));
    insertTextualSysMLv2(editingContextId, item.id, state.textualContent);
  };

  useEffect(() => {
    setState((prevState) => ({ ...prevState, insertInProgress: false }));
    if (textualSysMLv2Inserted) {
      onTextualSysMLv2Inserted();
    }
  }, [textualSysMLv2Inserted, onTextualSysMLv2Inserted]);

  return (
    <>
      <Dialog
        open={true}
        onClose={onClose}
        aria-labelledby="dialog-title"
        maxWidth="md"
        fullWidth
        data-testid={'insert-textual-sysmlv2-modal'}>
        <DialogTitle id="dialog-title">Insert textual SysMLv2</DialogTitle>
        <DialogContent>
          <div className={classes.form}>
            <TextField
              id="insert-textual-sysmlv2-modal-textarea"
              data-testid="insert-textual-sysmlv2-modal-textarea"
              className={classes.textarea}
              autoFocus
              multiline
              minRows={40}
              maxRows={40}
              variant="outlined"
              label="Write or paste textual SysMLv2 here"
              onChange={(event) => setState((prevState) => ({ ...prevState, textualContent: event.target.value }))}
            />
          </div>
        </DialogContent>
        <DialogActions>
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
            Insert
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};
