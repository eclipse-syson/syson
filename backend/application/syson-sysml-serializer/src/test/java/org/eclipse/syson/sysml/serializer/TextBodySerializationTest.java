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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.omg.sysml.lang.sysml.Comment;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.TextualRepresentation;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Preserves comment bodies and exports edits to documentation and textual representations.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class TextBodySerializationTest {

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * An EMF-built comment must retain its exact body after serialization and parsing, including zero, one, or two
     * trailing spaces.
     */
    @ParameterizedTest
    @ValueSource(strings = { "Comment", "Comment ", "Comment  " })
    void preservesCommentBodyWithoutTextAdapters(final String body) throws Exception {
        final var comment = SysMLFactory.eINSTANCE.createComment();
        comment.setBody(body);
        final String serialized = new SysMLSerializer().serialize(comment);
        final var parsed = new SysMLModelParser().parseComment(
                new ByteArrayInputStream(serialized.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);

        assertTrue(parsed.diagnostic().getSeverity() < Diagnostic.ERROR,
                () -> parsed.diagnostic().toString());
        assertEquals(body, parsed.model().getBody());
    }

    /**
     * A body typed in SysON may contain the block comment terminator; the export must still be parseable as one
     * comment, with the terminator broken up as {@code * /} since KerML cannot escape it.
     */
    @Test
    void breaksUpBlockCommentTerminatorInsideBody() throws Exception {
        final var comment = SysMLFactory.eINSTANCE.createComment();
        comment.setBody("before */ after");
        final String serialized = new SysMLSerializer().serialize(comment);
        final var parsed = new SysMLModelParser().parseComment(
                new ByteArrayInputStream(serialized.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);

        assertTrue(parsed.diagnostic().getSeverity() < Diagnostic.ERROR,
                () -> parsed.diagnostic().toString());
        assertEquals("before * / after", parsed.model().getBody());
    }

    /**
     * A comment created in SysON has no body until the user types one; serializing it must produce an empty comment
     * instead of failing the whole export.
     */
    @Test
    void serializesCommentWithoutBodyAsEmptyComment() {
        final var comment = SysMLFactory.eINSTANCE.createComment();

        assertEquals("/**/", new SysMLSerializer().serialize(comment).strip());
    }

    /**
     * Both modes must export edited documentation and representation bodies without their old content; assigning an
     * unchanged body must leave the output unchanged.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void exportsEditedCommentAndRepresentationBodies(final SerializationMode mode) throws IOException {
        final var parsed = parseRootNamespace("package P { doc /* original doc */ rep language \"SysML\" /* original rep */ }");
        final var serializer = new SysMLSerializer();
        final var options = SerializationOptions.defaults().withMode(mode);
        final String original = serializer.serialize(parsed, options);
        parsed.model().eAllContents().forEachRemaining(object -> {
            if (object instanceof final Comment comment) {
                comment.setBody(comment.getBody());
            } else if (object instanceof final TextualRepresentation representation) {
                representation.setBody(representation.getBody());
            }
        });
        assertEquals(original, serializer.serialize(parsed, options));
        parsed.model().eAllContents().forEachRemaining(object -> {
            if (object instanceof final Comment comment) {
                comment.setBody("updated doc");
            } else if (object instanceof final TextualRepresentation representation) {
                representation.setBody("updated rep");
            }
        });

        final String serialized = serializer.serialize(parsed, options);

        assertTrue(serialized.contains("updated doc"), serialized);
        assertTrue(serialized.contains("updated rep"), serialized);
        assertFalse(serialized.contains("original"), serialized);
    }

    private static ISysMLParseResult<Namespace> parseRootNamespace(final String source) throws IOException {
        final var parsed = SerializerRoundTripTestSupport.parseDocument(source);
        assertEquals(0, parsed.diagnostic().getSeverity());
        return parsed;
    }
}
