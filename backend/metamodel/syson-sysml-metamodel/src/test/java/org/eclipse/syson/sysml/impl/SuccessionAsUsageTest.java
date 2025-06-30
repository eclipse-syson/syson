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
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link SuccessionAsUsageTest}.
 *
 * @author Arthur Daussy
 */
public class SuccessionAsUsageTest {

    @Test
    @DisplayName("GIVEN a SuccessionAsUsage using a implicit source feature, WHEN checking for the source feature, then the previous feature should be returned")
    public void impliciteSourceFeature() {
        /**
         * <pre>
         * action def ActionDef1 {
                action a2;
                action a1;
                then a2;
            }
         * </pre>
         */
        ModelBuilder builder = new ModelBuilder();

        ActionDefinition actionDef = builder.createWithName(ActionDefinition.class, "ActionDef1");

        ActionUsage a2 = builder.createInWithName(ActionUsage.class, actionDef, "a2");
        ActionUsage a1 = builder.createInWithName(ActionUsage.class, actionDef, "a1");

        SuccessionAsUsage successionAsUsage = builder.createSuccessionAsUsage(SuccessionAsUsage.class, actionDef, null, a2);

        this.checkSuccessionUsage(a1, true, a2, false, successionAsUsage);
    }

    @Test
    @DisplayName("GIVEN a SuccessionAsUsage using a implicit source feature define in a library, WHEN checking for the source feature, then the previous feature should be returned")
    public void implicitSourceToStartFeature() {
        /**
         * <pre>
         * action start { // Emulate the start Action from standard librairy
         * }
         * action def ActionDef2 {
                action a2;
                from start
                then a2;
            }
         * </pre>
         */
        ModelBuilder builder = new ModelBuilder();

        ActionUsage start = builder.createWithName(ActionUsage.class, "start");

        ActionDefinition actionDef = builder.createWithName(ActionDefinition.class, "ActionDef2");

        ActionUsage a2 = builder.createInWithName(ActionUsage.class, actionDef, "a2");

        // This how the start element is "import" as a feature inside a ActionDef
        builder.createIn(Membership.class, actionDef).setMemberElement(start);

        SuccessionAsUsage successionAsUsage = builder.createSuccessionAsUsage(SuccessionAsUsage.class, actionDef, null, a2);

        this.checkSuccessionUsage(start, true, a2, false, successionAsUsage);
    }

    @Test
    @DisplayName("GIVEN a SuccessionAsUsage using a explicit source feature, WHEN checking for the source feature, then explicit source feature should be returned")
    public void explictSourceTargetFeature() {
        /**
         * <pre>
         * action def ActionDef1 {
                action a2;
                action a1;
                first a1 then a2;

            }
         * </pre>
         */
        ModelBuilder builder = new ModelBuilder();

        ActionDefinition actionDef = builder.createWithName(ActionDefinition.class, "ActionDef1");

        ActionUsage a2 = builder.createInWithName(ActionUsage.class, actionDef, "a2");
        ActionUsage a1 = builder.createInWithName(ActionUsage.class, actionDef, "a1");

        SuccessionAsUsage successionAsUsage = builder.createSuccessionAsUsage(SuccessionAsUsage.class, actionDef, a1, a2);

        this.checkSuccessionUsage(a1, false, a2, false, successionAsUsage);
    }

    private void checkSuccessionUsage(Feature expectedSource, boolean expectedSourceIsImplied, Feature expectedTarget, boolean expectedTargetIsImplied, SuccessionAsUsage toTest) {
        assertEquals(2, toTest.getEndFeature().size());

        Feature sourceFeatureRef = toTest.getEndFeature().get(0);
        assertTrue(sourceFeatureRef instanceof ReferenceUsage);
        ReferenceUsage sourceRefUsage = (ReferenceUsage) sourceFeatureRef;

        this.checkReferenceUsage(expectedSourceIsImplied, expectedSource, sourceRefUsage);

        Feature targetFeatureRef = toTest.getEndFeature().get(1);
        assertTrue(targetFeatureRef instanceof ReferenceUsage);
        ReferenceUsage targetRefUsage = (ReferenceUsage) targetFeatureRef;

        this.checkReferenceUsage(expectedTargetIsImplied, expectedTarget, targetRefUsage);

        this.checkEndFeatures(expectedSource, expectedTarget, toTest);

        assertThat(toTest.getSource()).hasSize(1)
                .allMatch(f -> f == expectedSource);
        assertThat(toTest.getTarget()).hasSize(1)
                .allMatch(f -> f == expectedTarget);
    }

    private void checkEndFeatures(Feature expectedSource, Feature expectedTarget, SuccessionAsUsage toTest) {
        assertThat(toTest.getSourceFeature()).as("The source feature should be computed to " + expectedSource.getName())
                .isEqualTo(expectedSource);
        assertThat(toTest.getTargetFeature()).as("The target feature should be explicity set to " + expectedTarget.getName())
                .hasSize(1)
                .allMatch(s -> s == expectedTarget);
    }

    private void checkReferenceUsage(boolean expectedIsImplied, Feature expectedTarget, ReferenceUsage referenceUsage) {
        String impliedMsg;
        if (expectedIsImplied) {
            impliedMsg = "implied";
        } else {
            impliedMsg = "not implied";

        }
        assertThat(referenceUsage.getOwnedSubsetting()).as("The ReferenceUsage should have only one " + impliedMsg + " subsetting targeting " + expectedTarget.getName())
                .hasSize(1)
                .allMatch(sub -> expectedIsImplied == sub.isIsImplied())
                .allMatch(sub -> sub.getSubsettedFeature() == expectedTarget);
    }
}
