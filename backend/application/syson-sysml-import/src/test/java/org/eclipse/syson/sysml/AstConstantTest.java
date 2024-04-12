/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test of AstConstant class.
 * 
 * @author gescande
 */
public class AstConstantTest {
  
    /**
     * Test getIdentifier
     */
    @Test
    void getIdentifierTest() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        JsonNode metaNode = mapper.createObjectNode();

        ((ObjectNode) rootNode).set("$meta", metaNode);
        ((ObjectNode) metaNode).put("elementId", "Identifier");

        String identifier = AstConstant.getIdentifier(rootNode);

        assertEquals("Identifier", identifier);
    }
  
    /**
     * Test getQualifiedName with simple qualified Name
     */
    @Test
    void getQualifiedNameSimpleTest() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        JsonNode rootMetaNode = mapper.createObjectNode();

        ((ObjectNode) rootNode).set("$meta", rootMetaNode);
        ((ObjectNode) rootMetaNode).put("qualifiedName", "Parent");

        String qualifiedName = AstConstant.getQualifiedName(rootNode);

        assertEquals("Parent", qualifiedName);
    }
  
    /**
     * Test getQualifiedName with complex qualified Name
     */
    @Test
    void getQualifiedNameComplexTest() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        JsonNode rootMetaNode = mapper.createObjectNode();

        ((ObjectNode) rootNode).set("$meta", rootMetaNode);
        ((ObjectNode) rootMetaNode).put("qualifiedName", "Parent::Text Space::Element");

        String qualifiedName = AstConstant.getQualifiedName(rootNode);

        assertEquals("Parent::Text Space::Element", qualifiedName);
    }
}
