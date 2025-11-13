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

import java.util.UUID;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.builder.generated.tree.TreeBuilders;
import org.eclipse.sirius.components.view.builder.generated.tree.TreeDescriptionBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.TextStyleDescriptionBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.tree.TreeDescription;
import org.eclipse.sirius.components.view.tree.TreeItemLabelDescription;
import org.eclipse.sirius.components.view.tree.TreeItemLabelFragmentDescription;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.tree.services.aql.TreeQueryAQLService;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Description of the SysON explorer tree using the ViewBuilder API from Sirius Web.
 *
 * @author gdaniel
 */
public class SysONExplorerTreeDescriptionProvider {

    public static final String SYSON_EXPLORER = "SysON Explorer";

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
                .childrenExpression(ServiceMethod.of4(TreeQueryAQLService::getChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, "existingRepresentations", "expanded", "activeFilterIds"))
                .deletableExpression(ServiceMethod.of0(TreeQueryAQLService::isDeletable).aqlSelf())
                .editableExpression(ServiceMethod.of0(TreeQueryAQLService::isEditable).aqlSelf())
                .elementsExpression(ServiceMethod.of1(TreeQueryAQLService::getElements).aql(IEditingContext.EDITING_CONTEXT, "activeFilterIds"))
                .hasChildrenExpression(ServiceMethod.of4(TreeQueryAQLService::hasChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, "existingRepresentations", "expanded", "activeFilterIds"))
                .treeItemIconExpression(ServiceMethod.of0(TreeQueryAQLService::getImageURL).aqlSelf())
                .kindExpression(ServiceMethod.of0(TreeQueryAQLService::getKind).aqlSelf())
                .parentExpression(ServiceMethod.of2(TreeQueryAQLService::getParent).aqlSelf("id", IEditingContext.EDITING_CONTEXT))
                // This predicate will NOT be used while creating the explorer, but we don't want to see the description
                // of the explorer in the list of representations that can be created. Thus, we will return false all
                // the time.
                .preconditionExpression(AQLConstants.AQL_FALSE)
                .selectableExpression(ServiceMethod.of0(TreeQueryAQLService::isSelectable).aqlSelf())
                .titleExpression(SYSON_EXPLORER)
                .treeItemIdExpression(ServiceMethod.of0(TreeQueryAQLService::getTreeItemId).aqlSelf())
                .treeItemObjectExpression(ServiceMethod.of1(TreeQueryAQLService::getTreeItemObject).aql("id", IEditingContext.EDITING_CONTEXT))
                .treeItemLabelDescriptions(this.createDefaultStyle())
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
                        this.getTypeFragmentDescription(),
                        this.getLibraryLabelFragmentDescription(),
                        this.getReadOnlyFragmentDescription())
                .build();
    }

    private TreeItemLabelFragmentDescription getDefaultLabelFragmentDescription() {
        return new TreeBuilders().newTreeItemLabelFragmentDescription()
                .labelExpression(ServiceMethod.of0(TreeQueryAQLService::getLabel).aqlSelf())
                .build();
    }

    private TreeItemLabelFragmentDescription getShortNameLabelFragmentDescription() {
        return new TreeBuilders().newTreeItemLabelFragmentDescription()
                .labelExpression(ServiceMethod.of0(TreeQueryAQLService::getShortName).aqlSelf())
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
                .labelExpression(ServiceMethod.of0(TreeQueryAQLService::getType).aqlSelf())
                .style(new ViewBuilders().newTextStyleDescription()
                        .name("GOLD_TEXT_STYLE_NAME")
                        .foregroundColorExpression("aql:'#ab8b01'")
                        .build())
                .build();
    }

    private TreeItemLabelFragmentDescription getReadOnlyFragmentDescription() {
        return new TreeBuilders().newTreeItemLabelFragmentDescription()
                .labelExpression(ServiceMethod.of0(TreeQueryAQLService::getReadOnlyTag).aqlSelf())
                .style(new ViewBuilders().newTextStyleDescription()
                        .name("ReadOnlyTagStyle")
                        .foregroundColorExpression("aql:'#64669b'")
                        .build())
                .build();
    }

    private TreeItemLabelFragmentDescription getLibraryLabelFragmentDescription() {
        return new TreeBuilders().newTreeItemLabelFragmentDescription()
                .labelExpression(ServiceMethod.of0(TreeQueryAQLService::getLibraryLabel).aqlSelf())
                .style(new ViewBuilders().newTextStyleDescription()
                        .name("LibraryLabelStyle")
                        .foregroundColorExpression("aql:'#ab8b01'")
                        .build())
                .build();
    }
}
