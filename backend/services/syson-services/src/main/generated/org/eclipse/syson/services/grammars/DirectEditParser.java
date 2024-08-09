// Generated from DirectEdit.g4 by ANTLR 4.10.1

/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, WS=20, Boolean=21, Integer=22, Real=23, DoubleQuotedString=24, 
		ABOUT=25, ABSTRACT=26, ABSTRACT_PREFIX=27, ALIAS=28, ALL=29, AND=30, AS=31, 
		ASSIGN=32, ASSOC=33, BEAHVIOR=34, BINDING=35, BOOL=36, BY=37, CHAINS=38, 
		CLASS=39, CLASSIFIER=40, COMMENT=41, COMPOSITE=42, CONJUGATE=43, CONJUGATES=44, 
		CONJUGATION=45, CONNECTOR=46, DATATYPE=47, DEFAULT=48, DEPENDENCY=49, 
		DERIVED=50, DERIVED_PREFIX=51, DIFFERENCES=52, DISJOINING=53, DISJOINT=54, 
		DOC=55, ELSE=56, END=57, END_PREFIX=58, EXPR=59, FALSE=60, FEATURE=61, 
		FEATURED=62, FEATURING=63, FILTER=64, FIRST=65, FLOW=66, FOR=67, FROM=68, 
		FUNCTION=69, HASTYPE=70, IF=71, INTERSECTS=72, IMPLIES=73, IMPORT=74, 
		IN=75, IN_PREFIX=76, INOUT=77, INOUT_PREFIX=78, INTERACTION=79, INV=80, 
		INVERSE=81, INVERTING=82, ISTYPE=83, LANGUAGE=84, MEMBER=85, METACLASS=86, 
		METADATA=87, MULTIPLICITY=88, NAMESPACE=89, NONUNIQUE=90, NONUNIQUE_SUFFIX=91, 
		NOT=92, NULL=93, OF=94, OR=95, ORDERED=96, ORDERED_SUFFIX=97, OUT=98, 
		OUT_PREFIX=99, PACKAGE=100, PORTION=101, PREDICATE=102, PRIAVTE=103, PROTECTED=104, 
		PUBLIC=105, READONLY=106, READONLY_PREFIX=107, REDEFINES=108, REDEFINITION=109, 
		REF=110, REF_PREFIX=111, REFERENCES=112, REP=113, RETURN=114, SPECIALIZTION=115, 
		SPECIALIZES=116, STEP=117, STRCUT=118, SUBCLASSIFIER=119, SUBSET=120, 
		SUBSETS=121, SUBTYPE=122, SUCCESSION=123, THEN=124, TO=125, TRUE=126, 
		TYPE=127, TYPED=128, TYPING=129, UNIONS=130, VARIATION=131, VARIATION_PREFIX=132, 
		XOR=133, ANY=134;
	public static final int
		RULE_expression = 0, RULE_prefixExpression = 1, RULE_directionPrefixExpression = 2, 
		RULE_abstractPrefixExpression = 3, RULE_variationPrefixExpression = 4, 
		RULE_readonlyPrefixExpression = 5, RULE_derivedPrefixExpression = 6, RULE_endPrefixExpression = 7, 
		RULE_referenceExpression = 8, RULE_multiplicityExpression = 9, RULE_multiplicityPropExpression = 10, 
		RULE_orderedMultiplicityExpression = 11, RULE_nonuniqueMultiplicityExpression = 12, 
		RULE_multiplicityExpressionMember = 13, RULE_featureExpressions = 14, 
		RULE_subsettingExpression = 15, RULE_redefinitionExpression = 16, RULE_typingExpression = 17, 
		RULE_valueExpression = 18, RULE_literalExpression = 19, RULE_measurementExpression = 20, 
		RULE_constraintExpression = 21, RULE_operand = 22, RULE_featureChainExpression = 23, 
		RULE_featureReference = 24, RULE_operatorExpression = 25, RULE_transitionExpression = 26, 
		RULE_triggerExpression = 27, RULE_triggerExpressionName = 28, RULE_guardExpression = 29, 
		RULE_effectExpression = 30, RULE_qualifiedName = 31, RULE_name = 32;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "prefixExpression", "directionPrefixExpression", "abstractPrefixExpression", 
			"variationPrefixExpression", "readonlyPrefixExpression", "derivedPrefixExpression", 
			"endPrefixExpression", "referenceExpression", "multiplicityExpression", 
			"multiplicityPropExpression", "orderedMultiplicityExpression", "nonuniqueMultiplicityExpression", 
			"multiplicityExpressionMember", "featureExpressions", "subsettingExpression", 
			"redefinitionExpression", "typingExpression", "valueExpression", "literalExpression", 
			"measurementExpression", "constraintExpression", "operand", "featureChainExpression", 
			"featureReference", "operatorExpression", "transitionExpression", "triggerExpression", 
			"triggerExpressionName", "guardExpression", "effectExpression", "qualifiedName", 
			"name"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", "'..'", "']'", "'*'", "':>'", "':>>'", "':'", "'='", "'.'", 
			"'<='", "'>='", "'<'", "'>'", "'=='", "'|'", "'/'", "','", "'::'", "'::>'", 
			null, null, null, null, null, "'about'", "'abstract'", null, "'alias'", 
			"'all'", "'and'", "'as'", "'assign'", "'assoc'", "'behavior'", "'binding'", 
			"'bool'", "'by'", "'chains'", "'class'", "'classifier'", "'comment'", 
			"'composite'", "'conjugate'", "'conjugates'", "'conjugation'", "'connector'", 
			"'datatype'", "'default'", "'dependency'", "'derived'", null, "'differences'", 
			"'disjoining'", "'disjoint'", "'doc'", "'else'", "'end'", null, "'expr'", 
			"'false'", "'feature'", "'featured'", "'featuring'", "'filter'", "'first'", 
			"'flow'", "'for'", "'from'", "'function'", "'hastype'", "'if'", "'intersects'", 
			"'implies'", "'import'", "'in'", null, "'inout'", null, "'interaction'", 
			"'inv'", "'inverse'", "'inverting'", "'istype'", "'language'", "'member'", 
			"'metaclass'", "'metadata'", "'multiplicity'", "'namespace'", "'nonunique'", 
			null, "'not'", "'null'", "'of'", "'or'", "'ordered'", null, "'out'", 
			null, "'package'", "'portion'", "'predicate'", "'private'", "'protected'", 
			"'public'", "'readonly'", null, "'redefines'", "'redefinition'", "'ref'", 
			null, "'references'", "'rep'", "'return'", "'specialization'", "'specializes'", 
			"'step'", "'struct'", "'subclassifier'", "'subset'", "'subsets'", "'subtype'", 
			"'succession'", "'then'", "'to'", "'true'", "'type'", "'typed'", "'typing'", 
			"'unions'", "'variation'", null, "'xor'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, "WS", "Boolean", "Integer", 
			"Real", "DoubleQuotedString", "ABOUT", "ABSTRACT", "ABSTRACT_PREFIX", 
			"ALIAS", "ALL", "AND", "AS", "ASSIGN", "ASSOC", "BEAHVIOR", "BINDING", 
			"BOOL", "BY", "CHAINS", "CLASS", "CLASSIFIER", "COMMENT", "COMPOSITE", 
			"CONJUGATE", "CONJUGATES", "CONJUGATION", "CONNECTOR", "DATATYPE", "DEFAULT", 
			"DEPENDENCY", "DERIVED", "DERIVED_PREFIX", "DIFFERENCES", "DISJOINING", 
			"DISJOINT", "DOC", "ELSE", "END", "END_PREFIX", "EXPR", "FALSE", "FEATURE", 
			"FEATURED", "FEATURING", "FILTER", "FIRST", "FLOW", "FOR", "FROM", "FUNCTION", 
			"HASTYPE", "IF", "INTERSECTS", "IMPLIES", "IMPORT", "IN", "IN_PREFIX", 
			"INOUT", "INOUT_PREFIX", "INTERACTION", "INV", "INVERSE", "INVERTING", 
			"ISTYPE", "LANGUAGE", "MEMBER", "METACLASS", "METADATA", "MULTIPLICITY", 
			"NAMESPACE", "NONUNIQUE", "NONUNIQUE_SUFFIX", "NOT", "NULL", "OF", "OR", 
			"ORDERED", "ORDERED_SUFFIX", "OUT", "OUT_PREFIX", "PACKAGE", "PORTION", 
			"PREDICATE", "PRIAVTE", "PROTECTED", "PUBLIC", "READONLY", "READONLY_PREFIX", 
			"REDEFINES", "REDEFINITION", "REF", "REF_PREFIX", "REFERENCES", "REP", 
			"RETURN", "SPECIALIZTION", "SPECIALIZES", "STEP", "STRCUT", "SUBCLASSIFIER", 
			"SUBSET", "SUBSETS", "SUBTYPE", "SUCCESSION", "THEN", "TO", "TRUE", "TYPE", 
			"TYPED", "TYPING", "UNIONS", "VARIATION", "VARIATION_PREFIX", "XOR", 
			"ANY"
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

	public static class ExpressionContext extends ParserRuleContext {
		public FeatureExpressionsContext featureExpressions() {
			return getRuleContext(FeatureExpressionsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(DirectEditParser.EOF, 0); }
		public PrefixExpressionContext prefixExpression() {
			return getRuleContext(PrefixExpressionContext.class,0);
		}
		public ReferenceExpressionContext referenceExpression() {
			return getRuleContext(ReferenceExpressionContext.class,0);
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
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(66);
				prefixExpression();
				}
				break;
			}
			setState(70);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(69);
				referenceExpression();
				}
				break;
			}
			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << WS) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY) | (1L << DERIVED) | (1L << DERIVED_PREFIX) | (1L << DIFFERENCES) | (1L << DISJOINING) | (1L << DISJOINT) | (1L << DOC) | (1L << ELSE) | (1L << END) | (1L << END_PREFIX) | (1L << EXPR) | (1L << FALSE) | (1L << FEATURE) | (1L << FEATURED) | (1L << FEATURING))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)) | (1L << (RETURN - 64)) | (1L << (SPECIALIZTION - 64)) | (1L << (SPECIALIZES - 64)) | (1L << (STEP - 64)) | (1L << (STRCUT - 64)) | (1L << (SUBCLASSIFIER - 64)) | (1L << (SUBSET - 64)) | (1L << (SUBSETS - 64)) | (1L << (SUBTYPE - 64)) | (1L << (SUCCESSION - 64)) | (1L << (THEN - 64)) | (1L << (TO - 64)) | (1L << (TRUE - 64)) | (1L << (TYPE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (XOR - 128)) | (1L << (ANY - 128)))) != 0)) {
				{
				setState(72);
				name();
				}
			}

			setState(76);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(75);
				multiplicityExpression();
				}
			}

			setState(79);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(78);
				multiplicityPropExpression();
				}
				break;
			}
			setState(81);
			featureExpressions();
			setState(82);
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

	public static class PrefixExpressionContext extends ParserRuleContext {
		public DirectionPrefixExpressionContext directionPrefixExpression() {
			return getRuleContext(DirectionPrefixExpressionContext.class,0);
		}
		public AbstractPrefixExpressionContext abstractPrefixExpression() {
			return getRuleContext(AbstractPrefixExpressionContext.class,0);
		}
		public VariationPrefixExpressionContext variationPrefixExpression() {
			return getRuleContext(VariationPrefixExpressionContext.class,0);
		}
		public ReadonlyPrefixExpressionContext readonlyPrefixExpression() {
			return getRuleContext(ReadonlyPrefixExpressionContext.class,0);
		}
		public DerivedPrefixExpressionContext derivedPrefixExpression() {
			return getRuleContext(DerivedPrefixExpressionContext.class,0);
		}
		public EndPrefixExpressionContext endPrefixExpression() {
			return getRuleContext(EndPrefixExpressionContext.class,0);
		}
		public PrefixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterPrefixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitPrefixExpression(this);
		}
	}

	public final PrefixExpressionContext prefixExpression() throws RecognitionException {
		PrefixExpressionContext _localctx = new PrefixExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_prefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(84);
				directionPrefixExpression();
				}
				break;
			}
			setState(88);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(87);
				abstractPrefixExpression();
				}
				break;
			}
			setState(91);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(90);
				variationPrefixExpression();
				}
				break;
			}
			setState(94);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(93);
				readonlyPrefixExpression();
				}
				break;
			}
			setState(97);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(96);
				derivedPrefixExpression();
				}
				break;
			}
			setState(100);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(99);
				endPrefixExpression();
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
		enterRule(_localctx, 4, RULE_directionPrefixExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			_la = _input.LA(1);
			if ( !(((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & ((1L << (IN_PREFIX - 76)) | (1L << (INOUT_PREFIX - 76)) | (1L << (OUT_PREFIX - 76)))) != 0)) ) {
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
		enterRule(_localctx, 6, RULE_abstractPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
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
		enterRule(_localctx, 8, RULE_variationPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
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
		enterRule(_localctx, 10, RULE_readonlyPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
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
		enterRule(_localctx, 12, RULE_derivedPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
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
		enterRule(_localctx, 14, RULE_endPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
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
		enterRule(_localctx, 16, RULE_referenceExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
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
		public List<MultiplicityExpressionMemberContext> multiplicityExpressionMember() {
			return getRuleContexts(MultiplicityExpressionMemberContext.class);
		}
		public MultiplicityExpressionMemberContext multiplicityExpressionMember(int i) {
			return getRuleContext(MultiplicityExpressionMemberContext.class,i);
		}
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
		enterRule(_localctx, 18, RULE_multiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(T__0);
			setState(120);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(117);
				((MultiplicityExpressionContext)_localctx).lowerBound = multiplicityExpressionMember();
				setState(118);
				match(T__1);
				}
				break;
			}
			setState(122);
			((MultiplicityExpressionContext)_localctx).upperBound = multiplicityExpressionMember();
			setState(123);
			match(T__2);
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
		enterRule(_localctx, 20, RULE_multiplicityPropExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDERED_SUFFIX) {
				{
				setState(125);
				orderedMultiplicityExpression();
				}
			}

			setState(129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NONUNIQUE_SUFFIX) {
				{
				setState(128);
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
		enterRule(_localctx, 22, RULE_orderedMultiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
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
		enterRule(_localctx, 24, RULE_nonuniqueMultiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
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
		enterRule(_localctx, 26, RULE_multiplicityExpressionMember);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			_la = _input.LA(1);
			if ( !(_la==T__3 || _la==Integer) ) {
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
		public ValueExpressionContext valueExpression() {
			return getRuleContext(ValueExpressionContext.class,0);
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
		enterRule(_localctx, 28, RULE_featureExpressions);
		int _la;
		try {
			setState(157);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(139);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(137);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(138);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__6:
				case T__7:
					break;
				default:
					break;
				}
				setState(142);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(141);
					typingExpression();
					}
				}

				setState(145);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(144);
					valueExpression();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(147);
					typingExpression();
					}
				}

				setState(152);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(150);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(151);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__7:
					break;
				default:
					break;
				}
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(154);
					valueExpression();
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
		enterRule(_localctx, 30, RULE_subsettingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			match(T__4);
			setState(160);
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
		enterRule(_localctx, 32, RULE_redefinitionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(T__5);
			setState(163);
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
		enterRule(_localctx, 34, RULE_typingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			match(T__6);
			setState(166);
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
		public LiteralExpressionContext literalExpression() {
			return getRuleContext(LiteralExpressionContext.class,0);
		}
		public MeasurementExpressionContext measurementExpression() {
			return getRuleContext(MeasurementExpressionContext.class,0);
		}
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
		enterRule(_localctx, 36, RULE_valueExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(T__7);
			setState(169);
			literalExpression();
			setState(174);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(170);
				match(T__0);
				setState(171);
				measurementExpression();
				setState(172);
				match(T__2);
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
		enterRule(_localctx, 38, RULE_literalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
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
		enterRule(_localctx, 40, RULE_measurementExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(178);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==T__2) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(181); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << WS) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY) | (1L << DERIVED) | (1L << DERIVED_PREFIX) | (1L << DIFFERENCES) | (1L << DISJOINING) | (1L << DISJOINT) | (1L << DOC) | (1L << ELSE) | (1L << END) | (1L << END_PREFIX) | (1L << EXPR) | (1L << FALSE) | (1L << FEATURE) | (1L << FEATURED) | (1L << FEATURING))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NONUNIQUE_SUFFIX - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (ORDERED_SUFFIX - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)) | (1L << (RETURN - 64)) | (1L << (SPECIALIZTION - 64)) | (1L << (SPECIALIZES - 64)) | (1L << (STEP - 64)) | (1L << (STRCUT - 64)) | (1L << (SUBCLASSIFIER - 64)) | (1L << (SUBSET - 64)) | (1L << (SUBSETS - 64)) | (1L << (SUBTYPE - 64)) | (1L << (SUCCESSION - 64)) | (1L << (THEN - 64)) | (1L << (TO - 64)) | (1L << (TRUE - 64)) | (1L << (TYPE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (XOR - 128)) | (1L << (ANY - 128)))) != 0) );
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
		public List<OperandContext> operand() {
			return getRuleContexts(OperandContext.class);
		}
		public OperandContext operand(int i) {
			return getRuleContext(OperandContext.class,i);
		}
		public OperatorExpressionContext operatorExpression() {
			return getRuleContext(OperatorExpressionContext.class,0);
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
		enterRule(_localctx, 42, RULE_constraintExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			operand();
			setState(184);
			operatorExpression();
			setState(185);
			operand();
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

	public static class OperandContext extends ParserRuleContext {
		public LiteralExpressionContext literalExpression() {
			return getRuleContext(LiteralExpressionContext.class,0);
		}
		public FeatureChainExpressionContext featureChainExpression() {
			return getRuleContext(FeatureChainExpressionContext.class,0);
		}
		public MeasurementExpressionContext measurementExpression() {
			return getRuleContext(MeasurementExpressionContext.class,0);
		}
		public OperandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterOperand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitOperand(this);
		}
	}

	public final OperandContext operand() throws RecognitionException {
		OperandContext _localctx = new OperandContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_operand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(187);
				literalExpression();
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(188);
					match(T__0);
					setState(189);
					measurementExpression();
					setState(190);
					match(T__2);
					}
				}

				}
				break;
			case 2:
				{
				setState(194);
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

	public static class FeatureChainExpressionContext extends ParserRuleContext {
		public FeatureReferenceContext featureReference() {
			return getRuleContext(FeatureReferenceContext.class,0);
		}
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
		enterRule(_localctx, 46, RULE_featureChainExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			featureReference();
			setState(200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(198);
				match(T__8);
				setState(199);
				featureChainExpression();
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

	public static class FeatureReferenceContext extends ParserRuleContext {
		public FeatureReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterFeatureReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitFeatureReference(this);
		}
	}

	public final FeatureReferenceContext featureReference() throws RecognitionException {
		FeatureReferenceContext _localctx = new FeatureReferenceContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_featureReference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(202);
				_la = _input.LA(1);
				if ( _la <= 0 || ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(205); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << WS) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY) | (1L << DERIVED) | (1L << DERIVED_PREFIX) | (1L << DIFFERENCES) | (1L << DISJOINING) | (1L << DISJOINT) | (1L << DOC) | (1L << ELSE) | (1L << END) | (1L << END_PREFIX) | (1L << EXPR) | (1L << FALSE) | (1L << FEATURE) | (1L << FEATURED) | (1L << FEATURING))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NONUNIQUE_SUFFIX - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (ORDERED_SUFFIX - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)) | (1L << (RETURN - 64)) | (1L << (SPECIALIZTION - 64)) | (1L << (SPECIALIZES - 64)) | (1L << (STEP - 64)) | (1L << (STRCUT - 64)) | (1L << (SUBCLASSIFIER - 64)) | (1L << (SUBSET - 64)) | (1L << (SUBSETS - 64)) | (1L << (SUBTYPE - 64)) | (1L << (SUCCESSION - 64)) | (1L << (THEN - 64)) | (1L << (TO - 64)) | (1L << (TRUE - 64)) | (1L << (TYPE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (XOR - 128)) | (1L << (ANY - 128)))) != 0) );
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

	public static class OperatorExpressionContext extends ParserRuleContext {
		public OperatorExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operatorExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).enterOperatorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DirectEditListener ) ((DirectEditListener)listener).exitOperatorExpression(this);
		}
	}

	public final OperatorExpressionContext operatorExpression() throws RecognitionException {
		OperatorExpressionContext _localctx = new OperatorExpressionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_operatorExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13))) != 0)) ) {
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
		enterRule(_localctx, 52, RULE_transitionExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(209);
				triggerExpression();
				}
				break;
			}
			setState(213);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(212);
				guardExpression();
				}
			}

			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(215);
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
		enterRule(_localctx, 54, RULE_triggerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			triggerExpressionName();
			setState(223);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(219);
				match(T__14);
				setState(220);
				triggerExpressionName();
				}
				}
				setState(225);
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
		enterRule(_localctx, 56, RULE_triggerExpressionName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			name();
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(227);
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
		public ValueExpressionContext valueExpression() {
			return getRuleContext(ValueExpressionContext.class,0);
		}
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
		enterRule(_localctx, 58, RULE_guardExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(T__0);
			setState(231);
			valueExpression();
			setState(232);
			match(T__2);
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
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
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
		enterRule(_localctx, 60, RULE_effectExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			match(T__15);
			setState(235);
			qualifiedName();
			setState(240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__16) {
				{
				{
				setState(236);
				match(T__16);
				setState(237);
				qualifiedName();
				}
				}
				setState(242);
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
			setState(243);
			name();
			setState(248);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17) {
				{
				{
				setState(244);
				match(T__17);
				setState(245);
				name();
				}
				}
				setState(250);
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

	public static class NameContext extends ParserRuleContext {
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
		enterRule(_localctx, 64, RULE_name);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(252); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(251);
					_la = _input.LA(1);
					if ( _la <= 0 || ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__18))) != 0) || _la==NONUNIQUE_SUFFIX || _la==ORDERED_SUFFIX) ) {
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
				setState(254); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
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

	public static final String _serializedATN =
		"\u0004\u0001\u0086\u0101\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0001\u0000\u0003\u0000D"+
		"\b\u0000\u0001\u0000\u0003\u0000G\b\u0000\u0001\u0000\u0003\u0000J\b\u0000"+
		"\u0001\u0000\u0003\u0000M\b\u0000\u0001\u0000\u0003\u0000P\b\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0003\u0001V\b\u0001\u0001"+
		"\u0001\u0003\u0001Y\b\u0001\u0001\u0001\u0003\u0001\\\b\u0001\u0001\u0001"+
		"\u0003\u0001_\b\u0001\u0001\u0001\u0003\u0001b\b\u0001\u0001\u0001\u0003"+
		"\u0001e\b\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0003"+
		"\ty\b\t\u0001\t\u0001\t\u0001\t\u0001\n\u0003\n\u007f\b\n\u0001\n\u0003"+
		"\n\u0082\b\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0003\u000e\u008c\b\u000e\u0001\u000e\u0003\u000e\u008f"+
		"\b\u000e\u0001\u000e\u0003\u000e\u0092\b\u000e\u0001\u000e\u0003\u000e"+
		"\u0095\b\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u0099\b\u000e\u0001"+
		"\u000e\u0003\u000e\u009c\b\u000e\u0003\u000e\u009e\b\u000e\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0003\u0012\u00af\b\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0014\u0004\u0014\u00b4\b\u0014\u000b\u0014\f\u0014\u00b5\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u00c1\b\u0016\u0001\u0016\u0003"+
		"\u0016\u00c4\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u00c9"+
		"\b\u0017\u0001\u0018\u0004\u0018\u00cc\b\u0018\u000b\u0018\f\u0018\u00cd"+
		"\u0001\u0019\u0001\u0019\u0001\u001a\u0003\u001a\u00d3\b\u001a\u0001\u001a"+
		"\u0003\u001a\u00d6\b\u001a\u0001\u001a\u0003\u001a\u00d9\b\u001a\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0005\u001b\u00de\b\u001b\n\u001b\f\u001b"+
		"\u00e1\t\u001b\u0001\u001c\u0001\u001c\u0003\u001c\u00e5\b\u001c\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0005\u001e\u00ef\b\u001e\n\u001e\f\u001e\u00f2\t\u001e"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u00f7\b\u001f\n\u001f"+
		"\f\u001f\u00fa\t\u001f\u0001 \u0004 \u00fd\b \u000b \f \u00fe\u0001 \u0000"+
		"\u0000!\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.02468:<>@\u0000\u0007\u0003\u0000LLNNcc\u0002"+
		"\u0000\u0004\u0004\u0016\u0016\u0001\u0000\u0015\u0018\u0001\u0000\u0003"+
		"\u0003\u0001\u0000\t\u000e\u0001\u0000\n\u000e\u0005\u0000\u0001\u0001"+
		"\u0005\b\u0013\u0013[[aa\u0104\u0000C\u0001\u0000\u0000\u0000\u0002U\u0001"+
		"\u0000\u0000\u0000\u0004f\u0001\u0000\u0000\u0000\u0006h\u0001\u0000\u0000"+
		"\u0000\bj\u0001\u0000\u0000\u0000\nl\u0001\u0000\u0000\u0000\fn\u0001"+
		"\u0000\u0000\u0000\u000ep\u0001\u0000\u0000\u0000\u0010r\u0001\u0000\u0000"+
		"\u0000\u0012t\u0001\u0000\u0000\u0000\u0014~\u0001\u0000\u0000\u0000\u0016"+
		"\u0083\u0001\u0000\u0000\u0000\u0018\u0085\u0001\u0000\u0000\u0000\u001a"+
		"\u0087\u0001\u0000\u0000\u0000\u001c\u009d\u0001\u0000\u0000\u0000\u001e"+
		"\u009f\u0001\u0000\u0000\u0000 \u00a2\u0001\u0000\u0000\u0000\"\u00a5"+
		"\u0001\u0000\u0000\u0000$\u00a8\u0001\u0000\u0000\u0000&\u00b0\u0001\u0000"+
		"\u0000\u0000(\u00b3\u0001\u0000\u0000\u0000*\u00b7\u0001\u0000\u0000\u0000"+
		",\u00c3\u0001\u0000\u0000\u0000.\u00c5\u0001\u0000\u0000\u00000\u00cb"+
		"\u0001\u0000\u0000\u00002\u00cf\u0001\u0000\u0000\u00004\u00d2\u0001\u0000"+
		"\u0000\u00006\u00da\u0001\u0000\u0000\u00008\u00e2\u0001\u0000\u0000\u0000"+
		":\u00e6\u0001\u0000\u0000\u0000<\u00ea\u0001\u0000\u0000\u0000>\u00f3"+
		"\u0001\u0000\u0000\u0000@\u00fc\u0001\u0000\u0000\u0000BD\u0003\u0002"+
		"\u0001\u0000CB\u0001\u0000\u0000\u0000CD\u0001\u0000\u0000\u0000DF\u0001"+
		"\u0000\u0000\u0000EG\u0003\u0010\b\u0000FE\u0001\u0000\u0000\u0000FG\u0001"+
		"\u0000\u0000\u0000GI\u0001\u0000\u0000\u0000HJ\u0003@ \u0000IH\u0001\u0000"+
		"\u0000\u0000IJ\u0001\u0000\u0000\u0000JL\u0001\u0000\u0000\u0000KM\u0003"+
		"\u0012\t\u0000LK\u0001\u0000\u0000\u0000LM\u0001\u0000\u0000\u0000MO\u0001"+
		"\u0000\u0000\u0000NP\u0003\u0014\n\u0000ON\u0001\u0000\u0000\u0000OP\u0001"+
		"\u0000\u0000\u0000PQ\u0001\u0000\u0000\u0000QR\u0003\u001c\u000e\u0000"+
		"RS\u0005\u0000\u0000\u0001S\u0001\u0001\u0000\u0000\u0000TV\u0003\u0004"+
		"\u0002\u0000UT\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000\u0000VX\u0001"+
		"\u0000\u0000\u0000WY\u0003\u0006\u0003\u0000XW\u0001\u0000\u0000\u0000"+
		"XY\u0001\u0000\u0000\u0000Y[\u0001\u0000\u0000\u0000Z\\\u0003\b\u0004"+
		"\u0000[Z\u0001\u0000\u0000\u0000[\\\u0001\u0000\u0000\u0000\\^\u0001\u0000"+
		"\u0000\u0000]_\u0003\n\u0005\u0000^]\u0001\u0000\u0000\u0000^_\u0001\u0000"+
		"\u0000\u0000_a\u0001\u0000\u0000\u0000`b\u0003\f\u0006\u0000a`\u0001\u0000"+
		"\u0000\u0000ab\u0001\u0000\u0000\u0000bd\u0001\u0000\u0000\u0000ce\u0003"+
		"\u000e\u0007\u0000dc\u0001\u0000\u0000\u0000de\u0001\u0000\u0000\u0000"+
		"e\u0003\u0001\u0000\u0000\u0000fg\u0007\u0000\u0000\u0000g\u0005\u0001"+
		"\u0000\u0000\u0000hi\u0005\u001b\u0000\u0000i\u0007\u0001\u0000\u0000"+
		"\u0000jk\u0005\u0084\u0000\u0000k\t\u0001\u0000\u0000\u0000lm\u0005k\u0000"+
		"\u0000m\u000b\u0001\u0000\u0000\u0000no\u00053\u0000\u0000o\r\u0001\u0000"+
		"\u0000\u0000pq\u0005:\u0000\u0000q\u000f\u0001\u0000\u0000\u0000rs\u0005"+
		"o\u0000\u0000s\u0011\u0001\u0000\u0000\u0000tx\u0005\u0001\u0000\u0000"+
		"uv\u0003\u001a\r\u0000vw\u0005\u0002\u0000\u0000wy\u0001\u0000\u0000\u0000"+
		"xu\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000yz\u0001\u0000\u0000"+
		"\u0000z{\u0003\u001a\r\u0000{|\u0005\u0003\u0000\u0000|\u0013\u0001\u0000"+
		"\u0000\u0000}\u007f\u0003\u0016\u000b\u0000~}\u0001\u0000\u0000\u0000"+
		"~\u007f\u0001\u0000\u0000\u0000\u007f\u0081\u0001\u0000\u0000\u0000\u0080"+
		"\u0082\u0003\u0018\f\u0000\u0081\u0080\u0001\u0000\u0000\u0000\u0081\u0082"+
		"\u0001\u0000\u0000\u0000\u0082\u0015\u0001\u0000\u0000\u0000\u0083\u0084"+
		"\u0005a\u0000\u0000\u0084\u0017\u0001\u0000\u0000\u0000\u0085\u0086\u0005"+
		"[\u0000\u0000\u0086\u0019\u0001\u0000\u0000\u0000\u0087\u0088\u0007\u0001"+
		"\u0000\u0000\u0088\u001b\u0001\u0000\u0000\u0000\u0089\u008c\u0003\u001e"+
		"\u000f\u0000\u008a\u008c\u0003 \u0010\u0000\u008b\u0089\u0001\u0000\u0000"+
		"\u0000\u008b\u008a\u0001\u0000\u0000\u0000\u008b\u008c\u0001\u0000\u0000"+
		"\u0000\u008c\u008e\u0001\u0000\u0000\u0000\u008d\u008f\u0003\"\u0011\u0000"+
		"\u008e\u008d\u0001\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000\u0000"+
		"\u008f\u0091\u0001\u0000\u0000\u0000\u0090\u0092\u0003$\u0012\u0000\u0091"+
		"\u0090\u0001\u0000\u0000\u0000\u0091\u0092\u0001\u0000\u0000\u0000\u0092"+
		"\u009e\u0001\u0000\u0000\u0000\u0093\u0095\u0003\"\u0011\u0000\u0094\u0093"+
		"\u0001\u0000\u0000\u0000\u0094\u0095\u0001\u0000\u0000\u0000\u0095\u0098"+
		"\u0001\u0000\u0000\u0000\u0096\u0099\u0003\u001e\u000f\u0000\u0097\u0099"+
		"\u0003 \u0010\u0000\u0098\u0096\u0001\u0000\u0000\u0000\u0098\u0097\u0001"+
		"\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009b\u0001"+
		"\u0000\u0000\u0000\u009a\u009c\u0003$\u0012\u0000\u009b\u009a\u0001\u0000"+
		"\u0000\u0000\u009b\u009c\u0001\u0000\u0000\u0000\u009c\u009e\u0001\u0000"+
		"\u0000\u0000\u009d\u008b\u0001\u0000\u0000\u0000\u009d\u0094\u0001\u0000"+
		"\u0000\u0000\u009e\u001d\u0001\u0000\u0000\u0000\u009f\u00a0\u0005\u0005"+
		"\u0000\u0000\u00a0\u00a1\u0003>\u001f\u0000\u00a1\u001f\u0001\u0000\u0000"+
		"\u0000\u00a2\u00a3\u0005\u0006\u0000\u0000\u00a3\u00a4\u0003>\u001f\u0000"+
		"\u00a4!\u0001\u0000\u0000\u0000\u00a5\u00a6\u0005\u0007\u0000\u0000\u00a6"+
		"\u00a7\u0003>\u001f\u0000\u00a7#\u0001\u0000\u0000\u0000\u00a8\u00a9\u0005"+
		"\b\u0000\u0000\u00a9\u00ae\u0003&\u0013\u0000\u00aa\u00ab\u0005\u0001"+
		"\u0000\u0000\u00ab\u00ac\u0003(\u0014\u0000\u00ac\u00ad\u0005\u0003\u0000"+
		"\u0000\u00ad\u00af\u0001\u0000\u0000\u0000\u00ae\u00aa\u0001\u0000\u0000"+
		"\u0000\u00ae\u00af\u0001\u0000\u0000\u0000\u00af%\u0001\u0000\u0000\u0000"+
		"\u00b0\u00b1\u0007\u0002\u0000\u0000\u00b1\'\u0001\u0000\u0000\u0000\u00b2"+
		"\u00b4\b\u0003\u0000\u0000\u00b3\u00b2\u0001\u0000\u0000\u0000\u00b4\u00b5"+
		"\u0001\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b5\u00b6"+
		"\u0001\u0000\u0000\u0000\u00b6)\u0001\u0000\u0000\u0000\u00b7\u00b8\u0003"+
		",\u0016\u0000\u00b8\u00b9\u00032\u0019\u0000\u00b9\u00ba\u0003,\u0016"+
		"\u0000\u00ba+\u0001\u0000\u0000\u0000\u00bb\u00c0\u0003&\u0013\u0000\u00bc"+
		"\u00bd\u0005\u0001\u0000\u0000\u00bd\u00be\u0003(\u0014\u0000\u00be\u00bf"+
		"\u0005\u0003\u0000\u0000\u00bf\u00c1\u0001\u0000\u0000\u0000\u00c0\u00bc"+
		"\u0001\u0000\u0000\u0000\u00c0\u00c1\u0001\u0000\u0000\u0000\u00c1\u00c4"+
		"\u0001\u0000\u0000\u0000\u00c2\u00c4\u0003.\u0017\u0000\u00c3\u00bb\u0001"+
		"\u0000\u0000\u0000\u00c3\u00c2\u0001\u0000\u0000\u0000\u00c4-\u0001\u0000"+
		"\u0000\u0000\u00c5\u00c8\u00030\u0018\u0000\u00c6\u00c7\u0005\t\u0000"+
		"\u0000\u00c7\u00c9\u0003.\u0017\u0000\u00c8\u00c6\u0001\u0000\u0000\u0000"+
		"\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9/\u0001\u0000\u0000\u0000\u00ca"+
		"\u00cc\b\u0004\u0000\u0000\u00cb\u00ca\u0001\u0000\u0000\u0000\u00cc\u00cd"+
		"\u0001\u0000\u0000\u0000\u00cd\u00cb\u0001\u0000\u0000\u0000\u00cd\u00ce"+
		"\u0001\u0000\u0000\u0000\u00ce1\u0001\u0000\u0000\u0000\u00cf\u00d0\u0007"+
		"\u0005\u0000\u0000\u00d03\u0001\u0000\u0000\u0000\u00d1\u00d3\u00036\u001b"+
		"\u0000\u00d2\u00d1\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001\u0000\u0000"+
		"\u0000\u00d3\u00d5\u0001\u0000\u0000\u0000\u00d4\u00d6\u0003:\u001d\u0000"+
		"\u00d5\u00d4\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000\u0000\u0000"+
		"\u00d6\u00d8\u0001\u0000\u0000\u0000\u00d7\u00d9\u0003<\u001e\u0000\u00d8"+
		"\u00d7\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9"+
		"5\u0001\u0000\u0000\u0000\u00da\u00df\u00038\u001c\u0000\u00db\u00dc\u0005"+
		"\u000f\u0000\u0000\u00dc\u00de\u00038\u001c\u0000\u00dd\u00db\u0001\u0000"+
		"\u0000\u0000\u00de\u00e1\u0001\u0000\u0000\u0000\u00df\u00dd\u0001\u0000"+
		"\u0000\u0000\u00df\u00e0\u0001\u0000\u0000\u0000\u00e07\u0001\u0000\u0000"+
		"\u0000\u00e1\u00df\u0001\u0000\u0000\u0000\u00e2\u00e4\u0003@ \u0000\u00e3"+
		"\u00e5\u0003\"\u0011\u0000\u00e4\u00e3\u0001\u0000\u0000\u0000\u00e4\u00e5"+
		"\u0001\u0000\u0000\u0000\u00e59\u0001\u0000\u0000\u0000\u00e6\u00e7\u0005"+
		"\u0001\u0000\u0000\u00e7\u00e8\u0003$\u0012\u0000\u00e8\u00e9\u0005\u0003"+
		"\u0000\u0000\u00e9;\u0001\u0000\u0000\u0000\u00ea\u00eb\u0005\u0010\u0000"+
		"\u0000\u00eb\u00f0\u0003>\u001f\u0000\u00ec\u00ed\u0005\u0011\u0000\u0000"+
		"\u00ed\u00ef\u0003>\u001f\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ef"+
		"\u00f2\u0001\u0000\u0000\u0000\u00f0\u00ee\u0001\u0000\u0000\u0000\u00f0"+
		"\u00f1\u0001\u0000\u0000\u0000\u00f1=\u0001\u0000\u0000\u0000\u00f2\u00f0"+
		"\u0001\u0000\u0000\u0000\u00f3\u00f8\u0003@ \u0000\u00f4\u00f5\u0005\u0012"+
		"\u0000\u0000\u00f5\u00f7\u0003@ \u0000\u00f6\u00f4\u0001\u0000\u0000\u0000"+
		"\u00f7\u00fa\u0001\u0000\u0000\u0000\u00f8\u00f6\u0001\u0000\u0000\u0000"+
		"\u00f8\u00f9\u0001\u0000\u0000\u0000\u00f9?\u0001\u0000\u0000\u0000\u00fa"+
		"\u00f8\u0001\u0000\u0000\u0000\u00fb\u00fd\b\u0006\u0000\u0000\u00fc\u00fb"+
		"\u0001\u0000\u0000\u0000\u00fd\u00fe\u0001\u0000\u0000\u0000\u00fe\u00fc"+
		"\u0001\u0000\u0000\u0000\u00fe\u00ff\u0001\u0000\u0000\u0000\u00ffA\u0001"+
		"\u0000\u0000\u0000#CFILOUX[^adx~\u0081\u008b\u008e\u0091\u0094\u0098\u009b"+
		"\u009d\u00ae\u00b5\u00c0\u00c3\u00c8\u00cd\u00d2\u00d5\u00d8\u00df\u00e4"+
		"\u00f0\u00f8\u00fe";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}