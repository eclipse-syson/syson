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
package org.eclipse.syson.sysml.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.Test;

/**
 * {@link NamespaceImportImpl} related tests.
 *
 * @author arichard
 */
public class NamespaceImportImplTest {

    @Test
    public void testSource() {
        AttributeUsage attUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        List<Element> mySources = new BasicEList<>();
        mySources.add(attUsage);
        NamespaceImport nsImport = SysmlFactory.eINSTANCE.createNamespaceImport();
        assertTrue(nsImport.getSource().isEmpty());
        assertNull(nsImport.getImportOwningNamespace());
        nsImport.eSet(SysmlPackage.eINSTANCE.getRelationship_Source(), mySources);
        // importOwningNamespace is derived so it must remain null when an element is added to source
        assertTrue(nsImport.getSource().isEmpty());
        assertNull(nsImport.getImportOwningNamespace());
    }

    @Test
    public void testTarget() {
        Namespace namespace = SysmlFactory.eINSTANCE.createNamespace();
        List<Element> myTargets = new BasicEList<>();
        myTargets.add(namespace);
        NamespaceImport nsImport = SysmlFactory.eINSTANCE.createNamespaceImport();
        assertTrue(nsImport.getTarget().isEmpty());
        assertNull(nsImport.getImportedNamespace());
        nsImport.eSet(SysmlPackage.eINSTANCE.getRelationship_Target(), myTargets);
        assertFalse(nsImport.getTarget().isEmpty());
        assertNotNull(nsImport.getImportedNamespace());
    }
}
