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
package org.eclipse.syson.application.index;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.SysmlSwitch;


/**
 * TODO
 *
 * @author gdaniel
 */
public class NestedIndexEntrySwitch extends SysmlSwitch<Optional<INestedIndexEntry>> {

    private final IIdentityService identityService;

    private final ILabelService labelService;

    public NestedIndexEntrySwitch(IIdentityService identityService, ILabelService labelService) {
        this.identityService = Objects.requireNonNull(identityService);
        this.labelService = Objects.requireNonNull(labelService);
    }

    @Override
    public Optional<INestedIndexEntry> caseElement(Element element) {
        NestedElementIndexEntry result = null;
        String objectId = this.identityService.getId(element);
        String type = this.getEClassifierName(element.eClass());
        String label = this.labelService.getStyledLabel(element).toString();

        String name = element.getName();
        String shortName = element.getShortName();
        String qualifiedName = element.getQualifiedName();

        return Optional.of(new NestedElementIndexEntry(objectId, type, label, name, shortName, qualifiedName));
    }

    @Override
    public Optional<INestedIndexEntry> caseSpecialization(Specialization specialization) {
        String objectId = this.identityService.getId(specialization);
        String type = this.getEClassifierName(specialization.eClass());
        String label = this.labelService.getStyledLabel(specialization).toString();

        String name = specialization.getName();
        String shortName = specialization.getShortName();
        String qualifiedName = specialization.getQualifiedName();

        Optional<INestedIndexEntry> general = this.doSwitch(specialization.getGeneral());

        return Optional.of(new NestedSpecializationIndexEntry(objectId, type, label, name, shortName, qualifiedName, general.orElse(null)));
    }

    @Override
    public Optional<INestedIndexEntry> defaultCase(EObject object) {
        return Optional.empty();
    }

    @Override
    public Optional<INestedIndexEntry> doSwitch(EObject eObject) {
        Optional<INestedIndexEntry> result = Optional.empty();
        if (eObject != null) {
            result = super.doSwitch(eObject);
        }
        return result;
    }

    private String getEClassifierName(EClassifier eClassifier) {
        String name = eClassifier.getName();
        if (eClassifier instanceof EClass eClass) {
            if (SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass)
                    && !SysmlPackage.eINSTANCE.getConnectorAsUsage().equals(eClass)
                    && !SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().equals(eClass)
                    && !SysmlPackage.eINSTANCE.getSuccessionAsUsage().equals(eClass)) {
                if (eClass.getName().endsWith("Usage")) {
                    name = eClass.getName().substring(0, eClass.getName().length() - 5);
                }
            }
        }
        return name;
    }
}
