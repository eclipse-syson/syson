/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the resolution of the ends of a connector.
 * <p>
 * KerML defines {@code endFeature} as the owned features having {@code isEnd = true}. An {@code EndFeatureMembership}
 * is one syntactic form implying it, but the ends declared with the {@code end} keyword inside a connection body are
 * owned through a plain {@link FeatureMembership}, so both forms have to be resolved.
 * </p>
 *
 * @author kabayama
 */
public class ConnectorEndTest {

    @Test
    @DisplayName("GIVEN a connection whose ends are owned through EndFeatureMemberships, WHEN resolving its ends, THEN both ends are resolved")
    public void connectorEndsOwnedThroughEndFeatureMemberships() {
        var first = this.createPart("part1");
        var second = this.createPart("part2");
        var connection = SysmlFactory.eINSTANCE.createConnectionUsage();
        connection.getOwnedRelationship().add(this.createEnd(first, true));
        connection.getOwnedRelationship().add(this.createEnd(second, true));

        this.checkEnds(connection, first, second);
    }

    @Test
    @DisplayName("GIVEN a connection whose ends are owned through plain FeatureMemberships, WHEN resolving its ends, THEN both ends are resolved")
    public void connectorEndsOwnedThroughFeatureMemberships() {
        var first = this.createPart("part1");
        var second = this.createPart("part2");
        var connection = SysmlFactory.eINSTANCE.createConnectionUsage();
        connection.getOwnedRelationship().add(this.createEnd(first, false));
        connection.getOwnedRelationship().add(this.createEnd(second, false));

        this.checkEnds(connection, first, second);
    }

    @Test
    @DisplayName("GIVEN a connection owning a feature which is not an end, WHEN resolving its ends, THEN that feature is not returned")
    public void ownedFeatureWhichIsNotAnEndIsNotAConnectorEnd() {
        var connection = SysmlFactory.eINSTANCE.createConnectionUsage();
        var notAnEnd = SysmlFactory.eINSTANCE.createReferenceUsage();
        notAnEnd.setIsEnd(false);
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        featureMembership.getOwnedRelatedElement().add(notAnEnd);
        connection.getOwnedRelationship().add(featureMembership);

        assertThat(connection.getConnectorEnd()).isEmpty();
        assertThat(connection.getRelatedFeature()).isEmpty();
        assertThat(connection.getSourceFeature()).isNull();
        assertThat(connection.getTargetFeature()).isEmpty();
    }

    private void checkEnds(ConnectionUsage connection, PartUsage expectedSource, PartUsage expectedTarget) {
        assertThat(connection.getConnectorEnd()).hasSize(2);
        assertThat(connection.getRelatedFeature()).containsExactly(expectedSource, expectedTarget);
        assertThat(connection.getSourceFeature()).isSameAs(expectedSource);
        assertThat(connection.getTargetFeature()).containsExactly(expectedTarget);
    }

    private PartUsage createPart(String name) {
        var part = SysmlFactory.eINSTANCE.createPartUsage();
        part.setDeclaredName(name);
        return part;
    }

    /**
     * Builds the equivalent of {@code end ::> referencedFeature;}.
     *
     * @param referencedFeature
     *            the feature referenced by the end
     * @param useEndFeatureMembership
     *            whether the end is owned through an {@code EndFeatureMembership} or a plain {@link FeatureMembership}
     * @return the membership owning the created end
     */
    private FeatureMembership createEnd(Feature referencedFeature, boolean useEndFeatureMembership) {
        var end = SysmlFactory.eINSTANCE.createReferenceUsage();
        end.setIsEnd(true);
        var referenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        referenceSubsetting.setReferencedFeature(referencedFeature);
        end.getOwnedRelationship().add(referenceSubsetting);

        FeatureMembership membership;
        if (useEndFeatureMembership) {
            membership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        } else {
            membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        }
        membership.getOwnedRelatedElement().add(end);
        return membership;
    }
}
