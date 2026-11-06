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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.data.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the observable feature-chain query contract.
 *
 * @author Arthur Daussy
 */
public class AccessibleFeatureChainsComputerTest {

    private static final String BASE_X = "RedefinedFeatures::Base::x";

    private static final String BASE_PRESERVED = "RedefinedFeatures::Base::preserved";

    private static final String CHILD_PRESERVED = "NestedRedefinitions::Child::preserved";

    private final MetamodelQueryElementService service = new MetamodelQueryElementService();

    /**
     * An empty context produces no feature chains and never evaluates a candidate predicate.
     */
    @Test
    @DisplayName("GIVEN an empty context, WHEN collecting feature chains, THEN no candidate is evaluated")
    public void emptyContext() throws IOException {
        var model = this.loadDirectFeaturesModel();
        var chains = this.service.getAllFeatureChains(model.empty(), (parent, feature) -> {
            throw new AssertionError("No traversal candidate expected");
        }, (parent, feature) -> {
            throw new AssertionError("No terminal candidate expected");
        });
        assertThat(chains).isEmpty();
    }

    /**
     * Each direct feature, including an untyped part, produces its own singleton chain in membership order.
     */
    @Test
    @DisplayName("GIVEN direct features, WHEN collecting all chains, THEN each feature is a terminal candidate")
    public void directFeatures() throws IOException {
        var model = this.loadDirectFeaturesModel();
        var chains = this.service.getAllFeatureChains(model.context(), (parent, feature) -> true, (parent, feature) -> true);
        assertThat(chains).containsExactly(List.of(model.a()), List.of(model.b()), List.of(model.untyped()));
    }

    /**
     * A rejected traversal candidate is absent and is not offered to the terminal predicate.
     */
    @Test
    @DisplayName("GIVEN a rejected feature, WHEN traversing candidates, THEN its terminal predicate is not evaluated")
    public void rejectTraversalCandidate() throws IOException {
        var model = this.loadDirectFeaturesModel();
        var excluded = model.b();
        var chains = this.service.getAllFeatureChains(model.context(), (parent, feature) -> feature != excluded, (parent, feature) -> {
            assertThat(feature).isNotSameAs(excluded);
            return true;
        });
        assertThat(chains).containsExactly(List.of(model.a()), List.of(model.untyped()));
    }

    /**
     * Terminal selection retains only accepted paths and both predicates receive the effective accessing context.
     */
    @Test
    @DisplayName("GIVEN a terminal filter, WHEN evaluating direct features, THEN the predicates receive their accessing context")
    public void selectTerminalCandidate() throws IOException {
        var model = this.loadDirectFeaturesModel();
        var selected = model.b();
        var chains = this.service.getAllFeatureChains(model.context(), (parent, feature) -> {
            assertThat(parent).isSameAs(model.context());
            return true;
        }, (parent, feature) -> {
            assertThat(parent).isSameAs(model.context());
            return feature == selected;
        });
        assertThat(chains).containsExactly(List.of(selected));
        assertThat(this.service.getAllFeatureChains(model.context(), (parent, feature) -> true, (parent, feature) -> false)).isEmpty();
    }

    /**
     * A limited stream evaluates candidates only on consumption and can stop after the first accepted chain.
     */
    @Test
    @DisplayName("GIVEN a feature-chain stream, WHEN consuming one result, THEN later candidates remain unevaluated")
    public void consumeFirstChain() throws IOException {
        var model = this.loadDirectFeaturesModel();
        var evaluations = new AtomicInteger();
        try (var chains = this.service.streamFeatureChains(model.context(), (parent, feature) -> {
            evaluations.incrementAndGet();
            return true;
        }, (parent, feature) -> true)) {
            assertThat(evaluations.get()).isZero();
            assertThat(chains.limit(1).toList()).containsExactly(List.of(model.a()));
            assertThat(evaluations.get()).isEqualTo(1);
        }
    }

    /**
     * A null context is rejected when requesting the stream, even when both optional predicates are absent.
     */
    @Test
    @DisplayName("GIVEN a null context, WHEN requesting a stream, THEN the context is rejected immediately")
    public void rejectNullContext() {
        assertThatNullPointerException().isThrownBy(() -> this.service.streamFeatureChains(null, (parent, feature) -> true, (parent, feature) -> true));
        assertThatNullPointerException().isThrownBy(() -> this.service.streamFeatureChains(null, null, null));
    }

    /**
     * Omitting both predicates accepts all direct candidates in both the stream and collecting APIs.
     */
    @Test
    @DisplayName("GIVEN null predicates, WHEN querying feature chains, THEN all candidates are accepted")
    public void acceptAllWithNullPredicates() throws IOException {
        var model = this.loadDirectFeaturesModel();
        var expected = List.of(List.of(model.a()), List.of(model.b()), List.of(model.untyped()));
        assertThat(this.service.getAllFeatureChains(model.context(), null, null)).isEqualTo(expected);
        try (var chains = this.service.streamFeatureChains(model.context(), null, null)) {
            assertThat(chains.toList()).isEqualTo(expected);
        }
    }

