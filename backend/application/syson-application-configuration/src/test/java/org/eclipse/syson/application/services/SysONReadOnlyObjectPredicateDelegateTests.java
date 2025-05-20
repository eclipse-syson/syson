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

import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicateDelegate;
import org.eclipse.sirius.components.core.services.ComposedReadOnlyObjectPredicate;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SysONReadOnlyObjectPredicateDelegate}. It is used to ensure:
 * <ul>
 * <li>It can handle all the various candidate objects it may be called upon as part of the SysON runtime</li>
 * <li>It returns {@code true} for resources and elements that are intended to be un-editable by end users (namely the
 * default, imported and referenced libraries)</li>
 * </ul>
 *
 * @see SysONReadOnlyObjectPredicateDelegate
 * @see ComposedReadOnlyObjectPredicate
 * @author flatombe
 */
public class SysONReadOnlyObjectPredicateDelegateTests {

    private final IReadOnlyObjectPredicateDelegate readOnlyObjectPredicateDelegate = new SysONReadOnlyObjectPredicateDelegate(new SysONResourceService());

    @SafeVarargs
    private static Resource createResource(final URI uri, final Consumer<Resource>... postTreatments) {
        final Resource resource = new ResourceImpl(uri);
        Stream.of(postTreatments).forEach(postTreatment -> postTreatment.accept(resource));
        return resource;
    }

    private void assertResourceAndAllContentsCanBeHandled(final Resource resource, final boolean expectedCanBeHandled) {
        Assertions.assertThat(EMFUtils.eAllContentStreamWithSelf(resource).map(objectUnderTest -> this.readOnlyObjectPredicateDelegate.canHandle(objectUnderTest)))
                .allMatch(actualCanBeHandled -> actualCanBeHandled == expectedCanBeHandled);
    }

    private void assertResourceAndAllContentsIsReadOnly(final Resource resource, final boolean expectedIsReadOnly) {
        this.assertResourceAndAllContentsCanBeHandled(resource, true);
        Assertions.assertThat(EMFUtils.eAllContentStreamWithSelf(resource).map(objectUnderTest -> this.readOnlyObjectPredicateDelegate.test(objectUnderTest)))
                .allMatch(actualIsReadOnly -> actualIsReadOnly == expectedIsReadOnly);
    }

    /**
     * Unit tests for {@link SysONReadOnlyObjectPredicateDelegate} on edge cases.
     *
     * @author flatombe
     */
    @Nested
    @DisplayName("Edge Cases")
    public class EdgeCases {
        private final Resource nullResource;

        private final Resource resourceWithNoUri;

        public EdgeCases() {
            this.nullResource = null;
            this.resourceWithNoUri = createResource(null);
        }

        @Test
        @DisplayName("Null input is not supported")
        public void testNullResourceIsNotHandled() {
            SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsCanBeHandled(this.nullResource, false);
        }

