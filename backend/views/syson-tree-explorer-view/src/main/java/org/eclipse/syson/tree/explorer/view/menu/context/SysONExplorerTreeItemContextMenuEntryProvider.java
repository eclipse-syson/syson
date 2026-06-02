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
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
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
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
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

    public static final String CREATE_EXPRESSION_MENU_ENTRY_CONTRIBUTION_ID = "createExpression";

    public static final String EDIT_EXPRESSION_MENU_ENTRY_CONTRIBUTION_ID = "editExpression";

    public static final String DELETE_EXPRESSION_MENU_ENTRY_CONTRIBUTION_ID = "deleteExpression";

    private final IObjectSearchService objectSearchService;

    private final ILibrarySearchService librarySearchService;

    private final ISemanticDataSearchService semanticDataSearchService;

    private final IRepresentationMetadataSearchService representationMetadataSearchService;

    private final SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider;

    private final ISysONExplorerService sysonExplorerService;

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    private final MetamodelQueryElementService metamodelQueryElementService;

    public SysONExplorerTreeItemContextMenuEntryProvider(IObjectSearchService objectSearchService, ILibrarySearchService librarySearchService, ISemanticDataSearchService semanticDataSearchService,
            IRepresentationMetadataSearchService representationMetadataSearchService,
            SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider, ISysONExplorerService sysonExplorerService, IReadOnlyObjectPredicate readOnlyObjectPredicate) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.librarySearchService = Objects.requireNonNull(librarySearchService);
        this.semanticDataSearchService = Objects.requireNonNull(semanticDataSearchService);
        this.representationMetadataSearchService = Objects.requireNonNull(representationMetadataSearchService);
        this.sysONTreeViewDescriptionProvider = Objects.requireNonNull(sysONTreeViewDescriptionProvider);
        this.sysonExplorerService = Objects.requireNonNull(sysonExplorerService);
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
        this.metamodelQueryElementService = new MetamodelQueryElementService();
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
            result.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.EXPAND_ALL, "", List.of(), false, List.of()));
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
                entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.NEW_ROOT_OBJECT, "", List.of(), false, List.of()));
            }
            entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.DOWNLOAD_DOCUMENT, "", List.of(), false, List.of()));
            return entries;
        }
        return List.of();
    }

    private List<ITreeItemContextMenuEntry> getObjectContextMenuEntries(IEMFEditingContext editingContext, TreeItem treeItem) {
        List<ITreeItemContextMenuEntry> entries = new ArrayList<>();
        var optionalEObject = this.objectSearchService.getObject(editingContext, treeItem.getId())
                .filter(EObject.class::isInstance)
                .map(EObject.class::cast);
        if (optionalEObject.isPresent()) {
            var object = optionalEObject.get();
            if (this.sysonExplorerService.isEditable(object)) {
                entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.NEW_OBJECT, "", List.of(), false, List.of()));
                if (this.canHaveNewRepresentation(editingContext, treeItem.getId(), object)) {
                    entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.NEW_REPRESENTATION, "", List.of(), false, List.of()));
                }
                entries.add(new SingleClickTreeItemContextMenuEntry(NEW_OBJECTS_FROM_TEXT_MENU_ENTRY_CONTRIBUTION_ID, "", List.of(), false, List.of()));
                if (this.canHaveNewExpression(editingContext, treeItem.getId(), object)) {
                    entries.add(new SingleClickTreeItemContextMenuEntry(CREATE_EXPRESSION_MENU_ENTRY_CONTRIBUTION_ID, "", List.of(), false, List.of()));
                }

                if (object instanceof Element && !(object instanceof Relationship)) {
                    entries.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.DUPLICATE_OBJECT, "", List.of(), false, List.of()));
                }
            }
            if (!this.readOnlyObjectPredicate.test(object) && object instanceof Element element) {
                this.addExpressionEditionEntries(entries, element);
            }
        }
        return entries;
    }

    private boolean canHaveNewRepresentation(IEMFEditingContext editingContext, String treeItemId, EObject object) {
        boolean result = true;
        if (object instanceof ViewUsage) {
            Optional<UUID> semanticDataId = new UUIDParser().parse(editingContext.getId());
            var sysonRepresentationAlreadyExists = this.representationMetadataSearchService
                    .findAllRepresentationMetadataBySemanticDataAndTargetObjectId(AggregateReference.to(semanticDataId.get()), treeItemId).stream()
                    .anyMatch(rm -> SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID.equals(rm.getDescriptionId())
                            || SysONRepresentationDescriptionIdentifiers.REQUIREMENTS_TABLE_VIEW_DESCRIPTION_ID.equals(rm.getDescriptionId()));
            if (sysonRepresentationAlreadyExists) {
                result = false;
            }
        }
        return result;
    }

    private boolean canHaveNewExpression(IEMFEditingContext editingContext, String treeItemId, EObject object) {
        return !this.readOnlyObjectPredicate.test(object) && object instanceof Element element && this.metamodelQueryElementService.canContainExpressionDefinition(element)
                && !this.metamodelQueryElementService.hasSingleExpressionDefinition(element);
    }

    private void addExpressionEditionEntries(List<ITreeItemContextMenuEntry> entries, Element element) {
        var expressionEntries = new ArrayList<String>();

        // "Edit expression" on the root Expression element itself; the normal "Delete" operation works on it so no need
        // to also add "Delete expression"
        if (this.metamodelQueryElementService.isTopLevelExpression(element)) {
            expressionEntries.add(EDIT_EXPRESSION_MENU_ENTRY_CONTRIBUTION_ID);
        } else if (this.metamodelQueryElementService.hasSingleExpressionDefinition(element)
                && !this.metamodelQueryElementService.hasSingleExpressionDefinition(element.getOwner())) {
            // "Edit expression" and "Delete expression" on the owner of a root Expression element
            expressionEntries.add(EDIT_EXPRESSION_MENU_ENTRY_CONTRIBUTION_ID);
            expressionEntries.add(DELETE_EXPRESSION_MENU_ENTRY_CONTRIBUTION_ID);
        }

        expressionEntries.forEach(id -> entries.add(new SingleClickTreeItemContextMenuEntry(id, "", List.of(), false, List.of())));
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
                        new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.DUPLICATE_REPRESENTATION, "", List.of(), false, List.of()));
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
                result.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.UPDATE_LIBRARY, "", List.of(), true, List.of()));
                result.add(new SingleClickTreeItemContextMenuEntry(ExplorerTreeItemContextMenuEntryProvider.REMOVE_LIBRARY, "Remove library", List.of("/icons/remove_library.svg"), true, List.of()));
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
