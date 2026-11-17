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
package org.eclipse.syson.sysml.linker.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.linker.MapBasedReferenceSetChecker;
import org.eclipse.syson.sysml.linker.QualifiedName;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests {@link MapBasedReferenceSetChecker}.
 *
 * @author Arthur Daussy
 */
public class MapBasedReferenceSetCheckerTests {

    private static final EReference REFERENCE = SysMLPackage.Literals.REFERENCE_SUBSETTING__REFERENCED_FEATURE;

    /**
     * Installs OMG logic delegates before tests create SysML elements.
     */
    @BeforeClass
    public static void beforeClass() {
        SysMLLogicStandaloneSetup.doSetup();
    }

    @Test
    public void qualifiedNameMarksAnUnsetReferenceAsSet() {
        ReferenceSubsetting object = SysMLFactory.eINSTANCE.createReferenceSubsetting();
        Map<EObject, Map<EReference, Set<QualifiedName>>> qualifiedNames = new LinkedHashMap<>();
        qualifiedNames.computeIfAbsent(object, unused -> new LinkedHashMap<>())
                .computeIfAbsent(REFERENCE, unused -> new LinkedHashSet<>())
                .add(new QualifiedName("explicitReference"));

        assertTrue(new MapBasedReferenceSetChecker(qualifiedNames).isSet(object, REFERENCE));
    }

    @Test
    public void fallsBackToUnresolvedEObjectValue() {
        ReferenceSubsetting object = SysMLFactory.eINSTANCE.createReferenceSubsetting();
        Feature value = SysMLFactory.eINSTANCE.createFeature();
        object.setReferencedFeature(value);

        assertTrue(new MapBasedReferenceSetChecker(Map.of()).isSet(object, REFERENCE));
    }

    @Test
    public void emptyQualifiedNameSetFallsBackToEObjectValue() {
        ReferenceSubsetting object = SysMLFactory.eINSTANCE.createReferenceSubsetting();
        Map<EObject, Map<EReference, Set<QualifiedName>>> qualifiedNames = Map.of(object, Map.of(REFERENCE, Set.of()));

        assertFalse(new MapBasedReferenceSetChecker(qualifiedNames).isSet(object, REFERENCE));
    }

    @Test
    public void absentQualifiedNameAndValueMeansUnset() {
        ReferenceSubsetting object = SysMLFactory.eINSTANCE.createReferenceSubsetting();

        assertFalse(new MapBasedReferenceSetChecker(Map.of()).isSet(object, REFERENCE));
    }
}
