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

package org.eclipse.syson.tree.explorer.view.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreAdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.sirius.components.core.api.IContentService;
import org.eclipse.sirius.components.core.api.IDefaultObjectSearchService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerServices;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerServices;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.pagination.Window;
import org.eclipse.syson.application.services.SysONResourceService;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONDefaultExplorerService;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFilterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

/**
 * Tests the {@link SysONExplorerFilterService} class.
 * 
 * @author dvojtise
 * @author flatombe
 */
public class SysONDefaultExplorerServicesTest {

    /**
     * Mock implementation of {@link IRepresentationMetadataSearchService}.
     * 
     * @author dvojtise
     * @author flatombe
     */
    private static final class IRepresentationMetadataSearchServiceNoOp implements IRepresentationMetadataSearchService {
        @Override
        public Optional<AggregateReference<SemanticData, UUID>> findSemanticDataByRepresentationId(UUID representationId) {
            return Optional.empty();
        }

        @Override
        public Optional<RepresentationMetadata> findMetadataById(UUID id) {
            return Optional.empty();
        }

        @Override
        public List<RepresentationMetadata> findAllRepresentationMetadataBySemanticDataAndTargetObjectId(AggregateReference<SemanticData, UUID> semanticData, String targetObjectId) {
            return null;
        }

        @Override
        public List<RepresentationMetadata> findAllRepresentationMetadataBySemanticData(AggregateReference<SemanticData, UUID> semanticData) {
            return null;
        }

        @Override
        public Window<RepresentationMetadata> findAllRepresentationMetadataBySemanticData(AggregateReference<SemanticData, UUID> semanticData, KeysetScrollPosition position, int limit) {
            return null;
        }

        @Override
        public boolean existsByIdAndKind(UUID id, List<String> kinds) {
            return false;
        }

        @Override
        public boolean existsById(UUID id) {
            return false;
        }

        @Override
        public boolean existAnyRepresentationMetadataForSemanticDataAndTargetObjectId(AggregateReference<SemanticData, UUID> semanticData, String targetObjectId) {
            return false;
        }
    }

    private static EditingContext editingContext;

    private static ISysONResourceService resourceService;

    private static ISysONDefaultExplorerService defaultExplorerService;

    @BeforeAll
    static void createEditingContext() {
        ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory();
        composedAdapterFactory.addAdapterFactory(new EcoreAdapterFactory());
        composedAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

        EPackage.Registry ePackageRegistry = new EPackageRegistryImpl();
        ePackageRegistry.put(EcorePackage.eINSTANCE.getNsURI(), EcorePackage.eINSTANCE);

        AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(composedAdapterFactory, new BasicCommandStack());
        ResourceSet resourceSet = editingDomain.getResourceSet();
        resourceSet.setPackageRegistry(ePackageRegistry);
        resourceSet.eAdapters().add(new ECrossReferenceAdapter());
        editingContext = new EditingContext(UUID.randomUUID().toString(), editingDomain, Map.of(), List.of());

        resourceService = new SysONResourceService();
        defaultExplorerService = createMockDefaultExplorerService(resourceService);
    }

    /**
     * Creates an {@link ISysONDefaultExplorerService} instance with mocks that is good enough for the unit tests of
     * this class.
     */
    private static ISysONDefaultExplorerService createMockDefaultExplorerService(final ISysONResourceService sysONResourceService) {
        IIdentityService identityService = new IIdentityService.NoOp();
        IContentService contentService = new IContentService.NoOp();
        IRepresentationMetadataSearchService representationMetadataSearchService = new IRepresentationMetadataSearchServiceNoOp();

        IObjectService objectService = new IObjectService.NoOp();
        ILabelService labelService = new ILabelService.NoOp();
        IReadOnlyObjectPredicate readOnlyObjectPredicate = new IReadOnlyObjectPredicate() {

            @Override
            public boolean test(Object arg0) {
                return false;
            }
        };
        IDefaultObjectSearchService defaultObjectSearchService = new IDefaultObjectSearchService.NoOp();

        IExplorerServices explorerServices = new ExplorerServices(objectService, labelService, List.of(), representationMetadataSearchService, readOnlyObjectPredicate, defaultObjectSearchService);

        ISysONExplorerFilterService filterService = new SysONExplorerFilterService(sysONResourceService);

        return new SysONDefaultExplorerServices(identityService, contentService, representationMetadataSearchService, explorerServices, labelService, filterService, sysONResourceService);
    }

    /**
     * Unit tests for
     * {@link ISysONDefaultExplorerService#hasChildren(Object, org.eclipse.sirius.components.core.api.IEditingContext, List, List, List)}.
     * 
     * @author flatombe
     * @author dvojtise
     */
    @Nested
    @DisplayName("boolean hasChildren(Object, IEditingContext, List<RepresentationMetadata>, List<String>, List<String>)")
    public class TestHasChildren {
        @Test
        @DisplayName("Supports non-sysML model elements")
        public void hasChildrenCanHandleNonSysmlContent() {
            EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
            ePackage.setName("somePackage");
            EClass c1 = EcoreFactory.eINSTANCE.createEClass();
            ePackage.getEClassifiers().add(c1);
            EClass c2 = EcoreFactory.eINSTANCE.createEClass();
            ePackage.getEClassifiers().add(c2);
            EAttribute c1a1 = EcoreFactory.eINSTANCE.createEAttribute();
            c1.getEStructuralFeatures().add(c1a1);
            EAttribute c1a2 = EcoreFactory.eINSTANCE.createEAttribute();
            c1.getEStructuralFeatures().add(c1a2);

            assertThat(defaultExplorerService.hasChildren(ePackage, editingContext, List.of(), List.of(), List.of())).isTrue();
            assertThat(defaultExplorerService.hasChildren(c1, editingContext, List.of(), List.of(), List.of())).isTrue();
            assertThat(defaultExplorerService.hasChildren(c1a1, editingContext, List.of(), List.of(), List.of())).isFalse();
        }
    }

    /**
     * Unit tests for {@link ISysONDefaultExplorerService#canCreateNewObjectsFromText(Object)}.
     * 
     * @author flatombe
     */
    @Nested
    @DisplayName("boolean canCreateNewObjectsFromText(Object)")
    public class TestCanCreateNewObjectsFromText {
        @Test
        @DisplayName("On a null object, cannot create new objects from text")
        public void testNull() {
            assertThat(defaultExplorerService.canCreateNewObjectsFromText(null)).isFalse();
        }

        @Test
        @DisplayName("On a non-SysML model element, cannot create new objects from text")
        public void testNonSysMLElements() {
            final EPackage ePackage = EcorePackage.eINSTANCE;
            assertThat(getAllConcreteEClasses(ePackage))
                    .noneMatch(eClass -> defaultExplorerService.canCreateNewObjectsFromText(
                            ePackage.getEFactoryInstance().create(eClass)));
        }

        @Test
        @DisplayName("On a SysML model element, can always create new objects from text")
        public void testSysMLElements() {
            final EPackage ePackage = SysmlPackage.eINSTANCE;
            assertThat(getAllConcreteEClasses(ePackage))
                    .allMatch(eClass -> defaultExplorerService.canCreateNewObjectsFromText(
                            ePackage.getEFactoryInstance().create(eClass)));
        }

        private static List<EClass> getAllConcreteEClasses(final EPackage ePackage) {
            return ePackage.getEClassifiers().stream()
                    .filter(EClass.class::isInstance)
                    .map(EClass.class::cast)
                    .filter(
                            Predicate.not(EClass::isInterface)
                                    .and(Predicate.not(EClass::isAbstract)))
                    .toList();
        }
    }

}
