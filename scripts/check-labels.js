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

const hasOnePriorityLabel =
  body.pull_request.labels.filter((label) => label.name.startsWith('priority:'))
    .length === 1;
const hasOneReviewLabel =
  body.pull_request.labels.filter((label) =>
    label.name.startsWith('pr: to review')
  ).length === 1;

if (!hasOnePriorityLabel || !hasOneReviewLabel) {
  console.log(
    'The pull request is either lacking the priority or the pr label'
  );
  process.exit(1);
} else if (body.pull_request.labels.length > 2) {
  console.log(
    'The pull request contains useless labels, please use priority and pr labels for pull requests'
  );
  process.exit(1);
}
