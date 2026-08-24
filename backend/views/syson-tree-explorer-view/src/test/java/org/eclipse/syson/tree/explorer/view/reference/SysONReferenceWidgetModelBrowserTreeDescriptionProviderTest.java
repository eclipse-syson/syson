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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IContentService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.sirius.web.application.browser.DefaultModelBrowsersTreeDescriptionProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.tree.explorer.filters.SysONTreeFilterConstants;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFilterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SysONReferenceWidgetModelBrowserTreeDescriptionProvider}.
 *
 * @author arichard
 */
public class SysONReferenceWidgetModelBrowserTreeDescriptionProviderTest {

    /**
     * Verifies that only the default Reference Widget description is replaced.
     */
    @Test
    @DisplayName("GIVEN default model browser descriptions, WHEN they are customized, THEN only the reference description is replaced")
    void getRepresentationDescriptions() {
        var defaultProvider = mock(DefaultModelBrowsersTreeDescriptionProvider.class);
        var contentService = mock(IContentService.class);
        var filterService = mock(ISysONExplorerFilterService.class);
        var editingContext = mock(IEditingContext.class);
        Object defaultChild = new Object();
        Object filteredChild = new Object();
        var referenceDescription = this.treeDescription(DefaultModelBrowsersTreeDescriptionProvider.REFERENCE_DESCRIPTION_ID, List.of(defaultChild));
        IRepresentationDescription anotherDescription = this.treeDescription("another-description", List.of());
        when(defaultProvider.getRepresentationDescriptions(editingContext)).thenReturn(List.of(anotherDescription, referenceDescription));
        when(filterService.applyFilters(eq(editingContext), eq(List.of(defaultChild)), eq(List.of(
                SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID,
                SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID)))).thenReturn(List.of(filteredChild));

        var provider = new SysONReferenceWidgetModelBrowserTreeDescriptionProvider(defaultProvider, contentService, filterService);

        var descriptions = provider.getRepresentationDescriptions(editingContext);

        assertThat(descriptions).singleElement().isInstanceOfSatisfying(TreeDescription.class, treeDescription -> {
            assertThat(treeDescription.getId()).isEqualTo(SysONReferenceWidgetModelBrowserTreeDescriptionProvider.DESCRIPTION_ID);
            var variableManager = new VariableManager();
            variableManager.put(IEditingContext.EDITING_CONTEXT, editingContext);
            assertThat(treeDescription.getChildrenProvider().apply(variableManager)).isEqualTo(List.of(filteredChild));
        });
        verify(filterService).applyFilters(eq(editingContext), eq(List.of(defaultChild)), eq(List.of(
                SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID,
                SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID)));
    }

    /**
     * Verifies that children retrieved from a resource or semantic object are filtered.
     */
    @Test
    @DisplayName("GIVEN a resource or semantic object, WHEN checking children, THEN its filtered children are used")
    void hasChildrenUsesFilteredSemanticChildren() {
        var defaultProvider = mock(DefaultModelBrowsersTreeDescriptionProvider.class);
        var contentService = mock(IContentService.class);
        var filterService = mock(ISysONExplorerFilterService.class);
        var editingContext = mock(IEditingContext.class);
        var referenceDescription = this.treeDescription(DefaultModelBrowsersTreeDescriptionProvider.REFERENCE_DESCRIPTION_ID, List.of());
        when(defaultProvider.getRepresentationDescriptions(editingContext)).thenReturn(List.of(referenceDescription));

        Resource resource = mock(Resource.class);
        EObject resourceChild = mock(EObject.class);
        when(resource.getContents()).thenReturn(ECollections.singletonEList(resourceChild));
        EObject semanticObject = mock(EObject.class);
        EObject semanticChild = mock(EObject.class);
        when(contentService.getContents(semanticObject)).thenReturn(List.of(semanticChild));
        when(filterService.applyFilters(eq(editingContext), eq(List.of(resourceChild)), org.mockito.ArgumentMatchers.anyList())).thenReturn(List.of(resourceChild));
        when(filterService.applyFilters(eq(editingContext), eq(List.of(semanticChild)), org.mockito.ArgumentMatchers.anyList())).thenReturn(List.of());

        var provider = new SysONReferenceWidgetModelBrowserTreeDescriptionProvider(defaultProvider, contentService, filterService);
        TreeDescription treeDescription = (TreeDescription) provider.getRepresentationDescriptions(editingContext).get(0);

        assertThat(treeDescription.getHasChildrenProvider().apply(this.variableManager(editingContext, resource))).isTrue();
        assertThat(treeDescription.getHasChildrenProvider().apply(this.variableManager(editingContext, semanticObject))).isFalse();
        assertThat(treeDescription.getHasChildrenProvider().apply(new VariableManager())).isFalse();
    }

