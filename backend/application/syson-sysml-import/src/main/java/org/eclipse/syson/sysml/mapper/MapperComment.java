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
package org.eclipse.syson.sysml.mapper;

import org.eclipse.syson.sysml.Comment;

/**
 * Mapping logic specific to Comments in SysML models from AST nodes.
 *
 * @author arichard.
 */
public class MapperComment extends MapperVisitorInterface {

    public MapperComment(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof Comment;
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        Comment eObject = (Comment) mapping.getSelf();
        String body = mapping.getMainNode().get("body").asText();
        if (body != null) {
            eObject.setBody(this.deleteCommentsChars(body));
        }
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
    }

    private String deleteCommentsChars(String body) {
        String bodyWithoutCommentsChars = body;
        if (bodyWithoutCommentsChars.startsWith("/*")) {
            bodyWithoutCommentsChars = bodyWithoutCommentsChars.replaceFirst("/\\*", "");
        }
        if (bodyWithoutCommentsChars.endsWith("*/")) {
            bodyWithoutCommentsChars = bodyWithoutCommentsChars.substring(0, bodyWithoutCommentsChars.length() - 2);
        }
        return bodyWithoutCommentsChars.trim();
    }
}