        @Test
        @DisplayName("Resource with no URI is not read-only")
        public void testResourceWithNoUriIsNotReadOnly() {
            SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.resourceWithNoUri, false);
        }
    }

    /**
     * Unit tests for {@link SysONReadOnlyObjectPredicateDelegate} on {@link Resource resources} from the KerML / SysML
     * standard libraries.
     *
     * @author flatombe
     */
    @Nested
    @DisplayName("Resources from the SysON default libraries")
    public class DefaultLibrariesResources {

        private final Resource resourceWithKermlUriScheme;

        private final Resource resourceWithSysmlUriScheme;

        private final SysONDefaultLibrariesConfiguration sysonDefaultLibrariesConfiguration = new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration());

        public DefaultLibrariesResources() {
            this.resourceWithKermlUriScheme = createResource(URI.createURI("%s:///%s".formatted(ElementUtil.KERML_LIBRARY_SCHEME, UUID.randomUUID())));
            this.resourceWithSysmlUriScheme = createResource(URI.createURI("%s:///%s".formatted(ElementUtil.SYSML_LIBRARY_SCHEME, UUID.randomUUID())));
        }

        @Test
        @DisplayName("Any resource with the KerML URI scheme is read-only")
        public void testResourceWithKermlUriSchemeIsReadOnly() {
            SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.resourceWithKermlUriScheme, true);
        }

        @Test
        @DisplayName("Any resource with the SysML URI scheme is read-only")
        public void testResourceWithSysmlUriSchemeIsReadOnly() {
            SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.resourceWithSysmlUriScheme, true);
        }

        @Test
        @DisplayName("All resources of all SysON default libraries are read-only (both the resource and all of its contents)")
        public void testDefaultLibraries() {
            this.sysonDefaultLibrariesConfiguration.getLibrariesResourceSet().getResources()
                    .forEach(resource -> SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(resource, true));
        }
    }

    /**
     * Unit tests for {@link SysONReadOnlyObjectPredicateDelegate} on EMF Resources, i.e. {@link Resource resources}
     * whose {@link URI#scheme() URI scheme} is {@link IEMFEditingContext#RESOURCE_SCHEME}.
     *
     * @author flatombe
     */
    @Nested
    @DisplayName("EMF Resources")
    public class EmfResources {

        private final Resource resourceWithEmfUriSchemeButNoContents;

        public EmfResources() {
            this.resourceWithEmfUriSchemeButNoContents = createResource(null);
        }

        private static URI createEmfUri() {
            return URI.createURI("%s:///%s".formatted(IEMFEditingContext.RESOURCE_SCHEME, UUID.randomUUID()));
        }

        @Test
        @DisplayName("Resource with no contents is not read-only")
        public void testResourceWithEmfUriSchemeButEmpty() {
            SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.resourceWithEmfUriSchemeButNoContents, false);
        }

        /**
         * Unit tests for {@link SysONReadOnlyObjectPredicateDelegate} on EMF Resources containing SysML {@link Element
         * elements}.
         *
         * @author flatombe
         */
        @Nested
        @DisplayName("SysML Resources (i.e. EMF Resources with a root SysML Namespace)")
        public class SysMLResources {

            private final Resource resourceWithSysmlPackage;

            private final Resource resourceWithSysmlLibraryPackage;

            private final Resource resourceWithSysmlMixedPackages;

            public SysMLResources() {
                this.resourceWithSysmlPackage = createResource(createEmfUri(), resource -> {
                    resource.getContents().add(createSysmlProjet());

                    // Arbitrary EAnnotation to ensure EAnnotations on SysML elements are also supported.
                    EMFUtils.eAllContentStreamWithSelf(resource).filter(EModelElement.class::isInstance).map(EModelElement.class::cast).forEach(eModelElement -> {
                        final EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
                        eAnnotation.setSource("source");
                        eModelElement.getEAnnotations().add(eAnnotation);
                    });
                });

                this.resourceWithSysmlLibraryPackage = createResource(createEmfUri(), resource -> {
                    resource.getContents().add(createSysmlLibrary());
                });

                this.resourceWithSysmlMixedPackages = createResource(createEmfUri(), resource -> {
                    resource.getContents().add(createSysmlMixedPackages());
                });
            }

            private static Namespace createSysmlProjet() {
                final Namespace rootNamespace = SysmlFactory.eINSTANCE.createNamespace();
                final OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                final Package sysmlPackage = SysmlFactory.eINSTANCE.createPackage();

                rootNamespace.getOwnedRelationship().add(owningMembership);
                owningMembership.getOwnedRelatedElement().add(sysmlPackage);

                return rootNamespace;
            }

            private static Namespace createSysmlLibrary() {
                final Namespace rootNamespace = SysmlFactory.eINSTANCE.createNamespace();
                final OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                final LibraryPackage libraryPackage = SysmlFactory.eINSTANCE.createLibraryPackage();

                rootNamespace.getOwnedRelationship().add(owningMembership);
                owningMembership.getOwnedRelatedElement().add(libraryPackage);

                return rootNamespace;
            }

            private static Namespace createSysmlMixedPackages() {
                final Namespace rootNamespace = SysmlFactory.eINSTANCE.createNamespace();

                final OwningMembership owningMembershipForPackage = SysmlFactory.eINSTANCE.createOwningMembership();
                final Package sysmlPackage = SysmlFactory.eINSTANCE.createPackage();
                final OwningMembership owningMembershipForLibraryPackage = SysmlFactory.eINSTANCE.createOwningMembership();
                final LibraryPackage libraryPackage = SysmlFactory.eINSTANCE.createLibraryPackage();

                rootNamespace.getOwnedRelationship().add(owningMembershipForPackage);
                rootNamespace.getOwnedRelationship().add(owningMembershipForLibraryPackage);

                owningMembershipForPackage.getOwnedRelatedElement().add(sysmlPackage);
                owningMembershipForLibraryPackage.getOwnedRelatedElement().add(libraryPackage);

                return rootNamespace;
            }

            @Test
            @DisplayName("Resource with SysML Package is not read-only (both the resource and all of its contents)")
            public void testResourceWithSysmlPackage() {
                SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.resourceWithSysmlPackage, false);
            }

            @Test
            @DisplayName("Resource with SysML LibraryPackage is not read-only (both the resource and all of its contents)")
            public void testResourceWithSysmlLibraryPackage() {
                SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.resourceWithSysmlLibraryPackage, false);
            }

            @Test
            @DisplayName("Resource with SysML Package and LibraryPackage is not read-only (both the resource and all of its contents)")
            public void testResourceWithSysmlMixedPackages() {
                SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.resourceWithSysmlMixedPackages, false);
            }

            /**
             * Unit tests for {@link SysONReadOnlyObjectPredicateDelegate} on {@link ElementUtil#isImported(Resource)
             * imported resources}.
             *
             * @author flatombe
             */
            @Nested
            @DisplayName("Imported Resources (carrying EAnnotation 'org.eclipse.syson.sysml.imported')")
            public class ImportedResources {

                private final Resource importedResourceWithSysmlPackage;

                private final Resource importedResourceWithSysmlLibraryPackage;

                private final Resource importedResourceWithSysmlMixedPackages;

                public ImportedResources() {
                    this.importedResourceWithSysmlPackage = createResource(createEmfUri(), resource -> {
                        resource.getContents().addAll(EcoreUtil.copyAll(SysMLResources.this.resourceWithSysmlPackage.getContents()));
                        ElementUtil.setIsImported(resource, true);
                    });

                    this.importedResourceWithSysmlLibraryPackage = createResource(createEmfUri(), resource -> {
                        resource.getContents().addAll(EcoreUtil.copyAll(SysMLResources.this.resourceWithSysmlLibraryPackage.getContents()));
                        ElementUtil.setIsImported(resource, true);
                    });

                    this.importedResourceWithSysmlMixedPackages = createResource(createEmfUri(), resource -> {
                        resource.getContents().addAll(EcoreUtil.copyAll(SysMLResources.this.resourceWithSysmlMixedPackages.getContents()));
                        ElementUtil.setIsImported(resource, true);
                    });
                }

                @Test
                @DisplayName("Imported resource containing Package is not read-only (both the resource and all of its contents)")
                public void testResourceWithSysmlPackageImported() {
                    SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.importedResourceWithSysmlPackage, false);
                }

                @Test
                @DisplayName("Imported resource containing LibraryPackage is read-only (both the resource and all of its contents)")
                public void testResourceWithSysmlLibraryPackageImported() {
                    SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.importedResourceWithSysmlLibraryPackage, true);
                }

                @Test
                @DisplayName("Imported resource containing Package and LibraryPackage is read-only (both the resource and all of its contents)")
                public void testResourceWithSysmlMixedPackagesImported() {
                    SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.importedResourceWithSysmlMixedPackages, true);
                }
            }

            /**
             * Unit tests for {@link SysONReadOnlyObjectPredicateDelegate} on
             * {@link ISysONResourceService#isFromReferencedLibrary(Resource) resources from referenced libraries}.
             *
             * @author flatombe
             */
            @Nested
            @DisplayName("Resources from referenced libraries (with a LibraryMetadataAdapter)")
            public class ReferencedResources {

                private final Resource referencedResourceWithSysmlPackage;

                private final Resource referencedResourceWithSysmlLibraryPackage;

                private final Resource referencedResourceWithSysmlMixedPackages;

                public ReferencedResources() {
                    this.referencedResourceWithSysmlPackage = createResource(createEmfUri(), resource -> {
                        resource.getContents().addAll(EcoreUtil.copyAll(SysMLResources.this.resourceWithSysmlPackage.getContents()));
                    }, resource -> resource.eAdapters().add(createLibraryMetadataAdapter()));

                    this.referencedResourceWithSysmlLibraryPackage = createResource(createEmfUri(), resource -> {
                        resource.getContents().addAll(EcoreUtil.copyAll(SysMLResources.this.resourceWithSysmlLibraryPackage.getContents()));
                    }, resource -> resource.eAdapters().add(createLibraryMetadataAdapter()));

                    this.referencedResourceWithSysmlMixedPackages = createResource(createEmfUri(), resource -> {
                        resource.getContents().addAll(EcoreUtil.copyAll(SysMLResources.this.resourceWithSysmlMixedPackages.getContents()));
                    }, resource -> resource.eAdapters().add(createLibraryMetadataAdapter()));
                }

                private static LibraryMetadataAdapter createLibraryMetadataAdapter() {
                    return new LibraryMetadataAdapter("namespace", "name", "version");
                }

                @Test
                @DisplayName("Resource from referenced library containing Package is read-only (both the resource and all of its contents)")
                public void testResourceWithSysmlPackageImported() {
                    // Note that this might be a bug, see https://github.com/eclipse-syson/syson/issues/1342.
                    SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.referencedResourceWithSysmlPackage, false);
                }

                @Test
                @DisplayName("Resource from referenced library containing LibraryPackage is read-only (both the resource and all of its contents)")
                public void testResourceWithSysmlLibraryPackageImported() {
                    SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.referencedResourceWithSysmlLibraryPackage, true);
                }

                @Test
                @DisplayName("Resource from referenced library containing Package and LibraryPackage is read-only (both the resource and all of its contents)")
                public void testResourceWithSysmlMixedPackagesImported() {
                    SysONReadOnlyObjectPredicateDelegateTests.this.assertResourceAndAllContentsIsReadOnly(this.referencedResourceWithSysmlMixedPackages, true);
                }
            }
        }
    }
}
