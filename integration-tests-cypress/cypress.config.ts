/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import { defineConfig } from 'cypress';

export default defineConfig({
  screenshotsFolder: 'target/screenshots',
  video: false,
  reporter: 'junit',
  reporterOptions: {
    mochaFile: 'target/result-[hash].xml',
    toConsole: true,
  },
  viewportWidth: 1920,
  viewportHeight: 1080,
  defaultCommandTimeout: 60000,
  requestTimeout: 60000,
  responseTimeout: 60000,
  env: {
    baseAPIUrl: 'http://localhost:8080',
  },
  e2e: {
    baseUrl: 'http://localhost:8080',
  },
});
