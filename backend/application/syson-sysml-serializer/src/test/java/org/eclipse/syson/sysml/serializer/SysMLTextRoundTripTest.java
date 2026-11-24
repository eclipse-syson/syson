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

import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.assertWhitespaceOnlyRoundTrip;

import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.ParserRuleFixture;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Requires ROUND_TRIP serialization to preserve source text apart from whitespace.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class SysMLTextRoundTripTest {

    /**
     * Every serializer fixture must round-trip through parsing and ROUND_TRIP serialization without changing
     * non-whitespace characters.
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport#sysmlFiles")
    void parseThenSerializeMatchesOriginalIgnoringWhitespace(
            final Path sysmlPath) throws IOException {
        assertWhitespaceOnlyRoundTrip(sysmlPath, new SysMLModelParser(),
                new SysMLSerializer());
    }

    /**
     * Every accepted parser rootNamespace fixture must retain its non-whitespace characters after ROUND_TRIP
     * serialization.
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport#parserRootNamespaceSysmlFiles")
    void parseParserRootNamespaceFixtureThenSerializeMatchesOriginalIgnoringWhitespace(
            final Path sysmlPath) throws IOException {
        assertWhitespaceOnlyRoundTrip(sysmlPath, new SysMLModelParser(),
                new SysMLSerializer());
    }

    /**
     * Every complete parser-rule fixture must consume its entire input without syntax errors and retain its
     * non-whitespace characters after serialization.
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport#completeParserRuleFixtureSysmlFiles")
    void parseParserRuleFixtureThenSerializeMatchesOriginalIgnoringWhitespace(
            final ParserRuleFixture fixture) throws IOException {
        assertWhitespaceOnlyRoundTrip(fixture, new SysMLModelParser(), new SysMLSerializer());
    }
}
