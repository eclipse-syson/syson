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

import static org.eclipse.syson.sysml.util.TestUtils.testInheritedFeature;

import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PartUsage}.
 * 
 * @author Arthur Daussy
 */
public class PartUsageImplTest {

    /**
     * <pre>
     * part def typeDef {
            attribute publicTypeDefAttribute;
            private attribute privateTypeDefAttribute;
            protected attribute protectedTypeDefAttribute;
        }
    
        part superPart : typeDef {
            attribute publicSuperPartAttribute;
            private attribute privateSuperPartAttribute;
            protected attribute protectedSuperPartAttribute;
            part containedPart{
    
            }
        }
    
        part subPart :> superPart {
            attribute publicSubPartAttribute;
            private attribute privateSubPartAttribute;
            protected attribute protectedSubPartAttribute;
        }
     * </pre>
     */

    private static class TestModel {

        private ModelBuilder builder = new ModelBuilder();

        private PartDefinition typeDef;

        private AttributeUsage publicTypeDefAttribute;

        private AttributeUsage privateTypeDefAttribute;

        private AttributeUsage protectedTypeDefAttribute;

        private PartUsage superPart;

        private AttributeUsage publicSuperPartAttribute;

        private AttributeUsage privateSuperPartAttribute;

        private AttributeUsage protectedSuperPartAttribute;

        private PartUsage containedPart;

        private PartUsage subPart;

        private AttributeUsage publicSubPartAttribute;

        private AttributeUsage protectedSubPartAttribute;

        private AttributeUsage privateSubPartAttribute;

        TestModel() {
            build();
        }

        private void build() {

            typeDef = builder.createWithName(PartDefinition.class, "typeDef");
            publicTypeDefAttribute = builder.createInWithName(AttributeUsage.class, typeDef, "publicTypeDefAttribute");
            privateTypeDefAttribute = builder.createInWithName(AttributeUsage.class, typeDef, "privateTypeDefAttribute");
            privateTypeDefAttribute.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
            protectedTypeDefAttribute = builder.createInWithName(AttributeUsage.class, typeDef, "protectedTypeDefAttribute");
            protectedTypeDefAttribute.getOwningMembership().setVisibility(VisibilityKind.PROTECTED);

            superPart = builder.createWithName(PartUsage.class, "superPart");
            publicSuperPartAttribute = builder.createInWithName(AttributeUsage.class, superPart, "publicSuperPartAttribute");
            privateSuperPartAttribute = builder.createInWithName(AttributeUsage.class, superPart, "privateSuperPartAttribute");
            privateSuperPartAttribute.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
            protectedSuperPartAttribute = builder.createInWithName(AttributeUsage.class, superPart, "protectedSuperPartAttribute");
            protectedSuperPartAttribute.getOwningMembership().setVisibility(VisibilityKind.PROTECTED);
            builder.setType(superPart, typeDef);

            containedPart = builder.createInWithName(PartUsage.class, superPart, "containedPart");

            subPart = builder.createWithName(PartUsage.class, "subPart");
            publicSubPartAttribute = builder.createInWithName(AttributeUsage.class, subPart, "publicSubPartAttribute");
            privateSubPartAttribute = builder.createInWithName(AttributeUsage.class, subPart, "privateSubPartAttribute");
            privateSubPartAttribute.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
            protectedSubPartAttribute = builder.createInWithName(AttributeUsage.class, subPart, "protectedSubPartAttribute");
            protectedSubPartAttribute.getOwningMembership().setVisibility(VisibilityKind.PROTECTED);
            builder.addRedefinition(subPart, superPart);
        }
    }

    @Test
    public void testGetInheritedMembers() {

        var testModel = new TestModel();

        testInheritedFeature(testModel.typeDef);
        testInheritedFeature(testModel.superPart, testModel.publicTypeDefAttribute, testModel.protectedTypeDefAttribute);
        testInheritedFeature(testModel.containedPart,
                // Visible from typeDelf
                testModel.publicTypeDefAttribute, testModel.protectedTypeDefAttribute,
                // Visible from owning part
                testModel.publicSuperPartAttribute, testModel.protectedSuperPartAttribute,
                // Itsef since if a feature of its parent
                testModel.containedPart);

        testInheritedFeature(testModel.subPart,
                // Visible from ypeDelf
                testModel.publicTypeDefAttribute, testModel.protectedTypeDefAttribute,
                // Visible from owning part
                testModel.publicSuperPartAttribute, testModel.protectedSuperPartAttribute,
                testModel.containedPart);

    }

}
