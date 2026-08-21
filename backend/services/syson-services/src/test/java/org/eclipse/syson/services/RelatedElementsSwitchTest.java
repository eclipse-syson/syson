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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Succession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RelatedElementsSwitch}'s relationship-deletion rules.
 * <p>
 * The switch identifies semantic relationships that must be deleted together when one of their referenced elements
 * is removed. These tests cover the supported relationship kinds, their matching structural features, and irrelevant
 * features that must not produce additional deletions. They provide focused regression coverage for deletion planning
 * using only in-memory SysML objects.
 * </p>
 * <p>
 * They do not perform a deletion, persist a model, or verify the Explorer and diagram user interfaces. Those effects
 * are performed by higher-level services and are outside the scope of this switch unit test.
 * </p>
 *
 * @author arichard
 */
class RelatedElementsSwitchTest {

    @DisplayName("GIVEN relationships, WHEN their referenced feature is deleted, THEN only the corresponding relationships are returned")
    @Test
    void testSimpleRelationshipCases() {
        Annotation annotation = SysmlFactory.eINSTANCE.createAnnotation();
        FeatureTyping featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        ReferenceSubsetting referenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        Subclassification subclassification = SysmlFactory.eINSTANCE.createSubclassification();
        Subsetting subsetting = SysmlFactory.eINSTANCE.createSubsetting();

        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement()).caseAnnotation(annotation)).containsExactly(annotation);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getFeatureTyping_Type()).caseFeatureTyping(featureTyping)).containsExactly(featureTyping);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature()).caseRedefinition(redefinition)).containsExactly(redefinition);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature()).caseReferenceSubsetting(referenceSubsetting))
                .containsExactly(referenceSubsetting);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getSubclassification_Superclassifier()).caseSubclassification(subclassification))
                .containsExactly(subclassification);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature()).caseSubsetting(subsetting)).containsExactly(subsetting);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getFeatureTyping_Type()).caseAnnotation(annotation)).isEmpty();
    }

    @DisplayName("GIVEN a dependency inside a membership, WHEN a client or supplier is deleted, THEN both objects are returned")
    @Test
    void testDependencyCases() {
        Dependency dependency = SysmlFactory.eINSTANCE.createDependency();
        Membership membership = SysmlFactory.eINSTANCE.createMembership();
        membership.getOwnedRelatedElement().add(dependency);

        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getDependency_Client()).caseDependency(dependency)).containsExactlyInAnyOrder(dependency, membership);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getDependency_Supplier()).caseDependency(dependency)).containsExactlyInAnyOrder(dependency, membership);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getFeatureTyping_Type()).caseDependency(dependency)).isEmpty();
    }

    @DisplayName("GIVEN a succession, WHEN either end is deleted, THEN the succession is returned unless an end was already deleted")
    @Test
    void testSuccessionCases() {
        Succession succession = SysmlFactory.eINSTANCE.createSuccession();

        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getRelationship_Source()).caseSuccession(succession)).containsExactly(succession);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getRelationship_Target()).caseSuccession(succession)).containsExactly(succession);
        assertThat(new RelatedElementsSwitch(SysmlPackage.eINSTANCE.getFeatureTyping_Type()).caseSuccession(succession)).isEmpty();
    }
}
