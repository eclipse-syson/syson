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

import java.util.List;

import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.redefine.RedefinitionStatus;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks conformance properties applied to a new redefining feature.
 *
 * @author Arthur Daussy
 */
public class RedefiningFeatureInitializationTest {

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    private final ModelBuilder builder = new ModelBuilder();

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.10, validateSubsettingConstantConformance, requires a variable target of a constant source to be constant.
     * A variable source without the constant constraint does not require constancy on its redefinition.
     *
     * @param constant
     *            whether the redefined source is constant
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a variable source with or without constancy, WHEN initializing a variable redefinition, THEN constancy is required only for a constant source")
    public void verifyConditionalConstantConformance(boolean constant) {
        // GIVEN The target is already variable; the variable source may additionally require constancy.
        Feature source = this.builder.create(Feature.class);
        source.setIsVariable(true);
        source.setIsConstant(constant);
        Feature local = this.builder.create(Feature.class);
        local.setIsVariable(true);
        PartDefinition owner = this.builder.create(PartDefinition.class);

        // WHEN Initialize the local feature against that source.
        RedefinitionStatus status = this.service.initializeRedefiningFeature(local, source, owner);

        // THEN Keep variability and require constancy precisely when the source is constant.
        this.assertSuccess(status);
        assertThat(local.isIsVariable()).isTrue();
        assertThat(local.isIsConstant()).isEqualTo(constant);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.1.2 (Conjugation), reverses inherited input/output directions.
     * Section 8.3.3.3.8, validateRedefinitionDirectionConformance, requires the redefinition to retain that contextual direction.
     */
    @Test
    @DisplayName("GIVEN an input on a conjugated port, WHEN initializing its redefinition, THEN the direction becomes output")
    public void verifyConjugatedDirection() {
        // GIVEN An input source belongs to a type conjugated in the destination, where its effective direction is out.
        PortDefinition base = this.builder.create(PortDefinition.class);
        ReferenceUsage source = this.builder.createIn(ReferenceUsage.class, base);
        source.setDirection(FeatureDirectionKind.IN);
        PortDefinition owner = this.builder.create(PortDefinition.class);
        Conjugation conjugation = this.builder.createIn(Conjugation.class, owner);
        conjugation.setConjugatedType(owner);
        conjugation.setOriginalType(base);
        ReferenceUsage local = this.builder.create(ReferenceUsage.class);
        assertThat(owner.directionOf(source)).isEqualTo(FeatureDirectionKind.OUT);

        // WHEN Initialize the local feature in that conjugated context.
        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));

        // THEN Use the destination's out direction rather than the source's stored in direction.
        assertThat(local.getDirection()).isEqualTo(FeatureDirectionKind.OUT);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.2, validateFeatureConstantIsVariable, requires every constant feature to be variable.
     * The service makes a constant target variable and applies the remaining source requirements.
     */
    @Test
    @DisplayName("GIVEN a constant non-variable target, WHEN initializing its redefinition, THEN the target becomes variable and retains constancy")
    public void verifyConstantNonVariableTarget() {
        // GIVEN a constant target whose variability has not been set.
        Feature source = this.builder.create(Feature.class);
        source.setIsOrdered(true);
        Feature local = this.builder.create(Feature.class);
        local.setIsConstant(true);

        // WHEN initializing the target against an ordered source.
        RedefinitionStatus status = this.service.initializeRedefiningFeature(local, source, this.builder.create(PartDefinition.class));

        // THEN initialization repairs variability while preserving constancy and propagating ordering.
        this.assertSuccess(status);
        assertThat(local.isIsConstant()).isTrue();
        assertThat(local.isIsVariable()).isTrue();
        assertThat(local.isIsOrdered()).isTrue();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.8, validateRedefinitionDirectionConformance, requires the same contextual in/out direction and a non-null direction for inout.
     * This service retains inout as well. SysML 2.1 Beta 2, 8.3.6.4, validateUsageIsReferential, requires directed usages to be non-composite.
     *
     * @param direction
     *            the source direction to retain in the destination context
     */
    @ParameterizedTest
    @EnumSource(FeatureDirectionKind.class)
    @DisplayName("GIVEN a directed feature, WHEN initializing its redefinition in a specialization, THEN its contextual direction is retained")
    public void verifyContextualDirection(FeatureDirectionKind direction) {
        // GIVEN The source has one of the supported directed parameter directions.
        PartDefinition base = this.builder.create(PartDefinition.class);
        ReferenceUsage source = this.builder.createIn(ReferenceUsage.class, base);
        source.setDirection(direction);
        PartDefinition owner = this.builder.create(PartDefinition.class);
        this.builder.addSubclassification(owner, base);
        ReferenceUsage local = this.builder.create(ReferenceUsage.class);

        // WHEN Initialize the local usage in its destination type.
        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));

