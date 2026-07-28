/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.table.requirements.view.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.tables.Table;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipExpose;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.metamodel.services.ElementInitializerSwitch;
import org.eclipse.syson.util.GetIntermediateContainerCreationSwitch;

/**
 * Mutation services for the Requirements Table View. The services declared in this class modify the model or the table
 * view.
 *
 * @author arichard
 */
public class RTVMutationServices {

    private final IObjectSearchService objectSearchService;

    private final DeleteService deleteService;

    public RTVMutationServices(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.deleteService = new DeleteService();
    }

    public boolean removeFromExposedElements(RequirementUsage requirementUsage, IEditingContext editingContext, Table siriusTable) {
        var viewUsage = this.getViewUsage(editingContext, siriusTable);
        if (viewUsage != null) {
            if (viewUsage.getExposedElement().contains(requirementUsage)) {
                List<MembershipExpose> membershipsExposed = viewUsage.getOwnedRelationship().stream()
                        .filter(MembershipExpose.class::isInstance)
                        .map(MembershipExpose.class::cast)
                        .filter(membershipExposed -> Objects.equals(membershipExposed.getImportedElement(), requirementUsage))
                        .toList();
                var iterator = membershipsExposed.iterator();
                while (iterator.hasNext()) {
                    var membershipExpose = iterator.next();
                    this.deleteService.deleteFromModel(membershipExpose);
                }
                return true;
            }
        }
        return false;
    }

    private ViewUsage getViewUsage(IEditingContext editingContext, Table table) {
        var tableTargetObjectId = table.getTargetObjectId();
        return this.objectSearchService.getObject(editingContext, tableTargetObjectId).stream()
                .filter(ViewUsage.class::isInstance)
                .map(ViewUsage.class::cast)
                .findFirst()
                .orElse(null);
    }

    public boolean editDocumentation(RequirementUsage requirementUsage, String newValue) {
        Documentation documentation = requirementUsage.getDocumentation().stream()
                .findFirst()
                .orElse(null);
        if (documentation != null) {
            documentation.setBody(newValue);
        } else {
            var newDocumentation = SysmlFactory.eINSTANCE.createDocumentation();
            newDocumentation.setBody(newValue);
            var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            requirementUsage.getOwnedRelationship().add(owningMembership);
            owningMembership.getOwnedRelatedElement().add(newDocumentation);
        }
        return true;
    }

    /**
     * Creates a new RequirementUsage visible inside the given ViewUsage.
     * The owning element of the given ViewUsage is the parent of the new created RequirementUsage.
     *
     * @param viewUsage
     *         the ViewUsage containing the table in which the new RequirementUsage will be created.
     * @return <code>true</code> if creation successes and <code>false</code> otherwise.
     */
    public boolean createRequirement(ViewUsage viewUsage) {
        var owningNamespace = viewUsage.getOwningNamespace();
        return this.createRequirement(owningNamespace, viewUsage) != null;
    }

    private RequirementUsage createRequirement(Element parent, ViewUsage viewUsage) {
        RequirementUsage newRequirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();
        Optional<EClass> optMembershipEClass = new GetIntermediateContainerCreationSwitch(parent).doSwitch(newRequirementUsage.eClass());
        if (optMembershipEClass.isPresent()) {
            var newMembership = SysmlFactory.eINSTANCE.create(optMembershipEClass.get());
            if (newMembership instanceof Membership membership) {
                var elementInitializerSwitch = new ElementInitializerSwitch();
                parent.getOwnedRelationship().add(membership);
                membership.getOwnedRelatedElement().add(newRequirementUsage);
                elementInitializerSwitch.doSwitch(newRequirementUsage);
                var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
                viewUsage.getOwnedRelationship().add(membershipExpose);
                elementInitializerSwitch.doSwitch(membership);
                membershipExpose.setImportedMembership(membership);
                return newRequirementUsage;
            }
        }
        return null;
    }

    /**
     * Creates a nested requirement (inside a given RequirementUsage).
     *
     * @param parent
     *         the RequirementUsage owing the new created RequirementUsage.
     * @param editingContext
     *         the (non-{@code null}) {@link IEditingContext}.
     * @param table
     *         the table in which the Requirement will be created.
     * @return the newly created RequirementUsage.
     */
    public RequirementUsage createNestedRequirement(RequirementUsage parent, IEditingContext editingContext, Table table) {
        var viewUsage = this.getViewUsage(editingContext, table);
        if (viewUsage != null) {
            return this.createRequirement(parent, viewUsage);
        }
        return null;
    }
}
