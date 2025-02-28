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
package org.eclipse.syson.tree.explorer.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IContentService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerServices;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.tree.explorer.view.fragments.LibrariesDirectory;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONDefaultExplorerService;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFilterService;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFragment;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link ISysONDefaultExplorerService}.
 *
 * @author gdaniel
 */
@Service
public class SysONDefaultExplorerServices implements ISysONDefaultExplorerService {

    private final IIdentityService identityService;

    private final IContentService contentService;

    private final IRepresentationMetadataSearchService representationMetadataSearchService;

    private final IExplorerServices explorerServices;

    private final ISysONExplorerFilterService filterService;

    private final UtilService utilService = new UtilService();

    public SysONDefaultExplorerServices(IIdentityService identityService, IContentService contentService, IRepresentationMetadataSearchService representationMetadataSearchService, IExplorerServices explorerServices,
            ISysONExplorerFilterService filterService) {
        this.identityService = Objects.requireNonNull(identityService);
        this.contentService = Objects.requireNonNull(contentService);
        this.representationMetadataSearchService = Objects.requireNonNull(representationMetadataSearchService);
        this.explorerServices = Objects.requireNonNull(explorerServices);
        this.filterService = Objects.requireNonNull(filterService);
    }

    @Override
    public List<Object> getElements(IEditingContext editingContext, List<String> activeFilterIds) {
        List<Object> results = new ArrayList<>();
        if (editingContext instanceof EditingContext siriusWebContext) {
            siriusWebContext.getDomain().getResourceSet().getResources().stream()
                    .filter(r -> !this.filterService.isSysMLStandardLibrary(r))
                    .filter(r -> !this.filterService.isKerMLStandardLibrary(r))
                    .filter(r -> !ElementUtil.isImported(r) || this.utilService.getLibraries(r, false).isEmpty())
                    .forEach(results::add);
            LibrariesDirectory librariesDirectory = new LibrariesDirectory("Libraries", this.filterService);
            if (librariesDirectory.hasChildren(editingContext, List.of(), activeFilterIds)) {
                // Do not display the libraries directory if is has no children.
                results.add(librariesDirectory);
            }
        }
        return results;
    }

    @Override
    public String getTreeItemId(Object self) {
        String id = null;
        if (self instanceof ISysONExplorerFragment fragment) {
            id = fragment.getId();
        } else {
            id = this.explorerServices.getTreeItemId(self);
        }
        return id;
    }

    @Override
    public String getKind(Object self) {
        return this.explorerServices.getKind(self);
    }

    @Override
    public String getLabel(Object self) {
        String label = "";
        if (self instanceof ISysONExplorerFragment fragment) {
            label = fragment.getLabel();
        } else {
            label = this.explorerServices.getLabel(self);
        }
        return label;
    }

    @Override
    public List<String> getImageURL(Object self) {
        List<String> result = List.of();
        if (self instanceof ISysONExplorerFragment fragment) {
            result = fragment.getIconURL();
        } else {
            result = this.explorerServices.getImageURL(self);
        }
        return result;
    }

    @Override
    public boolean hasChildren(Object self, IEditingContext editingContext, List<String> expandedIds, List<String> activeFilterIds) {
        boolean hasChildren = false;
        if (self instanceof ISysONExplorerFragment fragment) {
            hasChildren = fragment.hasChildren(editingContext, expandedIds, activeFilterIds);
        } else if (self instanceof Resource resource) {
            hasChildren = !this.filterService.applyFilters(resource.getContents(), activeFilterIds).isEmpty();
        } else if (self instanceof Element element) {
            List<Object> contents = this.filterService.applyFilters(this.contentService.getContents(self), activeFilterIds);
            hasChildren = !contents.isEmpty() && contents.stream().anyMatch(e -> !(e instanceof EAnnotation))
                || this.hasRepresentation(element, editingContext);
        }
        return hasChildren;
    }

