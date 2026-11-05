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

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests redefinition removal through the public metamodel operations and derived properties.
 *
 * @author Arthur Daussy
 */
public class TypeRedefinitionTest {

    private static final String PRESERVED = "Inheritance::Base::preserved";

    private static final String ORIGINAL = "Inheritance::Base::x";

    private static final String RENAMED = "Inheritance::Renamed::renamed";

    /**
     * Unrelated feature and non-feature memberships retain their identities and order without modifying the input list.
     */
    @Test
    @DisplayName("GIVEN unrelated memberships, WHEN removing redefined features, THEN their order and the input list are preserved")
    public void preserveUnrelatedMemberships() throws IOException {
        var model = this.loadModel();
        var type = model.get(Type.class, "Inheritance::Plain");
        var original = model.get(Feature.class, ORIGINAL).getOwningMembership();
        var preserved = model.get(Feature.class, PRESERVED).getOwningMembership();
        var definition = model.get(PartDefinition.class, "Inheritance::Base").getOwningMembership();
        var input = new BasicEList<Membership>(List.of(original, definition, preserved));
        assertThat(type.removeRedefinedFeatures(input)).containsExactly(original, definition, preserved);
        assertThat(input).containsExactly(original, definition, preserved);
        assertThat(type.removeRedefinedFeatures(new BasicEList<>())).isEmpty();
    }

    /**
     * A redefinition in the candidate list removes its source even when the source occurs earlier in that list.
     */
    @Test
    @DisplayName("GIVEN an inherited source before its redefinition, WHEN reducing memberships, THEN only the redefinition survives")
    public void removeSourceRedefinedByAnotherCandidate() throws IOException {
        var model = this.loadModel();
        var type = model.get(Type.class, "Inheritance::Plain");
        var original = model.get(Feature.class, ORIGINAL).getOwningMembership();
        var renamed = model.get(Feature.class, RENAMED).getOwningMembership();
        var preserved = model.get(Feature.class, PRESERVED).getOwningMembership();
        var input = new BasicEList<Membership>(List.of(original, preserved, renamed));
        assertThat(type.removeRedefinedFeatures(input)).containsExactly(preserved, renamed);
        assertThat(input).containsExactly(original, preserved, renamed);
    }

    /**
     * A local redefinition hides an inherited redefinition sharing its source, without directly redefining that candidate.
     */
    @Test
    @DisplayName("GIVEN local and inherited redefinitions of one source, WHEN reducing memberships, THEN the inherited redefinition is removed")
    public void removeInheritedRedefinitionSharingLocalSource() throws IOException {
        var model = this.loadModel();
        var type = model.get(Type.class, "Inheritance::SharedSource");
        var renamed = model.get(Feature.class, RENAMED).getOwningMembership();
        var preserved = model.get(Feature.class, PRESERVED).getOwningMembership();
        assertThat(type.removeRedefinedFeatures(new BasicEList<>(List.of(renamed, preserved)))).containsExactly(preserved);
    }

    /**
     * Definition contexts retain the actual redefining feature and inherit only unrelated source memberships.
     */
    @ParameterizedTest
    @CsvSource({
        "Renamed, Renamed::renamed",
        "Transitive, Transitive::finalValue",
        "SharedSource, SharedSource::selected",
        "Anonymous, Anonymous::x",
        "SameName, SameName::x"
    })
    @DisplayName("GIVEN a redefining definition, WHEN querying inherited and effective features, THEN the original sources are hidden")
    public void redefiningDefinitions(String contextName, String replacementName) throws IOException {
        var model = this.loadModel();
        var context = model.get(Type.class, "Inheritance::" + contextName);
        var replacement = model.get(Feature.class, "Inheritance::" + replacementName);
        var preserved = model.get(Feature.class, PRESERVED);
        assertThat(replacement).isNotSameAs(model.get(Feature.class, ORIGINAL));
        assertThat(context.getInheritedMembership()).containsExactly(preserved.getOwningMembership());
        assertThat(context.inheritedMemberships(new BasicEList<>(), new BasicEList<>(), false)).containsExactly(preserved.getOwningMembership());
        this.assertEffectiveFeatures(context, replacement, preserved);
    }

