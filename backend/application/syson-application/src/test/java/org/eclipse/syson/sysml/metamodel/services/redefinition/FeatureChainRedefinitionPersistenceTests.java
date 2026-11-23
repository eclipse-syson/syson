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

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.collaborative.dto.EditingContextEventInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.graphql.tests.EditingContextEventSubscriptionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.emfjson.resource.PackageNotFoundError;
import org.eclipse.sirius.web.application.editingcontext.services.api.IResourceLoader;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.Document;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.ModelConfigurationTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.data.SysMLTestModel;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.metamodel.util.ElementUtil;
import org.eclipse.syson.sysml.util.LibraryNamespaceProvider;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Checks that locally materialized feature chains survive application persistence and JSON reloading.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FeatureChainRedefinitionPersistenceTests extends AbstractIntegrationTests {

    private static final String CONTEXT = "Example::part1";

    private static final String SOURCE_CHILD = "Example::PD1::partPD2";

    private static final String SOURCE_ATTRIBUTE = "Example::PD2::attr2_1";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private EditingContextEventSubscriptionRunner editingContextEventSubscriptionRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private ISemanticDataSearchService semanticDataSearchService;

    @Autowired
    private IResourceLoader resourceLoader;

    private final MetamodelMutationElementService mutationService = new MetamodelMutationElementService();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    /**
     * Persists a newly materialized chain through the application and reloads the stored JSON in a fresh resource set.
     * Both local containment and redefinition targets must survive, including the attribute's inherited library type.
     */
    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a persisted model, WHEN materializing a nested feature chain and reloading the saved JSON, THEN containment, redefinition targets and inherited typing survive")
    public void persistAndReloadSelectedChain() {
        // GIVEN the relevant part of the persisted example:
        // part def PD2 { attribute attr2_1 : ScalarValues::String; }
        // part def PD1 { part partPD2 : PD2; }
        // part part1 : PD1;
        String contextId = ModelConfigurationTestProjectData.EDITING_CONTEXT_ID;
        EditingContextEventInput input = new EditingContextEventInput(UUID.randomUUID(), contextId);
        Flux<?> flux = this.editingContextEventSubscriptionRunner.run(input).flux();
        Runnable mutate = this.semanticRunnableFactory.createMutationRunnable(contextId, (editingContext, eventInput) -> {
            Element root = (Element) this.objectSearchService.getObject(editingContext, ModelConfigurationTestProjectData.PACKAGE_ID).orElseThrow();
            SysMLTestModel model = new SysMLTestModel(root.eResource());
            Type context = model.get(Type.class, CONTEXT);
            Feature child = model.get(Feature.class, SOURCE_CHILD);
            Feature attribute = model.get(Feature.class, SOURCE_ATTRIBUTE);
            // WHEN creating the local chain through the application's semantic mutation handler.
            FeatureChainRedefinitionResult result = this.mutationService.redefineFeatureChain(context, List.of(child, attribute));
            assertThat(result.isSuccess()).as("Unexpected failure: %s", result.messages()).isTrue();
            return new ExecuteEditingContextFunctionSuccessPayload(eventInput.id(), true);
        });
        Runnable checkPersisted = this.semanticRunnableFactory.createQueryRunnable(contextId, (editingContext, eventInput) -> {
            Element root = (Element) this.objectSearchService.getObject(editingContext, ModelConfigurationTestProjectData.PACKAGE_ID).orElseThrow();
            Set<Document> documents = this.semanticDataSearchService.findById(UUID.fromString(contextId)).orElseThrow().getDocuments();
            assertThat(documents).hasSize(1);
            Document document = documents.iterator().next();
            // THEN the stored document restores the local chain and its links in a fresh resource set.
            SysMLTestModel reloaded = this.reloadDocument(document, root.eResource());
            Type context = reloaded.get(Type.class, CONTEXT);
            Feature localChild = reloaded.get(Feature.class, "Example::part1::partPD2");
            Feature localAttribute = reloaded.get(Feature.class, "Example::part1::partPD2::attr2_1");
            assertThat(localChild.getOwningType()).isSameAs(context);
            assertThat(localAttribute.getOwningType()).isSameAs(localChild);
            assertThat(localChild.getOwnedRedefinition()).singleElement().satisfies(redefinition ->
                    assertThat(redefinition.getRedefinedFeature()).isSameAs(reloaded.get(Feature.class, SOURCE_CHILD)));
            assertThat(localAttribute.getOwnedRedefinition()).singleElement().satisfies(redefinition ->
                    assertThat(redefinition.getRedefinedFeature()).isSameAs(reloaded.get(Feature.class, SOURCE_ATTRIBUTE)));
            Type stringType = LibraryNamespaceProvider.getFrom(context).getNamespaceFromLibrary("ScalarValues::String", Type.class);
            assertThat(stringType).isNotNull();
            assertThat(localAttribute.getType()).contains(stringType);
            return new ExecuteEditingContextFunctionSuccessPayload(eventInput.id(), true);
        });
        StepVerifier.create(flux)
                .then(mutate)
                .then(checkPersisted)
                .thenCancel()
                .verify(Duration.ofSeconds(30));
    }

    /**
     * Loads stored JSON rather than copying live model objects. Library resources remain in the active editing context;
     * the new resource set resolves external references through its URI map and its own namespace provider.
     */
    private SysMLTestModel reloadDocument(Document document, Resource original) {
        ResourceSetImpl resourceSet = new ResourceSetImpl();
        resourceSet.setURIResourceMap(new HashMap<>());
        resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        LibraryNamespaceProvider provider = new LibraryNamespaceProvider(resourceSet);
        resourceSet.eAdapters().add(provider);
        original.getResourceSet().getResources().stream().filter(ElementUtil::isStandardLibraryResource).forEach(library -> {
            resourceSet.getURIResourceMap().put(library.getURI(), library);
            provider.addImmutableLibrariesNamespaces(library);
        });
        Resource resource = this.resourceLoader.toResource(resourceSet, document.getId().toString(), document.getName(), document.getContent(), false, document.isReadOnly()).orElseThrow();
        // shortcut: EMF JSON diagnoses external library resources as missing EPackages; verify their presence and resolved proxies until this diagnostic is corrected.
        assertThat(resource.getErrors()).allSatisfy(diagnostic -> {
            assertThat(diagnostic).isInstanceOf(PackageNotFoundError.class);
            Resource library = resourceSet.getResource(URI.createURI(((PackageNotFoundError) diagnostic).getUri()), false);
            assertThat(library).isNotNull();
            assertThat(ElementUtil.isStandardLibraryResource(library)).isTrue();
        });
        assertThat(EcoreUtil.UnresolvedProxyCrossReferencer.find(resource)).isEmpty();
        return new SysMLTestModel(resource);
    }
}
