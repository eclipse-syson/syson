/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.application.explorer;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.trees.api.ITreeEventHandler;
import org.eclipse.sirius.components.collaborative.trees.api.ITreeInput;
import org.eclipse.sirius.components.collaborative.trees.dto.DropTreeItemInput;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Disable the D&D inside the Explorer view. Sirius Web now allows the D&D of tree items inside the Explorer view, but
 * the default behavior is not suitable for SysON. Indeed, when an Element is D&D, SysON should also D&D its Membership
 * container at the same time.
 *
 * @author arichard
 */
@Service
@Primary
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class DropTreeItemHandler implements ITreeEventHandler {

    @Override
    public boolean canHandle(ITreeInput treeInput) {
        return treeInput instanceof DropTreeItemInput;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, TreeDescription treeDescription, Tree tree, ITreeInput treeInput) {
        payloadSink.tryEmitValue(new SuccessPayload(treeInput.id()));
    }
}
