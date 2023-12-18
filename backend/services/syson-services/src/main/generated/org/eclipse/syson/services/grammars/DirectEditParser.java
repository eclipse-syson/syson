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
		T__0=1, T__1=2, T__2=3, T__3=4, WS=5, Boolean=6, Integer=7, Real=8, SingleQuotedString=9, 
		DoubleQuotedString=10, Name=11, BasicName=12, BasicInitialCharacter=13, 
		BasicNameCharacter=14, UnrestrictedName=15, ABOUT=16, ABSTRACT=17, ALIAS=18, 
		ALL=19, AND=20, AS=21, ASSIGN=22, ASSOC=23, BEAHVIOR=24, BINDING=25, BOOL=26, 
		BY=27, CHAINS=28, CLASS=29, CLASSIFIER=30, COMMENT=31, COMPOSITE=32, CONJUGATE=33, 
		CONJUGATES=34, CONJUGATION=35, CONNECTOR=36, DATATYPE=37, DEFAULT=38, 
		DEPENDENCY=39, DERIVED=40, DIFFERENCES=41, DISJOINING=42, DISJOINT=43, 
		DOC=44, ELSE=45, END=46, EXPR=47, FALSE=48, FEATURE=49, FEATURED=50, FEATURING=51, 
		FILTER=52, FIRST=53, FLOW=54, FOR=55, FROM=56, FUNCTION=57, HASTYPE=58, 
		IF=59, INTERSECTS=60, IMPLIES=61, IMPORT=62, IN=63, INPUT=64, INTERACTION=65, 
		INV=66, INVERSE=67, INVERTING=68, ISTYPE=69, LANGUAGE=70, MEMBER=71, METACLASS=72, 
		METADATA=73, MULTIPLICITY=74, NAMESPACE=75, NONUNIQUE=76, NOT=77, NULL=78, 
		OF=79, OR=80, ORDERED=81, OUT=82, PACKAGE=83, PORTION=84, PREDICATE=85, 
		PRIAVTE=86, PROTECTED=87, PUBLIC=88, READONLY=89, REDEFINES=90, REDEFINITION=91, 
		RFERENCES=92, REP=93, RETURN=94, SPECIALIZTION=95, SPECIALIZES=96, STEP=97, 
		STRCUT=98, SUBCLASSIFIER=99, SUBSET=100, SUBSETS=101, SUBTYPE=102, SUCCESSION=103, 
		THEN=104, TO=105, TRUE=106, TYPE=107, TYPED=108, TYPING=109, UNIONS=110, 
		XOR=111;
	public static final int
		RULE_expression = 0, RULE_featureExpressions = 1, RULE_subsettingExpression = 2, 
		RULE_redefinitionExpression = 3, RULE_typingExpression = 4, RULE_valueExpression = 5;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "featureExpressions", "subsettingExpression", "redefinitionExpression", 
			"typingExpression", "valueExpression"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':>'", "':>>'", "':'", "'='", null, null, null, null, null, null, 
			null, null, null, null, null, "'about'", "'abstract'", "'alias'", "'all'", 
			"'and'", "'as'", "'assign'", "'assoc'", "'behavior'", "'binding'", "'bool'", 
			"'by'", "'chains'", "'class'", "'classifier'", "'comment'", "'composite'", 
			"'conjugate'", "'conjugates'", "'conjugation'", "'connector'", "'datatype'", 
			"'default'", "'dependency'", "'derived'", "'differences'", "'disjoining'", 
			"'disjoint'", "'doc'", "'else'", "'end'", "'expr'", "'false'", "'feature'", 
			"'featured'", "'featuring'", "'filter'", "'first'", "'flow'", "'for'", 
			"'from'", "'function'", "'hastype'", "'if'", "'intersects'", "'implies'", 
			"'import'", "'in'", "'inout'", "'interaction'", "'inv'", "'inverse'", 
			"'inverting'", "'istype'", "'language'", "'member'", "'metaclass'", "'metadata'", 
			"'multiplicity'", "'namespace'", "'nonunique'", "'not'", "'null'", "'of'", 
			"'or'", "'ordered'", "'out'", "'package'", "'portion'", "'predicate'", 
			"'private'", "'protected'", "'public'", "'readonly'", "'redefines'", 
			"'redefinition'", "'references'", "'rep'", "'return'", "'specialization'", 
			"'specializes'", "'step'", "'struct'", "'subclassifier'", "'subset'", 
			"'subsets'", "'subtype'", "'succession'", "'then'", "'to'", "'true'", 
			"'type'", "'typed'", "'typing'", "'unions'", "'xor'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, "WS", "Boolean", "Integer", "Real", "SingleQuotedString", 
			"DoubleQuotedString", "Name", "BasicName", "BasicInitialCharacter", "BasicNameCharacter", 
			"UnrestrictedName", "ABOUT", "ABSTRACT", "ALIAS", "ALL", "AND", "AS", 
			"ASSIGN", "ASSOC", "BEAHVIOR", "BINDING", "BOOL", "BY", "CHAINS", "CLASS", 
			"CLASSIFIER", "COMMENT", "COMPOSITE", "CONJUGATE", "CONJUGATES", "CONJUGATION", 
			"CONNECTOR", "DATATYPE", "DEFAULT", "DEPENDENCY", "DERIVED", "DIFFERENCES", 
			"DISJOINING", "DISJOINT", "DOC", "ELSE", "END", "EXPR", "FALSE", "FEATURE", 
			"FEATURED", "FEATURING", "FILTER", "FIRST", "FLOW", "FOR", "FROM", "FUNCTION", 
			"HASTYPE", "IF", "INTERSECTS", "IMPLIES", "IMPORT", "IN", "INPUT", "INTERACTION", 
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
			setState(13);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Name) {
				{
				setState(12);
				match(Name);
				}
			}

			setState(15);
			featureExpressions();
			setState(16);
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
		enterRule(_localctx, 2, RULE_featureExpressions);
		int _la;
		try {
			setState(38);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(20);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__0:
					{
					setState(18);
					subsettingExpression();
					}
					break;
				case T__1:
					{
					setState(19);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__2:
				case T__3:
				case Boolean:
				case Real:
				case DoubleQuotedString:
					break;
				default:
					break;
				}
				setState(23);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(22);
					typingExpression();
					}
				}

				setState(26);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << Boolean) | (1L << Real) | (1L << DoubleQuotedString))) != 0)) {
					{
					setState(25);
					valueExpression();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(29);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(28);
					typingExpression();
					}
				}

				setState(33);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__0:
					{
					setState(31);
					subsettingExpression();
					}
					break;
				case T__1:
					{
					setState(32);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__3:
				case Boolean:
				case Real:
				case DoubleQuotedString:
					break;
				default:
					break;
				}
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << Boolean) | (1L << Real) | (1L << DoubleQuotedString))) != 0)) {
					{
					setState(35);
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
		enterRule(_localctx, 4, RULE_subsettingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			match(T__0);
			setState(41);
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
		enterRule(_localctx, 6, RULE_redefinitionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			match(T__1);
			setState(44);
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
		enterRule(_localctx, 8, RULE_typingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			match(T__2);
			setState(47);
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
		enterRule(_localctx, 10, RULE_valueExpression);
		try {
			setState(54);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(49);
				match(T__3);
				setState(50);
				match(Integer);
				}
				break;
			case Real:
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				match(Real);
				}
				break;
			case Boolean:
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				match(Boolean);
				}
				break;
			case DoubleQuotedString:
				enterOuterAlt(_localctx, 4);
				{
				setState(53);
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
		"\u0004\u0001o9\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002"+
		"\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005"+
		"\u0007\u0005\u0001\u0000\u0003\u0000\u000e\b\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0003\u0001\u0015\b\u0001\u0001\u0001"+
		"\u0003\u0001\u0018\b\u0001\u0001\u0001\u0003\u0001\u001b\b\u0001\u0001"+
		"\u0001\u0003\u0001\u001e\b\u0001\u0001\u0001\u0001\u0001\u0003\u0001\""+
		"\b\u0001\u0001\u0001\u0003\u0001%\b\u0001\u0003\u0001\'\b\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0003\u00057\b\u0005\u0001\u0005\u0000\u0000\u0006"+
		"\u0000\u0002\u0004\u0006\b\n\u0000\u0000?\u0000\r\u0001\u0000\u0000\u0000"+
		"\u0002&\u0001\u0000\u0000\u0000\u0004(\u0001\u0000\u0000\u0000\u0006+"+
		"\u0001\u0000\u0000\u0000\b.\u0001\u0000\u0000\u0000\n6\u0001\u0000\u0000"+
		"\u0000\f\u000e\u0005\u000b\u0000\u0000\r\f\u0001\u0000\u0000\u0000\r\u000e"+
		"\u0001\u0000\u0000\u0000\u000e\u000f\u0001\u0000\u0000\u0000\u000f\u0010"+
		"\u0003\u0002\u0001\u0000\u0010\u0011\u0005\u0000\u0000\u0001\u0011\u0001"+
		"\u0001\u0000\u0000\u0000\u0012\u0015\u0003\u0004\u0002\u0000\u0013\u0015"+
		"\u0003\u0006\u0003\u0000\u0014\u0012\u0001\u0000\u0000\u0000\u0014\u0013"+
		"\u0001\u0000\u0000\u0000\u0014\u0015\u0001\u0000\u0000\u0000\u0015\u0017"+
		"\u0001\u0000\u0000\u0000\u0016\u0018\u0003\b\u0004\u0000\u0017\u0016\u0001"+
		"\u0000\u0000\u0000\u0017\u0018\u0001\u0000\u0000\u0000\u0018\u001a\u0001"+
		"\u0000\u0000\u0000\u0019\u001b\u0003\n\u0005\u0000\u001a\u0019\u0001\u0000"+
		"\u0000\u0000\u001a\u001b\u0001\u0000\u0000\u0000\u001b\'\u0001\u0000\u0000"+
		"\u0000\u001c\u001e\u0003\b\u0004\u0000\u001d\u001c\u0001\u0000\u0000\u0000"+
		"\u001d\u001e\u0001\u0000\u0000\u0000\u001e!\u0001\u0000\u0000\u0000\u001f"+
		"\"\u0003\u0004\u0002\u0000 \"\u0003\u0006\u0003\u0000!\u001f\u0001\u0000"+
		"\u0000\u0000! \u0001\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000\"$\u0001"+
		"\u0000\u0000\u0000#%\u0003\n\u0005\u0000$#\u0001\u0000\u0000\u0000$%\u0001"+
		"\u0000\u0000\u0000%\'\u0001\u0000\u0000\u0000&\u0014\u0001\u0000\u0000"+
		"\u0000&\u001d\u0001\u0000\u0000\u0000\'\u0003\u0001\u0000\u0000\u0000"+
		"()\u0005\u0001\u0000\u0000)*\u0005\u000b\u0000\u0000*\u0005\u0001\u0000"+
		"\u0000\u0000+,\u0005\u0002\u0000\u0000,-\u0005\u000b\u0000\u0000-\u0007"+
		"\u0001\u0000\u0000\u0000./\u0005\u0003\u0000\u0000/0\u0005\u000b\u0000"+
		"\u00000\t\u0001\u0000\u0000\u000012\u0005\u0004\u0000\u000027\u0005\u0007"+
		"\u0000\u000037\u0005\b\u0000\u000047\u0005\u0006\u0000\u000057\u0005\n"+
		"\u0000\u000061\u0001\u0000\u0000\u000063\u0001\u0000\u0000\u000064\u0001"+
		"\u0000\u0000\u000065\u0001\u0000\u0000\u00007\u000b\u0001\u0000\u0000"+
		"\u0000\t\r\u0014\u0017\u001a\u001d!$&6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}