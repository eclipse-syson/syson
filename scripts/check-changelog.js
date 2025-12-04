/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
const childProcess = require("child_process");
const fs = require("fs");

const workspace = process.env.GITHUB_WORKSPACE;
const event = process.env.GITHUB_EVENT;

const body = JSON.parse(event);
const baseSHA = body.pull_request.base.sha;
const headSHA = body.pull_request.head.sha;

const gitLogCommand = `git log --format=format:%H ${baseSHA}..${headSHA}`;
const result = childProcess.execSync(gitLogCommand, { encoding: "utf8" });
const lines = result.split(/\r?\n/);

console.log("The following commits will be reviewed:");
console.log(lines);

const changelog = fs.readFileSync(`${workspace}/CHANGELOG.adoc`, {
  encoding: "utf8",
});

const latestTag = childProcess.execSync("git describe --tags --abbrev=0", {
  encoding: "utf8",
});

const invalidChangelogContent =
  changelog.includes("<<<<<<<") ||
  changelog.includes("=======") ||
  changelog.includes(">>>>>>>");

const missingIssuesInChangelog = [];
for (let index = 0; index < lines.length; index++) {
  const line = lines[index];
  const gitShowCommand = `git rev-list --format=%B --max-count=1 ${line}`;
  const commitMessage = childProcess.execSync(gitShowCommand, {
    encoding: "utf8",
  });
  const commitMessageLines = commitMessage.split(/\r?\n/);

  if (commitMessageLines.length > 1) {
    // Skip the first line which only contains the hash of the commit
    const title = commitMessageLines[1];

    const tagStartIndex = title.indexOf("[");
    const tagEndIndex = title.indexOf("]", tagStartIndex);
    if (tagStartIndex >= 0 && tagStartIndex < tagEndIndex) {
      const tag = title.substring(tagStartIndex + "[".length, tagEndIndex);

      const tagAsNumber = Number(tag);
      if (!isNaN(tagAsNumber)) {
        const issueURL = `https://github.com/eclipse-syson/syson/issues/${tagAsNumber}`;

        if (!changelog.includes(issueURL)) {
          missingIssuesInChangelog.push(issueURL);
        }
      }
    }
  }
}

if (missingIssuesInChangelog.length > 0) {
  console.log(
    "The following issues should appear in the CHANGELOG with some documentation"
  );
  console.log(missingIssuesInChangelog);
  process.exit(1);
} else if (invalidChangelogContent) {
  console.log(
    'The CHANGELOG seems to contain Git conflict markers like "<<<<<<<", "=======" or ">>>>>>>"'
  );
  process.exit(1);
}
