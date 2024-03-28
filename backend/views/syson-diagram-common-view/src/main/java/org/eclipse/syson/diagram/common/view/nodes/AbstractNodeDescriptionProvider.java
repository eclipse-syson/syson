/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.Objects;

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.AbstractViewElementDescriptionProvider;

/**
 * Common pieces of node descriptions shared by {@link INodeDescriptionProvider} in all diagram View.
 *
 * @author arichard
 */
public abstract class AbstractNodeDescriptionProvider extends AbstractViewElementDescriptionProvider implements INodeDescriptionProvider {

    protected final IColorProvider colorProvider;

    public AbstractNodeDescriptionProvider(IColorProvider colorProvider) {
        this.colorProvider = Objects.requireNonNull(colorProvider);
    }

    protected NodeToolSection addElementsToolSection() {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Add")
                .nodeTools(this.addExistingElementsTool(false), this.addExistingElementsTool(true))
                .build();
    }
}
