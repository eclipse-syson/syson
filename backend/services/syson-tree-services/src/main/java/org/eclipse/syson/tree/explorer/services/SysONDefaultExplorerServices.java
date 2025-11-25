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
package org.eclipse.syson.tree.explorer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IContentService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerServices;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.tree.explorer.fragments.KerMLStandardLibraryDirectory;
import org.eclipse.syson.tree.explorer.fragments.LibrariesDirectory;
import org.eclipse.syson.tree.explorer.fragments.SysMLStandardLibraryDirectory;
import org.eclipse.syson.tree.explorer.fragments.UserLibrariesDirectory;
import org.eclipse.syson.tree.explorer.services.api.ISysONDefaultExplorerService;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFilterService;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFragment;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
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

    private final ILabelService labelService;

    private final ISysONExplorerFilterService filterService;

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    public SysONDefaultExplorerServices(IIdentityService identityService, IContentService contentService, IRepresentationMetadataSearchService representationMetadataSearchService,
            IExplorerServices explorerServices, ILabelService labelService, ISysONExplorerFilterService filterService, final IReadOnlyObjectPredicate readOnlyObjectPredicate) {
        this.identityService = Objects.requireNonNull(identityService);
        this.contentService = Objects.requireNonNull(contentService);
        this.representationMetadataSearchService = Objects.requireNonNull(representationMetadataSearchService);
        this.explorerServices = Objects.requireNonNull(explorerServices);
        this.labelService = Objects.requireNonNull(labelService);
        this.filterService = Objects.requireNonNull(filterService);
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
    }

    @Override
    public List<Object> getElements(IEditingContext editingContext, List<String> activeFilterIds) {
        List<Object> results = new ArrayList<>();
        if (editingContext instanceof EditingContext siriusWebContext) {
            siriusWebContext.getDomain().getResourceSet().getResources().stream()
                    .filter(r -> !this.filterService.isSysMLStandardLibrary(r))
                    .filter(r -> !this.filterService.isKerMLStandardLibrary(r))
                    .filter(r -> !this.filterService.isUserLibrary(editingContext, r))
                    .forEach(results::add);
            LibrariesDirectory librariesDirectory = new LibrariesDirectory("Libraries", editingContext, this.filterService);
            if (librariesDirectory.hasChildren(editingContext, List.of(), List.of(), activeFilterIds)) {
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
        final String result;
        if (self instanceof ISysONExplorerFragment fragment) {
            result = fragment.getKind();
        } else {
            result = this.explorerServices.getKind(self);
        }
        return result;
    }

    @Override
    public String getLabel(Object self) {
        String label = "";
        if (self instanceof ISysONExplorerFragment fragment) {
            label = fragment.getLabel();
        } else if (self instanceof Type type) {
            String name = type.getName();
            if (name != null) {
                label = name;
            }
        } else {
            label = this.getFallbackLabel(self);
        }
        return label;
    }

    private String getFallbackLabel(Object self) {
        StyledString styledLabel = this.labelService.getStyledLabel(self);
        if (styledLabel != null) {
            return styledLabel.toString();
        }
        return "";
    }

    @Override
    public List<String> getImageURL(Object self) {
        List<String> result = List.of();
        if (self instanceof ISysONExplorerFragment fragment) {
            result = fragment.getIconURL();
        } else {
            result = this.labelService.getImagePaths(self);
        }
        return result;
    }

    @Override
    public boolean hasChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
        boolean hasChildren = false;
        if (self instanceof ISysONExplorerFragment fragment) {
            hasChildren = fragment.hasChildren(editingContext, existingRepresentations, expandedIds, activeFilterIds);
        } else if (self instanceof Resource resource) {
            hasChildren = !this.filterService.applyFilters(editingContext, resource.getContents(), activeFilterIds).isEmpty();
        } else if (self instanceof Element element) {
            List<Object> contents = this.filterService.applyFilters(editingContext, this.contentService.getContents(self), activeFilterIds);
            hasChildren = !contents.isEmpty() && contents.stream().anyMatch(e -> !(e instanceof EAnnotation))
                    || this.hasRepresentation(element, editingContext);
        } else {
            hasChildren = this.explorerServices.hasChildren(self, editingContext, existingRepresentations);
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
    public List<Object> getChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
        List<Object> result = new ArrayList<>();
        String id = this.getTreeItemId(self);
        if (self instanceof ISysONExplorerFragment fragment) {
            if (expandedIds.contains(id)) {
                result.addAll(fragment.getChildren(editingContext, existingRepresentations, expandedIds, activeFilterIds));
            }
        } else {
            result.addAll(this.explorerServices.getDefaultChildren(self, editingContext, expandedIds, existingRepresentations));
        }

        result = this.filterService.applyFilters(editingContext, result, activeFilterIds);

        // Remove annotations: they aren't part of the SysML standard and shouldn't be visible to the user.
        return result.stream()
                .filter(element -> !(element instanceof EAnnotation))
                .toList();
    }

    @Override
    public boolean canExpandAll(TreeItem treeItem, IEditingContext editingContext) {
        List<String> nonExpandableDirectories = List.of(KerMLStandardLibraryDirectory.class, LibrariesDirectory.class, SysMLStandardLibraryDirectory.class, UserLibrariesDirectory.class)
                .stream()
                .map(Class::getSimpleName)
                .toList();
        return treeItem.isHasChildren() && !nonExpandableDirectories.contains(treeItem.getKind());
    }

    @Override
    public boolean canCreateNewObjectsFromText(Object self) {
        return self instanceof Element && this.isEditable(self);
    }

    @Override
    public Object getParent(Object self, String treeItemId, IEditingContext editingContext) {
        final Object result;
        if (self instanceof ISysONExplorerFragment fragment) {
            result = fragment.getParent();
        } else {
            result = this.explorerServices.getParent(self, treeItemId, editingContext);
        }
        return result;
    }

    @Override
    public Object getTreeItemObject(String treeItemId, IEditingContext editingContext) {
        final Object result;
        Optional<Resource> optionalResource = Optional.ofNullable(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .flatMap(emfEditingContext -> {
                    return emfEditingContext.getDomain().getResourceSet().getResources().stream()
                            .filter(resource -> resource.getURI().toString().contains(treeItemId))
                            .findFirst();
                });
        if (optionalResource.isPresent()) {
            result = optionalResource.get();
        } else {
            result = this.explorerServices.getTreeItemObject(treeItemId, editingContext);
        }
        return result;
    }

    @Override
    public boolean isEditable(Object self) {
        boolean result = true;
        if (self instanceof ISysONExplorerFragment fragment) {
            result = fragment.isEditable();
        } else if (self instanceof Element element) {
            result = !this.readOnlyObjectPredicate.test(element);
        } else if (self instanceof Resource resource) {
            result = !this.readOnlyObjectPredicate.test(resource);
        }
        return result;
    }

    @Override
    public boolean isDeletable(Object self) {
        boolean result = true;
        if (self instanceof ISysONExplorerFragment fragment) {
            result = fragment.isDeletable();
        } else if (self instanceof Element element) {
            result = !this.readOnlyObjectPredicate.test(element);
        } else if (self instanceof Resource resource) {
            // Allow to delete read-only resources imported from textual SysML, users may want to remove an imported
            // library from their project.
            result = !this.readOnlyObjectPredicate.test(resource) || ElementUtil.isImported(resource);
        } else if (self instanceof RepresentationMetadata representationMetadata) {
            // If it is a standard diagram or a requirements-table, it has been created on a ViewUsage.
            // In such cases, we don't want the Delete menu.
            // Users will delete the ViewUsage to also delete the standard diagram or the requirements-table.
            return !SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID.equals(representationMetadata.getDescriptionId())
                    && !SysONRepresentationDescriptionIdentifiers.REQUIREMENTS_TABLE_VIEW_DESCRIPTION_ID.equals(representationMetadata.getDescriptionId());
        }
        return result;
    }

    @Override
    public boolean isSelectable(Object self) {
        return true;
    }

    @Override
    public String getType(Object self) {
        StringBuilder type = new StringBuilder();
        if (self instanceof ViewUsage viewUsage) {
            var viewDefinition = viewUsage.getViewDefinition();
            if (viewDefinition != null) {
                type.append(" [");
                type.append(viewDefinition.getDeclaredName());
                type.append("]");
            }
        }
        return type.toString();
    }

    @Override
    public String getShortName(Object self) {
        if (self instanceof Element element) {
            String shortName = element.getShortName();
            if (shortName != null && !shortName.isBlank()) {
                return "<" + shortName + "> ";
            }
        }
        return "";
    }

    @Override
    public String getReadOnlyTag(Object self) {
        String result = "";
        if (self instanceof Resource resource) {
            if (this.readOnlyObjectPredicate.test(resource) && !ElementUtil.isStandardLibraryResource(resource)) {
                result = " [read-only]";
            }
        } else if (self instanceof KerMLStandardLibraryDirectory || self instanceof SysMLStandardLibraryDirectory) {
            result = " [read-only]";
        }
        return result;
    }

    @Override
    public String getLibraryLabel(Object self) {
        String result = "";
        if (self instanceof Resource resource) {
            result = resource.eAdapters().stream()
                    .filter(LibraryMetadataAdapter.class::isInstance)
                    .map(LibraryMetadataAdapter.class::cast)
                    .findFirst()
                    .map(libraryMetadataAdapter -> " - " + libraryMetadataAdapter.getName() + "@" + libraryMetadataAdapter.getVersion())
                    .orElse("");
        }
        return result;
    }
}
