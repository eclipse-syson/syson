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
package org.eclipse.syson.metamodel.helper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.util.LibraryNamespaceProvider;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link LibraryNamespaceProvider}.
 *
 * @author Arthur Daussy
 */
public class LibraryNamespaceProviderTests {

    private static List<Resource> standardLibraries;

    private final ModelBuilder builder = new ModelBuilder();

    private ResourceSet resourceSet;

    @AfterAll
    public static void afterAll() {
        standardLibraries = null;
    }

    @BeforeAll
    public static void beforeAll() {
        standardLibraries = new ArrayList<>(new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration()).getLibrariesResourceSet().getResources());
    }

    @BeforeEach
    public void beforeEach() {
        this.resourceSet = new ResourceSetImpl();
        this.resourceSet.getResources().addAll(standardLibraries);
        this.resourceSet.getResources().add(this.createStandardLibWithSpace());
    }

    @Test
    @DisplayName("Given a ResourceSet where all library has been registered a immutable, WHEN accessing some Namespace from their qualified name, THEN the targeted element should be returned.")
    public void checkWithRegisteredLibrary() {
        LibraryNamespaceProvider provider = new LibraryNamespaceProvider(this.resourceSet);
        // Set all resource to immutable
        for (Resource r : this.resourceSet.getResources()) {
            provider.addImmutableLibrariesNamespaces(r);
        }

        // Test some use case
        this.assertNamespaceAccess("BooleanFunctions::ToString::x", provider);
        this.assertNamespaceAccess("Clocks::TimeOf::timeOrderingConstraint", provider);
        this.assertNamespaceAccess("BaseFunctions::'==='::x", provider);
        this.assertNamespaceAccess("Views::asElementTable::columnView::viewRendering", provider);
        this.assertNamespaceAccess("USCustomaryUnits::'degree fahrenheit (absolute temperature scale)'::zeroDegreeFahrenheitToKelvinShift::source", provider);
        this.assertNamespaceAccess("'Custom library'::'Custom part def'::'Custom part usage'", provider);

    }

    @Test
    @DisplayName("Given a ResourceSet with no library registered as immutable, WHEN accessing some Namespace from their qualified name, THEN the targeted element should be returned.")

    public void withNoLibraryAdded() {
        LibraryNamespaceProvider provider = new LibraryNamespaceProvider(this.resourceSet);
        // Test some use case
        this.assertNamespaceAccess("BooleanFunctions::ToString::x", provider);
        this.assertNamespaceAccess("Clocks::TimeOf::timeOrderingConstraint", provider);
        this.assertNamespaceAccess("BaseFunctions::'==='::x", provider);
        this.assertNamespaceAccess("Views::asElementTable::columnView::viewRendering", provider);
        this.assertNamespaceAccess("USCustomaryUnits::'degree fahrenheit (absolute temperature scale)'::zeroDegreeFahrenheitToKelvinShift::source", provider);
        this.assertNamespaceAccess("USCustomaryUnits::'quad (10^15 Btu_IT)'::unitConversion::isExact", provider);
        this.assertNamespaceAccess("'Custom library'::'Custom part def'::'Custom part usage'", provider);

    }

    private void assertNamespaceAccess(String expectedQualifiedName, LibraryNamespaceProvider provider) {
        assertThat(provider.getNamespaceFromLibrary(expectedQualifiedName))
                .as("Looking for " + expectedQualifiedName)
                .isNotNull()
                .matches(e -> expectedQualifiedName.equals(e.getQualifiedName()));

    }

    private Resource createStandardLibWithSpace() {
        JsonResource resource = new JSONResourceFactory().createResource(URI.createURI("fakeURI://" + new Random().nextInt() + ".sysml"));
        this.resourceSet.getResources().add(resource);

        Namespace rootnamespace = this.builder.createRootNamespace();
        resource.getContents().add(rootnamespace);

        LibraryPackage lib = this.builder.createInWithName(LibraryPackage.class, rootnamespace, "Custom library");

        PartDefinition partDef = this.builder.createInWithName(PartDefinition.class, lib, "Custom part def");

        this.builder.createInWithName(PartUsage.class, partDef, "Custom part usage");
        return resource;
    }

}
