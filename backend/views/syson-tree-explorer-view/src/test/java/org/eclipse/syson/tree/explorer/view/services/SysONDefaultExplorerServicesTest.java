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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import org.eclipse.sirius.components.collaborative.api.IRepresentationImageProvider;
import org.eclipse.sirius.components.core.api.IContentService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IURLParser;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.application.object.services.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerServices;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerServices;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.syson.application.services.SysONResourceService;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFilterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

/**
 * Tests the {@link SysONExplorerFilterService} class.
 *
 * @author dvojtise
 */
public class SysONDefaultExplorerServicesTest {

    private final ISysONResourceService sysONResourceService = new SysONResourceService();
    private static EditingContext editingContext;
    private SysONDefaultExplorerServices sysONDefaultExplorerServices;

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
    }

    @Test
    @DisplayName("hasChildren method can handle non sysml content")
    public void hasChildrenCanHandleNonSysmlContent() {
        this.createMockServicesForHasChildren();

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

        assertThat(this.sysONDefaultExplorerServices.hasChildren(ePackage, editingContext, List.of(), List.of())).isTrue();
        assertThat(this.sysONDefaultExplorerServices.hasChildren(c1, editingContext, List.of(), List.of())).isTrue();
        assertThat(this.sysONDefaultExplorerServices.hasChildren(c1a1, editingContext, List.of(), List.of())).isFalse();
    }

    /**
     * Create instances of services required to test hasChildren. Use mock instances for non required services
     */
    private void createMockServicesForHasChildren() {

        IIdentityService identityService = new IIdentityService.NoOp();
        IContentService contentService = new IContentService.NoOp();
        IRepresentationMetadataSearchService representationMetadataSearchService = new IRepresentationMetadataSearchService() {

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
        };

        IObjectService objectService = new IObjectService.NoOp();
        IURLParser urlParser = new IURLParser.NoOp();
        List<IRepresentationImageProvider> representationImageProviders = new ArrayList<>();
        IReadOnlyObjectPredicate readOnlyObjectPredicate = new IReadOnlyObjectPredicate() {

            @Override
            public boolean test(Object arg0) {
                return false;
            }
        };
        IExplorerServices explorerServices = new ExplorerServices(objectService, urlParser, representationImageProviders, representationMetadataSearchService, readOnlyObjectPredicate);

        ISysONExplorerFilterService filterService = new SysONExplorerFilterService(this.sysONResourceService);

        this.sysONDefaultExplorerServices = new SysONDefaultExplorerServices(identityService, contentService, representationMetadataSearchService, explorerServices, filterService,
                this.sysONResourceService, new IObjectService.NoOp());
    }

}
