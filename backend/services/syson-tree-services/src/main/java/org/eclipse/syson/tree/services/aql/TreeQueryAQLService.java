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
package org.eclipse.syson.tree.services.aql;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;
import org.eclipse.syson.tree.services.TreeQuerySelectionDialogService;

/**
 * Entry point for all tree-related services doing queries in trees and called by AQL expressions in tree descriptions.
 *
 * @author arichard
 */
public class TreeQueryAQLService {

    private final ISysONExplorerService sysonExplorerService;

    private final TreeQuerySelectionDialogService treeQuerySelectionDialogService;

    /**
     * Creates a new tree query AQL service.
     *
     * @param sysonExplorerService
     *            the SysON explorer query service
     * @param treeQuerySelectionDialogService
     *            the selection dialog query service
     */
    public TreeQueryAQLService(ISysONExplorerService sysonExplorerService, TreeQuerySelectionDialogService treeQuerySelectionDialogService) {
        this.sysonExplorerService = Objects.requireNonNull(sysonExplorerService);
        this.treeQuerySelectionDialogService = Objects.requireNonNull(treeQuerySelectionDialogService);
    }

    public boolean isEditable(Object self) {
        return this.sysonExplorerService.isEditable(self);
    }

    public boolean isDeletable(Object self) {
        return this.sysonExplorerService.isDeletable(self);
    }

    public boolean isSelectable(Object self) {
        return this.sysonExplorerService.isSelectable(self);
    }

