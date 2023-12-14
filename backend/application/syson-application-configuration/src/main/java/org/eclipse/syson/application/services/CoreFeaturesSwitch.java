/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Specialization;
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
    public List<EStructuralFeature> caseElement(Element object) {
        var features = new ArrayList<EStructuralFeature>();
        features.add(SysmlPackage.eINSTANCE.getElement_DeclaredName());
        features.add(SysmlPackage.eINSTANCE.getElement_QualifiedName());
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
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getLiteralBoolean_Value());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseLiteralInteger(LiteralInteger object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getLiteralInteger_Value());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseLiteralRational(LiteralRational object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getLiteralRational_Value());
        return features;
    }

    @Override
    public List<EStructuralFeature> caseLiteralString(LiteralString object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getLiteralString_Value());
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
    public List<EStructuralFeature> caseSpecialization(Specialization object) {
        var features = new ArrayList<EStructuralFeature>();
        features.addAll(this.caseElement(object));
        features.add(SysmlPackage.eINSTANCE.getSpecialization_General());
        features.add(SysmlPackage.eINSTANCE.getSpecialization_Specific());
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
