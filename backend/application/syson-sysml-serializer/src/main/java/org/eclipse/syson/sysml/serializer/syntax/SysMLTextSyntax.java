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
package org.eclipse.syson.sysml.serializer.syntax;

import java.util.Map;
import java.util.Objects;

import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;

/**
 * Declaration keyword mappings, string literal quoting and library names that renderers write as fixed text.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SysMLTextSyntax {

    /**
     * Library feature that metadata usages implicitly subset. When the source also writes it explicitly next to a
     * redefinition, the specialization planner keeps it after the redefinition, as the parser recorded it.
     */
    public static final String METADATA_ITEMS = "Metadata::metadataItems";

    /**
     * Declaration keyword per metaclass name. A metaclass missing from this table gets a keyword derived from its name
     * ({@code FooDefinition} gives {@code foo def}, {@code FooUsage} gives {@code foo}), so the table must list every
     * metaclass for which that derivation is wrong: multi-word keywords ({@code use case}), short forms ({@code calc},
     * {@code enum}), control nodes, keywords that differ from the metaclass name ({@code BindingConnectorAsUsage} is
     * {@code binding}), and {@code ReferenceUsage}, which has no keyword. The other entries spell out the common
     * keywords for readability.
     */
    public static final Map<String, String> DECLARATION_KEYWORDS = Map.ofEntries(
            Map.entry("ActionDefinition", SysMLTokens.ACTION_DEFINITION),
            Map.entry("ActionUsage", SysMLTokens.ACTION_KEYWORD),
            Map.entry("AllocationDefinition", SysMLTokens.ALLOCATION_DEFINITION),
            Map.entry("AllocationUsage", SysMLTokens.ALLOCATION_KEYWORD),
            Map.entry("AnalysisCaseDefinition", SysMLTokens.ANALYSIS_DEFINITION),
            Map.entry("AnalysisCaseUsage", SysMLTokens.ANALYSIS_KEYWORD),
            Map.entry("AssertConstraintUsage", SysMLTokens.ASSERT_CONSTRAINT),
            Map.entry("AttributeDefinition", SysMLTokens.ATTRIBUTE_DEFINITION),
            Map.entry("AttributeUsage", SysMLTokens.ATTRIBUTE_KEYWORD),
            Map.entry("BindingConnectorAsUsage", SysMLTokens.BINDING_KEYWORD),
            Map.entry("CalculationDefinition", SysMLTokens.CALC_DEFINITION),
            Map.entry("CalculationUsage", SysMLTokens.CALC_KEYWORD),
            Map.entry("ConcernDefinition", SysMLTokens.CONCERN_DEFINITION),
            Map.entry("ConcernUsage", SysMLTokens.CONCERN_KEYWORD),
            Map.entry("ConnectionDefinition", SysMLTokens.CONNECTION_DEFINITION),
            Map.entry("ConnectionUsage", SysMLTokens.CONNECTION_KEYWORD),
            Map.entry("ConstraintDefinition", SysMLTokens.CONSTRAINT_DEFINITION),
            Map.entry("ConstraintUsage", SysMLTokens.CONSTRAINT_KEYWORD),
            Map.entry("DecisionNode", SysMLTokens.DECIDE_KEYWORD),
            Map.entry("EnumerationDefinition", SysMLTokens.ENUM_DEFINITION),
            Map.entry("EnumerationUsage", SysMLTokens.ENUM_KEYWORD),
            Map.entry("EventOccurrenceUsage", SysMLTokens.EVENT_OCCURRENCE),
            Map.entry("ForkNode", SysMLTokens.FORK_KEYWORD),
            Map.entry("ItemDefinition", SysMLTokens.ITEM_DEFINITION),
            Map.entry("ItemUsage", SysMLTokens.ITEM_KEYWORD),
            Map.entry("JoinNode", SysMLTokens.JOIN_KEYWORD),
            Map.entry("MergeNode", SysMLTokens.MERGE_KEYWORD),
            Map.entry("OccurrenceDefinition", SysMLTokens.OCCURRENCE_DEFINITION),
            Map.entry("OccurrenceUsage", SysMLTokens.OCCURRENCE_KEYWORD),
            Map.entry("PartDefinition", SysMLTokens.PART_DEFINITION),
            Map.entry("PartUsage", SysMLTokens.PART_KEYWORD),
            Map.entry("PortDefinition", SysMLTokens.PORT_DEFINITION),
            Map.entry("PortUsage", SysMLTokens.PORT_KEYWORD),
            Map.entry("ReferenceUsage", ""),
            Map.entry("RenderingDefinition", SysMLTokens.RENDERING_DEFINITION),
            Map.entry("RenderingUsage", SysMLTokens.RENDERING_KEYWORD),
            Map.entry("RequirementDefinition", SysMLTokens.REQUIREMENT_DEFINITION),
            Map.entry("RequirementUsage", SysMLTokens.REQUIREMENT_KEYWORD),
            Map.entry("SatisfyRequirementUsage", SysMLTokens.SATISFY_KEYWORD),
            Map.entry("StateDefinition", SysMLTokens.STATE_DEFINITION),
            Map.entry("StateUsage", SysMLTokens.STATE_KEYWORD),
            Map.entry("SuccessionFlowUsage", SysMLTokens.SUCCESSION_FLOW),
            Map.entry("UseCaseDefinition", SysMLTokens.USE_CASE_DEFINITION),
            Map.entry("UseCaseUsage", SysMLTokens.USE_CASE),
            Map.entry("VerificationCaseDefinition", SysMLTokens.VERIFICATION_DEFINITION),
            Map.entry("VerificationCaseUsage", SysMLTokens.VERIFICATION_KEYWORD),
            Map.entry("ViewDefinition", SysMLTokens.VIEW_DEFINITION),
            Map.entry("ViewUsage", SysMLTokens.VIEW_KEYWORD),
            Map.entry("ViewpointDefinition", SysMLTokens.VIEWPOINT_DEFINITION),
            Map.entry("ViewpointUsage", SysMLTokens.VIEWPOINT_KEYWORD));

    private SysMLTextSyntax() {
    }

    /**
     * Returns the {@code STRING_VALUE} literal that the parser reads back as {@code value}. Backslash, double quote,
     * line feed, carriage return and tab are escaped; every other character, including a single quote, is legal as
     * is. {@code null} gives the empty literal.
     *
     * @param value
     *            the raw string, or {@code null}
     * @return the double-quoted literal
     */
    public static String quote(final String value) {
        final String text = Objects.requireNonNullElse(value, "");
        final StringBuilder literal = new StringBuilder(text.length() + 2).append(SysMLTokens.STRING_LITERAL_DELIMITER);
        // Runs of characters that need no escape are copied in bulk rather than one character at a time.
        int runStart = 0;
        for (int index = 0; index < text.length(); index++) {
            final String escape = escapeSequence(text.charAt(index));
            if (escape != null) {
                literal.append(text, runStart, index).append(escape);
                runStart = index + 1;
            }
        }
        return literal.append(text, runStart, text.length()).append(SysMLTokens.STRING_LITERAL_DELIMITER).toString();
    }

    /** Returns the {@code STRING_VALUE} escape of a character, or {@code null} when it is written as is. */
    private static String escapeSequence(final char character) {
        return switch (character) {
            case '\\' -> "\\\\";
            case '"' -> "\\\"";
            case '\n' -> "\\n";
            case '\r' -> "\\r";
            case '\t' -> "\\t";
            default -> null;
        };
    }
}
