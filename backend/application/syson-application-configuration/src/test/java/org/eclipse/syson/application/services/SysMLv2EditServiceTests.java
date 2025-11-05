/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.application.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.sirius.components.collaborative.api.IRepresentationMetadataPersistenceService;
import org.eclipse.sirius.components.collaborative.api.IRepresentationPersistenceService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.components.core.api.IDefaultEditService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.Namespace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

/**
 * Unit tests for {@link SysMLv2EditService}.
 *
 * @author flatombe
 */
public class SysMLv2EditServiceTests {

    private static final String SYSML_RESOURCE_NAME = "Mock SysML Resource.sysml";

    private static final String NON_SYSML_RESOURCE_NAME = "Mock non-SysML Resource.notsysml";

    private UUID documentIdForSysMLResource;

    private UUID documentIdForNonSysMLResource;

    private DynamicallyConfigurableDefaultEditService defaultEditService;

    private DynamicallyConfigurableSysMLv2EditService sysMLv2EditService;

    private IEditingContext editingContext;

    /**
     * A mock implementation of {@link IDefaultEditService} which allows us to dynamically change some of its behaviors.
     *
     * We need it to construct our unit test assertions as assertions on which methods from {@link IDefaultEditService}
     * get called, and with which arguments.
     */
    private static final class DynamicallyConfigurableDefaultEditService extends IDefaultEditService.NoOp {

        /**
         * {@link FunctionalInterface} for
         * {@link IDefaultEditService#createRootObject(IEditingContext, UUID, String, String)}.
         */
        @FunctionalInterface
        private interface CreateRootObjectBehavior {
            Optional<Object> execute(IEditingContext editingContext, UUID documentId, String domainId, String rootObjectCreationDescriptionId);
        }

        private CreateRootObjectBehavior createRootObjectBehavior;

        public void setCreateRootObjectBehavior(final CreateRootObjectBehavior newCreateRootObjectBehavior) {
            this.createRootObjectBehavior = newCreateRootObjectBehavior;
        }

        @Override
        public Optional<Object> createRootObject(IEditingContext editingContext, UUID documentId, String domainId, String rootObjectCreationDescriptionId) {
            if (this.createRootObjectBehavior != null) {
                return this.createRootObjectBehavior.execute(editingContext, documentId, domainId, rootObjectCreationDescriptionId);
            } else {
                return super.createRootObject(editingContext, documentId, domainId, rootObjectCreationDescriptionId);
            }
        }
    }

    /**
     * An implementation of {@link SysMLv2EditService} which allows us to dynamically change some of its behaviors.
     *
     * We need it to construct our unit test assertions as assertions on which methods from {@link SysMLv2EditService}
     * get called, and with which arguments.
     */
    private static final class DynamicallyConfigurableSysMLv2EditService extends SysMLv2EditService {

        /**
         * {@link FunctionalInterface} for {@link SysMLv2EditService#createChild(IEditingContext, Object, String)}.
         */
        @FunctionalInterface
        private interface CreateChildBehavior {
            Optional<Object> execute(IEditingContext editingContext, Object object, String childCreationDescriptionId);
        }

        private CreateChildBehavior createChildBehavior;

        DynamicallyConfigurableSysMLv2EditService(IDefaultEditService defaultEditService, ILabelService labelService, IObjectSearchService objectSearchService,
                ISysONResourceService sysONResourceService, SysMLv2EditServiceExtraServices representationServices) {
            super(defaultEditService, labelService, objectSearchService,
                    sysONResourceService, representationServices);
        }

        public void setCreateChildBehavior(final CreateChildBehavior newCreateChildBehavior) {
            this.createChildBehavior = newCreateChildBehavior;
        }

        @Override
        public Optional<Object> createChild(IEditingContext editingContext, Object object, String childCreationDescriptionId) {
            if (this.createChildBehavior != null) {
                return this.createChildBehavior.execute(editingContext, object, childCreationDescriptionId);
            } else {
                return super.createChild(editingContext, object, childCreationDescriptionId);
            }
        }

    }

    private static Resource createSysMLResource(final UUID documentId) {
        final Resource resource = new ResourceImpl();
        resource.setURI(new JSONResourceFactory().createResourceURI(documentId.toString()));
        resource.eAdapters().add(new ResourceMetadataAdapter(SYSML_RESOURCE_NAME, false));
        return resource;
    }

    private static Resource createNonSysMLResource(final UUID documentId) {
        final Resource resource = new ResourceImpl();
        resource.setURI(new JSONResourceFactory().createResourceURI(documentId.toString()));
        resource.eAdapters().add(new ResourceMetadataAdapter(NON_SYSML_RESOURCE_NAME, false));
        return resource;
    }

    private static IEditingContext createEditingContext(Resource... resources) {
        ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory();
        composedAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

        EPackage.Registry ePackageRegistry = new EPackageRegistryImpl();

        AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(composedAdapterFactory, new BasicCommandStack());
        ResourceSet resourceSet = editingDomain.getResourceSet();
        resourceSet.setPackageRegistry(ePackageRegistry);
        resourceSet.eAdapters().add(new ECrossReferenceAdapter());
        resourceSet.getResources().addAll(Stream.of(resources).toList());

        return new EditingContext(UUID.nameUUIDFromBytes("Mock Editing Context".getBytes()).toString(), editingDomain, Map.of(), List.of());
    }

