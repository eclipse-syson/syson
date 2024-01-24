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
package org.eclipse.syson.sysml.metamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.sysml.util.SysmlResourceImpl;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ElementUtil} methods.
 *
 * @author arichard
 */
public class ElementUtilTest {

    /**
     * All expected UUIDs have been produced with https://www.uuidtools.com/v5.
     */
    @Test
    void testGenerateUUIDv5() {
        UUID baseUUID = ElementUtil.generateUUIDv5(ElementUtil.NAME_SPACE_URL_UUID, ElementUtil.KERML_LIBRARY_BASE_URI + "Base");
        assertEquals("cdd5d1e3-fe4b-52bd-8a01-51a53f22ba47", baseUUID.toString());
        UUID scalarValuesUUID = ElementUtil.generateUUIDv5(ElementUtil.NAME_SPACE_URL_UUID, ElementUtil.KERML_LIBRARY_BASE_URI + "ScalarValues");
        assertEquals("40bb440c-5036-58e1-8675-5afccb8b8f1d", scalarValuesUUID.toString());
        UUID isqUUID = ElementUtil.generateUUIDv5(ElementUtil.NAME_SPACE_URL_UUID, ElementUtil.SYSML_LIBRARY_BASE_URI + "ISQ");
        assertEquals("3fcc2ef4-a31c-522b-b69d-717d74bccfa6", isqUUID.toString());
        UUID isqBaseUUID = ElementUtil.generateUUIDv5(ElementUtil.NAME_SPACE_URL_UUID, ElementUtil.SYSML_LIBRARY_BASE_URI + "ISQBase");
        assertEquals("c42ede81-a2db-50ca-9727-40dc60310326", isqBaseUUID.toString());

        UUID scalarValuesBooleanUUID = ElementUtil.generateUUIDv5(scalarValuesUUID, "ScalarValues::Boolean");
        assertEquals("d1e9242d-b2e3-5270-bf69-4f4fb0447193", scalarValuesBooleanUUID.toString());
        UUID isqBaseMassUUID = ElementUtil.generateUUIDv5(isqBaseUUID, "ISQBase::mass");
        assertEquals("9af08108-292b-534a-a527-068c17423dfd", isqBaseMassUUID.toString());
    }

    @Test
    void testGenerateUUID() {
        URI uri = URI.createURI("kermllibrary:///" + UUID.randomUUID());
        Resource emfResource = new SysmlResourceImpl(uri);
        LibraryPackage libraryPackage = SysmlFactory.eINSTANCE.createLibraryPackage();
        emfResource.getContents().add(libraryPackage);
        libraryPackage.setIsStandard(true);
        libraryPackage.setDeclaredName("Base");
        libraryPackage.setElementId(ElementUtil.generateUUID(libraryPackage).toString());
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        libraryPackage.getOwnedRelationship().add(owningMembership);
        Classifier anything = SysmlFactory.eINSTANCE.createClassifier();
        anything.setDeclaredName("Anything");
        owningMembership.getOwnedRelatedElement().add(anything);
        
        UUID generatedUUID = ElementUtil.generateUUID(anything);
        anything.setElementId(generatedUUID.toString());
        assertEquals("d5b4e7df-e644-5f2f-b95e-cf6f1f6c076d", generatedUUID.toString());
        String anythingUUID = anything.getElementId();
        assertEquals("d5b4e7df-e644-5f2f-b95e-cf6f1f6c076d", anythingUUID);
    }
}
