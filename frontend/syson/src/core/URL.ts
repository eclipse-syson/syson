/*******************************************************************************
 * Copyright (c) 2019, 2022 Obeo.
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

const { VITE_HTTP_SERVER_PORT, VITE_WS_SERVER_PORT } = import.meta.env;

let httpURL = '';
if (!VITE_HTTP_SERVER_PORT || VITE_HTTP_SERVER_PORT.length === 0) {
  httpURL = `${window.location.protocol}//${window.location.host}`;
} else {
  httpURL = `${window.location.protocol}//${window.location.hostname}:${VITE_HTTP_SERVER_PORT}`;
}
export const httpOrigin = httpURL;

let wsURL = '';
if (!VITE_WS_SERVER_PORT || VITE_WS_SERVER_PORT.length === 0) {
  let wsProtocol = 'ws:';
  if ('https:' === window.location.protocol) {
    wsProtocol = 'wss:';
  }
  wsURL = `${wsProtocol}//${window.location.host}`;
} else {
  wsURL = `ws://${window.location.hostname}:${VITE_WS_SERVER_PORT}`;
}
export const wsOrigin = wsURL;
