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

import java.io.IOException;

import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Preserves quoted identifiers inside qualified SysML names.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class QuotedNameSerializationTest {

    private static final String WHITESPACE_REGEX = "\\s+";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * A separator inside a quoted identifier must not split that identifier; both quoted target names and quoted
     * namespace names must be preserved.
     */
    @ParameterizedTest
    @ValueSource(strings = { "package P { part def 'A::B'; part a : P::'A::B'; }",
        "package 'P::Q' { part def A; } part a : 'P::Q'::A;" })
    void preservesQuotedQualifiedNameSegments(final String source) throws IOException {
        final var parsed = parseRootNamespace(source);

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                new SysMLSerializer().serialize(parsed).replaceAll(WHITESPACE_REGEX, ""));
    }

    private static ISysMLParseResult<Namespace> parseRootNamespace(final String source) throws IOException {
        final var parsed = SerializerRoundTripTestSupport.parseDocument(source);
        assertEquals(0, parsed.diagnostic().getSeverity());
        return parsed;
    }
}
