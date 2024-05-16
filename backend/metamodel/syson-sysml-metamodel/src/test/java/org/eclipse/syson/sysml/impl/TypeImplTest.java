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
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link TypeImpl}.
 *
 * @author Arthur Daussy
 */
public class TypeImplTest  {

    /**
     * Test model
     *
     * <pre>
     * namespace {
     * package p1 {
     *
     *       part def MegaDef {
     *       }
     *
     *       part def SuperSuperDef :> MegaDef{
     *          attribute attr0;
     *       }
     *
     *       part def SuperDerf :> SuperSuperDef{
     *           attribute attr1;
     *           private attribute privateAttr;
     *           protected attribute protectedAttr;
     *       }
     *
     *       part def SubDef :> SuperDerf;
     *
     *
     *   }
     * }
     * </pre>
     *
     * @author Arthur Daussy
     */
    private static class TestModel {

        private ModelBuilder builder = new ModelBuilder();

        private Namespace root;

        private Package p1;

        private AttributeDefinition attrDef1;

        private PartDefinition superDef;

        private AttributeUsage attr1;

        private AttributeUsage protectedAttr;

        private PartDefinition subDef;

        private AttributeUsage privateAttr;

        private PartDefinition superSuperDef;

        private AttributeUsage attr0;

        private PartDefinition megaDef;

        TestModel() {
            this.build();
        }

        private void build() {
            this.root = builder.createWithName(Namespace.class, null);

            this.p1 = builder.createInWithName(Package.class, root, "p1");

            this.attrDef1 = this.builder.createInWithName(AttributeDefinition.class, this.p1, "attrDef1");

            this.superDef = this.builder.createInWithName(PartDefinition.class, this.p1, "SuperDef");

            this.attr1 = this.builder.createInWithName(AttributeUsage.class, this.superDef, "attr1");
            this.builder.setType(this.attr1, this.attrDef1);
            this.privateAttr = this.builder.createInWithName(AttributeUsage.class, this.superDef, "privateAttr");
            this.privateAttr.getOwningFeatureMembership().setVisibility(VisibilityKind.PRIVATE);
            this.protectedAttr = this.builder.createInWithName(AttributeUsage.class, this.superDef, "protectedAttr");
            this.protectedAttr.getOwningFeatureMembership().setVisibility(VisibilityKind.PROTECTED);

            this.subDef = this.builder.createInWithName(PartDefinition.class, this.p1, "SubDef");

            this.superSuperDef = this.builder.createInWithName(PartDefinition.class, this.p1, "superSuperDef");
            this.attr0 = this.builder.createInWithName(AttributeUsage.class, this.superSuperDef, "attr0");

            this.megaDef = this.builder.createInWithName(PartDefinition.class, this.p1, "megaDef");

            this.builder.addSuperType(this.subDef, this.superDef);
            this.builder.addSuperType(this.superDef, this.superSuperDef);
            this.builder.addSuperType(this.superSuperDef, this.megaDef);

        }
    }
    
    private ModelBuilder builder = new ModelBuilder();


    @DisplayName("Check that visible members of type get acces to inherited feature")
    @Test
    public void visibleMemberships() {
        var testModel = new TestModel();

        // Protected elements are not visible element
        assertContentEquals(testModel.subDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership());
        // No visible membership
        assertContentEquals(testModel.superDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.superSuperDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.megaDef.visibleMemberships(new BasicEList<>(), false, false));

    }

    @DisplayName("Check that visible members with loop does not create an infinite loop when computing visibleMembership")
    @Test
    public void visibleMembershipsWithLoop() {
        var testModel = new TestModel();

        builder.addSuperType(testModel.megaDef, testModel.subDef);
        /**
         * <pre>
         * package p1 {
         *
         *       part def MegaDef :> SubDef{
         *       }
         *
         *       part def SuperSuperDef :> MegaDef{
         *          attribute attr0;
         *       }
         *
         *       part def SuperDerf :> SuperSuperDef{
         *           attribute attr1;
         *           private attribute privateAttr;
         *           protected attribute protectedAttr;
         *       }
         *
         *       part def SubDef :> SuperDerf;
         *
         *
         *   }
         * </pre>
         **/

        assertContentEquals(testModel.megaDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.subDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.superDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership());

    }

    @DisplayName("Check that visible members of type get acces to inherited features")
    @Test
    public void inheritedMembers() {
        var testModel = new TestModel();

        assertContentEquals(testModel.subDef.inheritedMemberships(new BasicEList<>()),
                testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership(), testModel.protectedAttr.getOwningMembership());
        assertContentEquals(testModel.superDef.inheritedMemberships(new BasicEList<>()), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.superSuperDef.inheritedMemberships(new BasicEList<>()));
        assertContentEquals(testModel.megaDef.inheritedMemberships(new BasicEList<>()));

    }

    @DisplayName("Check that the features get access to inherited feature.")
    @Test
    public void getFeatures() {
        var testModel = new TestModel();

        assertContentEquals(testModel.subDef.getFeature(), testModel.attr1, testModel.attr0, testModel.protectedAttr);
        assertContentEquals(testModel.superDef.getFeature(), testModel.attr1, testModel.attr0, testModel.protectedAttr, testModel.privateAttr);
        assertContentEquals(testModel.superSuperDef.getFeature(), testModel.attr0);
        assertContentEquals(testModel.megaDef.getFeature());

    }

}
