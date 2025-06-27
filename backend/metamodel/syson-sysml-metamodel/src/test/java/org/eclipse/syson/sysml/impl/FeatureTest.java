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
package org.eclipse.syson.sysml.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.syson.sysml.Feature;
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

}
