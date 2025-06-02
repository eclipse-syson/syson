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
package org.eclipse.syson.services.data;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.VisibilityKind;

/**
 * Instance of the following model.
 *
 * <pre>
    package p1 {
        part flashlight {
            private import p1::Mass::*;
            attribute actualWeight : Mass;
            constraint {
                actualWeight <= 0.25 [nm]
            }
        }

        attribute def Mass {
            public attribute <nm> num;
        }

        part def Flashlight {}

        part superFlashLight {}
    }
 * </pre>
 *
 * @author Arthur Daussy
 */
public class SmallFlashlightExample {

    public static final String FLASHLIGHT_PART_USAGE_NAME = "flashlight";

    private final UtilService utilService = new UtilService();

    private PartUsage flashlightPartUsage;

    private PartDefinition flashLightDefinition;

    private PartUsage superFlashLightPartUsage;

    private AttributeUsage actualWeightAttributeUsage;

    private AttributeDefinition massAttributeDefinition;

    private AttributeUsage numAttributeUsage;

    private Package p1;

    private ConstraintUsage flashlightConstraint;

    private ResourceSetImpl resourceSet;

    private JsonResource resource;

    public SmallFlashlightExample() {
        this.build();
    }

    private void build() {
        this.resourceSet = new ResourceSetImpl();
        this.resource = new JSONResourceFactory().createResource(URI.createURI("fakeURI://testModel"));
        this.resourceSet.getResources().add(this.resource);
        this.p1 = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(this.p1);
        this.p1.setDeclaredName("p1");

        this.createFlashlight();

        this.flashLightDefinition = SysmlFactory.eINSTANCE.createPartDefinition();
        this.flashLightDefinition.setDeclaredName("Flashlight");
        this.addToRoot(this.flashLightDefinition);

        this.superFlashLightPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
        this.superFlashLightPartUsage.setDeclaredName("superFlashlight");
        this.addToRoot(this.superFlashLightPartUsage);
    }

    /**
     * Modify the model to type 'flashlight' with 'Flashlight'.
     *
     * @return this for convenience
     */
    public SmallFlashlightExample addFlashlightType() {
        this.utilService.setFeatureTyping(this.flashlightPartUsage, this.flashLightDefinition);
        return this;
    }

    /**
     * Modify the model to make 'flashlight' a subsetting of 'superFlashLight'.
     *
     * @return this for convenience
     */
    public SmallFlashlightExample addFlashlightSubsetting() {
        this.utilService.setSubsetting(this.flashlightPartUsage, this.superFlashLightPartUsage);
        return this;
    }

    /**
     * All the given element at the root of this model.
     *
     * @param element
     *            an element to add
     */
    public void addToRoot(Element element) {
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        this.p1.getOwnedRelationship().add(owningMembership);
        owningMembership.getOwnedRelatedElement().add(element);
    }

    private void createFlashlight() {
        this.flashlightPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
        this.flashlightPartUsage.setDeclaredName(FLASHLIGHT_PART_USAGE_NAME);

        this.addToRoot(this.flashlightPartUsage);

        FeatureMembership actualWeightMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        this.flashlightPartUsage.getOwnedRelationship().add(actualWeightMembership);
        actualWeightMembership.setVisibility(VisibilityKind.PUBLIC);
        this.actualWeightAttributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        actualWeightMembership.getOwnedRelatedElement().add(this.actualWeightAttributeUsage);
        this.actualWeightAttributeUsage.setDeclaredName("actualWeight");

        this.massAttributeDefinition = SysmlFactory.eINSTANCE.createAttributeDefinition();
        this.massAttributeDefinition.setDeclaredName("Mass");
        this.addToRoot(this.massAttributeDefinition);

        FeatureMembership numMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        this.massAttributeDefinition.getOwnedRelationship().add(numMembership);
        numMembership.setVisibility(VisibilityKind.PUBLIC);
        this.numAttributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        numMembership.getOwnedRelatedElement().add(this.numAttributeUsage);
        this.numAttributeUsage.setDeclaredName("num");
        this.numAttributeUsage.setDeclaredShortName("nm");

        // Set the type of actualWeight to mass
        this.utilService.setFeatureTyping(this.actualWeightAttributeUsage, this.massAttributeDefinition);

        // Create private import
        NamespaceImport nmImport = SysmlFactory.eINSTANCE.createNamespaceImport();
        nmImport.setImportedNamespace(this.massAttributeDefinition);
        nmImport.setIsImportAll(true);
        this.flashlightPartUsage.getOwnedRelationship().add(nmImport);

        // Create constraint
        this.flashlightConstraint = SysmlFactory.eINSTANCE.createConstraintUsage();
        FeatureMembership constraintMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        constraintMembership.getOwnedRelatedElement().add(this.flashlightConstraint);
        this.flashlightPartUsage.getOwnedRelationship().add(constraintMembership);
    }

    public PartUsage getFlashlightPartUsage() {
        return this.flashlightPartUsage;
    }

    public PartDefinition getFlashLightDefinition() {
        return this.flashLightDefinition;
    }

    public PartUsage getSuperFlashLightPartUsage() {
        return this.superFlashLightPartUsage;
    }

    public AttributeUsage getActualWeightAttributeUsage() {
        return this.actualWeightAttributeUsage;
    }

    public AttributeDefinition getMassAttributeDefinition() {
        return this.massAttributeDefinition;
    }

    public AttributeUsage getNumAttributeUsage() {
        return this.numAttributeUsage;
    }

    public Package getP1() {
        return this.p1;
    }

    public ConstraintUsage getFlashlightConstraint() {
        return this.flashlightConstraint;
    }

}
