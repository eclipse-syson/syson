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
package org.eclipse.syson.tree.explorer.view.duplicate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.sirius.components.core.CoreImageConstants;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextRepresentationDescriptionProvider;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.components.core.api.IURLParser;
import org.eclipse.sirius.components.core.api.SemanticKindConstants;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.GetOrCreateRandomIdProvider;
import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.sirius.components.trees.renderer.TreeRenderer;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerDescriptionProvider;
import org.eclipse.sirius.web.application.views.explorer.services.api.IContainmentFeatureProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.springframework.stereotype.Service;

/**
 * Provides a tree description to be used in the duplicate object action to select a valid container.
 *
 * @author Arthur Daussy
 */
@Service
public class SysONDuplicateTargetBrowserTreeDescriptionProvider implements IEditingContextRepresentationDescriptionProvider {

    public static final String SYSON_DUPLICATE_TARGET_BROWSER_TREE_REPRESENTATION_ID = "syson-duplicate-target-browser-tree-representation";

    private static final String MODEL_BROWSER_CONTAINER_PREFIX = "syson-modelBrowser://container";

    private final IIdentityService identityService;

    private final IURLParser urlParser;

    private final ILabelService labelService;

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    private final IObjectSearchService objectSearchService;

    private final IContainmentFeatureProvider containmentFeatureProvider;

    public SysONDuplicateTargetBrowserTreeDescriptionProvider(IIdentityService identityService, IURLParser urlParser, ILabelService labelService, IReadOnlyObjectPredicate readOnlyObjectPredicate,
            IObjectSearchService objectSearchService, IContainmentFeatureProvider containmentFeatureProvider) {
        this.identityService = Objects.requireNonNull(identityService);
        this.urlParser = Objects.requireNonNull(urlParser);
        this.labelService = Objects.requireNonNull(labelService);
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.containmentFeatureProvider = Objects.requireNonNull(containmentFeatureProvider);
    }

    @Override
    public List<IRepresentationDescription> getRepresentationDescriptions(IEditingContext editingContext) {
        TreeDescription simpleTreeDescription = TreeDescription.newTreeDescription(SYSON_DUPLICATE_TARGET_BROWSER_TREE_REPRESENTATION_ID)
                .label("Syson Duplicate Target Browser")
                .idProvider(variableManager -> variableManager.get(GetOrCreateRandomIdProvider.PREVIOUS_REPRESENTATION_ID, String.class).orElse(MODEL_BROWSER_CONTAINER_PREFIX))
                .treeItemIdProvider(this::getTreeItemId)
                .kindProvider(this::getKind)
                .labelProvider(this::getLabel)
                .targetObjectIdProvider(variableManager -> variableManager.get(IEditingContext.EDITING_CONTEXT, IEditingContext.class)
                        .map(IEditingContext::getId)
                        .orElse(null))
                .treeItemIconURLsProvider(this::getImageURL)
                .editableProvider(variableManager -> false)
                .deletableProvider(variableManager -> false)
                .selectableProvider(variableManager -> this.isSelectableProvider(variableManager, editingContext))
                .elementsProvider(this::getElements)
                .hasChildrenProvider(this::hasChildren)
                .childrenProvider(this::getChildren)
                .canCreatePredicate(variableManager -> false)
                .deleteHandler(variableManager -> new Failure(""))
                .renameHandler((variableManager, newName) -> new Failure(""))
                .treeItemObjectProvider(this::getTreeItemObject)
                .parentObjectProvider(this::getParentObject)
                .treeItemLabelProvider(this::getLabel)
                .iconURLsProvider(variableManager -> List.of())
                .build();
        return List.of(simpleTreeDescription);
    }


    private Object getTreeItemObject(VariableManager variableManager) {
        Object result = null;
        var optionalEditingContext = variableManager.get(IEditingContext.EDITING_CONTEXT, IEditingContext.class);
        var optionalId = variableManager.get(TreeDescription.ID, String.class);
        if (optionalId.isPresent() && optionalEditingContext.isPresent()) {
            var optionalObject = this.objectSearchService.getObject(optionalEditingContext.get(), optionalId.get());
            if (optionalObject.isPresent()) {
                result = optionalObject.get();
            } else {
                var optionalEditingDomain = Optional.of(optionalEditingContext.get())
                        .filter(IEMFEditingContext.class::isInstance)
                        .map(IEMFEditingContext.class::cast)
                        .map(IEMFEditingContext::getDomain);

                if (optionalEditingDomain.isPresent()) {
                    var editingDomain = optionalEditingDomain.get();
                    var resourceSet = editingDomain.getResourceSet();
                    var uri = new JSONResourceFactory().createResourceURI(optionalId.get());
                    result = resourceSet.getResources().stream()
                            .filter(resource -> resource.getURI().equals(uri))
                            .findFirst()
                            .orElse(null);
                }
            }
        }
        return result;
    }

    private Object getParentObject(VariableManager variableManager) {
        Object result = null;
        var self = variableManager.getVariables().get(VariableManager.SELF);
        if (self instanceof Element element) {
            var owningMemberElement = element.getOwningMembership();
            if (owningMemberElement != null) {
                result = owningMemberElement.eContainer();
            }
        }

        if (result == null && self instanceof Namespace ns && ns.eContainer() == null) {
            result = ns.eResource();
        }

        return result;
    }

