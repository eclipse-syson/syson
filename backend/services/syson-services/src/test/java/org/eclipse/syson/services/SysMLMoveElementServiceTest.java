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
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.services.EditingContextCrossReferenceAdapter;
import org.eclipse.sirius.emfjson.resource.JsonResourceFactoryImpl;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the SysML move element service.
 *
 * @author gdaniel
 */
public class SysMLMoveElementServiceTest extends AbstractServiceTest {

    private ResourceSet rSet;

    private Resource resource;

    private SysMLMoveElementService moveService;

    private List<EObject> readOnlyElements;

    @BeforeEach
    public void setUp() {
        this.readOnlyElements = new ArrayList<>();
        this.moveService = new SysMLMoveElementService(e -> this.readOnlyElements.contains(e));
        this.rSet = new ResourceSetImpl();
        // Make sure the resources we manipulate use the Sirius Web CrossReferenceAdapter.
        // This is particularly important when manipulating memberships, where the cross-referencer may produce
        // unexpected behaviors that cannot be observed in a resource without it.
        this.rSet.eAdapters().add(new EditingContextCrossReferenceAdapter());
        this.resource = new JsonResourceFactoryImpl().createResource(URI.createURI("UtilServiceTest"));
        this.rSet.getResources().add(this.resource);
    }

    @Test
    @DisplayName("GIVEN a PartUsage p1 in a Package, WHEN we move its owning membership to a PartUsage p2, THEN p2's memberships are updated and the Package doesn't contain p1 anymore")
    public void moveMembershipOfPartUsageInPackageToPartUsage() {
        Package pack = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(pack);
        pack.setDeclaredName("pack");
        PartUsage p1 = SysmlFactory.eINSTANCE.createPartUsage();
        p1.setDeclaredName("p1");
        this.addInPackage(p1, pack);
        PartUsage p2 = SysmlFactory.eINSTANCE.createPartUsage();
        p2.setDeclaredName("p2");
        this.addInPackage(p2, pack);

        this.moveService.moveSemanticElement(p1, p2);

        assertThat(pack.getOwnedRelationship()).hasSize(1);
        assertThat(p2.getOwnedRelationship()).hasSize(1);
        assertThat(p2.getOwnedRelationship().get(0))
                .isInstanceOf(FeatureMembership.class)
                .returns(List.of(p1), Relationship::getOwnedRelatedElement);
    }

    @Test
    @DisplayName("GIVEN a read only element, WHEN selected as a new parent during a move action, THEN the move action should fail")
    public void forbiddenMoveToReadOnlyElement() {
        Package pack = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(pack);
        Package p1 = SysmlFactory.eINSTANCE.createPackage();
        this.addInPackage(p1, pack);
        Package p2 = SysmlFactory.eINSTANCE.createPackage();
        this.addInPackage(p2, pack);

        this.readOnlyElements.add(p2);

        assertFalse(this.moveService.moveSemanticElement(p1, p2).isSuccess());
    }

    @Test
    @DisplayName("GIVEN a read only element, WHEN selected as a moving object during a move action, THEN the move action should fail")
    public void forbiddenMoveOfReadOnlyElement() {
        Package pack = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(pack);
        Package p1 = SysmlFactory.eINSTANCE.createPackage();
        this.addInPackage(p1, pack);
        Package p2 = SysmlFactory.eINSTANCE.createPackage();
        this.addInPackage(p2, pack);

        this.readOnlyElements.add(p2);

        assertFalse(this.moveService.moveSemanticElement(p2, p1).isSuccess());
    }

    @Test
    @DisplayName("GIVEN an SysML Element, WHEN selected as a moving object during a move action to a membership, THEN the move action should fail")
    public void forbiddenMoveToMembershit() {
        Package pack = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(pack);
        Package p1 = SysmlFactory.eINSTANCE.createPackage();
        this.addInPackage(p1, pack);
        Package p2 = SysmlFactory.eINSTANCE.createPackage();
        this.addInPackage(p2, pack);

        this.readOnlyElements.add(p2);

        assertFalse(this.moveService.moveSemanticElement(p2, p1.getOwningMembership()).isSuccess());
    }

    @Test
    @DisplayName("Check that a elements can't be move to one of its descendant")
    public void forbiddenMoveDescendantElements() {
        Package pack = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(pack);
        Package p1 = SysmlFactory.eINSTANCE.createPackage();
        this.addInPackage(p1, pack);
        Package p2 = SysmlFactory.eINSTANCE.createPackage();
        this.addInPackage(p2, p1);

        assertFalse(this.moveService.moveSemanticElement(p1, p2).isSuccess());
    }

    @Test
    @DisplayName("GIVEN a typed PartUsage in a Package, WHEN we move its owning membership to a PartDefinition, THEN the PartDefinition's membership are updated, and the PartUsage's FeatureTyping is still there")
    public void moveMembershipOfTypedPartUsageInPackageToPartDefinition() {
        Package pack = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(pack);
        pack.setDeclaredName("pack");
        PartUsage p1 = SysmlFactory.eINSTANCE.createPartUsage();
        p1.setDeclaredName("p1");
        this.addInPackage(p1, pack);
        PartDefinition pDef1 = SysmlFactory.eINSTANCE.createPartDefinition();
        pDef1.setDeclaredName("PDef1");
        this.addInPackage(pDef1, pack);
        FeatureTyping featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        p1.getOwnedRelationship().add(featureTyping);
        featureTyping.setType(pDef1);
        featureTyping.setTypedFeature(p1);
        PartDefinition pDef2 = SysmlFactory.eINSTANCE.createPartDefinition();
        pDef2.setDeclaredName("PDef2");
        this.addInPackage(pDef2, pack);

        this.moveService.moveSemanticElement(p1, pDef2);

        assertThat(pack.getOwnedRelationship()).hasSize(2);
        assertThat(pDef2.getOwnedRelationship()).hasSize(1);
        assertThat(pDef2.getOwnedRelationship().get(0))
                .isInstanceOf(FeatureMembership.class)
                .returns(List.of(p1), Relationship::getOwnedRelatedElement);
        // Make sure the FeatureTyping hasn't moved. This is typically the case if the memberships aren't created in the
        // right order, and the cross-referencer deleted references to p1.
        assertThat(p1.getOwnedRelationship()).hasSize(1);
        assertThat(p1.getOwnedRelationship().get(0)).isInstanceOf(FeatureTyping.class);
        FeatureTyping p1FeatureTyping = (FeatureTyping) p1.getOwnedRelationship().get(0);
        assertThat(p1FeatureTyping)
                .returns(pDef1, FeatureTyping::getType)
                .returns(p1, FeatureTyping::getTypedFeature);
    }

    private void addInPackage(Element element, Package pack) {
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        pack.getOwnedRelationship().add(owningMembership);
        owningMembership.getOwnedRelatedElement().add(element);
    }

}
