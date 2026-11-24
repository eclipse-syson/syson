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
grammar SysML;

@header {
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
package org.eclipse.syson.sysml.parser.internal;
}

options {
superClass=AbstractSysMLEMFParser;
}

import KerMLExpressions;

rootNamespace:
	{start(namespace__());} (
	packageBodyElement*
	) {attachTrailingComments();} {end(_localctx);}
;

identification:
	'<' (shortName=name {set(element_DeclaredShortName()); if ($shortName.quoted) attachQuotedShortName();}) '>' (declaredName=name {set(element_DeclaredName()); if ($declaredName.quoted) attachQuotedName();})?
	| (declaredName=name {set(element_DeclaredName()); if ($declaredName.quoted) attachQuotedName();})
;

relationshipBody:
	';'
	| '{' {attachBodyBlock();} (ownedAnnotation {add(element_OwnedRelationship());})* '}'
;

dependency:
	{start(dependency__());} (
	(prefixMetadataAnnotation {add(element_OwnedRelationship());})* 'dependency' (
		identification? 'from'
		| {attachDependencyFromOmitted();}
	)
	 (qualifiedName {addLink(dependency_Client(), element__());}) (
		',' (qualifiedName {addLink(dependency_Client(), element__());})
	)*
	 'to' (qualifiedName {addLink(dependency_Supplier(), element__());}) (
		',' (qualifiedName {addLink(dependency_Supplier(), element__());})
	)*
	 relationshipBody
	) {end(_localctx);}
;

annotation:
	{start(annotation__());} (
	qualifiedName {setLink(annotation_AnnotatedElement(), element__());}
	) {end(_localctx);}
;

