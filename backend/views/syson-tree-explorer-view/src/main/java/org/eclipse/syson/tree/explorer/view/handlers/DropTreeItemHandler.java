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
package org.eclipse.syson.tree.explorer.view.handlers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.trees.api.IDropTreeItemHandler;
import org.eclipse.sirius.components.collaborative.trees.dto.DropTreeItemInput;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.representations.Success;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.springframework.stereotype.Service;

/**
 * SysML implementation of the DnD inside the SysON Explorer view.
 *
 * @author arichard
 */
@Service
public class DropTreeItemHandler implements IDropTreeItemHandler {

    private ILabelService labelService;

    private IObjectSearchService objectSearchService;

    private SysONTreeViewDescriptionProvider treeProvider;

    private ISysMLMoveElementService moveService;

    public DropTreeItemHandler(ILabelService labelService, IObjectSearchService objectSearchService, SysONTreeViewDescriptionProvider treeProvider, ISysMLMoveElementService moveService) {
        this.labelService = Objects.requireNonNull(labelService);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.treeProvider = Objects.requireNonNull(treeProvider);
        this.moveService = Objects.requireNonNull(moveService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, Tree tree) {
        return this.treeProvider.getDescriptionId().equals(tree.getDescriptionId());
    }

    @Override
    public IStatus handle(IEditingContext editingContext, Tree tree, DropTreeItemInput input) {

        Object targetObject = this.objectSearchService.getObject(editingContext, input.targetElementId())
                .orElse(null);

        boolean atLeastOneSuccessDrop = false;
        List<String> failingDropMessages = new ArrayList<>();

        if (targetObject instanceof Element targetElement) {
            List<Object> droppedObjects = input.droppedElementIds().stream()
                    .map(id -> this.objectSearchService.getObject(editingContext, id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)

                    .toList();

            for (Object droppedObject : droppedObjects) {

                final MoveStatus moveStatus;
                if (droppedObject == targetObject) {
                    // DnD is quite sensitive in the frontend, we want to avoid sending a message each time a user do a
                    // micro DnD on the item itself. Instead we ignore the DnD. Drawbacks: the model is persisted in DB.
                    moveStatus = MoveStatus.buildSuccess();
                } else if (droppedObject instanceof Element droppedElement) {
                    moveStatus = this.moveService.moveSemanticElement(droppedElement, targetElement);
                } else {
                    moveStatus = MoveStatus.buildFailure("The dropped element is not a SysML Element");
                }
                atLeastOneSuccessDrop = atLeastOneSuccessDrop | moveStatus.isSuccess();
                if (!moveStatus.isSuccess()) {
                    failingDropMessages.add(MessageFormat.format("Unable to move {0} in {1}: {2}", this.getLabel(droppedObject), this.getLabel(targetElement), moveStatus.message()));
                }
            }
        } else {
            failingDropMessages.add("Unable to move the element in selected target");
        }

        if (atLeastOneSuccessDrop) {
            // Need a Success to force the model to save in DB
            return new Success(failingDropMessages.stream().map(m -> new Message(m, MessageLevel.WARNING)).toList());
        } else {
            // No semantic change so no need to store the model in DB
            return new Failure(failingDropMessages.stream().map(m -> new Message(m, MessageLevel.WARNING)).toList());
        }
    }

    private String getLabel(Object droppedElement) {
        StyledString styledLabel = this.labelService.getStyledLabel(droppedElement);
        if ((styledLabel == null || styledLabel.toString().isEmpty()) && droppedElement instanceof EObject droppedEObject) {
            styledLabel = StyledString.of(droppedEObject.eClass().getName());
        }
        return styledLabel.toString();
    }

}
