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

import { ExtensionRegistry } from '@eclipse-sirius/sirius-components-core';
import {
  diagramToolbarActionExtensionPoint,
  EdgeAppearanceSection,
  EdgeData,
  ImageNodeAppearanceSection,
  NodeData,
  PaletteAppearanceSectionContributionProps,
  paletteAppearanceSectionExtensionPoint,
  RectangularNodeAppearanceSection,
} from '@eclipse-sirius/sirius-components-diagrams';
import {
  GQLWidget,
  PropertySectionComponent,
  widgetContributionExtensionPoint,
} from '@eclipse-sirius/sirius-components-forms';
import {
  OmniboxCommand,
  OmniboxCommandOverrideContribution,
  omniboxCommandOverrideContributionExtensionPoint,
} from '@eclipse-sirius/sirius-components-omnibox';
import {
  PaletteToolOverriddenContributionProps,
  paletteToolOverrideExtensionPoint,
} from '@eclipse-sirius/sirius-components-palette';
import {
  GQLTreeItemContextMenuEntry,
  treeItemContextMenuEntryOverrideExtensionPoint,
  TreeItemContextMenuOverrideContribution,
} from '@eclipse-sirius/sirius-components-trees';
import {
  ApolloClientOptionsConfigurer,
  apolloClientOptionsConfigurersExtensionPoint,
  ImportLibraryCommand,
  navigationBarMenuIconExtensionPoint,
} from '@eclipse-sirius/sirius-web-application';
import QuestionMarkOutlinedIcon from '@mui/icons-material/QuestionMarkOutlined';
import { Edge, Node, useStoreApi } from '@xyflow/react';
import { SysMLImportedPackageNodePaletteAppearanceSection } from '../../nodes/imported_package/SysMLImportedPackageNodePaletteAppearanceSection';
import { SysMLNoteNodePaletteAppearanceSection } from '../../nodes/note/SysMLNoteNodePaletteAppearanceSection';
import { SysMLPackageNodePaletteAppearanceSection } from '../../nodes/package/SysMLPackageNodePaletteAppearanceSection';
import { sysMLNodesStyleDocumentTransform } from '../../nodes/SysMLNodesDocumentTransform';
import { SysMLViewFrameNodePaletteAppearanceSection } from '../../nodes/view_frame/SysMLViewFrameNodePaletteAppearanceSection';
import { DeleteExpressionDiagramToolOverriddenContribution } from '../expressions/DeleteExpressionDiagramToolOverriddenContribution';
import { DeleteSysMLExpressionMenuContribution } from '../expressions/DeleteSysMLExpressionMenuContribution';
import { EditExpressionDiagramToolOverriddenContribution } from '../expressions/EditExpressionDiagramToolOverriddenContribution';
import { EditSysMLExpressionMenuContribution } from '../expressions/EditSysMLExpressionMenuContribution';
import { ExpressionPropertySection } from '../expressions/ExpressionPropertySection';
import { NewExpressionDiagramToolOverriddenContribution } from '../expressions/NewExpressionDiagramToolOverriddenContribution';
import { NewSysMLExpressionMenuContribution } from '../expressions/NewSysMLExpressionMenuContribution';
import { InsertTextualSysMLMenuContribution } from '../InsertTextualSysMLv2MenuContribution';
import { SysONNavigationBarMenuIcon } from '../navigationBarMenu/SysONNavigationBarMenuIcon';
import { PublishProjectSysMLContentsAsLibraryCommand } from '../omnibox/PublishProjectSysMLContentsAsLibraryCommand';
import { SysONDiagramPanelMenu } from '../SysONDiagramPanelMenu';

const sysONExtensionRegistry: ExtensionRegistry = new ExtensionRegistry();

const omniboxCommandOverrides: OmniboxCommandOverrideContribution[] = [
  {
    canHandle: (action: OmniboxCommand) => {
      return action.id === 'publishProjectSysMLContentsAsLibrary';
    },
    component: PublishProjectSysMLContentsAsLibraryCommand,
  },
  {
    canHandle: (action: OmniboxCommand) => {
      return action.id === 'importPublishedLibrary';
    },
    component: ImportLibraryCommand,
  },
];

sysONExtensionRegistry.putData<OmniboxCommandOverrideContribution[]>(omniboxCommandOverrideContributionExtensionPoint, {
  identifier: `syson_${omniboxCommandOverrideContributionExtensionPoint.identifier}`,
  data: omniboxCommandOverrides,
});

const apolloClientOptionsConfigurer: ApolloClientOptionsConfigurer = (currentOptions) => {
  const { documentTransform } = currentOptions;

  const newDocumentTransform = documentTransform
    ? documentTransform.concat(sysMLNodesStyleDocumentTransform)
    : sysMLNodesStyleDocumentTransform;
  return {
    ...currentOptions,
    documentTransform: newDocumentTransform,
  };
};
sysONExtensionRegistry.putData(apolloClientOptionsConfigurersExtensionPoint, {
  identifier: `syson_${apolloClientOptionsConfigurersExtensionPoint.identifier}`,
  data: [apolloClientOptionsConfigurer],
});

sysONExtensionRegistry.addComponent(diagramToolbarActionExtensionPoint, {
  identifier: `syson_${diagramToolbarActionExtensionPoint.identifier}_CustomToolbarEntriesMenu`,
  Component: SysONDiagramPanelMenu,
});

