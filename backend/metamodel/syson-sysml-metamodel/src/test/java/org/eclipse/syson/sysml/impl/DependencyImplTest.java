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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.Test;

/**
 * {@link DependencyImpl} related tests.
 *
 * @author arichard
 */
public class DependencyImplTest {

    @Test
    public void testSource() {
        AttributeDefinition attDef = SysmlFactory.eINSTANCE.createAttributeDefinition();
        List<Element> mySources = new BasicEList<>();
        mySources.add(attDef);
        Dependency dependency = SysmlFactory.eINSTANCE.createDependency();
        assertTrue(dependency.getSource().isEmpty());
        assertTrue(dependency.getClient().isEmpty());
        dependency.eSet(SysmlPackage.eINSTANCE.getRelationship_Source(), mySources);
        assertFalse(dependency.getSource().isEmpty());
        assertFalse(dependency.getClient().isEmpty());
    }

    @Test
    public void testTarget() {
        AttributeDefinition attDef = SysmlFactory.eINSTANCE.createAttributeDefinition();
        List<Element> myTargets = new BasicEList<>();
        myTargets.add(attDef);
        Dependency dependency = SysmlFactory.eINSTANCE.createDependency();
        assertTrue(dependency.getTarget().isEmpty());
        assertTrue(dependency.getSupplier().isEmpty());
        dependency.eSet(SysmlPackage.eINSTANCE.getRelationship_Target(), myTargets);
        assertFalse(dependency.getTarget().isEmpty());
        assertFalse(dependency.getSupplier().isEmpty());
    }
}
