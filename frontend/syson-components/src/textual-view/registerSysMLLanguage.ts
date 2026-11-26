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

import { Monaco } from '@monaco-editor/react';

export const SYSML_LANGUAGE_ID = 'sysml';

const keywords = ['attribute', 'def', 'item', 'package', 'part', 'port', 'private', 'protected', 'public', 'ref'];

export const registerSysMLLanguage = (monaco: Monaco): void => {
  if (monaco.languages.getLanguages().some((language) => language.id === SYSML_LANGUAGE_ID)) {
    return;
  }

  monaco.languages.register({ id: SYSML_LANGUAGE_ID, extensions: ['.sysml'], aliases: ['SysML v2'] });
  monaco.languages.setMonarchTokensProvider(SYSML_LANGUAGE_ID, {
    keywords,
    tokenizer: {
      root: [
        [/[a-zA-Z_][\w$]*/, { cases: { '@keywords': 'keyword', '@default': 'identifier' } }],
        [/\/\/.*$/, 'comment'],
        [/"([^"\\]|\\.)*"/, 'string'],
        [/\d+(\.\d+)?/, 'number'],
        [/[{}()[\]]/, '@brackets'],
        [/[;,.]/, 'delimiter'],
        [/[<>=:+\-*/|&!~^%]+/, 'operator'],
      ],
    },
  });
};
