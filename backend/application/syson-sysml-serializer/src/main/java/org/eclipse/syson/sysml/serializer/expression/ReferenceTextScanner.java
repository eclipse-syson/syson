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
package org.eclipse.syson.sysml.serializer.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.syson.sysml.serializer.tokens.AtomicTokenScanner;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;

/**
 * Text-only spelling operations used to discover and rename reference occurrences.
 * These heuristics do not resolve model elements or constitute a SysML lexer; substitution steps over string
 * literals, quoted names and comments as the grammar's atomic tokens.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class ReferenceTextScanner {

    private ReferenceTextScanner() {
        // Prevent instantiation.
    }

    /**
     * Applies each rename, in order, to the first occurrence of its original spelling that no earlier rename claimed.
     * Occurrences are searched in {@code text} itself, never in inserted replacements, so a rename whose replacement
     * spells another rename's original is not rewritten again: {@code x.v.w} with {@code v -> w} and {@code w -> z}
     * gives {@code x.w.z}.
     *
     * @param text
     *            the text holding parser spellings
     * @param renames
     *            the renames, earlier ones taking precedence
     * @return the text with the claimed occurrences replaced
     */
    static String replaceFirstReferences(final String text, final List<ReferenceRename> renames) {
        final NavigableMap<Integer, ReferenceRename> claimedOccurrences = new TreeMap<>();
        for (final ReferenceRename rename : renames) {
            final int index = firstUnclaimedReferenceIndex(text, rename.original(), claimedOccurrences);
            if (index >= 0) {
                claimedOccurrences.put(index, rename);
            }
        }
        final StringBuilder updated = new StringBuilder();
        int copiedUntil = 0;
        for (final Map.Entry<Integer, ReferenceRename> occurrence : claimedOccurrences.entrySet()) {
            updated.append(text, copiedUntil, occurrence.getKey()).append(occurrence.getValue().replacement());
            copiedUntil = occurrence.getKey() + occurrence.getValue().original().length();
        }
        return updated.append(text.substring(copiedUntil)).toString();
    }

    /** Returns the first reference occurrence of {@code original} not overlapping a claimed one, or -1. */
    private static int firstUnclaimedReferenceIndex(final String text, final String original,
            final NavigableMap<Integer, ReferenceRename> claimedOccurrences) {
        int index = firstReferenceIndex(text, original, 0);
        int overlappedEnd = overlappedClaimEnd(claimedOccurrences, index, original.length());
        while (index >= 0 && overlappedEnd >= 0) {
            // Claimed occurrences start and end on token boundaries, so scanning resumes in step with the lexer.
            index = firstReferenceIndex(text, original, overlappedEnd);
            overlappedEnd = overlappedClaimEnd(claimedOccurrences, index, original.length());
        }
        return index;
    }

    /** Returns the end of the claimed occurrence overlapping {@code [start, start + length)}, or -1 if none does. */
    private static int overlappedClaimEnd(final NavigableMap<Integer, ReferenceRename> claimedOccurrences,
            final int start, final int length) {
        int overlappedEnd = -1;
        // Claims never overlap each other, so only the last one starting before the end can reach the start.
        final Map.Entry<Integer, ReferenceRename> previous = claimedOccurrences.lowerEntry(start + length);
        if (start >= 0 && previous != null) {
            final int previousEnd = previous.getKey() + previous.getValue().original().length();
            if (previousEnd > start) {
                overlappedEnd = previousEnd;
            }
        }
        return overlappedEnd;
    }

    /**
     * Returns where {@code original} first occurs from {@code from} as a reference not embedded in a longer identifier,
     * or -1. Occurrences inside string literals, comments or other quoted names are skipped; a quoted
     * {@code original} still matches the quoted name it spells.
     */
    static int firstReferenceIndex(final String text, final String original, final int from) {
        int index = from;
        while (index < text.length()) {
            if (text.startsWith(original, index)
                    && isReferenceBoundary(text, index, index + original.length())) {
                return index;
            }
            index = Math.max(index + 1, AtomicTokenScanner.atomicTokenEnd(text, index));
        }
        return -1;
    }

    /**
     * Tells whether {@code [start, end)} is a whole reference: not embedded in a longer identifier, and not a segment
     * of a longer qualified name, since the parser records a qualified name as one spelling ({@code B} does not occur
     * in {@code Q::B}). Feature chain segments ({@code x.v}) are recorded one by one and stay eligible.
     */
    private static boolean isReferenceBoundary(final String text,
            final int start, final int end) {
        final boolean startsAtBoundary = start == 0
                || !isIdentifierCharacter(text.charAt(start - 1))
                        && !text.startsWith(SysMLTokens.NAMESPACE_SEPARATOR, start - SysMLTokens.NAMESPACE_SEPARATOR.length());
        final boolean endsAtBoundary = end == text.length()
                || !isIdentifierCharacter(text.charAt(end))
                        && !text.startsWith(SysMLTokens.NAMESPACE_SEPARATOR, end);
        return startsAtBoundary && endsAtBoundary;
    }

    /**
     * Tells whether the character at {@code index} opens or closes a quoted name, that is a quote preceded by an even
     * number of backslashes: in {@code 'a\\'} the two backslashes encode one, and the quote ends the name.
     */
    private static boolean isQuoteDelimiter(final String text, final int index) {
        int backslashes = 0;
        while (index - backslashes > 0 && text.charAt(index - backslashes - 1) == '\\') {
            backslashes++;
        }
        return text.charAt(index) == '\'' && backslashes % 2 == 0;
    }

    private static boolean isIdentifierCharacter(final char character) {
        return Character.isLetterOrDigit(character) || character == '_';
    }

    /** Returns the last {@code ::} or {@code .} segment of a spelling, which names the referenced element itself. */
    static String lastReferenceSegment(final String name) {
        return name.substring(lastReferenceSegmentStart(name));
    }

    /** Returns where the last {@code ::} or {@code .} segment starts, ignoring separators inside quotes. */
    private static int lastReferenceSegmentStart(final String name) {
        int segmentStart = 0;
        boolean quoted = false;
        int index = 0;
        while (index < name.length()) {
            final char character = name.charAt(index);
            if (isQuoteDelimiter(name, index)) {
                quoted = !quoted;
            } else if (!quoted && character == '.') {
                segmentStart = index + 1;
            } else if (!quoted && name
                    .startsWith(SysMLTokens.NAMESPACE_SEPARATOR, index)) {
                segmentStart = index + 2;
                index++;
            }
            index++;
        }
        return segmentStart;
    }

    /**
     * Returns every name-like token of {@code text} together with its {@code ::} and {@code .} segments and the
     * unquoted form of quoted segments, as lookup keys for the indexed references. String literals and comments are
     * skipped whole, so an apostrophe inside them never opens a quoted name.
     */
    static Set<String> referenceTokens(final String text) {
        final Set<String> tokens = new HashSet<>();
        final StringBuilder token = new StringBuilder();
        int index = 0;
        while (index < text.length()) {
            final char character = text.charAt(index);
            final int atomicTokenEnd = AtomicTokenScanner.atomicTokenEnd(text, index);
            if (atomicTokenEnd > index && character == '\'') {
                token.append(text, index, atomicTokenEnd);
                index = atomicTokenEnd;
            } else if (atomicTokenEnd > index) {
                addReferenceToken(tokens, token);
                index = atomicTokenEnd;
            } else {
                if (isReferenceTokenCharacter(character)) {
                    token.append(character);
                } else {
                    addReferenceToken(tokens, token);
                }
                index++;
            }
        }
        addReferenceToken(tokens, token);
        return tokens;
    }

    private static boolean isReferenceTokenCharacter(final char character) {
        return Character.isLetterOrDigit(character) || isReferencePunctuation(character);
    }

    private static boolean isReferencePunctuation(final char character) {
        return character == '_' || character == '.' || character == ':';
    }

    private static void addReferenceToken(final Set<String> tokens,
            final StringBuilder token) {
        if (token.length() == 0) {
            return;
        }
        final String value = token.toString();
        tokens.add(value);
        addReferenceSegments(tokens, value, SysMLTokens.NAMESPACE_SEPARATOR);
        addReferenceSegments(tokens, value, SysMLTokens.PATH_SEPARATOR);
        // A path mixing both separators, such as P::a.b, also yields its atomic segments, a included.
        referenceSegments(value).forEach(segment -> addReferenceSegment(tokens, segment));
        token.setLength(0);
    }

    private static void addReferenceSegments(final Set<String> tokens,
            final String text, final String separator) {
        int start = 0;
        boolean quoted = false;
        int index = 0;
        while (index < text.length()) {
            if (isQuoteDelimiter(text, index)) {
                quoted = !quoted;
            } else if (!quoted && text.startsWith(separator, index)) {
                addReferenceSegment(tokens, text.substring(start, index));
                index += separator.length() - 1;
                start = index + 1;
            }
            index++;
        }
        addReferenceSegment(tokens, text.substring(start));
    }

    private static void addReferenceSegment(final Set<String> tokens,
            final String segment) {
        if (segment.isBlank()) {
            return;
        }
        tokens.add(segment);
        if (segment.length() > 1
                && segment.startsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER)
                && segment.endsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER)) {
            tokens.add(segment.substring(1, segment.length() - 1));
        }
    }

    /** Returns the segments of a spelling; a null text gives none and a blank or simple one is a single segment. */
    static List<String> referenceSegments(final String text) {
        List<String> segments = List.of();
        if (text == null) {
            return segments;
        } else if (text.isBlank()) {
            segments = List.of(text);
        } else if (text.contains(SysMLTokens.NAMESPACE_SEPARATOR)
                || text.contains(SysMLTokens.PATH_SEPARATOR)) {
            segments = splitOutsideQuotes(text);
        } else {
            segments = List.of(text);
        }
        return segments;
    }

    private static List<String> splitOutsideQuotes(final String text) {
        final List<String> segments = new ArrayList<>();
        int start = 0;
        boolean quoted = false;
        int index = 0;
        while (index < text.length()) {
            if (isQuoteDelimiter(text, index)) {
                quoted = !quoted;
            }
            final int separatorLength = referenceSeparatorLength(text, index);
            if (!quoted && separatorLength > 0) {
                final String segment = text.substring(start, index);
                if (!segment.isBlank()) {
                    segments.add(segment);
                }
                index += separatorLength - 1;
                start = index + 1;
            }
            index++;
        }
        final String lastSegment = text.substring(start);
        if (!lastSegment.isBlank()) {
            segments.add(lastSegment);
        }
        return List.copyOf(segments);
    }

    private static int referenceSeparatorLength(final String text,
            final int index) {
        int length = 0;
        if (text.startsWith(SysMLTokens.NAMESPACE_SEPARATOR, index)) {
            length = SysMLTokens.NAMESPACE_SEPARATOR.length();
        } else if (text.charAt(index) == '.') {
            length = 1;
        }
        return length;
    }

}
