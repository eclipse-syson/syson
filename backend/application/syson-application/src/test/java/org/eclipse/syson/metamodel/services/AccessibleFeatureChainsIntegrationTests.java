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
package org.eclipse.syson.metamodel.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.sirius.components.collaborative.dto.EditingContextEventInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.graphql.tests.EditingContextEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.data.ModelConfigurationTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.metamodel.services.NonStandardLibraryFeaturePredicate;
import org.eclipse.syson.sysml.metamodel.services.RequiresRedefinitionFeatureChainPredicate;
import org.eclipse.syson.sysml.metamodel.services.data.SysMLTestModel;
import org.eclipse.syson.sysml.util.LibraryNamespaceProvider;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests accessible feature chains on a persisted SysML model with the application's standard libraries.
 *
 * @author Arthur Daussy
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessibleFeatureChainsIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private EditingContextEventSubscriptionRunner editingContextEventSubscriptionRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    private final MetamodelQueryElementService service = new MetamodelQueryElementService();

    private final NonStandardLibraryFeaturePredicate outsideStandardLibraries = new NonStandardLibraryFeaturePredicate();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    /**
     * The unconfigured definition exposes nested attributes and the variation's common attribute, without traversing
     * variant memberships. See /models/accessible-feature-chains/ccp-example.sysml for the source of the SQL fixture.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN the persisted CCP example, WHEN querying its definition without standard library features, THEN nested attribute chains are returned")
    public void definitionAttributeChains() {
        this.checkModel(model -> {
            var partPD2 = model.get(Feature.class, "Example::PD1::partPD2");
            var partPD3 = model.get(Feature.class, "Example::PD1::partPD3");
            var variation = model.get(Feature.class, "Example::PD3::variantPart");
            var chains = this.service.getAllFeatureChains(model.get(Type.class, "Example::PD1"), this.outsideStandardLibraries, this::isAttributeOrRedefinition, 100);

            // Variants var1 and var2 are not FeatureMemberships; their own attributes are absent from this context.
            assertThat(chains).containsExactly(
                    List.of(model.get(Feature.class, "Example::PD1::attr1_1")),
                    List.of(model.get(Feature.class, "Example::PD1::attr1_2")),
                    List.of(partPD2, model.get(Feature.class, "Example::PD2::attr2_1")),
                    List.of(partPD3, model.get(Feature.class, "Example::PD3::attr3_1")),
                    List.of(partPD3, variation, model.get(Feature.class, "Example::AbstractVariant::commonAttr")));
        });
    }

    /**
     * The first configured occurrence uses its local replacements and the selected variant's attributes while retaining
     * unrelated inherited attributes. The terminal predicate must accept shorthand ReferenceUsage redefinitions too.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN the first configured occurrence, WHEN selecting attribute chains, THEN local redefinitions and refined variant attributes are retained")
    public void firstConfiguredOccurrence() {
        this.checkModel(model -> {
            var partPD3 = model.get(Feature.class, "Example::part1::partPD3");
            var variation = model.get(Feature.class, "Example::part1::partPD3::variantPart");
            var replacement = model.get(Feature.class, "Example::part1::partPD3::variantPart::attrVar1");
            assertThat(replacement).isNotInstanceOf(AttributeUsage.class);

            // The replacement identity must survive terminal selection even though its metaclass is ReferenceUsage.
            var chains = this.service.getAllFeatureChains(model.get(Type.class, "Example::part1"), this.outsideStandardLibraries, this::isAttributeOrRedefinition, 100);
            assertThat(chains).containsExactly(
                    List.of(partPD3, variation, replacement),
                    List.of(partPD3, variation, model.get(Feature.class, "Example::AbstractVariant::commonAttr")),
                    List.of(partPD3, model.get(Feature.class, "Example::PD3::attr3_1")),
                    List.of(model.get(Feature.class, "Example::PD1::attr1_1")),
                    List.of(model.get(Feature.class, "Example::PD1::attr1_2")),
                    List.of(model.get(Feature.class, "Example::PD1::partPD2"), model.get(Feature.class, "Example::PD2::attr2_1")));
        });
    }

    /**
     * The second occurrence replaces attributes at several levels and selects the other variant. Every segment must
     * use the effective local replacement, while the unchanged computed attribute remains inherited.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN nested shorthand redefinitions, WHEN querying the second occurrence, THEN original feature identities are hidden at every level")
    public void secondConfiguredOccurrence() {
        this.checkModel(model -> {
            var partPD2 = model.get(Feature.class, "Example::part2::partPD2");
            var partPD3 = model.get(Feature.class, "Example::part2::partPD3");
            var variation = model.get(Feature.class, "Example::part2::partPD3::variantPart");

            // Exact identities exclude the original PD1, PD2, PD3 and AbstractVariant features replaced locally.
            var chains = this.service.getAllFeatureChains(model.get(Type.class, "Example::part2"), this.outsideStandardLibraries, this::isAttributeOrRedefinition, 100);
            assertThat(chains).containsExactly(
                    List.of(model.get(Feature.class, "Example::part2::attr1_1")),
                    List.of(partPD2, model.get(Feature.class, "Example::part2::partPD2::attr2_1")),
                    List.of(partPD3, model.get(Feature.class, "Example::part2::partPD3::attr3_1")),
                    List.of(partPD3, variation, model.get(Feature.class, "Example::part2::partPD3::variantPart::commonAttr")),
                    List.of(partPD3, variation, model.get(Feature.class, "Example::part2::partPD3::variantPart::attr1Var2")),
                    List.of(partPD3, variation, model.get(Feature.class, "Example::part2::partPD3::variantPart::attr2Var2")),
                    List.of(model.get(Feature.class, "Example::PD1::attr1_2")));
        });
    }

    /**
     * Excluding standard library features from PD1 removes self/that branches while retaining
     * effective local or inherited model features. No terminal filter hides intermediate paths.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN an unconfigured definition, WHEN excluding standard library features, THEN model feature paths remain")
    public void definitionWithoutStandardLibraryFeatures() {
        this.checkModel(model -> {
            var context = model.get(Type.class, "Example::PD1");
            var chains = this.service.getAllFeatureChains(context, this.outsideStandardLibraries, null, 100);

            // Standard library features are pruned even when inherited through explicit String typing.
            assertThat(chains).extracting(chain -> chain.stream().map(Feature::getName).collect(Collectors.joining("."))).containsExactly(
                    "attr1_1",
                    "attr1_2",
                    "partPD2",
                    "partPD2.attr2_1",
                    "partPD3",
                    "partPD3.attr3_1",
                    "partPD3.variantPart",
                    "partPD3.variantPart.commonAttr");
        });
    }

    /**
     * Excluding standard library features from part1 removes self/that branches while retaining
     * effective local or inherited model features. No terminal filter hides intermediate paths.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN the first configured occurrence, WHEN excluding standard library features, THEN model feature paths remain")
    public void firstOccurrenceWithoutStandardLibraryFeatures() {
        this.checkModel(model -> {
            var context = model.get(Type.class, "Example::part1");
            var chains = this.service.getAllFeatureChains(context, this.outsideStandardLibraries, null, 100);

            // Standard library features are pruned even when inherited through explicit String typing.
            assertThat(chains).extracting(chain -> chain.stream().map(Feature::getName).collect(Collectors.joining("."))).containsExactly(
                    "partPD3",
                    "partPD3.variantPart",
                    "partPD3.variantPart.attrVar1",
                    "partPD3.variantPart.commonAttr",
                    "partPD3.attr3_1",
                    "attr1_1",
                    "attr1_2",
                    "partPD2",
                    "partPD2.attr2_1");
        });
    }

    /**
     * Excluding standard library features from part2 removes self/that branches while retaining
     * effective local or inherited model features. No terminal filter hides intermediate paths.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN the second configured occurrence, WHEN excluding standard library features, THEN model feature paths remain")
    public void secondOccurrenceWithoutStandardLibraryFeatures() {
        this.checkModel(model -> {
            var context = model.get(Type.class, "Example::part2");
            var chains = this.service.getAllFeatureChains(context, this.outsideStandardLibraries, null, 100);

            // Standard library features are pruned even when inherited through explicit String typing.
            assertThat(chains).extracting(chain -> chain.stream().map(Feature::getName).collect(Collectors.joining("."))).containsExactly(
                    "attr1_1",
                    "partPD2",
                    "partPD2.attr2_1",
                    "partPD3",
                    "partPD3.attr3_1",
                    "partPD3.variantPart",
                    "partPD3.variantPart.commonAttr",
                    "partPD3.variantPart.attr1Var2",
                    "partPD3.variantPart.attr2Var2",
                    "attr1_2");
        });
    }

    /**
     * From PD1, fully local chains are excluded while inherited segments remain selectable, regardless of value configurability.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN PD1, WHEN selecting chains requiring redefinition, THEN fully local chains are excluded")
    public void definitionChainsRequiringRedefinition() {
        this.checkModel(model -> {
            var context = model.get(Type.class, "Example::PD1");
            var chains = this.service.getAllFeatureChains(context, this.outsideStandardLibraries,
                    new RequiresRedefinitionFeatureChainPredicate(), 100);

            // Selection removes local results without pruning their inherited descendants.
            assertThat(chains).extracting(chain -> chain.stream().map(Feature::getName).collect(Collectors.joining("."))).containsExactly(
                    "partPD2.attr2_1",
                    "partPD3.attr3_1",
                    "partPD3.variantPart",
                    "partPD3.variantPart.commonAttr");
        });
    }

    /**
     * From part1, fully local chains are excluded while inherited segments remain selectable, regardless of value configurability.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN part1, WHEN selecting chains requiring redefinition, THEN fully local chains are excluded")
    public void firstOccurrenceChainsRequiringRedefinition() {
        this.checkModel(model -> {
            var context = model.get(Type.class, "Example::part1");
            var chains = this.service.getAllFeatureChains(context, this.outsideStandardLibraries,
                    new RequiresRedefinitionFeatureChainPredicate(), 100);

            // Selection removes local results without pruning their inherited descendants.
            assertThat(chains).extracting(chain -> chain.stream().map(Feature::getName).collect(Collectors.joining("."))).containsExactly(
                    "partPD3.variantPart.commonAttr",
                    "partPD3.attr3_1",
                    "attr1_1",
                    "attr1_2",
                    "partPD2",
                    "partPD2.attr2_1");
        });
    }

    /**
     * From part2, fully local chains are excluded while inherited segments remain selectable, regardless of value configurability.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN part2, WHEN selecting chains requiring redefinition, THEN fully local chains are excluded")
    public void secondOccurrenceChainsRequiringRedefinition() {
        this.checkModel(model -> {
            var context = model.get(Type.class, "Example::part2");
            var chains = this.service.getAllFeatureChains(context, this.outsideStandardLibraries,
                    new RequiresRedefinitionFeatureChainPredicate(), 100);

            // Selection removes local results without pruning their inherited descendants.
            assertThat(chains).extracting(chain -> chain.stream().map(Feature::getName).collect(Collectors.joining("."))).containsExactly(
                    "attr1_2");
        });
    }

    /**
     * Standard library features are pruned based on their resource, even when their memberships are explicitly declared.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN an explicitly declared library feature, WHEN filtering by resource origin, THEN its branch is excluded")
    public void optionallyExcludeStandardLibraryFeatures() {
        this.checkModel(model -> {
            var context = model.get(Type.class, "Example::part2");
            var inherited = context.getFeature().stream()
                    .filter(feature -> !this.outsideStandardLibraries.test(context, feature))
                    .findFirst().orElseThrow();
            assertThat(inherited.eResource()).isNotSameAs(context.eResource());
            assertThat(inherited.getOwningMembership().isIsImplied()).isFalse();

            // Limit traversal to this root candidate so the test does not enumerate the whole standard library graph.
            var included = this.service.getAllFeatureChains(context, (parent, feature) -> parent == context && feature == inherited, null, 100);
            assertThat(included).containsExactly(List.of(inherited));
            var excluded = this.service.getAllFeatureChains(context,
                    (parent, feature) -> parent == context && feature == inherited && this.outsideStandardLibraries.test(parent, feature), null, 100);
            assertThat(excluded).isEmpty();
        });
    }

    /**
     * Unfiltered traversal from PD2::attr2_1, typed by ScalarValues::String, exposes the library's self and that
     * features and terminates their cycles according to the service contract. Expected identities distinguish
     * DataValue::self, which redefines Anything::self, from the original encountered through that : Anything.
     * See /models/accessible-feature-chains/ccp-example.sysml for the source of the SQL fixture.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a String attribute and standard libraries, WHEN collecting chains without predicates, THEN inherited library features are returned and cycles terminate")
    public void unfilteredAttributeLibraryChains() {
        this.checkModel(model -> {
            var context = model.get(Feature.class, "Example::PD2::attr2_1");
            var libraries = LibraryNamespaceProvider.getFrom(context);
            assertThat(libraries).isNotNull();
            var dataSelf = libraries.getNamespaceFromLibrary("Base::DataValue::self", Feature.class);
            var anythingSelf = libraries.getNamespaceFromLibrary("Base::Anything::self", Feature.class);
            var that = libraries.getNamespaceFromLibrary("Base::things::that", Feature.class);
            assertThat(dataSelf).isNotNull();
            assertThat(anythingSelf).isNotNull();
            assertThat(that).isNotNull();

            // TODO Extend these expectations when missing implicit subsettings are implemented: attr2_1 should
            // specialize Base::dataValues (SysML v2.1 Beta 2, checkAttributeUsageSpecialization), and
            // Base::things::that should subset Base::things (KerML v1.1 Beta 2, 7.3.4.1 and checkFeatureSpecialization).
            // Their absence currently removes that at the root and prevents recursive that.that chains.
            // Both predicates are null: these are the complete results for the current implementation.
            var chains = this.service.getAllFeatureChains(context, null, null, 100);
            assertThat(chains).containsExactly(
                    List.of(dataSelf),
                    List.of(dataSelf, dataSelf),
                    List.of(dataSelf, dataSelf, dataSelf),
                    List.of(dataSelf, dataSelf, that),
                    List.of(dataSelf, dataSelf, that, anythingSelf),
                    List.of(dataSelf, that),
                    List.of(dataSelf, that, anythingSelf),
                    List.of(dataSelf, that, anythingSelf, anythingSelf),
                    List.of(dataSelf, that, anythingSelf, that));
        });
    }

    /**
     * Limiting an unfiltered definition query preserves its first chains, including implicitly inherited library features.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN PD1 with standard libraries, WHEN collecting five unfiltered chains, THEN only the first five results are returned")
    public void unfilteredPartDefinitionLibraryChains() {
        this.checkModel(model -> {
            var context = model.get(Definition.class, "Example::PD1");
            var partPD2 = model.get(Feature.class, "Example::PD1::partPD2");
            var attr2 = model.get(Feature.class, "Example::PD2::attr2_1");
            var libraries = LibraryNamespaceProvider.getFrom(context);
            assertThat(libraries).isNotNull();
            var dataSelf = libraries.getNamespaceFromLibrary("Base::DataValue::self", Feature.class);
            assertThat(dataSelf).isNotNull();

            // Stop before exploring the remaining library graph; the KerML self feature is still included.
            var chains = this.service.getAllFeatureChains(context, null, null, 5);
            assertThat(chains).containsExactly(
                    List.of(model.get(Feature.class, "Example::PD1::attr1_1")),
                    List.of(model.get(Feature.class, "Example::PD1::attr1_2")),
                    List.of(partPD2),
                    List.of(partPD2, attr2),
                    List.of(partPD2, attr2, dataSelf));
        });
    }

    /**
     * Paging selected chains from a persisted model preserves the same order and feature identities as full collection.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a SQL-loaded configured occurrence, WHEN selecting a page of attribute chains, THEN it matches the corresponding complete result slice")
    public void paginateConfiguredOccurrence() {
        this.checkModel(model -> {
            var context = model.get(Type.class, "Example::part2");
            var all = this.service.getAllFeatureChains(context, this.outsideStandardLibraries, this::isAttributeOrRedefinition, 100);
            // Other tests fix the complete expected sequence; this checks pagination after both predicates.
            assertThat(all).hasSize(7);
            try (var chains = this.service.streamFeatureChains(context, this.outsideStandardLibraries, this::isAttributeOrRedefinition)) {
                assertThat(chains.skip(2).limit(3).toList()).containsExactlyElementsOf(all.subList(2, 5));
            }
        });
    }

    private void checkModel(Consumer<SysMLTestModel> checker) {
        var checkerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService,
                ModelConfigurationTestProjectData.EDITING_CONTEXT_ID, ModelConfigurationTestProjectData.PACKAGE_ID);
        var input = new EditingContextEventInput(UUID.randomUUID(), ModelConfigurationTestProjectData.EDITING_CONTEXT_ID);
        var flux = this.editingContextEventSubscriptionRunner.run(input).flux();
        StepVerifier.create(flux)
                .then(checkerService.checkEditingContext(editingContext -> {
                    var root = (Element) this.objectSearchService.getObject(editingContext, ModelConfigurationTestProjectData.PACKAGE_ID).orElseThrow();
                    var model = new SysMLTestModel(root.eResource());
                    assertThat(model.get(PartUsage.class, "Example::part1").specializesFromLibrary("Parts::Part")).isTrue();
                    checker.accept(model);
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(20));
    }

    /**
     * Selects attributes and their direct or transitive redefinitions. Under SysML v2.1 Beta 2, section 7.6.4
     * (Reference Usages), a declaration without a kind keyword is a reference usage: {@code :>> attr1_1 = 8;}
     * creates a {@link org.eclipse.syson.sysml.ReferenceUsage}, while {@code attribute :>> attr1_1 = 8;}
     * creates an {@link AttributeUsage}. Redefining an attribute does not copy its metaclass, so instanceof alone
     * would exclude the shorthand declaration. {@link Feature#allRedefinedFeatures()} includes the feature itself
     * and its redefined sources, allowing both forms to be selected. This test predicate does not define configurability.
     */
    private boolean isAttributeOrRedefinition(Type context, List<Feature> chain) {
        Feature feature = chain.getLast();
        return feature.allRedefinedFeatures().stream().anyMatch(AttributeUsage.class::isInstance);
    }
}
