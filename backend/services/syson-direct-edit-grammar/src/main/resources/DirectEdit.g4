/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
grammar DirectEdit;

@header {
/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
}

// This rule is used as a top-level rule to parse expressions on graphical nodes.
nodeExpression :
	prefixNodeExpression referenceExpression? shortName? name? multiplicityExpression? featureExpressions EOF
;

// This rule is used as a top-level rule to parse expressions on compartment list items.
listItemExpression :
	prefixListItemExpression referenceExpression? shortName? name? multiplicityExpression? multiplicityPropExpression? featureExpressions EOF
;

prefixNodeExpression : 
	(abstractPrefixExpression | variationPrefixExpression | variantPrefixExpression)*
;

prefixListItemExpression : 
	(directionPrefixExpression | abstractPrefixExpression | variationPrefixExpression | constantPrefixExpression | derivedPrefixExpression | endPrefixExpression)*
;

directionPrefixExpression : 
	IN_PREFIX | OUT_PREFIX | INOUT_PREFIX
;

abstractPrefixExpression : 
	ABSTRACT_PREFIX
;

variationPrefixExpression : 
	VARIATION_PREFIX
;

variantPrefixExpression : 
	VARIANT_PREFIX
;

constantPrefixExpression : 
	CONSTANT_PREFIX
;

derivedPrefixExpression : 
	DERIVED_PREFIX
;

endPrefixExpression : 
	END_PREFIX
;

referenceExpression : 
	REF_PREFIX
;

multiplicityExpression :
	LBRACKET (lowerBound=multiplicityExpressionMember DOTDOT)? upperBound=multiplicityExpressionMember RBRACKET
;

multiplicityPropExpression :
	orderedMultiplicityExpression? nonuniqueMultiplicityExpression?
;

orderedMultiplicityExpression : 
	ORDERED_SUFFIX
;

nonuniqueMultiplicityExpression : 
	NONUNIQUE_SUFFIX
;

multiplicityExpressionMember :
	Integer | STAR
;

featureExpressions :
	(subsettingExpression|redefinitionExpression)? (typingExpression)? (featureValueExpression)?
	| (typingExpression)? (subsettingExpression|redefinitionExpression)? (featureValueExpression)?
;

subsettingExpression :
	SUBSETS_OP qualifiedName
;

redefinitionExpression :
	REDEFINES_OP qualifiedName
;

typingExpression :
	COLON qualifiedName
;

valueExpression :
	EQUALS literalExpression (LBRACKET measurementExpression RBRACKET)?
;

featureValueExpression :
	(DEFAULT_SUFFIX)? (EQUALS|ASSIGN_OP) expression
;

literalExpression : 
	Real | Boolean | Integer | DoubleQuotedString
;

measurementExpression : 
	~(RBRACKET)+
;

// This rule is used as a top-level rule to parse constraint expressions.
constraintExpression : 
	expression
;

expression
    : expression op=(POWER | STAR | DIV | MOD | XOR | PLUS | MINUS | PIPE | AMP | XOR_KEYWORD | DOTDOT | EQ | NEQ | EQ_STRICT | NEQ_STRICT | LT | GT | LTE | GTE ) expression       # BinaryOperationExpr
    | primaryExpression                                   # PrimaryExpr
    ;

primaryExpression
    : featureChainExpression
    | bracketAccessExpression
    | literalExpression
    | LPAREN expression RPAREN
    ;

bracketAccessExpression
    : primaryAtom LBRACKET sequenceExpressionList RBRACKET
    ;

primaryAtom
    : featureChainExpression
    | literalExpression
    | LPAREN expression RPAREN
    ;

sequenceExpressionList
    : expression (COMMA expression)*
    ;

featureChainExpression:
	refName (DOT featureChainExpression)?
;


transitionExpression :
	(triggerExpression)? (guardExpression)? (effectExpression)?
;

triggerExpression :
	triggerExpressionName (PIPE triggerExpressionName)*
;

triggerExpressionName :
	name (typingExpression)?
;

guardExpression :
	LBRACKET valueExpression RBRACKET
;

effectExpression :
	SLASH qualifiedName (COMMA qualifiedName)*
;

qualifiedName :
	name (NAMESPACE_SEP name)*
;

shortName :
	LT name? GT
;

refName :
	REFNAME | ESPACED_NAME | qualifiedName2
	;

qualifiedName2 :
	(REFNAME | ESPACED_NAME) (NAMESPACE_SEP (REFNAME | ESPACED_NAME))+
;

name :
	// We can't use ANY+ or .+ here because it conflicts with reserved keywords, which will be matched over ANY since 
	// they are longer. Using .+ is also too greedy, and will match ':' ':>' etc, making the parser unable to properly 
	// handle the input.
	~(COLON | SUBSETS_OP | NAMESPACE_SEP | REDEFINES_OP | EQUALS | ASSIGN_OP | LBRACKET | DEFAULT_SUFFIX | ORDERED_SUFFIX | NONUNIQUE_SUFFIX )+
;

// LEXER RULES

