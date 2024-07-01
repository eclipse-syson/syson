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
 * @see https://github.com/Systems-Modeling/SysML-v2-Release/blob/master/sysml/src/examples/Simple%20Tests/ItemTest.sysml
 *
 * @author Arthur Daussy
 */
public class ItemTest {

    private ModelBuilder builder;

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
        build();
    }

    private void build() {
        itemTest = builder.createWithName(Package.class, "ItemTest");

        f = builder.createInWithName(ItemUsage.class, itemTest, "f");

        aDef = builder.createInWithName(ItemDefinition.class, itemTest, "A");
        builder.setType(f, aDef);

        b = builder.createInWithName(ItemUsage.class, aDef, "b");

        c = builder.createInWithName(PartUsage.class, aDef, "c");
        c.getOwningMembership().setVisibility(VisibilityKind.PROTECTED);

        bDef = builder.createInWithName(ItemDefinition.class, itemTest, "B");
        bDef.setIsAbstract(true);
        builder.setType(b, bDef);

        PartUsage a = builder.createInWithName(PartUsage.class, bDef, "a");
        a.setIsAbstract(true);
        builder.setType(a, aDef);

        cDef = builder.createInWithName(PartDefinition.class, itemTest, "C");
        cDef.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);
        builder.setType(c, cDef);

        pDef = builder.createInWithName(PortDefinition.class, itemTest, "P");

        y = builder.createInWithName(ReferenceUsage.class, cDef, "y");
        y.setDirection(FeatureDirectionKind.IN);
        y.getOwningMembership().setVisibility(VisibilityKind.PRIVATE);

        a1 = builder.createInWithName(ItemUsage.class, pDef, "a1");
        a1.setDirection(FeatureDirectionKind.IN);
        builder.setType(a1, aDef);
        a2 = builder.createInWithName(ItemUsage.class, pDef, "a2");
        a2.setDirection(FeatureDirectionKind.OUT);
        builder.setType(a2, aDef);

        builder.setType(y, aDef);
        builder.setType(y, bDef);

    }

    public ModelBuilder getBuilder() {
        return builder;
    }

    public Package getItemTest() {
        return itemTest;
    }

    public ItemDefinition getaDef() {
        return aDef;
    }

    public ItemUsage getB() {
        return b;
    }

    public PartUsage getC() {
        return c;
    }

    public ItemDefinition getbDef() {
        return bDef;
    }

    public PartDefinition getcDef() {
        return cDef;
    }

    public PortDefinition getpDef() {
        return pDef;
    }

}
