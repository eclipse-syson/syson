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
package org.eclipse.syson.table.requirements.view.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.tables.Table;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.MembershipExpose;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewUsage;

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
        var tableTargetObjectId = siriusTable.getTargetObjectId();
        var viewUsage = this.objectSearchService.getObject(editingContext, tableTargetObjectId).stream()
                .filter(ViewUsage.class::isInstance)
                .map(ViewUsage.class::cast)
                .findFirst()
                .orElse(null);
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
}
