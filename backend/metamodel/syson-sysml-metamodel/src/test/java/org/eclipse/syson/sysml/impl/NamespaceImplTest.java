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

import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link NamespaceImpl}.
 * 
 * @author Arthur Daussy
 */
public class NamespaceImplTest {

    /**
     * Test model
     * 
     * <pre>
     * package p1 {
     *      part def def1;
     *      package p1x1 {
     *          part def def1x1;
     *          package p1x1x1 {
     *              part def def1x1x1;
     *          }
     *      }
     *      
     *      package p1x2 {
     *         part def def1x2;
     *      }
     *  }
     *  
     *  package p2 {
     *      part def def2;
     *      package p2x1 {
     *          part def def2x1;
     *      }
     *  }
     *  
     *  package pack3 {
     *      part def def3;
     *      package p3x1 {
     *          part def def3x1;
     *      }
     *  }
     * </pre>
     * 
     * @author Arthur Daussy
     */
    private static class TestModel {

        private ModelBuilder builder = new ModelBuilder();

        private Package p2;

        private PartDefinition def2;

        private Package p2x1;

        private PartDefinition def2x1;

        private Package p1;

        private PartDefinition def1;

        private Package p1x1;

        private PartDefinition def1x1;

        private Package p3;

        private Package p1x2;

        private PartDefinition def1x2;

        private Package p3x1;

        private PartDefinition def3;

        private PartDefinition def3x1;

        private Package p1x1x1;

        private PartDefinition def1x1x1;

        private PartDefinition privatedef1x1;

        private PartDefinition privateDef3;

        private PartDefinition privateDef3x1;

        TestModel() {
            build();
        }

        private void build() {

            p1 = builder.createWithName(Package.class, "p1");
            def1 = builder.createInWithName(PartDefinition.class, p1, "Def1");

            p1x1 = builder.createInWithName(Package.class, p1, "p1x1");
            def1x1 = builder.createInWithName(PartDefinition.class, p1x1, "def1x1");
            privatedef1x1 = builder.createInWithName(PartDefinition.class, p1x1, "privatedef1x1");
            privatedef1x1.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);

            p1x1x1 = builder.createInWithName(Package.class, p1x1, "p1x1x1");
            def1x1x1 = builder.createInWithName(PartDefinition.class, p1x1x1, "def1x1x1");

            p1x2 = builder.createInWithName(Package.class, p1, "p1x2");
            def1x2 = builder.createInWithName(PartDefinition.class, p1x2, "def1x2");

            p2 = builder.createWithName(Package.class, "p2");
            def2 = builder.createInWithName(PartDefinition.class, p2, "Def2");
            p2x1 = builder.createInWithName(Package.class, p2, "p2x1");
            def2x1 = builder.createInWithName(PartDefinition.class, p2x1, "def2x1");

            p3 = builder.createWithName(Package.class, "p3");
            def3 = builder.createInWithName(PartDefinition.class, p3, "Def3");
            privateDef3 = builder.createInWithName(PartDefinition.class, p3, "privateDef3");
            privateDef3.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);

