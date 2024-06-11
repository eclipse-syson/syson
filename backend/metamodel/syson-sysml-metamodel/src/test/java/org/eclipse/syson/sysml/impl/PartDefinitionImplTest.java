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

import static org.eclipse.syson.sysml.util.TestUtils.assertContentEquals;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link PartDefinitionImpl}.
 * 
 * @author Arthur Daussy
 */
public class PartDefinitionImplTest {

    private ModelBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new ModelBuilder();
    }

    @Test
    public void testGetInheritedMembers() {

        Package p1 = builder.createWithName(Package.class, "p1");
        
        /**
         * <pre>
         *   part def superDef {
         *      private attribute privateAttrSuperDef;
         *      protected attribute protecteAttrSuperDef;
         *      attribute publicAttrSuperDef;
         *  }
         *
         *  part def subDef;
         * </pre>
         */

        PartDefinition superDef = builder.createInWithName(PartDefinition.class, p1, "superDef");
        AttributeUsage publicAttrSuperDef = builder.createInWithName(AttributeUsage.class, superDef, "publicAttrSuperDef");
        AttributeUsage privateAttrSuperDef = builder.createInWithName(AttributeUsage.class, superDef, "privateAttrSuperDef");
        privateAttrSuperDef.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
        AttributeUsage protectedAttrSuperDef = builder.createInWithName(AttributeUsage.class, superDef, "protectedAttrSuperDef");
        protectedAttrSuperDef.getOwningMembership().setVisibility(VisibilityKind.PROTECTED);

        PartDefinition subDef = builder.createInWithName(PartDefinition.class, p1, "subDef");
        builder.addSuperType(subDef, superDef);

        assertContentEquals(subDef.getInheritedMembership(), publicAttrSuperDef.getOwningMembership(), protectedAttrSuperDef.getOwningMembership());
        assertContentEquals(subDef.inheritedMemberships(new BasicEList<>()), publicAttrSuperDef.getOwningMembership(), protectedAttrSuperDef.getOwningMembership());
        assertContentEquals(subDef.getInheritedFeature(), publicAttrSuperDef, protectedAttrSuperDef);

        assertContentEquals(superDef.getInheritedMembership());
        assertContentEquals(superDef.inheritedMemberships(new BasicEList<>()));
        assertContentEquals(superDef.getInheritedFeature());

    }

}
