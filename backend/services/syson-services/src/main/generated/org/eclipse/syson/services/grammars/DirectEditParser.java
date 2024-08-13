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
		T__9=10, T__10=11, T__11=12, T__12=13, WS=14, Boolean=15, Integer=16, 
		Real=17, DoubleQuotedString=18, ABOUT=19, ABSTRACT=20, ABSTRACT_PREFIX=21, 
		ALIAS=22, ALL=23, AND=24, AS=25, ASSIGN=26, ASSOC=27, BEAHVIOR=28, BINDING=29, 
		BOOL=30, BY=31, CHAINS=32, CLASS=33, CLASSIFIER=34, COMMENT=35, COMPOSITE=36, 
		CONJUGATE=37, CONJUGATES=38, CONJUGATION=39, CONNECTOR=40, DATATYPE=41, 
		DEFAULT=42, DEPENDENCY=43, DERIVED=44, DERIVED_PREFIX=45, DIFFERENCES=46, 
		DISJOINING=47, DISJOINT=48, DOC=49, ELSE=50, END=51, END_PREFIX=52, EXPR=53, 
		FALSE=54, FEATURE=55, FEATURED=56, FEATURING=57, FILTER=58, FIRST=59, 
		FLOW=60, FOR=61, FROM=62, FUNCTION=63, HASTYPE=64, IF=65, INTERSECTS=66, 
		IMPLIES=67, IMPORT=68, IN=69, IN_PREFIX=70, INOUT=71, INOUT_PREFIX=72, 
		INTERACTION=73, INV=74, INVERSE=75, INVERTING=76, ISTYPE=77, LANGUAGE=78, 
		MEMBER=79, METACLASS=80, METADATA=81, MULTIPLICITY=82, NAMESPACE=83, NONUNIQUE=84, 
		NONUNIQUE_SUFFIX=85, NOT=86, NULL=87, OF=88, OR=89, ORDERED=90, ORDERED_SUFFIX=91, 
		OUT=92, OUT_PREFIX=93, PACKAGE=94, PORTION=95, PREDICATE=96, PRIAVTE=97, 
		PROTECTED=98, PUBLIC=99, READONLY=100, READONLY_PREFIX=101, REDEFINES=102, 
		REDEFINITION=103, REF=104, REF_PREFIX=105, REFERENCES=106, REP=107, RETURN=108, 
		SPECIALIZTION=109, SPECIALIZES=110, STEP=111, STRCUT=112, SUBCLASSIFIER=113, 
		SUBSET=114, SUBSETS=115, SUBTYPE=116, SUCCESSION=117, THEN=118, TO=119, 
		TRUE=120, TYPE=121, TYPED=122, TYPING=123, UNIONS=124, VARIATION=125, 
		VARIATION_PREFIX=126, XOR=127, ANY=128;
	public static final int
		RULE_expression = 0, RULE_prefixExpression = 1, RULE_directionPrefixExpression = 2, 
		RULE_abstractPrefixExpression = 3, RULE_variationPrefixExpression = 4, 
		RULE_readonlyPrefixExpression = 5, RULE_derivedPrefixExpression = 6, RULE_endPrefixExpression = 7, 
		RULE_referenceExpression = 8, RULE_multiplicityExpression = 9, RULE_multiplicityPropExpression = 10, 
		RULE_orderedMultiplicityExpression = 11, RULE_nonuniqueMultiplicityExpression = 12, 
		RULE_multiplicityExpressionMember = 13, RULE_featureExpressions = 14, 
		RULE_subsettingExpression = 15, RULE_redefinitionExpression = 16, RULE_typingExpression = 17, 
		RULE_valueExpression = 18, RULE_measurementExpression = 19, RULE_transitionExpression = 20, 
		RULE_triggerExpression = 21, RULE_triggerExpressionName = 22, RULE_guardExpression = 23, 
		RULE_effectExpression = 24, RULE_qualifiedName = 25, RULE_name = 26;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "prefixExpression", "directionPrefixExpression", "abstractPrefixExpression", 
			"variationPrefixExpression", "readonlyPrefixExpression", "derivedPrefixExpression", 
			"endPrefixExpression", "referenceExpression", "multiplicityExpression", 
			"multiplicityPropExpression", "orderedMultiplicityExpression", "nonuniqueMultiplicityExpression", 
			"multiplicityExpressionMember", "featureExpressions", "subsettingExpression", 
			"redefinitionExpression", "typingExpression", "valueExpression", "measurementExpression", 
			"transitionExpression", "triggerExpression", "triggerExpressionName", 
			"guardExpression", "effectExpression", "qualifiedName", "name"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", "'..'", "']'", "'*'", "':>'", "':>>'", "':'", "'='", "'|'", 
			"'/'", "','", "'::'", "'::>'", null, null, null, null, null, "'about'", 
			"'abstract'", null, "'alias'", "'all'", "'and'", "'as'", "'assign'", 
			"'assoc'", "'behavior'", "'binding'", "'bool'", "'by'", "'chains'", "'class'", 
			"'classifier'", "'comment'", "'composite'", "'conjugate'", "'conjugates'", 
			"'conjugation'", "'connector'", "'datatype'", "'default'", "'dependency'", 
			"'derived'", null, "'differences'", "'disjoining'", "'disjoint'", "'doc'", 
			"'else'", "'end'", null, "'expr'", "'false'", "'feature'", "'featured'", 
			"'featuring'", "'filter'", "'first'", "'flow'", "'for'", "'from'", "'function'", 
			"'hastype'", "'if'", "'intersects'", "'implies'", "'import'", "'in'", 
			null, "'inout'", null, "'interaction'", "'inv'", "'inverse'", "'inverting'", 
			"'istype'", "'language'", "'member'", "'metaclass'", "'metadata'", "'multiplicity'", 
			"'namespace'", "'nonunique'", null, "'not'", "'null'", "'of'", "'or'", 
			"'ordered'", null, "'out'", null, "'package'", "'portion'", "'predicate'", 
			"'private'", "'protected'", "'public'", "'readonly'", null, "'redefines'", 
			"'redefinition'", "'ref'", null, "'references'", "'rep'", "'return'", 
			"'specialization'", "'specializes'", "'step'", "'struct'", "'subclassifier'", 
			"'subset'", "'subsets'", "'subtype'", "'succession'", "'then'", "'to'", 
			"'true'", "'type'", "'typed'", "'typing'", "'unions'", "'variation'", 
			null, "'xor'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "WS", "Boolean", "Integer", "Real", "DoubleQuotedString", 
			"ABOUT", "ABSTRACT", "ABSTRACT_PREFIX", "ALIAS", "ALL", "AND", "AS", 
			"ASSIGN", "ASSOC", "BEAHVIOR", "BINDING", "BOOL", "BY", "CHAINS", "CLASS", 
			"CLASSIFIER", "COMMENT", "COMPOSITE", "CONJUGATE", "CONJUGATES", "CONJUGATION", 
			"CONNECTOR", "DATATYPE", "DEFAULT", "DEPENDENCY", "DERIVED", "DERIVED_PREFIX", 
			"DIFFERENCES", "DISJOINING", "DISJOINT", "DOC", "ELSE", "END", "END_PREFIX", 
			"EXPR", "FALSE", "FEATURE", "FEATURED", "FEATURING", "FILTER", "FIRST", 
			"FLOW", "FOR", "FROM", "FUNCTION", "HASTYPE", "IF", "INTERSECTS", "IMPLIES", 
			"IMPORT", "IN", "IN_PREFIX", "INOUT", "INOUT_PREFIX", "INTERACTION", 
			"INV", "INVERSE", "INVERTING", "ISTYPE", "LANGUAGE", "MEMBER", "METACLASS", 
			"METADATA", "MULTIPLICITY", "NAMESPACE", "NONUNIQUE", "NONUNIQUE_SUFFIX", 
			"NOT", "NULL", "OF", "OR", "ORDERED", "ORDERED_SUFFIX", "OUT", "OUT_PREFIX", 
			"PACKAGE", "PORTION", "PREDICATE", "PRIAVTE", "PROTECTED", "PUBLIC", 
			"READONLY", "READONLY_PREFIX", "REDEFINES", "REDEFINITION", "REF", "REF_PREFIX", 
			"REFERENCES", "REP", "RETURN", "SPECIALIZTION", "SPECIALIZES", "STEP", 
			"STRCUT", "SUBCLASSIFIER", "SUBSET", "SUBSETS", "SUBTYPE", "SUCCESSION", 
			"THEN", "TO", "TRUE", "TYPE", "TYPED", "TYPING", "UNIONS", "VARIATION", 
			"VARIATION_PREFIX", "XOR", "ANY"
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
			setState(55);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(54);
				prefixExpression();
				}
				break;
			}
			setState(58);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(57);
				referenceExpression();
				}
				break;
			}
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 2)) & ~0x3f) == 0 && ((1L << (_la - 2)) & ((1L << (T__1 - 2)) | (1L << (T__2 - 2)) | (1L << (T__3 - 2)) | (1L << (T__8 - 2)) | (1L << (T__9 - 2)) | (1L << (T__10 - 2)) | (1L << (T__11 - 2)) | (1L << (WS - 2)) | (1L << (Boolean - 2)) | (1L << (Integer - 2)) | (1L << (Real - 2)) | (1L << (DoubleQuotedString - 2)) | (1L << (ABOUT - 2)) | (1L << (ABSTRACT - 2)) | (1L << (ABSTRACT_PREFIX - 2)) | (1L << (ALIAS - 2)) | (1L << (ALL - 2)) | (1L << (AND - 2)) | (1L << (AS - 2)) | (1L << (ASSIGN - 2)) | (1L << (ASSOC - 2)) | (1L << (BEAHVIOR - 2)) | (1L << (BINDING - 2)) | (1L << (BOOL - 2)) | (1L << (BY - 2)) | (1L << (CHAINS - 2)) | (1L << (CLASS - 2)) | (1L << (CLASSIFIER - 2)) | (1L << (COMMENT - 2)) | (1L << (COMPOSITE - 2)) | (1L << (CONJUGATE - 2)) | (1L << (CONJUGATES - 2)) | (1L << (CONJUGATION - 2)) | (1L << (CONNECTOR - 2)) | (1L << (DATATYPE - 2)) | (1L << (DEFAULT - 2)) | (1L << (DEPENDENCY - 2)) | (1L << (DERIVED - 2)) | (1L << (DERIVED_PREFIX - 2)) | (1L << (DIFFERENCES - 2)) | (1L << (DISJOINING - 2)) | (1L << (DISJOINT - 2)) | (1L << (DOC - 2)) | (1L << (ELSE - 2)) | (1L << (END - 2)) | (1L << (END_PREFIX - 2)) | (1L << (EXPR - 2)) | (1L << (FALSE - 2)) | (1L << (FEATURE - 2)) | (1L << (FEATURED - 2)) | (1L << (FEATURING - 2)) | (1L << (FILTER - 2)) | (1L << (FIRST - 2)) | (1L << (FLOW - 2)) | (1L << (FOR - 2)) | (1L << (FROM - 2)) | (1L << (FUNCTION - 2)) | (1L << (HASTYPE - 2)) | (1L << (IF - 2)))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (INTERSECTS - 66)) | (1L << (IMPLIES - 66)) | (1L << (IMPORT - 66)) | (1L << (IN - 66)) | (1L << (IN_PREFIX - 66)) | (1L << (INOUT - 66)) | (1L << (INOUT_PREFIX - 66)) | (1L << (INTERACTION - 66)) | (1L << (INV - 66)) | (1L << (INVERSE - 66)) | (1L << (INVERTING - 66)) | (1L << (ISTYPE - 66)) | (1L << (LANGUAGE - 66)) | (1L << (MEMBER - 66)) | (1L << (METACLASS - 66)) | (1L << (METADATA - 66)) | (1L << (MULTIPLICITY - 66)) | (1L << (NAMESPACE - 66)) | (1L << (NONUNIQUE - 66)) | (1L << (NOT - 66)) | (1L << (NULL - 66)) | (1L << (OF - 66)) | (1L << (OR - 66)) | (1L << (ORDERED - 66)) | (1L << (OUT - 66)) | (1L << (OUT_PREFIX - 66)) | (1L << (PACKAGE - 66)) | (1L << (PORTION - 66)) | (1L << (PREDICATE - 66)) | (1L << (PRIAVTE - 66)) | (1L << (PROTECTED - 66)) | (1L << (PUBLIC - 66)) | (1L << (READONLY - 66)) | (1L << (READONLY_PREFIX - 66)) | (1L << (REDEFINES - 66)) | (1L << (REDEFINITION - 66)) | (1L << (REF - 66)) | (1L << (REF_PREFIX - 66)) | (1L << (REFERENCES - 66)) | (1L << (REP - 66)) | (1L << (RETURN - 66)) | (1L << (SPECIALIZTION - 66)) | (1L << (SPECIALIZES - 66)) | (1L << (STEP - 66)) | (1L << (STRCUT - 66)) | (1L << (SUBCLASSIFIER - 66)) | (1L << (SUBSET - 66)) | (1L << (SUBSETS - 66)) | (1L << (SUBTYPE - 66)) | (1L << (SUCCESSION - 66)) | (1L << (THEN - 66)) | (1L << (TO - 66)) | (1L << (TRUE - 66)) | (1L << (TYPE - 66)) | (1L << (TYPED - 66)) | (1L << (TYPING - 66)) | (1L << (UNIONS - 66)) | (1L << (VARIATION - 66)) | (1L << (VARIATION_PREFIX - 66)) | (1L << (XOR - 66)) | (1L << (ANY - 66)))) != 0)) {
				{
				setState(60);
				name();
				}
			}

			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(63);
				multiplicityExpression();
				}
			}

			setState(67);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(66);
				multiplicityPropExpression();
				}
				break;
			}
			setState(69);
			featureExpressions();
			setState(70);
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
			setState(73);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(72);
				directionPrefixExpression();
				}
				break;
			}
			setState(76);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(75);
				abstractPrefixExpression();
				}
				break;
			}
			setState(79);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(78);
				variationPrefixExpression();
				}
				break;
			}
			setState(82);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(81);
				readonlyPrefixExpression();
				}
				break;
			}
			setState(85);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(84);
				derivedPrefixExpression();
				}
				break;
			}
			setState(88);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(87);
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
			setState(90);
			_la = _input.LA(1);
			if ( !(((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (IN_PREFIX - 70)) | (1L << (INOUT_PREFIX - 70)) | (1L << (OUT_PREFIX - 70)))) != 0)) ) {
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
			setState(92);
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
			setState(94);
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
			setState(96);
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
			setState(98);
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
			setState(100);
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
			setState(102);
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
			setState(104);
			match(T__0);
			setState(108);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(105);
				((MultiplicityExpressionContext)_localctx).lowerBound = multiplicityExpressionMember();
				setState(106);
				match(T__1);
				}
				break;
			}
			setState(110);
			((MultiplicityExpressionContext)_localctx).upperBound = multiplicityExpressionMember();
			setState(111);
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
			setState(114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDERED_SUFFIX) {
				{
				setState(113);
				orderedMultiplicityExpression();
				}
			}

			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NONUNIQUE_SUFFIX) {
				{
				setState(116);
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
			setState(119);
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
			setState(121);
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
			setState(123);
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
			setState(145);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(127);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(125);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(126);
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
				setState(130);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(129);
					typingExpression();
					}
				}

				setState(133);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(132);
					valueExpression();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(135);
					typingExpression();
					}
				}

				setState(140);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(138);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(139);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__7:
					break;
				default:
					break;
				}
				setState(143);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(142);
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
			setState(147);
			match(T__4);
			setState(148);
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
			setState(150);
			match(T__5);
			setState(151);
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
			setState(153);
			match(T__6);
			setState(154);
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
		public TerminalNode Real() { return getToken(DirectEditParser.Real, 0); }
		public TerminalNode Boolean() { return getToken(DirectEditParser.Boolean, 0); }
		public TerminalNode Integer() { return getToken(DirectEditParser.Integer, 0); }
		public TerminalNode DoubleQuotedString() { return getToken(DirectEditParser.DoubleQuotedString, 0); }
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
			setState(156);
			match(T__7);
			setState(157);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(162);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(158);
				match(T__0);
				setState(159);
				measurementExpression();
				setState(160);
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
		enterRule(_localctx, 38, RULE_measurementExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(164);
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
				setState(167); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & ((1L << (T__0 - 1)) | (1L << (T__1 - 1)) | (1L << (T__3 - 1)) | (1L << (T__4 - 1)) | (1L << (T__5 - 1)) | (1L << (T__6 - 1)) | (1L << (T__7 - 1)) | (1L << (T__8 - 1)) | (1L << (T__9 - 1)) | (1L << (T__10 - 1)) | (1L << (T__11 - 1)) | (1L << (T__12 - 1)) | (1L << (WS - 1)) | (1L << (Boolean - 1)) | (1L << (Integer - 1)) | (1L << (Real - 1)) | (1L << (DoubleQuotedString - 1)) | (1L << (ABOUT - 1)) | (1L << (ABSTRACT - 1)) | (1L << (ABSTRACT_PREFIX - 1)) | (1L << (ALIAS - 1)) | (1L << (ALL - 1)) | (1L << (AND - 1)) | (1L << (AS - 1)) | (1L << (ASSIGN - 1)) | (1L << (ASSOC - 1)) | (1L << (BEAHVIOR - 1)) | (1L << (BINDING - 1)) | (1L << (BOOL - 1)) | (1L << (BY - 1)) | (1L << (CHAINS - 1)) | (1L << (CLASS - 1)) | (1L << (CLASSIFIER - 1)) | (1L << (COMMENT - 1)) | (1L << (COMPOSITE - 1)) | (1L << (CONJUGATE - 1)) | (1L << (CONJUGATES - 1)) | (1L << (CONJUGATION - 1)) | (1L << (CONNECTOR - 1)) | (1L << (DATATYPE - 1)) | (1L << (DEFAULT - 1)) | (1L << (DEPENDENCY - 1)) | (1L << (DERIVED - 1)) | (1L << (DERIVED_PREFIX - 1)) | (1L << (DIFFERENCES - 1)) | (1L << (DISJOINING - 1)) | (1L << (DISJOINT - 1)) | (1L << (DOC - 1)) | (1L << (ELSE - 1)) | (1L << (END - 1)) | (1L << (END_PREFIX - 1)) | (1L << (EXPR - 1)) | (1L << (FALSE - 1)) | (1L << (FEATURE - 1)) | (1L << (FEATURED - 1)) | (1L << (FEATURING - 1)) | (1L << (FILTER - 1)) | (1L << (FIRST - 1)) | (1L << (FLOW - 1)) | (1L << (FOR - 1)) | (1L << (FROM - 1)) | (1L << (FUNCTION - 1)) | (1L << (HASTYPE - 1)))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (IF - 65)) | (1L << (INTERSECTS - 65)) | (1L << (IMPLIES - 65)) | (1L << (IMPORT - 65)) | (1L << (IN - 65)) | (1L << (IN_PREFIX - 65)) | (1L << (INOUT - 65)) | (1L << (INOUT_PREFIX - 65)) | (1L << (INTERACTION - 65)) | (1L << (INV - 65)) | (1L << (INVERSE - 65)) | (1L << (INVERTING - 65)) | (1L << (ISTYPE - 65)) | (1L << (LANGUAGE - 65)) | (1L << (MEMBER - 65)) | (1L << (METACLASS - 65)) | (1L << (METADATA - 65)) | (1L << (MULTIPLICITY - 65)) | (1L << (NAMESPACE - 65)) | (1L << (NONUNIQUE - 65)) | (1L << (NONUNIQUE_SUFFIX - 65)) | (1L << (NOT - 65)) | (1L << (NULL - 65)) | (1L << (OF - 65)) | (1L << (OR - 65)) | (1L << (ORDERED - 65)) | (1L << (ORDERED_SUFFIX - 65)) | (1L << (OUT - 65)) | (1L << (OUT_PREFIX - 65)) | (1L << (PACKAGE - 65)) | (1L << (PORTION - 65)) | (1L << (PREDICATE - 65)) | (1L << (PRIAVTE - 65)) | (1L << (PROTECTED - 65)) | (1L << (PUBLIC - 65)) | (1L << (READONLY - 65)) | (1L << (READONLY_PREFIX - 65)) | (1L << (REDEFINES - 65)) | (1L << (REDEFINITION - 65)) | (1L << (REF - 65)) | (1L << (REF_PREFIX - 65)) | (1L << (REFERENCES - 65)) | (1L << (REP - 65)) | (1L << (RETURN - 65)) | (1L << (SPECIALIZTION - 65)) | (1L << (SPECIALIZES - 65)) | (1L << (STEP - 65)) | (1L << (STRCUT - 65)) | (1L << (SUBCLASSIFIER - 65)) | (1L << (SUBSET - 65)) | (1L << (SUBSETS - 65)) | (1L << (SUBTYPE - 65)) | (1L << (SUCCESSION - 65)) | (1L << (THEN - 65)) | (1L << (TO - 65)) | (1L << (TRUE - 65)) | (1L << (TYPE - 65)) | (1L << (TYPED - 65)) | (1L << (TYPING - 65)) | (1L << (UNIONS - 65)) | (1L << (VARIATION - 65)) | (1L << (VARIATION_PREFIX - 65)) | (1L << (XOR - 65)) | (1L << (ANY - 65)))) != 0) );
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
		enterRule(_localctx, 40, RULE_transitionExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(169);
				triggerExpression();
				}
				break;
			}
			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(172);
				guardExpression();
				}
			}

			setState(176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(175);
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
		enterRule(_localctx, 42, RULE_triggerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			triggerExpressionName();
			setState(183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__8) {
				{
				{
				setState(179);
				match(T__8);
				setState(180);
				triggerExpressionName();
				}
				}
				setState(185);
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
		enterRule(_localctx, 44, RULE_triggerExpressionName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			name();
			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(187);
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
		enterRule(_localctx, 46, RULE_guardExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(T__0);
			setState(191);
			valueExpression();
			setState(192);
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
		enterRule(_localctx, 48, RULE_effectExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			match(T__9);
			setState(195);
			qualifiedName();
			setState(200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(196);
				match(T__10);
				setState(197);
				qualifiedName();
				}
				}
				setState(202);
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
		enterRule(_localctx, 50, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			name();
			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(204);
				match(T__11);
				setState(205);
				name();
				}
				}
				setState(210);
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
		enterRule(_localctx, 52, RULE_name);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(212); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(211);
					_la = _input.LA(1);
					if ( _la <= 0 || ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__12))) != 0) || _la==NONUNIQUE_SUFFIX || _la==ORDERED_SUFFIX) ) {
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
				setState(214); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
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
		"\u0004\u0001\u0080\u00d9\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0001\u0000\u0003"+
		"\u00008\b\u0000\u0001\u0000\u0003\u0000;\b\u0000\u0001\u0000\u0003\u0000"+
		">\b\u0000\u0001\u0000\u0003\u0000A\b\u0000\u0001\u0000\u0003\u0000D\b"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0003\u0001J\b"+
		"\u0001\u0001\u0001\u0003\u0001M\b\u0001\u0001\u0001\u0003\u0001P\b\u0001"+
		"\u0001\u0001\u0003\u0001S\b\u0001\u0001\u0001\u0003\u0001V\b\u0001\u0001"+
		"\u0001\u0003\u0001Y\b\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t"+
		"\u0001\t\u0003\tm\b\t\u0001\t\u0001\t\u0001\t\u0001\n\u0003\ns\b\n\u0001"+
		"\n\u0003\nv\b\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\u000e\u0001\u000e\u0003\u000e\u0080\b\u000e\u0001\u000e\u0003"+
		"\u000e\u0083\b\u000e\u0001\u000e\u0003\u000e\u0086\b\u000e\u0001\u000e"+
		"\u0003\u000e\u0089\b\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u008d\b"+
		"\u000e\u0001\u000e\u0003\u000e\u0090\b\u000e\u0003\u000e\u0092\b\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00a3\b\u0012\u0001\u0013"+
		"\u0004\u0013\u00a6\b\u0013\u000b\u0013\f\u0013\u00a7\u0001\u0014\u0003"+
		"\u0014\u00ab\b\u0014\u0001\u0014\u0003\u0014\u00ae\b\u0014\u0001\u0014"+
		"\u0003\u0014\u00b1\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0005\u0015"+
		"\u00b6\b\u0015\n\u0015\f\u0015\u00b9\t\u0015\u0001\u0016\u0001\u0016\u0003"+
		"\u0016\u00bd\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0005\u0018\u00c7\b\u0018\n"+
		"\u0018\f\u0018\u00ca\t\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0005"+
		"\u0019\u00cf\b\u0019\n\u0019\f\u0019\u00d2\t\u0019\u0001\u001a\u0004\u001a"+
		"\u00d5\b\u001a\u000b\u001a\f\u001a\u00d6\u0001\u001a\u0000\u0000\u001b"+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&(*,.024\u0000\u0005\u0003\u0000FFHH]]\u0002\u0000\u0004"+
		"\u0004\u0010\u0010\u0001\u0000\u000f\u0012\u0001\u0000\u0003\u0003\u0005"+
		"\u0000\u0001\u0001\u0005\b\r\rUU[[\u00de\u00007\u0001\u0000\u0000\u0000"+
		"\u0002I\u0001\u0000\u0000\u0000\u0004Z\u0001\u0000\u0000\u0000\u0006\\"+
		"\u0001\u0000\u0000\u0000\b^\u0001\u0000\u0000\u0000\n`\u0001\u0000\u0000"+
		"\u0000\fb\u0001\u0000\u0000\u0000\u000ed\u0001\u0000\u0000\u0000\u0010"+
		"f\u0001\u0000\u0000\u0000\u0012h\u0001\u0000\u0000\u0000\u0014r\u0001"+
		"\u0000\u0000\u0000\u0016w\u0001\u0000\u0000\u0000\u0018y\u0001\u0000\u0000"+
		"\u0000\u001a{\u0001\u0000\u0000\u0000\u001c\u0091\u0001\u0000\u0000\u0000"+
		"\u001e\u0093\u0001\u0000\u0000\u0000 \u0096\u0001\u0000\u0000\u0000\""+
		"\u0099\u0001\u0000\u0000\u0000$\u009c\u0001\u0000\u0000\u0000&\u00a5\u0001"+
		"\u0000\u0000\u0000(\u00aa\u0001\u0000\u0000\u0000*\u00b2\u0001\u0000\u0000"+
		"\u0000,\u00ba\u0001\u0000\u0000\u0000.\u00be\u0001\u0000\u0000\u00000"+
		"\u00c2\u0001\u0000\u0000\u00002\u00cb\u0001\u0000\u0000\u00004\u00d4\u0001"+
		"\u0000\u0000\u000068\u0003\u0002\u0001\u000076\u0001\u0000\u0000\u0000"+
		"78\u0001\u0000\u0000\u00008:\u0001\u0000\u0000\u00009;\u0003\u0010\b\u0000"+
		":9\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000;=\u0001\u0000\u0000"+
		"\u0000<>\u00034\u001a\u0000=<\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000"+
		"\u0000>@\u0001\u0000\u0000\u0000?A\u0003\u0012\t\u0000@?\u0001\u0000\u0000"+
		"\u0000@A\u0001\u0000\u0000\u0000AC\u0001\u0000\u0000\u0000BD\u0003\u0014"+
		"\n\u0000CB\u0001\u0000\u0000\u0000CD\u0001\u0000\u0000\u0000DE\u0001\u0000"+
		"\u0000\u0000EF\u0003\u001c\u000e\u0000FG\u0005\u0000\u0000\u0001G\u0001"+
		"\u0001\u0000\u0000\u0000HJ\u0003\u0004\u0002\u0000IH\u0001\u0000\u0000"+
		"\u0000IJ\u0001\u0000\u0000\u0000JL\u0001\u0000\u0000\u0000KM\u0003\u0006"+
		"\u0003\u0000LK\u0001\u0000\u0000\u0000LM\u0001\u0000\u0000\u0000MO\u0001"+
		"\u0000\u0000\u0000NP\u0003\b\u0004\u0000ON\u0001\u0000\u0000\u0000OP\u0001"+
		"\u0000\u0000\u0000PR\u0001\u0000\u0000\u0000QS\u0003\n\u0005\u0000RQ\u0001"+
		"\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000SU\u0001\u0000\u0000\u0000"+
		"TV\u0003\f\u0006\u0000UT\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000\u0000"+
		"VX\u0001\u0000\u0000\u0000WY\u0003\u000e\u0007\u0000XW\u0001\u0000\u0000"+
		"\u0000XY\u0001\u0000\u0000\u0000Y\u0003\u0001\u0000\u0000\u0000Z[\u0007"+
		"\u0000\u0000\u0000[\u0005\u0001\u0000\u0000\u0000\\]\u0005\u0015\u0000"+
		"\u0000]\u0007\u0001\u0000\u0000\u0000^_\u0005~\u0000\u0000_\t\u0001\u0000"+
		"\u0000\u0000`a\u0005e\u0000\u0000a\u000b\u0001\u0000\u0000\u0000bc\u0005"+
		"-\u0000\u0000c\r\u0001\u0000\u0000\u0000de\u00054\u0000\u0000e\u000f\u0001"+
		"\u0000\u0000\u0000fg\u0005i\u0000\u0000g\u0011\u0001\u0000\u0000\u0000"+
		"hl\u0005\u0001\u0000\u0000ij\u0003\u001a\r\u0000jk\u0005\u0002\u0000\u0000"+
		"km\u0001\u0000\u0000\u0000li\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000"+
		"\u0000mn\u0001\u0000\u0000\u0000no\u0003\u001a\r\u0000op\u0005\u0003\u0000"+
		"\u0000p\u0013\u0001\u0000\u0000\u0000qs\u0003\u0016\u000b\u0000rq\u0001"+
		"\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000su\u0001\u0000\u0000\u0000"+
		"tv\u0003\u0018\f\u0000ut\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000"+
		"v\u0015\u0001\u0000\u0000\u0000wx\u0005[\u0000\u0000x\u0017\u0001\u0000"+
		"\u0000\u0000yz\u0005U\u0000\u0000z\u0019\u0001\u0000\u0000\u0000{|\u0007"+
		"\u0001\u0000\u0000|\u001b\u0001\u0000\u0000\u0000}\u0080\u0003\u001e\u000f"+
		"\u0000~\u0080\u0003 \u0010\u0000\u007f}\u0001\u0000\u0000\u0000\u007f"+
		"~\u0001\u0000\u0000\u0000\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u0082"+
		"\u0001\u0000\u0000\u0000\u0081\u0083\u0003\"\u0011\u0000\u0082\u0081\u0001"+
		"\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000\u0083\u0085\u0001"+
		"\u0000\u0000\u0000\u0084\u0086\u0003$\u0012\u0000\u0085\u0084\u0001\u0000"+
		"\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086\u0092\u0001\u0000"+
		"\u0000\u0000\u0087\u0089\u0003\"\u0011\u0000\u0088\u0087\u0001\u0000\u0000"+
		"\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008c\u0001\u0000\u0000"+
		"\u0000\u008a\u008d\u0003\u001e\u000f\u0000\u008b\u008d\u0003 \u0010\u0000"+
		"\u008c\u008a\u0001\u0000\u0000\u0000\u008c\u008b\u0001\u0000\u0000\u0000"+
		"\u008c\u008d\u0001\u0000\u0000\u0000\u008d\u008f\u0001\u0000\u0000\u0000"+
		"\u008e\u0090\u0003$\u0012\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u008f"+
		"\u0090\u0001\u0000\u0000\u0000\u0090\u0092\u0001\u0000\u0000\u0000\u0091"+
		"\u007f\u0001\u0000\u0000\u0000\u0091\u0088\u0001\u0000\u0000\u0000\u0092"+
		"\u001d\u0001\u0000\u0000\u0000\u0093\u0094\u0005\u0005\u0000\u0000\u0094"+
		"\u0095\u00032\u0019\u0000\u0095\u001f\u0001\u0000\u0000\u0000\u0096\u0097"+
		"\u0005\u0006\u0000\u0000\u0097\u0098\u00032\u0019\u0000\u0098!\u0001\u0000"+
		"\u0000\u0000\u0099\u009a\u0005\u0007\u0000\u0000\u009a\u009b\u00032\u0019"+
		"\u0000\u009b#\u0001\u0000\u0000\u0000\u009c\u009d\u0005\b\u0000\u0000"+
		"\u009d\u00a2\u0007\u0002\u0000\u0000\u009e\u009f\u0005\u0001\u0000\u0000"+
		"\u009f\u00a0\u0003&\u0013\u0000\u00a0\u00a1\u0005\u0003\u0000\u0000\u00a1"+
		"\u00a3\u0001\u0000\u0000\u0000\u00a2\u009e\u0001\u0000\u0000\u0000\u00a2"+
		"\u00a3\u0001\u0000\u0000\u0000\u00a3%\u0001\u0000\u0000\u0000\u00a4\u00a6"+
		"\b\u0003\u0000\u0000\u00a5\u00a4\u0001\u0000\u0000\u0000\u00a6\u00a7\u0001"+
		"\u0000\u0000\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a7\u00a8\u0001"+
		"\u0000\u0000\u0000\u00a8\'\u0001\u0000\u0000\u0000\u00a9\u00ab\u0003*"+
		"\u0015\u0000\u00aa\u00a9\u0001\u0000\u0000\u0000\u00aa\u00ab\u0001\u0000"+
		"\u0000\u0000\u00ab\u00ad\u0001\u0000\u0000\u0000\u00ac\u00ae\u0003.\u0017"+
		"\u0000\u00ad\u00ac\u0001\u0000\u0000\u0000\u00ad\u00ae\u0001\u0000\u0000"+
		"\u0000\u00ae\u00b0\u0001\u0000\u0000\u0000\u00af\u00b1\u00030\u0018\u0000"+
		"\u00b0\u00af\u0001\u0000\u0000\u0000\u00b0\u00b1\u0001\u0000\u0000\u0000"+
		"\u00b1)\u0001\u0000\u0000\u0000\u00b2\u00b7\u0003,\u0016\u0000\u00b3\u00b4"+
		"\u0005\t\u0000\u0000\u00b4\u00b6\u0003,\u0016\u0000\u00b5\u00b3\u0001"+
		"\u0000\u0000\u0000\u00b6\u00b9\u0001\u0000\u0000\u0000\u00b7\u00b5\u0001"+
		"\u0000\u0000\u0000\u00b7\u00b8\u0001\u0000\u0000\u0000\u00b8+\u0001\u0000"+
		"\u0000\u0000\u00b9\u00b7\u0001\u0000\u0000\u0000\u00ba\u00bc\u00034\u001a"+
		"\u0000\u00bb\u00bd\u0003\"\u0011\u0000\u00bc\u00bb\u0001\u0000\u0000\u0000"+
		"\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd-\u0001\u0000\u0000\u0000\u00be"+
		"\u00bf\u0005\u0001\u0000\u0000\u00bf\u00c0\u0003$\u0012\u0000\u00c0\u00c1"+
		"\u0005\u0003\u0000\u0000\u00c1/\u0001\u0000\u0000\u0000\u00c2\u00c3\u0005"+
		"\n\u0000\u0000\u00c3\u00c8\u00032\u0019\u0000\u00c4\u00c5\u0005\u000b"+
		"\u0000\u0000\u00c5\u00c7\u00032\u0019\u0000\u00c6\u00c4\u0001\u0000\u0000"+
		"\u0000\u00c7\u00ca\u0001\u0000\u0000\u0000\u00c8\u00c6\u0001\u0000\u0000"+
		"\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c91\u0001\u0000\u0000\u0000"+
		"\u00ca\u00c8\u0001\u0000\u0000\u0000\u00cb\u00d0\u00034\u001a\u0000\u00cc"+
		"\u00cd\u0005\f\u0000\u0000\u00cd\u00cf\u00034\u001a\u0000\u00ce\u00cc"+
		"\u0001\u0000\u0000\u0000\u00cf\u00d2\u0001\u0000\u0000\u0000\u00d0\u00ce"+
		"\u0001\u0000\u0000\u0000\u00d0\u00d1\u0001\u0000\u0000\u0000\u00d13\u0001"+
		"\u0000\u0000\u0000\u00d2\u00d0\u0001\u0000\u0000\u0000\u00d3\u00d5\b\u0004"+
		"\u0000\u0000\u00d4\u00d3\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000"+
		"\u0000\u0000\u00d6\u00d4\u0001\u0000\u0000\u0000\u00d6\u00d7\u0001\u0000"+
		"\u0000\u0000\u00d75\u0001\u0000\u0000\u0000\u001f7:=@CILORUXlru\u007f"+
		"\u0082\u0085\u0088\u008c\u008f\u0091\u00a2\u00a7\u00aa\u00ad\u00b0\u00b7"+
		"\u00bc\u00c8\u00d0\u00d6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}