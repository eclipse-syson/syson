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
package org.eclipse.syson.metamodel.helper;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.LibraryNamespaceProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Checks the implicit parameter examples against the application's actual standard libraries.
 *
 * @author Arthur Daussy
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImplicitParameterRedefinitionLibraryTests {

    private Namespace model;

    private LibraryNamespaceProvider provider;

    /**
     * KerML 7.4.7.2 and 7.4.7.3 matches parameters of specialized behaviors and typed steps by position.
     * The XMI fixture includes the equivalent SysML declarations above the model.
     */
    @ParameterizedTest
    @CsvSource({"Simple, local1, A::p1",
                "Deep, deep1, Simple::local1",
                "Multiple, merged1, A::p1|Other::q1",
                "Mixed, explicit1, A::p1|Other::q1",
                "Mixed, automatic2, A::p2|Other::q2",
                "instance, usage1, A::p1",
                "anonymous, , A::p1"})
    @DisplayName("GIVEN implicit parameter declarations and standard libraries, WHEN querying their redefinitions, THEN both switch implementations expose the corresponding sources without persisting relationships")
    public void inferParameterRelationships(String contextName, String localName, String targets) {
        Type context = this.model.getOwnedMember().stream().filter(Type.class::isInstance).map(Type.class::cast)
                .filter(type -> contextName.equals(type.getDeclaredName())).findFirst().orElseThrow();
        Feature local = context.getOwnedFeature().stream()
                .filter(feature -> Objects.equals(localName, feature.getDeclaredName()))
                .findFirst().orElseThrow();
        List<Specialization> contained = local.getOwnedRelationship().stream().filter(Specialization.class::isInstance).map(Specialization.class::cast).toList();
        List<String> expected = Arrays.stream(targets.split("\\|")).map(name -> "ImplicitParameterProbe::" + name).toList();

        List<Redefinition> redefinitions = local.getOwnedRedefinition();
        List<Specialization> serviceRelationships = new org.eclipse.syson.sysml.metamodel.helper.ImplicitSpecializationSwitch(contained, this.provider).doSwitch(local);

        assertThat(redefinitions).extracting(Redefinition::getRedefinedFeature).extracting(Feature::getQualifiedName).containsExactlyElementsOf(expected);
        // The service switch only returns newly inferred relationships. The metamodel getter also returns stored ones.
        assertThat(serviceRelationships.stream().filter(Redefinition.class::isInstance).map(Redefinition.class::cast)
                .map(Redefinition::getRedefinedFeature)).containsExactlyElementsOf(redefinitions.stream()
                        .filter(Redefinition::isIsImplied).map(Redefinition::getRedefinedFeature).toList());
        assertThat(local.getOwnedRelationship()).containsExactlyElementsOf(contained);
        if (localName == null) {
            assertThat(local.getName()).isEqualTo("p1");
        }
    }

    @BeforeAll
    public void loadModelWithStandardLibraries() throws IOException {
        ResourceSet resourceSet = new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration()).getLibrariesResourceSet();
        this.provider = LibraryNamespaceProvider.get(resourceSet);
        Resource resource = new XMIResourceImpl(URI.createURI("test:/implicit-parameters.xmi"));
        resourceSet.getResources().add(resource);
        try (InputStream input = this.getClass().getResourceAsStream("/models/parameters/implicit-parameter-redefinitions.xmi")) {
            assertThat(input).isNotNull();
            resource.load(input, Map.of());
        }
        assertThat(resource.getErrors()).isEmpty();
        Namespace document = (Namespace) resource.getContents().getFirst();
        this.model = (Namespace) document.getOwnedMember().getFirst();
    }
}
