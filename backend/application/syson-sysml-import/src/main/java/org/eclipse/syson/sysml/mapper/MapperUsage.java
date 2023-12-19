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

import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to Usage in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperUsage extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperUsage.class);

    public MapperUsage(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(mapping.getSelf().eClass());
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
