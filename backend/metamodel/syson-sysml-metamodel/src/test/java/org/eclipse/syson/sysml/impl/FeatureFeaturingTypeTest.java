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

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.TypeFeaturing;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests featuring type traversal through feature chains, including inconsistent cyclic models built through the API.
 *
 * @author Arthur Daussy
 */
public class FeatureFeaturingTypeTest {

    private final ModelBuilder builder = new ModelBuilder();

    @Test
    @DisplayName("GIVEN a self-referencing feature without contexts, WHEN querying featuring types, THEN no context is invented")
    public void getFeaturingTypeWithoutContextInSelfCycle() {
        // GIVEN an API-built cycle: feature -> feature, with no explicit or implicit featuring type.
        Feature feature = this.builder.create(Feature.class);
        this.addChaining(feature, feature);

        // WHEN the getter traverses the self-reference, where recursion previously overflowed.
        EList<Type> result = feature.getFeaturingType();

        // THEN the cycle terminates without introducing a featuring type.
        assertThat(result).isEmpty();
        assertThat(feature.getChainingFeature()).containsExactly(feature);
    }

    @Test
    @DisplayName("GIVEN a self-cycle with explicit and implicit contexts, WHEN querying featuring types, THEN contexts and model are preserved")
    public void getFeaturingTypeWithContextsInSelfCycle() {
        // GIVEN an API-built self-cycle, explicitly featured twice by its owning type.
        Type owner = this.builder.create(Type.class);
        Feature feature = this.builder.createIn(Feature.class, owner);
        this.addFeaturing(feature, owner);
        this.addFeaturing(feature, owner);
        this.addChaining(feature, feature);
        List<Relationship> relationships = List.copyOf(feature.getOwnedRelationship());
        List<Relationship> ownerRelationships = List.copyOf(owner.getOwnedRelationship());

        // WHEN featuring types are computed twice, each traversal must have its own visited set.
        EList<Type> result = feature.getFeaturingType();
        EList<Type> repeatedResult = feature.getFeaturingType();

        // THEN explicit duplicates precede the implicit owner; no relationship is removed or created.
        assertThat(result).containsExactly(owner, owner, owner);
        assertThat(repeatedResult).containsExactlyElementsOf(result);
        assertThat(feature.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(owner.getOwnedRelationship()).containsExactlyElementsOf(ownerRelationships);
        assertThat(feature.getChainingFeature()).containsExactly(feature);
        assertThatThrownBy(() -> result.add(owner)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("GIVEN a two-feature cycle, WHEN querying either feature, THEN both contexts are retained in traversal order")
    public void getFeaturingTypeInTwoFeatureCycle() {
        // GIVEN API-built first -> second -> first, each feature with explicit and implicit contexts.
        Type firstOwner = this.builder.create(Type.class);
        Type secondOwner = this.builder.create(Type.class);
        Type firstExplicit = this.builder.create(Type.class);
        Type secondExplicit = this.builder.create(Type.class);
        Feature first = this.builder.createIn(Feature.class, firstOwner);
        Feature second = this.builder.createIn(Feature.class, secondOwner);
        this.addFeaturing(first, firstExplicit);
        this.addFeaturing(second, secondExplicit);
        this.addChaining(first, second);
        this.addChaining(second, first);

        // WHEN each feature is independently used as the root of the traversal.
        EList<Type> firstResult = first.getFeaturingType();
        EList<Type> secondResult = second.getFeaturingType();

        // THEN explicit contexts follow descent and implicit contexts follow the reverse order, once per feature.
        assertThat(firstResult).containsExactly(firstExplicit, secondExplicit, secondOwner, firstOwner);
        assertThat(secondResult).containsExactly(secondExplicit, firstExplicit, firstOwner, secondOwner);
        assertThat(first.getChainingFeature()).containsExactly(second);
        assertThat(second.getChainingFeature()).containsExactly(first);
    }

    @Test
    @DisplayName("GIVEN an acyclic chain with two chaining features, WHEN querying featuring types, THEN only the first branch contributes")
    public void getFeaturingTypeInAcyclicChain() {
        // GIVEN API-built first -> [second, ignored], second -> last, with distinct owners and a shared explicit context.
        Type firstOwner = this.builder.create(Type.class);
        Type secondOwner = this.builder.create(Type.class);
        Type lastOwner = this.builder.create(Type.class);
        Type explicit = this.builder.create(Type.class);
        Feature first = this.builder.createIn(Feature.class, firstOwner);
        Feature second = this.builder.createIn(Feature.class, secondOwner);
        Feature last = this.builder.createIn(Feature.class, lastOwner);
        Feature ignored = this.builder.createIn(Feature.class, this.builder.create(Type.class));
        this.addFeaturing(first, explicit);
        this.addFeaturing(second, explicit);
        this.addFeaturing(last, explicit);
        this.addChaining(first, second);
        this.addChaining(first, ignored);
        this.addChaining(second, last);

        // WHEN traversing a normal chain.
        EList<Type> result = first.getFeaturingType();

        // THEN duplicates and the existing explicit-descent/implicit-return order are preserved.
        assertThat(result).containsExactly(explicit, explicit, explicit, lastOwner, secondOwner, firstOwner);
    }

    @Test
    @DisplayName("GIVEN a previously queried cycle, WHEN its chain target changes, THEN the next query uses the updated model")
    public void getFeaturingTypeAfterChainChange() {
        // GIVEN API-built feature -> feature, then a separate feature with an implicit owner.
        Feature feature = this.builder.create(Feature.class);
        FeatureChaining chaining = this.addChaining(feature, feature);
        Type owner = this.builder.create(Type.class);
        Feature replacement = this.builder.createIn(Feature.class, owner);
        assertThat(feature.getFeaturingType()).isEmpty();

        // WHEN replacing the self-reference after the first query.
        chaining.setChainingFeature(replacement);
        EList<Type> result = feature.getFeaturingType();

        // THEN a fresh traversal observes the replacement's owner.
        assertThat(result).containsExactly(owner);
        assertThat(feature.getChainingFeature()).containsExactly(replacement);
    }

    @Test
    @DisplayName("GIVEN a long acyclic chain, WHEN querying featuring types, THEN traversal does not depend on stack depth")
    public void getFeaturingTypeInLongChain() {
        // GIVEN 10,000 API-built links ending at a feature with one implicit context.
        Type owner = this.builder.create(Type.class);
        Feature first = this.builder.createIn(Feature.class, owner);
        for (int index = 0; index < 10_000; index++) {
            Feature next = this.builder.create(Feature.class);
            this.addChaining(next, first);
            first = next;
        }

        // WHEN traversing the whole chain.
        EList<Type> result = first.getFeaturingType();

        // THEN the terminal context is returned without recursive stack growth.
        assertThat(result).containsExactly(owner);
    }

    private FeatureChaining addChaining(Feature feature, Feature target) {
        FeatureChaining chaining = this.builder.createIn(FeatureChaining.class, feature);
        chaining.setChainingFeature(target);
        return chaining;
    }

    private void addFeaturing(Feature feature, Type type) {
        TypeFeaturing featuring = this.builder.createIn(TypeFeaturing.class, feature);
        featuring.setFeaturingType(type);
        featuring.setFeatureOfType(feature);
    }
}
