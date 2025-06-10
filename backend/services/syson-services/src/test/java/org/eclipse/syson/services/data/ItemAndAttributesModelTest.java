/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.services.data;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Test model with attributes and items.
 *
 * <pre>
    package RootPackage {
        action a0 {
            in item i0_1;
        }

        item def I1 {
            in item i1_1;
        }

        private package P0 {
            item def I2 {
                in item i2_1 : I3;
            }

            item def I3 {
                in item i3_1;
            }
        }

        action a1 :> a0 {
            in item a1_1;
            in item a1_2 : I1;
            in item a1_3 : P0::I2;

            action a2 {
                in item a2_1;
            }
        }

        part p1 {
            attribute x1;
            attribute x2;
            attribute x3;

            part p1_1 {
                attribute x1;
                attribute x2;
                attribute x3;
            }
        }
    }
 * </pre>
 *
 * @author Arthur Daussy
 */
public class ItemAndAttributesModelTest {

    private final ModelBuilder builder = new ModelBuilder();

    private ResourceSetImpl resourceSet;

    private JsonResource resource;

    private ActionUsage a0;

    private ActionUsage a10;

    private ItemUsage i01;

    private ItemUsage i11;

    private ItemDefinition i1Def;

    private Package rootPackage;

    private Package p0;

    private ItemDefinition i2Def;

    private ItemUsage i21;

    private ItemDefinition i3Def;

    private ItemUsage i31;

    private ActionUsage a1;

    private AttributeUsage x1;

    private AttributeUsage x2;

    private AttributeUsage x3;

    private ItemUsage a11;

    private ItemUsage a12;

    private ItemUsage a13;

    private ActionUsage a2;

    private AttributeUsage a2x1;

    private AttributeUsage a2x2;

    private AttributeUsage a2x3;

    private ItemUsage a21;

    private Namespace rootNamespace;

    private PartUsage p1;

    private PartUsage p11;

    public ItemAndAttributesModelTest() {
        this.build();
    }

    private void build() {
        this.resourceSet = new ResourceSetImpl();
        this.resource = new JSONResourceFactory().createResource(URI.createURI("fakeURI://testModel"));
        this.resourceSet.getResources().add(this.resource);
        this.rootNamespace = this.builder.createRootNamespace();
        this.resource.getContents().add(this.rootNamespace);
        this.rootPackage = this.builder.createInWithName(Package.class, this.rootNamespace, "RootPackage");

        this.a0 = this.builder.createInWithName(ActionUsage.class, this.rootPackage, "a0");
        this.i01 = this.createInItemUsageIn(this.a0, "i0_1");

        this.i1Def = this.builder.createInWithName(ItemDefinition.class, this.rootPackage, "I1");
        this.i11 = this.createInItemUsageIn(this.i1Def, "i1_1");

        this.p0 = this.builder.createInWithName(Package.class, this.rootPackage, "P0");

        this.i2Def = this.builder.createInWithName(ItemDefinition.class, this.p0, "I2");

        this.i3Def = this.builder.createInWithName(ItemDefinition.class, this.p0, "I3");
        this.i31 = this.createInItemUsageIn(this.i3Def, "i3_1");

        this.i21 = this.createInItemUsageInWithType(this.i2Def, "i2_1", this.i3Def);

        this.a1 = this.builder.createInWithName(ActionUsage.class, this.rootPackage, "a1");
        this.builder.addSubsetting(this.a1, this.a0);


        this.a11 = this.createInItemUsageIn(this.a1, "a1_1");
        this.a12 = this.createInItemUsageInWithType(this.a1, "a1_2", this.i1Def);
        this.a13 = this.createInItemUsageInWithType(this.a1, "a1_3", this.i2Def);

        this.a2 = this.builder.createInWithName(ActionUsage.class, this.a1, "a2");

        this.a21 = this.createInItemUsageIn(this.a2, "a2_1");

        this.p1 = this.builder.createInWithName(PartUsage.class, this.rootPackage, "p1");

        this.x1 = this.builder.createInWithName(AttributeUsage.class, this.p1, "x1");
        this.x2 = this.builder.createInWithName(AttributeUsage.class, this.p1, "x2");
        this.x3 = this.builder.createInWithName(AttributeUsage.class, this.p1, "x3");

        this.p11 = this.builder.createInWithName(PartUsage.class, this.p1, "p1_1");

        this.a2x1 = this.builder.createInWithName(AttributeUsage.class, this.p11, "x1");
        this.a2x2 = this.builder.createInWithName(AttributeUsage.class, this.p11, "x2");
        this.a2x3 = this.builder.createInWithName(AttributeUsage.class, this.p11, "x3");

    }

    private ItemUsage createInItemUsageIn(Element parent, String name) {
        ItemUsage result = this.builder.createInWithName(ItemUsage.class, this.a0, name);
        result.setDirection(FeatureDirectionKind.IN);
        return result;
    }

    private ItemUsage createInItemUsageInWithType(Element parent, String name, Type type) {
        ItemUsage result = this.builder.createInWithName(ItemUsage.class, this.a0, name);
        this.builder.setType(result, type);
        result.setDirection(FeatureDirectionKind.IN);
        return result;
    }

    public ResourceSetImpl getResourceSet() {
        return this.resourceSet;
    }

    public JsonResource getResource() {
        return this.resource;
    }

    public ActionUsage getA0() {
        return this.a0;
    }

    public ActionUsage getA10() {
        return this.a10;
    }

    public PartUsage getP1() {
        return this.p1;
    }

    public PartUsage getP11() {
        return this.p11;
    }

    public ItemUsage getI01() {
        return this.i01;
    }

    public ItemUsage getI11() {
        return this.i11;
    }

    public ItemDefinition getI1Def() {
        return this.i1Def;
    }

    public Package getRootPackage() {
        return this.rootPackage;
    }

    public Package getP0() {
        return this.p0;
    }

    public ItemDefinition getI2Def() {
        return this.i2Def;
    }

    public ItemUsage getI21() {
        return this.i21;
    }

    public ItemDefinition getI3Def() {
        return this.i3Def;
    }

    public ItemUsage getI31() {
        return this.i31;
    }

    public ActionUsage getA1() {
        return this.a1;
    }

    public AttributeUsage getX1() {
        return this.x1;
    }

    public AttributeUsage getX2() {
        return this.x2;
    }

    public AttributeUsage getX3() {
        return this.x3;
    }

    public ItemUsage getA11() {
        return this.a11;
    }

    public ItemUsage getA12() {
        return this.a12;
    }

    public ItemUsage getA13() {
        return this.a13;
    }

    public ActionUsage getA2() {
        return this.a2;
    }

    public AttributeUsage getA2x1() {
        return this.a2x1;
    }

    public AttributeUsage getA2x2() {
        return this.a2x2;
    }

    public AttributeUsage getA2x3() {
        return this.a2x3;
    }

    public ItemUsage getA21() {
        return this.a21;
    }

}