    private boolean hasChildren(VariableManager variableManager) {
        boolean hasChildren = false;
        var self = variableManager.getVariables().get(VariableManager.SELF);
        if (self instanceof Element element) {
            hasChildren = !element.getOwnedElement().isEmpty();
        } else if (self instanceof Resource resource) {
            hasChildren = resource.getContents().stream()
                    .anyMatch(Element.class::isInstance);
        }
        return hasChildren;
    }

    private List<Object> getChildren(VariableManager variableManager) {
        List<Object> result = new ArrayList<>();

        List<String> expandedIds = new ArrayList<>();
        var objects = variableManager.getVariables().get(TreeRenderer.EXPANDED);
        if (objects instanceof List<?> list) {
            expandedIds = list.stream().filter(String.class::isInstance).map(String.class::cast).toList();
        }

        var optionalEditingContext = variableManager.get(IEditingContext.EDITING_CONTEXT, IEditingContext.class);
        if (optionalEditingContext.isPresent()) {
            var id = this.getTreeItemId(variableManager);
            if (expandedIds.contains(id)) {
                var self = variableManager.getVariables().get(VariableManager.SELF);
                if (self instanceof Element element) {
                    result.addAll(element.getOwnedElement());
                } else if (self instanceof Resource resource) {
                    resource.getContents().stream()
                            .filter(Element.class::isInstance)
                            .forEach(result::add);
                    // The current implementation of Sirius Web does not allow the copy of an element to the root of Resource see https://github.com/eclipse-sirius/sirius-web/issues/5853 when it
                    // does, this code could be improved to trim the root Namespace from the tree. When the user would select the Resource, it would automatically add the duplicated object to the
                    // root namespace of the Resource.
                }
            }
        }
        return result;
    }

    private List<? extends Object> getElements(VariableManager variableManager) {
        var optionalResourceSet = variableManager.get(IEditingContext.EDITING_CONTEXT, IEMFEditingContext.class)
                .map(IEMFEditingContext::getDomain)
                .map(EditingDomain::getResourceSet);

        if (optionalResourceSet.isPresent()) {
            var resourceSet = optionalResourceSet.get();
            return resourceSet.getResources().stream()
                    .filter(this::isValidTargetResource)
                    .sorted(Comparator.nullsLast(Comparator.comparing(this::getResourceLabel, String.CASE_INSENSITIVE_ORDER)))
                    .toList();
        }
        return List.of();
    }

    private String getResourceLabel(Resource resource) {
        return resource.eAdapters().stream()
                .filter(ResourceMetadataAdapter.class::isInstance)
                .map(ResourceMetadataAdapter.class::cast)
                .findFirst()
                .map(ResourceMetadataAdapter::getName)
                .orElse(resource.getURI().lastSegment());
    }

    private Boolean isSelectableProvider(VariableManager variableManager, IEditingContext editingContext) {
        var targetContainer = variableManager.getVariables().get(VariableManager.SELF);
        var representationId = variableManager.get(GetOrCreateRandomIdProvider.PREVIOUS_REPRESENTATION_ID, String.class).orElse(MODEL_BROWSER_CONTAINER_PREFIX);
        var ownerId = this.urlParser.getParameterValues(representationId).get("ownerId");
        return ownerId != null && ownerId.stream()
                .map(id -> this.objectSearchService.getObject(editingContext, id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .map(child -> !this.containmentFeatureProvider.getContainmentFeatures(targetContainer, child).isEmpty())
                .orElse(false);
    }

    private List<String> getImageURL(VariableManager variableManager) {
        List<String> imageURL = List.of(CoreImageConstants.DEFAULT_SVG);
        var self = variableManager.getVariables().get(VariableManager.SELF);
        if (self instanceof EObject) {
            imageURL = this.labelService.getImagePaths(self);
        } else if (self instanceof Resource resource) {
            imageURL = List.of("/icons/Resource.svg");
        }
        return imageURL;
    }

    private StyledString getLabel(VariableManager variableManager) {
        String label = "";
        var self = variableManager.getVariables().get(VariableManager.SELF);
        if (self instanceof EObject) {
            var styledString = this.labelService.getStyledLabel(self);
            if (!styledString.toString().isBlank()) {
                return styledString;
            } else {
                var kind = this.identityService.getKind(self);
                label = this.urlParser.getParameterValues(kind).get(SemanticKindConstants.ENTITY_ARGUMENT).get(0);
            }
        } else if (self instanceof Resource resource) {
            label = this.getResourceLabel(resource);
        }

        return StyledString.of(label);
    }

    private String getTreeItemId(VariableManager variableManager) {
        String id = null;
        var self = variableManager.getVariables().get(VariableManager.SELF);
        if (self instanceof Resource || self instanceof EObject) {
            id = this.identityService.getId(self);
        }
        return id;
    }

    private String getKind(VariableManager variableManager) {
        final String kind;
        var self = variableManager.getVariables().get(VariableManager.SELF);
        if (self instanceof Resource) {
            kind = ExplorerDescriptionProvider.DOCUMENT_KIND;
        } else {
            kind = this.identityService.getKind(self);
        }
        return kind;
    }

    private boolean isValidTargetResource(Resource resource) {
        return resource.getURI() != null
                && IEMFEditingContext.RESOURCE_SCHEME.equals(resource.getURI().scheme())
                && !this.readOnlyObjectPredicate.test(resource)
                && resource.getContents().stream().anyMatch(Element.class::isInstance);
    }
}
