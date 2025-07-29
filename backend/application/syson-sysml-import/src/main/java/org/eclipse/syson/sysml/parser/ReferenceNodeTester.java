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
package org.eclipse.syson.sysml.parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Predicate;

/**
 * Check if a given {@link JsonNode} represent a reference node in the AST. A reference node in the AST represent
 * "reference" between the parent node an element identified by two fields:
 *
 * <p>
 * <ul>
 * <li>reference : Represents the qualified name of the target (might be null if the reference has not been resolved
 * during AST creation process).</li>
 * <li>text: Represents the textual name used to reference the element if the text file.</li>
 * </ul>
 * </p>
 *
 * @author Arthur Daussy
 */
public class ReferenceNodeTester implements Predicate<JsonNode> {

    @Override
    public boolean test(JsonNode node) {
        return this.hasNullableTextualField(node, "reference") && this.hasNonNullTextualField(node, "text");
    }

    private boolean hasNullableTextualField(JsonNode node, String field) {
        JsonNode nodeField = node.get(field);
        return nodeField != null && this.isTextualOrNull(nodeField);
    }

    private boolean hasNonNullTextualField(JsonNode node, String field) {
        JsonNode nodeField = node.get(field);
        return nodeField != null && nodeField.isTextual();
    }

    private boolean isTextualOrNull(JsonNode node) {
        return node != null && (node.isTextual() || node.isNull());
    }

}
