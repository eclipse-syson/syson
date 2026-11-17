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
package org.eclipse.syson.sysml.linker;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Provides the metamodel {@link EReference} that carries the target of supported SysML relationship subtypes.
 * <p>
 * Imports and specializations expose their targets through references declared by their concrete subtype. This helper
 * centralizes the mapping used by the linker to access those references without resolving derived SysML properties.
 * </p>
 *
 * @author Arthur Daussy
 */
public class SysMLMetamodelHelper {

    /**
     * The {@link Map} from {@link Import} {@link EClass} to its import {@link EReference}.
     */
    public static final Map<EClass, EReference> IMPORT_REFERENCE = Map.ofEntries(
            Map.entry(SysMLPackage.eINSTANCE.getMembershipImport(), SysMLPackage.eINSTANCE.getMembershipImport_ImportedMembership()),
            Map.entry(SysMLPackage.eINSTANCE.getNamespaceImport(), SysMLPackage.eINSTANCE.getNamespaceImport_ImportedNamespace()),
            Map.entry(SysMLPackage.eINSTANCE.getMembershipExpose(), SysMLPackage.eINSTANCE.getMembershipImport_ImportedMembership()),
            Map.entry(SysMLPackage.eINSTANCE.getNamespaceExpose(), SysMLPackage.eINSTANCE.getNamespaceImport_ImportedNamespace()));

    /**
     * The {@link Map} from {@link Specialization} {@link EClass} to its specialize {@link EReference}.
     */
    public static final Map<EClass, EReference> SPECIALIZATION_REFERENCE = Map.ofEntries(
            Map.entry(SysMLPackage.eINSTANCE.getFeatureTyping(), SysMLPackage.eINSTANCE.getFeatureTyping_Type()),
            Map.entry(SysMLPackage.eINSTANCE.getSubclassification(), SysMLPackage.eINSTANCE.getSubclassification_Superclassifier()),
            Map.entry(SysMLPackage.eINSTANCE.getSubsetting(), SysMLPackage.eINSTANCE.getSubsetting_SubsettedFeature()),
            Map.entry(SysMLPackage.eINSTANCE.getCrossSubsetting(), SysMLPackage.eINSTANCE.getCrossSubsetting_CrossedFeature()),
            Map.entry(SysMLPackage.eINSTANCE.getRedefinition(), SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature()),
            Map.entry(SysMLPackage.eINSTANCE.getReferenceSubsetting(), SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature()),
            Map.entry(SysMLPackage.eINSTANCE.getConjugatedPortTyping(), SysMLPackage.eINSTANCE.getConjugatedPortTyping_ConjugatedPortDefinition()));

    /**
     * Gets the concrete target {@link EReference} for the given {@link Import}.
     *
     * @param imp
     *            the import whose target reference is requested
     * @return the target {@link EReference}, or <code>null</code> if the import's concrete {@link EClass} is not
     *         supported
     */
    public EReference getImportFeature(Import imp) {
        return IMPORT_REFERENCE.get(imp.eClass());
    }

    /**
     * Gets the concrete target {@link EReference} for the given {@link Specialization}.
     *
     * @param specialization
     *            the specialization whose target reference is requested
     * @return the target {@link EReference}, or <code>null</code> if the specialization's concrete {@link EClass} is
     *         not supported
     */
    public EReference getSpecializationFeature(Specialization specialization) {
        return SPECIALIZATION_REFERENCE.get(specialization.eClass());
    }
}
