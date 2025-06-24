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
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Test model with items.
 *
 * <pre>
    action externalAction {
                in item i00;
            }

            package P0 {
                item def ID1 {
                    in item id1 : ID2;
                }

                item def ID2 {
                    in item id2;
                }

                item def ID3 {
                    in item id3;
                }

                item def ID4 :> ID3{
                }
            }

            action a0 {
                item i0 : ID4;

                action a1 {

                    in item i1;
                    in item i2 : P0::ID1;

                    action a11 {
                        in item i11;
                        in item i11Test;
                    }
                }
            }
 * </pre>
 *
 * @author Arthur Daussy
 */
public class ItemsModelTest {
    private final ModelBuilder builder = new ModelBuilder();

    private ResourceSetImpl resourceSet;

    private JsonResource resource;

    private Namespace rootNamespace;

    private ActionUsage externalAction;

    private ItemUsage i00;

    private Package p0;

    private ItemDefinition idef1;

    private ItemUsage id1;

    private ItemDefinition idef2;

    private ItemUsage id2;

    private ActionUsage a0;

    private ItemUsage i0;

    private ActionUsage a1;

    private ItemUsage i1;

    private ItemUsage i2;

    private ActionUsage a11;

    private ItemUsage i11;

    private ItemUsage i11Test;

    private ItemDefinition idef3;

    private ItemUsage id3;

    private ItemDefinition idef4;

    public ItemsModelTest() {
        this.build();
    }

    private void build() {
        this.resourceSet = new ResourceSetImpl();
        this.resource = new JSONResourceFactory().createResource(URI.createURI("fakeURI://testModel"));
        this.resourceSet.getResources().add(this.resource);
        this.rootNamespace = this.builder.createRootNamespace();
        this.resource.getContents().add(this.rootNamespace);

        this.externalAction = this.builder.createInWithName(ActionUsage.class, this.rootNamespace, "externalAction");
        this.i00 = this.createInItemUsageIn(this.externalAction, "i00");


        this.p0 = this.builder.createInWithName(Package.class, this.rootNamespace, "P0");

        this.idef1 = this.builder.createInWithName(ItemDefinition.class, this.p0, "ID1");
        this.id1 = this.createInItemUsageIn(this.idef1, "id1");

        this.idef2 = this.builder.createInWithName(ItemDefinition.class, this.p0, "ID2");
        this.id2 = this.createInItemUsageIn(this.idef2, "id2");

        this.idef3 = this.builder.createInWithName(ItemDefinition.class, this.p0, "ID3");
        this.id3 = this.createInItemUsageIn(this.idef3, "id3");

        this.idef4 = this.builder.createInWithName(ItemDefinition.class, this.p0, "ID4");

        this.builder.addSubclassification(this.idef4, this.idef3);

        this.a0 = this.builder.createInWithName(ActionUsage.class, this.rootNamespace, "a0");
        this.i0 = this.createInItemUsageIn(this.a0, "i0");
        this.builder.setType(this.i0, this.idef4);

        this.a1 = this.builder.createInWithName(ActionUsage.class, this.a0, "a1");


        this.i1 = this.createInItemUsageIn(this.a1, "i1");
        this.i2 = this.createInItemUsageIn(this.a1, "i2");

        this.a11 = this.builder.createInWithName(ActionUsage.class, this.a1, "a11");

        this.i11 = this.createInItemUsageIn(this.a11, "i11");
        this.i11Test = this.createInItemUsageIn(this.a11, "i11Test");

        this.builder.setType(this.id1, this.idef2);
        this.builder.setType(this.i2, this.idef1);

    }

    private ItemUsage createInItemUsageIn(Element parent, String name) {
        ItemUsage result = this.builder.createInWithName(ItemUsage.class, parent, name);
        result.setDirection(FeatureDirectionKind.IN);
        return result;
    }


    public ResourceSetImpl getResourceSet() {
        return this.resourceSet;
    }

    public JsonResource getResource() {
        return this.resource;
    }

    public ModelBuilder getBuilder() {
        return this.builder;
    }

    public Namespace getRootNamespace() {
        return this.rootNamespace;
    }

    public ActionUsage getExternalAction() {
        return this.externalAction;
    }

    public ItemUsage getI00() {
        return this.i00;
    }

    public Package getP0() {
        return this.p0;
    }

    public ItemDefinition getIdef1() {
        return this.idef1;
    }

    public ItemUsage getId1() {
        return this.id1;
    }

    public ItemDefinition getIdef2() {
        return this.idef2;
    }

    public ItemUsage getId2() {
        return this.id2;
    }

    public ActionUsage getA0() {
        return this.a0;
    }

    public ItemUsage getI0() {
        return this.i0;
    }

    public ActionUsage getA1() {
        return this.a1;
    }

    public ItemUsage getI1() {
        return this.i1;
    }

    public ItemUsage getI2() {
        return this.i2;
    }

    public ActionUsage getA11() {
        return this.a11;
    }

    public ItemUsage getI11() {
        return this.i11;
    }

    public ItemUsage getI11Test() {
        return this.i11Test;
    }

    public ItemUsage getId3() {
        return this.id3;
    }

    public ItemDefinition getIdef3() {
        return this.idef3;
    }

    public ItemDefinition getIdef4() {
        return this.idef4;
    }
}
