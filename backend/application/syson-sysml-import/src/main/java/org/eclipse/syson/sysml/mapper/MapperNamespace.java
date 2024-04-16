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

import org.eclipse.syson.sysml.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to Namespace in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperNamespace extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperNamespace.class);

    public MapperNamespace(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof Namespace;
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        Namespace eObject = (Namespace) mapping.getSelf();

        if (eObject.getDeclaredName() == null || eObject.getDeclaredName().isEmpty()) {
            this.logger.debug("Set Namespace declareName to ROOT for " + eObject);
            eObject.setDeclaredName("ROOT");
        }
        this.objectFinder.addImportNamespace(eObject.getDeclaredName());
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
    }
}
