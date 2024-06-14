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
package org.eclipse.syson.sysml.export.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Consumer;

import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.VerificationCaseUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link NameDeresolver}.
 * 
 * @author Arthur Daussy
 */
public class NameDeresolverTest {

    private static final String P2_DEF2 = "p2::def2";

    private static final String DEF1 = "def1";

    private static final String P1X1_P1X1X1_DEF1X1X1 = "p1x1::p1x1x1::def1x1x1";

    private static final String P1_1 = "p1x1";

    private static final String P2 = "p2";

    private static final String P1 = "p1";

    private static final String PART_DEF2 = "PartDef2";

    private static final String PART_DEF1 = "PartDef1";

    private ModelBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new ModelBuilder();
    }

    @Test
    public void directOnwendMember() {

        Package p1 = builder.createWithName(Package.class, P1);
        PartDefinition partDef1 = builder.createInWithName(PartDefinition.class, p1, PART_DEF1);

        assertEquals(PART_DEF1, getDerolvedName(partDef1, partDef1));
    }

    private String getDerolvedName(Element toDeresolve, Element context) {
        return new NameDeresolver().getDeresolvedName(toDeresolve, context);
    }

    @Test
    public void checkNameCollision() {
        /**
         * <pre>
         * package Root {
         *   import Lib1::*;
         *   package p1 {
         *       ref attribute mass :> Lib2::mass; // Needs qualified name since the attribute hides the imported element
         *   }
         *   package p2 {
         *      ref attribute mass2 :> mass; // No need for qualified name since mass is directly visible
         *   }
         *   package p3 {
         *       ref attribute mass; // Needs qualified name since the attribute p3::mass hides the imported element
         *       package p3x1 {
         *           ref attribute mass2 :> Lib2::mass; // Needs qualified name since the attribute p3::mass hides the imported element
         *   }
         *       }
         * }
         *
         * package Lib1 {
         *    import Lib2::*;
         * }
         *
         * package Lib2 {
         *    attribute mass;
         * }
         * </pre>
         */

        Package lib2 = builder.createWithName(Package.class, "Lib2");
        PartDefinition massAttr = builder.createInWithName(PartDefinition.class, lib2, "mass");

        Package lib1 = builder.createWithName(Package.class, "Lib1");
        builder.createIn(NamespaceImport.class, lib1).setImportedNamespace(lib2);

        Package root = builder.createWithName(Package.class, "Root");
        builder.createIn(NamespaceImport.class, root).setImportedNamespace(lib1);

        Package p1 = builder.createInWithName(Package.class, root, "p1");
        PartDefinition p1Mass = builder.createInWithName(PartDefinition.class, p1, "mass");
        builder.addSuperType(p1Mass, massAttr);

        Package p2 = builder.createInWithName(Package.class, root, "p2");
        PartDefinition p2Mass = builder.createInWithName(PartDefinition.class, p2, "mass2");
        builder.addSuperType(p2Mass, massAttr);

        Package p3 = builder.createInWithName(Package.class, root, "p3");
        AttributeUsage p3Mass = builder.createInWithName(AttributeUsage.class, p3, "mass");

        Package p3x1 = builder.createInWithName(Package.class, p3, "p3x1");
        PartDefinition p3x1Mass = builder.createInWithName(PartDefinition.class, p3x1, "mass2");
        builder.addSuperType(p3x1Mass, massAttr);

        // Needs qualified name since the attribute p1::mass hides the imported element
        assertEquals("Lib2::mass", getDerolvedName(massAttr, p1Mass));
        // // No need for qualified name since mass is directly visible
        assertEquals("mass", getDerolvedName(massAttr, p2Mass));
        // // Needs qualified name since the attribute p3::mass hides the imported element
        assertEquals("Lib2::mass", getDerolvedName(massAttr, p3Mass));
        // Needs qualified name since the attribute p3::mass hides the imported element
        assertEquals("Lib2::mass", getDerolvedName(massAttr, p3x1Mass));
    }

    @Test
    public void deresolutionWithNameEscaping() {
        /**
         * <pre>
         * package p1 {
                attribute 'Attr 1';
                attribute 'Attr 2' :> 'Attr 1';
            }
         * </pre>
         */

        Package p1 = builder.createWithName(Package.class, P1);

        AttributeDefinition attr1 = builder.createInWithName(AttributeDefinition.class, p1, "Attr 1");
        AttributeDefinition attr2 = builder.createInWithName(AttributeDefinition.class, p1, "Attr 2");

        builder.addSuperType(attr2, attr1);

        assertEquals("'Attr 1'", getDerolvedName(attr1, attr2));
    }

    @Test
    public void transitiveMemberNameResolution() {
        Package p1 = builder.createWithName(Package.class, P1);
        PartDefinition partDef1 = builder.createInWithName(PartDefinition.class, p1, "def1");

        Package p2 = builder.createWithName(Package.class, "p2");

        NamespaceImport nmImport0 = builder.createIn(NamespaceImport.class, p2);
        nmImport0.setImportedNamespace(p1);

        Package p3 = builder.createWithName(Package.class, "p3");
        NamespaceImport nmImport = builder.createIn(NamespaceImport.class, p3);
        nmImport.setImportedNamespace(p2);

        PartDefinition partDef3 = builder.createInWithName(PartDefinition.class, p3, "def3");

        builder.addSuperType(partDef3, partDef1);
        /**
         * <pre>
         * package p1 {
                part def def1;
               
            }
        
            package p2 {
                import p1::*;
            }
        
            package p3 {
                import p2::*;
                part def def3 :> def1; 
                
            }
         * </pre>
         */
        assertEquals("def1", getDerolvedName(partDef1, partDef3));
    }

    @Test
    public void directImportedMember() {

        Package p1 = builder.createWithName(Package.class, P1);
        PartDefinition partDef1 = builder.createInWithName(PartDefinition.class, p1, PART_DEF1);

        Package p2 = builder.createWithName(Package.class, P2);
        PartDefinition partDef2 = builder.createInWithName(PartDefinition.class, p2, PART_DEF2);
        builder.addSuperType(partDef2, partDef1);

        // No import
        /**
         * <pre>
         * package p1 {
         *       part def PartDef1;
         *  }
         *  
         *  package p2 {
         *      part def PartDef2 :> p1::PartDef1;
         *  }
         * </pre>
         */
        assertEquals("p1::PartDef1", getDerolvedName(partDef1, partDef2));

        // Add import
        /**
         * <pre>
         * package p1 {
         *       part def PartDef1;
         *  }
         *  
         *  package p2 {
         *      import p1::*;
         *      part def PartDef2 :> PartDef1;
         *  }
         * </pre>
         */
        builder.createIn(NamespaceImport.class, p2).setImportedNamespace(p1);
        assertEquals(PART_DEF1, getDerolvedName(partDef1, partDef2));

    }

    @Test
    public void directNestedPackage() {
        Package p1 = builder.createWithName(Package.class, P1);
        Package p1x1 = builder.createWithName(Package.class, P1_1);
        PartDefinition partDef1 = builder.createInWithName(PartDefinition.class, p1x1, PART_DEF1);

        Package p2 = builder.createWithName(Package.class, P2);
        PartDefinition partDef2 = builder.createInWithName(PartDefinition.class, p2, PART_DEF2);
        builder.addSuperType(partDef2, partDef1);
        builder.createIn(NamespaceImport.class, p2).setImportedNamespace(p1);

        /**
         * <pre>
         * package p1 {
                package p1x1 {
                    part def PartDef1;
                }
            }
            
            package p2 {
                import p1::*;
                part def PartDef2 :> p1x1::PartDef1;
            }
         * </pre>
         */

        assertEquals("p1x1::PartDef1", getDerolvedName(partDef1, partDef2));

    }

    @Test
    public void recursiveImportNestedPackage() {
        Package p1 = builder.createWithName(Package.class, P1);
        Package p1x1 = builder.createInWithName(Package.class, p1, P1_1);
        PartDefinition partDef1 = builder.createInWithName(PartDefinition.class, p1x1, PART_DEF1);

        Package p2 = builder.createWithName(Package.class, P2);
        PartDefinition partDef2 = builder.createInWithName(PartDefinition.class, p2, PART_DEF2);
        builder.addSuperType(partDef2, partDef1);
        NamespaceImport nmImpot = builder.createIn(NamespaceImport.class, p2);
        nmImpot.setImportedNamespace(p1);
        nmImpot.setIsRecursive(true);

        /**
         * <pre>
         * package p1 {
                package p1x1 {
                    part def PartDef1;
                }
            }
            
            package p2 {
                import p1::*::**;
                part def PartDef2 :> PartDef1;
            }
         * </pre>
         */

        assertEquals(PART_DEF1, getDerolvedName(partDef1, partDef2));

    }

    @Test
    public void recursiveImport() {
        Package p1 = builder.createWithName(Package.class, P1);
        Package p1x1 = builder.createInWithName(Package.class, p1, P1_1);
        PartDefinition partDef1 = builder.createInWithName(PartDefinition.class, p1x1, PART_DEF1);

        Package p2 = builder.createWithName(Package.class, P2);
        NamespaceImport nmImpot = builder.createIn(NamespaceImport.class, p2);
        nmImpot.setImportedNamespace(p1);
        nmImpot.setIsRecursive(true);

        Package p3 = builder.createWithName(Package.class, "P3");
        NamespaceImport nmImpot2 = builder.createIn(NamespaceImport.class, p3);
        nmImpot2.setImportedNamespace(p2);
        nmImpot2.setIsRecursive(true);

        PartDefinition partDef3 = builder.createInWithName(PartDefinition.class, p3, "PartDef3");
        builder.addSuperType(partDef3, partDef1);

        /**
         * <pre>
         * package p1 {
                package p1x1 {
                    part def PartDef1;
                }
            }
            
            package p2 {
                import p1::*::**;
            }
            
            package 3 {
                import p2::*::**;
                part def PartDef3 :> PartDef1;
            }
         * </pre>
         */

        assertEquals(PART_DEF1, getDerolvedName(partDef1, partDef3));

        p3.getOwnedRelationship().remove(nmImpot2);
        assertEquals("p1::p1x1::PartDef1", getDerolvedName(partDef1, partDef3));
    }

    @Test
    public void noImport() {
        var model = new TestModel();
        // Descendant
        assertEquals(P1X1_P1X1X1_DEF1X1X1, getDerolvedName(model.def1x1x1, model.def1));
        assertEquals("p1x1::def1x1", getDerolvedName(model.def1x1, model.def1));
        assertEquals(DEF1, getDerolvedName(model.def1, model.def1));

        // In sibling package
        assertEquals(P2_DEF2, getDerolvedName(model.def2, model.def1));
        assertEquals("p2::p2x1::def2x1", getDerolvedName(model.def2x1, model.def1));
        assertEquals("p2::p2x1::p2x1x1::def2x1x1", getDerolvedName(model.def2x1x1, model.def1));

        // Ancestor
        assertEquals(DEF1, getDerolvedName(model.def1, model.def1x1x1));
        assertEquals("def1x1", getDerolvedName(model.def1x1, model.def1x1x1));
        assertEquals("def1x1x1", getDerolvedName(model.def1x1x1, model.def1x1x1));

        // In sibling ancestor
        assertEquals("p1x2::p1x2x1::def1x2x1", getDerolvedName(model.def1x2x1, model.def1x1x1));
        assertEquals("p1x2::def1x2", getDerolvedName(model.def1x2, model.def1x1x1));

        // In another package
        assertEquals("p2::p2x1::p2x1x1::def2x1x1", getDerolvedName(model.def2x1x1, model.def1x1x1));
        assertEquals("p2::p2x1::def2x1", getDerolvedName(model.def2x1, model.def1x1x1));
        assertEquals(P2_DEF2, getDerolvedName(model.def2, model.def1x1x1));

    }

    @DisplayName("Check name deresolution with feature with implicite name")
    @Test
    public void implicitNameWithConflict() {
        /**
         * <pre>
         *  package p1 {
         * 
         *      package p1x1 {
         *          verification v1;
         *      }
         * 
         *      part pu1 {
         *          perform v1;
         *       }
         *  }
         * </pre>
         */

        Package p1 = builder.createWithName(Package.class, "p1");

        Package p1x1 = builder.createInWithName(Package.class, p1, "p1x1");

        VerificationCaseUsage verif = builder.createInWithName(VerificationCaseUsage.class, p1x1, "v1");

        PartUsage partUsage = builder.createInWithName(PartUsage.class, p1, "pu1");

        PerformActionUsage performAction = builder.createInWithName(PerformActionUsage.class, partUsage, null);

        builder.addReferenceSubsetting(performAction, verif);

        assertEquals("p1x1::v1", getDerolvedName(verif, performAction));

        /*
         * We now add an import in p1 to p1x1 which makes v1 directly accessible from the perform action. Meaning that
         * we should normally could use directly "v1" to reference the verification. However the specificity of this
         * test is that the perform action is also named "v1" using the effectiveName rules of the PerformAction. In
         * such case it should create a name conflict forcing us to use "p1x1:v1" to reference the verification.
         * However, since the name of the perform action is "derived" we want to allows the "v1" to be resolved
         * against the verification.
         */

        /**
         * <pre>
         *  package p1 {
         *      import p1x1::*;
         * 
         *      package p1x1 {
         *          verification v1;
         *      }
         * 
         *      part pu1 {
         *          perform v1;
         *       }
         *  }
         * </pre>
         */
        builder.createIn(NamespaceImport.class, p1).setImportedNamespace(p1x1);

        assertEquals("v1", getDerolvedName(performAction, performAction));
        assertEquals("v1", getDerolvedName(verif, performAction));
    }

    @DisplayName("Check the deresolved names with a NamespaceImport at root level")
    @Test
    public void importRootNamespaceImpot() {

        var model = new TestModel();

        // Import P2 in P1
        builder.createIn(NamespaceImport.class, model.p1).setImportedNamespace(model.p2);

        // Descendant
        assertEquals(P1X1_P1X1X1_DEF1X1X1, getDerolvedName(model.def1x1x1, model.def1));
        assertEquals("p1x1::def1x1", getDerolvedName(model.def1x1, model.def1));
        assertEquals(DEF1, getDerolvedName(model.def1, model.def1));

        // In sibling package
        assertEquals("def2", getDerolvedName(model.def2, model.def1));
        assertEquals("p2x1::def2x1", getDerolvedName(model.def2x1, model.def1));
        assertEquals("p2x1::p2x1x1::def2x1x1", getDerolvedName(model.def2x1x1, model.def1));

        // Ancestor
        assertEquals(DEF1, getDerolvedName(model.def1, model.def1x1x1));
        assertEquals("def1x1", getDerolvedName(model.def1x1, model.def1x1x1));
        assertEquals("def1x1x1", getDerolvedName(model.def1x1x1, model.def1x1x1));

        // In sibling ancestor
        assertEquals("p1x2::p1x2x1::def1x2x1", getDerolvedName(model.def1x2x1, model.def1x1x1));
        assertEquals("p1x2::def1x2", getDerolvedName(model.def1x2, model.def1x1x1));

        // In another package
        assertEquals("p2x1::p2x1x1::def2x1x1", getDerolvedName(model.def2x1x1, model.def1x1x1));
        assertEquals("p2x1::def2x1", getDerolvedName(model.def2x1, model.def1x1x1));
        assertEquals("def2", getDerolvedName(model.def2, model.def1x1x1));

    }

    @DisplayName("Check the deresolved names with a NamespaceImport at root level with recursive import")
    @Test
    public void importRootRecursiveNamespaceImpot() {

        var model = new TestModel();

        // Import P2 in P1
        NamespaceImport namespaceImport = builder.createIn(NamespaceImport.class, model.p1);
        namespaceImport.setImportedNamespace(model.p2);
        namespaceImport.setIsRecursive(true);

        // Descendant
        assertEquals(P1X1_P1X1X1_DEF1X1X1, getDerolvedName(model.def1x1x1, model.def1));
        assertEquals("p1x1::def1x1", getDerolvedName(model.def1x1, model.def1));
        assertEquals(DEF1, getDerolvedName(model.def1, model.def1));

        // In sibling package
        assertEquals("def2", getDerolvedName(model.def2, model.def1));
        assertEquals("def2x1", getDerolvedName(model.def2x1, model.def1));
        assertEquals("def2x1x1", getDerolvedName(model.def2x1x1, model.def1));

        // Ancestor
        assertEquals(DEF1, getDerolvedName(model.def1, model.def1x1x1));
        assertEquals("def1x1", getDerolvedName(model.def1x1, model.def1x1x1));
        assertEquals("def1x1x1", getDerolvedName(model.def1x1x1, model.def1x1x1));

        // In sibling ancestor
        assertEquals("p1x2::p1x2x1::def1x2x1", getDerolvedName(model.def1x2x1, model.def1x1x1));
        assertEquals("p1x2::def1x2", getDerolvedName(model.def1x2, model.def1x1x1));

        // In another package
        assertEquals("def2x1x1", getDerolvedName(model.def2x1x1, model.def1x1x1));
        assertEquals("def2x1", getDerolvedName(model.def2x1, model.def1x1x1));
        assertEquals("def2", getDerolvedName(model.def2, model.def1x1x1));

    }

    @DisplayName("Check the deresolved name with a NamespaceImport in intermediate level")
    @Test
    public void importInterNamespaceImpot() {

        var model = new TestModel();

        // Import P2 in P1
        builder.createIn(NamespaceImport.class, model.p1x1).setImportedNamespace(model.p2x1);

        // Descendant
        assertEquals(P1X1_P1X1X1_DEF1X1X1, getDerolvedName(model.def1x1x1, model.def1));
        assertEquals(P1X1_P1X1X1_DEF1X1X1, getDerolvedName(model.def1x1x1, model.def1));
        assertEquals("p1x1::def1x1", getDerolvedName(model.def1x1, model.def1));

        // In sibling package
        assertEquals(P2_DEF2, getDerolvedName(model.def2, model.def1));
        assertEquals("p2::p2x1::def2x1", getDerolvedName(model.def2x1, model.def1));
        assertEquals("p2::p2x1::p2x1x1::def2x1x1", getDerolvedName(model.def2x1x1, model.def1));

        // Ancestor
        assertEquals(DEF1, getDerolvedName(model.def1, model.def1x1x1));
        assertEquals("def1x1", getDerolvedName(model.def1x1, model.def1x1x1));
        assertEquals("def1x1x1", getDerolvedName(model.def1x1x1, model.def1x1x1));

        // In sibling ancestor
        assertEquals("p1x2::p1x2x1::def1x2x1", getDerolvedName(model.def1x2x1, model.def1x1x1));
        assertEquals("p1x2::def1x2", getDerolvedName(model.def1x2, model.def1x1x1));

        // In another package
        assertEquals("p2x1x1::def2x1x1", getDerolvedName(model.def2x1x1, model.def1x1x1));
        assertEquals("def2x1", getDerolvedName(model.def2x1, model.def1x1x1));
        assertEquals(P2_DEF2, getDerolvedName(model.def2, model.def1x1x1));

    }

    @DisplayName("Check the deresolved names with a NamespaceImport in intermediate level with recursive import")
    @Test
    public void omportInterRecursiveNamespaceImpot() {

        var model = new TestModel();

        // Import P2 in P1
        NamespaceImport namespaceImport = builder.createIn(NamespaceImport.class, model.p1x1);
        namespaceImport.setImportedNamespace(model.p2x1);
        namespaceImport.setIsRecursive(true);

        // Descendant
        assertEquals(P1X1_P1X1X1_DEF1X1X1, getDerolvedName(model.def1x1x1, model.def1));
        assertEquals(P1X1_P1X1X1_DEF1X1X1, getDerolvedName(model.def1x1x1, model.def1));
        assertEquals("p1x1::def1x1", getDerolvedName(model.def1x1, model.def1));

        // In sibling package but no import
        assertEquals(P2_DEF2, getDerolvedName(model.def2, model.def1));
        assertEquals("p2::p2x1::def2x1", getDerolvedName(model.def2x1, model.def1));
        assertEquals("p2::p2x1::p2x1x1::def2x1x1", getDerolvedName(model.def2x1x1, model.def1));

        // Ancestor
        assertEquals(DEF1, getDerolvedName(model.def1, model.def1x1x1));
        assertEquals("def1x1", getDerolvedName(model.def1x1, model.def1x1x1));
        assertEquals("def1x1x1", getDerolvedName(model.def1x1x1, model.def1x1x1));

        // In sibling ancestor
        assertEquals("p1x2::p1x2x1::def1x2x1", getDerolvedName(model.def1x2x1, model.def1x1x1));
        assertEquals("p1x2::def1x2", getDerolvedName(model.def1x2, model.def1x1x1));

        // In another package with import of p2x1
        assertEquals("def2x1x1", getDerolvedName(model.def2x1x1, model.def1x1x1));
        assertEquals("def2x1", getDerolvedName(model.def2x1, model.def1x1x1));
        assertEquals(P2_DEF2, getDerolvedName(model.def2, model.def1x1x1));

    }

    /**
     * <pre>
     * package p1 {
            part def1;
            package p1x1 {
                part def1x1;
                package p1x1x1 {
                    part def def1x1x1;
                }
            }
        
            package p1x2 {
                part def def1x2;
                package p1x2x1 {
                    part def def1x2x1;
                }
            }
        }
        
        package p2 {
            part def def2;
            package p2x1 {
                part def def2x1;
                package p2x1x1 {
                    part def def2x1x1;
                }
            }
        }
     * </pre>
     * 
     * @author Arthur Daussy
     */
    private static final class TestModel {

        private ModelBuilder builder = new ModelBuilder();

        private Package p1;

        private Package p1x1;

        private Package p1x2;

        private Package p2;

        private Package p2x1;

        private PartDefinition def1;

        private PartDefinition def1x1;

        private PartDefinition def1x1x1;

        private PartDefinition def1x2;

        private PartDefinition def1x2x1;

        private PartDefinition def2;

        private PartDefinition def2x1;

        private PartDefinition def2x1x1;

        private TestModel() {
            build();
        }

        private void build() {

            p1 = createPackageAndPartDef("1", null, this::setDef1);
            p1x1 = createPackageAndPartDef("1x1", p1, this::setDef1x1);
            createPackageAndPartDef("1x1x1", p1x1, this::setDef1x1x1);

            p1x2 = createPackageAndPartDef("1x2", p1, this::setDef1x2);
            createPackageAndPartDef("1x2x1", p1x2, this::setDef1x2x1);

            p2 = createPackageAndPartDef("2", null, this::setDef2);
            p2x1 = createPackageAndPartDef("2x1", p2, this::setDef2x1);
            createPackageAndPartDef("2x1x1", p2x1, this::setDef2x1x1);

        }

        private Package createPackageAndPartDef(String name, Element parent, Consumer<PartDefinition> consumer) {
            Package pack = builder.createInWithName(Package.class, parent, "p" + name);
            PartDefinition def = builder.createInWithName(PartDefinition.class, pack, "def" + name);
            consumer.accept(def);

            return pack;
        }

        private void setDef1(PartDefinition def1) {
            this.def1 = def1;
        }

        private void setDef1x1(PartDefinition def1x1) {
            this.def1x1 = def1x1;
        }

        private void setDef1x1x1(PartDefinition def1x1x1) {
            this.def1x1x1 = def1x1x1;
        }

        private void setDef1x2(PartDefinition def1x2) {
            this.def1x2 = def1x2;
        }

        private void setDef1x2x1(PartDefinition def1x2x1) {
            this.def1x2x1 = def1x2x1;
        }

        private void setDef2(PartDefinition def2) {
            this.def2 = def2;
        }

        private void setDef2x1(PartDefinition def2x1) {
            this.def2x1 = def2x1;
        }

        private void setDef2x1x1(PartDefinition def2x1x1) {
            this.def2x1x1 = def2x1x1;
        }

    }
}
