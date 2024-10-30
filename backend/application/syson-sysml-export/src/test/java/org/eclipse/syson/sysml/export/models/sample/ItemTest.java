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
package org.eclipse.syson.sysml.export.models.sample;

import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Implementation with our meta model of the ItemTest from the examples provided by the OMG (examples/Simple
 * Tests/ItemTest.sysml).
 *
 * <pre>
 * package ItemTest {

    item f: A;

    public item def A {
        item b: B;
        protected ref part c: C;
    }

    abstract item def B {
        public abstract part a: A;
    }

    private part def C {
        private in ref y: A, B;
    }

    port def P {
        in item a1: A;
        out item a2: A;
    }

}
 * </pre>
 *
 * The content of UseCaseTest.sysml that have been copied below is under LGPL-3.0-only license. The LGPL-3.0-only
 * license is accessible at the root of this repository, in the LICENSE-LGPL file.
 *
 * @see https://github.com/Systems-Modeling/SysML-v2-Release/blob/master/sysml/src/examples/Simple%20Tests/ItemTest.sysml
 *
 * @author Arthur Daussy
 */
public class ItemTest {

    private final ModelBuilder builder;

    private Package itemTest;

    private ItemDefinition aDef;

    private ItemUsage b;

    private PartUsage c;

    private ItemDefinition bDef;

    private PartDefinition cDef;

    private PortDefinition pDef;

    private ReferenceUsage y;

    private ItemUsage a1;

    private ItemUsage a2;

    private ItemUsage f;

    public ItemTest(ModelBuilder builder) {
        super();
        this.builder = builder;
        this.build();
    }

    private void build() {
        this.itemTest = this.builder.createWithName(Package.class, "ItemTest");

        this.f = this.builder.createInWithName(ItemUsage.class, this.itemTest, "f");

        this.aDef = this.builder.createInWithName(ItemDefinition.class, this.itemTest, "A");
        this.builder.setType(this.f, this.aDef);

        this.b = this.builder.createInWithName(ItemUsage.class, this.aDef, "b");

        this.c = this.builder.createInWithName(PartUsage.class, this.aDef, "c");
        this.c.getOwningMembership().setVisibility(VisibilityKind.PROTECTED);

        this.bDef = this.builder.createInWithName(ItemDefinition.class, this.itemTest, "B");
        this.bDef.setIsAbstract(true);
        this.builder.setType(this.b, this.bDef);

        PartUsage a = this.builder.createInWithName(PartUsage.class, this.bDef, "a");
        a.setIsAbstract(true);
        this.builder.setType(a, this.aDef);

        this.cDef = this.builder.createInWithName(PartDefinition.class, this.itemTest, "C");
        this.cDef.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
        this.builder.setType(this.c, this.cDef);

        this.pDef = this.builder.createInWithName(PortDefinition.class, this.itemTest, "P");

        this.y = this.builder.createInWithName(ReferenceUsage.class, this.cDef, "y");
        this.y.setDirection(FeatureDirectionKind.IN);
        this.y.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);

        this.a1 = this.builder.createInWithName(ItemUsage.class, this.pDef, "a1");
        this.a1.setDirection(FeatureDirectionKind.IN);
        this.builder.setType(this.a1, this.aDef);
        this.a2 = this.builder.createInWithName(ItemUsage.class, this.pDef, "a2");
        this.a2.setDirection(FeatureDirectionKind.OUT);
        this.builder.setType(this.a2, this.aDef);

        this.builder.setType(this.y, this.aDef);
        this.builder.setType(this.y, this.bDef);

    }

    public ModelBuilder getBuilder() {
        return this.builder;
    }

    public Package getItemTest() {
        return this.itemTest;
    }

    public ItemDefinition getaDef() {
        return this.aDef;
    }

    public ItemUsage getB() {
        return this.b;
    }

    public PartUsage getC() {
        return this.c;
    }

    public ItemDefinition getbDef() {
        return this.bDef;
    }

    public PartDefinition getcDef() {
        return this.cDef;
    }

    public PortDefinition getpDef() {
        return this.pDef;
    }

}