    /**
     * An absent traversal predicate accepts every candidate while the terminal predicate still selects results.
     */
    @Test
    @DisplayName("GIVEN a null traversal predicate, WHEN selecting a terminal feature, THEN terminal filtering still applies")
    public void selectTerminalWithNullTraversalPredicate() throws IOException {
        var model = this.loadDirectFeaturesModel();
        var chains = this.service.getAllFeatureChains(model.context(), null, (parent, feature) -> {
            assertThat(parent).isSameAs(model.context());
            return feature == model.b();
        });
        assertThat(chains).containsExactly(List.of(model.b()));
    }

    /**
     * An absent terminal predicate includes every traversal candidate that passes the traversal predicate.
     */
    @Test
    @DisplayName("GIVEN a null terminal predicate, WHEN excluding a traversal candidate, THEN only accepted candidates are included")
    public void excludeCandidateWithNullTerminalPredicate() throws IOException {
        var model = this.loadDirectFeaturesModel();
        var chains = this.service.getAllFeatureChains(model.context(), (parent, feature) -> {
            assertThat(parent).isSameAs(model.context());
            return feature != model.b();
        }, null);
        assertThat(chains).containsExactly(List.of(model.a()), List.of(model.untyped()));
    }

    /**
     * Shared typings are explored independently, including three-segment paths and untyped owned content.
     */
    @Test
    @DisplayName("GIVEN nested features sharing a type, WHEN collecting chains, THEN every branch is explored in depth-first order")
    public void nestedFeatures() throws IOException {
        var model = this.loadNestedFeaturesModel();
        var chains = this.service.getAllFeatureChains(model.context(), null, null);
        assertThat(chains).containsExactly(
                List.of(model.left()),
                List.of(model.left(), model.x()),
                List.of(model.left(), model.deeper()),
                List.of(model.left(), model.deeper(), model.z()),
                List.of(model.right()),
                List.of(model.right(), model.x()),
                List.of(model.right(), model.deeper()),
                List.of(model.right(), model.deeper(), model.z()),
                List.of(model.local()),
                List.of(model.local(), model.y()),
                List.of(model.plain()));
    }

    /**
     * Rejecting an intermediate terminal candidate does not prevent discovering configurable descendants.
     */
    @Test
    @DisplayName("GIVEN an attribute-only terminal filter, WHEN traversing nested parts, THEN intermediate parts remain traversable")
    public void nestedTerminalSelection() throws IOException {
        var model = this.loadNestedFeaturesModel();
        var chains = this.service.getAllFeatureChains(model.context(), null, (parent, feature) -> feature instanceof AttributeUsage);
        assertThat(chains).containsExactly(
                List.of(model.left(), model.x()),
                List.of(model.left(), model.deeper(), model.z()),
                List.of(model.right(), model.x()),
                List.of(model.right(), model.deeper(), model.z()),
                List.of(model.local(), model.y()));
    }

    /**
     * A context-specific traversal rejection prunes one occurrence without removing the same feature in another branch.
     */
    @Test
    @DisplayName("GIVEN a context-specific traversal filter, WHEN rejecting an intermediate feature, THEN only that branch is pruned")
    public void pruneNestedBranch() throws IOException {
        var model = this.loadNestedFeaturesModel();
        var chains = this.service.getAllFeatureChains(model.context(), (parent, feature) -> parent != model.left() || feature != model.deeper(),
                (parent, feature) -> {
                    assertThat(parent == model.left() && feature == model.deeper()).isFalse();
                    return true;
                });
        assertThat(chains).containsExactly(
                List.of(model.left()),
                List.of(model.left(), model.x()),
                List.of(model.right()),
                List.of(model.right(), model.x()),
                List.of(model.right(), model.deeper()),
                List.of(model.right(), model.deeper(), model.z()),
                List.of(model.local()),
                List.of(model.local(), model.y()),
                List.of(model.plain()));
    }

    /**
     * Both predicates receive the usage providing the inherited feature, rather than its declaring definition.
     */
    @Test
    @DisplayName("GIVEN nested typed usages, WHEN evaluating predicates, THEN each candidate is evaluated in its accessing usage")
    public void nestedAccessingContext() throws IOException {
        var model = this.loadNestedFeaturesModel();
        var accessingContexts = new ArrayList<Type>();
        var chains = this.service.getAllFeatureChains(model.context(), (parent, feature) -> {
            if (feature == model.x()) {
                accessingContexts.add(parent);
            }
            return true;
        }, (parent, feature) -> feature == model.x() && parent == model.right());
        assertThat(chains).containsExactly(List.of(model.right(), model.x()));
        assertThat(accessingContexts).containsExactly(model.left(), model.right());
    }