sysONExtensionRegistry.addComponent(navigationBarMenuIconExtensionPoint, {
  identifier: `syson_${navigationBarMenuIconExtensionPoint.identifier}`,
  Component: SysONNavigationBarMenuIcon,
});

const treeItemContextMenuOverrideContributions: TreeItemContextMenuOverrideContribution[] = [
  {
    canHandle: (entry: GQLTreeItemContextMenuEntry) => {
      return entry.id === 'newObjectsFromText';
    },
    component: InsertTextualSysMLMenuContribution,
  },
  {
    canHandle: (entry: GQLTreeItemContextMenuEntry) => {
      return entry.id === 'createExpression';
    },
    component: NewSysMLExpressionMenuContribution,
  },
  {
    canHandle: (entry: GQLTreeItemContextMenuEntry) => {
      return entry.id === 'editExpression';
    },
    component: EditSysMLExpressionMenuContribution,
  },
  {
    canHandle: (entry: GQLTreeItemContextMenuEntry) => {
      return entry.id === 'deleteExpression';
    },
    component: DeleteSysMLExpressionMenuContribution,
  },
];

sysONExtensionRegistry.putData<TreeItemContextMenuOverrideContribution[]>(
  treeItemContextMenuEntryOverrideExtensionPoint,
  {
    identifier: `syson_${treeItemContextMenuEntryOverrideExtensionPoint.identifier}`,
    data: treeItemContextMenuOverrideContributions,
  }
);

/*******************************************************************************
 *
 * Custom nodes appearance contributions
 *
 *******************************************************************************/
const customNodePaletteAppearanceSectionContribution: PaletteAppearanceSectionContributionProps[] = [
  // standard nodes and edges from Sirius Web
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      const canHandle = diagramElementIds.every(
        (elementId) =>
          store.getState().nodeLookup.get(elementId)?.data.nodeAppearanceData?.gqlStyle.__typename ===
          'RectangularNodeStyle'
      );

      return canHandle;
    },
    component: RectangularNodeAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) =>
          store.getState().nodeLookup.get(elementId)?.data.nodeAppearanceData?.gqlStyle.__typename === 'ImageNodeStyle'
      );
    },
    component: ImageNodeAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every((elementId) => !!store.getState().edgeLookup.get(elementId));
    },
    component: EdgeAppearanceSection,
  },
  // custom nodes from SysON
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) => store.getState().nodeLookup.get(elementId)?.type === 'sysMLPackageNode'
      );
    },
    component: SysMLPackageNodePaletteAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) => store.getState().nodeLookup.get(elementId)?.type === 'sysMLImportedPackageNode'
      );
    },
    component: SysMLImportedPackageNodePaletteAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) => store.getState().nodeLookup.get(elementId)?.type === 'sysMLNoteNode'
      );
    },
    component: SysMLNoteNodePaletteAppearanceSection,
  },
  {
    canHandle: (diagramElementIds) => {
      const store = useStoreApi<Node<NodeData>, Edge<EdgeData>>();
      return diagramElementIds.every(
        (elementId) => store.getState().nodeLookup.get(elementId)?.type === 'sysMLNoteNode'
      );
    },
    component: SysMLViewFrameNodePaletteAppearanceSection,
  },
];

sysONExtensionRegistry.putData<PaletteAppearanceSectionContributionProps[]>(paletteAppearanceSectionExtensionPoint, {
  identifier: `syson_${paletteAppearanceSectionExtensionPoint.identifier}`,
  data: customNodePaletteAppearanceSectionContribution,
});

sysONExtensionRegistry.putData(widgetContributionExtensionPoint, {
  identifier: `syson_${widgetContributionExtensionPoint.identifier}`,
  data: [
    {
      name: 'ExpressionValuePropertySectionOverride',
      icon: <QuestionMarkOutlinedIcon />,
      previewComponent: () => null,
      component: (widget: GQLWidget): PropertySectionComponent<GQLWidget> | null => {
        let propertySectionComponent: PropertySectionComponent<GQLWidget> | null = null;
        if (widget.__typename == 'Textarea' && widget.label.startsWith('syson:expression-value-widget')) {
          propertySectionComponent = ExpressionPropertySection as PropertySectionComponent<GQLWidget>;
        }
        return propertySectionComponent;
      },
    },
  ],
});

// FIXME: This type should be exported by @eclipse-sirius/sirius-components-palette
interface GQLTool {
  id: string;
  label: string;
  iconURL: string[];
  __typename: string;
}

const diagramPaletteToolOverriddenContributions: PaletteToolOverriddenContributionProps[] = [
  {
    canHandle: (tool: GQLTool) => {
      return tool.id === 'tool_new_expression';
    },
    component: NewExpressionDiagramToolOverriddenContribution,
  },
  {
    canHandle: (tool: GQLTool) => {
      return tool.id === 'tool_edit_expression';
    },
    component: EditExpressionDiagramToolOverriddenContribution,
  },
  {
    canHandle: (tool: GQLTool) => {
      return tool.id === 'tool_delete_expression';
    },
    component: DeleteExpressionDiagramToolOverriddenContribution,
  },
];

sysONExtensionRegistry.putData<PaletteToolOverriddenContributionProps[]>(paletteToolOverrideExtensionPoint, {
  identifier: `syson_${paletteToolOverrideExtensionPoint.identifier}`,
  data: diagramPaletteToolOverriddenContributions,
});

export { sysONExtensionRegistry };
