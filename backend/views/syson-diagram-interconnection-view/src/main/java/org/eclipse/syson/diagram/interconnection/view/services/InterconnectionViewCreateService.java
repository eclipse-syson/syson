/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.diagram.interconnection.view.services;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ShowDiagramsInheritedMembersService;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FlowConnectionUsage;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Creation-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewCreateService extends ViewCreateService {

    public InterconnectionViewCreateService(IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, IObjectService objectService,
            ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService) {
        super(viewDiagramDescriptionSearchService, objectService, showDiagramsInheritedMembersService);
    }

    public BindingConnectorAsUsage createBindingConnectorAsUsage(Feature source, Feature target) {
        Namespace bindingContainer = this.getClosestContainingDefinitionOrPackageFrom(source);
        if (bindingContainer == null) {
            return null;
        }
        FeatureMembership featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        bindingContainer.getOwnedRelationship().add(featureMembership);

        BindingConnectorAsUsage bindingConnectorAsUsage = SysmlFactory.eINSTANCE.createBindingConnectorAsUsage();
        this.elementInitializer(bindingConnectorAsUsage);
        featureMembership.getOwnedRelatedElement().add(bindingConnectorAsUsage);

        EndFeatureMembership sourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        bindingConnectorAsUsage.getOwnedRelationship().add(sourceEndFeatureMembership);
        Feature sourceFeature = SysmlFactory.eINSTANCE.createFeature();
        this.elementInitializer(sourceFeature);
        sourceFeature.setIsEnd(true);
        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceFeature);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        this.elementInitializer(sourceReferenceSubsetting);
        sourceFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
        sourceReferenceSubsetting.setReferencedFeature(source);

        EndFeatureMembership targetEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        bindingConnectorAsUsage.getOwnedRelationship().add(targetEndFeatureMembership);
        Feature targetFeature = SysmlFactory.eINSTANCE.createFeature();
        this.elementInitializer(sourceFeature);
        targetFeature.setIsEnd(true);
        targetEndFeatureMembership.getOwnedRelatedElement().add(targetFeature);
        ReferenceSubsetting targetReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        this.elementInitializer(targetReferenceSubsetting);
        targetFeature.getOwnedRelationship().add(targetReferenceSubsetting);
        targetReferenceSubsetting.setReferencedFeature(target);

        return bindingConnectorAsUsage;
    }

    public InterfaceUsage createInterfaceUsage(PortUsage sourcePort, PortUsage targetPort) {
        Namespace interfaceContainer = this.getClosestContainingDefinitionOrPackageFrom(sourcePort);
        if (interfaceContainer == null) {
            return null;
        }
        FeatureMembership featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        interfaceContainer.getOwnedRelationship().add(featureMembership);

        InterfaceUsage interfaceUsage = SysmlFactory.eINSTANCE.createInterfaceUsage();
        this.elementInitializer(interfaceUsage);
        // Edges should have an empty default name. This is not the case when using the initializer, because
        // InterfaceUsage can be a node, which requires a default name.
        interfaceUsage.setDeclaredName("");
        featureMembership.getOwnedRelatedElement().add(interfaceUsage);

        EndFeatureMembership sourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        interfaceUsage.getOwnedRelationship().add(sourceEndFeatureMembership);
        Feature sourceFeature = SysmlFactory.eINSTANCE.createFeature();
        this.elementInitializer(sourceFeature);
        sourceFeature.setIsEnd(true);
        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceFeature);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        this.elementInitializer(sourceReferenceSubsetting);
        sourceFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
        sourceReferenceSubsetting.setReferencedFeature(sourcePort);

        EndFeatureMembership targetEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        interfaceUsage.getOwnedRelationship().add(targetEndFeatureMembership);
        Feature targetFeature = SysmlFactory.eINSTANCE.createFeature();
        this.elementInitializer(targetFeature);
        targetFeature.setIsEnd(true);
        targetEndFeatureMembership.getOwnedRelatedElement().add(targetFeature);
        ReferenceSubsetting targetReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        this.elementInitializer(targetReferenceSubsetting);
        targetFeature.getOwnedRelationship().add(targetReferenceSubsetting);
        targetReferenceSubsetting.setReferencedFeature(targetPort);

        return interfaceUsage;
    }

    public FlowConnectionUsage createFlowConnectionUsage(Feature source, Feature target) {
        Namespace flowContainer = this.getClosestContainingDefinitionOrPackageFrom(source);
        if (flowContainer == null) {
            return null;
        }
        FeatureMembership featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        flowContainer.getOwnedRelationship().add(featureMembership);

        FlowConnectionUsage flowConnectionUsage = SysmlFactory.eINSTANCE.createFlowConnectionUsage();
        this.elementInitializer(flowConnectionUsage);
        featureMembership.getOwnedRelatedElement().add(flowConnectionUsage);

        EndFeatureMembership sourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        flowConnectionUsage.getOwnedRelationship().add(sourceEndFeatureMembership);
        Feature sourceFeature = SysmlFactory.eINSTANCE.createFeature();
        this.elementInitializer(sourceFeature);
        sourceFeature.setIsEnd(true);
        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceFeature);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        this.elementInitializer(sourceReferenceSubsetting);
        sourceFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
        sourceReferenceSubsetting.setReferencedFeature(source);

        EndFeatureMembership targetEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        flowConnectionUsage.getOwnedRelationship().add(targetEndFeatureMembership);
        Feature targetFeature = SysmlFactory.eINSTANCE.createFeature();
        this.elementInitializer(targetFeature);
        targetFeature.setIsEnd(true);
        targetEndFeatureMembership.getOwnedRelatedElement().add(targetFeature);
        ReferenceSubsetting targetReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        this.elementInitializer(targetReferenceSubsetting);
        targetFeature.getOwnedRelationship().add(targetReferenceSubsetting);
        targetReferenceSubsetting.setReferencedFeature(target);

        return flowConnectionUsage;
    }

    private Namespace getClosestContainingDefinitionOrPackageFrom(Element element) {
        var owner = element.eContainer();
        while (!(owner instanceof Package || owner instanceof Definition) && owner != null) {
            owner = owner.eContainer();
        }
        return (Namespace) owner;
    }

    @Override
    public boolean canCreateDiagram(Element element) {
        List<EClass> acceptedRootTypes = List.of(
                SysmlPackage.eINSTANCE.getUsage(),
                SysmlPackage.eINSTANCE.getDefinition());
        return super.canCreateDiagram(element) && acceptedRootTypes.stream().anyMatch(rootType -> rootType.isSuperTypeOf(element.eClass()));
    }
}
