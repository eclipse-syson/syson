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
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, WS=15, Boolean=16, Integer=17, 
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
		XOR=115, ANY=116;
	public static final int
		RULE_expression = 0, RULE_referenceExpression = 1, RULE_multiplicityExpression = 2, 
		RULE_multiplicityExpressionMember = 3, RULE_featureExpressions = 4, RULE_subsettingExpression = 5, 
		RULE_redefinitionExpression = 6, RULE_typingExpression = 7, RULE_valueExpression = 8, 
		RULE_transitionExpression = 9, RULE_triggerExpression = 10, RULE_triggerExpressionName = 11, 
		RULE_guardExpression = 12, RULE_effectExpression = 13, RULE_qualifiedName = 14, 
		RULE_name = 15;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "referenceExpression", "multiplicityExpression", "multiplicityExpressionMember", 
			"featureExpressions", "subsettingExpression", "redefinitionExpression", 
			"typingExpression", "valueExpression", "transitionExpression", "triggerExpression", 
			"triggerExpressionName", "guardExpression", "effectExpression", "qualifiedName", 
			"name"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'ref '", "'['", "'..'", "']'", "'*'", "':>'", "':>>'", "':'", 
			"'='", "'|'", "'/'", "','", "'::'", "'::>'", null, null, null, null, 
			null, "'about'", "'abstract'", "'alias'", "'all'", "'and'", "'as'", "'assign'", 
			"'assoc'", "'behavior'", "'binding'", "'bool'", "'by'", "'chains'", "'class'", 
			"'classifier'", "'comment'", "'composite'", "'conjugate'", "'conjugates'", 
			"'conjugation'", "'connector'", "'datatype'", "'default'", "'dependency'", 
			"'derived'", "'differences'", "'disjoining'", "'disjoint'", "'doc'", 
			"'else'", "'end'", "'expr'", "'false'", "'feature'", "'featured'", "'featuring'", 
			"'filter'", "'first'", "'flow'", "'for'", "'from'", "'function'", "'hastype'", 
			"'if'", "'intersects'", "'implies'", "'import'", "'in'", "'inout'", "'interaction'", 
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
			null, null, null, "WS", "Boolean", "Integer", "Real", "DoubleQuotedString", 
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
			"TYPED", "TYPING", "UNIONS", "XOR", "ANY"
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
		public ReferenceExpressionContext referenceExpression() {
			return getRuleContext(ReferenceExpressionContext.class,0);
		}
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(32);
				referenceExpression();
				}
			}

			setState(36);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << WS) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY) | (1L << DERIVED) | (1L << DIFFERENCES) | (1L << DISJOINING) | (1L << DISJOINT) | (1L << DOC) | (1L << ELSE) | (1L << END) | (1L << EXPR) | (1L << FALSE) | (1L << FEATURE) | (1L << FEATURED) | (1L << FEATURING) | (1L << FILTER) | (1L << FIRST) | (1L << FLOW) | (1L << FOR) | (1L << FROM) | (1L << FUNCTION) | (1L << HASTYPE) | (1L << IF))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (INPUT - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (OUT - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (RFERENCES - 64)) | (1L << (REP - 64)) | (1L << (RETURN - 64)) | (1L << (SPECIALIZTION - 64)) | (1L << (SPECIALIZES - 64)) | (1L << (STEP - 64)) | (1L << (STRCUT - 64)) | (1L << (SUBCLASSIFIER - 64)) | (1L << (SUBSET - 64)) | (1L << (SUBSETS - 64)) | (1L << (SUBTYPE - 64)) | (1L << (SUCCESSION - 64)) | (1L << (THEN - 64)) | (1L << (TO - 64)) | (1L << (TRUE - 64)) | (1L << (TYPE - 64)) | (1L << (TYPED - 64)) | (1L << (TYPING - 64)) | (1L << (UNIONS - 64)) | (1L << (XOR - 64)) | (1L << (ANY - 64)))) != 0)) {
				{
				setState(35);
				name();
				}
			}

			setState(39);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(38);
				multiplicityExpression();
				}
			}

			setState(41);
			featureExpressions();
			setState(42);
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

	public static class ReferenceExpressionContext extends ParserRuleContext {
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
		enterRule(_localctx, 2, RULE_referenceExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			match(T__0);
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
		enterRule(_localctx, 4, RULE_multiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			match(T__1);
			setState(50);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(47);
				((MultiplicityExpressionContext)_localctx).lowerBound = multiplicityExpressionMember();
				setState(48);
				match(T__2);
				}
				break;
			}
			setState(52);
			((MultiplicityExpressionContext)_localctx).upperBound = multiplicityExpressionMember();
			setState(53);
			match(T__3);
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
		enterRule(_localctx, 6, RULE_multiplicityExpressionMember);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			_la = _input.LA(1);
			if ( !(_la==T__4 || _la==Integer) ) {
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
		enterRule(_localctx, 8, RULE_featureExpressions);
		int _la;
		try {
			setState(77);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(59);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__5:
					{
					setState(57);
					subsettingExpression();
					}
					break;
				case T__6:
					{
					setState(58);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__7:
				case T__8:
					break;
				default:
					break;
				}
				setState(62);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(61);
					typingExpression();
					}
				}

				setState(65);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(64);
					valueExpression();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(67);
					typingExpression();
					}
				}

				setState(72);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__5:
					{
					setState(70);
					subsettingExpression();
					}
					break;
				case T__6:
					{
					setState(71);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__8:
					break;
				default:
					break;
				}
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(74);
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
		enterRule(_localctx, 10, RULE_subsettingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			match(T__5);
			setState(80);
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
		enterRule(_localctx, 12, RULE_redefinitionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			match(T__6);
			setState(83);
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
		enterRule(_localctx, 14, RULE_typingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(T__7);
			setState(86);
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
		enterRule(_localctx, 16, RULE_valueExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(T__8);
			setState(89);
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
		enterRule(_localctx, 18, RULE_transitionExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(91);
				triggerExpression();
				}
				break;
			}
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(94);
				guardExpression();
				}
			}

			setState(98);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(97);
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
		enterRule(_localctx, 20, RULE_triggerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			triggerExpressionName();
			setState(105);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__9) {
				{
				{
				setState(101);
				match(T__9);
				setState(102);
				triggerExpressionName();
				}
				}
				setState(107);
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
		enterRule(_localctx, 22, RULE_triggerExpressionName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			name();
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(109);
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
		enterRule(_localctx, 24, RULE_guardExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(T__1);
			setState(113);
			valueExpression();
			setState(114);
			match(T__3);
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
		enterRule(_localctx, 26, RULE_effectExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(T__10);
			setState(117);
			qualifiedName();
			setState(122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(118);
				match(T__11);
				setState(119);
				qualifiedName();
				}
				}
				setState(124);
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
		enterRule(_localctx, 28, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			name();
			setState(130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(126);
				match(T__12);
				setState(127);
				name();
				}
				}
				setState(132);
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
		enterRule(_localctx, 30, RULE_name);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(134); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(133);
					_la = _input.LA(1);
					if ( _la <= 0 || ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__13))) != 0)) ) {
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
				setState(136); 
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
		"\u0004\u0001t\u008b\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0001\u0000\u0003\u0000\"\b\u0000\u0001\u0000\u0003\u0000%\b\u0000\u0001"+
		"\u0000\u0003\u0000(\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003"+
		"\u00023\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0003\u0004<\b\u0004\u0001\u0004\u0003"+
		"\u0004?\b\u0004\u0001\u0004\u0003\u0004B\b\u0004\u0001\u0004\u0003\u0004"+
		"E\b\u0004\u0001\u0004\u0001\u0004\u0003\u0004I\b\u0004\u0001\u0004\u0003"+
		"\u0004L\b\u0004\u0003\u0004N\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\t\u0003\t]\b\t\u0001\t\u0003\t`\b\t\u0001"+
		"\t\u0003\tc\b\t\u0001\n\u0001\n\u0001\n\u0005\nh\b\n\n\n\f\nk\t\n\u0001"+
		"\u000b\u0001\u000b\u0003\u000bo\b\u000b\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0001\r\u0001\r\u0005\ry\b\r\n\r\f\r|\t\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0005\u000e\u0081\b\u000e\n\u000e\f\u000e\u0084"+
		"\t\u000e\u0001\u000f\u0004\u000f\u0087\b\u000f\u000b\u000f\f\u000f\u0088"+
		"\u0001\u000f\u0000\u0000\u0010\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e\u0000\u0003\u0002\u0000\u0005"+
		"\u0005\u0011\u0011\u0001\u0000\u0010\u0013\u0003\u0000\u0001\u0002\u0006"+
		"\t\u000e\u000e\u008f\u0000!\u0001\u0000\u0000\u0000\u0002,\u0001\u0000"+
		"\u0000\u0000\u0004.\u0001\u0000\u0000\u0000\u00067\u0001\u0000\u0000\u0000"+
		"\bM\u0001\u0000\u0000\u0000\nO\u0001\u0000\u0000\u0000\fR\u0001\u0000"+
		"\u0000\u0000\u000eU\u0001\u0000\u0000\u0000\u0010X\u0001\u0000\u0000\u0000"+
		"\u0012\\\u0001\u0000\u0000\u0000\u0014d\u0001\u0000\u0000\u0000\u0016"+
		"l\u0001\u0000\u0000\u0000\u0018p\u0001\u0000\u0000\u0000\u001at\u0001"+
		"\u0000\u0000\u0000\u001c}\u0001\u0000\u0000\u0000\u001e\u0086\u0001\u0000"+
		"\u0000\u0000 \"\u0003\u0002\u0001\u0000! \u0001\u0000\u0000\u0000!\"\u0001"+
		"\u0000\u0000\u0000\"$\u0001\u0000\u0000\u0000#%\u0003\u001e\u000f\u0000"+
		"$#\u0001\u0000\u0000\u0000$%\u0001\u0000\u0000\u0000%\'\u0001\u0000\u0000"+
		"\u0000&(\u0003\u0004\u0002\u0000\'&\u0001\u0000\u0000\u0000\'(\u0001\u0000"+
		"\u0000\u0000()\u0001\u0000\u0000\u0000)*\u0003\b\u0004\u0000*+\u0005\u0000"+
		"\u0000\u0001+\u0001\u0001\u0000\u0000\u0000,-\u0005\u0001\u0000\u0000"+
		"-\u0003\u0001\u0000\u0000\u0000.2\u0005\u0002\u0000\u0000/0\u0003\u0006"+
		"\u0003\u000001\u0005\u0003\u0000\u000013\u0001\u0000\u0000\u00002/\u0001"+
		"\u0000\u0000\u000023\u0001\u0000\u0000\u000034\u0001\u0000\u0000\u0000"+
		"45\u0003\u0006\u0003\u000056\u0005\u0004\u0000\u00006\u0005\u0001\u0000"+
		"\u0000\u000078\u0007\u0000\u0000\u00008\u0007\u0001\u0000\u0000\u0000"+
		"9<\u0003\n\u0005\u0000:<\u0003\f\u0006\u0000;9\u0001\u0000\u0000\u0000"+
		";:\u0001\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000<>\u0001\u0000\u0000"+
		"\u0000=?\u0003\u000e\u0007\u0000>=\u0001\u0000\u0000\u0000>?\u0001\u0000"+
		"\u0000\u0000?A\u0001\u0000\u0000\u0000@B\u0003\u0010\b\u0000A@\u0001\u0000"+
		"\u0000\u0000AB\u0001\u0000\u0000\u0000BN\u0001\u0000\u0000\u0000CE\u0003"+
		"\u000e\u0007\u0000DC\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000"+
		"EH\u0001\u0000\u0000\u0000FI\u0003\n\u0005\u0000GI\u0003\f\u0006\u0000"+
		"HF\u0001\u0000\u0000\u0000HG\u0001\u0000\u0000\u0000HI\u0001\u0000\u0000"+
		"\u0000IK\u0001\u0000\u0000\u0000JL\u0003\u0010\b\u0000KJ\u0001\u0000\u0000"+
		"\u0000KL\u0001\u0000\u0000\u0000LN\u0001\u0000\u0000\u0000M;\u0001\u0000"+
		"\u0000\u0000MD\u0001\u0000\u0000\u0000N\t\u0001\u0000\u0000\u0000OP\u0005"+
		"\u0006\u0000\u0000PQ\u0003\u001c\u000e\u0000Q\u000b\u0001\u0000\u0000"+
		"\u0000RS\u0005\u0007\u0000\u0000ST\u0003\u001c\u000e\u0000T\r\u0001\u0000"+
		"\u0000\u0000UV\u0005\b\u0000\u0000VW\u0003\u001c\u000e\u0000W\u000f\u0001"+
		"\u0000\u0000\u0000XY\u0005\t\u0000\u0000YZ\u0007\u0001\u0000\u0000Z\u0011"+
		"\u0001\u0000\u0000\u0000[]\u0003\u0014\n\u0000\\[\u0001\u0000\u0000\u0000"+
		"\\]\u0001\u0000\u0000\u0000]_\u0001\u0000\u0000\u0000^`\u0003\u0018\f"+
		"\u0000_^\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`b\u0001\u0000"+
		"\u0000\u0000ac\u0003\u001a\r\u0000ba\u0001\u0000\u0000\u0000bc\u0001\u0000"+
		"\u0000\u0000c\u0013\u0001\u0000\u0000\u0000di\u0003\u0016\u000b\u0000"+
		"ef\u0005\n\u0000\u0000fh\u0003\u0016\u000b\u0000ge\u0001\u0000\u0000\u0000"+
		"hk\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000ij\u0001\u0000\u0000"+
		"\u0000j\u0015\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000ln\u0003"+
		"\u001e\u000f\u0000mo\u0003\u000e\u0007\u0000nm\u0001\u0000\u0000\u0000"+
		"no\u0001\u0000\u0000\u0000o\u0017\u0001\u0000\u0000\u0000pq\u0005\u0002"+
		"\u0000\u0000qr\u0003\u0010\b\u0000rs\u0005\u0004\u0000\u0000s\u0019\u0001"+
		"\u0000\u0000\u0000tu\u0005\u000b\u0000\u0000uz\u0003\u001c\u000e\u0000"+
		"vw\u0005\f\u0000\u0000wy\u0003\u001c\u000e\u0000xv\u0001\u0000\u0000\u0000"+
		"y|\u0001\u0000\u0000\u0000zx\u0001\u0000\u0000\u0000z{\u0001\u0000\u0000"+
		"\u0000{\u001b\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000\u0000}\u0082"+
		"\u0003\u001e\u000f\u0000~\u007f\u0005\r\u0000\u0000\u007f\u0081\u0003"+
		"\u001e\u000f\u0000\u0080~\u0001\u0000\u0000\u0000\u0081\u0084\u0001\u0000"+
		"\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000"+
		"\u0000\u0000\u0083\u001d\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000"+
		"\u0000\u0000\u0085\u0087\b\u0002\u0000\u0000\u0086\u0085\u0001\u0000\u0000"+
		"\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000\u0000"+
		"\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u001f\u0001\u0000\u0000"+
		"\u0000\u0013!$\'2;>ADHKM\\_binz\u0082\u0088";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}