        // THEN Retain the contextual direction and make the directed usage referential.
        assertThat(local.getDirection()).isEqualTo(direction);
        assertThat(local.isIsComposite()).isFalse();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.8, validateRedefinitionEndConformance, requires an end redefined within a connector to remain an end.
     * Section 8.3.3.3.2 requires a variable end to be constant (validateFeatureEndIsConstant), undirected (validateFeatureEndNoDirection)
     * and non-composite (validateFeatureEndNotDerivedAbstractCompositeOrPortion).
     */
    @Test
    @DisplayName("GIVEN an end feature in a connector, WHEN initializing its variable redefinition, THEN it remains an end and becomes constant and non-composite")
    public void verifyEndConformance() {
        // GIVEN The source is an end; the variable local target initially requests composition.
        Feature source = this.builder.create(Feature.class);
        source.setIsEnd(true);
        Feature local = this.builder.create(Feature.class);
        local.setIsVariable(true);
        local.setIsComposite(true);
        Connector owner = SysmlFactory.eINSTANCE.createConnector();

        // WHEN Initialize the target within a connector.
        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));

        // THEN Retain end semantics, clear composition and require constancy for this variable end.
        assertThat(local.isIsEnd()).isTrue();
        assertThat(local.isIsComposite()).isFalse();
        assertThat(local.isIsConstant()).isTrue();
        assertThat(local.getDirection()).isNull();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.2, validateFeatureEndNoDirection, prohibits a direction on an end feature; 8.3.3.3.8, validateRedefinitionDirectionConformance, requires the inherited direction.
     * The service reports the incompatible requirements without partial changes; rejection before mutation is an API guarantee.
     */
    @Test
    @DisplayName("GIVEN a directed source and an end target, WHEN initializing the redefinition, THEN the incompatibility is reported without changing the target")
    public void verifyIncompatibleEndAndDirection() {
        // GIVEN The target is an end but the source requires an input direction.
        PartDefinition base = this.builder.create(PartDefinition.class);
        ReferenceUsage source = this.builder.createIn(ReferenceUsage.class, base);
        source.setDirection(FeatureDirectionKind.IN);
        source.setIsOrdered(true);
        PartDefinition owner = this.builder.create(PartDefinition.class);
        this.builder.addSubclassification(owner, base);
        ReferenceUsage local = this.builder.create(ReferenceUsage.class);
        local.setIsEnd(true);

        // WHEN Try to initialize this incompatible combination.
        RedefinitionStatus status = this.service.initializeRedefiningFeature(local, source, owner);

        // THEN Report the conflict before changing the local direction or ordered flag.
        assertThat(status.isSuccess()).isFalse();
        assertThat(status.message()).isEqualTo("The redefining end feature cannot be directed, derived, abstract or a portion (KerML 8.3.3.3.2).");
        assertThat(local.getDirection()).isNull();
        assertThat(local.isIsOrdered()).isFalse();
        assertThat(local.isIsEnd()).isTrue();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.2, deriveFeatureType, obtains types through subsettings, including redefinitions.
     * Section 8.3.3.1.10, Type::multiplicities(), obtains inherited multiplicities through specializations; the source bounds and relation are preserved without duplicate declarations.
     */
    @Test
    @DisplayName("GIVEN a typed source with multiplicity, WHEN initializing its redefinition, THEN effective typing and multiplicity are inherited without duplicate declarations")
    public void verifyInheritedTypingAndMultiplicity() {
        // GIVEN The source carries a typing and multiplicity, and the local feature already redefines it.
        PartDefinition owner = this.builder.create(PartDefinition.class);
        AttributeUsage source = this.builder.createIn(AttributeUsage.class, owner);
        AttributeDefinition type = this.builder.create(AttributeDefinition.class);
        this.builder.setType(source, type);
        MultiplicityRange multiplicity = this.builder.addIntMutiplicityRange(source, 2);
        PartDefinition destination = this.builder.create(PartDefinition.class);
        this.builder.addSubclassification(destination, owner);
        AttributeUsage local = this.builder.create(AttributeUsage.class);
        this.builder.addRedefinition(local, source);
        List<Relationship> relationships = List.copyOf(local.getOwnedRelationship());

        // WHEN Initialize the local properties.
        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, destination));

        // THEN Inherit typing and the exact source multiplicity object while keeping local declarations absent.
        assertThat(local.getOwnedTyping()).isEmpty();
        assertThat(local.getMultiplicity()).isNull();
        assertThat(local.getType()).containsExactly(type);
        assertThat(source.getMultiplicity()).isSameAs(multiplicity);
        assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(source));
        assertThat(local.multiplicities()).containsExactly(multiplicity);
        assertThat(local.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * Service API contract, rather than a SysML/KerML rule: missing arguments return explicit failure statuses without mutating the model.
     */
    @Test
    @DisplayName("GIVEN a missing initialization argument, WHEN initializing a redefinition, THEN a failure identifies the missing argument")
    public void verifyMissingArguments() {
        // GIVEN Prepare a detached local feature and valid source and owner for the remaining arguments.
        PartUsage local = this.builder.create(PartUsage.class);
        PartUsage source = this.builder.create(PartUsage.class);
        PartDefinition owner = this.builder.create(PartDefinition.class);

        // WHEN Omit each required initialization argument in turn.
        assertThat(this.service.initializeRedefiningFeature(null, source, owner)).isEqualTo(RedefinitionStatus.failure("No redefining feature was provided."));
        // THEN Return a specific failure for each missing argument and leave the local feature detached.
        assertThat(this.service.initializeRedefiningFeature(local, null, owner)).isEqualTo(RedefinitionStatus.failure("No redefined feature was provided."));
        assertThat(this.service.initializeRedefiningFeature(local, source, null)).isEqualTo(RedefinitionStatus.failure("No destination owner was provided."));
        assertThat(local.getOwnedRelationship()).isEmpty();
    }

    /**
     * KerML 1.1 Beta 2, 7.3.4.5, permits anonymous redefinitions whose effective names come from the redefined feature.
     * Initializing only the required flags, without copying optional abstract/derived/portion declarations or attaching the feature, is the service contract.
     */
    @Test
    @DisplayName("GIVEN a source with optional flags and names, WHEN initializing its redefinition, THEN no unrelated declarations or memberships are copied")
    public void verifyNoUnnecessaryCopies() {
        // GIVEN Give the source explicit names and abstract, derived and portion flags.
        PartUsage source = this.builder.create(PartUsage.class);
        source.setDeclaredName("source");
        source.setDeclaredShortName("s");
        source.setIsAbstract(true);
        source.setIsDerived(true);
        source.setIsPortion(true);
        PartUsage local = this.builder.create(PartUsage.class);

        // WHEN Initialize an anonymous local feature.
        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, this.builder.create(PartDefinition.class)));

        // THEN Propagate required conformance properties without copying names or unrelated source flags.
        assertThat(local.getDeclaredName()).isNull();
        assertThat(local.getDeclaredShortName()).isNull();
        assertThat(local.isIsAbstract()).isFalse();
        assertThat(local.isIsDerived()).isFalse();
        assertThat(local.isIsPortion()).isFalse();
        assertThat(local.getOwnedRelationship()).isEmpty();
        assertThat(local.getOwner()).isNull();
    }

    /**
     * KerML 1.1 Beta 2, 7.3.4.4-5: subsettings of ordered features must be ordered, and this restriction also applies to redefinitions.
     */
    @Test
    @DisplayName("GIVEN an ordered attribute, WHEN initializing its redefinition, THEN the new attribute is ordered")
    public void verifyOrderedSource() {
        // GIVEN The inherited source is explicitly ordered.
        AttributeUsage source = this.builder.create(AttributeUsage.class);
        source.setIsOrdered(true);
        AttributeUsage local = this.builder.create(AttributeUsage.class);

        // WHEN Initialize its detached local attribute.
        RedefinitionStatus status = this.service.initializeRedefiningFeature(local, source, this.builder.create(PartDefinition.class));

        // THEN Set local ordering while leaving attachment to the caller.
        this.assertSuccess(status);
        assertThat(local.isIsOrdered()).isTrue();
        assertThat(local.getOwner()).isNull();
    }

    /**
     * SysML 2.1 Beta 2, 8.2.2.6.2, allows composite usages and referential usages declared with ref.
     * Preserving that choice for a part in an ordinary part context is service policy, not a requirement to copy isComposite from every redefined source.
     *
     * @param composite
     *            whether the source part is composite
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a referential or composite part, WHEN initializing it in a part context, THEN its composition choice is preserved")
    public void verifyPartCompositionChoice(boolean composite) {
        // GIVEN The source part is either composite or referential.
        PartUsage source = this.builder.create(PartUsage.class);
        source.setIsComposite(composite);
        PartUsage local = this.builder.create(PartUsage.class);

        // WHEN Initialize its local part inside an ordinary part definition.
        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, this.builder.create(PartDefinition.class)));

        // THEN Keep the source composition choice where the destination permits it.
        assertThat(local.isIsComposite()).isEqualTo(composite);
    }

    /**
     * SysML 2.1 Beta 2, 8.3.12.6, validatePortUsageIsReference, requires a port outside a PortDefinition or PortUsage to be referential.
     * Section 8.2.2.6.2 permits composite ports within ports; retaining the source choice in that case is service policy.
     */
    @Test
    @DisplayName("GIVEN a composite port, WHEN initializing it in port and part contexts, THEN composition is retained only inside the port")
    public void verifyPortComposition() {
        // GIVEN The source port is composite and the two destinations are a port definition and a part definition.
        PortUsage source = this.builder.create(PortUsage.class);
        source.setIsComposite(true);
        PortUsage nested = this.builder.create(PortUsage.class);
        PortUsage external = this.builder.create(PortUsage.class);

        // WHEN Initialize one local port in each destination.
        this.assertSuccess(this.service.initializeRedefiningFeature(nested, source, this.builder.create(PortDefinition.class)));
        // THEN Keep composition inside the port and make the externally owned port referential.
        this.assertSuccess(this.service.initializeRedefiningFeature(external, source, this.builder.create(PartDefinition.class)));

        assertThat(nested.isIsComposite()).isTrue();
        assertThat(external.isIsComposite()).isFalse();
    }

    /**
     * SysML 2.1 Beta 2, 8.3.7.3, validateAttributeUsageIsReference, and 8.3.6.3, validateReferenceUsageIsReference, require these categories to be referential.
     * Section 8.3.6.4, deriveUsageIsReference, defines isReference as the negation of isComposite, regardless of the source category.
     */
    @Test
    @DisplayName("GIVEN attribute and reference targets, WHEN initializing them from a composite source, THEN both remain non-composite")
    public void verifyReferentialCategories() {
        // GIVEN A composite source is used to initialize an attribute and a reference usage.
        PartUsage source = this.builder.create(PartUsage.class);
        source.setIsComposite(true);
        PartDefinition owner = this.builder.create(PartDefinition.class);
        AttributeUsage attribute = this.builder.create(AttributeUsage.class);
        ReferenceUsage reference = this.builder.create(ReferenceUsage.class);

        // WHEN Initialize both local categories in the same owner.
        this.assertSuccess(this.service.initializeRedefiningFeature(attribute, source, owner));
        // THEN Both categories remain referential regardless of the source's composition.
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
    public void verifyReferentialOwners() {
        // GIVEN A composite part is being redefined within an owner that restricts composition.
        PartUsage source = this.builder.create(PartUsage.class);
        source.setIsComposite(true);
        List<Type> owners = List.of(this.builder.create(AttributeUsage.class), this.builder.create(PortUsage.class));
        for (Type owner : owners) {
            PartUsage local = this.builder.create(PartUsage.class);

        // WHEN/THEN Initialize the local feature in each tested destination category. Respect the destination restriction and make the feature non-composite.
            this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));

            assertThat(local.isIsComposite()).isFalse();
        }
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.10, validateSubsettingUniquenessConformance, requires uniqueness when the source is unique.
     * Section 7.3.4.4 permits a unique feature to subset a non-unique source; 7.3.4.5 applies these rules to redefinitions.
     */
    @Test
    @DisplayName("GIVEN unique and non-unique sources, WHEN initializing redefinitions, THEN source uniqueness is enforced without forcing non-uniqueness")
    public void verifyUniquenessConformance() {
        // GIVEN The local attribute starts non-unique while its source has the default uniqueness requirement.
        AttributeUsage source = this.builder.create(AttributeUsage.class);
        AttributeUsage local = this.builder.create(AttributeUsage.class);
        local.setIsUnique(false);
        PartDefinition owner = this.builder.create(PartDefinition.class);

        // WHEN Initialize against the unique source, then repeat after clearing source uniqueness.
        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));
        // THEN Enforce required uniqueness without clearing the uniqueness already established on the local feature.
        assertThat(local.isIsUnique()).isTrue();
        source.setIsUnique(false);
        this.assertSuccess(this.service.initializeRedefiningFeature(local, source, owner));
        assertThat(local.isIsUnique()).isTrue();
    }

    /**
     * KerML 1.1 Beta 2, 7.4.3, prohibits a non-variable feature from subsetting a variable feature, including a constant one.
     * Section 7.3.4.5 makes redefinition a kind of subsetting; initialization propagates variability and required constancy.
     *
     * @param constant
     *            whether the redefined source is constant
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a variable source with or without constancy, WHEN initializing a non-variable redefinition, THEN the target becomes variable and preserves source constancy")
    public void verifyVariableSource(boolean constant) {
        // GIVEN an initially non-variable target and a variable source.
        Feature source = this.builder.create(Feature.class);
        source.setIsVariable(true);
        source.setIsConstant(constant);
        source.setIsOrdered(true);
        Feature local = this.builder.create(Feature.class);
        PartDefinition owner = this.builder.create(PartDefinition.class);

        // WHEN initializing the newly created feature against its variable source.
        RedefinitionStatus status = this.service.initializeRedefiningFeature(local, source, owner);

        // THEN the source requirements are applied before reporting success.
        assertThat(status.isSuccess()).isTrue();
        assertThat(local.isIsVariable()).isTrue();
        assertThat(local.isIsConstant()).isEqualTo(constant);
        assertThat(local.isIsOrdered()).isTrue();
        assertThat(local.getOwnedRelationship()).isEmpty();
        assertThat(local.getOwner()).isNull();
    }

    private void assertSuccess(RedefinitionStatus status) {
        assertThat(status.isSuccess()).isTrue();
        assertThat(status.message()).isEmpty();
    }
}
