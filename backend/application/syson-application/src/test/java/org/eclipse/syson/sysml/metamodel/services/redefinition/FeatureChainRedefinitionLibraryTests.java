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
package org.eclipse.syson.sysml.metamodel.services.redefinition;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.PackageNotFoundError;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.metamodel.services.NonStandardLibraryFeaturePredicate;
import org.eclipse.syson.sysml.metamodel.services.data.SysMLTestModel;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.metamodel.util.ElementUtil;
import org.eclipse.syson.sysml.util.LibraryNamespaceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Integrates feature-chain selection and mutation with the standard libraries loaded by the application.
 *
 * @author Arthur Daussy
 */
public class FeatureChainRedefinitionLibraryTests {

    private static final String SIMPLE = "SimpleParameterRedefinitions::";

    private static final String COMPLEX = "ComplexParameterRedefinitions::";

    private static ResourceSet resources;

    private final MetamodelQueryElementService queryService = new MetamodelQueryElementService();

    private final MetamodelMutationElementService mutationService = new MetamodelMutationElementService();

    @BeforeAll
    public static void loadLibraries() {
        resources = new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration()).getLibrariesResourceSet();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.2: a result is independent of the ordered regular parameter sequence.
     * Library defaults must not add duplicate result positions or force a simple result to own all regular parameters.
     *
     * @param contextName
     *            the calculation usage name relative to the fixture package
     * @param sourceName
     *            the qualified source parameter name relative to the fixture package
     * @param count
     *            the expected number of locally owned parameters, including the result
     */
    @ParameterizedTest
    @CsvSource({"calculation, Compute::result, 1",
                "combinedCalculation, Compute::operand, 3",
                "combinedCalculation, Compute::result, 3"})
    @DisplayName("GIVEN a calculation and standard libraries, WHEN materializing a simple result or a complex selection, THEN its result membership is preserved independently of regular parameter positions")
    public void verifyCalculationResults(String contextName, String sourceName, int count) throws IOException {
        // GIVEN Standard libraries are loaded alongside a simple or combined calculation signature.
        SysMLTestModel model = this.loadModel("calculation-parameter-redefinitions");
        Type context = model.get(Type.class, COMPLEX + contextName);
        Feature source = model.get(Feature.class, COMPLEX + sourceName);

        // WHEN Select a result or ordinary operand through the query API, then materialize it.
        FeatureChainRedefinitionResult result = this.mutationService.redefineFeatureChain(context, this.select(context, source));

        // THEN Count the expected local parameters and keep the single result in its return membership.
        this.assertSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(count);
        assertThat(context.getOwnedFeature().stream()
                .filter(feature -> feature.getOwningFeatureMembership() instanceof ReturnParameterMembership))
                .singleElement()
                .satisfies(local -> assertThat(local.allRedefinedFeatures()).contains(model.get(Feature.class, COMPLEX + "Compute::result")));
    }

    /**
     * Service policy materializes every parameter for genuinely branching model inheritance, including a diamond.
     *
     * @param contextName
     *            the action usage name selecting direct branching, a diamond or wrapped branching
     * @param count
     *            the expected number of locally owned positions in the completed signature
     */
    @ParameterizedTest
    @CsvSource({"multiple, 4",
                "diamond, 3",
                "wrapped, 4"})
    @DisplayName("GIVEN branching behavior inheritance and standard libraries, WHEN selecting the first parameter, THEN all model parameter positions are materialized")
    public void verifyComplexParameters(String contextName, int count) throws IOException {
        // GIVEN The model contains independent branches, a diamond or a wrapper around branching inheritance.
        SysMLTestModel model = this.loadModel("complex-parameter-redefinitions");
        Type context = model.get(Type.class, COMPLEX + contextName);
        Feature source = model.get(Feature.class, COMPLEX + "Base::input");

        // WHEN Select the first Base input with the application libraries available.
        FeatureChainRedefinitionResult result = this.mutationService.redefineFeatureChain(context, this.select(context, source));

        // THEN Complete the model signature and return only the selected local first position.
        this.assertSuccess(result);
        assertThat(context.getOwnedFeature()).hasSize(count);
        assertThat(result.chain()).containsExactly(context.getOwnedFeature().getFirst());
    }

