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
package org.eclipse.syson.tree.explorer.view.menu.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.collaborative.trees.api.ITreeItemContextMenuEntryProvider;
import org.eclipse.sirius.components.collaborative.trees.dto.ITreeItemContextMenuEntry;
import org.eclipse.sirius.components.collaborative.trees.dto.SingleClickTreeItemContextMenuEntry;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerDescriptionProvider;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerTreeItemContextMenuEntryProvider;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

/**
 * Customization of {@link ExplorerTreeItemContextMenuEntryProvider} for SysON to provide the contextual menu entries in
 * the 'Explorer' view.
 *
 * @author flatombe
 */
@Service
public class SysONExplorerTreeItemContextMenuEntryProvider implements ITreeItemContextMenuEntryProvider {

    public static final String NEW_OBJECTS_FROM_TEXT_MENU_ENTRY_CONTRIBUTION_ID = "newObjectsFromText";

    private final IObjectSearchService objectSearchService;

    private final ILibrarySearchService librarySearchService;

    private final ISemanticDataSearchService semanticDataSearchService;

    private final IRepresentationMetadataSearchService representationMetadataSearchService;

    private final SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider;

    private final ISysONExplorerService sysonExplorerService;

    public SysONExplorerTreeItemContextMenuEntryProvider(IObjectSearchService objectSearchService, ILibrarySearchService librarySearchService, ISemanticDataSearchService semanticDataSearchService,
            IRepresentationMetadataSearchService representationMetadataSearchService,
            SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider, ISysONExplorerService sysonExplorerService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.librarySearchService = Objects.requireNonNull(librarySearchService);
        this.semanticDataSearchService = Objects.requireNonNull(semanticDataSearchService);
        this.representationMetadataSearchService = Objects.requireNonNull(representationMetadataSearchService);
        this.sysONTreeViewDescriptionProvider = Objects.requireNonNull(sysONTreeViewDescriptionProvider);
        this.sysonExplorerService = Objects.requireNonNull(sysonExplorerService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, TreeDescription treeDescription, Tree tree, TreeItem treeItem) {
        return tree.getId().startsWith(ExplorerDescriptionProvider.PREFIX)
                && Objects.equals(tree.getDescriptionId(), this.sysONTreeViewDescriptionProvider.getDescriptionId());
    }

    @Override
    public List<ITreeItemContextMenuEntry> getTreeItemContextMenuEntries(IEditingContext editingContext, TreeDescription treeDescription, Tree tree, TreeItem treeItem) {
        List<ITreeItemContextMenuEntry> result = new ArrayList<>();
        if (editingContext instanceof IEMFEditingContext emfEditingContext) {
            result.addAll(this.getDocumentContextMenuEntries(emfEditingContext, treeItem));
            result.addAll(this.getObjectContextMenuEntries(emfEditingContext, treeItem));
            result.addAll(this.getRepresentationContextMenuEntries(emfEditingContext, treeItem));
            result.addAll(this.getLibraryRelatedEntries(emfEditingContext, treeItem));
        }
        if (this.sysonExplorerService.canExpandAll(treeItem, editingContext)) {
            result.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.EXPAND_ALL, "", List.of(), false));
        }
        return result;
    }

    private List<ITreeItemContextMenuEntry> getDocumentContextMenuEntries(IEMFEditingContext editingContext, TreeItem treeItem) {
        var optionalResource = this.objectSearchService.getObject(editingContext, treeItem.getId())
                .filter(Resource.class::isInstance)
                .map(Resource.class::cast);
        if (optionalResource.isPresent()) {
            var resource = optionalResource.get();

            List<ITreeItemContextMenuEntry> entries = new ArrayList<>();
            if (this.sysonExplorerService.isEditable(resource)) {
                entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.NEW_ROOT_OBJECT, "", List.of(), false));
            }
            entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.DOWNLOAD_DOCUMENT, "", List.of(), false));
            return entries;
        }
        return List.of();
    }

    private List<ITreeItemContextMenuEntry> getObjectContextMenuEntries(IEMFEditingContext editingContext, TreeItem treeItem) {
        var optionalEObject = this.objectSearchService.getObject(editingContext, treeItem.getId())
                .filter(EObject.class::isInstance)
                .map(EObject.class::cast);
        if (optionalEObject.isPresent()) {
            var object = optionalEObject.get();
            if (this.sysonExplorerService.isEditable(object)) {
                List<ITreeItemContextMenuEntry> entries = new ArrayList<>();
                entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.NEW_OBJECT, "", List.of(), false));
                if (object instanceof ViewUsage) {
                    Optional<UUID> semanticDataId = new UUIDParser().parse(editingContext.getId());
                    var sysonRepresentationAlreadyExists = this.representationMetadataSearchService
                            .findAllRepresentationMetadataBySemanticDataAndTargetObjectId(AggregateReference.to(semanticDataId.get()), treeItem.getId()).stream()
                            .anyMatch(rm -> SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID.equals(rm.getDescriptionId())
                                    || SysONRepresentationDescriptionIdentifiers.REQUIREMENTS_TABLE_VIEW_DESCRIPTION_ID.equals(rm.getDescriptionId()));
                    if (!sysonRepresentationAlreadyExists) {
                        entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.NEW_REPRESENTATION, "", List.of(), false));
                    }
                } else {
                    entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.NEW_REPRESENTATION, "", List.of(), false));
                }

                entries.add(new SingleClickTreeItemContextMenuEntry(NEW_OBJECTS_FROM_TEXT_MENU_ENTRY_CONTRIBUTION_ID, "", List.of(), false));

                if (object instanceof Element && !(object instanceof Relationship)) {
                    entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.DUPLICATE_OBJECT, "", List.of(), false));
                }
                return entries;
            }
        }
        return List.of();
    }

    private List<ITreeItemContextMenuEntry> getRepresentationContextMenuEntries(IEMFEditingContext editingContext, TreeItem treeItem) {
        var optionalRepresentationMetadata = this.objectSearchService.getObject(editingContext, treeItem.getId())
                .filter(RepresentationMetadata.class::isInstance)
                .map(RepresentationMetadata.class::cast);
        if (optionalRepresentationMetadata.isPresent()) {
            RepresentationMetadata representationMetadata = optionalRepresentationMetadata.get();
            if (!SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID.equals(representationMetadata.getDescriptionId())
                    && !SysONRepresentationDescriptionIdentifiers.REQUIREMENTS_TABLE_VIEW_DESCRIPTION_ID.equals(representationMetadata.getDescriptionId())) {
                return List.of(
                        new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.DUPLICATE_REPRESENTATION, "", List.of(), false));
            }
        }
        return List.of();
    }

    private List<ITreeItemContextMenuEntry> getLibraryRelatedEntries(IEMFEditingContext editingContext, TreeItem treeItem) {
        List<ITreeItemContextMenuEntry> result = new ArrayList<>();
        var optionalNotifier = this.objectSearchService.getObject(editingContext, treeItem.getId())
                .filter(Notifier.class::isInstance)
                .map(Notifier.class::cast);

        if (optionalNotifier.isEmpty()) {
            optionalNotifier = editingContext.getDomain().getResourceSet().getResources().stream()
                    .filter(resource -> resource.getURI().toString().contains(treeItem.getId()))
                    .map(Notifier.class::cast)
                    .findFirst();
        }

        var optionalLibraryMetadataAdapter = optionalNotifier.stream()
                .map(Notifier::eAdapters)
                .flatMap(Collection::stream)
                .filter(LibraryMetadataAdapter.class::isInstance)
                .map(LibraryMetadataAdapter.class::cast)
                .findFirst();

        if (optionalLibraryMetadataAdapter.isPresent()) {
            var libraryMetadataAdapter = optionalLibraryMetadataAdapter.get();
            if (this.isDirectDependency(editingContext, libraryMetadataAdapter) && !this.sysonExplorerService.isEditable(optionalNotifier.get())) {
                // We do not support the update or removal of a transitive dependency for the moment.
                result.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.UPDATE_LIBRARY, "", List.of(), true));
                result.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.REMOVE_LIBRARY, "Remove library", List.of("/icons/remove_library.svg"), true));
            }
        }
        return result;
    }

    private boolean isDirectDependency(IEMFEditingContext emfEditingContext, LibraryMetadataAdapter libraryMetadataAdapter) {
        var editingContextDependencies = new UUIDParser().parse(emfEditingContext.getId())
                .map(this.semanticDataSearchService::findAllDependenciesIdById)
                .stream()
                .flatMap(Collection::stream)
                .map(AggregateReference::getId)
                .toList();
        return this.librarySearchService.findByNamespaceAndNameAndVersion(libraryMetadataAdapter.getNamespace(), libraryMetadataAdapter.getName(), libraryMetadataAdapter.getVersion())
                .map(Library::getSemanticData)
                .map(AggregateReference::getId)
                .map(editingContextDependencies::contains)
                .orElse(false);
    }
}
