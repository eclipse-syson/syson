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
package org.eclipse.syson.sysml.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.internal.SysMLParsingFactoryImpl;
import org.junit.Test;
import org.omg.sysml.lang.sysml.AttributeUsage;
import org.omg.sysml.lang.sysml.PartUsage;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.Usage;

/**
 * Tests the SysML factory used by textual parsing.
 *
 * @author ylussaud
 */
public class SysMLParsingFactoryTests {

    private static final List<EClass> NON_COMPOSITE_USAGE_TYPES = List.of(
            SysMLPackage.Literals.ATTRIBUTE_USAGE,
            SysMLPackage.Literals.BINDING_CONNECTOR_AS_USAGE,
            SysMLPackage.Literals.EVENT_OCCURRENCE_USAGE,
            SysMLPackage.Literals.EXHIBIT_STATE_USAGE,
            SysMLPackage.Literals.INCLUDE_USE_CASE_USAGE,
            SysMLPackage.Literals.PERFORM_ACTION_USAGE,
            SysMLPackage.Literals.REFERENCE_USAGE,
            SysMLPackage.Literals.SUCCESSION_AS_USAGE);

    // This test should pass once https://openmbee.atlassian.net/browse/ST6RI-950 is solved
//    @Test
//    public void standardFactoryRetainsTheEcoreDefault() {
//        assertFalse(SysMLFactory.eINSTANCE.createPartUsage().isComposite());
//    }

    @Test
    public void parsingFactoryAppliesDefaultsToEveryConcreteUsageType() {
        SysMLFactory parsingFactory = new SysMLParsingFactoryImpl();
        assertNotSame(SysMLFactory.eINSTANCE, parsingFactory);

        for (EClassifier classifier : SysMLPackage.eINSTANCE.getEClassifiers()) {
            if (classifier instanceof EClass eClass && !eClass.isAbstract() && SysMLPackage.Literals.USAGE.isSuperTypeOf(eClass)) {
                Usage usage = (Usage) parsingFactory.create(eClass);
                boolean expectedComposite = NON_COMPOSITE_USAGE_TYPES.stream().noneMatch(type -> type.isSuperTypeOf(eClass));
                assertEquals(eClass.getName(), expectedComposite, usage.isComposite());
            }
        }
    }

    @Test
    public void parserCreatesCompositePartUsage() throws IOException {
        PartUsage partUsage = parsePartUsage("part p;");

        assertTrue(partUsage.isComposite());
    }

    @Test
    public void explicitRefOverridesTheParsingDefault() throws IOException {
        PartUsage partUsage = parsePartUsage("ref part p;");

        assertFalse(partUsage.isComposite());
    }

    @Test
    public void parserKeepsAttributeUsageNonComposite() throws IOException {
        ISysMLParseResult<AttributeUsage> result = new SysMLModelParser().parseAttributeUsage(
                new ByteArrayInputStream("attribute a;".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        assertSuccessful(result);
        assertFalse(result.model().isComposite());
    }

    private static PartUsage parsePartUsage(String text) throws IOException {
        ISysMLParseResult<PartUsage> result = new SysMLModelParser().parsePartUsage(
                new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        assertSuccessful(result);
        return result.model();
    }

    private static void assertSuccessful(ISysMLParseResult<?> result) {
        assertNotNull(result.model());
        assertTrue(result.diagnostic().toString(), result.diagnostic().getSeverity() < Diagnostic.ERROR);
    }
}
