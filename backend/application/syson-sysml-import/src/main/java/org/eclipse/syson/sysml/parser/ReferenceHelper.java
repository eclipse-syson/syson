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
package org.eclipse.syson.sysml.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.syson.sysml.AstConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Streams;

/**
 * ReferenceHelper.
 *
 * @author gescande.
 */
public class ReferenceHelper {

    /**
     * List of containing nodes of AST
     */
    private static final List<String> CONTAINMENT_NODES = Arrays.asList(AstConstant.TARGET_CONST, AstConstant.CHILDREN_CONST, AstConstant.HERITAGE_CONST, AstConstant.VALUE_CONST, AstConstant.TARGET_MEMBER_CONST, AstConstant.ASSIGNED_VALUE_CONST, AstConstant.OPERANDS_CONST, AstConstant.EXPRESSION_CONST);

    /**
     * List of non containing nodes of AST
     */
    private static final List<String> NON_CONTAINMENT_NODES = Arrays.asList(AstConstant.TARGET_REF_CONST);

    /**
     * Extract owned nodes.
     * @param astJson json node to parse
     * @return owned nodes of astJson
     */
    public static List<JsonNode>  extractOwnedObject(final JsonNode astJson) {

        final List<JsonNode> allElements = new ArrayList<>();

        CONTAINMENT_NODES.stream().filter(key -> astJson.has(key)).forEach(key -> {
            final JsonNode target = astJson.get(key);
            if (target.isArray()) {
                final var elements = Streams.stream(target.elements()).toList();
                allElements.addAll(elements);
            } else {
                allElements.add(target);
            }
        });

        return allElements;
    }   

    /**
     * Extract not owned nodes.
     * @param astJson json node to parse
     * @return not owned nodes of astJson
     */
    public static Map<String, List<JsonNode>>  extractNotOwnedObjectByAttribute(final JsonNode astJson) {

        final Map<String, List<JsonNode>> allElements = new HashMap<>();

        NON_CONTAINMENT_NODES.stream().filter(key -> astJson.has(key)).forEach(key -> {
            final List<JsonNode> objs = allElements.getOrDefault(key, new ArrayList<JsonNode>());

            final JsonNode target = astJson.get(key);
            if (target.isArray()) {
                final var elements = Streams.stream(target.elements()).toList();
                objs.addAll(elements);
            } else {
                objs.add(astJson.get(key));
            }
            allElements.put(key, objs);
        });

        return allElements;
    }
}
