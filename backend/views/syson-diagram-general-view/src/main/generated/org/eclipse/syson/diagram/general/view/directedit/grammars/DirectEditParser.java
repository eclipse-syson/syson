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
package org.eclipse.syson.diagram.general.view.directedit.grammars;

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
		T__0=1, T__1=2, T__2=3, WS=4, Integer=5, Real=6, String=7, Ident=8, ABOUT=9, 
		ABSTRACT=10, ALIAS=11, ALL=12, AND=13, AS=14, ASSIGN=15, ASSOC=16, BEAHVIOR=17, 
		BINDING=18, BOOL=19, BY=20, CHAINS=21, CLASS=22, CLASSIFIER=23, COMMENT=24, 
		COMPOSITE=25, CONJUGATE=26, CONJUGATES=27, CONJUGATION=28, CONNECTOR=29, 
		DATATYPE=30, DEFAULT=31, DEPENDENCY=32, DERIVED=33, DIFFERENCES=34, DISJOINING=35, 
		DISJOINT=36, DOC=37, ELSE=38, END=39, EXPR=40, FALSE=41, FEATURE=42, FEATURED=43, 
		FEATURING=44, FILTER=45, FIRST=46, FLOW=47, FOR=48, FROM=49, FUNCTION=50, 
		HASTYPE=51, IF=52, INTERSECTS=53, IMPLIES=54, IMPORT=55, IN=56, INPUT=57, 
		INTERACTION=58, INV=59, INVERSE=60, INVERTING=61, ISTYPE=62, LANGUAGE=63, 
		MEMBER=64, METACLASS=65, METADATA=66, MULTIPLICITY=67, NAMESPACE=68, NONUNIQUE=69, 
		NOT=70, NULL=71, OF=72, OR=73, ORDERED=74, OUT=75, PACKAGE=76, PORTION=77, 
		PREDICATE=78, PRIAVTE=79, PROTECTED=80, PUBLIC=81, READONLY=82, REDEFINES=83, 
		REDEFINITION=84, RFERENCES=85, REP=86, RETURN=87, SPECIALIZTION=88, SPECIALIZES=89, 
		STEP=90, STRCUT=91, SUBCLASSIFIER=92, SUBSET=93, SUBSETS=94, SUBTYPE=95, 
		SUCCESSION=96, THEN=97, TO=98, TRUE=99, TYPE=100, TYPED=101, TYPING=102, 
		UNIONS=103, XOR=104;
	public static final int
		RULE_expression = 0, RULE_typingExpression = 1, RULE_valueExpression = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "typingExpression", "valueExpression"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':'", "'='", "'.*'", null, null, null, null, null, "'about'", 
			"'abstract'", "'alias'", "'all'", "'and'", "'as'", "'assign'", "'assoc'", 
			"'behavior'", "'binding'", "'bool'", "'by'", "'chains'", "'class'", "'classifier'", 
			"'comment'", "'composite'", "'conjugate'", "'conjugates'", "'conjugation'", 
			"'connector'", "'datatype'", "'default'", "'dependency'", "'derived'", 
			"'differences'", "'disjoining'", "'disjoint'", "'doc'", "'else'", "'END'", 
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
			null, null, null, null, "WS", "Integer", "Real", "String", "Ident", "ABOUT", 
			"ABSTRACT", "ALIAS", "ALL", "AND", "AS", "ASSIGN", "ASSOC", "BEAHVIOR", 
			"BINDING", "BOOL", "BY", "CHAINS", "CLASS", "CLASSIFIER", "COMMENT", 
			"COMPOSITE", "CONJUGATE", "CONJUGATES", "CONJUGATION", "CONNECTOR", "DATATYPE", 
			"DEFAULT", "DEPENDENCY", "DERIVED", "DIFFERENCES", "DISJOINING", "DISJOINT", 
			"DOC", "ELSE", "END", "EXPR", "FALSE", "FEATURE", "FEATURED", "FEATURING", 
			"FILTER", "FIRST", "FLOW", "FOR", "FROM", "FUNCTION", "HASTYPE", "IF", 
			"INTERSECTS", "IMPLIES", "IMPORT", "IN", "INPUT", "INTERACTION", "INV", 
			"INVERSE", "INVERTING", "ISTYPE", "LANGUAGE", "MEMBER", "METACLASS", 
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
		public TerminalNode Ident() { return getToken(DirectEditParser.Ident, 0); }
		public TerminalNode EOF() { return getToken(DirectEditParser.EOF, 0); }
		public TypingExpressionContext typingExpression() {
			return getRuleContext(TypingExpressionContext.class,0);
		}
		public ValueExpressionContext valueExpression() {
			return getRuleContext(ValueExpressionContext.class,0);
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
			setState(6);
			match(Ident);
			setState(8);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(7);
				typingExpression();
				}
			}

			setState(11);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(10);
				valueExpression();
				}
			}

			setState(13);
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

	public static class TypingExpressionContext extends ParserRuleContext {
		public TerminalNode Ident() { return getToken(DirectEditParser.Ident, 0); }
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
		enterRule(_localctx, 2, RULE_typingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(15);
			match(T__0);
			setState(16);
			match(Ident);
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
		enterRule(_localctx, 4, RULE_valueExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			match(T__1);
			setState(19);
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

	public static final String _serializedATN =
		"\u0004\u0001h\u0016\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0001\u0000\u0001\u0000\u0003\u0000\t\b\u0000\u0001"+
		"\u0000\u0003\u0000\f\b\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0000"+
		"\u0000\u0003\u0000\u0002\u0004\u0000\u0000\u0014\u0000\u0006\u0001\u0000"+
		"\u0000\u0000\u0002\u000f\u0001\u0000\u0000\u0000\u0004\u0012\u0001\u0000"+
		"\u0000\u0000\u0006\b\u0005\b\u0000\u0000\u0007\t\u0003\u0002\u0001\u0000"+
		"\b\u0007\u0001\u0000\u0000\u0000\b\t\u0001\u0000\u0000\u0000\t\u000b\u0001"+
		"\u0000\u0000\u0000\n\f\u0003\u0004\u0002\u0000\u000b\n\u0001\u0000\u0000"+
		"\u0000\u000b\f\u0001\u0000\u0000\u0000\f\r\u0001\u0000\u0000\u0000\r\u000e"+
		"\u0005\u0000\u0000\u0001\u000e\u0001\u0001\u0000\u0000\u0000\u000f\u0010"+
		"\u0005\u0001\u0000\u0000\u0010\u0011\u0005\b\u0000\u0000\u0011\u0003\u0001"+
		"\u0000\u0000\u0000\u0012\u0013\u0005\u0002\u0000\u0000\u0013\u0014\u0005"+
		"\u0003\u0000\u0000\u0014\u0005\u0001\u0000\u0000\u0000\u0002\b\u000b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}