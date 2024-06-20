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
package org.eclipse.syson.sysml.impl;

import static org.eclipse.syson.sysml.SysmlPackage.CLASS;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.syson.sysml.*;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class SysmlPackageImpl extends EPackageImpl implements SysmlPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass acceptActionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass actionDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass actionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass actorMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass allocationDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass allocationUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass analysisCaseDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass analysisCaseUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass annotatingElementEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass annotationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass assertConstraintUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass assignmentActionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass associationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass associationStructureEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass attributeDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass attributeUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass behaviorEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass bindingConnectorEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass bindingConnectorAsUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass booleanExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass calculationDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass calculationUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass caseDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass caseUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass classEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass classifierEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass collectExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass commentEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass concernDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass concernUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass conjugatedPortDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass conjugatedPortTypingEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass conjugationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass connectionDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass connectionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass connectorEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass connectorAsUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass constraintDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass constraintUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass controlNodeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass dataTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass decisionNodeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass definitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass dependencyEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass differencingEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass disjoiningEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass documentationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass elementEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass elementFilterMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endFeatureMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass enumerationDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass enumerationUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass eventOccurrenceUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass exhibitStateUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass exposeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass expressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass featureEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass featureChainExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass featureChainingEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass featureInvertingEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass featureMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass featureReferenceExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass featureTypingEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass featureValueEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass featuringEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass flowConnectionDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass flowConnectionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass forkNodeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass forLoopActionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass framedConcernMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass functionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass ifActionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass importEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass includeUseCaseUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass interactionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass interfaceDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass interfaceUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass intersectingEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass invariantEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass invocationExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass itemDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass itemFeatureEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass itemFlowEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass itemFlowEndEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass itemUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass joinNodeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass libraryPackageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass lifeClassEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass literalBooleanEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass literalExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass literalInfinityEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass literalIntegerEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass literalRationalEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass literalStringEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass loopActionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass membershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass membershipExposeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass membershipImportEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass mergeNodeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass metaclassEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass metadataAccessExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass metadataDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass metadataFeatureEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass metadataUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass multiplicityEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass multiplicityRangeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass namespaceEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass namespaceExposeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass namespaceImportEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass nullExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass objectiveMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass occurrenceDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass occurrenceUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass operatorExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass owningMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass packageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass parameterMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass partDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass partUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass performActionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass portConjugationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass portDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass portUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass predicateEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass redefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass referenceSubsettingEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass referenceUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass relationshipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass renderingDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass renderingUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass requirementConstraintMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass requirementDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass requirementUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass requirementVerificationMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass resultExpressionMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass returnParameterMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass satisfyRequirementUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass selectExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass sendActionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass specializationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass stakeholderMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass stateDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass stateSubactionMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass stateUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass stepEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass structureEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass subclassificationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass subjectMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass subsettingEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass successionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass successionAsUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass successionFlowConnectionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass successionItemFlowEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass textualRepresentationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass transitionFeatureMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass transitionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass triggerInvocationExpressionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass typeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass typeFeaturingEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass unioningEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass usageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass useCaseDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass useCaseUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass variantMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass verificationCaseDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass verificationCaseUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass viewDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass viewpointDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass viewpointUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass viewRenderingMembershipEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass viewUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass whileLoopActionUsageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EEnum featureDirectionKindEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EEnum portionKindEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EEnum requirementConstraintKindEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EEnum stateSubactionKindEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EEnum transitionFeatureKindEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EEnum triggerKindEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EEnum visibilityKindEEnum = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry
     * EPackage.Registry} by the package package URI value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also
     * performs initialization of the package, or returns the registered package, if one already exists. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.eclipse.syson.sysml.SysmlPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private SysmlPackageImpl() {
        super(eNS_URI, SysmlFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     *
     * <p>
     * This method is used to initialize {@link SysmlPackage#eINSTANCE} when that field is accessed. Clients should not
     * invoke it directly. Instead, they should simply access that field to obtain the package. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static SysmlPackage init() {
        if (isInited)
            return (SysmlPackage) EPackage.Registry.INSTANCE.getEPackage(SysmlPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredSysmlPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        SysmlPackageImpl theSysmlPackage = registeredSysmlPackage instanceof SysmlPackageImpl ? (SysmlPackageImpl) registeredSysmlPackage : new SysmlPackageImpl();

        isInited = true;

        // Create package meta-data objects
        theSysmlPackage.createPackageContents();

        // Initialize created meta-data
        theSysmlPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theSysmlPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(SysmlPackage.eNS_URI, theSysmlPackage);
        return theSysmlPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAcceptActionUsage() {
        return this.acceptActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAcceptActionUsage_PayloadArgument() {
        return (EReference) this.acceptActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAcceptActionUsage_PayloadParameter() {
        return (EReference) this.acceptActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAcceptActionUsage_ReceiverArgument() {
        return (EReference) this.acceptActionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getAcceptActionUsage__IsTriggerAction() {
        return this.acceptActionUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getActionDefinition() {
        return this.actionDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActionDefinition_Action() {
        return (EReference) this.actionDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getActionUsage() {
        return this.actionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActionUsage_ActionDefinition() {
        return (EReference) this.actionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getActionUsage__Argument__int() {
        return this.actionUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getActionUsage__InputParameter__int() {
        return this.actionUsageEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getActionUsage__InputParameters() {
        return this.actionUsageEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getActionUsage__IsSubactionUsage() {
        return this.actionUsageEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getActorMembership() {
        return this.actorMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActorMembership_OwnedActorParameter() {
        return (EReference) this.actorMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAllocationDefinition() {
        return this.allocationDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAllocationDefinition_Allocation() {
        return (EReference) this.allocationDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAllocationUsage() {
        return this.allocationUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAllocationUsage_AllocationDefinition() {
        return (EReference) this.allocationUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAnalysisCaseDefinition() {
        return this.analysisCaseDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnalysisCaseDefinition_AnalysisAction() {
        return (EReference) this.analysisCaseDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnalysisCaseDefinition_ResultExpression() {
        return (EReference) this.analysisCaseDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAnalysisCaseUsage() {
        return this.analysisCaseUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnalysisCaseUsage_AnalysisAction() {
        return (EReference) this.analysisCaseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnalysisCaseUsage_AnalysisCaseDefinition() {
        return (EReference) this.analysisCaseUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnalysisCaseUsage_ResultExpression() {
        return (EReference) this.analysisCaseUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAnnotatingElement() {
        return this.annotatingElementEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnnotatingElement_AnnotatedElement() {
        return (EReference) this.annotatingElementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnnotatingElement_Annotation() {
        return (EReference) this.annotatingElementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAnnotation() {
        return this.annotationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnnotation_AnnotatedElement() {
        return (EReference) this.annotationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnnotation_AnnotatingElement() {
        return (EReference) this.annotationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAnnotation_OwningAnnotatedElement() {
        return (EReference) this.annotationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAssertConstraintUsage() {
        return this.assertConstraintUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssertConstraintUsage_AssertedConstraint() {
        return (EReference) this.assertConstraintUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAssignmentActionUsage() {
        return this.assignmentActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssignmentActionUsage_Referent() {
        return (EReference) this.assignmentActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssignmentActionUsage_TargetArgument() {
        return (EReference) this.assignmentActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssignmentActionUsage_ValueExpression() {
        return (EReference) this.assignmentActionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAssociation() {
        return this.associationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssociation_AssociationEnd() {
        return (EReference) this.associationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssociation_RelatedType() {
        return (EReference) this.associationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssociation_SourceType() {
        return (EReference) this.associationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssociation_TargetType() {
        return (EReference) this.associationEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAssociationStructure() {
        return this.associationStructureEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAttributeDefinition() {
        return this.attributeDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAttributeUsage() {
        return this.attributeUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAttributeUsage_AttributeDefinition() {
        return (EReference) this.attributeUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getBehavior() {
        return this.behaviorEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getBehavior_Parameter() {
        return (EReference) this.behaviorEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getBehavior_Step() {
        return (EReference) this.behaviorEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getBindingConnector() {
        return this.bindingConnectorEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getBindingConnectorAsUsage() {
        return this.bindingConnectorAsUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getBooleanExpression() {
        return this.booleanExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getBooleanExpression_Predicate() {
        return (EReference) this.booleanExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCalculationDefinition() {
        return this.calculationDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCalculationDefinition_Calculation() {
        return (EReference) this.calculationDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCalculationUsage() {
        return this.calculationUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCalculationUsage_CalculationDefinition() {
        return (EReference) this.calculationUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCaseDefinition() {
        return this.caseDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseDefinition_ActorParameter() {
        return (EReference) this.caseDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseDefinition_ObjectiveRequirement() {
        return (EReference) this.caseDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseDefinition_SubjectParameter() {
        return (EReference) this.caseDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCaseUsage() {
        return this.caseUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseUsage_ActorParameter() {
        return (EReference) this.caseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseUsage_CaseDefinition() {
        return (EReference) this.caseUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseUsage_ObjectiveRequirement() {
        return (EReference) this.caseUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseUsage_SubjectParameter() {
        return (EReference) this.caseUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getClass_() {
        return this.classEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getClassifier() {
        return this.classifierEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getClassifier_OwnedSubclassification() {
        return (EReference) this.classifierEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCollectExpression() {
        return this.collectExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getComment() {
        return this.commentEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getComment_Body() {
        return (EAttribute) this.commentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getComment_Locale() {
        return (EAttribute) this.commentEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConcernDefinition() {
        return this.concernDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConcernUsage() {
        return this.concernUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConcernUsage_ConcernDefinition() {
        return (EReference) this.concernUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConjugatedPortDefinition() {
        return this.conjugatedPortDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConjugatedPortDefinition_OriginalPortDefinition() {
        return (EReference) this.conjugatedPortDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConjugatedPortDefinition_OwnedPortConjugator() {
        return (EReference) this.conjugatedPortDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConjugatedPortTyping() {
        return this.conjugatedPortTypingEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConjugatedPortTyping_ConjugatedPortDefinition() {
        return (EReference) this.conjugatedPortTypingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConjugatedPortTyping_PortDefinition() {
        return (EReference) this.conjugatedPortTypingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConjugation() {
        return this.conjugationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConjugation_ConjugatedType() {
        return (EReference) this.conjugationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConjugation_OriginalType() {
        return (EReference) this.conjugationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConjugation_OwningType() {
        return (EReference) this.conjugationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConnectionDefinition() {
        return this.connectionDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnectionDefinition_ConnectionEnd() {
        return (EReference) this.connectionDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConnectionUsage() {
        return this.connectionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnectionUsage_ConnectionDefinition() {
        return (EReference) this.connectionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConnector() {
        return this.connectorEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnector_Association() {
        return (EReference) this.connectorEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnector_ConnectorEnd() {
        return (EReference) this.connectorEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getConnector_IsDirected() {
        return (EAttribute) this.connectorEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnector_RelatedFeature() {
        return (EReference) this.connectorEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnector_SourceFeature() {
        return (EReference) this.connectorEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnector_TargetFeature() {
        return (EReference) this.connectorEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConnectorAsUsage() {
        return this.connectorAsUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConstraintDefinition() {
        return this.constraintDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConstraintUsage() {
        return this.constraintUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConstraintUsage_ConstraintDefinition() {
        return (EReference) this.constraintUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getControlNode() {
        return this.controlNodeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getControlNode__MultiplicityHasBounds__Multiplicity_int_int() {
        return this.controlNodeEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDataType() {
        return this.dataTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDecisionNode() {
        return this.decisionNodeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDefinition() {
        return this.definitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_DirectedUsage() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDefinition_IsVariation() {
        return (EAttribute) this.definitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedAction() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedAllocation() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedAnalysisCase() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedAttribute() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedCalculation() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedCase() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedConcern() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedConnection() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedConstraint() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedEnumeration() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedFlow() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedInterface() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedItem() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedMetadata() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedOccurrence() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedPart() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedPort() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedReference() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedRendering() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedRequirement() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedState() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedTransition() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedUsage() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedUseCase() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedVerificationCase() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedView() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedViewpoint() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_Usage() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_Variant() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDefinition_VariantMembership() {
        return (EReference) this.definitionEClass.getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDependency() {
        return this.dependencyEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDependency_Client() {
        return (EReference) this.dependencyEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDependency_Supplier() {
        return (EReference) this.dependencyEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDifferencing() {
        return this.differencingEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDifferencing_DifferencingType() {
        return (EReference) this.differencingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDifferencing_TypeDifferenced() {
        return (EReference) this.differencingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDisjoining() {
        return this.disjoiningEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDisjoining_DisjoiningType() {
        return (EReference) this.disjoiningEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDisjoining_OwningType() {
        return (EReference) this.disjoiningEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDisjoining_TypeDisjoined() {
        return (EReference) this.disjoiningEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDocumentation() {
        return this.documentationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentation_DocumentedElement() {
        return (EReference) this.documentationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getElement() {
        return this.elementEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElement_AliasIds() {
        return (EAttribute) this.elementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElement_DeclaredName() {
        return (EAttribute) this.elementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElement_DeclaredShortName() {
        return (EAttribute) this.elementEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElement_Documentation() {
        return (EReference) this.elementEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElement_ElementId() {
        return (EAttribute) this.elementEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElement_IsImpliedIncluded() {
        return (EAttribute) this.elementEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElement_IsLibraryElement() {
        return (EAttribute) this.elementEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElement_Name() {
        return (EAttribute) this.elementEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElement_OwnedAnnotation() {
        return (EReference) this.elementEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElement_OwnedElement() {
        return (EReference) this.elementEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElement_OwnedRelationship() {
        return (EReference) this.elementEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElement_Owner() {
        return (EReference) this.elementEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElement_OwningMembership() {
        return (EReference) this.elementEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElement_OwningNamespace() {
        return (EReference) this.elementEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElement_OwningRelationship() {
        return (EReference) this.elementEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElement_QualifiedName() {
        return (EAttribute) this.elementEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElement_ShortName() {
        return (EAttribute) this.elementEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElement_TextualRepresentation() {
        return (EReference) this.elementEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getElement__EffectiveName() {
        return this.elementEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getElement__EffectiveShortName() {
        return this.elementEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getElement__EscapedName() {
        return this.elementEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getElement__LibraryNamespace() {
        return this.elementEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getElementFilterMembership() {
        return this.elementFilterMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElementFilterMembership_Condition() {
        return (EReference) this.elementFilterMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndFeatureMembership() {
        return this.endFeatureMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEnumerationDefinition() {
        return this.enumerationDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEnumerationDefinition_EnumeratedValue() {
        return (EReference) this.enumerationDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEnumerationUsage() {
        return this.enumerationUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEnumerationUsage_EnumerationDefinition() {
        return (EReference) this.enumerationUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEventOccurrenceUsage() {
        return this.eventOccurrenceUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEventOccurrenceUsage_EventOccurrence() {
        return (EReference) this.eventOccurrenceUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getExhibitStateUsage() {
        return this.exhibitStateUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getExhibitStateUsage_ExhibitedState() {
        return (EReference) this.exhibitStateUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getExpose() {
        return this.exposeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getExpression() {
        return this.expressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getExpression_Function() {
        return (EReference) this.expressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getExpression_IsModelLevelEvaluable() {
        return (EAttribute) this.expressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getExpression_Result() {
        return (EReference) this.expressionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getExpression__CheckCondition__Element() {
        return this.expressionEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getExpression__Evaluate__Element() {
        return this.expressionEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getExpression__ModelLevelEvaluable__EList() {
        return this.expressionEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFeature() {
        return this.featureEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_ChainingFeature() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeature_Direction() {
        return (EAttribute) this.featureEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_EndOwningType() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_FeaturingType() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeature_IsComposite() {
        return (EAttribute) this.featureEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeature_IsDerived() {
        return (EAttribute) this.featureEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeature_IsEnd() {
        return (EAttribute) this.featureEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeature_IsNonunique() {
        return (EAttribute) this.featureEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeature_IsOrdered() {
        return (EAttribute) this.featureEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeature_IsPortion() {
        return (EAttribute) this.featureEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeature_IsReadOnly() {
        return (EAttribute) this.featureEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeature_IsUnique() {
        return (EAttribute) this.featureEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_OwnedFeatureChaining() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_OwnedFeatureInverting() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_OwnedRedefinition() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_OwnedReferenceSubsetting() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_OwnedSubsetting() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_OwnedTypeFeaturing() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_OwnedTyping() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_OwningFeatureMembership() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_OwningType() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_Type() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeature_Valuation() {
        return (EReference) this.featureEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getFeature__DirectionFor__Type() {
        return this.featureEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getFeature__IsFeaturedWithin__Type() {
        return this.featureEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getFeature__NamingFeature() {
        return this.featureEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getFeature__Redefines__Feature() {
        return this.featureEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getFeature__RedefinesFromLibrary__String() {
        return this.featureEClass.getEOperations().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getFeature__SubsetsChain__Feature_Feature() {
        return this.featureEClass.getEOperations().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFeatureChainExpression() {
        return this.featureChainExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureChainExpression_TargetFeature() {
        return (EReference) this.featureChainExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getFeatureChainExpression__SourceTargetFeature() {
        return this.featureChainExpressionEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFeatureChaining() {
        return this.featureChainingEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureChaining_ChainingFeature() {
        return (EReference) this.featureChainingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureChaining_FeatureChained() {
        return (EReference) this.featureChainingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFeatureInverting() {
        return this.featureInvertingEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureInverting_FeatureInverted() {
        return (EReference) this.featureInvertingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureInverting_InvertingFeature() {
        return (EReference) this.featureInvertingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureInverting_OwningFeature() {
        return (EReference) this.featureInvertingEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFeatureMembership() {
        return this.featureMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureMembership_OwnedMemberFeature() {
        return (EReference) this.featureMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureMembership_OwningType() {
        return (EReference) this.featureMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFeatureReferenceExpression() {
        return this.featureReferenceExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureReferenceExpression_Referent() {
        return (EReference) this.featureReferenceExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFeatureTyping() {
        return this.featureTypingEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureTyping_OwningFeature() {
        return (EReference) this.featureTypingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureTyping_Type() {
        return (EReference) this.featureTypingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureTyping_TypedFeature() {
        return (EReference) this.featureTypingEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFeatureValue() {
        return this.featureValueEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureValue_FeatureWithValue() {
        return (EReference) this.featureValueEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeatureValue_IsDefault() {
        return (EAttribute) this.featureValueEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFeatureValue_IsInitial() {
        return (EAttribute) this.featureValueEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeatureValue_Value() {
        return (EReference) this.featureValueEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFeaturing() {
        return this.featuringEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeaturing_Feature() {
        return (EReference) this.featuringEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFeaturing_Type() {
        return (EReference) this.featuringEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFlowConnectionDefinition() {
        return this.flowConnectionDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFlowConnectionUsage() {
        return this.flowConnectionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFlowConnectionUsage_FlowConnectionDefinition() {
        return (EReference) this.flowConnectionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getForkNode() {
        return this.forkNodeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getForLoopActionUsage() {
        return this.forLoopActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForLoopActionUsage_LoopVariable() {
        return (EReference) this.forLoopActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForLoopActionUsage_SeqArgument() {
        return (EReference) this.forLoopActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFramedConcernMembership() {
        return this.framedConcernMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFramedConcernMembership_OwnedConcern() {
        return (EReference) this.framedConcernMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFramedConcernMembership_ReferencedConcern() {
        return (EReference) this.framedConcernMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFunction() {
        return this.functionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFunction_Expression() {
        return (EReference) this.functionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFunction_IsModelLevelEvaluable() {
        return (EAttribute) this.functionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFunction_Result() {
        return (EReference) this.functionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIfActionUsage() {
        return this.ifActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIfActionUsage_ElseAction() {
        return (EReference) this.ifActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIfActionUsage_IfArgument() {
        return (EReference) this.ifActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIfActionUsage_ThenAction() {
        return (EReference) this.ifActionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getImport() {
        return this.importEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getImport_ImportedElement() {
        return (EReference) this.importEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getImport_ImportOwningNamespace() {
        return (EReference) this.importEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getImport_IsImportAll() {
        return (EAttribute) this.importEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getImport_IsRecursive() {
        return (EAttribute) this.importEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getImport_Visibility() {
        return (EAttribute) this.importEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getImport__ImportedMemberships__EList() {
        return this.importEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIncludeUseCaseUsage() {
        return this.includeUseCaseUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIncludeUseCaseUsage_UseCaseIncluded() {
        return (EReference) this.includeUseCaseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInteraction() {
        return this.interactionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInterfaceDefinition() {
        return this.interfaceDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInterfaceDefinition_InterfaceEnd() {
        return (EReference) this.interfaceDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInterfaceUsage() {
        return this.interfaceUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInterfaceUsage_InterfaceDefinition() {
        return (EReference) this.interfaceUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIntersecting() {
        return this.intersectingEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIntersecting_IntersectingType() {
        return (EReference) this.intersectingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIntersecting_TypeIntersected() {
        return (EReference) this.intersectingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInvariant() {
        return this.invariantEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getInvariant_IsNegated() {
        return (EAttribute) this.invariantEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInvocationExpression() {
        return this.invocationExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInvocationExpression_Argument() {
        return (EReference) this.invocationExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getItemDefinition() {
        return this.itemDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getItemFeature() {
        return this.itemFeatureEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getItemFlow() {
        return this.itemFlowEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getItemFlow_Interaction() {
        return (EReference) this.itemFlowEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getItemFlow_ItemFeature() {
        return (EReference) this.itemFlowEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getItemFlow_ItemFlowEnd() {
        return (EReference) this.itemFlowEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getItemFlow_ItemType() {
        return (EReference) this.itemFlowEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getItemFlow_SourceOutputFeature() {
        return (EReference) this.itemFlowEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getItemFlow_TargetInputFeature() {
        return (EReference) this.itemFlowEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getItemFlowEnd() {
        return this.itemFlowEndEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getItemUsage() {
        return this.itemUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getItemUsage_ItemDefinition() {
        return (EReference) this.itemUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getJoinNode() {
        return this.joinNodeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLibraryPackage() {
        return this.libraryPackageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getLibraryPackage_IsStandard() {
        return (EAttribute) this.libraryPackageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLifeClass() {
        return this.lifeClassEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLiteralBoolean() {
        return this.literalBooleanEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getLiteralBoolean_Value() {
        return (EAttribute) this.literalBooleanEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLiteralExpression() {
        return this.literalExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLiteralInfinity() {
        return this.literalInfinityEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLiteralInteger() {
        return this.literalIntegerEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getLiteralInteger_Value() {
        return (EAttribute) this.literalIntegerEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLiteralRational() {
        return this.literalRationalEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getLiteralRational_Value() {
        return (EAttribute) this.literalRationalEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLiteralString() {
        return this.literalStringEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getLiteralString_Value() {
        return (EAttribute) this.literalStringEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLoopActionUsage() {
        return this.loopActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getLoopActionUsage_BodyAction() {
        return (EReference) this.loopActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMembership() {
        return this.membershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMembership_MemberElement() {
        return (EReference) this.membershipEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getMembership_MemberElementId() {
        return (EAttribute) this.membershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getMembership_MemberName() {
        return (EAttribute) this.membershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMembership_MembershipOwningNamespace() {
        return (EReference) this.membershipEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getMembership_MemberShortName() {
        return (EAttribute) this.membershipEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getMembership_Visibility() {
        return (EAttribute) this.membershipEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getMembership__IsDistinguishableFrom__Membership() {
        return this.membershipEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMembershipExpose() {
        return this.membershipExposeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMembershipImport() {
        return this.membershipImportEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMembershipImport_ImportedMembership() {
        return (EReference) this.membershipImportEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMergeNode() {
        return this.mergeNodeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMetaclass() {
        return this.metaclassEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMetadataAccessExpression() {
        return this.metadataAccessExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMetadataAccessExpression_ReferencedElement() {
        return (EReference) this.metadataAccessExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getMetadataAccessExpression__MetaclassFeature() {
        return this.metadataAccessExpressionEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMetadataDefinition() {
        return this.metadataDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMetadataFeature() {
        return this.metadataFeatureEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMetadataFeature_Metaclass() {
        return (EReference) this.metadataFeatureEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getMetadataFeature__EvaluateFeature__Feature() {
        return this.metadataFeatureEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getMetadataFeature__IsSemantic() {
        return this.metadataFeatureEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getMetadataFeature__IsSyntactic() {
        return this.metadataFeatureEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getMetadataFeature__SyntaxElement() {
        return this.metadataFeatureEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMetadataUsage() {
        return this.metadataUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMetadataUsage_MetadataDefinition() {
        return (EReference) this.metadataUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMultiplicity() {
        return this.multiplicityEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMultiplicityRange() {
        return this.multiplicityRangeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMultiplicityRange_Bound() {
        return (EReference) this.multiplicityRangeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMultiplicityRange_LowerBound() {
        return (EReference) this.multiplicityRangeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMultiplicityRange_UpperBound() {
        return (EReference) this.multiplicityRangeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getMultiplicityRange__HasBounds__int_int() {
        return this.multiplicityRangeEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getMultiplicityRange__ValueOf__Expression() {
        return this.multiplicityRangeEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNamespace() {
        return this.namespaceEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamespace_ImportedMembership() {
        return (EReference) this.namespaceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamespace_Member() {
        return (EReference) this.namespaceEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamespace_Membership() {
        return (EReference) this.namespaceEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamespace_OwnedImport() {
        return (EReference) this.namespaceEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamespace_OwnedMember() {
        return (EReference) this.namespaceEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamespace_OwnedMembership() {
        return (EReference) this.namespaceEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__ImportedMemberships__EList() {
        return this.namespaceEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__NamesOf__Element() {
        return this.namespaceEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__QualificationOf__String() {
        return this.namespaceEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__Resolve__String() {
        return this.namespaceEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__ResolveGlobal__String() {
        return this.namespaceEClass.getEOperations().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__ResolveLocal__String() {
        return this.namespaceEClass.getEOperations().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__ResolveVisible__String() {
        return this.namespaceEClass.getEOperations().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__UnqualifiedNameOf__String() {
        return this.namespaceEClass.getEOperations().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__VisibilityOf__Membership() {
        return this.namespaceEClass.getEOperations().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getNamespace__VisibleMemberships__EList_boolean_boolean() {
        return this.namespaceEClass.getEOperations().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNamespaceExpose() {
        return this.namespaceExposeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNamespaceImport() {
        return this.namespaceImportEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamespaceImport_ImportedNamespace() {
        return (EReference) this.namespaceImportEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNullExpression() {
        return this.nullExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getObjectiveMembership() {
        return this.objectiveMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getObjectiveMembership_OwnedObjectiveRequirement() {
        return (EReference) this.objectiveMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getOccurrenceDefinition() {
        return this.occurrenceDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOccurrenceDefinition_IsIndividual() {
        return (EAttribute) this.occurrenceDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOccurrenceDefinition_LifeClass() {
        return (EReference) this.occurrenceDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getOccurrenceUsage() {
        return this.occurrenceUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOccurrenceUsage_IndividualDefinition() {
        return (EReference) this.occurrenceUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOccurrenceUsage_IsIndividual() {
        return (EAttribute) this.occurrenceUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOccurrenceUsage_OccurrenceDefinition() {
        return (EReference) this.occurrenceUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOccurrenceUsage_PortionKind() {
        return (EAttribute) this.occurrenceUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getOperatorExpression() {
        return this.operatorExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOperatorExpression_Operand() {
        return (EReference) this.operatorExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOperatorExpression_Operator() {
        return (EAttribute) this.operatorExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getOwningMembership() {
        return this.owningMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOwningMembership_OwnedMemberElement() {
        return (EReference) this.owningMembershipEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOwningMembership_OwnedMemberElementId() {
        return (EAttribute) this.owningMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOwningMembership_OwnedMemberName() {
        return (EAttribute) this.owningMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOwningMembership_OwnedMemberShortName() {
        return (EAttribute) this.owningMembershipEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPackage() {
        return this.packageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPackage_FilterCondition() {
        return (EReference) this.packageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getPackage__IncludeAsMember__Element() {
        return this.packageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getParameterMembership() {
        return this.parameterMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getParameterMembership_OwnedMemberParameter() {
        return (EReference) this.parameterMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPartDefinition() {
        return this.partDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPartUsage() {
        return this.partUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPartUsage_PartDefinition() {
        return (EReference) this.partUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPerformActionUsage() {
        return this.performActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPerformActionUsage_PerformedAction() {
        return (EReference) this.performActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPortConjugation() {
        return this.portConjugationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPortConjugation_ConjugatedPortDefinition() {
        return (EReference) this.portConjugationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPortConjugation_OriginalPortDefinition() {
        return (EReference) this.portConjugationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPortDefinition() {
        return this.portDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPortDefinition_ConjugatedPortDefinition() {
        return (EReference) this.portDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPortUsage() {
        return this.portUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPortUsage_PortDefinition() {
        return (EReference) this.portUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPredicate() {
        return this.predicateEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRedefinition() {
        return this.redefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRedefinition_RedefinedFeature() {
        return (EReference) this.redefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRedefinition_RedefiningFeature() {
        return (EReference) this.redefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getReferenceSubsetting() {
        return this.referenceSubsettingEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getReferenceSubsetting_ReferencedFeature() {
        return (EReference) this.referenceSubsettingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getReferenceSubsetting_ReferencingFeature() {
        return (EReference) this.referenceSubsettingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getReferenceUsage() {
        return this.referenceUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRelationship() {
        return this.relationshipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRelationship_IsImplied() {
        return (EAttribute) this.relationshipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRelationship_OwnedRelatedElement() {
        return (EReference) this.relationshipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRelationship_OwningRelatedElement() {
        return (EReference) this.relationshipEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRelationship_RelatedElement() {
        return (EReference) this.relationshipEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRelationship_Source() {
        return (EReference) this.relationshipEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRelationship_Target() {
        return (EReference) this.relationshipEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRenderingDefinition() {
        return this.renderingDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRenderingDefinition_Rendering() {
        return (EReference) this.renderingDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRenderingUsage() {
        return this.renderingUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRenderingUsage_RenderingDefinition() {
        return (EReference) this.renderingUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRequirementConstraintMembership() {
        return this.requirementConstraintMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRequirementConstraintMembership_Kind() {
        return (EAttribute) this.requirementConstraintMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementConstraintMembership_OwnedConstraint() {
        return (EReference) this.requirementConstraintMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementConstraintMembership_ReferencedConstraint() {
        return (EReference) this.requirementConstraintMembershipEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRequirementDefinition() {
        return this.requirementDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_ActorParameter() {
        return (EReference) this.requirementDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_AssumedConstraint() {
        return (EReference) this.requirementDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_FramedConcern() {
        return (EReference) this.requirementDefinitionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRequirementDefinition_ReqId() {
        return (EAttribute) this.requirementDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_RequiredConstraint() {
        return (EReference) this.requirementDefinitionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_StakeholderParameter() {
        return (EReference) this.requirementDefinitionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_SubjectParameter() {
        return (EReference) this.requirementDefinitionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRequirementDefinition_Text() {
        return (EAttribute) this.requirementDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRequirementUsage() {
        return this.requirementUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementUsage_ActorParameter() {
        return (EReference) this.requirementUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementUsage_AssumedConstraint() {
        return (EReference) this.requirementUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementUsage_FramedConcern() {
        return (EReference) this.requirementUsageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRequirementUsage_ReqId() {
        return (EAttribute) this.requirementUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementUsage_RequiredConstraint() {
        return (EReference) this.requirementUsageEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementUsage_RequirementDefinition() {
        return (EReference) this.requirementUsageEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementUsage_StakeholderParameter() {
        return (EReference) this.requirementUsageEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementUsage_SubjectParameter() {
        return (EReference) this.requirementUsageEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRequirementUsage_Text() {
        return (EAttribute) this.requirementUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRequirementVerificationMembership() {
        return this.requirementVerificationMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementVerificationMembership_OwnedRequirement() {
        return (EReference) this.requirementVerificationMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRequirementVerificationMembership_VerifiedRequirement() {
        return (EReference) this.requirementVerificationMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getResultExpressionMembership() {
        return this.resultExpressionMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getResultExpressionMembership_OwnedResultExpression() {
        return (EReference) this.resultExpressionMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getReturnParameterMembership() {
        return this.returnParameterMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSatisfyRequirementUsage() {
        return this.satisfyRequirementUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSatisfyRequirementUsage_SatisfiedRequirement() {
        return (EReference) this.satisfyRequirementUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSatisfyRequirementUsage_SatisfyingFeature() {
        return (EReference) this.satisfyRequirementUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSelectExpression() {
        return this.selectExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSendActionUsage() {
        return this.sendActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSendActionUsage_PayloadArgument() {
        return (EReference) this.sendActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSendActionUsage_ReceiverArgument() {
        return (EReference) this.sendActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSendActionUsage_SenderArgument() {
        return (EReference) this.sendActionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSpecialization() {
        return this.specializationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSpecialization_General() {
        return (EReference) this.specializationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSpecialization_OwningType() {
        return (EReference) this.specializationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSpecialization_Specific() {
        return (EReference) this.specializationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStakeholderMembership() {
        return this.stakeholderMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStakeholderMembership_OwnedStakeholderParameter() {
        return (EReference) this.stakeholderMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStateDefinition() {
        return this.stateDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStateDefinition_DoAction() {
        return (EReference) this.stateDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStateDefinition_EntryAction() {
        return (EReference) this.stateDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStateDefinition_ExitAction() {
        return (EReference) this.stateDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getStateDefinition_IsParallel() {
        return (EAttribute) this.stateDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStateDefinition_State() {
        return (EReference) this.stateDefinitionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStateSubactionMembership() {
        return this.stateSubactionMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStateSubactionMembership_Action() {
        return (EReference) this.stateSubactionMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getStateSubactionMembership_Kind() {
        return (EAttribute) this.stateSubactionMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStateUsage() {
        return this.stateUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStateUsage_DoAction() {
        return (EReference) this.stateUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStateUsage_EntryAction() {
        return (EReference) this.stateUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStateUsage_ExitAction() {
        return (EReference) this.stateUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getStateUsage_IsParallel() {
        return (EAttribute) this.stateUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStateUsage_StateDefinition() {
        return (EReference) this.stateUsageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getStateUsage__IsSubstateUsage__boolean() {
        return this.stateUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStep() {
        return this.stepEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStep_Behavior() {
        return (EReference) this.stepEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStep_Parameter() {
        return (EReference) this.stepEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStructure() {
        return this.structureEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSubclassification() {
        return this.subclassificationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubclassification_OwningClassifier() {
        return (EReference) this.subclassificationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubclassification_Subclassifier() {
        return (EReference) this.subclassificationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubclassification_Superclassifier() {
        return (EReference) this.subclassificationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSubjectMembership() {
        return this.subjectMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubjectMembership_OwnedSubjectParameter() {
        return (EReference) this.subjectMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSubsetting() {
        return this.subsettingEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubsetting_OwningFeature() {
        return (EReference) this.subsettingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubsetting_SubsettedFeature() {
        return (EReference) this.subsettingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubsetting_SubsettingFeature() {
        return (EReference) this.subsettingEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSuccession() {
        return this.successionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSuccession_EffectStep() {
        return (EReference) this.successionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSuccession_GuardExpression() {
        return (EReference) this.successionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSuccession_TransitionStep() {
        return (EReference) this.successionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSuccession_TriggerStep() {
        return (EReference) this.successionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSuccessionAsUsage() {
        return this.successionAsUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSuccessionFlowConnectionUsage() {
        return this.successionFlowConnectionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSuccessionItemFlow() {
        return this.successionItemFlowEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTextualRepresentation() {
        return this.textualRepresentationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getTextualRepresentation_Body() {
        return (EAttribute) this.textualRepresentationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getTextualRepresentation_Language() {
        return (EAttribute) this.textualRepresentationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTextualRepresentation_RepresentedElement() {
        return (EReference) this.textualRepresentationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTransitionFeatureMembership() {
        return this.transitionFeatureMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getTransitionFeatureMembership_Kind() {
        return (EAttribute) this.transitionFeatureMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTransitionFeatureMembership_TransitionFeature() {
        return (EReference) this.transitionFeatureMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTransitionUsage() {
        return this.transitionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTransitionUsage_EffectAction() {
        return (EReference) this.transitionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTransitionUsage_GuardExpression() {
        return (EReference) this.transitionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTransitionUsage_Source() {
        return (EReference) this.transitionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTransitionUsage_Succession() {
        return (EReference) this.transitionUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTransitionUsage_Target() {
        return (EReference) this.transitionUsageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTransitionUsage_TriggerAction() {
        return (EReference) this.transitionUsageEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getTransitionUsage__TriggerPayloadParameter() {
        return this.transitionUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTriggerInvocationExpression() {
        return this.triggerInvocationExpressionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getTriggerInvocationExpression_Kind() {
        return (EAttribute) this.triggerInvocationExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getType() {
        return this.typeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_DifferencingType() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_DirectedFeature() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_EndFeature() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_Feature() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_FeatureMembership() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_InheritedFeature() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_InheritedMembership() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_Input() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_IntersectingType() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getType_IsAbstract() {
        return (EAttribute) this.typeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getType_IsConjugated() {
        return (EAttribute) this.typeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getType_IsSufficient() {
        return (EAttribute) this.typeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_Multiplicity() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_Output() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_OwnedConjugator() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_OwnedDifferencing() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_OwnedDisjoining() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_OwnedEndFeature() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_OwnedFeature() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_OwnedFeatureMembership() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_OwnedIntersecting() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_OwnedSpecialization() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_OwnedUnioning() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getType_UnioningType() {
        return (EReference) this.typeEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getType__AllSupertypes() {
        return this.typeEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getType__DirectionOf__Feature() {
        return this.typeEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getType__InheritedMemberships__EList() {
        return this.typeEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getType__Specializes__Type() {
        return this.typeEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getType__SpecializesFromLibrary__String() {
        return this.typeEClass.getEOperations().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTypeFeaturing() {
        return this.typeFeaturingEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTypeFeaturing_FeatureOfType() {
        return (EReference) this.typeFeaturingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTypeFeaturing_FeaturingType() {
        return (EReference) this.typeFeaturingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTypeFeaturing_OwningFeatureOfType() {
        return (EReference) this.typeFeaturingEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getUnioning() {
        return this.unioningEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUnioning_TypeUnioned() {
        return (EReference) this.unioningEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUnioning_UnioningType() {
        return (EReference) this.unioningEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getUsage() {
        return this.usageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_Definition() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_DirectedUsage() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getUsage_IsReference() {
        return (EAttribute) this.usageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getUsage_IsVariation() {
        return (EAttribute) this.usageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedAction() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedAllocation() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedAnalysisCase() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedAttribute() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedCalculation() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedCase() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedConcern() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedConnection() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedConstraint() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedEnumeration() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedFlow() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedInterface() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedItem() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedMetadata() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedOccurrence() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedPart() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedPort() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedReference() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedRendering() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedRequirement() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedState() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedTransition() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedUsage() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedUseCase() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedVerificationCase() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedView() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_NestedViewpoint() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_OwningDefinition() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_OwningUsage() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(32);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_Usage() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(33);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_Variant() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(34);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUsage_VariantMembership() {
        return (EReference) this.usageEClass.getEStructuralFeatures().get(35);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getUseCaseDefinition() {
        return this.useCaseDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUseCaseDefinition_IncludedUseCase() {
        return (EReference) this.useCaseDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getUseCaseUsage() {
        return this.useCaseUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUseCaseUsage_IncludedUseCase() {
        return (EReference) this.useCaseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUseCaseUsage_UseCaseDefinition() {
        return (EReference) this.useCaseUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getVariantMembership() {
        return this.variantMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getVariantMembership_OwnedVariantUsage() {
        return (EReference) this.variantMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getVerificationCaseDefinition() {
        return this.verificationCaseDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getVerificationCaseDefinition_VerifiedRequirement() {
        return (EReference) this.verificationCaseDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getVerificationCaseUsage() {
        return this.verificationCaseUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getVerificationCaseUsage_VerificationCaseDefinition() {
        return (EReference) this.verificationCaseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getVerificationCaseUsage_VerifiedRequirement() {
        return (EReference) this.verificationCaseUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getViewDefinition() {
        return this.viewDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewDefinition_SatisfiedViewpoint() {
        return (EReference) this.viewDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewDefinition_View() {
        return (EReference) this.viewDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewDefinition_ViewCondition() {
        return (EReference) this.viewDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewDefinition_ViewRendering() {
        return (EReference) this.viewDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getViewpointDefinition() {
        return this.viewpointDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewpointDefinition_ViewpointStakeholder() {
        return (EReference) this.viewpointDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getViewpointUsage() {
        return this.viewpointUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewpointUsage_ViewpointDefinition() {
        return (EReference) this.viewpointUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewpointUsage_ViewpointStakeholder() {
        return (EReference) this.viewpointUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getViewRenderingMembership() {
        return this.viewRenderingMembershipEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewRenderingMembership_OwnedRendering() {
        return (EReference) this.viewRenderingMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewRenderingMembership_ReferencedRendering() {
        return (EReference) this.viewRenderingMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getViewUsage() {
        return this.viewUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewUsage_ExposedElement() {
        return (EReference) this.viewUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewUsage_SatisfiedViewpoint() {
        return (EReference) this.viewUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewUsage_ViewCondition() {
        return (EReference) this.viewUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewUsage_ViewDefinition() {
        return (EReference) this.viewUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getViewUsage_ViewRendering() {
        return (EReference) this.viewUsageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EOperation getViewUsage__IncludeAsExposed__Element() {
        return this.viewUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getWhileLoopActionUsage() {
        return this.whileLoopActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getWhileLoopActionUsage_UntilArgument() {
        return (EReference) this.whileLoopActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getWhileLoopActionUsage_WhileArgument() {
        return (EReference) this.whileLoopActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EEnum getFeatureDirectionKind() {
        return this.featureDirectionKindEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EEnum getPortionKind() {
        return this.portionKindEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EEnum getRequirementConstraintKind() {
        return this.requirementConstraintKindEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EEnum getStateSubactionKind() {
        return this.stateSubactionKindEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EEnum getTransitionFeatureKind() {
        return this.transitionFeatureKindEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EEnum getTriggerKind() {
        return this.triggerKindEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EEnum getVisibilityKind() {
        return this.visibilityKindEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SysmlFactory getSysmlFactory() {
        return (SysmlFactory) this.getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on any invocation but
     * its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void createPackageContents() {
        if (this.isCreated)
            return;
        this.isCreated = true;

        // Create classes and their features
        this.acceptActionUsageEClass = this.createEClass(ACCEPT_ACTION_USAGE);
        this.createEReference(this.acceptActionUsageEClass, ACCEPT_ACTION_USAGE__PAYLOAD_ARGUMENT);
        this.createEReference(this.acceptActionUsageEClass, ACCEPT_ACTION_USAGE__PAYLOAD_PARAMETER);
        this.createEReference(this.acceptActionUsageEClass, ACCEPT_ACTION_USAGE__RECEIVER_ARGUMENT);
        this.createEOperation(this.acceptActionUsageEClass, ACCEPT_ACTION_USAGE___IS_TRIGGER_ACTION);

        this.actionDefinitionEClass = this.createEClass(ACTION_DEFINITION);
        this.createEReference(this.actionDefinitionEClass, ACTION_DEFINITION__ACTION);

        this.actionUsageEClass = this.createEClass(ACTION_USAGE);
        this.createEReference(this.actionUsageEClass, ACTION_USAGE__ACTION_DEFINITION);
        this.createEOperation(this.actionUsageEClass, ACTION_USAGE___ARGUMENT__INT);
        this.createEOperation(this.actionUsageEClass, ACTION_USAGE___INPUT_PARAMETER__INT);
        this.createEOperation(this.actionUsageEClass, ACTION_USAGE___INPUT_PARAMETERS);
        this.createEOperation(this.actionUsageEClass, ACTION_USAGE___IS_SUBACTION_USAGE);

        this.actorMembershipEClass = this.createEClass(ACTOR_MEMBERSHIP);
        this.createEReference(this.actorMembershipEClass, ACTOR_MEMBERSHIP__OWNED_ACTOR_PARAMETER);

        this.allocationDefinitionEClass = this.createEClass(ALLOCATION_DEFINITION);
        this.createEReference(this.allocationDefinitionEClass, ALLOCATION_DEFINITION__ALLOCATION);

        this.allocationUsageEClass = this.createEClass(ALLOCATION_USAGE);
        this.createEReference(this.allocationUsageEClass, ALLOCATION_USAGE__ALLOCATION_DEFINITION);

        this.analysisCaseDefinitionEClass = this.createEClass(ANALYSIS_CASE_DEFINITION);
        this.createEReference(this.analysisCaseDefinitionEClass, ANALYSIS_CASE_DEFINITION__ANALYSIS_ACTION);
        this.createEReference(this.analysisCaseDefinitionEClass, ANALYSIS_CASE_DEFINITION__RESULT_EXPRESSION);

        this.analysisCaseUsageEClass = this.createEClass(ANALYSIS_CASE_USAGE);
        this.createEReference(this.analysisCaseUsageEClass, ANALYSIS_CASE_USAGE__ANALYSIS_ACTION);
        this.createEReference(this.analysisCaseUsageEClass, ANALYSIS_CASE_USAGE__ANALYSIS_CASE_DEFINITION);
        this.createEReference(this.analysisCaseUsageEClass, ANALYSIS_CASE_USAGE__RESULT_EXPRESSION);

        this.annotatingElementEClass = this.createEClass(ANNOTATING_ELEMENT);
        this.createEReference(this.annotatingElementEClass, ANNOTATING_ELEMENT__ANNOTATED_ELEMENT);
        this.createEReference(this.annotatingElementEClass, ANNOTATING_ELEMENT__ANNOTATION);

        this.annotationEClass = this.createEClass(ANNOTATION);
        this.createEReference(this.annotationEClass, ANNOTATION__ANNOTATED_ELEMENT);
        this.createEReference(this.annotationEClass, ANNOTATION__ANNOTATING_ELEMENT);
        this.createEReference(this.annotationEClass, ANNOTATION__OWNING_ANNOTATED_ELEMENT);

        this.assertConstraintUsageEClass = this.createEClass(ASSERT_CONSTRAINT_USAGE);
        this.createEReference(this.assertConstraintUsageEClass, ASSERT_CONSTRAINT_USAGE__ASSERTED_CONSTRAINT);

        this.assignmentActionUsageEClass = this.createEClass(ASSIGNMENT_ACTION_USAGE);
        this.createEReference(this.assignmentActionUsageEClass, ASSIGNMENT_ACTION_USAGE__REFERENT);
        this.createEReference(this.assignmentActionUsageEClass, ASSIGNMENT_ACTION_USAGE__TARGET_ARGUMENT);
        this.createEReference(this.assignmentActionUsageEClass, ASSIGNMENT_ACTION_USAGE__VALUE_EXPRESSION);

        this.associationEClass = this.createEClass(ASSOCIATION);
        this.createEReference(this.associationEClass, ASSOCIATION__ASSOCIATION_END);
        this.createEReference(this.associationEClass, ASSOCIATION__RELATED_TYPE);
        this.createEReference(this.associationEClass, ASSOCIATION__SOURCE_TYPE);
        this.createEReference(this.associationEClass, ASSOCIATION__TARGET_TYPE);

        this.associationStructureEClass = this.createEClass(ASSOCIATION_STRUCTURE);

        this.attributeDefinitionEClass = this.createEClass(ATTRIBUTE_DEFINITION);

        this.attributeUsageEClass = this.createEClass(ATTRIBUTE_USAGE);
        this.createEReference(this.attributeUsageEClass, ATTRIBUTE_USAGE__ATTRIBUTE_DEFINITION);

        this.behaviorEClass = this.createEClass(BEHAVIOR);
        this.createEReference(this.behaviorEClass, BEHAVIOR__PARAMETER);
        this.createEReference(this.behaviorEClass, BEHAVIOR__STEP);

        this.bindingConnectorEClass = this.createEClass(BINDING_CONNECTOR);

        this.bindingConnectorAsUsageEClass = this.createEClass(BINDING_CONNECTOR_AS_USAGE);

        this.booleanExpressionEClass = this.createEClass(BOOLEAN_EXPRESSION);
        this.createEReference(this.booleanExpressionEClass, BOOLEAN_EXPRESSION__PREDICATE);

        this.calculationDefinitionEClass = this.createEClass(CALCULATION_DEFINITION);
        this.createEReference(this.calculationDefinitionEClass, CALCULATION_DEFINITION__CALCULATION);

        this.calculationUsageEClass = this.createEClass(CALCULATION_USAGE);
        this.createEReference(this.calculationUsageEClass, CALCULATION_USAGE__CALCULATION_DEFINITION);

        this.caseDefinitionEClass = this.createEClass(CASE_DEFINITION);
        this.createEReference(this.caseDefinitionEClass, CASE_DEFINITION__ACTOR_PARAMETER);
        this.createEReference(this.caseDefinitionEClass, CASE_DEFINITION__OBJECTIVE_REQUIREMENT);
        this.createEReference(this.caseDefinitionEClass, CASE_DEFINITION__SUBJECT_PARAMETER);

        this.caseUsageEClass = this.createEClass(CASE_USAGE);
        this.createEReference(this.caseUsageEClass, CASE_USAGE__ACTOR_PARAMETER);
        this.createEReference(this.caseUsageEClass, CASE_USAGE__CASE_DEFINITION);
        this.createEReference(this.caseUsageEClass, CASE_USAGE__OBJECTIVE_REQUIREMENT);
        this.createEReference(this.caseUsageEClass, CASE_USAGE__SUBJECT_PARAMETER);

        this.classEClass = this.createEClass(CLASS);

        this.classifierEClass = this.createEClass(CLASSIFIER);
        this.createEReference(this.classifierEClass, CLASSIFIER__OWNED_SUBCLASSIFICATION);

        this.collectExpressionEClass = this.createEClass(COLLECT_EXPRESSION);

        this.commentEClass = this.createEClass(COMMENT);
        this.createEAttribute(this.commentEClass, COMMENT__BODY);
        this.createEAttribute(this.commentEClass, COMMENT__LOCALE);

        this.concernDefinitionEClass = this.createEClass(CONCERN_DEFINITION);

        this.concernUsageEClass = this.createEClass(CONCERN_USAGE);
        this.createEReference(this.concernUsageEClass, CONCERN_USAGE__CONCERN_DEFINITION);

        this.conjugatedPortDefinitionEClass = this.createEClass(CONJUGATED_PORT_DEFINITION);
        this.createEReference(this.conjugatedPortDefinitionEClass, CONJUGATED_PORT_DEFINITION__ORIGINAL_PORT_DEFINITION);
        this.createEReference(this.conjugatedPortDefinitionEClass, CONJUGATED_PORT_DEFINITION__OWNED_PORT_CONJUGATOR);

        this.conjugatedPortTypingEClass = this.createEClass(CONJUGATED_PORT_TYPING);
        this.createEReference(this.conjugatedPortTypingEClass, CONJUGATED_PORT_TYPING__CONJUGATED_PORT_DEFINITION);
        this.createEReference(this.conjugatedPortTypingEClass, CONJUGATED_PORT_TYPING__PORT_DEFINITION);

        this.conjugationEClass = this.createEClass(CONJUGATION);
        this.createEReference(this.conjugationEClass, CONJUGATION__CONJUGATED_TYPE);
        this.createEReference(this.conjugationEClass, CONJUGATION__ORIGINAL_TYPE);
        this.createEReference(this.conjugationEClass, CONJUGATION__OWNING_TYPE);

        this.connectionDefinitionEClass = this.createEClass(CONNECTION_DEFINITION);
        this.createEReference(this.connectionDefinitionEClass, CONNECTION_DEFINITION__CONNECTION_END);

        this.connectionUsageEClass = this.createEClass(CONNECTION_USAGE);
        this.createEReference(this.connectionUsageEClass, CONNECTION_USAGE__CONNECTION_DEFINITION);

        this.connectorEClass = this.createEClass(CONNECTOR);
        this.createEAttribute(this.connectorEClass, CONNECTOR__IS_DIRECTED);
        this.createEReference(this.connectorEClass, CONNECTOR__ASSOCIATION);
        this.createEReference(this.connectorEClass, CONNECTOR__CONNECTOR_END);
        this.createEReference(this.connectorEClass, CONNECTOR__RELATED_FEATURE);
        this.createEReference(this.connectorEClass, CONNECTOR__SOURCE_FEATURE);
        this.createEReference(this.connectorEClass, CONNECTOR__TARGET_FEATURE);

        this.connectorAsUsageEClass = this.createEClass(CONNECTOR_AS_USAGE);

        this.constraintDefinitionEClass = this.createEClass(CONSTRAINT_DEFINITION);

        this.constraintUsageEClass = this.createEClass(CONSTRAINT_USAGE);
        this.createEReference(this.constraintUsageEClass, CONSTRAINT_USAGE__CONSTRAINT_DEFINITION);

        this.controlNodeEClass = this.createEClass(CONTROL_NODE);
        this.createEOperation(this.controlNodeEClass, CONTROL_NODE___MULTIPLICITY_HAS_BOUNDS__MULTIPLICITY_INT_INT);

        this.dataTypeEClass = this.createEClass(DATA_TYPE);

        this.decisionNodeEClass = this.createEClass(DECISION_NODE);

        this.definitionEClass = this.createEClass(DEFINITION);
        this.createEAttribute(this.definitionEClass, DEFINITION__IS_VARIATION);
        this.createEReference(this.definitionEClass, DEFINITION__DIRECTED_USAGE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_ACTION);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_ALLOCATION);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_ANALYSIS_CASE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_ATTRIBUTE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_CALCULATION);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_CASE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_CONCERN);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_CONNECTION);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_CONSTRAINT);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_ENUMERATION);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_FLOW);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_INTERFACE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_ITEM);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_METADATA);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_OCCURRENCE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_PART);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_PORT);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_REFERENCE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_RENDERING);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_REQUIREMENT);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_STATE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_TRANSITION);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_USAGE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_USE_CASE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_VERIFICATION_CASE);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_VIEW);
        this.createEReference(this.definitionEClass, DEFINITION__OWNED_VIEWPOINT);
        this.createEReference(this.definitionEClass, DEFINITION__USAGE);
        this.createEReference(this.definitionEClass, DEFINITION__VARIANT);
        this.createEReference(this.definitionEClass, DEFINITION__VARIANT_MEMBERSHIP);

        this.dependencyEClass = this.createEClass(DEPENDENCY);
        this.createEReference(this.dependencyEClass, DEPENDENCY__CLIENT);
        this.createEReference(this.dependencyEClass, DEPENDENCY__SUPPLIER);

        this.differencingEClass = this.createEClass(DIFFERENCING);
        this.createEReference(this.differencingEClass, DIFFERENCING__DIFFERENCING_TYPE);
        this.createEReference(this.differencingEClass, DIFFERENCING__TYPE_DIFFERENCED);

        this.disjoiningEClass = this.createEClass(DISJOINING);
        this.createEReference(this.disjoiningEClass, DISJOINING__DISJOINING_TYPE);
        this.createEReference(this.disjoiningEClass, DISJOINING__OWNING_TYPE);
        this.createEReference(this.disjoiningEClass, DISJOINING__TYPE_DISJOINED);

        this.documentationEClass = this.createEClass(DOCUMENTATION);
        this.createEReference(this.documentationEClass, DOCUMENTATION__DOCUMENTED_ELEMENT);

        this.elementEClass = this.createEClass(ELEMENT);
        this.createEAttribute(this.elementEClass, ELEMENT__ALIAS_IDS);
        this.createEAttribute(this.elementEClass, ELEMENT__DECLARED_NAME);
        this.createEAttribute(this.elementEClass, ELEMENT__DECLARED_SHORT_NAME);
        this.createEAttribute(this.elementEClass, ELEMENT__ELEMENT_ID);
        this.createEAttribute(this.elementEClass, ELEMENT__IS_IMPLIED_INCLUDED);
        this.createEAttribute(this.elementEClass, ELEMENT__IS_LIBRARY_ELEMENT);
        this.createEAttribute(this.elementEClass, ELEMENT__NAME);
        this.createEAttribute(this.elementEClass, ELEMENT__QUALIFIED_NAME);
        this.createEAttribute(this.elementEClass, ELEMENT__SHORT_NAME);
        this.createEReference(this.elementEClass, ELEMENT__DOCUMENTATION);
        this.createEReference(this.elementEClass, ELEMENT__OWNED_ANNOTATION);
        this.createEReference(this.elementEClass, ELEMENT__OWNED_ELEMENT);
        this.createEReference(this.elementEClass, ELEMENT__OWNED_RELATIONSHIP);
        this.createEReference(this.elementEClass, ELEMENT__OWNER);
        this.createEReference(this.elementEClass, ELEMENT__OWNING_MEMBERSHIP);
        this.createEReference(this.elementEClass, ELEMENT__OWNING_NAMESPACE);
        this.createEReference(this.elementEClass, ELEMENT__OWNING_RELATIONSHIP);
        this.createEReference(this.elementEClass, ELEMENT__TEXTUAL_REPRESENTATION);
        this.createEOperation(this.elementEClass, ELEMENT___EFFECTIVE_NAME);
        this.createEOperation(this.elementEClass, ELEMENT___EFFECTIVE_SHORT_NAME);
        this.createEOperation(this.elementEClass, ELEMENT___ESCAPED_NAME);
        this.createEOperation(this.elementEClass, ELEMENT___LIBRARY_NAMESPACE);

        this.elementFilterMembershipEClass = this.createEClass(ELEMENT_FILTER_MEMBERSHIP);
        this.createEReference(this.elementFilterMembershipEClass, ELEMENT_FILTER_MEMBERSHIP__CONDITION);

        this.endFeatureMembershipEClass = this.createEClass(END_FEATURE_MEMBERSHIP);

        this.enumerationDefinitionEClass = this.createEClass(ENUMERATION_DEFINITION);
        this.createEReference(this.enumerationDefinitionEClass, ENUMERATION_DEFINITION__ENUMERATED_VALUE);

        this.enumerationUsageEClass = this.createEClass(ENUMERATION_USAGE);
        this.createEReference(this.enumerationUsageEClass, ENUMERATION_USAGE__ENUMERATION_DEFINITION);

        this.eventOccurrenceUsageEClass = this.createEClass(EVENT_OCCURRENCE_USAGE);
        this.createEReference(this.eventOccurrenceUsageEClass, EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE);

        this.exhibitStateUsageEClass = this.createEClass(EXHIBIT_STATE_USAGE);
        this.createEReference(this.exhibitStateUsageEClass, EXHIBIT_STATE_USAGE__EXHIBITED_STATE);

        this.exposeEClass = this.createEClass(EXPOSE);

        this.expressionEClass = this.createEClass(EXPRESSION);
        this.createEAttribute(this.expressionEClass, EXPRESSION__IS_MODEL_LEVEL_EVALUABLE);
        this.createEReference(this.expressionEClass, EXPRESSION__FUNCTION);
        this.createEReference(this.expressionEClass, EXPRESSION__RESULT);
        this.createEOperation(this.expressionEClass, EXPRESSION___CHECK_CONDITION__ELEMENT);
        this.createEOperation(this.expressionEClass, EXPRESSION___EVALUATE__ELEMENT);
        this.createEOperation(this.expressionEClass, EXPRESSION___MODEL_LEVEL_EVALUABLE__ELIST);

        this.featureEClass = this.createEClass(FEATURE);
        this.createEAttribute(this.featureEClass, FEATURE__DIRECTION);
        this.createEAttribute(this.featureEClass, FEATURE__IS_COMPOSITE);
        this.createEAttribute(this.featureEClass, FEATURE__IS_DERIVED);
        this.createEAttribute(this.featureEClass, FEATURE__IS_END);
        this.createEAttribute(this.featureEClass, FEATURE__IS_NONUNIQUE);
        this.createEAttribute(this.featureEClass, FEATURE__IS_ORDERED);
        this.createEAttribute(this.featureEClass, FEATURE__IS_PORTION);
        this.createEAttribute(this.featureEClass, FEATURE__IS_READ_ONLY);
        this.createEAttribute(this.featureEClass, FEATURE__IS_UNIQUE);
        this.createEReference(this.featureEClass, FEATURE__CHAINING_FEATURE);
        this.createEReference(this.featureEClass, FEATURE__END_OWNING_TYPE);
        this.createEReference(this.featureEClass, FEATURE__FEATURING_TYPE);
        this.createEReference(this.featureEClass, FEATURE__OWNED_FEATURE_CHAINING);
        this.createEReference(this.featureEClass, FEATURE__OWNED_FEATURE_INVERTING);
        this.createEReference(this.featureEClass, FEATURE__OWNED_REDEFINITION);
        this.createEReference(this.featureEClass, FEATURE__OWNED_REFERENCE_SUBSETTING);
        this.createEReference(this.featureEClass, FEATURE__OWNED_SUBSETTING);
        this.createEReference(this.featureEClass, FEATURE__OWNED_TYPE_FEATURING);
        this.createEReference(this.featureEClass, FEATURE__OWNED_TYPING);
        this.createEReference(this.featureEClass, FEATURE__OWNING_FEATURE_MEMBERSHIP);
        this.createEReference(this.featureEClass, FEATURE__OWNING_TYPE);
        this.createEReference(this.featureEClass, FEATURE__TYPE);
        this.createEReference(this.featureEClass, FEATURE__VALUATION);
        this.createEOperation(this.featureEClass, FEATURE___DIRECTION_FOR__TYPE);
        this.createEOperation(this.featureEClass, FEATURE___IS_FEATURED_WITHIN__TYPE);
        this.createEOperation(this.featureEClass, FEATURE___NAMING_FEATURE);
        this.createEOperation(this.featureEClass, FEATURE___REDEFINES__FEATURE);
        this.createEOperation(this.featureEClass, FEATURE___REDEFINES_FROM_LIBRARY__STRING);
        this.createEOperation(this.featureEClass, FEATURE___SUBSETS_CHAIN__FEATURE_FEATURE);

        this.featureChainExpressionEClass = this.createEClass(FEATURE_CHAIN_EXPRESSION);
        this.createEReference(this.featureChainExpressionEClass, FEATURE_CHAIN_EXPRESSION__TARGET_FEATURE);
        this.createEOperation(this.featureChainExpressionEClass, FEATURE_CHAIN_EXPRESSION___SOURCE_TARGET_FEATURE);

        this.featureChainingEClass = this.createEClass(FEATURE_CHAINING);
        this.createEReference(this.featureChainingEClass, FEATURE_CHAINING__CHAINING_FEATURE);
        this.createEReference(this.featureChainingEClass, FEATURE_CHAINING__FEATURE_CHAINED);

        this.featureInvertingEClass = this.createEClass(FEATURE_INVERTING);
        this.createEReference(this.featureInvertingEClass, FEATURE_INVERTING__FEATURE_INVERTED);
        this.createEReference(this.featureInvertingEClass, FEATURE_INVERTING__INVERTING_FEATURE);
        this.createEReference(this.featureInvertingEClass, FEATURE_INVERTING__OWNING_FEATURE);

        this.featureMembershipEClass = this.createEClass(FEATURE_MEMBERSHIP);
        this.createEReference(this.featureMembershipEClass, FEATURE_MEMBERSHIP__OWNED_MEMBER_FEATURE);
        this.createEReference(this.featureMembershipEClass, FEATURE_MEMBERSHIP__OWNING_TYPE);

        this.featureReferenceExpressionEClass = this.createEClass(FEATURE_REFERENCE_EXPRESSION);
        this.createEReference(this.featureReferenceExpressionEClass, FEATURE_REFERENCE_EXPRESSION__REFERENT);

        this.featureTypingEClass = this.createEClass(FEATURE_TYPING);
        this.createEReference(this.featureTypingEClass, FEATURE_TYPING__OWNING_FEATURE);
        this.createEReference(this.featureTypingEClass, FEATURE_TYPING__TYPE);
        this.createEReference(this.featureTypingEClass, FEATURE_TYPING__TYPED_FEATURE);

        this.featureValueEClass = this.createEClass(FEATURE_VALUE);
        this.createEAttribute(this.featureValueEClass, FEATURE_VALUE__IS_DEFAULT);
        this.createEAttribute(this.featureValueEClass, FEATURE_VALUE__IS_INITIAL);
        this.createEReference(this.featureValueEClass, FEATURE_VALUE__FEATURE_WITH_VALUE);
        this.createEReference(this.featureValueEClass, FEATURE_VALUE__VALUE);

        this.featuringEClass = this.createEClass(FEATURING);
        this.createEReference(this.featuringEClass, FEATURING__FEATURE);
        this.createEReference(this.featuringEClass, FEATURING__TYPE);

        this.flowConnectionDefinitionEClass = this.createEClass(FLOW_CONNECTION_DEFINITION);

        this.flowConnectionUsageEClass = this.createEClass(FLOW_CONNECTION_USAGE);
        this.createEReference(this.flowConnectionUsageEClass, FLOW_CONNECTION_USAGE__FLOW_CONNECTION_DEFINITION);

        this.forkNodeEClass = this.createEClass(FORK_NODE);

        this.forLoopActionUsageEClass = this.createEClass(FOR_LOOP_ACTION_USAGE);
        this.createEReference(this.forLoopActionUsageEClass, FOR_LOOP_ACTION_USAGE__LOOP_VARIABLE);
        this.createEReference(this.forLoopActionUsageEClass, FOR_LOOP_ACTION_USAGE__SEQ_ARGUMENT);

        this.framedConcernMembershipEClass = this.createEClass(FRAMED_CONCERN_MEMBERSHIP);
        this.createEReference(this.framedConcernMembershipEClass, FRAMED_CONCERN_MEMBERSHIP__OWNED_CONCERN);
        this.createEReference(this.framedConcernMembershipEClass, FRAMED_CONCERN_MEMBERSHIP__REFERENCED_CONCERN);

        this.functionEClass = this.createEClass(FUNCTION);
        this.createEAttribute(this.functionEClass, FUNCTION__IS_MODEL_LEVEL_EVALUABLE);
        this.createEReference(this.functionEClass, FUNCTION__EXPRESSION);
        this.createEReference(this.functionEClass, FUNCTION__RESULT);

        this.ifActionUsageEClass = this.createEClass(IF_ACTION_USAGE);
        this.createEReference(this.ifActionUsageEClass, IF_ACTION_USAGE__ELSE_ACTION);
        this.createEReference(this.ifActionUsageEClass, IF_ACTION_USAGE__IF_ARGUMENT);
        this.createEReference(this.ifActionUsageEClass, IF_ACTION_USAGE__THEN_ACTION);

        this.importEClass = this.createEClass(IMPORT);
        this.createEAttribute(this.importEClass, IMPORT__IS_IMPORT_ALL);
        this.createEAttribute(this.importEClass, IMPORT__IS_RECURSIVE);
        this.createEAttribute(this.importEClass, IMPORT__VISIBILITY);
        this.createEReference(this.importEClass, IMPORT__IMPORTED_ELEMENT);
        this.createEReference(this.importEClass, IMPORT__IMPORT_OWNING_NAMESPACE);
        this.createEOperation(this.importEClass, IMPORT___IMPORTED_MEMBERSHIPS__ELIST);

        this.includeUseCaseUsageEClass = this.createEClass(INCLUDE_USE_CASE_USAGE);
        this.createEReference(this.includeUseCaseUsageEClass, INCLUDE_USE_CASE_USAGE__USE_CASE_INCLUDED);

        this.interactionEClass = this.createEClass(INTERACTION);

        this.interfaceDefinitionEClass = this.createEClass(INTERFACE_DEFINITION);
        this.createEReference(this.interfaceDefinitionEClass, INTERFACE_DEFINITION__INTERFACE_END);

        this.interfaceUsageEClass = this.createEClass(INTERFACE_USAGE);
        this.createEReference(this.interfaceUsageEClass, INTERFACE_USAGE__INTERFACE_DEFINITION);

        this.intersectingEClass = this.createEClass(INTERSECTING);
        this.createEReference(this.intersectingEClass, INTERSECTING__INTERSECTING_TYPE);
        this.createEReference(this.intersectingEClass, INTERSECTING__TYPE_INTERSECTED);

        this.invariantEClass = this.createEClass(INVARIANT);
        this.createEAttribute(this.invariantEClass, INVARIANT__IS_NEGATED);

        this.invocationExpressionEClass = this.createEClass(INVOCATION_EXPRESSION);
        this.createEReference(this.invocationExpressionEClass, INVOCATION_EXPRESSION__ARGUMENT);

        this.itemDefinitionEClass = this.createEClass(ITEM_DEFINITION);

        this.itemFeatureEClass = this.createEClass(ITEM_FEATURE);

        this.itemFlowEClass = this.createEClass(ITEM_FLOW);
        this.createEReference(this.itemFlowEClass, ITEM_FLOW__INTERACTION);
        this.createEReference(this.itemFlowEClass, ITEM_FLOW__ITEM_FEATURE);
        this.createEReference(this.itemFlowEClass, ITEM_FLOW__ITEM_FLOW_END);
        this.createEReference(this.itemFlowEClass, ITEM_FLOW__ITEM_TYPE);
        this.createEReference(this.itemFlowEClass, ITEM_FLOW__SOURCE_OUTPUT_FEATURE);
        this.createEReference(this.itemFlowEClass, ITEM_FLOW__TARGET_INPUT_FEATURE);

        this.itemFlowEndEClass = this.createEClass(ITEM_FLOW_END);

        this.itemUsageEClass = this.createEClass(ITEM_USAGE);
        this.createEReference(this.itemUsageEClass, ITEM_USAGE__ITEM_DEFINITION);

        this.joinNodeEClass = this.createEClass(JOIN_NODE);

        this.libraryPackageEClass = this.createEClass(LIBRARY_PACKAGE);
        this.createEAttribute(this.libraryPackageEClass, LIBRARY_PACKAGE__IS_STANDARD);

        this.lifeClassEClass = this.createEClass(LIFE_CLASS);

        this.literalBooleanEClass = this.createEClass(LITERAL_BOOLEAN);
        this.createEAttribute(this.literalBooleanEClass, LITERAL_BOOLEAN__VALUE);

        this.literalExpressionEClass = this.createEClass(LITERAL_EXPRESSION);

        this.literalInfinityEClass = this.createEClass(LITERAL_INFINITY);

        this.literalIntegerEClass = this.createEClass(LITERAL_INTEGER);
        this.createEAttribute(this.literalIntegerEClass, LITERAL_INTEGER__VALUE);

        this.literalRationalEClass = this.createEClass(LITERAL_RATIONAL);
        this.createEAttribute(this.literalRationalEClass, LITERAL_RATIONAL__VALUE);

        this.literalStringEClass = this.createEClass(LITERAL_STRING);
        this.createEAttribute(this.literalStringEClass, LITERAL_STRING__VALUE);

        this.loopActionUsageEClass = this.createEClass(LOOP_ACTION_USAGE);
        this.createEReference(this.loopActionUsageEClass, LOOP_ACTION_USAGE__BODY_ACTION);

        this.membershipEClass = this.createEClass(MEMBERSHIP);
        this.createEAttribute(this.membershipEClass, MEMBERSHIP__MEMBER_ELEMENT_ID);
        this.createEAttribute(this.membershipEClass, MEMBERSHIP__MEMBER_NAME);
        this.createEAttribute(this.membershipEClass, MEMBERSHIP__MEMBER_SHORT_NAME);
        this.createEAttribute(this.membershipEClass, MEMBERSHIP__VISIBILITY);
        this.createEReference(this.membershipEClass, MEMBERSHIP__MEMBER_ELEMENT);
        this.createEReference(this.membershipEClass, MEMBERSHIP__MEMBERSHIP_OWNING_NAMESPACE);
        this.createEOperation(this.membershipEClass, MEMBERSHIP___IS_DISTINGUISHABLE_FROM__MEMBERSHIP);

        this.membershipExposeEClass = this.createEClass(MEMBERSHIP_EXPOSE);

        this.membershipImportEClass = this.createEClass(MEMBERSHIP_IMPORT);
        this.createEReference(this.membershipImportEClass, MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP);

        this.mergeNodeEClass = this.createEClass(MERGE_NODE);

        this.metaclassEClass = this.createEClass(METACLASS);

        this.metadataAccessExpressionEClass = this.createEClass(METADATA_ACCESS_EXPRESSION);
        this.createEReference(this.metadataAccessExpressionEClass, METADATA_ACCESS_EXPRESSION__REFERENCED_ELEMENT);
        this.createEOperation(this.metadataAccessExpressionEClass, METADATA_ACCESS_EXPRESSION___METACLASS_FEATURE);

        this.metadataDefinitionEClass = this.createEClass(METADATA_DEFINITION);

        this.metadataFeatureEClass = this.createEClass(METADATA_FEATURE);
        this.createEReference(this.metadataFeatureEClass, METADATA_FEATURE__METACLASS);
        this.createEOperation(this.metadataFeatureEClass, METADATA_FEATURE___EVALUATE_FEATURE__FEATURE);
        this.createEOperation(this.metadataFeatureEClass, METADATA_FEATURE___IS_SEMANTIC);
        this.createEOperation(this.metadataFeatureEClass, METADATA_FEATURE___IS_SYNTACTIC);
        this.createEOperation(this.metadataFeatureEClass, METADATA_FEATURE___SYNTAX_ELEMENT);

        this.metadataUsageEClass = this.createEClass(METADATA_USAGE);
        this.createEReference(this.metadataUsageEClass, METADATA_USAGE__METADATA_DEFINITION);

        this.multiplicityEClass = this.createEClass(MULTIPLICITY);

        this.multiplicityRangeEClass = this.createEClass(MULTIPLICITY_RANGE);
        this.createEReference(this.multiplicityRangeEClass, MULTIPLICITY_RANGE__BOUND);
        this.createEReference(this.multiplicityRangeEClass, MULTIPLICITY_RANGE__LOWER_BOUND);
        this.createEReference(this.multiplicityRangeEClass, MULTIPLICITY_RANGE__UPPER_BOUND);
        this.createEOperation(this.multiplicityRangeEClass, MULTIPLICITY_RANGE___HAS_BOUNDS__INT_INT);
        this.createEOperation(this.multiplicityRangeEClass, MULTIPLICITY_RANGE___VALUE_OF__EXPRESSION);

        this.namespaceEClass = this.createEClass(NAMESPACE);
        this.createEReference(this.namespaceEClass, NAMESPACE__IMPORTED_MEMBERSHIP);
        this.createEReference(this.namespaceEClass, NAMESPACE__MEMBER);
        this.createEReference(this.namespaceEClass, NAMESPACE__MEMBERSHIP);
        this.createEReference(this.namespaceEClass, NAMESPACE__OWNED_IMPORT);
        this.createEReference(this.namespaceEClass, NAMESPACE__OWNED_MEMBER);
        this.createEReference(this.namespaceEClass, NAMESPACE__OWNED_MEMBERSHIP);
        this.createEOperation(this.namespaceEClass, NAMESPACE___IMPORTED_MEMBERSHIPS__ELIST);
        this.createEOperation(this.namespaceEClass, NAMESPACE___NAMES_OF__ELEMENT);
        this.createEOperation(this.namespaceEClass, NAMESPACE___QUALIFICATION_OF__STRING);
        this.createEOperation(this.namespaceEClass, NAMESPACE___RESOLVE__STRING);
        this.createEOperation(this.namespaceEClass, NAMESPACE___RESOLVE_GLOBAL__STRING);
        this.createEOperation(this.namespaceEClass, NAMESPACE___RESOLVE_LOCAL__STRING);
        this.createEOperation(this.namespaceEClass, NAMESPACE___RESOLVE_VISIBLE__STRING);
        this.createEOperation(this.namespaceEClass, NAMESPACE___UNQUALIFIED_NAME_OF__STRING);
        this.createEOperation(this.namespaceEClass, NAMESPACE___VISIBILITY_OF__MEMBERSHIP);
        this.createEOperation(this.namespaceEClass, NAMESPACE___VISIBLE_MEMBERSHIPS__ELIST_BOOLEAN_BOOLEAN);

        this.namespaceExposeEClass = this.createEClass(NAMESPACE_EXPOSE);

        this.namespaceImportEClass = this.createEClass(NAMESPACE_IMPORT);
        this.createEReference(this.namespaceImportEClass, NAMESPACE_IMPORT__IMPORTED_NAMESPACE);

        this.nullExpressionEClass = this.createEClass(NULL_EXPRESSION);

        this.objectiveMembershipEClass = this.createEClass(OBJECTIVE_MEMBERSHIP);
        this.createEReference(this.objectiveMembershipEClass, OBJECTIVE_MEMBERSHIP__OWNED_OBJECTIVE_REQUIREMENT);

        this.occurrenceDefinitionEClass = this.createEClass(OCCURRENCE_DEFINITION);
        this.createEAttribute(this.occurrenceDefinitionEClass, OCCURRENCE_DEFINITION__IS_INDIVIDUAL);
        this.createEReference(this.occurrenceDefinitionEClass, OCCURRENCE_DEFINITION__LIFE_CLASS);

        this.occurrenceUsageEClass = this.createEClass(OCCURRENCE_USAGE);
        this.createEAttribute(this.occurrenceUsageEClass, OCCURRENCE_USAGE__IS_INDIVIDUAL);
        this.createEAttribute(this.occurrenceUsageEClass, OCCURRENCE_USAGE__PORTION_KIND);
        this.createEReference(this.occurrenceUsageEClass, OCCURRENCE_USAGE__INDIVIDUAL_DEFINITION);
        this.createEReference(this.occurrenceUsageEClass, OCCURRENCE_USAGE__OCCURRENCE_DEFINITION);

        this.operatorExpressionEClass = this.createEClass(OPERATOR_EXPRESSION);
        this.createEAttribute(this.operatorExpressionEClass, OPERATOR_EXPRESSION__OPERATOR);
        this.createEReference(this.operatorExpressionEClass, OPERATOR_EXPRESSION__OPERAND);

        this.owningMembershipEClass = this.createEClass(OWNING_MEMBERSHIP);
        this.createEAttribute(this.owningMembershipEClass, OWNING_MEMBERSHIP__OWNED_MEMBER_ELEMENT_ID);
        this.createEAttribute(this.owningMembershipEClass, OWNING_MEMBERSHIP__OWNED_MEMBER_NAME);
        this.createEAttribute(this.owningMembershipEClass, OWNING_MEMBERSHIP__OWNED_MEMBER_SHORT_NAME);
        this.createEReference(this.owningMembershipEClass, OWNING_MEMBERSHIP__OWNED_MEMBER_ELEMENT);

        this.packageEClass = this.createEClass(PACKAGE);
        this.createEReference(this.packageEClass, PACKAGE__FILTER_CONDITION);
        this.createEOperation(this.packageEClass, PACKAGE___INCLUDE_AS_MEMBER__ELEMENT);

        this.parameterMembershipEClass = this.createEClass(PARAMETER_MEMBERSHIP);
        this.createEReference(this.parameterMembershipEClass, PARAMETER_MEMBERSHIP__OWNED_MEMBER_PARAMETER);

        this.partDefinitionEClass = this.createEClass(PART_DEFINITION);

        this.partUsageEClass = this.createEClass(PART_USAGE);
        this.createEReference(this.partUsageEClass, PART_USAGE__PART_DEFINITION);

        this.performActionUsageEClass = this.createEClass(PERFORM_ACTION_USAGE);
        this.createEReference(this.performActionUsageEClass, PERFORM_ACTION_USAGE__PERFORMED_ACTION);

        this.portConjugationEClass = this.createEClass(PORT_CONJUGATION);
        this.createEReference(this.portConjugationEClass, PORT_CONJUGATION__CONJUGATED_PORT_DEFINITION);
        this.createEReference(this.portConjugationEClass, PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION);

        this.portDefinitionEClass = this.createEClass(PORT_DEFINITION);
        this.createEReference(this.portDefinitionEClass, PORT_DEFINITION__CONJUGATED_PORT_DEFINITION);

        this.portUsageEClass = this.createEClass(PORT_USAGE);
        this.createEReference(this.portUsageEClass, PORT_USAGE__PORT_DEFINITION);

        this.predicateEClass = this.createEClass(PREDICATE);

        this.redefinitionEClass = this.createEClass(REDEFINITION);
        this.createEReference(this.redefinitionEClass, REDEFINITION__REDEFINED_FEATURE);
        this.createEReference(this.redefinitionEClass, REDEFINITION__REDEFINING_FEATURE);

        this.referenceSubsettingEClass = this.createEClass(REFERENCE_SUBSETTING);
        this.createEReference(this.referenceSubsettingEClass, REFERENCE_SUBSETTING__REFERENCED_FEATURE);
        this.createEReference(this.referenceSubsettingEClass, REFERENCE_SUBSETTING__REFERENCING_FEATURE);

        this.referenceUsageEClass = this.createEClass(REFERENCE_USAGE);

        this.relationshipEClass = this.createEClass(RELATIONSHIP);
        this.createEAttribute(this.relationshipEClass, RELATIONSHIP__IS_IMPLIED);
        this.createEReference(this.relationshipEClass, RELATIONSHIP__OWNED_RELATED_ELEMENT);
        this.createEReference(this.relationshipEClass, RELATIONSHIP__OWNING_RELATED_ELEMENT);
        this.createEReference(this.relationshipEClass, RELATIONSHIP__RELATED_ELEMENT);
        this.createEReference(this.relationshipEClass, RELATIONSHIP__SOURCE);
        this.createEReference(this.relationshipEClass, RELATIONSHIP__TARGET);

        this.renderingDefinitionEClass = this.createEClass(RENDERING_DEFINITION);
        this.createEReference(this.renderingDefinitionEClass, RENDERING_DEFINITION__RENDERING);

        this.renderingUsageEClass = this.createEClass(RENDERING_USAGE);
        this.createEReference(this.renderingUsageEClass, RENDERING_USAGE__RENDERING_DEFINITION);

        this.requirementConstraintMembershipEClass = this.createEClass(REQUIREMENT_CONSTRAINT_MEMBERSHIP);
        this.createEAttribute(this.requirementConstraintMembershipEClass, REQUIREMENT_CONSTRAINT_MEMBERSHIP__KIND);
        this.createEReference(this.requirementConstraintMembershipEClass, REQUIREMENT_CONSTRAINT_MEMBERSHIP__OWNED_CONSTRAINT);
        this.createEReference(this.requirementConstraintMembershipEClass, REQUIREMENT_CONSTRAINT_MEMBERSHIP__REFERENCED_CONSTRAINT);

        this.requirementDefinitionEClass = this.createEClass(REQUIREMENT_DEFINITION);
        this.createEAttribute(this.requirementDefinitionEClass, REQUIREMENT_DEFINITION__REQ_ID);
        this.createEAttribute(this.requirementDefinitionEClass, REQUIREMENT_DEFINITION__TEXT);
        this.createEReference(this.requirementDefinitionEClass, REQUIREMENT_DEFINITION__ACTOR_PARAMETER);
        this.createEReference(this.requirementDefinitionEClass, REQUIREMENT_DEFINITION__ASSUMED_CONSTRAINT);
        this.createEReference(this.requirementDefinitionEClass, REQUIREMENT_DEFINITION__FRAMED_CONCERN);
        this.createEReference(this.requirementDefinitionEClass, REQUIREMENT_DEFINITION__REQUIRED_CONSTRAINT);
        this.createEReference(this.requirementDefinitionEClass, REQUIREMENT_DEFINITION__STAKEHOLDER_PARAMETER);
        this.createEReference(this.requirementDefinitionEClass, REQUIREMENT_DEFINITION__SUBJECT_PARAMETER);

        this.requirementUsageEClass = this.createEClass(REQUIREMENT_USAGE);
        this.createEAttribute(this.requirementUsageEClass, REQUIREMENT_USAGE__REQ_ID);
        this.createEAttribute(this.requirementUsageEClass, REQUIREMENT_USAGE__TEXT);
        this.createEReference(this.requirementUsageEClass, REQUIREMENT_USAGE__ACTOR_PARAMETER);
        this.createEReference(this.requirementUsageEClass, REQUIREMENT_USAGE__ASSUMED_CONSTRAINT);
        this.createEReference(this.requirementUsageEClass, REQUIREMENT_USAGE__FRAMED_CONCERN);
        this.createEReference(this.requirementUsageEClass, REQUIREMENT_USAGE__REQUIRED_CONSTRAINT);
        this.createEReference(this.requirementUsageEClass, REQUIREMENT_USAGE__REQUIREMENT_DEFINITION);
        this.createEReference(this.requirementUsageEClass, REQUIREMENT_USAGE__STAKEHOLDER_PARAMETER);
        this.createEReference(this.requirementUsageEClass, REQUIREMENT_USAGE__SUBJECT_PARAMETER);

        this.requirementVerificationMembershipEClass = this.createEClass(REQUIREMENT_VERIFICATION_MEMBERSHIP);
        this.createEReference(this.requirementVerificationMembershipEClass, REQUIREMENT_VERIFICATION_MEMBERSHIP__OWNED_REQUIREMENT);
        this.createEReference(this.requirementVerificationMembershipEClass, REQUIREMENT_VERIFICATION_MEMBERSHIP__VERIFIED_REQUIREMENT);

        this.resultExpressionMembershipEClass = this.createEClass(RESULT_EXPRESSION_MEMBERSHIP);
        this.createEReference(this.resultExpressionMembershipEClass, RESULT_EXPRESSION_MEMBERSHIP__OWNED_RESULT_EXPRESSION);

        this.returnParameterMembershipEClass = this.createEClass(RETURN_PARAMETER_MEMBERSHIP);

        this.satisfyRequirementUsageEClass = this.createEClass(SATISFY_REQUIREMENT_USAGE);
        this.createEReference(this.satisfyRequirementUsageEClass, SATISFY_REQUIREMENT_USAGE__SATISFIED_REQUIREMENT);
        this.createEReference(this.satisfyRequirementUsageEClass, SATISFY_REQUIREMENT_USAGE__SATISFYING_FEATURE);

        this.selectExpressionEClass = this.createEClass(SELECT_EXPRESSION);

        this.sendActionUsageEClass = this.createEClass(SEND_ACTION_USAGE);
        this.createEReference(this.sendActionUsageEClass, SEND_ACTION_USAGE__PAYLOAD_ARGUMENT);
        this.createEReference(this.sendActionUsageEClass, SEND_ACTION_USAGE__RECEIVER_ARGUMENT);
        this.createEReference(this.sendActionUsageEClass, SEND_ACTION_USAGE__SENDER_ARGUMENT);

        this.specializationEClass = this.createEClass(SPECIALIZATION);
        this.createEReference(this.specializationEClass, SPECIALIZATION__GENERAL);
        this.createEReference(this.specializationEClass, SPECIALIZATION__OWNING_TYPE);
        this.createEReference(this.specializationEClass, SPECIALIZATION__SPECIFIC);

        this.stakeholderMembershipEClass = this.createEClass(STAKEHOLDER_MEMBERSHIP);
        this.createEReference(this.stakeholderMembershipEClass, STAKEHOLDER_MEMBERSHIP__OWNED_STAKEHOLDER_PARAMETER);

        this.stateDefinitionEClass = this.createEClass(STATE_DEFINITION);
        this.createEAttribute(this.stateDefinitionEClass, STATE_DEFINITION__IS_PARALLEL);
        this.createEReference(this.stateDefinitionEClass, STATE_DEFINITION__DO_ACTION);
        this.createEReference(this.stateDefinitionEClass, STATE_DEFINITION__ENTRY_ACTION);
        this.createEReference(this.stateDefinitionEClass, STATE_DEFINITION__EXIT_ACTION);
        this.createEReference(this.stateDefinitionEClass, STATE_DEFINITION__STATE);

        this.stateSubactionMembershipEClass = this.createEClass(STATE_SUBACTION_MEMBERSHIP);
        this.createEAttribute(this.stateSubactionMembershipEClass, STATE_SUBACTION_MEMBERSHIP__KIND);
        this.createEReference(this.stateSubactionMembershipEClass, STATE_SUBACTION_MEMBERSHIP__ACTION);

        this.stateUsageEClass = this.createEClass(STATE_USAGE);
        this.createEAttribute(this.stateUsageEClass, STATE_USAGE__IS_PARALLEL);
        this.createEReference(this.stateUsageEClass, STATE_USAGE__DO_ACTION);
        this.createEReference(this.stateUsageEClass, STATE_USAGE__ENTRY_ACTION);
        this.createEReference(this.stateUsageEClass, STATE_USAGE__EXIT_ACTION);
        this.createEReference(this.stateUsageEClass, STATE_USAGE__STATE_DEFINITION);
        this.createEOperation(this.stateUsageEClass, STATE_USAGE___IS_SUBSTATE_USAGE__BOOLEAN);

        this.stepEClass = this.createEClass(STEP);
        this.createEReference(this.stepEClass, STEP__BEHAVIOR);
        this.createEReference(this.stepEClass, STEP__PARAMETER);

        this.structureEClass = this.createEClass(STRUCTURE);

        this.subclassificationEClass = this.createEClass(SUBCLASSIFICATION);
        this.createEReference(this.subclassificationEClass, SUBCLASSIFICATION__OWNING_CLASSIFIER);
        this.createEReference(this.subclassificationEClass, SUBCLASSIFICATION__SUBCLASSIFIER);
        this.createEReference(this.subclassificationEClass, SUBCLASSIFICATION__SUPERCLASSIFIER);

        this.subjectMembershipEClass = this.createEClass(SUBJECT_MEMBERSHIP);
        this.createEReference(this.subjectMembershipEClass, SUBJECT_MEMBERSHIP__OWNED_SUBJECT_PARAMETER);

        this.subsettingEClass = this.createEClass(SUBSETTING);
        this.createEReference(this.subsettingEClass, SUBSETTING__OWNING_FEATURE);
        this.createEReference(this.subsettingEClass, SUBSETTING__SUBSETTED_FEATURE);
        this.createEReference(this.subsettingEClass, SUBSETTING__SUBSETTING_FEATURE);

        this.successionEClass = this.createEClass(SUCCESSION);
        this.createEReference(this.successionEClass, SUCCESSION__EFFECT_STEP);
        this.createEReference(this.successionEClass, SUCCESSION__GUARD_EXPRESSION);
        this.createEReference(this.successionEClass, SUCCESSION__TRANSITION_STEP);
        this.createEReference(this.successionEClass, SUCCESSION__TRIGGER_STEP);

        this.successionAsUsageEClass = this.createEClass(SUCCESSION_AS_USAGE);

        this.successionFlowConnectionUsageEClass = this.createEClass(SUCCESSION_FLOW_CONNECTION_USAGE);

        this.successionItemFlowEClass = this.createEClass(SUCCESSION_ITEM_FLOW);

        this.textualRepresentationEClass = this.createEClass(TEXTUAL_REPRESENTATION);
        this.createEAttribute(this.textualRepresentationEClass, TEXTUAL_REPRESENTATION__BODY);
        this.createEAttribute(this.textualRepresentationEClass, TEXTUAL_REPRESENTATION__LANGUAGE);
        this.createEReference(this.textualRepresentationEClass, TEXTUAL_REPRESENTATION__REPRESENTED_ELEMENT);

        this.transitionFeatureMembershipEClass = this.createEClass(TRANSITION_FEATURE_MEMBERSHIP);
        this.createEAttribute(this.transitionFeatureMembershipEClass, TRANSITION_FEATURE_MEMBERSHIP__KIND);
        this.createEReference(this.transitionFeatureMembershipEClass, TRANSITION_FEATURE_MEMBERSHIP__TRANSITION_FEATURE);

        this.transitionUsageEClass = this.createEClass(TRANSITION_USAGE);
        this.createEReference(this.transitionUsageEClass, TRANSITION_USAGE__EFFECT_ACTION);
        this.createEReference(this.transitionUsageEClass, TRANSITION_USAGE__GUARD_EXPRESSION);
        this.createEReference(this.transitionUsageEClass, TRANSITION_USAGE__SOURCE);
        this.createEReference(this.transitionUsageEClass, TRANSITION_USAGE__SUCCESSION);
        this.createEReference(this.transitionUsageEClass, TRANSITION_USAGE__TARGET);
        this.createEReference(this.transitionUsageEClass, TRANSITION_USAGE__TRIGGER_ACTION);
        this.createEOperation(this.transitionUsageEClass, TRANSITION_USAGE___TRIGGER_PAYLOAD_PARAMETER);

        this.triggerInvocationExpressionEClass = this.createEClass(TRIGGER_INVOCATION_EXPRESSION);
        this.createEAttribute(this.triggerInvocationExpressionEClass, TRIGGER_INVOCATION_EXPRESSION__KIND);

        this.typeEClass = this.createEClass(TYPE);
        this.createEAttribute(this.typeEClass, TYPE__IS_ABSTRACT);
        this.createEAttribute(this.typeEClass, TYPE__IS_CONJUGATED);
        this.createEAttribute(this.typeEClass, TYPE__IS_SUFFICIENT);
        this.createEReference(this.typeEClass, TYPE__DIFFERENCING_TYPE);
        this.createEReference(this.typeEClass, TYPE__DIRECTED_FEATURE);
        this.createEReference(this.typeEClass, TYPE__END_FEATURE);
        this.createEReference(this.typeEClass, TYPE__FEATURE);
        this.createEReference(this.typeEClass, TYPE__FEATURE_MEMBERSHIP);
        this.createEReference(this.typeEClass, TYPE__INHERITED_FEATURE);
        this.createEReference(this.typeEClass, TYPE__INHERITED_MEMBERSHIP);
        this.createEReference(this.typeEClass, TYPE__INPUT);
        this.createEReference(this.typeEClass, TYPE__INTERSECTING_TYPE);
        this.createEReference(this.typeEClass, TYPE__MULTIPLICITY);
        this.createEReference(this.typeEClass, TYPE__OUTPUT);
        this.createEReference(this.typeEClass, TYPE__OWNED_CONJUGATOR);
        this.createEReference(this.typeEClass, TYPE__OWNED_DIFFERENCING);
        this.createEReference(this.typeEClass, TYPE__OWNED_DISJOINING);
        this.createEReference(this.typeEClass, TYPE__OWNED_END_FEATURE);
        this.createEReference(this.typeEClass, TYPE__OWNED_FEATURE);
        this.createEReference(this.typeEClass, TYPE__OWNED_FEATURE_MEMBERSHIP);
        this.createEReference(this.typeEClass, TYPE__OWNED_INTERSECTING);
        this.createEReference(this.typeEClass, TYPE__OWNED_SPECIALIZATION);
        this.createEReference(this.typeEClass, TYPE__OWNED_UNIONING);
        this.createEReference(this.typeEClass, TYPE__UNIONING_TYPE);
        this.createEOperation(this.typeEClass, TYPE___ALL_SUPERTYPES);
        this.createEOperation(this.typeEClass, TYPE___DIRECTION_OF__FEATURE);
        this.createEOperation(this.typeEClass, TYPE___INHERITED_MEMBERSHIPS__ELIST);
        this.createEOperation(this.typeEClass, TYPE___SPECIALIZES__TYPE);
        this.createEOperation(this.typeEClass, TYPE___SPECIALIZES_FROM_LIBRARY__STRING);

        this.typeFeaturingEClass = this.createEClass(TYPE_FEATURING);
        this.createEReference(this.typeFeaturingEClass, TYPE_FEATURING__FEATURE_OF_TYPE);
        this.createEReference(this.typeFeaturingEClass, TYPE_FEATURING__FEATURING_TYPE);
        this.createEReference(this.typeFeaturingEClass, TYPE_FEATURING__OWNING_FEATURE_OF_TYPE);

        this.unioningEClass = this.createEClass(UNIONING);
        this.createEReference(this.unioningEClass, UNIONING__TYPE_UNIONED);
        this.createEReference(this.unioningEClass, UNIONING__UNIONING_TYPE);

        this.usageEClass = this.createEClass(USAGE);
        this.createEAttribute(this.usageEClass, USAGE__IS_REFERENCE);
        this.createEAttribute(this.usageEClass, USAGE__IS_VARIATION);
        this.createEReference(this.usageEClass, USAGE__DEFINITION);
        this.createEReference(this.usageEClass, USAGE__DIRECTED_USAGE);
        this.createEReference(this.usageEClass, USAGE__NESTED_ACTION);
        this.createEReference(this.usageEClass, USAGE__NESTED_ALLOCATION);
        this.createEReference(this.usageEClass, USAGE__NESTED_ANALYSIS_CASE);
        this.createEReference(this.usageEClass, USAGE__NESTED_ATTRIBUTE);
        this.createEReference(this.usageEClass, USAGE__NESTED_CALCULATION);
        this.createEReference(this.usageEClass, USAGE__NESTED_CASE);
        this.createEReference(this.usageEClass, USAGE__NESTED_CONCERN);
        this.createEReference(this.usageEClass, USAGE__NESTED_CONNECTION);
        this.createEReference(this.usageEClass, USAGE__NESTED_CONSTRAINT);
        this.createEReference(this.usageEClass, USAGE__NESTED_ENUMERATION);
        this.createEReference(this.usageEClass, USAGE__NESTED_FLOW);
        this.createEReference(this.usageEClass, USAGE__NESTED_INTERFACE);
        this.createEReference(this.usageEClass, USAGE__NESTED_ITEM);
        this.createEReference(this.usageEClass, USAGE__NESTED_METADATA);
        this.createEReference(this.usageEClass, USAGE__NESTED_OCCURRENCE);
        this.createEReference(this.usageEClass, USAGE__NESTED_PART);
        this.createEReference(this.usageEClass, USAGE__NESTED_PORT);
        this.createEReference(this.usageEClass, USAGE__NESTED_REFERENCE);
        this.createEReference(this.usageEClass, USAGE__NESTED_RENDERING);
        this.createEReference(this.usageEClass, USAGE__NESTED_REQUIREMENT);
        this.createEReference(this.usageEClass, USAGE__NESTED_STATE);
        this.createEReference(this.usageEClass, USAGE__NESTED_TRANSITION);
        this.createEReference(this.usageEClass, USAGE__NESTED_USAGE);
        this.createEReference(this.usageEClass, USAGE__NESTED_USE_CASE);
        this.createEReference(this.usageEClass, USAGE__NESTED_VERIFICATION_CASE);
        this.createEReference(this.usageEClass, USAGE__NESTED_VIEW);
        this.createEReference(this.usageEClass, USAGE__NESTED_VIEWPOINT);
        this.createEReference(this.usageEClass, USAGE__OWNING_DEFINITION);
        this.createEReference(this.usageEClass, USAGE__OWNING_USAGE);
        this.createEReference(this.usageEClass, USAGE__USAGE);
        this.createEReference(this.usageEClass, USAGE__VARIANT);
        this.createEReference(this.usageEClass, USAGE__VARIANT_MEMBERSHIP);

        this.useCaseDefinitionEClass = this.createEClass(USE_CASE_DEFINITION);
        this.createEReference(this.useCaseDefinitionEClass, USE_CASE_DEFINITION__INCLUDED_USE_CASE);

        this.useCaseUsageEClass = this.createEClass(USE_CASE_USAGE);
        this.createEReference(this.useCaseUsageEClass, USE_CASE_USAGE__INCLUDED_USE_CASE);
        this.createEReference(this.useCaseUsageEClass, USE_CASE_USAGE__USE_CASE_DEFINITION);

        this.variantMembershipEClass = this.createEClass(VARIANT_MEMBERSHIP);
        this.createEReference(this.variantMembershipEClass, VARIANT_MEMBERSHIP__OWNED_VARIANT_USAGE);

        this.verificationCaseDefinitionEClass = this.createEClass(VERIFICATION_CASE_DEFINITION);
        this.createEReference(this.verificationCaseDefinitionEClass, VERIFICATION_CASE_DEFINITION__VERIFIED_REQUIREMENT);

        this.verificationCaseUsageEClass = this.createEClass(VERIFICATION_CASE_USAGE);
        this.createEReference(this.verificationCaseUsageEClass, VERIFICATION_CASE_USAGE__VERIFICATION_CASE_DEFINITION);
        this.createEReference(this.verificationCaseUsageEClass, VERIFICATION_CASE_USAGE__VERIFIED_REQUIREMENT);

        this.viewDefinitionEClass = this.createEClass(VIEW_DEFINITION);
        this.createEReference(this.viewDefinitionEClass, VIEW_DEFINITION__SATISFIED_VIEWPOINT);
        this.createEReference(this.viewDefinitionEClass, VIEW_DEFINITION__VIEW);
        this.createEReference(this.viewDefinitionEClass, VIEW_DEFINITION__VIEW_CONDITION);
        this.createEReference(this.viewDefinitionEClass, VIEW_DEFINITION__VIEW_RENDERING);

        this.viewpointDefinitionEClass = this.createEClass(VIEWPOINT_DEFINITION);
        this.createEReference(this.viewpointDefinitionEClass, VIEWPOINT_DEFINITION__VIEWPOINT_STAKEHOLDER);

        this.viewpointUsageEClass = this.createEClass(VIEWPOINT_USAGE);
        this.createEReference(this.viewpointUsageEClass, VIEWPOINT_USAGE__VIEWPOINT_DEFINITION);
        this.createEReference(this.viewpointUsageEClass, VIEWPOINT_USAGE__VIEWPOINT_STAKEHOLDER);

        this.viewRenderingMembershipEClass = this.createEClass(VIEW_RENDERING_MEMBERSHIP);
        this.createEReference(this.viewRenderingMembershipEClass, VIEW_RENDERING_MEMBERSHIP__OWNED_RENDERING);
        this.createEReference(this.viewRenderingMembershipEClass, VIEW_RENDERING_MEMBERSHIP__REFERENCED_RENDERING);

        this.viewUsageEClass = this.createEClass(VIEW_USAGE);
        this.createEReference(this.viewUsageEClass, VIEW_USAGE__EXPOSED_ELEMENT);
        this.createEReference(this.viewUsageEClass, VIEW_USAGE__SATISFIED_VIEWPOINT);
        this.createEReference(this.viewUsageEClass, VIEW_USAGE__VIEW_CONDITION);
        this.createEReference(this.viewUsageEClass, VIEW_USAGE__VIEW_DEFINITION);
        this.createEReference(this.viewUsageEClass, VIEW_USAGE__VIEW_RENDERING);
        this.createEOperation(this.viewUsageEClass, VIEW_USAGE___INCLUDE_AS_EXPOSED__ELEMENT);

        this.whileLoopActionUsageEClass = this.createEClass(WHILE_LOOP_ACTION_USAGE);
        this.createEReference(this.whileLoopActionUsageEClass, WHILE_LOOP_ACTION_USAGE__UNTIL_ARGUMENT);
        this.createEReference(this.whileLoopActionUsageEClass, WHILE_LOOP_ACTION_USAGE__WHILE_ARGUMENT);

        // Create enums
        this.featureDirectionKindEEnum = this.createEEnum(FEATURE_DIRECTION_KIND);
        this.portionKindEEnum = this.createEEnum(PORTION_KIND);
        this.requirementConstraintKindEEnum = this.createEEnum(REQUIREMENT_CONSTRAINT_KIND);
        this.stateSubactionKindEEnum = this.createEEnum(STATE_SUBACTION_KIND);
        this.transitionFeatureKindEEnum = this.createEEnum(TRANSITION_FEATURE_KIND);
        this.triggerKindEEnum = this.createEEnum(TRIGGER_KIND);
        this.visibilityKindEEnum = this.createEEnum(VISIBILITY_KIND);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have no affect on any
     * invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void initializePackageContents() {
        if (this.isInitialized)
            return;
        this.isInitialized = true;

        // Initialize package
        this.setName(eNAME);
        this.setNsPrefix(eNS_PREFIX);
        this.setNsURI(eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        this.acceptActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.actionDefinitionEClass.getESuperTypes().add(this.getOccurrenceDefinition());
        this.actionDefinitionEClass.getESuperTypes().add(this.getBehavior());
        this.actionUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        this.actionUsageEClass.getESuperTypes().add(this.getStep());
        this.actorMembershipEClass.getESuperTypes().add(this.getParameterMembership());
        this.allocationDefinitionEClass.getESuperTypes().add(this.getConnectionDefinition());
        this.allocationUsageEClass.getESuperTypes().add(this.getConnectionUsage());
        this.analysisCaseDefinitionEClass.getESuperTypes().add(this.getCaseDefinition());
        this.analysisCaseUsageEClass.getESuperTypes().add(this.getCaseUsage());
        this.annotatingElementEClass.getESuperTypes().add(this.getElement());
        this.annotationEClass.getESuperTypes().add(this.getRelationship());
        this.assertConstraintUsageEClass.getESuperTypes().add(this.getConstraintUsage());
        this.assertConstraintUsageEClass.getESuperTypes().add(this.getInvariant());
        this.assignmentActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.associationEClass.getESuperTypes().add(this.getClassifier());
        this.associationEClass.getESuperTypes().add(this.getRelationship());
        this.associationStructureEClass.getESuperTypes().add(this.getAssociation());
        this.associationStructureEClass.getESuperTypes().add(this.getStructure());
        this.attributeDefinitionEClass.getESuperTypes().add(this.getDefinition());
        this.attributeDefinitionEClass.getESuperTypes().add(this.getDataType());
        this.attributeUsageEClass.getESuperTypes().add(this.getUsage());
        this.behaviorEClass.getESuperTypes().add(this.getClass_());
        this.bindingConnectorEClass.getESuperTypes().add(this.getConnector());
        this.bindingConnectorAsUsageEClass.getESuperTypes().add(this.getConnectorAsUsage());
        this.bindingConnectorAsUsageEClass.getESuperTypes().add(this.getBindingConnector());
        this.booleanExpressionEClass.getESuperTypes().add(this.getExpression());
        this.calculationDefinitionEClass.getESuperTypes().add(this.getActionDefinition());
        this.calculationDefinitionEClass.getESuperTypes().add(this.getFunction());
        this.calculationUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.calculationUsageEClass.getESuperTypes().add(this.getExpression());
        this.caseDefinitionEClass.getESuperTypes().add(this.getCalculationDefinition());
        this.caseUsageEClass.getESuperTypes().add(this.getCalculationUsage());
        this.classEClass.getESuperTypes().add(this.getClassifier());
        this.classifierEClass.getESuperTypes().add(this.getType());
        this.collectExpressionEClass.getESuperTypes().add(this.getOperatorExpression());
        this.commentEClass.getESuperTypes().add(this.getAnnotatingElement());
        this.concernDefinitionEClass.getESuperTypes().add(this.getRequirementDefinition());
        this.concernUsageEClass.getESuperTypes().add(this.getRequirementUsage());
        this.conjugatedPortDefinitionEClass.getESuperTypes().add(this.getPortDefinition());
        this.conjugatedPortTypingEClass.getESuperTypes().add(this.getFeatureTyping());
        this.conjugationEClass.getESuperTypes().add(this.getRelationship());
        this.connectionDefinitionEClass.getESuperTypes().add(this.getPartDefinition());
        this.connectionDefinitionEClass.getESuperTypes().add(this.getAssociationStructure());
        this.connectionUsageEClass.getESuperTypes().add(this.getConnectorAsUsage());
        this.connectionUsageEClass.getESuperTypes().add(this.getPartUsage());
        this.connectorEClass.getESuperTypes().add(this.getFeature());
        this.connectorEClass.getESuperTypes().add(this.getRelationship());
        this.connectorAsUsageEClass.getESuperTypes().add(this.getUsage());
        this.connectorAsUsageEClass.getESuperTypes().add(this.getConnector());
        this.constraintDefinitionEClass.getESuperTypes().add(this.getOccurrenceDefinition());
        this.constraintDefinitionEClass.getESuperTypes().add(this.getPredicate());
        this.constraintUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        this.constraintUsageEClass.getESuperTypes().add(this.getBooleanExpression());
        this.controlNodeEClass.getESuperTypes().add(this.getActionUsage());
        this.dataTypeEClass.getESuperTypes().add(this.getClassifier());
        this.decisionNodeEClass.getESuperTypes().add(this.getControlNode());
        this.definitionEClass.getESuperTypes().add(this.getClassifier());
        this.dependencyEClass.getESuperTypes().add(this.getRelationship());
        this.differencingEClass.getESuperTypes().add(this.getRelationship());
        this.disjoiningEClass.getESuperTypes().add(this.getRelationship());
        this.documentationEClass.getESuperTypes().add(this.getComment());
        this.elementFilterMembershipEClass.getESuperTypes().add(this.getOwningMembership());
        this.endFeatureMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        this.enumerationDefinitionEClass.getESuperTypes().add(this.getAttributeDefinition());
        this.enumerationUsageEClass.getESuperTypes().add(this.getAttributeUsage());
        this.eventOccurrenceUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        this.exhibitStateUsageEClass.getESuperTypes().add(this.getStateUsage());
        this.exhibitStateUsageEClass.getESuperTypes().add(this.getPerformActionUsage());
        this.exposeEClass.getESuperTypes().add(this.getImport());
        this.expressionEClass.getESuperTypes().add(this.getStep());
        this.featureEClass.getESuperTypes().add(this.getType());
        this.featureChainExpressionEClass.getESuperTypes().add(this.getOperatorExpression());
        this.featureChainingEClass.getESuperTypes().add(this.getRelationship());
        this.featureInvertingEClass.getESuperTypes().add(this.getRelationship());
        this.featureMembershipEClass.getESuperTypes().add(this.getOwningMembership());
        this.featureMembershipEClass.getESuperTypes().add(this.getFeaturing());
        this.featureReferenceExpressionEClass.getESuperTypes().add(this.getExpression());
        this.featureTypingEClass.getESuperTypes().add(this.getSpecialization());
        this.featureValueEClass.getESuperTypes().add(this.getOwningMembership());
        this.featuringEClass.getESuperTypes().add(this.getRelationship());
        this.flowConnectionDefinitionEClass.getESuperTypes().add(this.getConnectionDefinition());
        this.flowConnectionDefinitionEClass.getESuperTypes().add(this.getActionDefinition());
        this.flowConnectionDefinitionEClass.getESuperTypes().add(this.getInteraction());
        this.flowConnectionUsageEClass.getESuperTypes().add(this.getConnectionUsage());
        this.flowConnectionUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.flowConnectionUsageEClass.getESuperTypes().add(this.getItemFlow());
        this.forkNodeEClass.getESuperTypes().add(this.getControlNode());
        this.forLoopActionUsageEClass.getESuperTypes().add(this.getLoopActionUsage());
        this.framedConcernMembershipEClass.getESuperTypes().add(this.getRequirementConstraintMembership());
        this.functionEClass.getESuperTypes().add(this.getBehavior());
        this.ifActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.importEClass.getESuperTypes().add(this.getRelationship());
        this.includeUseCaseUsageEClass.getESuperTypes().add(this.getUseCaseUsage());
        this.includeUseCaseUsageEClass.getESuperTypes().add(this.getPerformActionUsage());
        this.interactionEClass.getESuperTypes().add(this.getAssociation());
        this.interactionEClass.getESuperTypes().add(this.getBehavior());
        this.interfaceDefinitionEClass.getESuperTypes().add(this.getConnectionDefinition());
        this.interfaceUsageEClass.getESuperTypes().add(this.getConnectionUsage());
        this.intersectingEClass.getESuperTypes().add(this.getRelationship());
        this.invariantEClass.getESuperTypes().add(this.getBooleanExpression());
        this.invocationExpressionEClass.getESuperTypes().add(this.getExpression());
        this.itemDefinitionEClass.getESuperTypes().add(this.getOccurrenceDefinition());
        this.itemDefinitionEClass.getESuperTypes().add(this.getStructure());
        this.itemFeatureEClass.getESuperTypes().add(this.getFeature());
        this.itemFlowEClass.getESuperTypes().add(this.getConnector());
        this.itemFlowEClass.getESuperTypes().add(this.getStep());
        this.itemFlowEndEClass.getESuperTypes().add(this.getFeature());
        this.itemUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        this.joinNodeEClass.getESuperTypes().add(this.getControlNode());
        this.libraryPackageEClass.getESuperTypes().add(this.getPackage());
        this.lifeClassEClass.getESuperTypes().add(this.getClass_());
        this.literalBooleanEClass.getESuperTypes().add(this.getLiteralExpression());
        this.literalExpressionEClass.getESuperTypes().add(this.getExpression());
        this.literalInfinityEClass.getESuperTypes().add(this.getLiteralExpression());
        this.literalIntegerEClass.getESuperTypes().add(this.getLiteralExpression());
        this.literalRationalEClass.getESuperTypes().add(this.getLiteralExpression());
        this.literalStringEClass.getESuperTypes().add(this.getLiteralExpression());
        this.loopActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.membershipEClass.getESuperTypes().add(this.getRelationship());
        this.membershipExposeEClass.getESuperTypes().add(this.getMembershipImport());
        this.membershipExposeEClass.getESuperTypes().add(this.getExpose());
        this.membershipImportEClass.getESuperTypes().add(this.getImport());
        this.mergeNodeEClass.getESuperTypes().add(this.getControlNode());
        this.metaclassEClass.getESuperTypes().add(this.getStructure());
        this.metadataAccessExpressionEClass.getESuperTypes().add(this.getExpression());
        this.metadataDefinitionEClass.getESuperTypes().add(this.getItemDefinition());
        this.metadataDefinitionEClass.getESuperTypes().add(this.getMetaclass());
        this.metadataFeatureEClass.getESuperTypes().add(this.getFeature());
        this.metadataFeatureEClass.getESuperTypes().add(this.getAnnotatingElement());
        this.metadataUsageEClass.getESuperTypes().add(this.getItemUsage());
        this.metadataUsageEClass.getESuperTypes().add(this.getMetadataFeature());
        this.multiplicityEClass.getESuperTypes().add(this.getFeature());
        this.multiplicityRangeEClass.getESuperTypes().add(this.getMultiplicity());
        this.namespaceEClass.getESuperTypes().add(this.getElement());
        this.namespaceExposeEClass.getESuperTypes().add(this.getNamespaceImport());
        this.namespaceExposeEClass.getESuperTypes().add(this.getExpose());
        this.namespaceImportEClass.getESuperTypes().add(this.getImport());
        this.nullExpressionEClass.getESuperTypes().add(this.getExpression());
        this.objectiveMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        this.occurrenceDefinitionEClass.getESuperTypes().add(this.getDefinition());
        this.occurrenceDefinitionEClass.getESuperTypes().add(this.getClass_());
        this.occurrenceUsageEClass.getESuperTypes().add(this.getUsage());
        this.operatorExpressionEClass.getESuperTypes().add(this.getInvocationExpression());
        this.owningMembershipEClass.getESuperTypes().add(this.getMembership());
        this.packageEClass.getESuperTypes().add(this.getNamespace());
        this.parameterMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        this.partDefinitionEClass.getESuperTypes().add(this.getItemDefinition());
        this.partUsageEClass.getESuperTypes().add(this.getItemUsage());
        this.performActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.performActionUsageEClass.getESuperTypes().add(this.getEventOccurrenceUsage());
        this.portConjugationEClass.getESuperTypes().add(this.getConjugation());
        this.portDefinitionEClass.getESuperTypes().add(this.getOccurrenceDefinition());
        this.portDefinitionEClass.getESuperTypes().add(this.getStructure());
        this.portUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        this.predicateEClass.getESuperTypes().add(this.getFunction());
        this.redefinitionEClass.getESuperTypes().add(this.getSubsetting());
        this.referenceSubsettingEClass.getESuperTypes().add(this.getSubsetting());
        this.referenceUsageEClass.getESuperTypes().add(this.getUsage());
        this.relationshipEClass.getESuperTypes().add(this.getElement());
        this.renderingDefinitionEClass.getESuperTypes().add(this.getPartDefinition());
        this.renderingUsageEClass.getESuperTypes().add(this.getPartUsage());
        this.requirementConstraintMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        this.requirementDefinitionEClass.getESuperTypes().add(this.getConstraintDefinition());
        this.requirementUsageEClass.getESuperTypes().add(this.getConstraintUsage());
        this.requirementVerificationMembershipEClass.getESuperTypes().add(this.getRequirementConstraintMembership());
        this.resultExpressionMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        this.returnParameterMembershipEClass.getESuperTypes().add(this.getParameterMembership());
        this.satisfyRequirementUsageEClass.getESuperTypes().add(this.getRequirementUsage());
        this.satisfyRequirementUsageEClass.getESuperTypes().add(this.getAssertConstraintUsage());
        this.selectExpressionEClass.getESuperTypes().add(this.getOperatorExpression());
        this.sendActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.specializationEClass.getESuperTypes().add(this.getRelationship());
        this.stakeholderMembershipEClass.getESuperTypes().add(this.getParameterMembership());
        this.stateDefinitionEClass.getESuperTypes().add(this.getActionDefinition());
        this.stateSubactionMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        this.stateUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.stepEClass.getESuperTypes().add(this.getFeature());
        this.structureEClass.getESuperTypes().add(this.getClass_());
        this.subclassificationEClass.getESuperTypes().add(this.getSpecialization());
        this.subjectMembershipEClass.getESuperTypes().add(this.getParameterMembership());
        this.subsettingEClass.getESuperTypes().add(this.getSpecialization());
        this.successionEClass.getESuperTypes().add(this.getConnector());
        this.successionAsUsageEClass.getESuperTypes().add(this.getConnectorAsUsage());
        this.successionAsUsageEClass.getESuperTypes().add(this.getSuccession());
        this.successionFlowConnectionUsageEClass.getESuperTypes().add(this.getFlowConnectionUsage());
        this.successionFlowConnectionUsageEClass.getESuperTypes().add(this.getSuccessionItemFlow());
        this.successionItemFlowEClass.getESuperTypes().add(this.getItemFlow());
        this.successionItemFlowEClass.getESuperTypes().add(this.getSuccession());
        this.textualRepresentationEClass.getESuperTypes().add(this.getAnnotatingElement());
        this.transitionFeatureMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        this.transitionUsageEClass.getESuperTypes().add(this.getActionUsage());
        this.triggerInvocationExpressionEClass.getESuperTypes().add(this.getInvocationExpression());
        this.typeEClass.getESuperTypes().add(this.getNamespace());
        this.typeFeaturingEClass.getESuperTypes().add(this.getFeaturing());
        this.unioningEClass.getESuperTypes().add(this.getRelationship());
        this.usageEClass.getESuperTypes().add(this.getFeature());
        this.useCaseDefinitionEClass.getESuperTypes().add(this.getCaseDefinition());
        this.useCaseUsageEClass.getESuperTypes().add(this.getCaseUsage());
        this.variantMembershipEClass.getESuperTypes().add(this.getOwningMembership());
        this.verificationCaseDefinitionEClass.getESuperTypes().add(this.getCaseDefinition());
        this.verificationCaseUsageEClass.getESuperTypes().add(this.getCaseUsage());
        this.viewDefinitionEClass.getESuperTypes().add(this.getPartDefinition());
        this.viewpointDefinitionEClass.getESuperTypes().add(this.getRequirementDefinition());
        this.viewpointUsageEClass.getESuperTypes().add(this.getRequirementUsage());
        this.viewRenderingMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        this.viewUsageEClass.getESuperTypes().add(this.getPartUsage());
        this.whileLoopActionUsageEClass.getESuperTypes().add(this.getLoopActionUsage());

        // Initialize classes, features, and operations; add parameters
        this.initEClass(this.acceptActionUsageEClass, AcceptActionUsage.class, "AcceptActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAcceptActionUsage_PayloadArgument(), this.getExpression(), null, "payloadArgument", null, 0, 1, AcceptActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getAcceptActionUsage_PayloadParameter(), this.getReferenceUsage(), null, "payloadParameter", null, 1, 1, AcceptActionUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getAcceptActionUsage_ReceiverArgument(), this.getExpression(), null, "receiverArgument", null, 0, 1, AcceptActionUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEOperation(this.getAcceptActionUsage__IsTriggerAction(), this.ecorePackage.getEBoolean(), "isTriggerAction", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.actionDefinitionEClass, ActionDefinition.class, "ActionDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getActionDefinition_Action(), this.getActionUsage(), null, "action", null, 0, -1, ActionDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.actionUsageEClass, ActionUsage.class, "ActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getActionUsage_ActionDefinition(), this.getBehavior(), null, "actionDefinition", null, 0, -1, ActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        EOperation op = this.initEOperation(this.getActionUsage__Argument__int(), this.getExpression(), "argument", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEInt(), "i", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getActionUsage__InputParameter__int(), this.getFeature(), "inputParameter", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEInt(), "i", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEOperation(this.getActionUsage__InputParameters(), this.getFeature(), "inputParameters", 0, -1, IS_UNIQUE, !IS_ORDERED);

        this.initEOperation(this.getActionUsage__IsSubactionUsage(), this.ecorePackage.getEBoolean(), "isSubactionUsage", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.actorMembershipEClass, ActorMembership.class, "ActorMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getActorMembership_OwnedActorParameter(), this.getPartUsage(), null, "ownedActorParameter", null, 1, 1, ActorMembership.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.allocationDefinitionEClass, AllocationDefinition.class, "AllocationDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAllocationDefinition_Allocation(), this.getAllocationUsage(), null, "allocation", null, 0, -1, AllocationDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.allocationUsageEClass, AllocationUsage.class, "AllocationUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAllocationUsage_AllocationDefinition(), this.getAllocationDefinition(), null, "allocationDefinition", null, 0, -1, AllocationUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.analysisCaseDefinitionEClass, AnalysisCaseDefinition.class, "AnalysisCaseDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAnalysisCaseDefinition_AnalysisAction(), this.getActionUsage(), null, "analysisAction", null, 0, -1, AnalysisCaseDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getAnalysisCaseDefinition_ResultExpression(), this.getExpression(), null, "resultExpression", null, 0, 1, AnalysisCaseDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.analysisCaseUsageEClass, AnalysisCaseUsage.class, "AnalysisCaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAnalysisCaseUsage_AnalysisAction(), this.getActionUsage(), null, "analysisAction", null, 0, -1, AnalysisCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getAnalysisCaseUsage_AnalysisCaseDefinition(), this.getAnalysisCaseDefinition(), null, "analysisCaseDefinition", null, 0, 1, AnalysisCaseUsage.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getAnalysisCaseUsage_ResultExpression(), this.getExpression(), null, "resultExpression", null, 0, 1, AnalysisCaseUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.annotatingElementEClass, AnnotatingElement.class, "AnnotatingElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAnnotatingElement_AnnotatedElement(), this.getElement(), null, "annotatedElement", null, 1, -1, AnnotatingElement.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getAnnotatingElement_Annotation(), this.getAnnotation(), this.getAnnotation_AnnotatingElement(), "annotation", null, 0, -1, AnnotatingElement.class, !IS_TRANSIENT,
                !IS_VOLATILE,
                IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        this.initEClass(this.annotationEClass, Annotation.class, "Annotation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAnnotation_AnnotatedElement(), this.getElement(), null, "annotatedElement", null, 1, 1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getAnnotation_AnnotatingElement(), this.getAnnotatingElement(), this.getAnnotatingElement_Annotation(), "annotatingElement", null, 1, 1, Annotation.class,
                !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getAnnotation_OwningAnnotatedElement(), this.getElement(), this.getElement_OwnedAnnotation(), "owningAnnotatedElement", null, 0, 1, Annotation.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.assertConstraintUsageEClass, AssertConstraintUsage.class, "AssertConstraintUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAssertConstraintUsage_AssertedConstraint(), this.getConstraintUsage(), null, "assertedConstraint", null, 1, 1, AssertConstraintUsage.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.assignmentActionUsageEClass, AssignmentActionUsage.class, "AssignmentActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAssignmentActionUsage_Referent(), this.getFeature(), null, "referent", null, 1, 1, AssignmentActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getAssignmentActionUsage_TargetArgument(), this.getExpression(), null, "targetArgument", null, 1, 1, AssignmentActionUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getAssignmentActionUsage_ValueExpression(), this.getExpression(), null, "valueExpression", null, 1, 1, AssignmentActionUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.associationEClass, Association.class, "Association", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAssociation_AssociationEnd(), this.getFeature(), null, "associationEnd", null, 0, -1, Association.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getAssociation_RelatedType(), this.getType(), null, "relatedType", null, 0, -1, Association.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getAssociation_SourceType(), this.getType(), null, "sourceType", null, 0, 1, Association.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getAssociation_TargetType(), this.getType(), null, "targetType", null, 0, -1, Association.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.associationStructureEClass, AssociationStructure.class, "AssociationStructure", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.attributeDefinitionEClass, AttributeDefinition.class, "AttributeDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.attributeUsageEClass, AttributeUsage.class, "AttributeUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAttributeUsage_AttributeDefinition(), this.getDataType(), null, "attributeDefinition", null, 0, -1, AttributeUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.behaviorEClass, Behavior.class, "Behavior", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getBehavior_Parameter(), this.getFeature(), null, "parameter", null, 0, -1, Behavior.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getBehavior_Step(), this.getStep(), null, "step", null, 0, -1, Behavior.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.bindingConnectorEClass, BindingConnector.class, "BindingConnector", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.bindingConnectorAsUsageEClass, BindingConnectorAsUsage.class, "BindingConnectorAsUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.booleanExpressionEClass, BooleanExpression.class, "BooleanExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getBooleanExpression_Predicate(), this.getPredicate(), null, "predicate", null, 0, 1, BooleanExpression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.calculationDefinitionEClass, CalculationDefinition.class, "CalculationDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getCalculationDefinition_Calculation(), this.getCalculationUsage(), null, "calculation", null, 0, -1, CalculationDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.calculationUsageEClass, CalculationUsage.class, "CalculationUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getCalculationUsage_CalculationDefinition(), this.getFunction(), null, "calculationDefinition", null, 0, 1, CalculationUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.caseDefinitionEClass, CaseDefinition.class, "CaseDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getCaseDefinition_ActorParameter(), this.getPartUsage(), null, "actorParameter", null, 0, -1, CaseDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getCaseDefinition_ObjectiveRequirement(), this.getRequirementUsage(), null, "objectiveRequirement", null, 0, 1, CaseDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getCaseDefinition_SubjectParameter(), this.getUsage(), null, "subjectParameter", null, 1, 1, CaseDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.caseUsageEClass, CaseUsage.class, "CaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getCaseUsage_ActorParameter(), this.getPartUsage(), null, "actorParameter", null, 0, -1, CaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getCaseUsage_CaseDefinition(), this.getCaseDefinition(), null, "caseDefinition", null, 0, 1, CaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getCaseUsage_ObjectiveRequirement(), this.getRequirementUsage(), null, "objectiveRequirement", null, 0, 1, CaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getCaseUsage_SubjectParameter(), this.getUsage(), null, "subjectParameter", null, 1, 1, CaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.classEClass, org.eclipse.syson.sysml.Class.class, "Class", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.classifierEClass, Classifier.class, "Classifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getClassifier_OwnedSubclassification(), this.getSubclassification(), this.getSubclassification_OwningClassifier(), "ownedSubclassification", null, 0, -1,
                Classifier.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.collectExpressionEClass, CollectExpression.class, "CollectExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.commentEClass, Comment.class, "Comment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getComment_Body(), this.ecorePackage.getEString(), "body", null, 1, 1, Comment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED,
                !IS_ORDERED);
        this.initEAttribute(this.getComment_Locale(), this.ecorePackage.getEString(), "locale", null, 0, 1, Comment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED,
                !IS_ORDERED);

        this.initEClass(this.concernDefinitionEClass, ConcernDefinition.class, "ConcernDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.concernUsageEClass, ConcernUsage.class, "ConcernUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getConcernUsage_ConcernDefinition(), this.getConcernDefinition(), null, "concernDefinition", null, 0, 1, ConcernUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.conjugatedPortDefinitionEClass, ConjugatedPortDefinition.class, "ConjugatedPortDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getConjugatedPortDefinition_OriginalPortDefinition(), this.getPortDefinition(), this.getPortDefinition_ConjugatedPortDefinition(), "originalPortDefinition", null, 1,
                1,
                ConjugatedPortDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getConjugatedPortDefinition_OwnedPortConjugator(), this.getPortConjugation(), this.getPortConjugation_ConjugatedPortDefinition(), "ownedPortConjugator", null, 1, 1,
                ConjugatedPortDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.conjugatedPortTypingEClass, ConjugatedPortTyping.class, "ConjugatedPortTyping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getConjugatedPortTyping_ConjugatedPortDefinition(), this.getConjugatedPortDefinition(), null, "conjugatedPortDefinition", null, 1, 1, ConjugatedPortTyping.class,
                !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getConjugatedPortTyping_PortDefinition(), this.getPortDefinition(), null, "portDefinition", null, 1, 1, ConjugatedPortTyping.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.conjugationEClass, Conjugation.class, "Conjugation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getConjugation_ConjugatedType(), this.getType(), null, "conjugatedType", null, 1, 1, Conjugation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getConjugation_OriginalType(), this.getType(), null, "originalType", null, 1, 1, Conjugation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getConjugation_OwningType(), this.getType(), this.getType_OwnedConjugator(), "owningType", null, 0, 1, Conjugation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.connectionDefinitionEClass, ConnectionDefinition.class, "ConnectionDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getConnectionDefinition_ConnectionEnd(), this.getUsage(), null, "connectionEnd", null, 0, -1, ConnectionDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.connectionUsageEClass, ConnectionUsage.class, "ConnectionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getConnectionUsage_ConnectionDefinition(), this.getAssociationStructure(), null, "connectionDefinition", null, 0, -1, ConnectionUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.connectorEClass, Connector.class, "Connector", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getConnector_IsDirected(), this.ecorePackage.getEBoolean(), "isDirected", "false", 1, 1, Connector.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getConnector_Association(), this.getAssociation(), null, "association", null, 0, -1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getConnector_ConnectorEnd(), this.getFeature(), null, "connectorEnd", null, 0, -1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getConnector_RelatedFeature(), this.getFeature(), null, "relatedFeature", null, 0, -1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getConnector_SourceFeature(), this.getFeature(), null, "sourceFeature", null, 0, 1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getConnector_TargetFeature(), this.getFeature(), null, "targetFeature", null, 0, -1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.connectorAsUsageEClass, ConnectorAsUsage.class, "ConnectorAsUsage", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.constraintDefinitionEClass, ConstraintDefinition.class, "ConstraintDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.constraintUsageEClass, ConstraintUsage.class, "ConstraintUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getConstraintUsage_ConstraintDefinition(), this.getPredicate(), null, "constraintDefinition", null, 0, 1, ConstraintUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.controlNodeEClass, ControlNode.class, "ControlNode", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        op = this.initEOperation(this.getControlNode__MultiplicityHasBounds__Multiplicity_int_int(), this.ecorePackage.getEBoolean(), "multiplicityHasBounds", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getMultiplicity(), "mult", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEInt(), "lower", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEInt(), "upper", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.dataTypeEClass, DataType.class, "DataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.decisionNodeEClass, DecisionNode.class, "DecisionNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.definitionEClass, Definition.class, "Definition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getDefinition_IsVariation(), this.ecorePackage.getEBoolean(), "isVariation", null, 1, 1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getDefinition_DirectedUsage(), this.getUsage(), null, "directedUsage", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedAction(), this.getActionUsage(), null, "ownedAction", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedAllocation(), this.getAllocationUsage(), null, "ownedAllocation", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedAnalysisCase(), this.getAnalysisCaseUsage(), null, "ownedAnalysisCase", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedAttribute(), this.getAttributeUsage(), null, "ownedAttribute", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedCalculation(), this.getCalculationUsage(), null, "ownedCalculation", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedCase(), this.getCaseUsage(), null, "ownedCase", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedConcern(), this.getConcernUsage(), null, "ownedConcern", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedConnection(), this.getConnectorAsUsage(), null, "ownedConnection", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedConstraint(), this.getConstraintUsage(), null, "ownedConstraint", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedEnumeration(), this.getEnumerationUsage(), null, "ownedEnumeration", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedFlow(), this.getFlowConnectionUsage(), null, "ownedFlow", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedInterface(), this.getInterfaceUsage(), null, "ownedInterface", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedItem(), this.getItemUsage(), null, "ownedItem", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedMetadata(), this.getMetadataUsage(), null, "ownedMetadata", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedOccurrence(), this.getOccurrenceUsage(), null, "ownedOccurrence", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedPart(), this.getPartUsage(), null, "ownedPart", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedPort(), this.getPortUsage(), null, "ownedPort", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedReference(), this.getReferenceUsage(), null, "ownedReference", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedRendering(), this.getRenderingUsage(), null, "ownedRendering", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedRequirement(), this.getRequirementUsage(), null, "ownedRequirement", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedState(), this.getStateUsage(), null, "ownedState", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedTransition(), this.getTransitionUsage(), null, "ownedTransition", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedUsage(), this.getUsage(), this.getUsage_OwningDefinition(), "ownedUsage", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedUseCase(), this.getUseCaseUsage(), null, "ownedUseCase", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedVerificationCase(), this.getVerificationCaseUsage(), null, "ownedVerificationCase", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedView(), this.getViewUsage(), null, "ownedView", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_OwnedViewpoint(), this.getViewpointUsage(), null, "ownedViewpoint", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_Usage(), this.getUsage(), null, "usage", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDefinition_Variant(), this.getUsage(), null, "variant", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getDefinition_VariantMembership(), this.getVariantMembership(), null, "variantMembership", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.dependencyEClass, Dependency.class, "Dependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getDependency_Client(), this.getElement(), null, "client", null, 1, -1, Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getDependency_Supplier(), this.getElement(), null, "supplier", null, 1, -1, Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        this.initEClass(this.differencingEClass, Differencing.class, "Differencing", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getDifferencing_DifferencingType(), this.getType(), null, "differencingType", null, 1, 1, Differencing.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getDifferencing_TypeDifferenced(), this.getType(), this.getType_OwnedDifferencing(), "typeDifferenced", null, 1, 1, Differencing.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.disjoiningEClass, Disjoining.class, "Disjoining", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getDisjoining_DisjoiningType(), this.getType(), null, "disjoiningType", null, 1, 1, Disjoining.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getDisjoining_OwningType(), this.getType(), this.getType_OwnedDisjoining(), "owningType", null, 0, 1, Disjoining.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getDisjoining_TypeDisjoined(), this.getType(), null, "typeDisjoined", null, 1, 1, Disjoining.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.documentationEClass, Documentation.class, "Documentation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getDocumentation_DocumentedElement(), this.getElement(), this.getElement_Documentation(), "documentedElement", null, 1, 1, Documentation.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.elementEClass, Element.class, "Element", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getElement_AliasIds(), this.ecorePackage.getEString(), "aliasIds", null, 0, -1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, IS_ORDERED);
        this.initEAttribute(this.getElement_DeclaredName(), this.ecorePackage.getEString(), "declaredName", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getElement_DeclaredShortName(), this.ecorePackage.getEString(), "declaredShortName", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getElement_ElementId(), this.ecorePackage.getEString(), "elementId", null, 1, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getElement_IsImpliedIncluded(), this.ecorePackage.getEBoolean(), "isImpliedIncluded", "false", 1, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getElement_IsLibraryElement(), this.ecorePackage.getEBoolean(), "isLibraryElement", null, 1, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getElement_Name(), this.ecorePackage.getEString(), "name", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                IS_DERIVED,
                !IS_ORDERED);
        this.initEAttribute(this.getElement_QualifiedName(), this.ecorePackage.getEString(), "qualifiedName", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getElement_ShortName(), this.ecorePackage.getEString(), "shortName", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getElement_Documentation(), this.getDocumentation(), this.getDocumentation_DocumentedElement(), "documentation", null, 0, -1, Element.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getElement_OwnedAnnotation(), this.getAnnotation(), this.getAnnotation_OwningAnnotatedElement(), "ownedAnnotation", null, 0, -1, Element.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getElement_OwnedElement(), this.getElement(), this.getElement_Owner(), "ownedElement", null, 0, -1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getElement_OwnedRelationship(), this.getRelationship(), this.getRelationship_OwningRelatedElement(), "ownedRelationship", null, 0, -1, Element.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getElement_Owner(), this.getElement(), this.getElement_OwnedElement(), "owner", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getElement_OwningMembership(), this.getOwningMembership(), this.getOwningMembership_OwnedMemberElement(), "owningMembership", null, 0, 1, Element.class, IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getElement_OwningNamespace(), this.getNamespace(), this.getNamespace_OwnedMember(), "owningNamespace", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getElement_OwningRelationship(), this.getRelationship(), this.getRelationship_OwnedRelatedElement(), "owningRelationship", null, 0, 1, Element.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getElement_TextualRepresentation(), this.getTextualRepresentation(), this.getTextualRepresentation_RepresentedElement(), "textualRepresentation", null, 0, -1,
                Element.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEOperation(this.getElement__EffectiveName(), this.ecorePackage.getEString(), "effectiveName", 0, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEOperation(this.getElement__EffectiveShortName(), this.ecorePackage.getEString(), "effectiveShortName", 0, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEOperation(this.getElement__EscapedName(), this.ecorePackage.getEString(), "escapedName", 0, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEOperation(this.getElement__LibraryNamespace(), this.getNamespace(), "libraryNamespace", 0, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.elementFilterMembershipEClass, ElementFilterMembership.class, "ElementFilterMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getElementFilterMembership_Condition(), this.getExpression(), null, "condition", null, 1, 1, ElementFilterMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.endFeatureMembershipEClass, EndFeatureMembership.class, "EndFeatureMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.enumerationDefinitionEClass, EnumerationDefinition.class, "EnumerationDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getEnumerationDefinition_EnumeratedValue(), this.getEnumerationUsage(), null, "enumeratedValue", null, 0, -1, EnumerationDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.enumerationUsageEClass, EnumerationUsage.class, "EnumerationUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getEnumerationUsage_EnumerationDefinition(), this.getEnumerationDefinition(), null, "enumerationDefinition", null, 1, 1, EnumerationUsage.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.eventOccurrenceUsageEClass, EventOccurrenceUsage.class, "EventOccurrenceUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getEventOccurrenceUsage_EventOccurrence(), this.getOccurrenceUsage(), null, "eventOccurrence", null, 1, 1, EventOccurrenceUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.exhibitStateUsageEClass, ExhibitStateUsage.class, "ExhibitStateUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getExhibitStateUsage_ExhibitedState(), this.getStateUsage(), null, "exhibitedState", null, 1, 1, ExhibitStateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.exposeEClass, Expose.class, "Expose", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.expressionEClass, Expression.class, "Expression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getExpression_IsModelLevelEvaluable(), this.ecorePackage.getEBoolean(), "isModelLevelEvaluable", null, 1, 1, Expression.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getExpression_Function(), this.getFunction(), null, "function", null, 0, 1, Expression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getExpression_Result(), this.getFeature(), null, "result", null, 1, 1, Expression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = this.initEOperation(this.getExpression__CheckCondition__Element(), this.ecorePackage.getEBoolean(), "checkCondition", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getElement(), "target", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getExpression__Evaluate__Element(), this.getElement(), "evaluate", 0, -1, !IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.getElement(), "target", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getExpression__ModelLevelEvaluable__EList(), this.ecorePackage.getEBoolean(), "modelLevelEvaluable", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getFeature(), "visited", 0, -1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.featureEClass, Feature.class, "Feature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getFeature_Direction(), this.getFeatureDirectionKind(), "direction", null, 0, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getFeature_IsComposite(), this.ecorePackage.getEBoolean(), "isComposite", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getFeature_IsDerived(), this.ecorePackage.getEBoolean(), "isDerived", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getFeature_IsEnd(), this.ecorePackage.getEBoolean(), "isEnd", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getFeature_IsNonunique(), this.ecorePackage.getEBoolean(), "isNonunique", "false", 1, 1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getFeature_IsOrdered(), this.ecorePackage.getEBoolean(), "isOrdered", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getFeature_IsPortion(), this.ecorePackage.getEBoolean(), "isPortion", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getFeature_IsReadOnly(), this.ecorePackage.getEBoolean(), "isReadOnly", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getFeature_IsUnique(), this.ecorePackage.getEBoolean(), "isUnique", "true", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeature_ChainingFeature(), this.getFeature(), null, "chainingFeature", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getFeature_EndOwningType(), this.getType(), this.getType_OwnedEndFeature(), "endOwningType", null, 0, 1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeature_FeaturingType(), this.getType(), null, "featuringType", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getFeature_OwnedFeatureChaining(), this.getFeatureChaining(), this.getFeatureChaining_FeatureChained(), "ownedFeatureChaining", null, 0, -1, Feature.class,
                IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getFeature_OwnedFeatureInverting(), this.getFeatureInverting(), this.getFeatureInverting_OwningFeature(), "ownedFeatureInverting", null, 0, -1, Feature.class,
                IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeature_OwnedRedefinition(), this.getRedefinition(), null, "ownedRedefinition", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeature_OwnedReferenceSubsetting(), this.getReferenceSubsetting(), this.getReferenceSubsetting_ReferencingFeature(), "ownedReferenceSubsetting", null, 0, 1,
                Feature.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeature_OwnedSubsetting(), this.getSubsetting(), this.getSubsetting_OwningFeature(), "ownedSubsetting", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeature_OwnedTypeFeaturing(), this.getTypeFeaturing(), this.getTypeFeaturing_OwningFeatureOfType(), "ownedTypeFeaturing", null, 0, -1, Feature.class, IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getFeature_OwnedTyping(), this.getFeatureTyping(), this.getFeatureTyping_OwningFeature(), "ownedTyping", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getFeature_OwningFeatureMembership(), this.getFeatureMembership(), this.getFeatureMembership_OwnedMemberFeature(), "owningFeatureMembership", null, 0, 1,
                Feature.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeature_OwningType(), this.getType(), this.getType_OwnedFeature(), "owningType", null, 0, 1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeature_Type(), this.getType(), null, "type", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getFeature_Valuation(), this.getFeatureValue(), null, "valuation", null, 0, 1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = this.initEOperation(this.getFeature__DirectionFor__Type(), this.getFeatureDirectionKind(), "directionFor", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getType(), "type", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getFeature__IsFeaturedWithin__Type(), this.ecorePackage.getEBoolean(), "isFeaturedWithin", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getType(), "type", 0, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEOperation(this.getFeature__NamingFeature(), this.getFeature(), "namingFeature", 0, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getFeature__Redefines__Feature(), this.ecorePackage.getEBoolean(), "redefines", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getFeature(), "redefinedFeature", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getFeature__RedefinesFromLibrary__String(), this.ecorePackage.getEBoolean(), "redefinesFromLibrary", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "libraryFeatureName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getFeature__SubsetsChain__Feature_Feature(), this.ecorePackage.getEBoolean(), "subsetsChain", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getFeature(), "first", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getFeature(), "second", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.featureChainExpressionEClass, FeatureChainExpression.class, "FeatureChainExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getFeatureChainExpression_TargetFeature(), this.getFeature(), null, "targetFeature", null, 1, 1, FeatureChainExpression.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEOperation(this.getFeatureChainExpression__SourceTargetFeature(), this.getFeature(), "sourceTargetFeature", 0, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.featureChainingEClass, FeatureChaining.class, "FeatureChaining", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getFeatureChaining_ChainingFeature(), this.getFeature(), null, "chainingFeature", null, 1, 1, FeatureChaining.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeatureChaining_FeatureChained(), this.getFeature(), this.getFeature_OwnedFeatureChaining(), "featureChained", null, 1, 1, FeatureChaining.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.featureInvertingEClass, FeatureInverting.class, "FeatureInverting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getFeatureInverting_FeatureInverted(), this.getFeature(), null, "featureInverted", null, 1, 1, FeatureInverting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeatureInverting_InvertingFeature(), this.getFeature(), null, "invertingFeature", null, 1, 1, FeatureInverting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeatureInverting_OwningFeature(), this.getFeature(), this.getFeature_OwnedFeatureInverting(), "owningFeature", null, 0, 1, FeatureInverting.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.featureMembershipEClass, FeatureMembership.class, "FeatureMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getFeatureMembership_OwnedMemberFeature(), this.getFeature(), this.getFeature_OwningFeatureMembership(), "ownedMemberFeature", null, 1, 1, FeatureMembership.class,
                IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeatureMembership_OwningType(), this.getType(), this.getType_OwnedFeatureMembership(), "owningType", null, 1, 1, FeatureMembership.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.featureReferenceExpressionEClass, FeatureReferenceExpression.class, "FeatureReferenceExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getFeatureReferenceExpression_Referent(), this.getFeature(), null, "referent", null, 1, 1, FeatureReferenceExpression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.featureTypingEClass, FeatureTyping.class, "FeatureTyping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getFeatureTyping_OwningFeature(), this.getFeature(), this.getFeature_OwnedTyping(), "owningFeature", null, 0, 1, FeatureTyping.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeatureTyping_Type(), this.getType(), null, "type", null, 1, 1, FeatureTyping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeatureTyping_TypedFeature(), this.getFeature(), null, "typedFeature", null, 1, 1, FeatureTyping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.featureValueEClass, FeatureValue.class, "FeatureValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getFeatureValue_IsDefault(), this.ecorePackage.getEBoolean(), "isDefault", "false", 1, 1, FeatureValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getFeatureValue_IsInitial(), this.ecorePackage.getEBoolean(), "isInitial", "false", 1, 1, FeatureValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeatureValue_FeatureWithValue(), this.getFeature(), null, "featureWithValue", null, 1, 1, FeatureValue.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeatureValue_Value(), this.getExpression(), null, "value", null, 1, 1, FeatureValue.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.featuringEClass, Featuring.class, "Featuring", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getFeaturing_Feature(), this.getFeature(), null, "feature", null, 1, 1, Featuring.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFeaturing_Type(), this.getType(), null, "type", null, 1, 1, Featuring.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.flowConnectionDefinitionEClass, FlowConnectionDefinition.class, "FlowConnectionDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.flowConnectionUsageEClass, FlowConnectionUsage.class, "FlowConnectionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getFlowConnectionUsage_FlowConnectionDefinition(), this.getInteraction(), null, "flowConnectionDefinition", null, 0, -1, FlowConnectionUsage.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.forkNodeEClass, ForkNode.class, "ForkNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.forLoopActionUsageEClass, ForLoopActionUsage.class, "ForLoopActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getForLoopActionUsage_LoopVariable(), this.getReferenceUsage(), null, "loopVariable", null, 1, 1, ForLoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getForLoopActionUsage_SeqArgument(), this.getExpression(), null, "seqArgument", null, 1, 1, ForLoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.framedConcernMembershipEClass, FramedConcernMembership.class, "FramedConcernMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getFramedConcernMembership_OwnedConcern(), this.getConcernUsage(), null, "ownedConcern", null, 1, 1, FramedConcernMembership.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFramedConcernMembership_ReferencedConcern(), this.getConcernUsage(), null, "referencedConcern", null, 1, 1, FramedConcernMembership.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.functionEClass, Function.class, "Function", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getFunction_IsModelLevelEvaluable(), this.ecorePackage.getEBoolean(), "isModelLevelEvaluable", null, 1, 1, Function.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFunction_Expression(), this.getExpression(), null, "expression", null, 0, -1, Function.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getFunction_Result(), this.getFeature(), null, "result", null, 1, 1, Function.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.ifActionUsageEClass, IfActionUsage.class, "IfActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getIfActionUsage_ElseAction(), this.getActionUsage(), null, "elseAction", null, 0, 1, IfActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getIfActionUsage_IfArgument(), this.getExpression(), null, "ifArgument", null, 1, 1, IfActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getIfActionUsage_ThenAction(), this.getActionUsage(), null, "thenAction", null, 1, 1, IfActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.importEClass, Import.class, "Import", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getImport_IsImportAll(), this.ecorePackage.getEBoolean(), "isImportAll", "false", 1, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getImport_IsRecursive(), this.ecorePackage.getEBoolean(), "isRecursive", "false", 1, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getImport_Visibility(), this.getVisibilityKind(), "visibility", "public", 1, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getImport_ImportedElement(), this.getElement(), null, "importedElement", null, 1, 1, Import.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getImport_ImportOwningNamespace(), this.getNamespace(), this.getNamespace_OwnedImport(), "importOwningNamespace", null, 1, 1, Import.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = this.initEOperation(this.getImport__ImportedMemberships__EList(), this.getMembership(), "importedMemberships", 0, -1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.getNamespace(), "excluded", 0, -1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.includeUseCaseUsageEClass, IncludeUseCaseUsage.class, "IncludeUseCaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getIncludeUseCaseUsage_UseCaseIncluded(), this.getUseCaseUsage(), null, "useCaseIncluded", null, 1, 1, IncludeUseCaseUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.interactionEClass, Interaction.class, "Interaction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.interfaceDefinitionEClass, InterfaceDefinition.class, "InterfaceDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getInterfaceDefinition_InterfaceEnd(), this.getPortUsage(), null, "interfaceEnd", null, 0, -1, InterfaceDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.interfaceUsageEClass, InterfaceUsage.class, "InterfaceUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getInterfaceUsage_InterfaceDefinition(), this.getInterfaceDefinition(), null, "interfaceDefinition", null, 0, -1, InterfaceUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.intersectingEClass, Intersecting.class, "Intersecting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getIntersecting_IntersectingType(), this.getType(), null, "intersectingType", null, 1, 1, Intersecting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getIntersecting_TypeIntersected(), this.getType(), this.getType_OwnedIntersecting(), "typeIntersected", null, 1, 1, Intersecting.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.invariantEClass, Invariant.class, "Invariant", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getInvariant_IsNegated(), this.ecorePackage.getEBoolean(), "isNegated", "false", 1, 1, Invariant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.invocationExpressionEClass, InvocationExpression.class, "InvocationExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getInvocationExpression_Argument(), this.getExpression(), null, "argument", null, 0, -1, InvocationExpression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.itemDefinitionEClass, ItemDefinition.class, "ItemDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.itemFeatureEClass, ItemFeature.class, "ItemFeature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.itemFlowEClass, ItemFlow.class, "ItemFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getItemFlow_Interaction(), this.getInteraction(), null, "interaction", null, 0, -1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getItemFlow_ItemFeature(), this.getItemFeature(), null, "itemFeature", null, 0, 1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getItemFlow_ItemFlowEnd(), this.getItemFlowEnd(), null, "itemFlowEnd", null, 0, 2, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getItemFlow_ItemType(), this.getClassifier(), null, "itemType", null, 0, -1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getItemFlow_SourceOutputFeature(), this.getFeature(), null, "sourceOutputFeature", null, 0, 1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getItemFlow_TargetInputFeature(), this.getFeature(), null, "targetInputFeature", null, 0, 1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.itemFlowEndEClass, ItemFlowEnd.class, "ItemFlowEnd", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.itemUsageEClass, ItemUsage.class, "ItemUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getItemUsage_ItemDefinition(), this.getStructure(), null, "itemDefinition", null, 0, -1, ItemUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.joinNodeEClass, JoinNode.class, "JoinNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.libraryPackageEClass, LibraryPackage.class, "LibraryPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getLibraryPackage_IsStandard(), this.ecorePackage.getEBoolean(), "isStandard", "false", 1, 1, LibraryPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.lifeClassEClass, LifeClass.class, "LifeClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.literalBooleanEClass, LiteralBoolean.class, "LiteralBoolean", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getLiteralBoolean_Value(), this.ecorePackage.getEBoolean(), "value", null, 1, 1, LiteralBoolean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.literalExpressionEClass, LiteralExpression.class, "LiteralExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.literalInfinityEClass, LiteralInfinity.class, "LiteralInfinity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.literalIntegerEClass, LiteralInteger.class, "LiteralInteger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getLiteralInteger_Value(), this.ecorePackage.getEInt(), "value", null, 1, 1, LiteralInteger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.literalRationalEClass, LiteralRational.class, "LiteralRational", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getLiteralRational_Value(), this.ecorePackage.getEDouble(), "value", null, 1, 1, LiteralRational.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.literalStringEClass, LiteralString.class, "LiteralString", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getLiteralString_Value(), this.ecorePackage.getEString(), "value", null, 1, 1, LiteralString.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.loopActionUsageEClass, LoopActionUsage.class, "LoopActionUsage", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getLoopActionUsage_BodyAction(), this.getActionUsage(), null, "bodyAction", null, 1, 1, LoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.membershipEClass, Membership.class, "Membership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getMembership_MemberElementId(), this.ecorePackage.getEString(), "memberElementId", null, 1, 1, Membership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getMembership_MemberName(), this.ecorePackage.getEString(), "memberName", null, 0, 1, Membership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getMembership_MemberShortName(), this.ecorePackage.getEString(), "memberShortName", null, 0, 1, Membership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getMembership_Visibility(), this.getVisibilityKind(), "visibility", "public", 1, 1, Membership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getMembership_MemberElement(), this.getElement(), null, "memberElement", null, 1, 1, Membership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getMembership_MembershipOwningNamespace(), this.getNamespace(), this.getNamespace_OwnedMembership(), "membershipOwningNamespace", null, 1, 1, Membership.class,
                IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = this.initEOperation(this.getMembership__IsDistinguishableFrom__Membership(), this.ecorePackage.getEBoolean(), "isDistinguishableFrom", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getMembership(), "other", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.membershipExposeEClass, MembershipExpose.class, "MembershipExpose", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.membershipImportEClass, MembershipImport.class, "MembershipImport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getMembershipImport_ImportedMembership(), this.getMembership(), null, "importedMembership", null, 1, 1, MembershipImport.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.mergeNodeEClass, MergeNode.class, "MergeNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.metaclassEClass, Metaclass.class, "Metaclass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.metadataAccessExpressionEClass, MetadataAccessExpression.class, "MetadataAccessExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getMetadataAccessExpression_ReferencedElement(), this.getElement(), null, "referencedElement", null, 1, 1, MetadataAccessExpression.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEOperation(this.getMetadataAccessExpression__MetaclassFeature(), this.getMetadataFeature(), "metaclassFeature", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.metadataDefinitionEClass, MetadataDefinition.class, "MetadataDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.metadataFeatureEClass, MetadataFeature.class, "MetadataFeature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getMetadataFeature_Metaclass(), this.getMetaclass(), null, "metaclass", null, 0, 1, MetadataFeature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = this.initEOperation(this.getMetadataFeature__EvaluateFeature__Feature(), this.getElement(), "evaluateFeature", 0, -1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getFeature(), "baseFeature", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEOperation(this.getMetadataFeature__IsSemantic(), this.ecorePackage.getEBoolean(), "isSemantic", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEOperation(this.getMetadataFeature__IsSyntactic(), this.ecorePackage.getEBoolean(), "isSyntactic", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEOperation(this.getMetadataFeature__SyntaxElement(), this.getElement(), "syntaxElement", 0, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.metadataUsageEClass, MetadataUsage.class, "MetadataUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getMetadataUsage_MetadataDefinition(), this.getMetaclass(), null, "metadataDefinition", null, 0, 1, MetadataUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.multiplicityEClass, Multiplicity.class, "Multiplicity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.multiplicityRangeEClass, MultiplicityRange.class, "MultiplicityRange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getMultiplicityRange_Bound(), this.getExpression(), null, "bound", null, 1, 2, MultiplicityRange.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getMultiplicityRange_LowerBound(), this.getExpression(), null, "lowerBound", null, 0, 1, MultiplicityRange.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getMultiplicityRange_UpperBound(), this.getExpression(), null, "upperBound", null, 1, 1, MultiplicityRange.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = this.initEOperation(this.getMultiplicityRange__HasBounds__int_int(), this.ecorePackage.getEBoolean(), "hasBounds", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEInt(), "lower", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEInt(), "upper", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getMultiplicityRange__ValueOf__Expression(), this.ecorePackage.getEInt(), "valueOf", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getExpression(), "bound", 0, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.namespaceEClass, Namespace.class, "Namespace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getNamespace_ImportedMembership(), this.getMembership(), null, "importedMembership", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getNamespace_Member(), this.getElement(), null, "member", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getNamespace_Membership(), this.getMembership(), null, "membership", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getNamespace_OwnedImport(), this.getImport(), this.getImport_ImportOwningNamespace(), "ownedImport", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getNamespace_OwnedMember(), this.getElement(), this.getElement_OwningNamespace(), "ownedMember", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getNamespace_OwnedMembership(), this.getMembership(), this.getMembership_MembershipOwningNamespace(), "ownedMembership", null, 0, -1, Namespace.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        op = this.initEOperation(this.getNamespace__ImportedMemberships__EList(), this.getMembership(), "importedMemberships", 0, -1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.getNamespace(), "excluded", 0, -1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getNamespace__NamesOf__Element(), this.ecorePackage.getEString(), "namesOf", 0, -1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getElement(), "element", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getNamespace__QualificationOf__String(), this.ecorePackage.getEString(), "qualificationOf", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "qualifiedName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getNamespace__Resolve__String(), this.getMembership(), "resolve", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "qualifiedName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getNamespace__ResolveGlobal__String(), this.getMembership(), "resolveGlobal", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "qualifiedName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getNamespace__ResolveLocal__String(), this.getMembership(), "resolveLocal", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getNamespace__ResolveVisible__String(), this.getMembership(), "resolveVisible", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getNamespace__UnqualifiedNameOf__String(), this.ecorePackage.getEString(), "unqualifiedNameOf", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "qualifiedName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getNamespace__VisibilityOf__Membership(), this.getVisibilityKind(), "visibilityOf", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getMembership(), "mem", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getNamespace__VisibleMemberships__EList_boolean_boolean(), this.getMembership(), "visibleMemberships", 0, -1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.getNamespace(), "excluded", 0, -1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEBoolean(), "isRecursive", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEBoolean(), "includeAll", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.namespaceExposeEClass, NamespaceExpose.class, "NamespaceExpose", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.namespaceImportEClass, NamespaceImport.class, "NamespaceImport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getNamespaceImport_ImportedNamespace(), this.getNamespace(), null, "importedNamespace", null, 1, 1, NamespaceImport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.nullExpressionEClass, NullExpression.class, "NullExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.objectiveMembershipEClass, ObjectiveMembership.class, "ObjectiveMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getObjectiveMembership_OwnedObjectiveRequirement(), this.getRequirementUsage(), null, "ownedObjectiveRequirement", null, 1, 1, ObjectiveMembership.class, IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.occurrenceDefinitionEClass, OccurrenceDefinition.class, "OccurrenceDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getOccurrenceDefinition_IsIndividual(), this.ecorePackage.getEBoolean(), "isIndividual", "false", 1, 1, OccurrenceDefinition.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getOccurrenceDefinition_LifeClass(), this.getLifeClass(), null, "lifeClass", null, 0, 1, OccurrenceDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.occurrenceUsageEClass, OccurrenceUsage.class, "OccurrenceUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getOccurrenceUsage_IsIndividual(), this.ecorePackage.getEBoolean(), "isIndividual", "false", 1, 1, OccurrenceUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getOccurrenceUsage_PortionKind(), this.getPortionKind(), "portionKind", null, 0, 1, OccurrenceUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getOccurrenceUsage_IndividualDefinition(), this.getOccurrenceDefinition(), null, "individualDefinition", null, 0, 1, OccurrenceUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getOccurrenceUsage_OccurrenceDefinition(), this.getClass_(), null, "occurrenceDefinition", null, 0, -1, OccurrenceUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.operatorExpressionEClass, OperatorExpression.class, "OperatorExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getOperatorExpression_Operator(), this.ecorePackage.getEString(), "operator", null, 1, 1, OperatorExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getOperatorExpression_Operand(), this.getExpression(), null, "operand", null, 0, -1, OperatorExpression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE,
                !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.owningMembershipEClass, OwningMembership.class, "OwningMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getOwningMembership_OwnedMemberElementId(), this.ecorePackage.getEString(), "ownedMemberElementId", null, 1, 1, OwningMembership.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getOwningMembership_OwnedMemberName(), this.ecorePackage.getEString(), "ownedMemberName", null, 0, 1, OwningMembership.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getOwningMembership_OwnedMemberShortName(), this.ecorePackage.getEString(), "ownedMemberShortName", null, 0, 1, OwningMembership.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getOwningMembership_OwnedMemberElement(), this.getElement(), this.getElement_OwningMembership(), "ownedMemberElement", null, 1, 1, OwningMembership.class,
                IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.packageEClass, org.eclipse.syson.sysml.Package.class, "Package", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getPackage_FilterCondition(), this.getExpression(), null, "filterCondition", null, 0, -1, org.eclipse.syson.sysml.Package.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        op = this.initEOperation(this.getPackage__IncludeAsMember__Element(), this.ecorePackage.getEBoolean(), "includeAsMember", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getElement(), "element", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.parameterMembershipEClass, ParameterMembership.class, "ParameterMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getParameterMembership_OwnedMemberParameter(), this.getFeature(), null, "ownedMemberParameter", null, 1, 1, ParameterMembership.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.partDefinitionEClass, PartDefinition.class, "PartDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.partUsageEClass, PartUsage.class, "PartUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getPartUsage_PartDefinition(), this.getPartDefinition(), null, "partDefinition", null, 0, -1, PartUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.performActionUsageEClass, PerformActionUsage.class, "PerformActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getPerformActionUsage_PerformedAction(), this.getActionUsage(), null, "performedAction", null, 1, 1, PerformActionUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.portConjugationEClass, PortConjugation.class, "PortConjugation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getPortConjugation_ConjugatedPortDefinition(), this.getConjugatedPortDefinition(), this.getConjugatedPortDefinition_OwnedPortConjugator(), "conjugatedPortDefinition",
                null, 1,
                1, PortConjugation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getPortConjugation_OriginalPortDefinition(), this.getPortDefinition(), null, "originalPortDefinition", null, 1, 1, PortConjugation.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.portDefinitionEClass, PortDefinition.class, "PortDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getPortDefinition_ConjugatedPortDefinition(), this.getConjugatedPortDefinition(), this.getConjugatedPortDefinition_OriginalPortDefinition(),
                "conjugatedPortDefinition", null, 0,
                1, PortDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.portUsageEClass, PortUsage.class, "PortUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getPortUsage_PortDefinition(), this.getPortDefinition(), null, "portDefinition", null, 0, -1, PortUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.predicateEClass, Predicate.class, "Predicate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.redefinitionEClass, Redefinition.class, "Redefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getRedefinition_RedefinedFeature(), this.getFeature(), null, "redefinedFeature", null, 1, 1, Redefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getRedefinition_RedefiningFeature(), this.getFeature(), null, "redefiningFeature", null, 1, 1, Redefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.referenceSubsettingEClass, ReferenceSubsetting.class, "ReferenceSubsetting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getReferenceSubsetting_ReferencedFeature(), this.getFeature(), null, "referencedFeature", null, 1, 1, ReferenceSubsetting.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getReferenceSubsetting_ReferencingFeature(), this.getFeature(), this.getFeature_OwnedReferenceSubsetting(), "referencingFeature", null, 1, 1,
                ReferenceSubsetting.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.referenceUsageEClass, ReferenceUsage.class, "ReferenceUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.relationshipEClass, Relationship.class, "Relationship", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getRelationship_IsImplied(), this.ecorePackage.getEBoolean(), "isImplied", "false", 1, 1, Relationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getRelationship_OwnedRelatedElement(), this.getElement(), this.getElement_OwningRelationship(), "ownedRelatedElement", null, 0, -1, Relationship.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRelationship_OwningRelatedElement(), this.getElement(), this.getElement_OwnedRelationship(), "owningRelatedElement", null, 0, 1, Relationship.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getRelationship_RelatedElement(), this.getElement(), null, "relatedElement", null, 0, -1, Relationship.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRelationship_Source(), this.getElement(), null, "source", null, 0, -1, Relationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRelationship_Target(), this.getElement(), null, "target", null, 0, -1, Relationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        this.initEClass(this.renderingDefinitionEClass, RenderingDefinition.class, "RenderingDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getRenderingDefinition_Rendering(), this.getRenderingUsage(), null, "rendering", null, 0, -1, RenderingDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.renderingUsageEClass, RenderingUsage.class, "RenderingUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getRenderingUsage_RenderingDefinition(), this.getRenderingDefinition(), null, "renderingDefinition", null, 0, 1, RenderingUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.requirementConstraintMembershipEClass, RequirementConstraintMembership.class, "RequirementConstraintMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getRequirementConstraintMembership_Kind(), this.getRequirementConstraintKind(), "kind", null, 1, 1, RequirementConstraintMembership.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getRequirementConstraintMembership_OwnedConstraint(), this.getConstraintUsage(), null, "ownedConstraint", null, 1, 1, RequirementConstraintMembership.class,
                IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getRequirementConstraintMembership_ReferencedConstraint(), this.getConstraintUsage(), null, "referencedConstraint", null, 1, 1, RequirementConstraintMembership.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.requirementDefinitionEClass, RequirementDefinition.class, "RequirementDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getRequirementDefinition_ReqId(), this.ecorePackage.getEString(), "reqId", null, 0, 1, RequirementDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getRequirementDefinition_Text(), this.ecorePackage.getEString(), "text", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getRequirementDefinition_ActorParameter(), this.getPartUsage(), null, "actorParameter", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementDefinition_AssumedConstraint(), this.getConstraintUsage(), null, "assumedConstraint", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementDefinition_FramedConcern(), this.getConcernUsage(), null, "framedConcern", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementDefinition_RequiredConstraint(), this.getConstraintUsage(), null, "requiredConstraint", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementDefinition_StakeholderParameter(), this.getPartUsage(), null, "stakeholderParameter", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementDefinition_SubjectParameter(), this.getUsage(), null, "subjectParameter", null, 1, 1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.requirementUsageEClass, RequirementUsage.class, "RequirementUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getRequirementUsage_ReqId(), this.ecorePackage.getEString(), "reqId", null, 0, 1, RequirementUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getRequirementUsage_Text(), this.ecorePackage.getEString(), "text", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE,
                IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getRequirementUsage_ActorParameter(), this.getPartUsage(), null, "actorParameter", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementUsage_AssumedConstraint(), this.getConstraintUsage(), null, "assumedConstraint", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementUsage_FramedConcern(), this.getConcernUsage(), null, "framedConcern", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementUsage_RequiredConstraint(), this.getConstraintUsage(), null, "requiredConstraint", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementUsage_RequirementDefinition(), this.getRequirementDefinition(), null, "requirementDefinition", null, 0, 1, RequirementUsage.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getRequirementUsage_StakeholderParameter(), this.getPartUsage(), null, "stakeholderParameter", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getRequirementUsage_SubjectParameter(), this.getUsage(), null, "subjectParameter", null, 1, 1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.requirementVerificationMembershipEClass, RequirementVerificationMembership.class, "RequirementVerificationMembership", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getRequirementVerificationMembership_OwnedRequirement(), this.getRequirementUsage(), null, "ownedRequirement", null, 1, 1, RequirementVerificationMembership.class,
                IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getRequirementVerificationMembership_VerifiedRequirement(), this.getRequirementUsage(), null, "verifiedRequirement", null, 1, 1,
                RequirementVerificationMembership.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.resultExpressionMembershipEClass, ResultExpressionMembership.class, "ResultExpressionMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getResultExpressionMembership_OwnedResultExpression(), this.getExpression(), null, "ownedResultExpression", null, 1, 1, ResultExpressionMembership.class, IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.returnParameterMembershipEClass, ReturnParameterMembership.class, "ReturnParameterMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.satisfyRequirementUsageEClass, SatisfyRequirementUsage.class, "SatisfyRequirementUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getSatisfyRequirementUsage_SatisfiedRequirement(), this.getRequirementUsage(), null, "satisfiedRequirement", null, 1, 1, SatisfyRequirementUsage.class, IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSatisfyRequirementUsage_SatisfyingFeature(), this.getFeature(), null, "satisfyingFeature", null, 1, 1, SatisfyRequirementUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.selectExpressionEClass, SelectExpression.class, "SelectExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.sendActionUsageEClass, SendActionUsage.class, "SendActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getSendActionUsage_PayloadArgument(), this.getExpression(), null, "payloadArgument", null, 1, 1, SendActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSendActionUsage_ReceiverArgument(), this.getExpression(), null, "receiverArgument", null, 0, 1, SendActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSendActionUsage_SenderArgument(), this.getExpression(), null, "senderArgument", null, 0, 1, SendActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.specializationEClass, Specialization.class, "Specialization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getSpecialization_General(), this.getType(), null, "general", null, 1, 1, Specialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSpecialization_OwningType(), this.getType(), this.getType_OwnedSpecialization(), "owningType", null, 0, 1, Specialization.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSpecialization_Specific(), this.getType(), null, "specific", null, 1, 1, Specialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.stakeholderMembershipEClass, StakeholderMembership.class, "StakeholderMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getStakeholderMembership_OwnedStakeholderParameter(), this.getPartUsage(), null, "ownedStakeholderParameter", null, 1, 1, StakeholderMembership.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.stateDefinitionEClass, StateDefinition.class, "StateDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getStateDefinition_IsParallel(), this.ecorePackage.getEBoolean(), "isParallel", "false", 1, 1, StateDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getStateDefinition_DoAction(), this.getActionUsage(), null, "doAction", null, 0, 1, StateDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getStateDefinition_EntryAction(), this.getActionUsage(), null, "entryAction", null, 0, 1, StateDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getStateDefinition_ExitAction(), this.getActionUsage(), null, "exitAction", null, 0, 1, StateDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getStateDefinition_State(), this.getStateUsage(), null, "state", null, 0, -1, StateDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.stateSubactionMembershipEClass, StateSubactionMembership.class, "StateSubactionMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getStateSubactionMembership_Kind(), this.getStateSubactionKind(), "kind", null, 1, 1, StateSubactionMembership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getStateSubactionMembership_Action(), this.getActionUsage(), null, "action", null, 1, 1, StateSubactionMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.stateUsageEClass, StateUsage.class, "StateUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getStateUsage_IsParallel(), this.ecorePackage.getEBoolean(), "isParallel", "false", 1, 1, StateUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
                !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getStateUsage_DoAction(), this.getActionUsage(), null, "doAction", null, 0, 1, StateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getStateUsage_EntryAction(), this.getActionUsage(), null, "entryAction", null, 0, 1, StateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getStateUsage_ExitAction(), this.getActionUsage(), null, "exitAction", null, 0, 1, StateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getStateUsage_StateDefinition(), this.getBehavior(), null, "stateDefinition", null, 0, -1, StateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        op = this.initEOperation(this.getStateUsage__IsSubstateUsage__boolean(), this.ecorePackage.getEBoolean(), "isSubstateUsage", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEBoolean(), "isParallel", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.stepEClass, Step.class, "Step", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getStep_Behavior(), this.getBehavior(), null, "behavior", null, 0, -1, Step.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getStep_Parameter(), this.getFeature(), null, "parameter", null, 0, -1, Step.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.structureEClass, Structure.class, "Structure", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.subclassificationEClass, Subclassification.class, "Subclassification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getSubclassification_OwningClassifier(), this.getClassifier(), this.getClassifier_OwnedSubclassification(), "owningClassifier", null, 0, 1, Subclassification.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSubclassification_Subclassifier(), this.getClassifier(), null, "subclassifier", null, 1, 1, Subclassification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSubclassification_Superclassifier(), this.getClassifier(), null, "superclassifier", null, 1, 1, Subclassification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.subjectMembershipEClass, SubjectMembership.class, "SubjectMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getSubjectMembership_OwnedSubjectParameter(), this.getUsage(), null, "ownedSubjectParameter", null, 1, 1, SubjectMembership.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.subsettingEClass, Subsetting.class, "Subsetting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getSubsetting_OwningFeature(), this.getFeature(), this.getFeature_OwnedSubsetting(), "owningFeature", null, 1, 1, Subsetting.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSubsetting_SubsettedFeature(), this.getFeature(), null, "subsettedFeature", null, 1, 1, Subsetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSubsetting_SubsettingFeature(), this.getFeature(), null, "subsettingFeature", null, 1, 1, Subsetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.successionEClass, Succession.class, "Succession", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getSuccession_EffectStep(), this.getStep(), null, "effectStep", null, 0, -1, Succession.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSuccession_GuardExpression(), this.getExpression(), null, "guardExpression", null, 0, -1, Succession.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSuccession_TransitionStep(), this.getStep(), null, "transitionStep", null, 0, 1, Succession.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getSuccession_TriggerStep(), this.getStep(), null, "triggerStep", null, 0, -1, Succession.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.successionAsUsageEClass, SuccessionAsUsage.class, "SuccessionAsUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.successionFlowConnectionUsageEClass, SuccessionFlowConnectionUsage.class, "SuccessionFlowConnectionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.successionItemFlowEClass, SuccessionItemFlow.class, "SuccessionItemFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.textualRepresentationEClass, TextualRepresentation.class, "TextualRepresentation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getTextualRepresentation_Body(), this.ecorePackage.getEString(), "body", null, 1, 1, TextualRepresentation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getTextualRepresentation_Language(), this.ecorePackage.getEString(), "language", null, 1, 1, TextualRepresentation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getTextualRepresentation_RepresentedElement(), this.getElement(), this.getElement_TextualRepresentation(), "representedElement", null, 1, 1,
                TextualRepresentation.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.transitionFeatureMembershipEClass, TransitionFeatureMembership.class, "TransitionFeatureMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getTransitionFeatureMembership_Kind(), this.getTransitionFeatureKind(), "kind", null, 1, 1, TransitionFeatureMembership.class, !IS_TRANSIENT, !IS_VOLATILE,
                IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getTransitionFeatureMembership_TransitionFeature(), this.getStep(), null, "transitionFeature", null, 1, 1, TransitionFeatureMembership.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.transitionUsageEClass, TransitionUsage.class, "TransitionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getTransitionUsage_EffectAction(), this.getActionUsage(), null, "effectAction", null, 0, -1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getTransitionUsage_GuardExpression(), this.getExpression(), null, "guardExpression", null, 0, -1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getTransitionUsage_Source(), this.getActionUsage(), null, "source", null, 1, 1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getTransitionUsage_Succession(), this.getSuccession(), null, "succession", null, 1, 1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getTransitionUsage_Target(), this.getActionUsage(), null, "target", null, 1, 1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getTransitionUsage_TriggerAction(), this.getAcceptActionUsage(), null, "triggerAction", null, 0, -1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEOperation(this.getTransitionUsage__TriggerPayloadParameter(), this.getReferenceUsage(), "triggerPayloadParameter", 0, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.triggerInvocationExpressionEClass, TriggerInvocationExpression.class, "TriggerInvocationExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getTriggerInvocationExpression_Kind(), this.getTriggerKind(), "kind", null, 1, 1, TriggerInvocationExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE,
                !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.typeEClass, Type.class, "Type", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getType_IsAbstract(), this.ecorePackage.getEBoolean(), "isAbstract", "false", 1, 1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getType_IsConjugated(), this.ecorePackage.getEBoolean(), "isConjugated", null, 1, 1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getType_IsSufficient(), this.ecorePackage.getEBoolean(), "isSufficient", "false", 1, 1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getType_DifferencingType(), this.getType(), null, "differencingType", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_DirectedFeature(), this.getFeature(), null, "directedFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_EndFeature(), this.getFeature(), null, "endFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_Feature(), this.getFeature(), null, "feature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_FeatureMembership(), this.getFeatureMembership(), null, "featureMembership", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_InheritedFeature(), this.getFeature(), null, "inheritedFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_InheritedMembership(), this.getMembership(), null, "inheritedMembership", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_Input(), this.getFeature(), null, "input", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_IntersectingType(), this.getType(), null, "intersectingType", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_Multiplicity(), this.getMultiplicity(), null, "multiplicity", null, 0, 1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getType_Output(), this.getFeature(), null, "output", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_OwnedConjugator(), this.getConjugation(), this.getConjugation_OwningType(), "ownedConjugator", null, 0, 1, Type.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getType_OwnedDifferencing(), this.getDifferencing(), this.getDifferencing_TypeDifferenced(), "ownedDifferencing", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_OwnedDisjoining(), this.getDisjoining(), this.getDisjoining_OwningType(), "ownedDisjoining", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getType_OwnedEndFeature(), this.getFeature(), this.getFeature_EndOwningType(), "ownedEndFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_OwnedFeature(), this.getFeature(), this.getFeature_OwningType(), "ownedFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_OwnedFeatureMembership(), this.getFeatureMembership(), this.getFeatureMembership_OwningType(), "ownedFeatureMembership", null, 0, -1, Type.class, IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_OwnedIntersecting(), this.getIntersecting(), this.getIntersecting_TypeIntersected(), "ownedIntersecting", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_OwnedSpecialization(), this.getSpecialization(), this.getSpecialization_OwningType(), "ownedSpecialization", null, 0, -1, Type.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_OwnedUnioning(), this.getUnioning(), this.getUnioning_TypeUnioned(), "ownedUnioning", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getType_UnioningType(), this.getType(), null, "unioningType", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEOperation(this.getType__AllSupertypes(), this.getType(), "allSupertypes", 0, -1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getType__DirectionOf__Feature(), this.getFeatureDirectionKind(), "directionOf", 0, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getFeature(), "feature", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getType__InheritedMemberships__EList(), this.getMembership(), "inheritedMemberships", 0, -1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.getType(), "excluded", 0, -1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getType__Specializes__Type(), this.ecorePackage.getEBoolean(), "specializes", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getType(), "supertype", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = this.initEOperation(this.getType__SpecializesFromLibrary__String(), this.ecorePackage.getEBoolean(), "specializesFromLibrary", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "libraryTypeName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.typeFeaturingEClass, TypeFeaturing.class, "TypeFeaturing", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getTypeFeaturing_FeatureOfType(), this.getFeature(), null, "featureOfType", null, 1, 1, TypeFeaturing.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getTypeFeaturing_FeaturingType(), this.getType(), null, "featuringType", null, 1, 1, TypeFeaturing.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getTypeFeaturing_OwningFeatureOfType(), this.getFeature(), this.getFeature_OwnedTypeFeaturing(), "owningFeatureOfType", null, 0, 1, TypeFeaturing.class, IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.unioningEClass, Unioning.class, "Unioning", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getUnioning_TypeUnioned(), this.getType(), this.getType_OwnedUnioning(), "typeUnioned", null, 1, 1, Unioning.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getUnioning_UnioningType(), this.getType(), null, "unioningType", null, 1, 1, Unioning.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.usageEClass, Usage.class, "Usage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getUsage_IsReference(), this.ecorePackage.getEBoolean(), "isReference", null, 1, 1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getUsage_IsVariation(), this.ecorePackage.getEBoolean(), "isVariation", null, 1, 1, Usage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getUsage_Definition(), this.getClassifier(), null, "definition", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_DirectedUsage(), this.getUsage(), null, "directedUsage", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedAction(), this.getActionUsage(), null, "nestedAction", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedAllocation(), this.getAllocationUsage(), null, "nestedAllocation", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedAnalysisCase(), this.getAnalysisCaseUsage(), null, "nestedAnalysisCase", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedAttribute(), this.getAttributeUsage(), null, "nestedAttribute", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedCalculation(), this.getCalculationUsage(), null, "nestedCalculation", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedCase(), this.getCaseUsage(), null, "nestedCase", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedConcern(), this.getConcernUsage(), null, "nestedConcern", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getUsage_NestedConnection(), this.getConnectorAsUsage(), null, "nestedConnection", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedConstraint(), this.getConstraintUsage(), null, "nestedConstraint", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedEnumeration(), this.getEnumerationUsage(), null, "nestedEnumeration", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedFlow(), this.getFlowConnectionUsage(), null, "nestedFlow", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getUsage_NestedInterface(), this.getInterfaceUsage(), null, "nestedInterface", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedItem(), this.getItemUsage(), null, "nestedItem", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedMetadata(), this.getMetadataUsage(), null, "nestedMetadata", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedOccurrence(), this.getOccurrenceUsage(), null, "nestedOccurrence", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedPart(), this.getPartUsage(), null, "nestedPart", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedPort(), this.getPortUsage(), null, "nestedPort", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedReference(), this.getReferenceUsage(), null, "nestedReference", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedRendering(), this.getRenderingUsage(), null, "nestedRendering", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedRequirement(), this.getRequirementUsage(), null, "nestedRequirement", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedState(), this.getStateUsage(), null, "nestedState", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedTransition(), this.getTransitionUsage(), null, "nestedTransition", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getUsage_NestedUsage(), this.getUsage(), this.getUsage_OwningUsage(), "nestedUsage", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedUseCase(), this.getUseCaseUsage(), null, "nestedUseCase", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedVerificationCase(), this.getVerificationCaseUsage(), null, "nestedVerificationCase", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedView(), this.getViewUsage(), null, "nestedView", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_NestedViewpoint(), this.getViewpointUsage(), null, "nestedViewpoint", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_OwningDefinition(), this.getDefinition(), this.getDefinition_OwnedUsage(), "owningDefinition", null, 0, 1, Usage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getUsage_OwningUsage(), this.getUsage(), this.getUsage_NestedUsage(), "owningUsage", null, 0, 1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getUsage_Usage(), this.getUsage(), null, "usage", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUsage_Variant(), this.getUsage(), null, "variant", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE,
                IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getUsage_VariantMembership(), this.getVariantMembership(), null, "variantMembership", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.useCaseDefinitionEClass, UseCaseDefinition.class, "UseCaseDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getUseCaseDefinition_IncludedUseCase(), this.getUseCaseUsage(), null, "includedUseCase", null, 0, -1, UseCaseDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.useCaseUsageEClass, UseCaseUsage.class, "UseCaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getUseCaseUsage_IncludedUseCase(), this.getUseCaseUsage(), null, "includedUseCase", null, 0, -1, UseCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getUseCaseUsage_UseCaseDefinition(), this.getUseCaseDefinition(), null, "useCaseDefinition", null, 0, 1, UseCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.variantMembershipEClass, VariantMembership.class, "VariantMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getVariantMembership_OwnedVariantUsage(), this.getUsage(), null, "ownedVariantUsage", null, 1, 1, VariantMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.verificationCaseDefinitionEClass, VerificationCaseDefinition.class, "VerificationCaseDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getVerificationCaseDefinition_VerifiedRequirement(), this.getRequirementUsage(), null, "verifiedRequirement", null, 0, -1, VerificationCaseDefinition.class,
                IS_TRANSIENT,
                IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.verificationCaseUsageEClass, VerificationCaseUsage.class, "VerificationCaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getVerificationCaseUsage_VerificationCaseDefinition(), this.getVerificationCaseDefinition(), null, "verificationCaseDefinition", null, 0, 1,
                VerificationCaseUsage.class,
                IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getVerificationCaseUsage_VerifiedRequirement(), this.getRequirementUsage(), null, "verifiedRequirement", null, 0, -1, VerificationCaseUsage.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.viewDefinitionEClass, ViewDefinition.class, "ViewDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getViewDefinition_SatisfiedViewpoint(), this.getViewpointUsage(), null, "satisfiedViewpoint", null, 0, -1, ViewDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getViewDefinition_View(), this.getViewUsage(), null, "view", null, 0, -1, ViewDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES,
                !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getViewDefinition_ViewCondition(), this.getExpression(), null, "viewCondition", null, 0, -1, ViewDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getViewDefinition_ViewRendering(), this.getRenderingUsage(), null, "viewRendering", null, 0, 1, ViewDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.viewpointDefinitionEClass, ViewpointDefinition.class, "ViewpointDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getViewpointDefinition_ViewpointStakeholder(), this.getPartUsage(), null, "viewpointStakeholder", null, 0, -1, ViewpointDefinition.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.viewpointUsageEClass, ViewpointUsage.class, "ViewpointUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getViewpointUsage_ViewpointDefinition(), this.getViewpointDefinition(), null, "viewpointDefinition", null, 0, 1, ViewpointUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getViewpointUsage_ViewpointStakeholder(), this.getPartUsage(), null, "viewpointStakeholder", null, 0, -1, ViewpointUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        this.initEClass(this.viewRenderingMembershipEClass, ViewRenderingMembership.class, "ViewRenderingMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getViewRenderingMembership_OwnedRendering(), this.getRenderingUsage(), null, "ownedRendering", null, 1, 1, ViewRenderingMembership.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getViewRenderingMembership_ReferencedRendering(), this.getRenderingUsage(), null, "referencedRendering", null, 1, 1, ViewRenderingMembership.class, IS_TRANSIENT,
                IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.viewUsageEClass, ViewUsage.class, "ViewUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getViewUsage_ExposedElement(), this.getElement(), null, "exposedElement", null, 0, -1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getViewUsage_SatisfiedViewpoint(), this.getViewpointUsage(), null, "satisfiedViewpoint", null, 0, -1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE,
                !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getViewUsage_ViewCondition(), this.getExpression(), null, "viewCondition", null, 0, -1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        this.initEReference(this.getViewUsage_ViewDefinition(), this.getViewDefinition(), null, "viewDefinition", null, 0, 1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getViewUsage_ViewRendering(), this.getRenderingUsage(), null, "viewRendering", null, 0, 1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE,
                IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = this.initEOperation(this.getViewUsage__IncludeAsExposed__Element(), this.ecorePackage.getEBoolean(), "includeAsExposed", 1, 1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.getElement(), "element", 1, 1, IS_UNIQUE, !IS_ORDERED);

        this.initEClass(this.whileLoopActionUsageEClass, WhileLoopActionUsage.class, "WhileLoopActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getWhileLoopActionUsage_UntilArgument(), this.getExpression(), null, "untilArgument", null, 0, 1, WhileLoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        this.initEReference(this.getWhileLoopActionUsage_WhileArgument(), this.getExpression(), null, "whileArgument", null, 1, 1, WhileLoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE,
                !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        // Initialize enums and add enum literals
        this.initEEnum(this.featureDirectionKindEEnum, FeatureDirectionKind.class, "FeatureDirectionKind");
        this.addEEnumLiteral(this.featureDirectionKindEEnum, FeatureDirectionKind.IN);
        this.addEEnumLiteral(this.featureDirectionKindEEnum, FeatureDirectionKind.INOUT);
        this.addEEnumLiteral(this.featureDirectionKindEEnum, FeatureDirectionKind.OUT);

        this.initEEnum(this.portionKindEEnum, PortionKind.class, "PortionKind");
        this.addEEnumLiteral(this.portionKindEEnum, PortionKind.SNAPSHOT);
        this.addEEnumLiteral(this.portionKindEEnum, PortionKind.TIMESLICE);

        this.initEEnum(this.requirementConstraintKindEEnum, RequirementConstraintKind.class, "RequirementConstraintKind");
        this.addEEnumLiteral(this.requirementConstraintKindEEnum, RequirementConstraintKind.ASSUMPTION);
        this.addEEnumLiteral(this.requirementConstraintKindEEnum, RequirementConstraintKind.REQUIREMENT);

        this.initEEnum(this.stateSubactionKindEEnum, StateSubactionKind.class, "StateSubactionKind");
        this.addEEnumLiteral(this.stateSubactionKindEEnum, StateSubactionKind.DO);
        this.addEEnumLiteral(this.stateSubactionKindEEnum, StateSubactionKind.ENTRY);
        this.addEEnumLiteral(this.stateSubactionKindEEnum, StateSubactionKind.EXIT);

        this.initEEnum(this.transitionFeatureKindEEnum, TransitionFeatureKind.class, "TransitionFeatureKind");
        this.addEEnumLiteral(this.transitionFeatureKindEEnum, TransitionFeatureKind.EFFECT);
        this.addEEnumLiteral(this.transitionFeatureKindEEnum, TransitionFeatureKind.GUARD);
        this.addEEnumLiteral(this.transitionFeatureKindEEnum, TransitionFeatureKind.TRIGGER);

        this.initEEnum(this.triggerKindEEnum, TriggerKind.class, "TriggerKind");
        this.addEEnumLiteral(this.triggerKindEEnum, TriggerKind.AFTER);
        this.addEEnumLiteral(this.triggerKindEEnum, TriggerKind.AT);
        this.addEEnumLiteral(this.triggerKindEEnum, TriggerKind.WHEN);

        this.initEEnum(this.visibilityKindEEnum, VisibilityKind.class, "VisibilityKind");
        this.addEEnumLiteral(this.visibilityKindEEnum, VisibilityKind.PRIVATE);
        this.addEEnumLiteral(this.visibilityKindEEnum, VisibilityKind.PROTECTED);
        this.addEEnumLiteral(this.visibilityKindEEnum, VisibilityKind.PUBLIC);

        // Create resource
        this.createResource(eNS_URI);

        // Create annotations
        // subsets
        this.createSubsetsAnnotations();
        // redefines
        this.createRedefinesAnnotations();
    }

    /**
     * Initializes the annotations for <b>subsets</b>. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void createSubsetsAnnotations() {
        String source = "subsets";
        this.addAnnotation(this.getAcceptActionUsage_PayloadParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedReference"),
                        URI.createURI(eNS_URI).appendFragment("//Step/parameter")
                });
        this.addAnnotation(this.getActionDefinition_Action(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Behavior/step"),
                        URI.createURI(eNS_URI).appendFragment("//Definition/usage")
                });
        this.addAnnotation(this.getAllocationDefinition_Allocation(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/usage")
                });
        this.addAnnotation(this.getAnalysisCaseDefinition_AnalysisAction(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ActionDefinition/action")
                });
        this.addAnnotation(this.getAnalysisCaseDefinition_ResultExpression(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Function/expression"),
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getAnalysisCaseUsage_AnalysisAction(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/usage")
                });
        this.addAnnotation(this.getAnalysisCaseUsage_ResultExpression(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getAnnotation_OwningAnnotatedElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Annotation/annotatedElement"),
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getAssignmentActionUsage_Referent(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/member")
                });
        this.addAnnotation(this.getAssociation_SourceType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Association/relatedType")
                });
        this.addAnnotation(this.getAssociation_TargetType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Association/relatedType")
                });
        this.addAnnotation(this.getBehavior_Step(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/feature")
                });
        this.addAnnotation(this.getCalculationDefinition_Calculation(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ActionDefinition/action"),
                        URI.createURI(eNS_URI).appendFragment("//Function/expression")
                });
        this.addAnnotation(this.getCaseDefinition_ActorParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Behavior/parameter"),
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedPart")
                });
        this.addAnnotation(this.getCaseDefinition_ObjectiveRequirement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedRequirement")
                });
        this.addAnnotation(this.getCaseDefinition_SubjectParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Behavior/parameter"),
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedUsage")
                });
        this.addAnnotation(this.getCaseUsage_ActorParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedPart"),
                        URI.createURI(eNS_URI).appendFragment("//Step/parameter")
                });
        this.addAnnotation(this.getCaseUsage_ObjectiveRequirement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedRequirement")
                });
        this.addAnnotation(this.getCaseUsage_SubjectParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Step/parameter"),
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedUsage")
                });
        this.addAnnotation(this.getClassifier_OwnedSubclassification(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedSpecialization")
                });
        this.addAnnotation(this.getConjugation_OwningType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Conjugation/conjugatedType"),
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getConnectionUsage_ConnectionDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ItemUsage/itemDefinition")
                });
        this.addAnnotation(this.getConnector_SourceFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Connector/relatedFeature")
                });
        this.addAnnotation(this.getConnector_TargetFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Connector/relatedFeature")
                });
        this.addAnnotation(this.getDefinition_DirectedUsage(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/usage"),
                        URI.createURI(eNS_URI).appendFragment("//Type/directedFeature")
                });
        this.addAnnotation(this.getDefinition_OwnedAction(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedOccurrence")
                });
        this.addAnnotation(this.getDefinition_OwnedAllocation(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedConnection")
                });
        this.addAnnotation(this.getDefinition_OwnedAnalysisCase(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedCase")
                });
        this.addAnnotation(this.getDefinition_OwnedAttribute(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedUsage")
                });
        this.addAnnotation(this.getDefinition_OwnedCalculation(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedAction")
                });
        this.addAnnotation(this.getDefinition_OwnedCase(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedCalculation")
                });
        this.addAnnotation(this.getDefinition_OwnedConcern(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedRequirement")
                });
        this.addAnnotation(this.getDefinition_OwnedConnection(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedPart")
                });
        this.addAnnotation(this.getDefinition_OwnedConstraint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedOccurrence")
                });
        this.addAnnotation(this.getDefinition_OwnedEnumeration(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedAttribute")
                });
        this.addAnnotation(this.getDefinition_OwnedFlow(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedConnection")
                });
        this.addAnnotation(this.getDefinition_OwnedInterface(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedConnection")
                });
        this.addAnnotation(this.getDefinition_OwnedItem(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedOccurrence")
                });
        this.addAnnotation(this.getDefinition_OwnedMetadata(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedItem")
                });
        this.addAnnotation(this.getDefinition_OwnedOccurrence(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedUsage")
                });
        this.addAnnotation(this.getDefinition_OwnedPart(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedItem")
                });
        this.addAnnotation(this.getDefinition_OwnedPort(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedUsage")
                });
        this.addAnnotation(this.getDefinition_OwnedReference(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedUsage")
                });
        this.addAnnotation(this.getDefinition_OwnedRendering(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedPart")
                });
        this.addAnnotation(this.getDefinition_OwnedRequirement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedConstraint")
                });
        this.addAnnotation(this.getDefinition_OwnedState(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedAction")
                });
        this.addAnnotation(this.getDefinition_OwnedTransition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedUsage")
                });
        this.addAnnotation(this.getDefinition_OwnedUsage(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature"),
                        URI.createURI(eNS_URI).appendFragment("//Definition/usage")
                });
        this.addAnnotation(this.getDefinition_OwnedUseCase(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedCase")
                });
        this.addAnnotation(this.getDefinition_OwnedVerificationCase(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedCase")
                });
        this.addAnnotation(this.getDefinition_OwnedView(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedPart")
                });
        this.addAnnotation(this.getDefinition_OwnedViewpoint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedRequirement")
                });
        this.addAnnotation(this.getDefinition_Usage(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/feature")
                });
        this.addAnnotation(this.getDefinition_Variant(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getDefinition_VariantMembership(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMembership")
                });
        this.addAnnotation(this.getDifferencing_TypeDifferenced(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getDisjoining_OwningType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Disjoining/typeDisjoined"),
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getDocumentation_DocumentedElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/owner")
                });
        this.addAnnotation(this.getElement_Documentation(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedElement")
                });
        this.addAnnotation(this.getElement_OwnedAnnotation(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getElement_OwningMembership(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/owningRelationship")
                });
        this.addAnnotation(this.getElement_TextualRepresentation(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedElement")
                });
        this.addAnnotation(this.getExpression_Result(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Step/parameter"),
                        URI.createURI(eNS_URI).appendFragment("//Type/output")
                });
        this.addAnnotation(this.getFeature_EndOwningType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Feature/owningType")
                });
        this.addAnnotation(this.getFeature_OwnedFeatureChaining(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getFeature_OwnedFeatureInverting(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getFeature_OwnedRedefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Feature/ownedSubsetting")
                });
        this.addAnnotation(this.getFeature_OwnedReferenceSubsetting(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Feature/ownedSubsetting")
                });
        this.addAnnotation(this.getFeature_OwnedSubsetting(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedSpecialization")
                });
        this.addAnnotation(this.getFeature_OwnedTypeFeaturing(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getFeature_OwnedTyping(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedSpecialization")
                });
        this.addAnnotation(this.getFeature_OwningFeatureMembership(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/owningMembership")
                });
        this.addAnnotation(this.getFeature_OwningType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/owningNamespace"),
                        URI.createURI(eNS_URI).appendFragment("//Feature/featuringType")
                });
        this.addAnnotation(this.getFeatureChainExpression_TargetFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/member")
                });
        this.addAnnotation(this.getFeatureChaining_FeatureChained(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getFeatureInverting_OwningFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement"),
                        URI.createURI(eNS_URI).appendFragment("//FeatureInverting/featureInverted")
                });
        this.addAnnotation(this.getFeatureReferenceExpression_Referent(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/member")
                });
        this.addAnnotation(this.getFeatureTyping_OwningFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//FeatureTyping/typedFeature")
                });
        this.addAnnotation(this.getFeatureValue_FeatureWithValue(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Membership/membershipOwningNamespace")
                });
        this.addAnnotation(this.getFeaturing_Feature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/relatedElement")
                });
        this.addAnnotation(this.getFeaturing_Type(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/relatedElement")
                });
        this.addAnnotation(this.getFunction_Expression(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Behavior/step")
                });
        this.addAnnotation(this.getFunction_Result(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Behavior/parameter"),
                        URI.createURI(eNS_URI).appendFragment("//Type/output")
                });
        this.addAnnotation(this.getImport_ImportOwningNamespace(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getIntersecting_TypeIntersected(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getInvocationExpression_Argument(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getItemFlow_ItemFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getItemFlow_ItemFlowEnd(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Connector/connectorEnd")
                });
        this.addAnnotation(this.getItemUsage_ItemDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//OccurrenceUsage/occurrenceDefinition")
                });
        this.addAnnotation(this.getMembership_MembershipOwningNamespace(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getMetadataFeature_Metaclass(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Feature/type")
                });
        this.addAnnotation(this.getMultiplicityRange_LowerBound(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//MultiplicityRange/bound")
                });
        this.addAnnotation(this.getMultiplicityRange_UpperBound(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//MultiplicityRange/bound")
                });
        this.addAnnotation(this.getNamespace_ImportedMembership(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/membership")
                });
        this.addAnnotation(this.getNamespace_OwnedImport(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getNamespace_OwnedMember(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/member")
                });
        this.addAnnotation(this.getNamespace_OwnedMembership(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/membership"),
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getOccurrenceDefinition_LifeClass(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getOccurrenceUsage_IndividualDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//OccurrenceUsage/occurrenceDefinition")
                });
        this.addAnnotation(this.getOwningMembership_OwnedMemberElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/ownedRelatedElement")
                });
        this.addAnnotation(this.getPackage_FilterCondition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getPartUsage_PartDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ItemUsage/itemDefinition")
                });
        this.addAnnotation(this.getPortDefinition_ConjugatedPortDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getRelationship_OwnedRelatedElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/relatedElement")
                });
        this.addAnnotation(this.getRelationship_OwningRelatedElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/relatedElement")
                });
        this.addAnnotation(this.getRelationship_Source(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/relatedElement")
                });
        this.addAnnotation(this.getRelationship_Target(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/relatedElement")
                });
        this.addAnnotation(this.getRenderingDefinition_Rendering(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/usage")
                });
        this.addAnnotation(this.getRequirementDefinition_ActorParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedPart"),
                        URI.createURI(eNS_URI).appendFragment("//Behavior/parameter")
                });
        this.addAnnotation(this.getRequirementDefinition_AssumedConstraint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getRequirementDefinition_FramedConcern(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//RequirementDefinition/requiredConstraint")
                });
        this.addAnnotation(this.getRequirementDefinition_RequiredConstraint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getRequirementDefinition_StakeholderParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedPart"),
                        URI.createURI(eNS_URI).appendFragment("//Behavior/parameter")
                });
        this.addAnnotation(this.getRequirementDefinition_SubjectParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Behavior/parameter"),
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedUsage")
                });
        this.addAnnotation(this.getRequirementUsage_ActorParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedPart"),
                        URI.createURI(eNS_URI).appendFragment("//Step/parameter")
                });
        this.addAnnotation(this.getRequirementUsage_AssumedConstraint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getRequirementUsage_FramedConcern(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//RequirementUsage/requiredConstraint")
                });
        this.addAnnotation(this.getRequirementUsage_RequiredConstraint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getRequirementUsage_StakeholderParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedPart"),
                        URI.createURI(eNS_URI).appendFragment("//Step/parameter")
                });
        this.addAnnotation(this.getRequirementUsage_SubjectParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Step/parameter"),
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedUsage")
                });
        this.addAnnotation(this.getSpecialization_OwningType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/specific"),
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getStateDefinition_State(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ActionDefinition/action")
                });
        this.addAnnotation(this.getStep_Behavior(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Feature/type")
                });
        this.addAnnotation(this.getSubsetting_OwningFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Subsetting/subsettingFeature")
                });
        this.addAnnotation(this.getTextualRepresentation_RepresentedElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/owner")
                });
        this.addAnnotation(this.getTransitionUsage_EffectAction(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/feature")
                });
        this.addAnnotation(this.getTransitionUsage_GuardExpression(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getTransitionUsage_Succession(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getTransitionUsage_TriggerAction(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getType_DirectedFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/feature")
                });
        this.addAnnotation(this.getType_EndFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/feature")
                });
        this.addAnnotation(this.getType_Feature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/member")
                });
        this.addAnnotation(this.getType_InheritedFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/feature")
                });
        this.addAnnotation(this.getType_InheritedMembership(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/membership")
                });
        this.addAnnotation(this.getType_Input(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/directedFeature")
                });
        this.addAnnotation(this.getType_Multiplicity(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getType_Output(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/directedFeature")
                });
        this.addAnnotation(this.getType_OwnedConjugator(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getType_OwnedDifferencing(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getType_OwnedDisjoining(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getType_OwnedEndFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/endFeature"),
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature")
                });
        this.addAnnotation(this.getType_OwnedFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getType_OwnedFeatureMembership(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMembership"),
                        URI.createURI(eNS_URI).appendFragment("//Type/featureMembership")
                });
        this.addAnnotation(this.getType_OwnedIntersecting(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getType_OwnedSpecialization(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getType_OwnedUnioning(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/ownedRelationship")
                });
        this.addAnnotation(this.getTypeFeaturing_OwningFeatureOfType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//TypeFeaturing/featureOfType"),
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getUnioning_TypeUnioned(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/owningRelatedElement")
                });
        this.addAnnotation(this.getUsage_DirectedUsage(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/usage"),
                        URI.createURI(eNS_URI).appendFragment("//Type/directedFeature")
                });
        this.addAnnotation(this.getUsage_NestedAction(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedOccurrence")
                });
        this.addAnnotation(this.getUsage_NestedAllocation(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedConnection")
                });
        this.addAnnotation(this.getUsage_NestedAnalysisCase(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedCase")
                });
        this.addAnnotation(this.getUsage_NestedAttribute(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedUsage")
                });
        this.addAnnotation(this.getUsage_NestedCalculation(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedAction")
                });
        this.addAnnotation(this.getUsage_NestedCase(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedCalculation")
                });
        this.addAnnotation(this.getUsage_NestedConcern(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedRequirement")
                });
        this.addAnnotation(this.getUsage_NestedConnection(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedPart")
                });
        this.addAnnotation(this.getUsage_NestedConstraint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedOccurrence")
                });
        this.addAnnotation(this.getUsage_NestedEnumeration(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedAttribute")
                });
        this.addAnnotation(this.getUsage_NestedFlow(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedConnection")
                });
        this.addAnnotation(this.getUsage_NestedInterface(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedConnection")
                });
        this.addAnnotation(this.getUsage_NestedItem(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedOccurrence")
                });
        this.addAnnotation(this.getUsage_NestedMetadata(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedItem")
                });
        this.addAnnotation(this.getUsage_NestedOccurrence(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedUsage")
                });
        this.addAnnotation(this.getUsage_NestedPart(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedItem")
                });
        this.addAnnotation(this.getUsage_NestedPort(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedUsage")
                });
        this.addAnnotation(this.getUsage_NestedReference(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedUsage")
                });
        this.addAnnotation(this.getUsage_NestedRendering(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedPart")
                });
        this.addAnnotation(this.getUsage_NestedRequirement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedConstraint")
                });
        this.addAnnotation(this.getUsage_NestedState(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedAction")
                });
        this.addAnnotation(this.getUsage_NestedTransition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedUsage")
                });
        this.addAnnotation(this.getUsage_NestedUsage(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedFeature"),
                        URI.createURI(eNS_URI).appendFragment("//Usage/usage")
                });
        this.addAnnotation(this.getUsage_NestedUseCase(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedCase")
                });
        this.addAnnotation(this.getUsage_NestedVerificationCase(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedCase")
                });
        this.addAnnotation(this.getUsage_NestedView(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedPart")
                });
        this.addAnnotation(this.getUsage_NestedViewpoint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedRequirement")
                });
        this.addAnnotation(this.getUsage_OwningDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Feature/owningType")
                });
        this.addAnnotation(this.getUsage_OwningUsage(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Feature/owningType")
                });
        this.addAnnotation(this.getUsage_Usage(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/feature")
                });
        this.addAnnotation(this.getUsage_Variant(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getUsage_VariantMembership(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMembership")
                });
        this.addAnnotation(this.getVerificationCaseUsage_VerificationCaseDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//CaseUsage/caseDefinition")
                });
        this.addAnnotation(this.getViewDefinition_SatisfiedViewpoint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/ownedRequirement")
                });
        this.addAnnotation(this.getViewDefinition_View(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/usage")
                });
        this.addAnnotation(this.getViewDefinition_ViewCondition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getViewUsage_ExposedElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/member")
                });
        this.addAnnotation(this.getViewUsage_SatisfiedViewpoint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/nestedRequirement")
                });
        this.addAnnotation(this.getViewUsage_ViewCondition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
    }

    /**
     * Initializes the annotations for <b>redefines</b>. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void createRedefinesAnnotations() {
        String source = "redefines";
        this.addAnnotation(this.getActionUsage_ActionDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Step/behavior"),
                        URI.createURI(eNS_URI).appendFragment("//OccurrenceUsage/occurrenceDefinition")
                });
        this.addAnnotation(this.getActorMembership_OwnedActorParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ParameterMembership/ownedMemberParameter")
                });
        this.addAnnotation(this.getAllocationUsage_AllocationDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ConnectionUsage/connectionDefinition")
                });
        this.addAnnotation(this.getAnalysisCaseUsage_AnalysisCaseDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//CaseUsage/caseDefinition")
                });
        this.addAnnotation(this.getAnnotation_AnnotatedElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getAnnotation_AnnotatingElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getAssociation_AssociationEnd(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/endFeature")
                });
        this.addAnnotation(this.getAssociation_RelatedType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/relatedElement")
                });
        this.addAnnotation(this.getAssociation_SourceType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getAssociation_TargetType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getAttributeUsage_AttributeDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/definition")
                });
        this.addAnnotation(this.getBehavior_Parameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/directedFeature")
                });
        this.addAnnotation(this.getBooleanExpression_Predicate(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Expression/function")
                });
        this.addAnnotation(this.getCalculationUsage_CalculationDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Expression/function"),
                        URI.createURI(eNS_URI).appendFragment("//ActionUsage/actionDefinition")
                });
        this.addAnnotation(this.getCaseUsage_CaseDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//CalculationUsage/calculationDefinition")
                });
        this.addAnnotation(this.getConcernUsage_ConcernDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//RequirementUsage/requirementDefinition")
                });
        this.addAnnotation(this.getConjugatedPortDefinition_OriginalPortDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/owningNamespace")
                });
        this.addAnnotation(this.getConjugatedPortDefinition_OwnedPortConjugator(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/ownedConjugator")
                });
        this.addAnnotation(this.getConjugatedPortTyping_ConjugatedPortDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//FeatureTyping/type")
                });
        this.addAnnotation(this.getConjugation_ConjugatedType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getConjugation_OriginalType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getConnectionDefinition_ConnectionEnd(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Association/associationEnd")
                });
        this.addAnnotation(this.getConnectionUsage_ConnectionDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Connector/association")
                });
        this.addAnnotation(this.getConnector_Association(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Feature/type")
                });
        this.addAnnotation(this.getConnector_ConnectorEnd(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/endFeature")
                });
        this.addAnnotation(this.getConnector_RelatedFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/relatedElement")
                });
        this.addAnnotation(this.getConnector_SourceFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getConnector_TargetFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getConstraintUsage_ConstraintDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//BooleanExpression/predicate")
                });
        this.addAnnotation(this.getDependency_Client(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getDependency_Supplier(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getDifferencing_DifferencingType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getDifferencing_TypeDifferenced(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getDisjoining_DisjoiningType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getDisjoining_TypeDisjoined(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getDocumentation_DocumentedElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//AnnotatingElement/annotatedElement")
                });
        this.addAnnotation(this.getElementFilterMembership_Condition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//OwningMembership/ownedMemberElement")
                });
        this.addAnnotation(this.getEnumerationDefinition_EnumeratedValue(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Definition/variant")
                });
        this.addAnnotation(this.getEnumerationUsage_EnumerationDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//AttributeUsage/attributeDefinition")
                });
        this.addAnnotation(this.getExhibitStateUsage_ExhibitedState(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//PerformActionUsage/performedAction")
                });
        this.addAnnotation(this.getExpression_Function(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Step/behavior")
                });
        this.addAnnotation(this.getFeatureChaining_ChainingFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getFeatureChaining_FeatureChained(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getFeatureInverting_FeatureInverted(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getFeatureInverting_InvertingFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getFeatureMembership_OwnedMemberFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//OwningMembership/ownedMemberElement"),
                        URI.createURI(eNS_URI).appendFragment("//Featuring/feature")
                });
        this.addAnnotation(this.getFeatureMembership_OwningType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Membership/membershipOwningNamespace"),
                        URI.createURI(eNS_URI).appendFragment("//Featuring/type")
                });
        this.addAnnotation(this.getFeatureTyping_OwningFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/owningType")
                });
        this.addAnnotation(this.getFeatureTyping_Type(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/general")
                });
        this.addAnnotation(this.getFeatureTyping_TypedFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/specific")
                });
        this.addAnnotation(this.getFeatureValue_Value(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//OwningMembership/ownedMemberElement")
                });
        this.addAnnotation(this.getFlowConnectionUsage_FlowConnectionDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ActionUsage/actionDefinition"),
                        URI.createURI(eNS_URI).appendFragment("//ConnectionUsage/connectionDefinition"),
                        URI.createURI(eNS_URI).appendFragment("//ItemFlow/interaction")
                });
        this.addAnnotation(this.getFramedConcernMembership_OwnedConcern(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//RequirementConstraintMembership/ownedConstraint")
                });
        this.addAnnotation(this.getFramedConcernMembership_ReferencedConcern(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//RequirementConstraintMembership/referencedConstraint")
                });
        this.addAnnotation(this.getImport_ImportOwningNamespace(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getIncludeUseCaseUsage_UseCaseIncluded(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//PerformActionUsage/performedAction")
                });
        this.addAnnotation(this.getInterfaceDefinition_InterfaceEnd(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ConnectionDefinition/connectionEnd")
                });
        this.addAnnotation(this.getInterfaceUsage_InterfaceDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ConnectionUsage/connectionDefinition")
                });
        this.addAnnotation(this.getIntersecting_IntersectingType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getIntersecting_TypeIntersected(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getItemFlow_Interaction(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Connector/association"),
                        URI.createURI(eNS_URI).appendFragment("//Step/behavior")
                });
        this.addAnnotation(this.getMembership_MemberElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getMembership_MembershipOwningNamespace(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getMembershipImport_ImportedMembership(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getMetadataUsage_MetadataDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ItemUsage/itemDefinition"),
                        URI.createURI(eNS_URI).appendFragment("//MetadataFeature/metaclass")
                });
        this.addAnnotation(this.getMultiplicityRange_Bound(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Namespace/ownedMember")
                });
        this.addAnnotation(this.getNamespaceImport_ImportedNamespace(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getObjectiveMembership_OwnedObjectiveRequirement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//FeatureMembership/ownedMemberFeature")
                });
        this.addAnnotation(this.getOccurrenceUsage_OccurrenceDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Usage/definition")
                });
        this.addAnnotation(this.getOwningMembership_OwnedMemberElementId(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Membership/memberElementId")
                });
        this.addAnnotation(this.getOwningMembership_OwnedMemberName(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Membership/memberName")
                });
        this.addAnnotation(this.getOwningMembership_OwnedMemberShortName(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Membership/memberShortName")
                });
        this.addAnnotation(this.getOwningMembership_OwnedMemberElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Membership/memberElement")
                });
        this.addAnnotation(this.getParameterMembership_OwnedMemberParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//FeatureMembership/ownedMemberFeature")
                });
        this.addAnnotation(this.getPerformActionUsage_PerformedAction(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//EventOccurrenceUsage/eventOccurrence")
                });
        this.addAnnotation(this.getPortConjugation_ConjugatedPortDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Conjugation/owningType")
                });
        this.addAnnotation(this.getPortConjugation_OriginalPortDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Conjugation/originalType")
                });
        this.addAnnotation(this.getPortUsage_PortDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//OccurrenceUsage/occurrenceDefinition")
                });
        this.addAnnotation(this.getRedefinition_RedefinedFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Subsetting/subsettedFeature")
                });
        this.addAnnotation(this.getRedefinition_RedefiningFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Subsetting/subsettingFeature")
                });
        this.addAnnotation(this.getReferenceSubsetting_ReferencedFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Subsetting/subsettedFeature")
                });
        this.addAnnotation(this.getReferenceSubsetting_ReferencingFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Subsetting/subsettingFeature"),
                        URI.createURI(eNS_URI).appendFragment("//Subsetting/owningFeature")
                });
        this.addAnnotation(this.getRenderingUsage_RenderingDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//PartUsage/partDefinition")
                });
        this.addAnnotation(this.getRequirementConstraintMembership_OwnedConstraint(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//FeatureMembership/ownedMemberFeature")
                });
        this.addAnnotation(this.getRequirementDefinition_ReqId(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/declaredShortName")
                });
        this.addAnnotation(this.getRequirementUsage_ReqId(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Element/declaredShortName")
                });
        this.addAnnotation(this.getRequirementUsage_RequirementDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ConstraintUsage/constraintDefinition")
                });
        this.addAnnotation(this.getRequirementVerificationMembership_OwnedRequirement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//RequirementConstraintMembership/ownedConstraint")
                });
        this.addAnnotation(this.getRequirementVerificationMembership_VerifiedRequirement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//RequirementConstraintMembership/referencedConstraint")
                });
        this.addAnnotation(this.getResultExpressionMembership_OwnedResultExpression(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//FeatureMembership/ownedMemberFeature")
                });
        this.addAnnotation(this.getSatisfyRequirementUsage_SatisfiedRequirement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//AssertConstraintUsage/assertedConstraint")
                });
        this.addAnnotation(this.getSpecialization_General(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getSpecialization_Specific(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getStakeholderMembership_OwnedStakeholderParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ParameterMembership/ownedMemberParameter")
                });
        this.addAnnotation(this.getStateSubactionMembership_Action(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//FeatureMembership/ownedMemberFeature")
                });
        this.addAnnotation(this.getStateUsage_StateDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ActionUsage/actionDefinition")
                });
        this.addAnnotation(this.getStep_Parameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Type/directedFeature")
                });
        this.addAnnotation(this.getSubclassification_OwningClassifier(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/owningType")
                });
        this.addAnnotation(this.getSubclassification_Subclassifier(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/specific")
                });
        this.addAnnotation(this.getSubclassification_Superclassifier(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/general")
                });
        this.addAnnotation(this.getSubjectMembership_OwnedSubjectParameter(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//ParameterMembership/ownedMemberParameter")
                });
        this.addAnnotation(this.getSubsetting_OwningFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/owningType")
                });
        this.addAnnotation(this.getSubsetting_SubsettedFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/general")
                });
        this.addAnnotation(this.getSubsetting_SubsettingFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Specialization/specific")
                });
        this.addAnnotation(this.getTextualRepresentation_RepresentedElement(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//AnnotatingElement/annotatedElement")
                });
        this.addAnnotation(this.getTransitionFeatureMembership_TransitionFeature(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//FeatureMembership/ownedMemberFeature")
                });
        this.addAnnotation(this.getTypeFeaturing_FeatureOfType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source"),
                        URI.createURI(eNS_URI).appendFragment("//Featuring/feature")
                });
        this.addAnnotation(this.getTypeFeaturing_FeaturingType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target"),
                        URI.createURI(eNS_URI).appendFragment("//Featuring/type")
                });
        this.addAnnotation(this.getUnioning_TypeUnioned(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/source")
                });
        this.addAnnotation(this.getUnioning_UnioningType(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Relationship/target")
                });
        this.addAnnotation(this.getUsage_Definition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//Feature/type")
                });
        this.addAnnotation(this.getUseCaseUsage_UseCaseDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//CaseUsage/caseDefinition")
                });
        this.addAnnotation(this.getVariantMembership_OwnedVariantUsage(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//OwningMembership/ownedMemberElement")
                });
        this.addAnnotation(this.getViewpointUsage_ViewpointDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//RequirementUsage/requirementDefinition")
                });
        this.addAnnotation(this.getViewRenderingMembership_OwnedRendering(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//FeatureMembership/ownedMemberFeature")
                });
        this.addAnnotation(this.getViewUsage_ViewDefinition(),
                source,
                new String[] {
                },
                new URI[] {
                        URI.createURI(eNS_URI).appendFragment("//PartUsage/partDefinition")
                });
    }

} // SysmlPackageImpl
