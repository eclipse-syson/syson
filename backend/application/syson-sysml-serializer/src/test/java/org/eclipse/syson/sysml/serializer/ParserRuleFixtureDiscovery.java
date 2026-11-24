/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.serializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Finds parser tests that declare both a parser entry point and a fixture folder.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class ParserRuleFixtureDiscovery {

    // shortcut: matches parser calls in fixture tests, use structural parsing if those calls change shape.
    private static final Pattern PARSER_METHOD_PATTERN = Pattern
            .compile("\\bparser\\.(parse\\w+)\\((?:is, charset|lexer)\\);");

    private static final Pattern TEST_RESOURCE_FOLDER_PATTERN = Pattern.compile(
            "return\\s+retrieveTestFolders\\(\"src/test/resources/([^\"]+)\"\\);");

    private ParserRuleFixtureDiscovery() {
        // Utility class.
    }

    /** Scans direct test files and returns an immutable list sorted by resource folder, then parser method. */
    static List<ParserRuleFolder> discover(final Path testSourceRoot)
            throws IOException {
        try (Stream<Path> testSources = Files.list(testSourceRoot)) {
            return testSources.filter(ParserRuleFixtureDiscovery::isParserRuleTestSource)
                    .map(ParserRuleFixtureDiscovery::readParserRuleFolder)
                    .flatMap(Optional::stream)
                    .sorted(Comparator
                            .comparing(ParserRuleFolder::resourceFolder)
                            .thenComparing(ParserRuleFolder::parseMethod))
                    .toList();
        }
    }

    private static boolean isParserRuleTestSource(final Path path) {
        final String fileName = path.getFileName().toString();
        return fileName.endsWith("Tests.java")
                && !"AbstractSysMLRuleTestSuite.java".equals(fileName)
                && !"AllSysMLTests.java".equals(fileName);
    }

    private static Optional<ParserRuleFolder> readParserRuleFolder(
            final Path testSource) {
        try {
            final String source = Files.readString(testSource,
                    StandardCharsets.UTF_8);
            final Matcher methodMatcher = PARSER_METHOD_PATTERN.matcher(source);
            final Matcher folderMatcher = TEST_RESOURCE_FOLDER_PATTERN
                    .matcher(source);
            final boolean hasMethod = methodMatcher.find();
            final boolean hasResourceFolder = folderMatcher.find();
            if (!hasMethod && !hasResourceFolder) {
                return Optional.empty();
            }
            if (!hasMethod || !hasResourceFolder) {
                throw new IllegalStateException(
                        "Incomplete parser fixture metadata in " + testSource);
            }
            return Optional.of(new ParserRuleFolder(folderMatcher.group(1),
                    methodMatcher.group(1)));
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    "Unable to read parser test source " + testSource,
                    exception);
        }
    }

    /** Parser resource folder and corresponding parser entry point. */
    record ParserRuleFolder(String resourceFolder, String parseMethod) {
    }
}
