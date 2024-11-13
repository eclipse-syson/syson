/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

const errors = [];

const workspace = process.env.GITHUB_WORKSPACE || process.env.PWD;
const findCommand = `find . -path "./frontend/*" -name "package.json"`;
const result = childProcess.execSync(findCommand, { encoding: "utf8" });
const filePaths = result.split(/\r?\n/);

console.log("The following files will be reviewed:");

// Let's find all the relevant package.json
const packageJsons = {};
for (let index = 0; index < filePaths.length; index++) {
  const filePath = filePaths[index];

  if (
    fs.existsSync(filePath) &&
    filePath.endsWith("/package.json") &&
    !filePath.includes("node_modules") &&
    !filePath.endsWith("syson/package.json")
  ) {
    console.log(filePath);
    const file = fs.readFileSync(`${workspace}/${filePath}`, {
      encoding: "utf8",
    });
    const json = JSON.parse(file);

    packageJsons[json.name] = json;
  }
}

console.log(
  "The following @eclipse-sirius/sirius-components dependencies have been found:"
);
const findSiriusComponentsPackageJsonsCommand = `find . -path "*/@eclipse-sirius/*" -name "package.json"`;
const resultSiriusComponentsPackageJsons = childProcess.execSync(
  findSiriusComponentsPackageJsonsCommand,
  { encoding: "utf8" }
);
const siriusComponentsPackageJsonsFilePaths =
  resultSiriusComponentsPackageJsons.split(/\r?\n/);
const siriusComponentsPackageJsons = {};
for (
  let index = 0;
  index < siriusComponentsPackageJsonsFilePaths.length;
  index++
) {
  const filePath = siriusComponentsPackageJsonsFilePaths[index];

  if (
    fs.existsSync(filePath) &&
    filePath.endsWith("/package.json") &&
    filePath.includes("node_modules/@eclipse-sirius/sirius-components-")
  ) {
    const file = fs.readFileSync(`${workspace}/${filePath}`, {
      encoding: "utf8",
    });
    const json = JSON.parse(file);
    const fileVersion = json.version;
    console.log(`${filePath} (${fileVersion})`);

    if (siriusComponentsPackageJsons[json.name] !== undefined) {
      errors.push(
        `The dependency ${json.name} already exists in node_modules.`
      );
      const existingVersion = siriusComponentsPackageJsons[json.name].version;
      if (fileVersion.localeCompare(existingVersion) === -1) {
        errors.push(`The most recent ${fileVersion} should be used.`);
        siriusComponentsPackageJsons[json.name] = json;
      } else {
        errors.push(`The most recent ${existingVersion} should be used.`);
      }
    } else {
      siriusComponentsPackageJsons[json.name] = json;
    }
  }
}

// Let's consider each package.json one by one
for (let name in packageJsons) {
  const packageJson = packageJsons[name];
  // The current package.json should have its own peerDependencies as devDependencies
  for (let currentPeerDependencyName in packageJson.peerDependencies) {
    if (!packageJson.devDependencies[currentPeerDependencyName]) {
      errors.push(
        `The dev dependency ${currentPeerDependencyName} is missing in ${name}`
      );
    }

    if (
      packageJson.peerDependencies[currentPeerDependencyName] !==
      packageJson.devDependencies[currentPeerDependencyName]
    ) {
      errors.push(
        `The dev dependency ${currentPeerDependencyName} does not have the same version has its peer dependency in ${name}`
      );
    }
  }

  // The current package.json should have the peerDependencies of each of its sirius components dependencies
  const siriusDependencies = Object.entries(
    packageJson.peerDependencies ?? {}
  ).filter(
    (dependencyEntry) =>
      dependencyEntry[0].startsWith("@eclipse-sirius") &&
      !dependencyEntry[0].endsWith("sirius-components-tsconfig")
  );

  const expectedPeerDependencies = {};

  for (const siriusDependency of siriusDependencies) {
    const requiredPeerDependencies =
      siriusComponentsPackageJsons[siriusDependency[0]].peerDependencies;
    Object.entries(requiredPeerDependencies).forEach(
      (requiredPeerDependency) => {
        expectedPeerDependencies[requiredPeerDependency[0]] =
          requiredPeerDependency[1];
      }
    );
  }

  for (let expectedPeerDependencyName in expectedPeerDependencies) {
    const existingPeerDependencyEntry =
      packageJson.peerDependencies[expectedPeerDependencyName];
    if (!existingPeerDependencyEntry) {
      errors.push(
        `The peer dependency ${expectedPeerDependencyName} is missing in ${name}`
      );
    } else if (
      existingPeerDependencyEntry !==
        expectedPeerDependencies[expectedPeerDependencyName] &&
      "*" !== expectedPeerDependencies[expectedPeerDependencyName]
    ) {
      errors.push(
        `The peer dependency ${expectedPeerDependencyName} should have the version ${expectedPeerDependencies[expectedPeerDependencyName]} instead of ${existingPeerDependencyEntry} in ${name}`
      );
    }

    const existingDevDependencyEntry =
      packageJson.devDependencies[expectedPeerDependencyName];
    if (!existingDevDependencyEntry) {
      errors.push(
        `The dev dependency ${expectedPeerDependencyName} is missing in ${name}`
      );
    } else if (
      existingDevDependencyEntry !==
        expectedPeerDependencies[expectedPeerDependencyName] &&
      "*" !== expectedPeerDependencies[expectedPeerDependencyName]
    ) {
      errors.push(
        `The dev dependency ${expectedPeerDependencyName} should have the version ${expectedPeerDependencies[expectedPeerDependencyName]} instead of ${existingDevDependencyEntry} in ${name}`
      );
    }
  }
}

if (errors.length > 0) {
  console.log("The following issues have been found in package.json files");
  console.log(errors);
  process.exit(1);
}
