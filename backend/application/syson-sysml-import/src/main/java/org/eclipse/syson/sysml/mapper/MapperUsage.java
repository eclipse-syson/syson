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

import org.eclipse.syson.sysml.Usage;

/**
 * Implements mapping logic specific to Usage in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperUsage extends MapperVisitorInterface {

    public MapperUsage(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof Usage;
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        Usage eObject = (Usage) mapping.getSelf();

        eObject.setIsComposite(!mapping.getMainNode().get("isReference").asBoolean());

        eObject.setIsEnd(mapping.getMainNode().has("isEnd"));

    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
    }
}
