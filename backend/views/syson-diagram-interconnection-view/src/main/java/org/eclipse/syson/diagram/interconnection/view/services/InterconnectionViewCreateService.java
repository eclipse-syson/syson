/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ShowDiagramsInheritedMembersService;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Creation-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewCreateService extends ViewCreateService {

    public InterconnectionViewCreateService(IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, IObjectSearchService objectSearchService,
            ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService, IFeedbackMessageService msgService) {
        super(viewDiagramDescriptionSearchService, objectSearchService, showDiagramsInheritedMembersService, msgService);
    }

    @Override
    public boolean canCreateDiagram(Element element) {
        List<EClass> acceptedRootTypes = List.of(
                SysmlPackage.eINSTANCE.getUsage(),
                SysmlPackage.eINSTANCE.getDefinition());
        return super.canCreateDiagram(element) && acceptedRootTypes.stream().anyMatch(rootType -> rootType.isSuperTypeOf(element.eClass()));
    }

    public Element createPartUsageAndBindingConnectorAsUsage(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            // create a new port on given part usage
            var newSelfPort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(self, newSelfPort);
            this.elementInitializer(newSelfPort);
            // create a new part usage as a self sibling
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.addChildInParent(parent, newPartUsage);
            this.elementInitializer(newPartUsage);
            // create a new port on the new part usage
            var newPartUsagePort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(newPartUsage, newPartUsagePort);
            this.elementInitializer(newPartUsagePort);
            // create binding connector as usage edge between both new ports
            this.createBindingConnectorAsUsage(newSelfPort, newPartUsagePort);
            return newPartUsage;
        }
        return self;
    }

    public Element createPartUsageAndFlowConnection(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            // create a new port on given part usage
            var newSelfPort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(self, newSelfPort);
            this.elementInitializer(newSelfPort);
            // create a new part usage as a self sibling
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.addChildInParent(parent, newPartUsage);
            this.elementInitializer(newPartUsage);
            // create a new port on the new part usage
            var newPartUsagePort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(newPartUsage, newPartUsagePort);
            this.elementInitializer(newPartUsagePort);
            // create flow connection edge between both new ports
            this.createFlowUsage(newSelfPort, newPartUsagePort);
            return newPartUsage;
        }
        return self;
    }

    public Element createPartUsageAndInterface(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            // create a new port on given part usage
            var newSelfPort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(self, newSelfPort);
            this.elementInitializer(newSelfPort);
            // create a new part usage as a self sibling
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.addChildInParent(parent, newPartUsage);
            this.elementInitializer(newPartUsage);
            // create a new port on the new part usage
            var newPartUsagePort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(newPartUsage, newPartUsagePort);
            this.elementInitializer(newPartUsagePort);
            // create interface edge between both new ports
            this.createInterfaceUsage(newSelfPort, newPartUsagePort);
            return newPartUsage;
        }
        return self;
    }

    private void addChildInParent(Element parent, Element child) {
        // Parent could be a Package where children are stored
        // in OwingMembership and not in FeatureMembership.
        Membership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        if (parent instanceof Package) {
            membership = SysmlFactory.eINSTANCE.createOwningMembership();
        }
        membership.getOwnedRelatedElement().add(child);
        parent.getOwnedRelationship().add(membership);
    }
}
