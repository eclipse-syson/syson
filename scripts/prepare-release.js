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
const childProcess = require("child_process");
const path = require("path");

const version = process.argv[2];
if (!version) {
  console.log("Use this script like this:");
  console.log("node scripts/prepare-release.js 2024.7.0");
  process.exit(1);
}

const workspace = process.cwd();

console.log(`Updating SysON to ${version}`);

const scriptPath = path.join(workspace, "scripts", "bump-version");
const bumpVersionCommand = `sh ${scriptPath} ${version}`;
console.log(bumpVersionCommand);
childProcess.execSync(bumpVersionCommand, { stdio: "inherit" });

const gitAddCommand = `git add backend frontend integration-tests-cypress integration-tests-playwright doc package.json package-lock.json pom.xml`;
console.log(gitAddCommand);
childProcess.execSync(gitAddCommand, { stdio: "inherit" });

const gitCommitCommand = `git commit -s -m "[releng] Bump version to ${version}"`;
console.log(gitCommitCommand);
childProcess.execSync(gitCommitCommand, { stdio: "inherit" });
