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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.Test;

/**
 * Tests the ordering, proxy handling and snapshot semantics of frequently used membership queries.
 *
 * @author arichard
 */
public class FeatureMembershipImplTest {

    /**
     * The first feature is returned even in mixed contents, and subsequent edits are reflected.
     */
    @Test
    public void firstOwnedFeature() {
        var membership = (FeatureMembershipImpl) SysmlFactory.eINSTANCE.createFeatureMembership();
        assertThat(membership.basicGetOwnedMemberFeature()).isNull();
        membership.getOwnedRelatedElement().add(SysmlFactory.eINSTANCE.createComment());
        assertThat(membership.basicGetOwnedMemberFeature()).isNull();
        var first = SysmlFactory.eINSTANCE.createPartUsage();
        var second = SysmlFactory.eINSTANCE.createPartUsage();
        membership.getOwnedRelatedElement().add(first);
        membership.getOwnedRelatedElement().add(second);
        assertThat(membership.basicGetOwnedMemberFeature()).isSameAs(first);
        membership.getOwnedRelatedElement().remove(first);
        assertThat(membership.basicGetOwnedMemberFeature()).isSameAs(second);
    }

    /**
     * The basic getter retains the proxy while the public resolving getter resolves it.
     */
    @Test
    public void resolveOwnedFeatureProxy() {
        var resolved = SysmlFactory.eINSTANCE.createPartUsage();
        var membership = new FeatureMembershipImpl() {
            /**
             * Resolves the test proxy to the expected feature.
             *
             * @param proxy
             *            the feature proxy
             * @return the resolved feature
             */
            @Override
            public EObject eResolveProxy(InternalEObject proxy) {
                return resolved;
            }
        };
        var proxy = SysmlFactory.eINSTANCE.createPartUsage();
        ((InternalEObject) proxy).eSetProxyURI(URI.createURI("test:/feature"));
        membership.getOwnedRelatedElement().add(proxy);
        assertThat(membership.basicGetOwnedMemberFeature()).isSameAs(proxy);
        assertThat(membership.getOwnedMemberFeature()).isSameAs(resolved);
    }

    /**
     * Redefinition results preserve EMF metadata, order and immutability without retaining stale model contents.
     */
    @Test
    public void ownedRedefinitionSnapshot() {
        var feature = SysmlFactory.eINSTANCE.createFeature();
        assertThat(feature.getOwnedRedefinition()).isEmpty();
        var first = SysmlFactory.eINSTANCE.createRedefinition();
        var second = SysmlFactory.eINSTANCE.createRedefinition();
        feature.getOwnedRelationship().add(first);
        feature.getOwnedRelationship().add(SysmlFactory.eINSTANCE.createFeatureTyping());
        feature.getOwnedRelationship().add(second);
        var result = feature.getOwnedRedefinition();
        assertThat(result).containsExactly(first, second);
        var setting = (EStructuralFeature.Setting) result;
        assertThat(setting.getEObject()).isSameAs(feature);
        assertThat(setting.getEStructuralFeature()).isSameAs(SysmlPackage.eINSTANCE.getFeature_OwnedRedefinition());
        assertThatThrownBy(result::clear).isInstanceOf(UnsupportedOperationException.class);
        feature.getOwnedRelationship().remove(first);
        assertThat(result).containsExactly(first, second);
        assertThat(feature.getOwnedRedefinition()).containsExactly(second);
    }
}
