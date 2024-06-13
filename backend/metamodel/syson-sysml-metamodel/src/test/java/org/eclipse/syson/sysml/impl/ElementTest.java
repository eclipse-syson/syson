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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link  ElementImpl}.
 *
 * @author gescande
 */
public class ElementTest {

    /**
     * Test model
     *
     * <pre>
     * namespace {
     * package p1 {
     *      doc / * This is a doc * /
     *      part def def1;
     *      package 'p1 x1' {
     *          part def 'def 1x1';
     *          package 'p1x 1x1' {
     *              part def def1x1x1;
     *          }
     *          private part def 'private def1x1';
     *      }
     *  }
     * }
     * </pre>
     *
     * @author Arthur Daussy
     */
    private class TestModel {
        private final ModelBuilder builder = new ModelBuilder();

        private Namespace root;
        private Package p1;
        private PartDefinition def1;
        private Package p1x1;
        private PartDefinition def1x1;
        private PartDefinition privatedef1x1;

        TestModel() {
            this.build();
        }

        private void build() {
            this.root = this.builder.createWithName(Namespace.class, null);
            this.p1 = this.builder.createInWithName(Package.class, this.root, "p1");
            this.builder.createInWithName(Documentation.class, this.p1, "documentation");
            this.def1 = this.builder.createInWithName(PartDefinition.class, this.p1, "Def1");
            this.p1x1 = this.builder.createInWithName(Package.class, this.p1, "p1 x1");
            this.def1x1 = this.builder.createInWithName(PartDefinition.class, this.p1x1, "def 1x1");
            this.builder.createInWithName(Package.class, this.p1x1, "p1x 1x1");
            this.privatedef1x1 = this.builder.createInWithName(PartDefinition.class, this.p1x1, "private def1x1");
            this.privatedef1x1.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
        }
    }

    @DisplayName("Check qualifiedName feature")
    @Test
    public void getQualifiedNameTest() {
        var testModel = new TestModel();
        assertNull(testModel.root.getQualifiedName());
        assertEquals("p1", testModel.p1.getQualifiedName());
        assertEquals("p1::Def1", testModel.def1.getQualifiedName());
        assertEquals("p1::'p1 x1'::'def 1x1'", testModel.def1x1.getQualifiedName());
        assertEquals("p1::'p1 x1'::'private def1x1'", testModel.privatedef1x1.getQualifiedName());
    }

    @DisplayName("Check documentation feature")
    @Test
    public void getDocumentationTest() {
        var testModel = new TestModel();
        assertNotNull(testModel.p1.getDocumentation());
        assertFalse(testModel.p1.getDocumentation().isEmpty());
    }
}
