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

/// <reference types="vite/client" />

import { RepresentationLoadingIndicator } from '@eclipse-sirius/sirius-components-core';
import Editor, { loader } from '@monaco-editor/react';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import EditorWorker from 'monaco-editor/esm/vs/editor/editor.worker.js?worker&inline';
import { JSX } from 'react';
import { registerSysMLLanguage, SYSML_LANGUAGE_ID } from './registerSysMLLanguage';
import { TextualViewEditorProps } from './TextualViewEditor.types';

globalThis.MonacoEnvironment ??= { getWorker: () => new EditorWorker() };
loader.config({ monaco });

export const TextualViewEditor = ({ text }: TextualViewEditorProps): JSX.Element => {
  return (
    <Editor
      beforeMount={registerSysMLLanguage}
      height="100%"
      language={SYSML_LANGUAGE_ID}
      loading={<RepresentationLoadingIndicator />}
      options={{
        ariaLabel: 'SysML v2 text',
        automaticLayout: true,
        lineNumbers: 'on',
        minimap: { enabled: false },
        readOnly: true,
        scrollBeyondLastLine: false,
      }}
      value={text}
    />
  );
};
