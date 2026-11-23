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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.metamodel.services.data.SysMLTestModel;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.metamodel.util.ElementUtil;
import org.eclipse.syson.sysml.util.LibraryNamespaceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integrates feature-chain selection and mutation with the standard libraries loaded by the application.
 *
 * @author Arthur Daussy
 */
public class FeatureChainRedefinitionLibraryTests {

    private static ResourceSet resources;

    private final MetamodelQueryElementService queryService = new MetamodelQueryElementService();

    private final MetamodelMutationElementService mutationService = new MetamodelMutationElementService();

    /**
     * A library may supply the selected source, but only the writable model context receives a new local feature.
     * The selection is bounded to the root to avoid recursively expanding the library's self feature.
     */
    @Test
    @DisplayName("GIVEN an inherited library self feature, WHEN materializing its selected chain, THEN the new feature belongs to the model and the library source is unchanged")
    public void verifyLibrarySourceRemainsUnchanged() throws IOException {
        SysMLTestModel model = this.loadModel("nested-feature-redefinitions");
        Type context = model.get(Type.class, "NestedFeatureRedefinitions::left");
        LibraryNamespaceProvider provider = LibraryNamespaceProvider.getFrom(context);
        Feature source = provider.getNamespaceFromLibrary("Parts::Part::self", Feature.class);
        assertThat(source).isNotNull();
        List<Relationship> relationships = List.copyOf(source.getOwnedRelationship());
        Resource sourceResource = source.eResource();

        FeatureChainRedefinitionResult result = this.mutationService.redefineFeatureChain(context, this.select(context, source));

        this.assertSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertThat(local.eResource()).isSameAs(context.eResource()).isNotSameAs(sourceResource);
            assertThat(local.eClass()).isSameAs(source.eClass());
            assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> assertThat(redefinition.getRedefinedFeature()).isSameAs(source));
        });
        assertThat(source.eResource()).isSameAs(sourceResource);
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    @BeforeAll
    public static void loadLibraries() {
        resources = new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration()).getLibrariesResourceSet();
    }

    @AfterEach
    public void removeTestResources() {
        resources.getResources().removeIf(resource -> !ElementUtil.isStandardLibraryResource(resource));
    }

    private void assertSuccess(FeatureChainRedefinitionResult result) {
        assertThat(result.isSuccess()).as("Unexpected failure: %s", result.messages()).isTrue();
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
        assertThat(resource.getErrors()).extracting(Resource.Diagnostic::getMessage).isEmpty();
        return new SysMLTestModel(resource);
    }

    /**
     * Limits traversal to root features so selecting one parameter does not enumerate the library graph.
     */
    private List<Feature> select(Type context, Feature source) {
        List<List<Feature>> selections = this.queryService.streamFeatureChains(context, (owner, feature) -> owner == context,
                (owner, chain) -> chain.getLast() == source).toList();
        assertThat(selections).containsExactly(List.of(source));
        return selections.getFirst();
    }
}
