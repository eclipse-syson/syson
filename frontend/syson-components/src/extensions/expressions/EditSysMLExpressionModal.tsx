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
import { GQLMessage } from '@eclipse-sirius/sirius-components-core';
import CheckCircleOutlinedIcon from '@mui/icons-material/CheckCircleOutlined';
import ErrorOutlineOutlinedIcon from '@mui/icons-material/ErrorOutlineOutlined';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import NotesOutlinedIcon from '@mui/icons-material/NotesOutlined';
import Accordion from '@mui/material/Accordion';
import AccordionDetails from '@mui/material/AccordionDetails';
import AccordionSummary from '@mui/material/AccordionSummary';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import InputAdornment from '@mui/material/InputAdornment';
import { Theme } from '@mui/material/styles';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { useEffect, useState } from 'react';
import { makeStyles } from 'tss-react/mui';
import { EditSysMLExpressionModalProps, EditSysMLExpressionModalState } from './EditSysMLExpressionModal.types';
import { useCreateExpression } from './useCreateExpression';
import { useDeleteExpression } from './useDeleteExpression';
import { useEditExpression } from './useEditExpression';
import { useExpressionTextualRepresentation } from './useExpressionTextualRepresentation';

const useEditSysMLExpressionModalStyles = makeStyles()((theme: Theme) => ({
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: theme.spacing(2),
    '& > *': {
      marginBottom: theme.spacing(1),
    },
  },
  textarea: {
    flexGrow: 1,
  },
  feedback: {
    display: 'flex',
    flexDirection: 'column',
    overflowX: 'auto',
  },
  actions: {
    marginRight: theme.spacing(2),
    marginBottom: theme.spacing(1),
  },
  adornment: {
    alignSelf: 'flex-end',
  },
  errorAccordion: {
    backgroundColor: '#FCE9E680',
  },
  status: {
    flexGrow: 1,
    marginLeft: '16px',
  },
}));

type ValidationStatus = 'unknown' | 'valid' | 'invalid';

const computeValidationStatus = (validationResult: GQLMessage[] | null): ValidationStatus => {
  if (validationResult === null) {
    return 'unknown';
  } else {
    const errors = validationResult.filter((message) => message.level !== 'INFO' && message.level !== 'SUCCESS');
    return errors.length === 0 ? 'valid' : 'invalid';
  }
};

