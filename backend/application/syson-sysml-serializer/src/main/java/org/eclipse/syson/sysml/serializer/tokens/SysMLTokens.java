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
package org.eclipse.syson.sysml.serializer.tokens;

/**
 * SysML grammar tokens shared by text rendering and syntax recognition.
 * <p>
 * Renderers write grammar text only through these constants, never through string literals, so that a keyword or
 * operator spelling is defined once, can be searched for, and cannot be mistyped in one renderer only. Several
 * constants share a value but not a meaning ({@code *} is multiplication, wildcard, infinity and the block comment
 * margin); use the one that names the grammar role. Values are compile-time constants so they can also be used in
 * switch labels. This vocabulary has no serializer dependencies.
 * </p>
 * <p>
 * Naming conventions:
 * </p>
 * <ul>
 * <li>{@code *_KEYWORD}, {@code *_LITERAL}, {@code *_OPERATOR}: one grammar token, without surrounding space;</li>
 * <li>{@code *_PREFIX}: the token followed by one space, for text that continues with an operand
 * ({@code "then "});</li>
 * <li>{@code *_INFIX}: the token surrounded by single spaces, placed between two operands ({@code " about "});</li>
 * <li>{@code *_SUFFIX}: the token preceded by one space, appended after an operand ({@code " def"});</li>
 * <li>{@code *_DEFINITION} and other multi-word names: complete keyword sequences such as {@code "part def"};</li>
 * <li>{@code *_FEATURE_NAME}: a library feature that the notation names with an ordinary identifier.</li>
 * </ul>
 * <p>
 * Keywords spelled by a metamodel enumeration literal (visibility {@code public}, direction {@code inout}, portion
 * kind {@code snapshot}/{@code timeslice}, trigger kind {@code at}/{@code after}, state subaction kind
 * {@code entry}/{@code exit}) are written from that literal and have no constant here. Reserved words for name
 * quoting come from the generated lexer, not from this class.
 * </p>
 * <p>
 * Space-bearing variants exist for code that builds syntax as a string (expressions, compact bodies); code writing
 * to a {@code TextOutput} should prefer the bare token and let the output own spacing. Add a constant when a new
 * grammar token or a recurring token sequence is needed, in the matching group, keeping the group alphabetical.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SysMLTokens {

    // Whitespace: literal characters whose exact value matters (string building, line splitting). Formatted
    // output should use the TextOutput layout methods and FormattingOptions instead.

    public static final String CARRIAGE_RETURN = "\r";

    public static final String LINE_FEED = "\n";

    public static final String SPACE = " ";

    public static final String TAB = "\t";

    // Keywords and literals: single reserved words of the SysML v2 textual notation.

    public static final String ABOUT_KEYWORD = "about";

    public static final String ABSTRACT_KEYWORD = "abstract";

    public static final String ACCEPT_KEYWORD = "accept";

    public static final String ACTION_KEYWORD = "action";

    public static final String ACTOR_KEYWORD = "actor";

    public static final String ALIAS_KEYWORD = "alias";

    public static final String ALLOCATE_KEYWORD = "allocate";

    public static final String ALLOCATION_KEYWORD = "allocation";

    public static final String ALL_KEYWORD = "all";

    public static final String ANALYSIS_KEYWORD = "analysis";

    public static final String ASSERT_KEYWORD = "assert";

    public static final String ASSIGN_KEYWORD = "assign";

    public static final String ASSUME_KEYWORD = "assume";

    public static final String AS_KEYWORD = "as";

    public static final String ATTRIBUTE_KEYWORD = "attribute";

    public static final String BINDING_KEYWORD = "binding";

    public static final String BIND_KEYWORD = "bind";

    public static final String BY_KEYWORD = "by";

    public static final String CALC_KEYWORD = "calc";

    public static final String CASE_KEYWORD = "case";

    public static final String COMMENT_KEYWORD = "comment";

    public static final String CONCERN_KEYWORD = "concern";

    public static final String CONNECTION_KEYWORD = "connection";

    public static final String CONNECT_KEYWORD = "connect";

    public static final String CONSTANT_KEYWORD = "constant";

    public static final String CONSTRAINT_KEYWORD = "constraint";

    public static final String CROSSES_KEYWORD = "crosses";

    public static final String DECIDE_KEYWORD = "decide";

    public static final String DEFAULT_KEYWORD = "default";

    public static final String DEFINED_KEYWORD = "defined";

    public static final String DEF_KEYWORD = "def";

    public static final String DEPENDENCY_KEYWORD = "dependency";

    public static final String DERIVED_KEYWORD = "derived";

    public static final String DOC_KEYWORD = "doc";

    public static final String DO_KEYWORD = "do";

    public static final String ELSE_KEYWORD = "else";

    public static final String END_KEYWORD = "end";

    public static final String ENUM_KEYWORD = "enum";

    public static final String EVENT_KEYWORD = "event";

    public static final String EXHIBIT_KEYWORD = "exhibit";

    public static final String EXPOSE_KEYWORD = "expose";

    public static final String FALSE_LITERAL = "false";

    public static final String FILTER_KEYWORD = "filter";

    public static final String FIRST_KEYWORD = "first";

    public static final String FLOW_KEYWORD = "flow";

    public static final String FORK_KEYWORD = "fork";

    public static final String FOR_KEYWORD = "for";

    public static final String FRAME_KEYWORD = "frame";

    public static final String FROM_KEYWORD = "from";

    public static final String IF_KEYWORD = "if";

    public static final String IMPORT_KEYWORD = "import";

    public static final String INCLUDE_KEYWORD = "include";

    public static final String INDIVIDUAL_KEYWORD = "individual";

    /** The unbounded upper bound of a multiplicity, as in {@code [0..*]}. */
    public static final String INFINITY_LITERAL = "*";

    public static final String INTERFACE_KEYWORD = "interface";

    public static final String IN_KEYWORD = "in";

    public static final String ITEM_KEYWORD = "item";

    public static final String JOIN_KEYWORD = "join";

    public static final String LANGUAGE_KEYWORD = "language";

    public static final String LIBRARY_KEYWORD = "library";

    public static final String LOCALE_KEYWORD = "locale";

    public static final String LOOP_KEYWORD = "loop";

    public static final String MERGE_KEYWORD = "merge";

    public static final String MESSAGE_KEYWORD = "message";

    public static final String METADATA_KEYWORD = "metadata";

    public static final String NEW_KEYWORD = "new";

    public static final String NONUNIQUE_KEYWORD = "nonunique";

    public static final String NULL_LITERAL = "null";

    public static final String OBJECTIVE_KEYWORD = "objective";

    public static final String OCCURRENCE_KEYWORD = "occurrence";

    public static final String OF_KEYWORD = "of";

    public static final String ORDERED_KEYWORD = "ordered";

    public static final String OUT_KEYWORD = "out";

    public static final String PACKAGE_KEYWORD = "package";

    public static final String PARALLEL_KEYWORD = "parallel";

    public static final String PART_KEYWORD = "part";

    public static final String PERFORM_KEYWORD = "perform";

    public static final String PORT_KEYWORD = "port";

    public static final String PRIVATE_KEYWORD = "private";

    public static final String PROTECTED_KEYWORD = "protected";

    public static final String REDEFINES_KEYWORD = "redefines";

    public static final String REFERENCES_KEYWORD = "references";

    public static final String REF_KEYWORD = "ref";

    public static final String RENDERING_KEYWORD = "rendering";

    public static final String RENDER_KEYWORD = "render";

    public static final String REP_KEYWORD = "rep";

    public static final String REQUIREMENT_KEYWORD = "requirement";

    public static final String REQUIRE_KEYWORD = "require";

    public static final String RETURN_KEYWORD = "return";

    public static final String SATISFY_KEYWORD = "satisfy";

    public static final String SEND_KEYWORD = "send";

    public static final String SPECIALIZES_KEYWORD = "specializes";

    public static final String STAKEHOLDER_KEYWORD = "stakeholder";

    public static final String STANDARD_KEYWORD = "standard";

    public static final String STATE_KEYWORD = "state";

    public static final String SUBJECT_KEYWORD = "subject";

    public static final String SUBSETS_KEYWORD = "subsets";

    public static final String SUCCESSION_KEYWORD = "succession";

    public static final String TERMINATE_KEYWORD = "terminate";

    public static final String THEN_KEYWORD = "then";

    public static final String TO_KEYWORD = "to";

    public static final String TRANSITION_KEYWORD = "transition";

    public static final String TRUE_LITERAL = "true";

    public static final String UNTIL_KEYWORD = "until";

    public static final String USE_KEYWORD = "use";

    public static final String VARIANT_KEYWORD = "variant";

    public static final String VARIATION_KEYWORD = "variation";

    public static final String VERIFICATION_KEYWORD = "verification";

    public static final String VERIFY_KEYWORD = "verify";

    public static final String VIA_KEYWORD = "via";

    public static final String VIEWPOINT_KEYWORD = "viewpoint";

    public static final String VIEW_KEYWORD = "view";

    public static final String WHEN_KEYWORD = "when";

    public static final String WHILE_KEYWORD = "while";

    // Library feature names: ordinary identifiers, not reserved words, that the notation gives a fixed meaning.

    /** The {@code done} snapshot of an action, target of a succession to the action's end. */
    public static final String DONE_FEATURE_NAME = "done";

    /** The {@code start} snapshot of an action, as in {@code first start;}. */
    public static final String START_FEATURE_NAME = "start";

    // Operators and delimiters: single symbolic tokens, plus the word operators of the expression grammar.

    /** The {@code **} spelling of exponentiation, equivalent to {@link #POWER_OPERATOR}. */
    public static final String ALTERNATE_POWER_OPERATOR = "**";

    /** Non-short-circuit and; the word {@code and} is the conditional form ({@link #AND_OPERATOR}). */
    public static final String AMPERSAND_OPERATOR = "&";

    public static final String AND_OPERATOR = "and";

    public static final String ASSIGNMENT_OPERATOR = ":=";

    /** Left margin of each continuation line of a multi-line block comment. */
    public static final String BLOCK_COMMENT_ASTERISK = "*";

    public static final String BLOCK_COMMENT_END = "*/";

    public static final String BLOCK_COMMENT_START = "/*";

    /** The {@code @} spelling of the classification test: {@code x @ Type}, or {@code @Type} on self. */
    public static final String CLASSIFICATION_TEST_OPERATOR = "@";

    public static final String CLOSE_BRACE = "}";

    public static final String CLOSE_BRACKET = "]";

    public static final String CLOSE_PARENTHESIS = ")";

    public static final String COLLECT_OPERATOR = "collect";

    public static final String COMMA = ",";

    /** The {@code ?} of {@code if c ? a else b}; also part of the {@code .?{ ... }} select body. */
    public static final String CONDITIONAL_SEPARATOR = "?";

    /**
     * The conjugation prefix of <code>&#126;PortDef</code>. Written as a unicode escape because the SysON Checkstyle
     * configuration only accepts a restricted character set in sources; use this constant instead of the literal.
     */
    public static final String CONJUGATION_OPERATOR = "\u007E";

    /** Symbol of a cross subsetting ({@code crosses}) on an owned cross feature. */
    public static final String CROSSING_OPERATOR = "=>";

    public static final String DIVISION_OPERATOR = "/";

    public static final String EQUALITY_OPERATOR = "==";

    public static final String EQUALS_OPERATOR = "=";

    public static final String GREATER_OR_EQUAL_OPERATOR = ">=";

    public static final String GREATER_THAN_OPERATOR = ">";

    public static final String HAS_TYPE_OPERATOR = "hastype";

    public static final String IDENTITY_OPERATOR = "===";

    public static final String IMPLIES_OPERATOR = "implies";

    /** Sequence indexing {@code a#(i)}; same character as {@link #METADATA_KEYWORD_PREFIX}. */
    public static final String INDEX_OPERATOR = "#";

    public static final String INEQUALITY_OPERATOR = "!=";

    public static final String IS_TYPE_OPERATOR = "istype";

    public static final String LESS_OR_EQUAL_OPERATOR = "<=";

    public static final String LESS_THAN_OPERATOR = "<";

    public static final String LINE_COMMENT_PREFIX = "//";

    /** Meta-classification test on a metadata reference: {@code x @@ Type}. */
    public static final String METACLASS_OPERATOR = "@@";

    /** Introduces a user-defined keyword (prefix metadata): {@code #Safety part p;}. */
    public static final String METADATA_KEYWORD_PREFIX = "#";

    /** Introduces a metadata usage member: {@code @Safety;}. */
    public static final String METADATA_USAGE_PREFIX = "@";

    public static final String META_OPERATOR = "meta";

    public static final String MINUS_OPERATOR = "-";

    public static final String MULTIPLICATION_OPERATOR = "*";

    public static final String NAMESPACE_SEPARATOR = "::";

    public static final String NAVIGATION_OPERATOR = "->";

    public static final String NON_IDENTITY_OPERATOR = "!==";

    public static final String NOT_OPERATOR = "not";

    public static final String NULL_COALESCING_OPERATOR = "??";

    public static final String OPEN_BRACE = "{";

    public static final String OPEN_BRACKET = "[";

    public static final String OPEN_PARENTHESIS = "(";

    public static final String OR_OPERATOR = "or";

    public static final String PATH_SEPARATOR = ".";

    /** Non-short-circuit or; the word {@code or} is the conditional form ({@link #OR_OPERATOR}). */
    public static final String PIPE_OPERATOR = "|";

    public static final String PLUS_OPERATOR = "+";

    public static final String POWER_OPERATOR = "^";

    public static final String RANGE_OPERATOR = "..";

    /** The {@code **} of a recursive import, as in {@code import P::**;}. */
    public static final String RECURSIVE_WILDCARD = "**";

    public static final String REDEFINITION_OPERATOR = ":>>";

    public static final String REFERENCE_SUBSETTING_OPERATOR = "::>";

    public static final String REMAINDER_OPERATOR = "%";

    /** Leading segment of an absolute qualified name ({@code $::P::x}), designating the root namespace. */
    public static final String ROOT_NAMESPACE_MARKER = "$";

    public static final String SELECT_OPERATOR = "select";

    public static final String SHORT_NAME_END = ">";

    /** Opening delimiter of a short name, as in {@code part <p1> engine;}. */
    public static final String SHORT_NAME_START = "<";

    public static final String STATEMENT_TERMINATOR = ";";

    public static final String STRING_LITERAL_DELIMITER = "\"";

    public static final String SUBSETTING_OPERATOR = ":>";

    public static final String TYPE_SEPARATOR = ":";

    /** Quote around a name that is not a basic identifier, as in {@code 'my part'}. */
    public static final String UNRESTRICTED_NAME_DELIMITER = "'";

    public static final String WILDCARD_SYMBOL = "*";

    public static final String XOR_OPERATOR = "xor";

    // Composite syntax: fixed token sequences, built from the tokens above so that a spelling change propagates.

    public static final String ABOUT_INFIX = SPACE + ABOUT_KEYWORD + SPACE;

    public static final String ACCEPT_PREFIX = ACCEPT_KEYWORD + SPACE;

    public static final String ACTION_DEFINITION = ACTION_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String ACTION_PREFIX = ACTION_KEYWORD + SPACE;

    public static final String ALLOCATION_DEFINITION = ALLOCATION_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String ALL_PREFIX = ALL_KEYWORD + SPACE;

    public static final String ANALYSIS_DEFINITION = ANALYSIS_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String ARGUMENT_SEPARATOR = COMMA + SPACE;

    public static final String ASSERT_CONSTRAINT = ASSERT_KEYWORD + SPACE + CONSTRAINT_KEYWORD;

    public static final String ASSERT_NOT = ASSERT_KEYWORD + SPACE + NOT_OPERATOR;

    public static final String ASSERT_NOT_CONSTRAINT = ASSERT_NOT + SPACE + CONSTRAINT_KEYWORD;

    public static final String ASSIGNMENT_INFIX = SPACE + ASSIGNMENT_OPERATOR + SPACE;

    public static final String ASSIGN_PREFIX = ASSIGN_KEYWORD + SPACE;

    public static final String AS_INFIX = SPACE + AS_KEYWORD + SPACE;

    public static final String AS_PREFIX = AS_KEYWORD + SPACE;

    public static final String ATTRIBUTE_DEFINITION = ATTRIBUTE_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String ATTRIBUTE_PREFIX = ATTRIBUTE_KEYWORD + SPACE;

    public static final String CALC_DEFINITION = CALC_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String CLASSIFICATION_TEST_INFIX = SPACE + CLASSIFICATION_TEST_OPERATOR + SPACE;

    public static final String CLOSE_BRACE_SUFFIX = SPACE + CLOSE_BRACE;

    /** Opens a collect body {@code x.{ ... }}. */
    public static final String COLLECTION_BODY_START = PATH_SEPARATOR + OPEN_BRACE;

    public static final String CONCERN_DEFINITION = CONCERN_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String CONDITIONAL_SEPARATOR_INFIX = SPACE + CONDITIONAL_SEPARATOR + SPACE;

    public static final String CONNECTION_DEFINITION = CONNECTION_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String CONSTRAINT_DEFINITION = CONSTRAINT_KEYWORD + SPACE + DEF_KEYWORD;

    /** Default initial-value form {@code default :=} of a feature value. */
    public static final String DEFAULT_ASSIGNMENT = DEFAULT_KEYWORD + SPACE + ASSIGNMENT_OPERATOR;

    /** Default bound-value form {@code default =} of a feature value. */
    public static final String DEFAULT_EQUALS = DEFAULT_KEYWORD + SPACE + EQUALS_OPERATOR;

    public static final String DEFINED_BY = DEFINED_KEYWORD + SPACE + BY_KEYWORD;

    public static final String DEFINED_BY_INFIX = SPACE + DEFINED_BY + SPACE;

    /** Turns a usage keyword into a definition keyword, e.g. {@code part} into {@code part def}. */
    public static final String DEF_SUFFIX = SPACE + DEF_KEYWORD;

    /** Recursive import tail {@code ::**}. */
    public static final String DESCENDANT_IMPORT_SUFFIX = NAMESPACE_SEPARATOR + RECURSIVE_WILDCARD;

    public static final String ELSE_INFIX = SPACE + ELSE_KEYWORD + SPACE;

    public static final String EMPTY_ARGUMENT_LIST = OPEN_PARENTHESIS + CLOSE_PARENTHESIS;

    public static final String ENUM_DEFINITION = ENUM_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String EQUALS_INFIX = SPACE + EQUALS_OPERATOR + SPACE;

    public static final String EVENT_OCCURRENCE = EVENT_KEYWORD + SPACE + OCCURRENCE_KEYWORD;

    /** Opens a select body {@code x.?{ ... }}. */
    public static final String FILTER_BODY_START = PATH_SEPARATOR + CONDITIONAL_SEPARATOR + OPEN_BRACE;

    public static final String FIRST_PREFIX = FIRST_KEYWORD + SPACE;

    /** The {@code first start} succession source, designating the start of the enclosing action. */
    public static final String FIRST_START = FIRST_KEYWORD + SPACE + START_FEATURE_NAME;

    public static final String IF_PREFIX = IF_KEYWORD + SPACE;

    public static final String INDEX_START = INDEX_OPERATOR + OPEN_PARENTHESIS;

    public static final String IN_PREFIX = IN_KEYWORD + SPACE;

    public static final String IS_TYPE_INFIX = SPACE + IS_TYPE_OPERATOR + SPACE;

    public static final String IS_TYPE_PREFIX = IS_TYPE_OPERATOR + SPACE;

    public static final String ITEM_DEFINITION = ITEM_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String LOCALE_INFIX = SPACE + LOCALE_KEYWORD + SPACE;

    public static final String LOCALE_PREFIX = LOCALE_KEYWORD + SPACE;

    /** Namespace import tail {@code ::*}. */
    public static final String MEMBER_IMPORT_SUFFIX = NAMESPACE_SEPARATOR + WILDCARD_SYMBOL;

    public static final String METACLASS_INFIX = SPACE + METACLASS_OPERATOR + SPACE;

    public static final String META_INFIX = SPACE + META_OPERATOR + SPACE;

    public static final String NEW_PREFIX = NEW_KEYWORD + SPACE;

    public static final String OCCURRENCE_DEFINITION = OCCURRENCE_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String OPEN_BRACE_INFIX = SPACE + OPEN_BRACE + SPACE;

    public static final String OPEN_BRACE_PREFIX = OPEN_BRACE + SPACE;

    public static final String PART_DEFINITION = PART_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String PORT_DEFINITION = PORT_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String PRIVATE_PREFIX = PRIVATE_KEYWORD + SPACE;

    /** Recursive namespace import tail {@code ::*::**}. */
    public static final String RECURSIVE_MEMBER_IMPORT_SUFFIX = MEMBER_IMPORT_SUFFIX + DESCENDANT_IMPORT_SUFFIX;

    public static final String REDEFINES_PREFIX = REDEFINES_KEYWORD + SPACE;

    public static final String REDEFINITION_PREFIX = REDEFINITION_OPERATOR + SPACE;

    public static final String REFERENCE_SUBSETTING_INFIX = SPACE + REFERENCE_SUBSETTING_OPERATOR + SPACE;

    public static final String REF_PREFIX = REF_KEYWORD + SPACE;

    public static final String RENDERING_DEFINITION = RENDERING_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String REQUIREMENT_DEFINITION = REQUIREMENT_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String SEND_PREFIX = SEND_KEYWORD + SPACE;

    /** Space then {@code <}, appended after a keyword before a short name. */
    public static final String SHORT_NAME_START_SUFFIX = SPACE + SHORT_NAME_START;

    /** Separates statements written on one line inside an inline expression body. */
    public static final String STATEMENT_SEPARATOR = STATEMENT_TERMINATOR + SPACE;

    public static final String STATE_DEFINITION = STATE_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String SUBSETS_INFIX = SPACE + SUBSETS_KEYWORD + SPACE;

    public static final String SUBSETTING_INFIX = SPACE + SUBSETTING_OPERATOR + SPACE;

    public static final String SUCCESSION_FLOW = SUCCESSION_KEYWORD + SPACE + FLOW_KEYWORD;

    public static final String TERMINATE_PREFIX = TERMINATE_KEYWORD + SPACE;

    /** A complete {@code terminate;} action without a target. */
    public static final String TERMINATE_STATEMENT = TERMINATE_KEYWORD + STATEMENT_TERMINATOR;

    public static final String THEN_PREFIX = THEN_KEYWORD + SPACE;

    public static final String TO_INFIX = SPACE + TO_KEYWORD + SPACE;

    public static final String TYPE_SEPARATOR_INFIX = SPACE + TYPE_SEPARATOR + SPACE;

    public static final String TYPE_SEPARATOR_PREFIX = TYPE_SEPARATOR + SPACE;

    public static final String UNBOUNDED_MULTIPLICITY = OPEN_BRACKET + INFINITY_LITERAL + CLOSE_BRACKET;

    /** The {@code [1]} multiplicity, recognized where it is the implied default. */
    public static final String UNIT_MULTIPLICITY = OPEN_BRACKET + "1" + CLOSE_BRACKET;

    public static final String USE_CASE = USE_KEYWORD + SPACE + CASE_KEYWORD;

    public static final String USE_CASE_DEFINITION = USE_KEYWORD + SPACE + CASE_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String VERIFICATION_DEFINITION = VERIFICATION_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String VIA_INFIX = SPACE + VIA_KEYWORD + SPACE;

    public static final String VIEWPOINT_DEFINITION = VIEWPOINT_KEYWORD + SPACE + DEF_KEYWORD;

    public static final String VIEW_DEFINITION = VIEW_KEYWORD + SPACE + DEF_KEYWORD;

    private SysMLTokens() {
        // Prevent instantiation.
    }
}
