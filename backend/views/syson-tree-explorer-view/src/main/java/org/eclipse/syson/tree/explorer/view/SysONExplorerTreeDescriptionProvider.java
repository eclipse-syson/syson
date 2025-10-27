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
package org.eclipse.syson.tree.explorer.view;

import java.util.List;
import java.util.UUID;

import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.builder.generated.tree.TreeBuilders;
import org.eclipse.sirius.components.view.builder.generated.tree.TreeDescriptionBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.TextStyleDescriptionBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.tree.TreeDescription;
import org.eclipse.sirius.components.view.tree.TreeItemContextMenuEntry;
import org.eclipse.sirius.components.view.tree.TreeItemLabelDescription;
import org.eclipse.sirius.components.view.tree.TreeItemLabelFragmentDescription;
import org.eclipse.sirius.emfjson.resource.JsonResource;

/**
 * Description of the SysON explorer tree using the ViewBuilder API from Sirius Web.
 *
 * @author gdaniel
 */
public class SysONExplorerTreeDescriptionProvider {

    public static final String SYSON_EXPLORER = "SysON Explorer";

    public static final String EXPAND_ALL_MENU_ENTRY_CONTRIBUTION_ID = "expandAll";

    public static final String NEW_OBJECTS_FROM_TEXT_MENU_ENTRY_CONTRIBUTION_ID = "newObjectsFromText";

    public View createView() {

        var sysonDefaultTreeView = new ViewBuilders()
                .newView()
                .descriptions(this.build())
                .build();

        UUID resourceId = UUID.nameUUIDFromBytes(SysONExplorerTreeDescriptionProvider.SYSON_EXPLORER.getBytes());
        String resourcePath = resourceId.toString();
        JsonResource resource = new JSONResourceFactory().createResourceFromPath(resourcePath);
        resource.eAdapters().add(new ResourceMetadataAdapter(SysONExplorerTreeDescriptionProvider.SYSON_EXPLORER, true));
        resource.getContents().add(sysonDefaultTreeView);

        return sysonDefaultTreeView;
    }

    private TreeDescription build() {
        TreeDescription description = new TreeDescriptionBuilder()
                .name(SYSON_EXPLORER)
                .childrenExpression("aql:self.getChildren(editingContext, existingRepresentations, expanded, activeFilterIds))")
                .deletableExpression("aql:self.isDeletable()")
                .editableExpression("aql:self.isEditable()")
                .elementsExpression("aql:editingContext.getElements(activeFilterIds)")
                .hasChildrenExpression("aql:self.hasChildren(editingContext, existingRepresentations, expanded, activeFilterIds)")
                .treeItemIconExpression("aql:self.getImageURL()")
                .kindExpression("aql:self.getKind()")
                .parentExpression("aql:self.getParent(id, editingContext)")
                // This predicate will NOT be used while creating the explorer, but we don't want to see the description
                // of the explorer in the list of representations that can be created. Thus, we will return false all
                // the time.
                .preconditionExpression("aql:false")
                .selectableExpression("aql:self.isSelectable()")
                .titleExpression(SYSON_EXPLORER)
                .treeItemIdExpression("aql:self.getTreeItemId()")
                .treeItemObjectExpression("aql:id.getTreeItemObject(editingContext)")
                .treeItemLabelDescriptions(this.createDefaultStyle())
                .contextMenuEntries(this.getContextMenuEntries().toArray(TreeItemContextMenuEntry[]::new))
                .build();
        return description;
    }

    private TreeItemLabelDescription createDefaultStyle() {
        return new TreeBuilders()
                .newTreeItemLabelDescription()
                .name("Default style")
                .preconditionExpression("aql:true")
                .children(this.getShortNameLabelFragmentDescription(),
                        this.getDefaultLabelFragmentDescription(),
                        this.getTypeFragmentDescription())
                .build();
    }

    private TreeItemLabelFragmentDescription getDefaultLabelFragmentDescription() {
        return new TreeBuilders().newTreeItemLabelFragmentDescription()
                .labelExpression("aql:self.getLabel()")
                .build();
    }

    private TreeItemLabelFragmentDescription getShortNameLabelFragmentDescription() {
        return new TreeBuilders().newTreeItemLabelFragmentDescription()
                .labelExpression("aql:self.getShortName()")
                .style(new TextStyleDescriptionBuilder()
                        .name("ShortNamePrefixStyle")
                        // Using color from MUI theme does not work so we use the hard coded color
                        // See https://github.com/eclipse-sirius/sirius-web/issues/5672
                        .foregroundColorExpression("aql:'#64669b'")
                        .build())
                .build();
    }

    private TreeItemLabelFragmentDescription getTypeFragmentDescription() {
        return new TreeBuilders().newTreeItemLabelFragmentDescription()
                .labelExpression("aql:self.getType()")
                .style(new ViewBuilders().newTextStyleDescription()
                        .name("GOLD_TEXT_STYLE_NAME")
                        .foregroundColorExpression("aql:'#ab8b01'")
                        .build())
                .build();
    }

    private List<TreeItemContextMenuEntry> getContextMenuEntries() {
        final TreeItemContextMenuEntry newObjectsFromTextMenuEntry = new TreeBuilders().newCustomTreeItemContextMenuEntry()
                .contributionId(NEW_OBJECTS_FROM_TEXT_MENU_ENTRY_CONTRIBUTION_ID)
                .preconditionExpression("aql:self.canCreateNewObjectsFromText()")
                .build();
        var expandAllMenuEntry = new TreeBuilders().newCustomTreeItemContextMenuEntry()
                .contributionId(EXPAND_ALL_MENU_ENTRY_CONTRIBUTION_ID)
                .preconditionExpression("aql:" + TreeItem.SELECTED_TREE_ITEM + ".canExpandAll(editingContext)")
                .build();
        return List.of(newObjectsFromTextMenuEntry, expandAllMenuEntry);
    }
}
