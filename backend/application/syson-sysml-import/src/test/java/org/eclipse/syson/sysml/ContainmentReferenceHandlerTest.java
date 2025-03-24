/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.syson.sysml.parser.ContainmentReferenceHandler;
import org.eclipse.syson.sysml.utils.MessageReporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link ContainmentReferenceHandler}.
 *
 * @author Arthur Daussy
 */
public class ContainmentReferenceHandlerTest {

    private MessageReporter msgReporter;

    private ContainmentReferenceHandler refHandler;


    @BeforeEach
    public void beforeEach() {
        this.msgReporter = new MessageReporter();
        this.refHandler = new ContainmentReferenceHandler(this.msgReporter);
    }

    @Test
    public void checkInvalidContainementNode() throws JsonMappingException, JsonProcessingException {
        assertFalse(this.refHandler.isContainmentReference(SysmlFactory.eINSTANCE.createPartDefinition(), "", this.read("")));
    }

    @Test
    public void checkInvalidTypeName() throws JsonMappingException, JsonProcessingException {
        assertNull(this.refHandler.handleContainmentReference(SysmlFactory.eINSTANCE.createPartDefinition(), "", this.read("""
                {
                    "$type" : "InvalidType"
                }
                """)));
        assertThat(this.msgReporter.getReportedMessages()).hasSize(1);
    }

    private JsonNode read(String input) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(input);
    }
}
