/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.application.services.form;

import java.util.Objects;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.form.services.api.IDetailsViewHelpTextProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * Provides custom Help text for the Details view widgets of the features isComposite and isReference.
 *
 * @author mcharfadi
 */
@Service
public class IsCompositeIsReferenceDetailViewHelpTextProvider implements IDetailsViewHelpTextProvider {
    @Override
    public boolean canHandle(Element element, EStructuralFeature eStructuralFeature) {
        return Objects.equals(eStructuralFeature, SysmlPackage.eINSTANCE.getFeature_IsComposite()) ||
                Objects.equals(eStructuralFeature, SysmlPackage.eINSTANCE.getUsage_IsReference());
    }

    @Override
    public String getHelpText(Element element, EStructuralFeature eStructuralFeature) {
        var helpText = "";
        if (Objects.equals(eStructuralFeature, SysmlPackage.eINSTANCE.getFeature_IsComposite())) {
            helpText = "Opposite value of isReference";
        } else if (Objects.equals(eStructuralFeature, SysmlPackage.eINSTANCE.getUsage_IsReference())) {
            helpText = "Opposite value of isComposite (cannot be edited, edit isComposite instead)";
        }
        return helpText;
    }
}
