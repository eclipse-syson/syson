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
package org.eclipse.syson.sysml.metamodel.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the property constraints applied when initializing a new redefining feature.
 *
 * @author Arthur Daussy
 */
public class RedefiningFeatureInitializationTest {

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    private final ModelBuilder builder = new ModelBuilder();

    /**
     * KerML 1.1 Beta 2, 7.3.4.4-5: subsettings of ordered features must be ordered, and this restriction also applies to redefinitions.
     * The service additionally keeps the new feature detached until the caller commits the complete chain.
     */
    @Test
    @DisplayName("GIVEN an ordered attribute, WHEN initializing its redefinition, THEN the new attribute is ordered")
    public void orderedSource() {
        var source = this.builder.create(AttributeUsage.class);
        source.setIsOrdered(true);
        var local = this.builder.create(AttributeUsage.class);

        var status = this.service.initializeRedefiningFeature(local, source, this.builder.create(PartDefinition.class));

        this.assertSuccess(status);
        assertThat(local.isIsOrdered()).isTrue();
        assertThat(local.getOwner()).isNull();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.10, validateSubsettingUniquenessConformance, requires uniqueness when the source is unique.
     * Section 7.3.4.4 permits a unique feature to subset a non-unique source; 7.3.4.5 applies these rules to redefinitions.
     */
    @Test
    @DisplayName("GIVEN unique and non-unique sources, WHEN initializing redefinitions, THEN source uniqueness is enforced without forcing non-uniqueness")
    public void uniquenessConformance() {
        var source = this.builder.create(AttributeUsage.class);
        var local = this.builder.create(AttributeUsage.class);
        local.setIsUnique(false);
        var owner = this.builder.create(PartDefinition.class);

        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));
        assertThat(local.isIsUnique()).isTrue();
        source.setIsUnique(false);
        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));
        assertThat(local.isIsUnique()).isTrue();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.8, validateRedefinitionDirectionConformance, requires the same contextual in/out direction and a non-null direction for inout.
     * This service retains inout as well. SysML 2.1 Beta 2, 8.3.6.4, validateUsageIsReferential, requires directed usages to be non-composite.
     */
    @ParameterizedTest
    @EnumSource(FeatureDirectionKind.class)
    @DisplayName("GIVEN a directed feature, WHEN initializing its redefinition in a specialization, THEN its contextual direction is retained")
    public void contextualDirection(FeatureDirectionKind direction) {
        var base = this.builder.create(PartDefinition.class);
        var source = this.builder.createIn(ReferenceUsage.class, base);
        source.setDirection(direction);
        var owner = this.builder.create(PartDefinition.class);
        this.builder.addSubclassification(owner, base);
        var local = this.builder.create(ReferenceUsage.class);

        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));

        assertThat(local.getDirection()).isEqualTo(direction);
        assertThat(local.isIsComposite()).isFalse();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.1.2 (Conjugation), reverses inherited input/output directions.
     * Section 8.3.3.3.8, validateRedefinitionDirectionConformance, requires the redefinition to retain that contextual direction.
     */
    @Test
    @DisplayName("GIVEN an input on a conjugated port, WHEN initializing its redefinition, THEN the direction becomes output")
    public void conjugatedDirection() {
        var base = this.builder.create(PortDefinition.class);
        var source = this.builder.createIn(ReferenceUsage.class, base);
        source.setDirection(FeatureDirectionKind.IN);
        var owner = this.builder.create(PortDefinition.class);
        var conjugation = this.builder.createIn(Conjugation.class, owner);
        conjugation.setConjugatedType(owner);
        conjugation.setOriginalType(base);
        var local = this.builder.create(ReferenceUsage.class);
        assertThat(owner.directionOf(source)).isEqualTo(FeatureDirectionKind.OUT);

        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));

        assertThat(local.getDirection()).isEqualTo(FeatureDirectionKind.OUT);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.10, validateSubsettingConstantConformance, requires a variable target of a constant source to be constant.
     * A variable source without the constant constraint does not require constancy on its redefinition.
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a variable source with or without constancy, WHEN initializing a variable redefinition, THEN constancy is required only for a constant source")
    public void conditionalConstantConformance(boolean constant) {
        var source = this.builder.create(Feature.class);
        source.setIsVariable(true);
        source.setIsConstant(constant);
        var local = this.builder.create(Feature.class);
        local.setIsVariable(true);
        var owner = this.builder.create(PartDefinition.class);

        var status = this.service.initializeRedefiningFeature(local, source, owner);

        this.assertSuccess(status);
        assertThat(local.isIsVariable()).isTrue();
        assertThat(local.isIsConstant()).isEqualTo(constant);
    }

    /**
     * KerML 1.1 Beta 2, 7.4.3, prohibits a non-variable feature from subsetting a variable feature, including a constant one.
     * Section 7.3.4.5 makes redefinition a kind of subsetting; rejection must precede all property changes.
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a variable source with or without constancy, WHEN initializing a non-variable redefinition, THEN initialization fails without changing the target")
    public void nonVariableRedefinitionOfVariableSource(boolean constant) {
        var source = this.builder.create(Feature.class);
        source.setIsVariable(true);
        source.setIsConstant(constant);
        source.setIsOrdered(true);
        var local = this.builder.create(Feature.class);
        var owner = this.builder.create(PartDefinition.class);

        var status = this.service.initializeRedefiningFeature(local, source, owner);

        assertThat(status).isEqualTo(RedefinitionStatus.failure("A non-variable feature cannot redefine a variable feature (KerML 7.4.3)."));
        assertThat(local.isIsVariable()).isFalse();
        assertThat(local.isIsConstant()).isFalse();
        assertThat(local.isIsOrdered()).isFalse();
        assertThat(local.getOwnedRelationship()).isEmpty();
        assertThat(local.getOwner()).isNull();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.8, validateRedefinitionEndConformance, requires an end redefined within a connector to remain an end.
     * Section 8.3.3.3.2 requires a variable end to be constant (validateFeatureEndIsConstant), undirected (validateFeatureEndNoDirection)
     * and non-composite (validateFeatureEndNotDerivedAbstractCompositeOrPortion).
     */
    @Test
    @DisplayName("GIVEN an end feature in a connector, WHEN initializing its variable redefinition, THEN it remains an end and becomes constant and non-composite")
    public void endConformance() {
        var source = this.builder.create(Feature.class);
        source.setIsEnd(true);
        var local = this.builder.create(Feature.class);
        local.setIsVariable(true);
        local.setIsComposite(true);
        var owner = SysmlFactory.eINSTANCE.createConnector();

        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));

        assertThat(local.isIsEnd()).isTrue();
        assertThat(local.isIsComposite()).isFalse();
        assertThat(local.isIsConstant()).isTrue();
        assertThat(local.getDirection()).isNull();
    }

    /**
     * SysML 2.1 Beta 2, 8.3.7.3, validateAttributeUsageIsReference, and 8.3.6.3, validateReferenceUsageIsReference, require these categories to be referential.
     * Section 8.3.6.4, deriveUsageIsReference, defines isReference as the negation of isComposite, regardless of the source category.
     */
    @Test
    @DisplayName("GIVEN attribute and reference targets, WHEN initializing them from a composite source, THEN both remain non-composite")
    public void referentialCategories() {
        var source = this.builder.create(PartUsage.class);
        source.setIsComposite(true);
        var owner = this.builder.create(PartDefinition.class);
        var attribute = this.builder.create(AttributeUsage.class);
        var reference = this.builder.create(ReferenceUsage.class);

        this.assertSuccess(this.service.initializeRedefiningFeature(attribute, source, owner));
        this.assertSuccess(this.service.initializeRedefiningFeature(reference, source, owner));

        assertThat(attribute.isIsComposite()).isFalse();
        assertThat(reference.isIsComposite()).isFalse();
    }

    /**
     * SysML 2.1 Beta 2, 8.3.7.3, validateAttributeUsageFeatures, requires all features of an attribute to be non-composite.
     * Section 8.3.12.6, validatePortUsageNestedUsagesNotComposite, imposes the same requirement on non-port usages nested in a port.
     */
    @Test
    @DisplayName("GIVEN a composite part, WHEN initializing it inside an attribute or port, THEN the destination requires a non-composite feature")
    public void referentialOwners() {
        var source = this.builder.create(PartUsage.class);
        source.setIsComposite(true);
        List<Type> owners = List.of(this.builder.create(AttributeUsage.class), this.builder.create(PortUsage.class));
        for (var owner : owners) {
            var local = this.builder.create(PartUsage.class);

            this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));

            assertThat(local.isIsComposite()).isFalse();
        }
    }

    /**
     * SysML 2.1 Beta 2, 8.3.12.6, validatePortUsageIsReference, requires a port outside a PortDefinition or PortUsage to be referential.
     * Section 8.2.2.6.2 permits composite ports within ports; retaining the source choice in that case is service policy.
     */
    @Test
    @DisplayName("GIVEN a composite port, WHEN initializing it in port and part contexts, THEN composition is retained only inside the port")
    public void portComposition() {
        var source = this.builder.create(PortUsage.class);
        source.setIsComposite(true);
        var nested = this.builder.create(PortUsage.class);
        var external = this.builder.create(PortUsage.class);

        this.assertSuccess(this.service.initializeRedefiningFeature(nested, source, this.builder.create(PortDefinition.class)));
        this.assertSuccess(this.service.initializeRedefiningFeature(external, source, this.builder.create(PartDefinition.class)));

        assertThat(nested.isIsComposite()).isTrue();
        assertThat(external.isIsComposite()).isFalse();
    }

    /**
     * KerML 1.1 Beta 2, 7.3.4.5, permits anonymous redefinitions whose effective names come from the redefined feature.
     * Initializing only the required flags, without copying optional abstract/derived/portion declarations or attaching the feature, is the service contract.
     */
    @Test
    @DisplayName("GIVEN a source with optional flags and names, WHEN initializing its redefinition, THEN no unrelated declarations or memberships are copied")
    public void noUnnecessaryCopies() {
        var source = this.builder.create(PartUsage.class);
        source.setDeclaredName("source");
        source.setDeclaredShortName("s");
        source.setIsAbstract(true);
        source.setIsDerived(true);
        source.setIsPortion(true);
        var local = this.builder.create(PartUsage.class);

        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, this.builder.create(PartDefinition.class)));

        assertThat(local.getDeclaredName()).isNull();
        assertThat(local.getDeclaredShortName()).isNull();
        assertThat(local.isIsAbstract()).isFalse();
        assertThat(local.isIsDerived()).isFalse();
        assertThat(local.isIsPortion()).isFalse();
        assertThat(local.getOwnedRelationship()).isEmpty();
        assertThat(local.getOwner()).isNull();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.2, validateFeatureEndNoDirection, prohibits a direction on an end feature; 8.3.3.3.8, validateRedefinitionDirectionConformance, requires the inherited direction.
     * The service reports the incompatible requirements without partial changes; rejection before mutation is an API guarantee.
     */
    @Test
    @DisplayName("GIVEN a directed source and an end target, WHEN initializing the redefinition, THEN the incompatibility is reported without changing the target")
    public void incompatibleEndAndDirection() {
        var base = this.builder.create(PartDefinition.class);
        var source = this.builder.createIn(ReferenceUsage.class, base);
        source.setDirection(FeatureDirectionKind.IN);
        source.setIsOrdered(true);
        var owner = this.builder.create(PartDefinition.class);
        this.builder.addSubclassification(owner, base);
        var local = this.builder.create(ReferenceUsage.class);
        local.setIsEnd(true);

        var status = this.service.initializeRedefiningFeature(local, source, owner);

        assertThat(status.isSuccess()).isFalse();
        assertThat(status.message()).isEqualTo("The redefining end feature cannot be directed, derived, abstract or a portion (KerML 8.3.3.3.2).");
        assertThat(local.getDirection()).isNull();
        assertThat(local.isIsOrdered()).isFalse();
        assertThat(local.isIsEnd()).isTrue();
    }

    /**
     * SysML 2.1 Beta 2, 8.2.2.6.2, allows composite usages and referential usages declared with ref.
     * Preserving that choice for a part in an ordinary part context is service policy, not a requirement to copy isComposite from every redefined source.
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a referential or composite part, WHEN initializing it in a part context, THEN its composition choice is preserved")
    public void partCompositionChoice(boolean composite) {
        var source = this.builder.create(PartUsage.class);
        source.setIsComposite(composite);
        var local = this.builder.create(PartUsage.class);

        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, this.builder.create(PartDefinition.class)));

        assertThat(local.isIsComposite()).isEqualTo(composite);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.2, deriveFeatureType, obtains types through subsettings, including redefinitions.
     * Section 8.3.3.1.10, Type::multiplicities(), obtains inherited multiplicities through specializations; the source bounds and relation are preserved without duplicate declarations.
     * The effective-multiplicity assertion is deferred because of the metamodel defect documented below.
     */
    @Test
    @DisplayName("GIVEN a typed source with multiplicity, WHEN initializing its redefinition, THEN source bounds and the inheritance relationship are preserved without duplicate declarations")
    public void inheritedTypingAndMultiplicity() {
        var owner = this.builder.create(PartDefinition.class);
        var source = this.builder.createIn(AttributeUsage.class, owner);
        var type = this.builder.create(AttributeDefinition.class);
        this.builder.setType(source, type);
        var multiplicity = this.builder.addIntMutiplicityRange(source, 2);
        var destination = this.builder.create(PartDefinition.class);
        this.builder.addSubclassification(destination, owner);
        var local = this.builder.create(AttributeUsage.class);
        this.builder.addRedefinition(local, source);
        var relationships = List.copyOf(local.getOwnedRelationship());

        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, destination));

        assertThat(local.getOwnedTyping()).isEmpty();
        assertThat(local.getMultiplicity()).isNull();
        assertThat(local.getType()).containsExactly(type);
        assertThat(source.getMultiplicity()).isSameAs(multiplicity);
        assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(source));
        // shortcut: TypeImpl.multiplicitiesClosure does not collect inherited bounds; assert effective multiplicities after a separate metamodel fix.
        assertThat(local.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.2, validateFeatureConstantIsVariable, requires every constant feature to be variable.
     * The service rejects an inconsistent target without changing its other properties.
     */
    @Test
    @DisplayName("GIVEN a constant non-variable target, WHEN initializing its redefinition, THEN initialization fails without changing its flags")
    public void constantNonVariableTarget() {
        var source = this.builder.create(Feature.class);
        source.setIsOrdered(true);
        var local = this.builder.create(Feature.class);
        local.setIsConstant(true);

        var status = this.service.initializeRedefiningFeature(local, source, this.builder.create(PartDefinition.class));

        assertThat(status).isEqualTo(RedefinitionStatus.failure("The redefining feature cannot be constant without being variable (KerML 8.3.3.3.2)."));
        assertThat(local.isIsConstant()).isTrue();
        assertThat(local.isIsVariable()).isFalse();
        assertThat(local.isIsOrdered()).isFalse();
    }

    /**
     * Service API contract, rather than a SysML/KerML rule: missing arguments return explicit failure statuses without mutating the model.
     */
    @Test
    @DisplayName("GIVEN a missing initialization argument, WHEN initializing a redefinition, THEN a failure identifies the missing argument")
    public void missingArguments() {
        var local = this.builder.create(PartUsage.class);
        var source = this.builder.create(PartUsage.class);
        var owner = this.builder.create(PartDefinition.class);

        assertThat(this.service.initializeRedefiningFeature(null, source, owner)).isEqualTo(RedefinitionStatus.failure("No redefining feature was provided."));
        assertThat(this.service.initializeRedefiningFeature(local, null, owner)).isEqualTo(RedefinitionStatus.failure("No redefined feature was provided."));
        assertThat(this.service.initializeRedefiningFeature(local, source, null)).isEqualTo(RedefinitionStatus.failure("No destination owner was provided."));
        assertThat(local.getOwnedRelationship()).isEmpty();
    }

    private void assertSuccess(RedefinitionStatus status) {
        assertThat(status.isSuccess()).isTrue();
        assertThat(status.message()).isEmpty();
    }
}
