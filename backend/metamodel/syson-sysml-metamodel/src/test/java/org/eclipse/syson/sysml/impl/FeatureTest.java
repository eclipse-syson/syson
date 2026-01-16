/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link FeatureImpl}.
 *
 * @author Arthur Daussy
 */
public class FeatureTest {

    @DisplayName("GIVEN a Feature with FeatureChain, WHEN computing its features and type, THEN check that type of last ChainingFeature is taking into account for computeing feature type and accessible features.")
    @Test
    public void checkFeatureChainTypeAndFeatrures() {

        ModelBuilder builder = new ModelBuilder();

        // Create a Type with features
        Type t1 = builder.createWithName(Type.class, "t1");
        Feature t1f1 = builder.createInWithName(Feature.class, t1, "t1Feature");
        Type t2 = builder.createWithName(Type.class, "t2");
        Feature t2f1 = builder.createInWithName(Feature.class, t2, "t2Feature");
        Type t3 = builder.createWithName(Type.class, "t3");
        Feature t3f1 = builder.createInWithName(Feature.class, t3, "t1Feature");

        builder.setType(t1f1, t2);
        builder.setType(t2f1, t3);

        // Create a Feature Chain

        Feature featureChain = builder.createFeatureChaining(t1f1, t2f1);

        assertThat(featureChain.getFeatureTarget()).isEqualTo(t2f1);
        assertThat(featureChain.getType()).contains(t3);

        assertThat(featureChain.getFeature()).isEqualTo(List.of(t3f1));
    }

    @DisplayName("Given some Features, WHEN computing their types, THEN the computation account subsetted feature")
    @Test
    public void checkType() {
        var model = new FeatureTypeTestModel();

        assertThat(model.item1.getType())
                .hasSize(1)
                .first().isEqualTo(model.subItemDef1);

        assertThat(model.item2.getType())
                .hasSize(1)
                .first().isEqualTo(model.subItemDef1);

        assertThat(model.item3.getType())
                .hasSize(2)
                .contains(model.subItemDef1, model.itemDef2);

        assertThat(model.item3.getType())
                .hasSize(2)
                .contains(model.subItemDef1, model.itemDef2);

        assertThat(model.item4.getType())
                .hasSize(2)
                .contains(model.subItemDef1, model.itemDef2);
    }

    @DisplayName("Given some Features in a subsetting loop, WHEN computing their types, THEN the computation do not run into stackoverflow")
    @Test
    public void checkTypeWithTypeLoop() {
        var model = new FeatureTypeTestModel();

        assertThat(model.itemLoop1.getType())
                .hasSize(2)
                .contains(model.subItemDef1, model.itemDef2);

        assertThat(model.itemLoop2.getType())
                .hasSize(2)
                .contains(model.subItemDef1, model.itemDef2);

        assertThat(model.itemLoop3.getType())
                .hasSize(2)
                .contains(model.subItemDef1, model.itemDef2);
    }

    @DisplayName("Given some Features, WHEN computing their typingFeatures, THEN the computation account subsetted feature")
    @Test
    public void checkTypingFeatures() {
        var model = new FeatureTypeTestModel();

        assertThat(model.item1.typingFeatures())
                .isEmpty();
        assertThat(model.item2.typingFeatures())
                .hasSize(1)
                .first().isEqualTo(model.item1);
    }

    @DisplayName("Given some Features in a subsetting loop, WHEN computing their typingFeatures, THEN the computation do not run into stackoverflow")
    @Test
    public void checkFeaturingTypesLoop() {
        var model = new FeatureTypeTestModel();

        assertThat(model.itemLoop3.typingFeatures())
                .hasSize(1)
                .first().isEqualTo(model.itemLoop2);
        assertThat(model.itemLoop2.typingFeatures())
                .hasSize(1)
                .first().isEqualTo(model.itemLoop1);
        assertThat(model.itemLoop1.typingFeatures())
                .hasSize(1)
                .first().isEqualTo(model.itemLoop3);
    }

    /**
     * Test model use to test geType method.
     *
     * <p>
     * <pre>
     * package root {
     *  item def SuperItemDef1;
     *  item def SubItemDef1 :>SuperItemDef1;
     *  item def ItemDef2;
     *
     *  item item1 : SubItemDef1;
     *  item item2 :> item1;
     *  item item3 :> item1 : ItemDef2;
     *
     *  item itemLoop1 :> itemLoop3 : SubItemDef1;
     *  item itemLoop2 :> itemLoop1 : ItemDef2;
     *  item itemLoop3 :> itemLoop2 : SuperItemDef1;
     *  item item4 : SubItemDef1, SuperItemDef1, ItemDef2
     *
     * }
     * </pre>
     * </p>
     *
     * @author Arthur Daussy
     */
    private static class FeatureTypeTestModel {

        private final ModelBuilder builder = new ModelBuilder();
        private Package root;
        private ItemDefinition superItemDef1;
        private ItemDefinition subItemDef1;
        private ItemUsage item1;
        private ItemUsage item2;
        private ItemUsage itemLoop1;
        private ItemUsage itemLoop2;
        private ItemUsage itemLoop3;
        private ItemDefinition itemDef2;
        private ItemUsage item3;
        private ItemUsage item4;

        FeatureTypeTestModel() {
            this.build();
        }

        private void build() {
            this.root = this.builder.createWithName(Package.class, "root");

            this.superItemDef1 = this.builder.createInWithName(ItemDefinition.class, this.root, "SuperItemDef1");
            this.subItemDef1 = this.builder.createInWithName(ItemDefinition.class, this.root, "SubItemDef1");
            this.itemDef2 = this.builder.createInWithName(ItemDefinition.class, this.root, "ItemDef2");
            this.builder.addSubclassification(this.subItemDef1, this.superItemDef1);

            this.item1 = this.builder.createInWithName(ItemUsage.class, this.root, "item1");
            this.builder.setType(this.item1, this.subItemDef1);
            this.item2 = this.builder.createInWithName(ItemUsage.class, this.root, "item2");
            this.builder.addSubsetting(this.item2, this.item1);
            this.item3 = this.builder.createInWithName(ItemUsage.class, this.root, "item3");
            this.builder.addSubsetting(this.item3, this.item2);
            this.builder.setType(this.item3, this.itemDef2);

            this.itemLoop1 = this.builder.createInWithName(ItemUsage.class, this.root, "itemLoop1");
            this.builder.setType(this.itemLoop1, this.subItemDef1);
            this.itemLoop2 = this.builder.createInWithName(ItemUsage.class, this.root, "itemLoop2");
            this.builder.setType(this.itemLoop2, this.itemDef2);
            this.itemLoop3 = this.builder.createInWithName(ItemUsage.class, this.root, "itemLoop3");
            this.builder.setType(this.itemLoop3, this.superItemDef1);

            this.builder.addSubsetting(this.itemLoop1, this.itemLoop3);
            this.builder.addSubsetting(this.itemLoop2, this.itemLoop1);
            this.builder.addSubsetting(this.itemLoop3, this.itemLoop2);

            this.item4 = this.builder.createInWithName(ItemUsage.class, this.root, "item4");
            this.builder.setType(this.item4, this.subItemDef1);
            this.builder.setType(this.item4, this.superItemDef1);
            this.builder.setType(this.item4, this.itemDef2);


        }
    }

}
