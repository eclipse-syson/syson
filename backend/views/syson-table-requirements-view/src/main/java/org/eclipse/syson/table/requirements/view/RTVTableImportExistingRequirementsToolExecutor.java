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
package org.eclipse.syson.table.requirements.view;

import java.util.Map;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.tables.api.IToolMenuEntryExecutor;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Success;
import org.eclipse.sirius.components.tables.Table;
import org.eclipse.sirius.components.tables.descriptions.TableDescription;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.metamodel.services.ElementInitializerSwitch;
import org.springframework.stereotype.Service;

/**
 * Executor for 'import-existing-requirements' tool entry.
 *
 * @author frouene
 */
@Service
public class RTVTableImportExistingRequirementsToolExecutor implements IToolMenuEntryExecutor {

    private final IObjectSearchService objectSearchService;

    public RTVTableImportExistingRequirementsToolExecutor(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
    }

    @Override
    public boolean canExecute(IEditingContext editingContext, TableDescription tableDescription, Table table, String menuEntryId) {
        return menuEntryId.equals(RTVTableToolMenuEntriesProvider.IMPORT_EXISTING_REQUIREMENTS_TABLE_TOOL_ENTRY);
    }

    @Override
    public IStatus execute(IEditingContext editingContext, TableDescription tableDescription, Table table, String menuEntryId) {

        var viewUsage = this.getViewUsage(editingContext, table);
        if (viewUsage != null) {
            this.exposeRequirements(editingContext, viewUsage);
            return new Success(ChangeKind.SEMANTIC_CHANGE, Map.of());
        }
        return new Failure("Unable to import existing requirements");
    }

    private ViewUsage getViewUsage(IEditingContext editingContext, Table table) {
        var objectId = table.getTargetObjectId();
        var parentObject = this.objectSearchService.getObject(editingContext, objectId);
        if (parentObject.isPresent()) {
            var object = parentObject.get();
            if (object instanceof ViewUsage viewUsage) {
                return viewUsage;
            }
        }
        return null;
    }

    private void exposeRequirements(IEditingContext editingContext, ViewUsage viewUsage) {
        var owningNamespace = viewUsage.getOwningNamespace();
        owningNamespace.getOwnedMember().stream()
                .filter(RequirementUsage.class::isInstance)
                .map(RequirementUsage.class::cast)
                .forEach(req -> {
                    if (!viewUsage.getExposedElement().contains(req)) {
                        var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
                        viewUsage.getOwnedRelationship().add(membershipExpose);
                        new ElementInitializerSwitch().doSwitch(membershipExpose);
                        membershipExpose.setImportedMembership(req.getOwningMembership());
                    }
                });
    }
}
