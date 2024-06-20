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
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TypeFeaturing;
import org.junit.jupiter.api.Test;

/**
 * {@link TypeFeaturingImpl} related tests.
 *
 * @author arichard
 */
public class TypeFeaturingImplTest {

    @Test
    public void testSource() {
        AttributeUsage attUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        List<Element> mySources = new BasicEList<>();
        mySources.add(attUsage);
        TypeFeaturing typeFeaturing = SysmlFactory.eINSTANCE.createTypeFeaturing();
        assertTrue(typeFeaturing.getSource().isEmpty());
        assertNull(typeFeaturing.getFeatureOfType());
        typeFeaturing.eSet(SysmlPackage.eINSTANCE.getRelationship_Source(), mySources);
        assertFalse(typeFeaturing.getSource().isEmpty());
        assertNotNull(typeFeaturing.getFeatureOfType());
    }

    @Test
    public void testTarget() {
        AttributeUsage attUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        List<Element> myTargets = new BasicEList<>();
        myTargets.add(attUsage);
        TypeFeaturing typeFeaturing = SysmlFactory.eINSTANCE.createTypeFeaturing();
        assertTrue(typeFeaturing.getTarget().isEmpty());
        assertNull(typeFeaturing.getFeaturingType());
        typeFeaturing.eSet(SysmlPackage.eINSTANCE.getRelationship_Target(), myTargets);
        assertFalse(typeFeaturing.getTarget().isEmpty());
        assertNotNull(typeFeaturing.getFeaturingType());
    }
}
