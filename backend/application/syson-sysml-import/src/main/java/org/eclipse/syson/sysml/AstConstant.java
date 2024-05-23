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

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Defines constants and utility methods for AST node parsing and information extraction.
 *
 * @author gescande
 */
public final class AstConstant {

    public static final String CHILDREN_CONST = "children";

    public static final String TARGET_CONST = "target";

    public static final String TARGET_CHAIN_CONST = "targetChain";

    public static final String TARGET_REF_CONST = "targetRef";

    public static final String HERITAGE_CONST = "heritage";

    public static final String QUOTE_CONST = "\'";

    public static final String TEXT_CONST = "text";

    public static final String REFERENCE_CONST = "reference";

    public static final String META_CONST = "$meta";

    public static final String QUALIFIED_CONST = "qualifiedName";

    public static final String ELEMENT_ID_CONST = "elementId";

    public static final String TYPE_CONST = "$type";

    public static final String VALUE_CONST = "value";

    public static final String LITERAL_CONST = "literal";

    public static final String OPERANDS_CONST = "operands";

    public static final String CLIENT_CONST = "client";

    public static final String SUPPLIER_CONST = "supplier";

    public static final String TYPE_RELATIONSHIPS_CONST = "typeRelationships";

    public static final String RESULT_CONST = "result";

    public static final String SOURCE_CONST = "source";

    public static final String THEN_CONST = "then";

    public static final String ACCEPTER_CONST = "accepter";

    public static final String GUARD_CONST = "guard";

    public static final String EFFECT_CONST = "effect";

    public static final String RECEIVER_CONST = "receiver";

    public static final String PAYLOAD_CONST = "payload";

    public static final String MULTIPLICITY_CONST = "multiplicity";

    public static final String RANGE_CONST = "range";

    public static final String ENDS_CONST = "ends";

    public static final String ABOUT_CONST = "about";

    public static final String SENDER_CONST = "sender";

    public static final String TRANSITION_LINK_SOURCE_CONST = "transitionLinkSource";

    public static final String PREFIXES_CONST = "prefixes";

    public static final String EXPRESSION_CONST = "expression";

    public static final String TARGET_MEMBER_CONST = "targetMember";

    public static final String ASSIGNED_VALUE_CONST = "assignedValue";

    public static final List<String> NODES = Arrays.asList(AstConstant.TARGET_CONST, AstConstant.TARGET_CHAIN_CONST, AstConstant.TARGET_REF_CONST, AstConstant.VALUE_CONST, AstConstant.RESULT_CONST,
            AstConstant.SOURCE_CONST, AstConstant.THEN_CONST, AstConstant.ACCEPTER_CONST, AstConstant.GUARD_CONST, AstConstant.EFFECT_CONST, AstConstant.RECEIVER_CONST, AstConstant.PAYLOAD_CONST,
            AstConstant.MULTIPLICITY_CONST, AstConstant.RANGE_CONST, AstConstant.ABOUT_CONST, AstConstant.SENDER_CONST, AstConstant.TRANSITION_LINK_SOURCE_CONST
    );

    public static String asCleanedText(final JsonNode node) {
        String result = null;

        if (node.isTextual()) {
            result = node.asText().replace(AstConstant.QUOTE_CONST, "");
        }
        if (node.isNumber()) {
            result = node.asText();
        }

        return result;
    }

    public static String getIdentifier(final JsonNode node) {
        String identifier = null;

        if (node.has(AstConstant.META_CONST)) {
            if (node.get(AstConstant.META_CONST).has(AstConstant.ELEMENT_ID_CONST)) {
                identifier = asCleanedText(node.get(META_CONST).get(ELEMENT_ID_CONST));
            } else {
                identifier = getQualifiedName(node);
            }
        }

        return identifier;
    }

    public static String getQualifiedName(final JsonNode node) {
        String identifier = null;

        if (node.has(AstConstant.META_CONST) && node.get(AstConstant.META_CONST).has(AstConstant.QUALIFIED_CONST)) {
            final String qualifiedName = asCleanedText(node.get(META_CONST).get(QUALIFIED_CONST));
            if (!qualifiedName.isEmpty() && !qualifiedName.isBlank() && !qualifiedName.equals("null")) {
                identifier = qualifiedName;
            }
        }

        return identifier;
    }


    public static  String getSearchText(final JsonNode jsonNode) {
        String searchText = null;
        if (jsonNode.has(AstConstant.REFERENCE_CONST) && !jsonNode.get(AstConstant.REFERENCE_CONST).isNull() && !jsonNode.get(AstConstant.REFERENCE_CONST).asText().isBlank()) {
            searchText = AstConstant.asCleanedText(jsonNode.get(AstConstant.REFERENCE_CONST));
        } else if (jsonNode.has(AstConstant.TEXT_CONST) && !jsonNode.get(AstConstant.TEXT_CONST).isNull() && !jsonNode.get(AstConstant.TEXT_CONST).asText().isBlank()) {
            searchText = AstConstant.asCleanedText(jsonNode.get(AstConstant.TEXT_CONST));
        }
        return searchText;
    }
}
