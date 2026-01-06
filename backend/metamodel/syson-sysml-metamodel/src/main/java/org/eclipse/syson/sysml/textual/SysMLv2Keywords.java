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
package org.eclipse.syson.sysml.textual;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Gather all keywords reserved by the SysML V2 textual language.
 *
 * @author Arthur Daussy
 */
public final class SysMLv2Keywords {

    // Symbols / Operators / Punctuation
    public static final String LESS_THAN = "<";
    public static final String GREATER_THAN = ">";
    public static final String SEMICOLON = ";";

    public static final String LEFT_BRACE = "{";
    public static final String RIGHT_BRACE = "}";

    public static final String COMMA = ",";
    public static final String DOT = ".";

    public static final String LEFT_PAREN = "(";
    public static final String RIGHT_PAREN = ")";

    public static final String LEFT_BRACKET = "[";
    public static final String RIGHT_BRACKET = "]";

    public static final String AT_SIGN = "@";
    public static final String HASH = "#";
    public static final String TILDE = "\u007E";

    public static final String COLON = ":";
    public static final String DOUBLE_COLON = "::";

    public static final String STAR = "*";
    public static final String DOUBLE_STAR = "**";

    public static final String RANGE = "..";
    public static final String OPTIONAL_NAVIGATION = ".?";

    public static final String ARROW = "->";
    public static final String IMPLIES = "=>";

    public static final String EQUALS = "=";
    public static final String ASSIGNMENT = ":=";

    // SysML/DSL-specific operators (based on your tokens)
    public static final String SPECIALIZES_OPERATOR = ":>";
    public static final String REDEFINES_OPERATOR = ":>>";
    public static final String DOUBLE_COLON_SPECIALIZES = "::>";

    // Word tokens / Keywords
    public static final String DEPENDENCY = "dependency";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String COMMENT = "comment";
    public static final String ABOUT = "about";
    public static final String LOCALE = "locale";
    public static final String DOC = "doc";
    public static final String REP = "rep";
    public static final String LANGUAGE = "language";
    public static final String METADATA = "metadata";
    public static final String DEF = "def";
    public static final String ABSTRACT = "abstract";
    public static final String REF = "ref";
    public static final String REDEFINES = "redefines";
    public static final String STANDARD = "standard";
    public static final String LIBRARY = "library";
    public static final String PACKAGE = "package";
    public static final String FILTER = "filter";
    public static final String ALIAS = "alias";
    public static final String FOR = "for";
    public static final String IMPORT = "import";
    public static final String ALL = "all";

    public static final String PUBLIC = "public";
    public static final String PRIVATE = "private";
    public static final String PROTECTED = "protected";

    public static final String SPECIALIZES = "specializes";
    public static final String ORDERED = "ordered";
    public static final String NONUNIQUE = "nonunique";
    public static final String DEFINED = "defined";
    public static final String BY = "by";
    public static final String SUBSETS = "subsets";
    public static final String REFERENCES = "references";
    public static final String CROSSES = "crosses";
    public static final String VARIATION = "variation";
    public static final String VARIANT = "variant";

    public static final String IN = "in";
    public static final String OUT = "out";
    public static final String INOUT = "inout";

    public static final String DERIVED = "derived";
    public static final String CONSTANT = "constant";
    public static final String END = "end";
    public static final String DEFAULT = "default";

    public static final String ATTRIBUTE = "attribute";
    public static final String ENUM = "enum";

    public static final String OCCURRENCE = "occurrence";
    public static final String INDIVIDUAL = "individual";
    public static final String SNAPSHOT = "snapshot";
    public static final String TIMESLICE = "timeslice";
    public static final String EVENT = "event";
    public static final String THEN = "then";
    public static final String ITEM = "item";
    public static final String PART = "part";
    public static final String PORT = "port";

    public static final String BINDING = "binding";
    public static final String BIND = "bind";
    public static final String SUCCESSION = "succession";
    public static final String FIRST = "first";

    public static final String CONNECTION = "connection";
    public static final String CONNECT = "connect";
    public static final String INTERFACE = "interface";

    public static final String ALLOCATION = "allocation";
    public static final String ALLOCATE = "allocate";
    public static final String FLOW = "flow";
    public static final String MESSAGE = "message";
    public static final String OF = "of";

    public static final String ACTION = "action";
    public static final String PERFORM = "perform";
    public static final String ACCEPT = "accept";
    public static final String VIA = "via";
    public static final String AT = "at";
    public static final String AFTER = "after";
    public static final String WHEN = "when";
    public static final String SEND = "send";
    public static final String ASSIGN = "assign";

