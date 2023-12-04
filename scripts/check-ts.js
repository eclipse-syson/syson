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

const event = process.env.GITHUB_EVENT;

const body = JSON.parse(event);
const baseSHA = body.pull_request.base.sha;
const headSHA = body.pull_request.head.sha;

const gitLogFilesCommand = `git diff --name-only --diff-filter=d ${baseSHA}...${headSHA}`;
const gitLogFilesResult = childProcess.execSync(gitLogFilesCommand, {
  encoding: 'utf8',
});
const filePaths = gitLogFilesResult.split(/\r?\n/);

console.log('The following files will be reviewed:');

const linesWithReferencesToDeprecatedCSS = [];
const linesWithMissingTypes = [];
const linesWithArrayType = [];
const linesWithPotentialNullOrUndefined = [];
const lineWithSiriusComponentsImportWithDist = [];

for (let index = 0; index < filePaths.length; index++) {
  const filePath = filePaths[index];
  console.log(filePath);

  if (filePath.endsWith('.ts') || filePath.endsWith('.tsx')) {
    const gitLogCommand = `git diff ${baseSHA}...${headSHA} -- ${filePath}`;
    const result = childProcess.execSync(gitLogCommand, { encoding: 'utf8' });
    const lines = result.split(/\r?\n/).filter((line) => line.startsWith('+'));

    for (let index = 0; index < lines.length; index++) {
      const line = lines[index];

      if (line.includes('var(--')) {
        linesWithReferencesToDeprecatedCSS.push(
          `${filePath}#${index}: ${line}`
        );
      } else if (
        line.includes('useState(') ||
        line.includes('useQuery(') ||
        line.includes('useMutation(') ||
        line.includes('useSubscription(') ||
        line.includes('useContext(') ||
        line.includes('useRef(') ||
        line.includes('useMachine(') ||
        line.includes('useReactFlow(') ||
        line.includes('useNodes(') ||
        line.includes('useEdges(')
      ) {
        linesWithMissingTypes.push(`${filePath}#${index}: ${line}`);
      } else if (line.includes(': Array<')) {
        linesWithArrayType.push(`${filePath}#${index}: ${line}`);
      } else if (line.includes('!.')) {
        linesWithPotentialNullOrUndefined.push(`${filePath}#${index}: ${line}`);
      } else if (
        line.includes('@eclipse-sirius/sirius-components') &&
        line.includes('/dist')
      ) {
        lineWithSiriusComponentsImportWithDist.push(
          `${filePath}#${index}: ${line}`
        );
      }
    }
  }
}

if (linesWithReferencesToDeprecatedCSS.length > 0) {
  console.log(
    'The following lines should not reference deprecated CSS and instead use the theme'
  );
  console.log(linesWithReferencesToDeprecatedCSS);
  process.exit(1);
} else if (linesWithMissingTypes.length > 0) {
  console.log('The following lines should be properly typed');
  console.log(linesWithMissingTypes);
  process.exit(1);
} else if (linesWithArrayType.length > 0) {
  console.log(
    'The following lines should be typed with Xxx[] instead of Array<Xxx>'
  );
  console.log(linesWithArrayType);
  process.exit(1);
} else if (linesWithPotentialNullOrUndefined.length > 0) {
  console.log(
    'The following lines should instead check that the value is not null or undefined'
  );
  console.log(linesWithPotentialNullOrUndefined);
  process.exit(1);
} else if (lineWithSiriusComponentsImportWithDist.length > 0) {
  console.log(
    "The following imports are referencing element in sirius-components 'dist' folder"
  );
  console.log(lineWithSiriusComponentsImportWithDist);
  process.exit(1);
}