    @BeforeEach
    public void setUp() {
        this.documentIdForSysMLResource = UUID.nameUUIDFromBytes(SYSML_RESOURCE_NAME.getBytes());
        this.documentIdForNonSysMLResource = UUID.nameUUIDFromBytes(NON_SYSML_RESOURCE_NAME.getBytes());
        this.defaultEditService = new DynamicallyConfigurableDefaultEditService();
        this.sysMLv2EditService = new DynamicallyConfigurableSysMLv2EditService(
                this.defaultEditService,
                new ILabelService.NoOp(),
                new IObjectSearchService.NoOp(),
                new SysONResourceService(new LibrarySearchServiceNoOp()),
                new SysMLv2EditServiceExtraServices(
                        new IDiagramCreationService.NoOp(),
                        new IRepresentationDescriptionSearchService.NoOp(),
                        new IRepresentationMetadataPersistenceService.NoOp(),
                        new IRepresentationPersistenceService.NoOp()));
        this.editingContext = createEditingContext(createSysMLResource(this.documentIdForSysMLResource), createNonSysMLResource(this.documentIdForNonSysMLResource));
    }

    /**
     * Copied from syson-tree-services
     *
     * @author flatombe
     */
    class LibrarySearchServiceNoOp implements ILibrarySearchService {

        @Override
        public Page<Library> findAll(Pageable pageable) {
            return new PageImpl<>(List.of());
        }

        @Override
        public boolean existsByNamespaceAndNameAndVersion(String namespace, String name, String version) {
            return false;
        }

        @Override
        public Optional<Library> findByNamespaceAndNameAndVersion(String namespace, String name, String version) {
            return Optional.empty();
        }

        @Override
        public Optional<Library> findBySemanticData(AggregateReference<SemanticData, UUID> semanticData) {
            return Optional.empty();
        }

        @Override
        public Page<Library> findAllByNamespaceAndName(String namespace, String name, Pageable pageable) {
            return new PageImpl<>(List.of());
        }

        @Override
        public List<Library> findAllById(Iterable<UUID> ids) {
            return List.of();
        }

        @Override
        public Optional<Library> findById(UUID id) {
            return Optional.empty();
        }
    }

    @Test
    public void testCreateRootSysMLPackageInSysMLResource() {
        AtomicReference<Boolean> methodCreateChildWasCalled = new AtomicReference<>(false);
        this.sysMLv2EditService.setCreateChildBehavior((__, object, childCreationDescriptionId) -> {
            methodCreateChildWasCalled.set(true);
            // We expect SysMLv2EditService to redirect the root SysML object creation to creating a child under a
            // SysML Namespace.
            Assertions.assertThat(object).isInstanceOf(Namespace.class);
            Assertions.assertThat(childCreationDescriptionId).startsWith("SysMLv2EditService-");
            return Optional.empty();
        });

        this.sysMLv2EditService.createRootObject(this.editingContext, this.documentIdForSysMLResource,
                "http://www.eclipse.org/syson/sysml", "SysMLv2EditService-Package");
        Assertions.assertThat(methodCreateChildWasCalled.get()).isTrue();
    }

    @Test
    public void testCreateRootSysMLPackageInNonSysMLResource() {
        AtomicReference<Boolean> methodCreateChildWasCalled = new AtomicReference<>(false);
        this.sysMLv2EditService.setCreateChildBehavior((__, object, childCreationDescriptionId) -> {
            methodCreateChildWasCalled.set(true);
            return Optional.empty();
        });
        AtomicReference<Boolean> methodCreateRootObjectWasCalled = new AtomicReference<>(false);
        this.defaultEditService.setCreateRootObjectBehavior((__, ___, ____, rootObjectCreationDescriptionId) -> {
            methodCreateRootObjectWasCalled.set(true);
            // We expect SysMLv2EditService to redirect the root non-SysML object creation to a non-SysML
            // IEditServiceDelegate implementation.
            Assertions.assertThat(rootObjectCreationDescriptionId).doesNotStartWith("SysMLv2EditService-");
            return Optional.empty();
        });

        this.sysMLv2EditService.createRootObject(this.editingContext, this.documentIdForNonSysMLResource,
                "http://www.eclipse.org/syson/sysml", "SysMLv2EditService-Package");

        Assertions.assertThat(methodCreateChildWasCalled.get()).isFalse();
        Assertions.assertThat(methodCreateRootObjectWasCalled.get()).isTrue();
    }

    @Test
    public void testCreateRootNonSysMLElementInNonSysMLResource() {
        AtomicReference<Boolean> methodCreateRootObjectWasCalled = new AtomicReference<>(false);
        this.defaultEditService.setCreateRootObjectBehavior((__, ___, ____, rootObjectCreationDescriptionId) -> {
            methodCreateRootObjectWasCalled.set(true);
            // We expect SysMLv2EditService to redirect the root non-SysML object creation to a non-SysML
            // IEditServiceDelegate implementation.
            Assertions.assertThat(rootObjectCreationDescriptionId).doesNotStartWith("SysMLv2EditService-");
            return Optional.empty();
        });
        this.sysMLv2EditService.createRootObject(this.editingContext, this.documentIdForNonSysMLResource,
                "http://www.eclipse.org/sirius-web/domain", "other");
        Assertions.assertThat(methodCreateRootObjectWasCalled.get()).isTrue();
    }
}
