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

import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to LiteralInteger in SysML models from AST node.
 *
 * @author wldblm
 */
public class MapperLiteralInteger extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperLiteralInteger.class);

    public MapperLiteralInteger(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getLiteralInteger().isSuperTypeOf(mapping.getSelf().eClass());
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        LiteralInteger eObject = (LiteralInteger) mapping.getSelf();

        eObject.setValue(mapping.getMainNode().get(AstConstant.LITERAL).asInt());
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {

    }
}