export const EditSysMLExpressionModal = ({
  editingContextId,
  elementId,
  mode,
  onClose,
}: EditSysMLExpressionModalProps) => {
  const { classes } = useEditSysMLExpressionModalStyles();

  const [state, setState] = useState<EditSysMLExpressionModalState>({
    textualContent: null,
    operationInProgress: null,
    validationResult: null,
  });
  const validationStatus = computeValidationStatus(state.validationResult);
  const busy = state.operationInProgress !== null;

  const { textualRepresentation, loading } = useExpressionTextualRepresentation(editingContextId, elementId);
  useEffect(() => {
    if (loading) {
      setState((prevState) => ({ ...prevState, operationInProgress: 'loading' }));
    } else {
      setState((prevState) => ({
        ...prevState,
        operationInProgress: null,
        textualContent: textualRepresentation,
        validationResult: null,
      }));
    }
  }, [textualRepresentation, loading]);

  const { createExpression, loading: creationInProgress, messages: postCreationMessages } = useCreateExpression();
  const { editExpression, loading: editionInProgress, messages: postEditionMessages } = useEditExpression();
  const { deleteExpression } = useDeleteExpression();

  const onUpdate = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    event.preventDefault();
    if (state.textualContent !== null) {
      if (mode === 'create' && state.textualContent.trim() !== '') {
        setState((prevState) => ({ ...prevState, operationInProgress: 'creating' }));
        createExpression(editingContextId, elementId, state.textualContent);
      } else if (mode === 'edit' && state.textualContent !== null) {
        if (state.textualContent.trim() === '') {
          deleteExpression(editingContextId, elementId);
          onClose();
        } else {
          setState((prevState) => ({ ...prevState, operationInProgress: 'editing' }));
          editExpression(editingContextId, elementId, state.textualContent);
        }
      }
    }
  };

  // Update validationResult when the operation is finished
  useEffect(() => {
    if (state.operationInProgress === 'creating' && !creationInProgress) {
      setState((prevState) => ({ ...prevState, operationInProgress: null, validationResult: postCreationMessages }));
    }
  }, [state.operationInProgress, creationInProgress, postCreationMessages]);
  useEffect(() => {
    if (state.operationInProgress === 'editing' && !editionInProgress) {
      setState((prevState) => ({ ...prevState, operationInProgress: null, validationResult: postEditionMessages }));
    }
  }, [state.operationInProgress, editionInProgress, postEditionMessages]);

  useEffect(() => {
    if (!busy && validationStatus === 'valid') {
      onClose(); // We're done: operation finished successfully
    }
  }, [busy, validationStatus, onClose]);

  return (
    <Dialog
      open={true}
      onClose={() => onClose()}
      aria-labelledby="dialog-title"
      maxWidth="md"
      fullWidth
      data-testid="edit-sysml-expression-modal"
      onKeyDown={(e) => e.stopPropagation()}>
      <DialogTitle id="dialog-title">{mode === 'create' ? 'Create Expression' : 'Edit Expression'}</DialogTitle>
      <DialogContent>
        <div className={classes.form}>
          <Typography variant="body2" color="textSecondary">
            Enter a valid expression below
          </Typography>
          <TextField
            id="edit-sysml-expression-modal-textarea"
            data-testid="edit-sysml-expression-modal-textarea"
            disabled={busy || state.textualContent === null}
            className={classes.textarea}
            autoFocus
            multiline
            error={validationStatus === 'invalid'}
            minRows={5}
            maxRows={5}
            variant="outlined"
            value={state.textualContent || ''}
            onChange={(event) =>
              setState((prevState) => ({ ...prevState, textualContent: event.target.value, validationResult: null }))
            }
            slotProps={{
              input: {
                endAdornment: <SysMLStatusChip validationStatus={validationStatus} />,
              },
            }}
          />
          <div className={classes.feedback}>
            {validationStatus === 'invalid' && state.validationResult ? (
              <Accordion className={classes.errorAccordion}>
                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                  <Typography variant="body2" color="error">
                    {state.validationResult.length + ' error' + (state.validationResult.length > 1 ? 's' : '')}
                  </Typography>
                </AccordionSummary>
                <AccordionDetails>
                  {state.validationResult.map((error, index) => (
                    <Typography variant="body2" color="error" key={index} data-testid="validation-error">
                      {index + 1 + '. ' + error.body}
                    </Typography>
                  ))}
                </AccordionDetails>
              </Accordion>
            ) : null}
          </div>
        </div>
      </DialogContent>
      <DialogActions className={classes.actions}>
        <div className={classes.status}>
          <ExpressionStatus
            blank={state.textualContent == null || state.textualContent.trim() === ''}
            validationStatus={validationStatus}
          />
        </div>
        <Button
          variant="outlined"
          color="secondary"
          type="button"
          data-testid="edit-expression-close"
          onClick={() => onClose()}>
          Cancel
        </Button>
        <Button
          variant="contained"
          disabled={busy || state.textualContent === null || (mode === 'create' && state.textualContent.trim() === '')}
          data-testid="edit-expression-submit"
          color="primary"
          onClick={onUpdate}>
          Update
        </Button>
      </DialogActions>
    </Dialog>
  );
};

const useExpressionStatusStyles = makeStyles()((theme: Theme) => ({
  statusSummary: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    gap: theme.spacing(1),
  },
}));

type ExpressionStatusProps = {
  blank: boolean;
  validationStatus: ValidationStatus;
};

const ExpressionStatus = ({ blank, validationStatus }: ExpressionStatusProps) => {
  const { classes } = useExpressionStatusStyles();
  if (blank) {
    return (
      <div className={classes.statusSummary}>
        <NotesOutlinedIcon color="disabled" data-testid="expression-blank" />
        <Typography variant="body1" color="disabled">
          Expression is blank, it will be deleted if you click on update
        </Typography>
      </div>
    );
  }
  switch (validationStatus) {
    case 'valid':
      return (
        <div className={classes.statusSummary}>
          <CheckCircleOutlinedIcon color="success" data-testid="expression-valid" />
          <Typography variant="body1" color="success">
            Valid
          </Typography>
        </div>
      );
    case 'invalid':
      return (
        <div className={classes.statusSummary}>
          <ErrorOutlineOutlinedIcon color="error" data-testid="expression-invalid" />
          <Typography variant="body1" color="error">
            Invalid expression, please check the syntax
          </Typography>
        </div>
      );
    default:
      return (
        <div className={classes.statusSummary}>
          <ErrorOutlineOutlinedIcon color="disabled" data-testid="expression-unknown" />
          <Typography variant="body1" color="disabled">
            Expression will be validated when you click on update
          </Typography>
        </div>
      );
  }
};

const chipFeedbackColor = (validationStatus: ValidationStatus): 'success' | 'error' | 'default' => {
  switch (validationStatus) {
    case 'valid':
      return 'success';
    case 'invalid':
      return 'error';
    default:
      return 'default';
  }
};

const SysMLStatusChip = ({ validationStatus }: { validationStatus: ValidationStatus }) => {
  const color = chipFeedbackColor(validationStatus);
  const iconColor = color === 'default' ? 'disabled' : color;
  return (
    <InputAdornment position="end" sx={{ alignSelf: 'flex-end', marginRight: 0 }}>
      <Chip
        color={color}
        icon={<NotesOutlinedIcon color={iconColor} />}
        size="small"
        variant="outlined"
        label="SysML"
      />
    </InputAdornment>
  );
};
