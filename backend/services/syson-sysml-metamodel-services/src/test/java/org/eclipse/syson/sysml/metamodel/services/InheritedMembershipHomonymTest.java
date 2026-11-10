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

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.helper.MembershipComputer;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Regressions for issue #2603: semantic inheritance must not silently discard unrelated homonyms.
 *
 * @author arichard
 */
@SuppressWarnings("deprecation")
public class InheritedMembershipHomonymTest {

    private static final String FEATURE_NAME = "x";

    /**
     * Both metamodel and services queries retain conflicting names until a semantic redefinition is added.
     *
     * @param usage whether the context is a typed occurrence
     * @param shortName whether the inherited name conflict uses a short name
     */
    @ParameterizedTest
    @CsvSource({"false, false", "false, true", "true, false", "true, true"})
    public void ownedAndInheritedHomonyms(boolean usage, boolean shortName) {
        var builder = new ModelBuilder();
        var base = builder.createWithName(PartDefinition.class, "Base");
        var original = builder.createInWithName(AttributeUsage.class, base, FEATURE_NAME);
        if (shortName) {
            original.setDeclaredName("original");
            original.setDeclaredShortName(FEATURE_NAME);
        }
        Type context;
        if (usage) {
            var occurrence = builder.createWithName(PartUsage.class, "occurrence");
            builder.setType(occurrence, base);
            context = occurrence;
        } else {
            var derived = builder.createWithName(PartDefinition.class, "Derived");
            builder.addSubclassification(derived, base);
            context = derived;
        }
        var owned = builder.createInWithName(AttributeUsage.class, context, FEATURE_NAME);

        assertThat(owned.redefines(original)).isFalse();
        assertThat(context.getInheritedFeature()).containsExactly(original);
        assertThat(context.getFeature()).containsExactlyInAnyOrder(owned, original);
        assertThat(context.getFeatureMembership()).extracting(membership -> membership.getMemberElement()).containsExactlyInAnyOrder(owned, original);
        assertThat(context.getMembership()).contains(owned.getOwningMembership(), original.getOwningMembership());
        assertThat(new MembershipComputer<>(context, new BasicEList<>()).inheritedMemberships()).contains(original.getOwningMembership());
        assertThat(new MembershipComputer<>(context, new BasicEList<>()).visibleMemberships(false, true, true))
                .contains(owned.getOwningMembership(), original.getOwningMembership());
        assertThat(new org.eclipse.syson.sysml.helper.MembershipComputer<>(context, new BasicEList<>()).visibleMemberships(false, true, true))
                .contains(owned.getOwningMembership(), original.getOwningMembership());

        builder.addRedefinition(owned, original);
        assertThat(context.getInheritedFeature()).doesNotContain(original);
        assertThat(context.getFeature()).containsExactly(owned);
        assertThat(new MembershipComputer<>(context, new BasicEList<>()).inheritedMemberships()).doesNotContain(original.getOwningMembership());
    }

    /**
     * Inherited homonyms survive both specialization orders and subsequent inheritance through another type.
     *
     * @param reverse whether the specialization order is reversed
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void inheritedHomonyms(boolean reverse) {
        var builder = new ModelBuilder();
        var left = builder.createWithName(PartDefinition.class, "Left");
        var right = builder.createWithName(PartDefinition.class, "Right");
        var leftFeature = builder.createInWithName(AttributeUsage.class, left, FEATURE_NAME);
        var rightFeature = builder.createInWithName(AttributeUsage.class, right, FEATURE_NAME);
        var derived = builder.createWithName(PartDefinition.class, "Derived");
        if (reverse) {
            builder.addSubclassification(derived, right);
            builder.addSubclassification(derived, left);
        } else {
            builder.addSubclassification(derived, left);
            builder.addSubclassification(derived, right);
        }
        var child = builder.createWithName(PartDefinition.class, "Child");
        builder.addSubclassification(child, derived);

        assertThat(derived.getInheritedFeature()).containsExactlyInAnyOrder(leftFeature, rightFeature);
        assertThat(child.getInheritedFeature()).containsExactlyInAnyOrder(leftFeature, rightFeature);
        assertThat(new MembershipComputer<>(child, new BasicEList<>()).inheritedMemberships())
                .containsExactlyInAnyOrder(leftFeature.getOwningMembership(), rightFeature.getOwningMembership());
    }

    /**
     * An imported homonym remains hidden by an owned member, independently of semantic inheritance.
     */
    @Test
    public void conflictingImportsRemainFiltered() {
        var builder = new ModelBuilder();
        var source = builder.createWithName(PartDefinition.class, "Source");
        var imported = builder.createInWithName(AttributeUsage.class, source, FEATURE_NAME);
        var context = builder.createWithName(PartDefinition.class, "Context");
        var owned = builder.createInWithName(AttributeUsage.class, context, FEATURE_NAME);
        var namespaceImport = SysmlFactory.eINSTANCE.createNamespaceImport();
        namespaceImport.setImportedNamespace(source);
        context.getOwnedRelationship().add(namespaceImport);

        assertThat(context.getMembership()).contains(owned.getOwningMembership()).doesNotContain(imported.getOwningMembership());
        assertThat(new MembershipComputer<>(context, new BasicEList<>()).visibleMemberships(false, true, true))
                .contains(owned.getOwningMembership()).doesNotContain(imported.getOwningMembership());
        assertThat(new org.eclipse.syson.sysml.helper.MembershipComputer<>(context, new BasicEList<>()).visibleMemberships(false, true, true))
                .contains(owned.getOwningMembership()).doesNotContain(imported.getOwningMembership());
    }

    /**
     * Feature-chain lookup targets the inherited identity without reusing or modifying an unrelated owned homonym.
     */
    @Test
    public void featureChainPreservesOwnedHomonym() {
        var builder = new ModelBuilder();
        var root = builder.createRootNamespace();
        var base = builder.createInWithName(PartDefinition.class, root, "Base");
        var inherited = builder.createInWithName(AttributeUsage.class, base, FEATURE_NAME);
        var occurrence = builder.createInWithName(PartUsage.class, root, "occurrence");
        builder.setType(occurrence, base);
        var owned = builder.createInWithName(AttributeUsage.class, occurrence, FEATURE_NAME);

        assertThat(new FeatureChainComputer().computeShortestPath(root, inherited, occurrence)).contains(List.of(occurrence, inherited));
        assertThat(occurrence.getOwnedFeature()).containsExactly(owned);
        assertThat(owned.getOwnedRedefinition()).isEmpty();
        assertThat(occurrence.getInheritedFeature()).containsExactly(inherited);
    }
}
