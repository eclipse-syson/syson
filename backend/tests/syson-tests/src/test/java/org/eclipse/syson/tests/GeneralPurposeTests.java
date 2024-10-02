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
package org.eclipse.syson.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * General purpose tests of the code.
 *
 * @author sbegaudeau
 * @author arichard
 */
public class GeneralPurposeTests {

    private static final String UNIX_PATH_SEPARATOR = "/";

    private static final String WINDOWS_PATH_SEPARATOR = "\\";

    private static final String GIT_FOLDER_NAME = ".git";

    private static final String BACKEND_FOLDER_PATH = "backend";

    private static final String FRONTEND_SRC_FOLDER_PATH = "frontend";

    private static final String TYPESCRIPT_FILE_EXTENSION = ".ts";

    private static final String TYPESCRIPT_JSX_FILE_EXTENSION = ".tsx";

    private static final String CSS_FILE_EXTENSION = ".css";

    private static final String JAVA_FILE_EXTENSION = "java";

    private static final String SUPPRESS_WARNINGS = "@SuppressWarnings";

    private static final String CHECKSTYLE_HIDDEN_FIELD = "@SuppressWarnings(\"checkstyle:HiddenField\")";

    private static final String CHECKSTYLE_ILLEGAL_CATCH = "@SuppressWarnings(\"checkstyle:IllegalCatch\")";

    private static final String CHECKSTYLE_MULTIPLE_STRING_LITERALS = "@SuppressWarnings(\"checkstyle:MultipleStringLiterals\")";

    private static final String CHECKSTYLE_NCSS = "@SuppressWarnings(\"checkstyle:JavaNCSS\")";

    private static final String CHECKSTYLE_INTERFACE_IS_TYPE = "@SuppressWarnings(\"checkstyle:InterfaceIsType\")";

    private static final String NON_NLS = "$NON-NLS-";

    private static final String BUILDER = "Builder";

    private static final String CHECKSTYLE_OFF = "CHECKSTYLE:OFF";

    private static final String ESLINT_DISABLE = "eslint-disable";

    private static final String THROW_NEW = "throw new";

    private static final String DEBUGGER = "debugger";

    private static final String CONSOLE_LOG = "console.log";

    private static final String ALERT = "alert(";

    private static final String CONFIRM = "confirm(";

    private static final String PROMPT = "prompt(";

    private static final String WIDTH_100 = "width: 100%;";

    private static final String HEIGHT_100 = "height: 100%;";

    private static final String INVALID_MATERIALUI_IMPORT = "from '@mui/material';";

    private static final String IT_ONLY = "it.only(";

    private static final List<Pattern> COPYRIGHT_HEADER = List.of(
            Pattern.compile(Pattern.quote("/*******************************************************************************")),
            Pattern.compile(" \\* Copyright \\(c\\) [0-9]{4}(, [0-9]{4})* (.*)\\.$"),
            Pattern.compile(Pattern.quote(" * This program and the accompanying materials")),
            Pattern.compile(Pattern.quote(" * are made available under the terms of the Eclipse Public License v2.0")),
            Pattern.compile(Pattern.quote(" * which accompanies this distribution, and is available at")),
            Pattern.compile(Pattern.quote(" * https://www.eclipse.org/legal/epl-2.0/")),
            Pattern.compile(Pattern.quote(" *")),
            Pattern.compile(Pattern.quote(" * SPDX-License-Identifier: EPL-2.0")),
            Pattern.compile(Pattern.quote(" *")),
            Pattern.compile(Pattern.quote(" * Contributors:")),
            Pattern.compile(Pattern.quote(" *     Obeo - initial API and implementation")),
            Pattern.compile(Pattern.quote(" *******************************************************************************/")));

    private static final List<String> GENERATED_MODULE_PATHS = List.of(
            "/syson-siriusweb-customnodes-metamodel",
            "/syson-sysml-metamodel",
            "/org/eclipse/syson/services/grammars"
    );

    /**
     * Finds the folder containing the Git repository.
     *
     * @return The root folder
     */
    private File getRootFolder() {
        String path = System.getProperty("user.dir");
        File classpathRoot = new File(path);

        File repositoryRootFolder = classpathRoot;
        while (!new File(repositoryRootFolder, GIT_FOLDER_NAME).exists()) {
            repositoryRootFolder = repositoryRootFolder.getParentFile();
        }

        return repositoryRootFolder;
    }

