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
import { ApolloLink, Observable } from '@apollo/client';
import { Selection, useSelection, useWorkbench } from '@eclipse-sirius/sirius-components-core';
import { DiagramToolbarActionProps } from '@eclipse-sirius/sirius-components-diagrams';
import { useEffect } from 'react';

interface FeatureChainSelection {
  editingContextId: string;
  diagramId: string;
  selection: Selection;
}

const listeners = new Set<(result: FeatureChainSelection) => void>();

// Observe the standard invocation so pointer and keyboard activation share selection handling.
export const redefineNestedFeatureSelectionLink = new ApolloLink((operation, forward) => {
  const input = operation.variables.input;
  const isFeatureChainInvocation =
    operation.operationName === 'invokeSingleClickOnDiagramElementTool' &&
    input?.variables?.some(
      (variable: { name: string; value: string }) =>
        variable.name === 'selectedObject' && variable.value.startsWith('contextual-feature-chain:')
    );
  if (!isFeatureChainInvocation) {
    return forward(operation);
  }
  return new Observable((observer) => {
    const subscription = forward(operation).subscribe({
      next: (result) => {
        const payload = result.data?.invokeSingleClickOnDiagramElementTool;
        if (
          !result.errors?.length &&
          payload?.__typename === 'InvokeSingleClickOnDiagramElementToolSuccessPayload' &&
          !payload.messages.some((message: { level: string }) => message.level === 'ERROR') &&
          payload.newSelection?.entries.length
        ) {
          listeners.forEach((listener) =>
            listener({
              editingContextId: input.editingContextId,
              diagramId: input.representationId,
              selection: payload.newSelection,
            })
          );
        }
        observer.next(result);
      },
      error: (error) => observer.error(error),
      complete: () => observer.complete(),
    });
    return () => subscription.unsubscribe();
  });
});

export const RedefineNestedFeatureSelection = ({ editingContextId, diagramId }: DiagramToolbarActionProps) => {
  const { setSelection } = useSelection();
  const workbench = useWorkbench();
  useEffect(() => {
    const listener = (result: FeatureChainSelection) => {
      if (result.editingContextId === editingContextId && result.diagramId === diagramId) {
        setSelection(result.selection);
        workbench
          .getWorkbenchPanelHandles()
          .flatMap((panel) => panel.getWorkbenchViewHandles())
          .find((view) => view.id === 'explorer')
          ?.applySelection?.(result.selection);
      }
    };
    listeners.add(listener);
    return () => {
      listeners.delete(listener);
    };
  }, [editingContextId, diagramId, setSelection, workbench]);
  return null;
};