    /**
     * A library may supply the selected source, but only the writable model context receives a new local feature.
     * The selection is bounded to the root to avoid recursively expanding the library's self feature.
     */
    @Test
    @DisplayName("GIVEN an inherited library self feature, WHEN materializing its selected chain, THEN the new feature belongs to the model and the library source is unchanged")
    public void verifyLibrarySourceRemainsUnchanged() throws IOException {
        // GIVEN The selected self feature belongs to the standard library rather than the writable model.
        SysMLTestModel model = this.loadModel("nested-feature-redefinitions");
        Type context = model.get(Type.class, "NestedFeatureRedefinitions::left");
        LibraryNamespaceProvider provider = LibraryNamespaceProvider.getFrom(context);
        Feature source = provider.getNamespaceFromLibrary("Parts::Part::self", Feature.class);
        assertThat(source).isNotNull();
        List<Relationship> relationships = List.copyOf(source.getOwnedRelationship());
        Resource sourceResource = source.eResource();

        // WHEN Materialize the library feature in the model context.
        FeatureChainRedefinitionResult result = this.mutationService.redefineFeatureChain(context, this.select(context, source));

        // THEN Attach the new feature to the model resource and preserve the library source and its relationships.
        this.assertSuccess(result);
        assertThat(result.chain())
                .singleElement()
                .satisfies(local -> {
                    assertThat(local.eResource())
                            .isSameAs(context.eResource())
                            .isNotSameAs(sourceResource);
                    assertThat(local.eClass()).isSameAs(source.eClass());
                    assertThat(local.getOwnedRedefinition())
                            .singleElement()
                            .satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(source));
                });
        assertThat(source.eResource()).isSameAs(sourceResource);
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * KerML 1.1 Beta 2, 7.4.7.2-3: a linear behavior permits a local positional prefix with an inherited suffix.
     * Loading the application's libraries must not turn ordinary default generalizations into additional model parameter branches.
     *
     * @param parameterName
     *            the inherited parameter name declared by Process
     * @param count
     *            the expected length of the local prefix through the selected position
     */
    @ParameterizedTest
    @CsvSource({"input, 1", "output, 2", "currentState, 3"})
    @DisplayName("GIVEN a linearly typed action and standard libraries, WHEN selecting one parameter through the query service, THEN only its necessary prefix is materialized")
    public void verifyLinearParameters(String parameterName, int count) throws IOException {
        // GIVEN The action has one model signature plus implicit Actions library generalizations.
        SysMLTestModel model = this.loadModel("simple-parameter-redefinitions");
        Type context = model.get(Type.class, SIMPLE + "processUse");
        Feature source = model.get(Feature.class, SIMPLE + "Process::" + parameterName);
        assertThat(context.specializesFromLibrary("Actions::actions")).isTrue();
        List<Feature> selected = this.select(context, source);

        // WHEN Select one inherited parameter through the query API.
        FeatureChainRedefinitionResult result = this.mutationService.redefineFeatureChain(context, selected);

        // THEN Create only the prefix through that position; library defaults do not force the suffix.
        this.assertSuccess(result);
        assertThat(context.getOwnedFeature()).as("Direct generals: %s", context.getOwnedSpecialization().stream()
                .map(specialization -> specialization.getGeneral().getQualifiedName())
                .toList()).hasSize(count);
        assertThat(result.chain()).containsExactly(context.getOwnedFeature().getLast());
    }

