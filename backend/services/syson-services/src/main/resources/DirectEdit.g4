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
	name? multiplicityExpression? featureExpressions EOF
;

multiplicityExpression :
	'[' (lowerBound=multiplicityExpressionMember '..') ? upperBound=multiplicityExpressionMember ']'
;

multiplicityExpressionMember :
	Integer | '*'
;

featureExpressions :
	(subsettingExpression|redefinitionExpression)? (typingExpression)? (valueExpression)?
	| (typingExpression)? (subsettingExpression|redefinitionExpression)? (valueExpression)?
	| (transitionExpression)?
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
	'=' (Real | Boolean | Integer | DoubleQuotedString)
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
	~(':' | ':>' | '::>' | ':>>' | '=' | '[' )+
;


// Reserved Keywords

ABOUT : 'about';
ABSTRACT : 'abstract';
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
DIFFERENCES : 'differences';
DISJOINING : 'disjoining';
DISJOINT : 'disjoint';
DOC : 'doc';
ELSE : 'else';
END : 'end';
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
INPUT : 'inout';
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
NOT : 'not';
NULL : 'null';
OF : 'of';
OR : 'or';
ORDERED : 'ordered';
OUT : 'out';
PACKAGE : 'package';
PORTION : 'portion';
PREDICATE : 'predicate';
PRIAVTE : 'private';
PROTECTED : 'protected';
PUBLIC : 'public';
READONLY : 'readonly';
REDEFINES : 'redefines';
REDEFINITION : 'redefinition';
RFERENCES : 'references';
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
XOR: 'xor';

// This rule is required to make sure the parser rule "name" can match any input (since it is defined as a negation we 
// need a dedicated rule to match anything).
// This rule is defined after all the other lexer rules: it should be matched if no other rule can be matched, and should
// never take priority over other rules that are more precise.
ANY:
	.
;