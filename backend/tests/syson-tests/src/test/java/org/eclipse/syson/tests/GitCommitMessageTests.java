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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

/**
 * Tests of the latest Git commit message.
 *
 * @author sbegaudeau
 */
public class GitCommitMessageTests {

    private static final String START = "[";

    private static final String END = "]";

    private static final String[] GIT_COMMAND = { "git", "log", "-1" };

    private static final List<String> KEYWORDS = List.of("cleanup", "doc", "fix", "releng", "test", "perf", "infra", "enh");

    private static final String ISSUE_URL_PREFIX = "Bug: https://github.com/";

    private static final String SIGNED_OFF_BY_PREFIX = "Signed-off-by:";

    private static final String INVALID_GIT_MESSAGE_TITLE = "Invalid Git message title, it should either contain an issue number or one of our regular keywords (cleanup, doc, test, etc)";

    private static final String MISSING_ISSUE_URL_FOOTER = "Missing issue URL footer";

    private List<String> runCommand() {
        List<String> lines = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(GIT_COMMAND);

            try (
                BufferedReader lineReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            ) {
                lines = lineReader.lines().collect(Collectors.toList());
                assertThat(errorReader.lines()).isEmpty();
            }
        } catch (IOException exception) {
            fail(exception.getMessage());
        }
        return lines;
    }

    /**
     * Test the title of the commit message.
     *
     * <p>
     * The title of the message can contain one of our standard keywords such as cleanup, doc, fix, releng or test in
     * the following fashion:
     * </p>
     * <code>
     * <pre>
     * [doc] Title
     * </pre>
     * </code>
     * <p>
     * or even
     * </p>
     * <code>
     * <pre>
     * [cleanup] title
     * </pre>
     * </code>
     * <p>
     * Those keywords should only be used in very specific situations, most of the time the title of a commit message
     * should have a reference to a bug. On top of that the full bug URL should be available later in the commit
     * message.
     * </p>
     * <code>
     * <pre>
     * [XXX] Title
     *
     * Bug: https://github.com/ORGANIZATION/REPOSITORY/issues/XXX
     * </pre>
     * </code>
     */
    @Test
    public void testTitle() {
        assumeTrue(this.runningOnCI());
        List<String> lines = this.runCommand();
        assertThat(lines.size()).isGreaterThan(5);

        String title = lines.get(4).trim();
        assertThat(title).startsWith(START).contains(END);

        int beginIndex = title.indexOf(START);
        int endIndex = title.indexOf(END);
        assertThat(beginIndex).isLessThan(endIndex);
        
        assertThat(title.length()).isGreaterThan(endIndex + 3);
        assertThat(title.substring(endIndex + 1, endIndex + 2)).isBlank();
        assertThat(title.substring(endIndex + 2, endIndex + 3)).isUpperCase();

        assertThat(lines.get(5)).isBlank();

        String tag = title.substring(beginIndex + 1, endIndex);
        if (!KEYWORDS.contains(tag)) {
            try {
                Integer.valueOf(tag);
                assertThat(lines)
                    .withFailMessage(MISSING_ISSUE_URL_FOOTER)
                    .filteredOn(line -> line.trim().startsWith(ISSUE_URL_PREFIX) && line.endsWith(tag))
                    .hasSize(1);
            } catch (NumberFormatException exception) {
                fail(INVALID_GIT_MESSAGE_TITLE);
            }
        }

    }

    /**
     * Test the presence of the Signed-off-by: line in the commit message.
     */
    @Test
    public void testSignedOffBy() {
        assumeTrue(this.runningOnCI());
        List<String> lines = this.runCommand();
        assertThat(lines).filteredOn(line -> line.trim().startsWith(SIGNED_OFF_BY_PREFIX)).isNotEmpty();

        Predicate<Integer> isSignedOffLine = (index) -> lines.get(index).trim().startsWith(SIGNED_OFF_BY_PREFIX);
        var indexOfSignedOff = IntStream.range(0, lines.size()).filter(isSignedOffLine::test).findFirst().orElse(-1);
        Predicate<Integer> isIssueUrlLine = (index) -> lines.get(index).trim().startsWith(ISSUE_URL_PREFIX);
        var indexOfIssueURL = IntStream.range(0, lines.size()).filter(isIssueUrlLine::test).findFirst().orElse(-1);

        if (indexOfSignedOff != -1 && indexOfIssueURL != -1) {
            assertThat(Math.abs(indexOfSignedOff - indexOfIssueURL))
                    .withFailMessage("The line 'Signed-off-by: ...' and 'Bug: ...' are not next to each other")
                    .isEqualTo(1);
        }
    }

    private boolean runningOnCI() {
        return System.getenv("CI") != null;
    }
}
