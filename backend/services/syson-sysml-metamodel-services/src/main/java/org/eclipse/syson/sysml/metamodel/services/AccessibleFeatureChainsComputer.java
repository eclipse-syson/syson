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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Type;

/**
 * Computes accessible feature chains from a given context {@link Type}.
 *
 * @author Arthur Daussy
 */
public class AccessibleFeatureChainsComputer {

    private final Type context;

    private final BiPredicate<Type, Feature> shouldTraverseFeature;

    private final BiPredicate<Type, List<Feature>> shouldIncludeFeatureChain;

    public AccessibleFeatureChainsComputer(Type context, BiPredicate<Type, Feature> shouldTraverseFeature, BiPredicate<Type, List<Feature>> shouldIncludeFeatureChain) {
        this.context = Objects.requireNonNull(context);
        this.shouldTraverseFeature = Objects.requireNonNullElse(shouldTraverseFeature, (parent, feature) -> true);
        this.shouldIncludeFeatureChain = Objects.requireNonNullElse(shouldIncludeFeatureChain, (root, chain) -> true);
    }

    /**
     * Computes the stream described by {@link MetamodelQueryElementService#streamFeatureChains}.
     *
     * @return a sequential stream of selected feature chains
     */
    public Stream<List<Feature>> compute() {
        return StreamSupport.stream(new Traversal(this.context, this.shouldTraverseFeature, this.shouldIncludeFeatureChain), false);
    }

    /**
     * Keeps the state of a single sequential traversal without retaining previously emitted chains.
     *
     * @author Arthur Daussy
     */
    private static final class Traversal extends AbstractSpliterator<List<Feature>> {

        /**
         * Root accessing context; its memberships are discovered only when traversal starts.
         */
        private final Type context;

        /**
         * Accepts a candidate and its branch before chain selection.
         */
        private final BiPredicate<Type, Feature> shouldTraverseFeature;

        /**
         * Selects whether the current path is emitted, without pruning descendants.
         */
        private final BiPredicate<Type, List<Feature>> shouldIncludeFeatureChain;

        /**
         * Active contexts, with the deepest context on top and each ancestor's remaining memberships below it.
         */
        private final Deque<Frame> frames = new ArrayDeque<>();

        /**
         * Identity counts of contexts and their feature types in the active branch, including repeated contexts.
         */
        private final Map<Type, Integer> activeTypes = new IdentityHashMap<>();

        /**
         * Mutable path from the root to the current candidate; emitted chains are independent snapshots of it.
         */
        private final List<Feature> path = new ArrayList<>();

        /**
         * Distinguishes an unstarted traversal from an exhausted traversal, both of which have an empty frame stack.
         */
        private boolean initialized;

        /**
         * The last segment needs child discovery, or removal at a recursion boundary, before visiting its siblings.
         */
        private boolean expandLastFeature;

        private Traversal(Type context, BiPredicate<Type, Feature> shouldTraverseFeature, BiPredicate<Type, List<Feature>> shouldIncludeFeatureChain) {
            // The number of selected chains is unknown without traversing the model; emitted chains are ordered and non-null.
            super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
            this.context = Objects.requireNonNull(context);
            this.shouldTraverseFeature = Objects.requireNonNull(shouldTraverseFeature);
            this.shouldIncludeFeatureChain = Objects.requireNonNull(shouldIncludeFeatureChain);
        }

        @Override
        public boolean tryAdvance(Consumer<? super List<Feature>> action) {
            Objects.requireNonNull(action);
            if (!this.initialized) {
                // Bind to the model on the first demand, rather than when creating the stream.
                this.tryPushContext(this.context);
                this.initialized = true;
            }
            while (!this.frames.isEmpty()) {
                if (this.expandLastFeature) {
                    // Defer child discovery until another result is requested, so limit does not expand unused paths.
                    if (!this.tryPushContext(this.path.getLast())) {
                        // After the first cycle, another repetition is visible but cannot be expanded.
                        this.path.removeLast();
                    }
                    this.expandLastFeature = false;
                }
                Frame frame = this.frames.peek();
                if (!frame.memberships().hasNext()) {
                    // This context is exhausted: restore the parent path before visiting its remaining siblings.
                    this.popContext();
                    if (!this.path.isEmpty()) {
                        this.path.removeLast();
                    }
                } else {
                    Feature feature = frame.memberships().next().getOwnedMemberFeature();
                    // A traversal rejection skips both this result and all descendants of this occurrence.
                    if (feature != null && this.shouldTraverseFeature.test(frame.context(), feature)) {
                        this.path.add(feature);
                        this.expandLastFeature = true;
                        List<Feature> chain = List.copyOf(this.path);
                        if (this.shouldIncludeFeatureChain.test(this.context, chain)) {
                            // One successful call emits exactly one independent chain, then preserves the resume state.
                            action.accept(chain);
                            return true;
                        }
                        // Chain rejection emits nothing, but the next iteration still explores this feature's children.
                    }
                }
            }
            // No remaining context means exhaustion; subsequent calls must also return false without emitting anything.
            return false;
        }

        private boolean tryPushContext(Type accessingContext) {
            Set<Type> types = Collections.newSetFromMap(new IdentityHashMap<>());
            types.add(accessingContext);
            if (accessingContext instanceof Feature feature) {
                types.addAll(feature.getType());
            }
            boolean repeated = types.stream().anyMatch(this.activeTypes::containsKey);
            boolean cycleEncountered = !this.frames.isEmpty() && this.frames.peek().cycleEncountered();
            if (repeated && cycleEncountered) {
                return false;
            }
            types.forEach(type -> this.activeTypes.merge(type, 1, Integer::sum));
            // Propagate the cycle state through new contexts so a local detour cannot restart recursion.
            this.frames.push(new Frame(accessingContext, accessingContext.getFeatureMembership().iterator(), types, repeated || cycleEncountered));
            return true;
        }

        private void popContext() {
            this.frames.pop().types().forEach(type -> {
                int remaining = this.activeTypes.get(type) - 1;
                if (remaining == 0) {
                    this.activeTypes.remove(type);
                } else {
                    this.activeTypes.put(type, remaining);
                }
            });
        }
    }

    /**
     * Retains one context's remaining memberships and whether a cycle has already been encountered in its branch.
     *
     * @author Arthur Daussy
     */
    private record Frame(Type context, Iterator<FeatureMembership> memberships, Set<Type> types, boolean cycleEncountered) {
    }
}
