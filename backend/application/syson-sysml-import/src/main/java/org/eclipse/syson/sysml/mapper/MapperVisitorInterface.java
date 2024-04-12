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

import org.eclipse.syson.sysml.finder.ObjectFinder;

/**
 * Defines the interface for AST mappers with abstract methods for visiting and mapping elements.
 *
 * @author gescande
 */
public abstract class MapperVisitorInterface {

    protected final ObjectFinder objectFinder;

    protected final MappingState mappingState;

    public MapperVisitorInterface(final ObjectFinder objectFinder, final MappingState mappingState) {
        this.objectFinder = objectFinder;
        this.mappingState = mappingState;
    }

    public abstract boolean canVisit(MappingElement mapping);

    public abstract void mappingVisit(MappingElement mapping);

    public abstract void referenceVisit(MappingElement mapping);

}
