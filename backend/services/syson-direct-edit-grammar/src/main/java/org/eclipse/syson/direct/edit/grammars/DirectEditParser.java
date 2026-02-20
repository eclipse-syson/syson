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

// Generated from DirectEdit.g4 by ANTLR 4.13.2

package org.eclipse.syson.direct.edit.grammars;

import java.util.List;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class DirectEditParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

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
		CONJUGATE=56, CONJUGATES=57, CONJUGATION=58, CONNECTOR=59, CONST=60, CONST_PREFIX=61, 
		CONSTANT=62, CONSTANT_PREFIX=63, DATATYPE=64, DEFAULT=65, DEFAULT_SUFFIX=66, 
		DEPENDENCY=67, DERIVED=68, DERIVED_PREFIX=69, DIFFERENCES=70, DISJOINING=71, 
		DISJOINT=72, DOC=73, ELSE=74, END=75, END_PREFIX=76, EXPR=77, FALSE=78, 
		FEATURE=79, FEATURED=80, FEATURING=81, FILTER=82, FIRST=83, FLOW=84, FOR=85, 
		FROM=86, FUNCTION=87, HASTYPE=88, IF=89, INTERSECTS=90, IMPLIES=91, IMPORT=92, 
		IN=93, IN_PREFIX=94, INOUT=95, INOUT_PREFIX=96, INTERACTION=97, INV=98, 
		INVERSE=99, INVERTING=100, ISTYPE=101, LANGUAGE=102, MEMBER=103, METACLASS=104, 
		METADATA=105, MULTIPLICITY=106, NAMESPACE=107, NONUNIQUE=108, NONUNIQUE_SUFFIX=109, 
		NEW=110, NOT=111, NULL=112, OF=113, OR=114, ORDERED=115, ORDERED_SUFFIX=116, 
		OUT=117, OUT_PREFIX=118, PACKAGE=119, PORTION=120, PREDICATE=121, PRIVATE=122, 
		PROTECTED=123, PUBLIC=124, REDEFINES=125, REDEFINITION=126, REF=127, REF_PREFIX=128, 
		REFERENCES=129, REP=130, RETURN=131, SNAPSHOT_PREFIX=132, SPECIALIZTION=133, 
		SPECIALIZES=134, STEP=135, STRUCT=136, SUBCLASSIFIER=137, SUBSET=138, 
		SUBSETS=139, SUBTYPE=140, SUCCESSION=141, THEN=142, TIMESLICE_PREFIX=143, 
		TO=144, TRUE=145, TYPE=146, TYPED=147, TYPING=148, UNIONS=149, VAR=150, 
		VARIATION=151, VARIATION_PREFIX=152, VARIANT=153, VARIANT_PREFIX=154, 
		XOR_KEYWORD=155, REFNAME=156, ANY=157, XOR=158;
	public static final int
		RULE_nodeExpression = 0, RULE_listItemExpression = 1, RULE_prefixNodeExpression = 2, 
		RULE_portionKindExpression = 3, RULE_prefixListItemExpression = 4, RULE_directionPrefixExpression = 5, 
		RULE_abstractPrefixExpression = 6, RULE_variationPrefixExpression = 7, 
		RULE_variantPrefixExpression = 8, RULE_constantPrefixExpression = 9, RULE_derivedPrefixExpression = 10, 
		RULE_endPrefixExpression = 11, RULE_referenceExpression = 12, RULE_multiplicityExpression = 13, 
		RULE_multiplicityPropExpression = 14, RULE_orderedMultiplicityExpression = 15, 
		RULE_nonuniqueMultiplicityExpression = 16, RULE_multiplicityExpressionMember = 17, 
		RULE_featureExpressions = 18, RULE_subsettingExpression = 19, RULE_redefinitionExpression = 20, 
		RULE_typingExpression = 21, RULE_valueExpression = 22, RULE_featureValueExpression = 23, 
		RULE_literalExpression = 24, RULE_measurementExpression = 25, RULE_constraintExpression = 26, 
		RULE_expression = 27, RULE_primaryExpression = 28, RULE_bracketAccessExpression = 29, 
		RULE_primaryAtom = 30, RULE_sequenceExpressionList = 31, RULE_featureChainExpression = 32, 
		RULE_transitionExpression = 33, RULE_triggerExpression = 34, RULE_triggerExpressionName = 35, 
		RULE_guardExpression = 36, RULE_effectExpression = 37, RULE_qualifiedName = 38, 
		RULE_shortName = 39, RULE_refName = 40, RULE_qualifiedName2 = 41, RULE_name = 42;
	private static String[] makeRuleNames() {
		return new String[] {
			"nodeExpression", "listItemExpression", "prefixNodeExpression", "portionKindExpression", 
			"prefixListItemExpression", "directionPrefixExpression", "abstractPrefixExpression", 
			"variationPrefixExpression", "variantPrefixExpression", "constantPrefixExpression", 
			"derivedPrefixExpression", "endPrefixExpression", "referenceExpression", 
			"multiplicityExpression", "multiplicityPropExpression", "orderedMultiplicityExpression", 
			"nonuniqueMultiplicityExpression", "multiplicityExpressionMember", "featureExpressions", 
			"subsettingExpression", "redefinitionExpression", "typingExpression", 
			"valueExpression", "featureValueExpression", "literalExpression", "measurementExpression", 
			"constraintExpression", "expression", "primaryExpression", "bracketAccessExpression", 
			"primaryAtom", "sequenceExpressionList", "featureChainExpression", "transitionExpression", 
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
			"'connector'", "'const'", null, "'constant'", null, "'datatype'", "'default'", 
			null, "'dependency'", "'derived'", null, "'differences'", "'disjoining'", 
			"'disjoint'", "'doc'", "'else'", "'end'", null, "'expr'", "'false'", 
			"'feature'", "'featured'", "'featuring'", "'filter'", "'first'", "'flow'", 
			"'for'", "'from'", "'function'", "'hastype'", "'if'", "'intersects'", 
			"'implies'", "'import'", "'in'", null, "'inout'", null, "'interaction'", 
			"'inv'", "'inverse'", "'inverting'", "'istype'", "'language'", "'member'", 
			"'metaclass'", "'metadata'", "'multiplicity'", "'namespace'", "'nonunique'", 
			null, "'new'", "'not'", "'null'", "'of'", "'or'", "'ordered'", null, 
			"'out'", null, "'package'", "'portion'", "'predicate'", "'private'", 
			"'protected'", "'public'", "'redefines'", "'redefinition'", "'ref'", 
			null, "'references'", "'rep'", "'return'", "'snapshot'", "'specialization'", 
			"'specializes'", "'step'", "'struct'", "'subclassifier'", "'subset'", 
			"'subsets'", "'subtype'", "'succession'", "'then'", "'timeslice'", "'to'", 
			"'true'", "'type'", "'typed'", "'typing'", "'unions'", "'var'", "'variation'", 
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
			"CONNECTOR", "CONST", "CONST_PREFIX", "CONSTANT", "CONSTANT_PREFIX", 
			"DATATYPE", "DEFAULT", "DEFAULT_SUFFIX", "DEPENDENCY", "DERIVED", "DERIVED_PREFIX", 
			"DIFFERENCES", "DISJOINING", "DISJOINT", "DOC", "ELSE", "END", "END_PREFIX", 
			"EXPR", "FALSE", "FEATURE", "FEATURED", "FEATURING", "FILTER", "FIRST", 
			"FLOW", "FOR", "FROM", "FUNCTION", "HASTYPE", "IF", "INTERSECTS", "IMPLIES", 
			"IMPORT", "IN", "IN_PREFIX", "INOUT", "INOUT_PREFIX", "INTERACTION", 
			"INV", "INVERSE", "INVERTING", "ISTYPE", "LANGUAGE", "MEMBER", "METACLASS", 
			"METADATA", "MULTIPLICITY", "NAMESPACE", "NONUNIQUE", "NONUNIQUE_SUFFIX", 
			"NEW", "NOT", "NULL", "OF", "OR", "ORDERED", "ORDERED_SUFFIX", "OUT", 
			"OUT_PREFIX", "PACKAGE", "PORTION", "PREDICATE", "PRIVATE", "PROTECTED", 
			"PUBLIC", "REDEFINES", "REDEFINITION", "REF", "REF_PREFIX", "REFERENCES", 
			"REP", "RETURN", "SNAPSHOT_PREFIX", "SPECIALIZTION", "SPECIALIZES", "STEP", 
			"STRUCT", "SUBCLASSIFIER", "SUBSET", "SUBSETS", "SUBTYPE", "SUCCESSION", 
			"THEN", "TIMESLICE_PREFIX", "TO", "TRUE", "TYPE", "TYPED", "TYPING", 
			"UNIONS", "VAR", "VARIATION", "VARIATION_PREFIX", "VARIANT", "VARIANT_PREFIX", 
			"XOR_KEYWORD", "REFNAME", "ANY", "XOR"
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(86);
			prefixNodeExpression();
			setState(88);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(87);
				referenceExpression();
				}
				break;
			}
			setState(91);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(90);
				shortName();
				}
				break;
			}
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -125835274L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -4538783999459333L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 2147483647L) != 0)) {
				{
				setState(93);
				name();
				}
			}

			setState(97);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(96);
				multiplicityExpression();
				}
				break;
			}
			setState(99);
			featureExpressions();
			setState(100);
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

	@SuppressWarnings("CheckReturnValue")
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
			setState(102);
			prefixListItemExpression();
			setState(104);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(103);
				referenceExpression();
				}
				break;
			}
			setState(107);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(106);
				shortName();
				}
				break;
			}
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -125835274L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -4538783999459333L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 2147483647L) != 0)) {
				{
				setState(109);
				name();
				}
			}

			setState(113);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(112);
				multiplicityExpression();
				}
				break;
			}
			setState(116);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(115);
				multiplicityPropExpression();
				}
				break;
			}
			setState(118);
			featureExpressions();
			setState(119);
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

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixNodeExpressionContext extends ParserRuleContext {
		public List<PortionKindExpressionContext> portionKindExpression() {
			return getRuleContexts(PortionKindExpressionContext.class);
		}
		public PortionKindExpressionContext portionKindExpression(int i) {
			return getRuleContext(PortionKindExpressionContext.class,i);
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
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(121);
					portionKindExpression();
					}
					} 
				}
				setState(126);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			setState(132);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(130);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case ABSTRACT_PREFIX:
						{
						setState(127);
						abstractPrefixExpression();
						}
						break;
					case VARIATION_PREFIX:
						{
						setState(128);
						variationPrefixExpression();
						}
						break;
					case VARIANT_PREFIX:
						{
						setState(129);
						variantPrefixExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(134);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class PortionKindExpressionContext extends ParserRuleContext {
		public TerminalNode TIMESLICE_PREFIX() { return getToken(DirectEditParser.TIMESLICE_PREFIX, 0); }
		public TerminalNode SNAPSHOT_PREFIX() { return getToken(DirectEditParser.SNAPSHOT_PREFIX, 0); }
		public PortionKindExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_portionKindExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterPortionKindExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitPortionKindExpression(this);
		}
	}

	public final PortionKindExpressionContext portionKindExpression() throws RecognitionException {
		PortionKindExpressionContext _localctx = new PortionKindExpressionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_portionKindExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			_la = _input.LA(1);
			if ( !(_la==SNAPSHOT_PREFIX || _la==TIMESLICE_PREFIX) ) {
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

	@SuppressWarnings("CheckReturnValue")
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
		public List<ConstantPrefixExpressionContext> constantPrefixExpression() {
			return getRuleContexts(ConstantPrefixExpressionContext.class);
		}
		public ConstantPrefixExpressionContext constantPrefixExpression(int i) {
			return getRuleContext(ConstantPrefixExpressionContext.class,i);
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
		enterRule(_localctx, 8, RULE_prefixListItemExpression);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(143);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case IN_PREFIX:
					case INOUT_PREFIX:
					case OUT_PREFIX:
						{
						setState(137);
						directionPrefixExpression();
						}
						break;
					case ABSTRACT_PREFIX:
						{
						setState(138);
						abstractPrefixExpression();
						}
						break;
					case VARIATION_PREFIX:
						{
						setState(139);
						variationPrefixExpression();
						}
						break;
					case CONSTANT_PREFIX:
						{
						setState(140);
						constantPrefixExpression();
						}
						break;
					case DERIVED_PREFIX:
						{
						setState(141);
						derivedPrefixExpression();
						}
						break;
					case END_PREFIX:
						{
						setState(142);
						endPrefixExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(147);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 10, RULE_directionPrefixExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			_la = _input.LA(1);
			if ( !(((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 16777221L) != 0)) ) {
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 12, RULE_abstractPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 14, RULE_variationPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 16, RULE_variantPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ConstantPrefixExpressionContext extends ParserRuleContext {
		public TerminalNode CONSTANT_PREFIX() { return getToken(DirectEditParser.CONSTANT_PREFIX, 0); }
		public ConstantPrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantPrefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterConstantPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitConstantPrefixExpression(this);
		}
	}

	public final ConstantPrefixExpressionContext constantPrefixExpression() throws RecognitionException {
		ConstantPrefixExpressionContext _localctx = new ConstantPrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_constantPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			match(CONSTANT_PREFIX);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 20, RULE_derivedPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 22, RULE_endPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 24, RULE_referenceExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 26, RULE_multiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(LBRACKET);
			setState(168);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(165);
				((MultiplicityExpressionContext)_localctx).lowerBound = multiplicityExpressionMember();
				setState(166);
				match(DOTDOT);
				}
				break;
			}
			setState(170);
			((MultiplicityExpressionContext)_localctx).upperBound = multiplicityExpressionMember();
			setState(171);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 28, RULE_multiplicityPropExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDERED_SUFFIX) {
				{
				setState(173);
				orderedMultiplicityExpression();
				}
			}

			setState(177);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NONUNIQUE_SUFFIX) {
				{
				setState(176);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 30, RULE_orderedMultiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 32, RULE_nonuniqueMultiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 34, RULE_multiplicityExpressionMember);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
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

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureExpressionsContext extends ParserRuleContext {
		public TypingExpressionContext typingExpression() {
			return getRuleContext(TypingExpressionContext.class,0);
		}
		public FeatureValueExpressionContext featureValueExpression() {
			return getRuleContext(FeatureValueExpressionContext.class,0);
		}
		public RedefinitionExpressionContext redefinitionExpression() {
			return getRuleContext(RedefinitionExpressionContext.class,0);
		}
		public SubsettingExpressionContext subsettingExpression() {
			return getRuleContext(SubsettingExpressionContext.class,0);
		}
		public List<MultiplicityExpressionContext> multiplicityExpression() {
			return getRuleContexts(MultiplicityExpressionContext.class);
		}
		public MultiplicityExpressionContext multiplicityExpression(int i) {
			return getRuleContext(MultiplicityExpressionContext.class,i);
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
		enterRule(_localctx, 36, RULE_featureExpressions);
		int _la;
		try {
			setState(219);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUBSETS_OP || _la==REDEFINES_OP) {
					{
					setState(187);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case REDEFINES_OP:
						{
						setState(185);
						redefinitionExpression();
						}
						break;
					case SUBSETS_OP:
						{
						setState(186);
						subsettingExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(190);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LBRACKET) {
						{
						setState(189);
						multiplicityExpression();
						}
					}

					}
				}

				setState(198);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(194);
					typingExpression();
					setState(196);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LBRACKET) {
						{
						setState(195);
						multiplicityExpression();
						}
					}

					}
				}

				setState(201);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 12)) & ~0x3f) == 0 && ((1L << (_la - 12)) & 18014398509498369L) != 0)) {
					{
					setState(200);
					featureValueExpression();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(204);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(203);
					typingExpression();
					}
				}

				setState(213);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUBSETS_OP || _la==REDEFINES_OP) {
					{
					setState(208);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SUBSETS_OP:
						{
						setState(206);
						subsettingExpression();
						}
						break;
					case REDEFINES_OP:
						{
						setState(207);
						redefinitionExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(211);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LBRACKET) {
						{
						setState(210);
						multiplicityExpression();
						}
					}

					}
				}

				setState(216);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 12)) & ~0x3f) == 0 && ((1L << (_la - 12)) & 18014398509498369L) != 0)) {
					{
					setState(215);
					featureValueExpression();
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(218);
				multiplicityExpression();
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 38, RULE_subsettingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(SUBSETS_OP);
			setState(222);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 40, RULE_redefinitionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			match(REDEFINES_OP);
			setState(225);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 42, RULE_typingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			match(COLON);
			setState(228);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 44, RULE_valueExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(EQUALS);
			setState(231);
			literalExpression();
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(232);
				match(LBRACKET);
				setState(233);
				measurementExpression();
				setState(234);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 46, RULE_featureValueExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT_SUFFIX) {
				{
				setState(238);
				match(DEFAULT_SUFFIX);
				}
			}

			setState(241);
			_la = _input.LA(1);
			if ( !(_la==EQUALS || _la==ASSIGN_OP) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(242);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 48, RULE_literalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 257698037760L) != 0)) ) {
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 50, RULE_measurementExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(246);
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
				setState(249); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -18L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 2147483647L) != 0) );
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 52, RULE_constraintExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
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

	@SuppressWarnings("CheckReturnValue")
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
	@SuppressWarnings("CheckReturnValue")
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
	@SuppressWarnings("CheckReturnValue")
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
		int _startState = 54;
		enterRecursionRule(_localctx, 54, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new PrimaryExprContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(254);
			primaryExpression();
			}
			_ctx.stop = _input.LT(-1);
			setState(261);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new BinaryOperationExprContext(new ExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_expression);
					setState(256);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(257);
					((BinaryOperationExprContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 17050886528L) != 0) || _la==XOR_KEYWORD || _la==XOR) ) {
						((BinaryOperationExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(258);
					expression(3);
					}
					} 
				}
				setState(263);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 56, RULE_primaryExpression);
		try {
			setState(271);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(264);
				featureChainExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(265);
				bracketAccessExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(266);
				literalExpression();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(267);
				match(LPAREN);
				setState(268);
				expression(0);
				setState(269);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 58, RULE_bracketAccessExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			primaryAtom();
			setState(274);
			match(LBRACKET);
			setState(275);
			sequenceExpressionList();
			setState(276);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 60, RULE_primaryAtom);
		try {
			setState(284);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ESPACED_NAME:
			case REFNAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(278);
				featureChainExpression();
				}
				break;
			case Boolean:
			case Integer:
			case Real:
			case DoubleQuotedString:
				enterOuterAlt(_localctx, 2);
				{
				setState(279);
				literalExpression();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 3);
				{
				setState(280);
				match(LPAREN);
				setState(281);
				expression(0);
				setState(282);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 62, RULE_sequenceExpressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(286);
			expression(0);
			setState(291);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(287);
				match(COMMA);
				setState(288);
				expression(0);
				}
				}
				setState(293);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 64, RULE_featureChainExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(294);
			refName();
			setState(297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(295);
				match(DOT);
				setState(296);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 66, RULE_transitionExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				{
				setState(299);
				triggerExpression();
				}
				break;
			}
			setState(303);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(302);
				guardExpression();
				}
			}

			setState(306);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SLASH) {
				{
				setState(305);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 68, RULE_triggerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(308);
			triggerExpressionName();
			setState(313);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PIPE) {
				{
				{
				setState(309);
				match(PIPE);
				setState(310);
				triggerExpressionName();
				}
				}
				setState(315);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 70, RULE_triggerExpressionName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			name();
			setState(318);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(317);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 72, RULE_guardExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			match(LBRACKET);
			setState(321);
			valueExpression();
			setState(322);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 74, RULE_effectExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			match(SLASH);
			setState(325);
			qualifiedName();
			setState(330);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(326);
				match(COMMA);
				setState(327);
				qualifiedName();
				}
				}
				setState(332);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 76, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
			name();
			setState(338);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAMESPACE_SEP) {
				{
				{
				setState(334);
				match(NAMESPACE_SEP);
				setState(335);
				name();
				}
				}
				setState(340);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 78, RULE_shortName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(341);
			match(LT);
			setState(343);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(342);
				name();
				}
				break;
			}
			setState(345);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 80, RULE_refName);
		try {
			setState(350);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(347);
				match(REFNAME);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(348);
				match(ESPACED_NAME);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(349);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 82, RULE_qualifiedName2);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(352);
			_la = _input.LA(1);
			if ( !(_la==ESPACED_NAME || _la==REFNAME) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(355); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(353);
					match(NAMESPACE_SEP);
					setState(354);
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
				setState(357); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
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
		enterRule(_localctx, 84, RULE_name);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(360); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(359);
					_la = _input.LA(1);
					if ( _la <= 0 || ((((_la) & ~0x3f) == 0 && ((1L << _la) & 125835272L) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 1134695999864833L) != 0)) ) {
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
				setState(362); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
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
		case 27:
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
		"\u0004\u0001\u009e\u016d\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0001\u0000\u0001\u0000\u0003"+
		"\u0000Y\b\u0000\u0001\u0000\u0003\u0000\\\b\u0000\u0001\u0000\u0003\u0000"+
		"_\b\u0000\u0001\u0000\u0003\u0000b\b\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0003\u0001i\b\u0001\u0001\u0001\u0003"+
		"\u0001l\b\u0001\u0001\u0001\u0003\u0001o\b\u0001\u0001\u0001\u0003\u0001"+
		"r\b\u0001\u0001\u0001\u0003\u0001u\b\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0005\u0002{\b\u0002\n\u0002\f\u0002~\t\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u0083\b\u0002\n\u0002\f\u0002"+
		"\u0086\t\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u0090\b\u0004\n\u0004"+
		"\f\u0004\u0093\t\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0003\r\u00a9\b\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0003\u000e\u00af"+
		"\b\u000e\u0001\u000e\u0003\u000e\u00b2\b\u000e\u0001\u000f\u0001\u000f"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0003\u0012\u00bc\b\u0012\u0001\u0012\u0003\u0012\u00bf\b\u0012\u0003"+
		"\u0012\u00c1\b\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00c5\b\u0012"+
		"\u0003\u0012\u00c7\b\u0012\u0001\u0012\u0003\u0012\u00ca\b\u0012\u0001"+
		"\u0012\u0003\u0012\u00cd\b\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00d1"+
		"\b\u0012\u0001\u0012\u0003\u0012\u00d4\b\u0012\u0003\u0012\u00d6\b\u0012"+
		"\u0001\u0012\u0003\u0012\u00d9\b\u0012\u0001\u0012\u0003\u0012\u00dc\b"+
		"\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u00ed\b\u0016\u0001"+
		"\u0017\u0003\u0017\u00f0\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0019\u0004\u0019\u00f8\b\u0019\u000b\u0019\f"+
		"\u0019\u00f9\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0005\u001b\u0104\b\u001b\n\u001b"+
		"\f\u001b\u0107\t\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u0110\b\u001c\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u011d\b\u001e"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u0122\b\u001f\n\u001f"+
		"\f\u001f\u0125\t\u001f\u0001 \u0001 \u0001 \u0003 \u012a\b \u0001!\u0003"+
		"!\u012d\b!\u0001!\u0003!\u0130\b!\u0001!\u0003!\u0133\b!\u0001\"\u0001"+
		"\"\u0001\"\u0005\"\u0138\b\"\n\"\f\"\u013b\t\"\u0001#\u0001#\u0003#\u013f"+
		"\b#\u0001$\u0001$\u0001$\u0001$\u0001%\u0001%\u0001%\u0001%\u0005%\u0149"+
		"\b%\n%\f%\u014c\t%\u0001&\u0001&\u0001&\u0005&\u0151\b&\n&\f&\u0154\t"+
		"&\u0001\'\u0001\'\u0003\'\u0158\b\'\u0001\'\u0001\'\u0001(\u0001(\u0001"+
		"(\u0003(\u015f\b(\u0001)\u0001)\u0001)\u0004)\u0164\b)\u000b)\f)\u0165"+
		"\u0001*\u0004*\u0169\b*\u000b*\f*\u016a\u0001*\u0000\u00016+\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		" \"$&(*,.02468:<>@BDFHJLNPRT\u0000\t\u0002\u0000\u0084\u0084\u008f\u008f"+
		"\u0003\u0000^^``vv\u0002\u0000\u000f\u000f##\u0002\u0000\f\f\u001a\u001a"+
		"\u0001\u0000\"%\u0001\u0000\u0004\u0004\u0006\u0000\u0007\b\r\u0013\u0016"+
		"\u0016\u001b!\u009b\u009b\u009e\u009e\u0002\u0000\u0001\u0001\u009c\u009c"+
		"\u0006\u0000\u0003\u0003\u000b\f\u0017\u001aBBmmtt\u017b\u0000V\u0001"+
		"\u0000\u0000\u0000\u0002f\u0001\u0000\u0000\u0000\u0004|\u0001\u0000\u0000"+
		"\u0000\u0006\u0087\u0001\u0000\u0000\u0000\b\u0091\u0001\u0000\u0000\u0000"+
		"\n\u0094\u0001\u0000\u0000\u0000\f\u0096\u0001\u0000\u0000\u0000\u000e"+
		"\u0098\u0001\u0000\u0000\u0000\u0010\u009a\u0001\u0000\u0000\u0000\u0012"+
		"\u009c\u0001\u0000\u0000\u0000\u0014\u009e\u0001\u0000\u0000\u0000\u0016"+
		"\u00a0\u0001\u0000\u0000\u0000\u0018\u00a2\u0001\u0000\u0000\u0000\u001a"+
		"\u00a4\u0001\u0000\u0000\u0000\u001c\u00ae\u0001\u0000\u0000\u0000\u001e"+
		"\u00b3\u0001\u0000\u0000\u0000 \u00b5\u0001\u0000\u0000\u0000\"\u00b7"+
		"\u0001\u0000\u0000\u0000$\u00db\u0001\u0000\u0000\u0000&\u00dd\u0001\u0000"+
		"\u0000\u0000(\u00e0\u0001\u0000\u0000\u0000*\u00e3\u0001\u0000\u0000\u0000"+
		",\u00e6\u0001\u0000\u0000\u0000.\u00ef\u0001\u0000\u0000\u00000\u00f4"+
		"\u0001\u0000\u0000\u00002\u00f7\u0001\u0000\u0000\u00004\u00fb\u0001\u0000"+
		"\u0000\u00006\u00fd\u0001\u0000\u0000\u00008\u010f\u0001\u0000\u0000\u0000"+
		":\u0111\u0001\u0000\u0000\u0000<\u011c\u0001\u0000\u0000\u0000>\u011e"+
		"\u0001\u0000\u0000\u0000@\u0126\u0001\u0000\u0000\u0000B\u012c\u0001\u0000"+
		"\u0000\u0000D\u0134\u0001\u0000\u0000\u0000F\u013c\u0001\u0000\u0000\u0000"+
		"H\u0140\u0001\u0000\u0000\u0000J\u0144\u0001\u0000\u0000\u0000L\u014d"+
		"\u0001\u0000\u0000\u0000N\u0155\u0001\u0000\u0000\u0000P\u015e\u0001\u0000"+
		"\u0000\u0000R\u0160\u0001\u0000\u0000\u0000T\u0168\u0001\u0000\u0000\u0000"+
		"VX\u0003\u0004\u0002\u0000WY\u0003\u0018\f\u0000XW\u0001\u0000\u0000\u0000"+
		"XY\u0001\u0000\u0000\u0000Y[\u0001\u0000\u0000\u0000Z\\\u0003N\'\u0000"+
		"[Z\u0001\u0000\u0000\u0000[\\\u0001\u0000\u0000\u0000\\^\u0001\u0000\u0000"+
		"\u0000]_\u0003T*\u0000^]\u0001\u0000\u0000\u0000^_\u0001\u0000\u0000\u0000"+
		"_a\u0001\u0000\u0000\u0000`b\u0003\u001a\r\u0000a`\u0001\u0000\u0000\u0000"+
		"ab\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000cd\u0003$\u0012\u0000"+
		"de\u0005\u0000\u0000\u0001e\u0001\u0001\u0000\u0000\u0000fh\u0003\b\u0004"+
		"\u0000gi\u0003\u0018\f\u0000hg\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000"+
		"\u0000ik\u0001\u0000\u0000\u0000jl\u0003N\'\u0000kj\u0001\u0000\u0000"+
		"\u0000kl\u0001\u0000\u0000\u0000ln\u0001\u0000\u0000\u0000mo\u0003T*\u0000"+
		"nm\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000oq\u0001\u0000\u0000"+
		"\u0000pr\u0003\u001a\r\u0000qp\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000"+
		"\u0000rt\u0001\u0000\u0000\u0000su\u0003\u001c\u000e\u0000ts\u0001\u0000"+
		"\u0000\u0000tu\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vw\u0003"+
		"$\u0012\u0000wx\u0005\u0000\u0000\u0001x\u0003\u0001\u0000\u0000\u0000"+
		"y{\u0003\u0006\u0003\u0000zy\u0001\u0000\u0000\u0000{~\u0001\u0000\u0000"+
		"\u0000|z\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}\u0084\u0001"+
		"\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000\u007f\u0083\u0003\f\u0006"+
		"\u0000\u0080\u0083\u0003\u000e\u0007\u0000\u0081\u0083\u0003\u0010\b\u0000"+
		"\u0082\u007f\u0001\u0000\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000"+
		"\u0082\u0081\u0001\u0000\u0000\u0000\u0083\u0086\u0001\u0000\u0000\u0000"+
		"\u0084\u0082\u0001\u0000\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000"+
		"\u0085\u0005\u0001\u0000\u0000\u0000\u0086\u0084\u0001\u0000\u0000\u0000"+
		"\u0087\u0088\u0007\u0000\u0000\u0000\u0088\u0007\u0001\u0000\u0000\u0000"+
		"\u0089\u0090\u0003\n\u0005\u0000\u008a\u0090\u0003\f\u0006\u0000\u008b"+
		"\u0090\u0003\u000e\u0007\u0000\u008c\u0090\u0003\u0012\t\u0000\u008d\u0090"+
		"\u0003\u0014\n\u0000\u008e\u0090\u0003\u0016\u000b\u0000\u008f\u0089\u0001"+
		"\u0000\u0000\u0000\u008f\u008a\u0001\u0000\u0000\u0000\u008f\u008b\u0001"+
		"\u0000\u0000\u0000\u008f\u008c\u0001\u0000\u0000\u0000\u008f\u008d\u0001"+
		"\u0000\u0000\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u0090\u0093\u0001"+
		"\u0000\u0000\u0000\u0091\u008f\u0001\u0000\u0000\u0000\u0091\u0092\u0001"+
		"\u0000\u0000\u0000\u0092\t\u0001\u0000\u0000\u0000\u0093\u0091\u0001\u0000"+
		"\u0000\u0000\u0094\u0095\u0007\u0001\u0000\u0000\u0095\u000b\u0001\u0000"+
		"\u0000\u0000\u0096\u0097\u0005(\u0000\u0000\u0097\r\u0001\u0000\u0000"+
		"\u0000\u0098\u0099\u0005\u0098\u0000\u0000\u0099\u000f\u0001\u0000\u0000"+
		"\u0000\u009a\u009b\u0005\u009a\u0000\u0000\u009b\u0011\u0001\u0000\u0000"+
		"\u0000\u009c\u009d\u0005?\u0000\u0000\u009d\u0013\u0001\u0000\u0000\u0000"+
		"\u009e\u009f\u0005E\u0000\u0000\u009f\u0015\u0001\u0000\u0000\u0000\u00a0"+
		"\u00a1\u0005L\u0000\u0000\u00a1\u0017\u0001\u0000\u0000\u0000\u00a2\u00a3"+
		"\u0005\u0080\u0000\u0000\u00a3\u0019\u0001\u0000\u0000\u0000\u00a4\u00a8"+
		"\u0005\u0003\u0000\u0000\u00a5\u00a6\u0003\"\u0011\u0000\u00a6\u00a7\u0005"+
		"\u0016\u0000\u0000\u00a7\u00a9\u0001\u0000\u0000\u0000\u00a8\u00a5\u0001"+
		"\u0000\u0000\u0000\u00a8\u00a9\u0001\u0000\u0000\u0000\u00a9\u00aa\u0001"+
		"\u0000\u0000\u0000\u00aa\u00ab\u0003\"\u0011\u0000\u00ab\u00ac\u0005\u0004"+
		"\u0000\u0000\u00ac\u001b\u0001\u0000\u0000\u0000\u00ad\u00af\u0003\u001e"+
		"\u000f\u0000\u00ae\u00ad\u0001\u0000\u0000\u0000\u00ae\u00af\u0001\u0000"+
		"\u0000\u0000\u00af\u00b1\u0001\u0000\u0000\u0000\u00b0\u00b2\u0003 \u0010"+
		"\u0000\u00b1\u00b0\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001\u0000\u0000"+
		"\u0000\u00b2\u001d\u0001\u0000\u0000\u0000\u00b3\u00b4\u0005t\u0000\u0000"+
		"\u00b4\u001f\u0001\u0000\u0000\u0000\u00b5\u00b6\u0005m\u0000\u0000\u00b6"+
		"!\u0001\u0000\u0000\u0000\u00b7\u00b8\u0007\u0002\u0000\u0000\u00b8#\u0001"+
		"\u0000\u0000\u0000\u00b9\u00bc\u0003(\u0014\u0000\u00ba\u00bc\u0003&\u0013"+
		"\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bb\u00ba\u0001\u0000\u0000"+
		"\u0000\u00bc\u00be\u0001\u0000\u0000\u0000\u00bd\u00bf\u0003\u001a\r\u0000"+
		"\u00be\u00bd\u0001\u0000\u0000\u0000\u00be\u00bf\u0001\u0000\u0000\u0000"+
		"\u00bf\u00c1\u0001\u0000\u0000\u0000\u00c0\u00bb\u0001\u0000\u0000\u0000"+
		"\u00c0\u00c1\u0001\u0000\u0000\u0000\u00c1\u00c6\u0001\u0000\u0000\u0000"+
		"\u00c2\u00c4\u0003*\u0015\u0000\u00c3\u00c5\u0003\u001a\r\u0000\u00c4"+
		"\u00c3\u0001\u0000\u0000\u0000\u00c4\u00c5\u0001\u0000\u0000\u0000\u00c5"+
		"\u00c7\u0001\u0000\u0000\u0000\u00c6\u00c2\u0001\u0000\u0000\u0000\u00c6"+
		"\u00c7\u0001\u0000\u0000\u0000\u00c7\u00c9\u0001\u0000\u0000\u0000\u00c8"+
		"\u00ca\u0003.\u0017\u0000\u00c9\u00c8\u0001\u0000\u0000\u0000\u00c9\u00ca"+
		"\u0001\u0000\u0000\u0000\u00ca\u00dc\u0001\u0000\u0000\u0000\u00cb\u00cd"+
		"\u0003*\u0015\u0000\u00cc\u00cb\u0001\u0000\u0000\u0000\u00cc\u00cd\u0001"+
		"\u0000\u0000\u0000\u00cd\u00d5\u0001\u0000\u0000\u0000\u00ce\u00d1\u0003"+
		"&\u0013\u0000\u00cf\u00d1\u0003(\u0014\u0000\u00d0\u00ce\u0001\u0000\u0000"+
		"\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d1\u00d3\u0001\u0000\u0000"+
		"\u0000\u00d2\u00d4\u0003\u001a\r\u0000\u00d3\u00d2\u0001\u0000\u0000\u0000"+
		"\u00d3\u00d4\u0001\u0000\u0000\u0000\u00d4\u00d6\u0001\u0000\u0000\u0000"+
		"\u00d5\u00d0\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000\u0000\u0000"+
		"\u00d6\u00d8\u0001\u0000\u0000\u0000\u00d7\u00d9\u0003.\u0017\u0000\u00d8"+
		"\u00d7\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9"+
		"\u00dc\u0001\u0000\u0000\u0000\u00da\u00dc\u0003\u001a\r\u0000\u00db\u00c0"+
		"\u0001\u0000\u0000\u0000\u00db\u00cc\u0001\u0000\u0000\u0000\u00db\u00da"+
		"\u0001\u0000\u0000\u0000\u00dc%\u0001\u0000\u0000\u0000\u00dd\u00de\u0005"+
		"\u0018\u0000\u0000\u00de\u00df\u0003L&\u0000\u00df\'\u0001\u0000\u0000"+
		"\u0000\u00e0\u00e1\u0005\u0019\u0000\u0000\u00e1\u00e2\u0003L&\u0000\u00e2"+
		")\u0001\u0000\u0000\u0000\u00e3\u00e4\u0005\u000b\u0000\u0000\u00e4\u00e5"+
		"\u0003L&\u0000\u00e5+\u0001\u0000\u0000\u0000\u00e6\u00e7\u0005\f\u0000"+
		"\u0000\u00e7\u00ec\u00030\u0018\u0000\u00e8\u00e9\u0005\u0003\u0000\u0000"+
		"\u00e9\u00ea\u00032\u0019\u0000\u00ea\u00eb\u0005\u0004\u0000\u0000\u00eb"+
		"\u00ed\u0001\u0000\u0000\u0000\u00ec\u00e8\u0001\u0000\u0000\u0000\u00ec"+
		"\u00ed\u0001\u0000\u0000\u0000\u00ed-\u0001\u0000\u0000\u0000\u00ee\u00f0"+
		"\u0005B\u0000\u0000\u00ef\u00ee\u0001\u0000\u0000\u0000\u00ef\u00f0\u0001"+
		"\u0000\u0000\u0000\u00f0\u00f1\u0001\u0000\u0000\u0000\u00f1\u00f2\u0007"+
		"\u0003\u0000\u0000\u00f2\u00f3\u00036\u001b\u0000\u00f3/\u0001\u0000\u0000"+
		"\u0000\u00f4\u00f5\u0007\u0004\u0000\u0000\u00f51\u0001\u0000\u0000\u0000"+
		"\u00f6\u00f8\b\u0005\u0000\u0000\u00f7\u00f6\u0001\u0000\u0000\u0000\u00f8"+
		"\u00f9\u0001\u0000\u0000\u0000\u00f9\u00f7\u0001\u0000\u0000\u0000\u00f9"+
		"\u00fa\u0001\u0000\u0000\u0000\u00fa3\u0001\u0000\u0000\u0000\u00fb\u00fc"+
		"\u00036\u001b\u0000\u00fc5\u0001\u0000\u0000\u0000\u00fd\u00fe\u0006\u001b"+
		"\uffff\uffff\u0000\u00fe\u00ff\u00038\u001c\u0000\u00ff\u0105\u0001\u0000"+
		"\u0000\u0000\u0100\u0101\n\u0002\u0000\u0000\u0101\u0102\u0007\u0006\u0000"+
		"\u0000\u0102\u0104\u00036\u001b\u0003\u0103\u0100\u0001\u0000\u0000\u0000"+
		"\u0104\u0107\u0001\u0000\u0000\u0000\u0105\u0103\u0001\u0000\u0000\u0000"+
		"\u0105\u0106\u0001\u0000\u0000\u0000\u01067\u0001\u0000\u0000\u0000\u0107"+
		"\u0105\u0001\u0000\u0000\u0000\u0108\u0110\u0003@ \u0000\u0109\u0110\u0003"+
		":\u001d\u0000\u010a\u0110\u00030\u0018\u0000\u010b\u010c\u0005\u0005\u0000"+
		"\u0000\u010c\u010d\u00036\u001b\u0000\u010d\u010e\u0005\u0006\u0000\u0000"+
		"\u010e\u0110\u0001\u0000\u0000\u0000\u010f\u0108\u0001\u0000\u0000\u0000"+
		"\u010f\u0109\u0001\u0000\u0000\u0000\u010f\u010a\u0001\u0000\u0000\u0000"+
		"\u010f\u010b\u0001\u0000\u0000\u0000\u01109\u0001\u0000\u0000\u0000\u0111"+
		"\u0112\u0003<\u001e\u0000\u0112\u0113\u0005\u0003\u0000\u0000\u0113\u0114"+
		"\u0003>\u001f\u0000\u0114\u0115\u0005\u0004\u0000\u0000\u0115;\u0001\u0000"+
		"\u0000\u0000\u0116\u011d\u0003@ \u0000\u0117\u011d\u00030\u0018\u0000"+
		"\u0118\u0119\u0005\u0005\u0000\u0000\u0119\u011a\u00036\u001b\u0000\u011a"+
		"\u011b\u0005\u0006\u0000\u0000\u011b\u011d\u0001\u0000\u0000\u0000\u011c"+
		"\u0116\u0001\u0000\u0000\u0000\u011c\u0117\u0001\u0000\u0000\u0000\u011c"+
		"\u0118\u0001\u0000\u0000\u0000\u011d=\u0001\u0000\u0000\u0000\u011e\u0123"+
		"\u00036\u001b\u0000\u011f\u0120\u0005\t\u0000\u0000\u0120\u0122\u0003"+
		"6\u001b\u0000\u0121\u011f\u0001\u0000\u0000\u0000\u0122\u0125\u0001\u0000"+
		"\u0000\u0000\u0123\u0121\u0001\u0000\u0000\u0000\u0123\u0124\u0001\u0000"+
		"\u0000\u0000\u0124?\u0001\u0000\u0000\u0000\u0125\u0123\u0001\u0000\u0000"+
		"\u0000\u0126\u0129\u0003P(\u0000\u0127\u0128\u0005\n\u0000\u0000\u0128"+
		"\u012a\u0003@ \u0000\u0129\u0127\u0001\u0000\u0000\u0000\u0129\u012a\u0001"+
		"\u0000\u0000\u0000\u012aA\u0001\u0000\u0000\u0000\u012b\u012d\u0003D\""+
		"\u0000\u012c\u012b\u0001\u0000\u0000\u0000\u012c\u012d\u0001\u0000\u0000"+
		"\u0000\u012d\u012f\u0001\u0000\u0000\u0000\u012e\u0130\u0003H$\u0000\u012f"+
		"\u012e\u0001\u0000\u0000\u0000\u012f\u0130\u0001\u0000\u0000\u0000\u0130"+
		"\u0132\u0001\u0000\u0000\u0000\u0131\u0133\u0003J%\u0000\u0132\u0131\u0001"+
		"\u0000\u0000\u0000\u0132\u0133\u0001\u0000\u0000\u0000\u0133C\u0001\u0000"+
		"\u0000\u0000\u0134\u0139\u0003F#\u0000\u0135\u0136\u0005\u0012\u0000\u0000"+
		"\u0136\u0138\u0003F#\u0000\u0137\u0135\u0001\u0000\u0000\u0000\u0138\u013b"+
		"\u0001\u0000\u0000\u0000\u0139\u0137\u0001\u0000\u0000\u0000\u0139\u013a"+
		"\u0001\u0000\u0000\u0000\u013aE\u0001\u0000\u0000\u0000\u013b\u0139\u0001"+
		"\u0000\u0000\u0000\u013c\u013e\u0003T*\u0000\u013d\u013f\u0003*\u0015"+
		"\u0000\u013e\u013d\u0001\u0000\u0000\u0000\u013e\u013f\u0001\u0000\u0000"+
		"\u0000\u013fG\u0001\u0000\u0000\u0000\u0140\u0141\u0005\u0003\u0000\u0000"+
		"\u0141\u0142\u0003,\u0016\u0000\u0142\u0143\u0005\u0004\u0000\u0000\u0143"+
		"I\u0001\u0000\u0000\u0000\u0144\u0145\u0005\u0014\u0000\u0000\u0145\u014a"+
		"\u0003L&\u0000\u0146\u0147\u0005\t\u0000\u0000\u0147\u0149\u0003L&\u0000"+
		"\u0148\u0146\u0001\u0000\u0000\u0000\u0149\u014c\u0001\u0000\u0000\u0000"+
		"\u014a\u0148\u0001\u0000\u0000\u0000\u014a\u014b\u0001\u0000\u0000\u0000"+
		"\u014bK\u0001\u0000\u0000\u0000\u014c\u014a\u0001\u0000\u0000\u0000\u014d"+
		"\u0152\u0003T*\u0000\u014e\u014f\u0005\u0017\u0000\u0000\u014f\u0151\u0003"+
		"T*\u0000\u0150\u014e\u0001\u0000\u0000\u0000\u0151\u0154\u0001\u0000\u0000"+
		"\u0000\u0152\u0150\u0001\u0000\u0000\u0000\u0152\u0153\u0001\u0000\u0000"+
		"\u0000\u0153M\u0001\u0000\u0000\u0000\u0154\u0152\u0001\u0000\u0000\u0000"+
		"\u0155\u0157\u0005\u0007\u0000\u0000\u0156\u0158\u0003T*\u0000\u0157\u0156"+
		"\u0001\u0000\u0000\u0000\u0157\u0158\u0001\u0000\u0000\u0000\u0158\u0159"+
		"\u0001\u0000\u0000\u0000\u0159\u015a\u0005\b\u0000\u0000\u015aO\u0001"+
		"\u0000\u0000\u0000\u015b\u015f\u0005\u009c\u0000\u0000\u015c\u015f\u0005"+
		"\u0001\u0000\u0000\u015d\u015f\u0003R)\u0000\u015e\u015b\u0001\u0000\u0000"+
		"\u0000\u015e\u015c\u0001\u0000\u0000\u0000\u015e\u015d\u0001\u0000\u0000"+
		"\u0000\u015fQ\u0001\u0000\u0000\u0000\u0160\u0163\u0007\u0007\u0000\u0000"+
		"\u0161\u0162\u0005\u0017\u0000\u0000\u0162\u0164\u0007\u0007\u0000\u0000"+
		"\u0163\u0161\u0001\u0000\u0000\u0000\u0164\u0165\u0001\u0000\u0000\u0000"+
		"\u0165\u0163\u0001\u0000\u0000\u0000\u0165\u0166\u0001\u0000\u0000\u0000"+
		"\u0166S\u0001\u0000\u0000\u0000\u0167\u0169\b\b\u0000\u0000\u0168\u0167"+
		"\u0001\u0000\u0000\u0000\u0169\u016a\u0001\u0000\u0000\u0000\u016a\u0168"+
		"\u0001\u0000\u0000\u0000\u016a\u016b\u0001\u0000\u0000\u0000\u016bU\u0001"+
		"\u0000\u0000\u00000X[^ahknqt|\u0082\u0084\u008f\u0091\u00a8\u00ae\u00b1"+
		"\u00bb\u00be\u00c0\u00c4\u00c6\u00c9\u00cc\u00d0\u00d3\u00d5\u00d8\u00db"+
		"\u00ec\u00ef\u00f9\u0105\u010f\u011c\u0123\u0129\u012c\u012f\u0132\u0139"+
		"\u013e\u014a\u0152\u0157\u015e\u0165\u016a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