    /**
     * Finding the first selected descendant does not evaluate subsequent descendants or sibling branches.
     */
    @Test
    @DisplayName("GIVEN a lazy nested traversal, WHEN consuming the first attribute, THEN later candidates remain unevaluated")
    public void consumeFirstNestedChain() throws IOException {
        var model = this.loadNestedFeaturesModel();
        var visited = new ArrayList<Feature>();
        try (var chains = this.service.streamFeatureChains(model.context(), (parent, feature) -> {
            visited.add(feature);
            return true;
        }, (parent, feature) -> feature instanceof AttributeUsage)) {
            assertThat(visited).isEmpty();
            assertThat(chains.limit(1).toList()).containsExactly(List.of(model.left(), model.x()));
            assertThat(visited).containsExactly(model.left(), model.x());
        }
    }

    /**
     * Explicit inheritance exposes the original features when the accessing type does not redefine them.
     */
    @Test
    @DisplayName("GIVEN explicit inheritance, WHEN collecting feature chains, THEN inherited feature identities are preserved")
    public void explicitlyInheritedFeatures() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        assertThat(this.service.getAllFeatureChains(model.get(PartDefinition.class, "RedefinedFeatures::Inherited"), null, null)).containsExactly(
                List.of(model.get(Feature.class, BASE_X)),
                List.of(model.get(Feature.class, BASE_PRESERVED)));
    }

    /**
     * A renamed redefinition hides its source by semantic identity, while preserving unrelated inherited features.
     */
    @Test
    @DisplayName("GIVEN a renamed redefinition, WHEN collecting feature chains, THEN its original feature is hidden")
    public void renamedRedefinition() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        assertThat(this.service.getAllFeatureChains(model.get(PartDefinition.class, "RedefinedFeatures::Renamed"), null, null)).containsExactly(
                List.of(model.get(Feature.class, "RedefinedFeatures::Renamed::renamed")),
                List.of(model.get(Feature.class, BASE_PRESERVED)));
    }

    /**
     * Redefining a redefinition hides both its immediate target and the original feature.
     */
    @Test
    @DisplayName("GIVEN transitive redefinitions, WHEN collecting feature chains, THEN only the most local redefinition remains")
    public void transitiveRedefinition() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        assertThat(this.service.getAllFeatureChains(model.get(PartDefinition.class, "RedefinedFeatures::Transitive"), null, null)).containsExactly(
                List.of(model.get(Feature.class, "RedefinedFeatures::Transitive::finalValue")),
                List.of(model.get(Feature.class, BASE_PRESERVED)));
    }

    /**
     * Anonymous and same-name redefinitions retain their own identities instead of the inherited source object.
     */
    @Test
    @DisplayName("GIVEN anonymous or same-name redefinitions, WHEN collecting feature chains, THEN the redefining objects are returned")
    public void anonymousAndSameNameRedefinitions() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        for (String name : List.of("RedefinedFeatures::Anonymous", "RedefinedFeatures::SameName")) {
            var redefinition = model.get(Feature.class, name + "::x");
            assertThat(redefinition).isNotSameAs(model.get(Feature.class, BASE_X));
            assertThat(this.service.getAllFeatureChains(model.get(PartDefinition.class, name), null, null)).containsExactly(
                    List.of(redefinition),
                    List.of(model.get(Feature.class, BASE_PRESERVED)));
        }
    }

    /**
     * A source inherited through the first diamond branch is hidden by a redefinition inherited through the second.
     */
    @Test
    @DisplayName("GIVEN diamond inheritance, WHEN a later branch redefines a shared feature, THEN the shared source is hidden")
    public void diamondRedefinition() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        assertThat(this.service.getAllFeatureChains(model.get(PartDefinition.class, "RedefinedFeatures::Diamond"), null, null)).containsExactly(
                List.of(model.get(Feature.class, BASE_PRESERVED)),
                List.of(model.get(Feature.class, "RedefinedFeatures::Right::rightValue")));
    }

    /**
     * Redefinitions are resolved before user filtering: excluding the replacement must not expose its source again.
     */
    @Test
    @DisplayName("GIVEN an excluded redefinition, WHEN traversing candidates, THEN the redefined source remains hidden")
    public void excludedRedefinitionDoesNotRestoreSource() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        var replacement = model.get(Feature.class, "RedefinedFeatures::Renamed::renamed");
        var chains = this.service.getAllFeatureChains(model.get(PartDefinition.class, "RedefinedFeatures::Renamed"), (parent, feature) -> {
            assertThat(feature).isNotSameAs(model.get(Feature.class, BASE_X));
            return feature != replacement;
        }, (parent, feature) -> {
            assertThat(feature).isNotSameAs(model.get(Feature.class, BASE_X));
            return true;
        });
        assertThat(chains).containsExactly(List.of(model.get(Feature.class, BASE_PRESERVED)));
    }

    /**
     * A parent redefinition provides the accessing context for its own renamed child and its remaining inherited child.
     */
    @Test
    @DisplayName("GIVEN nested redefinitions, WHEN collecting feature chains, THEN each level uses the effective redefining feature")
    public void nestedRedefinition() throws IOException {
        var model = this.loadNestedRedefinitionsModel();
        var sub = model.get(Feature.class, "NestedRedefinitions::Derived::sub");
        var renamed = model.get(Feature.class, "NestedRedefinitions::Derived::sub::renamed");
        var preserved = model.get(Feature.class, CHILD_PRESERVED);
        var chains = this.service.getAllFeatureChains(model.get(PartDefinition.class, "NestedRedefinitions::Derived"), null, (parent, feature) -> {
            if (feature == renamed || feature == preserved) {
                assertThat(parent).isSameAs(sub);
            }
            return true;
        });
        assertThat(chains).containsExactly(
                List.of(sub),
                List.of(sub, renamed),
                List.of(sub, preserved));
    }

    /**
     * A refined typing keeps its inherited redefinition together with the content owned by the redefining usage.
     */
    @Test
    @DisplayName("GIVEN a redefinition with refined typing, WHEN collecting chains, THEN local and refined-type features are visible")
    public void refinedTypingRedefinition() throws IOException {
        var model = this.loadNestedRedefinitionsModel();
        var sub = model.get(Feature.class, "NestedRedefinitions::Refined::sub");
        assertThat(this.service.getAllFeatureChains(model.get(PartDefinition.class, "NestedRedefinitions::Refined"), null, null)).containsExactly(
                List.of(sub),
                List.of(sub, model.get(Feature.class, "NestedRedefinitions::Refined::sub::localValue")),
                List.of(sub, model.get(Feature.class, "NestedRedefinitions::RefinedChild::refined")),
                List.of(sub, model.get(Feature.class, CHILD_PRESERVED)));
    }

    /**
     * Subsetting a typed variant makes its type's original attributes accessible without locally redefining them.
     * The returned chains must preserve those attribute identities and use the subsetting usage as accessing context.
     */
    @Test
    @DisplayName("GIVEN a subsetting toward a typed variant without local attributes, WHEN collecting chains, THEN the variant type's original attributes are returned")
    public void subsettedVariantWithoutAttributeRedefinition() throws IOException {
        var model = this.loadSubsettedVariantModel();
        var context = model.get(Feature.class, "SubsettedVariant::configured");
        var variation = model.get(Feature.class, "SubsettedVariant::configured::variantPart");
        var variant = model.get(Feature.class, "SubsettedVariant::PD3::variantPart::var1");
        var attribute = model.get(Feature.class, "SubsettedVariant::Var1::attrVar1");
        var common = model.get(Feature.class, "SubsettedVariant::AbstractVariant::commonAttr");

        // The usage has neither an explicit typing nor local attributes: Var1 comes from the subsetting target.
        assertThat(variation.getOwnedTyping()).isEmpty();
        assertThat(variation.getOwnedFeature()).isEmpty();
        assertThat(variation.getOwnedSubsetting()).extracting(Subsetting::getSubsettedFeature).contains(variant);
        assertThat(variation.getType()).containsExactly(model.get(Type.class, "SubsettedVariant::Var1"));

        var chains = this.service.getAllFeatureChains(context, null, (accessingContext, feature) -> {
            if (feature == attribute || feature == common) {
                assertThat(accessingContext).isSameAs(variation);
            }
            return true;
        });
        // Return the original definition-owned attributes directly, without inserting var1 into the path.
        // The redefined variation contributes commonAttr before the subsetting contributes attrVar1.
        assertThat(chains).containsExactly(
                List.of(variation),
                List.of(variation, common),
                List.of(variation, attribute));
    }

    /**
     * A replacement in one occurrence must not hide the original feature from an independent sibling occurrence.
     */
    @Test
    @DisplayName("GIVEN original and configured occurrences, WHEN collecting chains, THEN redefinition masking remains local to each branch")
    public void redefinitionIsLocalToAccessingContext() throws IOException {
        var model = this.loadNestedRedefinitionsModel();
        var original = model.get(Feature.class, "NestedRedefinitions::Occurrences::original");
        var configured = model.get(Feature.class, "NestedRedefinitions::Occurrences::configured");
        assertThat(this.service.getAllFeatureChains(model.get(PartDefinition.class, "NestedRedefinitions::Occurrences"), null, null)).containsExactly(
                List.of(original),
                List.of(original, model.get(Feature.class, "NestedRedefinitions::Child::x")),
                List.of(original, model.get(Feature.class, CHILD_PRESERVED)),
                List.of(configured),
                List.of(configured, model.get(Feature.class, "NestedRedefinitions::RefinedChild::refined")),
                List.of(configured, model.get(Feature.class, CHILD_PRESERVED)));
    }

    /**
     * A direct type cycle exposes the repeated type's features once, then stops expanding them.
     */
    @Test
    @DisplayName("GIVEN a direct type cycle, WHEN collecting chains, THEN the repeated type is enumerated once")
    public void directCycle() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var self = model.get(Feature.class, "CyclicFeatures::Direct::self");
        // One extra result detects excessive expansion without letting a regression traverse forever.
        try (var chains = this.service.streamFeatureChains(model.get(Type.class, "CyclicFeatures::Direct"), null, null)) {
            assertThat(chains.limit(3).toList()).containsExactly(
                    List.of(self),
                    List.of(self, self));
        }
    }

    /**
     * Returning to the root through a different feature also closes a cycle by type identity.
     */
    @Test
    @DisplayName("GIVEN an indirect cycle, WHEN returning to the root type, THEN its features are emitted without further expansion")
    public void indirectCycle() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var b = model.get(Feature.class, "CyclicFeatures::A::b");
        var a = model.get(Feature.class, "CyclicFeatures::B::a");
        // The third result is visible, but expanding it would start another turn around the cycle.
        try (var chains = this.service.streamFeatureChains(model.get(Type.class, "CyclicFeatures::A"), null, null)) {
            assertThat(chains.limit(4).toList()).containsExactly(
                    List.of(b),
                    List.of(b, a),
                    List.of(b, a, b));
        }
    }

    /**
     * Cycle tracking belongs to each active branch and is released before visiting its siblings.
     */
    @Test
    @DisplayName("GIVEN two occurrences of a recursive type, WHEN collecting chains, THEN both branches are explored independently")
    public void independentCyclicBranches() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var left = model.get(Feature.class, "CyclicFeatures::Branches::left");
        var right = model.get(Feature.class, "CyclicFeatures::Branches::right");
        var self = model.get(Feature.class, "CyclicFeatures::Direct::self");
        var plain = model.get(Feature.class, "CyclicFeatures::Branches::plain");
        // A type encountered in the left branch must not suppress the independent right branch.
        try (var chains = this.service.streamFeatureChains(model.get(Type.class, "CyclicFeatures::Branches"), null, null)) {
            assertThat(chains.limit(8).toList()).containsExactly(
                    List.of(left),
                    List.of(left, self),
                    List.of(left, self, self),
                    List.of(right),
                    List.of(right, self),
                    List.of(right, self, self),
                    List.of(plain));
        }
    }

    /**
     * The final cycle enumeration includes all siblings, while non-cyclic branches retain their descendants.
     */
    @Test
    @DisplayName("GIVEN a recursive type with non-cyclic content, WHEN cutting the cycle, THEN sibling traversal continues")
    public void cycleWithNonCyclicSibling() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var loop = model.get(Feature.class, "CyclicFeatures::WithSibling::recursive");
        var child = model.get(Feature.class, "CyclicFeatures::WithSibling::child");
        var tail = model.get(Feature.class, "CyclicFeatures::WithSibling::tail");
        var value = model.get(Feature.class, "CyclicFeatures::Leaf::value");
        // After the first cycle, the new Leaf type remains traversable, while another recursive occurrence is cut.
        try (var chains = this.service.streamFeatureChains(model.get(Type.class, "CyclicFeatures::WithSibling"), null, null)) {
            assertThat(chains.limit(9).toList()).containsExactly(
                    List.of(loop),
                    List.of(loop, loop),
                    List.of(loop, child),
                    List.of(loop, child, value),
                    List.of(loop, tail),
                    List.of(child),
                    List.of(child, value),
                    List.of(tail));
        }
    }

    /**
     * The repeated usage retains its own content and its non-cyclic descendants alongside inherited features.
     */
    @Test
    @DisplayName("GIVEN a cyclic usage with owned content, WHEN enumerating the repeated type, THEN owned and inherited features remain visible")
    public void cycleRetainsOwnedContent() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var back = model.get(Feature.class, "CyclicFeatures::LocalCycle::back");
        var local = model.get(Feature.class, "CyclicFeatures::LocalCycle::back::local");
        var value = model.get(Feature.class, "CyclicFeatures::LocalCycle::back::local::value");
        // The untyped local branch is new, so its value remains accessible after closing the type cycle.
        try (var chains = this.service.streamFeatureChains(model.get(Type.class, "CyclicFeatures::LocalCycle"), null, null)) {
            assertThat(chains.limit(5).toList()).containsExactly(
                    List.of(back),
                    List.of(back, local),
                    List.of(back, local, value),
                    List.of(back, back));
        }
    }

    /**
     * Every type of a multiply typed usage participates in cycle detection, without cutting new descendant types.
     */
    @Test
    @DisplayName("GIVEN multiple feature types, WHEN a later type repeats, THEN all effective features are enumerated before stopping")
    public void cycleThroughMultipleTyping() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var next = model.get(Feature.class, "CyclicFeatures::Multiple::next");
        var leaf = model.get(Feature.class, "CyclicFeatures::Extra::leaf");
        var value = model.get(Feature.class, "CyclicFeatures::Leaf::value");
        assertThat(next.getType()).containsExactly(
                model.get(Type.class, "CyclicFeatures::Extra"),
                model.get(Type.class, "CyclicFeatures::Multiple"));
        // The repeated second type closes the cycle without hiding the first type's membership.
        try (var chains = this.service.streamFeatureChains(model.get(Type.class, "CyclicFeatures::Multiple"), null, null)) {
            assertThat(chains.limit(5).toList()).containsExactly(
                    List.of(next),
                    List.of(next, leaf),
                    List.of(next, leaf, value),
                    List.of(next, next));
        }
    }

    /**
     * Rejecting all terminal results still exhausts a cyclic traversal instead of searching forever.
     */
    @Test
    @DisplayName("GIVEN a cycle and no accepted terminal, WHEN consuming the stream, THEN traversal terminates without results")
    public void cycleWithNoSelectedTerminal() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var visits = new AtomicInteger();
        // This guard makes a missing cycle cut fail promptly even though no result can satisfy a stream limit.
        var chains = this.service.getAllFeatureChains(model.get(Type.class, "CyclicFeatures::Direct"), (parent, feature) -> {
            assertThat(visits.incrementAndGet()).isLessThanOrEqualTo(3);
            return true;
        }, (parent, feature) -> false);
        assertThat(chains).isEmpty();
        assertThat(visits.get()).isEqualTo(2);
    }

    /**
     * A root usage contributes its value types to cycle detection even before any chain segment is visited.
     */
    @Test
    @DisplayName("GIVEN a typed usage as root, WHEN its type repeats through another usage, THEN the root typing closes the cycle")
    public void cycleFromTypedUsageContext() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var left = model.get(Feature.class, "CyclicFeatures::Branches::left");
        var self = model.get(Feature.class, "CyclicFeatures::Direct::self");
        // left and self are different usages of the same type; the root itself is excluded from the chains.
        try (var chains = this.service.streamFeatureChains(left, null, null)) {
            assertThat(chains.limit(3).toList()).containsExactly(
                    List.of(self),
                    List.of(self, self));
        }
    }

    /**
     * Traversal rejection still prunes candidates at the cycle boundary before terminal selection.
     */
    @Test
    @DisplayName("GIVEN a context-specific filter at a cycle boundary, WHEN rejecting its candidate, THEN only the first occurrence is emitted")
    public void pruneAtCycleBoundary() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var self = model.get(Feature.class, "CyclicFeatures::Direct::self");
        // At the second occurrence the accessing context is self, rather than its declaring definition.
        var chains = this.service.getAllFeatureChains(model.get(Type.class, "CyclicFeatures::Direct"), (parent, feature) -> parent != self,
                (parent, feature) -> {
                    assertThat(parent).isSameAs(model.get(Type.class, "CyclicFeatures::Direct"));
                    return true;
                });
        assertThat(chains).containsExactly(List.of(self));
    }

    /**
     * Descending through a new local branch must retain the cycle state and stop a subsequent return to an active type.
     */
    @Test
    @DisplayName("GIVEN a local branch after a cycle, WHEN it returns to an active type, THEN the returning feature is emitted without expansion")
    public void cycleStateSurvivesNewLocalBranch() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var back = model.get(Feature.class, "CyclicFeatures::WithReentry::back");
        var local = model.get(Feature.class, "CyclicFeatures::WithReentry::back::local");
        var value = model.get(Feature.class, "CyclicFeatures::WithReentry::back::local::value");
        var reentry = model.get(Feature.class, "CyclicFeatures::WithReentry::back::local::reentry");
        // Local content remains traversable, but it must not grant another recursion when reentry reaches WithReentry.
        try (var chains = this.service.streamFeatureChains(model.get(Type.class, "CyclicFeatures::WithReentry"), null, null)) {
            assertThat(chains.limit(6).toList()).containsExactly(
                    List.of(back),
                    List.of(back, local),
                    List.of(back, local, value),
                    List.of(back, local, reentry),
                    List.of(back, back));
        }
    }

    /**
     * A usage typed by two independent definitions exposes both sets of features once, in membership order.
     */
    @Test
    @DisplayName("GIVEN two independent feature types, WHEN collecting chains from their usage, THEN features from both types are returned")
    public void multipleFeatureTypes() throws IOException {
        var model = this.loadTypingBoundariesModel();
        var mixed = model.get(Feature.class, "TypingBoundaries::Context::mixed");
        assertThat(mixed.getType()).containsExactly(
                model.get(Type.class, "TypingBoundaries::A"),
                model.get(Type.class, "TypingBoundaries::B"));

        // The usage is the accessing context, so neither type nor the usage itself becomes a chain segment.
        var chains = this.service.getAllFeatureChains(mixed, null, null);
        assertThat(chains).containsExactly(
                List.of(model.get(Feature.class, "TypingBoundaries::A::a")),
                List.of(model.get(Feature.class, "TypingBoundaries::B::b")));
    }

    /**
     * A feature typed by an empty definition remains a terminal candidate but has no descendant chains.
     */
    @Test
    @DisplayName("GIVEN a feature with an empty value type, WHEN collecting chains, THEN the feature is retained without descendants")
    public void typedFeatureWithoutContent() throws IOException {
        var model = this.loadTypingBoundariesModel();
        var context = model.get(Type.class, "TypingBoundaries::Context");
        var scalar = model.get(Feature.class, "TypingBoundaries::Context::scalar");
        assertThat(scalar.getType()).containsExactly(model.get(Type.class, "TypingBoundaries::Scalar"));

        // Keep only scalar at the root; typing it must not manufacture another chain segment.
        assertThat(this.service.getAllFeatureChains(context, (parent, feature) -> feature == scalar, null))
                .containsExactly(List.of(scalar));
        assertThat(this.service.getAllFeatureChains(scalar, null, null)).isEmpty();
    }

    /**
     * A nested definition and its owned features do not participate in a traversal through feature memberships.
     */
    @Test
    @DisplayName("GIVEN a nested definition beside typed features, WHEN collecting chains, THEN only feature memberships are followed")
    public void ignoreNestedDefinitions() throws IOException {
        var model = this.loadTypingBoundariesModel();
        var context = model.get(Type.class, "TypingBoundaries::Context");
        var internal = model.get(Type.class, "TypingBoundaries::Context::Internal");
        var mixed = model.get(Feature.class, "TypingBoundaries::Context::mixed");
        assertThat(context.getOwnedMember()).contains(internal);
        assertThat(internal.getOwnedFeature()).containsExactly(model.get(Feature.class, "TypingBoundaries::Context::Internal::hidden"));

        // Internal really has content, but it is not reachable through a FeatureMembership of Context.
        assertThat(this.service.getAllFeatureChains(context, null, null)).containsExactly(
                List.of(mixed),
                List.of(mixed, model.get(Feature.class, "TypingBoundaries::A::a")),
                List.of(mixed, model.get(Feature.class, "TypingBoundaries::B::b")),
                List.of(model.get(Feature.class, "TypingBoundaries::Context::scalar")));
    }

    /**
     * Skipping results preserves depth-first order across ancestor and sibling transitions without visiting the next page.
     */
    @Test
    @DisplayName("GIVEN nested feature chains, WHEN skipping two results and consuming three, THEN the expected page is returned lazily")
    public void paginateNestedChains() throws IOException {
        var model = this.loadNestedFeaturesModel();
        var visited = new ArrayList<Feature>();
        try (var chains = this.service.streamFeatureChains(model.context(), (parent, feature) -> {
            visited.add(feature);
            return true;
        }, null)) {
            // The page ends on right itself: none of right's descendants should have been evaluated yet.
            assertThat(chains.skip(2).limit(3).toList()).containsExactly(
                    List.of(model.left(), model.deeper()),
                    List.of(model.left(), model.deeper(), model.z()),
                    List.of(model.right()));
            assertThat(visited).containsExactly(model.left(), model.x(), model.deeper(), model.z(), model.right());
        }
    }

    /**
     * Page offsets count selected chains, while rejected intermediate parts remain traversable.
     */
    @Test
    @DisplayName("GIVEN an attribute-only terminal filter, WHEN paginating results, THEN skipped parts do not count toward the page offset")
    public void paginateSelectedChains() throws IOException {
        var model = this.loadNestedFeaturesModel();
        var visited = new ArrayList<Feature>();
        try (var chains = this.service.streamFeatureChains(model.context(), (parent, feature) -> {
            visited.add(feature);
            return true;
        }, (parent, feature) -> feature instanceof AttributeUsage)) {
            // Skip left.x, then return left.deeper.z and right.x, without evaluating the rest of right.
            assertThat(chains.skip(1).limit(2).toList()).containsExactly(
                    List.of(model.left(), model.deeper(), model.z()),
                    List.of(model.right(), model.x()));
            assertThat(visited).containsExactly(model.left(), model.x(), model.deeper(), model.z(), model.right(), model.x());
        }
    }

    /**
     * A page can cross a cycle boundary, descend into new content and resume at a non-cyclic root sibling.
     */
    @Test
    @DisplayName("GIVEN a cycle with non-cyclic descendants, WHEN paginating across the cycle boundary, THEN path and cycle state are preserved")
    public void paginateCyclicChains() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        var recursive = model.get(Feature.class, "CyclicFeatures::WithSibling::recursive");
        var child = model.get(Feature.class, "CyclicFeatures::WithSibling::child");
        var tail = model.get(Feature.class, "CyclicFeatures::WithSibling::tail");
        var value = model.get(Feature.class, "CyclicFeatures::Leaf::value");
        try (var chains = this.service.streamFeatureChains(model.get(Type.class, "CyclicFeatures::WithSibling"), null, null)) {
            // The last result is a root sibling, rather than a descendant of the recursive occurrence.
            assertThat(chains.skip(3).limit(3).toList()).containsExactly(
                    List.of(recursive, child, value),
                    List.of(recursive, tail),
                    List.of(child));
        }
    }

    /**
     * Skipping every chain of a cyclic model exhausts the traversal and yields an empty following page.
     */
    @Test
    @DisplayName("GIVEN a bounded cyclic traversal, WHEN requesting a page after its last chain, THEN the page is empty")
    public void paginateBeyondLastChain() throws IOException {
        var model = this.loadCyclicFeaturesModel();
        // Direct has exactly self and self.self: there must be no third result after skipping both.
        try (var chains = this.service.streamFeatureChains(model.get(Type.class, "CyclicFeatures::Direct"), null, null)) {
            assertThat(chains.skip(2).limit(1).toList()).isEmpty();
        }
    }

    /**
     * A zero-size page consumes no candidate, even when it has a nonzero offset.
     */
    @Test
    @DisplayName("GIVEN an empty requested page, WHEN applying skip and limit, THEN neither predicate is evaluated")
    public void emptyPageDoesNotTraverseCandidates() throws IOException {
        var model = this.loadNestedFeaturesModel();
        try (var chains = this.service.streamFeatureChains(model.context(), (parent, feature) -> {
            throw new AssertionError("No traversal candidate expected for an empty page");
        }, (parent, feature) -> {
            throw new AssertionError("No terminal candidate expected for an empty page");
        })) {
            assertThat(chains.skip(2).limit(0).toList()).isEmpty();
        }
    }

    private SysMLTestModel loadSubsettedVariantModel() throws IOException {
        // See /feature-chains/subsetted-variant.xmi for the textual SysML model in an XML comment.
        return this.loadModel("subsetted-variant");
    }

    private SysMLTestModel loadTypingBoundariesModel() throws IOException {
        // See /feature-chains/typing-boundaries.xmi for the textual SysML model in an XML comment.
        return this.loadModel("typing-boundaries");
    }

    private SysMLTestModel loadCyclicFeaturesModel() throws IOException {
        // See /feature-chains/cyclic-features.xmi for the textual SysML model in an XML comment.
        return this.loadModel("cyclic-features");
    }

    private SysMLTestModel loadRedefinedFeaturesModel() throws IOException {
        // See /feature-chains/redefined-features.xmi for the textual SysML model in an XML comment.
        return this.loadModel("redefined-features");
    }

    private SysMLTestModel loadNestedRedefinitionsModel() throws IOException {
        // See /feature-chains/nested-redefinitions.xmi for the textual SysML model in an XML comment.
        return this.loadModel("nested-redefinitions");
    }

    /**
     * Load the direct-feature.xmi models.
     *
     * @return A model accessor
     * @throws IOException
     *         if something goes wrong during loading
     */
    private DirectFeaturesModel loadDirectFeaturesModel() throws IOException {
        // See /feature-chains/direct-features.xmi for the textual SysML model in an XML comment.
        var model = this.loadModel("direct-features");
        return new DirectFeaturesModel(
                model.get(PartDefinition.class, "DirectFeatures::Empty"),
                model.get(PartDefinition.class, "DirectFeatures::Context"),
                model.get(Feature.class, "DirectFeatures::Context::a"),
                model.get(Feature.class, "DirectFeatures::Context::b"),
                model.get(Feature.class, "DirectFeatures::Context::untyped"));
    }

    private NestedFeaturesModel loadNestedFeaturesModel() throws IOException {
        // See /feature-chains/nested-features.xmi for the textual SysML model in an XML comment.
        var model = this.loadModel("nested-features");
        return new NestedFeaturesModel(
                model.get(PartDefinition.class, "NestedFeatures::Context"),
                model.get(Feature.class, "NestedFeatures::Context::left"),
                model.get(Feature.class, "NestedFeatures::Context::right"),
                model.get(Feature.class, "NestedFeatures::Context::local"),
                model.get(Feature.class, "NestedFeatures::Context::plain"),
                model.get(Feature.class, "NestedFeatures::Child::x"),
                model.get(Feature.class, "NestedFeatures::Child::deeper"),
                model.get(Feature.class, "NestedFeatures::Leaf::z"),
                model.get(Feature.class, "NestedFeatures::Context::local::y"));
    }

    private SysMLTestModel loadModel(String name) throws IOException {
        var resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        var resource = new XMIResourceImpl(URI.createURI("test:/" + name + ".xmi"));
        resourceSet.getResources().add(resource);
        try (var input = this.getClass().getResourceAsStream("/feature-chains/" + name + ".xmi")) {
            assertThat(input).isNotNull();
            resource.load(input, Map.of());
        }
        assertThat(resource.getErrors()).isEmpty();
        return new SysMLTestModel(resource);
    }

    /**
     * Provides named access to the nested model; see its XMI comment for the textual SysML.
     *
     * @author Arthur Daussy
     */
    private record NestedFeaturesModel(PartDefinition context, Feature left, Feature right, Feature local, Feature plain, Feature x, Feature deeper, Feature z, Feature y) {
    }

    /**
     * Provides named access to the elements of the direct-features model.
     * <pre>
     * package DirectFeatures {
     *     part def Empty;
     *     part def Context {
     *         attribute a;
     *         attribute b;
     *         part untyped;
     *     }
     * }
     * </pre>
     *
     * @param empty
     *         the context without features
     * @param context
     *         the context containing the three direct features
     * @param a
     *         the first attribute
     * @param b
     *         the second attribute
     * @param untyped
     *         the part without an explicit type
     * @author Arthur Daussy
     */
    private record DirectFeaturesModel(PartDefinition empty, PartDefinition context, Feature a, Feature b, Feature untyped) {
    }
}