    /**
     * Finds all the files located under the given source folder path with the given extension.
     *
     * @param sourceFolderPath           The path of the source folder
     * @param includesGeneratedCodePaths Used to indicate if we want to consider generated code
     * @return The path of the files
     */
    private List<Path> findFilePaths(Path sourceFolderPath, String extension, boolean includesGeneratedCodePaths) {
        List<Path> filesPaths = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(sourceFolderPath)) {
            List<Path> filePaths = paths.filter(Files::isRegularFile)
                    .filter(filePath -> filePath.toFile().getName().endsWith(extension))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/node_modules/"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/coverage/"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/dist/"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/.turbo/"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/bin/"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/__mocks__/"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/static/"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/target/"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/.vscode/"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains(".d.ts"))
                    .filter(filePath -> !filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("/.mvn/wrapper/"))
                    .filter(filePath -> !includesGeneratedCodePaths || GENERATED_MODULE_PATHS.stream()
                            .noneMatch(modulePath -> filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains(modulePath)))
                    .toList();
            filesPaths.addAll(filePaths);
        } catch (IOException exception) {
            fail(exception.getMessage());
        }

        return filesPaths;
    }

    @Test
    public void checkJavaCode() {
        File rootFolder = this.getRootFolder();
        Path backendFolderPath = Paths.get(rootFolder.getAbsolutePath(), BACKEND_FOLDER_PATH);
        List<Path> javaFilePathsWithGenerated = this.findFilePaths(backendFolderPath, JAVA_FILE_EXTENSION, false);
        for (Path javaFilePath : javaFilePathsWithGenerated) {
            if (!javaFilePath.endsWith(GeneralPurposeTests.class.getSimpleName() + "." + JAVA_FILE_EXTENSION)) {
                try {
                    List<String> lines = Files.readAllLines(javaFilePath);
                    if (GENERATED_MODULE_PATHS.stream()
                            .noneMatch(modulePath -> javaFilePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains(modulePath))) {
                        for (int index = 0; index < lines.size(); index++) {
                            String line = lines.get(index);
                            this.testNoSuppressWarnings(index, line, javaFilePath, lines);
                            this.testNoCheckstyleOff(index, line, javaFilePath);
                            this.testNoThrowNewException(index, line, javaFilePath, lines);
                            this.testNoNonNls(index, line, javaFilePath);
                        }
                        this.testCopyrightHeader(javaFilePath, lines);
                    }
                } catch (IOException exception) {
                    fail(exception.getMessage());
                }
            }
        }
    }

    private void testCopyrightHeader(Path filePath, List<String> lines) {
        if (!this.isWhiteListed(filePath)) {
            for (int i = 0; i < COPYRIGHT_HEADER.size(); i++) {
                assertThat("Invalid copyright header in " + filePath, lines.get(i), matchesPattern(COPYRIGHT_HEADER.get(i)));
            }
            assertTrue(lines.size() >= COPYRIGHT_HEADER.size());
        }
    }

    private boolean isWhiteListed(Path filePath) {
        // @formatter:off
        return filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("src/icons")
                || filePath.toString().replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR).contains("GenericTool.tsx");
        // @formatter:on
    }

    private void testNoSuppressWarnings(int index, String line, Path javaFilePath, List<String> lines) {
        if (line.contains(SUPPRESS_WARNINGS)) {
            boolean isValidUsage = false;
            if (line.contains(CHECKSTYLE_HIDDEN_FIELD)) {
                isValidUsage = true;
                isValidUsage = isValidUsage && lines.size() > index;
                isValidUsage = isValidUsage && lines.get(index + 1).contains(BUILDER);
            } else if (line.contains(CHECKSTYLE_ILLEGAL_CATCH)) {
                isValidUsage = true;
            } else if (line.contains(CHECKSTYLE_MULTIPLE_STRING_LITERALS)) {
                isValidUsage = true;
            } else if (line.contains(CHECKSTYLE_NCSS)) {
                isValidUsage = true;
            } else if (line.contains(CHECKSTYLE_INTERFACE_IS_TYPE)) {
                isValidUsage = true;
            }
            if (!isValidUsage) {
                fail(this.createErrorMessage("@SuppressWarnings", javaFilePath, index));
            }
        }
    }

    private void testNoCheckstyleOff(int index, String line, Path javaFilePath) {
        if (line.contains(CHECKSTYLE_OFF)) {
            fail(this.createErrorMessage("CHECKSTYLE:OFF", javaFilePath, index));
        }
    }

    private void testNoThrowNewException(int index, String line, Path javaFilePath, List<String> lines) {
        if (line.contains(THROW_NEW)) {
            var isRecord = lines.stream().anyMatch(l -> l.contains("public record"));
            if (!isRecord) {
                fail(this.createErrorMessage("throw new XXXException", javaFilePath, index));
            }
        }
    }

    private void testNoNonNls(int index, String line, Path javaFilePath) {
        if (line.contains(NON_NLS)) {
            fail(this.createErrorMessage("$NON-NLS-", javaFilePath, index));
        }
    }

    @Test
    public void checkJavaScriptCode() {
        File rootFolder = this.getRootFolder();
        Path frontendFolderPath = Paths.get(rootFolder.getAbsolutePath(), FRONTEND_SRC_FOLDER_PATH);
        List<Path> typescriptFilePaths = this.findFilePaths(frontendFolderPath, TYPESCRIPT_FILE_EXTENSION, true);
        List<Path> typescriptJsxFilePaths = this.findFilePaths(frontendFolderPath, TYPESCRIPT_JSX_FILE_EXTENSION, true);
        Path integrationTestFolderPath = Paths.get(rootFolder.getAbsolutePath(), "integration-tests");
        List<Path> integrationTestTypescriptFilePaths = this.findFilePaths(integrationTestFolderPath, TYPESCRIPT_FILE_EXTENSION, true);

        List<Path> filePaths = new ArrayList<>();
        filePaths.addAll(typescriptFilePaths);
        filePaths.addAll(typescriptJsxFilePaths);
        filePaths.addAll(integrationTestTypescriptFilePaths);

        for (Path javascriptFilePath : filePaths) {
            try {
                List<String> lines = Files.readAllLines(javascriptFilePath);
                for (int index = 0; index < lines.size(); index++) {
                    String line = lines.get(index);
                    if (javascriptFilePath.toAbsolutePath().startsWith(frontendFolderPath.toAbsolutePath())) {
                        // Do not check ESLintDisable for cypress tests, we use them to ignore errors related to wait()
                        // calls.
                        this.testNoESLintDisable(index, line, javascriptFilePath);
                    }
                    this.testNoDebugger(index, line, javascriptFilePath);
                    this.testNoConsoleLog(index, line, javascriptFilePath);
                    this.testNoAlert(index, line, javascriptFilePath);
                    this.testNoConfirm(index, line, javascriptFilePath);
                    this.testNoPrompt(index, line, javascriptFilePath);
                    this.testNoInvalidMaterialUIImport(index, line, javascriptFilePath);
                    if (javascriptFilePath.toAbsolutePath().startsWith(integrationTestFolderPath.toAbsolutePath())) {
                        // Additional checks for cypress tests
                        this.testNoItOnly(index, line, javascriptFilePath);
                    }
                }
                this.testCopyrightHeader(javascriptFilePath, lines);
            } catch (IOException exception) {
                fail(exception.getMessage());
            }
        }
    }

    private void testNoESLintDisable(int index, String line, Path javascriptFilePath) {
        if (line.contains(ESLINT_DISABLE)) {
            fail(this.createErrorMessage(ESLINT_DISABLE, javascriptFilePath, index));
        }
    }

    private void testNoDebugger(int index, String line, Path javascriptFilePath) {
        if (line.contains(DEBUGGER)) {
            fail(this.createErrorMessage(DEBUGGER, javascriptFilePath, index));
        }
    }

    private void testNoConsoleLog(int index, String line, Path javascriptFilePath) {
        if (line.contains(CONSOLE_LOG)) {
            fail(this.createErrorMessage(CONSOLE_LOG, javascriptFilePath, index));
        }
    }

    private void testNoAlert(int index, String line, Path javascriptFilePath) {
        if (line.contains(ALERT)) {
            fail(this.createErrorMessage(ALERT, javascriptFilePath, index));
        }
    }

    private void testNoConfirm(int index, String line, Path javascriptFilePath) {
        if (line.contains(CONFIRM)) {
            fail(this.createErrorMessage(CONFIRM, javascriptFilePath, index));
        }
    }

    private void testNoPrompt(int index, String line, Path javascriptFilePath) {
        if (line.contains(PROMPT)) {
            fail(this.createErrorMessage(PROMPT, javascriptFilePath, index));
        }
    }

    private void testNoInvalidMaterialUIImport(int index, String line, Path javascriptFilePath) {
        if (line.contains(INVALID_MATERIALUI_IMPORT)) {
            fail(this.createErrorMessage(INVALID_MATERIALUI_IMPORT, javascriptFilePath, index));
        }
    }

    private void testNoItOnly(int index, String line, Path javascriptFilePath) {
        if (line.contains(IT_ONLY)) {
            fail(this.createErrorMessage(IT_ONLY, javascriptFilePath, index));
        }
    }

    @Test
    public void checkCSSCode() {
        File rootFolder = this.getRootFolder();
        Path frontendFolderPath = Paths.get(rootFolder.getAbsolutePath(), FRONTEND_SRC_FOLDER_PATH);
        List<Path> cssFilePaths = this.findFilePaths(frontendFolderPath, CSS_FILE_EXTENSION, true);

        for (Path cssFilePath : cssFilePaths) {
            try {
                List<String> lines = Files.readAllLines(cssFilePath);
                this.testCopyrightHeader(cssFilePath, lines);
                for (int index = 0; index < lines.size(); index++) {
                    String line = lines.get(index);
                    this.testHeight100Percent(index, line, cssFilePath);
                    this.testWidth100Percent(index, line, cssFilePath);
                }
            } catch (IOException exception) {
                fail(exception.getMessage());
            }
        }
    }

    /**
     * Test that we are not relying on width: 100%; in our CSS files.
     * <p>
     * In a component based approach, we should rely on layouting a container instead of specifying in the children how
     * they should be layouted in their parents regardless of the layout algorithm used. Use Grid or Flexbox to indicate
     * the layout of the parent but do not start from the children.
     * </p>
     * <p>
     * There are currently a couple of accepted usage such as in our modals but others may be removed. New usages are
     * highly unlikely. We are in the process of removing such usage, not adding new ones.
     * </p>
     *
     * @param index       The number of the line
     * @param line        The line to check
     * @param cssFilePath The path of the CSS file
     */
    private void testHeight100Percent(int index, String line, Path cssFilePath) {
        // @formatter:off
        var whitelist = Stream.of(
                Path.of("Modal.module.css"),
                Path.of("ToolSection.module.css")
        );
        // @formatter:on
        if (whitelist.filter(cssFilePath::endsWith).findFirst().isEmpty()) {
            if (line.contains(HEIGHT_100)) {
                fail(this.createErrorMessage(HEIGHT_100, cssFilePath, index));
            }
        }
    }

    /**
     * Test that we are not relying on width: 100%; in our CSS files.
     * <p>
     * In a component based approach, we should rely on layouting a container instead of specifying in the children how
     * they should be layouted in their parents regardless of the layout algorithm used. Use Grid or Flexbox to indicate
     * the layout of the parent but do not start from the children.
     * </p>
     * <p>
     * There are currently a couple of accepted usage such as in our modals but others may be removed. New usages are
     * highly unlikely. We are in the process of removing such usage, not adding new ones.
     * </p>
     *
     * @param index       The number of the line
     * @param line        The line to check
     * @param cssFilePath The path of the CSS file
     */
    private void testWidth100Percent(int index, String line, Path cssFilePath) {
        // @formatter:off
        var whitelist = Stream.of(
                Path.of("Modal.module.css"),
                Path.of("ToolSection.module.css")
        );
        // @formatter:on
        if (whitelist.filter(cssFilePath::endsWith).findFirst().isEmpty()) {
            if (line.contains(WIDTH_100)) {
                fail(this.createErrorMessage(WIDTH_100, cssFilePath, index));
            }
        }
    }

    private String createErrorMessage(String pattern, Path path, int lineNumber) {
        return "Invalid use of " + pattern + " in " + path.toString() + " line " + lineNumber;
    }
}
