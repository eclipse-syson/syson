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
package org.eclipse.syson.table.configurations.view.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.syson.services.ContextualFeatureChain;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.VariantMembership;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks configurable feature paths and selected variants.
 * The SysML snippets use the Java variable names to identify otherwise unnamed fixture elements.
 * These minimal Java fixtures omit standard libraries and implicit relationships added by textual import.
 *
 * @author Jerome Gout
 */
public class CTVQueryServicesTest {

    private final CTVQueryServices service = new CTVQueryServices();

    @Test
    @DisplayName("GIVEN a name containing a dot and a nested path, WHEN labeling rows, THEN escaped segments distinguish the paths")
    public void escapesFeatureChainSegments() {
        // GIVEN an inherited attribute named a.b and a local part a containing an attribute b
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        AttributeUsage dottedAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        dottedAttribute.setDeclaredName("a.b");
        this.addFeature(definition, dottedAttribute);
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(context, definition);
        PartUsage part = SysmlFactory.eINSTANCE.createPartUsage();
        part.setDeclaredName("a");
        this.addFeature(context, part);
        AttributeUsage nestedAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        nestedAttribute.setDeclaredName("b");
        this.addFeature(part, nestedAttribute);

        // WHEN collecting and labeling the feature chains
        List<String> labels = this.service.getConfigurableFeatureChains(context).stream()
                .map(this.service::getFeatureChainLabel).toList();

        // THEN only the inherited segment has a marker, and its dot is escaped
        assertThat(labels).containsExactly("^'a.b'", "a.b");
    }

