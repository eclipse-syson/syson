/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.application.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.ChildCreationDescription;
import org.eclipse.sirius.components.core.api.IDefaultEditService;
import org.eclipse.sirius.components.core.api.IEditServiceDelegate;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.syson.sysml.Element;
import org.springframework.stereotype.Service;

/**
 * Specific {@link IEditServiceDelegate} to handle edition of SysML elements.
 *
 * @author arichard
 */
@Service
public class SysMLv2EditService implements IEditServiceDelegate {

    private final IDefaultEditService defaultEditService;

    private final DeleteService deleteService;

    public SysMLv2EditService(IDefaultEditService defaultEditService) {
        this.defaultEditService = Objects.requireNonNull(defaultEditService);
        this.deleteService = new DeleteService();
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof Element;
    }

    @Override
    public boolean canHandle(IEditingContext editingContext) {
        return false;
    }

    @Override
    public List<ChildCreationDescription> getRootCreationDescriptions(IEditingContext editingContext, String domainId, boolean suggested, String referenceKind) {
        return this.defaultEditService.getRootCreationDescriptions(editingContext, domainId, suggested, referenceKind);
    }

    @Override
    public List<ChildCreationDescription> getChildCreationDescriptions(IEditingContext editingContext, String kind, String referenceKind) {
        return this.defaultEditService.getChildCreationDescriptions(editingContext, kind, referenceKind);
    }

    @Override
    public Optional<Object> createChild(IEditingContext editingContext, Object object, String childCreationDescriptionId) {
        return this.defaultEditService.createChild(editingContext, object, childCreationDescriptionId);
    }

    @Override
    public Optional<Object> createRootObject(IEditingContext editingContext, UUID documentId, String domainId, String rootObjectCreationDescriptionId) {
        return this.defaultEditService.createRootObject(editingContext, documentId, domainId, rootObjectCreationDescriptionId);
    }

    @Override
    public void delete(Object object) {
        Optional<Element> optionalElement = Optional.of(object)
                .filter(Element.class::isInstance)
                .map(Element.class::cast);

        optionalElement.ifPresent(element -> this.deleteService.deleteFromModel(element));
    }

    @Override
    public void editLabel(Object object, String labelField, String newValue) {
        this.defaultEditService.editLabel(object, labelField, newValue);
    }

}