            p3x1 = builder.createInWithName(Package.class, p3, "p3x1");
            def3x1 = builder.createInWithName(PartDefinition.class, p3x1, "def3x1");
            privateDef3x1 = builder.createInWithName(PartDefinition.class, p3x1, "def3x1");
            privateDef3x1.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
        }
    }

    private ModelBuilder builder = new ModelBuilder();

    @DisplayName("Check membership feature")
    @Test
    public void membership() {
        var testModel = new TestModel();
        assertContentEquals(testModel.p1.getMembership(), testModel.def1.getOwningMembership(), testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership());

        assertContentEquals(testModel.def1.getMembership());

        /* Also include private membership */
        assertContentEquals(testModel.p1x1.getMembership(), testModel.def1x1.getOwningMembership(), testModel.p1x1x1.getOwningMembership(), testModel.privatedef1x1.getOwningMembership());

        assertContentEquals(testModel.p2.getMembership(), testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership());
    }

    @DisplayName("Check imported memberships when no import is used")
    @Test
    public void importedMembershipEmpty() {
        var testModel = new TestModel();

        assertContentEquals(testModel.p1.getImportedMembership());

        NamespaceImport nmImport = builder.createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p2);

        assertContentEquals(testModel.p1.getImportedMembership(), testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership());
    }

    @DisplayName("Check imported membership with Membership import")
    @Test
    public void importedMembershipSimpleMembershipImport() {
        var testModel = new TestModel();

        MembershipImport nsImport = builder.createIn(MembershipImport.class, testModel.p1);
        nsImport.setImportedMembership(testModel.p2x1.getOwningMembership());

        assertContentEquals(testModel.p1.getImportedMembership(), testModel.p2x1.getOwningMembership());
    }

    @DisplayName("Check imported memberships with a simple NamespaceImport")
    @Test
    public void importedMembershipSimpleNamespaceImport() {
        var testModel = new TestModel();

        NamespaceImport nmImport = builder.createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p2);

        assertContentEquals(testModel.p1.getImportedMembership(), testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership());
    }

    @DisplayName("Check imported memberships with a simple recursive NamespaceImport")
    @Test
    public void importedMembershipRecursiveNamespaceImport() {
        var testModel = new TestModel();

        // Import p3 in P2
        NamespaceImport nmImport0 = builder.createIn(NamespaceImport.class, testModel.p2);
        nmImport0.setImportedNamespace(testModel.p3);

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = builder.createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p2);
        nmImport.setIsRecursive(true);

        assertContentEquals(testModel.p1.getImportedMembership(),
                // All P2 content
                testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership(), testModel.def2x1.getOwningMembership(),
                // All P3 content
                testModel.def3.getOwningMembership(), testModel.p3x1.getOwningMembership(), testModel.def3x1.getOwningMembership());
    }

    @DisplayName("Check imported memberships with a simple recursive NamespaceImport and import all = true")
    @Test
    public void importedMembershipRecursiveNamespaceImportAndImportAll() {
        var testModel = new TestModel();

        // Import p3 in p1 with recursion
        NamespaceImport nmImport = builder.createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p3);
        nmImport.setIsRecursive(true);
        nmImport.setIsImportAll(true);

        assertContentEquals(testModel.p1.getImportedMembership(),
                // All P3 content with private
                testModel.def3.getOwningMembership(), testModel.p3x1.getOwningMembership(), testModel.def3x1.getOwningMembership(), testModel.privateDef3.getOwningMembership(),
                testModel.privateDef3x1.getOwningMembership());
    }

    @DisplayName("Check imported membershits with a simple recursive NamespaceImport with a loop")
    @Test
    public void importedMembershipRecursiveNamespaceImportWithLopp() {
        var testModel = new TestModel();

        // Import p1 in p3
        builder.createIn(NamespaceImport.class, testModel.p3).setImportedNamespace(testModel.p1);

        // Import p3 in P2
        NamespaceImport namespaceImport = builder.createIn(NamespaceImport.class, testModel.p2);
        namespaceImport.setImportedNamespace(testModel.p3);

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = builder.createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p2);
        nmImport.setIsRecursive(true);

        assertContentEquals(testModel.p1.getImportedMembership(), //
                // All P2 content
                testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership(), testModel.def2x1.getOwningMembership(),
                // All P3 content
                testModel.def3.getOwningMembership(), testModel.p3x1.getOwningMembership(), testModel.def3x1.getOwningMembership(),
                // ALL P1 but not infinite loop
                testModel.def1.getOwningMembership(), testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership(),
                testModel.def1x1.getOwningMembership(), testModel.p1x1x1.getOwningMembership(), testModel.def1x1x1.getOwningMembership(),
                testModel.def1x2.getOwningMembership());
    }

    @DisplayName("Check imported membershits with a simple recursive NamespaceImport with a loop to itself")
    @Test
    public void importedMembershipRecursiveNamespaceImportWithSelfLoop() {
        var testModel = new TestModel();

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = builder.createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p1);
        nmImport.setIsRecursive(true);

        assertContentEquals(testModel.p1.getImportedMembership(),
                // All P1 but not infinite loop
                testModel.def1.getOwningMembership(), testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership(),
                testModel.def1x1.getOwningMembership(), testModel.p1x1x1.getOwningMembership(), testModel.def1x1x1.getOwningMembership(),
                testModel.def1x2.getOwningMembership());
    }

    @DisplayName("Check imported membershits with a simple recursive NamespaceImport with a loop one of its parent")
    @Test
    public void importedMembershipRecursiveNamespaceImportWithNestedSelfLoop() {
        var testModel = new TestModel();

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = builder.createIn(NamespaceImport.class, testModel.p1x1x1);
        nmImport.setImportedNamespace(testModel.p1);
        nmImport.setIsRecursive(true);

        assertContentEquals(testModel.p1x1x1.getImportedMembership(),
                // All P1 but not infinite loop
                testModel.def1.getOwningMembership(), testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership(),
                testModel.def1x1.getOwningMembership(), testModel.p1x1x1.getOwningMembership(), testModel.def1x1x1.getOwningMembership(),
                testModel.def1x2.getOwningMembership());
    }

}
