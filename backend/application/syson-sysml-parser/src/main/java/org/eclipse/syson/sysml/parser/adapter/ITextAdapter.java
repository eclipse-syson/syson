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
package org.eclipse.syson.sysml.parser.adapter;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

/**
 * An {@link Adapter} to keep track of parsed text for serialization round trip.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ITextAdapter extends Adapter {

    /**
     * <code>":>"</code>.
     */
    KeywordAdapter SPECIALIZES_SYMBOL = new KeywordAdapter();

    /**
     * <code>"specializes"</code>.
     */
    KeywordAdapter SPECIALIZES = new KeywordAdapter();

    /**
     * <code>":>"</code>.
     */
    KeywordAdapter SUBSETS_SYMBOL = new KeywordAdapter();

    /**
     * <code>"subsets"</code>.
     */
    KeywordAdapter SUBSETS = new KeywordAdapter();

    /**
     * <code>"::>"</code>.
     */
    KeywordAdapter REFERENCES_SYMBOL = new KeywordAdapter();

    /**
     * <code>"references"</code>.
     */
    KeywordAdapter REFERENCES = new KeywordAdapter();

    /**
     * <code>"=>"</code>.
     */
    KeywordAdapter CROSSES_SYMBOL = new KeywordAdapter();

    /**
     * <code>"crosses"</code>.
     */
    KeywordAdapter CROSSES = new KeywordAdapter();

    /**
     * <code>":>>"</code>.
     */
    KeywordAdapter REDEFINES_SYMBOL = new KeywordAdapter();

    /**
     * <code>"redefines"</code>.
     */
    KeywordAdapter REDEFINES = new KeywordAdapter();

    /**
     * A feature typing introduced by the verbose <code>"defined by"</code> form instead of <code>":"</code>.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>part dashboard defined by Dashboard;</code></li>
     * <li><code>part nnDashboard redefines dashboard defined by Dashboard;</code></li>
     * </ul>
     */
    KeywordAdapter DEFINED_BY = new KeywordAdapter();

    /**
     * The first feature typing relationship of a typing clause.
     *
     * <p>
     * This distinguishes <code>: A, B : C</code> from <code>: A, B, C</code>.
     * </p>
     */
    KeywordAdapter TYPING_CLAUSE_START = new KeywordAdapter();

    /** The optional {@code state} keyword was explicitly present before a reference. */
    KeywordAdapter EXPLICIT_STATE_USAGE_KEYWORD = new KeywordAdapter();

    /** A declaration used a grammar alternative without its usual keyword. */
    KeywordAdapter DECLARATION_KEYWORD_OMITTED = new KeywordAdapter();

    /** The optional {@code rep} keyword was present. */
    KeywordAdapter EXPLICIT_REP_KEYWORD = new KeywordAdapter();

    /** The optional {@code connect} keyword was present in an interface declaration. */
    KeywordAdapter EXPLICIT_INTERFACE_CONNECT_KEYWORD = new KeywordAdapter();

    /** An optional declaration keyword was explicitly present. */
    KeywordAdapter EXPLICIT_DECLARATION_KEYWORD = new KeywordAdapter();

    /**
     * A block body delimited by braces.
     *
     * <p>
     * Example: <code>part p { attribute a; }</code>
     * </p>
     */
    KeywordAdapter BODY_BLOCK = new KeywordAdapter();

    /**
     * An interface usage whose ends were introduced in the declaration connector form instead of as regular body
     * members.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>interface a to b;</code></li>
     * <li><code>interface i : I connect a to b { flow f from a to b; }</code></li>
     * </ul>
     *
     * <p>
     * Without this marker, a braced interface with two end members, such as
     * <code>interface i : I { end p1; end p2; }</code>, can be serialized back as a regular declaration body rather
     * than the compact connector form.
     * </p>
     */
    KeywordAdapter INTERFACE_CONNECTOR_DECLARATION = new KeywordAdapter();

    /**
     * A flow usage introduced by the <code>"message"</code> keyword.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>message request from client to server;</code></li>
     * <li><code>message :>> setSpeedMessage = vehicle.setSpeedFlow;</code></li>
     * </ul>
     */
    KeywordAdapter MESSAGE_USAGE = new KeywordAdapter();

    /**
     * A feature value whose expression was explicitly parenthesized.
     *
     * <p>
     * Example: <code>:>> elements = (new Translation((1, 0, 0)[frame]));</code>.
     * </p>
     */
    KeywordAdapter PARENTHESIZED_FEATURE_VALUE_EXPRESSION = new KeywordAdapter();

    /**
     * An expression that was explicitly parenthesized in source text.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>(actualVerticalVelocity &lt;= maxVerticalVelocity)</code></li>
     * <li><code>(failureRate * missionTime)</code></li>
     * </ul>
     *
     * <p>
     * The SysML model keeps the expression structure but not every pair of source parentheses. This marker lets the
     * serializer preserve those parentheses where they are meaningful for textual round-trip fidelity.
     * </p>
     */
    KeywordAdapter PARENTHESIZED_EXPRESSION = new KeywordAdapter();

    /** The null expression was written as an empty pair of parentheses. */
    KeywordAdapter EMPTY_PARENTHESIZED_EXPRESSION = new KeywordAdapter();

    /**
     * A lambda invocation whose argument list was explicitly enclosed in parentheses.
     */
    KeywordAdapter PARENTHESIZED_LAMBDA_ARGUMENT = new KeywordAdapter();

    /**
     * An interface connector declaration with multiplicity before its type.
     */
    KeywordAdapter INTERFACE_MULTIPLICITY_BEFORE_TYPE = new KeywordAdapter();

    /** A tuple or argument list ending with an explicit trailing comma. */
    KeywordAdapter TRAILING_COMMA = new KeywordAdapter();

    /**
     * A membership introduced by the action/control <code>"first"</code> keyword.
     *
     * <p>
     * Example: <code>first A3;</code>.
     * </p>
     */
    KeywordAdapter FIRST_MEMBERSHIP = new KeywordAdapter();

    /**
     * A regular comment written with the explicit <code>"comment"</code> keyword.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>comment /* body *&#47;</code></li>
     * <li><code>comment about C locale "en_US" /* body *&#47;</code></li>
     * </ul>
     *
     * <p>
     * Without this marker, shorthand comments such as <code>locale "en_US" /* body *&#47;</code> or bare block comments
     * can be serialized without the keyword.
     * </p>
     */
    KeywordAdapter COMMENT_KEYWORD = new KeywordAdapter();

    /**
     * A feature value introduced by <code>"default"</code>.
     *
     * <p>
     * Example: <code>attribute enabled default true;</code>
     * </p>
     */
    KeywordAdapter FEATURE_VALUE_DEFAULT = new KeywordAdapter();

    /**
     * A feature value introduced by <code>"default ="</code>.
     *
     * <p>
     * Example: <code>attribute enabled default = true;</code>
     * </p>
     */
    KeywordAdapter FEATURE_VALUE_DEFAULT_EQUALS = new KeywordAdapter();

    /**
     * A feature value introduced by <code>"default :="</code>.
     *
     * <p>
     * Example: <code>attribute enabled default := true;</code>
     * </p>
     */
    KeywordAdapter FEATURE_VALUE_DEFAULT_INITIAL = new KeywordAdapter();

    /**
     * An explicit reference prefix.
     *
     * <p>
     * Example: <code>ref part p;</code>
     * </p>
     */
    KeywordAdapter EXPLICIT_REF_PREFIX = new KeywordAdapter();

    /**
     * An end feature whose usage keyword was written explicitly.
     *
     * <p>
     * Example: <code>end port p : PortDef;</code> instead of <code>end p : PortDef;</code>.
     * </p>
     */
    KeywordAdapter END_FEATURE_KEYWORD = new KeywordAdapter();

    /**
     * An explicit public visibility marker.
     *
     * <p>
     * Example: <code>public part p;</code>
     * </p>
     */
    KeywordAdapter EXPLICIT_PUBLIC_VISIBILITY = new KeywordAdapter();

    /**
     * An alias whose target reference used a quoted final name segment.
     *
     * <p>
     * Example: <code>alias CargoBay for 'Magical Cargo Bay';</code>
     * </p>
     */
    KeywordAdapter QUOTED_ALIAS_TARGET = new KeywordAdapter();

    /**
     * An explicit satisfy assertion marker.
     *
     * <p>
     * Example: <code>assert satisfy r by p;</code>
     * </p>
     */
    KeywordAdapter SATISFY_ASSERT = new KeywordAdapter();

    /**
     * A requirement constraint member that used the optional <code>"constraint"</code> usage keyword after
     * <code>"assume"</code> or <code>"require"</code>.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>assume constraint c1 : C;</code></li>
     * <li><code>require #goal constraint payloadMassLimit;</code></li>
     * </ul>
     */
    KeywordAdapter REQUIREMENT_CONSTRAINT_KEYWORD = new KeywordAdapter();

    /**
     * A flow usage whose source end is introduced by <code>"from"</code>.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>flow from source to target;</code></li>
     * <li><code>succession flow from focus.image to shoot.image;</code></li>
     * </ul>
     *
     * <p>
     * Without this marker the compact alternative is used, for example <code>flow source to target;</code>.
     * </p>
     */
    KeywordAdapter FLOW_FROM = new KeywordAdapter();

    /**
     * An individual occurrence definition written with the compact <code>"individual def"</code> form instead of
     * <code>"individual occurrence def"</code>.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>individual def TestSystem;</code></li>
     * <li><code>individual occurrence def TestSystem;</code></li>
     * </ul>
     */
    KeywordAdapter COMPACT_INDIVIDUAL_DEFINITION = new KeywordAdapter();

    /**
     * An individual occurrence usage written with the compact <code>"individual"</code> form instead of
     * <code>"individual occurrence"</code>.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>individual testSystem : TestSystem;</code></li>
     * <li><code>individual occurrence testSystem : TestSystem;</code></li>
     * </ul>
     */
    KeywordAdapter COMPACT_INDIVIDUAL_USAGE = new KeywordAdapter();

    /**
     * A metadata usage introduced by the annotation shorthand <code>"@"</code>.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>@Safety;</code></li>
     * <li><code>metadata Safety;</code></li>
     * </ul>
     */
    KeywordAdapter METADATA_AT_SIGN = new KeywordAdapter();

    /** An anonymous metadata usage with an explicit type separator. */
    KeywordAdapter METADATA_COLON = new KeywordAdapter();

    /** A connection usage with an explicit <code>connection</code> keyword. */
    KeywordAdapter CONNECTION_KEYWORD = new KeywordAdapter();

    /**
     * A metadata usage introduced as prefix metadata with <code>"#"</code>.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>#Security part p;</code></li>
     * <li><code>#service port def ServiceDiscovery;</code></li>
     * </ul>
     */
    KeywordAdapter PREFIX_METADATA = new KeywordAdapter();

    /**
     * A name that was written with unrestricted-name quotes even if the raw name could be rendered as a regular
     * identifier.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>concern 'modularity';</code></li>
     * <li><code>frame 'modularity';</code></li>
     * </ul>
     */
    KeywordAdapter QUOTED_NAME = new KeywordAdapter();

    /**
     * A short name that was written with unrestricted-name quotes even if the raw name could be rendered as a regular
     * identifier.
     *
     * <p>
     * Example: <code>item def &lt;'asset'&gt; Asset;</code>
     * </p>
     */
    KeywordAdapter QUOTED_SHORT_NAME = new KeywordAdapter();

    /**
     * A non-unique feature modifier written after feature specialization rather than before it.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>part p nonunique : P;</code></li>
     * <li><code>occurrence situations : Situation[*] nonunique;</code></li>
     * </ul>
     */
    KeywordAdapter LATE_NONUNIQUE = new KeywordAdapter();

    /** The {@code nonunique} modifier follows {@code ordered}. */
    KeywordAdapter NONUNIQUE_AFTER_ORDERED = new KeywordAdapter();

    /**
     * An enumerated value whose optional <code>"enum"</code> usage keyword was omitted.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>enum def Status { Approved; NotApproved; }</code></li>
     * <li><code>enum def Status { enum Approved; enum NotApproved; }</code></li>
     * </ul>
     */
    KeywordAdapter ENUM_KEYWORD_OMITTED = new KeywordAdapter();

    /**
     * A dependency whose client endpoint is written without the optional <code>"from"</code> keyword.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>dependency source to target;</code></li>
     * <li><code>dependency from source to target;</code></li>
     * </ul>
     */
    KeywordAdapter DEPENDENCY_FROM_OMITTED = new KeywordAdapter();

    /**
     * A transition usage written with the optional <code>"transition"</code> keyword instead of the compact target
     * succession form.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>transition initial then off;</code></li>
     * <li><code>then off;</code></li>
     * </ul>
     */
    KeywordAdapter EXPLICIT_TRANSITION_KEYWORD = new KeywordAdapter();

    /**
     * A transition source written without the optional <code>"first"</code> keyword.
     *
     * <p>
     * Example: <code>transition start then off;</code>.
     * </p>
     */
    KeywordAdapter TRANSITION_FIRST_KEYWORD_OMITTED = new KeywordAdapter();

    /**
     * An action node whose optional <code>"action"</code> keyword was written before the node keyword.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>then action accept engineOff : EngineOff;</code></li>
     * <li><code>then accept sig after 10[SI::s];</code></li>
     * </ul>
     */
    KeywordAdapter EXPLICIT_ACTION_NODE_KEYWORD = new KeywordAdapter();

    /**
     * A target succession introduced by the default <code>"else"</code> branch instead of <code>"then"</code>.
     *
     * <p>
     * Examples:
     * </p>
     * <ul>
     * <li><code>if true then accepted;</code></li>
     * <li><code>else rejected;</code></li>
     * </ul>
     */
    KeywordAdapter DEFAULT_TARGET_SUCCESSION = new KeywordAdapter();

    /**
     * A guarded succession written with the explicit <code>"succession"</code> keyword instead of
     * <code>"transition"</code>.
     *
     * <p>
     * Example: <code>succession S first A if x == 0 then B;</code>.
     * </p>
     */
    KeywordAdapter SUCCESSION_KEYWORD = new KeywordAdapter();

    /** A return parameter whose implied out direction was not written in the source. */
    KeywordAdapter RETURN_DIRECTION_OMITTED = new KeywordAdapter();

    @Override
    Notifier getTarget();

}
