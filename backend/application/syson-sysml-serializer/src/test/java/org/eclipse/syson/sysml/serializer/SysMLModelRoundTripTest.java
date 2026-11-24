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

import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.assertSemanticRoundTripWithoutTextAdapters;

import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.ParserRuleFixture;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Requires normalized serialization without text adapters to preserve canonical model XMI.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class SysMLModelRoundTripTest {

    /**
     * Every serializer fixture must reproduce the same canonical XMI after removing ITextAdapter instances,
     * serializing, and reparsing.
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport#sysmlFiles")
    void parseThenSerializeWithoutTextAdaptersKeepsSameXmi(final Path sysmlPath)
            throws IOException {
        assertSemanticRoundTripWithoutTextAdapters(sysmlPath,
                new SysMLModelParser(), new SysMLSerializer());
    }

    /**
     * Every accepted parser rootNamespace fixture must reproduce the same canonical XMI without relying on
     * ITextAdapter instances.
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport#parserRootNamespaceSysmlFiles")
    void parseParserRootNamespaceFixtureThenSerializeWithoutTextAdaptersKeepsSameXmi(
            final Path sysmlPath) throws IOException {
        assertSemanticRoundTripWithoutTextAdapters(sysmlPath,
                new SysMLModelParser(), new SysMLSerializer());
    }

    /**
     * Every accepted parser-rule fixture must reproduce the same canonical XMI without ITextAdapter instances, using
     * its original parser entry point.
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport#parserRuleFixtureSysmlFiles")
    void parseParserRuleFixtureThenSerializeWithoutTextAdaptersKeepsSameXmi(
            final ParserRuleFixture fixture) throws IOException {
        assertSemanticRoundTripWithoutTextAdapters(fixture,
                new SysMLModelParser(), new SysMLSerializer());
    }
}
