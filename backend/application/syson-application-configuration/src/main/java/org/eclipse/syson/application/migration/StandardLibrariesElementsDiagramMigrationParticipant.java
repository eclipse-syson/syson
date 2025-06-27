/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.application.migration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.representations.migration.IRepresentationMigrationParticipant;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * Diagram migration participant used to migrate SysON diagrams with diagram elements having "targetObjectId" pointing
 * to standard libraries Elements previous to 2025.8.0.
 *
 * Let's say we have a Start node pointing to the Actions::Action::start Element from the standard library. This node
 * had for "targetObjectId" the "id" of the object instead of its "elementId". But the "id" property changes each time
 * the standard libraries are updated. So the "targetObjectId" reference was pointing to nothing after the update. With
 * this migration participant, the "targetObjectId" now all point to the "elementId" property which is stable
 * before/after standard libraries updates.
 *
 * This migration participant has been added at the same time than the update of SysONIdentityService which allows to
 * get the "elementId" instead of the "id" when it is a library Element.
 *
 * @author arichard
 */
@Service
public class StandardLibrariesElementsDiagramMigrationParticipant implements IRepresentationMigrationParticipant {

    private static final String PARTICIPANT_VERSION = "2025.8.0-202506301700";

    private final IObjectSearchService objectSearchService;

    public StandardLibrariesElementsDiagramMigrationParticipant(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
    }

    @Override
    public String getVersion() {
        return PARTICIPANT_VERSION;
    }

    @Override
    public String getKind() {
        return "siriusComponents://representation?type=Diagram";
    }

    @Override
    public void replaceJsonNode(IEditingContext editingContext, ObjectNode root, String currentAttribute, JsonNode currentValue) {
        if (currentAttribute.equals("targetObjectId") && currentValue instanceof TextNode textNode) {
            var optElement = this.objectSearchService.getObject(editingContext, textNode.asText())
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast);
            if (optElement.isPresent()) {
                var element = optElement.get();
                if (ElementUtil.isStandardLibraryResource(element.eResource())) {
                    root.put("targetObjectId", element.getElementId());
                }
            }
        }
    }
}
