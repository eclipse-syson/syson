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

const hasOneQueueLabel =
  body.pull_request.labels.filter((label) => label.name.startsWith("queue:"))
    .length === 1;
const hasOneReviewWindowLabel =
  body.pull_request.labels.filter((label) =>
    label.name.startsWith("review-window:"),
  ).length === 1;

const hasOnlyAllowedLabels = body.pull_request.labels
  .map((label) => label.name)
  .every(
    (label) => label.startsWith("queue:") || label.startsWith("review-window:"),
  );

if (!hasOneQueueLabel || !hasOneReviewWindowLabel) {
  console.log(
    "The pull request is either lacking the queue or the review-window label",
  );
  process.exit(1);
} else if (!hasOnlyAllowedLabels) {
  console.log(
    "The pull request contains useless labels, please use queue and review-window labels for pull requests",
  );
  process.exit(1);
}
