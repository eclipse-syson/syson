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
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.Namespace;
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
     * namespace {
     * package p1 {
     *      part def def1;
     *      package p1x1 {
     *          part def def1x1;
     *          package p1x1x1 {
     *              part def def1x1x1 :> def4;
     *          }
     *          private part def privatedef1x1 :> p4::def4;
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
     *  package p3 {
     *      part def def3;
     *      private part def privateDef3;
     *      package p3x1 {
     *          private part def def3x1;
     *      }
     *      private part def privateDef3
     *  }
     *
     *  package p4 {
     *      part def def4;
     *  }
     * }
     * </pre>
     *
     * @author Arthur Daussy
     */
    private class TestModel {

        private final ModelBuilder builder = new ModelBuilder();

        private ResourceSet context;

        private Namespace root;

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

        private Namespace root2;

        private Package p4;

        private PartDefinition def4;

        TestModel() {
            this.build();
        }

        public ModelBuilder getBuilder() {
            return this.builder;
        }

        private void build() {

            this.root = this.builder.createWithName(Namespace.class, null);

            this.p1 = this.builder.createInWithName(Package.class, this.root, "p1");
            this.def1 = this.builder.createInWithName(PartDefinition.class, this.p1, "Def1");

            this.p1x1 = this.builder.createInWithName(Package.class, this.p1, "p1x1");
            this.def1x1 = this.builder.createInWithName(PartDefinition.class, this.p1x1, "def1x1");
            this.privatedef1x1 = this.builder.createInWithName(PartDefinition.class, this.p1x1, "privatedef1x1");
            this.privatedef1x1.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);

            this.p1x1x1 = this.builder.createInWithName(Package.class, this.p1x1, "p1x1x1");
            this.def1x1x1 = this.builder.createInWithName(PartDefinition.class, this.p1x1x1, "def1x1x1");

            this.p1x2 = this.builder.createInWithName(Package.class, this.p1, "p1x2");
            this.def1x2 = this.builder.createInWithName(PartDefinition.class, this.p1x2, "def1x2");

            this.p2 = this.builder.createInWithName(Package.class, this.root, "p2");
            this.def2 = this.builder.createInWithName(PartDefinition.class, this.p2, "Def2");
            this.p2x1 = this.builder.createInWithName(Package.class, this.p2, "p2x1");
            this.def2x1 = this.builder.createInWithName(PartDefinition.class, this.p2x1, "def2x1");

            this.p3 = this.builder.createInWithName(Package.class, this.root, "p3");
            this.def3 = this.builder.createInWithName(PartDefinition.class, this.p3, "Def3");
            this.privateDef3 = this.builder.createInWithName(PartDefinition.class, this.p3, "privateDef3");
            this.privateDef3.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);

            this.p3x1 = this.builder.createInWithName(Package.class, this.p3, "p3x1");
            this.def3x1 = this.builder.createInWithName(PartDefinition.class, this.p3x1, "def3x1");
            this.privateDef3x1 = this.builder.createInWithName(PartDefinition.class, this.p3x1, "privateDef3x1");
            this.privateDef3x1.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);


            this.root2 = this.builder.createWithName(Namespace.class, null);
            this.p4 = this.builder.createInWithName(Package.class, this.root2, "p9");
            this.def4 = this.builder.createInWithName(PartDefinition.class, this.p4, "def9");

            this.builder.addSubclassification(this.def1x1x1, this.def4);
            this.builder.createIn(NamespaceImport.class, this.p1x1x1).setImportedNamespace(this.def4);
            this.builder.addSubclassification(this.privatedef1x1, this.def4);

            this.context = new ResourceSetImpl();
            Resource doc1 = new ResourceFactoryImpl().createResource(null);
            doc1.getContents().add(this.root);
            Resource doc2 = new ResourceFactoryImpl().createResource(null);
            doc2.getContents().add(this.root2);
            this.context.getResources().add(doc1);
            this.context.getResources().add(doc2);
        }
    }

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

        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p2);

        assertContentEquals(testModel.p1.getImportedMembership(), testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership());
    }

    @DisplayName("Check imported membership with Membership import")
    @Test
    public void importedMembershipSimpleMembershipImport() {
        var testModel = new TestModel();

        MembershipImport nsImport = testModel.getBuilder().createIn(MembershipImport.class, testModel.p1);
        nsImport.setImportedMembership(testModel.p2x1.getOwningMembership());

        assertContentEquals(testModel.p1.getImportedMembership(), testModel.p2x1.getOwningMembership());
    }

    @DisplayName("Check imported memberships with a simple NamespaceImport")
    @Test
    public void importedMembershipSimpleNamespaceImport() {
        var testModel = new TestModel();

        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p2);

        assertContentEquals(testModel.p1.getImportedMembership(), testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership());
    }

    @DisplayName("Check imported memberships with a simple recursive NamespaceImport")
    @Test
    public void importedMembershipRecursiveNamespaceImport() {
        var testModel = new TestModel();

        // Import p3 in P2
        NamespaceImport nmImport0 = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p2);
        nmImport0.setImportedNamespace(testModel.p3);

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p2);
        nmImport.setIsRecursive(true);

        assertContentEquals(testModel.p1.getImportedMembership(),
                // All P2 content
                testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership(), testModel.def2x1.getOwningMembership(),
                // All P3 content
                testModel.def3.getOwningMembership(), testModel.p3x1.getOwningMembership(), testModel.def3x1.getOwningMembership());
    }

    @DisplayName("Check imported memberships with transitive import")
    @Test
    public void importedMembershipTransitiveNamespaceImport() {
        var testModel = new TestModel();

        // Import p3 in P2
        NamespaceImport nmImport0 = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p3);
        nmImport0.setImportedNamespace(testModel.p2);

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p2);
        nmImport.setImportedNamespace(testModel.p1);

        assertContentEquals(testModel.p3.getImportedMembership(),
                // All P2 content
                testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership(),
                // All P1 content by transition
                testModel.def1.getOwningMembership(), testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership());
    }

    @DisplayName("Check memberships with transitive import")
    @Test
    public void membershipTransitiveNamespaceImport() {
        var testModel = new TestModel();

        // Import p3 in P2
        NamespaceImport nmImport0 = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p3);
        nmImport0.setImportedNamespace(testModel.p2);

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p2);
        nmImport.setImportedNamespace(testModel.p1);

        assertContentEquals(testModel.p3.getMembership(),
                // All P2 content
                testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership(),
                // All P1 content by transition
                testModel.def1.getOwningMembership(), testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership(),
                // P3 direct content
                testModel.def3.getOwningMembership(), testModel.privateDef3.getOwningMembership(), testModel.p3x1.getOwningMembership());
    }

    @DisplayName("Check imported memberships when name collision exists")
    @Test
    public void membershipNameCollision() {
        var testModel = new TestModel();

        // Set the name of def1 to def3 to create a name collision
        testModel.def1.setDeclaredName(testModel.def3.getDeclaredName());

        // Define the same short name for def3 and def2 to create name collision
        testModel.def2.setDeclaredShortName("d");
        testModel.def3.setDeclaredShortName("d");

        // Import p3 in P2
        NamespaceImport nmImport0 = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p3);
        nmImport0.setImportedNamespace(testModel.p2);

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p2);
        nmImport.setImportedNamespace(testModel.p1);

        assertContentEquals(testModel.p3.getImportedMembership(),
                // All P2 content except def2
                testModel.p2x1.getOwningMembership(),
                // All P1 content by transition except def1
                testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership());
        assertContentEquals(testModel.p3.getMembership(),
                // All P2 content except def2
                testModel.p2x1.getOwningMembership(),
                // All P1 content by transition except def1
                testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership(),
                // P3 direct content
                testModel.def3.getOwningMembership(), testModel.privateDef3.getOwningMembership(), testModel.p3x1.getOwningMembership());

    }

    @DisplayName("Check imported memberships when using prive import")
    @Test
    public void membershipPrivateNamespaceImport() {
        var testModel = new TestModel();

        // Import p3 in P2
        NamespaceImport nmImport0 = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p3);
        nmImport0.setImportedNamespace(testModel.p2);

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p2);
        nmImport.setImportedNamespace(testModel.p1);
        nmImport.setVisibility(VisibilityKind.PRIVATE);

        assertContentEquals(testModel.p3.getMembership(),
                // All P2 content
                testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership(),
                // P3 direct content
                testModel.def3.getOwningMembership(), testModel.privateDef3.getOwningMembership(), testModel.p3x1.getOwningMembership());
    }

    @DisplayName("Check imported memberships with a simple recursive NamespaceImport and import all = true")
    @Test
    public void importedMembershipRecursiveNamespaceImportAndImportAll() {
        var testModel = new TestModel();

        // Import p3 in p1 with recursion
        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p1);
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
    public void importedMembershipRecursiveNamespaceImportWithLoop() {
        var testModel = new TestModel();

        // Import p1 in p3
        testModel.getBuilder().createIn(NamespaceImport.class, testModel.p3).setImportedNamespace(testModel.p1);

        // Import p3 in P2
        NamespaceImport namespaceImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p2);
        namespaceImport.setImportedNamespace(testModel.p3);

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p2);
        nmImport.setIsRecursive(true);

        assertContentEquals(testModel.p1.getImportedMembership(), //
                // All P2 content
                testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership(), testModel.def2x1.getOwningMembership(),
                // All P3 content
                testModel.def3.getOwningMembership(), testModel.p3x1.getOwningMembership(), testModel.def3x1.getOwningMembership());

        assertContentEquals(testModel.p1.getMembership(), //
                // All P2 content
                testModel.def2.getOwningMembership(), testModel.p2x1.getOwningMembership(), testModel.def2x1.getOwningMembership(),
                // All P3 content
                testModel.def3.getOwningMembership(), testModel.p3x1.getOwningMembership(), testModel.def3x1.getOwningMembership(),
                // And no infinite loop
                testModel.def1.getOwningMembership(), testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership());
    }

    @DisplayName("Check imported membershits with a simple recursive NamespaceImport with a loop to itself")
    @Test
    public void importedMembershipRecursiveNamespaceImportWithSelfLoop() {
        var testModel = new TestModel();

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p1);
        nmImport.setImportedNamespace(testModel.p1);
        nmImport.setIsRecursive(true);

        // Can import its own member since it could cause name conflict
        assertContentEquals(testModel.p1.getImportedMembership());
    }

    @DisplayName("Check imported membershits with a simple recursive NamespaceImport with a loop one of its parent")
    @Test
    public void importedMembershipRecursiveNamespaceImportWithNestedSelfLoop() {
        var testModel = new TestModel();

        // Import p2 in p1 with recursion
        NamespaceImport nmImport = testModel.getBuilder().createIn(NamespaceImport.class, testModel.p1x1x1);
        nmImport.setImportedNamespace(testModel.p1);
        nmImport.setIsRecursive(true);

        assertContentEquals(testModel.p1x1x1.getImportedMembership(),
                testModel.def1.getOwningMembership(), testModel.p1x1.getOwningMembership(), testModel.p1x2.getOwningMembership(),
                testModel.def1x1.getOwningMembership(), testModel.p1x1x1.getOwningMembership(),
                testModel.def1x2.getOwningMembership());
    }

    @DisplayName("Test resolve Visible self qualified Name")
    @Test
    public void resolveVisibleSelf() {
        var testModel = new TestModel();

        assertEquals(testModel.def1x1, testModel.p1x1.resolveVisible(testModel.def1x1.getName()).getMemberElement());

        assertEquals(testModel.p1x1, testModel.p1.resolveVisible(testModel.p1x1.getName()).getMemberElement());

        assertNull(testModel.p1.resolveVisible(testModel.def1x1.getName()));
    }

    @DisplayName("Test resolveLocal")
    @Test
    public void resolveLocalSimpleTest() {
        var testModel = new TestModel();

        /*      package p1x1 {
         *          part def def1x1;
         *          package p1x1x1 {
         *              part def def1x1x1;
         *          }
         *          private part def privatedef1x1;
         *      }
         */
        // Direct resolve (level n-1)
        assertEquals(testModel.def1x1, testModel.p1x1.resolveLocal(testModel.def1x1.getName()).getMemberElement());
        assertEquals(testModel.p1x1x1, testModel.p1x1.resolveLocal(testModel.p1x1x1.getName()).getMemberElement());
        assertEquals(testModel.def1x1x1, testModel.p1x1x1.resolveLocal(testModel.def1x1x1.getName()).getMemberElement());
        // Direct resolve (level n-1) private
        assertEquals(testModel.privatedef1x1, testModel.p1x1.resolveLocal(testModel.privatedef1x1.getName()).getMemberElement());

        // Parent resolve (level n+1)
        assertEquals(testModel.def1x1, testModel.def1x1x1.resolveLocal(testModel.def1x1.getName()).getMemberElement());
        assertEquals(testModel.p1x1x1, testModel.def1x1x1.resolveLocal(testModel.p1x1x1.getName()).getMemberElement());
        // Direct resolve (level n-2) - KO
        assertNull(testModel.p1x1.resolveLocal(testModel.def1x1x1.getName()));

        // Parent resolve (level n+2)
        assertEquals(testModel.p1x1, testModel.def1x1x1.resolveLocal(testModel.p1x1.getName()).getMemberElement());

        // Self resolve (level n)
        assertEquals(testModel.def1x1, testModel.def1x1.resolveLocal(testModel.def1x1.getName()).getMemberElement());

        // Root resolve (level 0)
        assertEquals(testModel.p1, testModel.p1.resolveLocal(testModel.p1.getName()).getMemberElement());
    }

    @DisplayName("Test resolveVisible")
    @Test
    public void resolveVisibleTest() {
        var testModel = new TestModel();

        /*      package p1x1 {
         *          part def def1x1;
         *          package p1x1x1 {
         *              part def def1x1x1;
         *          }
         *          private part def privatedef1x1;
         *      }
         */

        // Direct resolve (level n-1)
        assertEquals(testModel.def1x1, testModel.p1x1.resolveVisible(testModel.def1x1.getName()).getMemberElement());

        // Direct resolve (level n-1) private - null
        assertNull(testModel.p1x1.resolveVisible(testModel.privatedef1x1.getName()));

    }

    @DisplayName("Test resolve")
    @Test
    public void resolveTest() {
        var testModel = new TestModel();

        /*
        * package p1 {
        *      part def def1;
        *      package p1x1 {
        *          part def def1x1;
        *          package p1x1x1 {
        *              part def def1x1x1;
        *          }
        *          private part def privatedef1x1;
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
        */

        // Direct resolve (level n-3) not with name because weak resolution
        assertNull(testModel.p1.resolve(testModel.def1x1x1.getName()));
        // Direct resolve (level n-3) same ok with qualified name
        assertEquals(testModel.def1x1x1, testModel.p1.resolve(testModel.def1x1x1.getQualifiedName()).getMemberElement());

        // Direct resolve (level n+1) ok with name and qualified name because upper
        assertEquals(testModel.def1, testModel.def1x1.resolve(testModel.def1.getName()).getMemberElement());
        assertEquals(testModel.def1, testModel.def1x1.resolve(testModel.def1.getQualifiedName()).getMemberElement());
    }

    @DisplayName("Test resolveGlobal")
    @Test
    public void resolveGlobalTest() {
        var testModel = new TestModel();

        /*
         *  package p1x1 {
         *    part def def1x1;
         *    package p1x1x1 {
         *      import p4::*;
         *      part def def1x1x1 :> def4;
         *    }
         *    private part def privatedef1x1 :> p4::def4;
         *  }
         *
         *  package p4 {
         *    part def def4;
         *  }
         */

        // Direct resolve (level n-2)
        assertEquals(testModel.def1x1x1, testModel.p1x1.resolve(testModel.def1x1x1.getQualifiedName()).getMemberElement());


        // Other document resolution
        assertEquals(testModel.def4, testModel.def1x1x1.resolve(testModel.def4.getQualifiedName()).getMemberElement());
        assertEquals(testModel.def4, testModel.privatedef1x1.resolve(testModel.def4.getQualifiedName()).getMemberElement());
    }

    @DisplayName("Test unqualifiedNameOf")
    @Test
    public void unqualifiedNameOfTest() {
        var testModel = new TestModel();

        assertEquals("test1", testModel.p1.unqualifiedNameOf("test1"));
        assertEquals("test2", testModel.p1.unqualifiedNameOf("test1::test2"));
        assertEquals("test3", testModel.p1.unqualifiedNameOf("test1::test2::test3"));

        assertEquals("test 1", testModel.p1.unqualifiedNameOf("test 1"));
        assertEquals("test 2", testModel.p1.unqualifiedNameOf("test 1::test 2"));
        assertEquals("test 3", testModel.p1.unqualifiedNameOf("test 1::test 2::test 3"));

    }

    @DisplayName("Test qualificationOf")
    @Test
    public void qualificationOfTest() {
        var testModel = new TestModel();

        assertNull(testModel.p1.qualificationOf("test1"));
        assertNull(testModel.p1.qualificationOf("test1::"));
        assertEquals("test1", testModel.p1.qualificationOf("test1::test2"));
        assertEquals("test1::test2", testModel.p1.qualificationOf("test1::test2::test3"));
    }

}
