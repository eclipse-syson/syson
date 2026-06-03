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
package org.eclipse.syson.tree.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.ConnectionDefinition;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFragment;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;
import org.springframework.stereotype.Service;

/**
 * Selection dialog services doing tree queries.
 *
 * @author arichard
 */
@Service
public class TreeQuerySelectionDialogService {

    private final ISysONExplorerService sysONExplorerService;

    /**
     * Creates a new selection dialog query service.
     *
     * @param sysONExplorerService
     *            the explorer service used to retrieve root elements and fragment children
     */
    public TreeQuerySelectionDialogService(ISysONExplorerService sysONExplorerService) {
        this.sysONExplorerService = Objects.requireNonNull(sysONExplorerService);
    }

    /**
     * Provides the children of element in the tree of the selection dialog for presenting all existing ConstraiNtUsage.
     *
     * @param selectionDialogTreeElement
     *            a (non-{@code null}) selection dialog tree element.
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @param expandedIds
     *            the list of already expanded treeItems, by their Ids.
     * @return the (non-{@code null}) {@link List} of all children that contain (possibly indirectly) an
     *         {@link org.eclipse.syson.sysml.ConstraintUsage}.
     */
    public List<? extends Object> getConstraintReferenceSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds, List.of(SysmlPackage.eINSTANCE.getConstraintUsage()));
    }

    /**
     * Provides the root elements in the tree of the selection dialog for presenting all existing ConstraintUsage.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @return the (non-{@code null}) {@link List} of all {@link Resource} and {@link ISysONExplorerFragment} that
     *         contain at least one {@link org.eclipse.syson.sysml.ConstraintUsage}.
     */
    public List<Object> getConstraintReferenceSelectionDialogElements(IEditingContext editingContext) {
        return this.getSelectionDialogElements(editingContext, List.of(SysmlPackage.eINSTANCE.getConstraintUsage()));
    }

    /**
     * Provides the root elements for the namespace import selection dialog.
     *
     * @param editingContext
     *            the editing context
     * @return the root elements containing packages
     */
    public List<Object> getNamespaceImportSelectionDialogElements(IEditingContext editingContext) {
        return this.getSelectionDialogElements(editingContext, List.of(SysmlPackage.eINSTANCE.getPackage()));
    }

    /**
     * Provides the children for the namespace import selection dialog.
     *
     * @param selectionDialogTreeElement
     *            the expanded tree element
     * @param editingContext
     *            the editing context
     * @param expandedIds
     *            the expanded tree item ids
     * @return the children containing packages
     */
    public List<? extends Object> getNamespaceImportSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds, List.of(SysmlPackage.eINSTANCE.getPackage()));
    }

    /**
     * Provides the root elements for the stakeholder selection dialog.
     *
     * @param editingContext
     *            the editing context
     * @return the root elements containing part usages
     */
    public List<Object> getStakeholderSelectionDialogElements(IEditingContext editingContext) {
        return this.getSelectionDialogElements(editingContext, List.of(SysmlPackage.eINSTANCE.getPartUsage()));
    }

    /**
     * Provides the children for the stakeholder selection dialog.
     *
     * @param selectionDialogTreeElement
     *            the expanded tree element
     * @param editingContext
     *            the editing context
     * @param expandedIds
     *            the expanded tree item ids
     * @return the children containing part usages
     */
    public List<? extends Object> getStakeholderSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds, List.of(SysmlPackage.eINSTANCE.getPartUsage()));
    }

    /**
     * Provides the root elements for the subject selection dialog.
     *
     * @param editingContext
     *            the editing context
     * @return the root elements containing types
     */
    public List<Object> getSubjectSelectionDialogElements(IEditingContext editingContext) {
        return this.getSelectionDialogElements(editingContext, List.of(SysmlPackage.eINSTANCE.getType()));
    }

    /**
     * Provides the children for the subject selection dialog.
     *
     * @param selectionDialogTreeElement
     *            the expanded tree element
     * @param editingContext
     *            the editing context
     * @param expandedIds
     *            the expanded tree item ids
     * @return the children containing types
     */
    public List<? extends Object> getSubjectSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds, List.of(SysmlPackage.eINSTANCE.getType()));
    }

    /**
     * Provides the root elements for the actor selection dialog.
     *
     * @param editingContext
     *            the editing context
     * @return the root elements containing part usages or part definitions
     */
    public List<Object> getActorSelectionDialogElements(IEditingContext editingContext) {
        return this.getSelectionDialogElements(editingContext, List.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getPartDefinition()));
    }

    /**
     * Provides the children for the actor selection dialog.
     *
     * @param selectionDialogTreeElement
     *            the expanded tree element
     * @param editingContext
     *            the editing context
     * @param expandedIds
     *            the expanded tree item ids
     * @return the children containing part usages or part definitions
     */
    public List<? extends Object> getActorSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds, List.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getPartDefinition()));
    }

    /**
     * Provides the root elements for the objective requirement selection dialog.
     *
     * @param editingContext
     *            the editing context
     * @return the root elements containing requirement usages or definitions
     */
    public List<Object> getObjectiveRequirementSelectionDialogElements(IEditingContext editingContext) {
        return this.getSelectionDialogElements(editingContext, List.of(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementDefinition()));
    }

    /**
     * Provides the children for the objective requirement selection dialog.
     *
     * @param selectionDialogTreeElement
     *            the expanded tree element
     * @param editingContext
     *            the editing context
     * @param expandedIds
     *            the expanded tree item ids
     * @return the children containing requirement usages or definitions
     */
    public List<? extends Object> getObjectiveRequirementSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds,
                List.of(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementDefinition()));
    }

    /**
     * Provides the root elements for the action reference selection dialog.
     *
     * @param editingContext
     *            the editing context
     * @return the root elements containing action usages
     */
    public List<Object> getActionReferenceSelectionDialogElements(IEditingContext editingContext) {
        return this.getSelectionDialogElements(editingContext, List.of(SysmlPackage.eINSTANCE.getActionUsage()));
    }

    /**
     * Provides the children for the action reference selection dialog.
     *
     * @param selectionDialogTreeElement
     *            the expanded tree element
     * @param editingContext
     *            the editing context
     * @param expandedIds
     *            the expanded tree item ids
     * @return the children containing action usages
     */
    public List<? extends Object> getActionReferenceSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds, List.of(SysmlPackage.eINSTANCE.getActionUsage()));
    }

    /**
     * Provides the root elements for the exhibit state selection dialog.
     *
     * @param editingContext
     *            the editing context
     * @return the root elements containing state usages
     */
    public List<Object> getExhibitStateSelectionDialogElements(IEditingContext editingContext) {
        return this.getSelectionDialogElements(editingContext, List.of(SysmlPackage.eINSTANCE.getStateUsage()));
    }

    /**
     * Provides the children for the exhibit state selection dialog.
     *
     * @param selectionDialogTreeElement
     *            the expanded tree element
     * @param editingContext
     *            the editing context
     * @param expandedIds
     *            the expanded tree item ids
     * @return the children containing state usages
     */
    public List<? extends Object> getExhibitStateSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds, List.of(SysmlPackage.eINSTANCE.getStateUsage()));
    }

    /**
     * Provides the root elements for the concern reference selection dialog.
     *
     * @param editingContext
     *            the editing context
     * @return the root elements containing concern usages
     */
    public List<Object> getConcernReferenceSelectionDialogElements(IEditingContext editingContext) {
        return this.getSelectionDialogElements(editingContext, List.of(SysmlPackage.eINSTANCE.getConcernUsage()));
    }

    /**
     * Provides the children for the concern reference selection dialog.
     *
     * @param selectionDialogTreeElement
     *            the expanded tree element
     * @param editingContext
     *            the editing context
     * @param expandedIds
     *            the expanded tree item ids
     * @return the children containing concern usages
     */
    public List<? extends Object> getConcernReferenceSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds) {
        return this.getSelectionDialogChildren(selectionDialogTreeElement, editingContext, expandedIds, List.of(SysmlPackage.eINSTANCE.getConcernUsage()));
    }

    /**
     * Return the {@code Usage} {@link EClass} corresponding to the given {@link Type}.
     *
     * @param type
     *            the type we want the {@code Usage} {@link EClass}
     * @return the {@code Usage} {@link EClass} corresponding to the given {@link Type}
     */
    public EClass getPortionKindSelectionDialogElement(Type type) {
        return switch (type) {
            case AllocationDefinition a -> SysmlPackage.eINSTANCE.getAllocationUsage();
            case ConnectionDefinition c -> SysmlPackage.eINSTANCE.getConnectionUsage();
            case PartDefinition p -> SysmlPackage.eINSTANCE.getPartUsage();
            case ConstraintDefinition c -> SysmlPackage.eINSTANCE.getConstraintUsage();
            case ItemDefinition i -> SysmlPackage.eINSTANCE.getItemUsage();
            case Usage u -> u.eClass();
            default -> SysmlPackage.eINSTANCE.getOccurrenceUsage();
        };
    }

    /**
     * Provides the root elements for a selection dialog using the given candidates.
     *
     * @param editingContext
     *            the editing context
     * @param candidates
     *            the accepted candidate classifiers
     * @return the root elements containing candidate instances
     */
    public List<Object> getSelectionDialogElements(IEditingContext editingContext, List<EClassifier> candidates) {
        var elementsContainingClassifiers = new ArrayList<>();
        List<Object> elements = this.sysONExplorerService.getElements(editingContext, List.of());
        for (Object rootElement : elements) {
            if (rootElement instanceof Resource resource && this.containsDirectlyOrIndirectlyInstancesOf(resource, candidates)) {
                elementsContainingClassifiers.add(resource);
            } else if (rootElement instanceof ISysONExplorerFragment fragment) {
                elementsContainingClassifiers.add(fragment);
            }
        }

        return elementsContainingClassifiers.stream()
                .sorted(Comparator.comparingInt(this::getSelectionDialogRootSortRank)
                        .thenComparing(this::getElementName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    /**
     * Provides the children for a selection dialog using the given candidates.
     *
     * @param selectionDialogTreeElement
     *            the expanded tree element
     * @param editingContext
     *            the editing context
     * @param expandedIds
     *            the expanded tree item ids
     * @param candidates
     *            the accepted candidate classifiers
     * @return the children containing candidate instances
     */
    public List<? extends Object> getSelectionDialogChildren(Object selectionDialogTreeElement, IEditingContext editingContext, List<String> expandedIds, List<EClassifier> candidates) {
        final List<? extends Object> result;

        if (selectionDialogTreeElement instanceof Resource resource) {
            result = resource.getContents().stream()
                    .filter(content -> candidates.stream().anyMatch(eClassifier -> eClassifier.isInstance(content)) || this.containsDirectlyOrIndirectlyInstancesOf(content, candidates))
                    .toList();
        } else if (selectionDialogTreeElement instanceof Element sysmlElement) {
            result = sysmlElement.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .map(Membership::getOwnedRelatedElement).flatMap(List::stream)
                    .filter(content -> candidates.stream().anyMatch(eClassifier -> eClassifier.isInstance(content)) || this.containsDirectlyOrIndirectlyInstancesOf(content, candidates))
                    .toList();
        } else if (selectionDialogTreeElement instanceof ISysONExplorerFragment fragment) {
            result = fragment.getChildren(editingContext, List.of(), expandedIds, List.of()).stream().filter(child -> {
                if (child instanceof Resource childResource && !this.containsDirectlyOrIndirectlyInstancesOf(childResource, candidates)) {
                    return false;
                }
                return true;
            }).toList();
        } else {
            result = new ArrayList<>();
        }

        return result;
    }

    private boolean containsDirectlyOrIndirectlyInstancesOf(Resource resource, List<EClassifier> eClassifiers) {
        boolean found = false;
        final Iterator<EObject> allContents = resource.getAllContents();
        while (!found && allContents.hasNext()) {
            var eObject = allContents.next();
            for (EClassifier it : eClassifiers) {
                found = it.isInstance(eObject);
                if (found) {
                    break;
                }
            }
        }
        return found;
    }

    private boolean containsDirectlyOrIndirectlyInstancesOf(EObject eObject, List<EClassifier> eClassifiers) {
        boolean found = false;
        final Iterator<EObject> allContents = eObject.eAllContents();
        while (!found && allContents.hasNext()) {
            var content = allContents.next();
            for (EClassifier it : eClassifiers) {
                found = it.isInstance(content);
                if (found) {
                    break;
                }
            }
        }
        return found;
    }

    private String getResourceName(Resource resource) {
        return resource.eAdapters().stream()
                .filter(ResourceMetadataAdapter.class::isInstance)
                .map(ResourceMetadataAdapter.class::cast)
                .findFirst()
                .map(ResourceMetadataAdapter::getName)
                .orElse(resource.getURI().lastSegment());
    }

    private String getElementName(Object element) {
        String elementName = "";
        if (element instanceof Resource resource) {
            elementName = this.getResourceName(resource);
        } else if (element instanceof ISysONExplorerFragment fragment) {
            elementName = fragment.getLabel();
        }
        return elementName;
    }

    private int getSelectionDialogRootSortRank(Object element) {
        int rank = Integer.MAX_VALUE;
        if (element instanceof Resource) {
            rank = 0;
        } else if (element instanceof ISysONExplorerFragment) {
            rank = 1;
        }
        return rank;
    }
}
