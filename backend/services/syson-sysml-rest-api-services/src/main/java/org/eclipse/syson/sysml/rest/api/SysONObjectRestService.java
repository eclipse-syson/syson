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
package org.eclipse.syson.sysml.rest.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.object.dto.Direction;
import org.eclipse.sirius.web.application.object.services.api.IDefaultObjectRestService;
import org.eclipse.sirius.web.application.object.services.api.IObjectRestServiceDelegate;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * SysON implementation of the Sirius Web delegate service used by objects-related REST APIs.
 *
 * @author arichard
 */
@Service
public class SysONObjectRestService implements IObjectRestServiceDelegate {

    private final IDefaultObjectRestService defaultObjectRestService;

    private final UtilService utilService;

    public SysONObjectRestService(IDefaultObjectRestService defaultObjectRestService) {
        this.defaultObjectRestService = Objects.requireNonNull(defaultObjectRestService);
        this.utilService = new UtilService();
    }

    @Override
    public boolean canHandle(IEditingContext editingContext) {
        return true;
    }

    @Override
    public List<Object> getElements(IEditingContext editingContext) {
        var allElements = new ArrayList<>();
        var rootElements = this.getRootElements(editingContext);
        for (Object rootObject : rootElements) {
            if (rootObject instanceof Element rootElement) {
                allElements.add(rootElement);
                rootElement.eAllContents().forEachRemaining(obj -> {
                    if (!(obj instanceof Relationship rel && rel.isIsImplied())) {
                        allElements.add(obj);
                    }
                });
            }
        }
        return allElements;
    }

    @Override
    public Optional<Object> getElementById(IEditingContext editingContext, String elementId) {
        return this.defaultObjectRestService.getElementById(editingContext, elementId);
    }

    @Override
    public List<Object> getRelationshipsByRelatedElement(IEditingContext editingContext, String elementId, Direction direction) {
        var relationshipsByRelatedElement = new HashSet<>();
        var elementById = this.defaultObjectRestService.getElementById(editingContext, elementId);
        if (elementById.isPresent()) {
            var object = elementById.get();
            if (object instanceof Element elt) {
                if (direction == Direction.OUT || direction == Direction.BOTH) {
                    relationshipsByRelatedElement.addAll(elt.getOwnedRelationship());
                }
                if (direction == Direction.IN || direction == Direction.BOTH) {
                    relationshipsByRelatedElement.addAll(this.getIngoingRelationships(elt));
                }
            }
        }
        relationshipsByRelatedElement.removeIf(obj -> obj instanceof Relationship rel && rel.isIsImplied());
        return new ArrayList<>(relationshipsByRelatedElement);
    }

    @Override
    public List<Object> getRootElements(IEditingContext editingContext) {
        var rootElements = new ArrayList<>();
        if (editingContext instanceof IEMFEditingContext emfEditingContext) {
            var resourceSet = emfEditingContext.getDomain().getResourceSet();
            var resources = resourceSet.getResources().stream()
                    .filter(res -> !(ElementUtil.isStandardLibraryResource(res)))
                    .toList();
            for (Resource resource : resources) {
                var contents = resource.getContents();
                for (Object rootContent : contents) {
                    if (rootContent instanceof Element elt && this.utilService.isRootNamespace(elt)) {
                        rootElements.addAll(elt.getOwnedElement());
                    }
                }
            }
        }
        return rootElements;
    }

    private Set<Object> getIngoingRelationships(Element elt) {
        Set<Object> ingoingRelationships = new HashSet<>();
        var referenceAdapter = ECrossReferenceAdapter.getCrossReferenceAdapter(elt);
        if (referenceAdapter != null) {
            Collection<Setting> inverseReferences = referenceAdapter.getInverseReferences(elt);
            for (Setting setting : inverseReferences) {
                EObject relatedElement = setting.getEObject();
                if (relatedElement instanceof Relationship) {
                    ingoingRelationships.add(relatedElement);
                }
            }
        }
        return ingoingRelationships;
    }
}
