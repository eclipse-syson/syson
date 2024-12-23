/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
console.log();

const changelog = fs.readFileSync(`${workspace}/CHANGELOG.adoc`, {
  encoding: "utf8",
});

const latestTag = childProcess.execSync("git describe --tags --abbrev=0", {
  encoding: "utf8",
});

// Get the next release version based on the latest tag
const regexpCoordinates = /v(\d{4})\.(\d{1,2})\..*/g;
const match = regexpCoordinates.exec(latestTag);
let yearReleaseVersion = Number(match[1]);
let majorReleaseVersion = Number(match[2]);
if (majorReleaseVersion === 11) {
  yearReleaseVersion++;
}
majorReleaseVersion = (majorReleaseVersion + 2) % 12;
if (majorReleaseVersion === 3) {
  majorReleaseVersion--;
}
const nextReleaseVersion = yearReleaseVersion + "." + majorReleaseVersion;
const releaseNotesPath = `doc/content/modules/user-manual/pages/release-notes/${nextReleaseVersion}.0.adoc`;
const releaseNotes = fs.readFileSync(`${workspace}/${releaseNotesPath}`);

const invalidChangelogContent =
  changelog.includes("<<<<<<<") ||
  changelog.includes("=======") ||
  changelog.includes(">>>>>>>");

const invalidReleaseNotesContent =
  releaseNotes.includes("<<<<<<<") ||
  releaseNotes.includes("=======") ||
  releaseNotes.includes(">>>>>>>");

const missingIssuesInChangelog = [];
const missingIssuesInReleaseNotes = [];
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

        // Check that the release notes has been updated in the commit
        const gitDescribeCommand = `git diff-tree --no-commit-id --name-only ${line} -r`;
        const changedFiles = childProcess.execSync(gitDescribeCommand, {
          encoding: "utf8",
        });
        const changedFilesLines = changedFiles.split(/\r?\n/);
        if (!changedFilesLines.includes(releaseNotesPath)) {
          missingIssuesInReleaseNotes.push(issueURL);
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
} else if (missingIssuesInReleaseNotes.length > 0) {
  console.log(
    `The commits referencing the following issues should contain a modification of the latest release notes (${releaseNotesPath})`
  );
  console.log(missingIssuesInReleaseNotes);
  process.exit(1);
} else if (invalidChangelogContent) {
  console.log(
    'The CHANGELOG seems to contain Git conflict markers like "<<<<<<<", "=======" or ">>>>>>>"'
  );
  process.exit(1);
} else if (invalidReleaseNotesContent) {
  console.log(
    'The release notes seems to contains Git conflict markers like "<<<<<<<", "=======" or ">>>>>>>"'
  );
  process.exit(1);
}
