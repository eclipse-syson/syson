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
package org.eclipse.syson.tree.explorer.view.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IContentService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextRepresentationDescriptionProvider;
import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.sirius.web.application.browser.DefaultModelBrowsersTreeDescriptionProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.tree.explorer.filters.SysONTreeFilterConstants;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFilterService;
import org.springframework.stereotype.Service;

/**
 * Provides the Reference Widget model-browser tree with the SysON semantic-tree presentation.
 *
 * @author arichard
 */
@Service
public class SysONReferenceWidgetModelBrowserTreeDescriptionProvider implements IEditingContextRepresentationDescriptionProvider {

    /**
     * The identifier of the custom Reference Widget model-browser tree description.
     */
    public static final String DESCRIPTION_ID = UUID.nameUUIDFromBytes("syson_reference_widget_model_browser_tree_description".getBytes()).toString();

    private static final List<String> FILTER_IDS = List.of(
            SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID,
            SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID);

    private final DefaultModelBrowsersTreeDescriptionProvider defaultModelBrowsersTreeDescriptionProvider;

    private final IContentService contentService;

    private final ISysONExplorerFilterService filterService;

    /**
     * Creates the custom Reference Widget model-browser tree-description provider.
     *
     * @param defaultModelBrowsersTreeDescriptionProvider
     *            the provider of the default Reference Widget model-browser tree description
     * @param contentService
     *            the service used to retrieve semantic children
     * @param filterService
     *            the service used to apply SysON semantic-tree filters
     */
    public SysONReferenceWidgetModelBrowserTreeDescriptionProvider(DefaultModelBrowsersTreeDescriptionProvider defaultModelBrowsersTreeDescriptionProvider,
            IContentService contentService, ISysONExplorerFilterService filterService) {
        this.defaultModelBrowsersTreeDescriptionProvider = Objects.requireNonNull(defaultModelBrowsersTreeDescriptionProvider);
        this.contentService = Objects.requireNonNull(contentService);
        this.filterService = Objects.requireNonNull(filterService);
    }

    @Override
    public List<IRepresentationDescription> getRepresentationDescriptions(IEditingContext editingContext) {
        return this.defaultModelBrowsersTreeDescriptionProvider.getRepresentationDescriptions(editingContext).stream()
                .filter(TreeDescription.class::isInstance)
                .map(TreeDescription.class::cast)
                .filter(treeDescription -> DefaultModelBrowsersTreeDescriptionProvider.REFERENCE_DESCRIPTION_ID.equals(treeDescription.getId()))
                .map(this::createDescription)
                .map(IRepresentationDescription.class::cast)
                .toList();
    }

    private TreeDescription createDescription(TreeDescription defaultDescription) {
        return TreeDescription.newTreeDescription(defaultDescription)
                .id(DESCRIPTION_ID)
                .hasChildrenProvider(this::hasChildren)
                .childrenProvider(variableManager -> this.getChildren(defaultDescription, variableManager))
                .parentObjectProvider(this::getParentObject)
                .build();
    }

    private boolean hasChildren(VariableManager variableManager) {
        return !this.applyFilters(variableManager, this.getDefaultChildren(variableManager)).isEmpty();
    }

    private List<Object> getChildren(TreeDescription defaultDescription, VariableManager variableManager) {
        List<? extends Object> defaultChildren = defaultDescription.getChildrenProvider().apply(variableManager);
        return this.applyFilters(variableManager, defaultChildren);
    }

    private List<Object> getDefaultChildren(VariableManager variableManager) {
        Object self = variableManager.getVariables().get(VariableManager.SELF);
        List<Object> children = List.of();
        if (self instanceof Resource resource) {
            children = new ArrayList<>(resource.getContents());
        } else if (self instanceof EObject) {
            children = this.contentService.getContents(self);
        }
        return children;
    }

    private List<Object> applyFilters(VariableManager variableManager, List<? extends Object> children) {
        return variableManager.get(IEditingContext.EDITING_CONTEXT, IEditingContext.class)
                .map(editingContext -> this.filterService.applyFilters(editingContext, children, FILTER_IDS))
                .orElseGet(List::of);
    }

    private Object getParentObject(VariableManager variableManager) {
        Object self = variableManager.getVariables().get(VariableManager.SELF);
        Object parent = null;
        if (self instanceof Element element && element.getOwningMembership() != null) {
            parent = element.getOwningMembership().eContainer();
        } else if (self instanceof EObject eObject) {
            parent = eObject.eContainer();
        }

        if (parent instanceof Namespace namespace && namespace.eContainer() == null) {
            return namespace.eResource();
        }
        return parent;
    }
}