    /**
     * Verifies that local ownership alone does not make a fixed declaration configurable, whether the root is a usage
     * or a definition. Fixed attributes at both levels and an unrelated reference are excluded; the default-valued
     * attribute and the nested attribute without a value remain visible.
     *
     * <pre>
     * part def Context {
     *     attribute fixed = 4;
     *     part nestedPart {
     *         attribute nestedFixed = 5;
     *         attribute nestedConfigurable;
     *     }
     *     attribute defaultAttribute default = 1;
     *     ref reference;
     * }
     * </pre>
     *
     * The usage case places the same declarations directly inside {@code part context} instead of a definition.
     *
     * @param usageContext
     *            whether the root is a PartUsage rather than a PartDefinition
     */
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    @DisplayName("GIVEN local fixed declarations, WHEN collecting rows on a usage or definition, THEN only configurable attributes are displayed")
    public void excludesLocalFixedDeclarations(boolean usageContext) {
        Type context = usageContext ? SysmlFactory.eINSTANCE.createPartUsage() : SysmlFactory.eINSTANCE.createPartDefinition();
        AttributeUsage fixed = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(context, fixed);
        this.addValue(fixed, 4, false);
        PartUsage nestedPart = SysmlFactory.eINSTANCE.createPartUsage();
        this.addFeature(context, nestedPart);
        AttributeUsage nestedFixed = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(nestedPart, nestedFixed);
        this.addValue(nestedFixed, 5, false);
        AttributeUsage nestedConfigurable = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(nestedPart, nestedConfigurable);
        AttributeUsage defaultAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(context, defaultAttribute);
        this.addValue(defaultAttribute, 1, true);
        this.addFeature(context, SysmlFactory.eINSTANCE.createReferenceUsage());

        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(defaultAttribute)),
                new ContextualFeatureChain(context, List.of(nestedPart, nestedConfigurable)));
    }

    /**
     * Verifies that terminal filtering excludes ordinary parts and inherited attributes with fixed values without
     * pruning their descendants. Only the default-valued attribute and the nested attribute without a value are expected,
     * using the original inherited feature identities and the accessing context.
     *
     * <pre>
     * part def Definition {
     *     attribute defaultAttribute default = 2;
     *     attribute fixedAttribute = 4;
     *     part nestedPart {
     *         attribute nestedAttribute;
     *     }
     * }
     * part context : Definition;
     * </pre>
     */
    @Test
    @DisplayName("GIVEN inherited features, WHEN collecting configurable chains, THEN parts and fixed attributes are excluded without pruning descendants")
    public void filtersPartsAndFixedInheritedAttributesWithoutPruningTheirChildren() {
        // Provide an overridable default (2) and a fixed inherited value (4) in the same definition.
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        AttributeUsage defaultAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(definition, defaultAttribute);
        this.addValue(defaultAttribute, 2, true);
        AttributeUsage fixedAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(definition, fixedAttribute);
        this.addValue(fixedAttribute, 4, false);

        // The ordinary part is not configurable itself, but its attribute without a value must remain reachable.
        PartUsage nestedPart = SysmlFactory.eINSTANCE.createPartUsage();
        this.addFeature(definition, nestedPart);
        AttributeUsage nestedAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(nestedPart, nestedAttribute);

        // Access inherited features through a usage rather than querying their owning definition directly.
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(context, definition);

        // Regression checkpoint: rejecting nestedPart must not prune nestedPart.nestedAttribute.
        // Both paths retain the inherited objects; fixedAttribute and the standalone nestedPart path are excluded.
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(defaultAttribute)),
                new ContextualFeatureChain(context, List.of(nestedPart, nestedAttribute)));
        assertThat(this.service.isConfigurableFeature(nestedPart)).isFalse();
    }

    @Test
    @DisplayName("GIVEN a reference variation, WHEN collecting its own and inherited rows, THEN the variation is configurable")
    public void includesReferenceVariations() {
        // GIVEN a reference variation owned by a definition
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        ReferenceUsage variation = SysmlFactory.eINSTANCE.createReferenceUsage();
        variation.setDeclaredName("choice");
        variation.setIsVariation(true);
        this.addFeature(definition, variation);
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(context, definition);

        // WHEN evaluating the variation and collecting rows from both contexts
        boolean configurable = this.service.isConfigurableFeature(variation);
        List<ContextualFeatureChain> localChains = this.service.getConfigurableFeatureChains(definition);
        List<ContextualFeatureChain> inheritedChains = this.service.getConfigurableFeatureChains(context);

        // THEN neither the declaration nor its inherited row is omitted
        assertThat(configurable).isTrue();
        assertThat(localChains).containsExactly(new ContextualFeatureChain(definition, List.of(variation)));
        assertThat(inheritedChains).containsExactly(new ContextualFeatureChain(context, List.of(variation)));
    }

    /**
     * Verifies that an attribute redefinition remains visible in its local context before and after assigning a fixed
     * value. Both the explicit attribute syntax and the shorthand syntax must retain the local replacement identity.
     * Once fixed, the value must not be configurable from another context inheriting the replacement.
     *
     * <pre>
     * part def Definition {
     *     attribute attribute default = 1;
     * }
     * part context : Definition {
     *     attribute :>> attribute;
     * }
     * </pre>
     *
     * The shorthand case replaces {@code attribute :>> attribute;} with {@code :>> attribute;}, creating a
     * ReferenceUsage. The test then assigns the local replacement a fixed value of 2 and creates an inheriting context.
     *
     * @param shorthand
     *            whether the local replacement is a ReferenceUsage rather than an AttributeUsage
     */
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    @DisplayName("GIVEN an attribute redefinition, WHEN configuring it locally, THEN both explicit and shorthand overrides remain visible")
    public void keepsLocalAttributeOverridesVisible(boolean shorthand) {
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        AttributeUsage attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(definition, attribute);
        this.addValue(attribute, 1, true);
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(context, definition);
        Feature localAttribute = shorthand ? SysmlFactory.eINSTANCE.createReferenceUsage() : SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(context, localAttribute);
        this.addRedefinition(localAttribute, attribute);
        ContextualFeatureChain expectedChain = new ContextualFeatureChain(context, List.of(localAttribute));

        assertThat(this.service.isConfigurableFeature(localAttribute)).isTrue();
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(expectedChain);

        this.addValue(localAttribute, 2, false);

        assertThat(this.service.isConfigurableFeature(localAttribute)).isFalse();
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(expectedChain);

        PartUsage inheritingContext = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(inheritingContext, context);
        assertThat(this.service.getConfigurableFeatureChains(inheritingContext)).isEmpty();
    }

    @Test
    @DisplayName("GIVEN short names and unnamed features, WHEN collecting rows, THEN labels and sorting use the same fallback names")
    public void labelsAndSortsShortNamesAndUnnamedFeatures() {
        // GIVEN an inherited short name, an unnamed attribute and a local named attribute
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        AttributeUsage shortNamedAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        shortNamedAttribute.setDeclaredShortName("zebra");
        this.addFeature(definition, shortNamedAttribute);
        AttributeUsage unnamedAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(definition, unnamedAttribute);
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(context, definition);
        AttributeUsage localAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        localAttribute.setDeclaredName("alpha");
        this.addFeature(context, localAttribute);

        // WHEN collecting and labeling the feature chains
        List<String> labels = this.service.getConfigurableFeatureChains(context).stream()
                .map(this.service::getFeatureChainLabel).toList();

        // THEN fallback names participate in sorting without inheritance markers
        assertThat(labels).containsExactly("^<unnamed>", "alpha", "^zebra");
    }

    /**
     * Verifies that two successive shorthand redefinitions preserve the configurability of an attribute without a
     * value and an occurrence or reference variation. Although both local replacements are ReferenceUsages, the rows
     * must retain their identities and recognize the kinds of the original declarations through the intermediate replacements.
     *
     * <pre>
     * part def Definition {
     *     attribute attribute;
     *     variation part variation;
     * }
     * part intermediate : Definition {
     *     :>> attribute;
     *     :>> variation;
     * }
     * part context : intermediate {
     *     :>> attribute;
     *     :>> variation;
     * }
     * </pre>
     *
     * @param referenceVariation
     *            whether the original variation is a ReferenceUsage rather than a PartUsage
     */
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    @DisplayName("GIVEN transitive shorthand redefinitions of an attribute and a variation, WHEN collecting rows, THEN their effective kinds are recognized")
    public void recognizesTransitiveShorthandRedefinitions(boolean referenceVariation) {
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        AttributeUsage attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(definition, attribute);
        Usage variation = referenceVariation ? SysmlFactory.eINSTANCE.createReferenceUsage() : SysmlFactory.eINSTANCE.createPartUsage();
        variation.setIsVariation(true);
        this.addFeature(definition, variation);
        PartUsage intermediate = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(intermediate, definition);
        ReferenceUsage intermediateAttribute = SysmlFactory.eINSTANCE.createReferenceUsage();
        this.addFeature(intermediate, intermediateAttribute);
        this.addRedefinition(intermediateAttribute, attribute);
        ReferenceUsage intermediateVariation = SysmlFactory.eINSTANCE.createReferenceUsage();
        this.addFeature(intermediate, intermediateVariation);
        this.addRedefinition(intermediateVariation, variation);
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(context, intermediate);
        ReferenceUsage localAttribute = SysmlFactory.eINSTANCE.createReferenceUsage();
        this.addFeature(context, localAttribute);
        this.addRedefinition(localAttribute, intermediateAttribute);
        ReferenceUsage localVariation = SysmlFactory.eINSTANCE.createReferenceUsage();
        this.addFeature(context, localVariation);
        this.addRedefinition(localVariation, intermediateVariation);

        assertThat(this.service.isConfigurableFeature(localAttribute)).isTrue();
        assertThat(this.service.isConfigurableFeature(localVariation)).isTrue();
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(localAttribute)),
                new ContextualFeatureChain(context, List.of(localVariation)));
    }

    /**
     * Verifies that configurable paths follow variant selection while keeping the inherited common attribute visible
     * exactly once. Without local attribute overrides, the initial and deselected states must expose two paths;
     * selecting variant1 must expose three paths, and selecting variant2 must expose four. Once attribute1 is redefined
     * locally and assigned 10, its local path must remain visible even after switching or removing the selection.
     *
     * <pre>
     * part def AbstractVariant {
     *     attribute commonAttribute default = 1;
     * }
     * part def Definition {
     *     variation part variation : AbstractVariant {
     *         variant part variant1 : AbstractVariant {
     *             attribute attribute1;
     *         }
     *         variant part variant2 : AbstractVariant {
     *             attribute attribute21;
     *             attribute attribute22;
     *         }
     *     }
     * }
     * part context : Definition;
     * </pre>
     *
     * The text describes the initial structure. Subsequent Java mutations redefine variation locally and select a
     * variant by subsetting it, for example {@code :>> variation :> Definition::variation::variant1;}. The test changes
     * and removes this subsetting before reselecting variant1, redefining attribute1 locally, and assigning 10.
     */
    @Test
    @DisplayName("GIVEN a variation, WHEN changing variant selection, THEN selected attributes follow the selection while common attributes and local overrides remain visible")
    public void showsOnlySelectedVariantAttributesAndKeepsCommonAttributesVisibleOnce() {
        // Define the variation and the shared inherited default (1), before creating either variant.
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        PartUsage variation = SysmlFactory.eINSTANCE.createPartUsage();
        variation.setIsVariation(true);
        this.addFeature(definition, variation);
        PartDefinition abstractVariant = SysmlFactory.eINSTANCE.createPartDefinition();
        AttributeUsage commonAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(abstractVariant, commonAttribute);
        this.addValue(commonAttribute, 1, true);
        this.addTyping(variation, abstractVariant);

        // Both variants inherit the same commonAttribute object and own distinct configurable attributes.
        PartUsage variant1 = this.addVariant(variation);
        this.addTyping(variant1, abstractVariant);
        AttributeUsage attribute1 = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(variant1, attribute1);
        PartUsage variant2 = this.addVariant(variation);
        this.addTyping(variant2, abstractVariant);
        AttributeUsage attribute21 = SysmlFactory.eINSTANCE.createAttributeUsage();
        AttributeUsage attribute22 = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(variant2, attribute21);
        this.addFeature(variant2, attribute22);

        // Initially context inherits variation without a local redefinition or a selected variant.
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(context, definition);

        // Only variation and variation.commonAttribute are visible; neither variant's own attributes may leak in.
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(variation)),
                new ContextualFeatureChain(context, List.of(variation, commonAttribute)));

        // Create a local variation in context while retaining the inherited variant and common-attribute identities.
        PartUsage localVariation = SysmlFactory.eINSTANCE.createPartUsage();
        this.addFeature(context, localVariation);
        this.addRedefinition(localVariation, variation);

        // Select variant1 through :> so localVariation inherits its attributes. A FeatureValue reference does not
        // specialize localVariation and would leave attribute1 inaccessible to both traversal and redefinition.
        Subsetting selection = SysmlFactory.eINSTANCE.createSubsetting();
        localVariation.getOwnedRelationship().add(selection);
        selection.setSubsettingFeature(localVariation);
        selection.setSubsettedFeature(variant1);

        // Regression checkpoint: the third path must end at the original attribute1, not an attribute of variant2.
        // The first segment is now localVariation, and the original commonAttribute must still occur exactly once.
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(localVariation)),
                new ContextualFeatureChain(context, List.of(localVariation, commonAttribute)),
                new ContextualFeatureChain(context, List.of(localVariation, attribute1)));

        // Switch selection before creating any local attribute override: such an override would remain owned by
        // localVariation and correctly stay visible even after changing the selected variant.
        selection.setSubsettedFeature(variant2);
        // Expect attribute21 and attribute22 from variant2, no attribute1 path, and the same commonAttribute once.
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(localVariation)),
                new ContextualFeatureChain(context, List.of(localVariation, commonAttribute)),
                new ContextualFeatureChain(context, List.of(localVariation, attribute21)),
                new ContextualFeatureChain(context, List.of(localVariation, attribute22)));

        // Remove the selection while keeping the local variation redefinition in the model.
        // Variant-specific paths must disappear; localVariation and the original commonAttribute remain visible.
        localVariation.getOwnedRelationship().remove(selection);
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(localVariation)),
                new ContextualFeatureChain(context, List.of(localVariation, commonAttribute)));
        // The selection belonged only to the local variation; the inherited declaration must not own it.
        assertThat(variation.getOwnedRelationship()).doesNotContain(selection);

        // Reselect variant1, then redefine its emitted attribute1 path locally and assign a fixed value of 10.
        localVariation.getOwnedRelationship().add(selection);
        selection.setSubsettedFeature(variant1);
        AttributeUsage localAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(localVariation, localAttribute);
        this.addRedefinition(localAttribute, attribute1);
        assertThat(localAttribute).isNotSameAs(attribute1);
        assertThat(localAttribute.getOwnedRedefinition()).singleElement()
                .satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(attribute1));
        this.addValue(localAttribute, 10, false);
        // The configured path ends at the local attribute, while commonAttribute keeps its original identity once.
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(localVariation)),
                new ContextualFeatureChain(context, List.of(localVariation, localAttribute)),
                new ContextualFeatureChain(context, List.of(localVariation, commonAttribute)));

        // Switching variants changes inherited paths, but does not delete the explicit local attribute assigned 10.
        selection.setSubsettedFeature(variant2);
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(localVariation)),
                new ContextualFeatureChain(context, List.of(localVariation, localAttribute)),
                new ContextualFeatureChain(context, List.of(localVariation, commonAttribute)),
                new ContextualFeatureChain(context, List.of(localVariation, attribute21)),
                new ContextualFeatureChain(context, List.of(localVariation, attribute22)));

        // Deselection removes variant2's inherited attributes; the locally owned attribute and shared default remain.
        localVariation.getOwnedRelationship().remove(selection);
        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(localVariation)),
                new ContextualFeatureChain(context, List.of(localVariation, localAttribute)),
                new ContextualFeatureChain(context, List.of(localVariation, commonAttribute)));
    }

    @Test
    @DisplayName("GIVEN local and inherited attributes, WHEN collecting configurable chains, THEN paths are sorted by feature names without inheritance markers")
    public void sortsFeatureChainsByNameWithoutInheritanceMarkers() {
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        AttributeUsage inheritedAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        inheritedAttribute.setDeclaredName("zebra");
        this.addFeature(definition, inheritedAttribute);
        PartUsage context = SysmlFactory.eINSTANCE.createPartUsage();
        this.addTyping(context, definition);
        AttributeUsage localAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        localAttribute.setDeclaredName("alpha");
        this.addFeature(context, localAttribute);

        assertThat(this.service.getConfigurableFeatureChains(context)).containsExactly(
                new ContextualFeatureChain(context, List.of(localAttribute)),
                new ContextualFeatureChain(context, List.of(inheritedAttribute)));
    }

    private void addFeature(Type owner, Feature feature) {
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        owner.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(feature);
    }

    private void addRedefinition(Feature feature, Feature redefinedFeature) {
        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        feature.getOwnedRelationship().add(redefinition);
        redefinition.setRedefiningFeature(feature);
        redefinition.setRedefinedFeature(redefinedFeature);
    }

    private void addTyping(Feature feature, Type type) {
        FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        feature.getOwnedRelationship().add(typing);
        typing.setType(type);
        typing.setTypedFeature(feature);
    }

    private void addValue(Feature feature, int value, boolean isDefault) {
        LiteralInteger expression = SysmlFactory.eINSTANCE.createLiteralInteger();
        expression.setValue(value);
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        feature.getOwnedRelationship().add(featureValue);
        featureValue.setIsDefault(isDefault);
        featureValue.getOwnedRelatedElement().add(expression);
    }

    private PartUsage addVariant(PartUsage variation) {
        PartUsage variant = SysmlFactory.eINSTANCE.createPartUsage();
        VariantMembership membership = SysmlFactory.eINSTANCE.createVariantMembership();
        variation.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(variant);
        return variant;
    }
}
