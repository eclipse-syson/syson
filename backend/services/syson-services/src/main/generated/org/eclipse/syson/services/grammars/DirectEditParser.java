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
		VARIANT=133, VARIANT_PREFIX=134, XOR=135, ANY=136;
	public static final int
		RULE_nodeExpression = 0, RULE_listItemExpression = 1, RULE_prefixNodeExpression = 2, 
		RULE_prefixListItemExpression = 3, RULE_directionPrefixExpression = 4, 
		RULE_abstractPrefixExpression = 5, RULE_variationPrefixExpression = 6, 
		RULE_variantPrefixExpression = 7, RULE_readonlyPrefixExpression = 8, RULE_derivedPrefixExpression = 9, 
		RULE_endPrefixExpression = 10, RULE_referenceExpression = 11, RULE_multiplicityExpression = 12, 
		RULE_multiplicityPropExpression = 13, RULE_orderedMultiplicityExpression = 14, 
		RULE_nonuniqueMultiplicityExpression = 15, RULE_multiplicityExpressionMember = 16, 
		RULE_featureExpressions = 17, RULE_subsettingExpression = 18, RULE_redefinitionExpression = 19, 
		RULE_typingExpression = 20, RULE_valueExpression = 21, RULE_literalExpression = 22, 
		RULE_measurementExpression = 23, RULE_constraintExpression = 24, RULE_operand = 25, 
		RULE_featureChainExpression = 26, RULE_featureReference = 27, RULE_operatorExpression = 28, 
		RULE_transitionExpression = 29, RULE_triggerExpression = 30, RULE_triggerExpressionName = 31, 
		RULE_guardExpression = 32, RULE_effectExpression = 33, RULE_qualifiedName = 34, 
		RULE_name = 35;
	private static String[] makeRuleNames() {
		return new String[] {
			"nodeExpression", "listItemExpression", "prefixNodeExpression", "prefixListItemExpression", 
			"directionPrefixExpression", "abstractPrefixExpression", "variationPrefixExpression", 
			"variantPrefixExpression", "readonlyPrefixExpression", "derivedPrefixExpression", 
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
			"'unions'", "'variation'", null, "'variant'", null, "'xor'"
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
			"TYPED", "TYPING", "UNIONS", "VARIATION", "VARIATION_PREFIX", "VARIANT", 
			"VARIANT_PREFIX", "XOR", "ANY"
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
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
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
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << WS) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY) | (1L << DERIVED) | (1L << DERIVED_PREFIX) | (1L << DIFFERENCES) | (1L << DISJOINING) | (1L << DISJOINT) | (1L << DOC) | (1L << ELSE) | (1L << END) | (1L << END_PREFIX) | (1L << EXPR) | (1L << FALSE) | (1L << FEATURE) | (1L << FEATURED) | (1L << FEATURING))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)) | (1L << (RETURN - 64)) | (1L << (SPECIALIZTION - 64)) | (1L << (SPECIALIZES - 64)) | (1L << (STEP - 64)) | (1L << (STRCUT - 64)) | (1L << (SUBCLASSIFIER - 64)) | (1L << (SUBSET - 64)) | (1L << (SUBSETS - 64)) | (1L << (SUBTYPE - 64)) | (1L << (SUCCESSION - 64)) | (1L << (THEN - 64)) | (1L << (TO - 64)) | (1L << (TRUE - 64)) | (1L << (TYPE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (VARIANT - 128)) | (1L << (VARIANT_PREFIX - 128)) | (1L << (XOR - 128)) | (1L << (ANY - 128)))) != 0)) {
				{
				setState(76);
				name();
				}
			}

			setState(79);
			featureExpressions();
			setState(80);
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
			setState(82);
			prefixListItemExpression();
			setState(84);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(83);
				referenceExpression();
				}
				break;
			}
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << WS) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY) | (1L << DERIVED) | (1L << DERIVED_PREFIX) | (1L << DIFFERENCES) | (1L << DISJOINING) | (1L << DISJOINT) | (1L << DOC) | (1L << ELSE) | (1L << END) | (1L << END_PREFIX) | (1L << EXPR) | (1L << FALSE) | (1L << FEATURE) | (1L << FEATURED) | (1L << FEATURING))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)) | (1L << (RETURN - 64)) | (1L << (SPECIALIZTION - 64)) | (1L << (SPECIALIZES - 64)) | (1L << (STEP - 64)) | (1L << (STRCUT - 64)) | (1L << (SUBCLASSIFIER - 64)) | (1L << (SUBSET - 64)) | (1L << (SUBSETS - 64)) | (1L << (SUBTYPE - 64)) | (1L << (SUCCESSION - 64)) | (1L << (THEN - 64)) | (1L << (TO - 64)) | (1L << (TRUE - 64)) | (1L << (TYPE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (VARIANT - 128)) | (1L << (VARIANT_PREFIX - 128)) | (1L << (XOR - 128)) | (1L << (ANY - 128)))) != 0)) {
				{
				setState(86);
				name();
				}
			}

			setState(90);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(89);
				multiplicityExpression();
				}
			}

			setState(93);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(92);
				multiplicityPropExpression();
				}
				break;
			}
			setState(95);
			featureExpressions();
			setState(96);
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
			setState(103);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(101);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case ABSTRACT_PREFIX:
						{
						setState(98);
						abstractPrefixExpression();
						}
						break;
					case VARIATION_PREFIX:
						{
						setState(99);
						variationPrefixExpression();
						}
						break;
					case VARIANT_PREFIX:
						{
						setState(100);
						variantPrefixExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(105);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
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
			setState(114);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(112);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case IN_PREFIX:
					case INOUT_PREFIX:
					case OUT_PREFIX:
						{
						setState(106);
						directionPrefixExpression();
						}
						break;
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
					case READONLY_PREFIX:
						{
						setState(109);
						readonlyPrefixExpression();
						}
						break;
					case DERIVED_PREFIX:
						{
						setState(110);
						derivedPrefixExpression();
						}
						break;
					case END_PREFIX:
						{
						setState(111);
						endPrefixExpression();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(116);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
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
			setState(117);
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
		enterRule(_localctx, 10, RULE_abstractPrefixExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
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
			setState(121);
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
			setState(123);
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
			setState(125);
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
			setState(127);
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
			setState(129);
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
			setState(131);
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
		enterRule(_localctx, 24, RULE_multiplicityExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			match(T__0);
			setState(137);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(134);
				((MultiplicityExpressionContext)_localctx).lowerBound = multiplicityExpressionMember();
				setState(135);
				match(T__1);
				}
				break;
			}
			setState(139);
			((MultiplicityExpressionContext)_localctx).upperBound = multiplicityExpressionMember();
			setState(140);
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
		enterRule(_localctx, 26, RULE_multiplicityPropExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDERED_SUFFIX) {
				{
				setState(142);
				orderedMultiplicityExpression();
				}
			}

			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NONUNIQUE_SUFFIX) {
				{
				setState(145);
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
			setState(148);
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
			setState(150);
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
		enterRule(_localctx, 32, RULE_multiplicityExpressionMember);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
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
		enterRule(_localctx, 34, RULE_featureExpressions);
		int _la;
		try {
			setState(174);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(156);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(154);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(155);
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
				setState(159);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(158);
					typingExpression();
					}
				}

				setState(162);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(161);
					valueExpression();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(165);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__6) {
					{
					setState(164);
					typingExpression();
					}
				}

				setState(169);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__4:
					{
					setState(167);
					subsettingExpression();
					}
					break;
				case T__5:
					{
					setState(168);
					redefinitionExpression();
					}
					break;
				case EOF:
				case T__7:
					break;
				default:
					break;
				}
				setState(172);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(171);
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
		enterRule(_localctx, 36, RULE_subsettingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			match(T__4);
			setState(177);
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
		enterRule(_localctx, 38, RULE_redefinitionExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(T__5);
			setState(180);
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
		enterRule(_localctx, 40, RULE_typingExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			match(T__6);
			setState(183);
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
		enterRule(_localctx, 42, RULE_valueExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			match(T__7);
			setState(186);
			literalExpression();
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(187);
				match(T__0);
				setState(188);
				measurementExpression();
				setState(189);
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
		enterRule(_localctx, 44, RULE_literalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
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
		enterRule(_localctx, 46, RULE_measurementExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(195);
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
				setState(198); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << WS) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY) | (1L << DERIVED) | (1L << DERIVED_PREFIX) | (1L << DIFFERENCES) | (1L << DISJOINING) | (1L << DISJOINT) | (1L << DOC) | (1L << ELSE) | (1L << END) | (1L << END_PREFIX) | (1L << EXPR) | (1L << FALSE) | (1L << FEATURE) | (1L << FEATURED) | (1L << FEATURING))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NONUNIQUE_SUFFIX - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (ORDERED_SUFFIX - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)) | (1L << (RETURN - 64)) | (1L << (SPECIALIZTION - 64)) | (1L << (SPECIALIZES - 64)) | (1L << (STEP - 64)) | (1L << (STRCUT - 64)) | (1L << (SUBCLASSIFIER - 64)) | (1L << (SUBSET - 64)) | (1L << (SUBSETS - 64)) | (1L << (SUBTYPE - 64)) | (1L << (SUCCESSION - 64)) | (1L << (THEN - 64)) | (1L << (TO - 64)) | (1L << (TRUE - 64)) | (1L << (TYPE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (VARIANT - 128)) | (1L << (VARIANT_PREFIX - 128)) | (1L << (XOR - 128)) | (1L << (ANY - 128)))) != 0) );
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
		enterRule(_localctx, 48, RULE_constraintExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(200);
			operand();
			setState(201);
			operatorExpression();
			setState(202);
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
		enterRule(_localctx, 50, RULE_operand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(204);
				literalExpression();
				setState(209);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(205);
					match(T__0);
					setState(206);
					measurementExpression();
					setState(207);
					match(T__2);
					}
				}

				}
				break;
			case 2:
				{
				setState(211);
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
		enterRule(_localctx, 52, RULE_featureChainExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			featureReference();
			setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(215);
				match(T__8);
				setState(216);
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
		enterRule(_localctx, 54, RULE_featureReference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(219);
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
				setState(222); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << WS) | (1L << Boolean) | (1L << Integer) | (1L << Real) | (1L << DoubleQuotedString) | (1L << ABOUT) | (1L << ABSTRACT) | (1L << ABSTRACT_PREFIX) | (1L << ALIAS) | (1L << ALL) | (1L << AND) | (1L << AS) | (1L << ASSIGN) | (1L << ASSOC) | (1L << BEAHVIOR) | (1L << BINDING) | (1L << BOOL) | (1L << BY) | (1L << CHAINS) | (1L << CLASS) | (1L << CLASSIFIER) | (1L << COMMENT) | (1L << COMPOSITE) | (1L << CONJUGATE) | (1L << CONJUGATES) | (1L << CONJUGATION) | (1L << CONNECTOR) | (1L << DATATYPE) | (1L << DEFAULT) | (1L << DEPENDENCY) | (1L << DERIVED) | (1L << DERIVED_PREFIX) | (1L << DIFFERENCES) | (1L << DISJOINING) | (1L << DISJOINT) | (1L << DOC) | (1L << ELSE) | (1L << END) | (1L << END_PREFIX) | (1L << EXPR) | (1L << FALSE) | (1L << FEATURE) | (1L << FEATURED) | (1L << FEATURING))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (FILTER - 64)) | (1L << (FIRST - 64)) | (1L << (FLOW - 64)) | (1L << (FOR - 64)) | (1L << (FROM - 64)) | (1L << (FUNCTION - 64)) | (1L << (HASTYPE - 64)) | (1L << (IF - 64)) | (1L << (INTERSECTS - 64)) | (1L << (IMPLIES - 64)) | (1L << (IMPORT - 64)) | (1L << (IN - 64)) | (1L << (IN_PREFIX - 64)) | (1L << (INOUT - 64)) | (1L << (INOUT_PREFIX - 64)) | (1L << (INTERACTION - 64)) | (1L << (INV - 64)) | (1L << (INVERSE - 64)) | (1L << (INVERTING - 64)) | (1L << (ISTYPE - 64)) | (1L << (LANGUAGE - 64)) | (1L << (MEMBER - 64)) | (1L << (METACLASS - 64)) | (1L << (METADATA - 64)) | (1L << (MULTIPLICITY - 64)) | (1L << (NAMESPACE - 64)) | (1L << (NONUNIQUE - 64)) | (1L << (NONUNIQUE_SUFFIX - 64)) | (1L << (NOT - 64)) | (1L << (NULL - 64)) | (1L << (OF - 64)) | (1L << (OR - 64)) | (1L << (ORDERED - 64)) | (1L << (ORDERED_SUFFIX - 64)) | (1L << (OUT - 64)) | (1L << (OUT_PREFIX - 64)) | (1L << (PACKAGE - 64)) | (1L << (PORTION - 64)) | (1L << (PREDICATE - 64)) | (1L << (PRIAVTE - 64)) | (1L << (PROTECTED - 64)) | (1L << (PUBLIC - 64)) | (1L << (READONLY - 64)) | (1L << (READONLY_PREFIX - 64)) | (1L << (REDEFINES - 64)) | (1L << (REDEFINITION - 64)) | (1L << (REF - 64)) | (1L << (REF_PREFIX - 64)) | (1L << (REFERENCES - 64)) | (1L << (REP - 64)) | (1L << (RETURN - 64)) | (1L << (SPECIALIZTION - 64)) | (1L << (SPECIALIZES - 64)) | (1L << (STEP - 64)) | (1L << (STRCUT - 64)) | (1L << (SUBCLASSIFIER - 64)) | (1L << (SUBSET - 64)) | (1L << (SUBSETS - 64)) | (1L << (SUBTYPE - 64)) | (1L << (SUCCESSION - 64)) | (1L << (THEN - 64)) | (1L << (TO - 64)) | (1L << (TRUE - 64)) | (1L << (TYPE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (TYPED - 128)) | (1L << (TYPING - 128)) | (1L << (UNIONS - 128)) | (1L << (VARIATION - 128)) | (1L << (VARIATION_PREFIX - 128)) | (1L << (VARIANT - 128)) | (1L << (VARIANT_PREFIX - 128)) | (1L << (XOR - 128)) | (1L << (ANY - 128)))) != 0) );
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
		enterRule(_localctx, 56, RULE_operatorExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
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
		enterRule(_localctx, 58, RULE_transitionExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(226);
				triggerExpression();
				}
				break;
			}
			setState(230);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(229);
				guardExpression();
				}
			}

			setState(233);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(232);
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
		enterRule(_localctx, 60, RULE_triggerExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(235);
			triggerExpressionName();
			setState(240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(236);
				match(T__14);
				setState(237);
				triggerExpressionName();
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
		enterRule(_localctx, 62, RULE_triggerExpressionName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			name();
			setState(245);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(244);
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
		enterRule(_localctx, 64, RULE_guardExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			match(T__0);
			setState(248);
			valueExpression();
			setState(249);
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
		enterRule(_localctx, 66, RULE_effectExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			match(T__15);
			setState(252);
			qualifiedName();
			setState(257);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__16) {
				{
				{
				setState(253);
				match(T__16);
				setState(254);
				qualifiedName();
				}
				}
				setState(259);
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
		enterRule(_localctx, 68, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			name();
			setState(265);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17) {
				{
				{
				setState(261);
				match(T__17);
				setState(262);
				name();
				}
				}
				setState(267);
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
		enterRule(_localctx, 70, RULE_name);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(269); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(268);
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
				setState(271); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
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
		"\u0004\u0001\u0088\u0112\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"\u0003\u0000N\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0003\u0001U\b\u0001\u0001\u0001\u0003\u0001X\b\u0001\u0001"+
		"\u0001\u0003\u0001[\b\u0001\u0001\u0001\u0003\u0001^\b\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002"+
		"f\b\u0002\n\u0002\f\u0002i\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003q\b\u0003\n\u0003\f\u0003"+
		"t\t\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0003"+
		"\f\u008a\b\f\u0001\f\u0001\f\u0001\f\u0001\r\u0003\r\u0090\b\r\u0001\r"+
		"\u0003\r\u0093\b\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0003\u0011\u009d\b\u0011\u0001"+
		"\u0011\u0003\u0011\u00a0\b\u0011\u0001\u0011\u0003\u0011\u00a3\b\u0011"+
		"\u0001\u0011\u0003\u0011\u00a6\b\u0011\u0001\u0011\u0001\u0011\u0003\u0011"+
		"\u00aa\b\u0011\u0001\u0011\u0003\u0011\u00ad\b\u0011\u0003\u0011\u00af"+
		"\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u00c0\b\u0015\u0001"+
		"\u0016\u0001\u0016\u0001\u0017\u0004\u0017\u00c5\b\u0017\u000b\u0017\f"+
		"\u0017\u00c6\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u00d2\b\u0019"+
		"\u0001\u0019\u0003\u0019\u00d5\b\u0019\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0003\u001a\u00da\b\u001a\u0001\u001b\u0004\u001b\u00dd\b\u001b\u000b"+
		"\u001b\f\u001b\u00de\u0001\u001c\u0001\u001c\u0001\u001d\u0003\u001d\u00e4"+
		"\b\u001d\u0001\u001d\u0003\u001d\u00e7\b\u001d\u0001\u001d\u0003\u001d"+
		"\u00ea\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e\u00ef\b"+
		"\u001e\n\u001e\f\u001e\u00f2\t\u001e\u0001\u001f\u0001\u001f\u0003\u001f"+
		"\u00f6\b\u001f\u0001 \u0001 \u0001 \u0001 \u0001!\u0001!\u0001!\u0001"+
		"!\u0005!\u0100\b!\n!\f!\u0103\t!\u0001\"\u0001\"\u0001\"\u0005\"\u0108"+
		"\b\"\n\"\f\"\u010b\t\"\u0001#\u0004#\u010e\b#\u000b#\f#\u010f\u0001#\u0000"+
		"\u0000$\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.02468:<>@BDF\u0000\u0007\u0003\u0000LLNNcc"+
		"\u0002\u0000\u0004\u0004\u0016\u0016\u0001\u0000\u0015\u0018\u0001\u0000"+
		"\u0003\u0003\u0001\u0000\t\u000e\u0001\u0000\n\u000e\u0005\u0000\u0001"+
		"\u0001\u0005\b\u0013\u0013[[aa\u0116\u0000H\u0001\u0000\u0000\u0000\u0002"+
		"R\u0001\u0000\u0000\u0000\u0004g\u0001\u0000\u0000\u0000\u0006r\u0001"+
		"\u0000\u0000\u0000\bu\u0001\u0000\u0000\u0000\nw\u0001\u0000\u0000\u0000"+
		"\fy\u0001\u0000\u0000\u0000\u000e{\u0001\u0000\u0000\u0000\u0010}\u0001"+
		"\u0000\u0000\u0000\u0012\u007f\u0001\u0000\u0000\u0000\u0014\u0081\u0001"+
		"\u0000\u0000\u0000\u0016\u0083\u0001\u0000\u0000\u0000\u0018\u0085\u0001"+
		"\u0000\u0000\u0000\u001a\u008f\u0001\u0000\u0000\u0000\u001c\u0094\u0001"+
		"\u0000\u0000\u0000\u001e\u0096\u0001\u0000\u0000\u0000 \u0098\u0001\u0000"+
		"\u0000\u0000\"\u00ae\u0001\u0000\u0000\u0000$\u00b0\u0001\u0000\u0000"+
		"\u0000&\u00b3\u0001\u0000\u0000\u0000(\u00b6\u0001\u0000\u0000\u0000*"+
		"\u00b9\u0001\u0000\u0000\u0000,\u00c1\u0001\u0000\u0000\u0000.\u00c4\u0001"+
		"\u0000\u0000\u00000\u00c8\u0001\u0000\u0000\u00002\u00d4\u0001\u0000\u0000"+
		"\u00004\u00d6\u0001\u0000\u0000\u00006\u00dc\u0001\u0000\u0000\u00008"+
		"\u00e0\u0001\u0000\u0000\u0000:\u00e3\u0001\u0000\u0000\u0000<\u00eb\u0001"+
		"\u0000\u0000\u0000>\u00f3\u0001\u0000\u0000\u0000@\u00f7\u0001\u0000\u0000"+
		"\u0000B\u00fb\u0001\u0000\u0000\u0000D\u0104\u0001\u0000\u0000\u0000F"+
		"\u010d\u0001\u0000\u0000\u0000HJ\u0003\u0004\u0002\u0000IK\u0003\u0016"+
		"\u000b\u0000JI\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KM\u0001"+
		"\u0000\u0000\u0000LN\u0003F#\u0000ML\u0001\u0000\u0000\u0000MN\u0001\u0000"+
		"\u0000\u0000NO\u0001\u0000\u0000\u0000OP\u0003\"\u0011\u0000PQ\u0005\u0000"+
		"\u0000\u0001Q\u0001\u0001\u0000\u0000\u0000RT\u0003\u0006\u0003\u0000"+
		"SU\u0003\u0016\u000b\u0000TS\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000"+
		"\u0000UW\u0001\u0000\u0000\u0000VX\u0003F#\u0000WV\u0001\u0000\u0000\u0000"+
		"WX\u0001\u0000\u0000\u0000XZ\u0001\u0000\u0000\u0000Y[\u0003\u0018\f\u0000"+
		"ZY\u0001\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000[]\u0001\u0000\u0000"+
		"\u0000\\^\u0003\u001a\r\u0000]\\\u0001\u0000\u0000\u0000]^\u0001\u0000"+
		"\u0000\u0000^_\u0001\u0000\u0000\u0000_`\u0003\"\u0011\u0000`a\u0005\u0000"+
		"\u0000\u0001a\u0003\u0001\u0000\u0000\u0000bf\u0003\n\u0005\u0000cf\u0003"+
		"\f\u0006\u0000df\u0003\u000e\u0007\u0000eb\u0001\u0000\u0000\u0000ec\u0001"+
		"\u0000\u0000\u0000ed\u0001\u0000\u0000\u0000fi\u0001\u0000\u0000\u0000"+
		"ge\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000h\u0005\u0001\u0000"+
		"\u0000\u0000ig\u0001\u0000\u0000\u0000jq\u0003\b\u0004\u0000kq\u0003\n"+
		"\u0005\u0000lq\u0003\f\u0006\u0000mq\u0003\u0010\b\u0000nq\u0003\u0012"+
		"\t\u0000oq\u0003\u0014\n\u0000pj\u0001\u0000\u0000\u0000pk\u0001\u0000"+
		"\u0000\u0000pl\u0001\u0000\u0000\u0000pm\u0001\u0000\u0000\u0000pn\u0001"+
		"\u0000\u0000\u0000po\u0001\u0000\u0000\u0000qt\u0001\u0000\u0000\u0000"+
		"rp\u0001\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000s\u0007\u0001\u0000"+
		"\u0000\u0000tr\u0001\u0000\u0000\u0000uv\u0007\u0000\u0000\u0000v\t\u0001"+
		"\u0000\u0000\u0000wx\u0005\u001b\u0000\u0000x\u000b\u0001\u0000\u0000"+
		"\u0000yz\u0005\u0084\u0000\u0000z\r\u0001\u0000\u0000\u0000{|\u0005\u0086"+
		"\u0000\u0000|\u000f\u0001\u0000\u0000\u0000}~\u0005k\u0000\u0000~\u0011"+
		"\u0001\u0000\u0000\u0000\u007f\u0080\u00053\u0000\u0000\u0080\u0013\u0001"+
		"\u0000\u0000\u0000\u0081\u0082\u0005:\u0000\u0000\u0082\u0015\u0001\u0000"+
		"\u0000\u0000\u0083\u0084\u0005o\u0000\u0000\u0084\u0017\u0001\u0000\u0000"+
		"\u0000\u0085\u0089\u0005\u0001\u0000\u0000\u0086\u0087\u0003 \u0010\u0000"+
		"\u0087\u0088\u0005\u0002\u0000\u0000\u0088\u008a\u0001\u0000\u0000\u0000"+
		"\u0089\u0086\u0001\u0000\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000"+
		"\u008a\u008b\u0001\u0000\u0000\u0000\u008b\u008c\u0003 \u0010\u0000\u008c"+
		"\u008d\u0005\u0003\u0000\u0000\u008d\u0019\u0001\u0000\u0000\u0000\u008e"+
		"\u0090\u0003\u001c\u000e\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u008f"+
		"\u0090\u0001\u0000\u0000\u0000\u0090\u0092\u0001\u0000\u0000\u0000\u0091"+
		"\u0093\u0003\u001e\u000f\u0000\u0092\u0091\u0001\u0000\u0000\u0000\u0092"+
		"\u0093\u0001\u0000\u0000\u0000\u0093\u001b\u0001\u0000\u0000\u0000\u0094"+
		"\u0095\u0005a\u0000\u0000\u0095\u001d\u0001\u0000\u0000\u0000\u0096\u0097"+
		"\u0005[\u0000\u0000\u0097\u001f\u0001\u0000\u0000\u0000\u0098\u0099\u0007"+
		"\u0001\u0000\u0000\u0099!\u0001\u0000\u0000\u0000\u009a\u009d\u0003$\u0012"+
		"\u0000\u009b\u009d\u0003&\u0013\u0000\u009c\u009a\u0001\u0000\u0000\u0000"+
		"\u009c\u009b\u0001\u0000\u0000\u0000\u009c\u009d\u0001\u0000\u0000\u0000"+
		"\u009d\u009f\u0001\u0000\u0000\u0000\u009e\u00a0\u0003(\u0014\u0000\u009f"+
		"\u009e\u0001\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0"+
		"\u00a2\u0001\u0000\u0000\u0000\u00a1\u00a3\u0003*\u0015\u0000\u00a2\u00a1"+
		"\u0001\u0000\u0000\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3\u00af"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a6\u0003(\u0014\u0000\u00a5\u00a4\u0001"+
		"\u0000\u0000\u0000\u00a5\u00a6\u0001\u0000\u0000\u0000\u00a6\u00a9\u0001"+
		"\u0000\u0000\u0000\u00a7\u00aa\u0003$\u0012\u0000\u00a8\u00aa\u0003&\u0013"+
		"\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00a9\u00a8\u0001\u0000\u0000"+
		"\u0000\u00a9\u00aa\u0001\u0000\u0000\u0000\u00aa\u00ac\u0001\u0000\u0000"+
		"\u0000\u00ab\u00ad\u0003*\u0015\u0000\u00ac\u00ab\u0001\u0000\u0000\u0000"+
		"\u00ac\u00ad\u0001\u0000\u0000\u0000\u00ad\u00af\u0001\u0000\u0000\u0000"+
		"\u00ae\u009c\u0001\u0000\u0000\u0000\u00ae\u00a5\u0001\u0000\u0000\u0000"+
		"\u00af#\u0001\u0000\u0000\u0000\u00b0\u00b1\u0005\u0005\u0000\u0000\u00b1"+
		"\u00b2\u0003D\"\u0000\u00b2%\u0001\u0000\u0000\u0000\u00b3\u00b4\u0005"+
		"\u0006\u0000\u0000\u00b4\u00b5\u0003D\"\u0000\u00b5\'\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b7\u0005\u0007\u0000\u0000\u00b7\u00b8\u0003D\"\u0000"+
		"\u00b8)\u0001\u0000\u0000\u0000\u00b9\u00ba\u0005\b\u0000\u0000\u00ba"+
		"\u00bf\u0003,\u0016\u0000\u00bb\u00bc\u0005\u0001\u0000\u0000\u00bc\u00bd"+
		"\u0003.\u0017\u0000\u00bd\u00be\u0005\u0003\u0000\u0000\u00be\u00c0\u0001"+
		"\u0000\u0000\u0000\u00bf\u00bb\u0001\u0000\u0000\u0000\u00bf\u00c0\u0001"+
		"\u0000\u0000\u0000\u00c0+\u0001\u0000\u0000\u0000\u00c1\u00c2\u0007\u0002"+
		"\u0000\u0000\u00c2-\u0001\u0000\u0000\u0000\u00c3\u00c5\b\u0003\u0000"+
		"\u0000\u00c4\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c6\u0001\u0000\u0000"+
		"\u0000\u00c6\u00c4\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000"+
		"\u0000\u00c7/\u0001\u0000\u0000\u0000\u00c8\u00c9\u00032\u0019\u0000\u00c9"+
		"\u00ca\u00038\u001c\u0000\u00ca\u00cb\u00032\u0019\u0000\u00cb1\u0001"+
		"\u0000\u0000\u0000\u00cc\u00d1\u0003,\u0016\u0000\u00cd\u00ce\u0005\u0001"+
		"\u0000\u0000\u00ce\u00cf\u0003.\u0017\u0000\u00cf\u00d0\u0005\u0003\u0000"+
		"\u0000\u00d0\u00d2\u0001\u0000\u0000\u0000\u00d1\u00cd\u0001\u0000\u0000"+
		"\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2\u00d5\u0001\u0000\u0000"+
		"\u0000\u00d3\u00d5\u00034\u001a\u0000\u00d4\u00cc\u0001\u0000\u0000\u0000"+
		"\u00d4\u00d3\u0001\u0000\u0000\u0000\u00d53\u0001\u0000\u0000\u0000\u00d6"+
		"\u00d9\u00036\u001b\u0000\u00d7\u00d8\u0005\t\u0000\u0000\u00d8\u00da"+
		"\u00034\u001a\u0000\u00d9\u00d7\u0001\u0000\u0000\u0000\u00d9\u00da\u0001"+
		"\u0000\u0000\u0000\u00da5\u0001\u0000\u0000\u0000\u00db\u00dd\b\u0004"+
		"\u0000\u0000\u00dc\u00db\u0001\u0000\u0000\u0000\u00dd\u00de\u0001\u0000"+
		"\u0000\u0000\u00de\u00dc\u0001\u0000\u0000\u0000\u00de\u00df\u0001\u0000"+
		"\u0000\u0000\u00df7\u0001\u0000\u0000\u0000\u00e0\u00e1\u0007\u0005\u0000"+
		"\u0000\u00e19\u0001\u0000\u0000\u0000\u00e2\u00e4\u0003<\u001e\u0000\u00e3"+
		"\u00e2\u0001\u0000\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000\u0000\u00e4"+
		"\u00e6\u0001\u0000\u0000\u0000\u00e5\u00e7\u0003@ \u0000\u00e6\u00e5\u0001"+
		"\u0000\u0000\u0000\u00e6\u00e7\u0001\u0000\u0000\u0000\u00e7\u00e9\u0001"+
		"\u0000\u0000\u0000\u00e8\u00ea\u0003B!\u0000\u00e9\u00e8\u0001\u0000\u0000"+
		"\u0000\u00e9\u00ea\u0001\u0000\u0000\u0000\u00ea;\u0001\u0000\u0000\u0000"+
		"\u00eb\u00f0\u0003>\u001f\u0000\u00ec\u00ed\u0005\u000f\u0000\u0000\u00ed"+
		"\u00ef\u0003>\u001f\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ef\u00f2"+
		"\u0001\u0000\u0000\u0000\u00f0\u00ee\u0001\u0000\u0000\u0000\u00f0\u00f1"+
		"\u0001\u0000\u0000\u0000\u00f1=\u0001\u0000\u0000\u0000\u00f2\u00f0\u0001"+
		"\u0000\u0000\u0000\u00f3\u00f5\u0003F#\u0000\u00f4\u00f6\u0003(\u0014"+
		"\u0000\u00f5\u00f4\u0001\u0000\u0000\u0000\u00f5\u00f6\u0001\u0000\u0000"+
		"\u0000\u00f6?\u0001\u0000\u0000\u0000\u00f7\u00f8\u0005\u0001\u0000\u0000"+
		"\u00f8\u00f9\u0003*\u0015\u0000\u00f9\u00fa\u0005\u0003\u0000\u0000\u00fa"+
		"A\u0001\u0000\u0000\u0000\u00fb\u00fc\u0005\u0010\u0000\u0000\u00fc\u0101"+
		"\u0003D\"\u0000\u00fd\u00fe\u0005\u0011\u0000\u0000\u00fe\u0100\u0003"+
		"D\"\u0000\u00ff\u00fd\u0001\u0000\u0000\u0000\u0100\u0103\u0001\u0000"+
		"\u0000\u0000\u0101\u00ff\u0001\u0000\u0000\u0000\u0101\u0102\u0001\u0000"+
		"\u0000\u0000\u0102C\u0001\u0000\u0000\u0000\u0103\u0101\u0001\u0000\u0000"+
		"\u0000\u0104\u0109\u0003F#\u0000\u0105\u0106\u0005\u0012\u0000\u0000\u0106"+
		"\u0108\u0003F#\u0000\u0107\u0105\u0001\u0000\u0000\u0000\u0108\u010b\u0001"+
		"\u0000\u0000\u0000\u0109\u0107\u0001\u0000\u0000\u0000\u0109\u010a\u0001"+
		"\u0000\u0000\u0000\u010aE\u0001\u0000\u0000\u0000\u010b\u0109\u0001\u0000"+
		"\u0000\u0000\u010c\u010e\b\u0006\u0000\u0000\u010d\u010c\u0001\u0000\u0000"+
		"\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u010d\u0001\u0000\u0000"+
		"\u0000\u010f\u0110\u0001\u0000\u0000\u0000\u0110G\u0001\u0000\u0000\u0000"+
		"\"JMTWZ]egpr\u0089\u008f\u0092\u009c\u009f\u00a2\u00a5\u00a9\u00ac\u00ae"+
		"\u00bf\u00c6\u00d1\u00d4\u00d9\u00de\u00e3\u00e6\u00e9\u00f0\u00f5\u0101"+
		"\u0109\u010f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}