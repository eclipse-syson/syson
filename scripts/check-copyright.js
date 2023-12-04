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
const childProcess = require('child_process');
const fs = require('fs');

const workspace = process.env.GITHUB_WORKSPACE;
const event = process.env.GITHUB_EVENT;

const body = JSON.parse(event);
const baseSHA = body.pull_request.base.sha;
const headSHA = body.pull_request.head.sha;

const gitLogCommand = `git diff --name-only ${baseSHA}...${headSHA}`;
const result = childProcess.execSync(gitLogCommand, { encoding: 'utf8' });
const filePaths = result.split(/\r?\n/);

console.log('The following files will be reviewed:');
console.log(filePaths);
console.log();

const filesWithAnInvalidCopyright = [];
for (let index = 0; index < filePaths.length; index++) {
  const filePath = filePaths[index];

  if (
    filePath.endsWith('.java') ||
    filePath.endsWith('.ts') ||
    filePath.endsWith('.tsx') ||
    filePath.endsWith('.js')
  ) {
    if (fs.existsSync(filePath)) {
      const file = fs.readFileSync(`${workspace}/${filePath}`, {
        encoding: 'utf8',
      });
      const lines = file.split(/\r?\n/);

      if (lines.length > 2 && lines[1].includes('Copyright')) {
        const currentYear = new Date().getFullYear();
        if (!lines[1].includes(currentYear.toString())) {
          console.log(`${filePath} : ${lines[1]}`);
          filesWithAnInvalidCopyright.push(filePath);
        }
      }
    }
  }
}

if (filesWithAnInvalidCopyright.length > 0) {
  console.log('The following files should have their copyright updated');
  console.log(filesWithAnInvalidCopyright);
  process.exit(1);
}
