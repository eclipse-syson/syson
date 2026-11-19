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
package org.eclipse.syson.tree.explorer.view.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.trees.dto.ExpandAllTreePathInput;
import org.eclipse.sirius.components.collaborative.trees.services.api.ITreeNavigationService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IURLParser;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.renderer.TreeRenderer;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests subtree ordering, filter forwarding, missing items and depth bounds of the optimized path provider.
 *
 * @author arichard
 */
public class SysONExpandAllTreePathProviderTest {

    private final ISysONExplorerService explorerService = mock(ISysONExplorerService.class);

    private final ITreeNavigationService navigationService = mock(ITreeNavigationService.class);

    private final IRepresentationMetadataSearchService metadataSearchService = mock(IRepresentationMetadataSearchService.class);

    private final IURLParser urlParser = mock(IURLParser.class);

    private final SysONTreeViewDescriptionProvider descriptionProvider = mock(SysONTreeViewDescriptionProvider.class);

    private final IEditingContext editingContext = mock(IEditingContext.class);

    private final Tree tree = mock(Tree.class);

    private final SysONExpandAllTreePathProvider provider = new SysONExpandAllTreePathProvider(this.explorerService, this.descriptionProvider,
            this.navigationService, this.urlParser, this.metadataSearchService);

    /**
     * Configures a valid Explorer query with an active filter.
     */
    @BeforeEach
    public void setup() {
        when(this.editingContext.getId()).thenReturn(UUID.randomUUID().toString());
        when(this.tree.getId()).thenReturn("explorer://test");
        when(this.tree.getDescriptionId()).thenReturn("syson");
        when(this.descriptionProvider.getDescriptionId()).thenReturn("syson");
        when(this.urlParser.getParameterValues(this.tree.getId())).thenReturn(Map.of(TreeRenderer.ACTIVE_FILTER_IDS, List.of("[active]")));
        when(this.urlParser.getParameterEntries("[active]")).thenReturn(List.of("active"));
    }

    /**
     * Checks preorder traversal and reuse of the supplied filter and metadata snapshot.
     */
    @Test
    @DisplayName("When a filtered subtree is traversed, preorder and maximum depth are preserved using the supplied metadata")
    public void traversesFilteredSubtree() {
        var representations = List.of(mock(RepresentationMetadata.class));
        when(this.metadataSearchService.findAllRepresentationMetadataBySemanticData(any())).thenReturn(representations);
        when(this.navigationService.getAncestors(this.editingContext, this.tree, "root")).thenReturn(List.of("document", "parent"));
        when(this.explorerService.getTreeItemObject("root", this.editingContext)).thenReturn("root");
        when(this.explorerService.getTreeItemId(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(this.explorerService.hasChildren(eq("root"), eq(this.editingContext), eq(representations), anyList(), eq(List.of("active")))).thenReturn(true);
        when(this.explorerService.hasChildren(eq("a"), eq(this.editingContext), eq(representations), anyList(), eq(List.of("active")))).thenReturn(true);
        when(this.explorerService.getChildren(eq("root"), eq(this.editingContext), eq(representations), anyList(), eq(List.of("active")))).thenReturn(List.of("a", "b"));
        when(this.explorerService.getChildren(eq("a"), eq(this.editingContext), eq(representations), anyList(), eq(List.of("active")))).thenReturn(List.of("c"));

        var input = new ExpandAllTreePathInput(UUID.randomUUID(), this.editingContext.getId(), this.tree.getId(), "root");
        var result = this.provider.handle(this.editingContext, this.tree, input);

        assertThat(result.id()).isEqualTo(input.id());
        assertThat(result.treePath().getTreeItemIdsToExpand()).containsExactly("root", "a", "c", "b");
        assertThat(result.treePath().getMaxDepth()).isEqualTo(4);
        verify(this.explorerService, times(1)).getTreeItemObject(any(), any());
        verify(this.metadataSearchService, times(1)).findAllRepresentationMetadataBySemanticData(any());
    }

    /**
     * Checks that even cyclic custom content terminates at the same 100-level limit as Sirius Web.
     */
    @Test
    @DisplayName("When custom tree content is cyclic, traversal stops at the 100-level depth limit")
    public void boundsCyclicContent() {
        when(this.explorerService.getTreeItemObject("root", this.editingContext)).thenReturn("root");
        when(this.explorerService.getTreeItemId("root")).thenReturn("root");
        when(this.explorerService.hasChildren(any(), any(), anyList(), anyList(), anyList())).thenReturn(true);
        when(this.explorerService.getChildren(any(), any(), anyList(), anyList(), anyList())).thenReturn(List.of("root"));

        var input = new ExpandAllTreePathInput(UUID.randomUUID(), this.editingContext.getId(), this.tree.getId(), "root");
        var path = this.provider.handle(this.editingContext, this.tree, input).treePath();

        assertThat(path.getTreeItemIdsToExpand()).containsExactly("root");
        assertThat(path.getMaxDepth()).isEqualTo(100);
        verify(this.explorerService, times(100)).getChildren(any(), any(), anyList(), anyList(), anyList());
    }

    /**
     * Checks the generic handler's missing-root behavior and description isolation.
     */
    @Test
    @DisplayName("When the root is missing, generic path behavior is preserved and other tree descriptions are ignored")
    public void handlesMissingRootAndIgnoresOtherDescriptions() {
        var input = new ExpandAllTreePathInput(UUID.randomUUID(), this.editingContext.getId(), this.tree.getId(), "missing");
        var path = this.provider.handle(this.editingContext, this.tree, input).treePath();
        assertThat(path.getTreeItemIdsToExpand()).containsExactly("missing");
        assertThat(path.getMaxDepth()).isEqualTo(1);
        assertThat(this.provider.canHandle(this.tree)).isTrue();
        when(this.tree.getDescriptionId()).thenReturn("another-tree");
        assertThat(this.provider.canHandle(this.tree)).isFalse();
    }
}