ownedAnnotation:
	{start(annotation__());} (
	(annotatingElement {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

annotatingMember:
	{start(owningMembership__());} (
	memberPrefix (annotatingElement {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

annotatingElement:
	comment
	| documentation
	| textualRepresentation
	| metadataUsage
;

comment:
	{start(comment__());} (
	(
		'comment' {attachCommentKeyword();} identification? (
			'about' (annotation {add(element_OwnedRelationship());}) (
				',' (annotation {add(element_OwnedRelationship());})
			)*
		)?
	)?
	 (
		'locale' (locale=STRING_VALUE {setStringValue(comment_Locale(), _localctx.locale);})
	)?
	 (body=REGULAR_COMMENT {setCommentBody(comment_Body(), _localctx.body);})
	) {end(_localctx);}
;

documentation:
	{start(documentation__());} (
	'doc' identification? (
		'locale' (locale=STRING_VALUE {setStringValue(comment_Locale(), _localctx.locale);})
	)?
	 (body=REGULAR_COMMENT {setCommentBody(comment_Body(), _localctx.body);})
	) {end(_localctx);}
;

textualRepresentation:
	{start(textualRepresentation__());} (
	(
		'rep' {attachExplicitRepKeyword();} identification?
	)?
	 'language' (language=STRING_VALUE {setStringValue(textualRepresentation_Language(), _localctx.language);}) (body=REGULAR_COMMENT {setCommentBody(textualRepresentation_Body(), _localctx.body);})
	) {end(_localctx);}
;

metadataKeyword:
	'metadata'
;

metadataDefKeyword:
	metadataKeyword 'def'
;

metadataUsageKeyword:
	metadataKeyword
	| '@' {attachMetadataAtSign();}
;

metadataDefinition:
	{start(metadataDefinition__());} (
	('abstract' {setTrue(type_IsAbstract());})? definitionExtensionKeyword* metadataDefKeyword definition
	) {end(_localctx);}
;

prefixMetadataAnnotation:
	{start(annotation__());} (
	'#' (prefixMetadataUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

prefixMetadataMember:
	{start(owningMembership__());} (
	'#' (prefixMetadataUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

prefixMetadataUsage:
	{start(metadataUsage__());} (
	{attachPrefixMetadata();} (metadataTyping {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

metadataUsage:
	{start(metadataUsage__());} (
	usageExtensionKeyword* metadataUsageKeyword metadataUsageDeclaration (
		'about' (annotation {add(element_OwnedRelationship());}) (
			',' (annotation {add(element_OwnedRelationship());})
		)*
	)?
	 metadataBody
	) {end(_localctx);}
;

metadataUsageDeclaration:
	(
		id=identification? separator=definedByKeyword {if ($id.ctx == null && ":".equals($separator.text)) attachMetadataColon();}
	)?
	 (metadataTyping {add(element_OwnedRelationship());})
;

metadataTyping:
	{start(featureTyping__());} (
	(qualifiedName {setLink(featureTyping_Type(), metaclass__());})
	) {end(_localctx);}
;

metadataBody:
	';'
	| '{' {attachBodyBlock();} (
		(definitionMember {add(element_OwnedRelationship());}) | (metadataBodyUsageMember {add(element_OwnedRelationship());}) | (aliasMember {add(element_OwnedRelationship());}) | (import_ {add(element_OwnedRelationship());})
	)* '}'
;

metadataBodyUsageMember:
	{start(featureMembership__());} (
	(metadataBodyUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

metadataBodyUsage:
	{start(referenceUsage__());} (
	('ref' {attachExplicitRefPrefix();})? (
		':>>' {attachRedefSymb();} | 'redefines' {attachRedef();}
	)? (ownedRedefinition {add(element_OwnedRelationship());}) featureSpecializationPart? valuePart? metadataBody
	) {end(_localctx);}
;

package_:
	{start(package__());} (
	(prefixMetadataMember {add(element_OwnedRelationship());})* packageDeclaration packageBody
	) {end(_localctx);}
;

libraryPackage:
	{start(libraryPackage__());} (
	('standard' {setTrue(libraryPackage_IsStandard());})? 'library' (prefixMetadataMember {add(element_OwnedRelationship());})* packageDeclaration packageBody
	) {end(_localctx);}
;

packageDeclaration:
	'package' identification?
;

packageBody:
	';'
	| '{' {attachBodyBlock();} (
		(packageMember {add(element_OwnedRelationship());})
		| (elementFilterMember {add(element_OwnedRelationship());})
		| (aliasMember {add(element_OwnedRelationship());})
		| (import_ {add(element_OwnedRelationship());})
	)* {attachTrailingComments();} '}'
;

packageBodyElement:
	(packageMember {add(element_OwnedRelationship());})
	| (elementFilterMember {add(element_OwnedRelationship());})
	| (aliasMember {add(element_OwnedRelationship());})
	| (import_ {add(element_OwnedRelationship());})
;

memberPrefix:
	(visibilityIndicator {set(membership_Visibility());})?
;

packageMember:
	{start(owningMembership__());} (
	memberPrefix (
		(definitionElement {add(relationship_OwnedRelatedElement());}) | (usageElement {add(relationship_OwnedRelatedElement());})
	)
	) {end(_localctx);}
;

elementFilterMember:
	{start(elementFilterMembership__());} (
	memberPrefix 'filter' (ownedExpression {add(relationship_OwnedRelatedElement());}) ';'
	) {end(_localctx);}
;

aliasMember:
	{start(membership__());} (
	memberPrefix 'alias' (
		'<' (name {set(membership_MemberShortName());}) '>'
	)?
		 (aliasName=name {set(membership_MemberName()); if ($aliasName.quoted) attachQuotedName();})? 'for' (target=qualifiedName {setLink(membership_MemberElement(), element__()); if ($target.quoted) attachQuotedAliasTarget();}) relationshipBody
	) {end(_localctx);}
;

importPrefix:
	(visibilityIndicator {set(import_Visibility());}) 'import' ('all' {setTrue(import_IsImportAll());})?
;

import_:
	(
		membershipImport | namespaceImport
	) {restart();} relationshipBody {end(_localctx);}
;

membershipImport:
	{start(membershipImport__());} (
	importPrefix importedMembership
	) {end(_localctx);}
;

importedMembership:
	(imported=qualifiedName {setLink(membershipImport_ImportedMembership(), membership__()); if ($imported.quoted) attachQuotedName();}) (
		'::' ('**' {setTrue(import_IsRecursive());})
	)?
;

namespaceImport:
	{start(namespaceImport__());} (
	importPrefix (
		importedNamespace | (filterPackage {add(relationship_OwnedRelatedElement());})
	)
	) {end(_localctx);}
;

importedNamespace:
	(imported=qualifiedName {setLink(namespaceImport_ImportedNamespace(), namespace__()); if ($imported.quoted) attachQuotedName();}) '::' '*' (
		'::' ('**' {setTrue(import_IsRecursive());})
	)?
;

filterPackage:
	{start(package__());} (
	(filterPackageImport {add(element_OwnedRelationship());}) (filterPackageMember {add(element_OwnedRelationship());})+
	) {end(_localctx);}
;

filterPackageImport:
	filterPackageMembershipImport
	| filterPackageNamespaceImport
;

filterPackageMembershipImport:
	{start(membershipImport__());} (
	importedMembership
	) {end(_localctx);}
;

filterPackageNamespaceImport:
	{start(namespaceImport__());} (
	importedNamespace
	) {end(_localctx);}
;

filterPackageMember:
	{start(elementFilterMembership__());} (
	(filterPackageMemberVisibility {set(membership_Visibility());}) (ownedExpression {add(relationship_OwnedRelatedElement());}) ']'
	) {end(_localctx);}
;

filterPackageMemberVisibility:
	'[' {pushValue(visibilityKind_Private());}
;

visibilityIndicator:
	'public' {pushValue(visibilityKind_Public());} {attachExplicitPublicVisibility();}
	| 'private' {pushValue(visibilityKind_Private());}
	| 'protected' {pushValue(visibilityKind_Protected());}
;

definitionElement:
	package_
	| libraryPackage
	| annotatingElement
	| dependency
	| attributeDefinition
	| enumerationDefinition
	| occurrenceDefinition
	| individualDefinition
	| itemDefinition
	| metadataDefinition
	| partDefinition
	| connectionDefinition
	| flowDefinition
	| interfaceDefinition
	| allocationDefinition
	| portDefinition
	| actionDefinition
	| calculationDefinition
	| stateDefinition
	| constraintDefinition
	| requirementDefinition
	| concernDefinition
	| caseDefinition
	| analysisCaseDefinition
	| verificationCaseDefinition
	| useCaseDefinition
	| viewDefinition
	| viewpointDefinition
	| renderingDefinition
	| extendedDefinition
;

usageElement:
	nonOccurrenceUsageElement
	| occurrenceUsageElement
;

subclassificationPart:
	specializesKeyword (ownedSubclassification {add(element_OwnedRelationship());}) (
		',' (ownedSubclassification {add(element_OwnedRelationship());})
	)*
;

specializesKeyword:
	':>' {attachSpecSymb();}
	| 'specializes' {attachSpec();}
;

ownedSubclassification:
	{start(subclassification__());} (
	(qualifiedName {setLink(subclassification_Superclassifier(), classifier__());})
	) {end(_localctx);}
;

featureDeclaration:
	identification featureSpecializationPart?
	| featureSpecializationPart
;

featureSpecializationPart:
	featureSpecialization+ multiplicityPart? {attachLateNonUnique();} featureSpecialization*
	| multiplicityPart featureSpecialization*
;

multiplicityPart:
	(ownedMultiplicity {add(element_OwnedRelationship());})
	| (ownedMultiplicity {add(element_OwnedRelationship());})? (
		('ordered' {setTrue(feature_IsOrdered());}) (nonunique {set(feature_IsUnique()); attachNonUniqueAfterOrdered();})?
		| (nonunique {set(feature_IsUnique());}) ('ordered' {setTrue(feature_IsOrdered());})?
	)
;

nonunique:
	('nonunique' {pushValue(false);})
;

featureSpecialization:
	typings
	| subsettings
	| references
	| crosses
	| redefinitions
;

typings:
	typedBy (
		',' (featureTyping {add(element_OwnedRelationship());})
	)*
;

typedBy:
	definedByKeyword (featureTyping {add(element_OwnedRelationship());})
;

definedByKeyword:
	':'
	| 'defined' 'by' {attachDefinedBy();}
;

subsettings:
	subsets (
		',' (ownedSubsetting {add(element_OwnedRelationship());})
	)*
;

subsets:
	subsetsKeyword (ownedSubsetting {add(element_OwnedRelationship());})
;

subsetsKeyword:
	':>' {attachSubsetSymb();}
	| 'subsets' {attachSubset();}
;

references:
	referencesKeyword (ownedReferenceSubsetting {add(element_OwnedRelationship());})
;

referencesKeyword:
	'::>' {attachRefSymb();}
	| 'references' {attachRef();}
;

crosses:
	crossesKeyword (ownedCrossSubsetting {add(element_OwnedRelationship());})
;

crossesKeyword:
	'=>' {attachCrossSymb();}
	| 'crosses' {attachCross();}
;

redefines:
	redefinesKeyword (ownedRedefinition {add(element_OwnedRelationship());})
;

redefinesKeyword:
	':>>' {attachRedefSymb();}
	| 'redefines' {attachRedef();}
;

featureTyping:
	ownedFeatureTyping
	| conjugatedPortTyping
;

ownedFeatureTyping:
	({start(featureTyping__()); attachTypingClauseStart();} target=qualifiedName {setLink(featureTyping_Type(), type__()); if ($target.quoted) attachQuotedName();} {end(_localctx);})
	| ({start(featureTyping__()); attachTypingClauseStart();} ownedFeatureChain {add(relationship_OwnedRelatedElement());} {end(_localctx);})
;

ownedSubsetting:
	({start(subsetting__());} target=qualifiedName {setLink(subsetting_SubsettedFeature(), feature__()); if ($target.quoted) attachQuotedName();} {end(_localctx);})
	| ({start(subsetting__());} ownedFeatureChain {add(relationship_OwnedRelatedElement());} {end(_localctx);})
;

ownedReferenceSubsetting returns [boolean quoted]:
	({start(referenceSubsetting__());} reference=qualifiedName {setLink(referenceSubsetting_ReferencedFeature(), feature__()); $quoted = $reference.quoted; if ($reference.quoted) attachQuotedName();} {end(_localctx);})
	| ({start(referenceSubsetting__());} ownedFeatureChain {add(relationship_OwnedRelatedElement());} {end(_localctx);})
;

ownedCrossSubsetting:
	({start(crossSubsetting__());} target=qualifiedName {setLink(crossSubsetting_CrossedFeature(), feature__()); if ($target.quoted) attachQuotedName();} {end(_localctx);})
	| ({start(crossSubsetting__());} ownedFeatureChain {add(relationship_OwnedRelatedElement());} {end(_localctx);})
;

redefinitions:
	redefines (
		',' (ownedRedefinition {add(element_OwnedRelationship());})
	)*
;

ownedRedefinition:
	({start(redefinition__());} target=qualifiedName {setLink(redefinition_RedefinedFeature(), feature__()); if ($target.quoted) attachQuotedName();} {end(_localctx);})
	| ({start(redefinition__());} ownedFeatureChain {add(relationship_OwnedRelatedElement());} {end(_localctx);})
;

ownedMultiplicity:
	{start(owningMembership__());} (
	(multiplicityRange {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

multiplicityRange:
	{start(multiplicityRange__());} (
	'[' (multiplicityExpressionMember {add(element_OwnedRelationship());}) (
		'..' (multiplicityExpressionMember {add(element_OwnedRelationship());})
	)?
	 ']'
	) {end(_localctx);}
;

multiplicityExpressionMember:
	{start(owningMembership__());} (
	((literalExpression | featureReferenceExpression) {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

basicDefinitionPrefix:
	('abstract' {setTrue(type_IsAbstract());})
	| ('variation' {setTrue(definition_IsVariation());})
;

definitionExtensionKeyword:
	(prefixMetadataMember {add(element_OwnedRelationship());})
;

definitionPrefix:
	basicDefinitionPrefix? definitionExtensionKeyword*
;

definition:
	definitionDeclaration definitionBody
;

definitionDeclaration:
	identification? subclassificationPart?
;

definitionBody:
	';'
	| '{' {attachBodyBlock();} definitionBodyItem* {attachTrailingComments();} '}'
;

definitionBodyItem:
	(definitionMember {add(element_OwnedRelationship());})
	| (variantUsageMember {add(element_OwnedRelationship());})
	| (nonOccurrenceUsageMember {add(element_OwnedRelationship());})
	| (emptySuccessionMember {add(element_OwnedRelationship());})? (occurrenceUsageMember {add(element_OwnedRelationship());})
	| (aliasMember {add(element_OwnedRelationship());})
	| (import_ {add(element_OwnedRelationship());})
;

definitionMember:
	{start(owningMembership__());} (
	memberPrefix (definitionElement {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

variantUsageMember:
	{start(variantMembership__());} (
	memberPrefix 'variant' (parsedUsage=variantUsageElement {add(relationship_OwnedRelatedElement());
		attachSameLineFollowingComments($parsedUsage.ctx);})
	) {end(_localctx);}
;

nonOccurrenceUsageMember:
	{start(featureMembership__());} (
	memberPrefix (parsedUsage=nonOccurrenceUsageElement {add(relationship_OwnedRelatedElement());
		attachSameLineFollowingComments($parsedUsage.ctx);})
	) {end(_localctx);}
;

occurrenceUsageMember:
	{start(featureMembership__());} (
	memberPrefix (parsedUsage=occurrenceUsageElement {add(relationship_OwnedRelatedElement());
		attachSameLineFollowingComments($parsedUsage.ctx);})
	) {end(_localctx);}
;

structureUsageMember:
	{start(featureMembership__());} (
	memberPrefix (parsedUsage=structureUsageElement {add(relationship_OwnedRelatedElement());
		attachSameLineFollowingComments($parsedUsage.ctx);})
	) {end(_localctx);}
;

behaviorUsageMember:
	{start(featureMembership__());} (
	memberPrefix (behaviorUsageElement {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

featureDirection:
	'in' {pushValue(featureDirectionKind_In());}
	| 'out' {pushValue(featureDirectionKind_Out());}
	| 'inout' {pushValue(featureDirectionKind_Inout());}
;

refPrefix:
	(featureDirection {set(feature_Direction());})? ('derived' {setTrue(feature_IsDerived());})? (
		('abstract' {setTrue(type_IsAbstract());}) | ('variation' {setTrue(usage_IsVariation());})
	)? ('constant' {setTrue(feature_IsConstant());})?
;

basicUsagePrefix:
	// set isComposite to false is equivalent to set isReference to true
	refPrefix ('ref' {setFalse(feature_IsComposite());} {attachExplicitRefPrefix();})?
;

endUsagePrefix:
	('end' {setTrue(feature_IsEnd());}) (ownedCrossFeatureMember {add(element_OwnedRelationship());})?
;

unextendedUsagePrefix:
	endUsagePrefix
	| basicUsagePrefix
;

usageExtensionKeyword:
	(prefixMetadataMember {add(element_OwnedRelationship());})
;

usagePrefix:
	unextendedUsagePrefix usageExtensionKeyword*
;

ownedCrossFeatureMember:
	{start(owningMembership__());} (
	(ownedCrossFeature {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

ownedCrossFeature:
	{start(referenceUsage__());} (
	basicUsagePrefix usageDeclaration
	) {end(_localctx);}
;

usage:
	usageDeclaration? usageCompletion
;

usageDeclaration:
	featureDeclaration
;

usageCompletion:
	valuePart? usageBody
;

usageBody:
	definitionBody
;

valuePart:
	(featureValue {add(element_OwnedRelationship());})
;

featureValue:
	{start(featureValue__());} (
	(
		'='
		| (':=' {setTrue(featureValue_IsInitial());})
		| ('default' {setTrue(featureValue_IsDefault());}) (
			'=' {attachFeatureValueDefaultEquals();}
			| (':=' {setTrue(featureValue_IsInitial());} {attachFeatureValueDefaultInitial();})
		)?
		{attachFeatureValueDefault();}
	) (expr=ownedExpression {add(relationship_OwnedRelatedElement());} {attachParenthesizedFeatureValueExpression($expr.ctx);})
	) {end(_localctx);}
;

referenceKeyword:
	'ref'
;

referenceUsageKeyword:
	referenceKeyword
;

defaultReferenceUsage:
	{start(referenceUsage__());} (
	('end' {setTrue(feature_IsEnd());})? refPrefix usageDeclaration valuePart? usageBody
	) {end(_localctx);}
;

referenceUsage:
	{start(referenceUsage__());} (
	(
		endUsagePrefix | refPrefix
	) referenceUsageKeyword {attachExplicitRefPrefix();} usage
	) {end(_localctx);}
;

variantReference:
	{start(referenceUsage__());} (
	(ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecialization* usageBody
	) {end(_localctx);}
;

nonOccurrenceUsageElement:
	defaultReferenceUsage
	| referenceUsage
	| attributeUsage
	| enumerationUsage
	| bindingConnectorAsUsage
	| successionAsUsage
	| extendedUsage
;

occurrenceUsageElement:
	structureUsageElement
	| behaviorUsageElement
;

structureUsageElement:
	occurrenceUsage
	| individualUsage
	| portionUsage
	| eventOccurrenceUsage
	| itemUsage
	| partUsage
	| viewUsage
	| renderingUsage
	| portUsage
	| connectionUsage
	| interfaceUsage
	| allocationUsage
	| message
	| flowUsage
	| successionFlowUsage
;

behaviorUsageElement:
	actionUsage
	| calculationUsage
	| stateUsage
	| constraintUsage
	| requirementUsage
	| concernUsage
	| caseUsage
	| analysisCaseUsage
	| verificationCaseUsage
	| useCaseUsage
	| viewpointUsage
	| performActionUsage
	| exhibitStateUsage
	| includeUseCaseUsage
	| assertConstraintUsage
	| satisfyRequirementUsage
;

variantUsageElement:
	variantReference
	| referenceUsage
	| attributeUsage
	| bindingConnectorAsUsage
	| successionAsUsage
	| occurrenceUsage
	| individualUsage
	| portionUsage
	| eventOccurrenceUsage
	| itemUsage
	| partUsage
	| viewUsage
	| renderingUsage
	| portUsage
	| connectionUsage
	| interfaceUsage
	| allocationUsage
	| message
	| flowUsage
	| successionFlowUsage
	| behaviorUsageElement
;

extendedDefinition:
	{start(definition__());} (
	basicDefinitionPrefix? definitionExtensionKeyword+ 'def' definition
	) {end(_localctx);}
;

extendedUsage:
	{start(usage__());} (
	unextendedUsagePrefix usageExtensionKeyword+ usage
	) {end(_localctx);}
;

attributeKeyword:
	'attribute'
;

attributeDefKeyword:
	attributeKeyword 'def'
;

attributeUsageKeyword:
	attributeKeyword
;

attributeDefinition:
	{start(attributeDefinition__());} (
	definitionPrefix attributeDefKeyword definition
	) {end(_localctx);}
;

attributeUsage:
	{start(attributeUsage__());} (
	usagePrefix attributeUsageKeyword usage
	) {end(_localctx);}
;

enumerationKeyword:
	'enum'
;

enumerationDefKeyword:
	enumerationKeyword 'def'
;

enumerationUsageKeyword:
	enumerationKeyword
;

enumerationDefinition:
	{start(enumerationDefinition__());} (
	definitionExtensionKeyword* enumerationDefKeyword definitionDeclaration enumerationBody
	) {end(_localctx);}
;

enumerationBody:
	';'
	| '{' {attachBodyBlock();} (
		(annotatingMember {add(element_OwnedRelationship());})
		| (enumerationUsageMember {add(element_OwnedRelationship());})
	)* {attachTrailingComments();} '}'
;

enumerationUsageMember:
	{start(variantMembership__());} (
	memberPrefix (enumeratedValue {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

enumeratedValue:
	{start(enumerationUsage__());} (
	usageExtensionKeyword* keyword=enumerationUsageKeyword? {if ($keyword.ctx == null) attachEnumKeywordOmitted();} usage
	) {end(_localctx);}
;

enumerationUsage:
	{start(enumerationUsage__());} (
	usagePrefix enumerationUsageKeyword usage
	) {end(_localctx);}
;

occurrenceKeyword:
	'occurrence'
;

occurrenceDefKeyword:
	occurrenceKeyword 'def'
;

occurrenceDefinitionPrefix:
	basicDefinitionPrefix? (
		('individual' {setTrue(occurrenceDefinition_IsIndividual());}) (emptyMultiplicityMember {add(element_OwnedRelationship());})
	)?
	 definitionExtensionKeyword*
;

occurrenceDefinition:
	{start(occurrenceDefinition__());} (
	occurrenceDefinitionPrefix occurrenceDefKeyword definition
	) {end(_localctx);}
;

individualDefinition:
	{start(occurrenceDefinition__());} (
	basicDefinitionPrefix? ('individual' {setTrue(occurrenceDefinition_IsIndividual());} {attachCompactIndividualDefinition();}) (emptyMultiplicityMember {add(element_OwnedRelationship());}) definitionExtensionKeyword* 'def' definition
	) {end(_localctx);}
;

emptyMultiplicityMember:
	{start(owningMembership__());} (
	(emptyMultiplicity {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

emptyMultiplicity:
	{start(multiplicity__());} (
	
	) {end(_localctx);}
;

occurrenceUsageKeyword:
	occurrenceKeyword
;

occurrenceUsagePrefix:
	(
		endUsagePrefix
		| basicUsagePrefix ('individual' {setTrue(occurrenceUsage_IsIndividual());})? (portionKind {set(occurrenceUsage_PortionKind());})?
	) usageExtensionKeyword*
;

occurrenceUsage:
	{start(occurrenceUsage__());} (
	occurrenceUsagePrefix occurrenceUsageKeyword usage
	) {end(_localctx);}
;

individualUsage:
	{start(occurrenceUsage__());} (
	basicUsagePrefix ('individual' {setTrue(occurrenceUsage_IsIndividual());} {attachCompactIndividualUsage();}) usageExtensionKeyword* usage
	) {end(_localctx);}
;

portionUsage:
	{start(occurrenceUsage__());} (
	basicUsagePrefix ('individual' {setTrue(occurrenceUsage_IsIndividual());})? (portionKind {set(occurrenceUsage_PortionKind());}) usageExtensionKeyword* usage
	) {end(_localctx);}
;

portionKind:
	'snapshot' {pushValue(portionKind_Snapshot());}
	| 'timeslice' {pushValue(portionKind_Timeslice());}
;

eventOccurrenceUsage:
	{start(eventOccurrenceUsage__());} (
	occurrenceUsagePrefix 'event' (
		(ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecializationPart? | occurrenceUsageKeyword usageDeclaration?
	) usageCompletion
	) {end(_localctx);}
;

emptySuccessionMember:
	{start(featureMembership__());} (
	(emptySuccession {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

emptySuccession:
	{start(successionAsUsage__());} (
	'then' (multiplicitySourceEndMember {add(element_OwnedRelationship());}) (emptyTargetEndMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

multiplicitySourceEndMember:
	{start(endFeatureMembership__());} (
	(multiplicitySourceEnd {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

multiplicitySourceEnd:
	{start(referenceUsage__());} (
	(ownedMultiplicity {add(element_OwnedRelationship());})?
	) {end(_localctx);}
;

emptyTargetEndMember:
	{start(endFeatureMembership__());} (
	(emptyTargetEnd {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

emptyTargetEnd:
	{start(referenceUsage__());} (
	
	) {end(_localctx);}
;

itemKeyword:
	'item'
;

itemDefKeyword:
	itemKeyword 'def'
;

itemUsageKeyword:
	itemKeyword
;

itemDefinition:
	{start(itemDefinition__());} (
	occurrenceDefinitionPrefix itemDefKeyword definition
	) {end(_localctx);}
;

itemUsage:
	{start(itemUsage__());} (
	occurrenceUsagePrefix itemUsageKeyword usage
	) {end(_localctx);}
;

partKeyword:
	'part'
;

partDefKeyword:
	partKeyword 'def'
;

partUsageKeyword:
	partKeyword
;

partDefinition:
	{start(partDefinition__());} (
	occurrenceDefinitionPrefix partDefKeyword definition
	) {end(_localctx);}
;

partUsage:
	{start(partUsage__());} (
	occurrenceUsagePrefix partUsageKeyword usage
	) {end(_localctx);}
;

portKeyword:
	'port'
;

portDefKeyword:
	portKeyword 'def'
;

portDefinition:
	{start(portDefinition__());} (
	definitionPrefix portDefKeyword definition (conjugatedPortDefinitionMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

conjugatedPortDefinitionMember:
	{start(owningMembership__());} (
	(conjugatedPortDefinition {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

conjugatedPortDefinition:
	{start(conjugatedPortDefinition__());} (
	(portConjugation {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

portConjugation:
	{start(portConjugation__());} (
	
	) {end(_localctx);}
;

conjugatedPortTyping:
	{start(conjugatedPortTyping__()); attachTypingClauseStart();} (
	(conjugatedQualifiedName {setLink(conjugatedPortTyping_ConjugatedPortDefinition(), conjugatedPortDefinition__());})
	) {end(_localctx);}
;

conjugatedQualifiedName:
	'~' (qualifiedName {conjugateQualifiedName();})
;

portUsageKeyword:
	portKeyword
;

portUsage:
	{start(portUsage__());} (
	occurrenceUsagePrefix portUsageKeyword {attachEndFeatureKeyword();} usage
	) {end(_localctx);}
;

connectorEndMember:
	{start(endFeatureMembership__());} (
	(connectorEnd {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

connectorEnd:
	{start(referenceUsage__());} (
	(ownedCrossMultiplicityMember {add(element_OwnedRelationship());})? (
		(name {set(element_DeclaredName());}) referencesKeyword
	)?
	 (ownedReferenceSubsetting {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

ownedCrossMultiplicityMember:
	{start(owningMembership__());} (
	(ownedCrossMultiplicity {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

ownedCrossMultiplicity:
	{start(feature__());} (
	(ownedMultiplicity {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

bindingKeyword:
	'binding'
;

bindingConnectorAsUsage:
	{start(bindingConnectorAsUsage__());} (
	usagePrefix (
		bindingKeyword {attachExplicitDeclarationKeyword();} usageDeclaration?
	)?
	 'bind' (connectorEndMember {add(element_OwnedRelationship());}) '=' (connectorEndMember {add(element_OwnedRelationship());}) definitionBody
	) {end(_localctx);}
;

successionKeyword:
	'succession'
;

successionAsUsage:
	{start(successionAsUsage__());} (
	usagePrefix (
		successionKeyword {attachSuccessionKeyword();} usageDeclaration?
	)?
	 'first' (connectorEndMember {add(element_OwnedRelationship());}) 'then' (connectorEndMember {add(element_OwnedRelationship());}) definitionBody
	) {end(_localctx);}
;

connectionKeyword:
	'connection'
;

connectionDefKeyword:
	connectionKeyword 'def'
;

connectionDefinition:
	{start(connectionDefinition__());} (
	occurrenceDefinitionPrefix connectionDefKeyword definition
	) {end(_localctx);}
;

connectorKeyword:
	'connect'
;

connectionUsageKeyword:
	connectionKeyword {attachConnectionKeyword();}
;

connectionUsage:
	{start(connectionUsage__());} (
	occurrenceUsagePrefix (
		connectionUsageKeyword usageDeclaration? valuePart? (
			connectorKeyword connectorPart
		)?
		 | connectorKeyword connectorPart
	) usageBody
	) {end(_localctx);}
;

connectorPart:
	binaryConnectorPart
	| naryConnectorPart
;

binaryConnectorPart:
	(connectorEndMember {add(element_OwnedRelationship());}) 'to' (connectorEndMember {add(element_OwnedRelationship());})
;

naryConnectorPart:
	'(' {attachNaryConnectorPart();} (connectorEndMember {add(element_OwnedRelationship());}) ',' (connectorEndMember {add(element_OwnedRelationship());}) (
		',' (connectorEndMember {add(element_OwnedRelationship());})
	)*
	 ')'
;

emptySourceEndMember:
	{start(endFeatureMembership__());} (
	(emptySourceEnd {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

emptySourceEnd:
	{start(referenceUsage__());} (
	
	) {end(_localctx);}
;

interfaceKeyword:
	'interface'
;

interfaceDefKeyword:
	interfaceKeyword 'def'
;

interfaceDefinition:
	{start(interfaceDefinition__());} (
	occurrenceDefinitionPrefix interfaceDefKeyword definitionDeclaration interfaceBody
	) {end(_localctx);}
;

interfaceBody:
	';'
	| '{' {attachBodyBlock();} interfaceBodyItem* '}'
;

interfaceBodyItem:
	(definitionMember {add(element_OwnedRelationship());})
	| (variantUsageMember {add(element_OwnedRelationship());})
	| (interfaceNonOccurrenceUsageMember {add(element_OwnedRelationship());})
	| (emptySuccessionMember {add(element_OwnedRelationship());})? (interfaceOccurrenceUsageMember {add(element_OwnedRelationship());})
	| (aliasMember {add(element_OwnedRelationship());})
	| (import_ {add(element_OwnedRelationship());})
;

interfaceNonOccurrenceUsageMember:
	{start(featureMembership__());} (
	memberPrefix (interfaceNonOccurrenceUsageElement {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

interfaceNonOccurrenceUsageElement:
	referenceUsage
	| attributeUsage
	| enumerationUsage
	| bindingConnectorAsUsage
	| successionAsUsage
;

interfaceOccurrenceUsageMember:
	{start(featureMembership__());} (
	memberPrefix (interfaceOccurrenceUsageElement {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

interfaceOccurrenceUsageElement:
	defaultInterfaceEnd
	| structureUsageElement
	| behaviorUsageElement
;

defaultInterfaceEnd:
	{start(portUsage__());} (
	('end' {setTrue(feature_IsEnd());}) usage
	) {end(_localctx);}
;

interfaceUsageKeyword:
	interfaceKeyword
;

interfaceUsage:
	{start(interfaceUsage__());} (
	occurrenceUsagePrefix interfaceUsageKeyword
	interfaceUsageDeclaration interfaceBody
	) {end(_localctx);}
;

interfaceUsageDeclaration:
	usageDeclaration? valuePart? (
		connectorKeyword {attachInterfaceConnectorDeclaration(); attachExplicitInterfaceConnectKeyword();} interfacePart
	)? {attachInterfaceMultiplicityBeforeType(_localctx);}
	| {attachInterfaceConnectorDeclaration();} interfacePart
		{attachInterfaceMultiplicityBeforeType(_localctx);}
;

interfacePart:
	binaryInterfacePart
	| naryInterfacePart
;

binaryInterfacePart:
	(interfaceEndMember {add(element_OwnedRelationship());}) 'to' (interfaceEndMember {add(element_OwnedRelationship());})
;

naryInterfacePart:
	'(' (interfaceEndMember {add(element_OwnedRelationship());}) ',' (interfaceEndMember {add(element_OwnedRelationship());}) (
		',' (interfaceEndMember {add(element_OwnedRelationship());})
	)*
	 ')'
;

interfaceEndMember:
	{start(endFeatureMembership__());} (
	(interfaceEnd {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

interfaceEnd:
	{start(portUsage__());} (
	(ownedCrossMultiplicityMember {add(element_OwnedRelationship());})? (
		(name {set(element_DeclaredName());}) referencesKeyword
	)?
	 (ownedReferenceSubsetting {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

allocationKeyword:
	'allocation'
;

allocationDefKeyword:
	allocationKeyword 'def'
;

allocationDefinition:
	{start(allocationDefinition__());} (
	occurrenceDefinitionPrefix allocationDefKeyword definition
	) {end(_localctx);}
;

allocationUsageKeyword:
	allocationKeyword
;

allocateKeyword:
	'allocate'
;

allocationUsage:
	{start(allocationUsage__());} (
	occurrenceUsagePrefix allocationUsageDeclaration usageBody
	) {end(_localctx);}
;

allocationUsageDeclaration:
	allocationUsageKeyword {attachExplicitDeclarationKeyword();} usageDeclaration? (
		allocateKeyword connectorPart
	)?
	| allocateKeyword connectorPart
;

flowKeyword:
	'flow'
;

flowDefKeyword:
	flowKeyword 'def'
;

flowDefinition:
	{start(flowDefinition__());} (
	occurrenceDefinitionPrefix flowDefKeyword definition
	) {end(_localctx);}
;

messageKeyword:
	'message'
;

message:
	{start(flowUsage__());} (
	occurrenceUsagePrefix messageKeyword {attachMessageUsage();} messageDeclaration definitionBody
	) {end(_localctx);}
;

messageDeclaration:
	usageDeclaration? valuePart? (
		'of' (payloadFeatureMember {add(element_OwnedRelationship());})
	)?
	 (
		'from' (messageEventMember {add(element_OwnedRelationship());}) 'to' (messageEventMember {add(element_OwnedRelationship());})
	)?
	| (messageEventMember {add(element_OwnedRelationship());}) 'to' (messageEventMember {add(element_OwnedRelationship());})
;

messageEventMember:
	{start(parameterMembership__());} (
	(messageEvent {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

messageEvent:
	{start(eventOccurrenceUsage__());} (
	(ownedReferenceSubsetting {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

flowUsage:
	{start(flowUsage__());} (
	occurrenceUsagePrefix flowKeyword flowDeclaration definitionBody
	) {end(_localctx);}
;

successionFlowKeyword:
	successionKeyword flowKeyword
;

successionFlowUsage:
	{start(successionFlowUsage__());} (
	occurrenceUsagePrefix successionFlowKeyword flowDeclaration definitionBody
	) {end(_localctx);}
;

flowDeclaration:
	usageDeclaration? valuePart? (
		'of' (payloadFeatureMember {add(element_OwnedRelationship());})
	)?
	 (
		'from' {attachFlowFrom();} (flowEndMember {add(element_OwnedRelationship());}) 'to' (flowEndMember {add(element_OwnedRelationship());})
	)?
	| (flowEndMember {add(element_OwnedRelationship());}) 'to' (flowEndMember {add(element_OwnedRelationship());})
;

payloadFeatureMember:
	{start(featureMembership__());} (
	(payloadFeature {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

payloadFeature:
	{start(payloadFeature__());} (
	payload
	) {end(_localctx);}
;

payload:
	id=identification? part=payloadFeatureSpecializationPart {if ($id.ctx == null) attachPayloadTypingClause($part.start);} valuePart?
	| identification? valuePart
	| (ownedFeatureTyping {add(element_OwnedRelationship());}) (ownedMultiplicity {add(element_OwnedRelationship());})?
	| (ownedMultiplicity {add(element_OwnedRelationship());}) (ownedFeatureTyping {add(element_OwnedRelationship());})
;

payloadFeatureSpecializationPart:
	featureSpecialization+ multiplicityPart? {attachLateNonUnique();} featureSpecialization*
	| multiplicityPart featureSpecialization+
;

flowEndMember:
	{start(endFeatureMembership__());} (
	(flowEnd {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

flowEnd:
	{start(flowEnd__());} (
	(flowEndSubsetting {add(element_OwnedRelationship());})? (flowFeatureMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

flowEndSubsetting:
	{start(referenceSubsetting__());} (qualifiedName {setLink(referenceSubsetting_ReferencedFeature(), feature__());}) '.' {end(_localctx);}
	| {start(referenceSubsetting__());} (featureChainPrefix {add(relationship_OwnedRelatedElement());}) {end(_localctx);}
;

featureChainPrefix:
	{start(feature__());} (
	(
		(ownedFeatureChaining {add(element_OwnedRelationship());}) '.'
	)+
	 (ownedFeatureChaining {add(element_OwnedRelationship());}) '.'
	) {end(_localctx);}
;

flowFeatureMember:
	{start(featureMembership__());} (
	(flowFeature {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

flowFeature:
	{start(referenceUsage__());} (
	(flowRedefinition {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

flowRedefinition:
	{start(redefinition__());} (
	(qualifiedName {setLink(redefinition_RedefinedFeature(), feature__());})
	) {end(_localctx);}
;

actionKeyword:
	'action'
;

actionDefKeyword:
	actionKeyword 'def'
;

actionDefinition:
	{start(actionDefinition__());} (
	occurrenceDefinitionPrefix actionDefKeyword definitionDeclaration actionBody
	) {end(_localctx);}
;

actionBody:
	';'
	| '{' {attachBodyBlock();} actionBodyItem* {attachTrailingComments();} '}'
;

actionBodyItem:
	(import_ {add(element_OwnedRelationship());})
	| (aliasMember {add(element_OwnedRelationship());})
	| (definitionMember {add(element_OwnedRelationship());})
	| (variantUsageMember {add(element_OwnedRelationship());})
	| (nonOccurrenceUsageMember {add(element_OwnedRelationship());})
	| (emptySuccessionMember {add(element_OwnedRelationship());})? (structureUsageMember {add(element_OwnedRelationship());})
	| (initialNodeMember {add(element_OwnedRelationship());}) (targetSuccessionMember {add(element_OwnedRelationship());})*
	| (emptySuccessionMember {add(element_OwnedRelationship());})? ((behaviorUsageMember | actionNodeMember) {add(element_OwnedRelationship());}) (targetSuccessionMember {add(element_OwnedRelationship());})*
	| (guardedSuccessionMember {add(element_OwnedRelationship());})
;

initialNodeMember:
	{start(membership__());} (
	memberPrefix 'first' {attachFirstMembership();} (qualifiedName {setLink(membership_MemberElement(), feature__());}) relationshipBody
	) {end(_localctx);}
;

actionNodeMember:
	{start(featureMembership__());} (
	memberPrefix (actionNode {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

targetSuccessionMember:
	{start(featureMembership__());} (
	memberPrefix (actionTargetSuccession {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

guardedSuccessionMember:
	{start(featureMembership__());} (
	memberPrefix (guardedSuccession {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

actionUsageKeyword:
	actionKeyword
;

actionUsage:
	{start(actionUsage__());} (
	occurrenceUsagePrefix actionUsageKeyword actionUsageDeclaration actionBody
	) {end(_localctx);}
;

performActionUsage:
	{start(performActionUsage__());} (
	occurrenceUsagePrefix 'perform' performActionUsageDeclaration actionBody
	) {end(_localctx);}
;

performActionUsageDeclaration:
	(
		(ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecializationPart? | actionUsageKeyword {attachExplicitActionNodeKeyword();} usageDeclaration?
	) valuePart?
;

actionUsageDeclaration:
	usageDeclaration? valuePart?
;

actionNode:
	sendNode
	| acceptNode
	| assignmentNode
	| ifNode
	| whileLoopNode
	| forLoopNode
	| terminateNode
	| controlNode
;

actionNodeUsageDeclaration:
	actionUsageKeyword {attachExplicitActionNodeKeyword();} usageDeclaration?
;

actionNodePrefix:
	occurrenceUsagePrefix actionNodeUsageDeclaration?
;

acceptNode:
	{start(acceptActionUsage__());} (
	occurrenceUsagePrefix acceptNodeDeclaration actionBody
	) {attachFollowingComments(_localctx);} {end(_localctx);}
;

acceptNodeDeclaration:
	actionNodeUsageDeclaration? 'accept' acceptParameterPart
;

acceptParameterPart:
	(payloadParameterMember {add(element_OwnedRelationship());}) (
		'via' (nodeParameterMember {add(element_OwnedRelationship());})
	)?
;

payloadParameterMember:
	{start(parameterMembership__());} (
	(payloadParameter {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

payloadParameter:
	{start(referenceUsage__());} payload {end(_localctx);}
	| {start(referenceUsage__());} identification? payloadFeatureSpecializationPart? triggerValuePart {end(_localctx);}
;

triggerValuePart:
	(triggerFeatureValue {add(element_OwnedRelationship());})
;

triggerFeatureValue:
	{start(featureValue__());} (
	(triggerExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

triggerExpression:
	{start(triggerInvocationExpression__());} (timeTriggerKind {set(triggerInvocationExpression_Kind());}) (argumentMember {add(element_OwnedRelationship());}) {end(_localctx);}
	| {start(triggerInvocationExpression__());} (changeTriggerKind {set(triggerInvocationExpression_Kind());}) (argumentExpressionMember {add(element_OwnedRelationship());}) {end(_localctx);}
;

timeTriggerKind:
	'at' {pushValue(triggerKind_At());}
	| 'after' {pushValue(triggerKind_After());}
;

changeTriggerKind:
	'when' {pushValue(triggerKind_When());}
;

argumentExpressionMember:
	{start(parameterMembership__());} (
	(argumentExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

argumentExpression:
	{start(feature__());} (
	(argumentExpressionValue {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

argumentExpressionValue:
	{start(featureValue__());} (
	(ownedExpressionReference {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

sendNode:
	{start(sendActionUsage__());} (
	occurrenceUsagePrefix actionNodeUsageDeclaration? 'send' (
		actionBody | (
			(nodeParameterMember {add(element_OwnedRelationship());}) senderReceiverPart? | (emptyParameterMember {add(element_OwnedRelationship());}) senderReceiverPart
		) actionBody
	)
	) {end(_localctx);}
;

sendNodeDeclaration:
	actionNodeUsageDeclaration? 'send' (nodeParameterMember {add(element_OwnedRelationship());}) senderReceiverPart?
;

senderReceiverPart:
	'via' (nodeParameterMember {add(element_OwnedRelationship());}) (
		'to' (nodeParameterMember {add(element_OwnedRelationship());})
	)?
	| (emptyParameterMember {add(element_OwnedRelationship());}) 'to' (nodeParameterMember {add(element_OwnedRelationship());})
;

nodeParameterMember:
	{start(parameterMembership__());} (
	(nodeParameter {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

nodeParameter:
	{start(referenceUsage__());} (
	(featureBinding {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

featureBinding:
	{start(featureValue__());} (
	(ownedExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

assignmentNode:
	{start(assignmentActionUsage__());} (
	occurrenceUsagePrefix assignmentNodeDeclaration actionBody
	) {end(_localctx);}
;

assignmentNodeDeclaration:
	actionNodeUsageDeclaration? 'assign' (assignmentTargetMember {add(element_OwnedRelationship());}) (featureChainMember {add(element_OwnedRelationship());}) ':=' (nodeParameterMember {add(element_OwnedRelationship());})
;

assignmentTargetMember:
	{start(parameterMembership__());} (
	(targetParameter {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

targetParameter:
	{start(referenceUsage__());} (
	(
		(targetBinding {add(element_OwnedRelationship());}) '.'
	)?
	 (targetFeatureMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

targetFeatureMember:
	{start(featureMembership__());} (
	(targetFeature {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

targetFeature:
	{start(referenceUsage__());} (
	(targetAccessedFeatureMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

targetAccessedFeatureMember:
	{start(featureMembership__());} (
	(emptyUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

targetBinding:
	{start(featureValue__());} (
	(targetExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

targetExpression:
	baseExpression (
		(
			{startFeatureChainExpression();} '.' (featureChainMember {add(element_OwnedRelationship());}) {end(_localctx);}
		)?
		 (
			{startOperatorExpression();} ('[' {set(operatorExpression_Operator(), "[");}) (sequenceExpression {addOperand();}) ']' {end(_localctx);}
			| {startOperatorExpression();} '->' (referenceTyping {add(element_OwnedRelationship());}) (
				(expressionBodyMember {add(element_OwnedRelationship());})
				| (functionReferenceMember {add(element_OwnedRelationship());})
				| argumentList
			) {end(_localctx);}
			| {startCollectExpression();} '.' (expressionBodyMember {add(element_OwnedRelationship());}) {end(_localctx);}
			| {startSelectExpression();} '.?' (expressionBodyMember {add(element_OwnedRelationship());}) {end(_localctx);}
		)
	)*
;

expressionParameterMember:
	{start(parameterMembership__());} (
	(ownedExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

ifNode:
	{start(ifActionUsage__());} (
	actionNodePrefix 'if' (expressionParameterMember {add(element_OwnedRelationship());}) (actionBodyParameterMember {add(element_OwnedRelationship());}) (
		'else' ((actionBodyParameterMember | ifNodeParameterMember) {add(element_OwnedRelationship());})
	)?
	) {end(_localctx);}
;

actionBodyParameterMember:
	{start(parameterMembership__());} (
	(actionBodyParameter {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

actionBodyParameter:
	{start(actionUsage__());} (
	(
		actionUsageKeyword {attachExplicitActionNodeKeyword();} usageDeclaration?
	)?
	 '{' actionBodyItem* '}'
	) {end(_localctx);}
;

ifNodeParameterMember:
	{start(parameterMembership__());} (
	(ifNode {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

whileLoopNode:
	{start(whileLoopActionUsage__());} (
	actionNodePrefix (
		'while' (expressionParameterMember {add(element_OwnedRelationship());}) | 'loop' (emptyParameterMember {add(element_OwnedRelationship());})
	) (actionBodyParameterMember {add(element_OwnedRelationship());}) (
		'until' (expressionParameterMember {add(element_OwnedRelationship());}) ';'
	)?
	) {end(_localctx);}
;

forLoopNode:
	{start(forLoopActionUsage__());} (
	actionNodePrefix 'for' (forVariableDeclarationMember {add(element_OwnedRelationship());}) 'in' (nodeParameterMember {add(element_OwnedRelationship());}) (actionBodyParameterMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

forVariableDeclarationMember:
	{start(featureMembership__());} (
	(forVariableDeclaration {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

forVariableDeclaration:
	{start(referenceUsage__());} (
	usageDeclaration
	) {end(_localctx);}
;

terminateNode:
	{start(terminateActionUsage__());} (
	occurrenceUsagePrefix actionNodeUsageDeclaration? 'terminate' (
		actionBody | (nodeParameterMember {add(element_OwnedRelationship());}) actionBody
	)
	) {end(_localctx);}
;

controlNode:
	mergeNode
	| decisionNode
	| joinNode
	| forkNode
;

controlNodePrefix:
	refPrefix ('individual' {setTrue(occurrenceUsage_IsIndividual());})? (portionKind {set(occurrenceUsage_PortionKind());})? usageExtensionKeyword*
;

mergeNode:
	{start(mergeNode__());} (
	controlNodePrefix ('merge' {setTrue(feature_IsComposite());}) usageDeclaration? actionBody
	) {end(_localctx);}
;

decisionNode:
	{start(decisionNode__());} (
	controlNodePrefix ('decide' {setTrue(feature_IsComposite());}) usageDeclaration? actionBody
	) {end(_localctx);}
;

joinNode:
	{start(joinNode__());} (
	controlNodePrefix ('join' {setTrue(feature_IsComposite());}) usageDeclaration? actionBody
	) {end(_localctx);}
;

forkNode:
	{start(forkNode__());} (
	controlNodePrefix ('fork' {setTrue(feature_IsComposite());}) usageDeclaration? actionBody
	) {end(_localctx);}
;

emptyParameterMember:
	{start(parameterMembership__());} (
	(emptyUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

emptyUsage:
	{start(referenceUsage__());} (
	
	) {end(_localctx);}
;

actionTargetSuccession:
	(
		targetSuccession | guardedTargetSuccession | defaultTargetSuccession
	) {restart();} usageBody {end(_localctx);}
;

targetSuccession:
	{start(successionAsUsage__());} (
	(multiplicitySourceEndMember {add(element_OwnedRelationship());}) 'then' (connectorEndMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

guardedTargetSuccession:
	{start(transitionUsage__());} (
	(emptyParameterMember {add(element_OwnedRelationship());}) (guardExpressionMember {add(element_OwnedRelationship());}) 'then' (transitionSuccessionMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

defaultTargetSuccession:
	{start(transitionUsage__());} (
	(emptyParameterMember {add(element_OwnedRelationship());}) 'else' {attachDefaultTargetSuccession();} (transitionSuccessionMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

guardedSuccession:
	{start(transitionUsage__());} (
	(
		successionKeyword {attachSuccessionKeyword();} usageDeclaration
	)?
	 'first' (transitionSourceMember {add(element_OwnedRelationship());}) (emptyParameterMember {add(element_OwnedRelationship());}) (guardExpressionMember {add(element_OwnedRelationship());}) 'then' (transitionSuccessionMember {add(element_OwnedRelationship());}) usageBody
	) {end(_localctx);}
;

stateKeyword:
	'state'
;

stateDefKeyword:
	stateKeyword 'def'
;

stateDefinition:
	{start(stateDefinition__());} (
	occurrenceDefinitionPrefix stateDefKeyword definitionDeclaration stateDefBody
	) {end(_localctx);}
;

stateDefBody:
	';'
	| ('parallel' {setTrue(stateDefinition_IsParallel());})? '{' {attachBodyBlock();} stateBodyPart {attachTrailingComments();} '}'
;

stateBodyPart:
	stateBodyItem*
;

stateBodyItem:
	(import_ {add(element_OwnedRelationship());})
	| (aliasMember {add(element_OwnedRelationship());})
	| (definitionMember {add(element_OwnedRelationship());})
	| (variantUsageMember {add(element_OwnedRelationship());})
	| (nonOccurrenceUsageMember {add(element_OwnedRelationship());})
	| (emptySuccessionMember {add(element_OwnedRelationship());})? (structureUsageMember {add(element_OwnedRelationship());})
	| (emptySuccessionMember {add(element_OwnedRelationship());})? (behaviorUsageMember {add(element_OwnedRelationship());}) (targetTransitionUsageMember {add(element_OwnedRelationship());})*
	| (transitionUsageMember {add(element_OwnedRelationship());})
	| (entryActionMember {add(element_OwnedRelationship());}) (entryTransitionMember {add(element_OwnedRelationship());})*
	| (doActionMember {add(element_OwnedRelationship());})
	| (exitActionMember {add(element_OwnedRelationship());})
;

entryActionMember:
	{start(stateSubactionMembership__());} (
	memberPrefix (entryActionKind {set(stateSubactionMembership_Kind());}) (stateActionUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

entryActionKind:
	'entry' {pushValue(stateSubactionKind_Entry());}
;

doActionMember:
	{start(stateSubactionMembership__());} (
	memberPrefix (doActionKind {set(stateSubactionMembership_Kind());}) (stateActionUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

doActionKind:
	'do' {pushValue(stateSubactionKind_Do());}
;

exitActionMember:
	{start(stateSubactionMembership__());} (
	memberPrefix (exitActionKind {set(stateSubactionMembership_Kind());}) (stateActionUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

exitActionKind:
	'exit' {pushValue(stateSubactionKind_Exit());}
;

entryTransitionMember:
	{start(featureMembership__());} (
	memberPrefix (
		(guardedTargetSuccession {add(relationship_OwnedRelatedElement());}) | 'then' (transitionSuccession {add(relationship_OwnedRelatedElement());})
	) ';'
	) {end(_localctx);}
;

stateActionUsage:
	emptyActionUsage ';'
	| performedActionUsage {restart();} {attachTrailingComments();} actionBody {end(_localctx);}
;

emptyActionUsage:
	{start(actionUsage__());} (
	
	) {end(_localctx);}
;

performedActionUsage:
	{start(performActionUsage__());} performActionUsageDeclaration {end(_localctx);}
	| {start(acceptActionUsage__());} acceptNodeDeclaration {end(_localctx);}
	| {start(sendActionUsage__());} sendNodeDeclaration {end(_localctx);}
	| {start(assignmentActionUsage__());} assignmentNodeDeclaration {end(_localctx);}
;

transitionUsageMember:
	{start(featureMembership__());} (
	memberPrefix (transitionUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

targetTransitionUsageMember:
	{start(featureMembership__());} (
	memberPrefix (targetTransitionUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

stateUsageKeyword:
	stateKeyword
;

stateUsage:
	{start(stateUsage__());} (
	occurrenceUsagePrefix stateUsageKeyword actionUsageDeclaration stateUsageBody
	) {end(_localctx);}
;

stateUsageBody:
	';'
	| ('parallel' {setTrue(stateUsage_IsParallel());})? '{' {attachBodyBlock();} stateBodyPart {attachTrailingComments();} '}'
;

exhibitStateUsage:
	{start(exhibitStateUsage__());} (
	occurrenceUsagePrefix 'exhibit' (
		(ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecializationPart? | stateUsageKeyword {attachExplicitStateUsageKeyword();} usageDeclaration?
	) valuePart? stateUsageBody
	) {end(_localctx);}
;

transitionUsageKeyword:
	'transition'
;

transitionUsage:
	{start(transitionUsage__());} (
	transitionUsageKeyword {attachExplicitTransitionKeyword();} (
		usageDeclaration? first='first'
	)?
	 (transitionSourceMember {add(element_OwnedRelationship()); if ($first == null) attachTransitionFirstKeywordOmitted();}) (emptyParameterMember {add(element_OwnedRelationship());}) (
		(emptyParameterMember {add(element_OwnedRelationship());}) (triggerActionMember {add(element_OwnedRelationship());})
	)?
	 (guardExpressionMember {add(element_OwnedRelationship());})? (effectBehaviorMember {add(element_OwnedRelationship());})? 'then' (transitionSuccessionMember {add(element_OwnedRelationship());}) actionBody
	) {end(_localctx);}
;

targetTransitionUsage:
	{start(transitionUsage__());} (
	(emptyParameterMember {add(element_OwnedRelationship());}) (
		transitionUsageKeyword {attachExplicitTransitionKeyword();} (
			(emptyParameterMember {add(element_OwnedRelationship());}) (triggerActionMember {add(element_OwnedRelationship());})
		)?
		 (guardExpressionMember {add(element_OwnedRelationship());})? (effectBehaviorMember {add(element_OwnedRelationship());})?
		 | (emptyParameterMember {add(element_OwnedRelationship());}) (triggerActionMember {add(element_OwnedRelationship());}) (guardExpressionMember {add(element_OwnedRelationship());})? (effectBehaviorMember {add(element_OwnedRelationship());})?
		 // this alternative can't be reached because of the first alternative
		 // | (guardExpressionMember {add(element_OwnedRelationship());}) (effectBehaviorMember {add(element_OwnedRelationship());})?
	)? 'then' (transitionSuccessionMember {add(element_OwnedRelationship());}) actionBody
	) {end(_localctx);}
;

transitionSourceMember:
	{start(membership__());} (qualifiedName {setLink(membership_MemberElement(), feature__());}) {end(_localctx);}
	| {start(owningMembership__());} (ownedFeatureChain {add(relationship_OwnedRelatedElement());}) {end(_localctx);}
;

triggerActionMember:
	{start(transitionFeatureMembership__());} (
	(triggerFeatureKind {set(transitionFeatureMembership_Kind());}) (triggerAction {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

triggerFeatureKind:
	'accept' {pushValue(transitionFeatureKind_Trigger());}
;

triggerAction:
	{start(acceptActionUsage__());} (
	part=acceptParameterPart
	) {attachFollowingComments($part.ctx);} {end(_localctx);}
;

guardExpressionMember:
	{start(transitionFeatureMembership__());} (
	(guardFeatureKind {set(transitionFeatureMembership_Kind());}) (ownedExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

guardFeatureKind:
	'if' {pushValue(transitionFeatureKind_Guard());}
;

effectBehaviorMember:
	{start(transitionFeatureMembership__());} (
	(effectFeatureKind {set(transitionFeatureMembership_Kind());}) (effectBehaviorUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

effectFeatureKind:
	'do' {pushValue(transitionFeatureKind_Effect());}
;

effectBehaviorUsage:
	emptyActionUsage
	| performedActionUsage (
		{restart();} '{' {attachBodyBlock();} actionBodyItem* '}' {end(_localctx);}
	)? {attachFollowingComments(_localctx);}
;

transitionSuccessionMember:
	{start(owningMembership__());} (
	(transitionSuccession {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

transitionSuccession:
	{start(successionAsUsage__());} (
	(emptySourceEndMember {add(element_OwnedRelationship());}) (connectorEndMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

calculationKeyword:
	'calc'
;

calculationDefKeyword:
	calculationKeyword 'def'
;

calculationDefinition:
	{start(calculationDefinition__());} (
	occurrenceDefinitionPrefix calculationDefKeyword definitionDeclaration calculationBody
	) {end(_localctx);}
;

calculationBody:
	';'
	| '{' {attachBodyBlock();} calculationBodyPart {attachTrailingComments();} '}'
;

calculationBodyPart:
	calculationBodyItem* (resultExpressionMember {add(element_OwnedRelationship());})?
;

calculationBodyItem:
	actionBodyItem
	| (returnParameterMember {add(element_OwnedRelationship());})
;

returnParameterMember:
	{start(returnParameterMembership__());} (
	memberPrefix 'return' (usageElement {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

resultExpressionMember:
	{start(resultExpressionMembership__());} (
	memberPrefix (ownedExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

calculationUsageKeyword:
	calculationKeyword
;

calculationUsage:
	{start(calculationUsage__());} (
	occurrenceUsagePrefix calculationUsageKeyword actionUsageDeclaration calculationBody
	) {end(_localctx);}
;

constraintKeyword:
	'constraint'
;

constraintDefKeyword:
	constraintKeyword 'def'
;

constraintDefinition:
	{start(constraintDefinition__());} (
	occurrenceDefinitionPrefix constraintDefKeyword definitionDeclaration calculationBody
	) {end(_localctx);}
;

constraintUsageKeyword:
	constraintKeyword
;

constraintUsage:
	{start(constraintUsage__());} (
	occurrenceUsagePrefix constraintUsageKeyword constraintUsageDeclaration calculationBody
	) {end(_localctx);}
;

assertConstraintUsage:
	{start(assertConstraintUsage__());} (
	occurrenceUsagePrefix 'assert' ('not' {setTrue(invariant_IsNegated());})? (
		{attachDeclarationKeywordOmitted();} (ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecializationPart? | constraintUsageKeyword constraintUsageDeclaration
	) calculationBody
	) {end(_localctx);}
;

constraintUsageDeclaration:
	usageDeclaration? valuePart?
;

requirementKeyword:
	'requirement'
;

requirementDefKeyword:
	requirementKeyword 'def'
;

requirementDefinition:
	{start(requirementDefinition__());} (
	occurrenceDefinitionPrefix requirementDefKeyword definitionDeclaration requirementBody
	) {end(_localctx);}
;

requirementBody:
	';'
	| '{' {attachBodyBlock();} requirementBodyItem* {attachTrailingComments();} '}'
;

requirementBodyItem:
	definitionBodyItem
	| (subjectMember {add(element_OwnedRelationship());})
	| (requirementConstraintMember {add(element_OwnedRelationship());})
	| (framedConcernMember {add(element_OwnedRelationship());})
	| (requirementVerificationMember {add(element_OwnedRelationship());})
	| (actorMember {add(element_OwnedRelationship());})
	| (stakeholderMember {add(element_OwnedRelationship());})
;

subjectMember:
	{start(subjectMembership__());} (
	memberPrefix (subject=subjectUsage {add(relationship_OwnedRelatedElement()); attachFollowingComments($subject.ctx);})
	) {end(_localctx);}
;

subjectUsage:
	{start(referenceUsage__());} (
	'subject' usageExtensionKeyword* usage
	) {end(_localctx);}
;

requirementConstraintMember:
	{start(requirementConstraintMembership__());} (
	memberPrefix (requirementConstraintKind {set(requirementConstraintMembership_Kind());}) (requirementConstraintUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

requirementConstraintKind:
	'assume' {pushValue(requirementConstraintKind_Assumption());}
	| 'require' {pushValue(requirementConstraintKind_Requirement());}
;

requirementConstraintUsage:
	{start(constraintUsage__());} (ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecializationPart? calculationBody {end(_localctx);}
	| {start(constraintUsage__());} (
		usageExtensionKeyword* constraintUsageKeyword {attachRequirementConstraintKeyword();}
		| usageExtensionKeyword+ {attachDeclarationKeywordOmitted();}
	) constraintUsageDeclaration calculationBody {end(_localctx);}
;

framedConcernMember:
	{start(framedConcernMembership__());} (
	memberPrefix (framedConcernKind {set(requirementConstraintMembership_Kind());}) (framedConcernUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

framedConcernKind:
	'frame' {pushValue(requirementConstraintKind_Requirement());}
;

framedConcernUsage:
	{start(concernUsage__());} (reference=ownedReferenceSubsetting {add(element_OwnedRelationship()); if ($reference.quoted) attachQuotedName();}) featureSpecializationPart? requirementBody {end(_localctx);}
	| {start(concernUsage__());} (
		usageExtensionKeyword* concernUsageKeyword
		| usageExtensionKeyword+ {attachDeclarationKeywordOmitted();}
	) constraintUsageDeclaration requirementBody {end(_localctx);}
;

actorMember:
	{start(actorMembership__());} (
	memberPrefix (actor=actorUsage {add(relationship_OwnedRelatedElement()); attachFollowingComments($actor.ctx);})
	) {end(_localctx);}
;

actorUsage:
	{start(partUsage__());} (
	'actor' usageExtensionKeyword* usage
	) {end(_localctx);}
;

stakeholderMember:
	{start(stakeholderMembership__());} (
	memberPrefix (stakeholder=stakeholderUsage {add(relationship_OwnedRelatedElement()); attachFollowingComments($stakeholder.ctx);})
	) {end(_localctx);}
;

stakeholderUsage:
	{start(partUsage__());} (
	'stakeholder' usageExtensionKeyword* usage
	) {end(_localctx);}
;

requirementUsageKeyword:
	requirementKeyword
;

requirementUsage:
	{start(requirementUsage__());} (
	occurrenceUsagePrefix requirementUsageKeyword constraintUsageDeclaration requirementBody
	) {end(_localctx);}
;

satisfyRequirementUsage:
	{start(satisfyRequirementUsage__());} (
	occurrenceUsagePrefix ('assert' {attachSatisfyAssert();})? ('not' {setTrue(invariant_IsNegated());})? 'satisfy' (
		{attachDeclarationKeywordOmitted();} (ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecializationPart?
		| requirementUsageKeyword usageDeclaration?
	) valuePart? (
		'by' (satisfactionSubjectMember {add(element_OwnedRelationship());})
	)?
	 requirementBody
	) {end(_localctx);}
;

satisfactionSubjectMember:
	{start(subjectMembership__());} (
	(satisfactionParameter {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

satisfactionParameter:
	{start(referenceUsage__());} (
	(satisfactionFeatureValue {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

satisfactionFeatureValue:
	{start(featureValue__());} (
	(satisfactionReferenceExpression {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

satisfactionReferenceExpression:
	{start(featureReferenceExpression__());} (
	(featureChainMember {add(element_OwnedRelationship());})
	) {end(_localctx);}
;

concernKeyword:
	'concern'
;

concernDefKeyword:
	concernKeyword 'def'
;

concernDefinition:
	{start(concernDefinition__());} (
	occurrenceDefinitionPrefix concernDefKeyword definitionDeclaration requirementBody
	) {end(_localctx);}
;

concernUsageKeyword:
	concernKeyword
;

concernUsage:
	{start(concernUsage__());} (
	occurrenceUsagePrefix concernUsageKeyword constraintUsageDeclaration requirementBody
	) {end(_localctx);}
;

caseKeyword:
	'case'
;

caseDefKeyword:
	caseKeyword 'def'
;

caseDefinition:
	{start(caseDefinition__());} (
	occurrenceDefinitionPrefix caseDefKeyword definitionDeclaration caseBody
	) {end(_localctx);}
;

caseBody:
	';'
	| '{' {attachBodyBlock();} caseBodyItem* (resultExpressionMember {add(element_OwnedRelationship());})? '}'
;

caseBodyItem:
	calculationBodyItem
	| (subjectMember {add(element_OwnedRelationship());})
	| (actorMember {add(element_OwnedRelationship());})
	| (objectiveMember {add(element_OwnedRelationship());})
;

objectiveMember:
	{start(objectiveMembership__());} (
	memberPrefix 'objective' (objective=objectiveRequirementUsage {add(relationship_OwnedRelatedElement()); attachFollowingComments($objective.ctx);})
	) {end(_localctx);}
;

objectiveRequirementUsage:
	{start(requirementUsage__());} (
	usageExtensionKeyword* constraintUsageDeclaration requirementBody
	) {end(_localctx);}
;

caseUsageKeyword:
	caseKeyword
;

caseUsage:
	{start(caseUsage__());} (
	occurrenceUsagePrefix caseUsageKeyword actionUsageDeclaration caseBody
	) {end(_localctx);}
;

analysisCaseKeyword:
	'analysis'
;

analysisCaseDefKeyword:
	analysisCaseKeyword 'def'
;

analysisCaseUsageKeyword:
	analysisCaseKeyword
;

analysisCaseDefinition:
	{start(analysisCaseDefinition__());} (
	occurrenceDefinitionPrefix analysisCaseDefKeyword definitionDeclaration caseBody
	) {end(_localctx);}
;

analysisCaseUsage:
	{start(analysisCaseUsage__());} (
	occurrenceUsagePrefix analysisCaseUsageKeyword actionUsageDeclaration caseBody
	) {end(_localctx);}
;

verificationCaseKeyword:
	'verification'
;

verificationCaseDefKeyword:
	verificationCaseKeyword 'def'
;

verificationCaseUsageKeyword:
	verificationCaseKeyword
;

verificationCaseDefinition:
	{start(verificationCaseDefinition__());} (
	occurrenceDefinitionPrefix verificationCaseDefKeyword definitionDeclaration caseBody
	) {end(_localctx);}
;

verificationCaseUsage:
	{start(verificationCaseUsage__());} (
	occurrenceUsagePrefix verificationCaseUsageKeyword actionUsageDeclaration caseBody
	) {end(_localctx);}
;

requirementVerificationMember:
	{start(requirementVerificationMembership__());} (
	memberPrefix (requirementVerificationKind {set(requirementConstraintMembership_Kind());}) (requirementVerificationUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

requirementVerificationKind:
	'verify' {pushValue(requirementConstraintKind_Requirement());}
;

requirementVerificationUsage:
	{start(requirementUsage__()); attachDeclarationKeywordOmitted();} (ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecialization* requirementBody {end(_localctx);}
	| {start(requirementUsage__());} (
		usageExtensionKeyword* requirementUsageKeyword | usageExtensionKeyword+ {attachDeclarationKeywordOmitted();}
	) constraintUsageDeclaration requirementBody {end(_localctx);}
;

useCaseKeyword:
	'use' 'case'
;

useCaseDefKeyword:
	useCaseKeyword 'def'
;

useCaseUsageKeyword:
	useCaseKeyword
;

useCaseDefinition:
	{start(useCaseDefinition__());} (
	occurrenceDefinitionPrefix useCaseDefKeyword definitionDeclaration caseBody
	) {end(_localctx);}
;

useCaseUsage:
	{start(useCaseUsage__());} (
	occurrenceUsagePrefix useCaseUsageKeyword actionUsageDeclaration caseBody
	) {end(_localctx);}
;

includeUseCaseUsage:
	{start(includeUseCaseUsage__());} (
	occurrenceUsagePrefix 'include' (
		(ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecializationPart? | useCaseUsageKeyword usageDeclaration?
	) valuePart? caseBody
	) {end(_localctx);}
;

viewKeyword:
	'view'
;

viewDefKeyword:
	viewKeyword 'def'
;

viewDefinition:
	{start(viewDefinition__());} (
	occurrenceDefinitionPrefix viewDefKeyword definitionDeclaration viewDefinitionBody
	) {end(_localctx);}
;

viewDefinitionBody:
	';'
	| '{' {attachBodyBlock();} viewDefinitionBodyItem* '}'
;

viewDefinitionBodyItem:
	definitionBodyItem
	| (elementFilterMember {add(element_OwnedRelationship());})
	| (viewRenderingMember {add(element_OwnedRelationship());})
;

viewRenderingMember:
	{start(viewRenderingMembership__());} (
	memberPrefix 'render' (viewRenderingUsage {add(relationship_OwnedRelatedElement());})
	) {end(_localctx);}
;

viewRenderingUsage:
	{start(renderingUsage__()); attachDeclarationKeywordOmitted();} (ownedReferenceSubsetting {add(element_OwnedRelationship());}) featureSpecializationPart? usageBody {end(_localctx);}
	| {start(renderingUsage__());} (
		usageExtensionKeyword* 'rendering' | usageExtensionKeyword+ {attachDeclarationKeywordOmitted();}
	) usage {end(_localctx);}
;

viewUsageKeyword:
	viewKeyword
;

viewUsage:
	{start(viewUsage__());} (
	occurrenceUsagePrefix viewUsageKeyword usageDeclaration? valuePart? viewBody
	) {end(_localctx);}
;

viewBody:
	';'
	| '{' {attachBodyBlock();} viewBodyItem* {attachTrailingComments();} '}'
;

viewBodyItem:
	definitionBodyItem
	| (elementFilterMember {add(element_OwnedRelationship());})
	| (expose {add(element_OwnedRelationship());})
	| (viewRenderingMember {add(element_OwnedRelationship());})
;

exposePrefix:
	(exposeVisibilityKind {set(import_Visibility());})
;

exposeVisibilityKind:
	'expose' {pushValue(visibilityKind_Protected());}
;

expose:
	(
		membershipExpose | namespaceExpose
	) {restart();} relationshipBody {end(_localctx);}
;

membershipExpose:
	{start(membershipExpose__());} (
	exposePrefix importedMembership
	) {end(_localctx);}
;

namespaceExpose:
	{start(namespaceExpose__());} (
	exposePrefix (
		importedNamespace | (filterPackage {add(relationship_OwnedRelatedElement());})
	)
	) {end(_localctx);}
;

viewpointKeyword:
	'viewpoint'
;

viewpointDefKeyword:
	viewpointKeyword 'def'
;

viewpointUsageKeyword:
	viewpointKeyword
;

viewpointDefinition:
	{start(viewpointDefinition__());} (
	occurrenceDefinitionPrefix viewpointDefKeyword definitionDeclaration requirementBody
	) {end(_localctx);}
;

viewpointUsage:
	{start(viewpointUsage__());} (
	occurrenceUsagePrefix viewpointUsageKeyword constraintUsageDeclaration requirementBody
	) {end(_localctx);}
;

renderingKeyword:
	'rendering'
;

renderingDefKeyword:
	renderingKeyword 'def'
;

renderingDefinition:
	{start(renderingDefinition__());} (
	occurrenceDefinitionPrefix renderingDefKeyword definition
	) {end(_localctx);}
;

renderingUsageKeyword:
	renderingKeyword
;

renderingUsage:
	{start(renderingUsage__());} (
	occurrenceUsagePrefix renderingUsageKeyword usage
	) {end(_localctx);}
;

expressionBody:
	{start(expression__());} (
	calculationBody
	) {end(_localctx);}
;
