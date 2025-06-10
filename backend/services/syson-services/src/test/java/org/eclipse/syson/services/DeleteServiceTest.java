/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.emfjson.resource.JsonResourceFactoryImpl;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link DeleteService} related tests.
 *
 * @author arichard
 */
public class DeleteServiceTest {

    private DeleteService deleteService;

    private Resource resource;

    @BeforeEach
    void beforeEach() {
        this.deleteService = new DeleteService();
        var rSet = new ResourceSetImpl();
        var ePackageRegistry = new EPackageRegistryImpl();
        ePackageRegistry.put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        rSet.setPackageRegistry(ePackageRegistry);
        this.resource = new JsonResourceFactoryImpl().createResource(URI.createURI("model.sysml"));
        rSet.getResources().add(this.resource);
    }

    @DisplayName("Given a part1 subsetting part2, when part2 is deleted, then the subsetting should also be deleted.")
    @Test
    void testSusbetting() {
        var part1 = SysmlFactory.eINSTANCE.createPartUsage();
        this.resource.getContents().add(part1);
        part1.setDeclaredName("part1");
        var part2 = SysmlFactory.eINSTANCE.createPartUsage();
        this.resource.getContents().add(part2);
        part2.setDeclaredName("part2");
        var subsetting = SysmlFactory.eINSTANCE.createSubsetting();
        part1.getOwnedRelationship().add(subsetting);
        subsetting.setSubsettedFeature(part2);
        subsetting.setSubsettingFeature(part1);

        this.deleteService.deleteFromModel(part2);
        assertThat(this.resource.getContents()).hasSize(1).contains(part1);
    }

    @DisplayName("GIVEN a part1 that has been exposed by a ViewUsage, WHEN part1 is deleted, THEN the MembershipExpose should also be deleted.")
    @Test
    void testMembershipExpose() {
        var pkg1 = SysmlFactory.eINSTANCE.createPackage();
        pkg1.setDeclaredName("Pkg1");
        this.resource.getContents().add(pkg1);

        var part1Membership = SysmlFactory.eINSTANCE.createOwningMembership();
        pkg1.getOwnedRelationship().add(part1Membership);
        var view1Membership = SysmlFactory.eINSTANCE.createOwningMembership();
        pkg1.getOwnedRelationship().add(view1Membership);

        var part1 = SysmlFactory.eINSTANCE.createPartUsage();
        part1.setDeclaredName("part1");
        part1Membership.getOwnedRelatedElement().add(part1);

        var view1 = SysmlFactory.eINSTANCE.createViewUsage();
        view1.setDeclaredName("view1");
        view1Membership.getOwnedRelatedElement().add(view1);

        var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
        view1.getOwnedRelationship().add(membershipExpose);
        membershipExpose.setImportedMembership(part1Membership);

        assertThat(this.resource.getContents()).hasSize(1).contains(pkg1);
        assertThat(pkg1.getMember()).hasSize(2);
        assertThat(view1.getExposedElement()).isNotEmpty();

        this.deleteService.deleteFromModel(part1);
        assertThat(pkg1.getMember()).hasSize(1).contains(view1);
        assertThat(view1.getExposedElement()).isEmpty();

    }
}
