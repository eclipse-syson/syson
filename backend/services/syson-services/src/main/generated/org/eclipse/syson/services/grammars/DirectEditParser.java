// Generated from DirectEdit.g4 by ANTLR 4.10.1

/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, WS=9, 
		Boolean=10, Integer=11, Real=12, SingleQuotedString=13, DoubleQuotedString=14, 
		Name=15, BasicName=16, BasicInitialCharacter=17, BasicNameCharacter=18, 
		UnrestrictedName=19, ABOUT=20, ABSTRACT=21, ALIAS=22, ALL=23, AND=24, 
		AS=25, ASSIGN=26, ASSOC=27, BEAHVIOR=28, BINDING=29, BOOL=30, BY=31, CHAINS=32, 
		CLASS=33, CLASSIFIER=34, COMMENT=35, COMPOSITE=36, CONJUGATE=37, CONJUGATES=38, 
		CONJUGATION=39, CONNECTOR=40, DATATYPE=41, DEFAULT=42, DEPENDENCY=43, 
		DERIVED=44, DIFFERENCES=45, DISJOINING=46, DISJOINT=47, DOC=48, ELSE=49, 
		END=50, EXPR=51, FALSE=52, FEATURE=53, FEATURED=54, FEATURING=55, FILTER=56, 
		FIRST=57, FLOW=58, FOR=59, FROM=60, FUNCTION=61, HASTYPE=62, IF=63, INTERSECTS=64, 
		IMPLIES=65, IMPORT=66, IN=67, INPUT=68, INTERACTION=69, INV=70, INVERSE=71, 
		INVERTING=72, ISTYPE=73, LANGUAGE=74, MEMBER=75, METACLASS=76, METADATA=77, 
		MULTIPLICITY=78, NAMESPACE=79, NONUNIQUE=80, NOT=81, NULL=82, OF=83, OR=84, 
		ORDERED=85, OUT=86, PACKAGE=87, PORTION=88, PREDICATE=89, PRIAVTE=90, 
		PROTECTED=91, PUBLIC=92, READONLY=93, REDEFINES=94, REDEFINITION=95, RFERENCES=96, 
		REP=97, RETURN=98, SPECIALIZTION=99, SPECIALIZES=100, STEP=101, STRCUT=102, 
		SUBCLASSIFIER=103, SUBSET=104, SUBSETS=105, SUBTYPE=106, SUCCESSION=107, 
		THEN=108, TO=109, TRUE=110, TYPE=111, TYPED=112, TYPING=113, UNIONS=114, 
		XOR=115;
	public static final int
		RULE_expression = 0, RULE_multiplicityExpression = 1, RULE_multiplicityExpressionMember = 2, 
		RULE_featureExpressions = 3, RULE_subsettingExpression = 4, RULE_redefinitionExpression = 5, 
		RULE_typingExpression = 6, RULE_valueExpression = 7;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "multiplicityExpression", "multiplicityExpressionMember", 
			"featureExpressions", "subsettingExpression", "redefinitionExpression", 
			"typingExpression", "valueExpression"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", "'..'", "']'", "'*'", "':>'", "':>>'", "':'", "'='", null, 
			null, null, null, null, null, null, null, null, null, null, "'about'", 
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
			null, null, null, null, null, null, null, null, null, "WS", "Boolean", 
			"Integer", "Real", "SingleQuotedString", "DoubleQuotedString", "Name", 
			"BasicName", "BasicInitialCharacter", "BasicNameCharacter", "UnrestrictedName", 
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
		public TerminalNode Name() { return getToken(DirectEditParser.Name, 0); }
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(17);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Name) {
				{
				setState(16);
				match(Name);
				}
			}

			setState(20);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(19);
				multiplicityExpression();
				}
			}

			setState(22);
			featureExpressions();
			setState(23);
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
			setState(25);
			match(T__0);
			setState(29);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(26);
				((MultiplicityExpressionContext)_localctx).lowerBound = multiplicityExpressionMember();
				setState(27);
				match(T__1);
				}
				break;
			}
			setState(31);
			((MultiplicityExpressionContext)_localctx).upperBound = multiplicityExpressionMember();
			setState(32);
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
			setState(34);
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
		enterRule(_localctx, 6, RULE_featureExpressions);
		int _la;
		try {
			setState(56);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(38);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(36);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(37);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__6:
				case T__7:
				case Boolean:
				case Real:
				case DoubleQuotedString:
					break;
				default:
					break;
				}
				setState(41);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(40);
					typingExpression();
					}
				}

				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << Boolean) | (1L << Real) | (1L << DoubleQuotedString))) != 0)) {
					{
					setState(43);
					valueExpression();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(46);
					typingExpression();
					}
				}

				setState(51);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(49);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(50);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__7:
				case Boolean:
				case Real:
				case DoubleQuotedString:
					break;
				default:
					break;
				}
				setState(54);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << Boolean) | (1L << Real) | (1L << DoubleQuotedString))) != 0)) {
					{
					setState(53);
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
		public TerminalNode Name() { return getToken(DirectEditParser.Name, 0); }
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
			setState(58);
			match(T__4);
			setState(59);
			match(Name);
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
		public TerminalNode Name() { return getToken(DirectEditParser.Name, 0); }
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
			setState(61);
			match(T__5);
			setState(62);
			match(Name);
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
		public TerminalNode Name() { return getToken(DirectEditParser.Name, 0); }
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
			setState(64);
			match(T__6);
			setState(65);
			match(Name);
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
		public TerminalNode Integer() { return getToken(DirectEditParser.Integer, 0); }
		public TerminalNode Real() { return getToken(DirectEditParser.Real, 0); }
		public TerminalNode Boolean() { return getToken(DirectEditParser.Boolean, 0); }
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
		try {
			setState(72);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(67);
				match(T__7);
				setState(68);
				match(Integer);
				}
				break;
			case Real:
				enterOuterAlt(_localctx, 2);
				{
				setState(69);
				match(Real);
				}
				break;
			case Boolean:
				enterOuterAlt(_localctx, 3);
				{
				setState(70);
				match(Boolean);
				}
				break;
			case DoubleQuotedString:
				enterOuterAlt(_localctx, 4);
				{
				setState(71);
				match(DoubleQuotedString);
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

	public static final String _serializedATN =
		"\u0004\u0001sK\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002"+
		"\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005"+
		"\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0001\u0000"+
		"\u0003\u0000\u0012\b\u0000\u0001\u0000\u0003\u0000\u0015\b\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0003\u0001\u001e\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0003\u0003\'\b\u0003\u0001"+
		"\u0003\u0003\u0003*\b\u0003\u0001\u0003\u0003\u0003-\b\u0003\u0001\u0003"+
		"\u0003\u00030\b\u0003\u0001\u0003\u0001\u0003\u0003\u00034\b\u0003\u0001"+
		"\u0003\u0003\u00037\b\u0003\u0003\u00039\b\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0003\u0007I\b\u0007\u0001\u0007\u0000\u0000\b\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0000\u0001\u0002\u0000\u0004\u0004\u000b\u000bQ\u0000\u0011"+
		"\u0001\u0000\u0000\u0000\u0002\u0019\u0001\u0000\u0000\u0000\u0004\"\u0001"+
		"\u0000\u0000\u0000\u00068\u0001\u0000\u0000\u0000\b:\u0001\u0000\u0000"+
		"\u0000\n=\u0001\u0000\u0000\u0000\f@\u0001\u0000\u0000\u0000\u000eH\u0001"+
		"\u0000\u0000\u0000\u0010\u0012\u0005\u000f\u0000\u0000\u0011\u0010\u0001"+
		"\u0000\u0000\u0000\u0011\u0012\u0001\u0000\u0000\u0000\u0012\u0014\u0001"+
		"\u0000\u0000\u0000\u0013\u0015\u0003\u0002\u0001\u0000\u0014\u0013\u0001"+
		"\u0000\u0000\u0000\u0014\u0015\u0001\u0000\u0000\u0000\u0015\u0016\u0001"+
		"\u0000\u0000\u0000\u0016\u0017\u0003\u0006\u0003\u0000\u0017\u0018\u0005"+
		"\u0000\u0000\u0001\u0018\u0001\u0001\u0000\u0000\u0000\u0019\u001d\u0005"+
		"\u0001\u0000\u0000\u001a\u001b\u0003\u0004\u0002\u0000\u001b\u001c\u0005"+
		"\u0002\u0000\u0000\u001c\u001e\u0001\u0000\u0000\u0000\u001d\u001a\u0001"+
		"\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e\u001f\u0001"+
		"\u0000\u0000\u0000\u001f \u0003\u0004\u0002\u0000 !\u0005\u0003\u0000"+
		"\u0000!\u0003\u0001\u0000\u0000\u0000\"#\u0007\u0000\u0000\u0000#\u0005"+
		"\u0001\u0000\u0000\u0000$\'\u0003\b\u0004\u0000%\'\u0003\n\u0005\u0000"+
		"&$\u0001\u0000\u0000\u0000&%\u0001\u0000\u0000\u0000&\'\u0001\u0000\u0000"+
		"\u0000\')\u0001\u0000\u0000\u0000(*\u0003\f\u0006\u0000)(\u0001\u0000"+
		"\u0000\u0000)*\u0001\u0000\u0000\u0000*,\u0001\u0000\u0000\u0000+-\u0003"+
		"\u000e\u0007\u0000,+\u0001\u0000\u0000\u0000,-\u0001\u0000\u0000\u0000"+
		"-9\u0001\u0000\u0000\u0000.0\u0003\f\u0006\u0000/.\u0001\u0000\u0000\u0000"+
		"/0\u0001\u0000\u0000\u000003\u0001\u0000\u0000\u000014\u0003\b\u0004\u0000"+
		"24\u0003\n\u0005\u000031\u0001\u0000\u0000\u000032\u0001\u0000\u0000\u0000"+
		"34\u0001\u0000\u0000\u000046\u0001\u0000\u0000\u000057\u0003\u000e\u0007"+
		"\u000065\u0001\u0000\u0000\u000067\u0001\u0000\u0000\u000079\u0001\u0000"+
		"\u0000\u00008&\u0001\u0000\u0000\u00008/\u0001\u0000\u0000\u00009\u0007"+
		"\u0001\u0000\u0000\u0000:;\u0005\u0005\u0000\u0000;<\u0005\u000f\u0000"+
		"\u0000<\t\u0001\u0000\u0000\u0000=>\u0005\u0006\u0000\u0000>?\u0005\u000f"+
		"\u0000\u0000?\u000b\u0001\u0000\u0000\u0000@A\u0005\u0007\u0000\u0000"+
		"AB\u0005\u000f\u0000\u0000B\r\u0001\u0000\u0000\u0000CD\u0005\b\u0000"+
		"\u0000DI\u0005\u000b\u0000\u0000EI\u0005\f\u0000\u0000FI\u0005\n\u0000"+
		"\u0000GI\u0005\u000e\u0000\u0000HC\u0001\u0000\u0000\u0000HE\u0001\u0000"+
		"\u0000\u0000HF\u0001\u0000\u0000\u0000HG\u0001\u0000\u0000\u0000I\u000f"+
		"\u0001\u0000\u0000\u0000\u000b\u0011\u0014\u001d&),/368H";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}