    /**
     * Verifies that parent lookup bypasses memberships and flattens root namespaces.
     */
    @Test
    @DisplayName("GIVEN a membership owned element, WHEN its parent is retrieved, THEN the namespace resource is returned")
    void parentObjectProviderFlattensMembershipsAndRootNamespaces() {
        var defaultProvider = mock(DefaultModelBrowsersTreeDescriptionProvider.class);
        var contentService = mock(IContentService.class);
        var filterService = mock(ISysONExplorerFilterService.class);
        var editingContext = mock(IEditingContext.class);
        var referenceDescription = this.treeDescription(DefaultModelBrowsersTreeDescriptionProvider.REFERENCE_DESCRIPTION_ID, List.of());
        when(defaultProvider.getRepresentationDescriptions(editingContext)).thenReturn(List.of(referenceDescription));

        Element element = mock(Element.class);
        OwningMembership membership = mock(OwningMembership.class);
        Namespace rootNamespace = mock(Namespace.class);
        Resource resource = mock(Resource.class);
        when(element.getOwningMembership()).thenReturn(membership);
        when(membership.eContainer()).thenReturn(rootNamespace);
        when(rootNamespace.eContainer()).thenReturn(null);
        when(rootNamespace.eResource()).thenReturn(resource);
        EObject object = mock(EObject.class);
        EObject parent = mock(EObject.class);
        when(object.eContainer()).thenReturn(parent);

        var provider = new SysONReferenceWidgetModelBrowserTreeDescriptionProvider(defaultProvider, contentService, filterService);
        TreeDescription treeDescription = (TreeDescription) provider.getRepresentationDescriptions(editingContext).get(0);

        assertThat(treeDescription.getParentObjectProvider().apply(this.variableManager(editingContext, element))).isSameAs(resource);
        assertThat(treeDescription.getParentObjectProvider().apply(this.variableManager(editingContext, object))).isSameAs(parent);
    }

    private VariableManager variableManager(IEditingContext editingContext, Object self) {
        var variableManager = new VariableManager();
        variableManager.put(IEditingContext.EDITING_CONTEXT, editingContext);
        variableManager.put(VariableManager.SELF, self);
        return variableManager;
    }

    private TreeDescription treeDescription(String id, List<?> children) {
        return TreeDescription.newTreeDescription(id)
                .label("Reference Widget")
                .idProvider(variableManager -> id)
                .treeItemIdProvider(variableManager -> "item")
                .kindProvider(variableManager -> "kind")
                .labelProvider(variableManager -> null)
                .targetObjectIdProvider(variableManager -> null)
                .parentObjectProvider(variableManager -> null)
                .treeItemIconURLsProvider(variableManager -> List.of())
                .editableProvider(variableManager -> false)
                .deletableProvider(variableManager -> false)
                .elementsProvider(variableManager -> List.of())
                .childrenProvider(variableManager -> children)
                .hasChildrenProvider(variableManager -> !children.isEmpty())
                .canCreatePredicate(variableManager -> false)
                .deleteHandler(variableManager -> null)
                .renameHandler((variableManager, newLabel) -> null)
                .treeItemObjectProvider(variableManager -> null)
                .treeItemLabelProvider(variableManager -> null)
                .iconURLsProvider(variableManager -> List.of())
                .build();
    }
}
