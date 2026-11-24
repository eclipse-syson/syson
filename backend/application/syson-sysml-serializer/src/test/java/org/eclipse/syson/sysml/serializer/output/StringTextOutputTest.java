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
package org.eclipse.syson.sysml.serializer.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.SysMLFactory;

/**
 * Tests plain string text output.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class StringTextOutputTest {

    /**
     * Blocks must emit the expected braces, configured indentation, and line separators.
     */
    @Test
    void emitsIndentedBlocks() {
        final TextOutput output = new StringTextOutput(
                new FormattingOptions("\n", "  "));

        output.token("package").space().token("P").space().beginBlock()
                .token("part").space().token("p").token(";").newline()
                .endBlock();

        assertEquals("package P {\n  part p;\n}", output.content());
    }

    /**
     * A pending space at the end of the output must not be materialized.
     */
    @Test
    void ignoresTrailingPendingSpace() {
        final TextOutput output = new StringTextOutput(
                new FormattingOptions("\n", "  "));

        output.token("part").space().token("p").space().token(";").space();

        assertEquals("part p ;", output.content());
    }

    /**
     * A reference name must be emitted as ordinary text without an additional representation of its EMF target.
     */
    @Test
    void emitsReferenceNamesAsTheSameTextAsTokens() {
        final StringTextOutput output = new StringTextOutput(
                new FormattingOptions("\n", "  "));
        final EObject target = SysMLFactory.eINSTANCE.createPackage();

        output.token("ref").space().referenceName("Target", target).token(";");

        assertEquals("ref Target;", output.content());
    }

    /**
     * Plain-text output must not evaluate the deferred reference-metadata supplier.
     */
    @Test
    void plainTextDoesNotResolveReferenceMetadata() {
        final TextOutput output = new StringTextOutput(
                new FormattingOptions("\n", "  "));

        output.referenceText("P::Target", () -> {
            fail("Plain text must not compute HTML reference metadata");
            return List.of();
        });

        assertEquals("P::Target", output.content());
    }

    /**
     * Closing a nested block must restore the parent indentation and preserve the configured CRLF line separators.
     */
    @Test
    void restoresIndentationAfterNestedBlocks() {
        final TextOutput output = new StringTextOutput(
                new FormattingOptions("\r\n", "  "));

        output.token("package P ").beginBlock().token("part p ").beginBlock()
                .token("part child;").endBlock().newline().token("part sibling;")
                .endBlock();

        assertEquals("package P {\r\n  part p {\r\n    part child;\r\n"
                + "  }\r\n  part sibling;\r\n}", output.content());
    }
}
