/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
grammar KerMLExpressions;

ownedExpressionMember:
	{start(featureMembership__());} (
	(ownedExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

ownedExpression:
	conditionalExpression
;

ownedExpressionReference:
	{start(featureReferenceExpression__());} (
	(ownedExpressionMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

conditionalExpression:
	nullCoalescingExpression
	| {start(operatorExpression__());} (conditionalOperator {set(operatorExpression_Operator());}) (nullCoalescingExpression {addOperand();}) '?' (ownedExpressionReference {addOperand();}) 'else' (ownedExpressionReference {addOperand();}) {end(_localctx);}
;

conditionalOperator:
	'if' {pushPreviousTokenText();}
;

nullCoalescingExpression:
	impliesExpression (
		{startOperatorExpression();} (nullCoalescingOperator {set(operatorExpression_Operator());}) (impliesExpressionReference {addOperand();}) {end(_localctx);}
	)*
;

nullCoalescingOperator:
	'??' {pushPreviousTokenText();}
;

impliesExpressionReference:
	{start(featureReferenceExpression__());} (
	(impliesExpressionMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

impliesExpressionMember:
	{start(featureMembership__());} (
	(impliesExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

impliesExpression:
	orExpression (
		{startOperatorExpression();} (impliesOperator {set(operatorExpression_Operator());}) (orExpressionReference {addOperand();}) {end(_localctx);}
	)*
;

impliesOperator:
	'implies' {pushPreviousTokenText();}
;

orExpressionReference:
	{start(featureReferenceExpression__());} (
	(orExpressionMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

orExpressionMember:
	{start(featureMembership__());} (
	(orExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

orExpression:
	xorExpression (
		{startOperatorExpression();} (
			(orOperator {set(operatorExpression_Operator());}) (xorExpression {addOperand();}) | (conditionalOrOperator {set(operatorExpression_Operator());}) (xorExpressionReference {addOperand();})
		) {end(_localctx);}
	)*
;

orOperator:
	'|' {pushPreviousTokenText();}
;

conditionalOrOperator:
	'or' {pushPreviousTokenText();}
;

xorExpressionReference:
	{start(featureReferenceExpression__());} (
	(xorExpressionMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

xorExpressionMember:
	{start(featureMembership__());} (
	(xorExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

xorExpression:
	andExpression (
		{startOperatorExpression();} (xorOperator {set(operatorExpression_Operator());}) (andExpression {addOperand();}) {end(_localctx);}
	)*
;

xorOperator:
	'xor' {pushPreviousTokenText();}
;

andExpression:
	equalityExpression (
		{startOperatorExpression();} (
			(andOperator {set(operatorExpression_Operator());}) (equalityExpression {addOperand();})
			| (conditionalAndOperator {set(operatorExpression_Operator());}) (equalityExpressionReference {addOperand();})
		) {end(_localctx);}
	)*
;

andOperator:
	'&' {pushPreviousTokenText();}
;

conditionalAndOperator:
	'and' {pushPreviousTokenText();}
;

equalityExpressionReference:
	{start(featureReferenceExpression__());} (
	(equalityExpressionMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

equalityExpressionMember:
	{start(featureMembership__());} (
	(equalityExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

equalityExpression:
	classificationExpression (
		{startOperatorExpression();} (equalityOperator {set(operatorExpression_Operator());}) (classificationExpression {addOperand();}) {end(_localctx);}
	)*
;

equalityOperator:
	'==' {pushPreviousTokenText();}
	| '!=' {pushPreviousTokenText();}
	| '===' {pushPreviousTokenText();}
	| '!==' {pushPreviousTokenText();}
;

classificationExpression:
	relationalExpression (
		{startOperatorExpression();} (classificationTestOperator {set(operatorExpression_Operator());}) (typeReferenceMember {add(element_OwnedRelationship());}) {end(_localctx);}
		| {startOperatorExpression();} (castOperator {set(operatorExpression_Operator());}) (typeResultMember {add(element_OwnedRelationship());}) {end(_localctx);}
	)?
	| {start(operatorExpression__());} (selfReferenceExpression {addOperand();}) (classificationTestOperator {set(operatorExpression_Operator());}) (typeReferenceMember {add(element_OwnedRelationship());}) {end(_localctx);}
	| {start(operatorExpression__());} (metadataReference {addOperand();}) (metaClassificationTestOperator {set(operatorExpression_Operator());}) (typeReferenceMember {add(element_OwnedRelationship());}) {end(_localctx);}
	| {start(operatorExpression__());} (selfReferenceExpression {addOperand();}) (castOperator {set(operatorExpression_Operator());}) (typeResultMember {add(element_OwnedRelationship());}) {end(_localctx);} (
		{startOperatorExpression();} (classificationTestOperator {set(operatorExpression_Operator());}) (typeReferenceMember {add(element_OwnedRelationship());}) {end(_localctx);}
	)*
	| {start(operatorExpression__());} (metadataReference {addOperand();}) (metaCastOperator {set(operatorExpression_Operator());}) (typeResultMember {add(element_OwnedRelationship());}) {end(_localctx);}
;

classificationTestOperator:
	'hastype' {pushPreviousTokenText();}
	| 'istype' {pushPreviousTokenText();}
	| '@' {pushPreviousTokenText();}
;

metaClassificationTestOperator:
	'@@' {pushPreviousTokenText();}
;

castOperator:
	'as' {pushPreviousTokenText();}
;

metaCastOperator:
	'meta' {pushPreviousTokenText();}
;

metadataReference:
	{start(metadataAccessExpression__());} (
	(elementReferenceMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

typeReferenceMember:
	{start(parameterMembership__());} (
	(typeReference {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

typeResultMember:
	{start(returnParameterMembership__());} (
	(typeReference {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

typeReference:
	{start(feature__());} (
	(referenceTyping {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

referenceTyping:
	{start(featureTyping__());} (
	(qualifiedName {setLink(featureTyping_Type(), type__());})
	) {end(_localctx);}
;

selfReferenceExpression:
	{start(featureReferenceExpression__());} (
	(selfReferenceMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

selfReferenceMember:
	{start(returnParameterMembership__());} (
	(emptyFeature {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

emptyFeature:
	{start(feature__());} (
	
	) {end(_localctx);}
;

relationalExpression:
	rangeExpression (
		{startOperatorExpression();} (relationalOperator {set(operatorExpression_Operator());}) (rangeExpression {addOperand();}) {end(_localctx);}
	)*
;

relationalOperator:
	'<' {pushPreviousTokenText();}
	| '>' {pushPreviousTokenText();}
	| '<=' {pushPreviousTokenText();}
	| '>=' {pushPreviousTokenText();}
;

rangeExpression:
	additiveExpression (
		{startOperatorExpression();} ('..' {set(operatorExpression_Operator(), "..");}) (additiveExpression {addOperand();}) {end(_localctx);}
	)?
;

additiveExpression:
	multiplicativeExpression (
		{startOperatorExpression();} (additiveOperator {set(operatorExpression_Operator());}) (multiplicativeExpression {addOperand();}) {end(_localctx);}
	)*
;

additiveOperator:
	'+' {pushPreviousTokenText();}
	| '-' {pushPreviousTokenText();}
;

multiplicativeExpression:
	exponentiationExpression (
		{startOperatorExpression();} (multiplicativeOperator {set(operatorExpression_Operator());}) (exponentiationExpression {addOperand();}) {end(_localctx);}
	)*
;

multiplicativeOperator:
	'*' {pushPreviousTokenText();}
	| '/' {pushPreviousTokenText();}
	| '%' {pushPreviousTokenText();}
;

exponentiationExpression:
	unaryExpression (
		{startOperatorExpression();} (exponentiationOperator {set(operatorExpression_Operator());}) (exponentiationExpression {addOperand();}) {end(_localctx);}
	)?
;

exponentiationOperator:
	'**' {pushPreviousTokenText();}
	| '^' {pushPreviousTokenText();}
;

unaryExpression:
	({start(operatorExpression__());} unaryOperator {set(operatorExpression_Operator());}) (extentExpression {addOperand();} {end(_localctx);})
	| extentExpression
;

unaryOperator:
	'+' {pushPreviousTokenText();}
	| '-' {pushPreviousTokenText();}
	| '~' {pushPreviousTokenText();}
	| 'not' {pushPreviousTokenText();}
;

extentExpression:
	({start(operatorExpression__());} 'all' {set(operatorExpression_Operator(), "all");}) (typeResultMember {add(element_OwnedRelationship());} {end(_localctx);})
	| primaryExpression
;

primaryExpression:
	baseExpression (
		{startFeatureChainExpression();} '.' (featureChainMember {add(element_OwnedRelationship());}) {end(_localctx);}
	)?
	 (
		(
			{startIndexExpression();} '#' '(' (sequenceExpression {addOperand();}) ')' {end(_localctx);}
			| {startOperatorExpression();} ('[' {set(operatorExpression_Operator(), "[");}) (sequenceExpression {addOperand();}) ']' {end(_localctx);}
			| {startInvocationExpression();} '->' (instantiatedTypeMember {add(element_OwnedRelationship());}) (
				(bodyExpression {addOperand();})
				| (functionReferenceExpression {addOperand();})
				| args=argumentList {attachParenthesizedLambdaInvocation($args.ctx);}
				) {end(_localctx);}
			| {startCollectExpression();} '.' (bodyExpression {addOperand();}) {end(_localctx);}
			| {startSelectExpression();} '.?' (bodyExpression {addOperand();}) {end(_localctx);}
		) (
			{startFeatureChainExpression();} '.' (featureChainMember {add(element_OwnedRelationship());}) {end(_localctx);}
		)?
	)*
;

functionReferenceExpression:
	{start(featureReferenceExpression__());} (
	(functionReferenceMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

functionReferenceMember:
	{start(featureMembership__());} (
	(functionReference {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

functionReference:
	{start(expression__());} (
	(referenceTyping {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

featureChainMember:
	({start(membership__());} member=qualifiedName {setLink(membership_MemberElement(), feature__()); if ($member.quoted) attachQuotedName();} {end(_localctx);})
	| {start(owningMembership__());} (ownedFeatureChain {add(relationship_OwnedRelatedElement());} {end(_localctx);})
;

ownedFeatureChain:
	{start(feature__());} (
	featureChain
	) {end(_localctx);}
;

baseExpression:
	nullExpression
	| literalExpression
	| featureReferenceExpression
	| metadataAccessExpression
	| invocationExpression
	| constructorExpression
	| bodyExpression
	| '(' sequenceExpression {attachParenthesizedExpression();} ')'
		{attachTrailingComma(_localctx);}
;

bodyExpression:
	{start(featureReferenceExpression__());} (
	(expressionBodyMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

expressionBodyMember:
	{start(featureMembership__());} (
	(expressionBody {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

expressionBody:
	{start(expression__());} (
	'{' (
		(bodyParameterMember {add(element_OwnedRelationship());}) ';'
	)*
	 (resultExpressionMember {add(element_OwnedRelationship());}) '}'
	) {end(_localctx);}
;

resultExpressionMember:
	{start(resultExpressionMembership__());} (
	(ownedExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

bodyParameterMember:
	{start(parameterMembership__());} (
	'in' (bodyParameter {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

bodyParameter:
	{start(feature__());} (
	(name {set(element_DeclaredName());})
	) {end(_localctx);}
;

sequenceExpression:
	ownedExpression (
		',' | {startOperatorExpression();} (',' {set(operatorExpression_Operator(), ",");}) (sequenceExpression {addOperand();}) {end(_localctx);}
	)?
;

featureReferenceExpression:
	{start(featureReferenceExpression__());} (
	(featureReferenceMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

featureReferenceMember:
	{start(membership__());} (
	(member=qualifiedName {setLink(membership_MemberElement(), feature__()); if ($member.quoted) attachQuotedName();})
	) {end(_localctx);}
;

metadataAccessExpression:
	{start(metadataAccessExpression__());} (
	(elementReferenceMember {add(element_OwnedRelationship());}) '.' 'metadata'
	) {end(_localctx);}
;

elementReferenceMember:
	{start(membership__());} (
	(member=qualifiedName {setLink(membership_MemberElement(), element__()); if ($member.quoted) attachQuotedName();})
	) {end(_localctx);}
;

invocationExpression:
	{start(invocationExpression__());} (
	(instantiatedTypeMember {add(element_OwnedRelationship());}) argumentList
	) {end(_localctx);}
;

constructorExpression:
	{start(constructorExpression__());} (
	'new' (instantiatedTypeMember {add(element_OwnedRelationship());}) (constructorResultMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

constructorResultMember:
	{start(returnParameterMembership__());} (
	(constructorResult {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

constructorResult:
	{start(feature__());} (
	argumentList
	) {end(_localctx);}
;

instantiatedTypeMember:
	({start(membership__());} qualifiedName {setLink(membership_MemberElement(), type__());} {end(_localctx);})
	| ({start(owningMembership__());} ownedFeatureChain {add(relationship_OwnedRelatedElement());} {end(_localctx);})
;

featureChain:
	(ownedFeatureChaining {add(element_OwnedRelationship());}) (
		'.' (ownedFeatureChaining {add(element_OwnedRelationship());})
	)+
;

ownedFeatureChaining:
	{start(featureChaining__());} (
	(member=qualifiedName {setLink(featureChaining_ChainingFeature(), feature__()); if ($member.quoted) attachQuotedName();})
	) {end(_localctx);}
;

argumentList:
	'(' (
		positionalArgumentList | namedArgumentList
	)? {attachTrailingComments();} ')' {attachTrailingComma(_localctx);}
;

positionalArgumentList:
	(argumentMember {add(element_OwnedRelationship());}) (
		',' (argumentMember {add(element_OwnedRelationship());})
	)*
;

argumentMember:
	{start(parameterMembership__());} (
	(argument {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

argument:
	{start(feature__());} (
	(value=argumentValue {add(element_OwnedRelationship()); attachFollowingComments($value.ctx);})
	) {end(_localctx);}
;

namedArgumentList:
	(namedArgumentMember {add(element_OwnedRelationship());}) (
		',' (namedArgumentMember {add(element_OwnedRelationship());})
	)*
;

namedArgumentMember:
	{start(parameterMembership__());} (
	(namedArgument {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

namedArgument:
	{start(feature__());} (
	(parameterRedefinition {add(element_OwnedRelationship());}) '=' (argumentValue {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

parameterRedefinition:
	{start(redefinition__());} (
	(target=qualifiedName {setLink(redefinition_RedefinedFeature(), feature__()); if ($target.quoted) attachQuotedName();})
	) {end(_localctx);}
;

argumentValue:
	{start(featureValue__());} (
	(ownedExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

nullExpression:
	{start(nullExpression__());} (
	(
		'null' | '(' ')' {attachEmptyParenthesizedExpression();}
	)
	) {end(_localctx);}
;

literalExpression:
	literalBoolean
	| literalString
	| literalInteger
	| literalReal
	| literalInfinity
;

literalBoolean:
	{start(literalBoolean__());} (
	(booleanValue {set(literalBoolean_Value());})
	) {end(_localctx);}
;

booleanValue:
	'true' {pushValue(true);}
	| 'false' {pushValue(false);}
;

literalString:
	{start(literalString__());} (
	(value=STRING_VALUE {setStringValue(literalString_Value(), _localctx.value);})
	) {end(_localctx);}
;

literalInteger:
	{start(literalInteger__());} (
	(value=DECIMAL_VALUE {set(literalInteger_Value(), _localctx.value);})
	) {end(_localctx);}
;

literalReal:
	{start(literalRational__());} (
	(realValue  {set(literalRational_Value(), fromString(eDouble(), currentContextText(_localctx)));})
	) {end(_localctx);}
;

realValue:
	(DECIMAL_VALUE? '.' (
		DECIMAL_VALUE | EXP_VALUE
	))
	| EXP_VALUE
;

literalInfinity:
	{start(literalInfinity__());} (
	'*'
	) {end(_localctx);}
;

name returns [boolean quoted]:
	ID {pushPreviousTokenText(); $quoted = false;}
	| UNRESTRICTED_NAME {pushValue(stringValue(previousTokenText())); $quoted = true;}
;

// This rule is the same as the name rule but doesn't un-escape each segment.
// It can be used in the rule qualifiedName, this way link resolution can distinguish global ($) from UNRESTRICTED_NAME ('$')
nameSemgent returns [boolean quoted]:
	ID {pushPreviousTokenText(); $quoted = false;}
	| UNRESTRICTED_NAME {pushValue(previousTokenText()); $quoted = true;}
;

globalQualification:
	('$' '::' {setGlobalQualifiedName();})
;

qualification:
	(
		(nameSemgent {addQualifiedNameSegment();}) '::'
	)+
;

qualifiedName returns [boolean quoted]:
	{pushQualifiedName();} (
	globalQualification? qualification? (n=nameSemgent {addQualifiedNameSegment(); $quoted = $n.quoted;})
	) {popQualifiedName();}
;

DECIMAL_VALUE:
	'0' .. '9' (
		'0' .. '9'
	)*
;

EXP_VALUE:
	DECIMAL_VALUE (
		'e' | 'E'
	) (
		'+' | '-'
	)? DECIMAL_VALUE
;

ID:
	(
		'a' .. 'z' | 'A' .. 'Z' | '_'
	) (
		'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9'
	)*
;

UNRESTRICTED_NAME:
	'\'' (
		'\\' (
			'b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\'
		) | ~('\\'  | '\'')
	)* '\''
;

STRING_VALUE:
	'"' (
		'\\' (
			'b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\'
		) | ~('\\'  | '"')
	)* '"'
;

REGULAR_COMMENT:
	'/*' .*? '*/'
;

ML_NOTE:
	'//*' .*? '*/'
-> channel(HIDDEN);

SL_NOTE:
	// the lookhead check is needed to exclude ML_NOTE otherwise the longest matched token is used
	// this lead to matching a SL_NOTE until the next new line
	'//' { _input.LA(1) != '*' }? (
		~('\n'  | '\r') ~('\n'  | '\r')*
	)?
	 (
		'\r'? '\n'
	)?
-> channel(HIDDEN);

WS:
	(
		' ' | '\t' | '\r' | '\n'
	)+
-> skip;
