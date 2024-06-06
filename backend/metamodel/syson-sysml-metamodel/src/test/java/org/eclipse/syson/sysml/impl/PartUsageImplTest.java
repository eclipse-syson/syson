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

        private final ModelBuilder builder = new ModelBuilder();

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
            this.build();
        }

        private void build() {
            this.typeDef = this.builder.createWithName(PartDefinition.class, "typeDef");
            this.publicTypeDefAttribute = this.builder.createInWithName(AttributeUsage.class, this.typeDef, "publicTypeDefAttribute");
            this.privateTypeDefAttribute = this.builder.createInWithName(AttributeUsage.class, this.typeDef, "privateTypeDefAttribute");
            this.privateTypeDefAttribute.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
            this.protectedTypeDefAttribute = this.builder.createInWithName(AttributeUsage.class, this.typeDef, "protectedTypeDefAttribute");
            this.protectedTypeDefAttribute.getOwningMembership().setVisibility(VisibilityKind.PROTECTED);

            this.superPart = this.builder.createWithName(PartUsage.class, "superPart");
            this.publicSuperPartAttribute = this.builder.createInWithName(AttributeUsage.class, this.superPart, "publicSuperPartAttribute");
            this.privateSuperPartAttribute = this.builder.createInWithName(AttributeUsage.class, this.superPart, "privateSuperPartAttribute");
            this.privateSuperPartAttribute.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
            this.protectedSuperPartAttribute = this.builder.createInWithName(AttributeUsage.class, this.superPart, "protectedSuperPartAttribute");
            this.protectedSuperPartAttribute.getOwningMembership().setVisibility(VisibilityKind.PROTECTED);
            this.builder.setType(this.superPart, this.typeDef);

            this.containedPart = this.builder.createInWithName(PartUsage.class, this.superPart, "containedPart");

            this.subPart = this.builder.createWithName(PartUsage.class, "subPart");
            this.publicSubPartAttribute = this.builder.createInWithName(AttributeUsage.class, this.subPart, "publicSubPartAttribute");
            this.privateSubPartAttribute = this.builder.createInWithName(AttributeUsage.class, this.subPart, "privateSubPartAttribute");
            this.privateSubPartAttribute.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
            this.protectedSubPartAttribute = this.builder.createInWithName(AttributeUsage.class, this.subPart, "protectedSubPartAttribute");
            this.protectedSubPartAttribute.getOwningMembership().setVisibility(VisibilityKind.PROTECTED);
            this.builder.addSubsetting(this.subPart, this.superPart);
        }
    }

    @Test
    public void testGetInheritedMembers() {
        var testModel = new TestModel();

        testInheritedFeature(testModel.typeDef);
        testInheritedFeature(testModel.superPart, testModel.publicTypeDefAttribute, testModel.protectedTypeDefAttribute);
        testInheritedFeature(testModel.containedPart,
                // Visible from typeDef
                testModel.publicTypeDefAttribute, testModel.protectedTypeDefAttribute,
                // Visible from owning part
                testModel.publicSuperPartAttribute, testModel.protectedSuperPartAttribute,
                // Itself since it's a feature of its parent
                testModel.containedPart);

        testInheritedFeature(testModel.subPart,
                // Visible from typeDef
                testModel.publicTypeDefAttribute, testModel.protectedTypeDefAttribute,
                // Visible from owning part
                testModel.publicSuperPartAttribute, testModel.protectedSuperPartAttribute,
                testModel.containedPart);
    }
}
