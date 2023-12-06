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
package org.eclipse.syson.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Helper class allowing to convert a String into a Sysml metamodel EClass.
 * 
 * @author arichard
 */
public final class SysMLMetamodelHelper {

    private SysMLMetamodelHelper() {
    }

    /**
     * Build the qualified name of the given {@link EClass} (e.g.: ePackageName::eClassName).
     *
     * @param eClass
     *            an {@link EClass}
     * @return the qualified name
     */
    public static String buildQualifiedName(EClass eClass) {
        return eClass.getEPackage().getName() + "::" + eClass.getName();
    }

    /**
     * Get the {@link EClass} from the {@link SysmlPackage} using the simple or qualified
     * name ("Package" vs "sysml::Package").
     *
     * @param type
     *            the searched type
     * @return a {@link EClass} or <code>null</code>
     */
    public static EClass toEClass(String type) {
        if (type != null && type.startsWith("sysml::")) {
            return toEClass(type.replace("sysml::", "")); //$NON-NLS-1$
        }
        final EClass eClass;
        EClassifier classifier = SysmlPackage.eINSTANCE.getEClassifier(type);
        if (classifier instanceof EClass) {
            eClass = (EClass) classifier;
        } else {
            eClass = null;
        }
        return eClass;
    }
}