    /**
     * Collect both selections before mutation, then reuse the materialized action and input when extending a nested chain.
     * Explicit source features and their shared definitions must remain unchanged despite implicit library generalizations.
     */
    @Test
    @DisplayName("GIVEN nested selections collected before mutation and standard libraries, WHEN materializing them successively, THEN their local prefix is shared and the source action remains unchanged")
    public void verifyNestedSelectionsWithSharedPrefix() throws IOException {
        // GIVEN Collect work.input.value before any mutation so its source objects stay those of the original model.
        SysMLTestModel model = this.loadModel("complex-parameter-redefinitions");
        Type context = model.get(Type.class, COMPLEX + "root");
        Feature work = model.get(Feature.class, COMPLEX + "Container::work");
        Feature input = model.get(Feature.class, COMPLEX + "Base::input");
        Feature value = model.get(Feature.class, COMPLEX + "Payload::value");
        List<Relationship> sourceRelationships = List.copyOf(work.getOwnedRelationship());
        List<List<Feature>> selected = this.queryService.streamFeatureChains(context, new NonStandardLibraryFeaturePredicate(),
                (owner, chain) -> chain.size() == 3 && chain.getFirst() == work && chain.get(1) == input && chain.getLast() == value)
                        .toList();
        assertThat(selected).containsExactly(List.of(work, input, value));
        List<Feature> prefix = List.of(work, input);

        // WHEN Materialize work.input first, then extend the original selection to value.
        FeatureChainRedefinitionResult first = this.mutationService.redefineFeatureChain(context, prefix);
        FeatureChainRedefinitionResult second = this.mutationService.redefineFeatureChain(context, selected.getFirst());

        // THEN Reuse the same local action and input, while preserving the source action.
        this.assertSuccess(first);
        this.assertSuccess(second);
        assertThat(second.chain().subList(0, 2)).containsExactlyElementsOf(first.chain());
        assertThat(second.chain().getFirst().getOwnedFeature()).hasSize(4);
        assertThat(second.chain().getLast().getOwningType()).isSameAs(first.chain().getLast());
        assertThat(work.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * Round-trip the application's JSON format for a complex calculation, preserving parameter order, identities,
     * all merged redefinition targets and the specialized result membership. This complements database persistence coverage.
     */
    @Test
    @DisplayName("GIVEN a materialized complex calculation with standard libraries, WHEN saving and reloading JSON, THEN parameter order, merged targets and result membership survive")
    public void verifyParameterJsonRoundTrip() throws IOException {
        // GIVEN Materialize the combined calculation and record parameter IDs and all redefinition targets.
        SysMLTestModel model = this.loadModel("calculation-parameter-redefinitions");
        String contextName = COMPLEX + "combinedCalculation";
        Type context = model.get(Type.class, contextName);
        Feature source = model.get(Feature.class, COMPLEX + "Compute::operand");
        // WHEN Serialize to JSON and load a replacement resource at the same URI.
        FeatureChainRedefinitionResult result = this.mutationService.redefineFeatureChain(context, this.select(context, source));
        // THEN Preserve position order, identities, merged targets and the specialized result membership after reload.
        this.assertSuccess(result);
        // Record the materialized signature before replacing its resource; compare stable IDs after reload.
        List<String> ids = context.getOwnedFeature().stream()
                .map(Element::getElementId)
                .toList();
        List<List<String>> targets = context.getOwnedFeature().stream()
                .map(feature -> feature.getOwnedRedefinition().stream()
                        .map(redefinition -> redefinition.getRedefinedFeature().getQualifiedName())
                        .toList())
                .toList();
        Resource json = new JSONResourceFactory().createResource(URI.createURI("sirius:///parameter-round-trip"));
        context.eResource().getResourceSet().getResources().add(json);
        // Move the model roots into the application's JSON resource so cross-resource links use their persisted URIs.
        json.getContents().addAll(context.eResource().getContents());
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();
        json.save(serialized, Map.of());

        // Remove the original resource so reference resolution must use the newly loaded objects.
        resources.getResources().remove(json);
        Resource loaded = new JSONResourceFactory().createResource(json.getURI());
        resources.getResources().add(loaded);
        loaded.load(new ByteArrayInputStream(serialized.toByteArray()), Map.of());

        // shortcut: EMF JSON diagnoses external library resources as missing EPackages; verify their presence and resolved proxies until this diagnostic is corrected.
        assertThat(loaded.getErrors()).allSatisfy(diagnostic -> {
            assertThat(diagnostic).isInstanceOf(PackageNotFoundError.class);
            Resource library = resources.getResource(URI.createURI(((PackageNotFoundError) diagnostic).getUri()), false);
            assertThat(library).isNotNull();
            assertThat(ElementUtil.isStandardLibraryResource(library)).isTrue();
        });
        SysMLTestModel reloaded = new SysMLTestModel(loaded);
        Type restored = reloaded.get(Type.class, contextName);
        assertThat(restored.getOwnedFeature())
                .extracting(Element::getElementId)
                .containsExactlyElementsOf(ids);
        assertThat(restored.getOwnedFeature().stream()
                .map(feature -> feature.getOwnedRedefinition().stream()
                        .map(redefinition -> redefinition.getRedefinedFeature().getQualifiedName())
                        .toList())
                .toList())
                .containsExactlyElementsOf(targets);
        assertThat(restored.getOwnedFeature().stream()
                .filter(feature -> feature.getOwningFeatureMembership() instanceof ReturnParameterMembership))
                .hasSize(1);
        // Repeat on reloaded objects to check that the persisted signature still permits identity-based reuse.
        FeatureChainRedefinitionResult repeated = this.mutationService.redefineFeatureChain(restored,
                List.of(reloaded.get(Feature.class, COMPLEX + "Compute::operand")));
        this.assertSuccess(repeated);
        assertThat(restored.getOwnedFeature())
                .extracting(Element::getElementId)
                .containsExactlyElementsOf(ids);
    }

    @AfterEach
    public void removeTestResources() {
        resources.getResources().removeIf(resource -> !ElementUtil.isStandardLibraryResource(resource));
    }

    private void assertSuccess(FeatureChainRedefinitionResult result) {
        assertThat(result.isSuccess())
                .as("Unexpected failure: %s", result.messages())
                .isTrue();
        assertThat(result.messages()).isEmpty();
    }

    /**
     * Reuses pilot-generated fixtures with the same standard libraries and namespace provider as the application.
     */
    private SysMLTestModel loadModel(String fixture) throws IOException {
        Resource resource = new XMIResourceImpl(URI.createURI("test:/" + fixture + ".xmi"));
        resources.getResources().add(resource);
        try (InputStream input = this.getClass().getResourceAsStream("/feature-chains/" + fixture + ".xmi")) {
            assertThat(input).isNotNull();
            resource.load(input, Map.of());
        }
        assertThat(resource.getErrors())
                .extracting(Resource.Diagnostic::getMessage)
                .isEmpty();
        return new SysMLTestModel(resource);
    }

    /**
     * Limits traversal to root features so selecting one parameter does not enumerate the library graph.
     */
    private List<Feature> select(Type context, Feature source) {
        List<List<Feature>> selections = this.queryService.streamFeatureChains(context, (owner, feature) -> owner == context,
                (owner, chain) -> chain.getLast() == source)
                        .toList();
        assertThat(selections).containsExactly(List.of(source));
        return selections.getFirst();
    }
}
