/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch for retrieving all data needed to create children for a given Element container.
 *
 * @author arichard
 */
public class GetChildCreationSwitch extends SysmlEClassSwitch<List<EClass>> {

    @Override
    public List<EClass> defaultCase(EObject object) {
        return List.of();
    }

    @Override
    public List<EClass> caseActionUsage(ActionUsage object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        childrenCandidates.add(SysmlPackage.eINSTANCE.getActionUsage());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getItemUsage());
        childrenCandidates.addAll(this.caseUsage(object));
        return childrenCandidates;
    }

    @Override
    public List<EClass> caseAttributeUsage(AttributeUsage object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        childrenCandidates.add(SysmlPackage.eINSTANCE.getReferenceUsage());
        childrenCandidates.addAll(this.caseUsage(object));
        return childrenCandidates;
    }

    @Override
    public List<EClass> caseDefinition(Definition object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        SysmlPackage.eINSTANCE.getEClassifiers().stream()
            .filter(EClass.class::isInstance)
            .map(EClass.class::cast)
            .filter(eClass -> {
                boolean authorizedClasses = SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass) || SysmlPackage.eINSTANCE.getImport().isSuperTypeOf(eClass);
                return !eClass.isAbstract() && !eClass.isInterface() && authorizedClasses;
            })
            .forEach(eClass -> {
                childrenCandidates.add(eClass);
            });
        childrenCandidates.add(SysmlPackage.eINSTANCE.getSubclassification());
        return childrenCandidates;
    }

    @Override
    public List<EClass> caseInterfaceUsage(InterfaceUsage object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        childrenCandidates.add(SysmlPackage.eINSTANCE.getPortUsage());
        childrenCandidates.addAll(this.caseUsage(object));
        return childrenCandidates;
    }

    @Override
    public List<EClass> caseItemUsage(ItemUsage object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        childrenCandidates.add(SysmlPackage.eINSTANCE.getReferenceUsage());
        childrenCandidates.addAll(this.caseUsage(object));
        return childrenCandidates;
    }

    @Override
    public List<EClass> caseEnumerationDefinition(EnumerationDefinition object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        childrenCandidates.add(SysmlPackage.eINSTANCE.getEnumerationUsage());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getSubclassification());
        return childrenCandidates;
    }

    @Override
    public List<EClass> caseFeatureMembership(FeatureMembership object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        SysmlPackage.eINSTANCE.getEClassifiers().stream()
            .filter(EClass.class::isInstance)
            .map(EClass.class::cast)
            .filter(eClass -> {
                boolean authorizedClasses = SysmlPackage.eINSTANCE.getFeature().isSuperTypeOf(eClass);
                return !eClass.isAbstract() && !eClass.isInterface() && authorizedClasses;
            })
            .forEach(eClass -> {
                childrenCandidates.add(eClass);
            });
        return childrenCandidates;
    }

    @Override
    public List<EClass> casePackage(Package object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        SysmlPackage.eINSTANCE.getEClassifiers().stream()
            .filter(EClass.class::isInstance)
            .map(EClass.class::cast)
            .filter(eClass -> {
                boolean authorizedClasses = SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(eClass) || SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass) || SysmlPackage.eINSTANCE.getImport().isSuperTypeOf(eClass);
                return !eClass.isAbstract() && !eClass.isInterface() && authorizedClasses;
            })
            .forEach(eClass -> {
                childrenCandidates.add(eClass);
            });
        return childrenCandidates;
    }

    @Override
    public List<EClass> casePartUsage(PartUsage object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        childrenCandidates.add(SysmlPackage.eINSTANCE.getPartUsage());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getPortUsage());
        childrenCandidates.addAll(this.caseUsage(object));
        return childrenCandidates;
    }

    @Override
    public List<EClass> casePortUsage(PortUsage object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        childrenCandidates.add(SysmlPackage.eINSTANCE.getReferenceUsage());
        childrenCandidates.addAll(this.caseUsage(object));
        return childrenCandidates;
    }

    @Override
    public List<EClass> caseUsage(Usage object) {
        List<EClass> childrenCandidates = new ArrayList<>();
        childrenCandidates.add(SysmlPackage.eINSTANCE.getAttributeUsage());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getFeatureTyping());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getSubsetting());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getRedefinition());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getLiteralBoolean());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getLiteralInfinity());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getLiteralInteger());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getLiteralRational());
        childrenCandidates.add(SysmlPackage.eINSTANCE.getLiteralString());
        return childrenCandidates;
    }
}
