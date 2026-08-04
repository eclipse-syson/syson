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
const childProcess = require("child_process");
const path = require("path");
const fs = require("fs");

const newSiriusWebVersion = process.argv[2];

if (!newSiriusWebVersion) {
  console.log("Use this script like this:");
  console.log("node scripts/update-sirius-web.js 2024.7.1");
  process.exit(1);
}

const workspace = process.cwd();

const updatePomProperty = (pomXmlPath, propertyName, value) => {
  const pomXmlContent = fs.readFileSync(pomXmlPath, { encoding: "utf-8" });
  const tag = `<${propertyName}>`;
  const closingTag = `</${propertyName}>`;
  const startTagIndex = pomXmlContent.indexOf(tag);
  const endTagIndex = pomXmlContent.indexOf(closingTag);
  if (startTagIndex === -1 || endTagIndex === -1) {
    throw new Error(`Could not find <${propertyName}> in ${pomXmlPath}`);
  }

  let newPomXmlContent = pomXmlContent.substring(0, startTagIndex + tag.length);
  newPomXmlContent += value;
  newPomXmlContent += pomXmlContent.substring(endTagIndex);
  fs.writeFileSync(pomXmlPath, newPomXmlContent, { encoding: "utf-8" });
};

const rootPomXmlPath = path.join(workspace, "pom.xml");
console.log(`Updating ${rootPomXmlPath}`);
updatePomProperty(rootPomXmlPath, "sirius.web.version", newSiriusWebVersion);

const siriusPackages = [
  "sirius-components-browser",
  "sirius-components-charts",
  "sirius-components-core",
  "sirius-components-datatree",
  "sirius-components-deck",
  "sirius-components-diagrams",
  "sirius-components-formdescriptioneditors",
  "sirius-components-forms",
  "sirius-components-gantt",
  "sirius-components-impactanalysis",
  "sirius-components-omnibox",
  "sirius-components-markdown",
  "sirius-components-palette",
  "sirius-components-portals",
  "sirius-components-selection",
  "sirius-components-tables",
  "sirius-components-trees",
  "sirius-components-tsconfig",
  "sirius-components-validation",
  "sirius-components-widget-reference",
  "sirius-components-widget-table",
  "sirius-web-application",
]
  .map((name) => `@eclipse-sirius/${name}@${newSiriusWebVersion}`)
  .join(" ");

const updateSiriusWebCommand = `npm install --save-exact ${siriusPackages}`;

console.log("Updating @eclipse-sirius/sirius-web in the frontend");
const sysonFrontendWorkingDirectory = path.join(workspace, "frontend", "syson");
childProcess.execSync(updateSiriusWebCommand, {
  cwd: sysonFrontendWorkingDirectory,
  stdio: "inherit",
});

const updateSiriusWebPeerCommand = `npm install --save-peer --save-exact ${siriusPackages}`;

const sysonComponentsFrontendWorkingDirectory = path.join(
  workspace,
  "frontend",
  "syson-components",
);
childProcess.execSync(updateSiriusWebPeerCommand, {
  cwd: sysonComponentsFrontendWorkingDirectory,
  stdio: "inherit",
});

const gitAddCommand = `git add .`;
console.log(gitAddCommand);
childProcess.execSync(gitAddCommand, { stdio: "inherit" });

const gitCommitCommand = `git commit -s -m "[releng] Switch to Sirius Web ${newSiriusWebVersion}"`;
console.log(gitCommitCommand);
childProcess.execSync(gitCommitCommand, { stdio: "inherit" });
