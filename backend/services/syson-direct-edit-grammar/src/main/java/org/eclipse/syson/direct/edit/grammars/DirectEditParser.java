// Generated from DirectEdit.g4 by ANTLR 4.13.2

/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
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
package org.eclipse.syson.direct.edit.grammars;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

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
		REFERENCES=129, REP=130, RETURN=131, SNAPSHOT=132, SNAPSHOT_PREFIX=133, 
		SPECIALIZTION=134, SPECIALIZES=135, STEP=136, STRUCT=137, SUBCLASSIFIER=138, 
		SUBSET=139, SUBSETS=140, SUBTYPE=141, SUCCESSION=142, THEN=143, TIMESLICE=144, 
		TIMESLICE_PREFIX=145, TO=146, TRUE=147, TYPE=148, TYPED=149, TYPING=150, 
		UNIONS=151, VAR=152, VARIATION=153, VARIATION_PREFIX=154, VARIANT=155, 
		VARIANT_PREFIX=156, XOR_KEYWORD=157, REFNAME=158, ANY=159, XOR=160;
	public static final int
		RULE_nodeExpression = 0, RULE_listItemExpression = 1, RULE_prefixNodeExpression = 2, 
		RULE_portionKindPrefixExpression = 3, RULE_prefixListItemExpression = 4, 
		RULE_directionPrefixExpression = 5, RULE_abstractPrefixExpression = 6, 
		RULE_variationPrefixExpression = 7, RULE_variantPrefixExpression = 8, 
		RULE_constantPrefixExpression = 9, RULE_derivedPrefixExpression = 10, 
		RULE_endPrefixExpression = 11, RULE_referenceExpression = 12, RULE_multiplicityExpression = 13, 
		RULE_multiplicityPropExpression = 14, RULE_orderedMultiplicityExpression = 15, 
		RULE_nonuniqueMultiplicityExpression = 16, RULE_multiplicityExpressionMember = 17, 
		RULE_featureExpressions = 18, RULE_subsettingExpression = 19, RULE_redefinitionExpression = 20, 
		RULE_typingExpression = 21, RULE_literalExpression = 22, RULE_measurementExpression = 23, 
		RULE_constraintExpression = 24, RULE_expression = 25, RULE_primaryExpression = 26, 
		RULE_bracketAccessExpression = 27, RULE_primaryAtom = 28, RULE_sequenceExpressionList = 29, 
		RULE_featureChainExpression = 30, RULE_qualifiedName = 31, RULE_shortName = 32, 
		RULE_refName = 33, RULE_qualifiedName2 = 34, RULE_name = 35;
	private static String[] makeRuleNames() {
		return new String[] {
			"nodeExpression", "listItemExpression", "prefixNodeExpression", "portionKindPrefixExpression", 
			"prefixListItemExpression", "directionPrefixExpression", "abstractPrefixExpression", 
			"variationPrefixExpression", "variantPrefixExpression", "constantPrefixExpression", 
			"derivedPrefixExpression", "endPrefixExpression", "referenceExpression", 
			"multiplicityExpression", "multiplicityPropExpression", "orderedMultiplicityExpression", 
			"nonuniqueMultiplicityExpression", "multiplicityExpressionMember", "featureExpressions", 
			"subsettingExpression", "redefinitionExpression", "typingExpression", 
			"literalExpression", "measurementExpression", "constraintExpression", 
			"expression", "primaryExpression", "bracketAccessExpression", "primaryAtom", 
			"sequenceExpressionList", "featureChainExpression", "qualifiedName", 
			"shortName", "refName", "qualifiedName2", "name"
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
			null, "'references'", "'rep'", "'return'", "'snapshot'", null, "'specialization'", 
			"'specializes'", "'step'", "'struct'", "'subclassifier'", "'subset'", 
			"'subsets'", "'subtype'", "'succession'", "'then'", "'timeslice'", null, 
			"'to'", "'true'", "'type'", "'typed'", "'typing'", "'unions'", "'var'", 
			"'variation'", null, "'variant'", null, "'xor'"
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
			"REP", "RETURN", "SNAPSHOT", "SNAPSHOT_PREFIX", "SPECIALIZTION", "SPECIALIZES", 
			"STEP", "STRUCT", "SUBCLASSIFIER", "SUBSET", "SUBSETS", "SUBTYPE", "SUCCESSION", 
			"THEN", "TIMESLICE", "TIMESLICE_PREFIX", "TO", "TRUE", "TYPE", "TYPED", 
			"TYPING", "UNIONS", "VAR", "VARIATION", "VARIATION_PREFIX", "VARIANT", 
			"VARIANT_PREFIX", "XOR_KEYWORD", "REFNAME", "ANY", "XOR"
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
			setState(72);
			prefixNodeExpression();
			setState(74);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(73);
				referenceExpression();
				}
				break;
			}
			setState(77);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(76);
				shortName();
				}
				break;
			}
			setState(80);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -125831178L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -4538783999459333L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 8589934591L) != 0)) {
				{
				setState(79);
				name();
				}
			}

			setState(83);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(82);
				multiplicityExpression();
				}
				break;
			}
			setState(85);
			featureExpressions();
			setState(86);
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
			setState(88);
			prefixListItemExpression();
			setState(90);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(89);
				referenceExpression();
				}
				break;
			}
			setState(93);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(92);
				shortName();
				}
				break;
			}
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -125831178L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -4538783999459333L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 8589934591L) != 0)) {
				{
				setState(95);
				name();
				}
			}

			setState(99);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(98);
				multiplicityExpression();
				}
				break;
			}
			setState(102);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(101);
				multiplicityPropExpression();
				}
				break;
			}
			setState(104);
			featureExpressions();
			setState(105);
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
		public List<PortionKindPrefixExpressionContext> portionKindPrefixExpression() {
			return getRuleContexts(PortionKindPrefixExpressionContext.class);
		}
		public PortionKindPrefixExpressionContext portionKindPrefixExpression(int i) {
			return getRuleContext(PortionKindPrefixExpressionContext.class,i);
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
			setState(113);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(111);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case ABSTRACT_PREFIX:
						{
						setState(107);
						abstractPrefixExpression();
						}
						break;
					case VARIATION_PREFIX:
						{
						setState(108);
						variationPrefixExpression();
						}
						break;
					case VARIANT_PREFIX:
						{
						setState(109);
						variantPrefixExpression();
						}
						break;
					case SNAPSHOT_PREFIX:
					case TIMESLICE_PREFIX:
						{
						setState(110);
						portionKindPrefixExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(115);
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

	@SuppressWarnings("CheckReturnValue")
	public static class PortionKindPrefixExpressionContext extends ParserRuleContext {
		public TerminalNode TIMESLICE_PREFIX() { return getToken(DirectEditParser.TIMESLICE_PREFIX, 0); }
		public TerminalNode SNAPSHOT_PREFIX() { return getToken(DirectEditParser.SNAPSHOT_PREFIX, 0); }
		public PortionKindPrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_portionKindPrefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterPortionKindPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitPortionKindPrefixExpression(this);
		}
	}

	public final PortionKindPrefixExpressionContext portionKindPrefixExpression() throws RecognitionException {
		PortionKindPrefixExpressionContext _localctx = new PortionKindPrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_portionKindPrefixExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
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
			setState(126);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(124);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case IN_PREFIX:
					case INOUT_PREFIX:
					case OUT_PREFIX:
						{
						setState(118);
						directionPrefixExpression();
						}
						break;
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
					case CONSTANT_PREFIX:
						{
						setState(121);
						constantPrefixExpression();
						}
						break;
					case DERIVED_PREFIX:
						{
						setState(122);
						derivedPrefixExpression();
						}
						break;
					case END_PREFIX:
						{
						setState(123);
						endPrefixExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(128);
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
			setState(129);
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
			setState(131);
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
			setState(133);
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
			setState(135);
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
			setState(137);
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
			setState(139);
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
			setState(141);
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
			setState(143);
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			match(LBRACKET);
			setState(152);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR || _la==Integer) {
				{
				setState(149);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(146);
					((MultiplicityExpressionContext)_localctx).lowerBound = multiplicityExpressionMember();
					setState(147);
					match(DOTDOT);
					}
					break;
				}
				setState(151);
				((MultiplicityExpressionContext)_localctx).upperBound = multiplicityExpressionMember();
				}
			}

			setState(154);
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
			setState(157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDERED_SUFFIX) {
				{
				setState(156);
				orderedMultiplicityExpression();
				}
			}

			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NONUNIQUE_SUFFIX) {
				{
				setState(159);
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
			setState(162);
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
			setState(164);
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
			setState(166);
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
			setState(196);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUBSETS_OP || _la==REDEFINES_OP) {
					{
					setState(170);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case REDEFINES_OP:
						{
						setState(168);
						redefinitionExpression();
						}
						break;
					case SUBSETS_OP:
						{
						setState(169);
						subsettingExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(173);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LBRACKET) {
						{
						setState(172);
						multiplicityExpression();
						}
					}

					}
				}

				setState(181);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(177);
					typingExpression();
					setState(179);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LBRACKET) {
						{
						setState(178);
						multiplicityExpression();
						}
					}

					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(184);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(183);
					typingExpression();
					}
				}

				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUBSETS_OP || _la==REDEFINES_OP) {
					{
					setState(188);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SUBSETS_OP:
						{
						setState(186);
						subsettingExpression();
						}
						break;
					case REDEFINES_OP:
						{
						setState(187);
						redefinitionExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(191);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LBRACKET) {
						{
						setState(190);
						multiplicityExpression();
						}
					}

					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(195);
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
			setState(198);
			match(SUBSETS_OP);
			setState(199);
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
			setState(201);
			match(REDEFINES_OP);
			setState(202);
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
			setState(204);
			match(COLON);
			setState(205);
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
		enterRule(_localctx, 44, RULE_literalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
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
		enterRule(_localctx, 46, RULE_measurementExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(209);
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
				setState(212); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -18L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 8589934591L) != 0) );
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
		enterRule(_localctx, 48, RULE_constraintExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
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
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new PrimaryExprContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(217);
			primaryExpression();
			}
			_ctx.stop = _input.LT(-1);
			setState(224);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new BinaryOperationExprContext(new ExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_expression);
					setState(219);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(220);
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
					setState(221);
					expression(3);
					}
					} 
				}
				setState(226);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
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
		enterRule(_localctx, 52, RULE_primaryExpression);
		try {
			setState(234);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(227);
				featureChainExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(228);
				bracketAccessExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(229);
				literalExpression();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(230);
				match(LPAREN);
				setState(231);
				expression(0);
				setState(232);
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
		enterRule(_localctx, 54, RULE_bracketAccessExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			primaryAtom();
			setState(237);
			match(LBRACKET);
			setState(238);
			sequenceExpressionList();
			setState(239);
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
		enterRule(_localctx, 56, RULE_primaryAtom);
		try {
			setState(247);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ESPACED_NAME:
			case REFNAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(241);
				featureChainExpression();
				}
				break;
			case Boolean:
			case Integer:
			case Real:
			case DoubleQuotedString:
				enterOuterAlt(_localctx, 2);
				{
				setState(242);
				literalExpression();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 3);
				{
				setState(243);
				match(LPAREN);
				setState(244);
				expression(0);
				setState(245);
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
		enterRule(_localctx, 58, RULE_sequenceExpressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			expression(0);
			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(250);
				match(COMMA);
				setState(251);
				expression(0);
				}
				}
				setState(256);
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
		enterRule(_localctx, 60, RULE_featureChainExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			refName();
			setState(260);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(258);
				match(DOT);
				setState(259);
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
		enterRule(_localctx, 62, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			name();
			setState(267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAMESPACE_SEP) {
				{
				{
				setState(263);
				match(NAMESPACE_SEP);
				setState(264);
				name();
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
		enterRule(_localctx, 64, RULE_shortName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			match(LT);
			setState(272);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(271);
				name();
				}
				break;
			}
			setState(274);
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
		enterRule(_localctx, 66, RULE_refName);
		try {
			setState(279);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(276);
				match(REFNAME);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(277);
				match(ESPACED_NAME);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(278);
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
		enterRule(_localctx, 68, RULE_qualifiedName2);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			_la = _input.LA(1);
			if ( !(_la==ESPACED_NAME || _la==REFNAME) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(284); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(282);
					match(NAMESPACE_SEP);
					setState(283);
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
				setState(286); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
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
		enterRule(_localctx, 70, RULE_name);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(289); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(288);
					_la = _input.LA(1);
					if ( _la <= 0 || ((((_la) & ~0x3f) == 0 && ((1L << _la) & 125831176L) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 1134695999864833L) != 0)) ) {
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
				setState(291); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
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
		case 25:
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
		"\u0004\u0001\u00a0\u0126\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"\"\u0002#\u0007#\u0001\u0000\u0001\u0000\u0003\u0000K\b\u0000\u0001\u0000"+
		"\u0003\u0000N\b\u0000\u0001\u0000\u0003\u0000Q\b\u0000\u0001\u0000\u0003"+
		"\u0000T\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0003\u0001[\b\u0001\u0001\u0001\u0003\u0001^\b\u0001\u0001\u0001"+
		"\u0003\u0001a\b\u0001\u0001\u0001\u0003\u0001d\b\u0001\u0001\u0001\u0003"+
		"\u0001g\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0005\u0002p\b\u0002\n\u0002\f\u0002s\t"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0005\u0004}\b\u0004\n\u0004\f\u0004\u0080"+
		"\t\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001"+
		"\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u0096\b"+
		"\r\u0001\r\u0003\r\u0099\b\r\u0001\r\u0001\r\u0001\u000e\u0003\u000e\u009e"+
		"\b\u000e\u0001\u000e\u0003\u000e\u00a1\b\u000e\u0001\u000f\u0001\u000f"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0003\u0012\u00ab\b\u0012\u0001\u0012\u0003\u0012\u00ae\b\u0012\u0003"+
		"\u0012\u00b0\b\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00b4\b\u0012"+
		"\u0003\u0012\u00b6\b\u0012\u0001\u0012\u0003\u0012\u00b9\b\u0012\u0001"+
		"\u0012\u0001\u0012\u0003\u0012\u00bd\b\u0012\u0001\u0012\u0003\u0012\u00c0"+
		"\b\u0012\u0003\u0012\u00c2\b\u0012\u0001\u0012\u0003\u0012\u00c5\b\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017"+
		"\u0004\u0017\u00d3\b\u0017\u000b\u0017\f\u0017\u00d4\u0001\u0018\u0001"+
		"\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0005\u0019\u00df\b\u0019\n\u0019\f\u0019\u00e2\t\u0019\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0003\u001a\u00eb\b\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0003\u001c\u00f8\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0005\u001d\u00fd\b\u001d\n\u001d\f\u001d\u0100\t\u001d\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0003\u001e\u0105\b\u001e\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0005\u001f\u010a\b\u001f\n\u001f\f\u001f\u010d\t\u001f\u0001 "+
		"\u0001 \u0003 \u0111\b \u0001 \u0001 \u0001!\u0001!\u0001!\u0003!\u0118"+
		"\b!\u0001\"\u0001\"\u0001\"\u0004\"\u011d\b\"\u000b\"\f\"\u011e\u0001"+
		"#\u0004#\u0122\b#\u000b#\f#\u0123\u0001#\u0000\u00012$\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \""+
		"$&(*,.02468:<>@BDF\u0000\b\u0002\u0000\u0085\u0085\u0091\u0091\u0003\u0000"+
		"^^``vv\u0002\u0000\u000f\u000f##\u0001\u0000\"%\u0001\u0000\u0004\u0004"+
		"\u0006\u0000\u0007\b\r\u0013\u0016\u0016\u001b!\u009d\u009d\u00a0\u00a0"+
		"\u0002\u0000\u0001\u0001\u009e\u009e\u0006\u0000\u0003\u0003\u000b\u000b"+
		"\u0017\u001aBBmmtt\u0132\u0000H\u0001\u0000\u0000\u0000\u0002X\u0001\u0000"+
		"\u0000\u0000\u0004q\u0001\u0000\u0000\u0000\u0006t\u0001\u0000\u0000\u0000"+
		"\b~\u0001\u0000\u0000\u0000\n\u0081\u0001\u0000\u0000\u0000\f\u0083\u0001"+
		"\u0000\u0000\u0000\u000e\u0085\u0001\u0000\u0000\u0000\u0010\u0087\u0001"+
		"\u0000\u0000\u0000\u0012\u0089\u0001\u0000\u0000\u0000\u0014\u008b\u0001"+
		"\u0000\u0000\u0000\u0016\u008d\u0001\u0000\u0000\u0000\u0018\u008f\u0001"+
		"\u0000\u0000\u0000\u001a\u0091\u0001\u0000\u0000\u0000\u001c\u009d\u0001"+
		"\u0000\u0000\u0000\u001e\u00a2\u0001\u0000\u0000\u0000 \u00a4\u0001\u0000"+
		"\u0000\u0000\"\u00a6\u0001\u0000\u0000\u0000$\u00c4\u0001\u0000\u0000"+
		"\u0000&\u00c6\u0001\u0000\u0000\u0000(\u00c9\u0001\u0000\u0000\u0000*"+
		"\u00cc\u0001\u0000\u0000\u0000,\u00cf\u0001\u0000\u0000\u0000.\u00d2\u0001"+
		"\u0000\u0000\u00000\u00d6\u0001\u0000\u0000\u00002\u00d8\u0001\u0000\u0000"+
		"\u00004\u00ea\u0001\u0000\u0000\u00006\u00ec\u0001\u0000\u0000\u00008"+
		"\u00f7\u0001\u0000\u0000\u0000:\u00f9\u0001\u0000\u0000\u0000<\u0101\u0001"+
		"\u0000\u0000\u0000>\u0106\u0001\u0000\u0000\u0000@\u010e\u0001\u0000\u0000"+
		"\u0000B\u0117\u0001\u0000\u0000\u0000D\u0119\u0001\u0000\u0000\u0000F"+
		"\u0121\u0001\u0000\u0000\u0000HJ\u0003\u0004\u0002\u0000IK\u0003\u0018"+
		"\f\u0000JI\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KM\u0001\u0000"+
		"\u0000\u0000LN\u0003@ \u0000ML\u0001\u0000\u0000\u0000MN\u0001\u0000\u0000"+
		"\u0000NP\u0001\u0000\u0000\u0000OQ\u0003F#\u0000PO\u0001\u0000\u0000\u0000"+
		"PQ\u0001\u0000\u0000\u0000QS\u0001\u0000\u0000\u0000RT\u0003\u001a\r\u0000"+
		"SR\u0001\u0000\u0000\u0000ST\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000"+
		"\u0000UV\u0003$\u0012\u0000VW\u0005\u0000\u0000\u0001W\u0001\u0001\u0000"+
		"\u0000\u0000XZ\u0003\b\u0004\u0000Y[\u0003\u0018\f\u0000ZY\u0001\u0000"+
		"\u0000\u0000Z[\u0001\u0000\u0000\u0000[]\u0001\u0000\u0000\u0000\\^\u0003"+
		"@ \u0000]\\\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^`\u0001"+
		"\u0000\u0000\u0000_a\u0003F#\u0000`_\u0001\u0000\u0000\u0000`a\u0001\u0000"+
		"\u0000\u0000ac\u0001\u0000\u0000\u0000bd\u0003\u001a\r\u0000cb\u0001\u0000"+
		"\u0000\u0000cd\u0001\u0000\u0000\u0000df\u0001\u0000\u0000\u0000eg\u0003"+
		"\u001c\u000e\u0000fe\u0001\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000"+
		"gh\u0001\u0000\u0000\u0000hi\u0003$\u0012\u0000ij\u0005\u0000\u0000\u0001"+
		"j\u0003\u0001\u0000\u0000\u0000kp\u0003\f\u0006\u0000lp\u0003\u000e\u0007"+
		"\u0000mp\u0003\u0010\b\u0000np\u0003\u0006\u0003\u0000ok\u0001\u0000\u0000"+
		"\u0000ol\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000on\u0001\u0000"+
		"\u0000\u0000ps\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000qr\u0001"+
		"\u0000\u0000\u0000r\u0005\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000"+
		"\u0000tu\u0007\u0000\u0000\u0000u\u0007\u0001\u0000\u0000\u0000v}\u0003"+
		"\n\u0005\u0000w}\u0003\f\u0006\u0000x}\u0003\u000e\u0007\u0000y}\u0003"+
		"\u0012\t\u0000z}\u0003\u0014\n\u0000{}\u0003\u0016\u000b\u0000|v\u0001"+
		"\u0000\u0000\u0000|w\u0001\u0000\u0000\u0000|x\u0001\u0000\u0000\u0000"+
		"|y\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000\u0000|{\u0001\u0000\u0000"+
		"\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000~\u007f"+
		"\u0001\u0000\u0000\u0000\u007f\t\u0001\u0000\u0000\u0000\u0080~\u0001"+
		"\u0000\u0000\u0000\u0081\u0082\u0007\u0001\u0000\u0000\u0082\u000b\u0001"+
		"\u0000\u0000\u0000\u0083\u0084\u0005(\u0000\u0000\u0084\r\u0001\u0000"+
		"\u0000\u0000\u0085\u0086\u0005\u009a\u0000\u0000\u0086\u000f\u0001\u0000"+
		"\u0000\u0000\u0087\u0088\u0005\u009c\u0000\u0000\u0088\u0011\u0001\u0000"+
		"\u0000\u0000\u0089\u008a\u0005?\u0000\u0000\u008a\u0013\u0001\u0000\u0000"+
		"\u0000\u008b\u008c\u0005E\u0000\u0000\u008c\u0015\u0001\u0000\u0000\u0000"+
		"\u008d\u008e\u0005L\u0000\u0000\u008e\u0017\u0001\u0000\u0000\u0000\u008f"+
		"\u0090\u0005\u0080\u0000\u0000\u0090\u0019\u0001\u0000\u0000\u0000\u0091"+
		"\u0098\u0005\u0003\u0000\u0000\u0092\u0093\u0003\"\u0011\u0000\u0093\u0094"+
		"\u0005\u0016\u0000\u0000\u0094\u0096\u0001\u0000\u0000\u0000\u0095\u0092"+
		"\u0001\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096\u0097"+
		"\u0001\u0000\u0000\u0000\u0097\u0099\u0003\"\u0011\u0000\u0098\u0095\u0001"+
		"\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009a\u0001"+
		"\u0000\u0000\u0000\u009a\u009b\u0005\u0004\u0000\u0000\u009b\u001b\u0001"+
		"\u0000\u0000\u0000\u009c\u009e\u0003\u001e\u000f\u0000\u009d\u009c\u0001"+
		"\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e\u00a0\u0001"+
		"\u0000\u0000\u0000\u009f\u00a1\u0003 \u0010\u0000\u00a0\u009f\u0001\u0000"+
		"\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1\u001d\u0001\u0000"+
		"\u0000\u0000\u00a2\u00a3\u0005t\u0000\u0000\u00a3\u001f\u0001\u0000\u0000"+
		"\u0000\u00a4\u00a5\u0005m\u0000\u0000\u00a5!\u0001\u0000\u0000\u0000\u00a6"+
		"\u00a7\u0007\u0002\u0000\u0000\u00a7#\u0001\u0000\u0000\u0000\u00a8\u00ab"+
		"\u0003(\u0014\u0000\u00a9\u00ab\u0003&\u0013\u0000\u00aa\u00a8\u0001\u0000"+
		"\u0000\u0000\u00aa\u00a9\u0001\u0000\u0000\u0000\u00ab\u00ad\u0001\u0000"+
		"\u0000\u0000\u00ac\u00ae\u0003\u001a\r\u0000\u00ad\u00ac\u0001\u0000\u0000"+
		"\u0000\u00ad\u00ae\u0001\u0000\u0000\u0000\u00ae\u00b0\u0001\u0000\u0000"+
		"\u0000\u00af\u00aa\u0001\u0000\u0000\u0000\u00af\u00b0\u0001\u0000\u0000"+
		"\u0000\u00b0\u00b5\u0001\u0000\u0000\u0000\u00b1\u00b3\u0003*\u0015\u0000"+
		"\u00b2\u00b4\u0003\u001a\r\u0000\u00b3\u00b2\u0001\u0000\u0000\u0000\u00b3"+
		"\u00b4\u0001\u0000\u0000\u0000\u00b4\u00b6\u0001\u0000\u0000\u0000\u00b5"+
		"\u00b1\u0001\u0000\u0000\u0000\u00b5\u00b6\u0001\u0000\u0000\u0000\u00b6"+
		"\u00c5\u0001\u0000\u0000\u0000\u00b7\u00b9\u0003*\u0015\u0000\u00b8\u00b7"+
		"\u0001\u0000\u0000\u0000\u00b8\u00b9\u0001\u0000\u0000\u0000\u00b9\u00c1"+
		"\u0001\u0000\u0000\u0000\u00ba\u00bd\u0003&\u0013\u0000\u00bb\u00bd\u0003"+
		"(\u0014\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bb\u0001\u0000"+
		"\u0000\u0000\u00bd\u00bf\u0001\u0000\u0000\u0000\u00be\u00c0\u0003\u001a"+
		"\r\u0000\u00bf\u00be\u0001\u0000\u0000\u0000\u00bf\u00c0\u0001\u0000\u0000"+
		"\u0000\u00c0\u00c2\u0001\u0000\u0000\u0000\u00c1\u00bc\u0001\u0000\u0000"+
		"\u0000\u00c1\u00c2\u0001\u0000\u0000\u0000\u00c2\u00c5\u0001\u0000\u0000"+
		"\u0000\u00c3\u00c5\u0003\u001a\r\u0000\u00c4\u00af\u0001\u0000\u0000\u0000"+
		"\u00c4\u00b8\u0001\u0000\u0000\u0000\u00c4\u00c3\u0001\u0000\u0000\u0000"+
		"\u00c5%\u0001\u0000\u0000\u0000\u00c6\u00c7\u0005\u0018\u0000\u0000\u00c7"+
		"\u00c8\u0003>\u001f\u0000\u00c8\'\u0001\u0000\u0000\u0000\u00c9\u00ca"+
		"\u0005\u0019\u0000\u0000\u00ca\u00cb\u0003>\u001f\u0000\u00cb)\u0001\u0000"+
		"\u0000\u0000\u00cc\u00cd\u0005\u000b\u0000\u0000\u00cd\u00ce\u0003>\u001f"+
		"\u0000\u00ce+\u0001\u0000\u0000\u0000\u00cf\u00d0\u0007\u0003\u0000\u0000"+
		"\u00d0-\u0001\u0000\u0000\u0000\u00d1\u00d3\b\u0004\u0000\u0000\u00d2"+
		"\u00d1\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000\u0000\u00d4"+
		"\u00d2\u0001\u0000\u0000\u0000\u00d4\u00d5\u0001\u0000\u0000\u0000\u00d5"+
		"/\u0001\u0000\u0000\u0000\u00d6\u00d7\u00032\u0019\u0000\u00d71\u0001"+
		"\u0000\u0000\u0000\u00d8\u00d9\u0006\u0019\uffff\uffff\u0000\u00d9\u00da"+
		"\u00034\u001a\u0000\u00da\u00e0\u0001\u0000\u0000\u0000\u00db\u00dc\n"+
		"\u0002\u0000\u0000\u00dc\u00dd\u0007\u0005\u0000\u0000\u00dd\u00df\u0003"+
		"2\u0019\u0003\u00de\u00db\u0001\u0000\u0000\u0000\u00df\u00e2\u0001\u0000"+
		"\u0000\u0000\u00e0\u00de\u0001\u0000\u0000\u0000\u00e0\u00e1\u0001\u0000"+
		"\u0000\u0000\u00e13\u0001\u0000\u0000\u0000\u00e2\u00e0\u0001\u0000\u0000"+
		"\u0000\u00e3\u00eb\u0003<\u001e\u0000\u00e4\u00eb\u00036\u001b\u0000\u00e5"+
		"\u00eb\u0003,\u0016\u0000\u00e6\u00e7\u0005\u0005\u0000\u0000\u00e7\u00e8"+
		"\u00032\u0019\u0000\u00e8\u00e9\u0005\u0006\u0000\u0000\u00e9\u00eb\u0001"+
		"\u0000\u0000\u0000\u00ea\u00e3\u0001\u0000\u0000\u0000\u00ea\u00e4\u0001"+
		"\u0000\u0000\u0000\u00ea\u00e5\u0001\u0000\u0000\u0000\u00ea\u00e6\u0001"+
		"\u0000\u0000\u0000\u00eb5\u0001\u0000\u0000\u0000\u00ec\u00ed\u00038\u001c"+
		"\u0000\u00ed\u00ee\u0005\u0003\u0000\u0000\u00ee\u00ef\u0003:\u001d\u0000"+
		"\u00ef\u00f0\u0005\u0004\u0000\u0000\u00f07\u0001\u0000\u0000\u0000\u00f1"+
		"\u00f8\u0003<\u001e\u0000\u00f2\u00f8\u0003,\u0016\u0000\u00f3\u00f4\u0005"+
		"\u0005\u0000\u0000\u00f4\u00f5\u00032\u0019\u0000\u00f5\u00f6\u0005\u0006"+
		"\u0000\u0000\u00f6\u00f8\u0001\u0000\u0000\u0000\u00f7\u00f1\u0001\u0000"+
		"\u0000\u0000\u00f7\u00f2\u0001\u0000\u0000\u0000\u00f7\u00f3\u0001\u0000"+
		"\u0000\u0000\u00f89\u0001\u0000\u0000\u0000\u00f9\u00fe\u00032\u0019\u0000"+
		"\u00fa\u00fb\u0005\t\u0000\u0000\u00fb\u00fd\u00032\u0019\u0000\u00fc"+
		"\u00fa\u0001\u0000\u0000\u0000\u00fd\u0100\u0001\u0000\u0000\u0000\u00fe"+
		"\u00fc\u0001\u0000\u0000\u0000\u00fe\u00ff\u0001\u0000\u0000\u0000\u00ff"+
		";\u0001\u0000\u0000\u0000\u0100\u00fe\u0001\u0000\u0000\u0000\u0101\u0104"+
		"\u0003B!\u0000\u0102\u0103\u0005\n\u0000\u0000\u0103\u0105\u0003<\u001e"+
		"\u0000\u0104\u0102\u0001\u0000\u0000\u0000\u0104\u0105\u0001\u0000\u0000"+
		"\u0000\u0105=\u0001\u0000\u0000\u0000\u0106\u010b\u0003F#\u0000\u0107"+
		"\u0108\u0005\u0017\u0000\u0000\u0108\u010a\u0003F#\u0000\u0109\u0107\u0001"+
		"\u0000\u0000\u0000\u010a\u010d\u0001\u0000\u0000\u0000\u010b\u0109\u0001"+
		"\u0000\u0000\u0000\u010b\u010c\u0001\u0000\u0000\u0000\u010c?\u0001\u0000"+
		"\u0000\u0000\u010d\u010b\u0001\u0000\u0000\u0000\u010e\u0110\u0005\u0007"+
		"\u0000\u0000\u010f\u0111\u0003F#\u0000\u0110\u010f\u0001\u0000\u0000\u0000"+
		"\u0110\u0111\u0001\u0000\u0000\u0000\u0111\u0112\u0001\u0000\u0000\u0000"+
		"\u0112\u0113\u0005\b\u0000\u0000\u0113A\u0001\u0000\u0000\u0000\u0114"+
		"\u0118\u0005\u009e\u0000\u0000\u0115\u0118\u0005\u0001\u0000\u0000\u0116"+
		"\u0118\u0003D\"\u0000\u0117\u0114\u0001\u0000\u0000\u0000\u0117\u0115"+
		"\u0001\u0000\u0000\u0000\u0117\u0116\u0001\u0000\u0000\u0000\u0118C\u0001"+
		"\u0000\u0000\u0000\u0119\u011c\u0007\u0006\u0000\u0000\u011a\u011b\u0005"+
		"\u0017\u0000\u0000\u011b\u011d\u0007\u0006\u0000\u0000\u011c\u011a\u0001"+
		"\u0000\u0000\u0000\u011d\u011e\u0001\u0000\u0000\u0000\u011e\u011c\u0001"+
		"\u0000\u0000\u0000\u011e\u011f\u0001\u0000\u0000\u0000\u011fE\u0001\u0000"+
		"\u0000\u0000\u0120\u0122\b\u0007\u0000\u0000\u0121\u0120\u0001\u0000\u0000"+
		"\u0000\u0122\u0123\u0001\u0000\u0000\u0000\u0123\u0121\u0001\u0000\u0000"+
		"\u0000\u0123\u0124\u0001\u0000\u0000\u0000\u0124G\u0001\u0000\u0000\u0000"+
		"&JMPSZ]`cfoq|~\u0095\u0098\u009d\u00a0\u00aa\u00ad\u00af\u00b3\u00b5\u00b8"+
		"\u00bc\u00bf\u00c1\u00c4\u00d4\u00e0\u00ea\u00f7\u00fe\u0104\u010b\u0110"+
		"\u0117\u011e\u0123";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}