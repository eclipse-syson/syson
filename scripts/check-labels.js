/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
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
const labels = body.pull_request.labels;

const errors = [];
if (labels.filter((label) => label.name.startsWith("priority:")).length !== 1) {
  errors.push("The pull request must have exactly one 'priority:' label");
}
if (
  labels.filter((label) => label.name.startsWith("pr: to review")).length !== 1
) {
  errors.push("The pull request must have exactly one 'pr: to review' label");
}
if (labels.length > 2) {
  errors.push(
    "The pull request contains useless labels, please use only priority and pr labels for pull requests",
  );
}

if (errors.length > 0) {
  errors.map((message) => console.log(message));
  process.exit(1);
}