    private boolean hasRepresentation(EObject self, IEditingContext editingContext) {
        var optionalSemanticDataId = new UUIDParser().parse(editingContext.getId());
        if (optionalSemanticDataId.isPresent()) {
            String id = this.identityService.getId(self);
            return this.representationMetadataSearchService.existAnyRepresentationMetadataForSemanticDataAndTargetObjectId(AggregateReference.to(optionalSemanticDataId.get()), id);
        }
        return false;
    }

    @Override
    public List<Object> getChildren(Object self, IEditingContext editingContext, List<String> expandedIds, List<String> activeFilterIds) {
        List<Object> result = new ArrayList<>();
        String id = this.getTreeItemId(self);
        if (self instanceof ISysONExplorerFragment fragment) {
            if (expandedIds.contains(id)) {
                result.addAll(fragment.getChildren(editingContext, expandedIds, activeFilterIds));
            }
        } else {
            result.addAll(this.explorerServices.getDefaultChildren(self, editingContext, expandedIds));
        }

        result = this.filterService.applyFilters(result, activeFilterIds);

        // Remove annotations: they aren't part of the SysML standard and shouldn't be visible to the user.
        return result.stream()
                .filter(element -> !(element instanceof EAnnotation))
                .toList();
    }

    @Override
    public Object getParent(Object self, String treeItemId, IEditingContext editingContext) {
        return this.explorerServices.getParent(self, treeItemId, editingContext);
    }

    @Override
    public Object getTreeItemObject(String treeItemId, IEditingContext editingContext) {
        return this.explorerServices.getTreeItemObject(treeItemId, editingContext);
    }

    @Override
    public boolean isEditable(Object self) {
        boolean result = true;
        if (self instanceof ISysONExplorerFragment fragment) {
            result = fragment.isEditable();
        } else if (self instanceof Namespace namespace) {
            if (this.utilService.isRootNamespace(namespace)) {
                result = !(this.filterService.isUserLibrary(namespace.eResource()))
                        && namespace.getOwnedElement().stream().noneMatch(ownedElement -> ElementUtil.isFromStandardLibrary(ownedElement));
            } else {
                result = !ElementUtil.isFromStandardLibrary(namespace)
                        && !(this.filterService.isUserLibrary(namespace.eResource()));
            }
        } else if (self instanceof Element element) {
            result = !ElementUtil.isFromStandardLibrary(element)
                    && !(this.filterService.isUserLibrary(element.eResource()));
        } else if (self instanceof Resource resource) {
            result = !(this.filterService.isUserLibrary(resource))
                    && resource.getContents().stream()
                            .filter(Namespace.class::isInstance)
                            .map(Namespace.class::cast)
                            .flatMap(namespace -> namespace.getOwnedElement().stream())
                            .noneMatch(ElementUtil::isFromStandardLibrary);
        }
        return result;
    }

    @Override
    public boolean isDeletable(Object self) {
        boolean result = true;
        if (self instanceof ISysONExplorerFragment fragment) {
            result = fragment.isEditable();
        } else if (self instanceof Namespace namespace) {
            if (this.utilService.isRootNamespace(namespace)) {
                result = !(this.filterService.isUserLibrary(namespace.eResource()))
                        && namespace.getOwnedElement().stream().noneMatch(ownedElement -> ElementUtil.isFromStandardLibrary(ownedElement));
            } else {
                result = !ElementUtil.isFromStandardLibrary(namespace)
                        && !(this.filterService.isUserLibrary(namespace.eResource()));
            }
        } else if (self instanceof Element element) {
            result = !ElementUtil.isFromStandardLibrary(element)
                    && !(this.filterService.isUserLibrary(element.eResource()));
        } else if (self instanceof Resource resource) {
            // Allow to delete resources containing user libraries, users may want to remove an imported library from
            // their project.
            result = resource.getContents().stream()
                    .filter(Namespace.class::isInstance)
                    .map(Namespace.class::cast)
                    .flatMap(namespace -> namespace.getOwnedElement().stream())
                    .noneMatch(ElementUtil::isFromStandardLibrary);
        }
        return result;
    }

    @Override
    public boolean isSelectable(Object self) {
        return true;
    }
}
