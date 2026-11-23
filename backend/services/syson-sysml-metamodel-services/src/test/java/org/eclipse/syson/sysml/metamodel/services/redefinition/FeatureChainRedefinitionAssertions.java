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
package org.eclipse.syson.sysml.metamodel.services.redefinition;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;

/**
 * Shared assertions for the feature-chain redefinition service contract.
 *
 * @author Arthur Daussy
 */
final class FeatureChainRedefinitionAssertions {

    private FeatureChainRedefinitionAssertions() {
    }

    /**
     * Checks that a chain operation succeeded without user-facing error messages.
     *
     * @param result
     *            the operation result to check
     */
    static void assertChainSuccess(FeatureChainRedefinitionResult result) {
        assertThat(result.isSuccess()).as("Unexpected failure: %s", result.messages()).isTrue();
        assertThat(result.messages()).isEmpty();
        assertThat(result.failureContext()).isNull();
        assertThat(result.failedSegmentIndex()).isNull();
        assertThat(result.failedFeature()).isNull();
    }

    /**
     * Checks exact category, identity, local ownership, anonymity and the explicit redefinition target.
     *
     * @param local
     *            the feature created by the service
     * @param owner
     *            the expected local owning type
     * @param source
     *            the selected source that the local feature must explicitly redefine
     */
    static void assertLocalRedefinition(Feature local, Type owner, Feature source) {
        assertThat(local).isNotSameAs(source);
        assertThat(local.eClass()).isSameAs(source.eClass());
        assertThat(local.getOwningType()).isSameAs(owner);
        assertThat(local.getDeclaredName()).isNull();
        assertThat(local.getDeclaredShortName()).isNull();
        assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> {
            assertThat(redefinition.getRedefiningFeature()).isSameAs(local);
            assertThat(redefinition.getRedefinedFeature()).isSameAs(source);
            assertThat(redefinition.isIsImplied()).isFalse();
        });
    }
}