ESPACED_NAME : '\'' (~['\r\n])* '\'' ;

// Whitespace
WS :
	[ \t\r\n\u000C]+ -> skip
;

// Operators and punctuation
LBRACKET : '[';
RBRACKET : ']';
LPAREN : '(';
RPAREN : ')';
LT : '<';
GT : '>';
COMMA : ',';
DOT : '.';
COLON : ':';
EQUALS : '=';
PLUS : '+';
MINUS : '-';
STAR : '*';
DIV : '/';
MOD : '%';
PIPE : '|';
AMP : '&';
SLASH : '/';
SINGLE_QUOTE : '\'';

// Multi-character operators
DOTDOT : '..';
NAMESPACE_SEP : '::';
SUBSETS_OP : ':>';
REDEFINES_OP : ':>>';
ASSIGN_OP : ':=';
POWER : '**';
EQ : '==';
NEQ : '!=';
EQ_STRICT : '===';
NEQ_STRICT : '!==';
LTE : '<=';
GTE : '>=';

// Literals
Boolean :
	TRUE|FALSE
;

Integer :
	('-')?[0-9]+
;

Real :
	[0-9]+'.'[0-9]+
;

DoubleQuotedString :
	'"' (~[\r\n"] | '""')* '"'
;



// Reserved Keywords
ABOUT : 'about';
ABSTRACT : 'abstract';
ABSTRACT_PREFIX : ABSTRACT WS;
ALIAS : 'alias';
ALL : 'all';
AND : 'and';
AS : 'as';
ASSIGN : 'assign';
ASSOC : 'assoc';
BEAHVIOR : 'behavior';
BINDING : 'binding';
BOOL : 'bool';
BY : 'by';
CHAINS : 'chains';
CLASS : 'class';
CLASSIFIER : 'classifier';
COMMENT : 'comment';
COMPOSITE : 'composite';
CONJUGATE : 'conjugate';
CONJUGATES : 'conjugates';
CONJUGATION : 'conjugation';
CONNECTOR : 'connector';
CONST : 'const';
CONST_PREFIX : CONST WS;
CONSTANT : 'constant';
CONSTANT_PREFIX : CONSTANT WS;
DATATYPE : 'datatype';
DEFAULT : 'default';
DEFAULT_SUFFIX : WS DEFAULT;
DEPENDENCY : 'dependency';
DERIVED : 'derived';
DERIVED_PREFIX : DERIVED WS;
DIFFERENCES : 'differences';
DISJOINING : 'disjoining';
DISJOINT : 'disjoint';
DOC : 'doc';
ELSE : 'else';
END : 'end';
END_PREFIX : END WS;
EXPR : 'expr';
FALSE : 'false';
FEATURE : 'feature';
FEATURED : 'featured';
FEATURING : 'featuring';
FILTER : 'filter';
FIRST : 'first';
FLOW : 'flow';
FOR : 'for';
FROM : 'from';
FUNCTION : 'function';
HASTYPE: 'hastype';
IF : 'if';
INTERSECTS : 'intersects';
IMPLIES : 'implies';
IMPORT : 'import';
IN : 'in';
IN_PREFIX : IN WS;
INOUT : 'inout';
INOUT_PREFIX : INOUT WS;
INTERACTION : 'interaction';
INV : 'inv';
INVERSE : 'inverse';
INVERTING : 'inverting';
ISTYPE : 'istype';
LANGUAGE : 'language';
MEMBER : 'member';
METACLASS : 'metaclass';
METADATA : 'metadata';
MULTIPLICITY : 'multiplicity';
NAMESPACE : 'namespace';
NONUNIQUE : 'nonunique';
NONUNIQUE_SUFFIX : WS NONUNIQUE;
NEW : 'new';
NOT : 'not';
NULL : 'null';
OF : 'of';
OR : 'or';
ORDERED : 'ordered';
ORDERED_SUFFIX : WS ORDERED;
OUT : 'out';
OUT_PREFIX : OUT WS;
PACKAGE : 'package';
PORTION : 'portion';
PREDICATE : 'predicate';
PRIVATE : 'private';
PROTECTED : 'protected';
PUBLIC : 'public';
REDEFINES : 'redefines';
REDEFINITION : 'redefinition';
REF : 'ref';
REF_PREFIX : REF WS;
REFERENCES : 'references';
REP : 'rep';
RETURN : 'return';
SPECIALIZTION : 'specialization';
SPECIALIZES : 'specializes';
STEP : 'step';
STRUCT : 'struct';
SUBCLASSIFIER : 'subclassifier';
SUBSET : 'subset';
SUBSETS : 'subsets';
SUBTYPE : 'subtype';
SUCCESSION : 'succession';
THEN : 'then';
TO : 'to';
TRUE : 'true';
TYPE : 'type';
TYPED : 'typed';
TYPING : 'typing';
UNIONS : 'unions';
VAR : 'var';
VARIATION : 'variation';
VARIATION_PREFIX : VARIATION WS;
VARIANT : 'variant';
VARIANT_PREFIX : VARIANT WS;
XOR_KEYWORD: 'xor';

REFNAME :
	[a-zA-Z_][a-zA-Z_0-9\-]*
;


// This rule is required to make sure the parser rule "name" can match any input (since it is defined as a negation we 
// need a dedicated rule to match anything).
// This rule is defined after all the other lexer rules: it should be matched if no other rule can be matched, and should
// never take priority over other rules that are more precise.
ANY:
	.
;