    public static final String IF = "if";
    public static final String ELSE = "else";
    public static final String WHILE = "while";
    public static final String LOOP = "loop";
    public static final String UNTIL = "until";
    public static final String TERMINATE = "terminate";
    public static final String MERGE = "merge";
    public static final String DECIDE = "decide";
    public static final String JOIN = "join";
    public static final String FORK = "fork";

    public static final String STATE = "state";
    public static final String PARALLEL = "parallel";
    public static final String ENTRY = "entry";
    public static final String DO = "do";
    public static final String EXIT = "exit";
    public static final String EXHIBIT = "exhibit";
    public static final String TRANSITION = "transition";
    public static final String CALC = "calc";
    public static final String RETURN = "return";

    public static final String CONSTRAINT = "constraint";
    public static final String ASSERT = "assert";
    public static final String NOT = "not";

    public static final String REQUIREMENT = "requirement";
    public static final String SUBJECT = "subject";
    public static final String ASSUME = "assume";
    public static final String REQUIRE = "require";
    public static final String FRAME = "frame";

    public static final String ACTOR = "actor";
    public static final String STAKEHOLDER = "stakeholder";
    public static final String SATISFY = "satisfy";
    public static final String CONCERN = "concern";
    public static final String CASE = "case";
    public static final String OBJECTIVE = "objective";
    public static final String ANALYSIS = "analysis";
    public static final String VERIFICATION = "verification";
    public static final String VERIFY = "verify";
    public static final String USE = "use";
    public static final String INCLUDE = "include";
    public static final String VIEW = "view";
    public static final String RENDER = "render";
    public static final String RENDERING = "rendering";
    public static final String EXPOSE = "expose";
    public static final String VIEWPOINT = "viewpoint";

    /** All word-based DSL keywords. */
    public static final Set<String> KEYWORDS;

    /** All operators / symbols / punctuation. */
    public static final Set<String> OPERATORS;

    /** Union of KEYWORDS and OPERATORS. */
    public static final Set<String> ALL_TOKENS;

    static {
        KEYWORDS = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(
                DEPENDENCY, FROM, TO, COMMENT, ABOUT, LOCALE, DOC, REP, LANGUAGE, METADATA, DEF, ABSTRACT, REF,
                REDEFINES, STANDARD, LIBRARY, PACKAGE, FILTER, ALIAS, FOR, IMPORT, ALL,
                PUBLIC, PRIVATE, PROTECTED,
                SPECIALIZES, ORDERED, NONUNIQUE, DEFINED, BY, SUBSETS, REFERENCES, CROSSES,
                VARIATION, VARIANT,
                IN, OUT, INOUT,
                DERIVED, CONSTANT, END, DEFAULT,
                ATTRIBUTE, ENUM,
                OCCURRENCE, INDIVIDUAL, SNAPSHOT, TIMESLICE, EVENT, THEN, ITEM, PART, PORT,
                BINDING, BIND, SUCCESSION, FIRST,
                CONNECTION, CONNECT, INTERFACE,
                ALLOCATION, ALLOCATE, FLOW, MESSAGE, OF,
                ACTION, PERFORM, ACCEPT, VIA, AT, AFTER, WHEN, SEND, ASSIGN,
                IF, ELSE, WHILE, LOOP, UNTIL, TERMINATE, MERGE, DECIDE, JOIN, FORK,
                STATE, PARALLEL, ENTRY, DO, EXIT, EXHIBIT, TRANSITION, CALC, RETURN,
                CONSTRAINT, ASSERT, NOT,
                REQUIREMENT, SUBJECT, ASSUME, REQUIRE, FRAME,
                ACTOR, STAKEHOLDER, SATISFY, CONCERN, CASE, OBJECTIVE, ANALYSIS,
                VERIFICATION, VERIFY,
                USE, INCLUDE, VIEW, RENDER, RENDERING, EXPOSE, VIEWPOINT
        )));

        OPERATORS = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(
                LESS_THAN, GREATER_THAN, SEMICOLON,
                LEFT_BRACE, RIGHT_BRACE,
                COMMA, DOT,
                LEFT_PAREN, RIGHT_PAREN,
                LEFT_BRACKET, RIGHT_BRACKET,
                AT_SIGN, HASH, TILDE,
                COLON, DOUBLE_COLON,
                STAR, DOUBLE_STAR,
                RANGE, OPTIONAL_NAVIGATION,
                ARROW, IMPLIES,
                EQUALS, ASSIGNMENT,
                SPECIALIZES_OPERATOR, REDEFINES_OPERATOR, DOUBLE_COLON_SPECIALIZES
        )));

        LinkedHashSet<String> all = new LinkedHashSet<>(KEYWORDS.size() + OPERATORS.size());
        all.addAll(KEYWORDS);
        all.addAll(OPERATORS);
        ALL_TOKENS = Collections.unmodifiableSet(all);
    }

    private SysMLv2Keywords() {
        // Utility class
    }
}
