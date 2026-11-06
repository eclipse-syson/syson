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
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.data.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests whether an accessible chain still requires local redefinitions.
 *
 * @author Arthur Daussy
 */
public class RequiresRedefinitionFeatureChainPredicateTest {

    private final RequiresRedefinitionFeatureChainPredicate predicate = new RequiresRedefinitionFeatureChainPredicate();

    /**
     * A parent and child already redefined under the root are excluded, while an inherited child remains eligible.
     */
    @Test
    @DisplayName("GIVEN a locally redefined parent, WHEN checking its children, THEN only the inherited child needs redefinition")
    public void distinguishLocalAndInheritedChildren() throws IOException {
        var model = this.loadModel();
        var context = model.get(Type.class, "NestedRedefinitions::Derived");
        var sub = model.get(Feature.class, "NestedRedefinitions::Derived::sub");
        var renamed = model.get(Feature.class, "NestedRedefinitions::Derived::sub::renamed");
        var preserved = model.get(Feature.class, "NestedRedefinitions::Child::preserved");

        assertThat(this.predicate.test(context, List.of(sub, renamed))).isFalse();
        assertThat(this.predicate.test(context, List.of(sub, preserved))).isTrue();
    }

    /**
     * A local declaration is already available even without an explicit Redefinition relationship.
     */
    @Test
    @DisplayName("GIVEN an owned declaration without redefinition, WHEN checking its chain, THEN no new redefinition is needed")
    public void excludeOwnedDeclaration() throws IOException {
        var model = this.loadModel();
        var context = model.get(Type.class, "NestedRedefinitions::Refined");
        var sub = model.get(Feature.class, "NestedRedefinitions::Refined::sub");
        var local = model.get(Feature.class, "NestedRedefinitions::Refined::sub::localValue");

        assertThat(local.getOwnedRedefinition()).isEmpty();
        assertThat(this.predicate.test(context, List.of(sub, local))).isFalse();
    }

    /**
     * An inherited parent still needs a local replacement even when its child is owned and already redefines another feature.
     */
    @Test
    @DisplayName("GIVEN an inherited parent with a local child, WHEN checking the whole chain, THEN the missing parent redefinition is detected")
    public void includeInheritedPrefix() throws IOException {
        var model = this.loadModel();
        var derived = model.get(PartDefinition.class, "NestedRedefinitions::Derived");
        var sub = model.get(Feature.class, "NestedRedefinitions::Derived::sub");
        var renamed = model.get(Feature.class, "NestedRedefinitions::Derived::sub::renamed");
        // Extend the fixture with `part def Further :> Derived;` without changing the inherited features.
        var further = SysmlFactory.eINSTANCE.createPartDefinition();
        var inheritance = SysmlFactory.eINSTANCE.createSubclassification();
        inheritance.setSubclassifier(further);
        inheritance.setSuperclassifier(derived);
        further.getOwnedRelationship().add(inheritance);

        assertThat(renamed.getOwningType()).isSameAs(sub);
        assertThat(this.predicate.test(further, List.of(sub, renamed))).isTrue();
    }

    /**
     * Rejecting local chains must still explore their inherited descendants, and the result limit applies after selection.
     */
    @Test
    @DisplayName("GIVEN local prefixes and an inherited descendant, WHEN selecting one chain, THEN the descendant remains reachable")
    public void selectBeforeLimitingWithoutPruning() throws IOException {
        var model = this.loadModel();
        var context = model.get(Type.class, "NestedRedefinitions::Derived");
        var sub = model.get(Feature.class, "NestedRedefinitions::Derived::sub");
        var preserved = model.get(Feature.class, "NestedRedefinitions::Child::preserved");

        var chains = new MetamodelQueryElementService().getAllFeatureChains(context, null, this.predicate, 1);
        assertThat(chains).containsExactly(List.of(sub, preserved));
    }

    /**
     * An empty chain needs no redefinition; missing root or chain arguments are rejected.
     */
    @Test
    @DisplayName("GIVEN an empty chain or missing arguments, WHEN checking redefinitions, THEN empty is excluded and null is rejected")
    public void validateArguments() {
        var context = SysmlFactory.eINSTANCE.createPartDefinition();
        assertThat(this.predicate.test(context, List.of())).isFalse();
        assertThatNullPointerException().isThrownBy(() -> this.predicate.test(null, List.of()));
        assertThatNullPointerException().isThrownBy(() -> this.predicate.test(context, null));
    }

    private SysMLTestModel loadModel() throws IOException {
        // See /feature-chains/nested-redefinitions.xmi for the textual SysML model in an XML comment.
        var resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        var resource = new XMIResourceImpl(URI.createURI("test:///nested-redefinitions"));
        resourceSet.getResources().add(resource);
        try (var input = this.getClass().getResourceAsStream("/feature-chains/nested-redefinitions.xmi")) {
            assertThat(input).isNotNull();
            resource.load(input, Map.of());
        }
        assertThat(resource.getErrors()).isEmpty();
        return new SysMLTestModel(resource);
    }
}
