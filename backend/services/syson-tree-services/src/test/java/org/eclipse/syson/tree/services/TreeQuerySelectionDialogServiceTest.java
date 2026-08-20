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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.tree.explorer.filters.SysONTreeFilterConstants;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFilterService;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFragment;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TreeQuerySelectionDialogService}.
 *
 * @author arichard
 */
public class TreeQuerySelectionDialogServiceTest {

    private static final List<String> SELECTION_DIALOG_FILTER_IDS = List.of(
            SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID,
            SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID,
            SysONTreeFilterConstants.HIDE_EXPOSE_ELEMENTS_TREE_ITEM_FILTER_ID,
            SysONTreeFilterConstants.HIDE_EXPRESSION_INTERNALS_ID);

    /**
     * Verifies that selection-dialog roots use the fixed Explorer-default filters.
     */
    @Test
    @DisplayName("GIVEN a selection dialog, WHEN its root elements are retrieved, THEN the fixed Explorer-default filters are used")
    void rootElementsUseSelectionDialogFilters() {
        var explorerService = mock(ISysONExplorerService.class);
        var filterService = mock(ISysONExplorerFilterService.class);
        var editingContext = mock(IEditingContext.class);
        var fragment = mock(ISysONExplorerFragment.class);
        var candidate = mock(EClassifier.class);
        when(explorerService.getElements(editingContext, SELECTION_DIALOG_FILTER_IDS)).thenReturn(List.of(fragment));

        var service = new TreeQuerySelectionDialogService(explorerService, filterService);

        assertThat(service.getSelectionDialogElements(editingContext, List.of(candidate))).containsExactly(fragment);
        verify(explorerService).getElements(editingContext, SELECTION_DIALOG_FILTER_IDS);
    }

    /**
     * Verifies that semantic selection-dialog children use the fixed Explorer-default filters without using the
     * Explorer tree child provider.
     */
    @Test
    @DisplayName("GIVEN a semantic selection dialog element, WHEN its children are retrieved, THEN the fixed Explorer-default filters are used")
    void semanticChildrenUseSelectionDialogFilters() {
        var explorerService = mock(ISysONExplorerService.class);
        var filterService = mock(ISysONExplorerFilterService.class);
        var editingContext = mock(IEditingContext.class);
        var element = mock(Element.class);
        var membership = mock(Membership.class);
        var candidate = mock(Element.class);
        var candidateClassifier = mock(EClassifier.class);
        var expandedIds = List.of("element-id");
        var ownedRelationships = new BasicEList<Relationship>();
        ownedRelationships.add(membership);
        when(element.getOwnedRelationship()).thenReturn(ownedRelationships);
        when(filterService.applyFilters(eq(editingContext), eq(List.of(membership)), eq(SELECTION_DIALOG_FILTER_IDS))).thenReturn(List.of(candidate));
        when(candidateClassifier.isInstance(candidate)).thenReturn(true);

        var service = new TreeQuerySelectionDialogService(explorerService, filterService);

        assertThat(service.getSelectionDialogChildren(element, editingContext, expandedIds, List.of(candidateClassifier)))
                .hasSize(1)
                .first()
                .isSameAs(candidate);
        verify(filterService).applyFilters(editingContext, List.of(membership), SELECTION_DIALOG_FILTER_IDS);
    }

    /**
     * Verifies that resource children are filtered without using the Explorer tree child provider.
     */
    @Test
    @DisplayName("GIVEN a selection dialog resource, WHEN its children are retrieved, THEN its native contents are filtered")
    void resourceChildrenUseNativeContentsWithSelectionDialogFilters() {
        var explorerService = mock(ISysONExplorerService.class);
        var filterService = mock(ISysONExplorerFilterService.class);
        var editingContext = mock(IEditingContext.class);
        var resource = mock(Resource.class);
        var resourceContent = mock(EObject.class);
        var candidate = mock(Element.class);
        var candidateClassifier = mock(EClassifier.class);
        var resourceContents = new BasicEList<EObject>();
        resourceContents.add(resourceContent);
        when(resource.getContents()).thenReturn(resourceContents);
        when(filterService.applyFilters(eq(editingContext), eq(resourceContents), eq(SELECTION_DIALOG_FILTER_IDS))).thenReturn(List.of(candidate));
        when(candidateClassifier.isInstance(candidate)).thenReturn(true);

        var service = new TreeQuerySelectionDialogService(explorerService, filterService);

        assertThat(service.getSelectionDialogChildren(resource, editingContext, List.of("resource-id"), List.of(candidateClassifier)))
                .hasSize(1)
                .first()
                .isSameAs(candidate);
        verify(filterService).applyFilters(editingContext, resourceContents, SELECTION_DIALOG_FILTER_IDS);
    }

    /**
     * Verifies that selection-dialog fragments use the fixed filters and exclude child resources without candidates.
     */
    @Test
    @DisplayName("GIVEN a selection dialog fragment, WHEN its children are retrieved, THEN filters are applied and empty resources are excluded")
    void fragmentChildrenUseSelectionDialogFilters() {
        var explorerService = mock(ISysONExplorerService.class);
        var filterService = mock(ISysONExplorerFilterService.class);
        var editingContext = mock(IEditingContext.class);
        var fragment = mock(ISysONExplorerFragment.class);
        var resourceWithoutCandidate = mock(Resource.class);
        var candidate = new Object();
        var candidateClassifier = mock(EClassifier.class);
        var expandedIds = List.of("fragment-id");
        TreeIterator<EObject> contents = new EmptyTreeIterator();
        when(resourceWithoutCandidate.getAllContents()).thenReturn(contents);
        when(fragment.getChildren(editingContext, List.of(), expandedIds, SELECTION_DIALOG_FILTER_IDS)).thenReturn(List.of(resourceWithoutCandidate, candidate));

        var service = new TreeQuerySelectionDialogService(explorerService, filterService);

        assertThat(service.getSelectionDialogChildren(fragment, editingContext, expandedIds, List.of(candidateClassifier)))
                .hasSize(1)
                .first()
                .isSameAs(candidate);
        verify(fragment).getChildren(editingContext, List.of(), expandedIds, SELECTION_DIALOG_FILTER_IDS);
    }

    /**
     * Verifies that unsupported selection-dialog tree elements have no children.
     */
    @Test
    @DisplayName("GIVEN an unsupported selection dialog element, WHEN its children are retrieved, THEN no child is returned")
    void unsupportedSelectionDialogElementHasNoChildren() {
        var explorerService = mock(ISysONExplorerService.class);
        var filterService = mock(ISysONExplorerFilterService.class);
        var service = new TreeQuerySelectionDialogService(explorerService, filterService);

        assertThat(service.getSelectionDialogChildren("unsupported", mock(IEditingContext.class), List.of(), List.of(mock(EClassifier.class)))).isEmpty();
    }

    /**
     * Empty {@link TreeIterator} used for resources that contain no candidate.
     *
     * @author arichard
     */
    private static final class EmptyTreeIterator implements TreeIterator<EObject> {

        /**
         * Indicates that the iterator has no element.
         *
         * @return {@code false}
         */
        @Override
        public boolean hasNext() {
            return false;
        }

        /**
         * Throws because the iterator is empty.
         *
         * @return never returns
         */
        @Override
        public EObject next() {
            return null;
        }

        /**
         * Does nothing because the iterator is empty.
         */
        @Override
        public void prune() {
            // Nothing to prune.
        }
    }
}
