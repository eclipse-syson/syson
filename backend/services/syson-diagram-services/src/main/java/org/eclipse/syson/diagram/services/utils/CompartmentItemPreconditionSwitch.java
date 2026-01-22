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
package org.eclipse.syson.diagram.services.utils;

import java.util.Objects;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch returning a string to answer the precondition expression of compartment items.
 *
 * @author arichard
 */
public class CompartmentItemPreconditionSwitch extends SysmlEClassSwitch<String> {

    private final EReference eReference;

    public CompartmentItemPreconditionSwitch(EReference eReference) {
        this.eReference = Objects.requireNonNull(eReference);
    }

    @Override
    public String defaultCase(EObject object) {
        EClassifier eType = this.eReference.getEType();
        var qualifiedName = SysMLMetamodelHelper.buildQualifiedName(eType);
        String preconditionExpression = AQLConstants.AQL_SELF + ".oclIsTypeOf(" + qualifiedName + ")";
        boolean isAbstract = false;
        if (Objects.equals(SysmlPackage.eINSTANCE.getUsage(), eType)) {
            isAbstract = true;
        } else if (Objects.equals(SysmlPackage.eINSTANCE.getDefinition(), eType)) {
            isAbstract = true;
        } else if (Objects.equals(SysmlPackage.eINSTANCE.getFeature(), eType)) {
            isAbstract = true;
        } else if (Objects.equals(SysmlPackage.eINSTANCE.getType(), eType)) {
            isAbstract = true;
        }
        if (isAbstract) {
            preconditionExpression = AQLConstants.AQL_SELF + ".oclIsKindOf(" + qualifiedName + ")";
        }
        return preconditionExpression;
    }
}
