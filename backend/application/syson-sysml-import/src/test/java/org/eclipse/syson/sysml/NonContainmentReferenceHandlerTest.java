/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.sysml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.eclipse.syson.sysml.parser.NonContainmentReferenceHandler;
import org.eclipse.syson.sysml.utils.MessageReporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * Test class for {@link NonContainmentReferenceHandler}.
 *
 * @author Arthur Daussy
 */
public class NonContainmentReferenceHandlerTest {

    private MessageReporter msgReporter;

    private NonContainmentReferenceHandler refHandler;

    @BeforeEach
    public void beforeEach() {
        this.msgReporter = new MessageReporter();
        this.refHandler = new NonContainmentReferenceHandler(this.msgReporter);
    }

    @Test
    public void checkInvalidTypeName() throws DatabindException, JacksonException {
        this.refHandler.createProxy(SysmlFactory.eINSTANCE.createPartDefinition(), "", this.read("""
                {
                    "$type" : "InvalidType"
                }
                """));
        assertThat(this.msgReporter.getReportedMessages()).hasSize(1)
                .hasSize(1)
                .allMatch(m -> m.body().contains("Unable to create"));
    }

    @Test
    public void checkInvalidReferenceName() throws DatabindException, JacksonException {
        this.refHandler.createProxy(SysmlFactory.eINSTANCE.createPartDefinition(), "", this.read("""
                {
                    "$type" : "PartUsage",
                    "text" : "someName"
                }
                """));
        assertThat(this.msgReporter.getReportedMessages())
                .hasSize(1)
                .allMatch(m -> m.body().contains("Unable to find a reference"));
    }

    @Test
    public void checkisNonContainmentReference() throws DatabindException, JacksonException {
        assertFalse(this.refHandler.isNonContainmentReference(SysmlFactory.eINSTANCE.createPartDefinition(), "", this.read("""
                {
                    "attr" : "value"
                }
                """)));
    }

    private JsonNode read(String input) throws DatabindException, JacksonException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(input);
    }

}
