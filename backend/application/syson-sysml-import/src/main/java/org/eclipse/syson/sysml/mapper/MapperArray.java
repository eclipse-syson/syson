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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides array-specific mapping functionalities.
 *
 * @author gescande
 */
public class MapperArray extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperArray.class);

    public MapperArray(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getMainNode().isArray();
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        mapping.getMainNode().forEach(t -> {
            this.logger.debug("add array element of parent = " + mapping.getParent());
            this.mappingState.toMap().add(new MappingElement(t, mapping.getParent()));
        });
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        this.mappingState.done().add(mapping);
    }
}
