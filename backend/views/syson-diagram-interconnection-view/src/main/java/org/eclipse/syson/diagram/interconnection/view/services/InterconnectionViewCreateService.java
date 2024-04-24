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

import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlFactory;

/**
 * Creation-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewCreateService extends ViewCreateService {

    private final UtilService utilService;

    public InterconnectionViewCreateService(IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, IObjectService objectService) {
        super(viewDiagramDescriptionSearchService, objectService);
        this.utilService = new UtilService();
    }

    public BindingConnectorAsUsage createBindingConnectorAsUsage(PortUsage sourcePort, PortUsage targetPort) {
        PartUsage containerPart = this.utilService.getContainerPart(sourcePort);
        if (containerPart == null) {
            return null;
        }
        FeatureMembership featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        containerPart.getOwnedRelationship().add(featureMembership);

        BindingConnectorAsUsage bindingConnectorAsUsage = SysmlFactory.eINSTANCE.createBindingConnectorAsUsage();
        bindingConnectorAsUsage.setDeclaredName("bind");
        featureMembership.getOwnedRelatedElement().add(bindingConnectorAsUsage);

        EndFeatureMembership sourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        bindingConnectorAsUsage.getOwnedRelationship().add(sourceEndFeatureMembership);
        Feature sourceFeature = SysmlFactory.eINSTANCE.createFeature();
        sourceFeature.setIsEnd(true);
        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceFeature);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        sourceFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
        sourceReferenceSubsetting.setReferencedFeature(sourcePort);

        EndFeatureMembership targetEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        bindingConnectorAsUsage.getOwnedRelationship().add(targetEndFeatureMembership);
        Feature targetFeature = SysmlFactory.eINSTANCE.createFeature();
        targetFeature.setIsEnd(true);
        targetEndFeatureMembership.getOwnedRelatedElement().add(targetFeature);
        ReferenceSubsetting targetReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        targetFeature.getOwnedRelationship().add(targetReferenceSubsetting);
        targetReferenceSubsetting.setReferencedFeature(targetPort);

        return bindingConnectorAsUsage;
    }

    public InterfaceUsage createInterfaceUsage(PortUsage sourcePort, PortUsage targetPort) {
        PartUsage containerPart = this.utilService.getContainerPart(sourcePort);
        if (containerPart == null) {
            return null;
        }
        FeatureMembership featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        containerPart.getOwnedRelationship().add(featureMembership);

        InterfaceUsage interfaceUsage = SysmlFactory.eINSTANCE.createInterfaceUsage();
        interfaceUsage.setDeclaredName("connect");
        featureMembership.getOwnedRelatedElement().add(interfaceUsage);

        EndFeatureMembership sourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        interfaceUsage.getOwnedRelationship().add(sourceEndFeatureMembership);
        Feature sourceFeature = SysmlFactory.eINSTANCE.createFeature();
        sourceFeature.setIsEnd(true);
        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceFeature);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        sourceFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
        sourceReferenceSubsetting.setReferencedFeature(sourcePort);

        EndFeatureMembership targetEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        interfaceUsage.getOwnedRelationship().add(targetEndFeatureMembership);
        Feature targetFeature = SysmlFactory.eINSTANCE.createFeature();
        targetFeature.setIsEnd(true);
        targetEndFeatureMembership.getOwnedRelatedElement().add(targetFeature);
        ReferenceSubsetting targetReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        targetFeature.getOwnedRelationship().add(targetReferenceSubsetting);
        targetReferenceSubsetting.setReferencedFeature(targetPort);

        return interfaceUsage;
    }
}
