// Generated from DirectEdit.g4 by ANTLR 4.10.1

/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.services.grammars;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DirectEditParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ESPACED_NAME=1, WS=2, LBRACKET=3, RBRACKET=4, LPAREN=5, RPAREN=6, LT=7, 
		GT=8, COMMA=9, DOT=10, COLON=11, EQUALS=12, PLUS=13, MINUS=14, STAR=15, 
		DIV=16, MOD=17, PIPE=18, AMP=19, SLASH=20, SINGLE_QUOTE=21, DOTDOT=22, 
		NAMESPACE_SEP=23, SUBSETS_OP=24, REDEFINES_OP=25, ASSIGN_OP=26, POWER=27, 
		EQ=28, NEQ=29, EQ_STRICT=30, NEQ_STRICT=31, LTE=32, GTE=33, Boolean=34, 
		Integer=35, Real=36, DoubleQuotedString=37, ABOUT=38, ABSTRACT=39, ABSTRACT_PREFIX=40, 
		ALIAS=41, ALL=42, AND=43, AS=44, ASSIGN=45, ASSOC=46, BEAHVIOR=47, BINDING=48, 
		BOOL=49, BY=50, CHAINS=51, CLASS=52, CLASSIFIER=53, COMMENT=54, COMPOSITE=55, 
		CONJUGATE=56, CONJUGATES=57, CONJUGATION=58, CONNECTOR=59, DATATYPE=60, 
		DEFAULT=61, DEFAULT_SUFFIX=62, DEPENDENCY=63, DERIVED=64, DERIVED_PREFIX=65, 
		DIFFERENCES=66, DISJOINING=67, DISJOINT=68, DOC=69, ELSE=70, END=71, END_PREFIX=72, 
		EXPR=73, FALSE=74, FEATURE=75, FEATURED=76, FEATURING=77, FILTER=78, FIRST=79, 
		FLOW=80, FOR=81, FROM=82, FUNCTION=83, HASTYPE=84, IF=85, INTERSECTS=86, 
		IMPLIES=87, IMPORT=88, IN=89, IN_PREFIX=90, INOUT=91, INOUT_PREFIX=92, 
		INTERACTION=93, INV=94, INVERSE=95, INVERTING=96, ISTYPE=97, LANGUAGE=98, 
		MEMBER=99, METACLASS=100, METADATA=101, MULTIPLICITY=102, NAMESPACE=103, 
		NONUNIQUE=104, NONUNIQUE_SUFFIX=105, NOT=106, NULL=107, OF=108, OR=109, 
		ORDERED=110, ORDERED_SUFFIX=111, OUT=112, OUT_PREFIX=113, PACKAGE=114, 
		PORTION=115, PREDICATE=116, PRIAVTE=117, PROTECTED=118, PUBLIC=119, READONLY=120, 
		READONLY_PREFIX=121, REDEFINES=122, REDEFINITION=123, REF=124, REF_PREFIX=125, 
		REFERENCES=126, REP=127, RETURN=128, SPECIALIZTION=129, SPECIALIZES=130, 
		STEP=131, STRCUT=132, SUBCLASSIFIER=133, SUBSET=134, SUBSETS=135, SUBTYPE=136, 
		SUCCESSION=137, THEN=138, TO=139, TRUE=140, TYPE=141, TYPED=142, TYPING=143, 
		UNIONS=144, VARIATION=145, VARIATION_PREFIX=146, VARIANT=147, VARIANT_PREFIX=148, 
		XOR_KEYWORD=149, REFNAME=150, ANY=151, XOR=152;
	public static final int
		RULE_nodeExpression = 0, RULE_listItemExpression = 1, RULE_prefixNodeExpression = 2, 
		RULE_prefixListItemExpression = 3, RULE_directionPrefixExpression = 4, 
		RULE_abstractPrefixExpression = 5, RULE_variationPrefixExpression = 6, 
		RULE_variantPrefixExpression = 7, RULE_readonlyPrefixExpression = 8, RULE_derivedPrefixExpression = 9, 
		RULE_endPrefixExpression = 10, RULE_referenceExpression = 11, RULE_multiplicityExpression = 12, 
		RULE_multiplicityPropExpression = 13, RULE_orderedMultiplicityExpression = 14, 
		RULE_nonuniqueMultiplicityExpression = 15, RULE_multiplicityExpressionMember = 16, 
		RULE_featureExpressions = 17, RULE_subsettingExpression = 18, RULE_redefinitionExpression = 19, 
		RULE_typingExpression = 20, RULE_valueExpression = 21, RULE_featureValueExpression = 22, 
		RULE_literalExpression = 23, RULE_measurementExpression = 24, RULE_constraintExpression = 25, 
		RULE_expression = 26, RULE_primaryExpression = 27, RULE_bracketAccessExpression = 28, 
		RULE_primaryAtom = 29, RULE_sequenceExpressionList = 30, RULE_featureChainExpression = 31, 
		RULE_transitionExpression = 32, RULE_triggerExpression = 33, RULE_triggerExpressionName = 34, 
		RULE_guardExpression = 35, RULE_effectExpression = 36, RULE_qualifiedName = 37, 
		RULE_shortName = 38, RULE_refName = 39, RULE_qualifiedName2 = 40, RULE_name = 41;
	private static String[] makeRuleNames() {
		return new String[] {
			"nodeExpression", "listItemExpression", "prefixNodeExpression", "prefixListItemExpression", 
			"directionPrefixExpression", "abstractPrefixExpression", "variationPrefixExpression", 
			"variantPrefixExpression", "readonlyPrefixExpression", "derivedPrefixExpression", 
			"endPrefixExpression", "referenceExpression", "multiplicityExpression", 
			"multiplicityPropExpression", "orderedMultiplicityExpression", "nonuniqueMultiplicityExpression", 
			"multiplicityExpressionMember", "featureExpressions", "subsettingExpression", 
			"redefinitionExpression", "typingExpression", "valueExpression", "featureValueExpression", 
			"literalExpression", "measurementExpression", "constraintExpression", 
			"expression", "primaryExpression", "bracketAccessExpression", "primaryAtom", 
			"sequenceExpressionList", "featureChainExpression", "transitionExpression", 
			"triggerExpression", "triggerExpressionName", "guardExpression", "effectExpression", 
			"qualifiedName", "shortName", "refName", "qualifiedName2", "name"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'['", "']'", "'('", "')'", "'<'", "'>'", "','", "'.'", 
			"':'", "'='", "'+'", "'-'", "'*'", null, "'%'", "'|'", "'&'", null, "'''", 
			"'..'", "'::'", "':>'", "':>>'", "':='", "'**'", "'=='", "'!='", "'==='", 
			"'!=='", "'<='", "'>='", null, null, null, null, "'about'", "'abstract'", 
			null, "'alias'", "'all'", "'and'", "'as'", "'assign'", "'assoc'", "'behavior'", 
			"'binding'", "'bool'", "'by'", "'chains'", "'class'", "'classifier'", 
			"'comment'", "'composite'", "'conjugate'", "'conjugates'", "'conjugation'", 
			"'connector'", "'datatype'", "'default'", null, "'dependency'", "'derived'", 
			null, "'differences'", "'disjoining'", "'disjoint'", "'doc'", "'else'", 
			"'end'", null, "'expr'", "'false'", "'feature'", "'featured'", "'featuring'", 
			"'filter'", "'first'", "'flow'", "'for'", "'from'", "'function'", "'hastype'", 
			"'if'", "'intersects'", "'implies'", "'import'", "'in'", null, "'inout'", 
			null, "'interaction'", "'inv'", "'inverse'", "'inverting'", "'istype'", 
			"'language'", "'member'", "'metaclass'", "'metadata'", "'multiplicity'", 
			"'namespace'", "'nonunique'", null, "'not'", "'null'", "'of'", "'or'", 
			"'ordered'", null, "'out'", null, "'package'", "'portion'", "'predicate'", 
			"'private'", "'protected'", "'public'", "'readonly'", null, "'redefines'", 
			"'redefinition'", "'ref'", null, "'references'", "'rep'", "'return'", 
			"'specialization'", "'specializes'", "'step'", "'struct'", "'subclassifier'", 
			"'subset'", "'subsets'", "'subtype'", "'succession'", "'then'", "'to'", 
			"'true'", "'type'", "'typed'", "'typing'", "'unions'", "'variation'", 
			null, "'variant'", null, "'xor'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ESPACED_NAME", "WS", "LBRACKET", "RBRACKET", "LPAREN", "RPAREN", 
			"LT", "GT", "COMMA", "DOT", "COLON", "EQUALS", "PLUS", "MINUS", "STAR", 
			"DIV", "MOD", "PIPE", "AMP", "SLASH", "SINGLE_QUOTE", "DOTDOT", "NAMESPACE_SEP", 
			"SUBSETS_OP", "REDEFINES_OP", "ASSIGN_OP", "POWER", "EQ", "NEQ", "EQ_STRICT", 
			"NEQ_STRICT", "LTE", "GTE", "Boolean", "Integer", "Real", "DoubleQuotedString", 
			"ABOUT", "ABSTRACT", "ABSTRACT_PREFIX", "ALIAS", "ALL", "AND", "AS", 
			"ASSIGN", "ASSOC", "BEAHVIOR", "BINDING", "BOOL", "BY", "CHAINS", "CLASS", 
			"CLASSIFIER", "COMMENT", "COMPOSITE", "CONJUGATE", "CONJUGATES", "CONJUGATION", 
			"CONNECTOR", "DATATYPE", "DEFAULT", "DEFAULT_SUFFIX", "DEPENDENCY", "DERIVED", 
			"DERIVED_PREFIX", "DIFFERENCES", "DISJOINING", "DISJOINT", "DOC", "ELSE", 
			"END", "END_PREFIX", "EXPR", "FALSE", "FEATURE", "FEATURED", "FEATURING", 
			"FILTER", "FIRST", "FLOW", "FOR", "FROM", "FUNCTION", "HASTYPE", "IF", 
			"INTERSECTS", "IMPLIES", "IMPORT", "IN", "IN_PREFIX", "INOUT", "INOUT_PREFIX", 
			"INTERACTION", "INV", "INVERSE", "INVERTING", "ISTYPE", "LANGUAGE", "MEMBER", 
			"METACLASS", "METADATA", "MULTIPLICITY", "NAMESPACE", "NONUNIQUE", "NONUNIQUE_SUFFIX", 
			"NOT", "NULL", "OF", "OR", "ORDERED", "ORDERED_SUFFIX", "OUT", "OUT_PREFIX", 
			"PACKAGE", "PORTION", "PREDICATE", "PRIAVTE", "PROTECTED", "PUBLIC", 
			"READONLY", "READONLY_PREFIX", "REDEFINES", "REDEFINITION", "REF", "REF_PREFIX", 
			"REFERENCES", "REP", "RETURN", "SPECIALIZTION", "SPECIALIZES", "STEP", 
			"STRCUT", "SUBCLASSIFIER", "SUBSET", "SUBSETS", "SUBTYPE", "SUCCESSION", 
			"THEN", "TO", "TRUE", "TYPE", "TYPED", "TYPING", "UNIONS", "VARIATION", 
			"VARIATION_PREFIX", "VARIANT", "VARIANT_PREFIX", "XOR_KEYWORD", "REFNAME", 
			"ANY", "XOR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "DirectEdit.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DirectEditParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class NodeExpressionContext extends ParserRuleContext {
		public PrefixNodeExpressionContext prefixNodeExpression() {
			return getRuleContext(PrefixNodeExpressionContext.class,0);
		}
		public FeatureExpressionsContext featureExpressions() {
			return getRuleContext(FeatureExpressionsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(DirectEditParser.EOF, 0); }
		public ReferenceExpressionContext referenceExpression() {
			return getRuleContext(ReferenceExpressionContext.class,0);
		}
		public ShortNameContext shortName() {
			return getRuleContext(ShortNameContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public MultiplicityExpressionContext multiplicityExpression() {
			return getRuleContext(MultiplicityExpressionContext.class,0);
		}
		public NodeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterNodeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitNodeExpression(this);
		}
	}

	public final NodeExpressionContext nodeExpression() throws RecognitionException {
		NodeExpressionContext _localctx = new NodeExpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_nodeExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			prefixNodeExpression();
			setState(86);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(85);
				referenceExpression();
				}
				break;
			}
			setState(89);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(88);
				shortName();
				}
				break;
			}
			setState(92);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ESPACED_NAME) | (1L << WS) | (1L << RBRACKET) | (1L << LPAREN) | (1L << RPAREN) | (1L << LT) | (1L << GT) | (1L << COMMA) | (1L << DOT) | (1L << PLUS) | (1L << MINUS) | (1L << STAR) | (1L << DIV) | (1L << MOD) | (1L << PIPE) | (1L << AMP) | (1L << SLASH) | (1L << SINGLE_QUOTE) | (1L << DOTDOT) | (1L << POWER) | (1L << EQ) | (1L << NEQ) | (1L << EQ_STRICT) | (1L << NEQ_STRICT) | (1L << LTE) | (1L << GTE) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (DERIVED - 64)) | (1L << (DERIVED_PREFIX - 64)) | (1L << (DIFFERENCES - 64)) | (1L << (DISJOINING - 64)) | (1L << (DISJOINT - 64)) | (1L << (DOC - 64)) | (1L << (ELSE - 64)) | (1L << (END - 64)) | (1L << (END_PREFIX - 64)) | (1L << (EXPR - 64)) | (1L << (FALSE - 64)) | (1L << (FEATURE - 64)) | (1L << (FEATURED - 64)) | (1L << (FEATURING - 64)) | (1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RETURN - 128)) | (1L << (SPECIALIZTION - 128)) | (1L << (SPECIALIZES - 128)) | (1L << (STEP - 128)) | (1L << (STRCUT - 128)) | (1L << (SUBCLASSIFIER - 128)) | (1L << (SUBSET - 128)) | (1L << (SUBSETS - 128)) | (1L << (SUBTYPE - 128)) | (1L << (SUCCESSION - 128)) | (1L << (THEN - 128)) | (1L << (TO - 128)) | (1L << (TRUE - 128)) | (1L << (TYPE - 128)) | (1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (VARIANT - 128)) | (1L << (VARIANT_PREFIX - 128)) | (1L << (XOR_KEYWORD - 128)) | (1L << (REFNAME - 128)) | (1L << (ANY - 128)) | (1L << (XOR - 128)))) != 0)) {
				{
				setState(91);
				name();
				}
			}

			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(94);
				multiplicityExpression();
				}
			}

			setState(97);
			featureExpressions();
			setState(98);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListItemExpressionContext extends ParserRuleContext {
		public PrefixListItemExpressionContext prefixListItemExpression() {
			return getRuleContext(PrefixListItemExpressionContext.class,0);
		}
		public FeatureExpressionsContext featureExpressions() {
			return getRuleContext(FeatureExpressionsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(DirectEditParser.EOF, 0); }
		public ReferenceExpressionContext referenceExpression() {
			return getRuleContext(ReferenceExpressionContext.class,0);
		}
		public ShortNameContext shortName() {
			return getRuleContext(ShortNameContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public MultiplicityExpressionContext multiplicityExpression() {
			return getRuleContext(MultiplicityExpressionContext.class,0);
		}
		public MultiplicityPropExpressionContext multiplicityPropExpression() {
			return getRuleContext(MultiplicityPropExpressionContext.class,0);
		}
		public ListItemExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listItemExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterListItemExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitListItemExpression(this);
		}
	}

	public final ListItemExpressionContext listItemExpression() throws RecognitionException {
		ListItemExpressionContext _localctx = new ListItemExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_listItemExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			prefixListItemExpression();
			setState(102);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(101);
				referenceExpression();
				}
				break;
			}
			setState(105);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(104);
				shortName();
				}
				break;
			}
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ESPACED_NAME) | (1L << WS) | (1L << RBRACKET) | (1L << LPAREN) | (1L << RPAREN) | (1L << LT) | (1L << GT) | (1L << COMMA) | (1L << DOT) | (1L << PLUS) | (1L << MINUS) | (1L << STAR) | (1L << DIV) | (1L << MOD) | (1L << PIPE) | (1L << AMP) | (1L << SLASH) | (1L << SINGLE_QUOTE) | (1L << DOTDOT) | (1L << POWER) | (1L << EQ) | (1L << NEQ) | (1L << EQ_STRICT) | (1L << NEQ_STRICT) | (1L << LTE) | (1L << GTE) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (DERIVED - 64)) | (1L << (DERIVED_PREFIX - 64)) | (1L << (DIFFERENCES - 64)) | (1L << (DISJOINING - 64)) | (1L << (DISJOINT - 64)) | (1L << (DOC - 64)) | (1L << (ELSE - 64)) | (1L << (END - 64)) | (1L << (END_PREFIX - 64)) | (1L << (EXPR - 64)) | (1L << (FALSE - 64)) | (1L << (FEATURE - 64)) | (1L << (FEATURED - 64)) | (1L << (FEATURING - 64)) | (1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RETURN - 128)) | (1L << (SPECIALIZTION - 128)) | (1L << (SPECIALIZES - 128)) | (1L << (STEP - 128)) | (1L << (STRCUT - 128)) | (1L << (SUBCLASSIFIER - 128)) | (1L << (SUBSET - 128)) | (1L << (SUBSETS - 128)) | (1L << (SUBTYPE - 128)) | (1L << (SUCCESSION - 128)) | (1L << (THEN - 128)) | (1L << (TO - 128)) | (1L << (TRUE - 128)) | (1L << (TYPE - 128)) | (1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (VARIANT - 128)) | (1L << (VARIANT_PREFIX - 128)) | (1L << (XOR_KEYWORD - 128)) | (1L << (REFNAME - 128)) | (1L << (ANY - 128)) | (1L << (XOR - 128)))) != 0)) {
				{
				setState(107);
				name();
				}
			}

			setState(111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(110);
				multiplicityExpression();
				}
			}

			setState(114);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(113);
				multiplicityPropExpression();
				}
				break;
			}
			setState(116);
			featureExpressions();
			setState(117);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrefixNodeExpressionContext extends ParserRuleContext {
		public List<AbstractPrefixExpressionContext> abstractPrefixExpression() {
			return getRuleContexts(AbstractPrefixExpressionContext.class);
		}
		public AbstractPrefixExpressionContext abstractPrefixExpression(int i) {
			return getRuleContext(AbstractPrefixExpressionContext.class,i);
		}
		public List<VariationPrefixExpressionContext> variationPrefixExpression() {
			return getRuleContexts(VariationPrefixExpressionContext.class);
		}
		public VariationPrefixExpressionContext variationPrefixExpression(int i) {
			return getRuleContext(VariationPrefixExpressionContext.class,i);
		}
		public List<VariantPrefixExpressionContext> variantPrefixExpression() {
			return getRuleContexts(VariantPrefixExpressionContext.class);
		}
		public VariantPrefixExpressionContext variantPrefixExpression(int i) {
			return getRuleContext(VariantPrefixExpressionContext.class,i);
		}
		public PrefixNodeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixNodeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterPrefixNodeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitPrefixNodeExpression(this);
		}
	}

	public final PrefixNodeExpressionContext prefixNodeExpression() throws RecognitionException {
		PrefixNodeExpressionContext _localctx = new PrefixNodeExpressionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_prefixNodeExpression);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(122);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case ABSTRACT_PREFIX:
						{
						setState(119);
						abstractPrefixExpression();
						}
						break;
					case VARIATION_PREFIX:
						{
						setState(120);
						variationPrefixExpression();
						}
						break;
					case VARIANT_PREFIX:
						{
						setState(121);
						variantPrefixExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(126);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrefixListItemExpressionContext extends ParserRuleContext {
		public List<DirectionPrefixExpressionContext> directionPrefixExpression() {
			return getRuleContexts(DirectionPrefixExpressionContext.class);
		}
		public DirectionPrefixExpressionContext directionPrefixExpression(int i) {
			return getRuleContext(DirectionPrefixExpressionContext.class,i);
		}
		public List<AbstractPrefixExpressionContext> abstractPrefixExpression() {
			return getRuleContexts(AbstractPrefixExpressionContext.class);
		}
		public AbstractPrefixExpressionContext abstractPrefixExpression(int i) {
			return getRuleContext(AbstractPrefixExpressionContext.class,i);
		}
		public List<VariationPrefixExpressionContext> variationPrefixExpression() {
			return getRuleContexts(VariationPrefixExpressionContext.class);
		}
		public VariationPrefixExpressionContext variationPrefixExpression(int i) {
			return getRuleContext(VariationPrefixExpressionContext.class,i);
		}
		public List<ReadonlyPrefixExpressionContext> readonlyPrefixExpression() {
			return getRuleContexts(ReadonlyPrefixExpressionContext.class);
		}
		public ReadonlyPrefixExpressionContext readonlyPrefixExpression(int i) {
			return getRuleContext(ReadonlyPrefixExpressionContext.class,i);
		}
		public List<DerivedPrefixExpressionContext> derivedPrefixExpression() {
			return getRuleContexts(DerivedPrefixExpressionContext.class);
		}
		public DerivedPrefixExpressionContext derivedPrefixExpression(int i) {
			return getRuleContext(DerivedPrefixExpressionContext.class,i);
		}
		public List<EndPrefixExpressionContext> endPrefixExpression() {
			return getRuleContexts(EndPrefixExpressionContext.class);
		}
		public EndPrefixExpressionContext endPrefixExpression(int i) {
			return getRuleContext(EndPrefixExpressionContext.class,i);
		}
		public PrefixListItemExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixListItemExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterPrefixListItemExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitPrefixListItemExpression(this);
		}
	}

	public final PrefixListItemExpressionContext prefixListItemExpression() throws RecognitionException {
		PrefixListItemExpressionContext _localctx = new PrefixListItemExpressionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_prefixListItemExpression);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(133);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case IN_PREFIX:
					case INOUT_PREFIX:
					case OUT_PREFIX:
						{
						setState(127);
						directionPrefixExpression();
						}
						break;
					case ABSTRACT_PREFIX:
						{
						setState(128);
						abstractPrefixExpression();
						}
						break;
					case VARIATION_PREFIX:
						{
						setState(129);
						variationPrefixExpression();
						}
						break;
					case READONLY_PREFIX:
						{
						setState(130);
						readonlyPrefixExpression();
						}
						break;
					case DERIVED_PREFIX:
						{
						setState(131);
						derivedPrefixExpression();
						}
						break;
					case END_PREFIX:
						{
						setState(132);
						endPrefixExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(137);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DirectionPrefixExpressionContext extends ParserRuleContext {
		public TerminalNode IN_PREFIX() { return getToken(DirectEditParser.IN_PREFIX, 0); }
		public TerminalNode OUT_PREFIX() { return getToken(DirectEditParser.OUT_PREFIX, 0); }
		public TerminalNode INOUT_PREFIX() { return getToken(DirectEditParser.INOUT_PREFIX, 0); }
		public DirectionPrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directionPrefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterDirectionPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitDirectionPrefixExpression(this);
		}
	}

	public final DirectionPrefixExpressionContext directionPrefixExpression() throws RecognitionException {
		DirectionPrefixExpressionContext _localctx = new DirectionPrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_directionPrefixExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			_la = _input.LA(1);
			if ( !(((((_la - 90)) & ~0x3f) == 0 && ((1L << (_la - 90)) & ((1L << (IN_PREFIX - 90)) | (1L << (INOUT_PREFIX - 90)) | (1L << (OUT_PREFIX - 90)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AbstractPrefixExpressionContext extends ParserRuleContext {
		public TerminalNode ABSTRACT_PREFIX() { return getToken(DirectEditParser.ABSTRACT_PREFIX, 0); }
		public AbstractPrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abstractPrefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterAbstractPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitAbstractPrefixExpression(this);
		}
	}

	public final AbstractPrefixExpressionContext abstractPrefixExpression() throws RecognitionException {
		AbstractPrefixExpressionContext _localctx = new AbstractPrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_abstractPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			match(ABSTRACT_PREFIX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariationPrefixExpressionContext extends ParserRuleContext {
		public TerminalNode VARIATION_PREFIX() { return getToken(DirectEditParser.VARIATION_PREFIX, 0); }
		public VariationPrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variationPrefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterVariationPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitVariationPrefixExpression(this);
		}
	}

	public final VariationPrefixExpressionContext variationPrefixExpression() throws RecognitionException {
		VariationPrefixExpressionContext _localctx = new VariationPrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_variationPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(VARIATION_PREFIX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariantPrefixExpressionContext extends ParserRuleContext {
		public TerminalNode VARIANT_PREFIX() { return getToken(DirectEditParser.VARIANT_PREFIX, 0); }
		public VariantPrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variantPrefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterVariantPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitVariantPrefixExpression(this);
		}
	}

	public final VariantPrefixExpressionContext variantPrefixExpression() throws RecognitionException {
		VariantPrefixExpressionContext _localctx = new VariantPrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_variantPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(VARIANT_PREFIX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReadonlyPrefixExpressionContext extends ParserRuleContext {
		public TerminalNode READONLY_PREFIX() { return getToken(DirectEditParser.READONLY_PREFIX, 0); }
		public ReadonlyPrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_readonlyPrefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterReadonlyPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitReadonlyPrefixExpression(this);
		}
	}

	public final ReadonlyPrefixExpressionContext readonlyPrefixExpression() throws RecognitionException {
		ReadonlyPrefixExpressionContext _localctx = new ReadonlyPrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_readonlyPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(READONLY_PREFIX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DerivedPrefixExpressionContext extends ParserRuleContext {
		public TerminalNode DERIVED_PREFIX() { return getToken(DirectEditParser.DERIVED_PREFIX, 0); }
		public DerivedPrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_derivedPrefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterDerivedPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitDerivedPrefixExpression(this);
		}
	}

	public final DerivedPrefixExpressionContext derivedPrefixExpression() throws RecognitionException {
		DerivedPrefixExpressionContext _localctx = new DerivedPrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_derivedPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(DERIVED_PREFIX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EndPrefixExpressionContext extends ParserRuleContext {
		public TerminalNode END_PREFIX() { return getToken(DirectEditParser.END_PREFIX, 0); }
		public EndPrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endPrefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterEndPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitEndPrefixExpression(this);
		}
	}

	public final EndPrefixExpressionContext endPrefixExpression() throws RecognitionException {
		EndPrefixExpressionContext _localctx = new EndPrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_endPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(END_PREFIX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReferenceExpressionContext extends ParserRuleContext {
		public TerminalNode REF_PREFIX() { return getToken(DirectEditParser.REF_PREFIX, 0); }
		public ReferenceExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_referenceExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterReferenceExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitReferenceExpression(this);
		}
	}

	public final ReferenceExpressionContext referenceExpression() throws RecognitionException {
		ReferenceExpressionContext _localctx = new ReferenceExpressionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_referenceExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(REF_PREFIX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MultiplicityExpressionContext extends ParserRuleContext {
		public MultiplicityExpressionMemberContext lowerBound;
		public MultiplicityExpressionMemberContext upperBound;
		public TerminalNode LBRACKET() { return getToken(DirectEditParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(DirectEditParser.RBRACKET, 0); }
		public List<MultiplicityExpressionMemberContext> multiplicityExpressionMember() {
			return getRuleContexts(MultiplicityExpressionMemberContext.class);
		}
		public MultiplicityExpressionMemberContext multiplicityExpressionMember(int i) {
			return getRuleContext(MultiplicityExpressionMemberContext.class,i);
		}
		public TerminalNode DOTDOT() { return getToken(DirectEditParser.DOTDOT, 0); }
		public MultiplicityExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicityExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterMultiplicityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitMultiplicityExpression(this);
		}
	}

	public final MultiplicityExpressionContext multiplicityExpression() throws RecognitionException {
		MultiplicityExpressionContext _localctx = new MultiplicityExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_multiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
			match(LBRACKET);
			setState(158);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(155);
				((MultiplicityExpressionContext)_localctx).lowerBound = multiplicityExpressionMember();
				setState(156);
				match(DOTDOT);
				}
				break;
			}
			setState(160);
			((MultiplicityExpressionContext)_localctx).upperBound = multiplicityExpressionMember();
			setState(161);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MultiplicityPropExpressionContext extends ParserRuleContext {
		public OrderedMultiplicityExpressionContext orderedMultiplicityExpression() {
			return getRuleContext(OrderedMultiplicityExpressionContext.class,0);
		}
		public NonuniqueMultiplicityExpressionContext nonuniqueMultiplicityExpression() {
			return getRuleContext(NonuniqueMultiplicityExpressionContext.class,0);
		}
		public MultiplicityPropExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicityPropExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterMultiplicityPropExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitMultiplicityPropExpression(this);
		}
	}

	public final MultiplicityPropExpressionContext multiplicityPropExpression() throws RecognitionException {
		MultiplicityPropExpressionContext _localctx = new MultiplicityPropExpressionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_multiplicityPropExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDERED_SUFFIX) {
				{
				setState(163);
				orderedMultiplicityExpression();
				}
			}

			setState(167);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NONUNIQUE_SUFFIX) {
				{
				setState(166);
				nonuniqueMultiplicityExpression();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrderedMultiplicityExpressionContext extends ParserRuleContext {
		public TerminalNode ORDERED_SUFFIX() { return getToken(DirectEditParser.ORDERED_SUFFIX, 0); }
		public OrderedMultiplicityExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderedMultiplicityExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterOrderedMultiplicityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitOrderedMultiplicityExpression(this);
		}
	}

	public final OrderedMultiplicityExpressionContext orderedMultiplicityExpression() throws RecognitionException {
		OrderedMultiplicityExpressionContext _localctx = new OrderedMultiplicityExpressionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_orderedMultiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(ORDERED_SUFFIX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonuniqueMultiplicityExpressionContext extends ParserRuleContext {
		public TerminalNode NONUNIQUE_SUFFIX() { return getToken(DirectEditParser.NONUNIQUE_SUFFIX, 0); }
		public NonuniqueMultiplicityExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonuniqueMultiplicityExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterNonuniqueMultiplicityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitNonuniqueMultiplicityExpression(this);
		}
	}

	public final NonuniqueMultiplicityExpressionContext nonuniqueMultiplicityExpression() throws RecognitionException {
		NonuniqueMultiplicityExpressionContext _localctx = new NonuniqueMultiplicityExpressionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_nonuniqueMultiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			match(NONUNIQUE_SUFFIX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MultiplicityExpressionMemberContext extends ParserRuleContext {
		public TerminalNode Integer() { return getToken(DirectEditParser.Integer, 0); }
		public TerminalNode STAR() { return getToken(DirectEditParser.STAR, 0); }
		public MultiplicityExpressionMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicityExpressionMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterMultiplicityExpressionMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitMultiplicityExpressionMember(this);
		}
	}

	public final MultiplicityExpressionMemberContext multiplicityExpressionMember() throws RecognitionException {
		MultiplicityExpressionMemberContext _localctx = new MultiplicityExpressionMemberContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_multiplicityExpressionMember);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			_la = _input.LA(1);
			if ( !(_la==STAR || _la==Integer) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FeatureExpressionsContext extends ParserRuleContext {
		public SubsettingExpressionContext subsettingExpression() {
			return getRuleContext(SubsettingExpressionContext.class,0);
		}
		public RedefinitionExpressionContext redefinitionExpression() {
			return getRuleContext(RedefinitionExpressionContext.class,0);
		}
		public TypingExpressionContext typingExpression() {
			return getRuleContext(TypingExpressionContext.class,0);
		}
		public FeatureValueExpressionContext featureValueExpression() {
			return getRuleContext(FeatureValueExpressionContext.class,0);
		}
		public FeatureExpressionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureExpressions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterFeatureExpressions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitFeatureExpressions(this);
		}
	}

	public final FeatureExpressionsContext featureExpressions() throws RecognitionException {
		FeatureExpressionsContext _localctx = new FeatureExpressionsContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_featureExpressions);
		int _la;
		try {
			setState(195);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(177);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SUBSETS_OP:
					{
					setState(175);
					subsettingExpression();
					}
					break;
				case REDEFINES_OP:
					{
					setState(176);
					redefinitionExpression();
					}
					break;
				case EOF:
				case COLON:
				case EQUALS:
				case ASSIGN_OP:
				case DEFAULT_SUFFIX:
					break;
				default:
					break;
				}
				setState(180);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(179);
					typingExpression();
					}
				}

				setState(183);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUALS) | (1L << ASSIGN_OP) | (1L << DEFAULT_SUFFIX))) != 0)) {
					{
					setState(182);
					featureValueExpression();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(186);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(185);
					typingExpression();
					}
				}

				setState(190);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SUBSETS_OP:
					{
					setState(188);
					subsettingExpression();
					}
					break;
				case REDEFINES_OP:
					{
					setState(189);
					redefinitionExpression();
					}
					break;
				case EOF:
				case EQUALS:
				case ASSIGN_OP:
				case DEFAULT_SUFFIX:
					break;
				default:
					break;
				}
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUALS) | (1L << ASSIGN_OP) | (1L << DEFAULT_SUFFIX))) != 0)) {
					{
					setState(192);
					featureValueExpression();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubsettingExpressionContext extends ParserRuleContext {
		public TerminalNode SUBSETS_OP() { return getToken(DirectEditParser.SUBSETS_OP, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public SubsettingExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subsettingExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterSubsettingExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitSubsettingExpression(this);
		}
	}

	public final SubsettingExpressionContext subsettingExpression() throws RecognitionException {
		SubsettingExpressionContext _localctx = new SubsettingExpressionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_subsettingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			match(SUBSETS_OP);
			setState(198);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RedefinitionExpressionContext extends ParserRuleContext {
		public TerminalNode REDEFINES_OP() { return getToken(DirectEditParser.REDEFINES_OP, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public RedefinitionExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_redefinitionExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterRedefinitionExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitRedefinitionExpression(this);
		}
	}

	public final RedefinitionExpressionContext redefinitionExpression() throws RecognitionException {
		RedefinitionExpressionContext _localctx = new RedefinitionExpressionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_redefinitionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(200);
			match(REDEFINES_OP);
			setState(201);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypingExpressionContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(DirectEditParser.COLON, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TypingExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typingExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterTypingExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitTypingExpression(this);
		}
	}

	public final TypingExpressionContext typingExpression() throws RecognitionException {
		TypingExpressionContext _localctx = new TypingExpressionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_typingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			match(COLON);
			setState(204);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueExpressionContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(DirectEditParser.EQUALS, 0); }
		public LiteralExpressionContext literalExpression() {
			return getRuleContext(LiteralExpressionContext.class,0);
		}
		public TerminalNode LBRACKET() { return getToken(DirectEditParser.LBRACKET, 0); }
		public MeasurementExpressionContext measurementExpression() {
			return getRuleContext(MeasurementExpressionContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(DirectEditParser.RBRACKET, 0); }
		public ValueExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterValueExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitValueExpression(this);
		}
	}

	public final ValueExpressionContext valueExpression() throws RecognitionException {
		ValueExpressionContext _localctx = new ValueExpressionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_valueExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(EQUALS);
			setState(207);
			literalExpression();
			setState(212);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(208);
				match(LBRACKET);
				setState(209);
				measurementExpression();
				setState(210);
				match(RBRACKET);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FeatureValueExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(DirectEditParser.EQUALS, 0); }
		public TerminalNode ASSIGN_OP() { return getToken(DirectEditParser.ASSIGN_OP, 0); }
		public TerminalNode DEFAULT_SUFFIX() { return getToken(DirectEditParser.DEFAULT_SUFFIX, 0); }
		public FeatureValueExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureValueExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterFeatureValueExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitFeatureValueExpression(this);
		}
	}

	public final FeatureValueExpressionContext featureValueExpression() throws RecognitionException {
		FeatureValueExpressionContext _localctx = new FeatureValueExpressionContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_featureValueExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT_SUFFIX) {
				{
				setState(214);
				match(DEFAULT_SUFFIX);
				}
			}

			setState(217);
			_la = _input.LA(1);
			if ( !(_la==EQUALS || _la==ASSIGN_OP) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(218);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralExpressionContext extends ParserRuleContext {
		public TerminalNode Real() { return getToken(DirectEditParser.Real, 0); }
		public TerminalNode Boolean() { return getToken(DirectEditParser.Boolean, 0); }
		public TerminalNode Integer() { return getToken(DirectEditParser.Integer, 0); }
		public TerminalNode DoubleQuotedString() { return getToken(DirectEditParser.DoubleQuotedString, 0); }
		public LiteralExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterLiteralExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitLiteralExpression(this);
		}
	}

	public final LiteralExpressionContext literalExpression() throws RecognitionException {
		LiteralExpressionContext _localctx = new LiteralExpressionContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_literalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MeasurementExpressionContext extends ParserRuleContext {
		public List<TerminalNode> RBRACKET() { return getTokens(DirectEditParser.RBRACKET); }
		public TerminalNode RBRACKET(int i) {
			return getToken(DirectEditParser.RBRACKET, i);
		}
		public MeasurementExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_measurementExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterMeasurementExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitMeasurementExpression(this);
		}
	}

	public final MeasurementExpressionContext measurementExpression() throws RecognitionException {
		MeasurementExpressionContext _localctx = new MeasurementExpressionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_measurementExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(222);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==RBRACKET) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(225); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ESPACED_NAME) | (1L << WS) | (1L << LBRACKET) | (1L << LPAREN) | (1L << RPAREN) | (1L << LT) | (1L << GT) | (1L << COMMA) | (1L << DOT) | (1L << COLON) | (1L << EQUALS) | (1L << PLUS) | (1L << MINUS) | (1L << STAR) | (1L << DIV) | (1L << MOD) | (1L << PIPE) | (1L << AMP) | (1L << SLASH) | (1L << SINGLE_QUOTE) | (1L << DOTDOT) | (1L << NAMESPACE_SEP) | (1L << SUBSETS_OP) | (1L << REDEFINES_OP) | (1L << ASSIGN_OP) | (1L << POWER) | (1L << EQ) | (1L << NEQ) | (1L << EQ_STRICT) | (1L << NEQ_STRICT) | (1L << LTE) | (1L << GTE) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEFAULT_SUFFIX) | (1L << DEPENDENCY))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (DERIVED - 64)) | (1L << (DERIVED_PREFIX - 64)) | (1L << (DIFFERENCES - 64)) | (1L << (DISJOINING - 64)) | (1L << (DISJOINT - 64)) | (1L << (DOC - 64)) | (1L << (ELSE - 64)) | (1L << (END - 64)) | (1L << (END_PREFIX - 64)) | (1L << (EXPR - 64)) | (1L << (FALSE - 64)) | (1L << (FEATURE - 64)) | (1L << (FEATURED - 64)) | (1L << (FEATURING - 64)) | (1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NONUNIQUE_SUFFIX - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (ORDERED_SUFFIX - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (RETURN - 128)) | (1L << (SPECIALIZTION - 128)) | (1L << (SPECIALIZES - 128)) | (1L << (STEP - 128)) | (1L << (STRCUT - 128)) | (1L << (SUBCLASSIFIER - 128)) | (1L << (SUBSET - 128)) | (1L << (SUBSETS - 128)) | (1L << (SUBTYPE - 128)) | (1L << (SUCCESSION - 128)) | (1L << (THEN - 128)) | (1L << (TO - 128)) | (1L << (TRUE - 128)) | (1L << (TYPE - 128)) | (1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (VARIANT - 128)) | (1L << (VARIANT_PREFIX - 128)) | (1L << (XOR_KEYWORD - 128)) | (1L << (REFNAME - 128)) | (1L << (ANY - 128)) | (1L << (XOR - 128)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstraintExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ConstraintExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraintExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterConstraintExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitConstraintExpression(this);
		}
	}

	public final ConstraintExpressionContext constraintExpression() throws RecognitionException {
		ConstraintExpressionContext _localctx = new ConstraintExpressionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_constraintExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BinaryOperationExprContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode POWER() { return getToken(DirectEditParser.POWER, 0); }
		public TerminalNode STAR() { return getToken(DirectEditParser.STAR, 0); }
		public TerminalNode DIV() { return getToken(DirectEditParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(DirectEditParser.MOD, 0); }
		public TerminalNode XOR() { return getToken(DirectEditParser.XOR, 0); }
		public TerminalNode PLUS() { return getToken(DirectEditParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(DirectEditParser.MINUS, 0); }
		public TerminalNode PIPE() { return getToken(DirectEditParser.PIPE, 0); }
		public TerminalNode AMP() { return getToken(DirectEditParser.AMP, 0); }
		public TerminalNode XOR_KEYWORD() { return getToken(DirectEditParser.XOR_KEYWORD, 0); }
		public TerminalNode DOTDOT() { return getToken(DirectEditParser.DOTDOT, 0); }
		public TerminalNode EQ() { return getToken(DirectEditParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(DirectEditParser.NEQ, 0); }
		public TerminalNode EQ_STRICT() { return getToken(DirectEditParser.EQ_STRICT, 0); }
		public TerminalNode NEQ_STRICT() { return getToken(DirectEditParser.NEQ_STRICT, 0); }
		public TerminalNode LT() { return getToken(DirectEditParser.LT, 0); }
		public TerminalNode GT() { return getToken(DirectEditParser.GT, 0); }
		public TerminalNode LTE() { return getToken(DirectEditParser.LTE, 0); }
		public TerminalNode GTE() { return getToken(DirectEditParser.GTE, 0); }
		public BinaryOperationExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterBinaryOperationExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitBinaryOperationExpr(this);
		}
	}
	public static class PrimaryExprContext extends ExpressionContext {
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public PrimaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterPrimaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitPrimaryExpr(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 52;
		enterRecursionRule(_localctx, 52, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new PrimaryExprContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(230);
			primaryExpression();
			}
			_ctx.stop = _input.LT(-1);
			setState(237);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new BinaryOperationExprContext(new ExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_expression);
					setState(232);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(233);
					((BinaryOperationExprContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << GT) | (1L << PLUS) | (1L << MINUS) | (1L << STAR) | (1L << DIV) | (1L << MOD) | (1L << PIPE) | (1L << AMP) | (1L << DOTDOT) | (1L << POWER) | (1L << EQ) | (1L << NEQ) | (1L << EQ_STRICT) | (1L << NEQ_STRICT) | (1L << LTE) | (1L << GTE))) != 0) || _la==XOR_KEYWORD || _la==XOR) ) {
						((BinaryOperationExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(234);
					expression(3);
					}
					} 
				}
				setState(239);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class PrimaryExpressionContext extends ParserRuleContext {
		public FeatureChainExpressionContext featureChainExpression() {
			return getRuleContext(FeatureChainExpressionContext.class,0);
		}
		public BracketAccessExpressionContext bracketAccessExpression() {
			return getRuleContext(BracketAccessExpressionContext.class,0);
		}
		public LiteralExpressionContext literalExpression() {
			return getRuleContext(LiteralExpressionContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(DirectEditParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(DirectEditParser.RPAREN, 0); }
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitPrimaryExpression(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_primaryExpression);
		try {
			setState(247);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(240);
				featureChainExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(241);
				bracketAccessExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(242);
				literalExpression();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(243);
				match(LPAREN);
				setState(244);
				expression(0);
				setState(245);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BracketAccessExpressionContext extends ParserRuleContext {
		public PrimaryAtomContext primaryAtom() {
			return getRuleContext(PrimaryAtomContext.class,0);
		}
		public TerminalNode LBRACKET() { return getToken(DirectEditParser.LBRACKET, 0); }
		public SequenceExpressionListContext sequenceExpressionList() {
			return getRuleContext(SequenceExpressionListContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(DirectEditParser.RBRACKET, 0); }
		public BracketAccessExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracketAccessExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterBracketAccessExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitBracketAccessExpression(this);
		}
	}

	public final BracketAccessExpressionContext bracketAccessExpression() throws RecognitionException {
		BracketAccessExpressionContext _localctx = new BracketAccessExpressionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_bracketAccessExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			primaryAtom();
			setState(250);
			match(LBRACKET);
			setState(251);
			sequenceExpressionList();
			setState(252);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimaryAtomContext extends ParserRuleContext {
		public FeatureChainExpressionContext featureChainExpression() {
			return getRuleContext(FeatureChainExpressionContext.class,0);
		}
		public LiteralExpressionContext literalExpression() {
			return getRuleContext(LiteralExpressionContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(DirectEditParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(DirectEditParser.RPAREN, 0); }
		public PrimaryAtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryAtom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterPrimaryAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitPrimaryAtom(this);
		}
	}

	public final PrimaryAtomContext primaryAtom() throws RecognitionException {
		PrimaryAtomContext _localctx = new PrimaryAtomContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_primaryAtom);
		try {
			setState(260);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ESPACED_NAME:
			case REFNAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(254);
				featureChainExpression();
				}
				break;
			case Boolean:
			case Integer:
			case Real:
			case DoubleQuotedString:
				enterOuterAlt(_localctx, 2);
				{
				setState(255);
				literalExpression();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 3);
				{
				setState(256);
				match(LPAREN);
				setState(257);
				expression(0);
				setState(258);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SequenceExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(DirectEditParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(DirectEditParser.COMMA, i);
		}
		public SequenceExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceExpressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterSequenceExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitSequenceExpressionList(this);
		}
	}

	public final SequenceExpressionListContext sequenceExpressionList() throws RecognitionException {
		SequenceExpressionListContext _localctx = new SequenceExpressionListContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_sequenceExpressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			expression(0);
			setState(267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(263);
				match(COMMA);
				setState(264);
				expression(0);
				}
				}
				setState(269);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FeatureChainExpressionContext extends ParserRuleContext {
		public RefNameContext refName() {
			return getRuleContext(RefNameContext.class,0);
		}
		public TerminalNode DOT() { return getToken(DirectEditParser.DOT, 0); }
		public FeatureChainExpressionContext featureChainExpression() {
			return getRuleContext(FeatureChainExpressionContext.class,0);
		}
		public FeatureChainExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureChainExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterFeatureChainExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitFeatureChainExpression(this);
		}
	}

	public final FeatureChainExpressionContext featureChainExpression() throws RecognitionException {
		FeatureChainExpressionContext _localctx = new FeatureChainExpressionContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_featureChainExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			refName();
			setState(273);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(271);
				match(DOT);
				setState(272);
				featureChainExpression();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TransitionExpressionContext extends ParserRuleContext {
		public TriggerExpressionContext triggerExpression() {
			return getRuleContext(TriggerExpressionContext.class,0);
		}
		public GuardExpressionContext guardExpression() {
			return getRuleContext(GuardExpressionContext.class,0);
		}
		public EffectExpressionContext effectExpression() {
			return getRuleContext(EffectExpressionContext.class,0);
		}
		public TransitionExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transitionExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterTransitionExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitTransitionExpression(this);
		}
	}

	public final TransitionExpressionContext transitionExpression() throws RecognitionException {
		TransitionExpressionContext _localctx = new TransitionExpressionContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_transitionExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(275);
				triggerExpression();
				}
				break;
			}
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(278);
				guardExpression();
				}
			}

			setState(282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SLASH) {
				{
				setState(281);
				effectExpression();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerExpressionContext extends ParserRuleContext {
		public List<TriggerExpressionNameContext> triggerExpressionName() {
			return getRuleContexts(TriggerExpressionNameContext.class);
		}
		public TriggerExpressionNameContext triggerExpressionName(int i) {
			return getRuleContext(TriggerExpressionNameContext.class,i);
		}
		public List<TerminalNode> PIPE() { return getTokens(DirectEditParser.PIPE); }
		public TerminalNode PIPE(int i) {
			return getToken(DirectEditParser.PIPE, i);
		}
		public TriggerExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggerExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterTriggerExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitTriggerExpression(this);
		}
	}

	public final TriggerExpressionContext triggerExpression() throws RecognitionException {
		TriggerExpressionContext _localctx = new TriggerExpressionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_triggerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			triggerExpressionName();
			setState(289);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PIPE) {
				{
				{
				setState(285);
				match(PIPE);
				setState(286);
				triggerExpressionName();
				}
				}
				setState(291);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerExpressionNameContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TypingExpressionContext typingExpression() {
			return getRuleContext(TypingExpressionContext.class,0);
		}
		public TriggerExpressionNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triggerExpressionName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterTriggerExpressionName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitTriggerExpressionName(this);
		}
	}

	public final TriggerExpressionNameContext triggerExpressionName() throws RecognitionException {
		TriggerExpressionNameContext _localctx = new TriggerExpressionNameContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_triggerExpressionName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(292);
			name();
			setState(294);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(293);
				typingExpression();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GuardExpressionContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(DirectEditParser.LBRACKET, 0); }
		public ValueExpressionContext valueExpression() {
			return getRuleContext(ValueExpressionContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(DirectEditParser.RBRACKET, 0); }
		public GuardExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_guardExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterGuardExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitGuardExpression(this);
		}
	}

	public final GuardExpressionContext guardExpression() throws RecognitionException {
		GuardExpressionContext _localctx = new GuardExpressionContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_guardExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			match(LBRACKET);
			setState(297);
			valueExpression();
			setState(298);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EffectExpressionContext extends ParserRuleContext {
		public TerminalNode SLASH() { return getToken(DirectEditParser.SLASH, 0); }
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(DirectEditParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(DirectEditParser.COMMA, i);
		}
		public EffectExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_effectExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterEffectExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitEffectExpression(this);
		}
	}

	public final EffectExpressionContext effectExpression() throws RecognitionException {
		EffectExpressionContext _localctx = new EffectExpressionContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_effectExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			match(SLASH);
			setState(301);
			qualifiedName();
			setState(306);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(302);
				match(COMMA);
				setState(303);
				qualifiedName();
				}
				}
				setState(308);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedNameContext extends ParserRuleContext {
		public List<NameContext> name() {
			return getRuleContexts(NameContext.class);
		}
		public NameContext name(int i) {
			return getRuleContext(NameContext.class,i);
		}
		public List<TerminalNode> NAMESPACE_SEP() { return getTokens(DirectEditParser.NAMESPACE_SEP); }
		public TerminalNode NAMESPACE_SEP(int i) {
			return getToken(DirectEditParser.NAMESPACE_SEP, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitQualifiedName(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			name();
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAMESPACE_SEP) {
				{
				{
				setState(310);
				match(NAMESPACE_SEP);
				setState(311);
				name();
				}
				}
				setState(316);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ShortNameContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(DirectEditParser.LT, 0); }
		public TerminalNode GT() { return getToken(DirectEditParser.GT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ShortNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shortName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterShortName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitShortName(this);
		}
	}

	public final ShortNameContext shortName() throws RecognitionException {
		ShortNameContext _localctx = new ShortNameContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_shortName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			match(LT);
			setState(319);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(318);
				name();
				}
				break;
			}
			setState(321);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RefNameContext extends ParserRuleContext {
		public TerminalNode REFNAME() { return getToken(DirectEditParser.REFNAME, 0); }
		public TerminalNode ESPACED_NAME() { return getToken(DirectEditParser.ESPACED_NAME, 0); }
		public QualifiedName2Context qualifiedName2() {
			return getRuleContext(QualifiedName2Context.class,0);
		}
		public RefNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterRefName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitRefName(this);
		}
	}

	public final RefNameContext refName() throws RecognitionException {
		RefNameContext _localctx = new RefNameContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_refName);
		try {
			setState(326);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(323);
				match(REFNAME);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(324);
				match(ESPACED_NAME);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(325);
				qualifiedName2();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedName2Context extends ParserRuleContext {
		public List<TerminalNode> REFNAME() { return getTokens(DirectEditParser.REFNAME); }
		public TerminalNode REFNAME(int i) {
			return getToken(DirectEditParser.REFNAME, i);
		}
		public List<TerminalNode> ESPACED_NAME() { return getTokens(DirectEditParser.ESPACED_NAME); }
		public TerminalNode ESPACED_NAME(int i) {
			return getToken(DirectEditParser.ESPACED_NAME, i);
		}
		public List<TerminalNode> NAMESPACE_SEP() { return getTokens(DirectEditParser.NAMESPACE_SEP); }
		public TerminalNode NAMESPACE_SEP(int i) {
			return getToken(DirectEditParser.NAMESPACE_SEP, i);
		}
		public QualifiedName2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterQualifiedName2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitQualifiedName2(this);
		}
	}

	public final QualifiedName2Context qualifiedName2() throws RecognitionException {
		QualifiedName2Context _localctx = new QualifiedName2Context(_ctx, getState());
		enterRule(_localctx, 80, RULE_qualifiedName2);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			_la = _input.LA(1);
			if ( !(_la==ESPACED_NAME || _la==REFNAME) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(331); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(329);
					match(NAMESPACE_SEP);
					setState(330);
					_la = _input.LA(1);
					if ( !(_la==ESPACED_NAME || _la==REFNAME) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(333); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NameContext extends ParserRuleContext {
		public List<TerminalNode> COLON() { return getTokens(DirectEditParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(DirectEditParser.COLON, i);
		}
		public List<TerminalNode> SUBSETS_OP() { return getTokens(DirectEditParser.SUBSETS_OP); }
		public TerminalNode SUBSETS_OP(int i) {
			return getToken(DirectEditParser.SUBSETS_OP, i);
		}
		public List<TerminalNode> NAMESPACE_SEP() { return getTokens(DirectEditParser.NAMESPACE_SEP); }
		public TerminalNode NAMESPACE_SEP(int i) {
			return getToken(DirectEditParser.NAMESPACE_SEP, i);
		}
		public List<TerminalNode> REDEFINES_OP() { return getTokens(DirectEditParser.REDEFINES_OP); }
		public TerminalNode REDEFINES_OP(int i) {
			return getToken(DirectEditParser.REDEFINES_OP, i);
		}
		public List<TerminalNode> EQUALS() { return getTokens(DirectEditParser.EQUALS); }
		public TerminalNode EQUALS(int i) {
			return getToken(DirectEditParser.EQUALS, i);
		}
		public List<TerminalNode> ASSIGN_OP() { return getTokens(DirectEditParser.ASSIGN_OP); }
		public TerminalNode ASSIGN_OP(int i) {
			return getToken(DirectEditParser.ASSIGN_OP, i);
		}
		public List<TerminalNode> LBRACKET() { return getTokens(DirectEditParser.LBRACKET); }
		public TerminalNode LBRACKET(int i) {
			return getToken(DirectEditParser.LBRACKET, i);
		}
		public List<TerminalNode> DEFAULT_SUFFIX() { return getTokens(DirectEditParser.DEFAULT_SUFFIX); }
		public TerminalNode DEFAULT_SUFFIX(int i) {
			return getToken(DirectEditParser.DEFAULT_SUFFIX, i);
		}
		public List<TerminalNode> ORDERED_SUFFIX() { return getTokens(DirectEditParser.ORDERED_SUFFIX); }
		public TerminalNode ORDERED_SUFFIX(int i) {
			return getToken(DirectEditParser.ORDERED_SUFFIX, i);
		}
		public List<TerminalNode> NONUNIQUE_SUFFIX() { return getTokens(DirectEditParser.NONUNIQUE_SUFFIX); }
		public TerminalNode NONUNIQUE_SUFFIX(int i) {
			return getToken(DirectEditParser.NONUNIQUE_SUFFIX, i);
		}
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitName(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_name);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(336); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(335);
					_la = _input.LA(1);
					if ( _la <= 0 || ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACKET) | (1L << COLON) | (1L << EQUALS) | (1L << NAMESPACE_SEP) | (1L << SUBSETS_OP) | (1L << REDEFINES_OP) | (1L << ASSIGN_OP) | (1L << DEFAULT_SUFFIX))) != 0) || _la==NONUNIQUE_SUFFIX || _la==ORDERED_SUFFIX) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(338); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 26:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0098\u0155\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0001\u0000\u0001\u0000\u0003\u0000W\b"+
		"\u0000\u0001\u0000\u0003\u0000Z\b\u0000\u0001\u0000\u0003\u0000]\b\u0000"+
		"\u0001\u0000\u0003\u0000`\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0003\u0001g\b\u0001\u0001\u0001\u0003\u0001"+
		"j\b\u0001\u0001\u0001\u0003\u0001m\b\u0001\u0001\u0001\u0003\u0001p\b"+
		"\u0001\u0001\u0001\u0003\u0001s\b\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002{\b\u0002\n\u0002"+
		"\f\u0002~\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0005\u0003\u0086\b\u0003\n\u0003\f\u0003\u0089\t\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u009f"+
		"\b\f\u0001\f\u0001\f\u0001\f\u0001\r\u0003\r\u00a5\b\r\u0001\r\u0003\r"+
		"\u00a8\b\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0003\u0011\u00b2\b\u0011\u0001\u0011"+
		"\u0003\u0011\u00b5\b\u0011\u0001\u0011\u0003\u0011\u00b8\b\u0011\u0001"+
		"\u0011\u0003\u0011\u00bb\b\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00bf"+
		"\b\u0011\u0001\u0011\u0003\u0011\u00c2\b\u0011\u0003\u0011\u00c4\b\u0011"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u00d5\b\u0015\u0001\u0016"+
		"\u0003\u0016\u00d8\b\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017"+
		"\u0001\u0017\u0001\u0018\u0004\u0018\u00e0\b\u0018\u000b\u0018\f\u0018"+
		"\u00e1\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0005\u001a\u00ec\b\u001a\n\u001a\f\u001a"+
		"\u00ef\t\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0003\u001b\u00f8\b\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u0105\b\u001d\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0005\u001e\u010a\b\u001e\n\u001e\f\u001e\u010d"+
		"\t\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u0112\b\u001f"+
		"\u0001 \u0003 \u0115\b \u0001 \u0003 \u0118\b \u0001 \u0003 \u011b\b "+
		"\u0001!\u0001!\u0001!\u0005!\u0120\b!\n!\f!\u0123\t!\u0001\"\u0001\"\u0003"+
		"\"\u0127\b\"\u0001#\u0001#\u0001#\u0001#\u0001$\u0001$\u0001$\u0001$\u0005"+
		"$\u0131\b$\n$\f$\u0134\t$\u0001%\u0001%\u0001%\u0005%\u0139\b%\n%\f%\u013c"+
		"\t%\u0001&\u0001&\u0003&\u0140\b&\u0001&\u0001&\u0001\'\u0001\'\u0001"+
		"\'\u0003\'\u0147\b\'\u0001(\u0001(\u0001(\u0004(\u014c\b(\u000b(\f(\u014d"+
		"\u0001)\u0004)\u0151\b)\u000b)\f)\u0152\u0001)\u0000\u00014*\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		" \"$&(*,.02468:<>@BDFHJLNPR\u0000\b\u0003\u0000ZZ\\\\qq\u0002\u0000\u000f"+
		"\u000f##\u0002\u0000\f\f\u001a\u001a\u0001\u0000\"%\u0001\u0000\u0004"+
		"\u0004\u0006\u0000\u0007\b\r\u0013\u0016\u0016\u001b!\u0095\u0095\u0098"+
		"\u0098\u0002\u0000\u0001\u0001\u0096\u0096\u0006\u0000\u0003\u0003\u000b"+
		"\f\u0017\u001a>>iioo\u015f\u0000T\u0001\u0000\u0000\u0000\u0002d\u0001"+
		"\u0000\u0000\u0000\u0004|\u0001\u0000\u0000\u0000\u0006\u0087\u0001\u0000"+
		"\u0000\u0000\b\u008a\u0001\u0000\u0000\u0000\n\u008c\u0001\u0000\u0000"+
		"\u0000\f\u008e\u0001\u0000\u0000\u0000\u000e\u0090\u0001\u0000\u0000\u0000"+
		"\u0010\u0092\u0001\u0000\u0000\u0000\u0012\u0094\u0001\u0000\u0000\u0000"+
		"\u0014\u0096\u0001\u0000\u0000\u0000\u0016\u0098\u0001\u0000\u0000\u0000"+
		"\u0018\u009a\u0001\u0000\u0000\u0000\u001a\u00a4\u0001\u0000\u0000\u0000"+
		"\u001c\u00a9\u0001\u0000\u0000\u0000\u001e\u00ab\u0001\u0000\u0000\u0000"+
		" \u00ad\u0001\u0000\u0000\u0000\"\u00c3\u0001\u0000\u0000\u0000$\u00c5"+
		"\u0001\u0000\u0000\u0000&\u00c8\u0001\u0000\u0000\u0000(\u00cb\u0001\u0000"+
		"\u0000\u0000*\u00ce\u0001\u0000\u0000\u0000,\u00d7\u0001\u0000\u0000\u0000"+
		".\u00dc\u0001\u0000\u0000\u00000\u00df\u0001\u0000\u0000\u00002\u00e3"+
		"\u0001\u0000\u0000\u00004\u00e5\u0001\u0000\u0000\u00006\u00f7\u0001\u0000"+
		"\u0000\u00008\u00f9\u0001\u0000\u0000\u0000:\u0104\u0001\u0000\u0000\u0000"+
		"<\u0106\u0001\u0000\u0000\u0000>\u010e\u0001\u0000\u0000\u0000@\u0114"+
		"\u0001\u0000\u0000\u0000B\u011c\u0001\u0000\u0000\u0000D\u0124\u0001\u0000"+
		"\u0000\u0000F\u0128\u0001\u0000\u0000\u0000H\u012c\u0001\u0000\u0000\u0000"+
		"J\u0135\u0001\u0000\u0000\u0000L\u013d\u0001\u0000\u0000\u0000N\u0146"+
		"\u0001\u0000\u0000\u0000P\u0148\u0001\u0000\u0000\u0000R\u0150\u0001\u0000"+
		"\u0000\u0000TV\u0003\u0004\u0002\u0000UW\u0003\u0016\u000b\u0000VU\u0001"+
		"\u0000\u0000\u0000VW\u0001\u0000\u0000\u0000WY\u0001\u0000\u0000\u0000"+
		"XZ\u0003L&\u0000YX\u0001\u0000\u0000\u0000YZ\u0001\u0000\u0000\u0000Z"+
		"\\\u0001\u0000\u0000\u0000[]\u0003R)\u0000\\[\u0001\u0000\u0000\u0000"+
		"\\]\u0001\u0000\u0000\u0000]_\u0001\u0000\u0000\u0000^`\u0003\u0018\f"+
		"\u0000_^\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`a\u0001\u0000"+
		"\u0000\u0000ab\u0003\"\u0011\u0000bc\u0005\u0000\u0000\u0001c\u0001\u0001"+
		"\u0000\u0000\u0000df\u0003\u0006\u0003\u0000eg\u0003\u0016\u000b\u0000"+
		"fe\u0001\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000gi\u0001\u0000\u0000"+
		"\u0000hj\u0003L&\u0000ih\u0001\u0000\u0000\u0000ij\u0001\u0000\u0000\u0000"+
		"jl\u0001\u0000\u0000\u0000km\u0003R)\u0000lk\u0001\u0000\u0000\u0000l"+
		"m\u0001\u0000\u0000\u0000mo\u0001\u0000\u0000\u0000np\u0003\u0018\f\u0000"+
		"on\u0001\u0000\u0000\u0000op\u0001\u0000\u0000\u0000pr\u0001\u0000\u0000"+
		"\u0000qs\u0003\u001a\r\u0000rq\u0001\u0000\u0000\u0000rs\u0001\u0000\u0000"+
		"\u0000st\u0001\u0000\u0000\u0000tu\u0003\"\u0011\u0000uv\u0005\u0000\u0000"+
		"\u0001v\u0003\u0001\u0000\u0000\u0000w{\u0003\n\u0005\u0000x{\u0003\f"+
		"\u0006\u0000y{\u0003\u000e\u0007\u0000zw\u0001\u0000\u0000\u0000zx\u0001"+
		"\u0000\u0000\u0000zy\u0001\u0000\u0000\u0000{~\u0001\u0000\u0000\u0000"+
		"|z\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}\u0005\u0001\u0000"+
		"\u0000\u0000~|\u0001\u0000\u0000\u0000\u007f\u0086\u0003\b\u0004\u0000"+
		"\u0080\u0086\u0003\n\u0005\u0000\u0081\u0086\u0003\f\u0006\u0000\u0082"+
		"\u0086\u0003\u0010\b\u0000\u0083\u0086\u0003\u0012\t\u0000\u0084\u0086"+
		"\u0003\u0014\n\u0000\u0085\u007f\u0001\u0000\u0000\u0000\u0085\u0080\u0001"+
		"\u0000\u0000\u0000\u0085\u0081\u0001\u0000\u0000\u0000\u0085\u0082\u0001"+
		"\u0000\u0000\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0085\u0084\u0001"+
		"\u0000\u0000\u0000\u0086\u0089\u0001\u0000\u0000\u0000\u0087\u0085\u0001"+
		"\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088\u0007\u0001"+
		"\u0000\u0000\u0000\u0089\u0087\u0001\u0000\u0000\u0000\u008a\u008b\u0007"+
		"\u0000\u0000\u0000\u008b\t\u0001\u0000\u0000\u0000\u008c\u008d\u0005("+
		"\u0000\u0000\u008d\u000b\u0001\u0000\u0000\u0000\u008e\u008f\u0005\u0092"+
		"\u0000\u0000\u008f\r\u0001\u0000\u0000\u0000\u0090\u0091\u0005\u0094\u0000"+
		"\u0000\u0091\u000f\u0001\u0000\u0000\u0000\u0092\u0093\u0005y\u0000\u0000"+
		"\u0093\u0011\u0001\u0000\u0000\u0000\u0094\u0095\u0005A\u0000\u0000\u0095"+
		"\u0013\u0001\u0000\u0000\u0000\u0096\u0097\u0005H\u0000\u0000\u0097\u0015"+
		"\u0001\u0000\u0000\u0000\u0098\u0099\u0005}\u0000\u0000\u0099\u0017\u0001"+
		"\u0000\u0000\u0000\u009a\u009e\u0005\u0003\u0000\u0000\u009b\u009c\u0003"+
		" \u0010\u0000\u009c\u009d\u0005\u0016\u0000\u0000\u009d\u009f\u0001\u0000"+
		"\u0000\u0000\u009e\u009b\u0001\u0000\u0000\u0000\u009e\u009f\u0001\u0000"+
		"\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a1\u0003 \u0010"+
		"\u0000\u00a1\u00a2\u0005\u0004\u0000\u0000\u00a2\u0019\u0001\u0000\u0000"+
		"\u0000\u00a3\u00a5\u0003\u001c\u000e\u0000\u00a4\u00a3\u0001\u0000\u0000"+
		"\u0000\u00a4\u00a5\u0001\u0000\u0000\u0000\u00a5\u00a7\u0001\u0000\u0000"+
		"\u0000\u00a6\u00a8\u0003\u001e\u000f\u0000\u00a7\u00a6\u0001\u0000\u0000"+
		"\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8\u001b\u0001\u0000\u0000"+
		"\u0000\u00a9\u00aa\u0005o\u0000\u0000\u00aa\u001d\u0001\u0000\u0000\u0000"+
		"\u00ab\u00ac\u0005i\u0000\u0000\u00ac\u001f\u0001\u0000\u0000\u0000\u00ad"+
		"\u00ae\u0007\u0001\u0000\u0000\u00ae!\u0001\u0000\u0000\u0000\u00af\u00b2"+
		"\u0003$\u0012\u0000\u00b0\u00b2\u0003&\u0013\u0000\u00b1\u00af\u0001\u0000"+
		"\u0000\u0000\u00b1\u00b0\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001\u0000"+
		"\u0000\u0000\u00b2\u00b4\u0001\u0000\u0000\u0000\u00b3\u00b5\u0003(\u0014"+
		"\u0000\u00b4\u00b3\u0001\u0000\u0000\u0000\u00b4\u00b5\u0001\u0000\u0000"+
		"\u0000\u00b5\u00b7\u0001\u0000\u0000\u0000\u00b6\u00b8\u0003,\u0016\u0000"+
		"\u00b7\u00b6\u0001\u0000\u0000\u0000\u00b7\u00b8\u0001\u0000\u0000\u0000"+
		"\u00b8\u00c4\u0001\u0000\u0000\u0000\u00b9\u00bb\u0003(\u0014\u0000\u00ba"+
		"\u00b9\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001\u0000\u0000\u0000\u00bb"+
		"\u00be\u0001\u0000\u0000\u0000\u00bc\u00bf\u0003$\u0012\u0000\u00bd\u00bf"+
		"\u0003&\u0013\u0000\u00be\u00bc\u0001\u0000\u0000\u0000\u00be\u00bd\u0001"+
		"\u0000\u0000\u0000\u00be\u00bf\u0001\u0000\u0000\u0000\u00bf\u00c1\u0001"+
		"\u0000\u0000\u0000\u00c0\u00c2\u0003,\u0016\u0000\u00c1\u00c0\u0001\u0000"+
		"\u0000\u0000\u00c1\u00c2\u0001\u0000\u0000\u0000\u00c2\u00c4\u0001\u0000"+
		"\u0000\u0000\u00c3\u00b1\u0001\u0000\u0000\u0000\u00c3\u00ba\u0001\u0000"+
		"\u0000\u0000\u00c4#\u0001\u0000\u0000\u0000\u00c5\u00c6\u0005\u0018\u0000"+
		"\u0000\u00c6\u00c7\u0003J%\u0000\u00c7%\u0001\u0000\u0000\u0000\u00c8"+
		"\u00c9\u0005\u0019\u0000\u0000\u00c9\u00ca\u0003J%\u0000\u00ca\'\u0001"+
		"\u0000\u0000\u0000\u00cb\u00cc\u0005\u000b\u0000\u0000\u00cc\u00cd\u0003"+
		"J%\u0000\u00cd)\u0001\u0000\u0000\u0000\u00ce\u00cf\u0005\f\u0000\u0000"+
		"\u00cf\u00d4\u0003.\u0017\u0000\u00d0\u00d1\u0005\u0003\u0000\u0000\u00d1"+
		"\u00d2\u00030\u0018\u0000\u00d2\u00d3\u0005\u0004\u0000\u0000\u00d3\u00d5"+
		"\u0001\u0000\u0000\u0000\u00d4\u00d0\u0001\u0000\u0000\u0000\u00d4\u00d5"+
		"\u0001\u0000\u0000\u0000\u00d5+\u0001\u0000\u0000\u0000\u00d6\u00d8\u0005"+
		">\u0000\u0000\u00d7\u00d6\u0001\u0000\u0000\u0000\u00d7\u00d8\u0001\u0000"+
		"\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9\u00da\u0007\u0002"+
		"\u0000\u0000\u00da\u00db\u00034\u001a\u0000\u00db-\u0001\u0000\u0000\u0000"+
		"\u00dc\u00dd\u0007\u0003\u0000\u0000\u00dd/\u0001\u0000\u0000\u0000\u00de"+
		"\u00e0\b\u0004\u0000\u0000\u00df\u00de\u0001\u0000\u0000\u0000\u00e0\u00e1"+
		"\u0001\u0000\u0000\u0000\u00e1\u00df\u0001\u0000\u0000\u0000\u00e1\u00e2"+
		"\u0001\u0000\u0000\u0000\u00e21\u0001\u0000\u0000\u0000\u00e3\u00e4\u0003"+
		"4\u001a\u0000\u00e43\u0001\u0000\u0000\u0000\u00e5\u00e6\u0006\u001a\uffff"+
		"\uffff\u0000\u00e6\u00e7\u00036\u001b\u0000\u00e7\u00ed\u0001\u0000\u0000"+
		"\u0000\u00e8\u00e9\n\u0002\u0000\u0000\u00e9\u00ea\u0007\u0005\u0000\u0000"+
		"\u00ea\u00ec\u00034\u001a\u0003\u00eb\u00e8\u0001\u0000\u0000\u0000\u00ec"+
		"\u00ef\u0001\u0000\u0000\u0000\u00ed\u00eb\u0001\u0000\u0000\u0000\u00ed"+
		"\u00ee\u0001\u0000\u0000\u0000\u00ee5\u0001\u0000\u0000\u0000\u00ef\u00ed"+
		"\u0001\u0000\u0000\u0000\u00f0\u00f8\u0003>\u001f\u0000\u00f1\u00f8\u0003"+
		"8\u001c\u0000\u00f2\u00f8\u0003.\u0017\u0000\u00f3\u00f4\u0005\u0005\u0000"+
		"\u0000\u00f4\u00f5\u00034\u001a\u0000\u00f5\u00f6\u0005\u0006\u0000\u0000"+
		"\u00f6\u00f8\u0001\u0000\u0000\u0000\u00f7\u00f0\u0001\u0000\u0000\u0000"+
		"\u00f7\u00f1\u0001\u0000\u0000\u0000\u00f7\u00f2\u0001\u0000\u0000\u0000"+
		"\u00f7\u00f3\u0001\u0000\u0000\u0000\u00f87\u0001\u0000\u0000\u0000\u00f9"+
		"\u00fa\u0003:\u001d\u0000\u00fa\u00fb\u0005\u0003\u0000\u0000\u00fb\u00fc"+
		"\u0003<\u001e\u0000\u00fc\u00fd\u0005\u0004\u0000\u0000\u00fd9\u0001\u0000"+
		"\u0000\u0000\u00fe\u0105\u0003>\u001f\u0000\u00ff\u0105\u0003.\u0017\u0000"+
		"\u0100\u0101\u0005\u0005\u0000\u0000\u0101\u0102\u00034\u001a\u0000\u0102"+
		"\u0103\u0005\u0006\u0000\u0000\u0103\u0105\u0001\u0000\u0000\u0000\u0104"+
		"\u00fe\u0001\u0000\u0000\u0000\u0104\u00ff\u0001\u0000\u0000\u0000\u0104"+
		"\u0100\u0001\u0000\u0000\u0000\u0105;\u0001\u0000\u0000\u0000\u0106\u010b"+
		"\u00034\u001a\u0000\u0107\u0108\u0005\t\u0000\u0000\u0108\u010a\u0003"+
		"4\u001a\u0000\u0109\u0107\u0001\u0000\u0000\u0000\u010a\u010d\u0001\u0000"+
		"\u0000\u0000\u010b\u0109\u0001\u0000\u0000\u0000\u010b\u010c\u0001\u0000"+
		"\u0000\u0000\u010c=\u0001\u0000\u0000\u0000\u010d\u010b\u0001\u0000\u0000"+
		"\u0000\u010e\u0111\u0003N\'\u0000\u010f\u0110\u0005\n\u0000\u0000\u0110"+
		"\u0112\u0003>\u001f\u0000\u0111\u010f\u0001\u0000\u0000\u0000\u0111\u0112"+
		"\u0001\u0000\u0000\u0000\u0112?\u0001\u0000\u0000\u0000\u0113\u0115\u0003"+
		"B!\u0000\u0114\u0113\u0001\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000"+
		"\u0000\u0115\u0117\u0001\u0000\u0000\u0000\u0116\u0118\u0003F#\u0000\u0117"+
		"\u0116\u0001\u0000\u0000\u0000\u0117\u0118\u0001\u0000\u0000\u0000\u0118"+
		"\u011a\u0001\u0000\u0000\u0000\u0119\u011b\u0003H$\u0000\u011a\u0119\u0001"+
		"\u0000\u0000\u0000\u011a\u011b\u0001\u0000\u0000\u0000\u011bA\u0001\u0000"+
		"\u0000\u0000\u011c\u0121\u0003D\"\u0000\u011d\u011e\u0005\u0012\u0000"+
		"\u0000\u011e\u0120\u0003D\"\u0000\u011f\u011d\u0001\u0000\u0000\u0000"+
		"\u0120\u0123\u0001\u0000\u0000\u0000\u0121\u011f\u0001\u0000\u0000\u0000"+
		"\u0121\u0122\u0001\u0000\u0000\u0000\u0122C\u0001\u0000\u0000\u0000\u0123"+
		"\u0121\u0001\u0000\u0000\u0000\u0124\u0126\u0003R)\u0000\u0125\u0127\u0003"+
		"(\u0014\u0000\u0126\u0125\u0001\u0000\u0000\u0000\u0126\u0127\u0001\u0000"+
		"\u0000\u0000\u0127E\u0001\u0000\u0000\u0000\u0128\u0129\u0005\u0003\u0000"+
		"\u0000\u0129\u012a\u0003*\u0015\u0000\u012a\u012b\u0005\u0004\u0000\u0000"+
		"\u012bG\u0001\u0000\u0000\u0000\u012c\u012d\u0005\u0014\u0000\u0000\u012d"+
		"\u0132\u0003J%\u0000\u012e\u012f\u0005\t\u0000\u0000\u012f\u0131\u0003"+
		"J%\u0000\u0130\u012e\u0001\u0000\u0000\u0000\u0131\u0134\u0001\u0000\u0000"+
		"\u0000\u0132\u0130\u0001\u0000\u0000\u0000\u0132\u0133\u0001\u0000\u0000"+
		"\u0000\u0133I\u0001\u0000\u0000\u0000\u0134\u0132\u0001\u0000\u0000\u0000"+
		"\u0135\u013a\u0003R)\u0000\u0136\u0137\u0005\u0017\u0000\u0000\u0137\u0139"+
		"\u0003R)\u0000\u0138\u0136\u0001\u0000\u0000\u0000\u0139\u013c\u0001\u0000"+
		"\u0000\u0000\u013a\u0138\u0001\u0000\u0000\u0000\u013a\u013b\u0001\u0000"+
		"\u0000\u0000\u013bK\u0001\u0000\u0000\u0000\u013c\u013a\u0001\u0000\u0000"+
		"\u0000\u013d\u013f\u0005\u0007\u0000\u0000\u013e\u0140\u0003R)\u0000\u013f"+
		"\u013e\u0001\u0000\u0000\u0000\u013f\u0140\u0001\u0000\u0000\u0000\u0140"+
		"\u0141\u0001\u0000\u0000\u0000\u0141\u0142\u0005\b\u0000\u0000\u0142M"+
		"\u0001\u0000\u0000\u0000\u0143\u0147\u0005\u0096\u0000\u0000\u0144\u0147"+
		"\u0005\u0001\u0000\u0000\u0145\u0147\u0003P(\u0000\u0146\u0143\u0001\u0000"+
		"\u0000\u0000\u0146\u0144\u0001\u0000\u0000\u0000\u0146\u0145\u0001\u0000"+
		"\u0000\u0000\u0147O\u0001\u0000\u0000\u0000\u0148\u014b\u0007\u0006\u0000"+
		"\u0000\u0149\u014a\u0005\u0017\u0000\u0000\u014a\u014c\u0007\u0006\u0000"+
		"\u0000\u014b\u0149\u0001\u0000\u0000\u0000\u014c\u014d\u0001\u0000\u0000"+
		"\u0000\u014d\u014b\u0001\u0000\u0000\u0000\u014d\u014e\u0001\u0000\u0000"+
		"\u0000\u014eQ\u0001\u0000\u0000\u0000\u014f\u0151\b\u0007\u0000\u0000"+
		"\u0150\u014f\u0001\u0000\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000"+
		"\u0152\u0150\u0001\u0000\u0000\u0000\u0152\u0153\u0001\u0000\u0000\u0000"+
		"\u0153S\u0001\u0000\u0000\u0000*VY\\_filorz|\u0085\u0087\u009e\u00a4\u00a7"+
		"\u00b1\u00b4\u00b7\u00ba\u00be\u00c1\u00c3\u00d4\u00d7\u00e1\u00ed\u00f7"+
		"\u0104\u010b\u0111\u0114\u0117\u011a\u0121\u0126\u0132\u013a\u013f\u0146"+
		"\u014d\u0152";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}