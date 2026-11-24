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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.PartUsage;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Defines the public serializer behavior for null inputs and incomplete models.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class SysMLSerializerApiTest {

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * A null parse result must serialize to an empty string.
     */
    @Test
    void serializesNullParseResultAsEmptyText() {
        assertEquals("", new SysMLSerializer().serialize(
                (ISysMLParseResult<? extends Element>) null));
    }

    /**
     * A null element must serialize to an empty string.
     */
    @Test
    void serializesNullElementAsEmptyText() {
        assertEquals("", new SysMLSerializer()
                .serialize((Element) null));
    }

    /**
     * A null element with null serialization options must produce an empty string without throwing an exception.
     */
    @Test
    void acceptsNullOptionsForNullElement() {
        assertEquals("",
                new SysMLSerializer().serialize(
                        (Element) null,
                        (SerializationOptions) null));
    }

    /**
     * A null element requested as HTML must produce an empty string.
     */
    @Test
    void serializesNullElementAsEmptyHtml() {
        assertEquals("", new SysMLSerializer()
                .serializeToHtml((Element) null));
    }

    /**
     * An assignment built without target or value, as an editor creates it before filling it in, must serialize
     * without throwing: the missing parts are left empty, as for other incomplete model elements, rather than being
     * replaced by an invented value. Its action node prefix (here {@code ref}, since a factory-built feature is not
     * composite) may precede the statement.
     */
    @Test
    void serializesAssignmentWithoutOperands() {
        final String serialized = new SysMLSerializer()
                .serialize(SysMLFactory.eINSTANCE.createAssignmentActionUsage());

        assertTrue(serialized.replaceAll("\\s+", "").endsWith("assign:=;"), serialized);
    }

    /**
     * The {@code defaultInterfaceEnd} rule builds only end port usages. A root of another metaclass serialized with
     * that entry point must be written as without it, keyword included: omitting the keyword would make the rule
     * reparse it as a port usage.
     */
    @Test
    void ignoresDefaultInterfaceEndEntryPointForOtherMetaclasses() {
        final PartUsage part = SysMLFactory.eINSTANCE.createPartUsage();
        part.setDeclaredName("p");
        part.setIsEnd(true);
        part.setIsOrdered(true);
        final SerializationOptions defaults = SerializationOptions.defaults();

        assertEquals(new SysMLSerializer().serialize(part, defaults),
                new SysMLSerializer().serialize(part, defaults.withParserEntryPoint("parseDefaultInterfaceEnd")));
    }
}