    /**
     * Both specialization orders retain the replacement, including when it has the original feature's name.
     */
    @ParameterizedTest
    @CsvSource({
        "Diamond, Right::rightValue",
        "ReverseDiamond, Right::rightValue",
        "DiamondSameName, RightSameName::x",
        "ReverseDiamondSameName, RightSameName::x"
    })
    @DisplayName("GIVEN diamond inheritance, WHEN resolving redefinitions, THEN specialization order does not change the surviving feature identities")
    public void diamondInheritance(String contextName, String replacementName) throws IOException {
        var model = this.loadModel();
        var context = model.get(Type.class, "Inheritance::" + contextName);
        var replacement = model.get(Feature.class, "Inheritance::" + replacementName);
        var preserved = model.get(Feature.class, PRESERVED);
        assertThat(context.getInheritedMembership()).containsExactlyInAnyOrder(replacement.getOwningMembership(), preserved.getOwningMembership());
        assertThat(context.getFeature()).containsExactlyInAnyOrder(replacement, preserved);
        assertThat(context.getFeatureMembership()).containsExactlyInAnyOrder(replacement.getOwningFeatureMembership(), preserved.getOwningFeatureMembership());
    }

    /**
     * Usage contexts apply local redefinitions and inherit already resolved redefinitions from their definitions.
     */
    @Test
    @DisplayName("GIVEN typed and configured usages, WHEN querying effective features, THEN sources are hidden only in the configured contexts")
    public void usageContexts() throws IOException {
        var model = this.loadModel();
        var original = model.get(Feature.class, ORIGINAL);
        var preserved = model.get(Feature.class, PRESERVED);
        this.assertEffectiveFeatures(model.get(Type.class, "Inheritance::plainUsage"), original, preserved);
        this.assertEffectiveFeatures(model.get(Type.class, "Inheritance::configuredUsage"),
                model.get(Feature.class, "Inheritance::configuredUsage::configured"), preserved);
        this.assertEffectiveFeatures(model.get(Type.class, "Inheritance::typedUsage"), model.get(Feature.class, RENAMED), preserved);
        this.assertEffectiveFeatures(model.get(Type.class, "Inheritance::Base"), original, preserved);
    }

    /**
     * A nested redefinition supplies its own feature membership while retaining unrelated inherited children.
     */
    @Test
    @DisplayName("GIVEN nested redefinitions, WHEN querying each context, THEN effective parent and child identities replace their sources")
    public void nestedRedefinitions() throws IOException {
        var model = this.loadModel();
        var sub = model.get(Feature.class, "Inheritance::Nested::sub");
        this.assertEffectiveFeatures(model.get(Type.class, "Inheritance::Nested"), sub);
        this.assertEffectiveFeatures(sub,
                model.get(Feature.class, "Inheritance::Nested::sub::localValue"),
                model.get(Feature.class, "Inheritance::Child::childPreserved"));
        var refined = model.get(Feature.class, "Inheritance::Refined::sub");
        this.assertEffectiveFeatures(model.get(Type.class, "Inheritance::Refined"), refined);
        this.assertEffectiveFeatures(refined,
                model.get(Feature.class, "Inheritance::RefinedChild::refined"),
                model.get(Feature.class, "Inheritance::Child::childPreserved"));
    }

    private void assertEffectiveFeatures(Type context, Feature... expected) {
        assertThat(context.getFeature()).containsExactly(expected);
        assertThat(context.getFeatureMembership()).containsExactlyElementsOf(List.of(expected).stream().map(Feature::getOwningFeatureMembership).toList());
    }

    private SysMLTestModel loadModel() throws IOException {
        // See /redefinitions/inherited-redefinitions.xmi for the textual SysML model in an XML comment.
        return SysMLTestModel.load("/redefinitions/inherited-redefinitions.xmi");
    }
}
