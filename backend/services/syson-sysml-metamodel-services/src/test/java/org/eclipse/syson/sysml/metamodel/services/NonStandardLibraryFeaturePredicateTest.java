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
package org.eclipse.syson.sysml.metamodel.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the resource-origin policy independently of specialization paths.
 *
 * @author Arthur Daussy
 */
public class NonStandardLibraryFeaturePredicateTest {

    private final NonStandardLibraryFeaturePredicate predicate = new NonStandardLibraryFeaturePredicate();

    /**
     * Only standard SysML and KerML resources are excluded; model and business library resources remain eligible.
     */
    @ParameterizedTest
    @CsvSource({ "sysmllibrary:///test, false", "kermllibrary:///test, false", "sirius:///model, true", "file:///business.sysml, true" })
    @DisplayName("GIVEN a feature resource, WHEN filtering its origin, THEN only standard library resources are excluded")
    public void filterResourceOrigin(String uri, boolean accepted) {
        var feature = SysmlFactory.eINSTANCE.createAttributeUsage();
        var resource = new ResourceImpl(URI.createURI(uri));
        resource.getContents().add(feature);

        // No accessing context or specialization path is needed to decide the candidate's eligibility.
        assertThat(this.predicate.test(null, feature)).isEqualTo(accepted);
    }

    /**
     * A detached feature is accepted because it does not belong to a standard library resource.
     */
    @Test
    @DisplayName("GIVEN a detached feature, WHEN filtering its origin, THEN the feature is accepted")
    public void acceptDetachedFeature() {
        var feature = SysmlFactory.eINSTANCE.createAttributeUsage();
        assertThat(this.predicate.test(null, feature)).isTrue();
    }

    /**
     * A missing candidate is rejected rather than silently treated as a detached feature.
     */
    @Test
    @DisplayName("GIVEN a null feature, WHEN filtering its origin, THEN the candidate is rejected")
    public void rejectNullFeature() {
        assertThatNullPointerException().isThrownBy(() -> this.predicate.test(null, null));
    }
}
