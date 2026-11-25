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
package org.eclipse.syson.sysml.services;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * Import-compatible name for the shared SysML metamodel copier.
 *
 * @author arichard
 */
public class SysMLMetamodelCopier extends org.eclipse.syson.sysml.metamodel.services.textual.SysMLMetamodelCopier {

    private static final long serialVersionUID = 1L;

    /** Creates a copier with the existing external reference mappings. */
    public SysMLMetamodelCopier(EPackage sourcePackage, EPackage targetPackage, Map<EObject, EObject> referenceTargets) {
        super(sourcePackage, targetPackage, referenceTargets);
    }
}
