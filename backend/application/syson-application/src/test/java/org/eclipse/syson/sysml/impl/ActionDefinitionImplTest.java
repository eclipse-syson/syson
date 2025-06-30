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
package org.eclipse.syson.sysml.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link ActionDefinition}.
 *
 * @author Arthur Daussy
 */
public class ActionDefinitionImplTest {

    private static List<Resource> standardLibraries;

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
    }

    private void addToNewResource(EObject root) {
        JsonResource resource = new JSONResourceFactory().createResource(URI.createURI("fakeURI://" + new Random().nextInt() + ".sysml"));
        this.resourceSet.getResources().add(resource);
        resource.getContents().add(root);
    }

    @DisplayName("GIVEN a ActionDefinition, WHEN checking its specialization, THEN it should specialized the default Actions::Action and its feature should be visible")
    @Test
    public void inheritSuperActionFeature() {
        ActionDefinition actionDefinition = SysmlFactory.eINSTANCE.createActionDefinition();
        this.addToNewResource(actionDefinition);

        EList<Specialization> specializations = actionDefinition.getOwnedSpecialization();
        assertThat(specializations)
                .hasSize(1)
                .allMatch(s -> s.getGeneral().getQualifiedName().equals("Actions::Action"));

        Specialization specialization = specializations.get(0);

        assertThat(specialization.getOwningRelatedElement()).isEqualTo(actionDefinition);

        EList<Membership> visibleMemberships = actionDefinition.visibleMemberships(new BasicEList<>(), false, false);

        assertThat(visibleMemberships).anyMatch(m -> m.getMemberElement() != null && "Actions::Action::start".equals(m.getMemberElement().getQualifiedName()));
    }
}
