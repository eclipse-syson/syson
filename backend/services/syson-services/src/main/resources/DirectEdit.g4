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
grammar DirectEdit;

@header {
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
}

expression :
	prefixExpression? referenceExpression? name? multiplicityExpression? multiplicityPropExpression? featureExpressions EOF
;

prefixExpression : 
	directionPrefixExpression? (abstractPrefixExpression | variationPrefixExpression)? readonlyPrefixExpression? derivedPrefixExpression? endPrefixExpression?
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

readonlyPrefixExpression : 
	READONLY_PREFIX
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
	'[' (lowerBound=multiplicityExpressionMember '..') ? upperBound=multiplicityExpressionMember ']'
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
	Integer | '*'
;

featureExpressions :
	(subsettingExpression|redefinitionExpression)? (typingExpression)? (valueExpression)?
	| (typingExpression)? (subsettingExpression|redefinitionExpression)? (valueExpression)?
;

subsettingExpression :
	':>' qualifiedName
;

redefinitionExpression :
	':>>' qualifiedName
;

typingExpression :
	':' qualifiedName
;

valueExpression :
	'=' literalExpression ('[' measurementExpression ']')?
;

literalExpression : 
	Real | Boolean | Integer | DoubleQuotedString
;

measurementExpression : 
	~(']')+
;

// This rule is used as a top-level rule to parse constraint expressions.
constraintExpression : 
	operand operatorExpression operand
;

operand :
	(literalExpression ('[' measurementExpression ']')? | featureChainExpression)
;

featureChainExpression:
	featureReference ('.' featureChainExpression)?
;

featureReference :
	~('<=' | '>=' | '<' | '>' | '==' | '.')+
;

operatorExpression : 
	'<=' | '>=' | '<' | '>' | '=='
;
	
transitionExpression :
	(triggerExpression)? (guardExpression)? (effectExpression)?
;

triggerExpression :
	triggerExpressionName ('|' triggerExpressionName)*
;

triggerExpressionName :
	name (typingExpression)?
;

guardExpression :
	'[' valueExpression ']'
;

effectExpression :
	'/' qualifiedName (',' qualifiedName)*
;

WS :
	[ \t\r\n\u000C]+ -> skip
;

Boolean :
	TRUE|FALSE
;

Integer :
	[0-9]+
;

Real :
	[0-9]+'.'[0-9]+
;

DoubleQuotedString :
	'"' (~[\r\n"] | '""')* '"'
;

qualifiedName :
	name ('::' name)*
;

name :
	// We can't use ANY+ or .+ here because it conflicts with reserved keywords, which will be matched over ANY since 
	// they are longer. Using .+ is also too greedy, and will match ':' ':>' etc, making the parser unable to properly 
	// handle the input.
	~(':' | ':>' | '::>' | ':>>' | '=' | '[' | ORDERED_SUFFIX | NONUNIQUE_SUFFIX )+
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
DATATYPE : 'datatype';
DEFAULT : 'default';
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
PRIAVTE : 'private';
PROTECTED : 'protected';
PUBLIC : 'public';
READONLY : 'readonly';
READONLY_PREFIX : READONLY WS;
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
STRCUT : 'struct';
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
VARIATION : 'variation';
VARIATION_PREFIX : VARIATION WS;
XOR: 'xor';

// This rule is required to make sure the parser rule "name" can match any input (since it is defined as a negation we 
// need a dedicated rule to match anything).
// This rule is defined after all the other lexer rules: it should be matched if no other rule can be matched, and should
// never take priority over other rules that are more precise.
ANY:
	.
;