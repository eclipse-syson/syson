/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
const event = process.env.GITHUB_EVENT;
const body = JSON.parse(event);

if (!body.pull_request.milestone) {
  console.log('The pull request is not affected to a milestone');
  process.exit(1);
}
