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
package org.eclipse.syson.sysml.util;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.syson.sysml.*;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the inheritance hierarchy until a non-null result is
 * returned, which is the result of the switch. <!-- end-user-doc -->
 *
 * @see org.eclipse.syson.sysml.SysmlPackage
 * @generated
 */
public class SysmlSwitch<T> extends Switch<T> {
    /**
     * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected static SysmlPackage modelPackage;

    /**
     * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public SysmlSwitch() {
        if (modelPackage == null) {
            modelPackage = SysmlPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param ePackage
     *            the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that
     * result. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case SysmlPackage.ACCEPT_ACTION_USAGE: {
                AcceptActionUsage acceptActionUsage = (AcceptActionUsage) theEObject;
                T result = this.caseAcceptActionUsage(acceptActionUsage);
                if (result == null)
                    result = this.caseActionUsage(acceptActionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(acceptActionUsage);
                if (result == null)
                    result = this.caseStep(acceptActionUsage);
                if (result == null)
                    result = this.caseUsage(acceptActionUsage);
                if (result == null)
                    result = this.caseFeature(acceptActionUsage);
                if (result == null)
                    result = this.caseType(acceptActionUsage);
                if (result == null)
                    result = this.caseNamespace(acceptActionUsage);
                if (result == null)
                    result = this.caseElement(acceptActionUsage);
                if (result == null)
                    result = this.caseEModelElement(acceptActionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ACTION_DEFINITION: {
                ActionDefinition actionDefinition = (ActionDefinition) theEObject;
                T result = this.caseActionDefinition(actionDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(actionDefinition);
                if (result == null)
                    result = this.caseBehavior(actionDefinition);
                if (result == null)
                    result = this.caseDefinition(actionDefinition);
                if (result == null)
                    result = this.caseClass(actionDefinition);
                if (result == null)
                    result = this.caseClassifier(actionDefinition);
                if (result == null)
                    result = this.caseType(actionDefinition);
                if (result == null)
                    result = this.caseNamespace(actionDefinition);
                if (result == null)
                    result = this.caseElement(actionDefinition);
                if (result == null)
                    result = this.caseEModelElement(actionDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ACTION_USAGE: {
                ActionUsage actionUsage = (ActionUsage) theEObject;
                T result = this.caseActionUsage(actionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(actionUsage);
                if (result == null)
                    result = this.caseStep(actionUsage);
                if (result == null)
                    result = this.caseUsage(actionUsage);
                if (result == null)
                    result = this.caseFeature(actionUsage);
                if (result == null)
                    result = this.caseType(actionUsage);
                if (result == null)
                    result = this.caseNamespace(actionUsage);
                if (result == null)
                    result = this.caseElement(actionUsage);
                if (result == null)
                    result = this.caseEModelElement(actionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ACTOR_MEMBERSHIP: {
                ActorMembership actorMembership = (ActorMembership) theEObject;
                T result = this.caseActorMembership(actorMembership);
                if (result == null)
                    result = this.caseParameterMembership(actorMembership);
                if (result == null)
                    result = this.caseFeatureMembership(actorMembership);
                if (result == null)
                    result = this.caseOwningMembership(actorMembership);
                if (result == null)
                    result = this.caseFeaturing(actorMembership);
                if (result == null)
                    result = this.caseMembership(actorMembership);
                if (result == null)
                    result = this.caseRelationship(actorMembership);
                if (result == null)
                    result = this.caseElement(actorMembership);
                if (result == null)
                    result = this.caseEModelElement(actorMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ALLOCATION_DEFINITION: {
                AllocationDefinition allocationDefinition = (AllocationDefinition) theEObject;
                T result = this.caseAllocationDefinition(allocationDefinition);
                if (result == null)
                    result = this.caseConnectionDefinition(allocationDefinition);
                if (result == null)
                    result = this.casePartDefinition(allocationDefinition);
                if (result == null)
                    result = this.caseAssociationStructure(allocationDefinition);
                if (result == null)
                    result = this.caseItemDefinition(allocationDefinition);
                if (result == null)
                    result = this.caseAssociation(allocationDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(allocationDefinition);
                if (result == null)
                    result = this.caseStructure(allocationDefinition);
                if (result == null)
                    result = this.caseRelationship(allocationDefinition);
                if (result == null)
                    result = this.caseDefinition(allocationDefinition);
                if (result == null)
                    result = this.caseClass(allocationDefinition);
                if (result == null)
                    result = this.caseClassifier(allocationDefinition);
                if (result == null)
                    result = this.caseType(allocationDefinition);
                if (result == null)
                    result = this.caseNamespace(allocationDefinition);
                if (result == null)
                    result = this.caseElement(allocationDefinition);
                if (result == null)
                    result = this.caseEModelElement(allocationDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ALLOCATION_USAGE: {
                AllocationUsage allocationUsage = (AllocationUsage) theEObject;
                T result = this.caseAllocationUsage(allocationUsage);
                if (result == null)
                    result = this.caseConnectionUsage(allocationUsage);
                if (result == null)
                    result = this.caseConnectorAsUsage(allocationUsage);
                if (result == null)
                    result = this.casePartUsage(allocationUsage);
                if (result == null)
                    result = this.caseConnector(allocationUsage);
                if (result == null)
                    result = this.caseItemUsage(allocationUsage);
                if (result == null)
                    result = this.caseFeature(allocationUsage);
                if (result == null)
                    result = this.caseRelationship(allocationUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(allocationUsage);
                if (result == null)
                    result = this.caseUsage(allocationUsage);
                if (result == null)
                    result = this.caseType(allocationUsage);
                if (result == null)
                    result = this.caseNamespace(allocationUsage);
                if (result == null)
                    result = this.caseElement(allocationUsage);
                if (result == null)
                    result = this.caseEModelElement(allocationUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ANALYSIS_CASE_DEFINITION: {
                AnalysisCaseDefinition analysisCaseDefinition = (AnalysisCaseDefinition) theEObject;
                T result = this.caseAnalysisCaseDefinition(analysisCaseDefinition);
                if (result == null)
                    result = this.caseCaseDefinition(analysisCaseDefinition);
                if (result == null)
                    result = this.caseCalculationDefinition(analysisCaseDefinition);
                if (result == null)
                    result = this.caseActionDefinition(analysisCaseDefinition);
                if (result == null)
                    result = this.caseFunction(analysisCaseDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(analysisCaseDefinition);
                if (result == null)
                    result = this.caseBehavior(analysisCaseDefinition);
                if (result == null)
                    result = this.caseDefinition(analysisCaseDefinition);
                if (result == null)
                    result = this.caseClass(analysisCaseDefinition);
                if (result == null)
                    result = this.caseClassifier(analysisCaseDefinition);
                if (result == null)
                    result = this.caseType(analysisCaseDefinition);
                if (result == null)
                    result = this.caseNamespace(analysisCaseDefinition);
                if (result == null)
                    result = this.caseElement(analysisCaseDefinition);
                if (result == null)
                    result = this.caseEModelElement(analysisCaseDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ANALYSIS_CASE_USAGE: {
                AnalysisCaseUsage analysisCaseUsage = (AnalysisCaseUsage) theEObject;
                T result = this.caseAnalysisCaseUsage(analysisCaseUsage);
                if (result == null)
                    result = this.caseCaseUsage(analysisCaseUsage);
                if (result == null)
                    result = this.caseCalculationUsage(analysisCaseUsage);
                if (result == null)
                    result = this.caseActionUsage(analysisCaseUsage);
                if (result == null)
                    result = this.caseExpression(analysisCaseUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(analysisCaseUsage);
                if (result == null)
                    result = this.caseStep(analysisCaseUsage);
                if (result == null)
                    result = this.caseUsage(analysisCaseUsage);
                if (result == null)
                    result = this.caseFeature(analysisCaseUsage);
                if (result == null)
                    result = this.caseType(analysisCaseUsage);
                if (result == null)
                    result = this.caseNamespace(analysisCaseUsage);
                if (result == null)
                    result = this.caseElement(analysisCaseUsage);
                if (result == null)
                    result = this.caseEModelElement(analysisCaseUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ANNOTATING_ELEMENT: {
                AnnotatingElement annotatingElement = (AnnotatingElement) theEObject;
                T result = this.caseAnnotatingElement(annotatingElement);
                if (result == null)
                    result = this.caseElement(annotatingElement);
                if (result == null)
                    result = this.caseEModelElement(annotatingElement);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ANNOTATION: {
                Annotation annotation = (Annotation) theEObject;
                T result = this.caseAnnotation(annotation);
                if (result == null)
                    result = this.caseRelationship(annotation);
                if (result == null)
                    result = this.caseElement(annotation);
                if (result == null)
                    result = this.caseEModelElement(annotation);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ASSERT_CONSTRAINT_USAGE: {
                AssertConstraintUsage assertConstraintUsage = (AssertConstraintUsage) theEObject;
                T result = this.caseAssertConstraintUsage(assertConstraintUsage);
                if (result == null)
                    result = this.caseConstraintUsage(assertConstraintUsage);
                if (result == null)
                    result = this.caseInvariant(assertConstraintUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(assertConstraintUsage);
                if (result == null)
                    result = this.caseBooleanExpression(assertConstraintUsage);
                if (result == null)
                    result = this.caseUsage(assertConstraintUsage);
                if (result == null)
                    result = this.caseExpression(assertConstraintUsage);
                if (result == null)
                    result = this.caseStep(assertConstraintUsage);
                if (result == null)
                    result = this.caseFeature(assertConstraintUsage);
                if (result == null)
                    result = this.caseType(assertConstraintUsage);
                if (result == null)
                    result = this.caseNamespace(assertConstraintUsage);
                if (result == null)
                    result = this.caseElement(assertConstraintUsage);
                if (result == null)
                    result = this.caseEModelElement(assertConstraintUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ASSIGNMENT_ACTION_USAGE: {
                AssignmentActionUsage assignmentActionUsage = (AssignmentActionUsage) theEObject;
                T result = this.caseAssignmentActionUsage(assignmentActionUsage);
                if (result == null)
                    result = this.caseActionUsage(assignmentActionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(assignmentActionUsage);
                if (result == null)
                    result = this.caseStep(assignmentActionUsage);
                if (result == null)
                    result = this.caseUsage(assignmentActionUsage);
                if (result == null)
                    result = this.caseFeature(assignmentActionUsage);
                if (result == null)
                    result = this.caseType(assignmentActionUsage);
                if (result == null)
                    result = this.caseNamespace(assignmentActionUsage);
                if (result == null)
                    result = this.caseElement(assignmentActionUsage);
                if (result == null)
                    result = this.caseEModelElement(assignmentActionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ASSOCIATION: {
                Association association = (Association) theEObject;
                T result = this.caseAssociation(association);
                if (result == null)
                    result = this.caseClassifier(association);
                if (result == null)
                    result = this.caseRelationship(association);
                if (result == null)
                    result = this.caseType(association);
                if (result == null)
                    result = this.caseNamespace(association);
                if (result == null)
                    result = this.caseElement(association);
                if (result == null)
                    result = this.caseEModelElement(association);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ASSOCIATION_STRUCTURE: {
                AssociationStructure associationStructure = (AssociationStructure) theEObject;
                T result = this.caseAssociationStructure(associationStructure);
                if (result == null)
                    result = this.caseAssociation(associationStructure);
                if (result == null)
                    result = this.caseStructure(associationStructure);
                if (result == null)
                    result = this.caseRelationship(associationStructure);
                if (result == null)
                    result = this.caseClass(associationStructure);
                if (result == null)
                    result = this.caseClassifier(associationStructure);
                if (result == null)
                    result = this.caseType(associationStructure);
                if (result == null)
                    result = this.caseNamespace(associationStructure);
                if (result == null)
                    result = this.caseElement(associationStructure);
                if (result == null)
                    result = this.caseEModelElement(associationStructure);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ATTRIBUTE_DEFINITION: {
                AttributeDefinition attributeDefinition = (AttributeDefinition) theEObject;
                T result = this.caseAttributeDefinition(attributeDefinition);
                if (result == null)
                    result = this.caseDefinition(attributeDefinition);
                if (result == null)
                    result = this.caseDataType(attributeDefinition);
                if (result == null)
                    result = this.caseClassifier(attributeDefinition);
                if (result == null)
                    result = this.caseType(attributeDefinition);
                if (result == null)
                    result = this.caseNamespace(attributeDefinition);
                if (result == null)
                    result = this.caseElement(attributeDefinition);
                if (result == null)
                    result = this.caseEModelElement(attributeDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ATTRIBUTE_USAGE: {
                AttributeUsage attributeUsage = (AttributeUsage) theEObject;
                T result = this.caseAttributeUsage(attributeUsage);
                if (result == null)
                    result = this.caseUsage(attributeUsage);
                if (result == null)
                    result = this.caseFeature(attributeUsage);
                if (result == null)
                    result = this.caseType(attributeUsage);
                if (result == null)
                    result = this.caseNamespace(attributeUsage);
                if (result == null)
                    result = this.caseElement(attributeUsage);
                if (result == null)
                    result = this.caseEModelElement(attributeUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.BEHAVIOR: {
                Behavior behavior = (Behavior) theEObject;
                T result = this.caseBehavior(behavior);
                if (result == null)
                    result = this.caseClass(behavior);
                if (result == null)
                    result = this.caseClassifier(behavior);
                if (result == null)
                    result = this.caseType(behavior);
                if (result == null)
                    result = this.caseNamespace(behavior);
                if (result == null)
                    result = this.caseElement(behavior);
                if (result == null)
                    result = this.caseEModelElement(behavior);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.BINDING_CONNECTOR: {
                BindingConnector bindingConnector = (BindingConnector) theEObject;
                T result = this.caseBindingConnector(bindingConnector);
                if (result == null)
                    result = this.caseConnector(bindingConnector);
                if (result == null)
                    result = this.caseFeature(bindingConnector);
                if (result == null)
                    result = this.caseRelationship(bindingConnector);
                if (result == null)
                    result = this.caseType(bindingConnector);
                if (result == null)
                    result = this.caseNamespace(bindingConnector);
                if (result == null)
                    result = this.caseElement(bindingConnector);
                if (result == null)
                    result = this.caseEModelElement(bindingConnector);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.BINDING_CONNECTOR_AS_USAGE: {
                BindingConnectorAsUsage bindingConnectorAsUsage = (BindingConnectorAsUsage) theEObject;
                T result = this.caseBindingConnectorAsUsage(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseConnectorAsUsage(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseBindingConnector(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseUsage(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseConnector(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseFeature(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseRelationship(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseType(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseNamespace(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseElement(bindingConnectorAsUsage);
                if (result == null)
                    result = this.caseEModelElement(bindingConnectorAsUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.BOOLEAN_EXPRESSION: {
                BooleanExpression booleanExpression = (BooleanExpression) theEObject;
                T result = this.caseBooleanExpression(booleanExpression);
                if (result == null)
                    result = this.caseExpression(booleanExpression);
                if (result == null)
                    result = this.caseStep(booleanExpression);
                if (result == null)
                    result = this.caseFeature(booleanExpression);
                if (result == null)
                    result = this.caseType(booleanExpression);
                if (result == null)
                    result = this.caseNamespace(booleanExpression);
                if (result == null)
                    result = this.caseElement(booleanExpression);
                if (result == null)
                    result = this.caseEModelElement(booleanExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CALCULATION_DEFINITION: {
                CalculationDefinition calculationDefinition = (CalculationDefinition) theEObject;
                T result = this.caseCalculationDefinition(calculationDefinition);
                if (result == null)
                    result = this.caseActionDefinition(calculationDefinition);
                if (result == null)
                    result = this.caseFunction(calculationDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(calculationDefinition);
                if (result == null)
                    result = this.caseBehavior(calculationDefinition);
                if (result == null)
                    result = this.caseDefinition(calculationDefinition);
                if (result == null)
                    result = this.caseClass(calculationDefinition);
                if (result == null)
                    result = this.caseClassifier(calculationDefinition);
                if (result == null)
                    result = this.caseType(calculationDefinition);
                if (result == null)
                    result = this.caseNamespace(calculationDefinition);
                if (result == null)
                    result = this.caseElement(calculationDefinition);
                if (result == null)
                    result = this.caseEModelElement(calculationDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CALCULATION_USAGE: {
                CalculationUsage calculationUsage = (CalculationUsage) theEObject;
                T result = this.caseCalculationUsage(calculationUsage);
                if (result == null)
                    result = this.caseActionUsage(calculationUsage);
                if (result == null)
                    result = this.caseExpression(calculationUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(calculationUsage);
                if (result == null)
                    result = this.caseStep(calculationUsage);
                if (result == null)
                    result = this.caseUsage(calculationUsage);
                if (result == null)
                    result = this.caseFeature(calculationUsage);
                if (result == null)
                    result = this.caseType(calculationUsage);
                if (result == null)
                    result = this.caseNamespace(calculationUsage);
                if (result == null)
                    result = this.caseElement(calculationUsage);
                if (result == null)
                    result = this.caseEModelElement(calculationUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CASE_DEFINITION: {
                CaseDefinition caseDefinition = (CaseDefinition) theEObject;
                T result = this.caseCaseDefinition(caseDefinition);
                if (result == null)
                    result = this.caseCalculationDefinition(caseDefinition);
                if (result == null)
                    result = this.caseActionDefinition(caseDefinition);
                if (result == null)
                    result = this.caseFunction(caseDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(caseDefinition);
                if (result == null)
                    result = this.caseBehavior(caseDefinition);
                if (result == null)
                    result = this.caseDefinition(caseDefinition);
                if (result == null)
                    result = this.caseClass(caseDefinition);
                if (result == null)
                    result = this.caseClassifier(caseDefinition);
                if (result == null)
                    result = this.caseType(caseDefinition);
                if (result == null)
                    result = this.caseNamespace(caseDefinition);
                if (result == null)
                    result = this.caseElement(caseDefinition);
                if (result == null)
                    result = this.caseEModelElement(caseDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CASE_USAGE: {
                CaseUsage caseUsage = (CaseUsage) theEObject;
                T result = this.caseCaseUsage(caseUsage);
                if (result == null)
                    result = this.caseCalculationUsage(caseUsage);
                if (result == null)
                    result = this.caseActionUsage(caseUsage);
                if (result == null)
                    result = this.caseExpression(caseUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(caseUsage);
                if (result == null)
                    result = this.caseStep(caseUsage);
                if (result == null)
                    result = this.caseUsage(caseUsage);
                if (result == null)
                    result = this.caseFeature(caseUsage);
                if (result == null)
                    result = this.caseType(caseUsage);
                if (result == null)
                    result = this.caseNamespace(caseUsage);
                if (result == null)
                    result = this.caseElement(caseUsage);
                if (result == null)
                    result = this.caseEModelElement(caseUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CLASS: {
                org.eclipse.syson.sysml.Class class_ = (org.eclipse.syson.sysml.Class) theEObject;
                T result = this.caseClass(class_);
                if (result == null)
                    result = this.caseClassifier(class_);
                if (result == null)
                    result = this.caseType(class_);
                if (result == null)
                    result = this.caseNamespace(class_);
                if (result == null)
                    result = this.caseElement(class_);
                if (result == null)
                    result = this.caseEModelElement(class_);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CLASSIFIER: {
                Classifier classifier = (Classifier) theEObject;
                T result = this.caseClassifier(classifier);
                if (result == null)
                    result = this.caseType(classifier);
                if (result == null)
                    result = this.caseNamespace(classifier);
                if (result == null)
                    result = this.caseElement(classifier);
                if (result == null)
                    result = this.caseEModelElement(classifier);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.COLLECT_EXPRESSION: {
                CollectExpression collectExpression = (CollectExpression) theEObject;
                T result = this.caseCollectExpression(collectExpression);
                if (result == null)
                    result = this.caseOperatorExpression(collectExpression);
                if (result == null)
                    result = this.caseInvocationExpression(collectExpression);
                if (result == null)
                    result = this.caseExpression(collectExpression);
                if (result == null)
                    result = this.caseStep(collectExpression);
                if (result == null)
                    result = this.caseFeature(collectExpression);
                if (result == null)
                    result = this.caseType(collectExpression);
                if (result == null)
                    result = this.caseNamespace(collectExpression);
                if (result == null)
                    result = this.caseElement(collectExpression);
                if (result == null)
                    result = this.caseEModelElement(collectExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.COMMENT: {
                Comment comment = (Comment) theEObject;
                T result = this.caseComment(comment);
                if (result == null)
                    result = this.caseAnnotatingElement(comment);
                if (result == null)
                    result = this.caseElement(comment);
                if (result == null)
                    result = this.caseEModelElement(comment);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONCERN_DEFINITION: {
                ConcernDefinition concernDefinition = (ConcernDefinition) theEObject;
                T result = this.caseConcernDefinition(concernDefinition);
                if (result == null)
                    result = this.caseRequirementDefinition(concernDefinition);
                if (result == null)
                    result = this.caseConstraintDefinition(concernDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(concernDefinition);
                if (result == null)
                    result = this.casePredicate(concernDefinition);
                if (result == null)
                    result = this.caseDefinition(concernDefinition);
                if (result == null)
                    result = this.caseFunction(concernDefinition);
                if (result == null)
                    result = this.caseClassifier(concernDefinition);
                if (result == null)
                    result = this.caseBehavior(concernDefinition);
                if (result == null)
                    result = this.caseType(concernDefinition);
                if (result == null)
                    result = this.caseClass(concernDefinition);
                if (result == null)
                    result = this.caseNamespace(concernDefinition);
                if (result == null)
                    result = this.caseElement(concernDefinition);
                if (result == null)
                    result = this.caseEModelElement(concernDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONCERN_USAGE: {
                ConcernUsage concernUsage = (ConcernUsage) theEObject;
                T result = this.caseConcernUsage(concernUsage);
                if (result == null)
                    result = this.caseRequirementUsage(concernUsage);
                if (result == null)
                    result = this.caseConstraintUsage(concernUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(concernUsage);
                if (result == null)
                    result = this.caseBooleanExpression(concernUsage);
                if (result == null)
                    result = this.caseUsage(concernUsage);
                if (result == null)
                    result = this.caseExpression(concernUsage);
                if (result == null)
                    result = this.caseStep(concernUsage);
                if (result == null)
                    result = this.caseFeature(concernUsage);
                if (result == null)
                    result = this.caseType(concernUsage);
                if (result == null)
                    result = this.caseNamespace(concernUsage);
                if (result == null)
                    result = this.caseElement(concernUsage);
                if (result == null)
                    result = this.caseEModelElement(concernUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONJUGATED_PORT_DEFINITION: {
                ConjugatedPortDefinition conjugatedPortDefinition = (ConjugatedPortDefinition) theEObject;
                T result = this.caseConjugatedPortDefinition(conjugatedPortDefinition);
                if (result == null)
                    result = this.casePortDefinition(conjugatedPortDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(conjugatedPortDefinition);
                if (result == null)
                    result = this.caseStructure(conjugatedPortDefinition);
                if (result == null)
                    result = this.caseDefinition(conjugatedPortDefinition);
                if (result == null)
                    result = this.caseClass(conjugatedPortDefinition);
                if (result == null)
                    result = this.caseClassifier(conjugatedPortDefinition);
                if (result == null)
                    result = this.caseType(conjugatedPortDefinition);
                if (result == null)
                    result = this.caseNamespace(conjugatedPortDefinition);
                if (result == null)
                    result = this.caseElement(conjugatedPortDefinition);
                if (result == null)
                    result = this.caseEModelElement(conjugatedPortDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONJUGATED_PORT_TYPING: {
                ConjugatedPortTyping conjugatedPortTyping = (ConjugatedPortTyping) theEObject;
                T result = this.caseConjugatedPortTyping(conjugatedPortTyping);
                if (result == null)
                    result = this.caseFeatureTyping(conjugatedPortTyping);
                if (result == null)
                    result = this.caseSpecialization(conjugatedPortTyping);
                if (result == null)
                    result = this.caseRelationship(conjugatedPortTyping);
                if (result == null)
                    result = this.caseElement(conjugatedPortTyping);
                if (result == null)
                    result = this.caseEModelElement(conjugatedPortTyping);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONJUGATION: {
                Conjugation conjugation = (Conjugation) theEObject;
                T result = this.caseConjugation(conjugation);
                if (result == null)
                    result = this.caseRelationship(conjugation);
                if (result == null)
                    result = this.caseElement(conjugation);
                if (result == null)
                    result = this.caseEModelElement(conjugation);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONNECTION_DEFINITION: {
                ConnectionDefinition connectionDefinition = (ConnectionDefinition) theEObject;
                T result = this.caseConnectionDefinition(connectionDefinition);
                if (result == null)
                    result = this.casePartDefinition(connectionDefinition);
                if (result == null)
                    result = this.caseAssociationStructure(connectionDefinition);
                if (result == null)
                    result = this.caseItemDefinition(connectionDefinition);
                if (result == null)
                    result = this.caseAssociation(connectionDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(connectionDefinition);
                if (result == null)
                    result = this.caseStructure(connectionDefinition);
                if (result == null)
                    result = this.caseRelationship(connectionDefinition);
                if (result == null)
                    result = this.caseDefinition(connectionDefinition);
                if (result == null)
                    result = this.caseClass(connectionDefinition);
                if (result == null)
                    result = this.caseClassifier(connectionDefinition);
                if (result == null)
                    result = this.caseType(connectionDefinition);
                if (result == null)
                    result = this.caseNamespace(connectionDefinition);
                if (result == null)
                    result = this.caseElement(connectionDefinition);
                if (result == null)
                    result = this.caseEModelElement(connectionDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONNECTION_USAGE: {
                ConnectionUsage connectionUsage = (ConnectionUsage) theEObject;
                T result = this.caseConnectionUsage(connectionUsage);
                if (result == null)
                    result = this.caseConnectorAsUsage(connectionUsage);
                if (result == null)
                    result = this.casePartUsage(connectionUsage);
                if (result == null)
                    result = this.caseConnector(connectionUsage);
                if (result == null)
                    result = this.caseItemUsage(connectionUsage);
                if (result == null)
                    result = this.caseFeature(connectionUsage);
                if (result == null)
                    result = this.caseRelationship(connectionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(connectionUsage);
                if (result == null)
                    result = this.caseUsage(connectionUsage);
                if (result == null)
                    result = this.caseType(connectionUsage);
                if (result == null)
                    result = this.caseNamespace(connectionUsage);
                if (result == null)
                    result = this.caseElement(connectionUsage);
                if (result == null)
                    result = this.caseEModelElement(connectionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONNECTOR: {
                Connector connector = (Connector) theEObject;
                T result = this.caseConnector(connector);
                if (result == null)
                    result = this.caseFeature(connector);
                if (result == null)
                    result = this.caseRelationship(connector);
                if (result == null)
                    result = this.caseType(connector);
                if (result == null)
                    result = this.caseNamespace(connector);
                if (result == null)
                    result = this.caseElement(connector);
                if (result == null)
                    result = this.caseEModelElement(connector);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONNECTOR_AS_USAGE: {
                ConnectorAsUsage connectorAsUsage = (ConnectorAsUsage) theEObject;
                T result = this.caseConnectorAsUsage(connectorAsUsage);
                if (result == null)
                    result = this.caseUsage(connectorAsUsage);
                if (result == null)
                    result = this.caseConnector(connectorAsUsage);
                if (result == null)
                    result = this.caseFeature(connectorAsUsage);
                if (result == null)
                    result = this.caseRelationship(connectorAsUsage);
                if (result == null)
                    result = this.caseType(connectorAsUsage);
                if (result == null)
                    result = this.caseNamespace(connectorAsUsage);
                if (result == null)
                    result = this.caseElement(connectorAsUsage);
                if (result == null)
                    result = this.caseEModelElement(connectorAsUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONSTRAINT_DEFINITION: {
                ConstraintDefinition constraintDefinition = (ConstraintDefinition) theEObject;
                T result = this.caseConstraintDefinition(constraintDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(constraintDefinition);
                if (result == null)
                    result = this.casePredicate(constraintDefinition);
                if (result == null)
                    result = this.caseDefinition(constraintDefinition);
                if (result == null)
                    result = this.caseFunction(constraintDefinition);
                if (result == null)
                    result = this.caseClassifier(constraintDefinition);
                if (result == null)
                    result = this.caseBehavior(constraintDefinition);
                if (result == null)
                    result = this.caseType(constraintDefinition);
                if (result == null)
                    result = this.caseClass(constraintDefinition);
                if (result == null)
                    result = this.caseNamespace(constraintDefinition);
                if (result == null)
                    result = this.caseElement(constraintDefinition);
                if (result == null)
                    result = this.caseEModelElement(constraintDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONSTRAINT_USAGE: {
                ConstraintUsage constraintUsage = (ConstraintUsage) theEObject;
                T result = this.caseConstraintUsage(constraintUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(constraintUsage);
                if (result == null)
                    result = this.caseBooleanExpression(constraintUsage);
                if (result == null)
                    result = this.caseUsage(constraintUsage);
                if (result == null)
                    result = this.caseExpression(constraintUsage);
                if (result == null)
                    result = this.caseStep(constraintUsage);
                if (result == null)
                    result = this.caseFeature(constraintUsage);
                if (result == null)
                    result = this.caseType(constraintUsage);
                if (result == null)
                    result = this.caseNamespace(constraintUsage);
                if (result == null)
                    result = this.caseElement(constraintUsage);
                if (result == null)
                    result = this.caseEModelElement(constraintUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONTROL_NODE: {
                ControlNode controlNode = (ControlNode) theEObject;
                T result = this.caseControlNode(controlNode);
                if (result == null)
                    result = this.caseActionUsage(controlNode);
                if (result == null)
                    result = this.caseOccurrenceUsage(controlNode);
                if (result == null)
                    result = this.caseStep(controlNode);
                if (result == null)
                    result = this.caseUsage(controlNode);
                if (result == null)
                    result = this.caseFeature(controlNode);
                if (result == null)
                    result = this.caseType(controlNode);
                if (result == null)
                    result = this.caseNamespace(controlNode);
                if (result == null)
                    result = this.caseElement(controlNode);
                if (result == null)
                    result = this.caseEModelElement(controlNode);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DATA_TYPE: {
                DataType dataType = (DataType) theEObject;
                T result = this.caseDataType(dataType);
                if (result == null)
                    result = this.caseClassifier(dataType);
                if (result == null)
                    result = this.caseType(dataType);
                if (result == null)
                    result = this.caseNamespace(dataType);
                if (result == null)
                    result = this.caseElement(dataType);
                if (result == null)
                    result = this.caseEModelElement(dataType);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DECISION_NODE: {
                DecisionNode decisionNode = (DecisionNode) theEObject;
                T result = this.caseDecisionNode(decisionNode);
                if (result == null)
                    result = this.caseControlNode(decisionNode);
                if (result == null)
                    result = this.caseActionUsage(decisionNode);
                if (result == null)
                    result = this.caseOccurrenceUsage(decisionNode);
                if (result == null)
                    result = this.caseStep(decisionNode);
                if (result == null)
                    result = this.caseUsage(decisionNode);
                if (result == null)
                    result = this.caseFeature(decisionNode);
                if (result == null)
                    result = this.caseType(decisionNode);
                if (result == null)
                    result = this.caseNamespace(decisionNode);
                if (result == null)
                    result = this.caseElement(decisionNode);
                if (result == null)
                    result = this.caseEModelElement(decisionNode);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DEFINITION: {
                Definition definition = (Definition) theEObject;
                T result = this.caseDefinition(definition);
                if (result == null)
                    result = this.caseClassifier(definition);
                if (result == null)
                    result = this.caseType(definition);
                if (result == null)
                    result = this.caseNamespace(definition);
                if (result == null)
                    result = this.caseElement(definition);
                if (result == null)
                    result = this.caseEModelElement(definition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DEPENDENCY: {
                Dependency dependency = (Dependency) theEObject;
                T result = this.caseDependency(dependency);
                if (result == null)
                    result = this.caseRelationship(dependency);
                if (result == null)
                    result = this.caseElement(dependency);
                if (result == null)
                    result = this.caseEModelElement(dependency);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DIFFERENCING: {
                Differencing differencing = (Differencing) theEObject;
                T result = this.caseDifferencing(differencing);
                if (result == null)
                    result = this.caseRelationship(differencing);
                if (result == null)
                    result = this.caseElement(differencing);
                if (result == null)
                    result = this.caseEModelElement(differencing);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DISJOINING: {
                Disjoining disjoining = (Disjoining) theEObject;
                T result = this.caseDisjoining(disjoining);
                if (result == null)
                    result = this.caseRelationship(disjoining);
                if (result == null)
                    result = this.caseElement(disjoining);
                if (result == null)
                    result = this.caseEModelElement(disjoining);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DOCUMENTATION: {
                Documentation documentation = (Documentation) theEObject;
                T result = this.caseDocumentation(documentation);
                if (result == null)
                    result = this.caseComment(documentation);
                if (result == null)
                    result = this.caseAnnotatingElement(documentation);
                if (result == null)
                    result = this.caseElement(documentation);
                if (result == null)
                    result = this.caseEModelElement(documentation);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ELEMENT: {
                Element element = (Element) theEObject;
                T result = this.caseElement(element);
                if (result == null)
                    result = this.caseEModelElement(element);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ELEMENT_FILTER_MEMBERSHIP: {
                ElementFilterMembership elementFilterMembership = (ElementFilterMembership) theEObject;
                T result = this.caseElementFilterMembership(elementFilterMembership);
                if (result == null)
                    result = this.caseOwningMembership(elementFilterMembership);
                if (result == null)
                    result = this.caseMembership(elementFilterMembership);
                if (result == null)
                    result = this.caseRelationship(elementFilterMembership);
                if (result == null)
                    result = this.caseElement(elementFilterMembership);
                if (result == null)
                    result = this.caseEModelElement(elementFilterMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.END_FEATURE_MEMBERSHIP: {
                EndFeatureMembership endFeatureMembership = (EndFeatureMembership) theEObject;
                T result = this.caseEndFeatureMembership(endFeatureMembership);
                if (result == null)
                    result = this.caseFeatureMembership(endFeatureMembership);
                if (result == null)
                    result = this.caseOwningMembership(endFeatureMembership);
                if (result == null)
                    result = this.caseFeaturing(endFeatureMembership);
                if (result == null)
                    result = this.caseMembership(endFeatureMembership);
                if (result == null)
                    result = this.caseRelationship(endFeatureMembership);
                if (result == null)
                    result = this.caseElement(endFeatureMembership);
                if (result == null)
                    result = this.caseEModelElement(endFeatureMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ENUMERATION_DEFINITION: {
                EnumerationDefinition enumerationDefinition = (EnumerationDefinition) theEObject;
                T result = this.caseEnumerationDefinition(enumerationDefinition);
                if (result == null)
                    result = this.caseAttributeDefinition(enumerationDefinition);
                if (result == null)
                    result = this.caseDefinition(enumerationDefinition);
                if (result == null)
                    result = this.caseDataType(enumerationDefinition);
                if (result == null)
                    result = this.caseClassifier(enumerationDefinition);
                if (result == null)
                    result = this.caseType(enumerationDefinition);
                if (result == null)
                    result = this.caseNamespace(enumerationDefinition);
                if (result == null)
                    result = this.caseElement(enumerationDefinition);
                if (result == null)
                    result = this.caseEModelElement(enumerationDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ENUMERATION_USAGE: {
                EnumerationUsage enumerationUsage = (EnumerationUsage) theEObject;
                T result = this.caseEnumerationUsage(enumerationUsage);
                if (result == null)
                    result = this.caseAttributeUsage(enumerationUsage);
                if (result == null)
                    result = this.caseUsage(enumerationUsage);
                if (result == null)
                    result = this.caseFeature(enumerationUsage);
                if (result == null)
                    result = this.caseType(enumerationUsage);
                if (result == null)
                    result = this.caseNamespace(enumerationUsage);
                if (result == null)
                    result = this.caseElement(enumerationUsage);
                if (result == null)
                    result = this.caseEModelElement(enumerationUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.EVENT_OCCURRENCE_USAGE: {
                EventOccurrenceUsage eventOccurrenceUsage = (EventOccurrenceUsage) theEObject;
                T result = this.caseEventOccurrenceUsage(eventOccurrenceUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(eventOccurrenceUsage);
                if (result == null)
                    result = this.caseUsage(eventOccurrenceUsage);
                if (result == null)
                    result = this.caseFeature(eventOccurrenceUsage);
                if (result == null)
                    result = this.caseType(eventOccurrenceUsage);
                if (result == null)
                    result = this.caseNamespace(eventOccurrenceUsage);
                if (result == null)
                    result = this.caseElement(eventOccurrenceUsage);
                if (result == null)
                    result = this.caseEModelElement(eventOccurrenceUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.EXHIBIT_STATE_USAGE: {
                ExhibitStateUsage exhibitStateUsage = (ExhibitStateUsage) theEObject;
                T result = this.caseExhibitStateUsage(exhibitStateUsage);
                if (result == null)
                    result = this.caseStateUsage(exhibitStateUsage);
                if (result == null)
                    result = this.casePerformActionUsage(exhibitStateUsage);
                if (result == null)
                    result = this.caseActionUsage(exhibitStateUsage);
                if (result == null)
                    result = this.caseEventOccurrenceUsage(exhibitStateUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(exhibitStateUsage);
                if (result == null)
                    result = this.caseStep(exhibitStateUsage);
                if (result == null)
                    result = this.caseUsage(exhibitStateUsage);
                if (result == null)
                    result = this.caseFeature(exhibitStateUsage);
                if (result == null)
                    result = this.caseType(exhibitStateUsage);
                if (result == null)
                    result = this.caseNamespace(exhibitStateUsage);
                if (result == null)
                    result = this.caseElement(exhibitStateUsage);
                if (result == null)
                    result = this.caseEModelElement(exhibitStateUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.EXPOSE: {
                Expose expose = (Expose) theEObject;
                T result = this.caseExpose(expose);
                if (result == null)
                    result = this.caseImport(expose);
                if (result == null)
                    result = this.caseRelationship(expose);
                if (result == null)
                    result = this.caseElement(expose);
                if (result == null)
                    result = this.caseEModelElement(expose);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.EXPRESSION: {
                Expression expression = (Expression) theEObject;
                T result = this.caseExpression(expression);
                if (result == null)
                    result = this.caseStep(expression);
                if (result == null)
                    result = this.caseFeature(expression);
                if (result == null)
                    result = this.caseType(expression);
                if (result == null)
                    result = this.caseNamespace(expression);
                if (result == null)
                    result = this.caseElement(expression);
                if (result == null)
                    result = this.caseEModelElement(expression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE: {
                Feature feature = (Feature) theEObject;
                T result = this.caseFeature(feature);
                if (result == null)
                    result = this.caseType(feature);
                if (result == null)
                    result = this.caseNamespace(feature);
                if (result == null)
                    result = this.caseElement(feature);
                if (result == null)
                    result = this.caseEModelElement(feature);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_CHAIN_EXPRESSION: {
                FeatureChainExpression featureChainExpression = (FeatureChainExpression) theEObject;
                T result = this.caseFeatureChainExpression(featureChainExpression);
                if (result == null)
                    result = this.caseOperatorExpression(featureChainExpression);
                if (result == null)
                    result = this.caseInvocationExpression(featureChainExpression);
                if (result == null)
                    result = this.caseExpression(featureChainExpression);
                if (result == null)
                    result = this.caseStep(featureChainExpression);
                if (result == null)
                    result = this.caseFeature(featureChainExpression);
                if (result == null)
                    result = this.caseType(featureChainExpression);
                if (result == null)
                    result = this.caseNamespace(featureChainExpression);
                if (result == null)
                    result = this.caseElement(featureChainExpression);
                if (result == null)
                    result = this.caseEModelElement(featureChainExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_CHAINING: {
                FeatureChaining featureChaining = (FeatureChaining) theEObject;
                T result = this.caseFeatureChaining(featureChaining);
                if (result == null)
                    result = this.caseRelationship(featureChaining);
                if (result == null)
                    result = this.caseElement(featureChaining);
                if (result == null)
                    result = this.caseEModelElement(featureChaining);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_INVERTING: {
                FeatureInverting featureInverting = (FeatureInverting) theEObject;
                T result = this.caseFeatureInverting(featureInverting);
                if (result == null)
                    result = this.caseRelationship(featureInverting);
                if (result == null)
                    result = this.caseElement(featureInverting);
                if (result == null)
                    result = this.caseEModelElement(featureInverting);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_MEMBERSHIP: {
                FeatureMembership featureMembership = (FeatureMembership) theEObject;
                T result = this.caseFeatureMembership(featureMembership);
                if (result == null)
                    result = this.caseOwningMembership(featureMembership);
                if (result == null)
                    result = this.caseFeaturing(featureMembership);
                if (result == null)
                    result = this.caseMembership(featureMembership);
                if (result == null)
                    result = this.caseRelationship(featureMembership);
                if (result == null)
                    result = this.caseElement(featureMembership);
                if (result == null)
                    result = this.caseEModelElement(featureMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_REFERENCE_EXPRESSION: {
                FeatureReferenceExpression featureReferenceExpression = (FeatureReferenceExpression) theEObject;
                T result = this.caseFeatureReferenceExpression(featureReferenceExpression);
                if (result == null)
                    result = this.caseExpression(featureReferenceExpression);
                if (result == null)
                    result = this.caseStep(featureReferenceExpression);
                if (result == null)
                    result = this.caseFeature(featureReferenceExpression);
                if (result == null)
                    result = this.caseType(featureReferenceExpression);
                if (result == null)
                    result = this.caseNamespace(featureReferenceExpression);
                if (result == null)
                    result = this.caseElement(featureReferenceExpression);
                if (result == null)
                    result = this.caseEModelElement(featureReferenceExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_TYPING: {
                FeatureTyping featureTyping = (FeatureTyping) theEObject;
                T result = this.caseFeatureTyping(featureTyping);
                if (result == null)
                    result = this.caseSpecialization(featureTyping);
                if (result == null)
                    result = this.caseRelationship(featureTyping);
                if (result == null)
                    result = this.caseElement(featureTyping);
                if (result == null)
                    result = this.caseEModelElement(featureTyping);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_VALUE: {
                FeatureValue featureValue = (FeatureValue) theEObject;
                T result = this.caseFeatureValue(featureValue);
                if (result == null)
                    result = this.caseOwningMembership(featureValue);
                if (result == null)
                    result = this.caseMembership(featureValue);
                if (result == null)
                    result = this.caseRelationship(featureValue);
                if (result == null)
                    result = this.caseElement(featureValue);
                if (result == null)
                    result = this.caseEModelElement(featureValue);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURING: {
                Featuring featuring = (Featuring) theEObject;
                T result = this.caseFeaturing(featuring);
                if (result == null)
                    result = this.caseRelationship(featuring);
                if (result == null)
                    result = this.caseElement(featuring);
                if (result == null)
                    result = this.caseEModelElement(featuring);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FLOW_CONNECTION_DEFINITION: {
                FlowConnectionDefinition flowConnectionDefinition = (FlowConnectionDefinition) theEObject;
                T result = this.caseFlowConnectionDefinition(flowConnectionDefinition);
                if (result == null)
                    result = this.caseConnectionDefinition(flowConnectionDefinition);
                if (result == null)
                    result = this.caseActionDefinition(flowConnectionDefinition);
                if (result == null)
                    result = this.caseInteraction(flowConnectionDefinition);
                if (result == null)
                    result = this.casePartDefinition(flowConnectionDefinition);
                if (result == null)
                    result = this.caseAssociationStructure(flowConnectionDefinition);
                if (result == null)
                    result = this.caseBehavior(flowConnectionDefinition);
                if (result == null)
                    result = this.caseItemDefinition(flowConnectionDefinition);
                if (result == null)
                    result = this.caseAssociation(flowConnectionDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(flowConnectionDefinition);
                if (result == null)
                    result = this.caseStructure(flowConnectionDefinition);
                if (result == null)
                    result = this.caseRelationship(flowConnectionDefinition);
                if (result == null)
                    result = this.caseDefinition(flowConnectionDefinition);
                if (result == null)
                    result = this.caseClass(flowConnectionDefinition);
                if (result == null)
                    result = this.caseClassifier(flowConnectionDefinition);
                if (result == null)
                    result = this.caseType(flowConnectionDefinition);
                if (result == null)
                    result = this.caseNamespace(flowConnectionDefinition);
                if (result == null)
                    result = this.caseElement(flowConnectionDefinition);
                if (result == null)
                    result = this.caseEModelElement(flowConnectionDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FLOW_CONNECTION_USAGE: {
                FlowConnectionUsage flowConnectionUsage = (FlowConnectionUsage) theEObject;
                T result = this.caseFlowConnectionUsage(flowConnectionUsage);
                if (result == null)
                    result = this.caseConnectionUsage(flowConnectionUsage);
                if (result == null)
                    result = this.caseActionUsage(flowConnectionUsage);
                if (result == null)
                    result = this.caseItemFlow(flowConnectionUsage);
                if (result == null)
                    result = this.caseConnectorAsUsage(flowConnectionUsage);
                if (result == null)
                    result = this.casePartUsage(flowConnectionUsage);
                if (result == null)
                    result = this.caseStep(flowConnectionUsage);
                if (result == null)
                    result = this.caseConnector(flowConnectionUsage);
                if (result == null)
                    result = this.caseItemUsage(flowConnectionUsage);
                if (result == null)
                    result = this.caseFeature(flowConnectionUsage);
                if (result == null)
                    result = this.caseRelationship(flowConnectionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(flowConnectionUsage);
                if (result == null)
                    result = this.caseUsage(flowConnectionUsage);
                if (result == null)
                    result = this.caseType(flowConnectionUsage);
                if (result == null)
                    result = this.caseNamespace(flowConnectionUsage);
                if (result == null)
                    result = this.caseElement(flowConnectionUsage);
                if (result == null)
                    result = this.caseEModelElement(flowConnectionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FORK_NODE: {
                ForkNode forkNode = (ForkNode) theEObject;
                T result = this.caseForkNode(forkNode);
                if (result == null)
                    result = this.caseControlNode(forkNode);
                if (result == null)
                    result = this.caseActionUsage(forkNode);
                if (result == null)
                    result = this.caseOccurrenceUsage(forkNode);
                if (result == null)
                    result = this.caseStep(forkNode);
                if (result == null)
                    result = this.caseUsage(forkNode);
                if (result == null)
                    result = this.caseFeature(forkNode);
                if (result == null)
                    result = this.caseType(forkNode);
                if (result == null)
                    result = this.caseNamespace(forkNode);
                if (result == null)
                    result = this.caseElement(forkNode);
                if (result == null)
                    result = this.caseEModelElement(forkNode);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FOR_LOOP_ACTION_USAGE: {
                ForLoopActionUsage forLoopActionUsage = (ForLoopActionUsage) theEObject;
                T result = this.caseForLoopActionUsage(forLoopActionUsage);
                if (result == null)
                    result = this.caseLoopActionUsage(forLoopActionUsage);
                if (result == null)
                    result = this.caseActionUsage(forLoopActionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(forLoopActionUsage);
                if (result == null)
                    result = this.caseStep(forLoopActionUsage);
                if (result == null)
                    result = this.caseUsage(forLoopActionUsage);
                if (result == null)
                    result = this.caseFeature(forLoopActionUsage);
                if (result == null)
                    result = this.caseType(forLoopActionUsage);
                if (result == null)
                    result = this.caseNamespace(forLoopActionUsage);
                if (result == null)
                    result = this.caseElement(forLoopActionUsage);
                if (result == null)
                    result = this.caseEModelElement(forLoopActionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FRAMED_CONCERN_MEMBERSHIP: {
                FramedConcernMembership framedConcernMembership = (FramedConcernMembership) theEObject;
                T result = this.caseFramedConcernMembership(framedConcernMembership);
                if (result == null)
                    result = this.caseRequirementConstraintMembership(framedConcernMembership);
                if (result == null)
                    result = this.caseFeatureMembership(framedConcernMembership);
                if (result == null)
                    result = this.caseOwningMembership(framedConcernMembership);
                if (result == null)
                    result = this.caseFeaturing(framedConcernMembership);
                if (result == null)
                    result = this.caseMembership(framedConcernMembership);
                if (result == null)
                    result = this.caseRelationship(framedConcernMembership);
                if (result == null)
                    result = this.caseElement(framedConcernMembership);
                if (result == null)
                    result = this.caseEModelElement(framedConcernMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FUNCTION: {
                Function function = (Function) theEObject;
                T result = this.caseFunction(function);
                if (result == null)
                    result = this.caseBehavior(function);
                if (result == null)
                    result = this.caseClass(function);
                if (result == null)
                    result = this.caseClassifier(function);
                if (result == null)
                    result = this.caseType(function);
                if (result == null)
                    result = this.caseNamespace(function);
                if (result == null)
                    result = this.caseElement(function);
                if (result == null)
                    result = this.caseEModelElement(function);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.IF_ACTION_USAGE: {
                IfActionUsage ifActionUsage = (IfActionUsage) theEObject;
                T result = this.caseIfActionUsage(ifActionUsage);
                if (result == null)
                    result = this.caseActionUsage(ifActionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(ifActionUsage);
                if (result == null)
                    result = this.caseStep(ifActionUsage);
                if (result == null)
                    result = this.caseUsage(ifActionUsage);
                if (result == null)
                    result = this.caseFeature(ifActionUsage);
                if (result == null)
                    result = this.caseType(ifActionUsage);
                if (result == null)
                    result = this.caseNamespace(ifActionUsage);
                if (result == null)
                    result = this.caseElement(ifActionUsage);
                if (result == null)
                    result = this.caseEModelElement(ifActionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.IMPORT: {
                Import import_ = (Import) theEObject;
                T result = this.caseImport(import_);
                if (result == null)
                    result = this.caseRelationship(import_);
                if (result == null)
                    result = this.caseElement(import_);
                if (result == null)
                    result = this.caseEModelElement(import_);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INCLUDE_USE_CASE_USAGE: {
                IncludeUseCaseUsage includeUseCaseUsage = (IncludeUseCaseUsage) theEObject;
                T result = this.caseIncludeUseCaseUsage(includeUseCaseUsage);
                if (result == null)
                    result = this.caseUseCaseUsage(includeUseCaseUsage);
                if (result == null)
                    result = this.casePerformActionUsage(includeUseCaseUsage);
                if (result == null)
                    result = this.caseCaseUsage(includeUseCaseUsage);
                if (result == null)
                    result = this.caseEventOccurrenceUsage(includeUseCaseUsage);
                if (result == null)
                    result = this.caseCalculationUsage(includeUseCaseUsage);
                if (result == null)
                    result = this.caseActionUsage(includeUseCaseUsage);
                if (result == null)
                    result = this.caseExpression(includeUseCaseUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(includeUseCaseUsage);
                if (result == null)
                    result = this.caseStep(includeUseCaseUsage);
                if (result == null)
                    result = this.caseUsage(includeUseCaseUsage);
                if (result == null)
                    result = this.caseFeature(includeUseCaseUsage);
                if (result == null)
                    result = this.caseType(includeUseCaseUsage);
                if (result == null)
                    result = this.caseNamespace(includeUseCaseUsage);
                if (result == null)
                    result = this.caseElement(includeUseCaseUsage);
                if (result == null)
                    result = this.caseEModelElement(includeUseCaseUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INTERACTION: {
                Interaction interaction = (Interaction) theEObject;
                T result = this.caseInteraction(interaction);
                if (result == null)
                    result = this.caseAssociation(interaction);
                if (result == null)
                    result = this.caseBehavior(interaction);
                if (result == null)
                    result = this.caseRelationship(interaction);
                if (result == null)
                    result = this.caseClass(interaction);
                if (result == null)
                    result = this.caseClassifier(interaction);
                if (result == null)
                    result = this.caseType(interaction);
                if (result == null)
                    result = this.caseNamespace(interaction);
                if (result == null)
                    result = this.caseElement(interaction);
                if (result == null)
                    result = this.caseEModelElement(interaction);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INTERFACE_DEFINITION: {
                InterfaceDefinition interfaceDefinition = (InterfaceDefinition) theEObject;
                T result = this.caseInterfaceDefinition(interfaceDefinition);
                if (result == null)
                    result = this.caseConnectionDefinition(interfaceDefinition);
                if (result == null)
                    result = this.casePartDefinition(interfaceDefinition);
                if (result == null)
                    result = this.caseAssociationStructure(interfaceDefinition);
                if (result == null)
                    result = this.caseItemDefinition(interfaceDefinition);
                if (result == null)
                    result = this.caseAssociation(interfaceDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(interfaceDefinition);
                if (result == null)
                    result = this.caseStructure(interfaceDefinition);
                if (result == null)
                    result = this.caseRelationship(interfaceDefinition);
                if (result == null)
                    result = this.caseDefinition(interfaceDefinition);
                if (result == null)
                    result = this.caseClass(interfaceDefinition);
                if (result == null)
                    result = this.caseClassifier(interfaceDefinition);
                if (result == null)
                    result = this.caseType(interfaceDefinition);
                if (result == null)
                    result = this.caseNamespace(interfaceDefinition);
                if (result == null)
                    result = this.caseElement(interfaceDefinition);
                if (result == null)
                    result = this.caseEModelElement(interfaceDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INTERFACE_USAGE: {
                InterfaceUsage interfaceUsage = (InterfaceUsage) theEObject;
                T result = this.caseInterfaceUsage(interfaceUsage);
                if (result == null)
                    result = this.caseConnectionUsage(interfaceUsage);
                if (result == null)
                    result = this.caseConnectorAsUsage(interfaceUsage);
                if (result == null)
                    result = this.casePartUsage(interfaceUsage);
                if (result == null)
                    result = this.caseConnector(interfaceUsage);
                if (result == null)
                    result = this.caseItemUsage(interfaceUsage);
                if (result == null)
                    result = this.caseFeature(interfaceUsage);
                if (result == null)
                    result = this.caseRelationship(interfaceUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(interfaceUsage);
                if (result == null)
                    result = this.caseUsage(interfaceUsage);
                if (result == null)
                    result = this.caseType(interfaceUsage);
                if (result == null)
                    result = this.caseNamespace(interfaceUsage);
                if (result == null)
                    result = this.caseElement(interfaceUsage);
                if (result == null)
                    result = this.caseEModelElement(interfaceUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INTERSECTING: {
                Intersecting intersecting = (Intersecting) theEObject;
                T result = this.caseIntersecting(intersecting);
                if (result == null)
                    result = this.caseRelationship(intersecting);
                if (result == null)
                    result = this.caseElement(intersecting);
                if (result == null)
                    result = this.caseEModelElement(intersecting);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INVARIANT: {
                Invariant invariant = (Invariant) theEObject;
                T result = this.caseInvariant(invariant);
                if (result == null)
                    result = this.caseBooleanExpression(invariant);
                if (result == null)
                    result = this.caseExpression(invariant);
                if (result == null)
                    result = this.caseStep(invariant);
                if (result == null)
                    result = this.caseFeature(invariant);
                if (result == null)
                    result = this.caseType(invariant);
                if (result == null)
                    result = this.caseNamespace(invariant);
                if (result == null)
                    result = this.caseElement(invariant);
                if (result == null)
                    result = this.caseEModelElement(invariant);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INVOCATION_EXPRESSION: {
                InvocationExpression invocationExpression = (InvocationExpression) theEObject;
                T result = this.caseInvocationExpression(invocationExpression);
                if (result == null)
                    result = this.caseExpression(invocationExpression);
                if (result == null)
                    result = this.caseStep(invocationExpression);
                if (result == null)
                    result = this.caseFeature(invocationExpression);
                if (result == null)
                    result = this.caseType(invocationExpression);
                if (result == null)
                    result = this.caseNamespace(invocationExpression);
                if (result == null)
                    result = this.caseElement(invocationExpression);
                if (result == null)
                    result = this.caseEModelElement(invocationExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_DEFINITION: {
                ItemDefinition itemDefinition = (ItemDefinition) theEObject;
                T result = this.caseItemDefinition(itemDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(itemDefinition);
                if (result == null)
                    result = this.caseStructure(itemDefinition);
                if (result == null)
                    result = this.caseDefinition(itemDefinition);
                if (result == null)
                    result = this.caseClass(itemDefinition);
                if (result == null)
                    result = this.caseClassifier(itemDefinition);
                if (result == null)
                    result = this.caseType(itemDefinition);
                if (result == null)
                    result = this.caseNamespace(itemDefinition);
                if (result == null)
                    result = this.caseElement(itemDefinition);
                if (result == null)
                    result = this.caseEModelElement(itemDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_FEATURE: {
                ItemFeature itemFeature = (ItemFeature) theEObject;
                T result = this.caseItemFeature(itemFeature);
                if (result == null)
                    result = this.caseFeature(itemFeature);
                if (result == null)
                    result = this.caseType(itemFeature);
                if (result == null)
                    result = this.caseNamespace(itemFeature);
                if (result == null)
                    result = this.caseElement(itemFeature);
                if (result == null)
                    result = this.caseEModelElement(itemFeature);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_FLOW: {
                ItemFlow itemFlow = (ItemFlow) theEObject;
                T result = this.caseItemFlow(itemFlow);
                if (result == null)
                    result = this.caseConnector(itemFlow);
                if (result == null)
                    result = this.caseStep(itemFlow);
                if (result == null)
                    result = this.caseFeature(itemFlow);
                if (result == null)
                    result = this.caseRelationship(itemFlow);
                if (result == null)
                    result = this.caseType(itemFlow);
                if (result == null)
                    result = this.caseNamespace(itemFlow);
                if (result == null)
                    result = this.caseElement(itemFlow);
                if (result == null)
                    result = this.caseEModelElement(itemFlow);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_FLOW_END: {
                ItemFlowEnd itemFlowEnd = (ItemFlowEnd) theEObject;
                T result = this.caseItemFlowEnd(itemFlowEnd);
                if (result == null)
                    result = this.caseFeature(itemFlowEnd);
                if (result == null)
                    result = this.caseType(itemFlowEnd);
                if (result == null)
                    result = this.caseNamespace(itemFlowEnd);
                if (result == null)
                    result = this.caseElement(itemFlowEnd);
                if (result == null)
                    result = this.caseEModelElement(itemFlowEnd);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_USAGE: {
                ItemUsage itemUsage = (ItemUsage) theEObject;
                T result = this.caseItemUsage(itemUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(itemUsage);
                if (result == null)
                    result = this.caseUsage(itemUsage);
                if (result == null)
                    result = this.caseFeature(itemUsage);
                if (result == null)
                    result = this.caseType(itemUsage);
                if (result == null)
                    result = this.caseNamespace(itemUsage);
                if (result == null)
                    result = this.caseElement(itemUsage);
                if (result == null)
                    result = this.caseEModelElement(itemUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.JOIN_NODE: {
                JoinNode joinNode = (JoinNode) theEObject;
                T result = this.caseJoinNode(joinNode);
                if (result == null)
                    result = this.caseControlNode(joinNode);
                if (result == null)
                    result = this.caseActionUsage(joinNode);
                if (result == null)
                    result = this.caseOccurrenceUsage(joinNode);
                if (result == null)
                    result = this.caseStep(joinNode);
                if (result == null)
                    result = this.caseUsage(joinNode);
                if (result == null)
                    result = this.caseFeature(joinNode);
                if (result == null)
                    result = this.caseType(joinNode);
                if (result == null)
                    result = this.caseNamespace(joinNode);
                if (result == null)
                    result = this.caseElement(joinNode);
                if (result == null)
                    result = this.caseEModelElement(joinNode);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LIBRARY_PACKAGE: {
                LibraryPackage libraryPackage = (LibraryPackage) theEObject;
                T result = this.caseLibraryPackage(libraryPackage);
                if (result == null)
                    result = this.casePackage(libraryPackage);
                if (result == null)
                    result = this.caseNamespace(libraryPackage);
                if (result == null)
                    result = this.caseElement(libraryPackage);
                if (result == null)
                    result = this.caseEModelElement(libraryPackage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LIFE_CLASS: {
                LifeClass lifeClass = (LifeClass) theEObject;
                T result = this.caseLifeClass(lifeClass);
                if (result == null)
                    result = this.caseClass(lifeClass);
                if (result == null)
                    result = this.caseClassifier(lifeClass);
                if (result == null)
                    result = this.caseType(lifeClass);
                if (result == null)
                    result = this.caseNamespace(lifeClass);
                if (result == null)
                    result = this.caseElement(lifeClass);
                if (result == null)
                    result = this.caseEModelElement(lifeClass);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_BOOLEAN: {
                LiteralBoolean literalBoolean = (LiteralBoolean) theEObject;
                T result = this.caseLiteralBoolean(literalBoolean);
                if (result == null)
                    result = this.caseLiteralExpression(literalBoolean);
                if (result == null)
                    result = this.caseExpression(literalBoolean);
                if (result == null)
                    result = this.caseStep(literalBoolean);
                if (result == null)
                    result = this.caseFeature(literalBoolean);
                if (result == null)
                    result = this.caseType(literalBoolean);
                if (result == null)
                    result = this.caseNamespace(literalBoolean);
                if (result == null)
                    result = this.caseElement(literalBoolean);
                if (result == null)
                    result = this.caseEModelElement(literalBoolean);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_EXPRESSION: {
                LiteralExpression literalExpression = (LiteralExpression) theEObject;
                T result = this.caseLiteralExpression(literalExpression);
                if (result == null)
                    result = this.caseExpression(literalExpression);
                if (result == null)
                    result = this.caseStep(literalExpression);
                if (result == null)
                    result = this.caseFeature(literalExpression);
                if (result == null)
                    result = this.caseType(literalExpression);
                if (result == null)
                    result = this.caseNamespace(literalExpression);
                if (result == null)
                    result = this.caseElement(literalExpression);
                if (result == null)
                    result = this.caseEModelElement(literalExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_INFINITY: {
                LiteralInfinity literalInfinity = (LiteralInfinity) theEObject;
                T result = this.caseLiteralInfinity(literalInfinity);
                if (result == null)
                    result = this.caseLiteralExpression(literalInfinity);
                if (result == null)
                    result = this.caseExpression(literalInfinity);
                if (result == null)
                    result = this.caseStep(literalInfinity);
                if (result == null)
                    result = this.caseFeature(literalInfinity);
                if (result == null)
                    result = this.caseType(literalInfinity);
                if (result == null)
                    result = this.caseNamespace(literalInfinity);
                if (result == null)
                    result = this.caseElement(literalInfinity);
                if (result == null)
                    result = this.caseEModelElement(literalInfinity);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_INTEGER: {
                LiteralInteger literalInteger = (LiteralInteger) theEObject;
                T result = this.caseLiteralInteger(literalInteger);
                if (result == null)
                    result = this.caseLiteralExpression(literalInteger);
                if (result == null)
                    result = this.caseExpression(literalInteger);
                if (result == null)
                    result = this.caseStep(literalInteger);
                if (result == null)
                    result = this.caseFeature(literalInteger);
                if (result == null)
                    result = this.caseType(literalInteger);
                if (result == null)
                    result = this.caseNamespace(literalInteger);
                if (result == null)
                    result = this.caseElement(literalInteger);
                if (result == null)
                    result = this.caseEModelElement(literalInteger);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_RATIONAL: {
                LiteralRational literalRational = (LiteralRational) theEObject;
                T result = this.caseLiteralRational(literalRational);
                if (result == null)
                    result = this.caseLiteralExpression(literalRational);
                if (result == null)
                    result = this.caseExpression(literalRational);
                if (result == null)
                    result = this.caseStep(literalRational);
                if (result == null)
                    result = this.caseFeature(literalRational);
                if (result == null)
                    result = this.caseType(literalRational);
                if (result == null)
                    result = this.caseNamespace(literalRational);
                if (result == null)
                    result = this.caseElement(literalRational);
                if (result == null)
                    result = this.caseEModelElement(literalRational);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_STRING: {
                LiteralString literalString = (LiteralString) theEObject;
                T result = this.caseLiteralString(literalString);
                if (result == null)
                    result = this.caseLiteralExpression(literalString);
                if (result == null)
                    result = this.caseExpression(literalString);
                if (result == null)
                    result = this.caseStep(literalString);
                if (result == null)
                    result = this.caseFeature(literalString);
                if (result == null)
                    result = this.caseType(literalString);
                if (result == null)
                    result = this.caseNamespace(literalString);
                if (result == null)
                    result = this.caseElement(literalString);
                if (result == null)
                    result = this.caseEModelElement(literalString);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LOOP_ACTION_USAGE: {
                LoopActionUsage loopActionUsage = (LoopActionUsage) theEObject;
                T result = this.caseLoopActionUsage(loopActionUsage);
                if (result == null)
                    result = this.caseActionUsage(loopActionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(loopActionUsage);
                if (result == null)
                    result = this.caseStep(loopActionUsage);
                if (result == null)
                    result = this.caseUsage(loopActionUsage);
                if (result == null)
                    result = this.caseFeature(loopActionUsage);
                if (result == null)
                    result = this.caseType(loopActionUsage);
                if (result == null)
                    result = this.caseNamespace(loopActionUsage);
                if (result == null)
                    result = this.caseElement(loopActionUsage);
                if (result == null)
                    result = this.caseEModelElement(loopActionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MEMBERSHIP: {
                Membership membership = (Membership) theEObject;
                T result = this.caseMembership(membership);
                if (result == null)
                    result = this.caseRelationship(membership);
                if (result == null)
                    result = this.caseElement(membership);
                if (result == null)
                    result = this.caseEModelElement(membership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MEMBERSHIP_EXPOSE: {
                MembershipExpose membershipExpose = (MembershipExpose) theEObject;
                T result = this.caseMembershipExpose(membershipExpose);
                if (result == null)
                    result = this.caseMembershipImport(membershipExpose);
                if (result == null)
                    result = this.caseExpose(membershipExpose);
                if (result == null)
                    result = this.caseImport(membershipExpose);
                if (result == null)
                    result = this.caseRelationship(membershipExpose);
                if (result == null)
                    result = this.caseElement(membershipExpose);
                if (result == null)
                    result = this.caseEModelElement(membershipExpose);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MEMBERSHIP_IMPORT: {
                MembershipImport membershipImport = (MembershipImport) theEObject;
                T result = this.caseMembershipImport(membershipImport);
                if (result == null)
                    result = this.caseImport(membershipImport);
                if (result == null)
                    result = this.caseRelationship(membershipImport);
                if (result == null)
                    result = this.caseElement(membershipImport);
                if (result == null)
                    result = this.caseEModelElement(membershipImport);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MERGE_NODE: {
                MergeNode mergeNode = (MergeNode) theEObject;
                T result = this.caseMergeNode(mergeNode);
                if (result == null)
                    result = this.caseControlNode(mergeNode);
                if (result == null)
                    result = this.caseActionUsage(mergeNode);
                if (result == null)
                    result = this.caseOccurrenceUsage(mergeNode);
                if (result == null)
                    result = this.caseStep(mergeNode);
                if (result == null)
                    result = this.caseUsage(mergeNode);
                if (result == null)
                    result = this.caseFeature(mergeNode);
                if (result == null)
                    result = this.caseType(mergeNode);
                if (result == null)
                    result = this.caseNamespace(mergeNode);
                if (result == null)
                    result = this.caseElement(mergeNode);
                if (result == null)
                    result = this.caseEModelElement(mergeNode);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METACLASS: {
                Metaclass metaclass = (Metaclass) theEObject;
                T result = this.caseMetaclass(metaclass);
                if (result == null)
                    result = this.caseStructure(metaclass);
                if (result == null)
                    result = this.caseClass(metaclass);
                if (result == null)
                    result = this.caseClassifier(metaclass);
                if (result == null)
                    result = this.caseType(metaclass);
                if (result == null)
                    result = this.caseNamespace(metaclass);
                if (result == null)
                    result = this.caseElement(metaclass);
                if (result == null)
                    result = this.caseEModelElement(metaclass);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METADATA_ACCESS_EXPRESSION: {
                MetadataAccessExpression metadataAccessExpression = (MetadataAccessExpression) theEObject;
                T result = this.caseMetadataAccessExpression(metadataAccessExpression);
                if (result == null)
                    result = this.caseExpression(metadataAccessExpression);
                if (result == null)
                    result = this.caseStep(metadataAccessExpression);
                if (result == null)
                    result = this.caseFeature(metadataAccessExpression);
                if (result == null)
                    result = this.caseType(metadataAccessExpression);
                if (result == null)
                    result = this.caseNamespace(metadataAccessExpression);
                if (result == null)
                    result = this.caseElement(metadataAccessExpression);
                if (result == null)
                    result = this.caseEModelElement(metadataAccessExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METADATA_DEFINITION: {
                MetadataDefinition metadataDefinition = (MetadataDefinition) theEObject;
                T result = this.caseMetadataDefinition(metadataDefinition);
                if (result == null)
                    result = this.caseItemDefinition(metadataDefinition);
                if (result == null)
                    result = this.caseMetaclass(metadataDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(metadataDefinition);
                if (result == null)
                    result = this.caseStructure(metadataDefinition);
                if (result == null)
                    result = this.caseDefinition(metadataDefinition);
                if (result == null)
                    result = this.caseClass(metadataDefinition);
                if (result == null)
                    result = this.caseClassifier(metadataDefinition);
                if (result == null)
                    result = this.caseType(metadataDefinition);
                if (result == null)
                    result = this.caseNamespace(metadataDefinition);
                if (result == null)
                    result = this.caseElement(metadataDefinition);
                if (result == null)
                    result = this.caseEModelElement(metadataDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METADATA_FEATURE: {
                MetadataFeature metadataFeature = (MetadataFeature) theEObject;
                T result = this.caseMetadataFeature(metadataFeature);
                if (result == null)
                    result = this.caseFeature(metadataFeature);
                if (result == null)
                    result = this.caseAnnotatingElement(metadataFeature);
                if (result == null)
                    result = this.caseType(metadataFeature);
                if (result == null)
                    result = this.caseNamespace(metadataFeature);
                if (result == null)
                    result = this.caseElement(metadataFeature);
                if (result == null)
                    result = this.caseEModelElement(metadataFeature);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METADATA_USAGE: {
                MetadataUsage metadataUsage = (MetadataUsage) theEObject;
                T result = this.caseMetadataUsage(metadataUsage);
                if (result == null)
                    result = this.caseItemUsage(metadataUsage);
                if (result == null)
                    result = this.caseMetadataFeature(metadataUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(metadataUsage);
                if (result == null)
                    result = this.caseAnnotatingElement(metadataUsage);
                if (result == null)
                    result = this.caseUsage(metadataUsage);
                if (result == null)
                    result = this.caseFeature(metadataUsage);
                if (result == null)
                    result = this.caseType(metadataUsage);
                if (result == null)
                    result = this.caseNamespace(metadataUsage);
                if (result == null)
                    result = this.caseElement(metadataUsage);
                if (result == null)
                    result = this.caseEModelElement(metadataUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MULTIPLICITY: {
                Multiplicity multiplicity = (Multiplicity) theEObject;
                T result = this.caseMultiplicity(multiplicity);
                if (result == null)
                    result = this.caseFeature(multiplicity);
                if (result == null)
                    result = this.caseType(multiplicity);
                if (result == null)
                    result = this.caseNamespace(multiplicity);
                if (result == null)
                    result = this.caseElement(multiplicity);
                if (result == null)
                    result = this.caseEModelElement(multiplicity);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MULTIPLICITY_RANGE: {
                MultiplicityRange multiplicityRange = (MultiplicityRange) theEObject;
                T result = this.caseMultiplicityRange(multiplicityRange);
                if (result == null)
                    result = this.caseMultiplicity(multiplicityRange);
                if (result == null)
                    result = this.caseFeature(multiplicityRange);
                if (result == null)
                    result = this.caseType(multiplicityRange);
                if (result == null)
                    result = this.caseNamespace(multiplicityRange);
                if (result == null)
                    result = this.caseElement(multiplicityRange);
                if (result == null)
                    result = this.caseEModelElement(multiplicityRange);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.NAMESPACE: {
                Namespace namespace = (Namespace) theEObject;
                T result = this.caseNamespace(namespace);
                if (result == null)
                    result = this.caseElement(namespace);
                if (result == null)
                    result = this.caseEModelElement(namespace);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.NAMESPACE_EXPOSE: {
                NamespaceExpose namespaceExpose = (NamespaceExpose) theEObject;
                T result = this.caseNamespaceExpose(namespaceExpose);
                if (result == null)
                    result = this.caseNamespaceImport(namespaceExpose);
                if (result == null)
                    result = this.caseExpose(namespaceExpose);
                if (result == null)
                    result = this.caseImport(namespaceExpose);
                if (result == null)
                    result = this.caseRelationship(namespaceExpose);
                if (result == null)
                    result = this.caseElement(namespaceExpose);
                if (result == null)
                    result = this.caseEModelElement(namespaceExpose);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.NAMESPACE_IMPORT: {
                NamespaceImport namespaceImport = (NamespaceImport) theEObject;
                T result = this.caseNamespaceImport(namespaceImport);
                if (result == null)
                    result = this.caseImport(namespaceImport);
                if (result == null)
                    result = this.caseRelationship(namespaceImport);
                if (result == null)
                    result = this.caseElement(namespaceImport);
                if (result == null)
                    result = this.caseEModelElement(namespaceImport);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.NULL_EXPRESSION: {
                NullExpression nullExpression = (NullExpression) theEObject;
                T result = this.caseNullExpression(nullExpression);
                if (result == null)
                    result = this.caseExpression(nullExpression);
                if (result == null)
                    result = this.caseStep(nullExpression);
                if (result == null)
                    result = this.caseFeature(nullExpression);
                if (result == null)
                    result = this.caseType(nullExpression);
                if (result == null)
                    result = this.caseNamespace(nullExpression);
                if (result == null)
                    result = this.caseElement(nullExpression);
                if (result == null)
                    result = this.caseEModelElement(nullExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OBJECTIVE_MEMBERSHIP: {
                ObjectiveMembership objectiveMembership = (ObjectiveMembership) theEObject;
                T result = this.caseObjectiveMembership(objectiveMembership);
                if (result == null)
                    result = this.caseFeatureMembership(objectiveMembership);
                if (result == null)
                    result = this.caseOwningMembership(objectiveMembership);
                if (result == null)
                    result = this.caseFeaturing(objectiveMembership);
                if (result == null)
                    result = this.caseMembership(objectiveMembership);
                if (result == null)
                    result = this.caseRelationship(objectiveMembership);
                if (result == null)
                    result = this.caseElement(objectiveMembership);
                if (result == null)
                    result = this.caseEModelElement(objectiveMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OCCURRENCE_DEFINITION: {
                OccurrenceDefinition occurrenceDefinition = (OccurrenceDefinition) theEObject;
                T result = this.caseOccurrenceDefinition(occurrenceDefinition);
                if (result == null)
                    result = this.caseDefinition(occurrenceDefinition);
                if (result == null)
                    result = this.caseClass(occurrenceDefinition);
                if (result == null)
                    result = this.caseClassifier(occurrenceDefinition);
                if (result == null)
                    result = this.caseType(occurrenceDefinition);
                if (result == null)
                    result = this.caseNamespace(occurrenceDefinition);
                if (result == null)
                    result = this.caseElement(occurrenceDefinition);
                if (result == null)
                    result = this.caseEModelElement(occurrenceDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OCCURRENCE_USAGE: {
                OccurrenceUsage occurrenceUsage = (OccurrenceUsage) theEObject;
                T result = this.caseOccurrenceUsage(occurrenceUsage);
                if (result == null)
                    result = this.caseUsage(occurrenceUsage);
                if (result == null)
                    result = this.caseFeature(occurrenceUsage);
                if (result == null)
                    result = this.caseType(occurrenceUsage);
                if (result == null)
                    result = this.caseNamespace(occurrenceUsage);
                if (result == null)
                    result = this.caseElement(occurrenceUsage);
                if (result == null)
                    result = this.caseEModelElement(occurrenceUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OPERATOR_EXPRESSION: {
                OperatorExpression operatorExpression = (OperatorExpression) theEObject;
                T result = this.caseOperatorExpression(operatorExpression);
                if (result == null)
                    result = this.caseInvocationExpression(operatorExpression);
                if (result == null)
                    result = this.caseExpression(operatorExpression);
                if (result == null)
                    result = this.caseStep(operatorExpression);
                if (result == null)
                    result = this.caseFeature(operatorExpression);
                if (result == null)
                    result = this.caseType(operatorExpression);
                if (result == null)
                    result = this.caseNamespace(operatorExpression);
                if (result == null)
                    result = this.caseElement(operatorExpression);
                if (result == null)
                    result = this.caseEModelElement(operatorExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OWNING_MEMBERSHIP: {
                OwningMembership owningMembership = (OwningMembership) theEObject;
                T result = this.caseOwningMembership(owningMembership);
                if (result == null)
                    result = this.caseMembership(owningMembership);
                if (result == null)
                    result = this.caseRelationship(owningMembership);
                if (result == null)
                    result = this.caseElement(owningMembership);
                if (result == null)
                    result = this.caseEModelElement(owningMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PACKAGE: {
                org.eclipse.syson.sysml.Package package_ = (org.eclipse.syson.sysml.Package) theEObject;
                T result = this.casePackage(package_);
                if (result == null)
                    result = this.caseNamespace(package_);
                if (result == null)
                    result = this.caseElement(package_);
                if (result == null)
                    result = this.caseEModelElement(package_);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PARAMETER_MEMBERSHIP: {
                ParameterMembership parameterMembership = (ParameterMembership) theEObject;
                T result = this.caseParameterMembership(parameterMembership);
                if (result == null)
                    result = this.caseFeatureMembership(parameterMembership);
                if (result == null)
                    result = this.caseOwningMembership(parameterMembership);
                if (result == null)
                    result = this.caseFeaturing(parameterMembership);
                if (result == null)
                    result = this.caseMembership(parameterMembership);
                if (result == null)
                    result = this.caseRelationship(parameterMembership);
                if (result == null)
                    result = this.caseElement(parameterMembership);
                if (result == null)
                    result = this.caseEModelElement(parameterMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PART_DEFINITION: {
                PartDefinition partDefinition = (PartDefinition) theEObject;
                T result = this.casePartDefinition(partDefinition);
                if (result == null)
                    result = this.caseItemDefinition(partDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(partDefinition);
                if (result == null)
                    result = this.caseStructure(partDefinition);
                if (result == null)
                    result = this.caseDefinition(partDefinition);
                if (result == null)
                    result = this.caseClass(partDefinition);
                if (result == null)
                    result = this.caseClassifier(partDefinition);
                if (result == null)
                    result = this.caseType(partDefinition);
                if (result == null)
                    result = this.caseNamespace(partDefinition);
                if (result == null)
                    result = this.caseElement(partDefinition);
                if (result == null)
                    result = this.caseEModelElement(partDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PART_USAGE: {
                PartUsage partUsage = (PartUsage) theEObject;
                T result = this.casePartUsage(partUsage);
                if (result == null)
                    result = this.caseItemUsage(partUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(partUsage);
                if (result == null)
                    result = this.caseUsage(partUsage);
                if (result == null)
                    result = this.caseFeature(partUsage);
                if (result == null)
                    result = this.caseType(partUsage);
                if (result == null)
                    result = this.caseNamespace(partUsage);
                if (result == null)
                    result = this.caseElement(partUsage);
                if (result == null)
                    result = this.caseEModelElement(partUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PERFORM_ACTION_USAGE: {
                PerformActionUsage performActionUsage = (PerformActionUsage) theEObject;
                T result = this.casePerformActionUsage(performActionUsage);
                if (result == null)
                    result = this.caseActionUsage(performActionUsage);
                if (result == null)
                    result = this.caseEventOccurrenceUsage(performActionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(performActionUsage);
                if (result == null)
                    result = this.caseStep(performActionUsage);
                if (result == null)
                    result = this.caseUsage(performActionUsage);
                if (result == null)
                    result = this.caseFeature(performActionUsage);
                if (result == null)
                    result = this.caseType(performActionUsage);
                if (result == null)
                    result = this.caseNamespace(performActionUsage);
                if (result == null)
                    result = this.caseElement(performActionUsage);
                if (result == null)
                    result = this.caseEModelElement(performActionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PORT_CONJUGATION: {
                PortConjugation portConjugation = (PortConjugation) theEObject;
                T result = this.casePortConjugation(portConjugation);
                if (result == null)
                    result = this.caseConjugation(portConjugation);
                if (result == null)
                    result = this.caseRelationship(portConjugation);
                if (result == null)
                    result = this.caseElement(portConjugation);
                if (result == null)
                    result = this.caseEModelElement(portConjugation);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PORT_DEFINITION: {
                PortDefinition portDefinition = (PortDefinition) theEObject;
                T result = this.casePortDefinition(portDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(portDefinition);
                if (result == null)
                    result = this.caseStructure(portDefinition);
                if (result == null)
                    result = this.caseDefinition(portDefinition);
                if (result == null)
                    result = this.caseClass(portDefinition);
                if (result == null)
                    result = this.caseClassifier(portDefinition);
                if (result == null)
                    result = this.caseType(portDefinition);
                if (result == null)
                    result = this.caseNamespace(portDefinition);
                if (result == null)
                    result = this.caseElement(portDefinition);
                if (result == null)
                    result = this.caseEModelElement(portDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PORT_USAGE: {
                PortUsage portUsage = (PortUsage) theEObject;
                T result = this.casePortUsage(portUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(portUsage);
                if (result == null)
                    result = this.caseUsage(portUsage);
                if (result == null)
                    result = this.caseFeature(portUsage);
                if (result == null)
                    result = this.caseType(portUsage);
                if (result == null)
                    result = this.caseNamespace(portUsage);
                if (result == null)
                    result = this.caseElement(portUsage);
                if (result == null)
                    result = this.caseEModelElement(portUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PREDICATE: {
                Predicate predicate = (Predicate) theEObject;
                T result = this.casePredicate(predicate);
                if (result == null)
                    result = this.caseFunction(predicate);
                if (result == null)
                    result = this.caseBehavior(predicate);
                if (result == null)
                    result = this.caseClass(predicate);
                if (result == null)
                    result = this.caseClassifier(predicate);
                if (result == null)
                    result = this.caseType(predicate);
                if (result == null)
                    result = this.caseNamespace(predicate);
                if (result == null)
                    result = this.caseElement(predicate);
                if (result == null)
                    result = this.caseEModelElement(predicate);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REDEFINITION: {
                Redefinition redefinition = (Redefinition) theEObject;
                T result = this.caseRedefinition(redefinition);
                if (result == null)
                    result = this.caseSubsetting(redefinition);
                if (result == null)
                    result = this.caseSpecialization(redefinition);
                if (result == null)
                    result = this.caseRelationship(redefinition);
                if (result == null)
                    result = this.caseElement(redefinition);
                if (result == null)
                    result = this.caseEModelElement(redefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REFERENCE_SUBSETTING: {
                ReferenceSubsetting referenceSubsetting = (ReferenceSubsetting) theEObject;
                T result = this.caseReferenceSubsetting(referenceSubsetting);
                if (result == null)
                    result = this.caseSubsetting(referenceSubsetting);
                if (result == null)
                    result = this.caseSpecialization(referenceSubsetting);
                if (result == null)
                    result = this.caseRelationship(referenceSubsetting);
                if (result == null)
                    result = this.caseElement(referenceSubsetting);
                if (result == null)
                    result = this.caseEModelElement(referenceSubsetting);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REFERENCE_USAGE: {
                ReferenceUsage referenceUsage = (ReferenceUsage) theEObject;
                T result = this.caseReferenceUsage(referenceUsage);
                if (result == null)
                    result = this.caseUsage(referenceUsage);
                if (result == null)
                    result = this.caseFeature(referenceUsage);
                if (result == null)
                    result = this.caseType(referenceUsage);
                if (result == null)
                    result = this.caseNamespace(referenceUsage);
                if (result == null)
                    result = this.caseElement(referenceUsage);
                if (result == null)
                    result = this.caseEModelElement(referenceUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RELATIONSHIP: {
                Relationship relationship = (Relationship) theEObject;
                T result = this.caseRelationship(relationship);
                if (result == null)
                    result = this.caseElement(relationship);
                if (result == null)
                    result = this.caseEModelElement(relationship);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RENDERING_DEFINITION: {
                RenderingDefinition renderingDefinition = (RenderingDefinition) theEObject;
                T result = this.caseRenderingDefinition(renderingDefinition);
                if (result == null)
                    result = this.casePartDefinition(renderingDefinition);
                if (result == null)
                    result = this.caseItemDefinition(renderingDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(renderingDefinition);
                if (result == null)
                    result = this.caseStructure(renderingDefinition);
                if (result == null)
                    result = this.caseDefinition(renderingDefinition);
                if (result == null)
                    result = this.caseClass(renderingDefinition);
                if (result == null)
                    result = this.caseClassifier(renderingDefinition);
                if (result == null)
                    result = this.caseType(renderingDefinition);
                if (result == null)
                    result = this.caseNamespace(renderingDefinition);
                if (result == null)
                    result = this.caseElement(renderingDefinition);
                if (result == null)
                    result = this.caseEModelElement(renderingDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RENDERING_USAGE: {
                RenderingUsage renderingUsage = (RenderingUsage) theEObject;
                T result = this.caseRenderingUsage(renderingUsage);
                if (result == null)
                    result = this.casePartUsage(renderingUsage);
                if (result == null)
                    result = this.caseItemUsage(renderingUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(renderingUsage);
                if (result == null)
                    result = this.caseUsage(renderingUsage);
                if (result == null)
                    result = this.caseFeature(renderingUsage);
                if (result == null)
                    result = this.caseType(renderingUsage);
                if (result == null)
                    result = this.caseNamespace(renderingUsage);
                if (result == null)
                    result = this.caseElement(renderingUsage);
                if (result == null)
                    result = this.caseEModelElement(renderingUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP: {
                RequirementConstraintMembership requirementConstraintMembership = (RequirementConstraintMembership) theEObject;
                T result = this.caseRequirementConstraintMembership(requirementConstraintMembership);
                if (result == null)
                    result = this.caseFeatureMembership(requirementConstraintMembership);
                if (result == null)
                    result = this.caseOwningMembership(requirementConstraintMembership);
                if (result == null)
                    result = this.caseFeaturing(requirementConstraintMembership);
                if (result == null)
                    result = this.caseMembership(requirementConstraintMembership);
                if (result == null)
                    result = this.caseRelationship(requirementConstraintMembership);
                if (result == null)
                    result = this.caseElement(requirementConstraintMembership);
                if (result == null)
                    result = this.caseEModelElement(requirementConstraintMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REQUIREMENT_DEFINITION: {
                RequirementDefinition requirementDefinition = (RequirementDefinition) theEObject;
                T result = this.caseRequirementDefinition(requirementDefinition);
                if (result == null)
                    result = this.caseConstraintDefinition(requirementDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(requirementDefinition);
                if (result == null)
                    result = this.casePredicate(requirementDefinition);
                if (result == null)
                    result = this.caseDefinition(requirementDefinition);
                if (result == null)
                    result = this.caseFunction(requirementDefinition);
                if (result == null)
                    result = this.caseClassifier(requirementDefinition);
                if (result == null)
                    result = this.caseBehavior(requirementDefinition);
                if (result == null)
                    result = this.caseType(requirementDefinition);
                if (result == null)
                    result = this.caseClass(requirementDefinition);
                if (result == null)
                    result = this.caseNamespace(requirementDefinition);
                if (result == null)
                    result = this.caseElement(requirementDefinition);
                if (result == null)
                    result = this.caseEModelElement(requirementDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REQUIREMENT_USAGE: {
                RequirementUsage requirementUsage = (RequirementUsage) theEObject;
                T result = this.caseRequirementUsage(requirementUsage);
                if (result == null)
                    result = this.caseConstraintUsage(requirementUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(requirementUsage);
                if (result == null)
                    result = this.caseBooleanExpression(requirementUsage);
                if (result == null)
                    result = this.caseUsage(requirementUsage);
                if (result == null)
                    result = this.caseExpression(requirementUsage);
                if (result == null)
                    result = this.caseStep(requirementUsage);
                if (result == null)
                    result = this.caseFeature(requirementUsage);
                if (result == null)
                    result = this.caseType(requirementUsage);
                if (result == null)
                    result = this.caseNamespace(requirementUsage);
                if (result == null)
                    result = this.caseElement(requirementUsage);
                if (result == null)
                    result = this.caseEModelElement(requirementUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REQUIREMENT_VERIFICATION_MEMBERSHIP: {
                RequirementVerificationMembership requirementVerificationMembership = (RequirementVerificationMembership) theEObject;
                T result = this.caseRequirementVerificationMembership(requirementVerificationMembership);
                if (result == null)
                    result = this.caseRequirementConstraintMembership(requirementVerificationMembership);
                if (result == null)
                    result = this.caseFeatureMembership(requirementVerificationMembership);
                if (result == null)
                    result = this.caseOwningMembership(requirementVerificationMembership);
                if (result == null)
                    result = this.caseFeaturing(requirementVerificationMembership);
                if (result == null)
                    result = this.caseMembership(requirementVerificationMembership);
                if (result == null)
                    result = this.caseRelationship(requirementVerificationMembership);
                if (result == null)
                    result = this.caseElement(requirementVerificationMembership);
                if (result == null)
                    result = this.caseEModelElement(requirementVerificationMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RESULT_EXPRESSION_MEMBERSHIP: {
                ResultExpressionMembership resultExpressionMembership = (ResultExpressionMembership) theEObject;
                T result = this.caseResultExpressionMembership(resultExpressionMembership);
                if (result == null)
                    result = this.caseFeatureMembership(resultExpressionMembership);
                if (result == null)
                    result = this.caseOwningMembership(resultExpressionMembership);
                if (result == null)
                    result = this.caseFeaturing(resultExpressionMembership);
                if (result == null)
                    result = this.caseMembership(resultExpressionMembership);
                if (result == null)
                    result = this.caseRelationship(resultExpressionMembership);
                if (result == null)
                    result = this.caseElement(resultExpressionMembership);
                if (result == null)
                    result = this.caseEModelElement(resultExpressionMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RETURN_PARAMETER_MEMBERSHIP: {
                ReturnParameterMembership returnParameterMembership = (ReturnParameterMembership) theEObject;
                T result = this.caseReturnParameterMembership(returnParameterMembership);
                if (result == null)
                    result = this.caseParameterMembership(returnParameterMembership);
                if (result == null)
                    result = this.caseFeatureMembership(returnParameterMembership);
                if (result == null)
                    result = this.caseOwningMembership(returnParameterMembership);
                if (result == null)
                    result = this.caseFeaturing(returnParameterMembership);
                if (result == null)
                    result = this.caseMembership(returnParameterMembership);
                if (result == null)
                    result = this.caseRelationship(returnParameterMembership);
                if (result == null)
                    result = this.caseElement(returnParameterMembership);
                if (result == null)
                    result = this.caseEModelElement(returnParameterMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE: {
                SatisfyRequirementUsage satisfyRequirementUsage = (SatisfyRequirementUsage) theEObject;
                T result = this.caseSatisfyRequirementUsage(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseRequirementUsage(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseAssertConstraintUsage(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseConstraintUsage(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseInvariant(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseBooleanExpression(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseUsage(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseExpression(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseStep(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseFeature(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseType(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseNamespace(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseElement(satisfyRequirementUsage);
                if (result == null)
                    result = this.caseEModelElement(satisfyRequirementUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SELECT_EXPRESSION: {
                SelectExpression selectExpression = (SelectExpression) theEObject;
                T result = this.caseSelectExpression(selectExpression);
                if (result == null)
                    result = this.caseOperatorExpression(selectExpression);
                if (result == null)
                    result = this.caseInvocationExpression(selectExpression);
                if (result == null)
                    result = this.caseExpression(selectExpression);
                if (result == null)
                    result = this.caseStep(selectExpression);
                if (result == null)
                    result = this.caseFeature(selectExpression);
                if (result == null)
                    result = this.caseType(selectExpression);
                if (result == null)
                    result = this.caseNamespace(selectExpression);
                if (result == null)
                    result = this.caseElement(selectExpression);
                if (result == null)
                    result = this.caseEModelElement(selectExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SEND_ACTION_USAGE: {
                SendActionUsage sendActionUsage = (SendActionUsage) theEObject;
                T result = this.caseSendActionUsage(sendActionUsage);
                if (result == null)
                    result = this.caseActionUsage(sendActionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(sendActionUsage);
                if (result == null)
                    result = this.caseStep(sendActionUsage);
                if (result == null)
                    result = this.caseUsage(sendActionUsage);
                if (result == null)
                    result = this.caseFeature(sendActionUsage);
                if (result == null)
                    result = this.caseType(sendActionUsage);
                if (result == null)
                    result = this.caseNamespace(sendActionUsage);
                if (result == null)
                    result = this.caseElement(sendActionUsage);
                if (result == null)
                    result = this.caseEModelElement(sendActionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SPECIALIZATION: {
                Specialization specialization = (Specialization) theEObject;
                T result = this.caseSpecialization(specialization);
                if (result == null)
                    result = this.caseRelationship(specialization);
                if (result == null)
                    result = this.caseElement(specialization);
                if (result == null)
                    result = this.caseEModelElement(specialization);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STAKEHOLDER_MEMBERSHIP: {
                StakeholderMembership stakeholderMembership = (StakeholderMembership) theEObject;
                T result = this.caseStakeholderMembership(stakeholderMembership);
                if (result == null)
                    result = this.caseParameterMembership(stakeholderMembership);
                if (result == null)
                    result = this.caseFeatureMembership(stakeholderMembership);
                if (result == null)
                    result = this.caseOwningMembership(stakeholderMembership);
                if (result == null)
                    result = this.caseFeaturing(stakeholderMembership);
                if (result == null)
                    result = this.caseMembership(stakeholderMembership);
                if (result == null)
                    result = this.caseRelationship(stakeholderMembership);
                if (result == null)
                    result = this.caseElement(stakeholderMembership);
                if (result == null)
                    result = this.caseEModelElement(stakeholderMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STATE_DEFINITION: {
                StateDefinition stateDefinition = (StateDefinition) theEObject;
                T result = this.caseStateDefinition(stateDefinition);
                if (result == null)
                    result = this.caseActionDefinition(stateDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(stateDefinition);
                if (result == null)
                    result = this.caseBehavior(stateDefinition);
                if (result == null)
                    result = this.caseDefinition(stateDefinition);
                if (result == null)
                    result = this.caseClass(stateDefinition);
                if (result == null)
                    result = this.caseClassifier(stateDefinition);
                if (result == null)
                    result = this.caseType(stateDefinition);
                if (result == null)
                    result = this.caseNamespace(stateDefinition);
                if (result == null)
                    result = this.caseElement(stateDefinition);
                if (result == null)
                    result = this.caseEModelElement(stateDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STATE_SUBACTION_MEMBERSHIP: {
                StateSubactionMembership stateSubactionMembership = (StateSubactionMembership) theEObject;
                T result = this.caseStateSubactionMembership(stateSubactionMembership);
                if (result == null)
                    result = this.caseFeatureMembership(stateSubactionMembership);
                if (result == null)
                    result = this.caseOwningMembership(stateSubactionMembership);
                if (result == null)
                    result = this.caseFeaturing(stateSubactionMembership);
                if (result == null)
                    result = this.caseMembership(stateSubactionMembership);
                if (result == null)
                    result = this.caseRelationship(stateSubactionMembership);
                if (result == null)
                    result = this.caseElement(stateSubactionMembership);
                if (result == null)
                    result = this.caseEModelElement(stateSubactionMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STATE_USAGE: {
                StateUsage stateUsage = (StateUsage) theEObject;
                T result = this.caseStateUsage(stateUsage);
                if (result == null)
                    result = this.caseActionUsage(stateUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(stateUsage);
                if (result == null)
                    result = this.caseStep(stateUsage);
                if (result == null)
                    result = this.caseUsage(stateUsage);
                if (result == null)
                    result = this.caseFeature(stateUsage);
                if (result == null)
                    result = this.caseType(stateUsage);
                if (result == null)
                    result = this.caseNamespace(stateUsage);
                if (result == null)
                    result = this.caseElement(stateUsage);
                if (result == null)
                    result = this.caseEModelElement(stateUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STEP: {
                Step step = (Step) theEObject;
                T result = this.caseStep(step);
                if (result == null)
                    result = this.caseFeature(step);
                if (result == null)
                    result = this.caseType(step);
                if (result == null)
                    result = this.caseNamespace(step);
                if (result == null)
                    result = this.caseElement(step);
                if (result == null)
                    result = this.caseEModelElement(step);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STRUCTURE: {
                Structure structure = (Structure) theEObject;
                T result = this.caseStructure(structure);
                if (result == null)
                    result = this.caseClass(structure);
                if (result == null)
                    result = this.caseClassifier(structure);
                if (result == null)
                    result = this.caseType(structure);
                if (result == null)
                    result = this.caseNamespace(structure);
                if (result == null)
                    result = this.caseElement(structure);
                if (result == null)
                    result = this.caseEModelElement(structure);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUBCLASSIFICATION: {
                Subclassification subclassification = (Subclassification) theEObject;
                T result = this.caseSubclassification(subclassification);
                if (result == null)
                    result = this.caseSpecialization(subclassification);
                if (result == null)
                    result = this.caseRelationship(subclassification);
                if (result == null)
                    result = this.caseElement(subclassification);
                if (result == null)
                    result = this.caseEModelElement(subclassification);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUBJECT_MEMBERSHIP: {
                SubjectMembership subjectMembership = (SubjectMembership) theEObject;
                T result = this.caseSubjectMembership(subjectMembership);
                if (result == null)
                    result = this.caseParameterMembership(subjectMembership);
                if (result == null)
                    result = this.caseFeatureMembership(subjectMembership);
                if (result == null)
                    result = this.caseOwningMembership(subjectMembership);
                if (result == null)
                    result = this.caseFeaturing(subjectMembership);
                if (result == null)
                    result = this.caseMembership(subjectMembership);
                if (result == null)
                    result = this.caseRelationship(subjectMembership);
                if (result == null)
                    result = this.caseElement(subjectMembership);
                if (result == null)
                    result = this.caseEModelElement(subjectMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUBSETTING: {
                Subsetting subsetting = (Subsetting) theEObject;
                T result = this.caseSubsetting(subsetting);
                if (result == null)
                    result = this.caseSpecialization(subsetting);
                if (result == null)
                    result = this.caseRelationship(subsetting);
                if (result == null)
                    result = this.caseElement(subsetting);
                if (result == null)
                    result = this.caseEModelElement(subsetting);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUCCESSION: {
                Succession succession = (Succession) theEObject;
                T result = this.caseSuccession(succession);
                if (result == null)
                    result = this.caseConnector(succession);
                if (result == null)
                    result = this.caseFeature(succession);
                if (result == null)
                    result = this.caseRelationship(succession);
                if (result == null)
                    result = this.caseType(succession);
                if (result == null)
                    result = this.caseNamespace(succession);
                if (result == null)
                    result = this.caseElement(succession);
                if (result == null)
                    result = this.caseEModelElement(succession);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUCCESSION_AS_USAGE: {
                SuccessionAsUsage successionAsUsage = (SuccessionAsUsage) theEObject;
                T result = this.caseSuccessionAsUsage(successionAsUsage);
                if (result == null)
                    result = this.caseConnectorAsUsage(successionAsUsage);
                if (result == null)
                    result = this.caseSuccession(successionAsUsage);
                if (result == null)
                    result = this.caseUsage(successionAsUsage);
                if (result == null)
                    result = this.caseConnector(successionAsUsage);
                if (result == null)
                    result = this.caseFeature(successionAsUsage);
                if (result == null)
                    result = this.caseRelationship(successionAsUsage);
                if (result == null)
                    result = this.caseType(successionAsUsage);
                if (result == null)
                    result = this.caseNamespace(successionAsUsage);
                if (result == null)
                    result = this.caseElement(successionAsUsage);
                if (result == null)
                    result = this.caseEModelElement(successionAsUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUCCESSION_FLOW_CONNECTION_USAGE: {
                SuccessionFlowConnectionUsage successionFlowConnectionUsage = (SuccessionFlowConnectionUsage) theEObject;
                T result = this.caseSuccessionFlowConnectionUsage(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseFlowConnectionUsage(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseSuccessionItemFlow(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseConnectionUsage(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseActionUsage(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseItemFlow(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseSuccession(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseConnectorAsUsage(successionFlowConnectionUsage);
                if (result == null)
                    result = this.casePartUsage(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseStep(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseConnector(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseItemUsage(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseFeature(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseRelationship(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseUsage(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseType(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseNamespace(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseElement(successionFlowConnectionUsage);
                if (result == null)
                    result = this.caseEModelElement(successionFlowConnectionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUCCESSION_ITEM_FLOW: {
                SuccessionItemFlow successionItemFlow = (SuccessionItemFlow) theEObject;
                T result = this.caseSuccessionItemFlow(successionItemFlow);
                if (result == null)
                    result = this.caseItemFlow(successionItemFlow);
                if (result == null)
                    result = this.caseSuccession(successionItemFlow);
                if (result == null)
                    result = this.caseConnector(successionItemFlow);
                if (result == null)
                    result = this.caseStep(successionItemFlow);
                if (result == null)
                    result = this.caseFeature(successionItemFlow);
                if (result == null)
                    result = this.caseRelationship(successionItemFlow);
                if (result == null)
                    result = this.caseType(successionItemFlow);
                if (result == null)
                    result = this.caseNamespace(successionItemFlow);
                if (result == null)
                    result = this.caseElement(successionItemFlow);
                if (result == null)
                    result = this.caseEModelElement(successionItemFlow);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TERMINATE_ACTION_USAGE: {
                TerminateActionUsage terminateActionUsage = (TerminateActionUsage) theEObject;
                T result = this.caseTerminateActionUsage(terminateActionUsage);
                if (result == null)
                    result = this.caseActionUsage(terminateActionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(terminateActionUsage);
                if (result == null)
                    result = this.caseStep(terminateActionUsage);
                if (result == null)
                    result = this.caseUsage(terminateActionUsage);
                if (result == null)
                    result = this.caseFeature(terminateActionUsage);
                if (result == null)
                    result = this.caseType(terminateActionUsage);
                if (result == null)
                    result = this.caseNamespace(terminateActionUsage);
                if (result == null)
                    result = this.caseElement(terminateActionUsage);
                if (result == null)
                    result = this.caseEModelElement(terminateActionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TEXTUAL_REPRESENTATION: {
                TextualRepresentation textualRepresentation = (TextualRepresentation) theEObject;
                T result = this.caseTextualRepresentation(textualRepresentation);
                if (result == null)
                    result = this.caseAnnotatingElement(textualRepresentation);
                if (result == null)
                    result = this.caseElement(textualRepresentation);
                if (result == null)
                    result = this.caseEModelElement(textualRepresentation);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP: {
                TransitionFeatureMembership transitionFeatureMembership = (TransitionFeatureMembership) theEObject;
                T result = this.caseTransitionFeatureMembership(transitionFeatureMembership);
                if (result == null)
                    result = this.caseFeatureMembership(transitionFeatureMembership);
                if (result == null)
                    result = this.caseOwningMembership(transitionFeatureMembership);
                if (result == null)
                    result = this.caseFeaturing(transitionFeatureMembership);
                if (result == null)
                    result = this.caseMembership(transitionFeatureMembership);
                if (result == null)
                    result = this.caseRelationship(transitionFeatureMembership);
                if (result == null)
                    result = this.caseElement(transitionFeatureMembership);
                if (result == null)
                    result = this.caseEModelElement(transitionFeatureMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TRANSITION_USAGE: {
                TransitionUsage transitionUsage = (TransitionUsage) theEObject;
                T result = this.caseTransitionUsage(transitionUsage);
                if (result == null)
                    result = this.caseActionUsage(transitionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(transitionUsage);
                if (result == null)
                    result = this.caseStep(transitionUsage);
                if (result == null)
                    result = this.caseUsage(transitionUsage);
                if (result == null)
                    result = this.caseFeature(transitionUsage);
                if (result == null)
                    result = this.caseType(transitionUsage);
                if (result == null)
                    result = this.caseNamespace(transitionUsage);
                if (result == null)
                    result = this.caseElement(transitionUsage);
                if (result == null)
                    result = this.caseEModelElement(transitionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TRIGGER_INVOCATION_EXPRESSION: {
                TriggerInvocationExpression triggerInvocationExpression = (TriggerInvocationExpression) theEObject;
                T result = this.caseTriggerInvocationExpression(triggerInvocationExpression);
                if (result == null)
                    result = this.caseInvocationExpression(triggerInvocationExpression);
                if (result == null)
                    result = this.caseExpression(triggerInvocationExpression);
                if (result == null)
                    result = this.caseStep(triggerInvocationExpression);
                if (result == null)
                    result = this.caseFeature(triggerInvocationExpression);
                if (result == null)
                    result = this.caseType(triggerInvocationExpression);
                if (result == null)
                    result = this.caseNamespace(triggerInvocationExpression);
                if (result == null)
                    result = this.caseElement(triggerInvocationExpression);
                if (result == null)
                    result = this.caseEModelElement(triggerInvocationExpression);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TYPE: {
                Type type = (Type) theEObject;
                T result = this.caseType(type);
                if (result == null)
                    result = this.caseNamespace(type);
                if (result == null)
                    result = this.caseElement(type);
                if (result == null)
                    result = this.caseEModelElement(type);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TYPE_FEATURING: {
                TypeFeaturing typeFeaturing = (TypeFeaturing) theEObject;
                T result = this.caseTypeFeaturing(typeFeaturing);
                if (result == null)
                    result = this.caseFeaturing(typeFeaturing);
                if (result == null)
                    result = this.caseRelationship(typeFeaturing);
                if (result == null)
                    result = this.caseElement(typeFeaturing);
                if (result == null)
                    result = this.caseEModelElement(typeFeaturing);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.UNIONING: {
                Unioning unioning = (Unioning) theEObject;
                T result = this.caseUnioning(unioning);
                if (result == null)
                    result = this.caseRelationship(unioning);
                if (result == null)
                    result = this.caseElement(unioning);
                if (result == null)
                    result = this.caseEModelElement(unioning);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.USAGE: {
                Usage usage = (Usage) theEObject;
                T result = this.caseUsage(usage);
                if (result == null)
                    result = this.caseFeature(usage);
                if (result == null)
                    result = this.caseType(usage);
                if (result == null)
                    result = this.caseNamespace(usage);
                if (result == null)
                    result = this.caseElement(usage);
                if (result == null)
                    result = this.caseEModelElement(usage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.USE_CASE_DEFINITION: {
                UseCaseDefinition useCaseDefinition = (UseCaseDefinition) theEObject;
                T result = this.caseUseCaseDefinition(useCaseDefinition);
                if (result == null)
                    result = this.caseCaseDefinition(useCaseDefinition);
                if (result == null)
                    result = this.caseCalculationDefinition(useCaseDefinition);
                if (result == null)
                    result = this.caseActionDefinition(useCaseDefinition);
                if (result == null)
                    result = this.caseFunction(useCaseDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(useCaseDefinition);
                if (result == null)
                    result = this.caseBehavior(useCaseDefinition);
                if (result == null)
                    result = this.caseDefinition(useCaseDefinition);
                if (result == null)
                    result = this.caseClass(useCaseDefinition);
                if (result == null)
                    result = this.caseClassifier(useCaseDefinition);
                if (result == null)
                    result = this.caseType(useCaseDefinition);
                if (result == null)
                    result = this.caseNamespace(useCaseDefinition);
                if (result == null)
                    result = this.caseElement(useCaseDefinition);
                if (result == null)
                    result = this.caseEModelElement(useCaseDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.USE_CASE_USAGE: {
                UseCaseUsage useCaseUsage = (UseCaseUsage) theEObject;
                T result = this.caseUseCaseUsage(useCaseUsage);
                if (result == null)
                    result = this.caseCaseUsage(useCaseUsage);
                if (result == null)
                    result = this.caseCalculationUsage(useCaseUsage);
                if (result == null)
                    result = this.caseActionUsage(useCaseUsage);
                if (result == null)
                    result = this.caseExpression(useCaseUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(useCaseUsage);
                if (result == null)
                    result = this.caseStep(useCaseUsage);
                if (result == null)
                    result = this.caseUsage(useCaseUsage);
                if (result == null)
                    result = this.caseFeature(useCaseUsage);
                if (result == null)
                    result = this.caseType(useCaseUsage);
                if (result == null)
                    result = this.caseNamespace(useCaseUsage);
                if (result == null)
                    result = this.caseElement(useCaseUsage);
                if (result == null)
                    result = this.caseEModelElement(useCaseUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VARIANT_MEMBERSHIP: {
                VariantMembership variantMembership = (VariantMembership) theEObject;
                T result = this.caseVariantMembership(variantMembership);
                if (result == null)
                    result = this.caseOwningMembership(variantMembership);
                if (result == null)
                    result = this.caseMembership(variantMembership);
                if (result == null)
                    result = this.caseRelationship(variantMembership);
                if (result == null)
                    result = this.caseElement(variantMembership);
                if (result == null)
                    result = this.caseEModelElement(variantMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VERIFICATION_CASE_DEFINITION: {
                VerificationCaseDefinition verificationCaseDefinition = (VerificationCaseDefinition) theEObject;
                T result = this.caseVerificationCaseDefinition(verificationCaseDefinition);
                if (result == null)
                    result = this.caseCaseDefinition(verificationCaseDefinition);
                if (result == null)
                    result = this.caseCalculationDefinition(verificationCaseDefinition);
                if (result == null)
                    result = this.caseActionDefinition(verificationCaseDefinition);
                if (result == null)
                    result = this.caseFunction(verificationCaseDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(verificationCaseDefinition);
                if (result == null)
                    result = this.caseBehavior(verificationCaseDefinition);
                if (result == null)
                    result = this.caseDefinition(verificationCaseDefinition);
                if (result == null)
                    result = this.caseClass(verificationCaseDefinition);
                if (result == null)
                    result = this.caseClassifier(verificationCaseDefinition);
                if (result == null)
                    result = this.caseType(verificationCaseDefinition);
                if (result == null)
                    result = this.caseNamespace(verificationCaseDefinition);
                if (result == null)
                    result = this.caseElement(verificationCaseDefinition);
                if (result == null)
                    result = this.caseEModelElement(verificationCaseDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VERIFICATION_CASE_USAGE: {
                VerificationCaseUsage verificationCaseUsage = (VerificationCaseUsage) theEObject;
                T result = this.caseVerificationCaseUsage(verificationCaseUsage);
                if (result == null)
                    result = this.caseCaseUsage(verificationCaseUsage);
                if (result == null)
                    result = this.caseCalculationUsage(verificationCaseUsage);
                if (result == null)
                    result = this.caseActionUsage(verificationCaseUsage);
                if (result == null)
                    result = this.caseExpression(verificationCaseUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(verificationCaseUsage);
                if (result == null)
                    result = this.caseStep(verificationCaseUsage);
                if (result == null)
                    result = this.caseUsage(verificationCaseUsage);
                if (result == null)
                    result = this.caseFeature(verificationCaseUsage);
                if (result == null)
                    result = this.caseType(verificationCaseUsage);
                if (result == null)
                    result = this.caseNamespace(verificationCaseUsage);
                if (result == null)
                    result = this.caseElement(verificationCaseUsage);
                if (result == null)
                    result = this.caseEModelElement(verificationCaseUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEW_DEFINITION: {
                ViewDefinition viewDefinition = (ViewDefinition) theEObject;
                T result = this.caseViewDefinition(viewDefinition);
                if (result == null)
                    result = this.casePartDefinition(viewDefinition);
                if (result == null)
                    result = this.caseItemDefinition(viewDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(viewDefinition);
                if (result == null)
                    result = this.caseStructure(viewDefinition);
                if (result == null)
                    result = this.caseDefinition(viewDefinition);
                if (result == null)
                    result = this.caseClass(viewDefinition);
                if (result == null)
                    result = this.caseClassifier(viewDefinition);
                if (result == null)
                    result = this.caseType(viewDefinition);
                if (result == null)
                    result = this.caseNamespace(viewDefinition);
                if (result == null)
                    result = this.caseElement(viewDefinition);
                if (result == null)
                    result = this.caseEModelElement(viewDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEWPOINT_DEFINITION: {
                ViewpointDefinition viewpointDefinition = (ViewpointDefinition) theEObject;
                T result = this.caseViewpointDefinition(viewpointDefinition);
                if (result == null)
                    result = this.caseRequirementDefinition(viewpointDefinition);
                if (result == null)
                    result = this.caseConstraintDefinition(viewpointDefinition);
                if (result == null)
                    result = this.caseOccurrenceDefinition(viewpointDefinition);
                if (result == null)
                    result = this.casePredicate(viewpointDefinition);
                if (result == null)
                    result = this.caseDefinition(viewpointDefinition);
                if (result == null)
                    result = this.caseFunction(viewpointDefinition);
                if (result == null)
                    result = this.caseClassifier(viewpointDefinition);
                if (result == null)
                    result = this.caseBehavior(viewpointDefinition);
                if (result == null)
                    result = this.caseType(viewpointDefinition);
                if (result == null)
                    result = this.caseClass(viewpointDefinition);
                if (result == null)
                    result = this.caseNamespace(viewpointDefinition);
                if (result == null)
                    result = this.caseElement(viewpointDefinition);
                if (result == null)
                    result = this.caseEModelElement(viewpointDefinition);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEWPOINT_USAGE: {
                ViewpointUsage viewpointUsage = (ViewpointUsage) theEObject;
                T result = this.caseViewpointUsage(viewpointUsage);
                if (result == null)
                    result = this.caseRequirementUsage(viewpointUsage);
                if (result == null)
                    result = this.caseConstraintUsage(viewpointUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(viewpointUsage);
                if (result == null)
                    result = this.caseBooleanExpression(viewpointUsage);
                if (result == null)
                    result = this.caseUsage(viewpointUsage);
                if (result == null)
                    result = this.caseExpression(viewpointUsage);
                if (result == null)
                    result = this.caseStep(viewpointUsage);
                if (result == null)
                    result = this.caseFeature(viewpointUsage);
                if (result == null)
                    result = this.caseType(viewpointUsage);
                if (result == null)
                    result = this.caseNamespace(viewpointUsage);
                if (result == null)
                    result = this.caseElement(viewpointUsage);
                if (result == null)
                    result = this.caseEModelElement(viewpointUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEW_RENDERING_MEMBERSHIP: {
                ViewRenderingMembership viewRenderingMembership = (ViewRenderingMembership) theEObject;
                T result = this.caseViewRenderingMembership(viewRenderingMembership);
                if (result == null)
                    result = this.caseFeatureMembership(viewRenderingMembership);
                if (result == null)
                    result = this.caseOwningMembership(viewRenderingMembership);
                if (result == null)
                    result = this.caseFeaturing(viewRenderingMembership);
                if (result == null)
                    result = this.caseMembership(viewRenderingMembership);
                if (result == null)
                    result = this.caseRelationship(viewRenderingMembership);
                if (result == null)
                    result = this.caseElement(viewRenderingMembership);
                if (result == null)
                    result = this.caseEModelElement(viewRenderingMembership);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEW_USAGE: {
                ViewUsage viewUsage = (ViewUsage) theEObject;
                T result = this.caseViewUsage(viewUsage);
                if (result == null)
                    result = this.casePartUsage(viewUsage);
                if (result == null)
                    result = this.caseItemUsage(viewUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(viewUsage);
                if (result == null)
                    result = this.caseUsage(viewUsage);
                if (result == null)
                    result = this.caseFeature(viewUsage);
                if (result == null)
                    result = this.caseType(viewUsage);
                if (result == null)
                    result = this.caseNamespace(viewUsage);
                if (result == null)
                    result = this.caseElement(viewUsage);
                if (result == null)
                    result = this.caseEModelElement(viewUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.WHILE_LOOP_ACTION_USAGE: {
                WhileLoopActionUsage whileLoopActionUsage = (WhileLoopActionUsage) theEObject;
                T result = this.caseWhileLoopActionUsage(whileLoopActionUsage);
                if (result == null)
                    result = this.caseLoopActionUsage(whileLoopActionUsage);
                if (result == null)
                    result = this.caseActionUsage(whileLoopActionUsage);
                if (result == null)
                    result = this.caseOccurrenceUsage(whileLoopActionUsage);
                if (result == null)
                    result = this.caseStep(whileLoopActionUsage);
                if (result == null)
                    result = this.caseUsage(whileLoopActionUsage);
                if (result == null)
                    result = this.caseFeature(whileLoopActionUsage);
                if (result == null)
                    result = this.caseType(whileLoopActionUsage);
                if (result == null)
                    result = this.caseNamespace(whileLoopActionUsage);
                if (result == null)
                    result = this.caseElement(whileLoopActionUsage);
                if (result == null)
                    result = this.caseEModelElement(whileLoopActionUsage);
                if (result == null)
                    result = this.defaultCase(theEObject);
                return result;
            }
            default:
                return this.defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Accept Action Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Accept Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAcceptActionUsage(AcceptActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Action Definition</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Action Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseActionDefinition(ActionDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Action Usage</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseActionUsage(ActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Actor Membership</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Actor Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseActorMembership(ActorMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allocation Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allocation Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAllocationDefinition(AllocationDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allocation Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allocation Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAllocationUsage(AllocationUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Analysis Case Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Analysis Case Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnalysisCaseDefinition(AnalysisCaseDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Analysis Case Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Analysis Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnalysisCaseUsage(AnalysisCaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Annotating Element</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Annotating Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnnotatingElement(AnnotatingElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Annotation</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Annotation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnnotation(Annotation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assert Constraint Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assert Constraint Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssertConstraintUsage(AssertConstraintUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assignment Action Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assignment Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssignmentActionUsage(AssignmentActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Association</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Association</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssociation(Association object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Association Structure</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Association Structure</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssociationStructure(AssociationStructure object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeDefinition(AttributeDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeUsage(AttributeUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Behavior</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Behavior</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBehavior(Behavior object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Binding Connector</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Binding Connector</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBindingConnector(BindingConnector object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Binding Connector As Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Binding Connector As Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBindingConnectorAsUsage(BindingConnectorAsUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Boolean Expression</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Boolean Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBooleanExpression(BooleanExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Calculation Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Calculation Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCalculationDefinition(CalculationDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Calculation Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Calculation Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCalculationUsage(CalculationUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case Definition</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseDefinition(CaseDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case Usage</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseUsage(CaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Class</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Class</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseClass(org.eclipse.syson.sysml.Class object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Classifier</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Classifier</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseClassifier(Classifier object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Collect Expression</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Collect Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCollectExpression(CollectExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Comment</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Comment</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComment(Comment object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Concern Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Concern Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConcernDefinition(ConcernDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Concern Usage</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Concern Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConcernUsage(ConcernUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conjugated Port Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conjugated Port Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConjugatedPortDefinition(ConjugatedPortDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conjugated Port Typing</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conjugated Port Typing</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConjugatedPortTyping(ConjugatedPortTyping object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conjugation</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conjugation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConjugation(Conjugation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connection Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connection Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectionDefinition(ConnectionDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connection Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connection Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectionUsage(ConnectionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connector</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connector</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnector(Connector object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connector As Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connector As Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectorAsUsage(ConnectorAsUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constraint Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constraint Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstraintDefinition(ConstraintDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constraint Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constraint Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstraintUsage(ConstraintUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Control Node</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Control Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseControlNode(ControlNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Type</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataType(DataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Decision Node</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Decision Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDecisionNode(DecisionNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Definition</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDefinition(Definition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dependency</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dependency</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDependency(Dependency object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Differencing</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Differencing</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDifferencing(Differencing object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Disjoining</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Disjoining</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDisjoining(Disjoining object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Documentation</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Documentation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDocumentation(Documentation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElement(Element object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element Filter Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element Filter Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElementFilterMembership(ElementFilterMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Feature Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Feature Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndFeatureMembership(EndFeatureMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enumeration Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enumeration Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumerationDefinition(EnumerationDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enumeration Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enumeration Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumerationUsage(EnumerationUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Event Occurrence Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Event Occurrence Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEventOccurrenceUsage(EventOccurrenceUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Exhibit State Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Exhibit State Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExhibitStateUsage(ExhibitStateUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Expose</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Expose</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExpose(Expose object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Expression</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExpression(Expression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeature(Feature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Chain Expression</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Chain Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureChainExpression(FeatureChainExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Chaining</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Chaining</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureChaining(FeatureChaining object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Inverting</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Inverting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureInverting(FeatureInverting object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureMembership(FeatureMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Reference Expression</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Reference Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureReferenceExpression(FeatureReferenceExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Typing</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Typing</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureTyping(FeatureTyping object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Value</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Value</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureValue(FeatureValue object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Featuring</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Featuring</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeaturing(Featuring object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Flow Connection Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Flow Connection Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFlowConnectionDefinition(FlowConnectionDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Flow Connection Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Flow Connection Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFlowConnectionUsage(FlowConnectionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Fork Node</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Fork Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForkNode(ForkNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>For Loop Action Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>For Loop Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForLoopActionUsage(ForLoopActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Framed Concern Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Framed Concern Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFramedConcernMembership(FramedConcernMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunction(Function object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Action Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfActionUsage(IfActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Import</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Import</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseImport(Import object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Include Use Case Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Include Use Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIncludeUseCaseUsage(IncludeUseCaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interaction</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interaction</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInteraction(Interaction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interface Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interface Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInterfaceDefinition(InterfaceDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interface Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interface Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInterfaceUsage(InterfaceUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Intersecting</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Intersecting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIntersecting(Intersecting object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Invariant</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Invariant</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInvariant(Invariant object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Invocation Expression</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Invocation Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInvocationExpression(InvocationExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Definition</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemDefinition(ItemDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Feature</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemFeature(ItemFeature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Flow</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Flow</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemFlow(ItemFlow object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Flow End</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Flow End</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemFlowEnd(ItemFlowEnd object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Usage</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemUsage(ItemUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Join Node</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Join Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseJoinNode(JoinNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Library Package</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Library Package</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLibraryPackage(LibraryPackage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Life Class</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Life Class</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLifeClass(LifeClass object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Boolean</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Boolean</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralBoolean(LiteralBoolean object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Expression</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralExpression(LiteralExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Infinity</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Infinity</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralInfinity(LiteralInfinity object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Integer</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Integer</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralInteger(LiteralInteger object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Rational</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Rational</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralRational(LiteralRational object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal String</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal String</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralString(LiteralString object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Loop Action Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Loop Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLoopActionUsage(LoopActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Membership</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMembership(Membership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Membership Expose</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Membership Expose</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMembershipExpose(MembershipExpose object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Membership Import</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Membership Import</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMembershipImport(MembershipImport object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Merge Node</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Merge Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMergeNode(MergeNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metaclass</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metaclass</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetaclass(Metaclass object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Access Expression</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Access Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataAccessExpression(MetadataAccessExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataDefinition(MetadataDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Feature</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataFeature(MetadataFeature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataUsage(MetadataUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multiplicity</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multiplicity</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiplicity(Multiplicity object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multiplicity Range</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multiplicity Range</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiplicityRange(MultiplicityRange object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namespace</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namespace</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamespace(Namespace object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namespace Expose</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namespace Expose</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamespaceExpose(NamespaceExpose object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namespace Import</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namespace Import</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamespaceImport(NamespaceImport object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Null Expression</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Null Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNullExpression(NullExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Objective Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Objective Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseObjectiveMembership(ObjectiveMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Occurrence Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Occurrence Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOccurrenceDefinition(OccurrenceDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Occurrence Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Occurrence Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOccurrenceUsage(OccurrenceUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operator Expression</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operator Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperatorExpression(OperatorExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Owning Membership</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Owning Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOwningMembership(OwningMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Package</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Package</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePackage(org.eclipse.syson.sysml.Package object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parameter Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parameter Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParameterMembership(ParameterMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Part Definition</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Part Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePartDefinition(PartDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Part Usage</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Part Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePartUsage(PartUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Perform Action Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Perform Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePerformActionUsage(PerformActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Port Conjugation</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Port Conjugation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePortConjugation(PortConjugation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Port Definition</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Port Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePortDefinition(PortDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Port Usage</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Port Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePortUsage(PortUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Predicate</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Predicate</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePredicate(Predicate object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Redefinition</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Redefinition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRedefinition(Redefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reference Subsetting</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reference Subsetting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReferenceSubsetting(ReferenceSubsetting object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reference Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reference Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReferenceUsage(ReferenceUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Relationship</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRelationship(Relationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rendering Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rendering Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRenderingDefinition(RenderingDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rendering Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rendering Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRenderingUsage(RenderingUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement Constraint Membership</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement Constraint Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirementConstraintMembership(RequirementConstraintMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirementDefinition(RequirementDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirementUsage(RequirementUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement Verification Membership</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement Verification Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirementVerificationMembership(RequirementVerificationMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Result Expression Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Result Expression Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseResultExpressionMembership(ResultExpressionMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Return Parameter Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Return Parameter Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReturnParameterMembership(ReturnParameterMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Satisfy Requirement Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Satisfy Requirement Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSatisfyRequirementUsage(SatisfyRequirementUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Select Expression</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Select Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSelectExpression(SelectExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Send Action Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Send Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSendActionUsage(SendActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Specialization</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Specialization</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecialization(Specialization object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Stakeholder Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Stakeholder Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStakeholderMembership(StakeholderMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>State Definition</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>State Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStateDefinition(StateDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>State Subaction Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>State Subaction Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStateSubactionMembership(StateSubactionMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>State Usage</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>State Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStateUsage(StateUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Step</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Step</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStep(Step object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Structure</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Structure</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStructure(Structure object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Subclassification</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Subclassification</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSubclassification(Subclassification object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Subject Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Subject Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSubjectMembership(SubjectMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Subsetting</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Subsetting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSubsetting(Subsetting object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Succession</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Succession</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSuccession(Succession object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Succession As Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Succession As Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSuccessionAsUsage(SuccessionAsUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Succession Flow Connection Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Succession Flow Connection Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSuccessionFlowConnectionUsage(SuccessionFlowConnectionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Succession Item Flow</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Succession Item Flow</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSuccessionItemFlow(SuccessionItemFlow object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Terminate Action Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Terminate Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTerminateActionUsage(TerminateActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Textual Representation</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Textual Representation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextualRepresentation(TextualRepresentation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Transition Feature Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Transition Feature Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTransitionFeatureMembership(TransitionFeatureMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Transition Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Transition Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTransitionUsage(TransitionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Trigger Invocation Expression</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Trigger Invocation Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTriggerInvocationExpression(TriggerInvocationExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseType(Type object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type Featuring</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type Featuring</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTypeFeaturing(TypeFeaturing object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unioning</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unioning</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnioning(Unioning object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Usage</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUsage(Usage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Use Case Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Use Case Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUseCaseDefinition(UseCaseDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Use Case Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Use Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUseCaseUsage(UseCaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Variant Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Variant Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVariantMembership(VariantMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Verification Case Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Verification Case Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerificationCaseDefinition(VerificationCaseDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Verification Case Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Verification Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerificationCaseUsage(VerificationCaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>View Definition</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>View Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewDefinition(ViewDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Viewpoint Definition</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Viewpoint Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewpointDefinition(ViewpointDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Viewpoint Usage</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Viewpoint Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewpointUsage(ViewpointUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>View Rendering Membership</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>View Rendering Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewRenderingMembership(ViewRenderingMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>View Usage</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>View Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewUsage(ViewUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>While Loop Action Usage</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>While Loop Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWhileLoopActionUsage(WhileLoopActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EModel Element</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EModel Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEModelElement(EModelElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!-- begin-user-doc --> This
     * implementation returns null; returning a non-null result will terminate the switch, but this is the last case
     * anyway. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} // SysmlSwitch
