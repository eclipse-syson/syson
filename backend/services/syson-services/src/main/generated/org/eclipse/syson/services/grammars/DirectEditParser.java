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
		T__9=10, T__10=11, T__11=12, T__12=13, WS=14, ANY=15, Boolean=16, Integer=17, 
		Real=18, DoubleQuotedString=19, ABOUT=20, ABSTRACT=21, ALIAS=22, ALL=23, 
		AND=24, AS=25, ASSIGN=26, ASSOC=27, BEAHVIOR=28, BINDING=29, BOOL=30, 
		BY=31, CHAINS=32, CLASS=33, CLASSIFIER=34, COMMENT=35, COMPOSITE=36, CONJUGATE=37, 
		CONJUGATES=38, CONJUGATION=39, CONNECTOR=40, DATATYPE=41, DEFAULT=42, 
		DEPENDENCY=43, DERIVED=44, DIFFERENCES=45, DISJOINING=46, DISJOINT=47, 
		DOC=48, ELSE=49, END=50, EXPR=51, FALSE=52, FEATURE=53, FEATURED=54, FEATURING=55, 
		FILTER=56, FIRST=57, FLOW=58, FOR=59, FROM=60, FUNCTION=61, HASTYPE=62, 
		IF=63, INTERSECTS=64, IMPLIES=65, IMPORT=66, IN=67, INPUT=68, INTERACTION=69, 
		INV=70, INVERSE=71, INVERTING=72, ISTYPE=73, LANGUAGE=74, MEMBER=75, METACLASS=76, 
		METADATA=77, MULTIPLICITY=78, NAMESPACE=79, NONUNIQUE=80, NOT=81, NULL=82, 
		OF=83, OR=84, ORDERED=85, OUT=86, PACKAGE=87, PORTION=88, PREDICATE=89, 
		PRIAVTE=90, PROTECTED=91, PUBLIC=92, READONLY=93, REDEFINES=94, REDEFINITION=95, 
		RFERENCES=96, REP=97, RETURN=98, SPECIALIZTION=99, SPECIALIZES=100, STEP=101, 
		STRCUT=102, SUBCLASSIFIER=103, SUBSET=104, SUBSETS=105, SUBTYPE=106, SUCCESSION=107, 
		THEN=108, TO=109, TRUE=110, TYPE=111, TYPED=112, TYPING=113, UNIONS=114, 
		XOR=115;
	public static final int
		RULE_expression = 0, RULE_multiplicityExpression = 1, RULE_multiplicityExpressionMember = 2, 
		RULE_featureExpressions = 3, RULE_subsettingExpression = 4, RULE_redefinitionExpression = 5, 
		RULE_typingExpression = 6, RULE_valueExpression = 7, RULE_transitionExpression = 8, 
		RULE_triggerExpression = 9, RULE_triggerExpressionName = 10, RULE_guardExpression = 11, 
		RULE_effectExpression = 12, RULE_qualifiedName = 13, RULE_name = 14;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "multiplicityExpression", "multiplicityExpressionMember", 
			"featureExpressions", "subsettingExpression", "redefinitionExpression", 
			"typingExpression", "valueExpression", "transitionExpression", "triggerExpression", 
			"triggerExpressionName", "guardExpression", "effectExpression", "qualifiedName", 
			"name"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", "'..'", "']'", "'*'", "':>'", "':>>'", "':'", "'='", "'|'", 
			"'/'", "','", "'::'", "'::>'", null, null, null, null, null, null, "'about'", 
			"'abstract'", "'alias'", "'all'", "'and'", "'as'", "'assign'", "'assoc'", 
			"'behavior'", "'binding'", "'bool'", "'by'", "'chains'", "'class'", "'classifier'", 
			"'comment'", "'composite'", "'conjugate'", "'conjugates'", "'conjugation'", 
			"'connector'", "'datatype'", "'default'", "'dependency'", "'derived'", 
			"'differences'", "'disjoining'", "'disjoint'", "'doc'", "'else'", "'end'", 
			"'expr'", "'false'", "'feature'", "'featured'", "'featuring'", "'filter'", 
			"'first'", "'flow'", "'for'", "'from'", "'function'", "'hastype'", "'if'", 
			"'intersects'", "'implies'", "'import'", "'in'", "'inout'", "'interaction'", 
			"'inv'", "'inverse'", "'inverting'", "'istype'", "'language'", "'member'", 
			"'metaclass'", "'metadata'", "'multiplicity'", "'namespace'", "'nonunique'", 
			"'not'", "'null'", "'of'", "'or'", "'ordered'", "'out'", "'package'", 
			"'portion'", "'predicate'", "'private'", "'protected'", "'public'", "'readonly'", 
			"'redefines'", "'redefinition'", "'references'", "'rep'", "'return'", 
			"'specialization'", "'specializes'", "'step'", "'struct'", "'subclassifier'", 
			"'subset'", "'subsets'", "'subtype'", "'succession'", "'then'", "'to'", 
			"'true'", "'type'", "'typed'", "'typing'", "'unions'", "'xor'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "WS", "ANY", "Boolean", "Integer", "Real", "DoubleQuotedString", 
			"ABOUT", "ABSTRACT", "ALIAS", "ALL", "AND", "AS", "ASSIGN", "ASSOC", 
			"BEAHVIOR", "BINDING", "BOOL", "BY", "CHAINS", "CLASS", "CLASSIFIER", 
			"COMMENT", "COMPOSITE", "CONJUGATE", "CONJUGATES", "CONJUGATION", "CONNECTOR", 
			"DATATYPE", "DEFAULT", "DEPENDENCY", "DERIVED", "DIFFERENCES", "DISJOINING", 
			"DISJOINT", "DOC", "ELSE", "END", "EXPR", "FALSE", "FEATURE", "FEATURED", 
			"FEATURING", "FILTER", "FIRST", "FLOW", "FOR", "FROM", "FUNCTION", "HASTYPE", 
			"IF", "INTERSECTS", "IMPLIES", "IMPORT", "IN", "INPUT", "INTERACTION", 
			"INV", "INVERSE", "INVERTING", "ISTYPE", "LANGUAGE", "MEMBER", "METACLASS", 
			"METADATA", "MULTIPLICITY", "NAMESPACE", "NONUNIQUE", "NOT", "NULL", 
			"OF", "OR", "ORDERED", "OUT", "PACKAGE", "PORTION", "PREDICATE", "PRIAVTE", 
			"PROTECTED", "PUBLIC", "READONLY", "REDEFINES", "REDEFINITION", "RFERENCES", 
			"REP", "RETURN", "SPECIALIZTION", "SPECIALIZES", "STEP", "STRCUT", "SUBCLASSIFIER", 
			"SUBSET", "SUBSETS", "SUBTYPE", "SUCCESSION", "THEN", "TO", "TRUE", "TYPE", 
			"TYPED", "TYPING", "UNIONS", "XOR"
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
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public MultiplicityExpressionContext multiplicityExpression() {
			return getRuleContext(MultiplicityExpressionContext.class,0);
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(30);
				name();
				}
				break;
			}
			setState(34);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(33);
				multiplicityExpression();
				}
				break;
			}
			setState(36);
			featureExpressions();
			setState(37);
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
		enterRule(_localctx, 2, RULE_multiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			match(T__0);
			setState(43);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(40);
				((MultiplicityExpressionContext)_localctx).lowerBound = multiplicityExpressionMember();
				setState(41);
				match(T__1);
				}
				break;
			}
			setState(45);
			((MultiplicityExpressionContext)_localctx).upperBound = multiplicityExpressionMember();
			setState(46);
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
		enterRule(_localctx, 4, RULE_multiplicityExpressionMember);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
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
		public TransitionExpressionContext transitionExpression() {
			return getRuleContext(TransitionExpressionContext.class,0);
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
		enterRule(_localctx, 6, RULE_featureExpressions);
		int _la;
		try {
			setState(73);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(52);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(50);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(51);
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
				setState(55);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(54);
					typingExpression();
					}
				}

				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(57);
					valueExpression();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(61);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(60);
					typingExpression();
					}
				}

				setState(65);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(63);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(64);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__7:
					break;
				default:
					break;
				}
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(67);
					valueExpression();
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(71);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
				case 1:
					{
					setState(70);
					transitionExpression();
					}
					break;
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
		enterRule(_localctx, 8, RULE_subsettingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			match(T__4);
			setState(76);
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
		enterRule(_localctx, 10, RULE_redefinitionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			match(T__5);
			setState(79);
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
		enterRule(_localctx, 12, RULE_typingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(T__6);
			setState(82);
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
		enterRule(_localctx, 14, RULE_valueExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(T__7);
			setState(85);
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
		enterRule(_localctx, 16, RULE_transitionExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(87);
				triggerExpression();
				}
				break;
			}
			setState(91);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(90);
				guardExpression();
				}
			}

			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(93);
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
		enterRule(_localctx, 18, RULE_triggerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			triggerExpressionName();
			setState(101);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__8) {
				{
				{
				setState(97);
				match(T__8);
				setState(98);
				triggerExpressionName();
				}
				}
				setState(103);
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
		enterRule(_localctx, 20, RULE_triggerExpressionName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			name();
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(105);
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
		enterRule(_localctx, 22, RULE_guardExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			match(T__0);
			setState(109);
			valueExpression();
			setState(110);
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
		enterRule(_localctx, 24, RULE_effectExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(T__9);
			setState(113);
			qualifiedName();
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(114);
				match(T__10);
				setState(115);
				qualifiedName();
				}
				}
				setState(120);
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
		enterRule(_localctx, 26, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			name();
			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(122);
				match(T__11);
				setState(123);
				name();
				}
				}
				setState(128);
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
		enterRule(_localctx, 28, RULE_name);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(130); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(129);
					_la = _input.LA(1);
					if ( _la <= 0 || ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__12))) != 0)) ) {
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
				setState(132); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
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
		"\u0004\u0001s\u0087\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001\u0000\u0003\u0000"+
		" \b\u0000\u0001\u0000\u0003\u0000#\b\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001,\b"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0003\u00035\b\u0003\u0001\u0003\u0003\u00038\b\u0003"+
		"\u0001\u0003\u0003\u0003;\b\u0003\u0001\u0003\u0003\u0003>\b\u0003\u0001"+
		"\u0003\u0001\u0003\u0003\u0003B\b\u0003\u0001\u0003\u0003\u0003E\b\u0003"+
		"\u0001\u0003\u0003\u0003H\b\u0003\u0003\u0003J\b\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0003\b"+
		"Y\b\b\u0001\b\u0003\b\\\b\b\u0001\b\u0003\b_\b\b\u0001\t\u0001\t\u0001"+
		"\t\u0005\td\b\t\n\t\f\tg\t\t\u0001\n\u0001\n\u0003\nk\b\n\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0005"+
		"\fu\b\f\n\f\f\fx\t\f\u0001\r\u0001\r\u0001\r\u0005\r}\b\r\n\r\f\r\u0080"+
		"\t\r\u0001\u000e\u0004\u000e\u0083\b\u000e\u000b\u000e\f\u000e\u0084\u0001"+
		"\u000e\u0000\u0000\u000f\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012"+
		"\u0014\u0016\u0018\u001a\u001c\u0000\u0003\u0002\u0000\u0004\u0004\u0011"+
		"\u0011\u0001\u0000\u0010\u0013\u0003\u0000\u0001\u0001\u0005\b\r\r\u008d"+
		"\u0000\u001f\u0001\u0000\u0000\u0000\u0002\'\u0001\u0000\u0000\u0000\u0004"+
		"0\u0001\u0000\u0000\u0000\u0006I\u0001\u0000\u0000\u0000\bK\u0001\u0000"+
		"\u0000\u0000\nN\u0001\u0000\u0000\u0000\fQ\u0001\u0000\u0000\u0000\u000e"+
		"T\u0001\u0000\u0000\u0000\u0010X\u0001\u0000\u0000\u0000\u0012`\u0001"+
		"\u0000\u0000\u0000\u0014h\u0001\u0000\u0000\u0000\u0016l\u0001\u0000\u0000"+
		"\u0000\u0018p\u0001\u0000\u0000\u0000\u001ay\u0001\u0000\u0000\u0000\u001c"+
		"\u0082\u0001\u0000\u0000\u0000\u001e \u0003\u001c\u000e\u0000\u001f\u001e"+
		"\u0001\u0000\u0000\u0000\u001f \u0001\u0000\u0000\u0000 \"\u0001\u0000"+
		"\u0000\u0000!#\u0003\u0002\u0001\u0000\"!\u0001\u0000\u0000\u0000\"#\u0001"+
		"\u0000\u0000\u0000#$\u0001\u0000\u0000\u0000$%\u0003\u0006\u0003\u0000"+
		"%&\u0005\u0000\u0000\u0001&\u0001\u0001\u0000\u0000\u0000\'+\u0005\u0001"+
		"\u0000\u0000()\u0003\u0004\u0002\u0000)*\u0005\u0002\u0000\u0000*,\u0001"+
		"\u0000\u0000\u0000+(\u0001\u0000\u0000\u0000+,\u0001\u0000\u0000\u0000"+
		",-\u0001\u0000\u0000\u0000-.\u0003\u0004\u0002\u0000./\u0005\u0003\u0000"+
		"\u0000/\u0003\u0001\u0000\u0000\u000001\u0007\u0000\u0000\u00001\u0005"+
		"\u0001\u0000\u0000\u000025\u0003\b\u0004\u000035\u0003\n\u0005\u00004"+
		"2\u0001\u0000\u0000\u000043\u0001\u0000\u0000\u000045\u0001\u0000\u0000"+
		"\u000057\u0001\u0000\u0000\u000068\u0003\f\u0006\u000076\u0001\u0000\u0000"+
		"\u000078\u0001\u0000\u0000\u00008:\u0001\u0000\u0000\u00009;\u0003\u000e"+
		"\u0007\u0000:9\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000;J\u0001"+
		"\u0000\u0000\u0000<>\u0003\f\u0006\u0000=<\u0001\u0000\u0000\u0000=>\u0001"+
		"\u0000\u0000\u0000>A\u0001\u0000\u0000\u0000?B\u0003\b\u0004\u0000@B\u0003"+
		"\n\u0005\u0000A?\u0001\u0000\u0000\u0000A@\u0001\u0000\u0000\u0000AB\u0001"+
		"\u0000\u0000\u0000BD\u0001\u0000\u0000\u0000CE\u0003\u000e\u0007\u0000"+
		"DC\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000EJ\u0001\u0000\u0000"+
		"\u0000FH\u0003\u0010\b\u0000GF\u0001\u0000\u0000\u0000GH\u0001\u0000\u0000"+
		"\u0000HJ\u0001\u0000\u0000\u0000I4\u0001\u0000\u0000\u0000I=\u0001\u0000"+
		"\u0000\u0000IG\u0001\u0000\u0000\u0000J\u0007\u0001\u0000\u0000\u0000"+
		"KL\u0005\u0005\u0000\u0000LM\u0003\u001a\r\u0000M\t\u0001\u0000\u0000"+
		"\u0000NO\u0005\u0006\u0000\u0000OP\u0003\u001a\r\u0000P\u000b\u0001\u0000"+
		"\u0000\u0000QR\u0005\u0007\u0000\u0000RS\u0003\u001a\r\u0000S\r\u0001"+
		"\u0000\u0000\u0000TU\u0005\b\u0000\u0000UV\u0007\u0001\u0000\u0000V\u000f"+
		"\u0001\u0000\u0000\u0000WY\u0003\u0012\t\u0000XW\u0001\u0000\u0000\u0000"+
		"XY\u0001\u0000\u0000\u0000Y[\u0001\u0000\u0000\u0000Z\\\u0003\u0016\u000b"+
		"\u0000[Z\u0001\u0000\u0000\u0000[\\\u0001\u0000\u0000\u0000\\^\u0001\u0000"+
		"\u0000\u0000]_\u0003\u0018\f\u0000^]\u0001\u0000\u0000\u0000^_\u0001\u0000"+
		"\u0000\u0000_\u0011\u0001\u0000\u0000\u0000`e\u0003\u0014\n\u0000ab\u0005"+
		"\t\u0000\u0000bd\u0003\u0014\n\u0000ca\u0001\u0000\u0000\u0000dg\u0001"+
		"\u0000\u0000\u0000ec\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000"+
		"f\u0013\u0001\u0000\u0000\u0000ge\u0001\u0000\u0000\u0000hj\u0003\u001c"+
		"\u000e\u0000ik\u0003\f\u0006\u0000ji\u0001\u0000\u0000\u0000jk\u0001\u0000"+
		"\u0000\u0000k\u0015\u0001\u0000\u0000\u0000lm\u0005\u0001\u0000\u0000"+
		"mn\u0003\u000e\u0007\u0000no\u0005\u0003\u0000\u0000o\u0017\u0001\u0000"+
		"\u0000\u0000pq\u0005\n\u0000\u0000qv\u0003\u001a\r\u0000rs\u0005\u000b"+
		"\u0000\u0000su\u0003\u001a\r\u0000tr\u0001\u0000\u0000\u0000ux\u0001\u0000"+
		"\u0000\u0000vt\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000w\u0019"+
		"\u0001\u0000\u0000\u0000xv\u0001\u0000\u0000\u0000y~\u0003\u001c\u000e"+
		"\u0000z{\u0005\f\u0000\u0000{}\u0003\u001c\u000e\u0000|z\u0001\u0000\u0000"+
		"\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000~\u007f"+
		"\u0001\u0000\u0000\u0000\u007f\u001b\u0001\u0000\u0000\u0000\u0080~\u0001"+
		"\u0000\u0000\u0000\u0081\u0083\b\u0002\u0000\u0000\u0082\u0081\u0001\u0000"+
		"\u0000\u0000\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000"+
		"\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085\u001d\u0001\u0000"+
		"\u0000\u0000\u0013\u001f\"+47:=ADGIX[^ejv~\u0084";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}