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
package org.eclipse.syson.diagram.common.view.services;

import org.eclipse.syson.diagram.common.view.services.dto.NodeDefaultSizeExpression;
import org.eclipse.syson.util.SysmlEClassSwitch;
import org.eclipse.syson.util.ViewConstants;

/**
 * Switch allowing to know the default size of a node representing a SysMLv2 concept.
 * It's used by AbstractDefinitionNodeDescriptionProvider#create and AbstractUsageNodeDescriptionProvider#create.
 *
 * @author frouene
 */
public class NodeDefaultSizeExpressionSwitch extends SysmlEClassSwitch<NodeDefaultSizeExpression> {

    @Override
    public NodeDefaultSizeExpression caseDefinition(org.eclipse.syson.sysml.Definition object) {
        return new NodeDefaultSizeExpression(ViewConstants.DEFAULT_DEFINITION_NODE_HEIGHT, ViewConstants.DEFAULT_DEFINITION_NODE_WIDTH);
    }

    @Override
    public NodeDefaultSizeExpression caseUsage(org.eclipse.syson.sysml.Usage object) {
        return new NodeDefaultSizeExpression(ViewConstants.DEFAULT_USAGE_NODE_HEIGHT, ViewConstants.DEFAULT_USAGE_NODE_WIDTH);
    }

    @Override
    public NodeDefaultSizeExpression caseNamespaceImport(org.eclipse.syson.sysml.NamespaceImport object) {
        return new NodeDefaultSizeExpression(ViewConstants.DEFAULT_PACKAGE_NODE_HEIGHT, ViewConstants.DEFAULT_PACKAGE_NODE_WIDTH);
    }
}
