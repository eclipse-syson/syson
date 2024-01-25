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
const path = require("path");
const fs = require("fs");

const newSiriusWebVersion = process.argv[2];

if (!newSiriusWebVersion) {
  console.log("Use this script like this:");
  console.log("node scripts/update-sirius-web.js 2023.12.0");
  process.exit(1);
}

const workspace = process.cwd();

const projects = new Map();
projects.set("syson-application", "application");
projects.set("syson-application-configuration", "application");
projects.set("syson-frontend", "application");
projects.set("syson-sysml-metamodel", "metamodel");
projects.set("syson-sysml-metamodel-edit", "metamodel");
projects.set("syson-siriusweb-customnodes-metamodel", "metamodel");
projects.set("syson-siriusweb-customnodes-metamodel-edit", "metamodel");
projects.set("syson-services", "services");
projects.set("syson-diagram-general-view", "views");
projects.set("syson-diagram-interconnection-view", "views");

console.log("Updating the following pom.xml:");
projects.forEach((folder, project) => {
  const pomXmlPath = path.join(
    workspace,
    "backend",
    folder,
    project,
    "pom.xml"
  );
  console.log(pomXmlPath);

  const pomXmlContent = fs.readFileSync(pomXmlPath, { encoding: "utf-8" });
  const startTagIndex = pomXmlContent.indexOf("<sirius.web.version>");
  const endTagIndex = pomXmlContent.indexOf("</sirius.web.version>");
  if (startTagIndex !== -1 && endTagIndex !== -1) {
    let newPomXmlContent = pomXmlContent.substring(
      0,
      startTagIndex + "<sirius.web.version>".length
    );
    newPomXmlContent += newSiriusWebVersion;
    newPomXmlContent += pomXmlContent.substring(endTagIndex);
    fs.writeFileSync(pomXmlPath, newPomXmlContent, { encoding: "utf-8" });
  }
});

const updateSiriusWebCommand = `npm install @eclipse-sirius/sirius-components-charts@${newSiriusWebVersion} @eclipse-sirius/sirius-components-core@${newSiriusWebVersion} @eclipse-sirius/sirius-components-deck@${newSiriusWebVersion} @eclipse-sirius/sirius-components-diagrams-reactflow@${newSiriusWebVersion} @eclipse-sirius/sirius-components-formdescriptioneditors@${newSiriusWebVersion} @eclipse-sirius/sirius-components-forms@${newSiriusWebVersion} @eclipse-sirius/sirius-components-gantt@${newSiriusWebVersion} @eclipse-sirius/sirius-components-widget-reference@${newSiriusWebVersion} @eclipse-sirius/sirius-components-selection@${newSiriusWebVersion} @eclipse-sirius/sirius-components-trees@${newSiriusWebVersion} @eclipse-sirius/sirius-components-validation@${newSiriusWebVersion} @eclipse-sirius/sirius-components-tsconfig@${newSiriusWebVersion} @eclipse-sirius/sirius-web-application@${newSiriusWebVersion} --save-exact`;

console.log("Updating @eclipse-sirius/sirius-web in the frontend");
const sysonFrontendWorkingDirectory = path.join(workspace, "frontend", "syson");
childProcess.execSync(updateSiriusWebCommand, {
  cwd: sysonFrontendWorkingDirectory,
  stdio: "inherit",
});
const sysonComponentsFrontendWorkingDirectory = path.join(
  workspace,
  "frontend",
  "syson-components"
);
childProcess.execSync(updateSiriusWebCommand, {
  cwd: sysonComponentsFrontendWorkingDirectory,
  stdio: "inherit",
});

const gitAddCommand = `git add .`;
console.log(gitAddCommand);
childProcess.execSync(gitAddCommand, { stdio: "inherit" });

const gitCommitCommand = `git commit -s -m "[releng] Switch to Sirius Web ${newSiriusWebVersion}"`;
console.log(gitCommitCommand);
childProcess.execSync(gitCommitCommand, { stdio: "inherit" });