    public List<Object> getChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
        return this.sysonExplorerService.getChildren(self, editingContext, existingRepresentations, expandedIds, activeFilterIds);
    }

    public String getKind(Object self) {
        return this.sysonExplorerService.getKind(self);
    }

    public List<String> getImageURL(Object self) {
        return this.sysonExplorerService.getImageURL(self);
    }

    public String getLabel(Object self) {
        return this.sysonExplorerService.getLabel(self);
    }

    public String getLibraryLabel(Object self) {
        return this.sysonExplorerService.getLibraryLabel(self);
    }

    public Object getParent(Object self, String treeItemId, IEditingContext editingContext) {
        return this.sysonExplorerService.getParent(self, treeItemId, editingContext);
    }

    public String getReadOnlyTag(Object self) {
        return this.sysonExplorerService.getReadOnlyTag(self);
    }

    public String getShortName(Object self) {
        return this.sysonExplorerService.getShortName(self);
    }

    public String getTreeItemId(Object self) {
        return this.sysonExplorerService.getTreeItemId(self);
    }

    public Object getTreeItemObject(String treeItemId, IEditingContext editingContext) {
        return this.sysonExplorerService.getTreeItemObject(treeItemId, editingContext);
    }

    public String getTreeItemTooltip(Object self) {
        return this.sysonExplorerService.getTreeItemTooltip(self);
    }

    public String getType(Object self) {
        return this.sysonExplorerService.getType(self);
    }

    public boolean hasChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
        return this.sysonExplorerService.hasChildren(self, editingContext, existingRepresentations, expandedIds, activeFilterIds);
    }

    public List<Object> getElements(IEditingContext editingContext, List<String> activeFilterIds) {
        return this.sysonExplorerService.getElements(editingContext, activeFilterIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getActionReferenceSelectionDialogChildren(Object, IEditingContext, List)}.
     */
    public List<? extends Object> getActionReferenceSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.treeQuerySelectionDialogService.getActionReferenceSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getActionReferenceSelectionDialogElements(IEditingContext)}.
     */
    public List<Object> getActionReferenceSelectionDialogElements(IEditingContext editingContext) {
        return this.treeQuerySelectionDialogService.getActionReferenceSelectionDialogElements(editingContext);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getActorSelectionDialogChildren(Object, IEditingContext, List)}.
     */
    public List<? extends Object> getActorSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.treeQuerySelectionDialogService.getActorSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getActorSelectionDialogElements(IEditingContext)}.
     */
    public List<Object> getActorSelectionDialogElements(IEditingContext editingContext) {
        return this.treeQuerySelectionDialogService.getActorSelectionDialogElements(editingContext);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getConcernReferenceSelectionDialogChildren(Object, IEditingContext, List)}.
     */
    public List<? extends Object> getConcernReferenceSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.treeQuerySelectionDialogService.getConcernReferenceSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getConcernReferenceSelectionDialogElements(IEditingContext)}.
     */
    public List<Object> getConcernReferenceSelectionDialogElements(IEditingContext editingContext) {
        return this.treeQuerySelectionDialogService.getConcernReferenceSelectionDialogElements(editingContext);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getConstraintReferenceSelectionDialogChildren(Object, IEditingContext, List)}.
     */
    public List<? extends Object> getConstraintReferenceSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.treeQuerySelectionDialogService.getConstraintReferenceSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getConstraintReferenceSelectionDialogElements(IEditingContext)}.
     */
    public List<Object> getConstraintReferenceSelectionDialogElements(IEditingContext editingContext) {
        return this.treeQuerySelectionDialogService.getConstraintReferenceSelectionDialogElements(editingContext);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getExhibitStateSelectionDialogChildren(Object, IEditingContext, List)}.
     */
    public List<? extends Object> getExhibitStateSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.treeQuerySelectionDialogService.getExhibitStateSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getExhibitStateSelectionDialogElements(IEditingContext)}.
     */
    public List<Object> getExhibitStateSelectionDialogElements(IEditingContext editingContext) {
        return this.treeQuerySelectionDialogService.getExhibitStateSelectionDialogElements(editingContext);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getPortionKindSelectionDialogElement(Type)}.
     */
    public EClass getPortionKindSelectionDialogElement(Type type) {
        return this.treeQuerySelectionDialogService.getPortionKindSelectionDialogElement(type);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getNamespaceImportSelectionDialogChildren(Object, IEditingContext, List)}.
     */
    public List<? extends Object> getNamespaceImportSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.treeQuerySelectionDialogService.getNamespaceImportSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getNamespaceImportSelectionDialogElements(IEditingContext)}.
     */
    public List<Object> getNamespaceImportSelectionDialogElements(IEditingContext editingContext) {
        return this.treeQuerySelectionDialogService.getNamespaceImportSelectionDialogElements(editingContext);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getObjectiveRequirementSelectionDialogChildren(Object, IEditingContext, List)}.
     */
    public List<? extends Object> getObjectiveRequirementSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.treeQuerySelectionDialogService.getObjectiveRequirementSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getObjectiveRequirementSelectionDialogElements(IEditingContext)}.
     */
    public List<Object> getObjectiveRequirementSelectionDialogElements(IEditingContext editingContext) {
        return this.treeQuerySelectionDialogService.getObjectiveRequirementSelectionDialogElements(editingContext);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getStakeholderSelectionDialogChildren(Object, IEditingContext, List)}.
     */
    public List<? extends Object> getStakeholderSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.treeQuerySelectionDialogService.getStakeholderSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getStakeholderSelectionDialogElements(IEditingContext)}.
     */
    public List<Object> getStakeholderSelectionDialogElements(IEditingContext editingContext) {
        return this.treeQuerySelectionDialogService.getStakeholderSelectionDialogElements(editingContext);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getSubjectSelectionDialogChildren(Object, IEditingContext, List)}.
     */
    public List<? extends Object> getSubjectSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.treeQuerySelectionDialogService.getSubjectSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getSubjectSelectionDialogElements(IEditingContext)}.
     */
    public List<Object> getSubjectSelectionDialogElements(IEditingContext editingContext) {
        return this.treeQuerySelectionDialogService.getSubjectSelectionDialogElements(editingContext);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getSelectionDialogChildren(Object, IEditingContext, List, List)}.
     */
    public List<? extends Object> getSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds, List<EClassifier> candidates) {
        return this.treeQuerySelectionDialogService.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds, candidates);
    }

    /**
     * {@link TreeQuerySelectionDialogService#getSelectionDialogElements(IEditingContext, List)}.
     */
    public List<Object> getSelectionDialogElements(IEditingContext editingContext, List<EClassifier> candidates) {
        return this.treeQuerySelectionDialogService.getSelectionDialogElements(editingContext, candidates);
    }
}
