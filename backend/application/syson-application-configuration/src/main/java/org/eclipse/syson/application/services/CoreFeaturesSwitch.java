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
package org.eclipse.syson.application.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PortConjugation;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch for retrieving the core features of a SysML element.
 *
 * @author arichard
 */
public class CoreFeaturesSwitch extends SysmlSwitch<List<EStructuralFeature>> {

    @Override
    public List<EStructuralFeature> defaultCase(EObject object) {
        return List.of();
    }

    @Override
    public List<EStructuralFeature> caseComment(Comment object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getComment_Body());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseDependency(Dependency object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getDependency_Client());
        features.add(SysmlPackage.eINSTANCE.getDependency_Supplier());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseElement(Element object) {
        var features = new ArrayList<EStructuralFeature>();
        features.add(SysmlPackage.eINSTANCE.getElement_DeclaredName());
        features.add(SysmlPackage.eINSTANCE.getElement_QualifiedName());
        features.add(SysmlPackage.eINSTANCE.getElement_DeclaredShortName());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseFeature(Feature object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getFeature_Direction());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseFeatureTyping(FeatureTyping object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseSpecialization(object));
        features.add(SysmlPackage.eINSTANCE.getFeatureTyping_Type());
        features.add(SysmlPackage.eINSTANCE.getFeatureTyping_TypedFeature());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseLiteralBoolean(LiteralBoolean object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseFeature(object));
        features.add(SysmlPackage.eINSTANCE.getLiteralBoolean_Value());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseLiteralInteger(LiteralInteger object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseFeature(object));
        features.add(SysmlPackage.eINSTANCE.getLiteralInteger_Value());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseLiteralRational(LiteralRational object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseFeature(object));
        features.add(SysmlPackage.eINSTANCE.getLiteralRational_Value());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseLiteralString(LiteralString object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseFeature(object));
        features.add(SysmlPackage.eINSTANCE.getLiteralString_Value());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseMembership(Membership object) {
        var features = new ArrayList<EStructuralFeature>();
        features.add(SysmlPackage.eINSTANCE.getMembership_Visibility());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseMembershipImport(MembershipImport object) {
        var features = new ArrayList<EStructuralFeature>();
        features.add(SysmlPackage.eINSTANCE.getMembershipImport_ImportedMembership());
        features.add(SysmlPackage.eINSTANCE.getImport_IsImportAll());
        features.add(SysmlPackage.eINSTANCE.getImport_IsRecursive());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseNamespaceImport(NamespaceImport object) {
        var features = new ArrayList<EStructuralFeature>();
        features.add(SysmlPackage.eINSTANCE.getNamespaceImport_ImportedNamespace());
        features.add(SysmlPackage.eINSTANCE.getImport_IsImportAll());
        features.add(SysmlPackage.eINSTANCE.getImport_IsRecursive());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseOccurrenceDefinition(OccurrenceDefinition object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getOccurrenceDefinition_IsIndividual());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseOccurrenceUsage(OccurrenceUsage object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseFeature(object));
        features.add(SysmlPackage.eINSTANCE.getOccurrenceUsage_IsIndividual());
        return features;
    }

    @Override
    public List<EStructuralFeature> casePortConjugation(PortConjugation object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getConjugation_ConjugatedType());
        features.add(SysmlPackage.eINSTANCE.getPortConjugation_OriginalPortDefinition());
        return features;
    }

    @Override
    public List<EStructuralFeature> casePortUsage(PortUsage object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseFeature(object));
        return features;
    }

    @Override
    public List<EStructuralFeature> caseRedefinition(Redefinition object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseSubsetting(object));
        features.add(SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature());
        features.add(SysmlPackage.eINSTANCE.getRedefinition_RedefiningFeature());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseReferenceSubsetting(ReferenceSubsetting object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature());
        features.add(SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencingFeature());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseRequirementConstraintMembership(RequirementConstraintMembership object) {
        var features = new ArrayList<EStructuralFeature>();
        features.add(SysmlPackage.eINSTANCE.getMembership_Visibility());
        features.add(SysmlPackage.eINSTANCE.getRequirementConstraintMembership_Kind());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseSpecialization(Specialization object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getSpecialization_General());
        features.add(SysmlPackage.eINSTANCE.getSpecialization_Specific());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseStateDefinition(StateDefinition object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseOccurrenceDefinition(object));
        features.add(SysmlPackage.eINSTANCE.getStateDefinition_IsParallel());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseStateSubactionMembership(StateSubactionMembership object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseMembership(object));
        features.add(SysmlPackage.eINSTANCE.getStateSubactionMembership_Kind());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseStateUsage(StateUsage object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseFeature(object));
        features.add(SysmlPackage.eINSTANCE.getStateUsage_IsParallel());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseSubclassification(Subclassification object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseSpecialization(object));
        features.add(SysmlPackage.eINSTANCE.getSubclassification_Subclassifier());
        features.add(SysmlPackage.eINSTANCE.getSubclassification_Superclassifier());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseSubsetting(Subsetting object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature());
        features.add(SysmlPackage.eINSTANCE.getSubsetting_SubsettingFeature());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseType(Type object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getType_IsAbstract());
        return features;
    }
}
