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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.LibraryPackage;
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
public class TypeImplTest {

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
    private class TestModel {

        private final ModelBuilder builder = new ModelBuilder();

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

        public ModelBuilder getBuilder() {
            return this.builder;
        }

        private void build() {
            this.root = this.builder.createRootNamespace();

            this.p1 = this.builder.createInWithName(Package.class, this.root, "p1");

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

            this.builder.addSubclassification(this.subDef, this.superDef);
            this.builder.addSubclassification(this.superDef, this.superSuperDef);
            this.builder.addSubclassification(this.superSuperDef, this.megaDef);
        }
    }


    @DisplayName("Check name collision with super type memberships")
    @Test
    public void nameCollisionWithInheritedMemberships() {
        var testModel = new TestModel();

        assertContentEquals(testModel.subDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership());

        // Then create an attribute named attr0 in subDef to hide the super SuperSuperDef::attr0 attribute
        AttributeUsage subDefAttr0 = testModel.getBuilder().createInWithName(AttributeUsage.class, testModel.subDef, "attr0");

        // SuperSuperDef::attr0 should not be visible anymore
        assertContentEquals(testModel.subDef.visibleMemberships(new BasicEList<>(), false, false),
                testModel.attr1.getOwningMembership(), subDefAttr0.getOwningMembership());
        assertContentEquals(testModel.subDef.getInheritedMembership(), testModel.attr1.getOwningMembership(), testModel.protectedAttr.getOwningMembership());

        AttributeUsage subDefAttr1 = testModel.getBuilder().createInWithName(AttributeUsage.class, testModel.subDef, "attr1");
        // SuperDef::attr1 should not be visible anymore
        assertContentEquals(testModel.subDef.visibleMemberships(new BasicEList<>(), false, false),
                subDefAttr1.getOwningMembership(), subDefAttr0.getOwningMembership());

    }

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

        testModel.getBuilder().addSubclassification(testModel.megaDef, testModel.subDef);
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
         * }
         * </pre>
         **/

        assertContentEquals(testModel.megaDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.subDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.superDef.visibleMemberships(new BasicEList<>(), false, false), testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership());

    }

    @DisplayName("Check that visible members of type get access to inherited features")
    @Test
    public void inheritedMembers() {
        var testModel = new TestModel();

        assertContentEquals(testModel.subDef.inheritedMemberships(new BasicEList<>()),
                testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership(), testModel.protectedAttr.getOwningMembership());
        assertContentEquals(testModel.superDef.inheritedMemberships(new BasicEList<>()), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.superDef.getInheritedMembership(), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.superSuperDef.inheritedMemberships(new BasicEList<>()));
        assertContentEquals(testModel.megaDef.inheritedMemberships(new BasicEList<>()));

        // Inherited membership is a subset of membership.
        // All those elements should also be contained in memebership.

        assertContentEquals(testModel.subDef.getMembership(),
                testModel.attr1.getOwningMembership(), testModel.attr0.getOwningMembership(), testModel.protectedAttr.getOwningMembership());
        assertContentEquals(testModel.superDef.getMembership(),
                // All inherited from SuperSuperDef
                testModel.attr0.getOwningMembership(),
                // All contained in superDef
                testModel.attr1.getOwningMembership(), testModel.privateAttr.getOwningMembership(), testModel.protectedAttr.getOwningMembership());

        assertContentEquals(testModel.superSuperDef.getMembership(), testModel.attr0.getOwningMembership());
        assertContentEquals(testModel.megaDef.getMembership());

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

    @DisplayName("Check that allSupertypes do not stackoverflow.")
    @Test
    public void allSupertypes() {
        var testModel = new TestModel();

        assertContentEquals(testModel.subDef.allSupertypes(), testModel.subDef, testModel.superDef, testModel.superSuperDef, testModel.megaDef);
        assertContentEquals(testModel.superDef.allSupertypes(), testModel.superDef, testModel.superSuperDef, testModel.megaDef);
        assertContentEquals(testModel.superSuperDef.allSupertypes(), testModel.superSuperDef, testModel.megaDef);
        assertContentEquals(testModel.megaDef.allSupertypes(), testModel.megaDef);

        testModel.getBuilder().addSubclassification(testModel.megaDef, testModel.subDef);
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
         * }
         * </pre>
         **/
        assertContentEquals(testModel.subDef.allSupertypes(), testModel.subDef, testModel.superDef, testModel.superSuperDef, testModel.megaDef);
        assertContentEquals(testModel.superDef.allSupertypes(), testModel.subDef, testModel.superDef, testModel.superSuperDef, testModel.megaDef);
        assertContentEquals(testModel.superSuperDef.allSupertypes(), testModel.subDef, testModel.superDef, testModel.superSuperDef, testModel.megaDef);
        assertContentEquals(testModel.megaDef.allSupertypes(), testModel.subDef, testModel.superDef, testModel.superSuperDef, testModel.megaDef);

    }

    @DisplayName("Check specializesFromLibrary")
    @Test
    public void specializesFromLibrary() {
        var testModel = new TestModel();
        var library = testModel.getBuilder().createRootNamespace();
        var libraryPackage = testModel.getBuilder().createInWithName(LibraryPackage.class, library, "MyLibrary");
        var baseAttribute = testModel.getBuilder().createInWithName(AttributeUsage.class, libraryPackage, "baseAttribute");

        testModel.getBuilder().addSubsetting(testModel.attr0, baseAttribute);

        /**
         * <pre>
         * package p1 {
         *       part def SuperSuperDef :> MegaDef{
         *          attribute attr0 :> baseAttribute;
         *       }
         * }
         * libraryPackage MyLibrary {
         *       attribute baseAttribute;
         * }
         * </pre>
         **/

        assertTrue(testModel.attr0.specializesFromLibrary("MyLibrary::baseAttribute"));
    }
}
